-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_laasse10_assessment DROP COLUMN online_instructions;
ALTER TABLE tl_laasse10_assessment DROP COLUMN offline_instructions;
ALTER TABLE tl_laasse10_assessment DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_laasse10_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='laasse10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;