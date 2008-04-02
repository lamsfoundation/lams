-- $Id$

SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lawiki10_attachment;
drop table if exists tl_lawiki10_wiki;
drop table if exists tl_lawiki10_wiki_user;
drop table if exists tl_lawiki10_message;
drop table if exists tl_lawiki10_message_seq;
drop table if exists tl_lawiki10_report;
drop table if exists tl_lawiki10_tool_session;
create table tl_lawiki10_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   wiki_uid bigint,
   message_uid bigint,
   primary key (uid)
)TYPE=InnoDB;
create table tl_lawiki10_wiki (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   allow_anonym smallint,
   run_offline smallint,
   lock_on_finished smallint,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use smallint,
   define_later smallint,
   content_id bigint unique,
   allow_edit smallint,
   allow_rich_editor smallint,
   allow_new_topic smallint, 
   allow_upload smallint, 
   maximum_reply integer, 
   minimum_reply integer,
   limited_of_chars integer,
   limited_input_flag smallint,
   reflect_instructions varchar(255),
   reflect_on_activity smallint, 
   primary key (uid)
)TYPE=InnoDB;
create table tl_lawiki10_wiki_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   session_id bigint,
   login_name varchar(255),
   session_finished smallint,
   primary key (uid)
)TYPE=InnoDB;
create table tl_lawiki10_message (
   uid bigint not null auto_increment,
   create_date datetime,
   last_reply_date datetime,
   update_date datetime,
   create_by bigint,
   modified_by bigint,
   subject varchar(255),
   body text,
   is_authored smallint,
   is_anonymous smallint,
   wiki_session_uid bigint,
   parent_uid bigint,
   wiki_uid bigint,
   reply_number integer,
   hide_flag smallint,
   report_id bigint,
   authored_parent_uid bigint,
   primary key (uid)
)TYPE=InnoDB;
create table tl_lawiki10_message_seq (
   uid bigint not null auto_increment,
   root_message_uid bigint,
   message_uid bigint,
   message_level smallint,
   primary key (uid)
)TYPE=InnoDB;
create table tl_lawiki10_report (
   uid bigint not null auto_increment,
   comment text,
   release_date datetime,
   mark float,
   primary key (uid)
)TYPE=InnoDB;
create table tl_lawiki10_tool_session (
   uid bigint not null auto_increment,
   version integer not null,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   mark_released smallint,
   wiki_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)TYPE=InnoDB;
alter table tl_lawiki10_attachment add index FK_NEW_1983840333_389AD9A2FE939F2A (message_uid), add constraint FK_NEW_1983840333_389AD9A2FE939F2A foreign key (message_uid) references tl_lawiki10_message (uid);
alter table tl_lawiki10_attachment add index FK_NEW_1983840333_389AD9A2131CE31E (wiki_uid), add constraint FK_NEW_1983840333_389AD9A2131CE31E foreign key (wiki_uid) references tl_lawiki10_wiki (uid);
alter table tl_lawiki10_wiki add index FK_NEW_1983840333_87917942E42F4351 (create_by), add constraint FK_NEW_1983840333_87917942E42F4351 foreign key (create_by) references tl_lawiki10_wiki_user (uid);
alter table tl_lawiki10_wiki_user add index FK_NEW_1983840333_7B83A4A85F0116B6 (session_id), add constraint FK_NEW_1983840333_7B83A4A85F0116B6 foreign key (session_id) references tl_lawiki10_tool_session (uid);
alter table tl_lawiki10_message add index FK_NEW_1983840333_4A6067E8E42F4351 (create_by), add constraint FK_NEW_1983840333_4A6067E8E42F4351 foreign key (create_by) references tl_lawiki10_wiki_user (uid);
alter table tl_lawiki10_message add index FK_NEW_1983840333_4A6067E897F0DB46 (report_id), add constraint FK_NEW_1983840333_4A6067E897F0DB46 foreign key (report_id) references tl_lawiki10_report (uid);
alter table tl_lawiki10_message add index FK_NEW_1983840333_4A6067E8131CE31E (wiki_uid), add constraint FK_NEW_1983840333_4A6067E8131CE31E foreign key (wiki_uid) references tl_lawiki10_wiki (uid);
alter table tl_lawiki10_message add index FK_NEW_1983840333_4A6067E824089E4D (parent_uid), add constraint FK_NEW_1983840333_4A6067E824089E4D foreign key (parent_uid) references tl_lawiki10_message (uid);
alter table tl_lawiki10_message add index FK_NEW_1983840333_4A6067E89357B45B (wiki_session_uid), add constraint FK_NEW_1983840333_4A6067E89357B45B foreign key (wiki_session_uid) references tl_lawiki10_tool_session (uid);
alter table tl_lawiki10_message add index FK_NEW_1983840333_4A6067E8647A7264 (modified_by), add constraint FK_NEW_1983840333_4A6067E8647A7264 foreign key (modified_by) references tl_lawiki10_wiki_user (uid);
alter table tl_lawiki10_message add index IX_msg_auth_parent (authored_parent_uid);
alter table tl_lawiki10_message_seq add index FK_NEW_1983840333_D2C71F88FE939F2A (message_uid), add constraint FK_NEW_1983840333_D2C71F88FE939F2A foreign key (message_uid) references tl_lawiki10_message (uid);
alter table tl_lawiki10_message_seq add index FK_NEW_1983840333_D2C71F8845213B4D (root_message_uid), add constraint FK_NEW_1983840333_D2C71F8845213B4D foreign key (root_message_uid) references tl_lawiki10_message (uid);
alter table tl_lawiki10_tool_session add index FK_NEW_1983840333_5A04D7AE131CE31E (wiki_uid), add constraint FK_NEW_1983840333_5A04D7AE131CE31E foreign key (wiki_uid) references tl_lawiki10_wiki (uid);


INSERT INTO tl_lawiki10_wiki (uid,title,instructions,online_instructions,offline_instructions,content_id,allow_anonym,run_offline,lock_on_finished,content_in_use,define_later,allow_edit,allow_rich_editor,
 allow_new_topic,allow_upload,maximum_reply, minimum_reply,limited_input_flag,limited_of_chars,reflect_on_activity) 
VALUES(1,"Wiki","Instructions",null,null,${default_content_id},0,0,0,0,0,1,0,1,0,1,0,1,5000,0);

INSERT INTO `tl_lawiki10_message` (`uid`, `create_date`, `last_reply_date`, `update_date`, `create_by`, `modified_by`, `subject`, `body`, `is_authored`, `is_anonymous`, `wiki_session_uid`, `parent_uid`, `wiki_uid`, `reply_number`, `hide_flag`, `report_id`) VALUES 
  (1,NOW(),NOW(),NOW(),null,null,'Wiki Page','Wiki content',1,0,NULL,NULL,1,0,0,NULL);
  
SET FOREIGN_KEY_CHECKS=1;
