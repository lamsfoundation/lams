
update lams_configuration set config_value='2.3.1' where config_key='Version';
update lams_configuration set config_value='2.3.1.200806190000' where config_key='LearnerClientVersion' OR config_key='ServerVersionNumber' OR config_key='MonitorClientVersion' OR config_key='AuthoringClientVersion';
update lams_configuration set config_value='2009-06-19' where config_key='DictionaryDateCreated';

