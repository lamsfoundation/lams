-- SQL statements to set the database/lams version at 2.3.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2450
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.shareresourcesforum.ApplicationResources1' 
		where library_activity_ui_image='images/icon_urlcontentmessageboard.swf' and learning_design_id is NULL;
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.chatscribe.ApplicationResources2' 
		where library_activity_ui_image='images/icon_groupreporting.swf' and learning_design_id is NULL;
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.forumscribe.ApplicationResources3' 
		where library_activity_ui_image='images/icon_forum_and_scribe.swf' and learning_design_id is NULL;
			
			
update lams_configuration set config_value='2.3.3' where config_key='Version';
update lams_configuration set config_value='2.3.3.200910300000' where config_key='LearnerClientVersion' OR config_key='ServerVersionNumber' OR config_key='MonitorClientVersion' OR config_key='AuthoringClientVersion';
update lams_configuration set config_value='2009-10-30' where config_key='DictionaryDateCreated';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;