-- SQL statements to update from LAMS 2.2

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LI-192 This script needs to be in updaters - before new tools are deployed
--ALTER TABLE lams_tool ADD COLUMN pedagogical_planner_url TEXT;

--  LDEV-1998 ------------- 
ALTER TABLE lams_system_tool ADD COLUMN pedagogical_planner_url TEXT;
UPDATE lams_system_tool SET pedagogical_planner_url='pedagogicalPlanner/initGrouping.do' WHERE system_tool_id=1;
CREATE TABLE lams_planner_nodes (
	uid BIGINT(20) NOT NULL auto_increment,
	parent_uid BIGINT(20),
	order_id TINYINT UNSIGNED NOT NULL,
	locked TINYINT(1) NOT NULL DEFAULT 0,
	content_folder_id VARCHAR(32),
	title VARCHAR(255) NOT NULL,
	brief_desc TEXT,
	full_desc TEXT,
	file_uuid  BIGINT(20) UNSIGNED,
	file_name VARCHAR(255),
	PRIMARY KEY (uid),
	UNIQUE KEY (parent_uid, order_id),
	CONSTRAINT FK_lams_planner_node_parent FOREIGN KEY (parent_uid)
	               REFERENCES lams_planner_nodes(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;
-- LDEV-2074 --------------
ALTER TABLE lams_learning_design ADD COLUMN floating_activity_id BIGINT(20);
CREATE INDEX idx_design_floating_act ON lams_learning_design (floating_activity_id ASC);
INSERT INTO lams_learning_activity_type VALUES (15, 'FLOATING');

-- LDEV-1983 --------------
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPUser','', 'config.smtp.user', 'config.header.email', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SMTPPassword','', 'config.smtp.password', 'config.header.email', 'STRING', 0);

-- LDEV-2099 --------------
ALTER TABLE lams_user ADD COLUMN lams_community_token VARCHAR(255);
ALTER TABLE lams_user ADD COLUMN lams_community_username VARCHAR(255);

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

-- LDEV-2107 --------------
INSERT into lams_workspace_folder_type VALUES (3, 'PUBLIC SEQUENCES');

-- LDEV-2115 --------------
ALTER TABLE lams_progress_completed ADD COLUMN completed_date_time DATETIME;

-- LDEV-2173 -------------- Adding in start dates for activities for gradebook
ALTER TABLE lams_progress_attempted ADD COLUMN start_date_time DATETIME;
ALTER TABLE lams_progress_completed ADD COLUMN start_date_time DATETIME;

-- LDEV-2163 --------------
CREATE TABLE lams_activity_evaluation (
	  activity_evaluation_id BIGINT(20) NOT NULL auto_increment
	, activity_id	BIGINT(20) NOT NULL
	, tool_output_definition VARCHAR(255) NOT NULL
	, INDEX (activity_id)
	, CONSTRAINT FK_lams_activity_evaluation_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (activity_evaluation_id)
)ENGINE=InnoDB;

-- LDEV-2174 -------------
ALTER TABLE lams_tool DROP COLUMN classpath_addition;
ALTER TABLE lams_tool DROP COLUMN context_file;

-- LDEV-2205 ------------ Adding couse level settings for gradebook
ALTER TABLE lams_organisation ADD COLUMN enable_monitor_gradebook TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE lams_organisation ADD COLUMN enable_learner_gradebook TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-2173 ------------ Gradebook stuff
CREATE TABLE lams_gradebook_user_activity (
	  uid BIGINT(20) NOT NULL auto_increment
	, activity_id BIGINT(20) NOT NULL
	, user_id BIGINT (20) NOT NULL
	, mark DOUBLE PRECISION 
    , marked_in_gradebook TINYINT(1) NOT NULL DEFAULT 0
	, feedback TEXT
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

-- LDEV-2207 ------------ Adding flag in lesson to release marks for gradebook
ALTER TABLE lams_lesson ADD COLUMN marks_released TINYINT DEFAULT 0;

-- LDEV-2165
ALTER TABLE lams_events modify COLUMN name VARCHAR(128) NOT NULL;
ALTER TABLE lams_events modify COLUMN scope VARCHAR(128) NOT NULL;

--  LDEV-2125 ------------- 
ALTER TABLE lams_user ADD COLUMN timezone TINYINT;

-- LDEV-2054
ALTER TABLE lams_grouping ADD COLUMN view_students_before_selection TINYINT DEFAULT 0;

-- LDEV-2197 ------------ Presence Chat Logging
CREATE TABLE lams_presence_chat_msgs (
	uid bigint NOT NULL auto_increment,
	room_name VARCHAR(255),
	from_user VARCHAR(255),
	to_user VARCHAR(255),
	date_sent DATETIME,
	message VARCHAR(1023),
	PRIMARY KEY (uid)
)ENGINE=InnoDB;

-- LDEV-2005 Video Recorder configuration stuff --------------
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('Red5ServerUrl','', 'config.red5.server.url', 'config.header.red5', 'STRING', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('Red5RecordingsUrl','', 'config.red5.recordings.url', 'config.header.red5', 'STRING', 0);

-- disable videorecorder
update lams_learning_library set valid_flag=0 where title="VideoRecorder";

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;