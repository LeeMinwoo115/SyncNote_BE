/* =========================================================
 * 1. Sequences
 * ========================================================= */
CREATE SEQUENCE IF NOT EXISTS room_seq
    START WITH 1
    INCREMENT BY 100
    CACHE 100;

CREATE SEQUENCE IF NOT EXISTS room_to_user_seq
    START WITH 1
    INCREMENT BY 100
    CACHE 100;

CREATE SEQUENCE IF NOT EXISTS board_seq
    START WITH 1
    INCREMENT BY 100
    CACHE 100;

CREATE SEQUENCE IF NOT EXISTS board_element_seq
    START WITH 1
    INCREMENT BY 100
    CACHE 100;

CREATE SEQUENCE IF NOT EXISTS note_seq
    START WITH 1
    INCREMENT BY 100
    CACHE 100;

CREATE SEQUENCE IF NOT EXISTS chat_seq
    START WITH 1
    INCREMENT BY 100
    CACHE 100;


/* =========================================================
 * 2. rooms
 * ========================================================= */
CREATE TABLE IF NOT EXISTS rooms (
                                     id              BIGINT PRIMARY KEY DEFAULT nextval('room_seq'),
                                     title           VARCHAR(255) NOT NULL,
                                     description     TEXT,
                                     visibility      VARCHAR(50) NOT NULL,
                                     invite_code     VARCHAR(255),
                                     owner_id        BIGINT NOT NULL,

                                     created_at      TIMESTAMP,
                                     updated_at      TIMESTAMP,

                                     CONSTRAINT fk_rooms_owner
                                         FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_rooms_owner_id
    ON rooms(owner_id);

CREATE INDEX IF NOT EXISTS idx_rooms_visibility
    ON rooms(visibility);

CREATE UNIQUE INDEX IF NOT EXISTS uk_rooms_invite_code
    ON rooms(invite_code)
    WHERE invite_code IS NOT NULL;


/* =========================================================
 * 3. room_to_users
 * ========================================================= */
CREATE TABLE IF NOT EXISTS room_to_users (
                                             id                  BIGINT PRIMARY KEY DEFAULT nextval('room_to_user_seq'),
                                             user_id             BIGINT NOT NULL,
                                             room_id             BIGINT NOT NULL,
                                             role                VARCHAR(50),
                                             joined_at           TIMESTAMP,
                                             last_read_chat_id   VARCHAR(255),
                                             last_visited_at     TIMESTAMP,

                                             created_at          TIMESTAMP,
                                             updated_at          TIMESTAMP,

                                             CONSTRAINT fk_room_to_users_user
                                                 FOREIGN KEY (user_id) REFERENCES users(id),
                                             CONSTRAINT fk_room_to_users_room
                                                 FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE INDEX IF NOT EXISTS idx_room_to_users_user_id
    ON room_to_users(user_id);

CREATE INDEX IF NOT EXISTS idx_room_to_users_room_id
    ON room_to_users(room_id);

CREATE UNIQUE INDEX IF NOT EXISTS uk_room_to_users_room_user
    ON room_to_users(room_id, user_id);


/* =========================================================
 * 4. boards
 * ========================================================= */
CREATE TABLE IF NOT EXISTS boards (
                                      id              BIGINT PRIMARY KEY DEFAULT nextval('board_seq'),
                                      room_id         BIGINT,

                                      created_at      TIMESTAMP,
                                      updated_at      TIMESTAMP,

                                      CONSTRAINT fk_boards_room
                                          FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE INDEX IF NOT EXISTS idx_boards_room_id
    ON boards(room_id);


/* =========================================================
 * 5. board_elements
 * ========================================================= */
CREATE TABLE IF NOT EXISTS board_elements (
                                              id              BIGINT PRIMARY KEY DEFAULT nextval('board_element_seq'),
                                              board_id        BIGINT,
                                              element_type    VARCHAR(50),
                                              created_by      BIGINT,
                                              updated_by      BIGINT,
                                              is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,
                                              data_json       TEXT,

                                              created_at      TIMESTAMP,
                                              updated_at      TIMESTAMP,

                                              CONSTRAINT fk_board_elements_board
                                                  FOREIGN KEY (board_id) REFERENCES boards(id),
                                              CONSTRAINT fk_board_elements_created_by
                                                  FOREIGN KEY (created_by) REFERENCES users(id),
                                              CONSTRAINT fk_board_elements_updated_by
                                                  FOREIGN KEY (updated_by) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_board_elements_board_id
    ON board_elements(board_id);

CREATE INDEX IF NOT EXISTS idx_board_elements_created_by
    ON board_elements(created_by);

CREATE INDEX IF NOT EXISTS idx_board_elements_updated_by
    ON board_elements(updated_by);

CREATE INDEX IF NOT EXISTS idx_board_elements_element_type
    ON board_elements(element_type);

CREATE INDEX IF NOT EXISTS idx_board_elements_is_deleted
    ON board_elements(is_deleted);


/* =========================================================
 * 6. notes
 * ========================================================= */
CREATE TABLE IF NOT EXISTS notes (
                                     id              BIGINT PRIMARY KEY DEFAULT nextval('note_seq'),
                                     room_id         BIGINT,
                                     content_json    TEXT,

                                     created_at      TIMESTAMP,
                                     updated_at      TIMESTAMP,

                                     CONSTRAINT fk_notes_room
                                         FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE INDEX IF NOT EXISTS idx_notes_room_id
    ON notes(room_id);


/* =========================================================
 * 7. chat_messages
 * ========================================================= */
CREATE TABLE IF NOT EXISTS chat_messages (
                                             id              BIGINT PRIMARY KEY DEFAULT nextval('chat_seq'),
                                             room_id         BIGINT,
                                             user_id         BIGINT,
                                             content         TEXT,

                                             created_at      TIMESTAMP,
                                             updated_at      TIMESTAMP,

                                             CONSTRAINT fk_chat_messages_room
                                                 FOREIGN KEY (room_id) REFERENCES rooms(id),
                                             CONSTRAINT fk_chat_messages_user
                                                 FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_chat_messages_room_id
    ON chat_messages(room_id);

CREATE INDEX IF NOT EXISTS idx_chat_messages_user_id
    ON chat_messages(user_id);

CREATE INDEX IF NOT EXISTS idx_chat_messages_created_at
    ON chat_messages(created_at);