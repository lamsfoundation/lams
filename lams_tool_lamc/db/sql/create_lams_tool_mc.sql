SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE tl_lamc11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , title TEXT
     , instructions MEDIUMTEXT
     , creation_date DATETIME
     , update_date DATETIME
     , reflect TINYINT(1) NOT NULL DEFAULT 0
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , retries TINYINT(1) NOT NULL DEFAULT 0
     , pass_mark INTEGER
     , show_report TINYINT(1) NOT NULL DEFAULT 0
     , reflectionSubject MEDIUMTEXT
     , showMarks TINYINT(1) NOT NULL DEFAULT 0
     , randomize TINYINT(1) NOT NULL DEFAULT 0
     , displayAnswers TINYINT(1) NOT NULL DEFAULT 1
     , submission_deadline datetime
     , use_select_leader_tool_ouput tinyint(1) NOT NULL DEFAULT 0
     , prefix_answers_with_letters tinyint(1) NOT NULL DEFAULT 1
     , enable_confidence_levels TINYINT(1) NOT NULL DEFAULT 0
     , display_feedback_only TINYINT(1) NOT NULL DEFAULT 0
     , UNIQUE UQ_tl_lamc11_content_1 (content_id)
     , PRIMARY KEY (uid)
);

CREATE TABLE tl_lamc11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , mc_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_name VARCHAR(100)
     , session_status VARCHAR(100)
     , mc_content_id BIGINT(20) NOT NULL
     , mc_group_leader_uid BIGINT(20)
     , PRIMARY KEY (uid)
     , UNIQUE UQ_tl_lamc11_session_1 (mc_session_id)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc_session_1 FOREIGN KEY (mc_content_id)
			REFERENCES tl_lamc11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lamc11_que_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , mc_session_id BIGINT(20) NOT NULL
     , username VARCHAR(255)
     , fullname VARCHAR(255)
     , responseFinalised TINYINT(1) NOT NULL DEFAULT 0
     , viewSummaryRequested TINYINT(1) NOT NULL DEFAULT 0
     , last_attempt_total_mark INTEGER
     , number_attempts int(11) DEFAULT 0
     , PRIMARY KEY (uid)
     , UNIQUE KEY que_usr_id (que_usr_id,mc_session_id)
     , INDEX (mc_session_id)
     , CONSTRAINT FK_tl_lamc11_que_usr_1 FOREIGN KEY (mc_session_id)
             REFERENCES tl_lamc11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lamc11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , mc_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc11_que_content_1 FOREIGN KEY (mc_content_id)
            REFERENCES tl_lamc11_content (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lamc11_usr_attempt (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , attempt_time DATETIME
     , isAttemptCorrect TINYINT(1) NOT NULL DEFAULT 0
     , mark VARCHAR(255)
     , passed TINYINT(1) NOT NULL DEFAULT 0
     , confidence_level INT(11) NOT NULL DEFAULT 0
     , PRIMARY KEY (uid)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_4 FOREIGN KEY (que_usr_id)
                  REFERENCES tl_lamc11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `tl_lamc11_configuration` (
	`config_key` varchar(30),
	`config_value` varchar(255),
	PRIMARY KEY (`config_key`)
);

ALTER TABLE tl_lamc11_session ADD CONSTRAINT FK_lamc11_session1 FOREIGN KEY (mc_group_leader_uid)
     		REFERENCES tl_lamc11_que_usr (uid) ON DELETE SET NULL ON UPDATE CASCADE;

-- data for content table
INSERT INTO tl_lamc11_content(uid, content_id , title, instructions, creation_date , reflect, questions_sequenced, created_by , define_later, retries, show_report, pass_mark) VALUES (1, ${default_content_id}, 'MCQ', 'Instructions', NOW(), 0, 0, 1, 0, 0, 0, 0);

-- data for QB question table
SET @max_question_id = COALESCE((SELECT MAX(question_id) FROM lams_qb_question),0) + 1;
INSERT INTO `lams_qb_question` (`type`,`question_id`,`version`,`create_date`,`name`,`description`,`max_mark`,`content_folder_id`)
	VALUES (1,@max_question_id,1,NOW(),'A Sample question?','Question description',1,'01234567-89ab-cdef-0123-4567890abcde');
INSERT INTO lams_sequence_generator(lams_qb_question_question_id) VALUES (@max_question_id);
SET @qb_question_uid = (SELECT MAX(uid) FROM lams_qb_question);
INSERT INTO `lams_qb_option` (`qb_question_uid`,`display_order`,`name`,`max_mark`) VALUES (@qb_question_uid,1,'Answer 1',0);
INSERT INTO `lams_qb_option` (`qb_question_uid`,`display_order`,`name`,`max_mark`) VALUES (@qb_question_uid,2,'Answer 2',1);
INSERT INTO lams_qb_collection_question	SELECT 1, @max_question_id;

-- data for tool question tables
INSERT INTO lams_qb_tool_question (qb_question_uid,tool_content_id,display_order) VALUES (@qb_question_uid,${default_content_id},1);
INSERT INTO tl_lamc11_que_content (uid,mc_content_id) VALUES ((SELECT MAX(tool_question_uid) FROM lams_qb_tool_question),1);


INSERT INTO `tl_lamc11_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

SET FOREIGN_KEY_CHECKS=1;