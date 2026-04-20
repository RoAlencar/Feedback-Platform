package br.com.fiap.shared.event;

import java.time.LocalDateTime;

public class FeedbackCreatedEvent {

    private String feedbackId;
    private String message;
    private boolean critical;
    private LocalDateTime createdAt;

    public FeedbackCreatedEvent(String feedbackId, String message, boolean critical, LocalDateTime createdAt) {
        this.feedbackId = feedbackId;
        this.message = message;
        this.critical = critical;
        this.createdAt = createdAt;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
