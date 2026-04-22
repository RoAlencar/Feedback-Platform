package br.com.fiap.shared.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackCriadoEvent(
        UUID feedbackId,
        String descricao,
        int nota,
        String urgencia,
        LocalDateTime criadoEm) {
}
