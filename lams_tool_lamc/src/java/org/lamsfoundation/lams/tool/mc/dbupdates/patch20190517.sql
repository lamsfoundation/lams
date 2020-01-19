-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-4821 remove obsolete hash field
ALTER TABLE tl_lamc11_que_content DROP COLUMN question_hash;

UPDATE lams_tool SET tool_version='20190517' WHERE tool_signature='lamc11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
