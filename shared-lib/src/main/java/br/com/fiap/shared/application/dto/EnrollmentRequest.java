package br.com.fiap.shared.application.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
        @NotNull UUID studentId,
        @NotNull UUID courseId) {
}
