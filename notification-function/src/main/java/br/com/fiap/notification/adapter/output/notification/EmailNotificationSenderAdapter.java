package br.com.fiap.notification.adapter.output.notification;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.logging.Logger;

import br.com.fiap.notification.application.port.output.NotificationSenderPort;
import br.com.fiap.notification.domain.NotificationRequest;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("email")
@ApplicationScoped
public class EmailNotificationSenderAdapter implements NotificationSenderPort {

    private static final Logger LOG = Logger.getLogger(EmailNotificationSenderAdapter.class);

    // Dedicated virtual-thread executor to run blocking SMTP calls outside any
    // Vert.x worker context, preventing the worker-context serialization deadlock
    // caused by BlockingMailerImpl.await() waiting on a callback that is queued
    // back to the same (blocked) Vert.x worker context.
    private static final ExecutorService SMTP_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    @Inject
    Mailer mailer;

    @Override
    public void send(NotificationRequest request) {
        String subject = "[ALERTA CRÍTICO] Feedback urgente recebido";
        String body = buildEmailBody(request);

        for (String recipient : request.recipientEmails()) {
            try {
                SMTP_EXECUTOR.submit(() -> mailer.send(Mail.withText(recipient, subject, body))).get();
            } catch (ExecutionException e) {
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                LOG.errorf(cause, "Falha ao enviar email para %s - feedbackId: %s", recipient, request.feedbackId());
                throw new RuntimeException(cause);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOG.errorf(e, "Envio interrompido para %s - feedbackId: %s", recipient, request.feedbackId());
                throw new RuntimeException(e);
            }
        }
    }

    private String buildEmailBody(NotificationRequest request) {
        // When urgencia is null the descricao field already carries the full
        // pre-built message (used by the retry path to re-send stored content).
        if (request.urgencia() == null) {
            return request.descricao() + String.format("%n%nFeedback ID:   %s%n", request.feedbackId());
        }
        return String.format(
                "ALERTA: Feedback crítico recebido%n%n" +
                        "Descrição:     %s%n" +
                        "Urgência:      %s%n" +
                        "Data de envio: %s%n%n" +
                        "Feedback ID:   %s%n",
                request.descricao(),
                request.urgencia(),
                request.dataEnvio(),
                request.feedbackId());
    }
}
