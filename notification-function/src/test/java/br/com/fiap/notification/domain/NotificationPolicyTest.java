package br.com.fiap.notification.domain;

import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotificationPolicyTest {

    private NotificationPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new NotificationPolicy();
    }

    @Test
    void shouldNotify_whenUrgencyIsCritical() {
        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                UUID.randomUUID(), "Péssimo atendimento", 1, "CRITICAL", LocalDateTime.now());

        assertTrue(policy.shouldNotify(event));
    }

    @Test
    void shouldNotNotify_whenUrgencyIsHigh() {
        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                UUID.randomUUID(), "Atendimento ruim", 3, "HIGH", LocalDateTime.now());

        assertFalse(policy.shouldNotify(event));
    }

    @Test
    void shouldNotNotify_whenUrgencyIsMedium() {
        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                UUID.randomUUID(), "Atendimento médio", 6, "MEDIUM", LocalDateTime.now());

        assertFalse(policy.shouldNotify(event));
    }

    @Test
    void shouldNotNotify_whenUrgencyIsLow() {
        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                UUID.randomUUID(), "Ótimo atendimento", 9, "LOW", LocalDateTime.now());

        assertFalse(policy.shouldNotify(event));
    }
}
