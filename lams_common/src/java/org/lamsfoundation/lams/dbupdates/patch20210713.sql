-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5200 Remove learning library groups feature
DROP TABLE IF EXISTS lams_learning_library_group;

-- Update server version so authoring auto rearrange can kick in

UPDATE lams_configuration SET config_value = '4.5' WHERE config_key = 'ServerVersionNumber';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
