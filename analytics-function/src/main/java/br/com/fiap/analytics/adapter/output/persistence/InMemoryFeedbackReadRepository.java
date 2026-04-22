package br.com.fiap.analytics.adapter.output.persistence;

import br.com.fiap.analytics.application.port.output.FeedbackReadRepositoryPort;
import br.com.fiap.shared.domain.entity.Feedback;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class InMemoryFeedbackReadRepository implements FeedbackReadRepositoryPort {

    @Override
    public List<Feedback> buscarTodos() {
        return List.of();
    }
}
