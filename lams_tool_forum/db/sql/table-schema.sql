alter table tl_lafrum11_attachment drop foreign key FK389AD9A2290DA510;
alter table tl_lafrum11_attachment drop foreign key FK389AD9A29EAD680D;
alter table tl_lafrum11_message drop foreign key FK4A6067E837026725;
alter table tl_lafrum11_message drop foreign key FK4A6067E8F7440FBC;
alter table tl_lafrum11_message drop foreign key FK4A6067E823DF1BD4;
alter table tl_lafrum11_tool_session drop foreign key FK5A04D7AE9912CEA7;
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
   toolSession bigint,
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
   primary key (uuid)
);
create table tl_lafrum11_forum_learners (
   uuid bigint not null auto_increment,
   user_id bigint,
   status bit,
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
   tool_session_id bigint,
   learner_id bigint,
   parent bigint,
   primary key (uuid)
);
create table tl_lafrum11_tool_session (
   uuid bigint not null auto_increment,
   sessionEndDate datetime,
   sessionStartDate datetime,
   status integer,
   primary key (uuid)
);
alter table tl_lafrum11_attachment add index FK389AD9A2290DA510 (toolSession), add constraint FK389AD9A2290DA510 foreign key (toolSession) references tl_lafrum11_message (uuid);
alter table tl_lafrum11_attachment add index FK389AD9A29EAD680D (forum), add constraint FK389AD9A29EAD680D foreign key (forum) references tl_lafrum11_forum (uuid);
alter table tl_lafrum11_message add index FK4A6067E837026725 (learner_id), add constraint FK4A6067E837026725 foreign key (learner_id) references tl_lafrum11_forum_learners (uuid);
alter table tl_lafrum11_message add index FK4A6067E8F7440FBC (parent), add constraint FK4A6067E8F7440FBC foreign key (parent) references tl_lafrum11_message (uuid);
alter table tl_lafrum11_message add index FK4A6067E823DF1BD4 (tool_session_id), add constraint FK4A6067E823DF1BD4 foreign key (tool_session_id) references tl_lafrum11_tool_session (uuid);
alter table tl_lafrum11_tool_session add index FK5A04D7AE9912CEA7 (uuid), add constraint FK5A04D7AE9912CEA7 foreign key (uuid) references tl_lafrum11_forum (uuid);
