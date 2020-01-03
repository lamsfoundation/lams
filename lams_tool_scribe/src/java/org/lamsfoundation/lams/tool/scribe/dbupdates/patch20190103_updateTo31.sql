SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170308.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4261 Remove optimistic locking management
ALTER TABLE tl_lascrb11_session DROP COLUMN version;


-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lascrb11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;