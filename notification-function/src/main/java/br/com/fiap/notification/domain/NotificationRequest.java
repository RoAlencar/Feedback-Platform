package br.com.fiap.notification.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record NotificationRequest(
        UUID feedbackId,
        String descricao,
        String urgencia,
        LocalDateTime dataEnvio,
        List<String> recipientEmails
) {}
