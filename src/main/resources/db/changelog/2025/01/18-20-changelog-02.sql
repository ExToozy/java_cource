create table data_source_error_log
(
    id               bigint generated always as identity primary key,
    error_message    varchar(255),
    method_signature varchar(255),
    stack_trace      varchar(255)
);

