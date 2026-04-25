CREATE TABLE feedbacks
(
    id             UUID         NOT NULL,
    description    VARCHAR(255) NOT NULL,
    student_id     UUID         NOT NULL,
    course_id      UUID         NOT NULL,
    score          INTEGER      NOT NULL,
    urgency_level  VARCHAR(255) NOT NULL,
    process_status VARCHAR(255) NOT NULL,
    submitted_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_feedbacks PRIMARY KEY (id)
);

ALTER TABLE feedbacks
    ADD CONSTRAINT FK_FEEDBACKS_ON_COURSE FOREIGN KEY (course_id) REFERENCES courses (id);

ALTER TABLE feedbacks
    ADD CONSTRAINT FK_FEEDBACKS_ON_STUDENT FOREIGN KEY (student_id) REFERENCES students (id);