

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS lams_activity_category;
DROP TABLE IF EXISTS lams_activity_learners;
DROP TABLE IF EXISTS lams_authentication_method;
DROP TABLE IF EXISTS lams_authentication_method_type;
DROP TABLE IF EXISTS lams_copy_type;
DROP TABLE IF EXISTS lams_cr_credential;
DROP TABLE IF EXISTS lams_cr_node;
DROP TABLE IF EXISTS lams_cr_node_version;
DROP TABLE IF EXISTS lams_cr_node_version_property;
DROP TABLE IF EXISTS lams_cr_workspace;
DROP TABLE IF EXISTS lams_cr_workspace_credential;
DROP TABLE IF EXISTS lams__theme;
DROP TABLE IF EXISTS lams_event_subscriptions;
DROP TABLE IF EXISTS lams_events;
DROP TABLE IF EXISTS lams_gate_activity_level;
DROP TABLE IF EXISTS lams_group;
DROP TABLE IF EXISTS lams_grouping;
DROP TABLE IF EXISTS lams_grouping_support_type;
DROP TABLE IF EXISTS lams_grouping_type;
DROP TABLE IF EXISTS lams_learner_progress;
DROP TABLE IF EXISTS lams_learning_activity;
DROP TABLE IF EXISTS lams_learning_activity_type;
DROP TABLE IF EXISTS lams_learning_design;
DROP TABLE IF EXISTS lams_learning_transition;
DROP TABLE IF EXISTS lams_learning_library;
DROP TABLE IF EXISTS lams_lesson;
DROP TABLE IF EXISTS lams_lesson_state;
DROP TABLE IF EXISTS lams_license;
DROP TABLE IF EXISTS lams_log_event;
DROP TABLE IF EXISTS lams_log_event_type;
DROP TABLE IF EXISTS lams_organisation;
DROP TABLE IF EXISTS lams_organisation_type;
DROP TABLE IF EXISTS lams_planner_nodes;
DROP TABLE IF EXISTS lams_progress_attempted;
DROP TABLE IF EXISTS lams_progress_completed;
DROP TABLE IF EXISTS lams_role;
DROP TABLE IF EXISTS lams_timezone;
DROP TABLE IF EXISTS lams_tool;
DROP TABLE IF EXISTS lams_tool_content;
DROP TABLE IF EXISTS lams_tool_session;
DROP TABLE IF EXISTS lams_tool_session_state;
DROP TABLE IF EXISTS lams_tool_session_type;
DROP TABLE IF EXISTS lams_user;
DROP TABLE IF EXISTS lams_user_group;
DROP TABLE IF EXISTS lams_user_organisation;
DROP TABLE IF EXISTS lams_user_organisation_role;
DROP TABLE IF EXISTS lams_user_preference;
DROP TABLE IF EXISTS lams_workspace;
DROP TABLE IF EXISTS lams_workspace_folder;
DROP TABLE IF EXISTS lams_workspace_folder_type;
DROP TABLE IF EXISTS lams_workspace_folder_content_type;
DROP TABLE IF EXISTS lams_workspace_folder_content;

-- Drop Quartz Tables
DROP TABLE IF EXISTS lams_quartz_JOB_LISTENERS;
DROP TABLE IF EXISTS lams_quartz_TRIGGER_LISTENERS;
DROP TABLE IF EXISTS lams_quartz_FIRED_TRIGGERS;
DROP TABLE IF EXISTS lams_quartz_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS lams_quartz_SCHEDULER_STATE;
DROP TABLE IF EXISTS lams_quartz_LOCKS;
DROP TABLE IF EXISTS lams_quartz_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS lams_quartz_CRON_TRIGGERS;
DROP TABLE IF EXISTS lams_quartz_BLOB_TRIGGERS;
DROP TABLE IF EXISTS lams_quartz_TRIGGERS;
DROP TABLE IF EXISTS lams_quartz_JOB_DETAILS;
DROP TABLE IF EXISTS lams_quartz_CALENDARS;

-- AutoPatch table
DROP TABLE IF EXISTS patches;

SET FOREIGN_KEY_CHECKS=1;
