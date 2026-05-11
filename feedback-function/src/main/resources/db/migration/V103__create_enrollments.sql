CREATE TYPE enrollment_status AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE enrollments (
    id UUID NOT NULL,
    student_id UUID NOT NULL,
    course_id UUID NOT NULL,
    enrollment_date TIMESTAMP NOT NULL,
    status enrollment_status NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT pk_enrollments PRIMARY KEY (id),
    CONSTRAINT fk_enrollments_students
        FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enrollments_courses
        FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT uk_enrollments_student_course UNIQUE (student_id, course_id)
);