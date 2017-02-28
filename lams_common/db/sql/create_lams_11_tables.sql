CREATE TABLE lams_gate_activity_level (
       gate_activity_level_id INT(11) NOT NULL
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (gate_activity_level_id)
);

CREATE TABLE lams_grouping_type (
       grouping_type_id INT(11) NOT NULL
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (grouping_type_id)
);

CREATE TABLE lams_learning_activity_type (
       learning_activity_type_id INT(11) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (learning_activity_type_id)
);

CREATE TABLE lams_learning_library (
       learning_library_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , description TEXT
     , title VARCHAR(255)
     , valid_flag TINYINT(1) NOT NULL DEFAULT 1
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (learning_library_id)
);

CREATE TABLE lams_organisation_type (
       organisation_type_id INT(3) NOT NULL
     , name VARCHAR(64) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (organisation_type_id)
     , UNIQUE KEY UQ_lams_organisation_type_name (name)
);

CREATE TABLE lams_role (
       role_id INT(6) NOT NULL
     , name VARCHAR(64) NOT NULL
     , description TEXT
     , create_date DATETIME NOT NULL
     , PRIMARY KEY (role_id)
     , KEY gname (name)
);

CREATE TABLE lams_tool_session_state (
       tool_session_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (tool_session_state_id)
);

CREATE TABLE lams_lesson_state (
       lesson_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (lesson_state_id)
);

CREATE TABLE lams_tool_session_type (
       tool_session_type_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (tool_session_type_id)
);

CREATE TABLE lams_license (
       license_id BIGINT(20) NOT NULL
     , name VARCHAR(200) NOT NULL
     , code VARCHAR(20) NOT NULL
     , url VARCHAR(256)
     , default_flag TINYINT(1) NOT NULL DEFAULT 0
     , picture_url VARCHAR(256)
     , PRIMARY KEY (license_id)
);

CREATE TABLE lams_copy_type (
       copy_type_id TINYINT(4) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (copy_type_id)
);

CREATE TABLE lams_activity_category (
       activity_category_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (activity_category_id)
);

CREATE TABLE lams_cr_workspace (
       workspace_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , name VARCHAR(255) NOT NULL
     , PRIMARY KEY (workspace_id)
);

CREATE TABLE lams_cr_credential (
       credential_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , name VARCHAR(255) NOT NULL
     , password VARCHAR(255) NOT NULL
     , PRIMARY KEY (credential_id)
);

CREATE TABLE lams_workspace_folder_type (
       lams_workspace_folder_type_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (lams_workspace_folder_type_id)
);

CREATE TABLE lams_grouping_support_type (
       grouping_support_type_id INT(3) NOT NULL
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (grouping_support_type_id)
);

CREATE TABLE lams_log_event_type (
       log_event_type_id INT(5) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (log_event_type_id)
);

CREATE TABLE lams_privilege (
       privilege_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , code VARCHAR(10) NOT NULL
     , description VARCHAR(255)
     , PRIMARY KEY (privilege_id)
     , UNIQUE KEY IX_lams_privilege_code (code)
);

CREATE TABLE lams_organisation_state (
       organisation_state_id INT(3) NOT NULL
     , description VARCHAR(255)
     , PRIMARY KEY (organisation_state_id)
);

CREATE TABLE lams_supported_locale (
       locale_id INTEGER NOT NULL AUTO_INCREMENT
     , language_iso_code VARCHAR(2) NOT NULL
     , country_iso_code VARCHAR(2)
     , description VARCHAR(255) NOT NULL
     , direction VARCHAR(3) NOT NULL
	 , fckeditor_code VARCHAR(10)
     , PRIMARY KEY (locale_id)
);

CREATE TABLE lams_auth_method_type (
       authentication_method_type_id INT(3) NOT NULL
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (authentication_method_type_id)
);

CREATE TABLE lams_authentication_method (
       authentication_method_id BIGINT(20) NOT NULL
     , authentication_method_type_id INT(3) NOT NULL DEFAULT 0
     , authentication_method_name VARCHAR(191) NOT NULL
     , UNIQUE UQ_lams_authentication_method_1 (authentication_method_name)
     , PRIMARY KEY (authentication_method_id)
     , KEY (authentication_method_type_id)
     , CONSTRAINT FK_lams_authorization_method_1 FOREIGN KEY (authentication_method_type_id)
                  REFERENCES lams_auth_method_type (authentication_method_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_workspace_folder (
       workspace_folder_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , parent_folder_id BIGINT(20)
     , name VARCHAR(255) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , organisation_id BIGINT(20)
     , create_date_time DATETIME NOT NULL
     , last_modified_date_time DATETIME
     , lams_workspace_folder_type_id INT(3) NOT NULL
     , PRIMARY KEY (workspace_folder_id)
     , KEY (parent_folder_id)
     , KEY (lams_workspace_folder_type_id)
     , CONSTRAINT FK_lams_workspace_folder_4 FOREIGN KEY (lams_workspace_folder_type_id)
                  REFERENCES lams_workspace_folder_type (lams_workspace_folder_type_id)
);

ALTER TABLE lams_workspace_folder ADD CONSTRAINT FK_lams_workspace_folder_2 FOREIGN KEY (parent_folder_id)
		REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE lams_organisation (
       organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , name VARCHAR(250) NOT NULL
     , code VARCHAR(20)
     , description TEXT
     , parent_organisation_id BIGINT(20)
     , organisation_type_id INT(3) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL
     , created_by BIGINT(20) NOT NULL
     , organisation_state_id INT(3) NOT NULL
     , admin_add_new_users TINYINT(1) NOT NULL DEFAULT 0
     , admin_browse_all_users TINYINT(1) NOT NULL DEFAULT 0
     , admin_change_status TINYINT(1) NOT NULL DEFAULT 0
     , admin_create_guest TINYINT(1) NOT NULL DEFAULT 0
     , enable_course_notifications TINYINT(1) NOT NULL DEFAULT 0
	 , enable_monitor_gradebook TINYINT(1) NOT NULL DEFAULT 0
     , enable_learner_gradebook TINYINT(1) NOT NULL DEFAULT 0
     , enable_single_activity_lessons TINYINT(1) NOT NULL DEFAULT 1
     , enable_live_edit TINYINT(1) NOT NULL DEFAULT 1
     , locale_id INTEGER
     , archived_date DATETIME
     , ordered_lesson_ids TEXT
     , PRIMARY KEY (organisation_id)
     , KEY (organisation_type_id)
     , CONSTRAINT FK_lams_organisation_1 FOREIGN KEY (organisation_type_id)
                  REFERENCES lams_organisation_type (organisation_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
  	 , KEY (parent_organisation_id)
     , KEY (organisation_state_id)
     , CONSTRAINT FK_lams_organisation_4 FOREIGN KEY (organisation_state_id)
                  REFERENCES lams_organisation_state (organisation_state_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (locale_id)
     , CONSTRAINT FK_lams_organisation_5 FOREIGN KEY (locale_id)
                  REFERENCES lams_supported_locale (locale_id)
);

ALTER TABLE lams_organisation ADD CONSTRAINT FK_lams_organisation_3 FOREIGN KEY (parent_organisation_id)
		REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

CREATE TABLE lams_theme (
       theme_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , name VARCHAR(100) NOT NULL
     , description VARCHAR(100)
     , image_directory VARCHAR(100)
     , PRIMARY KEY (theme_id)
     , UNIQUE KEY UQ_name (name)
);

CREATE TABLE lams_user (
       user_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , login VARCHAR(191) NOT NULL
     , password CHAR(64) NOT NULL
     , salt CHAR(64)
     , two_factor_auth_enabled TINYINT(1) DEFAULT 0
  	 , two_factor_auth_secret CHAR(64)
     , title VARCHAR(32)
     , first_name VARCHAR(128)
     , last_name VARCHAR(128)
     , address_line_1 VARCHAR(64)
     , address_line_2 VARCHAR(64)
     , address_line_3 VARCHAR(64)
     , city VARCHAR(64)
     , state VARCHAR(64)
     , postcode VARCHAR(10)
     , country VARCHAR(64)
     , day_phone VARCHAR(64)
     , evening_phone VARCHAR(64)
     , mobile_phone VARCHAR(64)
     , fax VARCHAR(64)
     , email VARCHAR(128)
     , disabled_flag TINYINT(1) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL
     , authentication_method_id BIGINT(20) NOT NULL DEFAULT 0
     , workspace_folder_id BIGINT(20)
     , theme_id BIGINT(20)
     , locale_id INTEGER
     , portrait_uuid BIGINT(20)
     , change_password TINYINT(1) DEFAULT 0
	 , lams_community_token VARCHAR(255)
 	 , lams_community_username VARCHAR(255)
 	 , timezone VARCHAR(255)
	 , tutorials_disabled TINYINT(1) DEFAULT 0
	 , first_login TINYINT(1) DEFAULT 1
     , modified_date DATETIME
     , last_visited_organisation_id BIGINT(20)
  	 , failed_attempts TINYINT(4)
     , lock_out_time DATETIME
     , PRIMARY KEY (user_id)
     , KEY (authentication_method_id)
     , CONSTRAINT FK_lams_user_1 FOREIGN KEY (authentication_method_id)
                  REFERENCES lams_authentication_method (authentication_method_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (workspace_folder_id)
     , CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (theme_id)
     , CONSTRAINT FK_lams_user_5 FOREIGN KEY (theme_id)
     			  REFERENCES lams_theme (theme_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (locale_id)
     , CONSTRAINT FK_lams_user_6 FOREIGN KEY (locale_id)
                  REFERENCES lams_supported_locale (locale_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , CONSTRAINT FK_lams_user_7 FOREIGN KEY (last_visited_organisation_id)
     			  REFERENCES lams_organisation (organisation_id)
     , UNIQUE KEY UQ_lams_user_login (login)
     , KEY email (email)
);

CREATE TABLE lams_learning_design (
       learning_design_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_design_ui_id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , first_activity_id BIGINT(20)
     , floating_activity_id BIGINT(20)
     , max_id INT(11)
     , valid_design_flag TINYINT(1) NOT NULL
     , read_only_flag TINYINT(1) NOT NULL
     , date_read_only DATETIME
     , user_id BIGINT(20) NOT NULL
     , original_user_id BIGINT(20) NOT NULL
     , help_TEXT TEXT
     , copy_type_id TINYINT(4) NOT NULL
     , create_date_time DATETIME NOT NULL
     , version VARCHAR(56)
     , original_learning_design_id BIGINT(20)
     , workspace_folder_id BIGINT(20)
     , duration BIGINT(38)
     , license_id BIGINT(20)
     , license_TEXT TEXT
     , last_modified_date_time DATETIME
     , content_folder_id VARCHAR(32)
     , edit_override_lock TINYINT(1) DEFAULT 0
     , edit_override_user_id BIGINT(20)
     , design_version INTEGER DEFAULT 1
     , removed TINYINT(1) NOT NULL DEFAULT 0
  	 , design_type VARCHAR(255)
     , PRIMARY KEY (learning_design_id)
     , KEY (user_id)
     , CONSTRAINT FK_lams_learning_design_3 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , KEY (workspace_folder_id)
     , CONSTRAINT FK_lams_learning_design_4 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
     , KEY (license_id)
     , CONSTRAINT FK_lams_learning_design_5 FOREIGN KEY (license_id)
                  REFERENCES lams_license (license_id)
     , KEY (copy_type_id)
     , CONSTRAINT FK_lams_learning_design_6 FOREIGN KEY (copy_type_id)
                  REFERENCES lams_copy_type (copy_type_id)
     , KEY (edit_override_user_id)
     , CONSTRAINT FK_lams_learning_design_7 FOREIGN KEY (edit_override_user_id)
                  REFERENCES lams_user (user_id)
     , KEY idx_design_parent_id (original_learning_design_id)
     , KEY idx_design_first_act(first_activity_id)
     , KEY idx_design_floating_act (floating_activity_id)
);

CREATE TABLE lams_grouping (
       grouping_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , grouping_ui_id INT(11)
     , grouping_type_id INT(11) NOT NULL
     , number_of_groups INT(11)
     , learners_per_group INT(11)
     , staff_group_id BIGINT(20) DEFAULT 0
     , max_number_of_groups INT(3)
	 , equal_number_of_learners_per_group TINYINT(1) DEFAULT 0
	 , view_students_before_selection TINYINT(1) DEFAULT 0
     , PRIMARY KEY (grouping_id)
     , KEY (grouping_type_id)
     , CONSTRAINT FK_lams_learning_grouping_1 FOREIGN KEY (grouping_type_id)
                  REFERENCES lams_grouping_type (grouping_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_group (
       group_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , group_name VARCHAR(255) NOT NULL
     , grouping_id BIGINT(20) NOT NULL
     , order_id INT(6) NOT NULL DEFAULT 1
     , group_ui_id INT(11)
     , PRIMARY KEY (group_id)
     , KEY (grouping_id)
     , CONSTRAINT FK_lams_learning_group_1 FOREIGN KEY (grouping_id)
                  REFERENCES lams_grouping (grouping_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_system_tool (
       system_tool_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_activity_type_id INT(11) NOT NULL
     , tool_display_name VARCHAR(255) NOT NULL
     , description TEXT
     , learner_url TEXT
     , learner_preview_url TEXT
     , learner_progress_url TEXT
     , monitor_url TEXT
     , contribute_url TEXT
     , help_url TEXT
     , create_date_time DATETIME NOT NULL
     , admin_url TEXT
	 , pedagogical_planner_url TEXT
     , PRIMARY KEY (system_tool_id)
     , UNIQUE KEY UQ_systool_activity_type (learning_activity_type_id)
     , CONSTRAINT FK_lams_system_tool FOREIGN KEY (learning_activity_type_id)
                  REFERENCES lams_learning_activity_type (learning_activity_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_tool (
       tool_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , tool_signature VARCHAR(64) NOT NULL
     , service_name VARCHAR(191) NOT NULL
     , tool_display_name VARCHAR(255) NOT NULL
     , description TEXT
     , tool_identifier VARCHAR(64) NOT NULL
     , tool_version VARCHAR(10) NOT NULL
     , learning_library_id BIGINT(20)
     , default_tool_content_id BIGINT(20)
     , valid_flag TINYINT(1) NOT NULL DEFAULT 1
     , grouping_support_type_id INT(3) NOT NULL
     , supports_run_offline_flag TINYINT(1) NOT NULL
     , learner_url TEXT NOT NULL
     , learner_preview_url TEXT
     , learner_progress_url TEXT
     , author_url TEXT NOT NULL
     , define_later_url TEXT
     , monitor_url TEXT 
	 , pedagogical_planner_url TEXT
     , help_url TEXT
     , create_date_time DATETIME NOT NULL
     , language_file VARCHAR(255)
     , modified_date_time DATETIME
     , admin_url TEXT
     , supports_outputs TINYINT(1) DEFAULT 0
	 , ext_lms_id VARCHAR(255)
	 , PRIMARY KEY (tool_id)
     , UNIQUE KEY UQ_lams_tool_sig (tool_signature)
     , UNIQUE KEY UQ_lams_tool_class_name (service_name)
     , KEY (learning_library_id)
     , CONSTRAINT FK_lams_tool_1 FOREIGN KEY (learning_library_id)
                  REFERENCES lams_learning_library (learning_library_id)
     , KEY (grouping_support_type_id)
     , CONSTRAINT FK_lams_tool_2 FOREIGN KEY (grouping_support_type_id)
                  REFERENCES lams_grouping_support_type (grouping_support_type_id)
);
      
CREATE TABLE lams_learning_activity (
       activity_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , activity_ui_id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , xcoord INT(11)
     , ycoord INT(11)
     , parent_activity_id BIGINT(20)
     , parent_ui_id INT(11)
     , learning_activity_type_id INT(11) NOT NULL DEFAULT 0
     , grouping_support_type_id INT(3) NOT NULL
     , apply_grouping_flag TINYINT(1) NOT NULL
     , grouping_id BIGINT(20)
     , grouping_ui_id INT(11)
     , order_id INT(11)
     , learning_design_id BIGINT(20)
     , learning_library_id BIGINT(20)
     , create_date_time DATETIME NOT NULL
     , max_number_of_options INT(5)
     , min_number_of_options INT(5)
     , options_instructions TEXT
     , tool_id BIGINT(20)
     , tool_content_id BIGINT(20)
     , activity_category_id INT(3) NOT NULL
     , gate_activity_level_id INT(11)
     , gate_open_flag TINYINT(1)
     , gate_start_time_offset BIGINT(38)
     , gate_end_time_offset BIGINT(38)
	 , gate_activity_completion_based TINYINT(1)
     , library_activity_ui_image VARCHAR(255)
     , create_grouping_id BIGINT(20)
     , create_grouping_ui_id INT(11)
     , library_activity_id BIGINT(20)
     , language_file VARCHAR(255)
     , system_tool_id BIGINT(20)
     , read_only TINYINT(1) DEFAULT 0
     , initialised TINYINT(1) DEFAULT 0
     , default_activity_id BIGINT(20)
     , start_xcoord INT(11)
     , start_ycoord INT(11)
     , end_xcoord INT(11)
     , end_ycoord INT(11) 
     , stop_after_activity TINYINT(1) NOT NULL DEFAULT 0
	 , transition_to_id BIGINT(20)
	 , transition_from_id BIGINT(20)
     , PRIMARY KEY (activity_id)
     , KEY lams_learning_activity_tool_content_id (tool_content_id)
     , KEY (learning_library_id)
     , CONSTRAINT FK_lams_learning_activity_7 FOREIGN KEY (learning_library_id)
                  REFERENCES lams_learning_library (learning_library_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (learning_design_id)
     , CONSTRAINT FK_lams_learning_activity_6 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (parent_activity_id)
     , CONSTRAINT FK_learning_activity_2 FOREIGN KEY (parent_activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (learning_activity_type_id)
     , CONSTRAINT FK_learning_activity_3 FOREIGN KEY (learning_activity_type_id)
                  REFERENCES lams_learning_activity_type (learning_activity_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (grouping_id)
     , CONSTRAINT FK_learning_activity_6 FOREIGN KEY (grouping_id)
                  REFERENCES lams_grouping (grouping_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (tool_id)
     , CONSTRAINT FK_lams_learning_activity_8 FOREIGN KEY (tool_id)
                  REFERENCES lams_tool (tool_id)
     , KEY (gate_activity_level_id)
     , CONSTRAINT FK_lams_learning_activity_10 FOREIGN KEY (gate_activity_level_id)
                  REFERENCES lams_gate_activity_level (gate_activity_level_id)
     , KEY (create_grouping_id)
     , CONSTRAINT FK_lams_learning_activity_9 FOREIGN KEY (create_grouping_id)
                  REFERENCES lams_grouping (grouping_id)
     , KEY (library_activity_id)
     , CONSTRAINT FK_lams_learning_activity_11 FOREIGN KEY (library_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , KEY (activity_category_id)
     , CONSTRAINT FK_lams_learning_activity_12 FOREIGN KEY (activity_category_id)
                  REFERENCES lams_activity_category (activity_category_id)
     , KEY (grouping_support_type_id)
     , CONSTRAINT FK_lams_learning_activity_13 FOREIGN KEY (grouping_support_type_id)
                  REFERENCES lams_grouping_support_type (grouping_support_type_id)
     , KEY (system_tool_id)
     , CONSTRAINT FK_lams_learning_activity_14 FOREIGN KEY (system_tool_id)
                  REFERENCES lams_system_tool (system_tool_id)
);

CREATE TABLE lams_planner_activity_metadata (
       activity_id BIGINT(20) NOT NULL
	 , collapsed TINYINT(1) DEFAULT 0
	 , expanded TINYINT(1) DEFAULT 0
	 , hidden TINYINT(1) DEFAULT 0
	 , editing_advice VARCHAR(255)
     , CONSTRAINT FK_lams_planner_metadata_primary FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_input_activity (
       activity_id BIGINT(20) NOT NULL
     , input_activity_id BIGINT(20) NOT NULL
     , UNIQUE KEY UQ_lams_input_activity_1 (activity_id, input_activity_id)
     , KEY (activity_id)
     , CONSTRAINT FK_lams_input_activity_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , CONSTRAINT FK_lams_input_activity_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
);

CREATE TABLE lams_branch_condition (
       condition_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , condition_ui_id INT(11)
     , order_id INT(11) 
     , name VARCHAR(255) NOT NULL
     , display_name VARCHAR(255)
     , type VARCHAR(30) NOT NULL
     , start_value VARCHAR(255)
     , end_value VARCHAR(255)
     , exact_match_value VARCHAR(255)
     , PRIMARY KEY (condition_id)
);

CREATE TABLE lams_TEXT_search_condition (
       condition_id BIGINT(20) NOT NULL
	 , TEXT_search_all_words TEXT
	 , TEXT_search_phrase TEXT
	 , TEXT_search_any_words TEXT
	 , TEXT_search_excluded_words TEXT
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT TextSearchConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_branch_activity_entry (
       entry_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , entry_ui_id INT(11)
     , group_id BIGINT(20)
     , sequence_activity_id BIGINT(20)
     , branch_activity_id BIGINT(20) NOT NULL
     , condition_id BIGINT(20)
	 , open_gate TINYINT(1)
     , PRIMARY KEY (entry_id)
     , KEY (group_id)
     , CONSTRAINT FK_lams_group_activity_1 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
     , KEY (sequence_activity_id)
     , CONSTRAINT FK_lams_branch_map_sequence FOREIGN KEY (sequence_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , KEY (branch_activity_id)
     , CONSTRAINT FK_lams_branch_map_branch FOREIGN KEY (branch_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , KEY (condition_id)
     , CONSTRAINT FK_lams_branch_activity_entry_4 FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition (condition_id)
);



CREATE TABLE lams_lesson (
       lesson_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_design_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , name VARCHAR(255) NOT NULL
     , description TEXT
     , create_date_time DATETIME NOT NULL
     , organisation_id BIGINT(20)
     , class_grouping_id BIGINT(20)
     , lesson_state_id INT(3) NOT NULL
     , start_date_time DATETIME
     , scheduled_number_days_to_lesson_finish INT(3)
     , schedule_start_date_time DATETIME
     , end_date_time DATETIME
     , schedule_end_date_time DATETIME
     , previous_state_id INT(3)
     , learner_presence_avail TINYINT(1) DEFAULT 0
     , learner_im_avail TINYINT(1) DEFAULT 0
	 , live_edit_enabled TINYINT(1) DEFAULT 0
	 , enable_lesson_notifications TINYINT(1) DEFAULT 0
     , locked_for_edit TINYINT(1) DEFAULT 0
	 , marks_released TINYINT(1) DEFAULT 0
     , version INTEGER DEFAULT 1
     , enable_lesson_INTro TINYINT(1) DEFAULT 0
     , display_design_image TINYINT(1) DEFAULT 0
     , force_restart TINYINT(1) DEFAULT 0
     , allow_restart TINYINT(1) DEFAULT 0
     , PRIMARY KEY (lesson_id)
     , KEY (learning_design_id)
     , CONSTRAINT FK_lams_lesson_1_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
     , KEY (user_id)
     , CONSTRAINT FK_lams_lesson_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , KEY (organisation_id)
     , CONSTRAINT FK_lams_lesson_3 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id)
     , KEY (lesson_state_id)
     , CONSTRAINT FK_lams_lesson_4 FOREIGN KEY (lesson_state_id)
                  REFERENCES lams_lesson_state (lesson_state_id)
     , KEY (class_grouping_id)
     , CONSTRAINT FK_lams_lesson_5 FOREIGN KEY (class_grouping_id)
                  REFERENCES lams_grouping (grouping_id)
);


CREATE TABLE lams_user_organisation (
       user_organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (user_organisation_id)
     , KEY (organisation_id)
     , CONSTRAINT FK_lams_user_organisation_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (user_id)
     , CONSTRAINT FK_lams_user_organisation_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_learner_progress (
       learner_progress_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL
     , lesson_id BIGINT(20) NOT NULL
     , lesson_completed_flag TINYINT(1) NOT NULL DEFAULT 0
     , waiting_flag TINYINT NOT NULL
     , start_date_time DATETIME NOT NULL
     , finish_date_time DATETIME
     , current_activity_id BIGINT(20)
     , next_activity_id BIGINT(20)
     , previous_activity_id BIGINT(20)
     , requires_restart_flag TINYINT(1) NOT NULL
     , PRIMARY KEY (learner_progress_id)
     , UNIQUE KEY IX_lams_learner_progress_1 (user_id,lesson_id)
     , KEY (user_id)
     , CONSTRAINT FK_lams_learner_progress_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , KEY (lesson_id)
     , CONSTRAINT FK_lams_learner_progress_2 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
     , KEY (current_activity_id)
     , CONSTRAINT FK_lams_learner_progress_3 FOREIGN KEY (current_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , KEY (next_activity_id)
     , CONSTRAINT FK_lams_learner_progress_4 FOREIGN KEY (next_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , KEY (previous_activity_id)
     , CONSTRAINT FK_lams_learner_progress_5 FOREIGN KEY (previous_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
);

CREATE TABLE lams_cr_node (
       node_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , workspace_id BIGINT(20) UNSIGNED NOT NULL
     , path VARCHAR(255)
     , type VARCHAR(255) NOT NULL
     , created_date_time DATETIME NOT NULL
     , next_version_id BIGINT(20) UNSIGNED NOT NULL DEFAULT 1
     , parent_nv_id BIGINT(20) UNSIGNED
     , PRIMARY KEY (node_id)
     , KEY (workspace_id)
     , CONSTRAINT FK_lams_cr_node_1 FOREIGN KEY (workspace_id)
                  REFERENCES lams_cr_workspace (workspace_id)
);

CREATE TABLE lams_cr_node_version (
       nv_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , node_id BIGINT(20) UNSIGNED NOT NULL
     , version_id BIGINT(20) UNSIGNED NOT NULL
     , created_date_time DATETIME NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (nv_id)
     , KEY (node_id)
     , CONSTRAINT FK_lams_cr_node_version_2 FOREIGN KEY (node_id)
                  REFERENCES lams_cr_node (node_id)
);


CREATE TABLE lams_user_organisation_role (
       user_organisation_role_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_organisation_id BIGINT(20) NOT NULL
     , role_id INT(6) NOT NULL
     , PRIMARY KEY (user_organisation_role_id)
     , KEY (role_id)
     , CONSTRAINT FK_lams_user_organisation_role_2 FOREIGN KEY (role_id)
                  REFERENCES lams_role (role_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (user_organisation_id)
     , CONSTRAINT FK_lams_user_organisation_role_3 FOREIGN KEY (user_organisation_id)
                  REFERENCES lams_user_organisation (user_organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_tool_session (
       tool_session_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , tool_session_name VARCHAR(255) NOT NULL
     , tool_session_type_id INT(3) NOT NULL
     , lesson_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL
     , tool_session_state_id INT(3) NOT NULL
     , create_date_time DATETIME NOT NULL
     , group_id BIGINT(20)
     , user_id BIGINT(20)
     , unique_key VARCHAR(128) NOT NULL
     , PRIMARY KEY (tool_session_id)
     , UNIQUE UQ_lams_tool_session_1 (unique_key)
     , KEY (tool_session_state_id)
     , CONSTRAINT FK_lams_tool_session_4 FOREIGN KEY (tool_session_state_id)
                  REFERENCES lams_tool_session_state (tool_session_state_id)
     , KEY (user_id)
     , CONSTRAINT FK_lams_tool_session_5 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , KEY (tool_session_type_id)
     , CONSTRAINT FK_lams_tool_session_7 FOREIGN KEY (tool_session_type_id)
                  REFERENCES lams_tool_session_type (tool_session_type_id)
     , KEY (activity_id)
     , CONSTRAINT FK_lams_tool_session_8 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , KEY (group_id)
     , CONSTRAINT FK_lams_tool_session_1 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
);

CREATE TABLE lams_progress_completed (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL
     , completed_date_time DATETIME
	 , start_date_time DATETIME
     , PRIMARY KEY (learner_progress_id, activity_id)
     , KEY (learner_progress_id)
     , CONSTRAINT FK_lams_progress_completed_1 FOREIGN KEY (learner_progress_id)
                  REFERENCES lams_learner_progress (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE
     , KEY (activity_id)
     , CONSTRAINT FK_lams_progress_completed_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_progress_attempted (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL
	 , start_date_time DATETIME
     , PRIMARY KEY (learner_progress_id, activity_id)
     , KEY (learner_progress_id)
     , CONSTRAINT FK_lams_progress_current_1 FOREIGN KEY (learner_progress_id)
                  REFERENCES lams_learner_progress (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE
     , KEY (activity_id)
     , CONSTRAINT FK_lams_progress_current_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_user_group (
       user_id BIGINT(20) NOT NULL
     , group_id BIGINT(20) NOT NULL
     , scheduled_lesson_end_date DATETIME
     , PRIMARY KEY (user_id, group_id)
     , KEY (user_id)
     , CONSTRAINT FK_lams_user_group_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , KEY (group_id)
     , CONSTRAINT FK_lams_user_group_2 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
);

CREATE TABLE lams_tool_content (
       tool_content_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , tool_id BIGINT(20) NOT NULL
     , PRIMARY KEY (tool_content_id)
     , KEY (tool_id)
     , CONSTRAINT FK_lams_tool_content_1 FOREIGN KEY (tool_id)
                  REFERENCES lams_tool (tool_id)
);

CREATE TABLE lams_gate_allowed_learners (
       user_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL
     , KEY (user_id)
     , CONSTRAINT FK_TABLE_32_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , KEY (activity_id)
     , CONSTRAINT FK_TABLE_32_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
);

CREATE TABLE lams_cr_workspace_credential (
       wc_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , workspace_id BIGINT(20) UNSIGNED NOT NULL
     , credential_id BIGINT(20) UNSIGNED NOT NULL
     , PRIMARY KEY (wc_id)
     , KEY (workspace_id)
     , CONSTRAINT FK_lams_cr_workspace_credential_1 FOREIGN KEY (workspace_id)
                  REFERENCES lams_cr_workspace (workspace_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (credential_id)
     , CONSTRAINT FK_lams_cr_workspace_credential_2 FOREIGN KEY (credential_id)
                  REFERENCES lams_cr_credential (credential_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_cr_node_version_property (
       id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , nv_id BIGINT(20) UNSIGNED NOT NULL
     , name VARCHAR(255) NOT NULL
     , value VARCHAR(255) NOT NULL
     , type TINYINT NOT NULL
     , PRIMARY KEY (id)
     , KEY (nv_id)
     , CONSTRAINT FK_lams_cr_node_version_property_1 FOREIGN KEY (nv_id)
                  REFERENCES lams_cr_node_version (nv_id)
);

CREATE TABLE lams_log_event (
       id BIGINT(20) NOT NULL AUTO_INCREMENT
     , log_event_type_id INT(5) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , occurred_date_time DATETIME NOT NULL
     , learning_design_id BIGINT(20)
     , lesson_id BIGINT(20)
     , activity_id BIGINT(20)
     , PRIMARY KEY (id)
     , KEY (occurred_date_time)
     , CONSTRAINT FK_lams_event_log_1 FOREIGN KEY (log_event_type_id)
                  REFERENCES lams_log_event_type (log_event_type_id)
	 , KEY (user_id)                  
     , CONSTRAINT FK_lams_event_log_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)           
     , CONSTRAINT FK_lams_event_log_3 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE
     , CONSTRAINT FK_lams_event_log_4 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
     , CONSTRAINT FK_lams_event_log_5 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
);

CREATE TABLE lams_workspace_folder_content (
       folder_content_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_type_id INT(3) NOT NULL
     , name VARCHAR(64) NOT NULL
     , description VARCHAR(64) NOT NULL
     , create_date_time DATETIME NOT NULL
     , last_modified_date DATETIME NOT NULL
     , workspace_folder_id BIGINT(20) NOT NULL
     , uuid BIGINT(20)
     , version_id BIGINT(20)
     , mime_type VARCHAR(10) NOT NULL
     , PRIMARY KEY (folder_content_id)
     , UNIQUE KEY unique_content_name (name, workspace_folder_id, mime_type)
     , UNIQUE KEY unique_node_version (workspace_folder_id, uuid, version_id)
     , KEY (content_type_id)
     , KEY (workspace_folder_id)
     , CONSTRAINT FK_lams_workspace_folder_content_1 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
);

CREATE TABLE lams_learning_transition (
       transition_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , transition_ui_id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , to_activity_id BIGINT(20) NOT NULL
     , from_activity_id BIGINT(20) NOT NULL
     , learning_design_id BIGINT(20)
     , create_date_time DATETIME NOT NULL
     , to_ui_id INT(11)
     , from_ui_id INT(11)
	 , transition_type TINYINT NOT NULL DEFAULT 0
     , PRIMARY KEY (transition_id)
     , KEY (from_activity_id)
     , CONSTRAINT FK_learning_transition_3 FOREIGN KEY (from_activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (to_activity_id)
     , CONSTRAINT FK_learning_transition_2 FOREIGN KEY (to_activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , KEY (learning_design_id)
     , CONSTRAINT lddefn_transition_ibfk_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
ALTER TABLE lams_learning_activity  
	  ADD CONSTRAINT FK_lams_learning_activity_15 FOREIGN KEY (transition_to_id)
                  REFERENCES lams_learning_transition (transition_id)
     , ADD CONSTRAINT FK_lams_learning_activity_16 FOREIGN KEY (transition_from_id)
                  REFERENCES lams_learning_transition (transition_id);

CREATE TABLE lams_role_privilege (
       rp_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , role_id INT(6) NOT NULL
     , privilege_id BIGINT(20) NOT NULL
     , PRIMARY KEY (rp_id)
     , KEY (privilege_id)
     , CONSTRAINT FK_lams_role_privilege_1 FOREIGN KEY (privilege_id)
                  REFERENCES lams_privilege (privilege_id)
     , KEY (role_id)
     , CONSTRAINT FK_lams_role_privilege_2 FOREIGN KEY (role_id)
                  REFERENCES lams_role (role_id)
);

CREATE TABLE lams_configuration (
       config_key VARCHAR(30) NOT NULL
     , config_value VARCHAR(255)
     , description_key VARCHAR(255)
     , header_name VARCHAR(50)
     , format VARCHAR(30)
     , required TINYINT(1) NOT NULL DEFAULT 0
     , PRIMARY KEY (config_key)
);

CREATE TABLE lams_timezone (
	   id BIGINT(20) NOT NULL AUTO_INCREMENT
     , timezone_id VARCHAR(255) NOT NULL
     , server_timezone TINYINT(1) DEFAULT 0
     , PRIMARY KEY (id)
);

CREATE TABLE lams_password_request (
       request_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL
     , request_key VARCHAR(32) NOT NULL
     , request_date DATETIME NOT NULL
     , PRIMARY KEY (request_id)
     , UNIQUE KEY IX_lams_psswd_rqst_key (request_key)
);

CREATE TABLE lams_notification_event (
       uid BIGINT NOT NULL AUTO_INCREMENT
     , scope VARCHAR(128) NOT NULL
     , name VARCHAR(128) NOT NULL
     , event_session_id BIGINT
	 , subject VARCHAR(255)
	 , message TEXT
	 , fail_time DATETIME
	 , html_format TINYINT(1) DEFAULT 0
	 , KEY (scope,name,event_session_id)
     , PRIMARY KEY (uid)
);

CREATE TABLE lams_notification_subscription (
       uid BIGINT NOT NULL AUTO_INCREMENT
     , user_id BIGINT
     , event_uid BIGINT
     , delivery_method_id TINYINT UNSIGNED
     , last_operation_message TEXT
     , PRIMARY KEY (uid)
     , CONSTRAINT EventSubscriptionsToUsers FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
     , KEY (event_uid)
     , CONSTRAINT EventSubscriptionsToEvent FOREIGN KEY (event_uid)
                  REFERENCES lams_notification_event (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_competence (
       competence_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT
     , learning_design_id BIGINT
     , description TEXT
     , title VARCHAR(191) 
	 , UNIQUE KEY (learning_design_id, title)
     , PRIMARY KEY (competence_id)
     , CONSTRAINT LearningDesignCompetenceMap FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design(learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_competence_mapping (
       competence_mapping_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT
     , competence_id BIGINT
     , activity_id BIGINT 
     , PRIMARY KEY (competence_mapping_id)
     , UNIQUE KEY (competence_id, activity_id)
	 , KEY (activity_id)
     , CONSTRAINT FK_lams_competence_mapping_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT FK_lams_competence_mapping_2 FOREIGN KEY (competence_id)
	                  REFERENCES lams_competence (competence_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE patches (
       system_name VARCHAR(30) NOT NULL
     , patch_level INTEGER(11) NOT NULL
     , patch_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , patch_in_progress CHAR(1) NOT NULL DEFAULT 'F'
     , PRIMARY KEY(system_name, patch_level)
);

CREATE TABLE lams_registration (
	   uid BIGINT NOT NULL AUTO_INCREMENT
     , site_name VARCHAR(255) NOT NULL
     , organisation VARCHAR(255) NOT NULL
     , name VARCHAR(255) NOT NULL
     , email VARCHAR(255) NOT NULL
	 , server_country VARCHAR(2) NOT NULL
	 , public_directory TINYINT(1) DEFAULT 1
	 , enable_lams_community TINYINT(1) DEFAULT 0
	 , server_key VARCHAR(255) NOT NULL
	 , server_id VARCHAR(255) NOT NULL
     , PRIMARY KEY (uid)
);

CREATE TABLE lams_planner_nodes (
	   uid BIGINT(20) NOT NULL AUTO_INCREMENT
	 , parent_uid BIGINT(20)
	 , order_id TINYINT UNSIGNED NOT NULL
	 , locked TINYINT(1) NOT NULL DEFAULT 0
	 , content_folder_id VARCHAR(32)
	 , title VARCHAR(255) NOT NULL
	 , brief_desc TEXT
	 , full_desc TEXT
	 , ld_id  BIGINT(20)
	 , user_id BIGINT(20)
	 , permissions INTEGER
	 , PRIMARY KEY (uid)
	 , UNIQUE KEY (parent_uid, order_id)
	 , CONSTRAINT FK_lams_planner_node_parent FOREIGN KEY (parent_uid)
	               REFERENCES lams_planner_nodes(uid) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT FK_lams_planner_node_user FOREIGN KEY (user_id)
	               REFERENCES lams_user(user_id) ON DELETE SET NULL ON UPDATE SET NULL
); 

CREATE TABLE lams_activity_evaluation (
	  activity_evaluation_id BIGINT(20) NOT NULL AUTO_INCREMENT
	, activity_id	BIGINT(20) NOT NULL
	, tool_output_definition VARCHAR(255) NOT NULL
	, KEY (activity_id)
	, CONSTRAINT FK_lams_activity_evaluation_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (activity_evaluation_id)
);

CREATE TABLE lams_gradebook_user_activity (
	  uid BIGINT(20) NOT NULL AUTO_INCREMENT
	, activity_id BIGINT(20) NOT NULL
	, user_id BIGINT (20) NOT NULL
	, mark DOUBLE PRECISION 
	, feedback TEXT
	, marked_in_gradebook TINYINT(1) NOT NULL DEFAULT 0
	, update_date DATETIME
	, PRIMARY KEY (uid)
	, KEY (activity_id, user_id)
	, CONSTRAINT FK_lams_gradebook_user_activity_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	, CONSTRAINT FK_lams_gradebook_user_activity_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE lams_gradebook_user_lesson (
	  uid BIGINT(20) NOT NULL AUTO_INCREMENT
	, lesson_id BIGINT(20) NOT NULL
	, user_id BIGINT (20) NOT NULL
	, mark DOUBLE PRECISION
	, feedback TEXT
    , PRIMARY KEY (uid)
	, KEY (lesson_id, user_id)
	, CONSTRAINT FK_lams_gradebook_user_lesson_1 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
	, CONSTRAINT FK_lams_gradebook_user_lesson_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_data_flow (
	  data_flow_object_id BIGINT(20) NOT NULL AUTO_INCREMENT
	, transition_id BIGINT(20) NOT NULL
	, order_id INT(11) 
	, name VARCHAR(255) NOT NULL
	, display_name VARCHAR(255)
	, tool_assigment_id INT(11)
	, PRIMARY KEY (data_flow_object_id)
	, CONSTRAINT FK_lams_learning_transition_1 FOREIGN KEY (transition_id)
                  REFERENCES lams_learning_transition (transition_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_user_disabled_tutorials (
     user_id BIGINT(20) NOT NULL
   , page_str CHAR(5) NOT NULL
   , PRIMARY KEY (user_id,page_str)
   , CONSTRAINT FK_lams_user_disabled_tutorials_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_planner_recent_learning_designs (
     user_id  BIGINT(20) NOT NULL
   , learning_design_id BIGINT(20) NOT NULL
   , last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   , PRIMARY KEY (user_id,learning_design_id)
   , CONSTRAINT FK_lams_planner_recent_learning_designs_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
   , CONSTRAINT FK_lams_planner_recent_learning_designs_2 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_planner_node_role (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , node_uid BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , role_id INT(6) NOT NULL
     , PRIMARY KEY (uid)
     , CONSTRAINT FK_planner_node_role_user FOREIGN KEY (user_id)
     		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE NO ACTION
     , CONSTRAINT FK_planner_node_role_node FOREIGN KEY (node_uid)
     		REFERENCES lams_planner_nodes (uid) ON DELETE CASCADE ON UPDATE NO ACTION
     , CONSTRAINT FK_planner_node_role_role FOREIGN KEY (role_id)
     		REFERENCES lams_role (role_id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE lams_lesson_dependency (
      lesson_id BIGINT(20)
   ,  preceding_lesson_id BIGINT(20)
   , PRIMARY KEY (lesson_id,preceding_lesson_id)
   , CONSTRAINT FK_lams_lesson_dependency_1 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , CONSTRAINT FK_lams_lesson_dependency_2 FOREIGN KEY (preceding_lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE lams_notebook_entry (
     uid BIGINT(20) NOT NULL AUTO_INCREMENT
   , external_id BIGINT(20)
   , external_id_type INT(11)
   , external_signature VARCHAR(255)
   , user_id INT(11)
   , title VARCHAR(255)
   , entry TEXT
   , create_date DATETIME
   , last_modified DATETIME
   , PRIMARY KEY (uid)
   , KEY ext_sig_user (external_id,external_id_type,external_signature,user_id)
   , KEY idx_create_date (create_date)
);


CREATE TABLE lams_presence_chat_msgs (
	  uid BIGINT NOT NULL AUTO_INCREMENT
    , lesson_id BIGINT(20)
    , from_user VARCHAR(191)
	, to_user VARCHAR(191)
	, date_sent DATETIME
	, message VARCHAR(1023)
	, PRIMARY KEY (uid)
	, KEY FK_lams_presence_chat_msgs_lesson (lesson_id)
    , KEY idx_lams_presence_chat_msgs_from (from_user)
    , KEY idx_lams_presence_chat_msgs_to (to_user)
    , CONSTRAINT FK_lams_presence_chat_msgs_lesson FOREIGN KEY (lesson_id)
    		REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE lams_ext_server_type (
	  server_type_id INT(11) NOT NULL
	, description VARCHAR(255) NOT NULL
	, PRIMARY KEY (server_type_id)
);

CREATE TABLE lams_ext_server_org_map (
    sid INT(11) NOT NULL AUTO_INCREMENT
  , serverid VARCHAR(191) NOT NULL
  , serverkey TEXT NOT NULL
  , servername VARCHAR(255) NOT NULL
  , serverdesc TEXT
  , prefix VARCHAR(11) NOT NULL
  , userinfo_url TEXT NOT NULL
  , server_url VARCHAR(255)
  , timeout_url TEXT NOT NULL
  , lesson_finish_url TEXT
  , disabled BIT(1) NOT NULL
  , time_to_live_login_request INT(11) DEFAULT 80
  , time_to_live_login_request_enabled tinyINT(1) NOT NULL DEFAULT 0
  , ext_groups_url TEXT
  , server_type_id INT(11) NOT NULL DEFAULT 1
  , lti_consumer_monitor_roles TEXT
  , PRIMARY KEY (sid)
  , UNIQUE KEY serverid (serverid)
  , UNIQUE KEY prefix (prefix)
  , KEY FK_lams_ext_server_type (server_type_id)
  , CONSTRAINT FK_lams_ext_server_type FOREIGN KEY (server_type_id)
  		REFERENCES lams_ext_server_type (server_type_id)
);

CREATE TABLE lams_ext_course_class_map (
    sid INT(11) NOT NULL AUTO_INCREMENT
  , courseid VARCHAR(255) NOT NULL
  , classid BIGINT(20) NOT NULL
  , ext_server_org_map_id INT(11) NOT NULL
  , PRIMARY KEY (sid)
  , KEY classid (classid)
  , KEY ext_server_org_map_id (ext_server_org_map_id)
  , CONSTRAINT lams_ext_course_class_map_fk FOREIGN KEY (classid)
  		REFERENCES lams_organisation (organisation_id)
  , CONSTRAINT lams_ext_course_class_map_fk1 FOREIGN KEY (ext_server_org_map_id)
  		REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE lams_ext_user_userid_map (
    sid INT(11) NOT NULL AUTO_INCREMENT
  , external_username VARCHAR(250) NOT NULL
  , user_id BIGINT(20) NOT NULL
  , ext_server_org_map_id INT(11) NOT NULL
  , tc_gradebook_id TEXT
  , PRIMARY KEY (sid)
  , KEY user_id (user_id)
  , KEY ext_server_org_map_id (ext_server_org_map_id)
  , CONSTRAINT lams_ext_user_userid_map_fk FOREIGN KEY (user_id)
  		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
  , CONSTRAINT lams_ext_user_userid_map_fk1 FOREIGN KEY (ext_server_org_map_id)
  		REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_ext_server_tool_map (
    uid BIGINT(20) NOT NULL AUTO_INCREMENT
  , tool_id BIGINT(20) NOT NULL
  , ext_server_org_map_id INT(11) NOT NULL
  , PRIMARY KEY (uid)
  , UNIQUE KEY unique_adapter_map (ext_server_org_map_id,tool_id)
  , KEY lams_ext_server_tool_map_fk2 (tool_id)
  , CONSTRAINT lams_ext_server_tool_map_fk1 FOREIGN KEY (ext_server_org_map_id)
  		REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE
  , CONSTRAINT lams_ext_server_tool_map_fk2 FOREIGN KEY (tool_id)
  		REFERENCES lams_tool (tool_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_ext_server_lesson_map (
    uid BIGINT(20) NOT NULL AUTO_INCREMENT
  , lesson_id BIGINT(20) NOT NULL
  , ext_server_org_map_id INT(11) NOT NULL
  , resource_link_id VARCHAR(255)
  , PRIMARY KEY (uid)
  , UNIQUE KEY lesson_id (lesson_id)
  , KEY lams_ext_server_lesson_map_fk1 (ext_server_org_map_id)
  , CONSTRAINT lams_ext_server_lesson_map_fk1 FOREIGN KEY (ext_server_org_map_id)
  		REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE
  , CONSTRAINT lams_ext_server_lesson_map_fk2 FOREIGN KEY (lesson_id)
  		REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_signup_organisation (
  signup_organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  organisation_id BIGINT(20) NOT NULL,
  add_to_lessons TINYINT(1) DEFAULT 1,
  add_as_staff TINYINT(1) DEFAULT 0,
  add_with_author TINYINT(1) DEFAULT 0,
  add_with_monitor TINYINT(1) DEFAULT 0,
  course_key VARCHAR(255),
  blurb TEXT,
  create_date DATETIME,
  disabled TINYINT(1) DEFAULT 0,
  conTEXT VARCHAR(191) NOT NULL,
  login_tab_active TINYINT(1) DEFAULT 0,
  PRIMARY KEY (signup_organisation_id),
  UNIQUE KEY conTEXT (conTEXT),
  KEY organisation_id (organisation_id)
);

CREATE TABLE lams_presence_user (
  nickname VARCHAR(191) NOT NULL,
  lesson_id BIGINT(20) NOT NULL,
  last_presence DATETIME,
  PRIMARY KEY (nickname,lesson_id),
  KEY FK_lams_presence_user_lesson (lesson_id),
  CONSTRAINT FK_lams_presence_user_lesson FOREIGN KEY (lesson_id)
  		REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_organisation_grouping (
  grouping_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  organisation_id BIGINT(20) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (grouping_id),
  KEY FK_lams_organisation_grouping_1 (organisation_id),
  CONSTRAINT FK_lams_organisation_grouping_1 FOREIGN KEY (organisation_id)
  	REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_organisation_group (
  group_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  grouping_id BIGINT(20) NOT NULL,
  name VARCHAR(255),
  PRIMARY KEY (group_id),
  KEY FK_lams_organisation_group_1 (grouping_id),
  CONSTRAINT FK_lams_organisation_group_1 FOREIGN KEY (grouping_id)
  	REFERENCES lams_organisation_grouping (grouping_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_user_organisation_group (
  group_id BIGINT(20) NOT NULL,
  user_id BIGINT(20) NOT NULL,
  PRIMARY KEY (group_id,user_id),
  KEY FK_lams_user_organisation_group_2 (user_id),
  CONSTRAINT FK_lams_user_organisation_group_1 FOREIGN KEY (group_id)
  	REFERENCES lams_organisation_group (group_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_user_organisation_group_2 FOREIGN KEY (user_id)
  	REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_learning_design_annotation (
  uid BIGINT(20) NOT NULL AUTO_INCREMENT,
  learning_design_id BIGINT(20) NOT NULL,
  ui_id INT(11),
  title VARCHAR(1024),
  xcoord INT(11),
  ycoord INT(11),
  end_xcoord INT(11),
  end_ycoord INT(11),
  color char(7),
  PRIMARY KEY (uid),
  KEY FK_lams_learning_design_annotation_1 (learning_design_id),
  CONSTRAINT FK_lams_learning_design_annotation_1 FOREIGN KEY (learning_design_id)
  		REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_learning_design_access (
  learning_design_id BIGINT(20) NOT NULL,
  user_id BIGINT(20) NOT NULL,
  access_date DATETIME,
  PRIMARY KEY (learning_design_id,user_id),
  KEY FK_lams_learning_design_access_2 (user_id),
  CONSTRAINT FK_lams_learning_design_access_1 FOREIGN KEY (learning_design_id)
  		REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_learning_design_access_2 FOREIGN KEY (user_id)
  		REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_learning_library_group (
  group_id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(64) NOT NULL,
  PRIMARY KEY (group_id)
);

CREATE TABLE lams_learning_library_to_group (
  group_id INT(11) NOT NULL,
  learning_library_id BIGINT(20) NOT NULL,
  PRIMARY KEY (group_id,learning_library_id)
);

CREATE TABLE lams_rating_criteria_type (
  rating_criteria_type_id INT(11) NOT NULL,
  description VARCHAR(255) NOT NULL,
  PRIMARY KEY (rating_criteria_type_id)
);

CREATE TABLE lams_rating_criteria (
  rating_criteria_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  title VARCHAR(255),
  rating_criteria_type_id INT(11) NOT NULL DEFAULT 0,
  comments_enabled TINYINT(1) NOT NULL DEFAULT 0,
  comments_min_words_limit INT(11) DEFAULT 0,
  order_id INT(11) NOT NULL,
  tool_content_id BIGINT(20),
  item_id BIGINT(20),
  lesson_id BIGINT(20),
  rating_style BIGINT(20) NOT NULL DEFAULT 1,
  max_rating BIGINT(20) NOT NULL DEFAULT '5',
  minimum_rates INT(11) DEFAULT 0,
  maximum_rates INT(11) DEFAULT 0,
  PRIMARY KEY (rating_criteria_id),
  KEY rating_criteria_type_id (rating_criteria_type_id),
  KEY tool_content_id (tool_content_id),
  KEY lesson_id (lesson_id),
  CONSTRAINT FK_lams_rating_criteria_1 FOREIGN KEY (rating_criteria_type_id)
  	REFERENCES lams_rating_criteria_type (rating_criteria_type_id),
  CONSTRAINT FK_lams_rating_criteria_2 FOREIGN KEY (tool_content_id)
  	REFERENCES lams_tool_content (tool_content_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_lams_rating_criteria_3 FOREIGN KEY (lesson_id)
  	REFERENCES lams_lesson (lesson_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE lams_comment_session (
  uid BIGINT(20) NOT NULL AUTO_INCREMENT,
  external_id BIGINT(20),
  external_id_type INT(1),
  external_signature VARCHAR(40),
  PRIMARY KEY (uid),
  UNIQUE KEY comment_ext_sig_user (external_id,external_id_type,external_signature)
);

CREATE TABLE lams_comment (
  uid BIGINT(20) NOT NULL AUTO_INCREMENT,
  session_id BIGINT(20) NOT NULL,
  body TEXT,
  create_by BIGINT(20),
  create_date DATETIME,
  update_date DATETIME,
  update_by BIGINT(20),
  last_modified DATETIME,
  last_reply_date DATETIME,
  reply_number INT(11),
  hide_flag SMALLINT(6),
  parent_uid BIGINT(20),
  root_comment_uid BIGINT(20),
  comment_level SMALLINT(6),
  thread_comment_uid BIGINT(20),
  sticky SMALLINT(6) DEFAULT 0,
  monitor SMALLINT(6) DEFAULT 0,
  PRIMARY KEY (uid),
  KEY FK_comment_session (session_id),
  KEY FK_comment_create (create_by),
  KEY FK_comment_modify (update_by),
  KEY FK_comment_parent (parent_uid),
  KEY FK_comment_root (root_comment_uid),
  KEY FK_comment_thread (thread_comment_uid),
  KEY IX_comment_level_sticky (comment_level,sticky),
  CONSTRAINT FK_comment_create FOREIGN KEY (create_by)
  	REFERENCES lams_user (user_id),
  CONSTRAINT FK_comment_modify FOREIGN KEY (update_by)
  	REFERENCES lams_user (user_id),
  CONSTRAINT FK_comment_parent FOREIGN KEY (parent_uid)
  	REFERENCES lams_comment (uid),
  CONSTRAINT FK_comment_root FOREIGN KEY (root_comment_uid)
  	REFERENCES lams_comment (uid),
  CONSTRAINT FK_comment_session FOREIGN KEY (session_id)
  	REFERENCES lams_comment_session (uid),
  CONSTRAINT FK_comment_thread FOREIGN KEY (thread_comment_uid)
  	REFERENCES lams_comment (uid)
);

CREATE TABLE lams_comment_likes (
  uid BIGINT(20) NOT NULL AUTO_INCREMENT,
  comment_uid BIGINT(20) NOT NULL,
  user_id BIGINT(20) NOT NULL,
  vote TINYINT(1),
  PRIMARY KEY (uid),
  UNIQUE KEY comment_like_unique (comment_uid,user_id),
  KEY FK_commentlike_comment (comment_uid),
  KEY FK_commentlike_user (user_id),
  CONSTRAINT FK_commentlike_comment FOREIGN KEY (comment_uid)
  	REFERENCES lams_comment (uid),
  CONSTRAINT FK_commentlike_user FOREIGN KEY (user_id)
  	REFERENCES lams_user (user_id)
);

CREATE TABLE lams_learner_progress_archive (
  learner_progress_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL,
  lesson_id BIGINT(20) NOT NULL,
  attempt_id TINYINT(4) NOT NULL DEFAULT 1,
  lesson_completed_flag TINYINT(1) NOT NULL DEFAULT 0,
  start_date_time DATETIME NOT NULL,
  finish_date_time DATETIME,
  current_activity_id BIGINT(20),
  PRIMARY KEY (learner_progress_id),
  UNIQUE KEY IX_lams_learner_progress_archive_1 (user_id,lesson_id,attempt_id),
  KEY FK_lams_learner_progress_archive_2 (lesson_id),
  KEY FK_lams_learner_progress_archive_3 (current_activity_id),
  CONSTRAINT FK_lams_learner_progress_archive_1 FOREIGN KEY (user_id)
  	REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_learner_progress_archive_2 FOREIGN KEY (lesson_id)
  	REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_learner_progress_archive_3 FOREIGN KEY (current_activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE lams_progress_attempted_archive (
  learner_progress_id BIGINT(20) NOT NULL,
  activity_id BIGINT(20) NOT NULL,
  start_date_time DATETIME,
  PRIMARY KEY (learner_progress_id,activity_id),
  KEY FK_lams_progress_current_archive_2 (activity_id),
  CONSTRAINT FK_lams_progress_current_archive_1 FOREIGN KEY (learner_progress_id)
  	REFERENCES lams_learner_progress_archive (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_progress_current_archive_2 FOREIGN KEY (activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_progress_completed_archive (
  learner_progress_id BIGINT(20) NOT NULL,
  activity_id BIGINT(20) NOT NULL,
  completed_date_time DATETIME,
  start_date_time DATETIME,
  PRIMARY KEY (learner_progress_id,activity_id),
  KEY FK_lams_progress_completed_archive_2 (activity_id),
  CONSTRAINT FK_lams_progress_completed_archive_1 FOREIGN KEY (learner_progress_id)
  	REFERENCES lams_learner_progress_archive (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_progress_completed_archive_2 FOREIGN KEY (activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_learning_command (
  uid BIGINT(20) NOT NULL AUTO_INCREMENT,
  lesson_id BIGINT(20),
  user_name VARCHAR(191),
  create_date DATETIME NOT NULL,
  command_TEXT TEXT,
  PRIMARY KEY (uid),
  KEY idx_lesson_id (lesson_id),
  KEY idx_user_name (user_name),
  KEY idx_create_date (create_date)
);

CREATE TABLE lams_favorite_organisation (
  favorite_organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  organisation_id BIGINT(20) NOT NULL,
  user_id BIGINT(20) NOT NULL,
  PRIMARY KEY (favorite_organisation_id),
  KEY organisation_id (organisation_id),
  KEY user_id (user_id),
  CONSTRAINT FK_lams_favorite_organisation_1 FOREIGN KEY (organisation_id)
  	REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_lams_favorite_organisation_2 FOREIGN KEY (user_id)
  	REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);