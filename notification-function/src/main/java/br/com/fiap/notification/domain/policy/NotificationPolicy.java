package br.com.fiap.notification.domain.policy;

import br.com.fiap.shared.application.dto.FeedbackEventDTO;

public final class NotificationPolicy {

    private static final String URGENCIA_CRITICA = "CRITICA";

    private NotificationPolicy() {
    }

    public static boolean deveNotificar(FeedbackEventDTO event) {
        return URGENCIA_CRITICA.equalsIgnoreCase(event.urgencia());
    }
}
