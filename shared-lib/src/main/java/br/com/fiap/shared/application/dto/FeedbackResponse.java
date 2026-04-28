package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.valueObject.ProcessStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackResponse(UUID id,
                               String description,
                               int score,
                               String urgency,
                               LocalDateTime createdAt,
                               ProcessStatus processStatus) {

    public static FeedbackResponse fromDomain(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getDescription().valor(),
                feedback.getScore().valor(),
                feedback.getUrgency().name(),
                feedback.getSubmittedAt(),
                feedback.getStatus()
        );
    }
}