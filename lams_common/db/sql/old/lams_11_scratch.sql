CREATE TABLE lams_gate_activity_level (
       gate_activity_level_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (gate_activity_level_id)
)TYPE=InnoDB;

CREATE TABLE lams_grouping_type (
       grouping_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (grouping_type_id)
)TYPE=InnoDB;

CREATE TABLE lams_learning_activity_type (
       learning_activity_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (learning_activity_type_id)
)TYPE=InnoDB;

CREATE TABLE lams_learning_library (
       learning_library_id BIGINT(20) NOT NULL DEFAULT 0
     , description TEXT
     , title VARCHAR(255)
     , create_date_time DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , PRIMARY KEY (learning_library_id)
)TYPE=InnoDB;

CREATE TABLE lams_organisation_type (
       organisation_type_id INT(3) NOT NULL DEFAULT 0
     , name VARCHAR(64) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (organisation_type_id)
)TYPE=InnoDB;
CREATE UNIQUE INDEX UQ_lams_organisation_type_name ON lams_organisation_type (name ASC);

CREATE TABLE lams_role (
       role_id INT(6) NOT NULL DEFAULT 0
     , name VARCHAR(64) NOT NULL
     , description TEXT
     , create_date BIGINT(20)
     , PRIMARY KEY (role_id)
)TYPE=InnoDB;
CREATE INDEX gname ON lams_role (name ASC);

CREATE TABLE lams_tool_content (
       tool_content_id BIGINT(20) NOT NULL
     , tool_content_key BIGINT(20) NOT NULL
     , PRIMARY KEY (tool_content_id)
)TYPE=InnoDB;

CREATE TABLE lams_tool (
       tool_id BIGINT(20) NOT NULL
     , learner_url TEXT NOT NULL
     , supports_grouping_flag TINYINT(1) NOT NULL DEFAULT 0
     , author_url TEXT
     , supports_define_later_flag TINYINT(1) NOT NULL DEFAULT 0
     , define_later_url TEXT
     , PRIMARY KEY (tool_id)
)TYPE=InnoDB;

CREATE TABLE lams_tool_session_state (
       tool_session_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (tool_session_state_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_tool_session_state (
       user_tool_session_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (user_tool_session_state_id)
)TYPE=InnoDB;

CREATE TABLE lams_authentication_method_type (
       authentication_method_type_id INT(3) NOT NULL DEFAULT 0
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (authentication_method_type_id)
)TYPE=InnoDB;

CREATE TABLE lams_authentication_method (
       authentication_method_id BIGINT(20) NOT NULL DEFAULT 0
     , authentication_method_type_id INT(3) NOT NULL DEFAULT 0
     , PRIMARY KEY (authentication_method_id)
)TYPE=InnoDB;

CREATE TABLE lams_workspace_folder (
       workspace_folder_id BIGINT(20) NOT NULL DEFAULT 0
     , parent_folder_id BIGINT(20)
     , name VARCHAR(64) NOT NULL
     , workspace_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (workspace_folder_id)
)TYPE=InnoDB;

CREATE TABLE lams_workspace (
       workspace_id BIGINT(20) NOT NULL DEFAULT 0
     , root_folder_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (workspace_id)
)TYPE=InnoDB;

CREATE TABLE lams_user (
       user_id BIGINT(20) NOT NULL DEFAULT 0
     , login VARCHAR(20) NOT NULL
     , password VARCHAR(50) NOT NULL
     , title VARCHAR(32)
     , first_name VARCHAR(64)
     , last_name VARCHAR(128)
     , address_line_1 VARCHAR(64)
     , address_line_2 VARCHAR(64)
     , address_line_3 VARCHAR(64)
     , city VARCHAR(64)
     , state VARCHAR(64)
     , country VARCHAR(64)
     , day_phone VARCHAR(64)
     , evening_phone VARCHAR(64)
     , mobile_phone VARCHAR(64)
     , fax VARCHAR(64)
     , email VARCHAR(128)
     , disabled_flag TINYINT(1) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , authentication_method_id BIGINT(20) NOT NULL DEFAULT 0
     , workspace_id BIGINT(20)
     , PRIMARY KEY (user_id)
)TYPE=InnoDB;
CREATE UNIQUE INDEX UQ_lams_user_login ON lams_user (login ASC);
CREATE INDEX login ON lams_user (login ASC);

CREATE TABLE lams_learning_design (
       learning_design_id BIGINT(20) NOT NULL DEFAULT 0
     , id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , first_activity_id BIGINT(20)
     , max_id INT(11)
     , valid_design_flag TINYINT(4) NOT NULL
     , read_only_flag TINYINT(4) NOT NULL
     , date_read_only DATETIME
     , read_access BIGINT(20)
     , write_access BIGINT(20)
     , user_id BIGINT(20) NOT NULL
     , help_text TEXT
     , lesson_copy_flag TINYINT(4) NOT NULL DEFAULT 0
     , create_date_time DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , version VARCHAR(56) NOT NULL
     , parent_learning_design_id BIGINT(20)
     , open_date_time DATETIME
     , close_date_time DATETIME
     , PRIMARY KEY (learning_design_id)
)TYPE=InnoDB;
CREATE INDEX idx_design_first_act ON lams_learning_design (first_activity_id ASC);

CREATE TABLE lams_grouping (
       grouping_id BIGINT(20) NOT NULL DEFAULT 0
     , grouping_type_id INT(11) NOT NULL DEFAULT 0
     , number_of_groups INT(11)
     , learners_per_group INT(11)
     , staff_group_id BIGINT(20) DEFAULT 0
     , PRIMARY KEY (grouping_id)
)TYPE=InnoDB;

CREATE TABLE lams_group (
       group_id BIGINT(20) NOT NULL DEFAULT 0
     , grouping_id BIGINT(20) NOT NULL DEFAULT 0
     , order_id INT(6) NOT NULL DEFAULT 1
     , PRIMARY KEY (group_id)
)TYPE=InnoDB;

CREATE TABLE lams_organisation (
       organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , name VARCHAR(250)
     , description VARCHAR(250)
     , parent_organisation_id BIGINT(20)
     , organisation_type_id INT(3) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , workspace_id BIGINT(20)
     , PRIMARY KEY (organisation_id)
)TYPE=InnoDB;

CREATE TABLE lams_learning_activity (
       activity_id BIGINT(20) NOT NULL DEFAULT 0
     , id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , xcoord INT(11)
     , ycoord INT(11)
     , parent_activity_id BIGINT(20)
     , learning_activity_type_id INT(11) NOT NULL DEFAULT 0
     , grouping_id BIGINT(20)
     , order_id INT(11)
     , define_later_flag TINYINT(4) NOT NULL DEFAULT 0
     , learning_design_id BIGINT(20) DEFAULT 0
     , learning_library_id BIGINT(20) DEFAULT 0
     , create_date_time DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , offline_instructions TEXT
     , max_number_of_options INT(5)
     , min_number_of_options INT(5)
     , tool_id BIGINT(20)
     , tool_content_id BIGINT(20)
     , gate_activity_level_id INT(11) DEFAULT 0
     , gate_start_date_time DATETIME
     , gate_end_date_time DATETIME
     , PRIMARY KEY (activity_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_organisation (
       user_organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_organisation_id)
)TYPE=InnoDB;

CREATE TABLE lams_lesson (
       lesson_id BIGINT(20) NOT NULL
     , learning_design_id BIGINT(20) NOT NULL DEFAULT 0
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (lesson_id)
)TYPE=InnoDB;

CREATE TABLE lams_learner_progress (
       user_id BIGINT(20) NOT NULL DEFAULT 0
     , lesson_id BIGINT(20) NOT NULL
     , learner_progress_id BIGINT(20) NOT NULL
     , PRIMARY KEY (learner_progress_id)
)TYPE=InnoDB;

CREATE TABLE lams_tool_session (
       tool_session_id BIGINT(20) NOT NULL
     , group_id BIGINT(20) DEFAULT 0
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
     , tool_session_key BIGINT(20) NOT NULL
     , tool_session_state_id INT(3) NOT NULL
     , PRIMARY KEY (tool_session_id)
)TYPE=InnoDB;

CREATE TABLE lams_learning_transition (
       transition_id BIGINT(20) NOT NULL DEFAULT 0
     , id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , to_activity_id BIGINT(20) NOT NULL DEFAULT 0
     , from_activity_id BIGINT(20) NOT NULL DEFAULT 0
     , learning_design_id BIGINT(20) NOT NULL DEFAULT 0
     , create_date_time DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , PRIMARY KEY (transition_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_organisation_role (
       user_organisation_role_id BIGINT(20) NOT NULL DEFAULT 0
     , user_organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , role_id INT(6) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_organisation_role_id)
)TYPE=InnoDB;

CREATE TABLE lams_progress_completed (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (learner_progress_id, activity_id)
)TYPE=InnoDB;

CREATE TABLE lams_progress_current (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (learner_progress_id, activity_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_tool_session (
       tool_session_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , user_tool_session_state_id INT(3) NOT NULL
     , PRIMARY KEY (tool_session_id, user_id)
)TYPE=InnoDB;

CREATE TABLE lams_user_group (
       user_id BIGINT(20) NOT NULL DEFAULT 0
     , group_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_id, group_id)
)TYPE=InnoDB;

CREATE TABLE lams_authentication_method_parameter (
       authentication_parameter_id BIGINT(20) NOT NULL DEFAULT 0
     , authentication_method_id BIGINT(20) NOT NULL DEFAULT 0
     , name VARCHAR(128) NOT NULL
     , value VARCHAR(255)
     , PRIMARY KEY (authentication_parameter_id)
)TYPE=InnoDB;

ALTER TABLE lams_authentication_method
  ADD CONSTRAINT FK_lams_authorization_method_1
      FOREIGN KEY (authentication_method_type_id)
      REFERENCES lams_authentication_method_type (authentication_method_type_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_workspace_folder
  ADD CONSTRAINT FK_lams_workspace_folder_2
      FOREIGN KEY (parent_folder_id)
      REFERENCES lams_workspace_folder (workspace_folder_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_workspace
  ADD CONSTRAINT FK_lams_workspace_1
      FOREIGN KEY (root_folder_id)
      REFERENCES lams_workspace_folder (workspace_folder_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_user
  ADD CONSTRAINT FK_lams_user_1
      FOREIGN KEY (authentication_method_id)
      REFERENCES lams_authentication_method (authentication_method_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_user
  ADD CONSTRAINT FK_lams_user_2
      FOREIGN KEY (workspace_id)
      REFERENCES lams_workspace (workspace_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_design
  ADD CONSTRAINT FK_lams_learning_design_2
      FOREIGN KEY (parent_learning_design_id)
      REFERENCES lams_learning_design (learning_design_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_design
  ADD CONSTRAINT FK_lams_learning_design_3
      FOREIGN KEY (user_id)
      REFERENCES lams_user (user_id);

ALTER TABLE lams_grouping
  ADD CONSTRAINT FK_lams_learning_grouping_1
      FOREIGN KEY (grouping_type_id)
      REFERENCES lams_grouping_type (grouping_type_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_group
  ADD CONSTRAINT FK_lams_learning_group_1
      FOREIGN KEY (grouping_id)
      REFERENCES lams_grouping (grouping_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_organisation
  ADD CONSTRAINT FK_lams_organisation_1
      FOREIGN KEY (organisation_type_id)
      REFERENCES lams_organisation_type (organisation_type_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_organisation
  ADD CONSTRAINT FK_lams_organisation_2
      FOREIGN KEY (workspace_id)
      REFERENCES lams_workspace (workspace_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_organisation
  ADD CONSTRAINT FK_lams_organisation_3
      FOREIGN KEY (parent_organisation_id)
      REFERENCES lams_organisation (organisation_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_7
      FOREIGN KEY (learning_library_id)
      REFERENCES lams_learning_library (learning_library_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_6
      FOREIGN KEY (learning_design_id)
      REFERENCES lams_learning_design (learning_design_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_2
      FOREIGN KEY (parent_activity_id)
      REFERENCES lams_learning_activity (activity_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_3
      FOREIGN KEY (learning_activity_type_id)
      REFERENCES lams_learning_activity_type (learning_activity_type_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_learning_activity_6
      FOREIGN KEY (grouping_id)
      REFERENCES lams_grouping (grouping_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_8
      FOREIGN KEY (tool_id)
      REFERENCES lams_tool (tool_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_9
      FOREIGN KEY (tool_content_id)
      REFERENCES lams_tool_content (tool_content_id);

ALTER TABLE lams_learning_activity
  ADD CONSTRAINT FK_lams_learning_activity_10
      FOREIGN KEY (gate_activity_level_id)
      REFERENCES lams_gate_activity_level (gate_activity_level_id);

ALTER TABLE lams_user_organisation
  ADD CONSTRAINT u_user_organisation_ibfk_1
      FOREIGN KEY (user_id)
      REFERENCES lams_user (user_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_user_organisation
  ADD CONSTRAINT u_user_organisation_ibfk_2
      FOREIGN KEY (organisation_id)
      REFERENCES lams_organisation (organisation_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_lesson
  ADD CONSTRAINT FK_lams_lesson_1_1
      FOREIGN KEY (learning_design_id)
      REFERENCES lams_learning_design (learning_design_id);

ALTER TABLE lams_lesson
  ADD CONSTRAINT FK_lams_lesson_2
      FOREIGN KEY (user_id)
      REFERENCES lams_user (user_id);

ALTER TABLE lams_learner_progress
  ADD CONSTRAINT FK_lams_learner_progress_1
      FOREIGN KEY (user_id)
      REFERENCES lams_user (user_id);

ALTER TABLE lams_learner_progress
  ADD CONSTRAINT FK_lams_learner_progress_2
      FOREIGN KEY (lesson_id)
      REFERENCES lams_lesson (lesson_id);

ALTER TABLE lams_tool_session
  ADD CONSTRAINT FK_lams_tool_session_1
      FOREIGN KEY (group_id)
      REFERENCES lams_group (group_id);

ALTER TABLE lams_tool_session
  ADD CONSTRAINT FK_lams_tool_session_4
      FOREIGN KEY (tool_session_state_id)
      REFERENCES lams_tool_session_state (tool_session_state_id);

ALTER TABLE lams_learning_transition
  ADD CONSTRAINT FK_learning_transition_3
      FOREIGN KEY (from_activity_id)
      REFERENCES lams_learning_activity (activity_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_transition
  ADD CONSTRAINT FK_learning_transition_2
      FOREIGN KEY (to_activity_id)
      REFERENCES lams_learning_activity (activity_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_learning_transition
  ADD CONSTRAINT lddefn_transition_ibfk_1
      FOREIGN KEY (learning_design_id)
      REFERENCES lams_learning_design (learning_design_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_user_organisation_role
  ADD CONSTRAINT FK_lams_user_organisation_role_2
      FOREIGN KEY (role_id)
      REFERENCES lams_role (role_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_user_organisation_role
  ADD CONSTRAINT FK_lams_user_organisation_role_3
      FOREIGN KEY (user_organisation_id)
      REFERENCES lams_user_organisation (user_organisation_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

ALTER TABLE lams_progress_completed
  ADD CONSTRAINT FK_lams_progress_completed_1
      FOREIGN KEY (learner_progress_id)
      REFERENCES lams_learner_progress (learner_progress_id);

ALTER TABLE lams_progress_completed
  ADD CONSTRAINT FK_lams_progress_completed_2
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_progress_current
  ADD CONSTRAINT FK_lams_progress_current_1
      FOREIGN KEY (learner_progress_id)
      REFERENCES lams_learner_progress (learner_progress_id);

ALTER TABLE lams_progress_current
  ADD CONSTRAINT FK_lams_progress_current_2
      FOREIGN KEY (activity_id)
      REFERENCES lams_learning_activity (activity_id);

ALTER TABLE lams_user_tool_session
  ADD CONSTRAINT FK_lams_user_tool_session_1
      FOREIGN KEY (tool_session_id)
      REFERENCES lams_tool_session (tool_session_id);

ALTER TABLE lams_user_tool_session
  ADD CONSTRAINT FK_lams_user_tool_session_2
      FOREIGN KEY (user_id)
      REFERENCES lams_user (user_id);

ALTER TABLE lams_user_tool_session
  ADD CONSTRAINT FK_lams_user_tool_session_3
      FOREIGN KEY (user_tool_session_state_id)
      REFERENCES lams_user_tool_session_state (user_tool_session_state_id);

ALTER TABLE lams_user_group
  ADD CONSTRAINT FK_lams_user_group_1
      FOREIGN KEY (user_id)
      REFERENCES lams_user (user_id);

ALTER TABLE lams_user_group
  ADD CONSTRAINT FK_lams_user_group_2
      FOREIGN KEY (group_id)
      REFERENCES lams_group (group_id);

ALTER TABLE lams_authentication_method_parameter
  ADD CONSTRAINT FK_lams_authorization_method_parameter_1
      FOREIGN KEY (authentication_method_id)
      REFERENCES lams_authentication_method (authentication_method_id)
   ON DELETE NO ACTION
   ON UPDATE NO ACTION;

