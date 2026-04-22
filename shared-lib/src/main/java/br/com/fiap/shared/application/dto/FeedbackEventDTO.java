package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.event.FeedbackCriadoEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackEventDTO(
        @JsonProperty("feedbackId") UUID feedbackId,
        @JsonProperty("descricao") String descricao,
        @JsonProperty("nota") int nota,
        @JsonProperty("urgencia") String urgencia,
        @JsonProperty("criadoEm") LocalDateTime criadoEm) {

    @JsonCreator
    public FeedbackEventDTO {
    }

    public static FeedbackEventDTO fromEvent(FeedbackCriadoEvent event) {
        return new FeedbackEventDTO(
                event.feedbackId(),
                event.descricao(),
                event.nota(),
                event.urgencia(),
                event.criadoEm());
    }
}
