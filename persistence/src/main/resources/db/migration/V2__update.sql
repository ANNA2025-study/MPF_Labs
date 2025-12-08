CREATE TABLE IF NOT EXISTS users (
                                     id integer generated always as identity primary key,
                                     username VARCHAR(64) NOT NULL UNIQUE,
    display_name VARCHAR(255)
    );

ALTER TABLE comments
    ADD COLUMN user_id BIGINT;

ALTER TABLE comments
    ADD CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE SET NULL;
