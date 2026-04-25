
-- Tabela notifications (sem FK)

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,

    feedback_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,

    channel VARCHAR(20) NOT NULL,
    send_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    attempts INTEGER NOT NULL DEFAULT 0,

    message TEXT NOT NULL,

    sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- validações de domínio
    CONSTRAINT chk_channel
        CHECK (channel IN ('EMAIL', 'SMS', 'PUSH')),

    CONSTRAINT chk_status
        CHECK (send_status IN ('PENDING', 'SENT', 'FAILED'))
);