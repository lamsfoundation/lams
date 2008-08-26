SET FOREIGN_KEY_CHECKS=0;

DROP TABLE if exists tl_ladaco10_attachments cascade;
DROP TABLE if exists tl_ladaco10_contents cascade;
DROP TABLE if exists tl_ladaco10_questions cascade;
DROP TABLE if exists tl_ladaco10_answer_options cascade;
DROP TABLE if exists tl_ladaco10_sessions cascade;
DROP TABLE if exists tl_ladaco10_users cascade;
DROP TABLE if exists tl_ladaco10_answers cascade;

CREATE TABLE tl_ladaco10_contents (
   uid bigint NOT NULL UNIQUE auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint DEFAULT 0,
   lock_on_finished tinyint DEFAULT 0,
   min_records tinyint unsigned DEFAULT 0,
   max_records tinyint unsigned DEFAULT 0,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint DEFAULT 0,
   define_later tinyint DEFAULT 0,
   content_id bigint UNIQUE,
   reflect_instructions varchar(255), 
   reflect_on_activity tinyint,
   learner_entry_notify tinyint DEFAULT 0,
   record_submit_notify tinyint DEFAULT 0,
   PRIMARY KEY (uid)
)TYPE=innodb;

CREATE TABLE tl_ladaco10_sessions (
   uid bigint NOT NULL UNIQUE auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   content_uid bigint,
   session_id bigint UNIQUE,
   session_name varchar(250),
   PRIMARY KEY (uid)
)TYPE=innodb;

CREATE TABLE tl_ladaco10_attachments (
   uid bigint NOT NULL UNIQUE auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime ,
   content_uid bigint,
   PRIMARY KEY (uid)
)TYPE=innodb;

CREATE TABLE tl_ladaco10_questions (
   uid bigint NOT NULL UNIQUE auto_increment,
   description text,
   create_by bigint,
   create_date datetime ,
   is_required tinyint DEFAULT 0,
   question_type tinyint unsigned,
   min_constraint float,
   max_constraint float,
   digits_decimal tinyint unsigned,
   summary tinyint unsigned,
   content_uid bigint,
   session_uid bigint,
   PRIMARY KEY (uid)
)TYPE=innodb;

CREATE TABLE tl_ladaco10_answer_options 
(uid bigint NOT NULL UNIQUE auto_increment,
question_uid bigint,
sequence_num tinyint unsigned DEFAULT 1,
answer_option varchar(255),
PRIMARY KEY (uid))
TYPE=innodb;

CREATE TABLE tl_ladaco10_users (
   uid bigint NOT NULL auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   content_uid bigint,
   PRIMARY KEY (uid)
)TYPE=innodb;

CREATE TABLE tl_ladaco10_answers 
(uid bigint NOT NULL UNIQUE auto_increment,
user_uid bigint,
question_uid bigint,
record_id smallint unsigned,
answer text,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   file_version_id bigint,
PRIMARY KEY (uid)) TYPE=innodb;

ALTER TABLE tl_ladaco10_attachments ADD INDEX (content_uid), ADD CONSTRAINT  FOREIGN KEY (content_uid) REFERENCES tl_ladaco10_contents (uid);
ALTER TABLE tl_ladaco10_contents ADD INDEX  (create_by), ADD CONSTRAINT DacoToUser FOREIGN KEY (create_by) REFERENCES tl_ladaco10_users (uid);
ALTER TABLE tl_ladaco10_questions ADD INDEX  (create_by), ADD CONSTRAINT QuestionToUser FOREIGN KEY (create_by) REFERENCES tl_ladaco10_users (uid);
ALTER TABLE tl_ladaco10_questions ADD INDEX  (content_uid), ADD CONSTRAINT QuestionToDaco FOREIGN KEY (content_uid) REFERENCES tl_ladaco10_contents (uid);
ALTER TABLE tl_ladaco10_questions ADD INDEX  (session_uid), ADD CONSTRAINT  FOREIGN KEY (session_uid) REFERENCES tl_ladaco10_sessions (uid);
ALTER TABLE tl_ladaco10_answer_options ADD INDEX  (question_uid), ADD CONSTRAINT  FOREIGN KEY (question_uid) REFERENCES tl_ladaco10_questions (uid);
ALTER TABLE tl_ladaco10_sessions ADD INDEX  (content_uid), ADD CONSTRAINT SessionToDaco FOREIGN KEY (content_uid) REFERENCES tl_ladaco10_contents (uid);
ALTER TABLE tl_ladaco10_users ADD INDEX  (session_uid), ADD CONSTRAINT UserToSession FOREIGN KEY (session_uid) REFERENCES tl_ladaco10_sessions (uid);
ALTER TABLE tl_ladaco10_users ADD INDEX  (content_uid), ADD CONSTRAINT UserToDaco FOREIGN KEY (content_uid) REFERENCES tl_ladaco10_contents (uid);
ALTER TABLE tl_ladaco10_answers ADD INDEX  (user_uid), ADD CONSTRAINT AnswerToUser FOREIGN KEY (user_uid) REFERENCES tl_ladaco10_users (uid);
ALTER TABLE tl_ladaco10_answers ADD INDEX  (question_uid), ADD CONSTRAINT AnswerToQuestion FOREIGN KEY (question_uid) REFERENCES tl_ladaco10_questions (uid);

INSERT INTO `tl_ladaco10_contents` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, `lock_on_finished`,`min_records`,`max_records`, `instructions`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`,`reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'Data Collection',0,0,0,0,'Instructions',NULL,NULL,0,0,${default_content_id},0);
  
INSERT INTO `tl_ladaco10_questions` (`uid`, `description`, `create_by`, `create_date`, `is_required`, `question_type`, `min_constraint`, `max_constraint`,`digits_decimal`,`summary`, `content_uid`, `session_uid`) VALUES 
  (DEFAULT,'<div>What is your favourite colour?</div>',NULL,NOW(),0,1,NULL,NULL,NULL,NULL,1,NULL);

SET FOREIGN_KEY_CHECKS=1;