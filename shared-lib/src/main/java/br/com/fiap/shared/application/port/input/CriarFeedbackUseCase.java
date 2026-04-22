package br.com.fiap.shared.application.port.input;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;

public interface CriarFeedbackUseCase {

    FeedbackResponse executar(FeedbackRequest request);
}
