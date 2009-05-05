update lams_configuration set config_value='2.3' where config_key='Version';
update lams_configuration set config_value='2.3.0.200905010000' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='2009-05-01' where config_key='DictionaryDateCreated';

-- 2.2 only 
-- update lams_learning_library set valid_flag=0 where title='Dimdim';