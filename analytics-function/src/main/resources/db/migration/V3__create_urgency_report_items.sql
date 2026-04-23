CREATE TABLE urgency_report_items (
                                      id UUID PRIMARY KEY,
                                      weekly_report_id UUID NOT NULL,
                                      urgency_level VARCHAR(20) NOT NULL,
                                      feedback_count INTEGER NOT NULL,
                                      CONSTRAINT fk_urgency_report_items_weekly_report
                                          FOREIGN KEY (weekly_report_id)
                                              REFERENCES weekly_reports (id)
                                              ON DELETE CASCADE
);