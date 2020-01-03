SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170209.sql to patch20181214.sql
-- It should upgrade LAMS to version 3.1


-- LDEV-4217 Remove LAMS Community integration
DROP TABLE IF EXISTS `lams_registration`;
ALTER TABLE `lams_user` DROP COLUMN `lams_community_username`, DROP COLUMN `lams_community_token`;
DELETE FROM `lams_configuration` WHERE `config_key`='LAMS_Community_enable';




-- LDEV-4261 Remove optimistic locking management
ALTER TABLE lams_lesson DROP COLUMN version;




-- LDEV-4255, LDEV-4256 Removing UI options no longer used
DELETE FROM `lams_configuration` WHERE `config_key`='LearnerCollapsProgressPanel';
DELETE FROM `lams_configuration` WHERE `config_key`='AuthoringActivitiesColour';
DELETE FROM `lams_configuration` WHERE `config_key`='LearnerProgressBatchSize';




-- LDEV-4124 Add size to labels
ALTER TABLE lams_learning_design_annotation ADD COLUMN size TINYINT;




-- LDEV-4288 Create tables to store archived gradebook entries 

CREATE TABLE lams_gradebook_user_lesson_archive (
  uid bigint(20) NOT NULL,
  lesson_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  mark double,
  feedback text,
  PRIMARY KEY (uid),
  KEY lesson_id (lesson_id, user_id),
  CONSTRAINT FK_lams_gradebook_user_lesson_archive_1 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_gradebook_user_lesson_archive_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_gradebook_user_activity_archive (
  uid bigint(20) NOT NULL,
  activity_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  mark double,
  feedback text,
  marked_in_gradebook tinyint(1) NOT NULL DEFAULT 0,
  update_date datetime,
  PRIMARY KEY (uid),
  KEY activity_id (activity_id, user_id),
  CONSTRAINT FK_lams_gradebook_user_activity_archive_1 FOREIGN KEY (activity_id) REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT FK_lams_gradebook_user_activity_archive_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);




-- LDEV-4184 Allow longer ID format

ALTER TABLE lams_learning_design MODIFY COLUMN content_folder_id CHAR(36);




-- LDEV-4312 Handle unsuccessful mark submission to integrated servers

INSERT INTO lams_log_event_type VALUES (7, 'TYPE_LEARNER_LESSON_COMPLETE');
INSERT INTO lams_log_event_type VALUES (8, 'TYPE_LEARNER_LESSON_MARK_SUBMIT');




-- LDEV-4334 Add a new Gradebook tab to Monitor

ALTER TABLE lams_organisation DROP COLUMN enable_monitor_gradebook;




--     LDEV-2999 Include a rating option for viewed resources
ALTER TABLE lams_rating ADD COLUMN tool_session_id BIGINT(20) DEFAULT NULL,
	ADD CONSTRAINT FK_lams_rating_3  FOREIGN KEY (tool_session_id)
  	REFERENCES lams_tool_session (tool_session_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
  
ALTER TABLE lams_rating_comment ADD COLUMN tool_session_id BIGINT(20) DEFAULT NULL,
	ADD CONSTRAINT FK_lams_rating_comment_3  FOREIGN KEY (tool_session_id)
  	REFERENCES lams_tool_session (tool_session_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Update existing Image Gallery Ratings
DROP TABLE IF EXISTS tmp_imagegallery_rating_sessionid;
CREATE TEMPORARY TABLE tmp_imagegallery_rating_sessionid AS
SELECT r.uid rating_uid, igs.session_id session_id
FROM lams_rating r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laimag10_imagegallery ig ON rc.tool_content_id = ig.content_id
JOIN tl_laimag10_session igs ON ig.uid = igs.imageGallery_uid 
JOIN tl_laimag10_user igu ON r.user_id = igu.user_id AND igs.uid = igu.session_uid;

UPDATE lams_rating r
JOIN tmp_imagegallery_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_imagegallery_rating_sessionid;
CREATE TEMPORARY TABLE tmp_imagegallery_rating_sessionid AS
SELECT r.uid rating_uid, igs.session_id session_id
FROM lams_rating_comment r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laimag10_imagegallery ig ON rc.tool_content_id = ig.content_id
JOIN tl_laimag10_session igs ON ig.uid = igs.imageGallery_uid 
JOIN tl_laimag10_user igu ON r.user_id = igu.user_id AND igs.uid = igu.session_uid;

UPDATE lams_rating_comment r
JOIN tmp_imagegallery_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

-- Update existing Peer Review done by the Peer Review tool

-- Update existing QA
DROP TABLE IF EXISTS tmp_qa_rating_sessionid;
CREATE TEMPORARY TABLE tmp_qa_rating_sessionid AS
SELECT r.uid rating_uid, qas.qa_session_id session_id
FROM lams_rating r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laqa11_content qa ON qa.qa_content_id = rc.tool_content_id
JOIN tl_laqa11_session qas ON qas.qa_content_id = qa.uid
JOIN tl_laqa11_que_usr qau ON r.user_id = qau.que_usr_id AND qas.uid = qau.qa_session_id;

UPDATE lams_rating r
JOIN tmp_qa_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_qa_rating_sessionid;
CREATE TEMPORARY TABLE tmp_qa_rating_sessionid AS
SELECT r.uid rating_uid, qas.qa_session_id session_id
FROM lams_rating_comment r
JOIN lams_rating_criteria rc ON r.rating_criteria_id = rc.rating_criteria_id
JOIN tl_laqa11_content qa ON qa.qa_content_id = rc.tool_content_id
JOIN tl_laqa11_session qas ON qas.qa_content_id = qa.uid
JOIN tl_laqa11_que_usr qau ON r.user_id = qau.que_usr_id AND qas.uid = qau.qa_session_id;

UPDATE lams_rating_comment r
JOIN tmp_qa_rating_sessionid tmp ON r.uid = tmp.rating_uid
SET r.tool_session_id = tmp.session_id;

DROP TABLE tmp_qa_rating_sessionid;




-- LDEV-4184, LDEV-4368 Allow longer ID format for password recovery request key

ALTER TABLE lams_password_request MODIFY COLUMN request_key CHAR(36);




-- LDEV-4366 Add tables for Kumalive

CREATE TABLE lams_kumalive (
	   kumalive_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , created_by BIGINT(20)
     , finished TINYINT(1) NOT NULL DEFAULT 0
     , name VARCHAR(250)
     , PRIMARY KEY (kumalive_id)
     , CONSTRAINT FK_lams_kumalive_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
  	 , CONSTRAINT FK_lams_kumalive_2 FOREIGN KEY (created_by)
                  REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_rubric (
	   rubric_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , kumalive_id BIGINT(20)
     , order_id TINYINT NOT NULL
     , name VARCHAR(250)
     , PRIMARY KEY (rubric_id)
     , CONSTRAINT FK_lams_kumalive_rubric_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
  	 , CONSTRAINT FK_lams_kumalive_rubric_2 FOREIGN KEY (kumalive_id)
                  REFERENCES lams_kumalive (kumalive_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_score (
	   score_id BIGINT(20) NOT NULL AUTO_INCREMENT
	 , rubric_id BIGINT(20) NOT NULL
     , user_id BIGINT(20)
     , batch BIGINT(10)
     , score TINYINT
     , PRIMARY KEY (score_id)
     , CONSTRAINT FK_lams_kumalive_score_1 FOREIGN KEY (rubric_id)
                  REFERENCES lams_kumalive_rubric (rubric_id) ON DELETE CASCADE ON UPDATE CASCADE
  	 , CONSTRAINT FK_lams_kumalive_score_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);




-- LDEV-4390 Create purple theme

INSERT INTO `lams_theme` (`name`, `description`, `image_directory`) VALUES ('purple', 'Purple Theme', 'css');




-- LDEV-4401 Remove deprecated parameters for integrations
ALTER TABLE lams_ext_server_org_map DROP COLUMN timeout_url;
ALTER TABLE lams_ext_server_org_map DROP COLUMN server_url;




-- Add column for weighted outputs

ALTER TABLE lams_activity_evaluation ADD COLUMN weight TINYINT DEFAULT NULL,
									 DROP PRIMARY KEY,
									 ADD PRIMARY KEY (activity_id),
									 DROP COLUMN activity_evaluation_id;

DELETE FROM lams_configuration WHERE config_key LIKE '%ClientVersion';

UPDATE lams_configuration SET config_value = '3.0.1' WHERE config_key = 'ServerVersionNumber';




-- LDEV-4428 Start supporting cy and id languages in CKEditor
UPDATE lams_supported_locale SET fckeditor_code='cy' WHERE locale_id='15';
UPDATE lams_supported_locale SET fckeditor_code='id' WHERE locale_id='31';




-- LDEV-4447 Add "Allow Kumalive" to system settings and organisation
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('AllowKumalive','true', 'config.allow.kumalive', 'config.header.features', 'BOOLEAN', 1);

ALTER TABLE lams_organisation ADD COLUMN enable_kumalive TINYINT(1) NOT NULL DEFAULT 0 AFTER enable_live_edit;




-- LDEV-4453 Add "gradebook on lesson complete" feature
ALTER TABLE lams_lesson ADD COLUMN gradebook_on_complete TINYINT(1) DEFAULT '0';
		



-- LDEV-4459 Add email notification archive
CREATE TABLE lams_email_notification_archive (
	uid BIGINT(20) NOT NULL AUTO_INCREMENT,
	organisation_id BIGINT(20),
	lesson_id BIGINT(20),
	search_type TINYINT NOT NULL,
	sent_on DATETIME NOT NULL,
	body TEXT,
	PRIMARY KEY (uid),
	CONSTRAINT FK_lams_email_notification_archive_1 FOREIGN KEY (organisation_id)
    	REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_lams_email_notification_archive_2 FOREIGN KEY (lesson_id)
    	REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_email_notification_recipient_archive (
	email_notification_uid BIGINT(20) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	CONSTRAINT FK_lams_email_notification_recipient_archive_1 FOREIGN KEY (email_notification_uid)
    	REFERENCES lams_email_notification_archive (uid) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_lams_email_notification_recipient_archive_2 FOREIGN KEY (user_id)
    	REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);
		



-- LDEV-4436 Ability to change portrait in learner
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('DisplayPortrait','true', 'config.display.portrait', 'config.header.look.feel', 'BOOLEAN', 0);




-- LDEV-4454 Add tables for Kumalive polls

CREATE TABLE lams_kumalive_poll (
	   poll_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , kumalive_id BIGINT(20) NOT NULL
     , name VARCHAR(250)
     , votes_released TINYINT(1) DEFAULT 0
     , voters_released TINYINT(1) DEFAULT 0
     , start_date DATETIME NOT NULL
     , finish_date DATETIME
     , PRIMARY KEY (poll_id)
     , CONSTRAINT FK_lams_kumalive_poll_1 FOREIGN KEY (kumalive_id)
                  REFERENCES lams_kumalive (kumalive_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_poll_answer (
	   answer_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , poll_id BIGINT(20) NOT NULL
     , order_id TINYINT NOT NULL
     , name VARCHAR(250)
     , PRIMARY KEY (answer_id)
     , CONSTRAINT FK_lams_kumalive_poll_answer_1 FOREIGN KEY (poll_id)
                  REFERENCES lams_kumalive_poll (poll_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE lams_kumalive_poll_vote (
	   answer_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , vote_date DATETIME
     , PRIMARY KEY (answer_id, user_id)
     , CONSTRAINT FK_lams_kumalive_poll_vote_1 FOREIGN KEY (answer_id)
                  REFERENCES lams_kumalive_poll_answer (answer_id) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_lams_kumalive_poll_vote_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);




-- LDEV-4485 Remove ratings when whole lesson is removed
ALTER TABLE lams_rating DROP FOREIGN KEY FK_lams_rating_3;
ALTER TABLE lams_rating ADD CONSTRAINT FK_lams_rating_3 FOREIGN KEY (tool_session_id) REFERENCES lams_tool_session (tool_session_id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE lams_rating_comment DROP FOREIGN KEY FK_lams_rating_comment_3;
ALTER TABLE lams_rating_comment ADD CONSTRAINT FK_lams_rating_comment_3 FOREIGN KEY (tool_session_id) REFERENCES lams_tool_session (tool_session_id) ON DELETE CASCADE ON UPDATE CASCADE;		



N_KEY_CHECKS = 0;
 
--  LDEV-4421 Need an item column to support Share Resources comments
-- No point having the unique index comment_ext_sig_user any longer
-- as it would need to include external_secondary_id which could be null
-- and two null values are always treated as distinct. Keeping an index on 
-- signature and main id for lookup efficiency.
ALTER TABLE lams_comment_session
ADD COLUMN external_secondary_id BIGINT(20);

ALTER TABLE lams_comment_session
DROP INDEX comment_ext_sig_user; 

ALTER TABLE lams_comment_session
ADD INDEX comment_ext_sig (external_id ASC, external_signature ASC);




--  LDEV-4501 Revise audit log
ALTER TABLE lams_log_event_type
ADD COLUMN area VARCHAR(255) NULL;

UPDATE lams_log_event_type
SET area = "LESSON";

INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(9, 'TYPE_ACTIVITY_EDIT', 'LESSON');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(10, 'TYPE_FORCE_COMPLETE', 'LESSON');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(11, 'TYPE_USER_ORG_ADMIN', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(12, 'TYPE_LOGIN_AS', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(13, 'TYPE_PASSWORD_CHANGE', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(14, 'TYPE_ROLE_FAILURE', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(15, 'TYPE_ACCOUNT_LOCKED', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(16, 'TYPE_NOTIFICATION', 'NOTIFICATION');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(17, 'TYPE_MARK_UPDATED', 'MARKS');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(18, 'TYPE_MARK_RELEASED', 'MARKS');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(19, 'TYPE_LEARNER_CONTENT_UPDATED', 'LEARNER_CONTENT');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(20, 'TYPE_LEARNER_CONTENT_SHOW_HIDE', 'LEARNER_CONTENT');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(21, 'TYPE_UNKNOWN', 'UNKNOWN');

ALTER TABLE lams_log_event 
DROP FOREIGN KEY FK_lams_event_log_5,
DROP FOREIGN KEY FK_lams_event_log_4,
DROP FOREIGN KEY FK_lams_event_log_3,
DROP FOREIGN KEY FK_lams_event_log_2,
DROP FOREIGN KEY FK_lams_event_log_1;

ALTER TABLE lams_log_event 
MODIFY COLUMN user_id BIGINT(20) NULL,
DROP COLUMN learning_design_id,
ADD COLUMN target_user_id BIGINT(20) NULL,
ADD COLUMN description TEXT NULL,
DROP INDEX occurred_date_time ,
ADD INDEX event_log_occurred_date_time (occurred_date_time ASC),
ADD INDEX FK_event_log_event_type_idx (log_event_type_id ASC),
DROP INDEX FK_lams_event_log_5 ,
DROP INDEX FK_lams_event_log_4 ,
DROP INDEX FK_lams_event_log_3 ,
DROP INDEX user_id ,
DROP INDEX FK_lams_event_log_1 ;

ALTER TABLE lams_log_event 
ADD CONSTRAINT FK_event_log_event_type
  FOREIGN KEY (log_event_type_id)
  REFERENCES lams_log_event_type (log_event_type_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
  
  
  

--  LDEV-4507 Make sure that duplicate groups are not created

-- Update staff group order ID so it is not considered a duplicate of learners' group
UPDATE lams_grouping AS gi JOIN lams_group AS g ON gi.staff_group_id = g.group_id SET g.order_id = 2;

-- delete duplicate groups
CREATE TABLE lams_group_delete 
SELECT g3.group_id FROM 
(SELECT g2.group_id, COUNT(g2.grouping_id) AS c2 FROM 
  (SELECT g1.group_id, g1.order_id, g1.grouping_id, COUNT(ug1.user_id) AS c1 
   FROM lams_group AS g1 
   LEFT JOIN lams_user_group AS ug1 USING (group_id) 
   GROUP BY g1.group_id 
   ORDER BY c1 ASC) AS g2 
GROUP BY g2.grouping_id, g2.order_id HAVING c2 > 1 
) AS g3; 

DELETE s FROM lams_tool_session AS s JOIN lams_group_delete AS g USING (group_id); 
DELETE ug FROM lams_user_group AS ug JOIN lams_group_delete AS g USING (group_id); 
DELETE e FROM lams_branch_activity_entry AS e JOIN lams_group_delete AS g USING (group_id); 
DELETE g2 FROM lams_group AS g2 JOIN lams_group_delete AS g USING (group_id); 

DROP TABLE lams_group_delete;

-- run delete again so groups with other order IDs are removed too
CREATE TABLE lams_group_delete 
SELECT g3.group_id FROM 
(SELECT g2.group_id, COUNT(g2.grouping_id) AS c2 FROM 
  (SELECT g1.group_id, g1.order_id, g1.grouping_id, COUNT(ug1.user_id) AS c1 
   FROM lams_group AS g1 
   LEFT JOIN lams_user_group AS ug1 USING (group_id) 
   GROUP BY g1.group_id 
   ORDER BY c1 ASC) AS g2 
GROUP BY g2.grouping_id, g2.order_id HAVING c2 > 1 
) AS g3; 

DELETE s FROM lams_tool_session AS s JOIN lams_group_delete AS g USING (group_id); 
DELETE ug FROM lams_user_group AS ug JOIN lams_group_delete AS g USING (group_id); 
DELETE e FROM lams_branch_activity_entry AS e JOIN lams_group_delete AS g USING (group_id); 
DELETE g2 FROM lams_group AS g2 JOIN lams_group_delete AS g USING (group_id); 

DROP TABLE lams_group_delete;

-- finally add an index
ALTER TABLE lams_group ADD UNIQUE KEY UQ_lams_group_1 (grouping_id, order_id);
 



-- LDEV-4508 Add Kumalive table to track learners raising/lowering hands
-- and possibly other activity in the future

CREATE TABLE lams_kumalive_log (
	   log_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , kumalive_id BIGINT(20) NOT NULL
     , user_id BIGINT(20)
     , log_date DATETIME NOT NULL
     , log_type TINYINT
     , PRIMARY KEY (log_id)
     , CONSTRAINT FK_lams_kumalive_log_1 FOREIGN KEY (kumalive_id)
                  REFERENCES lams_kumalive (kumalive_id) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_lams_kumalive_log_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
);




-- LDEV-3640 Add obvious cascades. Without them Learning Design delete fails.

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_6;
ALTER TABLE lams_learning_activity ADD CONSTRAINT FK_lams_learning_activity_6 FOREIGN KEY (learning_design_id)
REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_15;
ALTER TABLE lams_learning_activity ADD CONSTRAINT FK_lams_learning_activity_15 FOREIGN KEY (transition_to_id)
REFERENCES lams_learning_transition (transition_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_16;
ALTER TABLE lams_learning_activity ADD CONSTRAINT FK_lams_learning_activity_16 FOREIGN KEY (transition_from_id)
REFERENCES lams_learning_transition (transition_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE lams_learning_transition DROP FOREIGN KEY lddefn_transition_ibfk_1;
ALTER TABLE lams_learning_transition ADD CONSTRAINT lddefn_transition_ibfk_1 FOREIGN KEY (learning_design_id)
REFERENCES lams_learning_design (learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE;




-- LDEV-4538 Add columns to gate activity to track user and time of opening

ALTER TABLE lams_learning_activity ADD COLUMN gate_open_user BIGINT(20) AFTER gate_open_flag,
								   ADD COLUMN gate_open_time DATETIME AFTER gate_open_user,
								   ADD CONSTRAINT FK_lams_learning_activity_17 FOREIGN KEY (gate_open_user)
								       REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE;

								       
								       

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE lams_comment MODIFY body MEDIUMTEXT;
ALTER TABLE lams_competence MODIFY description MEDIUMTEXT;
ALTER TABLE lams_email_notification_archive MODIFY body MEDIUMTEXT;
ALTER TABLE lams_ext_server_org_map MODIFY serverdesc MEDIUMTEXT;
ALTER TABLE lams_gradebook_user_activity MODIFY feedback MEDIUMTEXT;
ALTER TABLE lams_gradebook_user_activity_archive MODIFY feedback MEDIUMTEXT;
ALTER TABLE lams_gradebook_user_lesson_archive MODIFY feedback MEDIUMTEXT;
ALTER TABLE lams_learning_activity MODIFY description MEDIUMTEXT;
ALTER TABLE lams_learning_activity MODIFY options_instructions MEDIUMTEXT;
ALTER TABLE lams_learning_design MODIFY description MEDIUMTEXT;
ALTER TABLE lams_learning_design MODIFY help_text MEDIUMTEXT;
ALTER TABLE lams_learning_design MODIFY license_TEXT MEDIUMTEXT;
ALTER TABLE lams_learning_library MODIFY description MEDIUMTEXT;
ALTER TABLE lams_learning_transition MODIFY description MEDIUMTEXT;
ALTER TABLE lams_lesson MODIFY description MEDIUMTEXT;
ALTER TABLE lams_notebook_entry MODIFY entry MEDIUMTEXT;
ALTER TABLE lams_notification_event MODIFY message MEDIUMTEXT;
ALTER TABLE lams_notification_subscription MODIFY last_operation_message MEDIUMTEXT;
ALTER TABLE lams_organisation MODIFY description MEDIUMTEXT;
ALTER TABLE lams_planner_nodes MODIFY brief_desc MEDIUMTEXT;
ALTER TABLE lams_planner_nodes MODIFY full_desc MEDIUMTEXT;
ALTER TABLE lams_rating_comment MODIFY comment MEDIUMTEXT;




-- LDEV-4564 Audit Log entry for Live Edit
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(22, 'TYPE_LIVE_EDIT', 'LESSON');




-- LDEV-4559 Option to allow students to post anonymously
ALTER TABLE lams_comment 
ADD COLUMN anonymous SMALLINT(6) NULL DEFAULT 0;




-- LDEV-
CREATE TABLE lams_policy_state (
       policy_state_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (policy_state_id)
);

CREATE TABLE lams_policy_type (
       policy_type_id INT(3) NOT NULL
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (policy_type_id)
);

CREATE TABLE lams_policy (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , policy_id BIGINT(20)
     , created_by BIGINT(20) NOT NULL
     , policy_name VARCHAR(255) NOT NULL
     , version LONG
     , summary TEXT
     , full_policy TEXT
     , last_modified DATETIME NOT NULL
     , policy_state_id INT(3) NOT NULL
     , policy_type_id INT(3) NOT NULL
     , PRIMARY KEY (uid)
     , KEY (created_by)
     , CONSTRAINT FK_lams_lesson_1 FOREIGN KEY (created_by)
                  REFERENCES lams_user (user_id)
     , KEY (policy_state_id)
     , CONSTRAINT FK_lams_policy_2 FOREIGN KEY (policy_state_id)
                  REFERENCES lams_policy_state (policy_state_id)
     , KEY (policy_type_id)
     , CONSTRAINT FK_lams_policy_3 FOREIGN KEY (policy_type_id)
                  REFERENCES lams_policy_type (policy_type_id)
);

CREATE TABLE lams_policy_consent (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , date_agreed_on DATETIME NOT NULL
     , policy_uid BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , KEY (policy_uid)
     , CONSTRAINT FK_lams_consent_1_1 FOREIGN KEY (policy_uid)
                  REFERENCES lams_policy (uid)
     , KEY (user_id)
     , CONSTRAINT FK_lams_consent_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
);

INSERT INTO lams_policy_state VALUES (1, 'ACTIVE');
INSERT INTO lams_policy_state VALUES (2, 'INACTIVE');

INSERT INTO lams_policy_type VALUES (1, 'TYPE_SITE_POLICY');
INSERT INTO lams_policy_type VALUES (2, 'TYPE_PRIVACY_POLICY');
INSERT INTO lams_policy_type VALUES (3, 'TYPE_THIRD_PARTIES_POLICY');
INSERT INTO lams_policy_type VALUES (4, 'TYPE_OTHER');




-- LDEV-4594 / LDEV-4583 Allow/Block access to index.do for integration learners. Default to false - do not allow direct access.
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('AllowDirectAccessIntgrtnLrnr','false', 'config.allow.direct.access.for.integration.learners', 'config.header.features', 'BOOLEAN', 1);




-- LDEV-4592 Bump server version so LDs exported with this version do not undergo resources path rewriting in XML
UPDATE lams_configuration SET config_value = '3.0.2' WHERE config_key = 'ServerVersionNumber';




-- LDEV-4603 Delete preview lessons with peer review activity doesn't work
ALTER TABLE lams_rating DROP FOREIGN KEY FK_lams_rating_3;
ALTER TABLE lams_rating_comment DROP FOREIGN KEY FK_lams_rating_comment_3;




-- LDEV-4605 Codify country for users
UPDATE lams_user SET country=NULL;
ALTER TABLE lams_user CHANGE COLUMN country country VARCHAR(2) NULL DEFAULT NULL ;





-- LDEV-4609 Bind archived activity marks in batches
DELETE FROM lams_gradebook_user_activity_archive;
DELETE FROM lams_gradebook_user_lesson_archive;

ALTER TABLE lams_gradebook_user_lesson_archive ADD COLUMN archive_date DATETIME;
ALTER TABLE lams_gradebook_user_activity_archive ADD COLUMN archive_date DATETIME;

DELETE FROM lams_progress_attempted_archive;
DELETE FROM lams_progress_completed_archive;
DELETE FROM lams_learner_progress_archive;

ALTER TABLE lams_learner_progress_archive ADD COLUMN archive_date DATETIME;




-- LDEV-4620 Audit Log entry for Marks released in tool
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(23, 'TYPE_TOOL_MARK_RELEASED', 'MARKS');




-- LDEV-4616 Remove obsolete column
ALTER TABLE lams_organisation DROP FOREIGN KEY FK_lams_organisation_5,
							  DROP COLUMN locale_id;

							  
							  


-- LDEV-4625 Add default country in sysadmin menu
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('ServerCountry','AU', 'config.server.country', 'config.header.look.feel', 'STRING', 1);




-- LDEV-4617 Add column for email verification
ALTER TABLE lams_signup_organisation ADD COLUMN email_verify TINYINT(1) DEFAULT 0 AFTER add_with_monitor;
ALTER TABLE lams_user ADD COLUMN email_verified TINYINT(1) DEFAULT 1 AFTER email;




-- LDEV-4641 Add SMTP port and authentication type
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('SMTPPort','25', 'config.smtp.port', 'config.header.email', 'LONG', 0),
	   ('SMTPAuthSecurity','none', 'config.smtp.auth.security', 'config.header.email', 'STRING', 1);

	   

-- LDEV-4639 Remove tutorials stub
ALTER TABLE lams_user DROP COLUMN tutorials_disabled;
DROP TABLE lams_user_disabled_tutorials;




-- LDEV-4644 Add learning outcomes tables

CREATE TABLE lams_outcome_scale (
	scale_id MEDIUMINT AUTO_INCREMENT,
	organisation_id BIGINT,
	name VARCHAR(255),
	code VARCHAR(50),
	description TEXT,
	content_folder_id CHAR(36),
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (scale_id),
	INDEX (name),
	INDEX (code),
	UNIQUE INDEX (code, organisation_id),
	CONSTRAINT FK_lams_outcome_scale_1 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
	);
	
CREATE TABLE lams_outcome_scale_item (
	item_id INT AUTO_INCREMENT,
	scale_id MEDIUMINT,
	value TINYINT,
	name VARCHAR(255),
	PRIMARY KEY (item_id),
	CONSTRAINT FK_lams_outcome_scale_item_1 FOREIGN KEY (scale_id) REFERENCES lams_outcome_scale (scale_id) ON DELETE CASCADE ON UPDATE CASCADE
	);

CREATE TABLE lams_outcome (
	outcome_id MEDIUMINT AUTO_INCREMENT,
	organisation_id BIGINT,
	scale_id MEDIUMINT NOT NULL,
	name VARCHAR(255),
	code VARCHAR(50),
	description TEXT,
	content_folder_id CHAR(36),
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (outcome_id),
	INDEX (name),
	INDEX (code),
	UNIQUE INDEX (code, organisation_id),
	CONSTRAINT FK_lams_outcome_1 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE,
	-- Do not remove outcomes when a scale gets removed. Programmer needs to do it manually to make sure it is the right step.
	CONSTRAINT FK_lams_outcome_2 FOREIGN KEY (scale_id) REFERENCES lams_outcome_scale (scale_id) ON UPDATE CASCADE
	);
	
CREATE TABLE lams_outcome_mapping (
	mapping_id BIGINT AUTO_INCREMENT,
	outcome_id MEDIUMINT NOT NULL,
	lesson_id BIGINT,
	tool_content_id BIGINT,
	item_id BIGINT,
	PRIMARY KEY (mapping_id),
	CONSTRAINT FK_lams_outcome_mapping_1 FOREIGN KEY (outcome_id) REFERENCES lams_outcome (outcome_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_mapping_2 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_mapping_3 FOREIGN KEY (tool_content_id) REFERENCES lams_tool_content (tool_content_id) ON DELETE CASCADE ON UPDATE CASCADE
	);

CREATE TABLE lams_outcome_result (
	result_id BIGINT AUTO_INCREMENT,
	mapping_id BIGINT NOT NULL,
	user_id BIGINT,
	value TINYINT,
	create_by BIGINT,
	create_date_time DATETIME NOT NULL,
	PRIMARY KEY (result_id),
	CONSTRAINT FK_lams_outcome_result_1 FOREIGN KEY (mapping_id) REFERENCES lams_outcome_mapping (mapping_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_result_2 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_outcome_result_3 FOREIGN KEY (create_by) REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE
	);

-- create default scale
INSERT INTO lams_outcome_scale VALUES (1, NULL, 'Default attainment scale', 'default', 'Default global scale', NULL, 1, NOW());
INSERT INTO lams_outcome_scale_item VALUES (1, 1, 0, 'Not yet attained'), (2, 1, 1, 'Attained');
	



-- LDEV-4440 Update system tool access URLs after migration to Spring MVC
UPDATE lams_system_tool SET
learner_url = 'learning/grouping/performGrouping.do',
learner_preview_url = 'learning/grouping/performGrouping.do',
learner_progress_url = 'learning/grouping/viewGrouping.do?mode=teacher'
WHERE system_tool_id = 1;

UPDATE lams_system_tool SET
learner_url = 'learning/gate/knockGate.do',
learner_preview_url = 'learning/gate/knockGate.do'
WHERE system_tool_id = 2;

UPDATE lams_system_tool SET
learner_url = 'learning/gate/knockGate.do',
learner_preview_url = 'learning/gate/knockGate.do'
WHERE system_tool_id = 3;

UPDATE lams_system_tool SET
learner_url = 'learning/gate/knockGate.do',
learner_preview_url = 'learning/gate/knockGate.do'
WHERE system_tool_id = 4;

UPDATE lams_system_tool SET
learner_url = 'learning/gate/knockGate.do',
learner_preview_url = 'learning/gate/knockGate.do'
WHERE system_tool_id = '5';

UPDATE lams_system_tool SET
learner_url = 'learning/branching/performBranching.do',
learner_preview_url = 'learning/branching/performBranching.do'
WHERE system_tool_id = '6';

UPDATE lams_system_tool SET
learner_url = 'learning/branching/performBranching.do',
learner_preview_url = 'learning/branching/performBranching.do'
WHERE system_tool_id = '7';

UPDATE lams_system_tool SET
learner_url = 'learning/branching/performBranching.do',
learner_preview_url = 'learning/branching/performBranching.do'
WHERE system_tool_id = '8';

UPDATE lams_system_tool SET
learner_url = 'learning/gate/knockGate.do',
learner_preview_url = 'learning/gate/knockGate.do'
WHERE system_tool_id = '10';

-- LDEV-4645 Add columns for default lesson settings
ALTER TABLE lams_ext_server_org_map
	ADD COLUMN learner_presence_avail TINYINT(1) DEFAULT 0,
	ADD COLUMN learner_im_avail TINYINT(1) DEFAULT 0,
	ADD COLUMN live_edit_enabled TINYINT(1) DEFAULT 1,
	ADD COLUMN enable_lesson_notifications TINYINT(1) DEFAULT 1,
	ADD COLUMN force_restart TINYINT(1) DEFAULT 0,
	ADD COLUMN allow_restart TINYINT(1) DEFAULT 0,
	ADD COLUMN gradebook_on_complete TINYINT(1) DEFAULT 1;

	
	

-- LDEV-4440 Update system tool access URLs after migration to Spring MVC
UPDATE lams_system_tool SET monitor_url = 'monitoring/grouping/startGrouping.do', contribute_url = 'monitoring/grouping/startGrouping.do' WHERE system_tool_id = '1';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '2';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '3';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '4';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '5';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/chosenBranching.do?method=assignBranch', contribute_url = 'monitoring/grouping/startGrouping.do' WHERE system_tool_id = '6';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/groupedBranching.do?method=viewBranching', contribute_url = 'monitoring/groupedBranching.do?method=assignBranch' WHERE system_tool_id = '7';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/toolBranching.do?method=viewBranching', contribute_url = 'monitoring/toolBranching.do?method=viewBranching' WHERE system_tool_id = '8';

UPDATE lams_system_tool SET learner_progress_url = 'monitoring/complexProgress.do', monitor_url = 'monitoring/sequence/viewSequence.do', contribute_url = 'monitoring/sequence/viewSequence.do' WHERE system_tool_id = '9';

UPDATE lams_system_tool SET monitor_url = 'monitoring/gate/viewGate.do', contribute_url = 'monitoring/gate/viewGate.do' WHERE system_tool_id = '10';




-- LDEV-4658 Enable profile portrait editing
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('EnablePortraitEditing','true', 'config.enable.portrait.editing', 'config.header.features', 'BOOLEAN', 0);




-- LDEV-4184 Allow longer ID format

ALTER TABLE lams_planner_nodes MODIFY COLUMN content_folder_id CHAR(36);




-- LDEV-4686 Remove Kaltura settings from admin config settings	
DELETE FROM lams_configuration WHERE (config_key = 'KalturaServer');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaPartnerId');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaSubPartnerId');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaUserSecret');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaKCWUiConfId');
DELETE FROM lams_configuration WHERE (config_key = 'KalturaKDPUiConfId');



-- LDEV-4710 Config setting to display or not the "Forgot your password?" option
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('EnableForgotYourPasswordLink','true', 'config.enable.forgot.your.password.link', 'config.header.features', 'BOOLEAN', 0);




-- LDEV-4705 Remove course admin role
DROP TABLE lams_role_privilege;
DROP TABLE lams_privilege;

-- add group manager role to user who has got only group admin and not group manager yet
INSERT INTO lams_user_organisation_role
	SELECT NULL, user_organisation_id, 2
	FROM lams_user_organisation_role AS a 
	WHERE role_id = 6 
	AND NOT EXISTS
		(SELECT 1 FROM lams_user_organisation_role WHERE a.user_organisation_id = user_organisation_id AND role_id = 2);

-- delete role assigments for group admin
DELETE FROM lams_user_organisation_role WHERE role_id = 6;

-- delete role itself
DELETE FROM lams_role WHERE role_id = 6;




-- LDEV-4705 Remove course admin role
SET @admin_roles = (SELECT config_value FROM lams_configuration WHERE config_key = 'LDAPGroupAdminMap');
UPDATE lams_configuration SET config_value = CONCAT(config_value, ';', @admin_roles)
WHERE config_key = 'LDAPGroupManagerMap';

DELETE FROM lams_configuration WHERE config_key = 'LDAPGroupAdminMap';



--     LDEV-4726 Update and add new creative common licenses 4.0 for sequences
--  Add an order_id column so we can sequence them how we like
ALTER TABLE lams_license 
ADD COLUMN order_id TINYINT NULL DEFAULT '0';

UPDATE lams_license SET order_id = license_id;
UPDATE lams_license SET order_id = 8 WHERE license_id = 6;

UPDATE lams_license 
SET name = 'CC Attribution-ShareAlike 4.0', 
url = 'https://creativecommons.org/licenses/by-sa/4.0/', 
picture_url = '/images/license/by-sa.svg'
WHERE license_id = 5;

UPDATE lams_license 
SET name = 'CC Attribution-No Derivatives 4.0', 
url = 'https://creativecommons.org/licenses/by-nd/4.0/', 
picture_url = '/images/license/by-nd.svg'
WHERE license_id = 2;

UPDATE lams_license 
SET name = 'CC Attribution-Noncommercial 4.0', 
url = 'https://creativecommons.org/licenses/by-nc/4.0/', 
picture_url = '/images/license/by-nc.eu.svg'
WHERE license_id = 4;

UPDATE lams_license 
SET name = 'LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 4.0', 
url = 'https://creativecommons.org/licenses/by-nc-sa/4.0/', 
picture_url = '/images/license/by-nc-sa.eu.svg'
WHERE license_id = 1;

UPDATE lams_license 
SET name = 'CC Attribution-Noncommercial-No Derivatives 4.0', 
url = 'https://creativecommons.org/licenses/by-nc-nd/4.0/', 
picture_url = '/images/license/by-nc-nd.svg'
WHERE license_id = 3;

INSERT into lams_license 
VALUES (7, 'CC Attribution 4.0', 'by', 'https://creativecommons.org/licenses/by/4.0/', 0, '/images/license/by.svg', 6);

INSERT into lams_license 
VALUES (8, 'Public Domain', 'CC0', 'https://creativecommons.org/publicdomain/zero/1.0/', 0, '/images/license/publicdomain.svg', 7);


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;