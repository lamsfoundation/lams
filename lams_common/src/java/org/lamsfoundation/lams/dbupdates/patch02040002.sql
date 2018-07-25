SET AUTOCOMMIT = 0;

-- LDEV-1616
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ProfileEditEnable','true', 'config.profile.edit.enable', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ProfilePartialEditEnable','true', 'config.profile.partial.edit.enable', 'config.header.features', 'BOOLEAN', 1); 

-- LDEV-1356
alter table lams_user add column modified_date datetime;

COMMIT;
SET AUTOCOMMIT = 1;
