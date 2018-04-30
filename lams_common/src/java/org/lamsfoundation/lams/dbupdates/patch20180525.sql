-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE lams_comment MODIFY body MEDIUMTEXT;
ALTER TABLE lams_competence MODIFY description MEDIUMTEXT;
ALTER TABLE lams_email_notification_archive MODIFY body MEDIUMTEXT;
ALTER TABLE lams_ext_server_org_map MODIFY serverdesc MEDIUMTEXT;
ALTER TABLE lams_gradebook_user_activity MODIFY feedback MEDIUMTEXT;
ALTER TABLE lams_gradebook_user_activity_archive MODIFY feedback MEDIUMTEXT;
ALTER TABLE lams_gradebook_user_lesson_archive MODIFY feedback MEDIUMTEXT;
ALTER TABLE lams_learning_activity MODIFY description MEDIUMTEXT;
ALTER TABLE lams_learning_activity MODIFY options_instructions MEDIUMTEXT;
ALTER TABLE lams_learning_design MODIFY description MEDIUMTEXT;
ALTER TABLE lams_learning_design MODIFY help_text MEDIUMTEXT;
ALTER TABLE lams_learning_design MODIFY license_TEXT MEDIUMTEXT;
ALTER TABLE lams_learning_library MODIFY description MEDIUMTEXT;
ALTER TABLE lams_learning_transition MODIFY description MEDIUMTEXT;
ALTER TABLE lams_lesson MODIFY description MEDIUMTEXT;
ALTER TABLE lams_notebook_entry MODIFY entry MEDIUMTEXT;
ALTER TABLE lams_notification_event MODIFY message MEDIUMTEXT;
ALTER TABLE lams_notification_subscription MODIFY last_operation_message MEDIUMTEXT;
ALTER TABLE lams_organisation MODIFY description MEDIUMTEXT;
ALTER TABLE lams_planner_nodes MODIFY brief_desc MEDIUMTEXT;
ALTER TABLE lams_planner_nodes MODIFY full_desc MEDIUMTEXT;
ALTER TABLE lams_rating_comment MODIFY comment MEDIUMTEXT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;