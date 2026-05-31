package br.com.fiap.notification.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;

import br.com.fiap.notification.adapter.output.notification.NotificationSenderFactory;
import br.com.fiap.notification.adapter.output.persistence.entity.AdminJpaEntity;
import br.com.fiap.notification.adapter.output.persistence.repository.AdminRepository;
import br.com.fiap.notification.adapter.output.persistence.repository.NotificationRepository;
import br.com.fiap.notification.domain.NotificationRequest;
import br.com.fiap.notification.domain.enums.NotificationSendStatus;
import br.com.fiap.notification.infra.NotificationEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RetryFailedNotificationsUseCase {

    private static final Logger LOG = Logger.getLogger(RetryFailedNotificationsUseCase.class);

    private final NotificationRepository notificationRepository;
    private final AdminRepository adminRepository;
    private final NotificationSenderFactory senderFactory;

    public RetryFailedNotificationsUseCase(
            NotificationRepository notificationRepository,
            AdminRepository adminRepository,
            NotificationSenderFactory senderFactory) {
        this.notificationRepository = notificationRepository;
        this.adminRepository = adminRepository;
        this.senderFactory = senderFactory;
    }

    @Transactional
    public void execute() {
        List<NotificationEntity> failed = notificationRepository.findFailedEligibleForRetry();

        if (failed.isEmpty()) {
            return;
        }

        LOG.infof("Retry: %d notificação(ões) elegível(eis) para reenvio", failed.size());

        for (NotificationEntity notification : failed) {
            Optional<AdminJpaEntity> adminOpt = adminRepository.findByIdOptional(notification.getReceiverId());

            if (adminOpt.isEmpty()) {
                LOG.warnf("Admin %s não encontrado para notificação %s — descartando",
                        notification.getReceiverId(), notification.getId());
                notification.setSendStatus(NotificationSendStatus.FAILED);
                notification.setAttempts(notification.getAttempts() + 1);
                notification.setUpdatedAt(LocalDateTime.now());
                continue;
            }

            AdminJpaEntity admin = adminOpt.get();

            try {
                senderFactory.getSender().send(buildRequestFromNotification(notification, admin));
                notification.setSendStatus(NotificationSendStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
                notification.setUpdatedAt(LocalDateTime.now());
                LOG.infof("Retry bem-sucedido: notificação %s → %s", notification.getId(), admin.getEmail());
            } catch (Exception e) {
                notification.setAttempts(notification.getAttempts() + 1);
                notification.setUpdatedAt(LocalDateTime.now());
                LOG.errorf(e, "Retry falhou (tentativa %d): notificação %s → %s",
                        notification.getAttempts(), notification.getId(), admin.getEmail());
            }
        }
    }

    private NotificationRequest buildRequestFromNotification(NotificationEntity notification, AdminJpaEntity admin) {
        return new NotificationRequest(
                notification.getFeedbackId(),
                notification.getMessage(),
                null,
                null,
                List.of(admin.getEmail()));
    }
}
