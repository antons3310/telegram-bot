-- liquibase formatted sql

-- changeset ashebalkin:1
create table notification_task
(
    id                   serial       NOT NULL PRIMARY KEY,
    chat_id              bigint       NOT NULL,
    notification_date    timestamp    NOT NULL,
    sent_date            timestamp,
    notification_message varchar(255) NOT NULL,
    status               varchar(255) NOT NULL default 'PROCESS'

);

alter table notification_task
    owner to bot;

create index notification_list_date_inx on notification_task (notification_date) where status = 'PROCESS';

