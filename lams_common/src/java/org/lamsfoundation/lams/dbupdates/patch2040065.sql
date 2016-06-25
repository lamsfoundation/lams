-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3674 Remove Flash themes
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'AuthoringScreenSize';
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'LearnerScreenSize';
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'MonitorScreenSize';
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'AdminScreenSize';

COMMIT;
SET AUTOCOMMIT = 1;
