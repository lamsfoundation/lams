-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
-- generated sql
drop table if exists tl_laddim10_attachment;
drop table if exists tl_laddim10_config;
drop table if exists tl_laddim10_dimdim;
drop table if exists tl_laddim10_session;
drop table if exists tl_laddim10_user;
create table tl_laddim10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, dimdim_uid bigint, primary key (uid));
create table tl_laddim10_config (uid bigint not null auto_increment, config_key varchar(255), config_value varchar(255), primary key (uid));
create table tl_laddim10_dimdim (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, reflect_on_activity bit, reflect_instructions text, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, max_attendee_mikes integer, primary key (uid));
create table tl_laddim10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), dimdim_uid bigint, meeting_created bit, meeting_key varchar(255), max_attendee_mikes integer, primary key (uid));
create table tl_laddim10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, dimdim_session_uid bigint, notebook_entry_uid bigint, primary key (uid));
-- end generated sql

-- set engine to innodb for all tables
alter table tl_laddim10_attachment engine=innodb;
alter table tl_laddim10_dimdim engine=innodb;
alter table tl_laddim10_config engine=innodb;
alter table tl_laddim10_session engine=innodb;
alter table tl_laddim10_user engine=innodb;

-- generated sql
alter table tl_laddim10_attachment add index fk_laddim10_dimdim_attachment_to_dimdim (dimdim_uid), add constraint fk_laddim10_dimdim_attachment_to_dimdim foreign key (dimdim_uid) references tl_laddim10_dimdim (uid);
alter table tl_laddim10_session add index fk_laddim10_dimdim_session_to_dimdim (dimdim_uid), add constraint fk_laddim10_dimdim_session_to_dimdim foreign key (dimdim_uid) references tl_laddim10_dimdim (uid);
alter table tl_laddim10_user add index fk_laddim10_dimdim_user_to_dimdim_session (dimdim_session_uid), add constraint fk_laddim10_dimdim_user_to_dimdim_session foreign key (dimdim_session_uid) references tl_laddim10_session (uid);
-- end generated sql

INSERT INTO tl_laddim10_dimdim (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity,
	max_attendee_mikes
	)
VALUES(
	"Dimdim",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	0,
	1
);

-- default configuration values
INSERT INTO tl_laddim10_config (config_key, config_value) VALUES ("standard_server_url", "http://172.20.100.253/");
INSERT INTO tl_laddim10_config (config_key, config_value) VALUES ("enterprise_server_url", "http://209.217.107.183/");
INSERT INTO tl_laddim10_config (config_key, config_value) VALUES ("admin_login", "Administrator");
-- INSERT INTO tl_laddim10_config (config_key, config_value) VALUES ("admin_password", "dimdim123");
-- INSERT INTO tl_laddim10_config (config_key, config_value) VALUES ("version", "enterprise");


SET FOREIGN_KEY_CHECKS=1;
