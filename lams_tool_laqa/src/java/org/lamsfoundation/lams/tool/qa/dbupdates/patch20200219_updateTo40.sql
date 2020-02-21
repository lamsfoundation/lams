SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains file patch20190809.sql
-- It should upgrade this tool to version 4.0

--LDEV-4845 Bump version so content version filter kicks in
UPDATE lams_tool SET tool_version='20190809' WHERE tool_signature='laqa11';

-- LDEV-4951 Update tools version for LAMS 4.0 release
UPDATE lams_tool SET tool_version='20190219' WHERE tool_signature='laqa11';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;