package br.com.fiap.feedback.application.service;

import br.com.fiap.shared.application.dto.FeedbackRequest;
import br.com.fiap.shared.application.dto.FeedbackResponse;
import br.com.fiap.shared.application.port.input.CriarFeedbackUseCase;
import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.application.port.output.FeedbackRepositoryPort;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.event.FeedbackCriadoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CriarFeedbackService implements CriarFeedbackUseCase {

    @Inject
    FeedbackRepositoryPort repositorio;

    @Inject
    EventPublisherPort publicador;

    @Override
    public FeedbackResponse executar(FeedbackRequest request) {
        Feedback feedback = Feedback.criar(request.descricao(), request.nota());
        repositorio.salvar(feedback);

        publicador.publicar(new FeedbackCriadoEvent(
                feedback.getId(),
                feedback.getDescricao().valor(),
                feedback.getNota().valor(),
                feedback.getUrgencia().name(),
                feedback.getCriadoEm()));

        return FeedbackResponse.fromDomain(feedback);
    }
}
