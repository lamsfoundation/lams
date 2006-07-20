-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lantbk11_attachment;
drop table if exists tl_lantbk11_notebook;
drop table if exists tl_lantbk11_session;
drop table if exists tl_lantbk11_user;
create table tl_lantbk11_attachment (uid bigint not null auto_increment, file_version_id bigint, file_type varchar(255), file_name varchar(255), file_uuid bigint, create_date datetime, notebook_uid bigint, primary key (uid));
create table tl_lantbk11_notebook (uid bigint not null auto_increment, create_date datetime, update_date datetime, create_by bigint, title varchar(255), instructions text, run_offline bit, lock_on_finished bit, online_instructions text, offline_instructions text, content_in_use bit, define_later bit, tool_content_id bigint, primary key (uid));
create table tl_lantbk11_session (uid bigint not null auto_increment, session_end_date datetime, session_start_date datetime, status integer, session_id bigint, session_name varchar(250), notebook_uid bigint, primary key (uid));
create table tl_lantbk11_user (uid bigint not null auto_increment, user_id bigint, last_name varchar(255), login_name varchar(255), first_name varchar(255), finishedActivity bit, notebook_session_uid bigint, primary key (uid));
alter table tl_lantbk11_attachment add index FK2464A040C50BB56A (notebook_uid), add constraint FK2464A040C50BB56A foreign key (notebook_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_session add index FK447CD899C50BB56A (notebook_uid), add constraint FK447CD899C50BB56A foreign key (notebook_uid) references tl_lantbk11_notebook (uid);
alter table tl_lantbk11_user add index FKEA7B0CA888E2F833 (notebook_session_uid), add constraint FKEA7B0CA888E2F833 foreign key (notebook_session_uid) references tl_lantbk11_session (uid);

INSERT INTO tl_lantbk11_notebook (title,instructions,online_instructions,offline_instructions,tool_content_id,run_offline,lock_on_finished,filtering_enabled, content_in_use,define_later) 
VALUES("LAMS Notebook","Notebook Instruction","Online instructions","Offline instructions",${default_content_id},0,0,0,0,0);

SET FOREIGN_KEY_CHECKS=1;