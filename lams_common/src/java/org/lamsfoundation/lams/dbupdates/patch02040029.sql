SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-3254 Allow periodic update of configuration cache
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ConfigCacheRefresInterval', 0, 'config.cache.refresh', 'config.header.system', 'LONG', 0);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;