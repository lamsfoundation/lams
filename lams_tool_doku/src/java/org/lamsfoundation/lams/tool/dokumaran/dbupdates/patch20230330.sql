-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5378 Add max mark for whole dokumaran activity
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN max_mark TINYINT UNSIGNED NOT NULL DEFAULT '100' AFTER reflect_on_activity;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
