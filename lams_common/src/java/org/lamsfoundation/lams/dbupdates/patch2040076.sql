-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-4096 Add role management feature for LTI tool consumers
ALTER TABLE lams_ext_server_org_map ADD COLUMN lti_consumer_monitor_roles text DEFAULT NULL;

COMMIT;
SET AUTOCOMMIT = 1;
