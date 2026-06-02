CREATE TABLE analytics_feedback_events (
                                           feedback_id UUID NOT NULL,
                                           description TEXT NOT NULL,
                                           score INTEGER NOT NULL,
                                           urgency_level VARCHAR(20) NOT NULL,
                                           submitted_at TIMESTAMP NOT NULL,
                                           processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                           CONSTRAINT pk_analytics_feedback_events PRIMARY KEY (feedback_id),

                                           CONSTRAINT chk_analytics_feedback_events_score
                                               CHECK (score BETWEEN 0 AND 10),

                                           CONSTRAINT chk_analytics_feedback_events_urgency
                                               CHECK (urgency_level IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL'))
);

ALTER TABLE weekly_reports
    ADD CONSTRAINT uk_weekly_reports_period
        UNIQUE (period_start, period_end);