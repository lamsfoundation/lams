drop table if exists lams_notebook_entry;
create table lams_notebook_entry (uid bigint not null auto_increment, external_id bigint, external_id_type integer, external_signature varchar(255), user_id integer, title varchar(255), entry text, create_date datetime, update_date datetime, primary key (uid));
