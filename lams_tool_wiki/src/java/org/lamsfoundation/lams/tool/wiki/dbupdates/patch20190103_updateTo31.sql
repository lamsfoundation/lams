SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20171023.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lanb11_content MODIFY content MEDIUMTEXT;
ALTER TABLE tl_lanb11_content MODIFY reflect_instructions MEDIUMTEXT;


-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lawiki10/authoring/authoring.do',
	learner_url = 'tool/lawiki10/learning/learning.do?mode=learner',
	learner_preview_url = 'tool/lawiki10/learning/learning.do?mode=author',
	learner_progress_url = 'tool/lawiki10/learning/learning.do?mode=teacher',
	monitor_url = 'tool/lawiki10/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lawiki10/authoring/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lawiki10';

UPDATE lams_tool SET tool_version='20180810' WHERE tool_signature='lawiki10';



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lawiki10';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;