package br.com.fiap.shared.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackCreatedEvent(
        UUID feedbackId,
        String description,
        int grade,
        String urgency,
        LocalDateTime createdAt) {
}
