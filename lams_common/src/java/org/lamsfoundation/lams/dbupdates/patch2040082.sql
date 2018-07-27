SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4144 Timezone warning
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ShowTimezoneWarning','true', 'config.show.timezone.warning', 'config.header.features', 'BOOLEAN', 1);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;