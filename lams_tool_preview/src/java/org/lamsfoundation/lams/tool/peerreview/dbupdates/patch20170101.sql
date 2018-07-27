-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_laprev11_peerreview MODIFY COLUMN lock_on_finished TINYINT(1),
								   MODIFY COLUMN content_in_use TINYINT(1),
								   MODIFY COLUMN define_later TINYINT(1),
								   MODIFY COLUMN show_ratings_left_for_user TINYINT(1) DEFAULT 1,
								   MODIFY COLUMN reflect_on_activity TINYINT(1),
								   MODIFY COLUMN self_review TINYINT(1) DEFAULT 0,
								   MODIFY COLUMN notify_users_of_results TINYINT(1) DEFAULT 1,
								   MODIFY COLUMN show_ratings_left_by_user TINYINT(1) DEFAULT 0;
									 	
ALTER TABLE tl_laprev11_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laprev11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;