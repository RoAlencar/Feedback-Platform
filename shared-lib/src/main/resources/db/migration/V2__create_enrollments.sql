CREATE TYPE enrollment_status AS ENUM ('ACTIVE', 'INACTIVE');

CREATE TABLE enrollments (
  id UUID NOT NULL DEFAULT gen_random_uuid(),
  status enrollment_status NOT NULL,
  CONSTRAINT pk_enrollments PRIMARY KEY (id)
);

INSERT INTO
  enrollments (status)
VALUES
  ('ACTIVE');

INSERT INTO
  enrollments (status)
VALUES
  ('INACTIVE');
