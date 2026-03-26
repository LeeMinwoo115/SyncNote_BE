-- 1) chat_messages 보강
ALTER TABLE chat_messages
    ADD COLUMN IF NOT EXISTS message_type VARCHAR(20);

ALTER TABLE chat_messages
    ADD COLUMN IF NOT EXISTS is_edited BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE chat_messages
    ADD COLUMN IF NOT EXISTS is_deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- 기존 데이터는 모두 일반 텍스트 메시지로 간주
UPDATE chat_messages
SET message_type = 'TEXT'
WHERE message_type IS NULL;

ALTER TABLE chat_messages
    ALTER COLUMN message_type SET NOT NULL;

-- 2) room_to_users.last_read_chat_id 정리
-- 기존 last_read_chat_id가 문자열 타입일 수 있으므로 bigint로 변환
ALTER TABLE room_to_users
    ALTER COLUMN last_read_chat_id TYPE BIGINT
    USING NULLIF(last_read_chat_id, '')::BIGINT;

-- FK 추가
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_room_to_users_last_read_chat'
    ) THEN
        ALTER TABLE room_to_users
            ADD CONSTRAINT fk_room_to_users_last_read_chat
            FOREIGN KEY (last_read_chat_id)
            REFERENCES chat_messages(id)
            ON DELETE SET NULL;
    END IF;
END $$;

-- 조회 성능용 인덱스
CREATE INDEX IF NOT EXISTS idx_chat_messages_room_id_id
    ON chat_messages(room_id, id);

CREATE INDEX IF NOT EXISTS idx_room_to_users_room_id
    ON room_to_users(room_id);

CREATE INDEX IF NOT EXISTS idx_room_to_users_last_read_chat_id
    ON room_to_users(last_read_chat_id);