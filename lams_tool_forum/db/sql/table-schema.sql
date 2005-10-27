alter table tl_lafrum11_attachment drop foreign key FK389AD9A2FE939F2A;
alter table tl_lafrum11_attachment drop foreign key FK389AD9A2131CE31E;
alter table tl_lafrum11_message drop foreign key FK4A6067E8131CE31E;
alter table tl_lafrum11_message drop foreign key FK4A6067E824089E4D;
alter table tl_lafrum11_message drop foreign key FK4A6067E8C6FF3C72;
alter table tl_lafrum11_message drop foreign key FK4A6067E8B0A7E6B3;
alter table tl_lafrum11_tool_session drop foreign key FK5A04D7AE131CE31E;
drop table if exists tl_lafrum11_attachment;
drop table if exists tl_lafrum11_forum;
drop table if exists tl_lafrum11_forum_user;
drop table if exists tl_lafrum11_message;
drop table if exists tl_lafrum11_tool_session;
create table tl_lafrum11_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   forum_uid bigint,
   message_uid bigint,
   primary key (uid)
);
create table tl_lafrum11_forum (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   allow_anonym bit,
   run_offline bit,
   lock_on_finished bit,
   instructions varchar(255),
   online_instructions varchar(255),
   offline_instructions varchar(255),
   content_in_use bit,
   define_later bit,
   content_id bigint unique,
   primary key (uid)
);
create table tl_lafrum11_forum_user (
   uid bigint not null auto_increment,
   user_id bigint,
   status bit,
   full_name varchar(255),
   user_name varchar(255),
   primary key (uid)
);
create table tl_lafrum11_message (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   modified_by bigint,
   subject varchar(255),
   body text,
   is_authored bit,
   is_anonymous bit,
   forum_session_uid bigint,
   user_uid bigint,
   forum_uid bigint,
   parent_uid bigint,
   primary key (uid)
);
create table tl_lafrum11_tool_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   forum_uid bigint,
   session_id bigint,
   primary key (uid)
);
alter table tl_lafrum11_attachment add index FK389AD9A2FE939F2A (message_uid), add constraint FK389AD9A2FE939F2A foreign key (message_uid) references tl_lafrum11_message (uid);
alter table tl_lafrum11_attachment add index FK389AD9A2131CE31E (forum_uid), add constraint FK389AD9A2131CE31E foreign key (forum_uid) references tl_lafrum11_forum (uid);
alter table tl_lafrum11_message add index FK4A6067E8131CE31E (forum_uid), add constraint FK4A6067E8131CE31E foreign key (forum_uid) references tl_lafrum11_forum (uid);
alter table tl_lafrum11_message add index FK4A6067E824089E4D (parent_uid), add constraint FK4A6067E824089E4D foreign key (parent_uid) references tl_lafrum11_message (uid);
alter table tl_lafrum11_message add index FK4A6067E8C6FF3C72 (forum_session_uid), add constraint FK4A6067E8C6FF3C72 foreign key (forum_session_uid) references tl_lafrum11_tool_session (uid);
alter table tl_lafrum11_message add index FK4A6067E8B0A7E6B3 (user_uid), add constraint FK4A6067E8B0A7E6B3 foreign key (user_uid) references tl_lafrum11_forum_user (uid);
alter table tl_lafrum11_tool_session add index FK5A04D7AE131CE31E (forum_uid), add constraint FK5A04D7AE131CE31E foreign key (forum_uid) references tl_lafrum11_forum (uid);
