package br.com.fiap.analytics.application.port.output;

import br.com.fiap.shared.domain.entity.Feedback;

import java.util.List;

public interface FeedbackReadRepositoryPort {

    List<Feedback> buscarTodos();
}
