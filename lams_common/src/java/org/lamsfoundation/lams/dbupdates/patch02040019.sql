-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3112 Remove Flash Learner
DELETE FROM lams_configuration WHERE config_key = 'EnableFlash'
                                  OR config_key = 'ForceMobileDevToUseFlashless';
ALTER TABLE lams_user DROP COLUMN enable_flash;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
