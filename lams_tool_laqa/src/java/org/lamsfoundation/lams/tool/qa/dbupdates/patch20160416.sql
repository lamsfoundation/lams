-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3751 Prevent students from submitting questions but allow them to view responses
ALTER TABLE tl_laqa11_content ADD COLUMN show_other_answers_after_deadline TINYINT(1) NOT NULL DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;