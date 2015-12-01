-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3621 Ability to import and use groups from integrated server
ALTER TABLE lams_ext_server_org_map ADD COLUMN ext_groups_url text DEFAULT NULL;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
