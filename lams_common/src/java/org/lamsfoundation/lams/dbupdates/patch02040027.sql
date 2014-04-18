SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-3219 Adding option to turn validation off for user details
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationUsername','true', 'config.user.validation.username', 'config.header.user.validation', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationFirstLastName','true', 'config.user.validation.first.last.name', 'config.header.user.validation', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationEmail','true', 'config.user.validation.emails', 'config.header.user.validation', 'BOOLEAN', 0);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
