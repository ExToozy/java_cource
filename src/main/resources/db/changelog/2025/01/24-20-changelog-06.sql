ALTER TABLE account
    add column account_id     uuid unique,
    add column frozen_amount  numeric default 0,
    add column account_status varchar(255) not null default 'OPEN'