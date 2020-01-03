SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180425.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_laqa11_content MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_laqa11_content MODIFY reflectionSubject MEDIUMTEXT;
ALTER TABLE tl_laqa11_que_content MODIFY question MEDIUMTEXT;
ALTER TABLE tl_laqa11_que_content MODIFY feedback MEDIUMTEXT;
ALTER TABLE tl_laqa11_usr_resp MODIFY answer MEDIUMTEXT;
ALTER TABLE tl_laqa11_usr_resp MODIFY answer_autosaved MEDIUMTEXT;


-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/laqa11/authoring/authoring.do',
	learner_url = 'tool/laqa11/learning/learning.do?mode=learner',
	learner_preview_url = 'tool/laqa11/learning/learning.do?mode=author',
	learner_progress_url = 'tool/laqa11/learning/learning.do?mode=teacher',
	monitor_url = 'tool/laqa11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/laqa11/pedagogicalPlanner/initPedagogicalPlannerForm.do',
	admin_url= 'tool/laqa11/admin.do' 
WHERE tool_signature = 'laqa11';



--  LDEV-4665 Remove Cognitive Skills Wizard
DROP TABLE tl_laqa11_wizard_category;
DROP TABLE tl_laqa11_wizard_cognitive_skill;
DROP TABLE tl_laqa11_wizard_question;
DROP TABLE tl_laqa11_configuration;
UPDATE lams_tool SET admin_url= NULL WHERE tool_signature = 'laqa11';


						    
-- Trigger class name rename when importing an old LD
UPDATE lams_tool SET tool_version='20181202' WHERE tool_signature='laqa11';



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='laqa11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;