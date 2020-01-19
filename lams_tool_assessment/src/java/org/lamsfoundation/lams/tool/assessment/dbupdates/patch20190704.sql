-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

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

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
