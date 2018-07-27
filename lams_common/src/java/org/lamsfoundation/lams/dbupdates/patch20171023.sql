-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4447 Add "Allow Kumalive" to system settings and organisation
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('AllowKumalive','true', 'config.allow.kumalive', 'config.header.features', 'BOOLEAN', 1);

ALTER TABLE lams_organisation ADD COLUMN enable_kumalive TINYINT(1) NOT NULL DEFAULT 0 AFTER enable_live_edit;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;