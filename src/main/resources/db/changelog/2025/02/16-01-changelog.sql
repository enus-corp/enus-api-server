-- liquibase formatted sql

-- changeset idohyeon:1739714641303-1
CREATE TABLE IF NOT EXISTS articles
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    deleted_at      TIMESTAMP WITHOUT TIME ZONE,
    article_id      VARCHAR(255)                            NOT NULL,
    media_code      VARCHAR(255)                            NOT NULL,
    title           VARCHAR(255)                            NOT NULL,
    summary         TEXT                                    NOT NULL,
    content         TEXT                                    NOT NULL,
    section_code    VARCHAR(255)                            NOT NULL,
    subsection_code VARCHAR(255)                            NOT NULL,
    publish_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_article PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-2
CREATE TABLE IF NOT EXISTS keywords
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    deleted_at TIMESTAMP WITHOUT TIME ZONE,
    word       VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_keyword PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-3
CREATE TABLE IF NOT EXISTS login_histories
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    deleted_at   TIMESTAMP WITHOUT TIME ZONE,
    username     VARCHAR(255)                            NOT NULL,
    ip_address   VARCHAR(255)                            NOT NULL,
    login_status INTEGER                                 NOT NULL,
    reason       VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_loginhistory PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-4
CREATE TABLE IF NOT EXISTS password_reset_tokens
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id       BIGINT                                  NOT NULL,
    response_code VARCHAR(255)                            NOT NULL,
    email_code    VARCHAR(255)                            NOT NULL,
    expires_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_passwordresettoken PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-5
CREATE TABLE IF NOT EXISTS popular_articles
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    article_id VARCHAR(255)                            NOT NULL,
    media_code VARCHAR(255)                            NOT NULL,
    media_name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_populararticle PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-6
CREATE TABLE IF NOT EXISTS roles
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    role    VARCHAR(255)                            NOT NULL,
    user_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-7
CREATE TABLE IF NOT EXISTS users
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY                  NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    deleted_at      TIMESTAMP WITHOUT TIME ZONE,
    first_name      VARCHAR(20)                                              NOT NULL,
    last_name       VARCHAR(20)                                              NOT NULL,
    username        VARCHAR(20)                                              NOT NULL,
    password        VARCHAR(255)                                             NOT NULL,
    email           VARCHAR(100)                                             NOT NULL,
    is_expired      SMALLINT DEFAULT 0                                       NOT NULL,
    is_locked       SMALLINT DEFAULT 0                                       NOT NULL,
    attempt         SMALLINT DEFAULT 0 CHECK (attempt >= 0 AND attempt <= 5) NOT NULL,
    last_attempt_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-8
CREATE TABLE user_keywords
(
    id                   INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id              BIGINT                                   NOT NULL,
    keyword_id           BIGINT                                   NOT NULL,
    notification_enabled BOOLEAN,
    CONSTRAINT pk_userkeywords PRIMARY KEY (id)
);

-- changeset idohyeon:1739714641303-9
ALTER TABLE keywords
    ADD CONSTRAINT uc_keyword_word UNIQUE (word);

-- changeset idohyeon:1739714641303-10
ALTER TABLE password_reset_tokens
    ADD CONSTRAINT uc_passwordresettoken_user UNIQUE (user_id);

-- changeset idohyeon:1739714641303-11
ALTER TABLE roles
    ADD CONSTRAINT uc_role_role UNIQUE (role);

-- changeset idohyeon:1739714641303-12
ALTER TABLE users
    ADD CONSTRAINT uc_user_username UNIQUE (username);

-- changeset idohyeon:1739714641303-13
CREATE INDEX idx_email ON users (email);

-- changeset idohyeon:1739714641303-14
CREATE INDEX idx_username ON users (username);

-- changeset idohyeon:1739714641303-15
ALTER TABLE password_reset_tokens
    ADD CONSTRAINT FK_PASSWORDRESETTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset idohyeon:1739714641303-16
ALTER TABLE roles
    ADD CONSTRAINT FK_ROLE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

-- changeset idohyeon:1739714641303-17
ALTER TABLE user_keywords
    ADD CONSTRAINT FK_USERKEYWORDS_ON_KEYWORD FOREIGN KEY (keyword_id) REFERENCES keywords (id);

-- changeset idohyeon:1739714641303-18
ALTER TABLE user_keywords
    ADD CONSTRAINT FK_USERKEYWORDS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

