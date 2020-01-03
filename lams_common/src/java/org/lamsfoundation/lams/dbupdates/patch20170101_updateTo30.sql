SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch02040007.sql to patch2040084.sql
-- It should upgrade LAMS to version 3.0


-- LDEV-2450
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.shareresourcesforum.ApplicationResources' 
		where library_activity_ui_image='images/icon_urlcontentmessageboard.swf' and learning_design_id is NULL;
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.chatscribe.ApplicationResources' 
		where library_activity_ui_image='images/icon_groupreporting.swf' and learning_design_id is NULL;
update lams_learning_activity set 
	language_file='org.lamsfoundation.lams.library.forumscribe.ApplicationResources' 
		where library_activity_ui_image='images/icon_forum_and_scribe.swf' and learning_design_id is NULL;
			
-- upgrade versions to 2.4		
update lams_configuration set config_value='2.4' where config_key='Version';
update lams_configuration set config_value='2.4.0.201204131000' where config_key='LearnerClientVersion' OR config_key='ServerVersionNumber' OR config_key='MonitorClientVersion' OR config_key='AuthoringClientVersion';
update lams_configuration set config_value='2012-04-13' where config_key='DictionaryDateCreated';



-- LDEV-2806 Fill Activities with missing transition data
UPDATE lams_learning_activity AS act
 LEFT JOIN  lams_learning_transition AS trans_to
  ON trans_to.to_activity_id = act.activity_id
  AND trans_to.transition_type = 0
 LEFT JOIN lams_learning_transition AS trans_from
  ON trans_from.from_activity_id = act.activity_id
  AND trans_from.transition_type = 0
 SET act.transition_from_id = trans_from.transition_id,
     act.transition_to_id = trans_to.transition_id;

     

-- delete all entried from  lams_supported_locale 
DELETE FROM  lams_supported_locale;

-- Add again all supported Locales  LDEV-2830
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (1, 'en', 'AU', 'English (Australia)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (2, 'es', 'ES', 'Español', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (3, 'mi', 'NZ', 'Māori', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (4, 'de', 'DE', 'Deutsch', 'LTR', 'de');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (5, 'zh', 'CN', '简体中文', 'LTR', 'zh-cn');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (6, 'fr', 'FR', 'Français', 'LTR', 'fr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (7, 'it', 'IT', 'Italiano', 'LTR', 'it');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (8, 'no', 'NO', 'Norsk', 'LTR', 'no');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (9, 'sv', 'SE', 'Svenska', 'LTR', 'sv');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (10, 'ko', 'KR', '한국어', 'LTR', 'ko');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (11, 'pl', 'PL', 'Polski', 'LTR', 'pl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (12, 'pt', 'BR', 'Português (Brasil)', 'LTR', 'pt-br');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (13, 'hu', 'HU', 'Magyar', 'LTR', 'hu');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (14, 'bg', 'BG', 'Български', 'LTR', 'bg');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (15, 'cy', 'GB', 'Cymraeg (Cymru)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (16, 'th', 'TH', 'Thai', 'LTR', 'th');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (17, 'el', 'GR', 'Ελληνικά', 'LTR', 'el');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (18, 'nl', 'BE', 'Nederlands (België)', 'LTR', 'nl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (19, 'ar', 'JO', 'عربي', 'RTL', 'ar');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (20, 'da', 'DK', 'Dansk', 'LTR', 'da');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (21, 'ru', 'RU', 'Русский', 'LTR', 'ru');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (22, 'vi', 'VN', 'Tiếng Việt', 'LTR', 'vi');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (23, 'zh', 'TW', 'Chinese (Taiwan)', 'LTR', 'zh');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (24, 'ja', 'JP', '日本語', 'LTR', 'ja');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (25, 'ms', 'MY', 'Malay (Malaysia)', 'LTR', 'ms');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (26, 'tr', 'TR', 'Türkçe', 'LTR', 'tr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (27, 'ca', 'ES', 'Català', 'LTR', 'ca');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (28, 'sl', 'SI', 'Slovenščina', 'LTR', 'sl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (29, 'es', 'MX', 'Español (México)', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (30, 'cs', 'CZ', 'Čeština', 'LTR', 'cs');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (31, 'id', 'ID', 'Indonesian', 'LTR', 'en-au');



-- LDEV-2829  Lesson intro page
ALTER TABLE lams_lesson ADD COLUMN enable_lesson_intro TINYINT(1) DEFAULT 0;
ALTER TABLE lams_lesson ADD COLUMN display_design_image TINYINT(1) DEFAULT 0;



-- LDEV-2889  Collapsible progress panel in Learner
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LearnerCollapsProgressPanel','true', 'config.learner.collapsible.progress.panel', 'config.header.features', 'BOOLEAN', 0);



-- LDEV-2890 When importing a sequence make the suffix string optional, turned off by default
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('SuffixImportedLD','false', 'config.authoring.suffix', 'config.header.features', 'BOOLEAN', 0);



-- LDEV-2905 Integrate lams_signup to LAMS core
CREATE TABLE IF NOT EXISTS lams_signup_organisation (
	signup_organisation_id BIGINT(20) NOT NULL auto_increment,
	organisation_id BIGINT(20) NOT NULL,
	add_to_lessons TINYINT(1) DEFAULT 1,
	add_as_staff TINYINT(1) DEFAULT 0,
	add_with_author TINYINT(1) DEFAULT 0,
	add_with_monitor TINYINT(1) DEFAULT 0,
	course_key VARCHAR(255),
	blurb TEXT,
	create_date DATETIME,
	disabled TINYINT(1) DEFAULT 0,
	context VARCHAR(255) UNIQUE,
	PRIMARY KEY (signup_organisation_id),
	INDEX (organisation_id)
)ENGINE=InnoDB;

DROP TABLE IF EXISTS lams_signup_user;


-- LDEV-2949
UPDATE lams_presence_chat_msgs SET room_name=SUBSTRING_INDEX(room_name, '-', 1);
UPDATE lams_presence_chat_msgs SET room_name=SUBSTRING_INDEX(room_name, '_', 1);

ALTER TABLE lams_presence_chat_msgs CHANGE COLUMN room_name lesson_id BIGINT(20) NOT NULL,
									ADD CONSTRAINT FK_lams_presence_chat_msgs_lesson FOREIGN KEY (lesson_id)
                  						REFERENCES lams_lesson (lesson_id)
                  						ON UPDATE CASCADE ON DELETE CASCADE,
                  					ADD INDEX idx_lams_presence_chat_msgs_from (from_user),
                  					ADD INDEX idx_lams_presence_chat_msgs_to   (to_user);
				 
CREATE TABLE lams_presence_user (
	nickname VARCHAR(255) NOT NULL,
	lesson_id BIGINT(20) NOT NULL,
	last_presence DATETIME,
	PRIMARY KEY (nickname, lesson_id),
    CONSTRAINT FK_lams_presence_user_lesson FOREIGN KEY (lesson_id)
        REFERENCES lams_lesson (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE
)ENGINE=InnoDB;

DELETE FROM lams_configuration WHERE header_name='config.header.chat';



-- 	LDEV-2986 Allow choosing which tab is active on a signup page
ALTER TABLE lams_signup_organisation ADD COLUMN login_tab_active TINYINT(1) DEFAULT 0; 




-- 	LDEV-3010 Emails sent via forum when rich text editor is able should be sent with the correct mime-type
ALTER TABLE lams_events ADD COLUMN html_format TINYINT(1) DEFAULT 0; 




-- 	LDEV-3070 Allow single activity lesson creation on redesigned index page
ALTER TABLE lams_organisation ADD COLUMN enable_single_activity_lessons TINYINT(1) NOT NULL DEFAULT 1
	  AFTER enable_learner_gradebook; 



-- 	LDEV-3083  update executable script extensions
UPDATE lams_configuration set config_value = '.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs,.php,.jsp,.asp,.aspx,.pl,.do,.py,.tcl,.cgi,.shtml,.stm,.cfm,.adp' where config_key = 'ExecutableExtensions';




-- LDEV-3112 Remove Flash Learner
DELETE FROM lams_configuration WHERE config_key = 'EnableFlash'
                                  OR config_key = 'ForceMobileDevToUseFlashless';
ALTER TABLE lams_user DROP COLUMN enable_flash;




-- LDEV-3094 In Gradebook fix computing overall marks when using Groups
UPDATE lams_gradebook_user_lesson AS gless,
	(SELECT tses.lesson_id, gact.user_id, SUM(gact.mark) AS mark
	FROM lams_gradebook_user_activity AS gact
	JOIN (SELECT DISTINCT lesson_id, activity_id FROM lams_tool_session) AS tses
	USING (activity_id)
	GROUP BY tses.lesson_id, gact.user_id) AS agg
SET gless.mark = agg.mark
WHERE gless.lesson_id = agg.lesson_id AND gless.user_id = agg.user_id; 




-- LDEV-3119 Course level groups
CREATE TABLE lams_organisation_grouping (
	grouping_id BIGINT(20) NOT NULL AUTO_INCREMENT,
	organisation_id BIGINT(20) NOT NULL,
	name     VARCHAR(255),
	PRIMARY KEY (grouping_id),
	CONSTRAINT FK_lams_organisation_grouping_1 FOREIGN KEY (organisation_id)
    	REFERENCES lams_organisation (organisation_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE lams_organisation_group (
	group_id BIGINT(20) NOT NULL AUTO_INCREMENT,
	grouping_id BIGINT(20) NOT NULL,
	name     VARCHAR(255),
	PRIMARY KEY (group_id),
	CONSTRAINT FK_lams_organisation_group_1 FOREIGN KEY (grouping_id)
    	REFERENCES lams_organisation_grouping (grouping_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE lams_user_organisation_group (
	group_id BIGINT(20) NOT NULL,
	user_id BIGINT(20) NOT NULL,
	PRIMARY KEY (group_id, user_id),
	CONSTRAINT FK_lams_user_organisation_group_1 FOREIGN KEY (group_id)
    	REFERENCES lams_organisation_group (group_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_lams_user_organisation_group_2 FOREIGN KEY (user_id)
    	REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;



-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and run offline activity options
ALTER TABLE lams_learning_activity CHANGE COLUMN run_offline_flag run_offline_flag TINYINT(1) NOT NULL DEFAULT '0';

ALTER TABLE lams_learning_design DROP COLUMN online_instructions;
ALTER TABLE lams_learning_design DROP COLUMN offline_instructions;

-- LDEV-3172: Reduce the Inactive User Timeout setting to 3 hours

UPDATE lams_configuration SET config_value = '10800' WHERE config_key = 'UserInactiveTimeout';



-- LDEV-3170 Force restart from beginning of lesson for learners

ALTER TABLE lams_lesson ADD COLUMN learner_restart TINYINT(1) DEFAULT 0;




-- LDEV-3115 Flashless Authoring: annotation labels and regions, access dates for recently used LDs

CREATE TABLE IF NOT EXISTS lams_learning_design_annotation (
	  uid BIGINT(20) NOT NULL auto_increment
	, learning_design_id BIGINT(20) NOT NULL
	, ui_id INT(11)
	, title VARCHAR(1024)
	, xcoord INT(11)
	, ycoord INT(11)
	, end_xcoord INT(11)
	, end_ycoord INT(11)
	, color CHAR(7)
	, CONSTRAINT FK_lams_learning_design_annotation_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
                  ON UPDATE CASCADE ON DELETE CASCADE
	, PRIMARY KEY (uid)
)ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS lams_learning_design_access (
	  learning_design_id BIGINT(20) NOT NULL
	, user_id BIGINT(20) NOT NULL
	, access_date DATETIME
	, CONSTRAINT FK_lams_learning_design_access_1 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
                  ON UPDATE CASCADE ON DELETE CASCADE
    , CONSTRAINT FK_lams_learning_design_access_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)
                  ON UPDATE CASCADE ON DELETE CASCADE
	, PRIMARY KEY (learning_design_id, user_id)
)ENGINE=InnoDB;





-- LDEV-3207 New type of schedule gate, based on activity completion

ALTER TABLE lams_learning_activity ADD COLUMN gate_activity_completion_based TINYINT(1)
									   AFTER gate_end_date_time;


									   
									   

-- LDEV-3207 Remove unnecessary columns from schedule gate.

ALTER TABLE lams_learning_activity DROP COLUMN gate_start_date_time,
								   DROP COLUMN gate_end_date_time;


								   
								   
--  LDEV-3219 Adding option to turn validation off for user details
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationUsername','true', 'config.user.validation.username', 'config.header.user.validation', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationFirstLastName','true', 'config.user.validation.first.last.name', 'config.header.user.validation', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('UserValidationEmail','true', 'config.user.validation.emails', 'config.header.user.validation', 'BOOLEAN', 0);




-- LDEV-3225 Remove unnecessary columns.

ALTER TABLE lams_tool DROP COLUMN contribute_url,
					  DROP COLUMN moderation_url;


					  
					  
--  LDEV-3254 Allow periodic update of configuration cache
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ConfigCacheRefresInterval', 0, 'config.cache.refresh', 'config.header.system', 'LONG', 0);




--  LDEV-3269 Introduce tool groups
CREATE TABLE lams_learning_library_group (
	  group_id INT NOT NULL AUTO_INCREMENT
	, name VARCHAR(64) NOT NULL
	, PRIMARY KEY (group_id)
)ENGINE=InnoDB;

CREATE TABLE lams_learning_library_to_group (
	  group_id INT NOT NULL
	, learning_library_id BIGINT(20) NOT NULL
	, PRIMARY KEY (group_id, learning_library_id)
)ENGINE=InnoDB;




--  LDEV-3292 Add index on Activity table

CREATE INDEX lams_learning_activity_tool_content_id ON lams_learning_activity(tool_content_id);




--  LDEV-3315 Remove obsolete AUTHOR ADMIN role

DELETE FROM lams_user_organisation_role WHERE role_id = (SELECT role_id FROM lams_role WHERE name = 'AUTHOR ADMIN');
DELETE FROM lams_role_privilege WHERE role_id = (SELECT role_id FROM lams_role WHERE name = 'AUTHOR ADMIN');
DELETE FROM lams_role WHERE name = 'AUTHOR ADMIN';




--  LDEV-3366: Fix Learning Library IDs incorrectly imported from Learning Designs

UPDATE lams_learning_activity AS act, lams_tool AS tool
SET act.learning_library_id = tool.learning_library_id
WHERE act.tool_id = tool.tool_id AND act.learning_library_id <> tool.learning_library_id;




--  LDEV-3382: Add datetime parameter check to LoginRequest
ALTER TABLE lams_ext_server_org_map ADD COLUMN time_to_live_login_request integer DEFAULT 80;




--  LDEV-3383: Remove Web authentication method
UPDATE lams_user SET authentication_method_id = 1 WHERE authentication_method_id = 2;
DELETE FROM lams_authentication_method WHERE authentication_method_id = 2;




--  LDEV-3382: Add datetime parameter check to LoginRequest
ALTER TABLE lams_ext_server_org_map ADD COLUMN time_to_live_login_request_enabled TINYINT(1) NOT NULL DEFAULT 0;




-- LDEV-3340: Remove cache management from sysadmin
DELETE FROM lams_configuration WHERE config_key = 'UseCacheDebugListener';



-- 	LDEV-3410 Add feature allowing disable export portfolio and live edit 
ALTER TABLE lams_organisation ADD COLUMN enable_live_edit TINYINT(1) NOT NULL DEFAULT 1
	  AFTER enable_single_activity_lessons;
ALTER TABLE lams_organisation ADD COLUMN enable_export_portfolio TINYINT(1) NOT NULL DEFAULT 1
	  AFTER enable_live_edit;

	  
	  

-- 	LDEV-3420 Add date column to Gradebook marks
ALTER TABLE lams_gradebook_user_activity ADD COLUMN update_date DATETIME DEFAULT NULL;




-- LDEV-3450 Implement peer review feature
CREATE TABLE lams_rating_criteria_type (
       rating_criteria_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (rating_criteria_type_id)
)ENGINE=InnoDB;

CREATE TABLE lams_rating_criteria (
       rating_criteria_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , title VARCHAR(255)
     , rating_criteria_type_id INT(11) NOT NULL DEFAULT 0
     , comments_enabled TINYINT(1) NOT NULL DEFAULT 0
     , comments_min_words_limit INT(11) DEFAULT 0
     , order_id INT(11) NOT NULL
     , tool_content_id BIGINT(20)
     , item_id BIGINT(20)
     , lesson_id BIGINT(20)
     , PRIMARY KEY (rating_criteria_id)
     , INDEX (rating_criteria_type_id)
     , CONSTRAINT FK_lams_rating_criteria_1 FOREIGN KEY (rating_criteria_type_id)
                  REFERENCES lams_rating_criteria_type (rating_criteria_type_id)
     , INDEX (tool_content_id)
     , CONSTRAINT FK_lams_rating_criteria_2 FOREIGN KEY (tool_content_id)
                  REFERENCES lams_tool_content (tool_content_id) ON DELETE NO ACTION ON UPDATE NO ACTION                  
     , INDEX (lesson_id)
     , CONSTRAINT FK_lams_rating_criteria_3 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

CREATE TABLE lams_rating (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating_criteria_id BIGINT(20) NOT NULL
     , item_id BIGINT(20)
     , user_id BIGINT(20) NOT NULL
     , rating FLOAT
     , PRIMARY KEY (uid)
     , INDEX (rating_criteria_id)
     , CONSTRAINT FK_lams_rating_1 FOREIGN KEY (rating_criteria_id)
                  REFERENCES lams_rating_criteria (rating_criteria_id) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (user_id)
     , CONSTRAINT FK_lams_rating_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE lams_rating_comment (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , rating_criteria_id BIGINT(20) NOT NULL
     , item_id BIGINT(20)
     , user_id BIGINT(20) NOT NULL
     , comment text
     , PRIMARY KEY (uid)
     , INDEX (rating_criteria_id)
     , CONSTRAINT FK_lams_rating_comment_1 FOREIGN KEY (rating_criteria_id)
                  REFERENCES lams_rating_criteria (rating_criteria_id) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (user_id)
     , CONSTRAINT FK_lams_rating_comment_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

INSERT INTO lams_rating_criteria_type VALUES (1, 'TOOL_ACTIVITY');
INSERT INTO lams_rating_criteria_type VALUES (2, 'AUTHORED_ITEM');
INSERT INTO lams_rating_criteria_type VALUES (3, 'LEARNER_ITEM');
INSERT INTO lams_rating_criteria_type VALUES (4, 'LESSON');




-- LDEV-3491 Mark designs as removed without deleting the record
ALTER TABLE lams_learning_design ADD COLUMN removed TINYINT(1) NOT NULL DEFAULT 0;




-- LDEV-3523 Type used to classify a learning design into a grouping understood by a user. Can be used by integrations and contents determined by the integration.
ALTER TABLE lams_learning_design ADD COLUMN design_type VARCHAR(255);




-- LDEV-3292 Add a vital index for notebook entries
CREATE INDEX ext_sig_user ON lams_notebook_entry(external_id, external_id_type, external_signature, user_id); 




-- LDEV-3578 Add salt to password. Remove browser encryption.
ALTER TABLE lams_user MODIFY COLUMN password CHAR(64),
					  ADD COLUMN salt CHAR(64) AFTER password;
					  
DELETE FROM lams_configuration WHERE config_key = 'LDAPEncryptPasswordFromBrowser';




-- LDEV-3597 Remove SSL configuration as it is done differently in WildFly
DELETE FROM lams_configuration WHERE config_key = 'TruststorePassword'
                                  OR config_key = 'TruststorePath';

                                  
                                  
--  LDEV-2903 Remove the table as it is never used
DROP TABLE IF EXISTS lams_lesson_learner;




-- LDEV-3606 Add comment's posted date
ALTER TABLE lams_rating_comment ADD COLUMN posted_date datetime;
UPDATE lams_rating_comment SET posted_date=NOW();




-- LDEV-3614 The table is used for Gate activities only. It can be optimised to hold only allowed learners.
DELETE FROM lams_activity_learners WHERE allowed_to_pass = 0;

ALTER TABLE lams_activity_learners DROP COLUMN allowed_to_pass,
								   RENAME TO lams_gate_allowed_learners;



-- LDEV-3621 Ability to import and use groups from integrated server
ALTER TABLE lams_ext_server_org_map ADD COLUMN ext_groups_url text DEFAULT NULL;



-- LDEV-3629 Drop an obsolete column, otherwise it would require conversion.
-- It is in a separate script so it does not fail if the next script fails for any reason and needs to be repeated.

ALTER TABLE lams_user DROP COLUMN openid_url;



-- LDEV-3629 Use 4 bytes Unicode characters in the whole DB

-- Alter character set and encoding of the each table.
-- The database char set needs to be altered manually as at this point we do not know its name.
-- Limit indexed text fields' length.

ALTER TABLE lams_activity_category CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_activity_evaluation CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_auth_method_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_authentication_method CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
									   MODIFY COLUMN authentication_method_name VARCHAR(191) NOT NULL;
ALTER TABLE lams_branch_activity_entry CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_branch_condition CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_competence CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
							MODIFY COLUMN title VARCHAR(191) NOT NULL;
ALTER TABLE lams_competence_mapping CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_copy_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_cr_credential CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_cr_node CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_cr_node_version CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_cr_node_version_property CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_cr_workspace CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_cr_workspace_credential CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_data_flow CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_event_subscriptions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_events CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_ext_course_class_map CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_ext_server_lesson_map CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_ext_server_org_map CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
									MODIFY COLUMN serverid VARCHAR(191) NOT NULL;
ALTER TABLE lams_ext_server_tool_map CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_ext_user_userid_map CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_gate_activity_level CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_gate_allowed_learners CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_gradebook_user_activity CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_gradebook_user_lesson CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_group CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_grouping CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_grouping_support_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_grouping_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_input_activity CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learner_progress CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_activity CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_activity_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_design CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_design_access CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_design_annotation CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_library CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_library_group CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_library_to_group CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_learning_transition CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_lesson CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_lesson_dependency CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_lesson_state CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_license CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_log_event CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_log_event_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_notebook_entry CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_openid_config CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_organisation CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_organisation_group CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_organisation_grouping CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_organisation_state CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_organisation_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_password_request CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_planner_activity_metadata CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_planner_node_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_planner_nodes CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_planner_recent_learning_designs CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_presence_chat_msgs CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_presence_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
							   MODIFY COLUMN nickname VARCHAR(191) NOT NULL;
ALTER TABLE lams_privilege CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_progress_attempted CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_progress_completed CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_qtz_BLOB_TRIGGERS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								   MODIFY COLUMN TRIGGER_NAME varchar(191) NOT NULL,
								   MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL;
ALTER TABLE lams_qtz_CALENDARS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
							   MODIFY COLUMN CALENDAR_NAME varchar(191) NOT NULL;
ALTER TABLE lams_qtz_CRON_TRIGGERS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								   MODIFY COLUMN TRIGGER_NAME varchar(191) NOT NULL,
								   MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL;
ALTER TABLE lams_qtz_FIRED_TRIGGERS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								    MODIFY COLUMN TRIGGER_NAME varchar(191) NOT NULL,
								    MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL,
								    MODIFY COLUMN INSTANCE_NAME varchar(191) NOT NULL,
								    MODIFY COLUMN JOB_NAME varchar(191) DEFAULT NULL,
								    MODIFY COLUMN JOB_GROUP varchar(191) DEFAULT NULL;
ALTER TABLE lams_qtz_JOB_DETAILS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								 MODIFY COLUMN JOB_NAME varchar(191) NOT NULL,
								 MODIFY COLUMN JOB_GROUP varchar(191) NOT NULL;
ALTER TABLE lams_qtz_LOCKS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_qtz_PAUSED_TRIGGER_GRPS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								   		 MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL;
ALTER TABLE lams_qtz_SCHEDULER_STATE CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
		   						     MODIFY COLUMN INSTANCE_NAME varchar(191) NOT NULL;
ALTER TABLE lams_qtz_SIMPLE_TRIGGERS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								     MODIFY COLUMN TRIGGER_NAME varchar(191) NOT NULL,
								     MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL;
ALTER TABLE lams_qtz_SIMPROP_TRIGGERS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								      MODIFY COLUMN TRIGGER_NAME varchar(191) NOT NULL,
								      MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL;
ALTER TABLE lams_qtz_TRIGGERS CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
							  MODIFY COLUMN TRIGGER_NAME varchar(191) NOT NULL,
							  MODIFY COLUMN TRIGGER_GROUP varchar(191) NOT NULL,
							  MODIFY COLUMN JOB_NAME varchar(191) NOT NULL,
							  MODIFY COLUMN JOB_GROUP varchar(191) NOT NULL,
							  MODIFY COLUMN CALENDAR_NAME varchar(191) DEFAULT NULL;
ALTER TABLE lams_rating CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_rating_comment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_rating_criteria CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_rating_criteria_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_registration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_role_privilege CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_signup_organisation CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
									 MODIFY COLUMN context VARCHAR(191) NOT NULL;
ALTER TABLE lams_supported_locale CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_system_tool CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_text_search_condition CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_theme CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_timezone CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_tool CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
					  MODIFY COLUMN service_name VARCHAR(191) NOT NULL;
ALTER TABLE lams_tool_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_tool_import_support CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_tool_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_tool_session_state CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_tool_session_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
					  MODIFY COLUMN login VARCHAR(191) NOT NULL;
ALTER TABLE lams_user_disabled_tutorials CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_user_group CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_user_organisation CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_user_organisation_collapsed CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_user_organisation_group CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_user_organisation_role CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_wkspc_fld_content_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_wkspc_wkspc_folder CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_workspace CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_workspace_folder CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_workspace_folder_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE lams_workspace_folder_type CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE patches CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_condition CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_condition_expression CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_eadventure CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_item_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_param CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_eueadv10_var CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_assessment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_assessment_overall_feedback CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_assessment_question CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_assessment_result CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_assessment_unit CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_option_answer CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_question_option CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_question_reference CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_question_result CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laasse10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_labbb10_bbb CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_labbb10_config CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_labbb10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_labbb10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lachat11_chat CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lachat11_conditions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lachat11_message CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lachat11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lachat11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_ladaco10_answer_options CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_ladaco10_answers CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_ladaco10_contents CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_ladaco10_questions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_ladaco10_sessions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_ladaco10_users CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_attachment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_condition_topics CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_conditions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_forum CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_forum_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_message CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_message_rating CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_message_seq CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_report CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_timestamp CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lafrum11_tool_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lagmap10_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lagmap10_gmap CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lagmap10_marker CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lagmap10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lagmap10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_image_vote CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_imagegallery CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_imagegallery_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_item_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimag10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_commoncartridge CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_commoncartridge_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_item_instruction CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_item_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laimsc11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_comment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_item_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_kaltura CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_kaltura_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_rating CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lakalt11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lalead11_leaderselection CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lalead11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lalead11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamc11_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamc11_options_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamc11_que_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamc11_que_usr CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamc11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamc11_usr_attempt CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamind10_mindmap CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamind10_node CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamind10_request CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamind10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lamind10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lanb11_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lanb11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lanb11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lantbk11_conditions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lantbk11_notebook CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lantbk11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lantbk11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lapixl10_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lapixl10_pixlr CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lapixl10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lapixl10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_condition_questions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_conditions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_que_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_que_usr CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_usr_resp CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_wizard_category CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_wizard_cognitive_skill CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_laqa11_wizard_question CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_larsrc11_item_instruction CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_larsrc11_item_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_larsrc11_resource CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_larsrc11_resource_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_larsrc11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_larsrc11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasbmt11_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasbmt11_report CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasbmt11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasbmt11_submission_details CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasbmt11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrb11_heading CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrb11_report_entry CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrb11_scribe CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrb11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrb11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_answer_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_burning_question CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_scratchie CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_scratchie_answer CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_scratchie_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lascrt11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasprd10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasprd10_spreadsheet CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasprd10_spreadsheet_mark CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasprd10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasprd10_user_modified_spreadsheet CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_answer CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_condition_questions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_conditions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_option CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_question CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_survey CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lasurv11_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_condition CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_condition_tl_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_item_attachment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_item_comment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_item_log CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_tasklist CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_tasklist_item CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_latask10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_comment CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_conditions CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_rating CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_recording CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lavidr10_videorecorder CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lavote11_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lavote11_nomination_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lavote11_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lavote11_usr CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lavote11_usr_attempt CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lawiki10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lawiki10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lawiki10_wiki CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE tl_lawiki10_wiki_page CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
								  MODIFY COLUMN title VARCHAR(191) NOT NULL;
ALTER TABLE tl_lawiki10_wiki_page_content CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lawook10_configuration CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lawook10_session CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lawook10_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ALTER TABLE tl_lawook10_wookie CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Clean up after the major changes.
OPTIMIZE TABLE lams_activity_category;
OPTIMIZE TABLE lams_activity_evaluation;
OPTIMIZE TABLE lams_auth_method_type;
OPTIMIZE TABLE lams_authentication_method;
OPTIMIZE TABLE lams_branch_activity_entry;
OPTIMIZE TABLE lams_branch_condition;
OPTIMIZE TABLE lams_competence;
OPTIMIZE TABLE lams_competence_mapping;
OPTIMIZE TABLE lams_configuration;
OPTIMIZE TABLE lams_copy_type;
OPTIMIZE TABLE lams_cr_credential;
OPTIMIZE TABLE lams_cr_node;
OPTIMIZE TABLE lams_cr_node_version;
OPTIMIZE TABLE lams_cr_node_version_property;
OPTIMIZE TABLE lams_cr_workspace;
OPTIMIZE TABLE lams_cr_workspace_credential;
OPTIMIZE TABLE lams_data_flow;
OPTIMIZE TABLE lams_event_subscriptions;
OPTIMIZE TABLE lams_events;
OPTIMIZE TABLE lams_ext_course_class_map;
OPTIMIZE TABLE lams_ext_server_lesson_map;
OPTIMIZE TABLE lams_ext_server_org_map;
OPTIMIZE TABLE lams_ext_server_tool_map;
OPTIMIZE TABLE lams_ext_user_userid_map;
OPTIMIZE TABLE lams_gate_activity_level;
OPTIMIZE TABLE lams_gate_allowed_learners;
OPTIMIZE TABLE lams_gradebook_user_activity;
OPTIMIZE TABLE lams_gradebook_user_lesson;
OPTIMIZE TABLE lams_group;
OPTIMIZE TABLE lams_grouping;
OPTIMIZE TABLE lams_grouping_support_type;
OPTIMIZE TABLE lams_grouping_type;
OPTIMIZE TABLE lams_input_activity;
OPTIMIZE TABLE lams_learner_progress;
OPTIMIZE TABLE lams_learning_activity;
OPTIMIZE TABLE lams_learning_activity_type;
OPTIMIZE TABLE lams_learning_design;
OPTIMIZE TABLE lams_learning_design_access;
OPTIMIZE TABLE lams_learning_design_annotation;
OPTIMIZE TABLE lams_learning_library;
OPTIMIZE TABLE lams_learning_library_group;
OPTIMIZE TABLE lams_learning_library_to_group;
OPTIMIZE TABLE lams_learning_transition;
OPTIMIZE TABLE lams_lesson;
OPTIMIZE TABLE lams_lesson_dependency;
OPTIMIZE TABLE lams_lesson_state;
OPTIMIZE TABLE lams_license;
OPTIMIZE TABLE lams_log_event;
OPTIMIZE TABLE lams_log_event_type;
OPTIMIZE TABLE lams_notebook_entry;
OPTIMIZE TABLE lams_openid_config;
OPTIMIZE TABLE lams_organisation;
OPTIMIZE TABLE lams_organisation_group;
OPTIMIZE TABLE lams_organisation_grouping;
OPTIMIZE TABLE lams_organisation_state;
OPTIMIZE TABLE lams_organisation_type;
OPTIMIZE TABLE lams_password_request;
OPTIMIZE TABLE lams_planner_activity_metadata;
OPTIMIZE TABLE lams_planner_node_role;
OPTIMIZE TABLE lams_planner_nodes;
OPTIMIZE TABLE lams_planner_recent_learning_designs;
OPTIMIZE TABLE lams_presence_chat_msgs;
OPTIMIZE TABLE lams_presence_user;
OPTIMIZE TABLE lams_privilege;
OPTIMIZE TABLE lams_progress_attempted;
OPTIMIZE TABLE lams_progress_completed;
OPTIMIZE TABLE lams_qtz_blob_triggers;
OPTIMIZE TABLE lams_qtz_calendars;
OPTIMIZE TABLE lams_qtz_cron_triggers;
OPTIMIZE TABLE lams_qtz_fired_triggers;
OPTIMIZE TABLE lams_qtz_job_details;
OPTIMIZE TABLE lams_qtz_locks;
OPTIMIZE TABLE lams_qtz_paused_trigger_grps;
OPTIMIZE TABLE lams_qtz_scheduler_state;
OPTIMIZE TABLE lams_qtz_simple_triggers;
OPTIMIZE TABLE lams_qtz_simprop_triggers;
OPTIMIZE TABLE lams_qtz_triggers;
OPTIMIZE TABLE lams_rating;
OPTIMIZE TABLE lams_rating_comment;
OPTIMIZE TABLE lams_rating_criteria;
OPTIMIZE TABLE lams_rating_criteria_type;
OPTIMIZE TABLE lams_registration;
OPTIMIZE TABLE lams_role;
OPTIMIZE TABLE lams_role_privilege;
OPTIMIZE TABLE lams_signup_organisation;
OPTIMIZE TABLE lams_supported_locale;
OPTIMIZE TABLE lams_system_tool;
OPTIMIZE TABLE lams_text_search_condition;
OPTIMIZE TABLE lams_theme;
OPTIMIZE TABLE lams_timezone;
OPTIMIZE TABLE lams_tool;
OPTIMIZE TABLE lams_tool_content;
OPTIMIZE TABLE lams_tool_import_support;
OPTIMIZE TABLE lams_tool_session;
OPTIMIZE TABLE lams_tool_session_state;
OPTIMIZE TABLE lams_tool_session_type;
OPTIMIZE TABLE lams_user;
OPTIMIZE TABLE lams_user_disabled_tutorials;
OPTIMIZE TABLE lams_user_group;
OPTIMIZE TABLE lams_user_organisation;
OPTIMIZE TABLE lams_user_organisation_collapsed;
OPTIMIZE TABLE lams_user_organisation_group;
OPTIMIZE TABLE lams_user_organisation_role;
OPTIMIZE TABLE lams_wkspc_fld_content_type;
OPTIMIZE TABLE lams_wkspc_wkspc_folder;
OPTIMIZE TABLE lams_workspace;
OPTIMIZE TABLE lams_workspace_folder;
OPTIMIZE TABLE lams_workspace_folder_content;
OPTIMIZE TABLE lams_workspace_folder_type;
OPTIMIZE TABLE patches;
OPTIMIZE TABLE tl_eueadv10_condition;
OPTIMIZE TABLE tl_eueadv10_condition_expression;
OPTIMIZE TABLE tl_eueadv10_eadventure;
OPTIMIZE TABLE tl_eueadv10_item_log;
OPTIMIZE TABLE tl_eueadv10_param;
OPTIMIZE TABLE tl_eueadv10_session;
OPTIMIZE TABLE tl_eueadv10_user;
OPTIMIZE TABLE tl_eueadv10_var;
OPTIMIZE TABLE tl_laasse10_assessment;
OPTIMIZE TABLE tl_laasse10_assessment_overall_feedback;
OPTIMIZE TABLE tl_laasse10_assessment_question;
OPTIMIZE TABLE tl_laasse10_assessment_result;
OPTIMIZE TABLE tl_laasse10_assessment_unit;
OPTIMIZE TABLE tl_laasse10_option_answer;
OPTIMIZE TABLE tl_laasse10_question_option;
OPTIMIZE TABLE tl_laasse10_question_reference;
OPTIMIZE TABLE tl_laasse10_question_result;
OPTIMIZE TABLE tl_laasse10_session;
OPTIMIZE TABLE tl_laasse10_user;
OPTIMIZE TABLE tl_labbb10_bbb;
OPTIMIZE TABLE tl_labbb10_config;
OPTIMIZE TABLE tl_labbb10_session;
OPTIMIZE TABLE tl_labbb10_user;
OPTIMIZE TABLE tl_lachat11_chat;
OPTIMIZE TABLE tl_lachat11_conditions;
OPTIMIZE TABLE tl_lachat11_message;
OPTIMIZE TABLE tl_lachat11_session;
OPTIMIZE TABLE tl_lachat11_user;
OPTIMIZE TABLE tl_ladaco10_answer_options;
OPTIMIZE TABLE tl_ladaco10_answers;
OPTIMIZE TABLE tl_ladaco10_contents;
OPTIMIZE TABLE tl_ladaco10_questions;
OPTIMIZE TABLE tl_ladaco10_sessions;
OPTIMIZE TABLE tl_ladaco10_users;
OPTIMIZE TABLE tl_lafrum11_attachment;
OPTIMIZE TABLE tl_lafrum11_condition_topics;
OPTIMIZE TABLE tl_lafrum11_conditions;
OPTIMIZE TABLE tl_lafrum11_forum;
OPTIMIZE TABLE tl_lafrum11_forum_user;
OPTIMIZE TABLE tl_lafrum11_message;
OPTIMIZE TABLE tl_lafrum11_message_rating;
OPTIMIZE TABLE tl_lafrum11_message_seq;
OPTIMIZE TABLE tl_lafrum11_report;
OPTIMIZE TABLE tl_lafrum11_timestamp;
OPTIMIZE TABLE tl_lafrum11_tool_session;
OPTIMIZE TABLE tl_lagmap10_configuration;
OPTIMIZE TABLE tl_lagmap10_gmap;
OPTIMIZE TABLE tl_lagmap10_marker;
OPTIMIZE TABLE tl_lagmap10_session;
OPTIMIZE TABLE tl_lagmap10_user;
OPTIMIZE TABLE tl_laimag10_configuration;
OPTIMIZE TABLE tl_laimag10_image_vote;
OPTIMIZE TABLE tl_laimag10_imagegallery;
OPTIMIZE TABLE tl_laimag10_imagegallery_item;
OPTIMIZE TABLE tl_laimag10_item_log;
OPTIMIZE TABLE tl_laimag10_session;
OPTIMIZE TABLE tl_laimag10_user;
OPTIMIZE TABLE tl_laimsc11_commoncartridge;
OPTIMIZE TABLE tl_laimsc11_commoncartridge_item;
OPTIMIZE TABLE tl_laimsc11_configuration;
OPTIMIZE TABLE tl_laimsc11_item_instruction;
OPTIMIZE TABLE tl_laimsc11_item_log;
OPTIMIZE TABLE tl_laimsc11_session;
OPTIMIZE TABLE tl_laimsc11_user;
OPTIMIZE TABLE tl_lakalt11_comment;
OPTIMIZE TABLE tl_lakalt11_item_log;
OPTIMIZE TABLE tl_lakalt11_kaltura;
OPTIMIZE TABLE tl_lakalt11_kaltura_item;
OPTIMIZE TABLE tl_lakalt11_rating;
OPTIMIZE TABLE tl_lakalt11_session;
OPTIMIZE TABLE tl_lakalt11_user;
OPTIMIZE TABLE tl_lalead11_leaderselection;
OPTIMIZE TABLE tl_lalead11_session;
OPTIMIZE TABLE tl_lalead11_user;
OPTIMIZE TABLE tl_lamc11_content;
OPTIMIZE TABLE tl_lamc11_options_content;
OPTIMIZE TABLE tl_lamc11_que_content;
OPTIMIZE TABLE tl_lamc11_que_usr;
OPTIMIZE TABLE tl_lamc11_session;
OPTIMIZE TABLE tl_lamc11_usr_attempt;
OPTIMIZE TABLE tl_lamind10_mindmap;
OPTIMIZE TABLE tl_lamind10_node;
OPTIMIZE TABLE tl_lamind10_request;
OPTIMIZE TABLE tl_lamind10_session;
OPTIMIZE TABLE tl_lamind10_user;
OPTIMIZE TABLE tl_lanb11_content;
OPTIMIZE TABLE tl_lanb11_session;
OPTIMIZE TABLE tl_lanb11_user;
OPTIMIZE TABLE tl_lantbk11_conditions;
OPTIMIZE TABLE tl_lantbk11_notebook;
OPTIMIZE TABLE tl_lantbk11_session;
OPTIMIZE TABLE tl_lantbk11_user;
OPTIMIZE TABLE tl_lapixl10_configuration;
OPTIMIZE TABLE tl_lapixl10_pixlr;
OPTIMIZE TABLE tl_lapixl10_session;
OPTIMIZE TABLE tl_lapixl10_user;
OPTIMIZE TABLE tl_laqa11_condition_questions;
OPTIMIZE TABLE tl_laqa11_conditions;
OPTIMIZE TABLE tl_laqa11_configuration;
OPTIMIZE TABLE tl_laqa11_content;
OPTIMIZE TABLE tl_laqa11_que_content;
OPTIMIZE TABLE tl_laqa11_que_usr;
OPTIMIZE TABLE tl_laqa11_session;
OPTIMIZE TABLE tl_laqa11_usr_resp;
OPTIMIZE TABLE tl_laqa11_wizard_category;
OPTIMIZE TABLE tl_laqa11_wizard_cognitive_skill;
OPTIMIZE TABLE tl_laqa11_wizard_question;
OPTIMIZE TABLE tl_larsrc11_item_instruction;
OPTIMIZE TABLE tl_larsrc11_item_log;
OPTIMIZE TABLE tl_larsrc11_resource;
OPTIMIZE TABLE tl_larsrc11_resource_item;
OPTIMIZE TABLE tl_larsrc11_session;
OPTIMIZE TABLE tl_larsrc11_user;
OPTIMIZE TABLE tl_lasbmt11_content;
OPTIMIZE TABLE tl_lasbmt11_report;
OPTIMIZE TABLE tl_lasbmt11_session;
OPTIMIZE TABLE tl_lasbmt11_submission_details;
OPTIMIZE TABLE tl_lasbmt11_user;
OPTIMIZE TABLE tl_lascrb11_heading;
OPTIMIZE TABLE tl_lascrb11_report_entry;
OPTIMIZE TABLE tl_lascrb11_scribe;
OPTIMIZE TABLE tl_lascrb11_session;
OPTIMIZE TABLE tl_lascrb11_user;
OPTIMIZE TABLE tl_lascrt11_answer_log;
OPTIMIZE TABLE tl_lascrt11_burning_question;
OPTIMIZE TABLE tl_lascrt11_configuration;
OPTIMIZE TABLE tl_lascrt11_scratchie;
OPTIMIZE TABLE tl_lascrt11_scratchie_answer;
OPTIMIZE TABLE tl_lascrt11_scratchie_item;
OPTIMIZE TABLE tl_lascrt11_session;
OPTIMIZE TABLE tl_lascrt11_user;
OPTIMIZE TABLE tl_lasprd10_session;
OPTIMIZE TABLE tl_lasprd10_spreadsheet;
OPTIMIZE TABLE tl_lasprd10_spreadsheet_mark;
OPTIMIZE TABLE tl_lasprd10_user;
OPTIMIZE TABLE tl_lasprd10_user_modified_spreadsheet;
OPTIMIZE TABLE tl_lasurv11_answer;
OPTIMIZE TABLE tl_lasurv11_condition_questions;
OPTIMIZE TABLE tl_lasurv11_conditions;
OPTIMIZE TABLE tl_lasurv11_option;
OPTIMIZE TABLE tl_lasurv11_question;
OPTIMIZE TABLE tl_lasurv11_session;
OPTIMIZE TABLE tl_lasurv11_survey;
OPTIMIZE TABLE tl_lasurv11_user;
OPTIMIZE TABLE tl_latask10_condition;
OPTIMIZE TABLE tl_latask10_condition_tl_item;
OPTIMIZE TABLE tl_latask10_item_attachment;
OPTIMIZE TABLE tl_latask10_item_comment;
OPTIMIZE TABLE tl_latask10_item_log;
OPTIMIZE TABLE tl_latask10_session;
OPTIMIZE TABLE tl_latask10_tasklist;
OPTIMIZE TABLE tl_latask10_tasklist_item;
OPTIMIZE TABLE tl_latask10_user;
OPTIMIZE TABLE tl_lavidr10_comment;
OPTIMIZE TABLE tl_lavidr10_conditions;
OPTIMIZE TABLE tl_lavidr10_rating;
OPTIMIZE TABLE tl_lavidr10_recording;
OPTIMIZE TABLE tl_lavidr10_session;
OPTIMIZE TABLE tl_lavidr10_user;
OPTIMIZE TABLE tl_lavidr10_videorecorder;
OPTIMIZE TABLE tl_lavote11_content;
OPTIMIZE TABLE tl_lavote11_nomination_content;
OPTIMIZE TABLE tl_lavote11_session;
OPTIMIZE TABLE tl_lavote11_usr;
OPTIMIZE TABLE tl_lavote11_usr_attempt;
OPTIMIZE TABLE tl_lawiki10_session;
OPTIMIZE TABLE tl_lawiki10_user;
OPTIMIZE TABLE tl_lawiki10_wiki;
OPTIMIZE TABLE tl_lawiki10_wiki_page;
OPTIMIZE TABLE tl_lawiki10_wiki_page_content;
OPTIMIZE TABLE tl_lawook10_configuration;
OPTIMIZE TABLE tl_lawook10_session;
OPTIMIZE TABLE tl_lawook10_user;
OPTIMIZE TABLE tl_lawook10_wookie;



-- LDEV-3631 	Simple Commenting Widget
CREATE TABLE IF NOT EXISTS lams_comment_session (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `external_id` bigint(20) DEFAULT NULL,
  `external_id_type` int(1) DEFAULT NULL,
  `external_signature` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `comment_ext_sig_user` (`external_id`,`external_id_type`,`external_signature`)
);

CREATE TABLE IF NOT EXISTS lams_comment (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_id` bigint(20) NOT NULL,
  `body` text DEFAULT NULL,
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
  PRIMARY KEY (`uid`),
  KEY `FK_comment_session` (`session_id`),
  KEY `FK_comment_create` (`create_by`),
  KEY `FK_comment_modify` (`update_by`),
  KEY `FK_comment_parent` (`parent_uid`),
  KEY `FK_comment_root` (`root_comment_uid`),
  KEY `FK_comment_thread` (`thread_comment_uid`),
  CONSTRAINT `FK_comment_session` FOREIGN KEY (`session_id`) REFERENCES `lams_comment_session` (`uid`),
  CONSTRAINT `FK_comment_create` FOREIGN KEY (`create_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_comment_modify` FOREIGN KEY (`update_by`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_comment_parent` FOREIGN KEY (`parent_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_comment_root` FOREIGN KEY (`root_comment_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_comment_thread` FOREIGN KEY (`thread_comment_uid`) REFERENCES `lams_comment` (`uid`)
);

CREATE TABLE IF NOT EXISTS lams_comment_likes (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `vote` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE INDEX `comment_like_unique` (`comment_uid`, `user_id`),
  KEY `FK_commentlike_comment` (`comment_uid`),
  KEY `FK_commentlike_user` (`user_id`),
  CONSTRAINT `FK_commentlike_comment` FOREIGN KEY (`comment_uid`) REFERENCES `lams_comment` (`uid`),
  CONSTRAINT `FK_commentlike_user` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
);



-- LDEV-3661 Remove internal SMTP server
DELETE FROM lams_configuration WHERE config_key = 'InternalSMTPServer';

-- clean up Event Notifications tables
DELETE FROM lams_events WHERE fail_time IS NOT NULL;

ALTER TABLE lams_event_subscriptions RENAME lams_notification_subscription;
ALTER TABLE lams_events RENAME lams_notification_event,
						DROP COLUMN triggered,
						DROP COLUMN default_subject,
						DROP COLUMN default_message;

						

-- LDEV-3660 Remove export portfolio columns
ALTER TABLE lams_tool DROP COLUMN export_pfolio_learner_url,
					  DROP COLUMN export_pfolio_class_url;
					  
ALTER TABLE lams_system_tool DROP COLUMN export_pfolio_learner_url,
					  		 DROP COLUMN export_pfolio_class_url;
					  		 
ALTER TABLE lams_lesson DROP COLUMN learner_exportport_avail;

ALTER TABLE lams_organisation DROP COLUMN enable_export_portfolio;



-- LDEV-3707 Use Flashless chosen grouping Monitoring interface
UPDATE lams_system_tool SET contribute_url = 'monitoring/grouping.do?method=startGrouping' WHERE tool_display_name = 'Monitor Chosen Branching';



-- LDEV-3674 Remove Flash themes
ALTER TABLE lams_user DROP FOREIGN KEY FK_lams_user_4,
					  DROP COLUMN flash_theme_id,
					  CHANGE COLUMN html_theme_id theme_id bigint(20);
DELETE FROM lams_theme WHERE theme_type = 2;
ALTER TABLE lams_theme DROP COLUMN theme_type;
					  
-- Use SVG icons by default
UPDATE lams_learning_activity SET library_activity_ui_image = REPLACE(library_activity_ui_image,'.swf','.svg');

-- No more Flash dumps and themes
DELETE FROM lams_configuration WHERE config_key IN ('DumpDir', 'DefaultFlashTheme');
UPDATE lams_configuration SET config_key = 'DefaultTheme' WHERE config_key = 'DefaultHTMLTheme';



-- LDEV-3749 Remove OpenId DB stuff
DROP TABLE lams_openid_config;




-- LDEV-2949 Remove obsolete column
ALTER TABLE lams_user DROP COLUMN chat_id;




-- LDEV-3661 Remove obsolete columns
ALTER TABLE lams_notification_subscription DROP COLUMN periodicity,
										   DROP COLUMN last_operation_time;

										   
										   

-- LDEV-3756 Noticeboard comments: set thread to top
ALTER TABLE lams_comment 
ADD COLUMN sticky SMALLINT(6) NULL DEFAULT 0,
ADD INDEX IX_comment_level_sticky (comment_level ASC, sticky ASC);




-- LDEV-3773 Get rid of Workspaces. Only use Workspace Folders.
ALTER TABLE lams_user DROP FOREIGN KEY FK_lams_user_2,
					  CHANGE COLUMN workspace_id workspace_folder_id BIGINT(20);
UPDATE lams_user AS u JOIN lams_workspace AS w ON u.workspace_folder_id = w.workspace_id
	SET u.workspace_folder_id = w.default_fld_id;
ALTER TABLE lams_user ADD CONSTRAINT FK_lams_user_2 FOREIGN KEY (workspace_folder_id)
                  REFERENCES lams_workspace_folder (workspace_folder_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lams_workspace_folder ADD COLUMN organisation_id BIGINT(20) AFTER user_id;
UPDATE lams_workspace_folder AS f
	JOIN lams_workspace AS w ON f.workspace_folder_id = w.default_fld_id OR f.workspace_folder_id = w.def_run_seq_fld_id
	JOIN lams_organisation AS o USING (workspace_id)
	SET f.organisation_id = o.organisation_id;
ALTER TABLE lams_organisation DROP FOREIGN KEY FK_lams_organisation_2,
							  DROP COLUMN workspace_id;

DROP TABLE lams_wkspc_wkspc_folder,
		   lams_workspace;

-- additional clean up
ALTER TABLE lams_workspace_folder_content DROP FOREIGN KEY FK_lams_workspace_folder_content_2;
DROP TABLE lams_wkspc_fld_content_type;
       



--  LDEV-3826 Cognitive Skills Wizard in Q and A has been removed in Bootstrap so the admin screen is no longer needed.
UPDATE lams_tool 
SET admin_url = null
WHERE tool_signature = 'laqa11';




-- LDEV-3839: Lesson restart

-- Tables for archiving learner progress
CREATE TABLE lams_learner_progress_archive (
  learner_progress_id bigint(20) NOT NULL AUTO_INCREMENT,
  user_id bigint(20) NOT NULL,
  lesson_id bigint(20) NOT NULL,
  attempt_id TINYINT NOT NULL DEFAULT 1,
  lesson_completed_flag tinyint(1) NOT NULL DEFAULT 0,
  start_date_time datetime NOT NULL,
  finish_date_time datetime,
  current_activity_id bigint(20),
  PRIMARY KEY (learner_progress_id),
  UNIQUE KEY IX_lams_learner_progress_archive_1 (user_id, lesson_id, attempt_id),
  CONSTRAINT FK_lams_learner_progress_archive_1 FOREIGN KEY (user_id)
  	REFERENCES lams_user (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_learner_progress_archive_2 FOREIGN KEY (lesson_id)
  	REFERENCES lams_lesson (lesson_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_learner_progress_archive_3 FOREIGN KEY (current_activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET utf8mb4;


CREATE TABLE lams_progress_attempted_archive (
  learner_progress_id bigint(20) NOT NULL,
  activity_id bigint(20) NOT NULL,
  start_date_time datetime,
  PRIMARY KEY (learner_progress_id, activity_id),
  CONSTRAINT FK_lams_progress_current_archive_1 FOREIGN KEY (learner_progress_id)
  	REFERENCES lams_learner_progress_archive (learner_progress_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_progress_current_archive_2 FOREIGN KEY (activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET utf8mb4;


CREATE TABLE lams_progress_completed_archive (
  learner_progress_id bigint(20) NOT NULL,
  activity_id bigint(20) NOT NULL,
  completed_date_time datetime,
  start_date_time datetime,
  PRIMARY KEY (learner_progress_id,activity_id),
  CONSTRAINT FK_lams_progress_completed_archive_1 FOREIGN KEY (learner_progress_id)
  	REFERENCES lams_learner_progress_archive (learner_progress_id) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_lams_progress_completed_archive_2 FOREIGN KEY (activity_id)
  	REFERENCES lams_learning_activity (activity_id) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- make restart an option
ALTER TABLE lams_lesson CHANGE COLUMN learner_restart force_restart tinyint(1) DEFAULT '0',
						ADD COLUMN allow_restart tinyint(1) DEFAULT '0';


						
						
						

-- LDEV-3674 Remove Flash themes
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'AuthoringScreenSize';
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'LearnerScreenSize';
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'MonitorScreenSize';
UPDATE lams_configuration SET config_value = '1280x720' WHERE config_key = 'AdminScreenSize';




-- LDEV-3765 Create a table for storing commands sent to Learners
-- No foreign keys as maybe in the future it will be possible to send commands to a learner in any lesson
-- or to all learners in a given lesson.

CREATE TABLE lams_learning_command (
	uid BIGINT(20) NOT NULL AUTO_INCREMENT,
	lesson_id BIGINT(20),
	user_name VARCHAR(191),
	create_date DATETIME NOT NULL,
	command_text TEXT,
	PRIMARY KEY (uid),
	INDEX idx_lesson_id (lesson_id),
	INDEX idx_user_name (user_name),
	INDEX idx_create_date (create_date)
)ENGINE=InnoDB;




-- LDEV-3965 Ability to enable two-factor authentication for selected users
ALTER TABLE lams_user ADD COLUMN two_factor_auth_enabled TINYINT(1) DEFAULT 0 AFTER salt;
ALTER TABLE lams_user ADD COLUMN two_factor_auth_secret CHAR(64) AFTER two_factor_auth_enabled;



-- LDEV-3292 Add indexes, as prompted by a client
ALTER TABLE tl_lafrum11_forum_user ADD INDEX idx_user_id (user_id);
ALTER TABLE lams_notebook_entry ADD INDEX idx_create_date (create_date);




-- LDEV-3997 LAMS to become LTI compliant tool provider (version 1.1)
CREATE TABLE lams_ext_server_type (
       server_type_id INT(11) NOT NULL DEFAULT 0
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (server_type_id)
)ENGINE=InnoDB;

INSERT INTO lams_ext_server_type VALUES (1, 'INTEGRATED SERVER');
INSERT INTO lams_ext_server_type VALUES (2, 'LTI TOOL CONSUMER');

ALTER TABLE lams_ext_server_org_map ADD COLUMN server_type_id INT(11) NOT NULL DEFAULT 1, ADD CONSTRAINT FK_lams_ext_server_type FOREIGN KEY (server_type_id) REFERENCES lams_ext_server_type(server_type_id);
UPDATE lams_ext_server_org_map SET server_type_id=1;

ALTER TABLE lams_ext_server_lesson_map ADD COLUMN resource_link_id VARCHAR(255);
ALTER TABLE lams_ext_user_userid_map ADD COLUMN tc_gradebook_id VARCHAR(250);

-- LDEV-3998 Remove reference to Organisation from ExtServerOrgMap class
ALTER TABLE lams_ext_server_org_map DROP FOREIGN KEY lams_ext_server_org_map_fk;
ALTER TABLE lams_ext_server_org_map DROP COLUMN orgid, DROP INDEX orgid;



--  LDEV-3219 Adding option to turn validation off for user details
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('SiteName','LAMS', 'config.site.name', 'config.header.system', 'STRING', 1);




--     LDEV-3767 Peer review: two new review methods
ALTER TABLE lams_rating_criteria ADD COLUMN rating_style BIGINT(20) NOT NULL DEFAULT 1;
ALTER TABLE lams_rating_criteria ADD COLUMN max_rating BIGINT(20) NOT NULL DEFAULT 5;
ALTER TABLE lams_rating_criteria ADD COLUMN minimum_rates INT(11) DEFAULT '0';
ALTER TABLE lams_rating_criteria ADD COLUMN maximum_rates INT(11) DEFAULT '0';
  
UPDATE lams_rating_criteria SET rating_style = 0 WHERE comments_enabled = 1;
UPDATE lams_rating_criteria SET title = "Comment" WHERE comments_enabled = 1 AND title is null;




--  LDEV-4023 Implement password policy 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyMinChars','8', 'config.password.minimum.characters', 'config.header.password.policy', 'LONG', 1);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyUppercase','true', 'config.password.uppercase', 'config.header.password.policy', 'BOOLEAN', 0);
 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyLowercase','true', 'config.password.lowercase', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicyNumerics','true', 'config.password.numerics', 'config.header.password.policy', 'BOOLEAN', 0);

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('PasswordPolicySymbols','false', 'config.password.symbols', 'config.header.password.policy', 'BOOLEAN', 0);




-- LDEV-3962 Bootstrap index page
DROP TABLE lams_user_organisation_collapsed;

CREATE TABLE lams_favorite_organisation (
       favorite_organisation_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , organisation_id BIGINT(20) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , PRIMARY KEY (favorite_organisation_id)
     , INDEX (organisation_id)
     , CONSTRAINT FK_lams_favorite_organisation_1 FOREIGN KEY (organisation_id)
                  REFERENCES lams_organisation (organisation_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (user_id)
     , CONSTRAINT FK_lams_favorite_organisation_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB;

ALTER TABLE lams_user ADD COLUMN last_visited_organisation_id BIGINT(20), 
	ADD CONSTRAINT FK_lams_user_7 FOREIGN KEY (last_visited_organisation_id) REFERENCES lams_organisation(organisation_id);

	
	

-- LDEV-4049 Option for not displaying stacktraces in config settings

insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ErrorStackTrace','true', 'config.stacktrace.error', 'config.header.system', 'BOOLEAN', 0);




--    LDEV-3979 Enforce field character limitation in course creation.

ALTER TABLE lams_organisation MODIFY description text;




-- LDEV-4096 Add role management feature for LTI tool consumers
ALTER TABLE lams_ext_server_org_map ADD COLUMN lti_consumer_monitor_roles text DEFAULT NULL;




-- LDEV-4030 Disable login for a few minutes after X number of attempts
ALTER TABLE lams_user 
ADD COLUMN failed_attempts TINYINT  DEFAULT NULL,
ADD COLUMN lock_out_time DATETIME DEFAULT NULL;



 
--  LDEV-4023 Implement password policy 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('FailedAttempts','3', 'config.failed.attempts', 'config.header.password.policy', 'LONG', 1);


insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('LockOutTime','5', 'config.lock.out.time', 'config.header.password.policy', 'LONG', 1);



--  LDEV-4123 Theme management: remove old highcontrast
UPDATE lams_user SET theme_id = (SELECT theme_id FROM lams_theme WHERE name = 'defaultHTML')
WHERE theme_id = (SELECT theme_id FROM lams_theme WHERE name = 'highContrast');

DELETE FROM lams_theme WHERE name = 'highContrast';



-- LDEV-4145 Comments to have different background when posted via Monitor UI
ALTER TABLE lams_comment 
ADD COLUMN monitor SMALLINT(6) NULL DEFAULT 0;




-- LDEV-4097 Change lams_ext_user_userid_map's tc_gradebook_id to text
ALTER TABLE lams_ext_user_userid_map CHANGE COLUMN tc_gradebook_id tc_gradebook_id TEXT;



 
--  LDEV-4144 Timezone warning
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)
values ('ShowTimezoneWarning','true', 'config.show.timezone.warning', 'config.header.features', 'BOOLEAN', 1);




-- LDEV-3640 Add necessary cascades

ALTER TABLE lams_progress_attempted DROP FOREIGN KEY FK_lams_progress_current_1, DROP FOREIGN KEY FK_lams_progress_current_2;
ALTER TABLE lams_progress_attempted ADD CONSTRAINT FK_lams_progress_current_1 FOREIGN KEY (learner_progress_id)
			REFERENCES lams_learner_progress (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE,
  		ADD CONSTRAINT FK_lams_progress_current_2 FOREIGN KEY (activity_id)
  			REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE lams_progress_completed DROP FOREIGN KEY FK_lams_progress_completed_1, DROP FOREIGN KEY FK_lams_progress_completed_2;
ALTER TABLE lams_progress_completed ADD CONSTRAINT FK_lams_progress_completed_1 FOREIGN KEY (learner_progress_id)
			REFERENCES lams_learner_progress (learner_progress_id) ON DELETE CASCADE ON UPDATE CASCADE,
  		ADD CONSTRAINT FK_lams_progress_completed_2 FOREIGN KEY (activity_id)
  			REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE;


 
-- LDEV-4180 Update configuration to 3.0 release

update lams_configuration set config_value='3.0' where config_key='Version';
update lams_configuration set config_value='3.0.0.201701010000' where config_key='LearnerClientVersion' OR config_key='ServerVersionNumber' OR config_key='MonitorClientVersion' OR config_key='AuthoringClientVersion';
update lams_configuration set config_value='2017-01-01' where config_key='DictionaryDateCreated';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;