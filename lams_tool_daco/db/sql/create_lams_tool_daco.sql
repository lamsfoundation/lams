SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_ladaco10_contents (
   uid bigint NOT NULL UNIQUE auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   lock_on_finished tinyint(1) DEFAULT 0,
   min_records tinyint unsigned DEFAULT 0,
   max_records tinyint unsigned DEFAULT 0,
   instructions MEDIUMTEXT,
   content_in_use tinyint(1) DEFAULT 0,
   define_later tinyint(1) DEFAULT 0,
   content_id bigint,
   reflect_instructions MEDIUMTEXT, 
   reflect_on_activity tinyint(1),
   learner_entry_notify tinyint(1) DEFAULT 0,
   record_submit_notify tinyint(1) DEFAULT 0,
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

CREATE TABLE tl_ladaco10_sessions (
   uid bigint NOT NULL UNIQUE auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   content_uid bigint,
   session_id bigint,
   session_name varchar(250),
   PRIMARY KEY (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT SessionToDaco FOREIGN KEY (content_uid)
   		REFERENCES tl_ladaco10_contents (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_ladaco10_questions (
   uid bigint NOT NULL UNIQUE auto_increment,
   description MEDIUMTEXT,
   create_by bigint,
   create_date datetime ,
   is_required tinyint(1) DEFAULT 0,
   question_type tinyint unsigned,
   min_constraint float,
   max_constraint float,
   digits_decimal tinyint unsigned,
   summary tinyint unsigned,
   content_uid bigint,
   session_uid bigint,
   PRIMARY KEY (uid),
   CONSTRAINT QuestionToDaco FOREIGN KEY (content_uid)
   		REFERENCES tl_ladaco10_contents (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT QuestionToUser FOREIGN KEY (create_by)
   		REFERENCES tl_ladaco10_users (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT tl_ladaco10_questions_ibfk_1 FOREIGN KEY (session_uid)
   		REFERENCES tl_ladaco10_sessions (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_ladaco10_answer_options (
	uid bigint NOT NULL UNIQUE auto_increment,
	question_uid bigint,
	sequence_num tinyint unsigned DEFAULT 1,
	answer_option varchar(255),
	PRIMARY KEY (uid),
	CONSTRAINT tl_ladaco10_answer_options_ibfk_1 FOREIGN KEY (question_uid)
		REFERENCES tl_ladaco10_questions (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_ladaco10_users (
   uid bigint NOT NULL auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   content_uid bigint,
   PRIMARY KEY (uid),
   CONSTRAINT UserToDaco FOREIGN KEY (content_uid)
   		REFERENCES tl_ladaco10_contents (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT UserToSession FOREIGN KEY (session_uid)
   		REFERENCES tl_ladaco10_sessions (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_ladaco10_answers (
   uid bigint NOT NULL UNIQUE auto_increment,
   user_uid bigint,
   question_uid bigint,
   record_id smallint unsigned,
   answer MEDIUMTEXT,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   file_version_id bigint,
   create_date datetime,
   PRIMARY KEY (uid),
   CONSTRAINT AnswerToQuestion FOREIGN KEY (question_uid)
   		REFERENCES tl_ladaco10_questions (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT AnswerToUser FOREIGN KEY (user_uid)
   		REFERENCES tl_ladaco10_users (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_ladaco10_contents ADD CONSTRAINT DacoToUser FOREIGN KEY (create_by)
   		REFERENCES tl_ladaco10_users (uid) ON DELETE SET NULL ON UPDATE CASCADE;


INSERT INTO tl_ladaco10_contents (uid, title, lock_on_finished,min_records,max_records, instructions,
									content_in_use, define_later, content_id,reflect_on_activity) VALUES
  (1,'Data Collection',0,0,0,'Instructions',0,0,${default_content_id},0);
  
INSERT INTO tl_ladaco10_questions (uid, description, create_date, is_required, question_type, content_uid) VALUES 
  (NULL,'<div>What is your favourite colour?</div>',NOW(),0,1,1);

SET FOREIGN_KEY_CHECKS=1;