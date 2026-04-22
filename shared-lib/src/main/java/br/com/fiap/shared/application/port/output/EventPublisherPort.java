package br.com.fiap.shared.application.port.output;

import br.com.fiap.shared.domain.event.FeedbackCriadoEvent;

public interface EventPublisherPort {

    void publicar(FeedbackCriadoEvent event);
}
