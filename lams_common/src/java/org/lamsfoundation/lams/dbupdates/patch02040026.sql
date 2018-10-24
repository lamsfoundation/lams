-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3207 Remove unnecessary columns from schedule gate.

ALTER TABLE lams_learning_activity DROP COLUMN gate_start_date_time,
								   DROP COLUMN gate_end_date_time;


-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;