create table race (
    key_name varchar(20) not null,
    field_value varchar(40) not null,
    constraint race_uk unique (key_name, field_value)
);
