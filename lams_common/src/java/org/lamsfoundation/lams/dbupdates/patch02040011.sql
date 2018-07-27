-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2889  Collapsible progress panel in Learner
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerCollapsProgressPanel','true', 'config.learner.collapsible.progress.panel', 'config.header.features', 'BOOLEAN', 0);

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;