-- CVS ID: $Id$
 
 -- TODO check InnoDB is set

SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_laddim10_attachment;
drop table if exists tl_laddim10_dimdim;
drop table if exists tl_laddim10_session;
drop table if exists tl_laddim10_user;
create table tl_laddim10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, dimdim_uid bigint, primary key (uid));
create table tl_laddim10_dimdim (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, allow_rich_editor bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, topic varchar(255), meeting_key varchar(255), max_attendee_mikes integer, primary key (uid));
create table tl_laddim10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), dimdim_uid bigint, topic varchar(255), meeting_key varchar(255), max_attendee_mikes integer, conf_key varchar(255), primary key (uid));
create table tl_laddim10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, dimdim_session_uid bigint, entry_uid bigint, primary key (uid));
alter table tl_laddim10_attachment add index FK3525B03D7B14D4B (dimdim_uid), add constraint FK3525B03D7B14D4B foreign key (dimdim_uid) references tl_laddim10_dimdim (uid);
alter table tl_laddim10_session add index FKC847CCB6D7B14D4B (dimdim_uid), add constraint FKC847CCB6D7B14D4B foreign key (dimdim_uid) references tl_laddim10_dimdim (uid);
alter table tl_laddim10_user add index FK91F0D5AB77EFDAE4 (dimdim_session_uid), add constraint FK91F0D5AB77EFDAE4 foreign key (dimdim_session_uid) references tl_laddim10_session (uid);

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
	allow_rich_editor
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
	0
);

SET FOREIGN_KEY_CHECKS=1;
