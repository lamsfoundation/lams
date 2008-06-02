-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lagmap10_attachment;
drop table if exists tl_lagmap10_gmap;
drop table if exists tl_lagmap10_marker;
drop table if exists tl_lagmap10_session;
drop table if exists tl_lagmap10_user;
create table tl_lagmap10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, gmap_uid bigint, primary key (uid));
create table tl_lagmap10_gmap (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, allow_rich_editor bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, primary key (uid));
create table tl_lagmap10_marker (uid bigint not null auto_increment, longitude double precision, latitude double precision, info_window_message varchar(255), create_date datetime, update_date datetime, is_authored bit, gmap_uid bigint, title varchar(255), primary key (uid));
create table tl_lagmap10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), gmap_uid bigint, primary key (uid));
create table tl_lagmap10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, gmap_session_uid bigint, entry_uid bigint, primary key (uid));
alter table tl_lagmap10_attachment add index FKBA2ECCB274028C80 (gmap_uid), add constraint FKBA2ECCB274028C80 foreign key (gmap_uid) references tl_lagmap10_gmap (uid);
alter table tl_lagmap10_marker add index FK10F2274974028C80 (gmap_uid), add constraint FK10F2274974028C80 foreign key (gmap_uid) references tl_lagmap10_gmap (uid);
alter table tl_lagmap10_session add index FK519D516774028C80 (gmap_uid), add constraint FK519D516774028C80 foreign key (gmap_uid) references tl_lagmap10_gmap (uid);
alter table tl_lagmap10_user add index FK7CB3F69AC5F056D9 (gmap_session_uid), add constraint FK7CB3F69AC5F056D9 foreign key (gmap_session_uid) references tl_lagmap10_session (uid);

INSERT INTO tl_lagmap10_gmap (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_rich_editor) 
VALUES(
	"Gmap",
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
