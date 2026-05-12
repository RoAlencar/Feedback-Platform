CREATE TABLE weekly_reports (
                                id UUID PRIMARY KEY,
                                period_start DATE NOT NULL,
                                period_end DATE NOT NULL,
                                average_score NUMERIC(4,2) NOT NULL,
                                total_feedbacks INTEGER NOT NULL,
                                generated_at TIMESTAMP NOT NULL
);