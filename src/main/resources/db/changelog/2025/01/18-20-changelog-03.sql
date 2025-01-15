create table transaction
(
    id             bigint generated always as identity primary key,
    amount         numeric,
    completed_at   timestamp without time zone,
    client_from_id bigint,
    client_to_id   bigint,
    foreign key (client_to_id) references account (id),
    foreign key (client_from_id) references account (id)
);

