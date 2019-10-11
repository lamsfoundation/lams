SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4874 Restrict displaying names for students that are not within student's group
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('RestrictedGroupUserNames','true', 'config.restricted.displaying.user.names.in.groupings', 'config.header.privacy.settings', 'BOOLEAN', 0);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
