alter table tl_lafrum11_attachment drop foreign key FK389AD9A29EAD680D;
alter table tl_lafrum11_message drop foreign key FK4A6067E8F7440FBC;
alter table tl_lafrum11_message drop foreign key FK4A6067E89EAD680D;
drop table if exists tl_lafrum11_attachment;
drop table if exists tl_lafrum11_forum;
drop table if exists tl_lafrum11_forum_learners;
drop table if exists tl_lafrum11_message;
drop table if exists tl_lafrum11_tool_session;
create table tl_lafrum11_attachment (
   uuid bigint not null auto_increment,
   version_id bigint,
   type varchar(255),
   file_name varchar(255),
   forum bigint,
   primary key (uuid)
);
create table tl_lafrum11_forum (
   uuid bigint not null auto_increment,
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
   tool_content_id bigint,
   primary key (uuid)
);
create table tl_lafrum11_forum_learners (
   uuid bigint not null auto_increment,
   user_id bigint,
   status bit,
   session_id bigint,
   fullName varchar(255),
   userName varchar(255),
   primary key (uuid)
);
create table tl_lafrum11_message (
   uuid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   modified_by bigint,
   subject varchar(255),
   body text,
   is_authored bit,
   is_anonymous bit,
   FORUM bigint,
   parent bigint,
   primary key (uuid)
);
create table tl_lafrum11_tool_session (
   uuid bigint not null auto_increment,
   sessionEndDate datetime,
   sessionStartDate datetime,
   status integer,
   toolContentId bigint,
   toolSessionId bigint,
   primary key (uuid)
);
alter table tl_lafrum11_attachment add index FK389AD9A29EAD680D (forum), add constraint FK389AD9A29EAD680D foreign key (forum) references tl_lafrum11_forum (uuid);
alter table tl_lafrum11_message add index FK4A6067E8F7440FBC (parent), add constraint FK4A6067E8F7440FBC foreign key (parent) references tl_lafrum11_message (uuid);
alter table tl_lafrum11_message add index FK4A6067E89EAD680D (FORUM), add constraint FK4A6067E89EAD680D foreign key (FORUM) references tl_lafrum11_forum (uuid);
