ALTER TABLE password_reset_tokens
ADD COLUMN is_active BOOLEAN DEFAULT TRUE;