DROP INDEX PRIMARY ON lams_gate_activity_level;
DROP INDEX PRIMARY ON lams_grouping_type;
DROP INDEX PRIMARY ON lams_learning_activity_type;
DROP INDEX PRIMARY ON lams_learning_library;
DROP INDEX PRIMARY ON lams_organisation_type;
DROP INDEX UQ_lams_organisation_type_name ON lams_organisation_type;
DROP INDEX PRIMARY ON lams_role;
DROP INDEX gname ON lams_role;
DROP INDEX PRIMARY ON lams_authentication_method_type;
DROP INDEX PRIMARY ON lams_authentication_method;
DROP INDEX authentication_method_type_id ON lams_authentication_method;
DROP INDEX PRIMARY ON lams_workspace_folder;
DROP INDEX parent_folder_id ON lams_workspace_folder;
DROP INDEX PRIMARY ON lams_workspace;
DROP INDEX root_folder_id ON lams_workspace;
DROP INDEX PRIMARY ON lams_learning_design;
DROP INDEX idx_design_parent_id ON lams_learning_design;
DROP INDEX idx_design_user_id ON lams_learning_design;
DROP INDEX idx_design_first_act ON lams_learning_design;
DROP INDEX PRIMARY ON lams_grouping;
DROP INDEX idx_grouping_grouping_type ON lams_grouping;
DROP INDEX PRIMARY ON lams_group;
DROP INDEX idx_group_grouping ON lams_group;
DROP INDEX PRIMARY ON lams_organisation;
DROP INDEX organisation_type_id ON lams_organisation;
DROP INDEX workspace_id ON lams_organisation;
DROP INDEX parent_organisation_id ON lams_organisation;
DROP INDEX PRIMARY ON lams_user;
DROP INDEX UQ_lams_user_login ON lams_user;
DROP INDEX authentication_method_id ON lams_user;
DROP INDEX workspace_id ON lams_user;
DROP INDEX login ON lams_user;
DROP INDEX PRIMARY ON lams_learning_activity;
DROP INDEX idx_activity_library_id ON lams_learning_activity;
DROP INDEX idx_activity_design_id ON lams_learning_activity;
DROP INDEX idx_activity_grouping_id ON lams_learning_activity;
DROP INDEX idx_activity_activity_type_id ON lams_learning_activity;
DROP INDEX idx_activity_parent_id ON lams_learning_activity;
DROP INDEX PRIMARY ON lams_user_organisation;
DROP INDEX user_id ON lams_user_organisation;
DROP INDEX organisation_id ON lams_user_organisation;
DROP INDEX PRIMARY ON lams_user_organisation_role;
DROP INDEX role_id ON lams_user_organisation_role;
DROP INDEX user_organisation_id ON lams_user_organisation_role;
DROP INDEX PRIMARY ON lams_learning_transition;
DROP INDEX ldId ON lams_learning_transition;
DROP INDEX idx_transition_to_activity ON lams_learning_transition;
DROP INDEX idx_transition_from_activity ON lams_learning_transition;

DROP TABLE lams_learning_transition;
DROP TABLE lams_tool_content;
DROP TABLE lams_user_group;
DROP TABLE lams_user_tool_session;
DROP TABLE lams_progress_current;
DROP TABLE lams_progress_completed;
DROP TABLE lams_user_organisation_role;
DROP TABLE lams_tool_session;
DROP TABLE lams_learner_progress;
DROP TABLE lams_lesson;
DROP TABLE lams_user_organisation;
DROP TABLE lams_learning_activity;
DROP TABLE lams_user;
DROP TABLE lams_organisation;
DROP TABLE lams_group;
DROP TABLE lams_grouping;
DROP TABLE lams_learning_design;
DROP TABLE lams_workspace;
DROP TABLE lams_workspace_folder;
DROP TABLE lams_authentication_method;
DROP TABLE lams_authentication_method_type;
DROP TABLE lams_lesson_state;
DROP TABLE lams_user_tool_session_state;
DROP TABLE lams_tool_session_state;
DROP TABLE lams_tool;
DROP TABLE lams_role;
DROP TABLE lams_organisation_type;
DROP TABLE lams_learning_library;
DROP TABLE lams_learning_activity_type;
DROP TABLE lams_grouping_type;
DROP TABLE lams_gate_activity_level;
































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

ALTER TABLE lams_user
  ADD CONSTRAINT FK_lams_user_3
      FOREIGN KEY (base_organisation_id)
      REFERENCES lams_organisation (organisation_id);

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

ALTER TABLE lams_lesson
  ADD CONSTRAINT FK_lams_lesson_3
      FOREIGN KEY (organisation_id)
      REFERENCES lams_organisation (organisation_id);

ALTER TABLE lams_lesson
  ADD CONSTRAINT FK_lams_lesson_4
      FOREIGN KEY (lams_lesson_state_id)
      REFERENCES lams_lesson_state (lams_lesson_state_id);

ALTER TABLE lams_lesson
  ADD CONSTRAINT FK_lams_lesson_5
      FOREIGN KEY (class_grouping_id)
      REFERENCES lams_grouping (grouping_id);

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

ALTER TABLE lams_tool_content
  ADD CONSTRAINT FK_lams_tool_content_1
      FOREIGN KEY (tool_id)
      REFERENCES lams_tool (tool_id);

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

