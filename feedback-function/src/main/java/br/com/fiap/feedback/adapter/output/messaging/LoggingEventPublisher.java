package br.com.fiap.feedback.adapter.output.messaging;

import br.com.fiap.shared.application.port.output.EventPublisherPort;
import br.com.fiap.shared.domain.event.FeedbackCriadoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LoggingEventPublisher implements EventPublisherPort {

    private static final Logger LOG = Logger.getLogger(LoggingEventPublisher.class);

    @Override
    public void publicar(FeedbackCriadoEvent event) {
        LOG.infof("Publicando FeedbackCriadoEvent: id=%s urgencia=%s nota=%d",
                event.feedbackId(), event.urgencia(), event.nota());
    }
}
