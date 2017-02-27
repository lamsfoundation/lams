-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_lasprd10_spreadsheet MODIFY COLUMN lock_on_finished TINYINT(1),
									 	MODIFY COLUMN content_in_use TINYINT(1),
									 	MODIFY COLUMN define_later TINYINT(1),
									 	MODIFY COLUMN is_learner_allowed_to_save TINYINT(1),
									 	MODIFY COLUMN reflect_on_activity TINYINT(1),
									 	MODIFY COLUMN is_marking_enabled TINYINT(1);
									 	
ALTER TABLE tl_lasprd10_session MODIFY COLUMN status TINYINT(1);									 		 
	
ALTER TABLE tl_lasprd10_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lasprd10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;