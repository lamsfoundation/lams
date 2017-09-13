-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4401 Remove deprecated parameters for integrations
ALTER TABLE lams_ext_server_org_map DROP COLUMN timeout_url;
ALTER TABLE lams_ext_server_org_map DROP COLUMN server_url;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;