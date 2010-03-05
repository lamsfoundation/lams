SET AUTOCOMMIT = 0;

-- LDEV-2509 Adding configuration setting for Embedded SMTP server
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('InternalSMTPServer','true', 'config.use.internal.smtp.server', 'config.header.email', 'BOOLEAN', 0);

COMMIT;
SET AUTOCOMMIT = 1;