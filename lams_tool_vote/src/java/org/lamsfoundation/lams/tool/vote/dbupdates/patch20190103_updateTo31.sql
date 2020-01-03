SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180425.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lavote11/authoring/start.do',
	learner_url = 'tool/lavote11/learning/start.do?mode=learner',
	learner_preview_url = 'tool/lavote11/learning/start.do?mode=author',
	learner_progress_url = 'tool/lavote11/learning/start.do?mode=teacher',
	monitor_url = 'tool/lavote11/monitoring/start.do',
	pedagogical_planner_url = 'tool/lavote11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lavote11';

UPDATE lams_tool SET tool_version='20171023' WHERE tool_signature='lavote11';


-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lavote11_content MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_lavote11_content MODIFY reflectionSubject MEDIUMTEXT;
ALTER TABLE tl_lavote11_nomination_content MODIFY nomination MEDIUMTEXT;


						    
-- Trigger class name rename when importing an old LD
UPDATE lams_tool SET tool_version='20181202' WHERE tool_signature='lavote11';



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lavote11';



-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;