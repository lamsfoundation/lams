SET FOREIGN_KEY_CHECKS=0;

create table tl_lafrum11_forum (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   allow_anonym tinyint(1),
   lock_on_finished tinyint(1),
   instructions MEDIUMTEXT,
   content_in_use tinyint(1),
   define_later tinyint(1),
   content_id bigint,
   allow_edit tinyint(1),
   allow_rich_editor tinyint(1),
   allow_new_topic tinyint(1), 
   allow_upload tinyint(1),
   allow_rate_messages tinyint(1),
   maximum_reply integer, 
   minimum_reply integer,
   limited_of_chars integer,
   limited_input_flag tinyint(1),
   reflect_instructions MEDIUMTEXT,
   reflect_on_activity tinyint(1), 
   notify_learners_on_forum_posting tinyint(1) DEFAULT 0,
   notify_teachers_on_forum_posting tinyint(1) DEFAULT 0,
   mark_release_notify tinyint(1) DEFAULT 0,
   submission_deadline datetime,
   minimum_rate int(11) DEFAULT 0,
   maximum_rate int(11) DEFAULT 0,
   min_characters int(11) DEFAULT 0,
   limited_min_characters tinyint(1) DEFAULT 0,
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_lafrum11_tool_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   mark_released tinyint(1),
   forum_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid),
   UNIQUE KEY (session_id),
   CONSTRAINT FK5A04D7AE131CE31E FOREIGN KEY (forum_uid)
   		REFERENCES tl_lafrum11_forum (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lafrum11_forum_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   session_id bigint,
   login_name varchar(255),
   session_finished tinyint(1),
   primary key (uid),
   KEY idx_user_id (user_id),
   CONSTRAINT FK7B83A4A85F0116B6 FOREIGN KEY (session_id)
   		REFERENCES tl_lafrum11_tool_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lafrum11_report (
   uid bigint not null auto_increment,
   comment MEDIUMTEXT,
   mark float,
   primary key (uid)
);

create table tl_lafrum11_message (
   uid bigint not null auto_increment,
   create_date datetime,
   last_reply_date datetime,
   update_date datetime,
   create_by bigint,
   modified_by bigint,
   subject varchar(255),
   body MEDIUMTEXT,
   sequence_id integer,
   is_authored tinyint(1),
   is_anonymous tinyint(1),
   forum_session_uid bigint,
   parent_uid bigint,
   forum_uid bigint,
   reply_number integer,
   hide_flag tinyint(1),
   report_id bigint,
   authored_parent_uid bigint,
   is_monitor tinyint(1) DEFAULT 0,
   PRIMARY KEY (uid),
   CONSTRAINT FK4A6067E8131CE31E FOREIGN KEY (forum_uid)
   		REFERENCES tl_lafrum11_forum (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK4A6067E8647A7264 FOREIGN KEY (modified_by)
   		REFERENCES tl_lafrum11_forum_user (uid) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT FK4A6067E89357B45B FOREIGN KEY (forum_session_uid)
   		REFERENCES tl_lafrum11_tool_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK4A6067E897F0DB46 FOREIGN KEY (report_id)
   		REFERENCES tl_lafrum11_report (uid),
   CONSTRAINT FK4A6067E8E42F4351 FOREIGN KEY (create_by)
   		REFERENCES tl_lafrum11_forum_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

create table tl_lafrum11_message_seq (
   uid bigint not null auto_increment,
   root_message_uid bigint,
   message_uid bigint,
   message_level smallint,
   thread_message_uid bigint(20),
   PRIMARY KEY (uid),
   CONSTRAINT FKD2C71F8845213B4D FOREIGN KEY (root_message_uid)
   		REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FKD2C71F88FE939F2A FOREIGN KEY (message_uid)
   		REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT fkfrum11mseqthread FOREIGN KEY (thread_message_uid)
   		REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

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
                  REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE   ON UPDATE CASCADE
);

CREATE TABLE tl_lafrum11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT ForumConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionToForum FOREIGN KEY (content_uid)
                  REFERENCES tl_lafrum11_forum(uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lafrum11_condition_topics (
 	   condition_id BIGINT(20)
 	 , topic_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,topic_uid)
	 , CONSTRAINT ForumConditionQuestionToForumCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_lafrum11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionQuestionToForumQuestion FOREIGN KEY (topic_uid)
                  REFERENCES tl_lafrum11_message(uid) ON DELETE CASCADE ON UPDATE CASCADE	
);

create table tl_lafrum11_timestamp (
  uid BIGINT(20) not null auto_increment,
  message_uid BIGINT(20) not null,
  timestamp_date DATETIME not null,
  forum_user_uid BIGINT(20) not null,
  primary key (uid),
  CONSTRAINT ForumUserFK FOREIGN KEY (forum_user_uid)
  		REFERENCES tl_lafrum11_forum_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT MessageFK FOREIGN KEY (message_uid)
  		REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lafrum11_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   forum_uid bigint,
   message_uid bigint,
   primary key (uid),
   CONSTRAINT FK389AD9A2131CE31E FOREIGN KEY (forum_uid)
   		REFERENCES tl_lafrum11_forum (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK389AD9A2FE939F2A FOREIGN KEY (message_uid)
   		REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_lafrum11_forum ADD CONSTRAINT FK87917942E42F4351 FOREIGN KEY (create_by)
  		REFERENCES tl_lafrum11_forum_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_lafrum11_message ADD CONSTRAINT FK4A6067E824089E4D FOREIGN KEY (parent_uid)
   		REFERENCES tl_lafrum11_message (uid) ON DELETE CASCADE ON UPDATE CASCADE;

INSERT INTO tl_lafrum11_forum (uid,title,instructions,content_id,allow_anonym,lock_on_finished,content_in_use,define_later,allow_edit,allow_rich_editor,
 allow_new_topic,allow_upload,allow_rate_messages,maximum_reply, minimum_reply,limited_input_flag,limited_of_chars,notify_learners_on_forum_posting,notify_teachers_on_forum_posting,reflect_on_activity) 
VALUES(1,"Forum","Instructions",${default_content_id},0,0,0,0,1,0,1,0,0,1,0,1,5000,0,0,0);

INSERT INTO tl_lafrum11_message (uid, create_date, last_reply_date, update_date, subject, body,
								   sequence_id, is_authored, is_anonymous, forum_uid, reply_number, hide_flag)
VALUES (1,NOW(),NOW(),NOW(),'Topic Heading','Topic message',1,1,0,1,0,0);

CREATE TABLE tl_lafrum11_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(30),
  config_value VARCHAR(255),
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

INSERT INTO tl_lafrum11_configuration (config_key, config_value) VALUES ('keepLearnerContent',	'false');

SET FOREIGN_KEY_CHECKS=1;