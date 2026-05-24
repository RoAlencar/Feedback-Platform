package br.com.fiap.analytics.application.service;

import br.com.fiap.analytics.adapter.input.web.dto.FeedbackRequestDTO;
import br.com.fiap.analytics.application.client.NotificationClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NotificationDispatcherService {

    private static final Logger log =
            Logger.getLogger(NotificationDispatcherService.class);

    @Inject
    @RestClient
    NotificationClient notificationClient;

    public void notifyCriticalFeedback(
            FeedbackRequestDTO dto
    ) {

        log.info(
                "Sending critical notification: "
                        + dto.feedbackId()
        );

        notificationClient.sendNotification(dto);

        log.info("Notification sent successfully");
    }
}