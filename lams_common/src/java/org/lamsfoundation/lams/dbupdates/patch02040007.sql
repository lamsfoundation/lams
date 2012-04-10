-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-2450
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.shareresourcesforum.ApplicationResources' 
		where library_activity_ui_image='images/icon_urlcontentmessageboard.swf' and learning_design_id is NULL;
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.chatscribe.ApplicationResources' 
		where library_activity_ui_image='images/icon_groupreporting.swf' and learning_design_id is NULL;
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.forumscribe.ApplicationResources' 
		where library_activity_ui_image='images/icon_forum_and_scribe.swf' and learning_design_id is NULL;
			
-- upgrade versions to 2.4		
update lams_configuration set config_value='2.4' where config_key='Version';
update lams_configuration set config_value='2.4.0.201204131000' where config_key='LearnerClientVersion' OR config_key='ServerVersionNumber' OR config_key='MonitorClientVersion' OR config_key='AuthoringClientVersion';
update lams_configuration set config_value='2012-04-13' where config_key='DictionaryDateCreated';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;