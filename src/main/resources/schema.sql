drop table if exists comments cascade;
drop table if exists bookings cascade;
drop table if exists users cascade;
drop table if exists items cascade;
drop table if exists requests cascade;

create table if not exists users
    (
        id      bigint          generated by default as identity    primary key,
        name    varchar(255)                                        not null,
        email   varchar(128)                                        not null,
        constraint uq_user_email unique (email)
    );

create table if not exists requests
(
    id              bigint  generated by default as identity    primary key,
    description     varchar                                     not null,
    requester_id    bigint  references users(id)                on delete cascade,
    created         timestamp without time zone
);

create table if not exists items
    (
        id              bigint          generated by default as identity    primary key,
        name            varchar(255)                                        not null,
        description     varchar                                             not null,
        available       boolean         default true                        not null,
        owner_id        bigint          references users(id)                on delete cascade,
        request_id      bigint          references requests(id)             on delete cascade
    );

create table if not exists bookings
    (
        id          bigint                      generated by default as identity    primary key,
        date_start  timestamp without time zone                                     not null,
        date_end    timestamp without time zone                                     not null,
        status      varchar(50)                                                     not null,
        item_id     bigint                      references items(id)                on delete cascade,
        booker_id   bigint                      references users(id)                on delete cascade
    );

create table if not exists comments
    (
        id              bigint                      generated by default as identity    primary key,
        text            varchar                                                         not null,
        item_id         bigint                      references items(id)                on delete cascade,
        author_id       bigint                      references users(id)                on delete cascade,
        posting_date    timestamp without time zone                                     default now()
    );