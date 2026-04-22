package br.com.fiap.shared.application.port.input;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;

public interface CreateFeedbackUseCase {

    FeedbackResponse execute(FeedbackRequest feedbackRequest);
}
