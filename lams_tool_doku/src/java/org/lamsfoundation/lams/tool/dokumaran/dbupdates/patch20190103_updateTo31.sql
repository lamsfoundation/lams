SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170303.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4259 Add time limit feature
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN time_limit integer DEFAULT 0;
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN time_limit_launched_date datetime;


-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_ladoku11_dokumaran MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_ladoku11_dokumaran MODIFY reflect_instructions MEDIUMTEXT;

-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='ladoku11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;