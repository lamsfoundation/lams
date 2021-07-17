-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5219 Add "stop-students-at-activity-before-gate" feature

ALTER TABLE lams_learning_activity ADD COLUMN gate_stop_at_preceding_activity TINYINT NOT NULL DEFAULT 0 AFTER gate_password;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
