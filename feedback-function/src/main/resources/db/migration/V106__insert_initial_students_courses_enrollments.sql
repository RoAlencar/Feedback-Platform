-- ============================================================
-- V106__insert_initial_students_courses_enrollments.sql
-- Carga inicial do banco da feedback-function
-- ============================================================

INSERT INTO students (id, name, email, is_active)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Ana Souza', 'ana.souza@fiap.com.br', TRUE),
    ('22222222-2222-2222-2222-222222222222', 'Bruno Lima', 'bruno.lima@fiap.com.br', TRUE),
    ('33333333-3333-3333-3333-333333333333', 'Carla Mendes', 'carla.mendes@fiap.com.br', TRUE),
    ('44444444-4444-4444-4444-444444444444', 'Diego Oliveira', 'diego.oliveira@fiap.com.br', TRUE),
    ('55555555-5555-5555-5555-555555555555', 'Fernanda Alves', 'fernanda.alves@fiap.com.br', TRUE)
ON CONFLICT (email) DO NOTHING;


INSERT INTO courses (id, name)
VALUES
    ('aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa', 'Arquitetura de Software'),
    ('bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb', 'Banco de Dados'),
    ('cccccccc-3333-3333-3333-cccccccccccc', 'Cloud Computing'),
    ('dddddddd-4444-4444-4444-dddddddddddd', 'Engenharia de Software')
ON CONFLICT (id) DO NOTHING;


INSERT INTO enrollments (
    id,
    student_id,
    course_id,
    enrollment_date,
    status
)
VALUES
    (
        'e1111111-1111-1111-1111-111111111111',
        '11111111-1111-1111-1111-111111111111',
        'aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa',
        CURRENT_DATE,
        'ACTIVE'
    ),
    (
        'e2222222-2222-2222-2222-222222222222',
        '22222222-2222-2222-2222-222222222222',
        'bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb',
        CURRENT_DATE,
        'ACTIVE'
    ),
    (
        'e3333333-3333-3333-3333-333333333333',
        '33333333-3333-3333-3333-333333333333',
        'cccccccc-3333-3333-3333-cccccccccccc',
        CURRENT_DATE,
        'ACTIVE'
    ),
    (
        'e4444444-4444-4444-4444-444444444444',
        '44444444-4444-4444-4444-444444444444',
        'dddddddd-4444-4444-4444-dddddddddddd',
        CURRENT_DATE,
        'ACTIVE'
    ),
    (
        'e5555555-5555-5555-5555-555555555555',
        '55555555-5555-5555-5555-555555555555',
        'aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa',
        CURRENT_DATE,
        'ACTIVE'
    )
ON CONFLICT DO NOTHING;