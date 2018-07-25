-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3228 Ability to change, add, remove MCQ questions even after student have reached it
ALTER TABLE tl_lavote11_content DROP COLUMN content_in_use;

UPDATE lams_tool SET tool_version='20140520' WHERE tool_signature='lavote11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;