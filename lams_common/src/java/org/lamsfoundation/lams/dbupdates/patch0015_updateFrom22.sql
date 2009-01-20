-- SQL statements to update from LAMS 2.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

--  LDEV-1998 ------------- 
ALTER TABLE lams_tool ADD COLUMN pedagogical_planner_url TEXT;
ALTER TABLE lams_system_tool ADD COLUMN pedagogical_planner_url TEXT;
UPDATE lams_system_tool SET pedagogical_planner_url='pedagogicalPlanner/initGrouping.do' WHERE system_tool_id=1;

-- LDEV-2074 --------------
ALTER TABLE lams_learning_design ADD COLUMN first_activity_id BIGINT(20);
CREATE INDEX idx_design_floating_act ON lams_learning_design (floating_activity_id ASC);

-- LDEV-1983 --------------
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPUser','', 'config.smtp.user', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPPassword','', 'config.smtp.password', 'config.header.email', 'STRING', 0);


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;