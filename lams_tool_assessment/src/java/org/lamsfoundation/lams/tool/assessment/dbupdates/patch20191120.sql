-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4880 Confidence level variations
ALTER TABLE tl_laasse10_assessment ADD COLUMN confidence_levels_type TINYINT DEFAULT 1;
UPDATE lams_tool SET tool_version='20191120' WHERE tool_signature='laasse10';
								 	
-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;