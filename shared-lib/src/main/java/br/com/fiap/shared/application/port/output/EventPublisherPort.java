package br.com.fiap.shared.application.port.output;

import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;

public interface EventPublisherPort {

    void publish(FeedbackCreatedEvent event);
}
