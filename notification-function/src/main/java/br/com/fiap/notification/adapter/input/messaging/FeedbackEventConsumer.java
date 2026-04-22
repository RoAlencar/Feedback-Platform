package br.com.fiap.notification.adapter.input.messaging;

import br.com.fiap.notification.application.service.NotificarFeedbackCriticoService;
import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FeedbackEventConsumer {

    @Inject
    NotificarFeedbackCriticoService service;

    public void consumir(FeedbackEventDTO event) {
        service.processar(event);
    }
}
