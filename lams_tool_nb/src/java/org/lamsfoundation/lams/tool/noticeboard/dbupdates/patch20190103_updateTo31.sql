SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180425.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lanb11_content MODIFY content MEDIUMTEXT;
ALTER TABLE tl_lanb11_content MODIFY reflect_instructions MEDIUMTEXT;




-- LDEV-4559 Option to allow students to post anonymously
ALTER TABLE tl_lanb11_content ADD COLUMN allow_anonymous TINYINT(1) DEFAULT 0;




-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lanb11/authoring/authoring.do',
	learner_url = 'tool/lanb11/learning/learner.do',
	learner_preview_url = 'tool/lanb11/learning/author.do',
	learner_progress_url = 'tool/lanb11/learning/teacher.do',
	monitor_url = 'tool/lanb11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lanb11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lanb11';

UPDATE lams_tool SET tool_version='20180716' WHERE tool_signature='lanb11';



						    
-- Trigger class name rename when importing an old LD
UPDATE lams_tool SET tool_version='20181202' WHERE tool_signature='lanb11';



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lanb11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;