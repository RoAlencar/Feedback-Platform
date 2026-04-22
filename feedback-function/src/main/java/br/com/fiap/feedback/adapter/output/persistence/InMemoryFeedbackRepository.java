package br.com.fiap.feedback.adapter.output.persistence;

import br.com.fiap.shared.application.port.output.FeedbackRepositoryPort;
import br.com.fiap.shared.domain.entity.Feedback;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class InMemoryFeedbackRepository implements FeedbackRepositoryPort {

    private final Map<UUID, Feedback> store = new ConcurrentHashMap<>();

    @Override
    public void salvar(Feedback feedback) {
        store.put(feedback.getId(), feedback);
    }

    @Override
    public Optional<Feedback> buscarPorId(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Feedback> buscarTodos() {
        return List.copyOf(store.values());
    }
}
