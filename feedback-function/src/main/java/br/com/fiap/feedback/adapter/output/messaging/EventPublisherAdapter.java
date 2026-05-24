package br.com.fiap.feedback.adapter.output.messaging;

import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.logging.Logger;

@ApplicationScoped
public class EventPublisherAdapter implements EventPublisherPort {

    private static final Logger LOG = Logger.getLogger(EventPublisherAdapter.class.getName());

    @Override
    public void publish(FeedbackCreatedEvent event) {
        LOG.info("Publishing feedback event: " + event.feedbackId());
    }
}
