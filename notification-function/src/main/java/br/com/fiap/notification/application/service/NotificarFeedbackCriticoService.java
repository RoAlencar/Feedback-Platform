package br.com.fiap.notification.application.service;

import br.com.fiap.notification.application.port.output.NotifierPort;
import br.com.fiap.notification.domain.policy.NotificationPolicy;
import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class NotificarFeedbackCriticoService {

    @Inject
    NotifierPort notifier;

    public void processar(FeedbackEventDTO event) {
        if (NotificationPolicy.deveNotificar(event)) {
            notifier.notificar(event);
        }
    }
}
