CREATE TABLE notification_task
(
    id        SERIAL    NOT NULL,
    chat_id   SERIAL    NOT NULL,
    note_text TEXT      NOT NULL,
    date_time TIMESTAMP NOT NULL
);