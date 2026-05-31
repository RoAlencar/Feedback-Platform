package br.com.fiap.notification.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.jboss.logging.Logger;

import br.com.fiap.notification.adapter.output.notification.NotificationSenderFactory;
import br.com.fiap.notification.adapter.output.persistence.entity.AdminJpaEntity;
import br.com.fiap.notification.adapter.output.persistence.repository.AdminRepository;
import br.com.fiap.notification.adapter.output.persistence.repository.NotificationRepository;
import br.com.fiap.notification.domain.NotificationPolicy;
import br.com.fiap.notification.domain.NotificationRequest;
import br.com.fiap.notification.domain.enums.NotificationChannel;
import br.com.fiap.notification.domain.enums.NotificationSendStatus;
import br.com.fiap.notification.infra.NotificationEntity;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProcessFeedbackNotificationUseCase {

    private static final Logger LOG = Logger.getLogger(ProcessFeedbackNotificationUseCase.class);

    private final NotificationPolicy policy;
    private final AdminRepository adminRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSenderFactory senderFactory;

    public ProcessFeedbackNotificationUseCase(
            NotificationPolicy policy,
            AdminRepository adminRepository,
            NotificationRepository notificationRepository,
            NotificationSenderFactory senderFactory) {
        this.policy = policy;
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
        this.senderFactory = senderFactory;
    }

    public void execute(FeedbackCreatedEvent event) {
        if (!policy.shouldNotify(event)) {
            LOG.debugf("Feedback %s ignorado (urgência: %s)", event.feedbackId(), event.urgency());
            return;
        }

        List<AdminJpaEntity> admins = fetchAdmins();
        if (admins.isEmpty()) {
            LOG.warnf("Nenhum administrador ativo encontrado para notificar. feedbackId: %s", event.feedbackId());
            return;
        }

        NotificationChannel channel = senderFactory.resolveChannel();

        for (AdminJpaEntity admin : admins) {
            NotificationRequest request = new NotificationRequest(
                    event.feedbackId(),
                    event.description(),
                    event.urgency(),
                    event.createdAt(),
                    List.of(admin.getEmail()));

            NotificationEntity notification = new NotificationEntity(
                    event.feedbackId(),
                    admin.getId(),
                    buildMessage(event),
                    channel);

            NotificationSendStatus status;
            try {
                senderFactory.getSender().send(request);
                status = NotificationSendStatus.SENT;
                notification.setSentAt(LocalDateTime.now());
                LOG.infof("Notificação enviada para admin %s - feedbackId: %s", admin.getEmail(), event.feedbackId());
            } catch (Exception e) {
                status = NotificationSendStatus.FAILED;
                notification.setAttempts(notification.getAttempts() + 1);
                notification.setUpdatedAt(LocalDateTime.now());
                LOG.errorf(e, "Falha ao notificar admin %s - feedbackId: %s", admin.getEmail(), event.feedbackId());
            }

            notification.setSendStatus(status);
            persist(notification);
        }
    }

    @Transactional
    List<AdminJpaEntity> fetchAdmins() {
        return adminRepository.findAllActive();
    }

    @Transactional
    void persist(NotificationEntity notification) {
        notificationRepository.persist(notification);
    }

    private String buildMessage(FeedbackCreatedEvent event) {
        return String.format("Feedback crítico recebido. Descrição: %s | Urgência: %s | Data: %s",
                event.description(), event.urgency(), event.createdAt());
    }
}
