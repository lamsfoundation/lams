-- LI-192 This script needs to be in updaters - before new tools are deployed
ALTER TABLE lams_tool ADD COLUMN pedagogical_planner_url TEXT;

-- Version strings
update lams_configuration set config_value='2.3' where config_key='Version';
update lams_configuration set config_value='2.3.0.200905110000' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='2009-05-11' where config_key='DictionaryDateCreated';
