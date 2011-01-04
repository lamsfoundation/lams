-- Upgrade LAMS 2.3.5
-- update lams_configuration set config_value='2.3' where config_key='Version';
update lams_configuration set config_value='2.3.5.201012150000' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='2010-12-15' where config_key='DictionaryDateCreated';

-- 2.2 only 
-- update lams_learning_library set valid_flag=0 where title='Dimdim';