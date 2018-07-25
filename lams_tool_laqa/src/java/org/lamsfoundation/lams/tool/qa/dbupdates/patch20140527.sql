-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3236 Ability to change, add, remove Q&A questions even after student have reached it
ALTER TABLE tl_laqa11_content DROP COLUMN content_inUse;
ALTER TABLE tl_laqa11_content DROP COLUMN synch_in_monitor;

ALTER TABLE tl_laqa11_content ADD COLUMN notify_response_submit TINYINT(1) NOT NULL DEFAULT 0;

UPDATE lams_tool SET tool_version='20140527' WHERE tool_signature='laqa11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;