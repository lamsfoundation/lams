-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and run offline activity options
-- Comment out this part until we will be able to modify tools' tool_insert.sql and tool_insert.sql activity_insert.sql to exclude inserting according columns   
--ALTER TABLE lams_learning_activity DROP COLUMN define_later_flag;
--ALTER TABLE lams_learning_activity DROP COLUMN run_offline_flag;

--ALTER TABLE lams_tool DROP COLUMN supports_run_offline_flag;
--ALTER TABLE lams_tool DROP COLUMN define_later_url;

--TODO remove the next line after uncommenting previous section
ALTER TABLE lams_learning_activity CHANGE COLUMN run_offline_flag run_offline_flag TINYINT(1) NOT NULL DEFAULT '0';

ALTER TABLE lams_learning_design DROP COLUMN online_instructions;
ALTER TABLE lams_learning_design DROP COLUMN offline_instructions;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
