SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4023 Implement password policy 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('FailedAttempts','3', 'config.failed.attempts', 'config.header.password.policy', 'LONG', 1);


insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('LockOutTime','5', 'config.lock.out.time', 'config.header.password.policy', 'LONG', 1);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
