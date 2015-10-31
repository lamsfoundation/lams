
CREATE TABLE lams_gate_activity_level (
       gate_activity_level_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (gate_activity_level_id)
)ENGINE=InnoDB;

CREATE TABLE lams_grouping_type (
       grouping_type_id INT(11) NOT NULL
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (grouping_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_learning_activity_type (
       learning_activity_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (learning_activity_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_learning_library (
       learning_library_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , description TEXT
     , title VARCHAR(255)
     , valid_flag TINYINT(1) NOT NULL DEFAULT 1
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (learning_library_id)
)ENGINE=InnoDB;

CREATE TABLE lams_organisation_type (
       organisation_type_id INT(3) NOT NULL
     , name VARCHAR(64) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (organisation_type_id)
)ENGINE=InnoDB;
CREATE UNIQUE INDEX UQ_lams_organisation_type_name ON lams_organisation_type (name ASC);

CREATE TABLE lams_role (
       role_id INT(6) NOT NULL DEFAULT 0
     , name VARCHAR(64) NOT NULL
     , description TEXT
     , create_date DATETIME NOT NULL
     , PRIMARY KEY (role_id)
)ENGINE=InnoDB;
CREATE INDEX gname ON lams_role (name ASC);

CREATE TABLE lams_tool_session_state (
       tool_session_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (tool_session_state_id)
)ENGINE=InnoDB;

CREATE TABLE lams_lesson_state (
       lesson_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (lesson_state_id)
)ENGINE=InnoDB;

CREATE TABLE lams_tool_session_type (
       tool_session_type_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (tool_session_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_license (
       license_id BIGINT(20) NOT NULL
     , name VARCHAR(200) NOT NULL
     , code VARCHAR(20) NOT NULL
     , url VARCHAR(256)
     , default_flag TINYINT(1) NOT NULL DEFAULT 0
     , picture_url VARCHAR(256)
     , PRIMARY KEY (license_id)
)ENGINE=InnoDB;

CREATE TABLE lams_copy_type (
       copy_type_id TINYINT(4) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (copy_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_activity_category (
       activity_category_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (activity_category_id)
)ENGINE=InnoDB;

CREATE TABLE lams_cr_workspace (
       workspace_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , name VARCHAR(255) NOT NULL
     , PRIMARY KEY (workspace_id)
)ENGINE=InnoDB;
ALTER TABLE lams_cr_workspace COMMENT='Content repository workspace';

CREATE TABLE lams_cr_credential (
       credential_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , name VARCHAR(255) NOT NULL
     , password VARCHAR(255) NOT NULL
     , PRIMARY KEY (credential_id)
)ENGINE=InnoDB;
ALTER TABLE lams_cr_credential COMMENT='Records the identification properties for a tool.';

CREATE TABLE lams_workspace_folder_type (
       lams_workspace_folder_type_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (lams_workspace_folder_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_grouping_support_type (
       grouping_support_type_id INT(3) NOT NULL
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (grouping_support_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_log_event_type (
       log_event_type_id INT(5) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (log_event_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_wkspc_fld_content_type (
       content_type_id INT(3) NOT NULL AUTO_INCREMENT
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (content_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_privilege (
       privilege_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , code VARCHAR(10) NOT NULL
     , description VARCHAR(255)
     , PRIMARY KEY (privilege_id)
)ENGINE=InnoDB;
CREATE UNIQUE INDEX IX_lams_privilege_code ON lams_privilege (code ASC);

CREATE TABLE lams_organisation_state (
       organisation_state_id INT(3) NOT NULL
     , description VARCHAR(255)
     , PRIMARY KEY (organisation_state_id)
)ENGINE=InnoDB;

CREATE TABLE lams_supported_locale (
       locale_id INTEGER NOT NULL AUTO_INCREMENT
     , language_iso_code VARCHAR(2) NOT NULL
     , country_iso_code VARCHAR(2)
     , description VARCHAR(255) NOT NULL
     , direction VARCHAR(3) NOT NULL
	 , fckeditor_code VARCHAR(10)
     , PRIMARY KEY (locale_id)
)ENGINE=InnoDB;
ALTER TABLE lams_supported_locale COMMENT='Describes the valid language/country combinations.'; 
ALTER TABLE lams_supported_locale MODIFY COLUMN language_iso_code VARCHAR(2) NOT NULL 
	COMMENT 'ISO 639-1 Language Code (2 letter version) Java only supports 2 letter properly, not the 3 letter codes.'; 
ALTER TABLE lams_supported_locale MODIFY COLUMN country_iso_code VARCHAR(2) 
	COMMENT 'ISO 3166 Country Code. Cannot use in unique key as allows null.';

CREATE TABLE lams_auth_method_type (
       authentication_method_type_id INT(3) NOT NULL
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (authentication_method_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_authentication_method (
       authentication_method_id BIGINT(20) NOT NULL
     , authentication_method_type_id INT(3) NOT NULL DEFAULT 0
     , authentication_method_name VARCHAR(255) NOT NULL
     , UNIQUE UQ_lams_authentication_method_1 (authentication_method_name)
     , PRIMARY KEY (authentication_method_id)
     , INDEX (authentication_method_type_id)
     , CONSTRAINT FK_lams_authorization_method_1 FOREIGN KEY (authentication_method_type_id)
                  REFERENCES lams_auth_method_type (authentication_method_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE lams_workspace_folder (
       workspace_folder_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , parent_folder_id BIGINT(20)
     , name VARCHAR(255) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , create_date_time DATETIME NOT NULL
     , last_modified_date_time DATETIME
     , lams_workspace_folder_type_id INT(3) NOT NULL
     , PRIMARY KEY (workspace_folder_id)
     , INDEX (parent_folder_id)
     , CONSTRAINT FK_lams_workspace_folder_2 FOREIGN KEY (parent_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (lams_workspace_folder_type_id)
     , CONSTRAINT FK_lams_workspace_folder_4 FOREIGN KEY (lams_workspace_folder_type_id)
                  REFERENCES lams_workspace_folder_type (lams_workspace_folder_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_workspace (
       workspace_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , default_fld_id BIGINT(20) 
     , def_run_seq_fld_id BIGINT(20)
     , name VARCHAR(255)
     , PRIMARY KEY (workspace_id)
     , INDEX (def_run_seq_fld_id)
     , CONSTRAINT FK_lams_workspace_1 FOREIGN KEY (def_run_seq_fld_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
     , INDEX (default_fld_id)
     , CONSTRAINT FK_lams_workspace_2 FOREIGN KEY (default_fld_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
)ENGINE=InnoDB;

CREATE TABLE lams_wkspc_wkspc_folder (
       id BIGINT(20) NOT NULL AUTO_INCREMENT
     , workspace_id BIGINT(20) 
     , workspace_folder_id BIGINT(20) 
     , PRIMARY KEY (id)
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_ww_folder_1 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id)
     , INDEX (workspace_folder_id)
     , CONSTRAINT FK_lams_ww_folder_2 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
)ENGINE=InnoDB;

CREATE TABLE lams_organisation (
       organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , name VARCHAR(250) NOT NULL
     , code VARCHAR(20)
     , description VARCHAR(250)
     , parent_organisation_id BIGINT(20)
     , organisation_type_id INT(3) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL
     , created_by BIGINT(20) NOT NULL
     , workspace_id BIGINT(20)
     , organisation_state_id INT(3) NOT NULL
     , admin_add_new_users TINYINT(1) NOT NULL DEFAULT 0
     , admin_browse_all_users TINYINT(1) NOT NULL DEFAULT 0
     , admin_change_status TINYINT(1) NOT NULL DEFAULT 0
     , admin_create_guest TINYINT(1) NOT NULL DEFAULT 0
     , enable_course_notifications TINYINT(1) NOT NULL DEFAULT 0
	 , enable_monitor_gradebook TINYINT(1) NOT NULL DEFAULT 0
     , enable_learner_gradebook TINYINT(1) NOT NULL DEFAULT 0
     , locale_id INTEGER
     , archived_date DATETIME
     , ordered_lesson_ids TEXT
     , PRIMARY KEY (organisation_id)
     , INDEX (organisation_type_id)
     , CONSTRAINT FK_lams_organisation_1 FOREIGN KEY (organisation_type_id)
                  REFERENCES lams_organisation_type (organisation_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_organisation_2 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (parent_organisation_id)
     , CONSTRAINT FK_lams_organisation_3 FOREIGN KEY (parent_organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (organisation_state_id)
     , CONSTRAINT FK_lams_organisation_4 FOREIGN KEY (organisation_state_id)
                  REFERENCES lams_organisation_state (organisation_state_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (locale_id)
     , CONSTRAINT FK_lams_organisation_5 FOREIGN KEY (locale_id)
                  REFERENCES lams_supported_locale (locale_id)
)ENGINE=InnoDB;

CREATE TABLE lams_theme (
       theme_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , name VARCHAR(100) NOT NULL
     , description VARCHAR(100)
     , image_directory VARCHAR(100)
     , theme_type TINYINT(11)
     , PRIMARY KEY (theme_id)
     , UNIQUE UQ_name (name)
)ENGINE=InnoDB;

CREATE TABLE lams_user (
       user_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , login VARCHAR(255) NOT NULL
     , password VARCHAR(50) NOT NULL
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
     , workspace_id BIGINT(20)
     , flash_theme_id BIGINT(20)
     , html_theme_id BIGINT(20)
     , chat_id VARCHAR(255)
     , locale_id INTEGER
     , portrait_uuid BIGINT(20)
     , change_password TINYINT DEFAULT 0
     , enable_flash TINYINT(1) DEFAULT 1
	 , lams_community_token VARCHAR(255)
 	 , lams_community_username VARCHAR(255)
 	 , timezone VARCHAR(255)
	 , tutorials_disabled TINYINT(1) DEFAULT 0
	 , first_login TINYINT(1) DEFAULT 1
     , modified_date DATETIME
     , openid_url VARCHAR(255) UNIQUE
     , PRIMARY KEY (user_id)
     , INDEX (authentication_method_id)
     , CONSTRAINT FK_lams_user_1 FOREIGN KEY (authentication_method_id)
                  REFERENCES lams_authentication_method (authentication_method_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (flash_theme_id)
     , CONSTRAINT FK_lams_user_4 FOREIGN KEY (flash_theme_id)
                  REFERENCES lams_theme (theme_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (html_theme_id)
     , CONSTRAINT FK_lams_user_5 FOREIGN KEY (html_theme_id)
                  REFERENCES lams_theme (theme_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (locale_id)
     , CONSTRAINT FK_lams_user_6 FOREIGN KEY (locale_id)
                  REFERENCES lams_supported_locale (locale_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX idx_openid_url (openid_url)
)ENGINE=InnoDB;
ALTER TABLE lams_user MODIFY COLUMN chat_id VARCHAR(255)
      COMMENT 'ID used for Jabber';
CREATE UNIQUE INDEX UQ_lams_user_login ON lams_user (login ASC);
CREATE INDEX login ON lams_user (login ASC);
CREATE INDEX email ON lams_user (email ASC);

CREATE TABLE lams_learning_design (
       learning_design_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_design_ui_id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , first_activity_id BIGINT(20)
     , floating_activity_id BIGINT(20)
     , max_id INT(11)
     , valid_design_flag TINYINT(4) NOT NULL
     , read_only_flag TINYINT(4) NOT NULL
     , date_read_only DATETIME
     , user_id BIGINT(20) NOT NULL
     , original_user_id BIGINT(20) NOT NULL
     , help_text TEXT
     , online_instructions TEXT
     , offline_instructions TEXT
     , copy_type_id TINYINT(4) NOT NULL
     , create_date_time DATETIME NOT NULL
     , version VARCHAR(56)
     , original_learning_design_id BIGINT(20)
     , workspace_folder_id BIGINT(20)
     , duration BIGINT(38)
     , license_id BIGINT(20)
     , license_text TEXT
     , last_modified_date_time DATETIME
     , content_folder_id VARCHAR(32)
     , edit_override_lock TINYINT DEFAULT 0
     , edit_override_user_id BIGINT(20)
     , design_version INTEGER DEFAULT 1
     , PRIMARY KEY (learning_design_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_learning_design_3 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (workspace_folder_id)
     , CONSTRAINT FK_lams_learning_design_4 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
     , INDEX (license_id)
     , CONSTRAINT FK_lams_learning_design_5 FOREIGN KEY (license_id)
                  REFERENCES lams_license (license_id)
     , INDEX (copy_type_id)
     , CONSTRAINT FK_lams_learning_design_6 FOREIGN KEY (copy_type_id)
                  REFERENCES lams_copy_type (copy_type_id)
     , INDEX (edit_override_user_id)
     , CONSTRAINT FK_lams_learning_design_7 FOREIGN KEY (edit_override_user_id)
                  REFERENCES lams_user (user_id)
)ENGINE=InnoDB;
CREATE INDEX idx_design_parent_id ON lams_learning_design (original_learning_design_id ASC);
CREATE INDEX idx_design_first_act ON lams_learning_design (first_activity_id ASC);
CREATE INDEX idx_design_floating_act ON lams_learning_design (floating_activity_id ASC);

CREATE TABLE lams_grouping (
       grouping_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , grouping_ui_id INT(11)
     , grouping_type_id INT(11) NOT NULL
     , number_of_groups INT(11)
     , learners_per_group INT(11)
     , staff_group_id BIGINT(20) DEFAULT 0
     , max_number_of_groups INT(3)
	 , equal_number_of_learners_per_group TINYINT DEFAULT 0
	 , view_students_before_selection TINYINT DEFAULT 0
     , PRIMARY KEY (grouping_id)
     , INDEX (grouping_type_id)
     , CONSTRAINT FK_lams_learning_grouping_1 FOREIGN KEY (grouping_type_id)
                  REFERENCES lams_grouping_type (grouping_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE lams_group (
       group_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , group_name VARCHAR(255) NOT NULL
     , grouping_id BIGINT(20) NOT NULL
     , order_id INT(6) NOT NULL DEFAULT 1
     , group_ui_id INT(11)
     , PRIMARY KEY (group_id)
     , INDEX (grouping_id)
     , CONSTRAINT FK_lams_learning_group_1 FOREIGN KEY (grouping_id)
                  REFERENCES lams_grouping (grouping_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE lams_system_tool (
       system_tool_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_activity_type_id INT(11) NOT NULL
     , tool_display_name VARCHAR(255) NOT NULL
     , description TEXT
     , learner_url TEXT
     , learner_preview_url TEXT
     , learner_progress_url TEXT
     , export_pfolio_learner_url TEXT
     , export_pfolio_class_url TEXT
     , monitor_url TEXT
     , contribute_url TEXT
     , help_url TEXT
     , create_date_time DATETIME NOT NULL
     , admin_url TEXT
	 , pedagogical_planner_url TEXT
     , UNIQUE UQ_systool_activity_type (learning_activity_type_id)
     , PRIMARY KEY (system_tool_id)
     , INDEX (learning_activity_type_id)
     , CONSTRAINT FK_lams_system_tool FOREIGN KEY (learning_activity_type_id)
                  REFERENCES lams_learning_activity_type (learning_activity_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;
ALTER TABLE lams_system_tool MODIFY COLUMN learner_preview_url TEXT
      COMMENT 'Learner screen for preview a learning design. ';
ALTER TABLE lams_system_tool MODIFY COLUMN learner_progress_url TEXT
      COMMENT 'Teacher''s view of a learner''s screen.';
            
CREATE TABLE lams_tool (
       tool_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , tool_signature VARCHAR(64) NOT NULL
     , service_name VARCHAR(255) NOT NULL
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
     , export_pfolio_learner_url TEXT 
     , export_pfolio_class_url TEXT
     , monitor_url TEXT 
     , contribute_url TEXT
     , moderation_url TEXT
	 , pedagogical_planner_url TEXT
     , help_url TEXT
     , create_date_time DATETIME NOT NULL
     , language_file VARCHAR(255)
     , modified_date_time DATETIME
     , admin_url TEXT
     , supports_outputs TINYINT(1) DEFAULT 0
	 , ext_lms_id VARCHAR(255) DEFAULT NULL
     , UNIQUE UQ_lams_tool_sig (tool_signature)
     , UNIQUE UQ_lams_tool_class_name (service_name)
     , PRIMARY KEY (tool_id)
     , INDEX (learning_library_id)
     , CONSTRAINT FK_lams_tool_1 FOREIGN KEY (learning_library_id)
                  REFERENCES lams_learning_library (learning_library_id)
     , INDEX (grouping_support_type_id)
     , CONSTRAINT FK_lams_tool_2 FOREIGN KEY (grouping_support_type_id)
                  REFERENCES lams_grouping_support_type (grouping_support_type_id)
)ENGINE=InnoDB;
ALTER TABLE lams_tool MODIFY COLUMN learner_preview_url TEXT
      COMMENT 'Learner screen for preview a learning design. ';
ALTER TABLE lams_tool MODIFY COLUMN learner_progress_url TEXT
      COMMENT 'Teacher''s view of a learner''s screen.';
      
CREATE TABLE lams_learning_activity (
       activity_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , activity_ui_id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , help_text TEXT
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
     , define_later_flag TINYINT(4) NOT NULL DEFAULT 0
     , learning_design_id BIGINT(20)
     , learning_library_id BIGINT(20)
     , create_date_time DATETIME NOT NULL
     , run_offline_flag TINYINT(1) NOT NULL
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
     , gate_start_date_time DATETIME
     , gate_end_date_time DATETIME
     , library_activity_ui_image VARCHAR(255)
     , create_grouping_id BIGINT(20)
     , create_grouping_ui_id INT(11)
     , library_activity_id BIGINT(20)
     , language_file VARCHAR(255)
     , system_tool_id BIGINT(20)
     , read_only TINYINT DEFAULT 0
     , initialised TINYINT DEFAULT 0
     , default_activity_id BIGINT(20)
     , start_xcoord INT(11)
     , start_ycoord INT(11)
     , end_xcoord INT(11)
     , end_ycoord INT(11) 
     , stop_after_activity TINYINT NOT NULL DEFAULT 0
	 , transition_to_id BIGINT(20)
	 , transition_from_id BIGINT(20)
     , PRIMARY KEY (activity_id)
     , INDEX (learning_library_id)
     , CONSTRAINT FK_lams_learning_activity_7 FOREIGN KEY (learning_library_id)
                  REFERENCES lams_learning_library (learning_library_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (learning_design_id)
     , CONSTRAINT FK_lams_learning_activity_6 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (parent_activity_id)
     , CONSTRAINT FK_learning_activity_2 FOREIGN KEY (parent_activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (learning_activity_type_id)
     , CONSTRAINT FK_learning_activity_3 FOREIGN KEY (learning_activity_type_id)
                  REFERENCES lams_learning_activity_type (learning_activity_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (grouping_id)
     , CONSTRAINT FK_learning_activity_6 FOREIGN KEY (grouping_id)
                  REFERENCES lams_grouping (grouping_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (tool_id)
     , CONSTRAINT FK_lams_learning_activity_8 FOREIGN KEY (tool_id)
                  REFERENCES lams_tool (tool_id)
     , INDEX (gate_activity_level_id)
     , CONSTRAINT FK_lams_learning_activity_10 FOREIGN KEY (gate_activity_level_id)
                  REFERENCES lams_gate_activity_level (gate_activity_level_id)
     , INDEX (create_grouping_id)
     , CONSTRAINT FK_lams_learning_activity_9 FOREIGN KEY (create_grouping_id)
                  REFERENCES lams_grouping (grouping_id)
     , INDEX (library_activity_id)
     , CONSTRAINT FK_lams_learning_activity_11 FOREIGN KEY (library_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (activity_category_id)
     , CONSTRAINT FK_lams_learning_activity_12 FOREIGN KEY (activity_category_id)
                  REFERENCES lams_activity_category (activity_category_id)
     , INDEX (grouping_support_type_id)
     , CONSTRAINT FK_lams_learning_activity_13 FOREIGN KEY (grouping_support_type_id)
                  REFERENCES lams_grouping_support_type (grouping_support_type_id)
     , INDEX (system_tool_id)
     , CONSTRAINT FK_lams_learning_activity_14 FOREIGN KEY (system_tool_id)
                  REFERENCES lams_system_tool (system_tool_id)
)ENGINE=InnoDB;

CREATE TABLE lams_planner_activity_metadata (
       activity_id BIGINT(20) NOT NULL
	 , collapsed TINYINT(1) DEFAULT 0
	 , expanded TINYINT(1) DEFAULT 0
	 , hidden TINYINT(1) DEFAULT 0
	 , editing_advice VARCHAR(255)
     , CONSTRAINT FK_lams_planner_metadata_primary FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_input_activity (
       activity_id BIGINT(20) NOT NULL
     , input_activity_id BIGINT(20) NOT NULL
     , UNIQUE UQ_lams_input_activity_1 (activity_id, input_activity_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_input_activity_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_input_activity_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)ENGINE=InnoDB;

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
)ENGINE=InnoDB;

CREATE TABLE lams_text_search_condition (
       condition_id BIGINT(20) NOT NULL
	 , text_search_all_words TEXT
	 , text_search_phrase TEXT
	 , text_search_any_words TEXT
	 , text_search_excluded_words TEXT
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT TextSearchConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_branch_activity_entry (
       entry_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , entry_ui_id INT(11)
     , group_id BIGINT(20)
     , sequence_activity_id BIGINT(20)
     , branch_activity_id BIGINT(20) NOT NULL
     , condition_id BIGINT(20)
	 , open_gate TINYINT
     , PRIMARY KEY (entry_id)
     , INDEX (group_id)
     , CONSTRAINT FK_lams_group_activity_1 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
     , INDEX (sequence_activity_id)
     , CONSTRAINT FK_lams_branch_map_sequence FOREIGN KEY (sequence_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (branch_activity_id)
     , CONSTRAINT FK_lams_branch_map_branch FOREIGN KEY (branch_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (condition_id)
     , CONSTRAINT FK_lams_branch_activity_entry_4 FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition (condition_id)
)ENGINE=InnoDB;



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
     , scheduled_number_days_to_lesson_finish INT(3) DEFAULT NULL
     , schedule_start_date_time DATETIME
     , end_date_time DATETIME
     , schedule_end_date_time DATETIME
     , previous_state_id INT(3)
     , learner_exportport_avail TINYINT(1) DEFAULT 1
     , learner_presence_avail TINYINT(1) DEFAULT 0
     , learner_im_avail TINYINT(1) DEFAULT 0
	 , live_edit_enabled TINYINT(1) DEFAULT 0
	 , enable_lesson_notifications TINYINT(1) DEFAULT 0
     , locked_for_edit TINYINT DEFAULT 0
	 , marks_released TINYINT DEFAULT 0
     , version INTEGER DEFAULT 1
     , PRIMARY KEY (lesson_id)
     , INDEX (learning_design_id)
     , CONSTRAINT FK_lams_lesson_1_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_lesson_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (organisation_id)
     , CONSTRAINT FK_lams_lesson_3 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id)
     , INDEX (lesson_state_id)
     , CONSTRAINT FK_lams_lesson_4 FOREIGN KEY (lesson_state_id)
                  REFERENCES lams_lesson_state (lesson_state_id)
     , INDEX (class_grouping_id)
     , CONSTRAINT FK_lams_lesson_5 FOREIGN KEY (class_grouping_id)
                  REFERENCES lams_grouping (grouping_id)
)ENGINE=InnoDB;


CREATE TABLE lams_user_organisation (
       user_organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (user_organisation_id)
     , INDEX (organisation_id)
     , CONSTRAINT FK_lams_user_organisation_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (user_id)
     , CONSTRAINT FK_lams_user_organisation_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

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
     , INDEX (user_id)
     , CONSTRAINT FK_lams_learner_progress_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (lesson_id)
     , CONSTRAINT FK_lams_learner_progress_2 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
     , INDEX (current_activity_id)
     , CONSTRAINT FK_lams_learner_progress_3 FOREIGN KEY (current_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (next_activity_id)
     , CONSTRAINT FK_lams_learner_progress_4 FOREIGN KEY (next_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (previous_activity_id)
     , CONSTRAINT FK_lams_learner_progress_5 FOREIGN KEY (previous_activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)ENGINE=InnoDB;
CREATE UNIQUE INDEX IX_lams_learner_progress_1 ON lams_learner_progress (user_id ASC, lesson_id ASC);

CREATE TABLE lams_cr_node (
       node_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , workspace_id BIGINT(20) UNSIGNED NOT NULL
     , path VARCHAR(255)
     , type VARCHAR(255) NOT NULL
     , created_date_time DATETIME NOT NULL
     , next_version_id BIGINT(20) UNSIGNED NOT NULL DEFAULT 1
     , parent_nv_id BIGINT(20) UNSIGNED
     , PRIMARY KEY (node_id)
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_cr_node_1 FOREIGN KEY (workspace_id)
                  REFERENCES lams_cr_workspace (workspace_id)
)ENGINE=InnoDB;
ALTER TABLE lams_cr_node COMMENT='The main table containing the node definition';

CREATE TABLE lams_cr_node_version (
       nv_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , node_id BIGINT(20) UNSIGNED NOT NULL
     , version_id BIGINT(20) UNSIGNED NOT NULL
     , created_date_time DATETIME NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (nv_id)
     , INDEX (node_id)
     , CONSTRAINT FK_lams_cr_node_version_2 FOREIGN KEY (node_id)
                  REFERENCES lams_cr_node (node_id)
)ENGINE=InnoDB;
ALTER TABLE lams_cr_node_version COMMENT='Represents a version of a node';


CREATE TABLE lams_user_organisation_role (
       user_organisation_role_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_organisation_id BIGINT(20) NOT NULL
     , role_id INT(6) NOT NULL
     , PRIMARY KEY (user_organisation_role_id)
     , INDEX (role_id)
     , CONSTRAINT FK_lams_user_organisation_role_2 FOREIGN KEY (role_id)
                  REFERENCES lams_role (role_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (user_organisation_id)
     , CONSTRAINT FK_lams_user_organisation_role_3 FOREIGN KEY (user_organisation_id)
                  REFERENCES lams_user_organisation (user_organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

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
     , UNIQUE UQ_lams_tool_session_1 (unique_key)
     , PRIMARY KEY (tool_session_id)
     , INDEX (tool_session_state_id)
     , CONSTRAINT FK_lams_tool_session_4 FOREIGN KEY (tool_session_state_id)
                  REFERENCES lams_tool_session_state (tool_session_state_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_tool_session_5 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (tool_session_type_id)
     , CONSTRAINT FK_lams_tool_session_7 FOREIGN KEY (tool_session_type_id)
                  REFERENCES lams_tool_session_type (tool_session_type_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_tool_session_8 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
     , INDEX (group_id)
     , CONSTRAINT FK_lams_tool_session_1 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
)ENGINE=InnoDB;

CREATE TABLE lams_progress_completed (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL
     , completed_date_time DATETIME
	 , start_date_time DATETIME
     , PRIMARY KEY (learner_progress_id, activity_id)
     , INDEX (learner_progress_id)
     , CONSTRAINT FK_lams_progress_completed_1 FOREIGN KEY (learner_progress_id)
                  REFERENCES lams_learner_progress (learner_progress_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_progress_completed_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)ENGINE=InnoDB;

CREATE TABLE lams_progress_attempted (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL
	 , start_date_time DATETIME
     , PRIMARY KEY (learner_progress_id, activity_id)
     , INDEX (learner_progress_id)
     , CONSTRAINT FK_lams_progress_current_1 FOREIGN KEY (learner_progress_id)
                  REFERENCES lams_learner_progress (learner_progress_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_progress_current_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)ENGINE=InnoDB;

CREATE TABLE lams_user_group (
       user_id BIGINT(20) NOT NULL
     , group_id BIGINT(20) NOT NULL
     , scheduled_lesson_end_date DATETIME
     , PRIMARY KEY (user_id, group_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_user_group_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (group_id)
     , CONSTRAINT FK_lams_user_group_2 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
)ENGINE=InnoDB;

CREATE TABLE lams_tool_content (
       tool_content_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , tool_id BIGINT(20) NOT NULL
     , PRIMARY KEY (tool_content_id)
     , INDEX (tool_id)
     , CONSTRAINT FK_lams_tool_content_1 FOREIGN KEY (tool_id)
                  REFERENCES lams_tool (tool_id)
)ENGINE=InnoDB;

CREATE TABLE lams_activity_learners (
       user_id BIGINT(20) NOT NULL DEFAULT 0
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
	 , allowed_to_pass TINYINT NOT NULL DEFAULT 0
     , INDEX (user_id)
     , CONSTRAINT FK_TABLE_32_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_TABLE_32_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)ENGINE=InnoDB;

CREATE TABLE lams_cr_workspace_credential (
       wc_id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , workspace_id BIGINT(20) UNSIGNED NOT NULL
     , credential_id BIGINT(20) UNSIGNED NOT NULL
     , PRIMARY KEY (wc_id)
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_cr_workspace_credential_1 FOREIGN KEY (workspace_id)
                  REFERENCES lams_cr_workspace (workspace_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (credential_id)
     , CONSTRAINT FK_lams_cr_workspace_credential_2 FOREIGN KEY (credential_id)
                  REFERENCES lams_cr_credential (credential_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;
ALTER TABLE lams_cr_workspace_credential COMMENT='Maps tools access to workspaces';

CREATE TABLE lams_cr_node_version_property (
       id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
     , nv_id BIGINT(20) UNSIGNED NOT NULL
     , name VARCHAR(255) NOT NULL
     , value VARCHAR(255) NOT NULL
     , type TINYINT NOT NULL
     , PRIMARY KEY (id)
     , INDEX (nv_id)
     , CONSTRAINT FK_lams_cr_node_version_property_1 FOREIGN KEY (nv_id)
                  REFERENCES lams_cr_node_version (nv_id)
)ENGINE=InnoDB;

CREATE TABLE lams_log_event (
       id BIGINT(20) NOT NULL AUTO_INCREMENT
     , log_event_type_id INT(5) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , occurred_date_time DATETIME NOT NULL
     , learning_design_id BIGINT(20)
     , lesson_id BIGINT(20)
     , activity_id BIGINT(20)
     , PRIMARY KEY (id)
     , INDEX (occurred_date_time)
     , CONSTRAINT FK_lams_event_log_1 FOREIGN KEY (log_event_type_id)
                  REFERENCES lams_log_event_type (log_event_type_id)
	 , INDEX (user_id)                  
     , CONSTRAINT FK_lams_event_log_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)           
     , CONSTRAINT FK_lams_event_log_3 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE
     , CONSTRAINT FK_lams_event_log_4 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
     , CONSTRAINT FK_lams_event_log_5 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)ENGINE=InnoDB;

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
     , UNIQUE unique_content_name (name, workspace_folder_id, mime_type)
     , UNIQUE unique_node_version (workspace_folder_id, uuid, version_id)
     , PRIMARY KEY (folder_content_id)
     , INDEX (workspace_folder_id)
     , CONSTRAINT FK_lams_workspace_folder_content_1 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id)
     , INDEX (content_type_id)
     , CONSTRAINT FK_lams_workspace_folder_content_2 FOREIGN KEY (content_type_id)
                  REFERENCES lams_wkspc_fld_content_type (content_type_id)
)ENGINE=InnoDB;

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
     , INDEX (from_activity_id)
     , CONSTRAINT FK_learning_transition_3 FOREIGN KEY (from_activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (to_activity_id)
     , CONSTRAINT FK_learning_transition_2 FOREIGN KEY (to_activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (learning_design_id)
     , CONSTRAINT lddefn_transition_ibfk_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;
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
     , INDEX (privilege_id)
     , CONSTRAINT FK_lams_role_privilege_1 FOREIGN KEY (privilege_id)
                  REFERENCES lams_privilege (privilege_id)
     , INDEX (role_id)
     , CONSTRAINT FK_lams_role_privilege_2 FOREIGN KEY (role_id)
                  REFERENCES lams_role (role_id)
)ENGINE=InnoDB;

CREATE TABLE lams_tool_import_support (
       id BIGINT(20) NOT NULL AUTO_INCREMENT
     , installed_tool_signature VARCHAR(15) NOT NULL
     , supports_tool_signature VARCHAR(50) NOT NULL
     , PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE lams_configuration (
       config_key VARCHAR(30) NOT NULL
     , config_value VARCHAR(255)
     , description_key VARCHAR(255)
     , header_name VARCHAR(50)
     , format VARCHAR(30)
     , required TINYINT NOT NULL DEFAULT 0
     , PRIMARY KEY (config_key)
)ENGINE=InnoDB;

CREATE TABLE lams_timezone (
	   id BIGINT(20) NOT NULL AUTO_INCREMENT
     , timezone_id VARCHAR(255) NOT NULL
     , server_timezone TINYINT DEFAULT 0
     , PRIMARY KEY (id)
)ENGINE=InnoDB;

CREATE TABLE lams_user_organisation_collapsed (
       user_organisation_id BIGINT(20) NOT NULL
     , collapsed TINYINT(1) NOT NULL DEFAULT 1
     , PRIMARY KEY (user_organisation_id)
     , CONSTRAINT FK_lams_user_organisation_collapsed_1 FOREIGN KEY (user_organisation_id)
                  REFERENCES lams_user_organisation (user_organisation_id)
)ENGINE=InnoDB;

CREATE TABLE lams_password_request (
       request_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL
     , request_key VARCHAR(32) NOT NULL
     , request_date DATETIME NOT NULL
     , PRIMARY KEY (request_id)
)ENGINE=InnoDB;
CREATE UNIQUE INDEX IX_lams_psswd_rqst_key ON lams_password_request (request_key ASC);

CREATE TABLE lams_events (
       uid BIGINT NOT NULL UNIQUE auto_increment
     , scope VARCHAR(128) NOT NULL
     , name VARCHAR(128) NOT NULL
     , event_session_id BIGINT
     , triggered TINYINT
     , default_subject VARCHAR(255)
     , default_message TEXT
	 , subject VARCHAR(255)
	 , message TEXT
	 , fail_time DATETIME
	 , INDEX (scope,name,event_session_id)
     , PRIMARY KEY (uid)
)ENGINE=InnoDB;

CREATE TABLE lams_event_subscriptions (
       uid BIGINT NOT NULL UNIQUE auto_increment
     , user_id BIGINT
     , event_uid BIGINT
     , delivery_method_id TINYINT UNSIGNED
     , periodicity BIGINT
     , last_operation_time DATETIME
     , last_operation_message TEXT
     , PRIMARY KEY (uid)
     , CONSTRAINT EventSubscriptionsToUsers FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (event_uid)
     , CONSTRAINT EventSubscriptionsToEvent FOREIGN KEY (event_uid)
                  REFERENCES lams_events (uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_competence (
       competence_id BIGINT NOT NULL UNIQUE auto_increment
     , learning_design_id BIGINT
     , description TEXT
     , title VARCHAR(255) 
	 , UNIQUE KEY (learning_design_id, title)
     , PRIMARY KEY (competence_id)
     , CONSTRAINT LearningDesignCompetenceMap FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design(learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_competence_mapping (
       competence_mapping_id BIGINT NOT NULL UNIQUE auto_increment
     , competence_id BIGINT
     , activity_id BIGINT 
     , PRIMARY KEY (competence_mapping_id)
	 , INDEX (activity_id)
	 , UNIQUE KEY (competence_id, activity_id)
     , CONSTRAINT FK_lams_competence_mapping_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT FK_lams_competence_mapping_2 FOREIGN KEY (competence_id)
	                  REFERENCES lams_competence (competence_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE patches (
       system_name VARCHAR(30) NOT NULL
     , patch_level INTEGER(11) NOT NULL
     , patch_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , patch_in_progress CHAR(1) NOT NULL DEFAULT 'F'
     , PRIMARY KEY (system_name)
)ENGINE=InnoDB;

CREATE TABLE lams_registration (
	   uid BIGINT NOT NULL auto_increment
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
)ENGINE=InnoDB;

CREATE TABLE lams_planner_nodes (
	uid BIGINT(20) NOT NULL auto_increment,
	parent_uid BIGINT(20),
	order_id TINYINT UNSIGNED NOT NULL,
	locked TINYINT(1) NOT NULL DEFAULT 0,
	content_folder_id VARCHAR(32),
	title VARCHAR(255) NOT NULL,
	brief_desc TEXT,
	full_desc TEXT,
	ld_id  BIGINT(20),
	user_id BIGINT(20),
	permissions INTEGER,
	PRIMARY KEY (uid),
	UNIQUE KEY (parent_uid, order_id),
	CONSTRAINT FK_lams_planner_node_parent FOREIGN KEY (parent_uid)
	               REFERENCES lams_planner_nodes(uid) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_planner_node_user FOREIGN KEY (user_id)
	               REFERENCES lams_user(user_id) ON DELETE SET NULL ON UPDATE SET NULL
)ENGINE=InnoDB; 

CREATE TABLE lams_activity_evaluation (
	  activity_evaluation_id BIGINT(20) NOT NULL auto_increment
	, activity_id	BIGINT(20) NOT NULL
	, tool_output_definition VARCHAR(255) NOT NULL
	, INDEX (activity_id)
	, CONSTRAINT FK_lams_activity_evaluation_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (activity_evaluation_id)
)ENGINE=InnoDB;

CREATE TABLE lams_gradebook_user_activity (
	  uid BIGINT(20) NOT NULL auto_increment
	, activity_id BIGINT(20) NOT NULL
	, user_id BIGINT (20) NOT NULL
	, mark DOUBLE PRECISION 
	, feedback TEXT
	, marked_in_gradebook TINYINT(1) NOT NULL DEFAULT 0
	, INDEX (activity_id, user_id)
	, CONSTRAINT FK_lams_gradebook_user_activity_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	, CONSTRAINT FK_lams_gradebook_user_activity_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (uid)
)ENGINE=InnoDB;

CREATE TABLE lams_gradebook_user_lesson (
	  uid BIGINT(20) NOT NULL auto_increment
	, lesson_id BIGINT(20) NOT NULL
	, user_id BIGINT (20) NOT NULL
	, mark DOUBLE PRECISION
	, feedback TEXT
	, INDEX (lesson_id, user_id)
	, CONSTRAINT FK_lams_gradebook_user_lesson_1 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
	, CONSTRAINT FK_lams_gradebook_user_lesson_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (uid)
)ENGINE=InnoDB;

CREATE TABLE lams_data_flow (
	  data_flow_object_id BIGINT(20) NOT NULL auto_increment
	, transition_id BIGINT(20) NOT NULL
	, order_id INT(11) 
	, name VARCHAR(255) NOT NULL
	, display_name VARCHAR(255)
	, tool_assigment_id INT(11)
	, CONSTRAINT FK_lams_learning_transition_1 FOREIGN KEY (transition_id)
                  REFERENCES lams_learning_transition (transition_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (data_flow_object_id)
)ENGINE=InnoDB;

CREATE TABLE lams_user_disabled_tutorials (
     user_id BIGINT(20) NOT NULL
   , page_str CHAR(5) NOT NULL
   , CONSTRAINT FK_lams_user_disabled_tutorials_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (user_id,page_str)
)ENGINE=InnoDB;

CREATE TABLE lams_planner_recent_learning_designs (
     user_id  BIGINT(20) NOT NULL
   , learning_design_id BIGINT(20) NOT NULL
   , last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   , CONSTRAINT FK_lams_planner_recent_learning_designs_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
   , CONSTRAINT FK_lams_planner_recent_learning_designs_2 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (user_id,learning_design_id)
)ENGINE=InnoDB;

CREATE TABLE lams_planner_node_role (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , node_uid BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , role_id INT(6) NOT NULL
     , PRIMARY KEY (uid)
)ENGINE=InnoDB;

ALTER TABLE lams_planner_node_role ADD CONSTRAINT FK_planner_node_role_user FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE lams_planner_node_role ADD CONSTRAINT FK_planner_node_role_node FOREIGN KEY (node_uid) REFERENCES lams_planner_nodes (uid) ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE lams_planner_node_role ADD CONSTRAINT FK_planner_node_role_role FOREIGN KEY (role_id) REFERENCES lams_role (role_id) ON DELETE CASCADE ON UPDATE NO ACTION;

CREATE TABLE lams_openid_config (
     config_key VARCHAR(20) NOT NULL UNIQUE
   , config_value VARCHAR(255) NOT NULL
   , PRIMARY KEY (config_key)
)ENGINE=InnoDB;

CREATE TABLE lams_lesson_dependency (
      lesson_id BIGINT(20)
   ,  preceding_lesson_id BIGINT(20)
   , CONSTRAINT FK_lams_lesson_dependency_1 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , CONSTRAINT FK_lams_lesson_dependency_2 FOREIGN KEY (preceding_lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (lesson_id,preceding_lesson_id)
)ENGINE=InnoDB;
