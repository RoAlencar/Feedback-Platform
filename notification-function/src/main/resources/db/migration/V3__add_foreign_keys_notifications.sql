-- ============================================
-- ADD FOREIGN KEYS
-- ============================================

ALTER TABLE notifications
ADD CONSTRAINT fk_notifications_feedback
FOREIGN KEY (feedback_id)
REFERENCES feedbacks(id)
ON DELETE CASCADE;

ALTER TABLE notifications
ADD CONSTRAINT fk_notifications_admin
FOREIGN KEY (receiver_id)
REFERENCES admins(user_id)
ON DELETE CASCADE;