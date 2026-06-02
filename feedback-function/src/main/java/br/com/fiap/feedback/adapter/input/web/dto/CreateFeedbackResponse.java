package br.com.fiap.feedback.adapter.input.web.dto;

import br.com.fiap.shared.domain.entity.Feedback;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateFeedbackResponse(
        UUID id,
        UUID studentId,
        UUID courseId,
        String description,
        int score,
        LocalDateTime submittedAt,
        String message
) {

    public static CreateFeedbackResponse fromDomain(Feedback feedback){

        return new CreateFeedbackResponse(
                feedback.getId(),
                feedback.getStudentId(),
                feedback.getCourseId(),
                feedback.getDescription().valor(),
                feedback.getScore().valor(),
                feedback.getSubmittedAt(),
                "Registration successful"
        );
    }

}
