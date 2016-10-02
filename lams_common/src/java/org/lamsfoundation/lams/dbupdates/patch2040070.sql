SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-3219 Adding option to turn validation off for user details
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('SiteName','LAMS', 'config.site.name', 'config.header.system', 'STRING', 1);

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;
