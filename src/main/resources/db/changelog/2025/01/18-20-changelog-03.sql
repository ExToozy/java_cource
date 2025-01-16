create table transaction
(
    id           bigint generated always as identity primary key,
    amount       numeric,
    completed_at timestamp without time zone,
    client_id    bigint,
    foreign key (client_id) references account (id)
);

