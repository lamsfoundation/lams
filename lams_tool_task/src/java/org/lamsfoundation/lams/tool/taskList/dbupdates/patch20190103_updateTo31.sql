SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180425.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1




-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_latask10_item_comment MODIFY comment MEDIUMTEXT;
ALTER TABLE tl_latask10_tasklist MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_latask10_tasklist MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_latask10_tasklist_item MODIFY description MEDIUMTEXT;


-- LDEV-4707 Remove never used fields from TaskListItem
ALTER TABLE tl_latask10_tasklist_item DROP COLUMN show_comments_to_all,
	DROP COLUMN is_comments_files_allowed;

UPDATE lams_tool SET tool_version='20181216' WHERE tool_signature='latask10';



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='latask10';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;