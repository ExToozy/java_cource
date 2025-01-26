CREATE EXTENSION IF NOT EXISTS pgcrypto;

update transaction
SET transaction_id = gen_random_uuid(),
    requested_at   = completed_at,
    status         = 'ACCEPTED';


update account
set account_id = gen_random_uuid();