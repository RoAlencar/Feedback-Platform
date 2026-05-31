-- ============================================================
-- V204__insert_initial_admins.sql
-- Carga inicial do banco da notification-function
-- Contém administradores responsáveis por receber alertas
-- ============================================================

INSERT INTO admins (id, name, email, is_active)
VALUES
    ('99999999-1111-1111-1111-999999999999', 'Mariana Costa', 'mariana.costa@fiap.com.br', TRUE),
    ('88888888-2222-2222-2222-888888888888', 'Rafael Pereira', 'rafael.pereira@fiap.com.br', TRUE),
    ('77777777-3333-3333-3333-777777777777', 'Juliana Rocha', 'juliana.rocha@fiap.com.br', TRUE)
ON CONFLICT (email) DO NOTHING;