package br.com.fiap.shared.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(
        @NotNull UUID studentId,
        @NotNull UUID courseId,
        @NotBlank String description,
        @Min(0) @Max(10) int grade){

}
