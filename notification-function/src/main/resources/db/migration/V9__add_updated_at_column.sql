ALTER TABLE notifications 
ALTER COLUMN updated_at SET DEFAULT NOW();

UPDATE notifications 
SET updated_at = NOW()
WHERE updated_at IS NULL;