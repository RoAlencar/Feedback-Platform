CREATE TABLE notifications (
    id UUID NOT NULL,
    feedback_id UUID NOT NULL,
    receiver_id UUID NOT NULL,
    channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL',
    send_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attempts INTEGER NOT NULL DEFAULT 0,
    message TEXT NOT NULL,
    sent_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notifications_admin
        FOREIGN KEY (receiver_id) REFERENCES admins(id)
        ON DELETE CASCADE
);