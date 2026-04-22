package br.com.fiap.shared.application.port.output;

import br.com.fiap.shared.domain.entity.Feedback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeedbackRepositoryPort {

    void save(Feedback feedback);

    Optional<Feedback> findById(UUID id);

    List<Feedback> findAll();
}
