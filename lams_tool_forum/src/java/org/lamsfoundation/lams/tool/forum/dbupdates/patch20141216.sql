-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3395 Implement a minimum number of characters for forum postings
ALTER TABLE tl_lafrum11_forum ADD COLUMN min_characters integer DEFAULT 0; 
ALTER TABLE tl_lafrum11_forum ADD COLUMN limited_min_characters tinyint DEFAULT 0;

UPDATE lams_tool SET tool_version='20141216' WHERE tool_signature='lafrum11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;