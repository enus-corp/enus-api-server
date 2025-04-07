ALTER TABLE users 
    ALTER COLUMN first_name DROP NOT NULL,
    ALTER COLUMN last_name DROP NOT NULL,
    ALTER COLUMN username DROP NOT NULL,
    ALTER COLUMN password DROP NOT NULL,
    ALTER COLUMN gender DROP NOT NULL,
    ALTER COLUMN age DROP NOT NULL,
    ADD COLUMN is_oauth_user BOOLEAN DEFAULT FALSE,
    ADD CONSTRAINT chk_password_not_null_for_non_oauth 
    CHECK (
        (is_oauth_user = TRUE AND password IS NULL) OR
        (is_oauth_user = FALSE AND password IS NOT NULL)
    );