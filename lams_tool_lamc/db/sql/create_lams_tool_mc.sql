SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tl_lamc11_configuration;

CREATE TABLE tl_lamc11_configuration (
  config_key varchar(30) NOT NULL,
  config_value varchar(255) DEFAULT NULL,
  PRIMARY KEY (config_key)
);

DROP TABLE IF EXISTS tl_lamc11_content;

CREATE TABLE tl_lamc11_content (
  uid bigint NOT NULL AUTO_INCREMENT,
  content_id bigint NOT NULL,
  title text,
  instructions mediumtext,
  creation_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  reflect tinyint(1) NOT NULL DEFAULT '0',
  questions_sequenced tinyint(1) NOT NULL DEFAULT '0',
  created_by bigint NOT NULL DEFAULT '0',
  define_later tinyint(1) NOT NULL DEFAULT '0',
  retries tinyint(1) NOT NULL DEFAULT '0',
  pass_mark int DEFAULT NULL,
  show_report tinyint(1) NOT NULL DEFAULT '0',
  reflectionSubject mediumtext,
  showMarks tinyint(1) NOT NULL DEFAULT '0',
  randomize tinyint(1) NOT NULL DEFAULT '0',
  displayAnswers tinyint(1) NOT NULL DEFAULT '1',
  submission_deadline datetime DEFAULT NULL,
  use_select_leader_tool_ouput tinyint(1) NOT NULL DEFAULT '0',
  prefix_answers_with_letters tinyint(1) NOT NULL DEFAULT '1',
  enable_confidence_levels tinyint(1) NOT NULL DEFAULT '0',
  display_feedback_only tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (uid),
  UNIQUE KEY UQ_tl_lamc11_content_1 (content_id)
);

DROP TABLE IF EXISTS tl_lamc11_que_content;

CREATE TABLE tl_lamc11_que_content (
  uid bigint NOT NULL AUTO_INCREMENT,
  mc_content_id bigint NOT NULL,
  PRIMARY KEY (uid),
  KEY mc_content_id (mc_content_id),
  CONSTRAINT FK_tl_lamc11_que_content_1 FOREIGN KEY (mc_content_id) REFERENCES tl_lamc11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lamc11_que_usr;

CREATE TABLE tl_lamc11_que_usr (
  uid bigint NOT NULL AUTO_INCREMENT,
  que_usr_id bigint NOT NULL,
  mc_session_id bigint NOT NULL,
  username varchar(255) DEFAULT NULL,
  fullname varchar(255) DEFAULT NULL,
  responseFinalised tinyint(1) NOT NULL DEFAULT '0',
  viewSummaryRequested tinyint(1) NOT NULL DEFAULT '0',
  last_attempt_total_mark int DEFAULT NULL,
  number_attempts int DEFAULT '0',
  PRIMARY KEY (uid),
  UNIQUE KEY que_usr_id (que_usr_id,mc_session_id),
  KEY mc_session_id (mc_session_id),
  CONSTRAINT FK_tl_lamc11_que_usr_1 FOREIGN KEY (mc_session_id) REFERENCES tl_lamc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lamc11_session;

CREATE TABLE tl_lamc11_session (
  uid bigint NOT NULL AUTO_INCREMENT,
  mc_session_id bigint NOT NULL,
  session_start_date datetime DEFAULT NULL,
  session_end_date datetime DEFAULT NULL,
  session_name varchar(100) DEFAULT NULL,
  session_status varchar(100) DEFAULT NULL,
  mc_content_id bigint NOT NULL,
  mc_group_leader_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY UQ_tl_lamc11_session_1 (mc_session_id),
  KEY mc_content_id (mc_content_id),
  KEY FK_lamc11_session1 (mc_group_leader_uid),
  CONSTRAINT FK_lamc11_session1 FOREIGN KEY (mc_group_leader_uid) REFERENCES tl_lamc11_que_usr (uid) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT FK_tl_lamc_session_1 FOREIGN KEY (mc_content_id) REFERENCES tl_lamc11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_lamc11_usr_attempt;

CREATE TABLE tl_lamc11_usr_attempt (
  uid bigint NOT NULL AUTO_INCREMENT,
  qb_tool_question_uid bigint NOT NULL,
  que_usr_id bigint NOT NULL,
  attempt_time datetime DEFAULT NULL,
  isAttemptCorrect tinyint(1) NOT NULL DEFAULT '0',
  mark varchar(255) DEFAULT NULL,
  passed tinyint(1) NOT NULL DEFAULT '0',
  confidence_level int NOT NULL DEFAULT '0',
  PRIMARY KEY (uid),
  UNIQUE KEY IDX_attempt_duplicate_prevent (qb_tool_question_uid,que_usr_id),
  KEY que_usr_id (que_usr_id),
  CONSTRAINT FK_tl_lamc11_usr_attempt_1 FOREIGN KEY (uid) REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_lamc11_usr_attempt_4 FOREIGN KEY (que_usr_id) REFERENCES tl_lamc11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO tl_lamc11_configuration VALUES ('hideTitles','false');

INSERT INTO tl_lamc11_content(uid, content_id , title, instructions, creation_date , reflect, questions_sequenced, created_by , define_later, retries, show_report, pass_mark) VALUES (1, ${default_content_id}, 'MCQ', 'Instructions', NOW(), 0, 0, 1, 0, 0, 0, 0);
 
INSERT INTO lams_qb_tool_question (qb_question_uid, tool_content_id, answer_required, display_order) VALUES (1, ${default_content_id},0,1);

INSERT INTO tl_lamc11_que_content VALUES (LAST_INSERT_ID(),1);

SET FOREIGN_KEY_CHECKS=1;