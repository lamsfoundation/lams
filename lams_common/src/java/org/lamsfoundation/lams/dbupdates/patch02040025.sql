-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3207 New type of schedule gate, based on activity completion

ALTER TABLE lams_learning_activity ADD COLUMN gate_activity_completion_based TINYINT(1)
									   AFTER gate_end_date_time;


-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;