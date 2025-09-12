create table users
(
    id uuid not null
        primary key,
    created_at timestamp(6),
    email varchar(255) not null
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    firstname varchar(255) not null,
    external_id uuid,
    lastname varchar(255) not null,
    updated_at timestamp(6),
    username varchar(255) not null
        constraint ukr43af9ap4edm43mmtq01oddj6
            unique
);

alter table users owner to postgres;
