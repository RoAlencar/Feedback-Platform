package br.com.fiap.shared.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FeedbackRequest(
        @NotBlank String descricao,
        @Min(0) @Max(10) int nota) {
}
