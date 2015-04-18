-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3450 Set max and min rates limits
ALTER TABLE tl_laimag10_imagegallery ADD COLUMN minimum_rates integer DEFAULT 0; 
ALTER TABLE tl_laimag10_imagegallery ADD COLUMN maximum_rates integer DEFAULT 0;

UPDATE lams_tool SET tool_version='20150416' WHERE tool_signature='laimag10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;