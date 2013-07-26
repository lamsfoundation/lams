-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-3070 Allow single activity lesson creation on redesigned index page
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SingleActivityLessonsEnabled','false', 'config.authoring.single.activity', 'config.header.features', 'BOOLEAN', 0);

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
