--liquibase formatted sql
create table notification_task
(
    id        serial    not null,
    chat_id   serial    not null,
    note_text text      not null,
    date_time timestamp not null
);