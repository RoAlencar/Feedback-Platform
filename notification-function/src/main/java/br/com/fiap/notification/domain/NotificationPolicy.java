package br.com.fiap.notification.domain;

import br.com.fiap.shared.domain.event.FeedbackCreatedEvent;
import br.com.fiap.shared.domain.valueObject.UrgencyLevel;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationPolicy {

    public boolean shouldNotify(FeedbackCreatedEvent event) {
        return UrgencyLevel.CRITICAL.name().equalsIgnoreCase(event.urgency());
    }
}
