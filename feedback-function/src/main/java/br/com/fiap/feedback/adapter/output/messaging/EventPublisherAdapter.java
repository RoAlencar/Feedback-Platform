package br.com.fiap.feedback.adapter.output.messaging;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EventPublisherAdapter implements EventPublisherPort {

    private static final Logger LOG = Logger.getLogger(EventPublisherAdapter.class);

    @Inject
    @Channel("feedback-created")
    MutinyEmitter<FeedbackEventDTO> emitter;

    @Override
    public void publish(FeedbackCreatedEvent event) {
        FeedbackEventDTO dto = FeedbackEventDTO.fromEvent(event);
        emitter.sendAndAwait(dto);
        LOG.infof("Evento publicado para feedback %s (urgência: %s)", event.feedbackId(), event.urgency());
    }
}
