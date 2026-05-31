package br.com.fiap.notification.application.usecase;

import br.com.fiap.notification.adapter.output.notification.NotificationSenderFactory;
import br.com.fiap.notification.adapter.output.persistence.entity.AdminJpaEntity;
import br.com.fiap.notification.adapter.output.persistence.repository.AdminRepository;
import br.com.fiap.notification.adapter.output.persistence.repository.NotificationRepository;
import br.com.fiap.notification.application.port.output.NotificationSenderPort;
import br.com.fiap.notification.domain.NotificationPolicy;
import br.com.fiap.notification.domain.enums.NotificationChannel;
import br.com.fiap.notification.domain.enums.NotificationSendStatus;
import br.com.fiap.notification.infra.NotificationEntity;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessFeedbackNotificationUseCaseTest {

    private NotificationPolicy policy;
    private AdminRepository adminRepository;
    private NotificationRepository notificationRepository;
    private NotificationSenderFactory senderFactory;
    private NotificationSenderPort sender;

    private ProcessFeedbackNotificationUseCase useCase;

    @BeforeEach
    void setUp() {
        policy = mock(NotificationPolicy.class);
        adminRepository = mock(AdminRepository.class);
        notificationRepository = mock(NotificationRepository.class);
        senderFactory = mock(NotificationSenderFactory.class);
        sender = mock(NotificationSenderPort.class);

        when(senderFactory.getSender()).thenReturn(sender);
        when(senderFactory.resolveChannel()).thenReturn(NotificationChannel.EMAIL);

        useCase = new ProcessFeedbackNotificationUseCase(policy, adminRepository, notificationRepository,
                senderFactory);
    }

    @Test
    void shouldSkip_whenPolicyReturnsFalse() {
        FeedbackCreatedEvent event = criticalEvent();
        when(policy.shouldNotify(event)).thenReturn(false);

        useCase.execute(event);

        verifyNoInteractions(adminRepository, notificationRepository, senderFactory);
    }

    @Test
    void shouldSkip_whenNoActiveAdmins() {
        FeedbackCreatedEvent event = criticalEvent();
        when(policy.shouldNotify(event)).thenReturn(true);
        when(adminRepository.findAllActive()).thenReturn(List.of());

        useCase.execute(event);

        verifyNoInteractions(notificationRepository, senderFactory);
    }

    @Test
    void shouldPersistSent_whenSendSucceeds() {
        FeedbackCreatedEvent event = criticalEvent();
        AdminJpaEntity admin = adminEntity("admin@test.com");

        when(policy.shouldNotify(event)).thenReturn(true);
        when(adminRepository.findAllActive()).thenReturn(List.of(admin));
        doNothing().when(sender).send(any());

        useCase.execute(event);

        ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).persist(captor.capture());
        assertEquals(NotificationSendStatus.SENT, captor.getValue().getSendStatus());
    }

    @Test
    void shouldPersistFailed_whenSendThrows() {
        FeedbackCreatedEvent event = criticalEvent();
        AdminJpaEntity admin = adminEntity("admin@test.com");

        when(policy.shouldNotify(event)).thenReturn(true);
        when(adminRepository.findAllActive()).thenReturn(List.of(admin));
        doThrow(new RuntimeException("SMTP error")).when(sender).send(any());

        useCase.execute(event);

        ArgumentCaptor<NotificationEntity> captor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).persist(captor.capture());
        assertEquals(NotificationSendStatus.FAILED, captor.getValue().getSendStatus());
        assertEquals(1, captor.getValue().getAttempts());
    }

    @Test
    void shouldSendOnce_perAdmin_forMultipleAdmins() {
        FeedbackCreatedEvent event = criticalEvent();
        AdminJpaEntity admin1 = adminEntity("admin1@test.com");
        AdminJpaEntity admin2 = adminEntity("admin2@test.com");

        when(policy.shouldNotify(event)).thenReturn(true);
        when(adminRepository.findAllActive()).thenReturn(List.of(admin1, admin2));
        doNothing().when(sender).send(any());

        useCase.execute(event);

        verify(notificationRepository, times(2)).persist(any(NotificationEntity.class));
    }

    // --- helpers ---

    private FeedbackCreatedEvent criticalEvent() {
        return new FeedbackCreatedEvent(UUID.randomUUID(), "Péssimo", 1, "CRITICAL", LocalDateTime.now());
    }

    private AdminJpaEntity adminEntity(String email) {
        return new AdminJpaEntity(UUID.randomUUID(), "Admin", email, true);
    }
}
