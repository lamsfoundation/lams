

SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lafrum11_attachment;
drop table if exists tl_lafrum11_forum;
drop table if exists tl_lafrum11_forum_user;
drop table if exists tl_lafrum11_message;
drop table if exists tl_lafrum11_message_seq;
drop table if exists tl_lafrum11_report;
drop table if exists tl_lafrum11_message_rating;
drop table if exists tl_lafrum11_tool_session;
drop table if exists tl_lafrum11_timestamp;
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
)ENGINE=InnoDB;
create table tl_lafrum11_forum (
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
   allow_rate_messages smallint,
   maximum_reply integer, 
   minimum_reply integer,
   limited_of_chars integer,
   limited_input_flag smallint,
   reflect_instructions varchar(255),
   reflect_on_activity smallint, 
   notify_learners_on_forum_posting tinyint DEFAULT 0,
   notify_teachers_on_forum_posting tinyint DEFAULT 0,
   mark_release_notify tinyint DEFAULT 0,
   submission_deadline datetime,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lafrum11_forum_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   session_id bigint,
   login_name varchar(255),
   session_finished smallint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lafrum11_message (
   uid bigint not null auto_increment,
   create_date datetime,
   last_reply_date datetime,
   update_date datetime,
   create_by bigint,
   modified_by bigint,
   subject varchar(255),
   body text,
   sequence_id integer,
   is_authored smallint,
   is_anonymous smallint,
   forum_session_uid bigint,
   parent_uid bigint,
   forum_uid bigint,
   reply_number integer,
   hide_flag smallint,
   report_id bigint,
   authored_parent_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lafrum11_message_seq (
   uid bigint not null auto_increment,
   root_message_uid bigint,
   message_uid bigint,
   message_level smallint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lafrum11_report (
   uid bigint not null auto_increment,
   comment text,
   release_date datetime,
   mark float,
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_lafrum11_message_rating (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating float
     , user_id BIGINT(20) NOT NULL
     , message_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (user_id)
     , CONSTRAINT FK_tl_lafrum11_message_rating_1 FOREIGN KEY (user_id)
                  REFERENCES tl_lafrum11_forum_user (uid)
     , INDEX (message_id)
     , CONSTRAINT FK_tl_lafrum11_message_rating_2 FOREIGN KEY (message_id)
                  REFERENCES tl_lafrum11_message (uid)
)ENGINE=InnoDB;
create table tl_lafrum11_tool_session (
   uid bigint not null auto_increment,
   version integer not null,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   mark_released smallint,
   forum_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)ENGINE=InnoDB;
CREATE TABLE tl_lafrum11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT ForumConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionToForum FOREIGN KEY (content_uid)
                  REFERENCES tl_lafrum11_forum(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE tl_lafrum11_condition_topics (
 	   condition_id BIGINT(20)
 	 , topic_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,topic_uid)
	 , CONSTRAINT ForumConditionQuestionToForumCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_lafrum11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionQuestionToForumQuestion FOREIGN KEY (topic_uid)
                  REFERENCES tl_lafrum11_message(uid) ON DELETE CASCADE ON UPDATE CASCADE	
)ENGINE=InnoDB;
create table tl_lafrum11_timestamp (
  uid BIGINT(20) not null auto_increment,
  message_uid BIGINT(20) not null,
  timestamp_date DATETIME not null,
  forum_user_uid BIGINT(20) not null,
  primary key (`uid`),
  unique key `uid` (`uid`),
  key `message_uid` (`message_uid`),
  key `forum_user_uid` (`forum_user_uid`)
)ENGINE=InnoDB;

alter table tl_lafrum11_attachment add index FK389AD9A2FE939F2A (message_uid), add constraint FK389AD9A2FE939F2A foreign key (message_uid) references tl_lafrum11_message (uid);
alter table tl_lafrum11_attachment add index FK389AD9A2131CE31E (forum_uid), add constraint FK389AD9A2131CE31E foreign key (forum_uid) references tl_lafrum11_forum (uid);
alter table tl_lafrum11_forum add index FK87917942E42F4351 (create_by), add constraint FK87917942E42F4351 foreign key (create_by) references tl_lafrum11_forum_user (uid);
alter table tl_lafrum11_forum_user add index FK7B83A4A85F0116B6 (session_id), add constraint FK7B83A4A85F0116B6 foreign key (session_id) references tl_lafrum11_tool_session (uid);
alter table tl_lafrum11_message add index FK4A6067E8E42F4351 (create_by), add constraint FK4A6067E8E42F4351 foreign key (create_by) references tl_lafrum11_forum_user (uid);
alter table tl_lafrum11_message add index FK4A6067E897F0DB46 (report_id), add constraint FK4A6067E897F0DB46 foreign key (report_id) references tl_lafrum11_report (uid);
alter table tl_lafrum11_message add index FK4A6067E8131CE31E (forum_uid), add constraint FK4A6067E8131CE31E foreign key (forum_uid) references tl_lafrum11_forum (uid);
alter table tl_lafrum11_message add index FK4A6067E824089E4D (parent_uid), add constraint FK4A6067E824089E4D foreign key (parent_uid) references tl_lafrum11_message (uid);
alter table tl_lafrum11_message add index FK4A6067E89357B45B (forum_session_uid), add constraint FK4A6067E89357B45B foreign key (forum_session_uid) references tl_lafrum11_tool_session (uid);
alter table tl_lafrum11_message add index FK4A6067E8647A7264 (modified_by), add constraint FK4A6067E8647A7264 foreign key (modified_by) references tl_lafrum11_forum_user (uid);
alter table tl_lafrum11_message add index IX_msg_auth_parent (authored_parent_uid);
alter table tl_lafrum11_message_seq add index FKD2C71F88FE939F2A (message_uid), add constraint FKD2C71F88FE939F2A foreign key (message_uid) references tl_lafrum11_message (uid);
alter table tl_lafrum11_message_seq add index FKD2C71F8845213B4D (root_message_uid), add constraint FKD2C71F8845213B4D foreign key (root_message_uid) references tl_lafrum11_message (uid);
alter table tl_lafrum11_tool_session add index FK5A04D7AE131CE31E (forum_uid), add constraint FK5A04D7AE131CE31E foreign key (forum_uid) references tl_lafrum11_forum (uid);
alter table tl_lafrum11_timestamp add index ForumUserFK (forum_user_uid), add constraint ForumUserFK foreign key (forum_user_uid) references tl_lafrum11_forum_user (uid);
alter table tl_lafrum11_timestamp add index MessageFK (message_uid), add constraint MessageFK foreign key (message_uid) references tl_lafrum11_message (uid);

INSERT INTO tl_lafrum11_forum (uid,title,instructions,online_instructions,offline_instructions,content_id,allow_anonym,run_offline,lock_on_finished,content_in_use,define_later,allow_edit,allow_rich_editor,
 allow_new_topic,allow_upload,allow_rate_messages,maximum_reply, minimum_reply,limited_input_flag,limited_of_chars,notify_learners_on_forum_posting,notify_teachers_on_forum_posting,reflect_on_activity) 
VALUES(1,"Forum","Instructions",null,null,${default_content_id},0,0,0,0,0,1,0,1,0,0,1,0,1,5000,0,0,0);

INSERT INTO `tl_lafrum11_message` (`uid`, `create_date`, `last_reply_date`, `update_date`, `create_by`, `modified_by`, `subject`, `body`, `sequence_id`, `is_authored`, `is_anonymous`, `forum_session_uid`, `parent_uid`, `forum_uid`, `reply_number`, `hide_flag`, `report_id`) VALUES 
  (1,NOW(),NOW(),NOW(),null,null,'Topic Heading','Topic message',1,1,0,NULL,NULL,1,0,0,NULL);

SET FOREIGN_KEY_CHECKS=1;