SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_ladaco10_attachment;
drop table if exists tl_ladaco10_contents;
drop table if exists tl_ladaco10_questions;
drop table if exists tl_ladaco10_answer_options;
drop table if exists tl_ladaco10_session;
drop table if exists tl_ladaco10_user;
drop table if exists tl_ladaco10_answers;
create table tl_ladaco10_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   content_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_ladaco10_contents (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   lock_on_finished tinyint,
	min_records smallint,
	max_records smallint,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint, 
   primary key (uid)
)type=innodb;
create table tl_ladaco10_questions (
   uid bigint not null auto_increment,
   description text,
   organization_xml text,
   create_by bigint,
   create_date datetime,
   create_by_author tinyint,
   is_hide tinyint,
   is_required tinyint,
   question_type smallint,
   min_constraint float,
	max_constraint float,
	digits_decimal smallint,
	summary smallint,
   content_uid bigint,
   session_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_ladaco10_answer_options 
(uid bigint not null auto_increment,
question_uid bigint,
sequence_num smallint unsigned not null,
answer_option varchar(255),
primary key (uid))
type=innodb;

create table tl_ladaco10_question_log (
   uid bigint not null auto_increment,
   access_date datetime,
   question_uid bigint,
   user_uid bigint,
   complete tinyint,
   session_id bigint,
   primary key (uid)
)type=innodb;
create table tl_ladaco10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   content_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)type=innodb;
create table tl_ladaco10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   content_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_ladaco10_answers 
(uid bigint not null auto_increment,
user_uid bigint,
question_uid bigint,
record_id smallint unsigned not null,
answer text,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   file_version_id bigint,
primary key (uid)) type=innodb;

alter table tl_ladaco10_attachment add index FK_NEW_1728979407_1E7009430E79035 (content_uid), add constraint FK_NEW_1728979407_1E7009430E79035 foreign key (content_uid) references tl_ladaco10_contents (uid);
alter table tl_ladaco10_contents add index FK_NEW_1728979407_89093BF758092FB (create_by), add constraint FK_NEW_1728979407_89093BF758092FB foreign key (create_by) references tl_ladaco10_user (uid);
alter table tl_ladaco10_questions add index FK_NEW_1728979407_F52D1F93758092FB (create_by), add constraint FK_NEW_1728979407_F52D1F93758092FB foreign key (create_by) references tl_ladaco10_user (uid);
alter table tl_ladaco10_questions add index FK_NEW_1728979407_F52D1F9330E79035 (content_uid), add constraint FK_NEW_1728979407_F52D1F9330E79035 foreign key (content_uid) references tl_ladaco10_contents (uid);
alter table tl_ladaco10_questions add index FK_NEW_1728979407_F52D1F93EC0D3147 (session_uid), add constraint FK_NEW_1728979407_F52D1F93EC0D3147 foreign key (session_uid) references tl_ladaco10_session (uid);
alter table tl_ladaco10_answer_options add index FK_NEW_1728979407_30113BFCEC0D1113 (question_uid), add constraint FK_NEW_1728979407_30113BFCEC0D1113 foreign key (question_uid) references tl_ladaco10_questions (uid);
alter table tl_ladaco10_question_log add index FK_NEW_1728979407_693580A438BF8DFE (question_uid), add constraint FK_NEW_1728979407_693580A438BF8DFE foreign key (question_uid) references tl_ladaco10_questions (uid);
alter table tl_ladaco10_question_log add index FK_NEW_1728979407_693580A441F9365D (user_uid), add constraint FK_NEW_1728979407_693580A441F9365D foreign key (user_uid) references tl_ladaco10_user (uid);
alter table tl_ladaco10_session add index FK_NEW_1728979407_24AA78C530E79035 (content_uid), add constraint FK_NEW_1728979407_24AA78C530E79035 foreign key (content_uid) references tl_ladaco10_contents (uid);
alter table tl_ladaco10_user add index FK_NEW_1728979407_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1728979407_30113BFCEC0D3147 foreign key (session_uid) references tl_ladaco10_session (uid);
alter table tl_ladaco10_user add index FK_NEW_1728979407_30113BFC309ED320 (content_uid), add constraint FK_NEW_1728979407_30113BFC309ED320 foreign key (content_uid) references tl_ladaco10_contents (uid);
alter table tl_ladaco10_answers add index FK_NEW_1728979407_30113BFCEC0D1111 (user_uid), add constraint FK_NEW_1728979407_30113BFCEC0D1111 foreign key (user_uid) references tl_ladaco10_user (uid);
alter table tl_ladaco10_answers add index FK_NEW_1728979407_30113BFCEC0D1112 (question_uid), add constraint FK_NEW_1728979407_30113BFCEC0D1112 foreign key (question_uid) references tl_ladaco10_questions (uid);

INSERT INTO `tl_ladaco10_contents` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, `lock_on_finished`,`min_records`,`max_records`, `instructions`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`,`reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'Daco','0','0',0,0,'Instructions ',null,null,0,0,${default_content_id},0);
  
INSERT INTO `tl_ladaco10_questions` (`uid`, `description`, `organization_xml`, `create_by`, `create_date`, `create_by_author`, `is_hide`, `is_required`, `question_type`, `min_constraint`, `max_constraint`,`digits_decimal`,`summary`, `content_uid`, `session_uid`) VALUES 
  (1,'<div>What is your favourite colour?</div>',NULL,null,NOW(),1,0,0,1,NULL,NULL,NULL,NULL,1,NULL);
    
SET FOREIGN_KEY_CHECKS=1;