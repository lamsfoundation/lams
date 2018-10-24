-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4686 Remove Kaltura settings from admin config settings	
DELETE FROM lams_configuration WHERE (config_key = 'KalturaServer');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaPartnerId');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaSubPartnerId');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaUserSecret');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaKCWUiConfId');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaKDPUiConfId');

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
