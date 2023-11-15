create table users
(
    is_active            boolean,
    is_blocked           boolean,
    is_connected_account boolean,
    chat_id              bigint,
    id                   bigserial
        primary key,
    last_activity_at     timestamp(6) with time zone,
    registered_at        timestamp(6) with time zone,
    bot_state            varchar(255),
    prev_bot_state       varchar(255),
    tg_username          varchar(255),
    username             varchar(255)
);

alter table users
    owner to postgres;