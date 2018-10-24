SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4023 Implement password policy 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyMinChars','8', 'config.password.minimum.characters', 'config.header.password.policy', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyUppercase','true', 'config.password.uppercase', 'config.header.password.policy', 'BOOLEAN', 0);
 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyLowercase','true', 'config.password.lowercase', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyNumerics','true', 'config.password.numerics', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicySymbols','false', 'config.password.symbols', 'config.header.password.policy', 'BOOLEAN', 0);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;