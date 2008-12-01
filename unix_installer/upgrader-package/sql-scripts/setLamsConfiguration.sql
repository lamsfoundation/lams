update lams_configuration set config_value='${LAMS_VERSION}' where config_key='Version';
update lams_configuration set config_value='${LAMS_SERVER_VERSION}' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='${LAMS_LANGUAGE_VERSION}' where config_key='DictionaryDateCreated';

-- 2.2 only 
update lams_learning_library set valid_flag=0 where title='Dimdim';