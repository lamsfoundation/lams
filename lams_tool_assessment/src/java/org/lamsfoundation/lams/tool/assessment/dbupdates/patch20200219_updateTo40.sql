SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190423.sql to patch20191205.sql
-- It should upgrade this tool to version 4.0

-- LDEV-4813 Add a missing foreign key and index to speed up queries
-- We need to check if patch20190722.sql from lams_common has not run already						
SET @exist := (SELECT COUNT(*) from information_schema.statistics WHERE 
	table_name = 'lams_qb_question' AND
	table_schema = database());
SET @sqlstmt := IF(@exist > 0,
	'SELECT ''INFO: Question Bank has already created necessary foreign keys''',
	'ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_2 FOREIGN KEY (question_option_uid)	REFERENCES tl_laasse10_question_option (uid) ON DELETE CASCADE ON UPDATE CASCADE');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;

--LDEV-4821 remove obsolete hash field
ALTER TABLE tl_laasse10_assessment_question DROP COLUMN question_hash;

UPDATE lams_tool SET tool_version='20190517' WHERE tool_signature='laasse10';

-- LDEV-4836 remove obsolete columns
ALTER TABLE tl_laasse10_question_reference DROP COLUMN title;
ALTER TABLE tl_laasse10_question_reference DROP COLUMN question_type;

-- fill assessment_question.random_question with real values for old questions
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN random_question TINYINT(1) NOT NULL DEFAULT 0;
UPDATE tl_laasse10_assessment_question AS asque
SET asque.random_question = 1
WHERE NOT EXISTS
	(SELECT 1 FROM tl_laasse10_question_reference WHERE question_uid = asque.uid);

UPDATE lams_tool SET tool_version='20190704' WHERE tool_signature='laasse10';

--LDEV-4845 Bump version so content version filter kicks in
UPDATE lams_tool SET tool_version='20190809' WHERE tool_signature='laasse10';

-- LDEV-4875 Add VSA question type to Assessment and Scratchie
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN activity_uuid_providing_vsa_answers INT(11);

-- LDEV-4813 Add missing indexes to speed up queries
ALTER TABLE tl_laasse10_assessment_result ADD INDEX (latest);
ALTER TABLE tl_laasse10_option_answer ADD INDEX (answer_boolean); 
 
-- LDEV-4880 Confidence level variations
ALTER TABLE tl_laasse10_assessment ADD COLUMN confidence_levels_type TINYINT DEFAULT 1;
UPDATE lams_tool SET tool_version='20191120' WHERE tool_signature='laasse10';

--LDEV-4921 Allow question title hiding in learner
CREATE TABLE `tl_laasse10_configuration` (
  `config_key` varchar(30),
  `config_value` varchar(255),
  PRIMARY KEY (`config_key`)
);
INSERT INTO `tl_laasse10_configuration` (`config_key`, `config_value`) VALUES ('hideTitles', 'false');

UPDATE lams_tool SET admin_url = 'tool/laasse10/admin/start.do' WHERE tool_signature = 'laasse10';

-- LDEV-4951 Update tools version for LAMS 4.0 release
UPDATE lams_tool SET tool_version='20190219' WHERE tool_signature='laasse10';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;