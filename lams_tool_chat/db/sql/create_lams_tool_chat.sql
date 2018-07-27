SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lachat11_chat (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  create_date datetime,
  update_date datetime,
  create_by bigint(20),
  title varchar(255),
  instructions mediumtext,
  lock_on_finished bit(1),
  reflect_on_activity bit(1),
  reflect_instructions mediumtext,
  content_in_use bit(1),
  define_later bit(1),
  tool_content_id bigint(20),
  filtering_enabled bit(1),
  filter_keywords mediumtext,
  submission_deadline datetime,
  PRIMARY KEY (uid)
);

CREATE TABLE tl_lachat11_session (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  session_end_date datetime,
  session_start_date datetime,
  status int(11),
  session_id bigint(20),
  session_name varchar(250),
  chat_uid bigint(20),
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  CONSTRAINT FK96E446B1A3926E3 FOREIGN KEY (chat_uid)
  		REFERENCES tl_lachat11_chat (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lachat11_user (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20),
  last_name varchar(255),
  login_name varchar(255),
  first_name varchar(255),
  finishedActivity bit(1),
  nickname varchar(255),
  chat_session_uid bigint(20),
  last_presence datetime,
  PRIMARY KEY (uid),
  CONSTRAINT FK4EB82169C8469FC FOREIGN KEY (chat_session_uid)
  		REFERENCES tl_lachat11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lachat11_message (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  chat_session_uid bigint(20) NOT NULL,
  from_user_uid bigint(20),
  to_user_uid bigint(20),
  type varchar(255),
  body mediumtext,
  send_date datetime,
  hidden bit(1),
  PRIMARY KEY (uid),
  CONSTRAINT FKCC08C1DC2AF61E05 FOREIGN KEY (to_user_uid)
  		REFERENCES tl_lachat11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FKCC08C1DC9C8469FC FOREIGN KEY (chat_session_uid)
  		REFERENCES tl_lachat11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FKCC08C1DCCF3BF9B6 FOREIGN KEY (from_user_uid)
  		REFERENCES tl_lachat11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lachat11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT ChatConditionInheritance FOREIGN KEY (condition_id)
           REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ChatConditionToChat FOREIGN KEY (content_uid)
          REFERENCES tl_lachat11_chat(uid) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO tl_lachat11_chat (
	title,
	instructions,
	tool_content_id,
	lock_on_finished,
	filtering_enabled,
	content_in_use,
	define_later,
	reflect_on_activity) 
VALUES(
	"Chat",
	"Instructions",
	${default_content_id},
	0,
	0,
	0,
	0,
	0
);

SET FOREIGN_KEY_CHECKS=1;