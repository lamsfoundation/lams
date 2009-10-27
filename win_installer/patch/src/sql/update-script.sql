
update lams_configuration set config_value='2.3.2' where config_key='Version';
update lams_configuration set config_value='2.3.2.200908280000' where config_key='LearnerClientVersion' OR config_key='ServerVersionNumber' OR config_key='MonitorClientVersion' OR config_key='AuthoringClientVersion';
update lams_configuration set config_value='2009-08-28' where config_key='DictionaryDateCreated';


