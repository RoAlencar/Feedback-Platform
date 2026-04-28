ALTER TABLE feedbacks
    ADD CONSTRAINT chk_urgency_level
        CHECK (urgency_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'));