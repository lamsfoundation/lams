-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5443 Add "start in monitor" option to integrations
ALTER TABLE lams_ext_server_org_map ADD COLUMN start_in_monitor TINYINT DEFAULT 0 AFTER allow_restart;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;