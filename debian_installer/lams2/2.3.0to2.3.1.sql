-- Version strings
update lams_configuration set config_value='2.3.1' where config_key='Version';
update lams_configuration set config_value='2.3.1.200906190000' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='2009-06-19' where config_key='DictionaryDateCreated';