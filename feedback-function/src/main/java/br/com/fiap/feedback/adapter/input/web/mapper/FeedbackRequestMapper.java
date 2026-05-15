package br.com.fiap.feedback.adapter.input.web.mapper;

import br.com.fiap.feedback.adapter.input.web.dto.CreateFeedbackRequest;
import br.com.fiap.feedback.application.usecase.CreateFeedbackCommand;

public final class FeedbackRequestMapper {

    private FeedbackRequestMapper() {
    }

    public static CreateFeedbackCommand toCommand(CreateFeedbackRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Payload da avaliação é obrigatório");
        }

        return new CreateFeedbackCommand(
                request.studentId(),
                request.courseId(),
                request.description(),
                request.score()
        );
    }
}