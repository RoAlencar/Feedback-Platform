ALTER TABLE feedbacks
    ADD CONSTRAINT chk_feedbacks_score
        CHECK (score BETWEEN 0 AND 10);

ALTER TABLE feedbacks
    ADD CONSTRAINT chk_feedbacks_urgency_level
        CHECK (urgency_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'));

ALTER TABLE feedbacks
    ADD CONSTRAINT chk_feedbacks_process_status
        CHECK (process_status IN ('PENDING', 'PROCESSED'));