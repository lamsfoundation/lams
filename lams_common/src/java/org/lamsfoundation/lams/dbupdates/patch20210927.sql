-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-4288 Prevent duplicate entries

ALTER TABLE lams_gradebook_user_activity DROP KEY activity_id,
										 ADD UNIQUE KEY UQ_lams_gradebook_user_activity_1 (activity_id, user_id);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
