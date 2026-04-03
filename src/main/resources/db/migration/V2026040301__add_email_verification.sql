-- 1. users 테이블에 email_verified 컬럼 추가
ALTER TABLE users
ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE;

-- 2. email_verifications 테이블 생성
CREATE TABLE email_verifications (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    token VARCHAR(100) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    expires_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
);

-- 3. 제약조건 추가
ALTER TABLE email_verifications
    ADD CONSTRAINT uk_email_verifications_email UNIQUE (email);

ALTER TABLE email_verifications
    ADD CONSTRAINT uk_email_verifications_token UNIQUE (token);

-- 4. 인덱스 추가
CREATE INDEX idx_email_verifications_expires_at
    ON email_verifications (expires_at);

CREATE INDEX idx_email_verifications_verified
    ON email_verifications (verified);