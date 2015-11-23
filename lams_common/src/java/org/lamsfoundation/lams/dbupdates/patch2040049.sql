-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3614 The table is used for Gate activities only. It can be optimised to hold only allowed learners.
DELETE FROM lams_activity_learners WHERE allowed_to_pass = 0;

ALTER TABLE lams_activity_learners DROP COLUMN allowed_to_pass,
								   RENAME TO lams_gate_allowed_learners;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;