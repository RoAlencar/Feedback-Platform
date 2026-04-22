CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    feedback_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    channel VARCHAR(50) NOT NULL,
    send_status VARCHAR(20) NOT NULL,
    attempts INT DEFAULT 0,
    message TEXT NOT NULL,
    sent_at TIMESTAMP
);