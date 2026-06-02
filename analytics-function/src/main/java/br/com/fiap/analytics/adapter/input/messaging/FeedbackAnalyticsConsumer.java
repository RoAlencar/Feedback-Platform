package br.com.fiap.analytics.adapter.input.messaging;

import br.com.fiap.analytics.application.usecase.ProcessFeedbackCreatedEventUseCase;
import br.com.fiap.shared.application.dto.FeedbackEventDTO;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class FeedbackAnalyticsConsumer {

    private static final Logger LOG = Logger.getLogger(FeedbackAnalyticsConsumer.class);

    private final ProcessFeedbackCreatedEventUseCase useCase;

    public FeedbackAnalyticsConsumer(ProcessFeedbackCreatedEventUseCase useCase) {
        this.useCase = useCase;
    }

    @Incoming("feedback-created")
    @Blocking
    public void consume(FeedbackEventDTO dto) {
        LOG.infof(
                "Evento recebido no analytics: feedbackId=%s score=%s urgência=%s",
                dto.feedbackId(),
                dto.grade(),
                dto.urgency()
        );

        useCase.execute(dto);
    }
}