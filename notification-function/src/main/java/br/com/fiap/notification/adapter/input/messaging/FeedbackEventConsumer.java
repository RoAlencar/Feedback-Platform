package br.com.fiap.notification.adapter.input.messaging;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import br.com.fiap.notification.application.usecase.ProcessFeedbackNotificationUseCase;
import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeedbackEventConsumer {

    private static final Logger LOG = Logger.getLogger(FeedbackEventConsumer.class);

    private final ProcessFeedbackNotificationUseCase useCase;

    public FeedbackEventConsumer(ProcessFeedbackNotificationUseCase useCase) {
        this.useCase = useCase;
    }

    @Incoming("feedback-created")
    public void consume(FeedbackEventDTO dto) {
        LOG.infof("Evento recebido: feedbackId=%s urgência=%s", dto.feedbackId(), dto.urgency());
        FeedbackCreatedEvent event = new FeedbackCreatedEvent(
                dto.feedbackId(),
                dto.description(),
                dto.grade(),
                dto.urgency(),
                dto.createdAt());
        useCase.execute(event);
    }
}
