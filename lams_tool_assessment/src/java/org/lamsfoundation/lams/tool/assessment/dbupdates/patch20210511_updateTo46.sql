SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190517.sql to patch20210511.sql
-- It should upgrade this tool to version 4.6

-- LDEV-4821 remove obsolete hash field
ALTER TABLE tl_laasse10_assessment_question DROP COLUMN question_hash;

-- LDEV-4836 remove obsolete columns
ALTER TABLE tl_laasse10_question_reference DROP COLUMN title;
ALTER TABLE tl_laasse10_question_reference DROP COLUMN question_type;

-- fill assessment_question.random_question with real values for old questions
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN random_question TINYINT(1) NOT NULL DEFAULT 0;
UPDATE tl_laasse10_assessment_question AS asque
SET asque.random_question = 1
WHERE NOT EXISTS
	(SELECT 1 FROM tl_laasse10_question_reference WHERE question_uid = asque.uid);

-- LDEV-4875 Add VSA question type to Assessment and Scratchie
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN activity_uuid_providing_vsa_answers INT(11);

-- LDEV-4880 Confidence level variations
ALTER TABLE tl_laasse10_assessment ADD COLUMN confidence_levels_type TINYINT DEFAULT 1;

--LDEV-4921 Allow question title hiding in learner
CREATE TABLE `tl_laasse10_configuration` (
  `config_key` varchar(30),
  `config_value` varchar(255),
  PRIMARY KEY (`config_key`)
 );

INSERT INTO `tl_laasse10_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

UPDATE lams_tool SET admin_url = 'tool/laasse10/admin/start.do' WHERE tool_signature = 'laasse10';

--LDEV-4976 Add cascade so when a row from parent table gets removed, a row from inheriting table gets removed too
ALTER TABLE tl_laasse10_question_result ADD CONSTRAINT FK_tl_laasse10_question_result_2 FOREIGN KEY (uid) 
	REFERENCES lams_qb_tool_answer (answer_uid) ON DELETE CASCADE ON UPDATE CASCADE;

--LDEV-5041 Add absolute time limit in Assessment
ALTER TABLE tl_laasse10_assessment CHANGE COLUMN time_limit relative_time_limit SMALLINT UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE tl_laasse10_assessment ADD COLUMN absolute_time_limit DATETIME AFTER relative_time_limit;

CREATE TABLE tl_laasse10_time_limit (
	assessment_uid BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	adjustment SMALLINT NOT NULL DEFAULT 0,
	CONSTRAINT FK_tl_laasse10_time_limit_1 FOREIGN KEY (assessment_uid)
		REFERENCES tl_laasse10_assessment (uid) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_tl_laasse10_time_limit_2 FOREIGN KEY (user_id)
		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
	);
	
--LDEV-5033 Add justification to question answer in Assessment
ALTER TABLE tl_laasse10_question_result ADD COLUMN justification VARCHAR(10000);
ALTER TABLE tl_laasse10_assessment ADD COLUMN allow_answer_justification TINYINT DEFAULT 0 AFTER allow_history_responses;
		
-- LDEV-4813 Add a missing foreign key and index
ALTER TABLE tl_laasse10_assessment_result ADD CONSTRAINT FK_tl_laasse10_assessment_result_1 FOREIGN KEY (session_id)
	REFERENCES tl_laasse10_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE;


SET FOREIGN_KEY_CHECKS=1;