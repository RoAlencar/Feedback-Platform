CREATE TABLE students (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_students PRIMARY KEY (id),
    CONSTRAINT uk_students_email UNIQUE (email)
);