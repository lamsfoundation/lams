-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4436 Ability to change portrait in learner
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('DisplayPortrait','true', 'config.display.portrait', 'config.header.look.feel', 'BOOLEAN', 0);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;