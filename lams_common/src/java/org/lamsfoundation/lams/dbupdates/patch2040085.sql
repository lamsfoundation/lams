-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4217 Remove LAMS Community integration
DROP TABLE IF EXISTS `lams_registration`;
ALTER TABLE `lams_user` DROP COLUMN `lams_community_username`, DROP COLUMN `lams_community_token`;
DELETE FROM `lams_configuration` WHERE `config_key`='LAMS_Community_enable';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
