package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackEventDTO(
        @JsonProperty("feedbackId") UUID feedbackId,
        @JsonProperty("description") String description,
        @JsonProperty("score") int grade,
        @JsonProperty("urgency") String urgency,
        @JsonProperty("createdAt") LocalDateTime createdAt) {

    @JsonCreator
    public FeedbackEventDTO {
    }

    public static FeedbackEventDTO fromEvent(FeedbackCreatedEvent event) {
        return new FeedbackEventDTO(
                event.feedbackId(),
                event.description(),
                event.grade(),
                event.urgency(),
                event.createdAt());
    }
}