package br.com.fiap.feedback.adapter.input.web.dto;

import java.util.UUID;

public record CreateFeedbackRequest(
        UUID studentId,
        UUID courseId,
        String description,
        Integer score
) {}
