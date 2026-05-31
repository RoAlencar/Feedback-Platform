package br.com.fiap.notification.adapter.output.notification;

import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import br.com.fiap.notification.application.port.output.NotificationSenderPort;
import br.com.fiap.notification.domain.NotificationRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("gcp")
@ApplicationScoped
public class GcpPubSubNotificationSenderAdapter implements NotificationSenderPort {

    private static final Logger LOG = Logger.getLogger(GcpPubSubNotificationSenderAdapter.class);

    @ConfigProperty(name = "gcp.project-id", defaultValue = "not-configured")
    String projectId;

    @ConfigProperty(name = "gcp.pubsub.topic-id", defaultValue = "feedback-critical-alerts")
    String topicId;

    @Override
    public void send(NotificationRequest request) {
        Publisher publisher = null;
        try {
            TopicName topicName = TopicName.of(projectId, topicId);
            publisher = Publisher.newBuilder(topicName).build();

            String payload = new ObjectMapper().writeValueAsString(Map.of(
                    "feedbackId", request.feedbackId().toString(),
                    "descricao", request.descricao(),
                    "urgencia", request.urgencia(),
                    "dataEnvio", request.dataEnvio().toString(),
                    "recipients", request.recipientEmails()));

            PubsubMessage message = PubsubMessage.newBuilder()
                    .setData(ByteString.copyFromUtf8(payload))
                    .putAttributes("urgency", request.urgencia())
                    .build();

            ApiFuture<String> future = publisher.publish(message);
            String messageId = future.get();
            LOG.infof("Mensagem publicada no Pub/Sub - messageId: %s, feedbackId: %s", messageId, request.feedbackId());

        } catch (Exception e) {
            LOG.errorf(e, "Falha ao publicar no Pub/Sub - feedbackId: %s", request.feedbackId());
            throw new RuntimeException("Falha ao publicar notificação no GCP Pub/Sub", e);
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
