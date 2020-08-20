SET FOREIGN_KEY_CHECKS=0;
SET NAMES utf8mb4 ;

--
-- Table structure for table `lams_activity_category`
--

DROP TABLE IF EXISTS `lams_activity_category`;

CREATE TABLE `lams_activity_category` (
  `activity_category_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`activity_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `lams_activity_evaluation`
--

DROP TABLE IF EXISTS `lams_activity_evaluation`;

CREATE TABLE `lams_activity_evaluation` (
  `activity_id` bigint(20) NOT NULL,
  `tool_output_definition` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `weight` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`activity_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_activity_evaluation_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `lams_auth_method_type`
--

DROP TABLE IF EXISTS `lams_auth_method_type`;

CREATE TABLE `lams_auth_method_type` (
  `authentication_method_type_id` int(3) NOT NULL,
  `description` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`authentication_method_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_authentication_method`
--

DROP TABLE IF EXISTS `lams_authentication_method`;

CREATE TABLE `lams_authentication_method` (
  `authentication_method_id` bigint(20) NOT NULL,
  `authentication_method_type_id` int(3) NOT NULL DEFAULT '0',
  `authentication_method_name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`authentication_method_id`),
  UNIQUE KEY `UQ_lams_authentication_method_1` (`authentication_method_name`),
  KEY `authentication_method_type_id` (`authentication_method_type_id`),
  CONSTRAINT `FK_lams_authorization_method_1` FOREIGN KEY (`authentication_method_type_id`) REFERENCES `lams_auth_method_type` (`authentication_method_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `lams_branch_activity_entry`
--

DROP TABLE IF EXISTS `lams_branch_activity_entry`;

CREATE TABLE `lams_branch_activity_entry` (
  `entry_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entry_ui_id` int(11) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `sequence_activity_id` bigint(20) DEFAULT NULL,
  `branch_activity_id` bigint(20) NOT NULL,
  `condition_id` bigint(20) DEFAULT NULL,
  `open_gate` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`entry_id`),
  KEY `group_id` (`group_id`),
  KEY `sequence_activity_id` (`sequence_activity_id`),
  KEY `branch_activity_id` (`branch_activity_id`),
  KEY `condition_id` (`condition_id`),
  CONSTRAINT `FK_lams_branch_activity_entry_4` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`),
  CONSTRAINT `FK_lams_branch_map_branch` FOREIGN KEY (`branch_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_branch_map_sequence` FOREIGN KEY (`sequence_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_group_activity_1` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_branch_condition`
--

DROP TABLE IF EXISTS `lams_branch_condition`;

CREATE TABLE `lams_branch_condition` (
  `condition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `condition_ui_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `display_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `end_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `exact_match_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_comment`
--

DROP TABLE IF EXISTS `lams_comment`;

CREATE TABLE `lams_comment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) NOT NULL,
  `body` mediumtext COLLATE utf8mb4_unicode_ci,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  `last_reply_date` datetime DEFAULT NULL,
  `reply_number` int(11) DEFAULT NULL,
  `hide_flag` smallint(6) DEFAULT NULL,
  `parent_uid` bigint(20) DEFAULT NULL,
  `root_comment_uid` bigint(20) DEFAULT NULL,
  `comment_level` smallint(6) DEFAULT NULL,
  `thread_comment_uid` bigint(20) DEFAULT NULL,
  `sticky` smallint(6) DEFAULT '0',
  `monitor` smallint(6) DEFAULT '0',
  `anonymous` smallint(6) DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `FK_comment_session` (`session_id`),
  KEY `FK_comment_create` (`create_by`),
  KEY `FK_comment_modify` (`update_by`),
  KEY `FK_comment_parent` (`parent_uid`),
  KEY `FK_comment_root` (`root_comment_uid`),
  KEY `FK_comment_thread` (`thread_comment_uid`),
  KEY `IX_comment_level_sticky` (`comment_level`,`sticky`),
  CONSTRAINT `FK_comment_create` FOREIGN KEY (`create_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_comment_modify` FOREIGN KEY (`update_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_comment_parent` FOREIGN KEY (`parent_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_comment_root` FOREIGN KEY (`root_comment_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_comment_session` FOREIGN KEY (`session_id`) REFERENCES `lams_comment_session` (`uid`),
  CONSTRAINT `FK_comment_thread` FOREIGN KEY (`thread_comment_uid`) REFERENCES `lams_comment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_comment_likes`
--

DROP TABLE IF EXISTS `lams_comment_likes`;

CREATE TABLE `lams_comment_likes` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `vote` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `comment_like_unique` (`comment_uid`,`user_id`),
  KEY `FK_commentlike_comment` (`comment_uid`),
  KEY `FK_commentlike_user` (`user_id`),
  CONSTRAINT `FK_commentlike_comment` FOREIGN KEY (`comment_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_commentlike_user` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_comment_session`
--

DROP TABLE IF EXISTS `lams_comment_session`;

CREATE TABLE `lams_comment_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `external_id` bigint(20) DEFAULT NULL,
  `external_id_type` int(1) DEFAULT NULL,
  `external_signature` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `external_secondary_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `comment_ext_sig` (`external_id`,`external_signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_competence`
--

DROP TABLE IF EXISTS `lams_competence`;

CREATE TABLE `lams_competence` (
  `competence_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_id` bigint(20) DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `title` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`competence_id`),
  UNIQUE KEY `competence_id` (`competence_id`),
  UNIQUE KEY `learning_design_id` (`learning_design_id`,`title`),
  CONSTRAINT `LearningDesignCompetenceMap` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_competence_mapping`
--

DROP TABLE IF EXISTS `lams_competence_mapping`;

CREATE TABLE `lams_competence_mapping` (
  `competence_mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competence_id` bigint(20) DEFAULT NULL,
  `activity_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`competence_mapping_id`),
  UNIQUE KEY `competence_mapping_id` (`competence_mapping_id`),
  UNIQUE KEY `competence_id` (`competence_id`,`activity_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_competence_mapping_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_competence_mapping_2` FOREIGN KEY (`competence_id`) REFERENCES `lams_competence` (`competence_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_configuration`
--

DROP TABLE IF EXISTS `lams_configuration`;

CREATE TABLE `lams_configuration` (
  `config_key` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `config_value` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `header_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `format` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `required` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_copy_type`
--

DROP TABLE IF EXISTS `lams_copy_type`;

CREATE TABLE `lams_copy_type` (
  `copy_type_id` tinyint(4) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`copy_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_cr_credential`
--

DROP TABLE IF EXISTS `lams_cr_credential`;

CREATE TABLE `lams_cr_credential` (
  `credential_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`credential_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_cr_node`
--

DROP TABLE IF EXISTS `lams_cr_node`;

CREATE TABLE `lams_cr_node` (
  `node_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `portrait_uuid` BINARY(16),
  `workspace_id` bigint(20) unsigned NOT NULL,
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date_time` datetime NOT NULL,
  `next_version_id` bigint(20) unsigned NOT NULL DEFAULT '1',
  `parent_nv_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  UNIQUE INDEX IDX_portrait_uuid (portrait_uuid),
  KEY `workspace_id` (`workspace_id`),
  CONSTRAINT `FK_lams_cr_node_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_cr_node_version`
--

DROP TABLE IF EXISTS `lams_cr_node_version`;

CREATE TABLE `lams_cr_node_version` (
  `nv_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `node_id` bigint(20) unsigned NOT NULL,
  `version_id` bigint(20) unsigned NOT NULL,
  `created_date_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`nv_id`),
  KEY `node_id` (`node_id`),
  CONSTRAINT `FK_lams_cr_node_version_2` FOREIGN KEY (`node_id`) REFERENCES `lams_cr_node` (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_cr_node_version_property`
--

DROP TABLE IF EXISTS `lams_cr_node_version_property`;

CREATE TABLE `lams_cr_node_version_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nv_id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `nv_id` (`nv_id`),
  CONSTRAINT `FK_lams_cr_node_version_property_1` FOREIGN KEY (`nv_id`) REFERENCES `lams_cr_node_version` (`nv_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_cr_workspace`
--

DROP TABLE IF EXISTS `lams_cr_workspace`;

CREATE TABLE `lams_cr_workspace` (
  `workspace_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`workspace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_cr_workspace_credential`
--

DROP TABLE IF EXISTS `lams_cr_workspace_credential`;

CREATE TABLE `lams_cr_workspace_credential` (
  `wc_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) unsigned NOT NULL,
  `credential_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`wc_id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `credential_id` (`credential_id`),
  CONSTRAINT `FK_lams_cr_workspace_credential_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`),
  CONSTRAINT `FK_lams_cr_workspace_credential_2` FOREIGN KEY (`credential_id`) REFERENCES `lams_cr_credential` (`credential_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_data_flow`
--

DROP TABLE IF EXISTS `lams_data_flow`;

CREATE TABLE `lams_data_flow` (
  `data_flow_object_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transition_id` bigint(20) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `display_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tool_assigment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`data_flow_object_id`),
  KEY `FK_lams_learning_transition_1` (`transition_id`),
  CONSTRAINT `FK_lams_learning_transition_1` FOREIGN KEY (`transition_id`) REFERENCES `lams_learning_transition` (`transition_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_email_notification_archive`
--

DROP TABLE IF EXISTS `lams_email_notification_archive`;

CREATE TABLE `lams_email_notification_archive` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) DEFAULT NULL,
  `lesson_id` bigint(20) DEFAULT NULL,
  `search_type` tinyint(4) NOT NULL,
  `sent_on` datetime NOT NULL,
  `body` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`uid`),
  KEY `FK_lams_email_notification_archive_1` (`organisation_id`),
  KEY `FK_lams_email_notification_archive_2` (`lesson_id`),
  CONSTRAINT `FK_lams_email_notification_archive_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_email_notification_archive_2` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_email_notification_recipient_archive`
--

DROP TABLE IF EXISTS `lams_email_notification_recipient_archive`;

CREATE TABLE `lams_email_notification_recipient_archive` (
  `email_notification_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `FK_lams_email_notification_recipient_archive_1` (`email_notification_uid`),
  KEY `FK_lams_email_notification_recipient_archive_2` (`user_id`),
  CONSTRAINT `FK_lams_email_notification_recipient_archive_1` FOREIGN KEY (`email_notification_uid`) REFERENCES `lams_email_notification_archive` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_email_notification_recipient_archive_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_ext_course_class_map`
--

DROP TABLE IF EXISTS `lams_ext_course_class_map`;

CREATE TABLE `lams_ext_course_class_map` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `courseid` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `classid` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY (`sid`),
  KEY `classid` (`classid`),
  KEY `ext_server_org_map_id` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_course_class_map_fk` FOREIGN KEY (`classid`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `lams_ext_course_class_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_ext_server_lesson_map`
--

DROP TABLE IF EXISTS `lams_ext_server_lesson_map`;

CREATE TABLE `lams_ext_server_lesson_map` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  `resource_link_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `lesson_id` (`lesson_id`),
  KEY `lams_ext_server_lesson_map_fk1` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_server_lesson_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_server_lesson_map_fk2` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_ext_server_org_map`
--

DROP TABLE IF EXISTS `lams_ext_server_org_map`;

CREATE TABLE `lams_ext_server_org_map` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `serverid` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `serverkey` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `servername` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `serverdesc` mediumtext COLLATE utf8mb4_unicode_ci,
  `prefix` varchar(11) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userinfo_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `lesson_finish_url` text COLLATE utf8mb4_unicode_ci,
  `logout_url` TEXT,
  `disabled` bit(1) NOT NULL,
  `time_to_live_login_request` int(11) DEFAULT '80',
  `time_to_live_login_request_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `ext_groups_url` text COLLATE utf8mb4_unicode_ci,
  `server_type_id` int(11) NOT NULL DEFAULT '1',
  `lti_consumer_monitor_roles` text COLLATE utf8mb4_unicode_ci,
  `learner_presence_avail` tinyint(1) DEFAULT '0',
  `learner_im_avail` tinyint(1) DEFAULT '0',
  `live_edit_enabled` tinyint(1) DEFAULT '1',
  `enable_lesson_notifications` tinyint(1) DEFAULT '1',
  `force_restart` tinyint(1) DEFAULT '0',
  `allow_restart` tinyint(1) DEFAULT '0',
  `gradebook_on_complete` tinyint(1) DEFAULT '1',
  `use_alternative_user_id_parameter_name` tinyint(1) DEFAULT '0',
  `membership_url` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `serverid` (`serverid`),
  UNIQUE KEY `prefix` (`prefix`),
  KEY `FK_lams_ext_server_type` (`server_type_id`),
  CONSTRAINT `FK_lams_ext_server_type` FOREIGN KEY (`server_type_id`) REFERENCES `lams_ext_server_type` (`server_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_ext_server_tool_map`
--

DROP TABLE IF EXISTS `lams_ext_server_tool_map`;

CREATE TABLE `lams_ext_server_tool_map` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_id` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `unique_adapter_map` (`ext_server_org_map_id`,`tool_id`),
  KEY `lams_ext_server_tool_map_fk2` (`tool_id`),
  CONSTRAINT `lams_ext_server_tool_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_server_tool_map_fk2` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_ext_server_type`
--

DROP TABLE IF EXISTS `lams_ext_server_type`;

CREATE TABLE `lams_ext_server_type` (
  `server_type_id` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`server_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_ext_user_userid_map`
--

DROP TABLE IF EXISTS `lams_ext_user_userid_map`;

CREATE TABLE `lams_ext_user_userid_map` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `external_username` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  `tc_gradebook_id` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`sid`),
  KEY `user_id` (`user_id`),
  KEY `ext_server_org_map_id` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_user_userid_map_fk` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_user_userid_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_favorite_organisation`
--

DROP TABLE IF EXISTS `lams_favorite_organisation`;

CREATE TABLE `lams_favorite_organisation` (
  `favorite_organisation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`favorite_organisation_id`),
  KEY `organisation_id` (`organisation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_favorite_organisation_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_favorite_organisation_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_gate_activity_level`
--

DROP TABLE IF EXISTS `lams_gate_activity_level`;

CREATE TABLE `lams_gate_activity_level` (
  `gate_activity_level_id` int(11) NOT NULL,
  `description` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`gate_activity_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_gate_allowed_learners`
--

DROP TABLE IF EXISTS `lams_gate_allowed_learners`;

CREATE TABLE `lams_gate_allowed_learners` (
  `user_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_TABLE_32_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_TABLE_32_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_gradebook_user_activity`
--

DROP TABLE IF EXISTS `lams_gradebook_user_activity`;

CREATE TABLE `lams_gradebook_user_activity` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double DEFAULT NULL,
  `feedback` mediumtext COLLATE utf8mb4_unicode_ci,
  `marked_in_gradebook` tinyint(1) NOT NULL DEFAULT '0',
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `activity_id` (`activity_id`,`user_id`),
  KEY `FK_lams_gradebook_user_activity_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_gradebook_user_activity_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_gradebook_user_activity_archive`
--

DROP TABLE IF EXISTS `lams_gradebook_user_activity_archive`;

CREATE TABLE `lams_gradebook_user_activity_archive` (
  `uid` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double DEFAULT NULL,
  `feedback` mediumtext COLLATE utf8mb4_unicode_ci,
  `marked_in_gradebook` tinyint(1) NOT NULL DEFAULT '0',
  `update_date` datetime DEFAULT NULL,
  `archive_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `activity_id` (`activity_id`,`user_id`),
  KEY `FK_lams_gradebook_user_activity_archive_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_activity_archive_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_gradebook_user_activity_archive_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_gradebook_user_lesson`
--

DROP TABLE IF EXISTS `lams_gradebook_user_lesson`;

CREATE TABLE `lams_gradebook_user_lesson` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double DEFAULT NULL,
  `feedback` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `lesson_id` (`lesson_id`,`user_id`),
  KEY `FK_lams_gradebook_user_lesson_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_gradebook_user_lesson_archive`
--

DROP TABLE IF EXISTS `lams_gradebook_user_lesson_archive`;

CREATE TABLE `lams_gradebook_user_lesson_archive` (
  `uid` bigint(20) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double DEFAULT NULL,
  `feedback` mediumtext COLLATE utf8mb4_unicode_ci,
  `archive_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `lesson_id` (`lesson_id`,`user_id`),
  KEY `FK_lams_gradebook_user_lesson_archive_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_archive_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_gradebook_user_lesson_archive_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_group`
--

DROP TABLE IF EXISTS `lams_group`;

CREATE TABLE `lams_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `grouping_id` bigint(20) NOT NULL,
  `order_id` int(6) NOT NULL DEFAULT '1',
  `group_ui_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `UQ_lams_group_1` (`grouping_id`,`order_id`),
  KEY `grouping_id` (`grouping_id`),
  CONSTRAINT `FK_lams_learning_group_1` FOREIGN KEY (`grouping_id`) REFERENCES `lams_grouping` (`grouping_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_grouping`
--

DROP TABLE IF EXISTS `lams_grouping`;

CREATE TABLE `lams_grouping` (
  `grouping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grouping_ui_id` int(11) DEFAULT NULL,
  `grouping_type_id` int(11) NOT NULL,
  `number_of_groups` int(11) DEFAULT NULL,
  `learners_per_group` int(11) DEFAULT NULL,
  `staff_group_id` bigint(20) DEFAULT '0',
  `max_number_of_groups` int(3) DEFAULT NULL,
  `equal_number_of_learners_per_group` tinyint(1) DEFAULT '0',
  `view_students_before_selection` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`grouping_id`),
  KEY `grouping_type_id` (`grouping_type_id`),
  CONSTRAINT `FK_lams_learning_grouping_1` FOREIGN KEY (`grouping_type_id`) REFERENCES `lams_grouping_type` (`grouping_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_grouping_support_type`
--

DROP TABLE IF EXISTS `lams_grouping_support_type`;

CREATE TABLE `lams_grouping_support_type` (
  `grouping_support_type_id` int(3) NOT NULL,
  `description` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`grouping_support_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_grouping_type`
--

DROP TABLE IF EXISTS `lams_grouping_type`;

CREATE TABLE `lams_grouping_type` (
  `grouping_type_id` int(11) NOT NULL,
  `description` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`grouping_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_input_activity`
--

DROP TABLE IF EXISTS `lams_input_activity`;

CREATE TABLE `lams_input_activity` (
  `activity_id` bigint(20) NOT NULL,
  `input_activity_id` bigint(20) NOT NULL,
  UNIQUE KEY `UQ_lams_input_activity_1` (`activity_id`,`input_activity_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_input_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_input_activity_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive`
--

DROP TABLE IF EXISTS `lams_kumalive`;

CREATE TABLE `lams_kumalive` (
  `kumalive_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `finished` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`kumalive_id`),
  KEY `FK_lams_kumalive_1` (`organisation_id`),
  KEY `FK_lams_kumalive_2` (`created_by`),
  CONSTRAINT `FK_lams_kumalive_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_kumalive_2` FOREIGN KEY (`created_by`) REFERENCES `lams_user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive_log`
--

DROP TABLE IF EXISTS `lams_kumalive_log`;

CREATE TABLE `lams_kumalive_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kumalive_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `log_date` datetime NOT NULL,
  `log_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `FK_lams_kumalive_log_1` (`kumalive_id`),
  KEY `FK_lams_kumalive_log_2` (`user_id`),
  CONSTRAINT `FK_lams_kumalive_log_1` FOREIGN KEY (`kumalive_id`) REFERENCES `lams_kumalive` (`kumalive_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_kumalive_log_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive_poll`
--

DROP TABLE IF EXISTS `lams_kumalive_poll`;

CREATE TABLE `lams_kumalive_poll` (
  `poll_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `kumalive_id` bigint(20) NOT NULL,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `votes_released` tinyint(1) DEFAULT '0',
  `voters_released` tinyint(1) DEFAULT '0',
  `start_date` datetime NOT NULL,
  `finish_date` datetime DEFAULT NULL,
  PRIMARY KEY (`poll_id`),
  KEY `FK_lams_kumalive_poll_1` (`kumalive_id`),
  CONSTRAINT `FK_lams_kumalive_poll_1` FOREIGN KEY (`kumalive_id`) REFERENCES `lams_kumalive` (`kumalive_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive_poll_answer`
--

DROP TABLE IF EXISTS `lams_kumalive_poll_answer`;

CREATE TABLE `lams_kumalive_poll_answer` (
  `answer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `poll_id` bigint(20) NOT NULL,
  `order_id` tinyint(4) NOT NULL,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`answer_id`),
  KEY `FK_lams_kumalive_poll_answer_1` (`poll_id`),
  CONSTRAINT `FK_lams_kumalive_poll_answer_1` FOREIGN KEY (`poll_id`) REFERENCES `lams_kumalive_poll` (`poll_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive_poll_vote`
--

DROP TABLE IF EXISTS `lams_kumalive_poll_vote`;

CREATE TABLE `lams_kumalive_poll_vote` (
  `answer_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `vote_date` datetime DEFAULT NULL,
  PRIMARY KEY (`answer_id`,`user_id`),
  KEY `FK_lams_kumalive_poll_vote_2` (`user_id`),
  CONSTRAINT `FK_lams_kumalive_poll_vote_1` FOREIGN KEY (`answer_id`) REFERENCES `lams_kumalive_poll_answer` (`answer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_kumalive_poll_vote_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive_rubric`
--

DROP TABLE IF EXISTS `lams_kumalive_rubric`;

CREATE TABLE `lams_kumalive_rubric` (
  `rubric_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `kumalive_id` bigint(20) DEFAULT NULL,
  `order_id` tinyint(4) NOT NULL,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`rubric_id`),
  KEY `FK_lams_kumalive_rubric_1` (`organisation_id`),
  KEY `FK_lams_kumalive_rubric_2` (`kumalive_id`),
  CONSTRAINT `FK_lams_kumalive_rubric_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_kumalive_rubric_2` FOREIGN KEY (`kumalive_id`) REFERENCES `lams_kumalive` (`kumalive_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_kumalive_score`
--

DROP TABLE IF EXISTS `lams_kumalive_score`;

CREATE TABLE `lams_kumalive_score` (
  `score_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rubric_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `batch` bigint(10) DEFAULT NULL,
  `score` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`score_id`),
  KEY `FK_lams_kumalive_score_1` (`rubric_id`),
  KEY `FK_lams_kumalive_score_2` (`user_id`),
  CONSTRAINT `FK_lams_kumalive_score_1` FOREIGN KEY (`rubric_id`) REFERENCES `lams_kumalive_rubric` (`rubric_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_kumalive_score_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learner_progress`
--

DROP TABLE IF EXISTS `lams_learner_progress`;

CREATE TABLE `lams_learner_progress` (
  `learner_progress_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `lesson_completed_flag` tinyint(1) NOT NULL DEFAULT '0',
  `waiting_flag` tinyint(4) NOT NULL,
  `start_date_time` datetime NOT NULL,
  `finish_date_time` datetime DEFAULT NULL,
  `current_activity_id` bigint(20) DEFAULT NULL,
  `next_activity_id` bigint(20) DEFAULT NULL,
  `previous_activity_id` bigint(20) DEFAULT NULL,
  `requires_restart_flag` tinyint(1) NOT NULL,
  PRIMARY KEY (`learner_progress_id`),
  UNIQUE KEY `IX_lams_learner_progress_1` (`user_id`,`lesson_id`),
  KEY `user_id` (`user_id`),
  KEY `lesson_id` (`lesson_id`),
  KEY `current_activity_id` (`current_activity_id`),
  KEY `next_activity_id` (`next_activity_id`),
  KEY `previous_activity_id` (`previous_activity_id`),
  CONSTRAINT `FK_lams_learner_progress_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_learner_progress_2` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_learner_progress_3` FOREIGN KEY (`current_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_learner_progress_4` FOREIGN KEY (`next_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_learner_progress_5` FOREIGN KEY (`previous_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learner_progress_archive`
--

DROP TABLE IF EXISTS `lams_learner_progress_archive`;

CREATE TABLE `lams_learner_progress_archive` (
  `learner_progress_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `attempt_id` tinyint(4) NOT NULL DEFAULT '1',
  `lesson_completed_flag` tinyint(1) NOT NULL DEFAULT '0',
  `start_date_time` datetime NOT NULL,
  `finish_date_time` datetime DEFAULT NULL,
  `current_activity_id` bigint(20) DEFAULT NULL,
  `archive_date` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`),
  UNIQUE KEY `IX_lams_learner_progress_archive_1` (`user_id`,`lesson_id`,`attempt_id`),
  KEY `FK_lams_learner_progress_archive_2` (`lesson_id`),
  KEY `FK_lams_learner_progress_archive_3` (`current_activity_id`),
  CONSTRAINT `FK_lams_learner_progress_archive_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learner_progress_archive_2` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learner_progress_archive_3` FOREIGN KEY (`current_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_activity`
--

DROP TABLE IF EXISTS `lams_learning_activity`;

CREATE TABLE `lams_learning_activity` (
  `activity_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_ui_id` int(11) DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `xcoord` int(11) DEFAULT NULL,
  `ycoord` int(11) DEFAULT NULL,
  `parent_activity_id` bigint(20) DEFAULT NULL,
  `parent_ui_id` int(11) DEFAULT NULL,
  `learning_activity_type_id` int(11) NOT NULL DEFAULT '0',
  `grouping_support_type_id` int(3) NOT NULL,
  `apply_grouping_flag` tinyint(1) NOT NULL,
  `grouping_id` bigint(20) DEFAULT NULL,
  `grouping_ui_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `learning_design_id` bigint(20) DEFAULT NULL,
  `learning_library_id` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  `max_number_of_options` int(5) DEFAULT NULL,
  `min_number_of_options` int(5) DEFAULT NULL,
  `options_instructions` mediumtext COLLATE utf8mb4_unicode_ci,
  `tool_id` bigint(20) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `activity_category_id` int(3),
  `gate_activity_level_id` int(11) DEFAULT NULL,
  `gate_open_flag` tinyint(1) DEFAULT NULL,
  `gate_open_user` bigint(20) DEFAULT NULL,
  `gate_open_time` datetime DEFAULT NULL,
  `gate_start_time_offset` bigint(38) DEFAULT NULL,
  `gate_end_time_offset` bigint(38) DEFAULT NULL,
  `gate_activity_completion_based` tinyint(1) DEFAULT NULL,
  `library_activity_ui_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_grouping_id` bigint(20) DEFAULT NULL,
  `create_grouping_ui_id` int(11) DEFAULT NULL,
  `library_activity_id` bigint(20) DEFAULT NULL,
  `language_file` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `system_tool_id` bigint(20) DEFAULT NULL,
  `read_only` tinyint(1) DEFAULT '0',
  `initialised` tinyint(1) DEFAULT '0',
  `default_activity_id` bigint(20) DEFAULT NULL,
  `start_xcoord` int(11) DEFAULT NULL,
  `start_ycoord` int(11) DEFAULT NULL,
  `end_xcoord` int(11) DEFAULT NULL,
  `end_ycoord` int(11) DEFAULT NULL,
  `stop_after_activity` tinyint(1) NOT NULL DEFAULT '0',
  `transition_to_id` bigint(20) DEFAULT NULL,
  `transition_from_id` bigint(20) DEFAULT NULL,
  `branching_ordered_asc` TINYINT(1),
  PRIMARY KEY (`activity_id`),
  KEY `lams_learning_activity_tool_content_id` (`tool_content_id`),
  KEY `learning_library_id` (`learning_library_id`),
  KEY `learning_design_id` (`learning_design_id`),
  KEY `parent_activity_id` (`parent_activity_id`),
  KEY `learning_activity_type_id` (`learning_activity_type_id`),
  KEY `grouping_id` (`grouping_id`),
  KEY `tool_id` (`tool_id`),
  KEY `gate_activity_level_id` (`gate_activity_level_id`),
  KEY `create_grouping_id` (`create_grouping_id`),
  KEY `library_activity_id` (`library_activity_id`),
  KEY `activity_category_id` (`activity_category_id`),
  KEY `grouping_support_type_id` (`grouping_support_type_id`),
  KEY `system_tool_id` (`system_tool_id`),
  KEY `FK_lams_learning_activity_15` (`transition_to_id`),
  KEY `FK_lams_learning_activity_16` (`transition_from_id`),
  KEY `FK_lams_learning_activity_17` (`gate_open_user`),
  CONSTRAINT `FK_lams_learning_activity_10` FOREIGN KEY (`gate_activity_level_id`) REFERENCES `lams_gate_activity_level` (`gate_activity_level_id`),
  CONSTRAINT `FK_lams_learning_activity_11` FOREIGN KEY (`library_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_learning_activity_12` FOREIGN KEY (`activity_category_id`) REFERENCES `lams_activity_category` (`activity_category_id`),
  CONSTRAINT `FK_lams_learning_activity_13` FOREIGN KEY (`grouping_support_type_id`) REFERENCES `lams_grouping_support_type` (`grouping_support_type_id`),
  CONSTRAINT `FK_lams_learning_activity_14` FOREIGN KEY (`system_tool_id`) REFERENCES `lams_system_tool` (`system_tool_id`),
  CONSTRAINT `FK_lams_learning_activity_15` FOREIGN KEY (`transition_to_id`) REFERENCES `lams_learning_transition` (`transition_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learning_activity_16` FOREIGN KEY (`transition_from_id`) REFERENCES `lams_learning_transition` (`transition_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learning_activity_17` FOREIGN KEY (`gate_open_user`) REFERENCES `lams_user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learning_activity_6` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learning_activity_7` FOREIGN KEY (`learning_library_id`) REFERENCES `lams_learning_library` (`learning_library_id`),
  CONSTRAINT `FK_lams_learning_activity_8` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`),
  CONSTRAINT `FK_lams_learning_activity_9` FOREIGN KEY (`create_grouping_id`) REFERENCES `lams_grouping` (`grouping_id`),
  CONSTRAINT `FK_learning_activity_2` FOREIGN KEY (`parent_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_learning_activity_3` FOREIGN KEY (`learning_activity_type_id`) REFERENCES `lams_learning_activity_type` (`learning_activity_type_id`),
  CONSTRAINT `FK_learning_activity_6` FOREIGN KEY (`grouping_id`) REFERENCES `lams_grouping` (`grouping_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_activity_type`
--

DROP TABLE IF EXISTS `lams_learning_activity_type`;

CREATE TABLE `lams_learning_activity_type` (
  `learning_activity_type_id` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`learning_activity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_command`
--

DROP TABLE IF EXISTS `lams_learning_command`;

CREATE TABLE `lams_learning_command` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `command_TEXT` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`uid`),
  KEY `idx_lesson_id` (`lesson_id`),
  KEY `idx_user_name` (`user_name`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_design`
--

DROP TABLE IF EXISTS `lams_learning_design`;

CREATE TABLE `lams_learning_design` (
  `learning_design_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_ui_id` int(11) DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `first_activity_id` bigint(20) DEFAULT NULL,
  `floating_activity_id` bigint(20) DEFAULT NULL,
  `max_id` int(11) DEFAULT NULL,
  `valid_design_flag` tinyint(1) NOT NULL,
  `read_only_flag` tinyint(1) NOT NULL,
  `date_read_only` datetime DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `original_user_id` bigint(20) NOT NULL,
  `help_text` mediumtext COLLATE utf8mb4_unicode_ci,
  `copy_type_id` tinyint(4) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `version` varchar(56) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `original_learning_design_id` bigint(20) DEFAULT NULL,
  `workspace_folder_id` bigint(20) DEFAULT NULL,
  `duration` bigint(38) DEFAULT NULL,
  `license_id` bigint(20) DEFAULT NULL,
  `license_TEXT` mediumtext COLLATE utf8mb4_unicode_ci,
  `last_modified_date_time` datetime DEFAULT NULL,
  `content_folder_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `edit_override_lock` tinyint(1) DEFAULT '0',
  `edit_override_user_id` bigint(20) DEFAULT NULL,
  `design_version` int(11) DEFAULT '1',
  `removed` tinyint(1) NOT NULL DEFAULT '0',
  `design_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`learning_design_id`),
  KEY `user_id` (`user_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  KEY `license_id` (`license_id`),
  KEY `copy_type_id` (`copy_type_id`),
  KEY `edit_override_user_id` (`edit_override_user_id`),
  KEY `idx_design_parent_id` (`original_learning_design_id`),
  KEY `idx_design_first_act` (`first_activity_id`),
  KEY `idx_design_floating_act` (`floating_activity_id`),
  CONSTRAINT `FK_lams_learning_design_3` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_learning_design_4` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_learning_design_5` FOREIGN KEY (`license_id`) REFERENCES `lams_license` (`license_id`),
  CONSTRAINT `FK_lams_learning_design_6` FOREIGN KEY (`copy_type_id`) REFERENCES `lams_copy_type` (`copy_type_id`),
  CONSTRAINT `FK_lams_learning_design_7` FOREIGN KEY (`edit_override_user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_design_access`
--

DROP TABLE IF EXISTS `lams_learning_design_access`;

CREATE TABLE `lams_learning_design_access` (
  `learning_design_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `access_date` datetime DEFAULT NULL,
  PRIMARY KEY (`learning_design_id`,`user_id`),
  KEY `FK_lams_learning_design_access_2` (`user_id`),
  CONSTRAINT `FK_lams_learning_design_access_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_learning_design_access_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_design_annotation`
--

DROP TABLE IF EXISTS `lams_learning_design_annotation`;

CREATE TABLE `lams_learning_design_annotation` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_id` bigint(20) NOT NULL,
  `ui_id` int(11) DEFAULT NULL,
  `title` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `xcoord` int(11) DEFAULT NULL,
  `ycoord` int(11) DEFAULT NULL,
  `end_xcoord` int(11) DEFAULT NULL,
  `end_ycoord` int(11) DEFAULT NULL,
  `color` char(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `size` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_lams_learning_design_annotation_1` (`learning_design_id`),
  CONSTRAINT `FK_lams_learning_design_annotation_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_library`
--

DROP TABLE IF EXISTS `lams_learning_library`;

CREATE TABLE `lams_learning_library` (
  `learning_library_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valid_flag` tinyint(1) NOT NULL DEFAULT '1',
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY (`learning_library_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_library_group`
--

DROP TABLE IF EXISTS `lams_learning_library_group`;

CREATE TABLE `lams_learning_library_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_library_to_group`
--

DROP TABLE IF EXISTS `lams_learning_library_to_group`;

CREATE TABLE `lams_learning_library_to_group` (
  `group_id` int(11) NOT NULL,
  `learning_library_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`learning_library_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_learning_transition`
--

DROP TABLE IF EXISTS `lams_learning_transition`;
 
CREATE TABLE `lams_learning_transition` (
  `transition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transition_ui_id` int(11) DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_activity_id` bigint(20) NOT NULL,
  `from_activity_id` bigint(20) NOT NULL,
  `learning_design_id` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  `to_ui_id` int(11) DEFAULT NULL,
  `from_ui_id` int(11) DEFAULT NULL,
  `transition_type` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`transition_id`),
  KEY `from_activity_id` (`from_activity_id`),
  KEY `to_activity_id` (`to_activity_id`),
  KEY `learning_design_id` (`learning_design_id`),
  CONSTRAINT `FK_learning_transition_2` FOREIGN KEY (`to_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_learning_transition_3` FOREIGN KEY (`from_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `lddefn_transition_ibfk_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_lesson`
--

DROP TABLE IF EXISTS `lams_lesson`;

CREATE TABLE `lams_lesson` (
  `lesson_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `create_date_time` datetime NOT NULL,
  `organisation_id` bigint(20) DEFAULT NULL,
  `class_grouping_id` bigint(20) DEFAULT NULL,
  `lesson_state_id` int(3) NOT NULL,
  `start_date_time` datetime DEFAULT NULL,
  `scheduled_number_days_to_lesson_finish` int(3) DEFAULT NULL,
  `schedule_start_date_time` datetime DEFAULT NULL,
  `end_date_time` datetime DEFAULT NULL,
  `schedule_end_date_time` datetime DEFAULT NULL,
  `previous_state_id` int(3) DEFAULT NULL,
  `learner_presence_avail` TINYINT(1) NOT NULL DEFAULT 0,
  `learner_im_avail` TINYINT(1) NOT NULL DEFAULT 0,
  `live_edit_enabled` TINYINT(1) NOT NULL DEFAULT 0,
  `enable_lesson_notifications` TINYINT(1) NOT NULL DEFAULT 0,
  `locked_for_edit` TINYINT(1) NOT NULL DEFAULT 0,
  `marks_released` TINYINT(1) NOT NULL DEFAULT 0,
  `enable_lesson_INTro` TINYINT(1) NOT NULL DEFAULT 0,
  `display_design_image` TINYINT(1) NOT NULL DEFAULT 0,
  `force_restart` TINYINT(1) NOT NULL DEFAULT 0,
  `allow_restart` TINYINT(1) NOT NULL DEFAULT 0,
  `gradebook_on_complete` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`lesson_id`),
  KEY `learning_design_id` (`learning_design_id`),
  KEY `user_id` (`user_id`),
  KEY `organisation_id` (`organisation_id`),
  KEY `lesson_state_id` (`lesson_state_id`),
  KEY `class_grouping_id` (`class_grouping_id`),
  CONSTRAINT `FK_lams_lesson_1_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`),
  CONSTRAINT `FK_lams_lesson_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_lesson_3` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_lesson_4` FOREIGN KEY (`lesson_state_id`) REFERENCES `lams_lesson_state` (`lesson_state_id`),
  CONSTRAINT `FK_lams_lesson_5` FOREIGN KEY (`class_grouping_id`) REFERENCES `lams_grouping` (`grouping_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_lesson_dependency`
--

DROP TABLE IF EXISTS `lams_lesson_dependency`;

CREATE TABLE `lams_lesson_dependency` (
  `lesson_id` bigint(20) NOT NULL,
  `preceding_lesson_id` bigint(20) NOT NULL,
  PRIMARY KEY (`lesson_id`,`preceding_lesson_id`),
  KEY `FK_lams_lesson_dependency_2` (`preceding_lesson_id`),
  CONSTRAINT `FK_lams_lesson_dependency_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_lesson_dependency_2` FOREIGN KEY (`preceding_lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_lesson_state`
--

DROP TABLE IF EXISTS `lams_lesson_state`;

CREATE TABLE `lams_lesson_state` (
  `lesson_state_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`lesson_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_license`
--

DROP TABLE IF EXISTS `lams_license`;

CREATE TABLE `lams_license` (
  `license_id` bigint(20) NOT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `default_flag` tinyint(1) NOT NULL DEFAULT '0',
  `picture_url` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `order_id` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`license_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_log_event`
--

DROP TABLE IF EXISTS `lams_log_event`;

CREATE TABLE `lams_log_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_event_type_id` int(5) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `occurred_date_time` datetime NOT NULL,
  `lesson_id` bigint(20) DEFAULT NULL,
  `activity_id` bigint(20) DEFAULT NULL,
  `target_user_id` bigint(20) DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `event_log_occurred_date_time` (`occurred_date_time`),
  KEY `FK_event_log_event_type_idx` (`log_event_type_id`),
  KEY `event_log_date_and_type` (`occurred_date_time`, `log_event_type_id`),
  CONSTRAINT `FK_event_log_event_type` FOREIGN KEY (`log_event_type_id`) REFERENCES `lams_log_event_type` (`log_event_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_log_event_type`
--

DROP TABLE IF EXISTS `lams_log_event_type`;

CREATE TABLE `lams_log_event_type` (
  `log_event_type_id` int(5) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `area` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`log_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_notebook_entry`
--

DROP TABLE IF EXISTS `lams_notebook_entry`;

CREATE TABLE `lams_notebook_entry` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `external_id` bigint(20) DEFAULT NULL,
  `external_id_type` int(11) DEFAULT NULL,
  `external_signature` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `entry` mediumtext COLLATE utf8mb4_unicode_ci,
  `create_date` datetime DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `ext_sig_user` (`external_id`,`external_id_type`,`external_signature`,`user_id`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_notification_event`
--

DROP TABLE IF EXISTS `lams_notification_event`;

CREATE TABLE `lams_notification_event` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `scope` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_session_id` bigint(20) DEFAULT NULL,
  `subject` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `message` mediumtext COLLATE utf8mb4_unicode_ci,
  `fail_time` datetime DEFAULT NULL,
  `html_format` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `scope` (`scope`,`name`,`event_session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_notification_subscription`
--

DROP TABLE IF EXISTS `lams_notification_subscription`;

CREATE TABLE `lams_notification_subscription` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `event_uid` bigint(20) DEFAULT NULL,
  `delivery_method_id` tinyint(3) unsigned DEFAULT NULL,
  `last_operation_message` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`uid`),
  KEY `EventSubscriptionsToUsers` (`user_id`),
  KEY `event_uid` (`event_uid`),
  CONSTRAINT `EventSubscriptionsToEvent` FOREIGN KEY (`event_uid`) REFERENCES `lams_notification_event` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EventSubscriptionsToUsers` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_organisation`
--

DROP TABLE IF EXISTS `lams_organisation`;

CREATE TABLE `lams_organisation` (
  `organisation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci,
  `parent_organisation_id` bigint(20) DEFAULT NULL,
  `organisation_type_id` int(3) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `organisation_state_id` int(3) NOT NULL,
  `admin_add_new_users` tinyint(1) NOT NULL DEFAULT '0',
  `admin_browse_all_users` tinyint(1) NOT NULL DEFAULT '0',
  `admin_change_status` tinyint(1) NOT NULL DEFAULT '0',
  `admin_create_guest` tinyint(1) NOT NULL DEFAULT '0',
  `enable_course_notifications` tinyint(1) NOT NULL DEFAULT '0',
  `enable_learner_gradebook` tinyint(1) NOT NULL DEFAULT '0',
  `enable_single_activity_lessons` tinyint(1) NOT NULL DEFAULT '1',
  `enable_live_edit` tinyint(1) NOT NULL DEFAULT '1',
  `enable_kumalive` tinyint(1) NOT NULL DEFAULT '0',
  `archived_date` datetime DEFAULT NULL,
  `ordered_lesson_ids` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`organisation_id`),
  KEY `organisation_type_id` (`organisation_type_id`),
  KEY `parent_organisation_id` (`parent_organisation_id`),
  KEY `organisation_state_id` (`organisation_state_id`),
  CONSTRAINT `FK_lams_organisation_1` FOREIGN KEY (`organisation_type_id`) REFERENCES `lams_organisation_type` (`organisation_type_id`),
  CONSTRAINT `FK_lams_organisation_3` FOREIGN KEY (`parent_organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_organisation_4` FOREIGN KEY (`organisation_state_id`) REFERENCES `lams_organisation_state` (`organisation_state_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_organisation_group`
--

DROP TABLE IF EXISTS `lams_organisation_group`;

CREATE TABLE `lams_organisation_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grouping_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  KEY `FK_lams_organisation_group_1` (`grouping_id`),
  CONSTRAINT `FK_lams_organisation_group_1` FOREIGN KEY (`grouping_id`) REFERENCES `lams_organisation_grouping` (`grouping_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_organisation_grouping`
--

DROP TABLE IF EXISTS `lams_organisation_grouping`;

CREATE TABLE `lams_organisation_grouping` (
  `grouping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`grouping_id`),
  KEY `FK_lams_organisation_grouping_1` (`organisation_id`),
  CONSTRAINT `FK_lams_organisation_grouping_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_organisation_state`
--

DROP TABLE IF EXISTS `lams_organisation_state`;

CREATE TABLE `lams_organisation_state` (
  `organisation_state_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`organisation_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_organisation_type`
--

DROP TABLE IF EXISTS `lams_organisation_type`;

CREATE TABLE `lams_organisation_type` (
  `organisation_type_id` int(3) NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`organisation_type_id`),
  UNIQUE KEY `UQ_lams_organisation_type_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_outcome`
--

DROP TABLE IF EXISTS `lams_outcome`;

CREATE TABLE `lams_outcome` (
  `outcome_id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `scale_id` mediumint(9) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY (`outcome_id`),
  KEY `name` (`name`),
  KEY `code` (`code`),
  KEY `FK_lams_outcome_2` (`scale_id`),
  CONSTRAINT `FK_lams_outcome_2` FOREIGN KEY (`scale_id`) REFERENCES `lams_outcome_scale` (`scale_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_outcome_mapping`
--

DROP TABLE IF EXISTS `lams_outcome_mapping`;

CREATE TABLE `lams_outcome_mapping` (
  `mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `outcome_id` mediumint(9) NOT NULL,
  `lesson_id` bigint(20) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  qb_question_id INT,
  PRIMARY KEY (`mapping_id`),
  KEY `FK_lams_outcome_mapping_1` (`outcome_id`),
  KEY `FK_lams_outcome_mapping_2` (`lesson_id`),
  KEY `FK_lams_outcome_mapping_3` (`tool_content_id`),
  CONSTRAINT `FK_lams_outcome_mapping_1` FOREIGN KEY (`outcome_id`) REFERENCES `lams_outcome` (`outcome_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_outcome_mapping_2` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_outcome_mapping_3` FOREIGN KEY (`tool_content_id`) REFERENCES `lams_tool_content` (`tool_content_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_outcome_result`
--

DROP TABLE IF EXISTS `lams_outcome_result`;

CREATE TABLE `lams_outcome_result` (
  `result_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mapping_id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `value` tinyint(4) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY (`result_id`),
  KEY `FK_lams_outcome_result_1` (`mapping_id`),
  KEY `FK_lams_outcome_result_2` (`user_id`),
  KEY `FK_lams_outcome_result_3` (`create_by`),
  CONSTRAINT `FK_lams_outcome_result_1` FOREIGN KEY (`mapping_id`) REFERENCES `lams_outcome_mapping` (`mapping_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_outcome_result_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_outcome_result_3` FOREIGN KEY (`create_by`) REFERENCES `lams_user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_outcome_scale`
--

DROP TABLE IF EXISTS `lams_outcome_scale`;
			 
CREATE TABLE `lams_outcome_scale` (
  `scale_id` mediumint(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY (`scale_id`),
  KEY `name` (`name`),
  KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_outcome_scale_item`
--

DROP TABLE IF EXISTS `lams_outcome_scale_item`;

CREATE TABLE `lams_outcome_scale_item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `scale_id` mediumint(9) DEFAULT NULL,
  `value` tinyint(4) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `FK_lams_outcome_scale_item_1` (`scale_id`),
  CONSTRAINT `FK_lams_outcome_scale_item_1` FOREIGN KEY (`scale_id`) REFERENCES `lams_outcome_scale` (`scale_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_password_request`
--

DROP TABLE IF EXISTS `lams_password_request`;

CREATE TABLE `lams_password_request` (
  `request_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `request_key` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_date` datetime NOT NULL,
  PRIMARY KEY (`request_id`),
  UNIQUE KEY `IX_lams_psswd_rqst_key` (`request_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_planner_activity_metadata`
--

DROP TABLE IF EXISTS `lams_planner_activity_metadata`;

CREATE TABLE `lams_planner_activity_metadata` (
  `activity_id` bigint(20) NOT NULL,
  `collapsed` tinyint(1) DEFAULT '0',
  `expanded` tinyint(1) DEFAULT '0',
  `hidden` tinyint(1) DEFAULT '0',
  `editing_advice` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  KEY `FK_lams_planner_metadata_primary` (`activity_id`),
  CONSTRAINT `FK_lams_planner_metadata_primary` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_planner_node_role`
--

DROP TABLE IF EXISTS `lams_planner_node_role`;

CREATE TABLE `lams_planner_node_role` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` int(6) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_planner_node_role_user` (`user_id`),
  KEY `FK_planner_node_role_node` (`node_uid`),
  KEY `FK_planner_node_role_role` (`role_id`),
  CONSTRAINT `FK_planner_node_role_node` FOREIGN KEY (`node_uid`) REFERENCES `lams_planner_nodes` (`uid`) ON DELETE CASCADE,
  CONSTRAINT `FK_planner_node_role_role` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`) ON DELETE CASCADE,
  CONSTRAINT `FK_planner_node_role_user` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_planner_nodes`
--

DROP TABLE IF EXISTS `lams_planner_nodes`;

CREATE TABLE `lams_planner_nodes` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_uid` bigint(20) DEFAULT NULL,
  `order_id` tinyint(3) unsigned NOT NULL,
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  `content_folder_id` char(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `brief_desc` mediumtext COLLATE utf8mb4_unicode_ci,
  `full_desc` mediumtext COLLATE utf8mb4_unicode_ci,
  `ld_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `permissions` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `parent_uid` (`parent_uid`,`order_id`),
  KEY `FK_lams_planner_node_user` (`user_id`),
  CONSTRAINT `FK_lams_planner_node_parent` FOREIGN KEY (`parent_uid`) REFERENCES `lams_planner_nodes` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_planner_node_user` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_planner_recent_learning_designs`
--

DROP TABLE IF EXISTS `lams_planner_recent_learning_designs`;

CREATE TABLE `lams_planner_recent_learning_designs` (
  `user_id` bigint(20) NOT NULL,
  `learning_design_id` bigint(20) NOT NULL,
  `last_modified_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`learning_design_id`),
  KEY `FK_lams_planner_recent_learning_designs_2` (`learning_design_id`),
  CONSTRAINT `FK_lams_planner_recent_learning_designs_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_planner_recent_learning_designs_2` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_policy`
--

DROP TABLE IF EXISTS `lams_policy`;

CREATE TABLE `lams_policy` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `policy_id` bigint(20) DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `policy_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` mediumtext COLLATE utf8mb4_unicode_ci,
  `summary` text COLLATE utf8mb4_unicode_ci,
  `full_policy` text COLLATE utf8mb4_unicode_ci,
  `last_modified` datetime NOT NULL,
  `policy_state_id` int(3) NOT NULL,
  `policy_type_id` int(3) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `created_by` (`created_by`),
  KEY `policy_state_id` (`policy_state_id`),
  KEY `policy_type_id` (`policy_type_id`),
  CONSTRAINT `FK_lams_lesson_1` FOREIGN KEY (`created_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_policy_2` FOREIGN KEY (`policy_state_id`) REFERENCES `lams_policy_state` (`policy_state_id`),
  CONSTRAINT `FK_lams_policy_3` FOREIGN KEY (`policy_type_id`) REFERENCES `lams_policy_type` (`policy_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_policy_consent`
--

DROP TABLE IF EXISTS `lams_policy_consent`;

CREATE TABLE `lams_policy_consent` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_agreed_on` datetime NOT NULL,
  `policy_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `policy_uid` (`policy_uid`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_consent_1_1` FOREIGN KEY (`policy_uid`) REFERENCES `lams_policy` (`uid`),
  CONSTRAINT `FK_lams_consent_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_policy_state`
--

DROP TABLE IF EXISTS `lams_policy_state`;

 
CREATE TABLE `lams_policy_state` (
  `policy_state_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`policy_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_policy_type`
--

DROP TABLE IF EXISTS `lams_policy_type`;

CREATE TABLE `lams_policy_type` (
  `policy_type_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`policy_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_presence_chat_msgs`
--

DROP TABLE IF EXISTS `lams_presence_chat_msgs`;

CREATE TABLE `lams_presence_chat_msgs` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint(20) DEFAULT NULL,
  `from_user` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `to_user` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_sent` datetime DEFAULT NULL,
  `message` varchar(1023) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_lams_presence_chat_msgs_lesson` (`lesson_id`),
  KEY `idx_lams_presence_chat_msgs_from` (`from_user`),
  KEY `idx_lams_presence_chat_msgs_to` (`to_user`),
  CONSTRAINT `FK_lams_presence_chat_msgs_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_presence_user`
--

DROP TABLE IF EXISTS `lams_presence_user`;

CREATE TABLE `lams_presence_user` (
  `nickname` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `last_presence` datetime DEFAULT NULL,
  PRIMARY KEY (`nickname`,`lesson_id`),
  KEY `FK_lams_presence_user_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_presence_user_lesson` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_progress_attempted`
--

DROP TABLE IF EXISTS `lams_progress_attempted`;

CREATE TABLE `lams_progress_attempted` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `start_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_current_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_progress_current_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_progress_attempted_archive`
--

DROP TABLE IF EXISTS `lams_progress_attempted_archive`;

CREATE TABLE `lams_progress_attempted_archive` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `start_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`,`activity_id`),
  KEY `FK_lams_progress_current_archive_2` (`activity_id`),
  CONSTRAINT `FK_lams_progress_current_archive_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress_archive` (`learner_progress_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_progress_current_archive_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_progress_completed`
--

DROP TABLE IF EXISTS `lams_progress_completed`;

CREATE TABLE `lams_progress_completed` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `completed_date_time` datetime DEFAULT NULL,
  `start_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_completed_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_progress_completed_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_progress_completed_archive`
--

DROP TABLE IF EXISTS `lams_progress_completed_archive`;

CREATE TABLE `lams_progress_completed_archive` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `completed_date_time` datetime DEFAULT NULL,
  `start_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`,`activity_id`),
  KEY `FK_lams_progress_completed_archive_2` (`activity_id`),
  CONSTRAINT `FK_lams_progress_completed_archive_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress_archive` (`learner_progress_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_progress_completed_archive_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_rating`
--

DROP TABLE IF EXISTS `lams_rating`;

CREATE TABLE `lams_rating` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `rating_criteria_id` bigint(20) NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `rating` float DEFAULT NULL,
  `tool_session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `rating_criteria_id` (`rating_criteria_id`),
  KEY `user_id` (`user_id`),
  KEY `FK_lams_rating_3` (`tool_session_id`),
  CONSTRAINT `FK_lams_rating_1` FOREIGN KEY (`rating_criteria_id`) REFERENCES `lams_rating_criteria` (`rating_criteria_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_rating_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_rating_comment`
--

DROP TABLE IF EXISTS `lams_rating_comment`;

CREATE TABLE `lams_rating_comment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `rating_criteria_id` bigint(20) NOT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `comment` mediumtext COLLATE utf8mb4_unicode_ci,
  `posted_date` datetime DEFAULT NULL,
  `tool_session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `rating_criteria_id` (`rating_criteria_id`),
  KEY `user_id` (`user_id`),
  KEY `FK_lams_rating_comment_3` (`tool_session_id`),
  CONSTRAINT `FK_lams_rating_comment_1` FOREIGN KEY (`rating_criteria_id`) REFERENCES `lams_rating_criteria` (`rating_criteria_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_rating_comment_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_rating_criteria`
--

DROP TABLE IF EXISTS `lams_rating_criteria`;
 
CREATE TABLE `lams_rating_criteria` (
  `rating_criteria_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rating_criteria_type_id` int(11) NOT NULL DEFAULT '0',
  `comments_enabled` tinyint(1) NOT NULL DEFAULT '0',
  `comments_min_words_limit` int(11) DEFAULT '0',
  `order_id` int(11) NOT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `lesson_id` bigint(20) DEFAULT NULL,
  `rating_style` bigint(20) NOT NULL DEFAULT '1',
  `max_rating` bigint(20) NOT NULL DEFAULT '5',
  `minimum_rates` int(11) DEFAULT '0',
  `maximum_rates` int(11) DEFAULT '0',
  PRIMARY KEY (`rating_criteria_id`),
  KEY `rating_criteria_type_id` (`rating_criteria_type_id`),
  KEY `tool_content_id` (`tool_content_id`),
  KEY `lesson_id` (`lesson_id`),
  CONSTRAINT `FK_lams_rating_criteria_1` FOREIGN KEY (`rating_criteria_type_id`) REFERENCES `lams_rating_criteria_type` (`rating_criteria_type_id`),
  CONSTRAINT `FK_lams_rating_criteria_2` FOREIGN KEY (`tool_content_id`) REFERENCES `lams_tool_content` (`tool_content_id`),
  CONSTRAINT `FK_lams_rating_criteria_3` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_rating_criteria_type`
--

DROP TABLE IF EXISTS `lams_rating_criteria_type`;

CREATE TABLE `lams_rating_criteria_type` (
  `rating_criteria_type_id` int(11) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`rating_criteria_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_role`
--

DROP TABLE IF EXISTS `lams_role`;

CREATE TABLE `lams_role` (
  `role_id` int(6) NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `gname` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_signup_organisation`
--

DROP TABLE IF EXISTS `lams_signup_organisation`;

CREATE TABLE `lams_signup_organisation` (
  `signup_organisation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `add_to_lessons` tinyint(1) DEFAULT '1',
  `add_as_staff` tinyint(1) DEFAULT '0',
  `add_with_author` tinyint(1) DEFAULT '0',
  `add_with_monitor` tinyint(1) DEFAULT '0',
  `email_verify` tinyint(1) DEFAULT '0',
  `course_key` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `blurb` text COLLATE utf8mb4_unicode_ci,
  `create_date` datetime DEFAULT NULL,
  `disabled` tinyint(1) DEFAULT '0',
  `conTEXT` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `login_tab_active` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`signup_organisation_id`),
  UNIQUE KEY `conTEXT` (`conTEXT`),
  KEY `organisation_id` (`organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_supported_locale`
--

DROP TABLE IF EXISTS `lams_supported_locale`;

CREATE TABLE `lams_supported_locale` (
  `locale_id` int(11) NOT NULL AUTO_INCREMENT,
  `language_iso_code` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `country_iso_code` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `direction` varchar(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fckeditor_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`locale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_system_tool`
--

DROP TABLE IF EXISTS `lams_system_tool`;

CREATE TABLE `lams_system_tool` (
  `system_tool_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_activity_type_id` int(11) NOT NULL,
  `tool_display_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `learner_url` text COLLATE utf8mb4_unicode_ci,
  `learner_preview_url` text COLLATE utf8mb4_unicode_ci,
  `learner_progress_url` text COLLATE utf8mb4_unicode_ci,
  `monitor_url` text COLLATE utf8mb4_unicode_ci,
  `contribute_url` text COLLATE utf8mb4_unicode_ci,
  `help_url` text COLLATE utf8mb4_unicode_ci,
  `create_date_time` datetime NOT NULL,
  `admin_url` text COLLATE utf8mb4_unicode_ci,
  `pedagogical_planner_url` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`system_tool_id`),
  UNIQUE KEY `UQ_systool_activity_type` (`learning_activity_type_id`),
  CONSTRAINT `FK_lams_system_tool` FOREIGN KEY (`learning_activity_type_id`) REFERENCES `lams_learning_activity_type` (`learning_activity_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_text_search_condition`
--

DROP TABLE IF EXISTS `lams_text_search_condition`;

CREATE TABLE `lams_text_search_condition` (
  `condition_id` bigint(20) NOT NULL,
  `text_search_all_words` text COLLATE utf8mb4_unicode_ci,
  `text_search_phrase` text COLLATE utf8mb4_unicode_ci,
  `text_search_any_words` text COLLATE utf8mb4_unicode_ci,
  `text_search_excluded_words` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`condition_id`),
  CONSTRAINT `TextSearchConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_theme`
--

DROP TABLE IF EXISTS `lams_theme`;

CREATE TABLE `lams_theme` (
  `theme_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_directory` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`theme_id`),
  UNIQUE KEY `UQ_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_timezone`
--

DROP TABLE IF EXISTS `lams_timezone`;

CREATE TABLE `lams_timezone` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `timezone_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `server_timezone` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_tool`
--

DROP TABLE IF EXISTS `lams_tool`;

CREATE TABLE `lams_tool` (
  `tool_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_signature` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tool_display_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `tool_identifier` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tool_version` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `learning_library_id` bigint(20) DEFAULT NULL,
  `default_tool_content_id` bigint(20) DEFAULT NULL,
  `valid_flag` tinyint(1) NOT NULL DEFAULT '1',
  `grouping_support_type_id` int(3) NOT NULL,
  `learner_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `learner_preview_url` text COLLATE utf8mb4_unicode_ci,
  `learner_progress_url` text COLLATE utf8mb4_unicode_ci,
  `author_url` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `monitor_url` text COLLATE utf8mb4_unicode_ci,
  `pedagogical_planner_url` text COLLATE utf8mb4_unicode_ci,
  `help_url` text COLLATE utf8mb4_unicode_ci,
  `create_date_time` datetime NOT NULL,
  `language_file` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `modified_date_time` datetime DEFAULT NULL,
  `admin_url` text COLLATE utf8mb4_unicode_ci,
  `supports_outputs` tinyint(1) DEFAULT '0',
  `ext_lms_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`tool_id`),
  UNIQUE KEY `UQ_lams_tool_sig` (`tool_signature`),
  UNIQUE KEY `UQ_lams_tool_class_name` (`service_name`),
  KEY `learning_library_id` (`learning_library_id`),
  KEY `grouping_support_type_id` (`grouping_support_type_id`),
  CONSTRAINT `FK_lams_tool_1` FOREIGN KEY (`learning_library_id`) REFERENCES `lams_learning_library` (`learning_library_id`),
  CONSTRAINT `FK_lams_tool_2` FOREIGN KEY (`grouping_support_type_id`) REFERENCES `lams_grouping_support_type` (`grouping_support_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_tool_content`
--

DROP TABLE IF EXISTS `lams_tool_content`;

CREATE TABLE `lams_tool_content` (
  `tool_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tool_content_id`),
  KEY `tool_id` (`tool_id`),
  CONSTRAINT `FK_lams_tool_content_1` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_tool_session`
--

DROP TABLE IF EXISTS `lams_tool_session`;

CREATE TABLE `lams_tool_session` (
  `tool_session_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_session_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tool_session_type_id` int(3) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `tool_session_state_id` int(3) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `unique_key` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`tool_session_id`),
  UNIQUE KEY `UQ_lams_tool_session_1` (`unique_key`),
  KEY `tool_session_state_id` (`tool_session_state_id`),
  KEY `user_id` (`user_id`),
  KEY `tool_session_type_id` (`tool_session_type_id`),
  KEY `activity_id` (`activity_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `FK_lams_tool_session_1` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`),
  CONSTRAINT `FK_lams_tool_session_4` FOREIGN KEY (`tool_session_state_id`) REFERENCES `lams_tool_session_state` (`tool_session_state_id`),
  CONSTRAINT `FK_lams_tool_session_5` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_tool_session_7` FOREIGN KEY (`tool_session_type_id`) REFERENCES `lams_tool_session_type` (`tool_session_type_id`),
  CONSTRAINT `FK_lams_tool_session_8` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_tool_session_state`
--

DROP TABLE IF EXISTS `lams_tool_session_state`;

CREATE TABLE `lams_tool_session_state` (
  `tool_session_state_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`tool_session_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_tool_session_type`
--

DROP TABLE IF EXISTS `lams_tool_session_type`;

CREATE TABLE `lams_tool_session_type` (
  `tool_session_type_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`tool_session_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_user`
--

DROP TABLE IF EXISTS `lams_user`;

CREATE TABLE `lams_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` char(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `salt` char(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `two_factor_auth_enabled` tinyint(1) DEFAULT '0',
  `two_factor_auth_secret` char(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `first_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_line_1` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_line_2` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_line_3` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `postcode` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `day_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `evening_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile_phone` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fax` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email_verified` tinyint(1) DEFAULT '1',
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `authentication_method_id` bigint(20) NOT NULL DEFAULT '0',
  `workspace_folder_id` bigint(20) DEFAULT NULL,
  `theme_id` bigint(20) DEFAULT NULL,
  `locale_id` int(11) DEFAULT NULL,
  `portrait_uuid` BINARY(16),
  `change_password` tinyint(1) DEFAULT '0',
  `timezone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `first_login` tinyint(1) DEFAULT '1',
  `modified_date` datetime DEFAULT NULL,
  `last_visited_organisation_id` bigint(20) DEFAULT NULL,
  `failed_attempts` tinyint(4) DEFAULT NULL,
  `lock_out_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UQ_lams_user_login` (`login`),
  KEY `authentication_method_id` (`authentication_method_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  KEY `theme_id` (`theme_id`),
  KEY `locale_id` (`locale_id`),
  KEY `FK_lams_user_7` (`last_visited_organisation_id`),
  KEY `email` (`email`),
  CONSTRAINT `FK_lams_user_1` FOREIGN KEY (`authentication_method_id`) REFERENCES `lams_authentication_method` (`authentication_method_id`),
  CONSTRAINT `FK_lams_user_2` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_user_5` FOREIGN KEY (`theme_id`) REFERENCES `lams_theme` (`theme_id`),
  CONSTRAINT `FK_lams_user_6` FOREIGN KEY (`locale_id`) REFERENCES `lams_supported_locale` (`locale_id`),
  CONSTRAINT `FK_lams_user_7` FOREIGN KEY (`last_visited_organisation_id`) REFERENCES `lams_organisation` (`organisation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_user_group`
--

DROP TABLE IF EXISTS `lams_user_group`;

CREATE TABLE `lams_user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `scheduled_lesson_end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `FK_lams_user_group_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_user_group_2` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_user_organisation`
--

DROP TABLE IF EXISTS `lams_user_organisation`;

CREATE TABLE `lams_user_organisation` (
  `user_organisation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_organisation_id`),
  KEY `organisation_id` (`organisation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_user_organisation_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_user_organisation_group`
--

DROP TABLE IF EXISTS `lams_user_organisation_group`;

CREATE TABLE `lams_user_organisation_group` (
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`group_id`,`user_id`),
  KEY `FK_lams_user_organisation_group_2` (`user_id`),
  CONSTRAINT `FK_lams_user_organisation_group_1` FOREIGN KEY (`group_id`) REFERENCES `lams_organisation_group` (`group_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_user_organisation_group_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_user_organisation_role`
--

DROP TABLE IF EXISTS `lams_user_organisation_role`;

CREATE TABLE `lams_user_organisation_role` (
  `user_organisation_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_organisation_id` bigint(20) NOT NULL,
  `role_id` int(6) NOT NULL,
  PRIMARY KEY (`user_organisation_role_id`),
  KEY `role_id` (`role_id`),
  KEY `user_organisation_id` (`user_organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_role_2` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`),
  CONSTRAINT `FK_lams_user_organisation_role_3` FOREIGN KEY (`user_organisation_id`) REFERENCES `lams_user_organisation` (`user_organisation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_workspace_folder`
--

DROP TABLE IF EXISTS `lams_workspace_folder`;

CREATE TABLE `lams_workspace_folder` (
  `workspace_folder_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_folder_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `organisation_id` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  `last_modified_date_time` datetime DEFAULT NULL,
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  PRIMARY KEY (`workspace_folder_id`),
  KEY `parent_folder_id` (`parent_folder_id`),
  KEY `lams_workspace_folder_type_id` (`lams_workspace_folder_type_id`),
  CONSTRAINT `FK_lams_workspace_folder_2` FOREIGN KEY (`parent_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_workspace_folder_4` FOREIGN KEY (`lams_workspace_folder_type_id`) REFERENCES `lams_workspace_folder_type` (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_workspace_folder_content`
--

DROP TABLE IF EXISTS `lams_workspace_folder_content`;

CREATE TABLE `lams_workspace_folder_content` (
  `folder_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type_id` int(3) NOT NULL,
  `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_date_time` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `workspace_folder_id` bigint(20) NOT NULL,
  `uuid` bigint(20) DEFAULT NULL,
  `version_id` bigint(20) DEFAULT NULL,
  `mime_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`folder_content_id`),
  UNIQUE KEY `unique_content_name` (`name`,`workspace_folder_id`,`mime_type`),
  UNIQUE KEY `unique_node_version` (`workspace_folder_id`,`uuid`,`version_id`),
  KEY `content_type_id` (`content_type_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_workspace_folder_content_1` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_workspace_folder_type`
--

DROP TABLE IF EXISTS `lams_workspace_folder_type`;

CREATE TABLE `lams_workspace_folder_type` (
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `patches`
--

DROP TABLE IF EXISTS `patches`;

CREATE TABLE `patches` (
  `system_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `patch_level` int(11) NOT NULL,
  `patch_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `patch_in_progress` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'F',
  PRIMARY KEY (`system_name`,`patch_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_qb_question`
--

DROP TABLE IF EXISTS `lams_qb_question`;

CREATE TABLE `lams_qb_question` (
  `uid` BIGINT AUTO_INCREMENT,
  `uuid` BINARY(16) NOT NULL,
  `type` TINYINT NOT NULL,
  `question_id` INT NOT NULL,
  `version` SMALLINT NOT NULL DEFAULT 1,
  `create_date` DATETIME NOT NULL DEFAULT NOW(),
  `content_folder_id` char(36) NOT NULL,
  `name` TEXT,
  `description` MEDIUMTEXT,
  `max_mark` INT,
  `feedback` TEXT,
  `penalty_factor` float DEFAULT 0,
  `answer_required` TINYINT(1) DEFAULT 0,
  `multiple_answers_allowed` TINYINT(1) DEFAULT 0,
  `incorrect_answer_nullifies_mark` TINYINT(1) DEFAULT 0,
  `feedback_on_correct` TEXT,
  `feedback_on_partially_correct` TEXT,
  `feedback_on_incorrect` TEXT,
  `shuffle` TINYINT(1) DEFAULT 0,
  `prefix_answers_with_letters` TINYINT(1) DEFAULT 0,
  `case_sensitive` TINYINT(1) DEFAULT 0,
  `correct_answer` TINYINT(1) DEFAULT 0,
  `allow_rich_editor` TINYINT(1) DEFAULT 0,
  `max_words_limit` int(11) DEFAULT 0,
  `min_words_limit` int(11) DEFAULT 0,
  `hedging_justification_enabled` TINYINT(1) DEFAULT 0,
  `autocomplete_enabled` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (uid),
  CONSTRAINT UQ_question_version UNIQUE INDEX (question_id, version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                               
DROP TRIGGER IF EXISTS before_insert_qb_question;
			
CREATE TRIGGER before_insert_qb_question
  BEFORE INSERT ON lams_qb_question
  FOR EACH ROW
  SET new.uuid = IF(new.uuid IS NULL, UUID_TO_BIN(UUID()), new.uuid);


--
-- Table structure for table `lams_qb_tool_question`
--

DROP TABLE IF EXISTS `lams_qb_tool_question`;

CREATE TABLE lams_qb_tool_question (
  `tool_question_uid` BIGINT AUTO_INCREMENT,
  `qb_question_uid` BIGINT NOT NULL,
  `tool_content_id` BIGINT NOT NULL,
  `display_order` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (tool_question_uid),
  INDEX (tool_content_id),
  CONSTRAINT FK_lams_qb_tool_question_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON UPDATE CASCADE
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_qb_option`
--

DROP TABLE IF EXISTS `lams_qb_option`;

CREATE TABLE lams_qb_option (
  `uid` BIGINT AUTO_INCREMENT,
  `qb_question_uid` BIGINT NOT NULL,
  `display_order` TINYINT NOT NULL DEFAULT 1,
  `name` TEXT,
 `matching_pair` TEXT,
  `numerical_option` float DEFAULT 0,
  `max_mark` float DEFAULT 0,
  `accepted_error` float DEFAULT 0,
  `feedback` TEXT,
  PRIMARY KEY (uid),
  INDEX (display_order),
  CONSTRAINT FK_lams_qb_option_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_qb_question_unit`
--

DROP TABLE IF EXISTS `lams_qb_question_unit`;

CREATE TABLE lams_qb_question_unit (
  `uid` BIGINT AUTO_INCREMENT,
  `qb_question_uid` BIGINT NOT NULL,
  `display_order` TINYINT NOT NULL DEFAULT 1,
  `multiplier` float DEFAULT 0,
  `name` varchar(255),
  PRIMARY KEY (uid),
  CONSTRAINT FK_lams_qb_question_unit_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_qb_tool_answer`
--

DROP TABLE IF EXISTS `lams_qb_tool_answer`;

CREATE TABLE lams_qb_tool_answer (
  `answer_uid` BIGINT AUTO_INCREMENT,
  `tool_question_uid` BIGINT NOT NULL,
  `qb_option_uid` BIGINT DEFAULT NULL,
  `answer` MEDIUMTEXT,
  PRIMARY KEY (answer_uid),
  CONSTRAINT FK_lams_qb_tool_answer_1 FOREIGN KEY (tool_question_uid) REFERENCES lams_qb_tool_question (tool_question_uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_qb_tool_answer_2 FOREIGN KEY (qb_option_uid) REFERENCES lams_qb_option (uid)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_sequence_generator`
--

DROP TABLE IF EXISTS `lams_sequence_generator`;

CREATE TABLE lams_sequence_generator (
  lams_qb_question_question_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX IDX_lams_qb_question_question_id ON lams_sequence_generator(lams_qb_question_question_id);


--
-- Table structure for table `lams_qb_collection`
--

DROP TABLE IF EXISTS `lams_qb_collection`;

CREATE TABLE lams_qb_collection (
  `uid` BIGINT AUTO_INCREMENT, 
  `name` VARCHAR(255),
  `user_id` BIGINT,
  `personal` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (uid),
  INDEX (personal),
  CONSTRAINT FK_lams_qb_collection_1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_qb_collection_question`
--

DROP TABLE IF EXISTS `lams_qb_collection_question`;

CREATE TABLE lams_qb_collection_question (
  `collection_uid`  BIGINT NOT NULL,
  `qb_question_id` INT NOT NULL,
  CONSTRAINT FK_lams_qb_collection_question_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_qb_collection_question_2 FOREIGN KEY (qb_question_id) REFERENCES lams_qb_question (question_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_qb_collection_organisation`
--

DROP TABLE IF EXISTS `lams_qb_collection_organisation`;

CREATE TABLE lams_qb_collection_organisation (
  `collection_uid`  BIGINT NOT NULL,
  `organisation_id` BIGINT NOT NULL,
  CONSTRAINT FK_lams_qb_collection_share_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_qb_collection_share_2 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


--
-- Table structure for table `lams_user_organisation_collapsed`
--

DROP TABLE IF EXISTS `lams_user_organisation_collapsed`;

CREATE TABLE `lams_user_organisation_collapsed` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `collapsed` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`uid`),
  KEY `organisation_id` (`organisation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


SET FOREIGN_KEY_CHECKS=1;