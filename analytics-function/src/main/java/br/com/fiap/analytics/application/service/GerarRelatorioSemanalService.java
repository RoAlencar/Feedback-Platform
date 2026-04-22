package br.com.fiap.analytics.application.service;

import br.com.fiap.analytics.application.port.output.FeedbackReadRepositoryPort;
import br.com.fiap.analytics.domain.RelatorioSemanal;
import br.com.fiap.shared.domain.entity.Feedback;
import br.com.fiap.shared.domain.valueobject.Urgencia;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GerarRelatorioSemanalService {

    @Inject
    FeedbackReadRepositoryPort repositorio;

    public RelatorioSemanal executar() {
        List<Feedback> feedbacks = repositorio.buscarTodos();

        long total = feedbacks.size();
        long criticos = feedbacks.stream()
                .filter(f -> f.getUrgencia() == Urgencia.CRITICA)
                .count();
        double mediaNotas = feedbacks.stream()
                .mapToInt(f -> f.getNota().valor())
                .average()
                .orElse(0.0);

        return new RelatorioSemanal(total, criticos, mediaNotas);
    }
}
