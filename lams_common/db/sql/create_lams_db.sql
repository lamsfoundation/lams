-- MySQL Script
-- Modify your server name
-- Modify username and password for the lams db
-- Create lams database for LAMS
-- 13/DEC/2004

SET FOREIGN_KEY_CHECKS=0;
DROP DATABASE IF EXISTS lams;
CREATE DATABASE lams;
SET FOREIGN_KEY_CHECKS=1;

USE mysql;

DELETE FROM user WHERE user='';
-- DELETE FROM user WHERE user IS NULL;

DELETE FROM db WHERE user='';
-- DELETE FROM db WHERE user IS NULL;

GRANT ALL PRIVILEGES ON *.* TO lams@localhost IDENTIFIED BY 'lamsdemo';
REVOKE PROCESS,SUPER ON *.* from lams@localhost;

GRANT ALL PRIVILEGES ON *.* TO lams@developer02 IDENTIFIED BY 'lamsdemo';
REVOKE PROCESS,SUPER ON *.* from lams@developer02;

GRANT ALL PRIVILEGES ON *.* TO lams@'%' IDENTIFIED BY 'lamsdemo';
REVOKE PROCESS,SUPER ON *.* from lams@'%';

FLUSH PRIVILEGES;

GRANT ALL PRIVILEGES ON lams.* TO lams@localhost IDENTIFIED BY 'lamsdemo' WITH GRANT OPTION;
--The line below should be uncommented and 'developer02' should be replaced with the actual host name
--GRANT ALL PRIVILEGES ON lams.* TO lams@developer02 IDENTIFIED BY 'lamsdemo' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON lams.* TO lams@'%' IDENTIFIED BY 'lamsdemo' WITH GRANT OPTION;
	
FLUSH PRIVILEGES;

USE lams;

-- Drop, Create and Populate LAMS Tables

SET FOREIGN_KEY_CHECKS=0;

# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-12-14 16:57:02
# 

DROP TABLE IF EXISTS lams_gate_activity_level;
CREATE TABLE lams_gate_activity_level (
       gate_activity_level_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (gate_activity_level_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_grouping_type;
CREATE TABLE lams_grouping_type (
       grouping_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(128) NOT NULL
     , PRIMARY KEY (grouping_type_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_learning_activity_type;
CREATE TABLE lams_learning_activity_type (
       learning_activity_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (learning_activity_type_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_learning_library;
CREATE TABLE lams_learning_library (
       learning_library_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , description TEXT
     , title VARCHAR(255)
     , create_date_time DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , PRIMARY KEY (learning_library_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_organisation_type;
CREATE TABLE lams_organisation_type (
       organisation_type_id INT(3) NOT NULL DEFAULT 0
     , name VARCHAR(64) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (organisation_type_id)
)TYPE=InnoDB;
CREATE UNIQUE INDEX UQ_lams_organisation_type_name ON lams_organisation_type (name ASC);

DROP TABLE IF EXISTS lams_role;
CREATE TABLE lams_role (
       role_id INT(6) NOT NULL DEFAULT 0
     , name VARCHAR(64) NOT NULL
     , description TEXT
     , create_date BIGINT(20)
     , PRIMARY KEY (role_id)
)TYPE=InnoDB;
CREATE INDEX gname ON lams_role (name ASC);

DROP TABLE IF EXISTS lams_tool;
CREATE TABLE lams_tool (
       tool_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , tool_signature VARCHAR(64) NOT NULL
     , class_name VARCHAR(255) NOT NULL
     , tool_display_name VARCHAR(255) NOT NULL
     , description TEXT
     , default_tool_content_id BIGINT(20) NOT NULL
     , supports_grouping_flag TINYINT(1) NOT NULL DEFAULT 0
     , supports_define_later_flag TINYINT(1) NOT NULL DEFAULT 0
     , learner_url TEXT NOT NULL
     , author_url TEXT NOT NULL
     , define_later_url TEXT
     , export_portfolio_url TEXT NOT NULL
     , monitor_url TEXT NOT NULL
     , UNIQUE UQ_lams_tool_sig (tool_signature)
     , UNIQUE UQ_lams_tool_class_name (class_name)
     , PRIMARY KEY (tool_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_tool_session_state;
CREATE TABLE lams_tool_session_state (
       tool_session_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (tool_session_state_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_user_tool_session_state;
CREATE TABLE lams_user_tool_session_state (
       user_tool_session_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (user_tool_session_state_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_lesson_state;
CREATE TABLE lams_lesson_state (
       lams_lesson_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (lams_lesson_state_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_authentication_method_type;
CREATE TABLE lams_authentication_method_type (
       authentication_method_type_id INT(3) NOT NULL DEFAULT 0
     , description VARCHAR(64) NOT NULL
     , PRIMARY KEY (authentication_method_type_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_authentication_method;
CREATE TABLE lams_authentication_method (
       authentication_method_id BIGINT(20) NOT NULL DEFAULT 0
     , authentication_method_type_id INT(3) NOT NULL DEFAULT 0
     , parameters_file_name VARCHAR(255) NOT NULL
     , PRIMARY KEY (authentication_method_id)
     , INDEX (authentication_method_type_id)
     , CONSTRAINT FK_lams_authorization_method_1 FOREIGN KEY (authentication_method_type_id)
                  REFERENCES lams_authentication_method_type (authentication_method_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_workspace_folder;
CREATE TABLE lams_workspace_folder (
       workspace_folder_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , parent_folder_id BIGINT(20)
     , name VARCHAR(64) NOT NULL
     , workspace_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (workspace_folder_id)
     , INDEX (parent_folder_id)
     , CONSTRAINT FK_lams_workspace_folder_2 FOREIGN KEY (parent_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_workspace;
CREATE TABLE lams_workspace (
       workspace_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , root_folder_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (workspace_id)
     , INDEX (root_folder_id)
     , CONSTRAINT FK_lams_workspace_1 FOREIGN KEY (root_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_grouping;
CREATE TABLE lams_grouping (
       grouping_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , grouping_type_id INT(11) NOT NULL DEFAULT 0
     , number_of_groups INT(11)
     , learners_per_group INT(11)
     , staff_group_id BIGINT(20) DEFAULT 0
     , PRIMARY KEY (grouping_id)
     , INDEX (grouping_type_id)
     , CONSTRAINT FK_lams_learning_grouping_1 FOREIGN KEY (grouping_type_id)
                  REFERENCES lams_grouping_type (grouping_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_group;
CREATE TABLE lams_group (
       group_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , grouping_id BIGINT(20) NOT NULL DEFAULT 0
     , order_id INT(6) NOT NULL DEFAULT 1
     , PRIMARY KEY (group_id)
     , INDEX (grouping_id)
     , CONSTRAINT FK_lams_learning_group_1 FOREIGN KEY (grouping_id)
                  REFERENCES lams_grouping (grouping_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_organisation;
CREATE TABLE lams_organisation (
       organisation_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , name VARCHAR(250)
     , description VARCHAR(250)
     , parent_organisation_id BIGINT(20)
     , organisation_type_id INT(3) NOT NULL DEFAULT 0
     , create_date DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
     , workspace_id BIGINT(20)
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
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_user;
CREATE TABLE lams_user (
       user_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
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
     , user_organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , base_organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_id)
     , INDEX (authentication_method_id)
     , CONSTRAINT FK_lams_user_1 FOREIGN KEY (authentication_method_id)
                  REFERENCES lams_authentication_method (authentication_method_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (workspace_id)
     , CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_id)
                  REFERENCES lams_workspace (workspace_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (base_organisation_id)
     , CONSTRAINT FK_lams_user_3 FOREIGN KEY (base_organisation_id)
                  REFERENCES lams_organisation (organisation_id)
)TYPE=InnoDB;
CREATE UNIQUE INDEX UQ_lams_user_login ON lams_user (login ASC);
CREATE INDEX login ON lams_user (login ASC);

DROP TABLE IF EXISTS lams_learning_design;
CREATE TABLE lams_learning_design (
       learning_design_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
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
     , INDEX (parent_learning_design_id)
     , CONSTRAINT FK_lams_learning_design_2 FOREIGN KEY (parent_learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (user_id)
     , CONSTRAINT FK_lams_learning_design_3 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
)TYPE=InnoDB;
CREATE INDEX idx_design_first_act ON lams_learning_design (first_activity_id ASC);

DROP TABLE IF EXISTS lams_learning_activity;
CREATE TABLE lams_learning_activity (
       activity_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
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
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_user_organisation;
CREATE TABLE lams_user_organisation (
       user_organisation_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_organisation_id)
     , INDEX (user_id)
     , CONSTRAINT u_user_organisation_ibfk_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (organisation_id)
     , CONSTRAINT u_user_organisation_ibfk_2 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_lesson;
CREATE TABLE lams_lesson (
       lesson_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , learning_design_id BIGINT(20) NOT NULL DEFAULT 0
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , create_date_time DATETIME NOT NULL
     , organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , lams_lesson_state_id INT(3) NOT NULL
     , start_date_time DATETIME
     , end_date_time DATETIME
     , class_grouping_id BIGINT(20) NOT NULL DEFAULT 0
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
     , INDEX (lams_lesson_state_id)
     , CONSTRAINT FK_lams_lesson_4 FOREIGN KEY (lams_lesson_state_id)
                  REFERENCES lams_lesson_state (lams_lesson_state_id)
     , INDEX (class_grouping_id)
     , CONSTRAINT FK_lams_lesson_5 FOREIGN KEY (class_grouping_id)
                  REFERENCES lams_grouping (grouping_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_learner_progress;
CREATE TABLE lams_learner_progress (
       learner_progress_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , lesson_id BIGINT(20) NOT NULL
     , lesson_completed_flag TINYINT(1) NOT NULL DEFAULT 0
     , start_date_time DATETIME NOT NULL
     , finish_date_time DATETIME
     , PRIMARY KEY (learner_progress_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_learner_progress_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (lesson_id)
     , CONSTRAINT FK_lams_learner_progress_2 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_tool_session;
CREATE TABLE lams_tool_session (
       tool_session_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , group_id BIGINT(20) DEFAULT 0
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
     , tool_session_key BIGINT(20) NOT NULL
     , tool_session_state_id INT(3) NOT NULL
     , create_date_time DATETIME NOT NULL
     , PRIMARY KEY (tool_session_id)
     , INDEX (group_id)
     , CONSTRAINT FK_lams_tool_session_1 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
     , INDEX (tool_session_state_id)
     , CONSTRAINT FK_lams_tool_session_4 FOREIGN KEY (tool_session_state_id)
                  REFERENCES lams_tool_session_state (tool_session_state_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_user_organisation_role;
CREATE TABLE lams_user_organisation_role (
       user_organisation_role_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , user_organisation_id BIGINT(20) NOT NULL DEFAULT 0
     , role_id INT(6) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_organisation_role_id)
     , INDEX (role_id)
     , CONSTRAINT FK_lams_user_organisation_role_2 FOREIGN KEY (role_id)
                  REFERENCES lams_role (role_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (user_organisation_id)
     , CONSTRAINT FK_lams_user_organisation_role_3 FOREIGN KEY (user_organisation_id)
                  REFERENCES lams_user_organisation (user_organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_progress_completed;
CREATE TABLE lams_progress_completed (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (learner_progress_id, activity_id)
     , INDEX (learner_progress_id)
     , CONSTRAINT FK_lams_progress_completed_1 FOREIGN KEY (learner_progress_id)
                  REFERENCES lams_learner_progress (learner_progress_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_progress_completed_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_progress_current;
CREATE TABLE lams_progress_current (
       learner_progress_id BIGINT(20) NOT NULL
     , activity_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (learner_progress_id, activity_id)
     , INDEX (learner_progress_id)
     , CONSTRAINT FK_lams_progress_current_1 FOREIGN KEY (learner_progress_id)
                  REFERENCES lams_learner_progress (learner_progress_id)
     , INDEX (activity_id)
     , CONSTRAINT FK_lams_progress_current_2 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_user_tool_session;
CREATE TABLE lams_user_tool_session (
       tool_session_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL DEFAULT 0
     , user_tool_session_state_id INT(3) NOT NULL
     , PRIMARY KEY (tool_session_id, user_id)
     , INDEX (tool_session_id)
     , CONSTRAINT FK_lams_user_tool_session_1 FOREIGN KEY (tool_session_id)
                  REFERENCES lams_tool_session (tool_session_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_user_tool_session_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (user_tool_session_state_id)
     , CONSTRAINT FK_lams_user_tool_session_3 FOREIGN KEY (user_tool_session_state_id)
                  REFERENCES lams_user_tool_session_state (user_tool_session_state_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_user_group;
CREATE TABLE lams_user_group (
       user_id BIGINT(20) NOT NULL DEFAULT 0
     , group_id BIGINT(20) NOT NULL DEFAULT 0
     , PRIMARY KEY (user_id, group_id)
     , INDEX (user_id)
     , CONSTRAINT FK_lams_user_group_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
     , INDEX (group_id)
     , CONSTRAINT FK_lams_user_group_2 FOREIGN KEY (group_id)
                  REFERENCES lams_group (group_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_tool_content;
CREATE TABLE lams_tool_content (
       tool_content_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , tool_id BIGINT(20) NOT NULL
     , PRIMARY KEY (tool_content_id)
     , INDEX (tool_id)
     , CONSTRAINT FK_lams_tool_content_1 FOREIGN KEY (tool_id)
                  REFERENCES lams_tool (tool_id)
)TYPE=InnoDB;

DROP TABLE IF EXISTS lams_learning_transition;
CREATE TABLE lams_learning_transition (
       transition_id BIGINT(20) NOT NULL DEFAULT 0 AUTO_INCREMENT
     , id INT(11)
     , description TEXT
     , title VARCHAR(255)
     , to_activity_id BIGINT(20) NOT NULL DEFAULT 0
     , from_activity_id BIGINT(20) NOT NULL DEFAULT 0
     , learning_design_id BIGINT(20) NOT NULL DEFAULT 0
     , create_date_time DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00'
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
)TYPE=InnoDB;


INSERT INTO lams_role VALUES (1, 'SYSADMIN', 'LAMS System Adminstrator', NOW());
INSERT INTO lams_role VALUES (2, 'ADMIN', 'Organization Adminstrator', NOW());
INSERT INTO lams_role VALUES (3, 'AUTHOR', 'Authors Learning Designs', NOW());
INSERT INTO lams_role VALUES (4, 'STAFF', 'Member of Staff', NOW());
INSERT INTO lams_role VALUES (5, 'LEARNER', 'Student', NOW());

INSERT INTO lams_authentication_method_type VALUES(1, 'LAMS');
INSERT INTO lams_authentication_method_type VALUES(2, 'WEB_AUTH');
INSERT INTO lams_authentication_method_type VALUES(3, 'LDAP');

INSERT INTO lams_organisation_type VALUES(1, 'ROOT ORGANISATION', 'root all other organisations: controlled by Sysadmin');
INSERT INTO lams_organisation_type VALUES(2, 'BASE ORGANISATION', 'base organization: represents a real seperate organization sucha s a university ');
INSERT INTO lams_organisation_type VALUES(3, 'SUB-ORGANIZATION', 'sub organization of a base organization');

INSERT INTO lams_grouping_type VALUES (1, 'NORMAL');
INSERT INTO lams_grouping_type VALUES (2, 'CLASS');

INSERT INTO lams_learning_activity_type VALUES (1, 'TOOL');
INSERT INTO lams_learning_activity_type VALUES (2, 'GROUPING_RANDOM');
INSERT INTO lams_learning_activity_type VALUES (3, 'GROUPING_CHOSEN');
INSERT INTO lams_learning_activity_type VALUES (4, 'GATE_SYNCH');
INSERT INTO lams_learning_activity_type VALUES (5, 'GATE_SCHEDULE');
INSERT INTO lams_learning_activity_type VALUES (6, 'GATE_PERMISSION');
INSERT INTO lams_learning_activity_type VALUES (7, 'PARALLEL');
INSERT INTO lams_learning_activity_type VALUES (8, 'OPTIONS');
INSERT INTO lams_learning_activity_type VALUES (9, 'SEQUENCE');
INSERT INTO lams_learning_activity_type VALUES (10, 'BRANCH_GROUP_CONTENT');

INSERT INTO lams_gate_activity_level VALUES (1, 'LEARNER');
INSERT INTO lams_gate_activity_level VALUES (2, 'GROUP');
INSERT INTO lams_gate_activity_level VALUES (3, 'CLASS');

INSERT INTO lams_tool_session_state VALUES  (1, 'STARTED');
INSERT INTO lams_tool_session_state VALUES  (2, 'FINISHED');
INSERT INTO lams_tool_session_state VALUES  (3, 'SUSPENDED');
INSERT INTO lams_tool_session_state VALUES  (4, 'RESUMED');
INSERT INTO lams_tool_session_state VALUES  (5, 'ERROR');

INSERT INTO lams_user_tool_session_state VALUES (1, 'JOINED');
INSERT INTO lams_user_tool_session_state VALUES (2, 'LEFT' );
INSERT INTO lams_user_tool_session_state VALUES (3, 'SUSPENDED');
INSERT INTO lams_user_tool_session_state VALUES (4, 'RESUMED');
INSERT INTO lams_user_tool_session_state VALUES (5, 'ERROR');

SET FOREIGN_KEY_CHECKS=1;