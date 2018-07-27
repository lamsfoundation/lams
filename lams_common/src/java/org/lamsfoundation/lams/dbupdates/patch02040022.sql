-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and run offline activity options
ALTER TABLE lams_learning_activity CHANGE COLUMN run_offline_flag run_offline_flag TINYINT(1) NOT NULL DEFAULT '0';

ALTER TABLE lams_learning_design DROP COLUMN online_instructions;
ALTER TABLE lams_learning_design DROP COLUMN offline_instructions;

-- LDEV-3172: Reduce the Inactive User Timeout setting to 3 hours

UPDATE lams_configuration SET config_value = '10800' WHERE config_key = 'UserInactiveTimeout';

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
