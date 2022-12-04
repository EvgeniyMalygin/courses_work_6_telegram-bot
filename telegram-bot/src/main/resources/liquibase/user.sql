-- liquibase formatted sql

-- changeset emalygin:1
CREATE TABLE notification_task (
                       id SERIAL,
                       chat_id BIGINT,
                       message TEXT,
                       data_Time_Message TIMESTAMP
)