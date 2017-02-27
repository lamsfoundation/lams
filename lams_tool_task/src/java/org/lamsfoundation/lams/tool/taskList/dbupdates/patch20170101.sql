-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_latask10_tasklist MODIFY COLUMN lock_on_finished TINYINT(1),
							   	 MODIFY COLUMN content_in_use TINYINT(1),
							   	 MODIFY COLUMN define_later TINYINT(1),
							   	 MODIFY COLUMN is_sequential_order TINYINT(1),
							   	 MODIFY COLUMN reflect_on_activity TINYINT(1),
							   	 MODIFY COLUMN allow_contribute_tasks TINYINT(1),
							   	 MODIFY COLUMN is_monitor_verification_required TINYINT(1),
							   	 MODIFY COLUMN allow_contribute_tasks TINYINT(1);
									 	
ALTER TABLE tl_latask10_tasklist_item MODIFY COLUMN create_by_author TINYINT(1),
								 	  MODIFY COLUMN is_required TINYINT(1),
								 	  MODIFY COLUMN is_comments_allowed TINYINT(1),
								 	  MODIFY COLUMN is_comments_required TINYINT(1),
								 	  MODIFY COLUMN is_files_allowed TINYINT(1),
								 	  MODIFY COLUMN is_files_required TINYINT(1),
								 	  MODIFY COLUMN is_comments_files_allowed TINYINT(1),
								 	  MODIFY COLUMN show_comments_to_all TINYINT(1),
								 	  MODIFY COLUMN is_child_task TINYINT(1);

ALTER TABLE tl_latask10_item_log MODIFY COLUMN complete TINYINT(1);	

ALTER TABLE tl_latask10_session MODIFY COLUMN status TINYINT(1);	

ALTER TABLE tl_latask10_user MODIFY COLUMN session_finished TINYINT(1),
							 MODIFY COLUMN is_verified_by_monitor TINYINT(1);

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='latask10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;