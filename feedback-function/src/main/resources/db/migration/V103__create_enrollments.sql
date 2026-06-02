-- ============================================================
-- V103__create_enrollments.sql
-- ============================================================
CREATE TABLE enrollments (
    id UUID NOT NULL,
    student_id UUID NOT NULL,
    course_id UUID NOT NULL,
    enrollment_date DATE NOT NULL,
    -- ALTERAÇÃO PRINCIPAL: ENUM removido
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT pk_enrollments PRIMARY KEY (id),
    CONSTRAINT fk_enrollments_students FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enrollments_courses FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT uk_enrollments_student_course UNIQUE (student_id, course_id)
);