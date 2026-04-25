CREATE TABLE students
(
    id        UUID         NOT NULL,
    name      VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    is_active BOOLEAN      NOT NULL,
    CONSTRAINT pk_students PRIMARY KEY (id)
);