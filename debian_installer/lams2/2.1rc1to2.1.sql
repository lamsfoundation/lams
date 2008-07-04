-- Database changes from 2.1 RC1 to 2.1

-- LDEV-1579
alter table lams_tool add column supports_outputs TINYINT(1) DEFAULT 0;
update lams_tool set supports_outputs = 1 where tool_signature = "lafrum11";
update lams_tool set tool_version = "20080220" where tool_signature = "lafrum11";
update lams_tool set supports_outputs = 1 where tool_signature = "lamc11";
update lams_tool set tool_version = "20070820" where tool_signature = "lamc11";
update lams_tool set supports_outputs = 1 where tool_signature = "lavote11";

-- LDEV-1597
ALTER TABLE tl_lavote11_content
ADD COLUMN show_results TINYINT(1) NOT NULL DEFAULT 1;
UPDATE tl_lavote11_content set show_results = 1;
UPDATE lams_tool SET tool_version = "20080326" WHERE tool_signature = "lavote11";

-- LDEV-1598
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AuthoringScreenSize','800x600', 'config.authoring.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('MonitorScreenSize','800x600', 'config.monitor.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerScreenSize','800x600', 'config.learner.screen.size', 'config.header.look.feel', 'STRING', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('AdminScreenSize','800x600', 'config.admin.screen.size', 'config.header.look.feel', 'STRING', 1);

-- LDEV-273
CREATE INDEX email ON lams_user (email ASC);

-- LDEV-1702
UPDATE lams_supported_locale SET description='日本語' WHERE language_iso_code='ja' AND country_iso_code='JP';

-- LDEV-1707
ALTER TABLE tl_lasbmt11_report MODIFY COLUMN marks float;
UPDATE lams_tool SET tool_version = "20080509" WHERE tool_signature = "lasbmt11";

-- Version strings
update lams_configuration set config_value='2.1' where config_key='Version';
update lams_configuration set config_value='2.1.200806190000' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='2008-06-19' where config_key='DictionaryDateCreated';

