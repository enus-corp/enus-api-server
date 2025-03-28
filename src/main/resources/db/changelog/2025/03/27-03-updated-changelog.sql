ALTER TABLE password_reset_tokens
DROP CONSTRAINT uc_passwordresettoken_user;

ALTER TABLE password_reset_tokens
ADD CONSTRAINT fk_password_reset_tokens_users
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE;