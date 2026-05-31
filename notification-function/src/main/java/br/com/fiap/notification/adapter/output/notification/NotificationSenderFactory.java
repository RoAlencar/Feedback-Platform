package br.com.fiap.notification.adapter.output.notification;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.fiap.notification.application.port.output.NotificationSenderPort;
import br.com.fiap.notification.domain.enums.NotificationChannel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
public class NotificationSenderFactory {

    @ConfigProperty(name = "notification.channel", defaultValue = "EMAIL")
    String channelConfig;

    @Inject
    @Named("email")
    NotificationSenderPort emailSender;

    @Inject
    @Named("gcp")
    NotificationSenderPort gcpSender;

    public NotificationSenderPort getSender() {
        if (NotificationChannel.GCP_PUBSUB.name().equalsIgnoreCase(channelConfig)) {
            return gcpSender;
        }
        return emailSender;
    }

    public NotificationChannel resolveChannel() {
        if (NotificationChannel.GCP_PUBSUB.name().equalsIgnoreCase(channelConfig)) {
            return NotificationChannel.GCP_PUBSUB;
        }
        return NotificationChannel.EMAIL;
    }
}
