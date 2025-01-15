create table account
(
    id           bigint generated always as identity primary key,
    account_type varchar(255),
    balance      numeric,
    client_id    bigint,
    foreign key (client_id) references client (id)
);
