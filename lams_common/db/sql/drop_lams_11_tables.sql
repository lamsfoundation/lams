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
DROP TABLE lams_lesson_learner;
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
































