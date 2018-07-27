-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3420 Add date column to Gradebook marks
ALTER TABLE lams_gradebook_user_activity ADD COLUMN update_date DATETIME DEFAULT NULL;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
