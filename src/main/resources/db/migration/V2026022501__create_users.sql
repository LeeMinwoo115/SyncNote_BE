-- 1) Sequence: user_seq
CREATE SEQUENCE IF NOT EXISTS user_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 100; -- allocationSize=100에 맞춤

-- 2) Table: users
CREATE TABLE IF NOT EXISTS users (
                                     id            BIGINT PRIMARY KEY DEFAULT nextval('user_seq'),

                                     email         VARCHAR(100) NOT NULL,
                                     nickname      VARCHAR(20)  NOT NULL,
                                     password      TEXT NULL,

                                     role          VARCHAR(50)  NOT NULL,   -- EnumType.STRING

                                     deleted_at    TIMESTAMP NULL,

                                     provider_type VARCHAR(50) NULL,        -- EnumType.STRING
                                     provider_id   TEXT NULL,

                                     created_at    TIMESTAMP NULL,
                                     updated_at    TIMESTAMP NULL
);

-- 3) Indexes / Constraints
-- 소프트 삭제를 쓰는 경우: deleted_at IS NULL 인 "활성 사용자"만 유니크 보장
CREATE UNIQUE INDEX IF NOT EXISTS uk_users_email_active
    ON users (email)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_users_nickname_active
    ON users (nickname)
    WHERE deleted_at IS NULL;

-- 조회가 많을 가능성이 높은 컬럼 인덱스
CREATE INDEX IF NOT EXISTS idx_users_provider
    ON users (provider_type, provider_id);

-- deleted_at 조건 조회 최적화(선택)
CREATE INDEX IF NOT EXISTS idx_users_deleted_at
    ON users (deleted_at);