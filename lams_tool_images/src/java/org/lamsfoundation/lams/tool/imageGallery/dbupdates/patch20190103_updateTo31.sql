SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170731.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1
-- LDEV-4396 Remove obsolete fields
ALTER TABLE tl_laimag10_imagegallery_item DROP COLUMN file_version_id;
ALTER TABLE tl_laimag10_imagegallery_item DROP COLUMN file_type;

UPDATE lams_tool SET tool_version='20170731' WHERE tool_signature='laimag10';




-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_laimag10_imagegallery MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_laimag10_imagegallery MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_laimag10_imagegallery_item MODIFY description MEDIUMTEXT;




-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='laimag10';



-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;