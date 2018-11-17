-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4707 Remove never used fields from TaskListItem
ALTER TABLE tl_latask10_tasklist_item DROP COLUMN show_comments_to_all,
	DROP COLUMN is_comments_files_allowed;

UPDATE lams_tool SET tool_version='20181216' WHERE tool_signature='latask10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;