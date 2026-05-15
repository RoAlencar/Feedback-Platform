package br.com.fiap.feedback.application.usecase;

import java.util.UUID;

public record CreateFeedbackCommand(
        UUID studentId,
        UUID courseId,
        String description,
        Integer score
) {}
