CREATE TABLE feedbacks (
    id UUID NOT NULL,
    description TEXT NOT NULL,
    student_id UUID NOT NULL,
    course_id UUID NOT NULL,
    score INTEGER NOT NULL,
    urgency_level VARCHAR(20) NOT NULL,
    process_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    submitted_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_feedbacks PRIMARY KEY (id),
    CONSTRAINT fk_feedbacks_student
        FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_feedbacks_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
);