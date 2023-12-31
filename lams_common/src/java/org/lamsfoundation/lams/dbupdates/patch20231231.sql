-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- Disable tools to be deprecated
UPDATE lams_learning_library SET valid_flag = 0 WHERE description IN ('Share commonCartridge', 'Pixlr Tool', 'Scribe Tool', 'Wiki Tool', 'Chat and Scribe', 'Forum and Scribe');
-- Set version release to v4.8
UPDATE lams_configuration SET config_value = '4.8' WHERE config_key = 'ServerVersionNumber' AND config_value <> '4.8';
UPDATE lams_configuration SET config_value = '2023-12-30' WHERE config_key = 'DictionaryDateCreated';
UPDATE lams_configuration SET config_value = 'Australia/Sydney' WHERE config_key = 'ServerTimezone' AND config_value IS NULL;
-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
