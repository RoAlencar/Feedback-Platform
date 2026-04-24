UPDATE notifications 
SET send_status = 'PENDING'
WHERE send_status IS NULL;

ALTER TABLE notifications 
ADD COLUMN IF NOT EXISTS receiver_id BIGINT;