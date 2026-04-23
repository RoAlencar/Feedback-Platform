CREATE TABLE daily_report_items (
                                    id UUID PRIMARY KEY,
                                    weekly_report_id UUID NOT NULL,
                                    date DATE NOT NULL,
                                    feedback_count INTEGER NOT NULL,
                                    CONSTRAINT fk_daily_report_items_weekly_report
                                        FOREIGN KEY (weekly_report_id)
                                            REFERENCES weekly_reports (id)
                                            ON DELETE CASCADE
);