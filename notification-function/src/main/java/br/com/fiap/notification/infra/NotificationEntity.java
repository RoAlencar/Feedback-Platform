package br.com.fiap.notification.infra;

import br.com.fiap.notification.domain.enums.NotificationChannel;
import br.com.fiap.notification.domain.enums.NotificationSendStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feedback_id", nullable = false)
    private Long feedbackId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel = NotificationChannel.EMAIL;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "send_status", nullable = false)
    private NotificationSendStatus sendStatus = NotificationSendStatus.PENDING;

    // ===== Construtor padrão (JPA)
    public NotificationEntity() {
    }

    // ===== Construtor útil
    public NotificationEntity(Long feedbackId, String message, NotificationChannel channel) {
        this.feedbackId = feedbackId;
        this.message = message;
        this.channel = channel;
        this.createdAt = LocalDateTime.now();
    }

    // ===== Getters e Setters

    public Long getId() {
        return id;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public NotificationSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(NotificationSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }
}