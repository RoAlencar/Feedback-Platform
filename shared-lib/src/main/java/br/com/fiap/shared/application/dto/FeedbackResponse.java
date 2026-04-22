package br.com.fiap.shared.application.dto;

import br.com.fiap.shared.domain.entity.Feedback;

import java.time.LocalDateTime;
import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        String descricao,
        int nota,
        String urgencia,
        LocalDateTime criadoEm) {

    public static FeedbackResponse fromDomain(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getDescricao().valor(),
                feedback.getNota().valor(),
                feedback.getUrgencia().name(),
                feedback.getCriadoEm());
    }
}
