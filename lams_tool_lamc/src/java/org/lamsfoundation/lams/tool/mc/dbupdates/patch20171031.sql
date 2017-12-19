-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4451 add 'Enable Confidence levels' advanced setting
ALTER TABLE tl_lamc11_content ADD COLUMN enable_confidence_levels TINYINT(1) NOT NULL DEFAULT 0;

--add questions' hashes in order to be able to search for the similar questions with the same title and question
ALTER TABLE tl_lamc11_que_content ADD COLUMN question_hash CHAR(40);
UPDATE tl_lamc11_que_content SET question_hash = SHA1(question);

--add confidence level property to tl_laasse10_question_result
ALTER TABLE tl_lamc11_usr_attempt ADD COLUMN confidence_level int;

UPDATE lams_tool SET tool_version='20171031' WHERE tool_signature='lamc11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;