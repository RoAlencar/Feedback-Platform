package br.com.fiap.notification.application.port.output;

import br.com.fiap.notification.domain.NotificationRequest;

public interface NotificationSenderPort {
    void send(NotificationRequest request);
}
