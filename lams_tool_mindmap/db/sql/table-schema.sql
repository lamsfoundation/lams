
alter table tl_lamind10_attachment drop foreign key FK_NEW_972920762_12090F57FC940906;
alter table tl_lamind10_session drop foreign key FK_NEW_972920762_B7C198E2FC940906;
alter table tl_lamind10_user drop foreign key FK_NEW_972920762_CB8A58FFA3B0FADF;

drop table if exists tl_lamind10_attachment;
drop table if exists tl_lamind10_mindmap;
drop table if exists tl_lamind10_session;
drop table if exists tl_lamind10_user;
drop table if exists tl_lamind10_node;
drop table if exists tl_lamind10_request;

create table tl_lamind10_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, mindmap_uid bigint, primary key (uid));
create table tl_lamind10_mindmap (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, multiuser_mode bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, primary key (uid));
create table tl_lamind10_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), mindmap_uid bigint, primary key (uid));
create table tl_lamind10_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, mindmap_session_uid bigint, entry_uid bigint, primary key (uid));
alter table tl_lamind10_attachment add index FK_NEW_972920762_12090F57FC940906 (mindmap_uid), add constraint FK_NEW_972920762_12090F57FC940906 foreign key (mindmap_uid) references tl_lamind10_mindmap (uid);
alter table tl_lamind10_session add index FK_NEW_972920762_B7C198E2FC940906 (mindmap_uid), add constraint FK_NEW_972920762_B7C198E2FC940906 foreign key (mindmap_uid) references tl_lamind10_mindmap (uid);
alter table tl_lamind10_user add index FK_NEW_972920762_CB8A58FFA3B0FADF (mindmap_session_uid), add constraint FK_NEW_972920762_CB8A58FFA3B0FADF foreign key (mindmap_session_uid) references tl_lamind10_session (uid);
