-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-4813 Add indexes for columns used for joins

ALTER TABLE lams_log_event ADD INDEX event_log_user (user_id),
						   ADD INDEX event_log_target_user (target_user_id),
						   ADD INDEX event_log_lesson (lesson_id),
						   ADD INDEX event_log_activity (activity_id);


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
