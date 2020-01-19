
-- SQL statements to update to LAMS 2.4.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;
-- Put all sql statements below here

-- LDEV-2921 Set max and min rate per thread
ALTER TABLE tl_lafrum11_forum ADD COLUMN minimum_rate integer DEFAULT 0; 
ALTER TABLE tl_lafrum11_forum ADD COLUMN maximum_rate integer DEFAULT 0;

UPDATE lams_tool SET tool_version = "20121109" WHERE tool_signature = "lafrum11";

-- Put all sql statements above here
-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;
