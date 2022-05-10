SET FOREIGN_KEY_CHECKS=0;

create table tl_laasse10_assessment (
   uid bigint(20) not null auto_increment,
   create_date datetime,
   update_date datetime,
   submission_deadline datetime,
   create_by bigint,
   title varchar(255),
   time_limit integer DEFAULT 0,
   attempts_allowed integer DEFAULT 1,
   passing_mark integer DEFAULT 0,
   instructions MEDIUMTEXT,
   define_later tinyint(1),
   content_id bigint,
   allow_question_feedback tinyint(1),
   allow_overall_feedback tinyint(1),
   allow_disclose_answers TINYINT(1) DEFAULT 0,
   allow_right_answers tinyint(1),
   allow_wrong_answers tinyint(1),
   allow_grades_after_attempt tinyint(1),
   allow_history_responses tinyint(1),
   display_summary tinyint(1),
   questions_per_page integer DEFAULT 0,
   shuffled tinyint(1),
   attempt_completion_notify tinyint(1) DEFAULT 0,
   reflect_on_activity bit(1) DEFAULT 0,
   reflect_instructions MEDIUMTEXT,
   numbered tinyint(1) DEFAULT 1,
   use_select_leader_tool_ouput tinyint(1) NOT NULL DEFAULT 0,
   question_etherpad_enabled TINYINT(1) DEFAULT 0,
   enable_confidence_levels TINYINT(1) NOT NULL DEFAULT 0,
   confidence_levels_type TINYINT DEFAULT 1,
   PRIMARY KEY (uid),
   UNIQUE KEY content_id (content_id)
);

create table tl_laasse10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   assessment_uid bigint,
   session_id bigint,
   session_name varchar(250),
   group_leader_uid bigint(20),
   primary key (uid),
   UNIQUE KEY session_id (session_id),
   CONSTRAINT FK_NEW_1720029621_24AA78C530E79035 FOREIGN KEY (assessment_uid)
   		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   assessment_uid bigint,
   primary key (uid),
   UNIQUE KEY user_id (user_id,session_uid),
   CONSTRAINT FK_NEW_1720029621_30113BFC309ED320 FOREIGN KEY (assessment_uid)
   		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_NEW_1720029621_30113BFCEC0D3147 FOREIGN KEY (session_uid)
   		REFERENCES tl_laasse10_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_assessment_question (
   uid bigint not null auto_increment,
   sequence_id integer,
   random_question TINYINT(1) NOT NULL DEFAULT 0,
   assessment_uid bigint,
   session_uid bigint,
   correct_answers_disclosed TINYINT(1) DEFAULT 0,
   groups_answers_disclosed TINYINT(1) DEFAULT 0,
   PRIMARY KEY (uid),
   CONSTRAINT FK_NEW_1720029621_F52D1F9330E79035 FOREIGN KEY (assessment_uid)
   		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_question_reference (
   uid bigint not null auto_increment,
   question_uid bigint,
   sequence_id integer,
   default_grade integer DEFAULT 1,
   random_question tinyint(1) DEFAULT 0,
   assessment_uid bigint,
   primary key (uid),
   CONSTRAINT FK_tl_laasse10_question_reference_1 FOREIGN KEY (question_uid)
   		REFERENCES tl_laasse10_assessment_question (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_laasse10_question_reference_2 FOREIGN KEY (assessment_uid)
   		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_assessment_overall_feedback (
   uid bigint not null unique auto_increment,
   assessment_uid bigint,
   sequence_id integer,
   grade_boundary integer,
   feedback MEDIUMTEXT,
   primary key (uid),
   CONSTRAINT FK_tl_laasse10_assessment_overall_feedback_1 FOREIGN KEY (assessment_uid)
   		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_assessment_result (
   uid bigint not null auto_increment,
   assessment_uid bigint,
   start_date datetime,
   finish_date datetime,
   user_uid bigint,
   session_id bigint,
   maximum_grade integer,
   grade float,
   latest tinyint(1) DEFAULT NULL,
   time_limit_launched_date datetime,
   primary key (uid),
   UNIQUE KEY `UQ_tl_laasse10_assessment_result_5` (`assessment_uid`,`user_uid`,`latest`),
   KEY `latest` (`latest`),
   CONSTRAINT FK_tl_laasse10_assessment_result_2 FOREIGN KEY (user_uid)
   		REFERENCES tl_laasse10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_laasse10_assessment_result_3 FOREIGN KEY (assessment_uid)
   		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_question_result (
   uid bigint not null auto_increment,
   result_uid bigint,
   answer_float float,
   answer_boolean tinyint(1),
   mark float,
   penalty float,
   finish_date datetime,
   max_mark float,
   confidence_level INT(11) NOT NULL DEFAULT 0,
   primary key (uid),
   CONSTRAINT FK_tl_laasse10_question_result_1 FOREIGN KEY (result_uid)
   		REFERENCES tl_laasse10_assessment_result (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_laasse10_option_answer (
   uid bigint not null unique auto_increment,
   question_result_uid bigint,
   question_option_uid bigint,
   answer_boolean tinyint(1),
   answer_int integer,
   primary key (uid),
   KEY `answer_boolean` (`answer_boolean`),
   CONSTRAINT FK_tl_laasse10_option_answer_1 FOREIGN KEY (question_result_uid)
   		REFERENCES tl_laasse10_question_result (uid) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FK_tl_laasse10_option_answer_2 FOREIGN KEY (question_option_uid) 
   		REFERENCES lams_qb_option (uid) ON DELETE CASCADE ON UPDATE CASCADE
);


ALTER TABLE tl_laasse10_assessment ADD CONSTRAINT FK_NEW_1720029621_89093BF758092FB FOREIGN KEY (create_by)
		REFERENCES tl_laasse10_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE tl_laasse10_session ADD CONSTRAINT tl_laasse10_session FOREIGN KEY (group_leader_uid)
   		REFERENCES tl_laasse10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE;
   		
CREATE TABLE `tl_laasse10_configuration` (
   `config_key` varchar(30),
   `config_value` varchar(255),
   PRIMARY KEY (`config_key`)
);

INSERT INTO tl_laasse10_assessment (uid, title, instructions, define_later, content_id, allow_question_feedback,
								    allow_overall_feedback, allow_right_answers, allow_wrong_answers,
								    allow_grades_after_attempt, allow_history_responses, display_summary, shuffled) VALUES
  (1,'Assessment','Instructions',0,${default_content_id},0,0,0,0,0,0,0,0);
  
INSERT INTO `tl_laasse10_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

SET FOREIGN_KEY_CHECKS=1;