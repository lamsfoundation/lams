-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5253 Make Login As feature configurable

INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required) 
VALUES ('LoginAsEnable', 'true', 'config.login.as.enable', 'config.header.privacy.settings', 'BOOLEAN', 1);


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
