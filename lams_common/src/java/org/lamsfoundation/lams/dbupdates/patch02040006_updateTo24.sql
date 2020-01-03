SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch02040000.sql to patch02040006.sql
-- It should upgrade LAMS to version 2.4

-- LDEV-2369
ALTER TABLE lams_user ADD COLUMN tutorials_disabled TINYINT(1) DEFAULT 0;
ALTER TABLE lams_user ADD COLUMN first_login TINYINT(1) DEFAULT 1;

CREATE TABLE lams_user_disabled_tutorials (
     user_id BIGINT(20) NOT NULL
   , page_str CHAR(5) NOT NULL
   , CONSTRAINT FK_lams_user_disabled_tutorials_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (user_id,page_str)
)ENGINE=InnoDB;

-- LDEV-2374
ALTER TABLE lams_learning_activity ADD COLUMN transition_to_id BIGINT(20);
ALTER TABLE lams_learning_activity ADD COLUMN transition_from_id BIGINT(20);

ALTER TABLE lams_learning_transition MODIFY COLUMN learning_design_id BIGINT(20);
ALTER TABLE lams_learning_transition DROP INDEX UQ_transition_activities;
ALTER TABLE lams_learning_transition ADD COLUMN transition_type TINYINT NOT NULL DEFAULT 0;
ALTER TABLE lams_learning_activity  
	  ADD CONSTRAINT FK_lams_learning_activity_15 FOREIGN KEY (transition_to_id)
                  REFERENCES lams_learning_transition (transition_id)
     , ADD CONSTRAINT FK_lams_learning_activity_16 FOREIGN KEY (transition_from_id)
                  REFERENCES lams_learning_transition (transition_id);

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



-- SIF-10 Addding a release date to the lesson table so we can 
-- track release events
alter table lams_lesson add column release_date DATETIME;
alter table lams_lesson add index idx_release_date (release_date);

-- LDEV-2550

DROP TABLE IF EXISTS lams_log_event;
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

DELETE FROM lams_log_event_type;
INSERT INTO lams_log_event_type VALUES (1, 'TYPE_TEACHER_LEARNING_DESIGN_CREATE');
INSERT INTO lams_log_event_type VALUES (2, 'TYPE_TEACHER_LESSON_CREATE');
INSERT INTO lams_log_event_type VALUES (3, 'TYPE_TEACHER_LESSON_START');
INSERT INTO lams_log_event_type VALUES (4, 'TYPE_TEACHER_LESSON_CHANGE_STATE');
INSERT INTO lams_log_event_type VALUES (5, 'TYPE_LEARNER_ACTIVITY_START');
INSERT INTO lams_log_event_type VALUES (6, 'TYPE_LEARNER_ACTIVITY_FINISH');

-- LDEV-2639 Make print button in LAMS learner optional 
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)  
VALUES ('DisplayPrintButton','false', 'config.display.print.button', 'config.header.features', 'BOOLEAN', 1);

-- LDEV-2628 Report instant completion
ALTER TABLE lams_ext_server_org_map ADD COLUMN lesson_finish_url text DEFAULT NULL;

CREATE TABLE lams_ext_server_lesson_map (
  uid BIGINT(20) NOT NULL auto_increment,
  lesson_id BIGINT(20) NOT NULL,
  ext_server_org_map_id int(11) NOT NULL,
  PRIMARY KEY  (uid),
  UNIQUE KEY `lesson_id` (`lesson_id`),
  CONSTRAINT lams_ext_server_lesson_map_fk1 FOREIGN KEY (ext_server_org_map_id) REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT lams_ext_server_lesson_map_fk2 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- LDEV-2642 and LDEV-2646 Move metadata to separate table. Use LD ID rather than ZIPed file for template.
CREATE TABLE lams_planner_activity_metadata (
       activity_id BIGINT(20) NOT NULL
	 , collapsed TINYINT(1) DEFAULT 0
	 , expanded TINYINT(1) DEFAULT 0
	 , hidden TINYINT(1) DEFAULT 0
	 , editing_advice VARCHAR(255)
     , CONSTRAINT FK_lams_planner_metadata_primary FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

ALTER TABLE lams_planner_nodes DROP COLUMN file_uuid, DROP COLUMN file_name, ADD COLUMN ld_id BIGINT(20), ADD COLUMN teachers_permissions TINYINT UNSIGNED;

-- all statements below should be moved to a next patch file?
ALTER TABLE lams_planner_nodes CHANGE COLUMN teachers_permissions permissions INTEGER, ADD COLUMN user_id BIGINT(20), ADD CONSTRAINT FK_lams_planner_node_user FOREIGN KEY (user_id) REFERENCES lams_user(user_id) ON DELETE SET NULL ON UPDATE SET NULL;

-- LDEV-2664 Switch learner interface for learners if they use a mobile device
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ForceMobileDevToUseFlashless','true', 'config.force.mobile.use.flashlesh', 'config.header.features', 'BOOLEAN', 1);

-- LDEV-2704 Weaken foreign key's constraints due to problems with learning designs removal
ALTER TABLE lams_log_event DROP FOREIGN KEY FK_lams_event_log_3;
ALTER TABLE lams_log_event ADD CONSTRAINT FK_lams_event_log_3 FOREIGN KEY (learning_design_id) REFERENCES lams_learning_design (learning_design_id ) ON DELETE CASCADE;

-- WVI-14 adding Lesson time limits ability
ALTER TABLE lams_lesson ADD COLUMN scheduled_number_days_to_lesson_finish INT(3) DEFAULT NULL;
ALTER TABLE lams_user_group ADD COLUMN scheduled_lesson_end_date DATETIME;

-- WVI-15 Email Notifications
ALTER TABLE lams_lesson ADD COLUMN enable_lesson_notifications TINYINT(1) DEFAULT 0;
ALTER TABLE lams_organisation ADD COLUMN enable_course_notifications TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-2756 Introducing Kaltura CKEditor plugin
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaServer','http://www.kaltura.com', 'config.kaltura.server', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaPartnerId','1073272', 'config.kaltura.partner.id', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaSubPartnerId','107327200', 'config.kaltura.sub.partner.id', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaUserSecret','f6b13f7e128e081b5cc9bb9664fd8717', 'config.kaltura.user.secret', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaKCWUiConfId','1000741', 'config.kaltura.kcw.uiconfid', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaKDPUiConfId','6308762', 'config.kaltura.kdp.uiconfid', 'config.header.kaltura', 'STRING', 0);

-- LDEV-2560 Add original author information
ALTER TABLE lams_learning_design ADD COLUMN original_user_id BIGINT(20) AFTER user_id;

-- LDEV-2747 An API to create users in bulk from an external system
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('EnableServerRegistration', 'false', 'config.server2server.registration.enable', 'config.header.system', 'BOOLEAN', 1);

-- LDEV-2782 Conditional release for lessons
CREATE TABLE lams_lesson_dependency (
      lesson_id BIGINT(20)
   ,  preceding_lesson_id BIGINT(20)
   , CONSTRAINT FK_lams_lesson_dependency_1 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , CONSTRAINT FK_lams_lesson_dependency_2 FOREIGN KEY (preceding_lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (lesson_id,preceding_lesson_id)
)ENGINE=InnoDB;

ALTER TABLE lams_lesson DROP COLUMN release_date;


-- LDEV-1616
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ProfileEditEnable','true', 'config.profile.edit.enable', 'config.header.features', 'BOOLEAN', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ProfilePartialEditEnable','true', 'config.profile.partial.edit.enable', 'config.header.features', 'BOOLEAN', 1); 

-- LDEV-1356
alter table lams_user add column modified_date datetime;


-- SIF-4 Adding an openid_url for each user
ALTER TABLE lams_user ADD COLUMN openid_url VARCHAR(255) UNIQUE;
ALTER TABLE lams_user ADD INDEX idx_openid_url (openid_url);

CREATE TABLE lams_openid_config (
     config_key VARCHAR(20) NOT NULL
   , config_value VARCHAR(255) NOT NULL
   , PRIMARY KEY (config_key)
)ENGINE=InnoDB;

INSERT INTO lams_openid_config(config_key, config_value) values ("enabled", "false");
INSERT INTO lams_openid_config(config_key, config_value) values ("portalURL", "");
INSERT INTO lams_openid_config(config_key, config_value) values ("trustedIDPs", "");



-- SIF-10 Addding a release date to the lesson table so we can 
-- track release events
alter table lams_lesson add column release_date DATETIME;
alter table lams_lesson add index idx_release_date (release_date);

-- LDEV-2550

DROP TABLE IF EXISTS lams_log_event;
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

DELETE FROM lams_log_event_type;
INSERT INTO lams_log_event_type VALUES (1, 'TYPE_TEACHER_LEARNING_DESIGN_CREATE');
INSERT INTO lams_log_event_type VALUES (2, 'TYPE_TEACHER_LESSON_CREATE');
INSERT INTO lams_log_event_type VALUES (3, 'TYPE_TEACHER_LESSON_START');
INSERT INTO lams_log_event_type VALUES (4, 'TYPE_TEACHER_LESSON_CHANGE_STATE');
INSERT INTO lams_log_event_type VALUES (5, 'TYPE_LEARNER_ACTIVITY_START');
INSERT INTO lams_log_event_type VALUES (6, 'TYPE_LEARNER_ACTIVITY_FINISH');

-- LDEV-2639 Make print button in LAMS learner optional 
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)  
VALUES ('DisplayPrintButton','false', 'config.display.print.button', 'config.header.features', 'BOOLEAN', 1);

-- LDEV-2628 Report instant completion
ALTER TABLE lams_ext_server_org_map ADD COLUMN lesson_finish_url text DEFAULT NULL;

CREATE TABLE lams_ext_server_lesson_map (
  uid BIGINT(20) NOT NULL auto_increment,
  lesson_id BIGINT(20) NOT NULL,
  ext_server_org_map_id int(11) NOT NULL,
  PRIMARY KEY  (uid),
  UNIQUE KEY `lesson_id` (`lesson_id`),
  CONSTRAINT lams_ext_server_lesson_map_fk1 FOREIGN KEY (ext_server_org_map_id) REFERENCES lams_ext_server_org_map (sid) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT lams_ext_server_lesson_map_fk2 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- LDEV-2642 and LDEV-2646 Move metadata to separate table. Use LD ID rather than ZIPed file for template.
CREATE TABLE lams_planner_activity_metadata (
       activity_id BIGINT(20) NOT NULL
	 , collapsed TINYINT(1) DEFAULT 0
	 , expanded TINYINT(1) DEFAULT 0
	 , hidden TINYINT(1) DEFAULT 0
	 , editing_advice VARCHAR(255)
     , CONSTRAINT FK_lams_planner_metadata_primary FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

ALTER TABLE lams_planner_nodes DROP COLUMN file_uuid, DROP COLUMN file_name, ADD COLUMN ld_id BIGINT(20), ADD COLUMN teachers_permissions TINYINT UNSIGNED;

-- all statements below should be moved to a next patch file?
ALTER TABLE lams_planner_nodes CHANGE COLUMN teachers_permissions permissions INTEGER, ADD COLUMN user_id BIGINT(20), ADD CONSTRAINT FK_lams_planner_node_user FOREIGN KEY (user_id) REFERENCES lams_user(user_id) ON DELETE SET NULL ON UPDATE SET NULL;

-- LDEV-2664 Switch learner interface for learners if they use a mobile device
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ForceMobileDevToUseFlashless','true', 'config.force.mobile.use.flashlesh', 'config.header.features', 'BOOLEAN', 1);

-- LDEV-2704 Weaken foreign key's constraints due to problems with learning designs removal
ALTER TABLE lams_log_event DROP FOREIGN KEY FK_lams_event_log_3;
ALTER TABLE lams_log_event ADD CONSTRAINT FK_lams_event_log_3 FOREIGN KEY (learning_design_id) REFERENCES lams_learning_design (learning_design_id ) ON DELETE CASCADE;

-- WVI-14 adding Lesson time limits ability
ALTER TABLE lams_lesson ADD COLUMN scheduled_number_days_to_lesson_finish INT(3) DEFAULT NULL;
ALTER TABLE lams_user_group ADD COLUMN scheduled_lesson_end_date DATETIME;

-- WVI-15 Email Notifications
ALTER TABLE lams_lesson ADD COLUMN enable_lesson_notifications TINYINT(1) DEFAULT 0;
ALTER TABLE lams_organisation ADD COLUMN enable_course_notifications TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-2756 Introducing Kaltura CKEditor plugin
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaServer','http://www.kaltura.com', 'config.kaltura.server', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaPartnerId','1073272', 'config.kaltura.partner.id', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaSubPartnerId','107327200', 'config.kaltura.sub.partner.id', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaUserSecret','f6b13f7e128e081b5cc9bb9664fd8717', 'config.kaltura.user.secret', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaKCWUiConfId','1000741', 'config.kaltura.kcw.uiconfid', 'config.header.kaltura', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('KalturaKDPUiConfId','6308762', 'config.kaltura.kdp.uiconfid', 'config.header.kaltura', 'STRING', 0);

-- LDEV-2560 Add original author information
ALTER TABLE lams_learning_design ADD COLUMN original_user_id BIGINT(20) AFTER user_id;

-- LDEV-2747 An API to create users in bulk from an external system
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('EnableServerRegistration', 'false', 'config.server2server.registration.enable', 'config.header.system', 'BOOLEAN', 1);

-- LDEV-2782 Conditional release for lessons
CREATE TABLE lams_lesson_dependency (
      lesson_id BIGINT(20)
   ,  preceding_lesson_id BIGINT(20)
   , CONSTRAINT FK_lams_lesson_dependency_1 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , CONSTRAINT FK_lams_lesson_dependency_2 FOREIGN KEY (preceding_lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (lesson_id,preceding_lesson_id)
)ENGINE=InnoDB;

ALTER TABLE lams_lesson DROP COLUMN release_date;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
