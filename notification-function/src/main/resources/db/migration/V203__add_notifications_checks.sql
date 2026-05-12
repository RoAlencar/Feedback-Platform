ALTER TABLE notifications
    ADD CONSTRAINT chk_notifications_channel
        CHECK (channel IN ('EMAIL'));

ALTER TABLE notifications
    ADD CONSTRAINT chk_notifications_status
        CHECK (send_status IN ('PENDING', 'SENT', 'FAILED'));

ALTER TABLE notifications
    ADD CONSTRAINT chk_notifications_attempts
        CHECK (attempts >= 0);