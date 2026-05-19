package br.com.fiap.analytics.adapter.input.web.dto;

public record FeedbackRequestDTO(
        Long feedbackId,
        Long studentId,
        Long courseId,
        Integer rating,
        String comment
) {
}