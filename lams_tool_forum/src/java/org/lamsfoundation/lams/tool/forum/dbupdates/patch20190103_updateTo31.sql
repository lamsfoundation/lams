SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20170308.sql to patch20190121.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4261 Remove optimistic locking management
ALTER TABLE tl_lafrum11_tool_session DROP COLUMN version;



-- LDEV-4371 Add Forum admin page
CREATE TABLE tl_lafrum11_configuration (
  uid bigint(20) NOT NULL AUTO_INCREMENT,
  config_key VARCHAR(30),
  config_value VARCHAR(255),
  PRIMARY KEY (uid),
  UNIQUE KEY config_key (config_key)
);

INSERT INTO tl_lafrum11_configuration (config_key, config_value) VALUES ('keepLearnerContent',	'false');

UPDATE lams_tool SET admin_url='tool/lafrum11/admin/start.do' WHERE tool_signature='lafrum11';




-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lafrum11_forum MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_lafrum11_forum MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_lafrum11_message MODIFY body MEDIUMTEXT;
ALTER TABLE tl_lafrum11_report MODIFY comment MEDIUMTEXT;




-- LDEV-4603 Delete preview lessons with peer review activity doesn't work
ALTER TABLE tl_lafrum11_message_rating DROP FOREIGN KEY FK_tl_lafrum11_message_rating_2;
ALTER TABLE tl_lafrum11_message_rating ADD CONSTRAINT FK_tl_lafrum11_message_rating_2
  FOREIGN KEY (message_id)
  REFERENCES tl_lafrum11_message (uid)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

  
  
  
-- LDEV-4621 Release marks is now a state
ALTER TABLE tl_lafrum11_report DROP COLUMN release_date;




-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lafrum11/authoring/authoring.do',
	learner_url = 'tool/lafrum11/learning/viewForum.do?mode=learner',
	learner_preview_url = 'tool/lafrum11/learning/viewForum.do?mode=author',
	learner_progress_url = 'tool/lafrum11/learning/viewForum.do?mode=teacher',
	monitor_url = 'tool/lafrum11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lafrum11/authoring/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lafrum11';

UPDATE lams_tool SET tool_version='20180806' WHERE tool_signature='lafrum11';



						    
-- Trigger class name rename when importing an old LD
UPDATE lams_tool SET tool_version='20181202' WHERE tool_signature='lafrum11';



-- LDEV-4756 Unable to add attachments in Forum
UPDATE lams_cr_credential set name = "forum" where name = "forumw";



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lafrum11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;