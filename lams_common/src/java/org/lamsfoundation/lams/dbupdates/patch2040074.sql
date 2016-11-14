SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
-- LDEV-4049 Option for not displaying stacktraces in config settings

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ErrorStackTrace','true', 'config.stacktrace.error', 'config.header.system', 'BOOLEAN', 0);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;