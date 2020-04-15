SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

--  LDEV-4997 Etherpad as service

INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('EtherpadServerUrl', 'http://localhost:9001', 'config.etherpad.server.url',  'config.header.etherpad', 'STRING', 0),
	   ('EtherpadApiKey',    '', 					  'config.etherpad.api.key',     'config.header.etherpad', 'STRING', 0),
	   ('EtherpadInstanceID','LAMS', 				  'config.etherpad.instance.id','config.header.etherpad', 'STRING', 0);

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;

