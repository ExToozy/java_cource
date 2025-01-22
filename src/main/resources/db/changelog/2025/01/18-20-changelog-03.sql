create table transaction
(
    id           bigint generated always as identity primary key,
    amount       numeric,
    completed_at timestamp without time zone,
    account_id   bigint,
    foreign key (account_id) references account (id)
);

