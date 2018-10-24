-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3170 Force restart from beginning of lesson for learners

ALTER TABLE lams_lesson ADD COLUMN learner_restart TINYINT(1) DEFAULT 0;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
