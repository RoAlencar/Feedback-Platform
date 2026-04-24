CREATE TABLE courses (
  id UUID NOT NULL DEFAULT gen_random_uuid(),
  name VARCHAR(255) NOT NULL,
  CONSTRAINT pk_courses PRIMARY KEY (id)
);
