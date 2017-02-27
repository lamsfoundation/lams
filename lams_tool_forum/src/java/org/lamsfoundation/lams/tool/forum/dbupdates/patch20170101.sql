-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_lafrum11_forum MODIFY COLUMN allow_anonym TINYINT(1),
							  MODIFY COLUMN lock_on_finished TINYINT(1),
							  MODIFY COLUMN define_later TINYINT(1),
							  MODIFY COLUMN content_in_use TINYINT(1),
							  MODIFY COLUMN allow_edit TINYINT(1),
							  MODIFY COLUMN allow_rich_editor TINYINT(1),
							  MODIFY COLUMN allow_new_topic TINYINT(1),
							  MODIFY COLUMN allow_upload TINYINT(1),
							  MODIFY COLUMN allow_rate_messages TINYINT(1),
							  MODIFY COLUMN limited_input_flag TINYINT(1),
							  MODIFY COLUMN reflect_on_activity TINYINT(1),
							  MODIFY COLUMN notify_learners_on_forum_posting tinyint(1) DEFAULT 0,
   							  MODIFY COLUMN notify_teachers_on_forum_posting tinyint(1) DEFAULT 0,
   							  MODIFY COLUMN mark_release_notify tinyint(1) DEFAULT 0,
							  MODIFY COLUMN limited_min_characters tinyint(1) DEFAULT 0;
							  
ALTER TABLE tl_lafrum11_forum_user MODIFY COLUMN session_finished TINYINT(1);

ALTER TABLE tl_lafrum11_message MODIFY COLUMN is_authored TINYINT(1),
							    MODIFY COLUMN is_anonymous TINYINT(1),
							    MODIFY COLUMN hide_flag TINYINT(1),
							    MODIFY COLUMN is_monitor TINYINT(1) DEFAULT 0;
							    
ALTER TABLE tl_lafrum11_tool_session MODIFY COLUMN mark_released TINYINT(1);							    
							    
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lafrum11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;