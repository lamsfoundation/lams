-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5401 Add learner-triggered absolute time limit to assessment
ALTER TABLE tl_laasse10_assessment RENAME COLUMN absolute_time_limit TO absolute_time_limit_finish;
ALTER TABLE tl_laasse10_assessment ADD COLUMN absolute_time_limit SMALLINT UNSIGNED NOT NULL DEFAULT '0' AFTER relative_time_limit;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;