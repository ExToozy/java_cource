ALTER TABLE transaction
    add column transaction_id uuid unique,
    add column requested_at   timestamp without time zone,
    add column status         varchar(255) not null default 'REQUESTED'

