-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4710 Config setting to display or not the "Forgot your password?" option
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('EnableForgotYourPasswordLink','true', 'config.enable.forgot.your.password.link', 'config.header.features', 'BOOLEAN', 0);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;