-- FOR LAMS 2.1.1 
-- Adding a configuration entry into for the google map api key

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)  values ('GmapKey','', 'config.gmap.gmapkey', 'config.gmap.section.title', 'STRING', 0);