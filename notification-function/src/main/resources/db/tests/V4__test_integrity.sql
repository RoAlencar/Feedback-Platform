-- ============================================
-- FULL INTEGRATION & DATA INTEGRITY TEST
-- WITH STEP-BY-STEP VALIDATION
-- ============================================

-- ============================================
-- 0. CLEAN DATABASE
-- ============================================
DELETE FROM notifications;
DELETE FROM feedbacks;
DELETE FROM admins;

-- Validate clean state
SELECT 'STEP 0 - CLEAN STATE' AS step, * FROM notifications;

-- ============================================
-- 1. CHECK FOREIGN KEYS
-- ============================================
SELECT 'STEP 1 - FK CHECK' AS step,
    conname AS constraint_name,
    conrelid::regclass AS table,
    confrelid::regclass AS referenced_table
FROM pg_constraint
WHERE conrelid::regclass = 'notifications'::regclass;

-- ============================================
-- 2. BASE DATA (SEED)
-- ============================================

INSERT INTO feedbacks (id, descricao, nota, created_at)
VALUES (1, 'Feedback test', 5, NOW());

INSERT INTO admins (user_id, nome, email, created_at)
VALUES (1, 'Admin Test', 'admin@email.com', NOW());

-- Validate seed
SELECT 'STEP 2 - SEED DATA' AS step, * FROM feedbacks;
SELECT 'STEP 2 - SEED DATA' AS step, * FROM admins;

-- ============================================
-- 3. INSERT NOTIFICATION (PENDING)
-- ============================================

INSERT INTO notifications (
    feedback_id,
    receiver_id,
    channel,
    send_status,
    attempts,
    message
) VALUES (
    1,
    1,
    'EMAIL',
    'PENDING',
    0,
    'Notification test'
);

-- Validate insert
SELECT 'STEP 3 - AFTER INSERT (PENDING)' AS step, * FROM notifications;

-- ============================================
-- 4. SIMULATE SUCCESS (SENT)
-- ============================================

UPDATE notifications
SET send_status = 'SENT',
    sent_at = NOW(),
    updated_at = NOW()
WHERE id = 1;

-- Validate SENT
SELECT 'STEP 4 - AFTER SENT' AS step, * FROM notifications;

-- ============================================
-- 5. SIMULATE FAILURE (FAILED)
-- ============================================

UPDATE notifications
SET send_status = 'FAILED',
    attempts = attempts + 1,
    updated_at = NOW()
WHERE id = 1;

-- Validate FAILED
SELECT 'STEP 5 - AFTER FAILED' AS step, * FROM notifications;

-- ============================================
-- 6. RETRY QUEUE VALIDATION
-- ============================================

SELECT 'STEP 6 - RETRY QUEUE' AS step, *
FROM notifications
WHERE send_status = 'FAILED'
AND attempts < 3
ORDER BY updated_at ASC;

-- ============================================
-- 7. FOREIGN KEY ERROR TESTS
-- ============================================

-- Invalid feedback_id (should fail)
INSERT INTO notifications (
    feedback_id,
    receiver_id,
    channel,
    send_status,
    attempts,
    message
) VALUES (
    999,   -- does not exist ❌
    1,
    'EMAIL',
    'PENDING',
    0,
    'FK error - feedback'
);

-- Invalid receiver_id (should fail)
INSERT INTO notifications (
    feedback_id,
    receiver_id,
    channel,
    send_status,
    attempts,
    message
) VALUES (
    1,
    999,   -- does not exist ❌
    'EMAIL',
    'PENDING',
    0,
    'FK error - admin'
);

-- ============================================
-- 8. FINAL STATE VALIDATION
-- ============================================

SELECT 'STEP 8 - FINAL FEEDBACKS' AS step, * FROM feedbacks;
SELECT 'STEP 8 - FINAL ADMINS' AS step, * FROM admins;
SELECT 'STEP 8 - FINAL NOTIFICATIONS' AS step, * FROM notifications;