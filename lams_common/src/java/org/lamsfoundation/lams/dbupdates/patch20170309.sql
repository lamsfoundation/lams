-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4255, LDEV-4256 Removing UI options no longer used
DELETE FROM `lams_configuration` WHERE `config_key`='LearnerCollapsProgressPanel';
DELETE FROM `lams_configuration` WHERE `config_key`='AuthoringActivitiesColour';
DELETE FROM `lams_configuration` WHERE `config_key`='LearnerProgressBatchSize';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
