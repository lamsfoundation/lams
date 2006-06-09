-- CVS ID: $Id$
 
SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lachat11_attachment;
drop table if exists tl_lachat11_chat;
drop table if exists tl_lachat11_message;
drop table if exists tl_lachat11_session;
drop table if exists tl_lachat11_user;
create table tl_lachat11_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   chat_uid bigint,
   primary key (uid)
);
create table tl_lachat11_chat (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   instructions text,
   run_offline bit,
   lock_on_finished bit,
   online_instructions text,
   offline_instructions text,
   content_in_use bit,
   define_later bit,
   tool_content_id bigint,
   filtering_enabled bit,
   filter_keywords text,
   primary key (uid)
);
create table tl_lachat11_message (
   uid bigint not null auto_increment,
   chat_session_uid bigint not null,
   from_user_id bigint,
   to_user_id bigint,
   type varchar(255),
   body varchar(255),
   send_date datetime,
   hidden bit,
   primary key (uid)
);
create table tl_lachat11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   session_id bigint,
   session_name varchar(250),
   chat_uid bigint,
   jabber_room varchar(250),
   primary key (uid)
);
create table tl_lachat11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   login_name varchar(255),
   first_name varchar(255),
   jabber_id varchar(255),
   finishedActivity bit,
   chat_session_uid bigint,
   primary key (uid)
);
alter table tl_lachat11_attachment add index FK9ED6CB2E1A3926E3 (chat_uid), add constraint FK9ED6CB2E1A3926E3 foreign key (chat_uid) references tl_lachat11_chat (uid);
alter table tl_lachat11_message add index FKCC08C1DCC15A415F (from_user_id), add constraint FKCC08C1DCC15A415F foreign key (from_user_id) references tl_lachat11_user (uid);
alter table tl_lachat11_message add index FKCC08C1DC50B2D730 (to_user_id), add constraint FKCC08C1DC50B2D730 foreign key (to_user_id) references tl_lachat11_user (uid);
alter table tl_lachat11_message add index FKCC08C1DC9C8469FC (chat_session_uid), add constraint FKCC08C1DC9C8469FC foreign key (chat_session_uid) references tl_lachat11_session (uid);
alter table tl_lachat11_session add index FK96E446B1A3926E3 (chat_uid), add constraint FK96E446B1A3926E3 foreign key (chat_uid) references tl_lachat11_chat (uid);
alter table tl_lachat11_user add index FK4EB82169C8469FC (chat_session_uid), add constraint FK4EB82169C8469FC foreign key (chat_session_uid) references tl_lachat11_session (uid);

INSERT INTO tl_lachat11_chat (title,instructions,online_instructions,offline_instructions,tool_content_id,run_offline,lock_on_finished,filtering_enabled, content_in_use,define_later) 
VALUES("LAMS Chat","Chat Instruction","Online instructions","Offline instructions",${default_content_id},0,0,0,0,0);

SET FOREIGN_KEY_CHECKS=1;