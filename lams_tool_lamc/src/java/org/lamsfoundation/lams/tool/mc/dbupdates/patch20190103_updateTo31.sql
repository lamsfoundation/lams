SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180425.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_laimsc11_commoncartridge MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_laimsc11_commoncartridge MODIFY reflect_instructions MEDIUMTEXT;


-- LDEV-4671 Remove "locked when finished" option
ALTER TABLE tl_laimsc11_commoncartridge DROP COLUMN lock_on_finished;



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='laimsc11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;