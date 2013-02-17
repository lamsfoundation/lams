-- SQL statements to update to LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

-- 	LDEV-2953 Add notebook option after learners finish Assessment
ALTER TABLE tl_laasse10_assessment ADD COLUMN reflect_on_activity BIT DEFAULT false;
ALTER TABLE tl_laasse10_assessment ADD COLUMN reflect_instructions TEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;