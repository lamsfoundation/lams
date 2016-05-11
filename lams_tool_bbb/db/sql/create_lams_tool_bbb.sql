
 
SET FOREIGN_KEY_CHECKS=0;
-- generated sql
drop table if exists tl_labbb10_attachment;
drop table if exists tl_labbb10_config;
drop table if exists tl_labbb10_bbb;
drop table if exists tl_labbb10_session;
drop table if exists tl_labbb10_user;
create table tl_labbb10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, bbb_uid bigint, primary key (uid));
create table tl_labbb10_config (uid bigint not null auto_increment, config_key varchar(255), config_value varchar(255), primary key (uid));
create table tl_labbb10_bbb (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, reflect_on_activity bit, reflect_instructions text, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, primary key (uid));
create table tl_labbb10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), bbb_uid bigint, meeting_created bit, meeting_key varchar(255), attendee_password varchar(20), moderator_password varchar(20), primary key (uid));
create table tl_labbb10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, bbb_session_uid bigint, notebook_entry_uid bigint, primary key (uid));
-- end generated sql

-- set engine to innodb for all tables
alter table tl_labbb10_attachment engine=innodb;
alter table tl_labbb10_bbb engine=innodb;
alter table tl_labbb10_config engine=innodb;
alter table tl_labbb10_session engine=innodb;
alter table tl_labbb10_user engine=innodb;

-- generated sql
alter table tl_labbb10_attachment add index fk_labbb10_bbb_attachment_to_bbb (bbb_uid), add constraint fk_labbb10_bbb_attachment_to_bbb foreign key (bbb_uid) references tl_labbb10_bbb (uid);
alter table tl_labbb10_session add index fk_labbb10_bbb_session_to_bbb (bbb_uid), add constraint fk_labbb10_bbb_session_to_bbb foreign key (bbb_uid) references tl_labbb10_bbb (uid);
alter table tl_labbb10_user add index fk_labbb10_bbb_user_to_bbb_session (bbb_session_uid), add constraint fk_labbb10_bbb_user_to_bbb_session foreign key (bbb_session_uid) references tl_labbb10_session (uid);
-- end generated sql

INSERT INTO tl_labbb10_bbb (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	reflect_on_activity
	)
VALUES(
	"Web Conference",
	"Instructions",
	"",
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	0
);

-- default configuration values
INSERT INTO tl_labbb10_config (config_key, config_value) VALUES ("server_url", "http://172.20.100.253/");
INSERT INTO tl_labbb10_config (config_key, config_value) VALUES ("security_salt", "12309usadoiaksdjblquge12312");


SET FOREIGN_KEY_CHECKS=1;
