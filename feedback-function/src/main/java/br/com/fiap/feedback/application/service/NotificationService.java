package br.com.fiap.feedback.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NotificationService {

    private static final Logger LOG =
            Logger.getLogger(NotificationService.class);

    public void notifyCriticalFeedback() {

        LOG.warn("Sending critical feedback notification");
    }
}