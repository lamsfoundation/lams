-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3631 Use simple commenting widget
ALTER TABLE tl_lanb11_content ADD COLUMN allow_comments TINYINT(1) DEFAULT 0;

UPDATE lams_tool SET tool_version='20151210' WHERE tool_signature='lanb11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;