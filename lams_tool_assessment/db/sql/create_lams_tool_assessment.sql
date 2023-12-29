SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tl_laasse10_assessment;

CREATE TABLE tl_laasse10_assessment (
  uid bigint NOT NULL AUTO_INCREMENT,
  create_date datetime DEFAULT NULL,
  update_date datetime DEFAULT NULL,
  submission_deadline datetime DEFAULT NULL,
  create_by bigint DEFAULT NULL,
  title varchar(255) DEFAULT NULL,
  relative_time_limit smallint unsigned NOT NULL DEFAULT '0',
  absolute_time_limit datetime DEFAULT NULL,
  attempts_allowed int DEFAULT '1',
  passing_mark int DEFAULT '0',
  instructions mediumtext,
  define_later tinyint(1) DEFAULT NULL,
  content_id bigint DEFAULT NULL,
  allow_question_feedback tinyint(1) DEFAULT NULL,
  allow_overall_feedback tinyint(1) DEFAULT NULL,
  allow_disclose_answers tinyint(1) DEFAULT '0',
  allow_right_answers tinyint(1) DEFAULT NULL,
  allow_wrong_answers tinyint(1) DEFAULT NULL,
  allow_grades_after_attempt tinyint(1) DEFAULT NULL,
  allow_history_responses tinyint(1) DEFAULT NULL,
  allow_answer_justification tinyint DEFAULT '0',
  allow_discussion_sentiment tinyint DEFAULT '0',
  display_summary tinyint(1) DEFAULT NULL,
  display_max_mark TINYINT NOT NULL DEFAULT 0,
  questions_per_page int DEFAULT '0',
  shuffled tinyint(1) DEFAULT NULL,
  attempt_completion_notify tinyint(1) DEFAULT '0',
  reflect_on_activity bit(1) DEFAULT b'0',
  reflect_instructions mediumtext,
  numbered tinyint(1) DEFAULT '1',
  use_select_leader_tool_ouput tinyint(1) NOT NULL DEFAULT '0',
  question_etherpad_enabled tinyint(1) DEFAULT '0',
  enable_confidence_levels tinyint(1) NOT NULL DEFAULT '0',
  confidence_levels_type tinyint DEFAULT '1',
  PRIMARY KEY (uid),
  UNIQUE KEY content_id (content_id),
  KEY FK_NEW_1720029621_89093BF758092FB (create_by),
  CONSTRAINT FK_NEW_1720029621_89093BF758092FB FOREIGN KEY (create_by) REFERENCES tl_laasse10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_assessment_overall_feedback;

CREATE TABLE tl_laasse10_assessment_overall_feedback (
  uid bigint NOT NULL AUTO_INCREMENT,
  assessment_uid bigint DEFAULT NULL,
  sequence_id int DEFAULT NULL,
  grade_boundary int DEFAULT NULL,
  feedback mediumtext,
  PRIMARY KEY (uid),
  UNIQUE KEY uid (uid),
  KEY FK_tl_laasse10_assessment_overall_feedback_1 (assessment_uid),
  CONSTRAINT FK_tl_laasse10_assessment_overall_feedback_1 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS tl_laasse10_assessment_question;

CREATE TABLE tl_laasse10_assessment_question (
  uid bigint NOT NULL AUTO_INCREMENT,
  assessment_uid bigint DEFAULT NULL,
  correct_answers_disclosed tinyint(1) DEFAULT '0',
  groups_answers_disclosed tinyint(1) DEFAULT '0',
  random_question tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (uid),
  KEY FK_NEW_1720029621_F52D1F9330E79035 (assessment_uid),
  CONSTRAINT FK_NEW_1720029621_F52D1F9330E79035 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_assessment_result;

CREATE TABLE tl_laasse10_assessment_result (
  uid bigint NOT NULL AUTO_INCREMENT,
  assessment_uid bigint DEFAULT NULL,
  start_date datetime DEFAULT NULL,
  finish_date datetime DEFAULT NULL,
  user_uid bigint DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  maximum_grade int DEFAULT NULL,
  grade float DEFAULT NULL,
  latest tinyint(1) DEFAULT NULL,
  time_limit_launched_date datetime DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY UQ_tl_laasse10_assessment_result_5 (assessment_uid,user_uid,latest),
  KEY latest (latest),
  KEY FK_tl_laasse10_assessment_result_2 (user_uid),
  KEY FK_tl_laasse10_assessment_result_1 (session_id),
  CONSTRAINT FK_tl_laasse10_assessment_result_1 FOREIGN KEY (session_id) REFERENCES tl_laasse10_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laasse10_assessment_result_2 FOREIGN KEY (user_uid) REFERENCES tl_laasse10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laasse10_assessment_result_3 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_configuration;

CREATE TABLE tl_laasse10_configuration (
  config_key varchar(30) NOT NULL,
  config_value varchar(255) DEFAULT NULL,
  PRIMARY KEY (config_key)
);

DROP TABLE IF EXISTS tl_laasse10_option_answer;

CREATE TABLE tl_laasse10_option_answer (
  uid bigint NOT NULL AUTO_INCREMENT,
  question_result_uid bigint DEFAULT NULL,
  question_option_uid bigint DEFAULT NULL,
  answer_boolean tinyint(1) DEFAULT NULL,
  answer_int int DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY uid (uid),
  KEY answer_boolean (answer_boolean),
  KEY FK_tl_laasse10_option_answer_1 (question_result_uid),
  KEY FK_tl_laasse10_option_answer_2 (question_option_uid),
  CONSTRAINT FK_tl_laasse10_option_answer_1 FOREIGN KEY (question_result_uid) REFERENCES tl_laasse10_question_result (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laasse10_option_answer_2 FOREIGN KEY (question_option_uid) REFERENCES lams_qb_option (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_question_reference;

CREATE TABLE tl_laasse10_question_reference (
  uid bigint NOT NULL AUTO_INCREMENT,
  question_uid bigint DEFAULT NULL,
  sequence_id int DEFAULT NULL,
  default_grade int DEFAULT '1',
  random_question tinyint(1) DEFAULT '0',
  assessment_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  KEY FK_tl_laasse10_question_reference_1 (question_uid),
  KEY FK_tl_laasse10_question_reference_2 (assessment_uid),
  CONSTRAINT FK_tl_laasse10_question_reference_1 FOREIGN KEY (question_uid) REFERENCES tl_laasse10_assessment_question (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laasse10_question_reference_2 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_question_result;

CREATE TABLE tl_laasse10_question_result (
                                             uid bigint NOT NULL AUTO_INCREMENT,
                                             result_uid bigint DEFAULT NULL,
                                             answer_float float DEFAULT NULL,
                                             answer_boolean tinyint(1) DEFAULT NULL,
                                             mark float DEFAULT NULL,
                                             penalty float DEFAULT NULL,
                                             finish_date datetime DEFAULT NULL,
                                             max_mark float DEFAULT NULL,
                                             confidence_level int NOT NULL DEFAULT '0',
                                             justification varchar(10000) DEFAULT NULL,
                                             marked_by bigint DEFAULT NULL,
                                             marker_comment varchar(100) DEFAULT NULL,
                                             PRIMARY KEY (uid),
                                             KEY FK_tl_laasse10_question_result_1 (result_uid),
                                             KEY FK_tl_laasse10_question_result_3 (marked_by),
                                             CONSTRAINT FK_tl_laasse10_question_result_1 FOREIGN KEY (result_uid) REFERENCES tl_laasse10_assessment_result (uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                             CONSTRAINT FK_tl_laasse10_question_result_2 FOREIGN KEY (uid) REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                             CONSTRAINT FK_tl_laasse10_question_result_3 FOREIGN KEY (marked_by) REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_session;

CREATE TABLE tl_laasse10_session (
  uid bigint NOT NULL AUTO_INCREMENT,
  session_end_date datetime DEFAULT NULL,
  session_start_date datetime DEFAULT NULL,
  `status` int DEFAULT NULL,
  assessment_uid bigint DEFAULT NULL,
  session_id bigint DEFAULT NULL,
  session_name varchar(250) DEFAULT NULL,
  group_leader_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY session_id (session_id),
  KEY FK_NEW_1720029621_24AA78C530E79035 (assessment_uid),
  KEY tl_laasse10_session (group_leader_uid),
  CONSTRAINT FK_NEW_1720029621_24AA78C530E79035 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT tl_laasse10_session FOREIGN KEY (group_leader_uid) REFERENCES tl_laasse10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_time_limit;

CREATE TABLE tl_laasse10_time_limit (
  assessment_uid bigint NOT NULL,
  user_id bigint NOT NULL,
  adjustment smallint NOT NULL DEFAULT '0',
  KEY FK_tl_laasse10_time_limit_1 (assessment_uid),
  KEY FK_tl_laasse10_time_limit_2 (user_id),
  CONSTRAINT FK_tl_laasse10_time_limit_1 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_tl_laasse10_time_limit_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_user;

CREATE TABLE tl_laasse10_user (
  uid bigint NOT NULL AUTO_INCREMENT,
  user_id bigint DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  login_name varchar(255) DEFAULT NULL,
  session_finished smallint DEFAULT NULL,
  session_uid bigint DEFAULT NULL,
  assessment_uid bigint DEFAULT NULL,
  PRIMARY KEY (uid),
  UNIQUE KEY user_id (user_id,session_uid),
  KEY FK_NEW_1720029621_30113BFC309ED320 (assessment_uid),
  KEY FK_NEW_1720029621_30113BFCEC0D3147 (session_uid),
  CONSTRAINT FK_NEW_1720029621_30113BFC309ED320 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_NEW_1720029621_30113BFCEC0D3147 FOREIGN KEY (session_uid) REFERENCES tl_laasse10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS tl_laasse10_section;

CREATE TABLE tl_laasse10_section (
                                     uid mediumint unsigned NOT NULL AUTO_INCREMENT,
                                     assessment_uid bigint DEFAULT NULL,
                                     display_order tinyint unsigned NOT NULL DEFAULT '1',
                                     `name` varchar(100) DEFAULT NULL,
                                     question_count tinyint unsigned NOT NULL,
                                     PRIMARY KEY (uid),
                                     KEY FK_tl_laasse10_section_1 (assessment_uid),
                                     CONSTRAINT FK_tl_laasse10_section_1 FOREIGN KEY (assessment_uid) REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO tl_laasse10_configuration VALUES ('hideTitles','false'),  ('autoexpandJustification', 'false');

INSERT INTO tl_laasse10_assessment (uid, title, instructions, define_later, content_id, allow_question_feedback,
								    allow_overall_feedback, allow_right_answers, allow_wrong_answers,
								    allow_grades_after_attempt, allow_history_responses, display_summary, shuffled) VALUES
  (1,'Assessment','Instructions',0,${default_content_id},0,0,0,0,0,0,0,0);

SET FOREIGN_KEY_CHECKS=1;