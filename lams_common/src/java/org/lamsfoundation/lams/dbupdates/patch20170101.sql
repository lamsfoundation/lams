-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE lams_user RENAME KEY html_theme_id TO theme_id,
					  MODIFY COLUMN change_password TINYINT(1);

ALTER TABLE lams_input_activity DROP KEY activity_id_2;

ALTER TABLE lams_notification_event DROP KEY uid;
ALTER TABLE lams_notification_subscription DROP KEY uid;

ALTER TABLE lams_competence DROP KEY competence_id;
ALTER TABLE lams_competence_mapping DROP KEY competence_mapping_id;

ALTER TABLE lams_learning_design MODIFY COLUMN valid_design_flag TINYINT(1),
								 MODIFY COLUMN read_only_flag TINYINT(1),
								 MODIFY COLUMN edit_override_lock TINYINT(1);
								 
ALTER TABLE lams_grouping MODIFY COLUMN equal_number_of_learners_per_group TINYINT(1),
						  MODIFY COLUMN view_students_before_selection TINYINT(1);
						  
ALTER TABLE lams_learning_activity DROP COLUMN define_later_flag,
								   DROP COLUMN run_offline_flag,
								   DROP COLUMN help_text,
								   MODIFY COLUMN read_only TINYINT(1),
								   MODIFY COLUMN initialised TINYINT(1),
								   MODIFY COLUMN stop_after_activity TINYINT(1);
								   
ALTER TABLE lams_branch_activity_entry MODIFY COLUMN open_gate TINYINT(1);

ALTER TABLE lams_lesson MODIFY COLUMN locked_for_edit TINYINT(1),
						MODIFY COLUMN marks_released TINYINT(1);
						
ALTER TABLE lams_configuration MODIFY COLUMN required TINYINT(1);

ALTER TABLE lams_timezone MODIFY COLUMN server_timezone TINYINT(1);

-- fix a typo
UPDATE lams_configuration SET config_key = 'ConfigCacheRefreshInterval' WHERE config_key = 'ConfigCacheRefresInterval';

-- LDEV-3147 Get rid of 'supports_run_offline_flag' and 'define_later_url' columns from lams_tool table
ALTER TABLE lams_tool DROP COLUMN supports_run_offline_flag;
ALTER TABLE lams_tool DROP COLUMN define_later_url;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
