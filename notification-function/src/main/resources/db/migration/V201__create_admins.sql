CREATE TABLE admins (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_admins PRIMARY KEY (id),
    CONSTRAINT uk_admins_email UNIQUE (email)
);