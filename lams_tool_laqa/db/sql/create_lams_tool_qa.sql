SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tl_laqa11_condition_questions;

CREATE TABLE tl_laqa11_condition_questions (
  condition_id bigint NOT NULL,
  question_uid bigint NOT NULL,
  PRIMARY KEY (condition_id,question_uid),
  KEY QaConditionQuestionToQaQuestion (question_uid),
  CONSTRAINT QaConditionQuestionToQaCondition FOREIGN KEY (condition_id) REFERENCES tl_laqa11_conditions (condition_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT QaConditionQuestionToQaQuestion FOREIGN KEY (question_uid) REFERENCES tl_laqa11_que_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laqa11_conditions;

CREATE TABLE tl_laqa11_conditions (
  condition_id bigint NOT NULL,
  content_uid bigint DEFAULT NULL,
  PRIMARY KEY (condition_id),
  KEY QaConditionToQaContent (content_uid),
  CONSTRAINT QaConditionInheritance FOREIGN KEY (condition_id) REFERENCES lams_branch_condition (condition_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT QaConditionToQaContent FOREIGN KEY (content_uid) REFERENCES tl_laqa11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laqa11_content;

CREATE TABLE tl_laqa11_content (
  uid bigint NOT NULL AUTO_INCREMENT,
  qa_content_id bigint NOT NULL,
  title text,
  instructions mediumtext,
  creation_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  submission_deadline datetime DEFAULT NULL,
  reflect tinyint(1) NOT NULL DEFAULT '0',
  questions_sequenced tinyint(1) NOT NULL DEFAULT '0',
  username_visible tinyint(1) NOT NULL DEFAULT '0',
  allow_rate_answers tinyint(1) NOT NULL DEFAULT '0',
  created_by bigint NOT NULL DEFAULT '0',
  define_later tinyint(1) NOT NULL DEFAULT '0',
  reflectionSubject mediumtext,
  lockWhenFinished tinyint(1) NOT NULL DEFAULT '1',
  showOtherAnswers tinyint(1) NOT NULL DEFAULT '1',
  allow_rich_editor tinyint(1) NOT NULL DEFAULT '0',
  use_select_leader_tool_ouput tinyint(1) NOT NULL DEFAULT '0',
  notify_response_submit tinyint(1) NOT NULL DEFAULT '0',
  minimum_rates int DEFAULT '0',
  maximum_rates int DEFAULT '0',
  no_reedit_allowed tinyint(1) NOT NULL DEFAULT '0',
  show_other_answers_after_deadline tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (uid),
  KEY tl_laqa11_content_qa_content_id (qa_content_id)
);

DROP TABLE IF EXISTS tl_laqa11_que_content;

CREATE TABLE tl_laqa11_que_content (
  uid bigint NOT NULL AUTO_INCREMENT,
  qa_content_id bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY qa_content_id (qa_content_id),
  CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (qa_content_id) REFERENCES tl_laqa11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laqa11_que_usr;

CREATE TABLE tl_laqa11_que_usr (
  uid bigint NOT NULL AUTO_INCREMENT,
  que_usr_id bigint NOT NULL,
  username varchar(255) DEFAULT NULL,
  responseFinalized tinyint(1) NOT NULL DEFAULT '0',
  qa_session_id bigint NOT NULL,
  fullname varchar(255) DEFAULT NULL,
  learnerFinished tinyint(1) NOT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY que_usr_id (que_usr_id,qa_session_id),
  KEY qa_session_id (qa_session_id),
  CONSTRAINT FK_tl_laqa11_que_usr_1 FOREIGN KEY (qa_session_id) REFERENCES tl_laqa11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laqa11_session;

CREATE TABLE tl_laqa11_session (
  uid bigint NOT NULL AUTO_INCREMENT,
  qa_session_id bigint NOT NULL,
  session_start_date datetime DEFAULT NULL,
  session_end_date datetime DEFAULT NULL,
  session_name varchar(100) DEFAULT NULL,
  session_status varchar(100) DEFAULT NULL,
  qa_content_id bigint NOT NULL,
  qa_group_leader_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY qa_session_id (qa_session_id),
  KEY qa_content_id (qa_content_id),
  KEY FK_laqa11_session1 (qa_group_leader_uid),
  CONSTRAINT FK_laqa11_session1 FOREIGN KEY (qa_group_leader_uid) REFERENCES tl_laqa11_que_usr (uid) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (qa_content_id) REFERENCES tl_laqa11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laqa11_usr_resp;

CREATE TABLE tl_laqa11_usr_resp (
  uid bigint NOT NULL AUTO_INCREMENT,
  time_zone varchar(255) DEFAULT NULL,
  attempt_time datetime DEFAULT NULL,
  que_usr_id bigint NOT NULL,
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  answer_autosaved mediumtext,
  PRIMARY KEY (uid),
  KEY que_usr_id (que_usr_id),
  CONSTRAINT FK_tl_laqa11_usr_resp_1 FOREIGN KEY (uid) REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laqa11_usr_resp_3 FOREIGN KEY (que_usr_id) REFERENCES tl_laqa11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

-- data for content table
INSERT INTO tl_laqa11_content (qa_content_id, title, instructions, creation_date, lockWhenFinished)  VALUES (${default_content_id}, 'Q&A', 'Instructions', NOW() , 0);

-- data for content questions table
 
INSERT INTO lams_qb_tool_question (qb_question_uid, tool_content_id, answer_required, display_order) VALUES (3, ${default_content_id},0,1);

INSERT INTO tl_laqa11_que_content VALUES (LAST_INSERT_ID(),1);

SET FOREIGN_KEY_CHECKS=1;