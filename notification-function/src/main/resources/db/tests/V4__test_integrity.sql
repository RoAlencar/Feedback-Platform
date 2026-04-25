-- ================================
-- TESTE DE INTEGRIDADE REFERENCIAL
-- ================================

-- Limpa dados (ordem importa por causa das FKs)
DELETE FROM feedbacks;
DELETE FROM users;

-- Verificar se as FKs existem
SELECT
    conname AS constraint_name,
    conrelid::regclass AS table,
    confrelid::regclass AS referenced_table
FROM pg_constraint
WHERE conrelid::regclass = 'notifications'::regclass;

-- ================================
-- 1. Inserção VÁLIDA (deve funcionar)
-- ================================

INSERT INTO feedbacks (id, descricao, nota)
VALUES (1, 'Teste feedback', 5);

INSERT INTO admins (user_id, nome)
VALUES (1, 'Admin Teste');

-- ================================
-- 2. Inserir notificação válida (deve funcionar)
-- ================================
INSERT INTO notifications (
    feedback_id,
    receiver_id,
    channel,
    send_status,
    attempts,
    message,
    sent_at
) VALUES (
    1,              -- existe em feedbacks ✅
    1,              -- existe em admins ✅
    'EMAIL',
    'PENDING',
    0,
    'Teste de notificação',
    NOW()
);

-- ================================
-- 3. TESTES DE ERRO FK inválida (feedback_id)
-- ================================

INSERT INTO notifications (
    feedback_id,
    receiver_id,
    channel,
    send_status,
    attempts,
    message,
    sent_at
) VALUES (
    999,            -- NÃO EXISTE ❌
    1,
    'EMAIL',
    'PENDING',
    0,
    'Erro esperado',
    NOW()
);

-- ================================
-- 4. TESTES DE ERRO FK inválida (receiver_id)
-- ================================

INSERT INTO notifications (
    feedback_id,
    receiver_id,
    channel,
    send_status,
    attempts,
    message,
    sent_at
) VALUES (
    1,
    999,            -- NÃO EXISTE ❌
    'EMAIL',
    'PENDING',
    0,
    'Erro esperado',
    NOW()
);
-- ================================
-- 5. CONSULTAS PARA VALIDAÇÃO
-- ================================

SELECT * FROM feedbacks;
SELECT * FROM notifications;