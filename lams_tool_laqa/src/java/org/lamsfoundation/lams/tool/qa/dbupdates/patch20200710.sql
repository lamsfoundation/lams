-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5050 Essential code is in patch20200710.sql in lams_common, but we can bump tool version here for clarity

UPDATE lams_tool SET tool_version='20200710' WHERE tool_signature='laqa11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
