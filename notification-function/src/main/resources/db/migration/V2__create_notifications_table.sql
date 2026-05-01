-- ============================================
-- CREATE notifications TABLE (UUID VERSION)
-- ============================================

CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    feedback_id UUID NOT NULL,
    receiver_id UUID NOT NULL,

    channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL',
    send_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

    attempts INTEGER NOT NULL DEFAULT 0,
    message TEXT NOT NULL,

    sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- domain constraints
    CONSTRAINT chk_channel
        CHECK (channel IN ('EMAIL', 'SMS', 'PUSH')),

    CONSTRAINT chk_status
        CHECK (send_status IN ('PENDING', 'SENT', 'FAILED'))
);