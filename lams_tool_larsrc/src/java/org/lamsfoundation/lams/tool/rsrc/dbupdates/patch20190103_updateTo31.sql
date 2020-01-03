SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170419.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-2999 Rating Option for each Item
ALTER TABLE tl_larsrc11_resource_item ADD COLUMN is_allow_rating TINYINT(1) default 0;



-- LDEV-2999 Rating Option for each Item
ALTER TABLE tl_larsrc11_resource_item ADD COLUMN is_allow_comments TINYINT(1) default 0;



-- LDEV-4502 Redo change as it was lost in the sql merges
-- LDEV-2941 Enable the option "Open URL in pop-up" for the example URL by default
UPDATE tl_larsrc11_resource_item SET open_url_new_window=1 WHERE uid='1';



-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_larsrc11_item_instruction MODIFY description MEDIUMTEXT;
ALTER TABLE tl_larsrc11_resource MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_larsrc11_resource MODIFY reflect_instructions MEDIUMTEXT;


-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='larsrc11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;