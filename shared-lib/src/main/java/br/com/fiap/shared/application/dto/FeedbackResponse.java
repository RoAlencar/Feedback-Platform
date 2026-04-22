package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.entity.Feedback;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackResponse(UUID id,
                               String description,
                               int grade,
                               String urgency,
                               LocalDateTime createdAt) {

    public static FeedbackResponse fromDomain(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getDescription().valor(),
                feedback.getGrade().valor(),
                feedback.getUrgency().name(),
                feedback.getCreatedAt()
        );
    }
}