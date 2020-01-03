SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180801.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lachat11/authoring/authoring.do',
	learner_url = 'tool/lachat11/learning/learning.do?mode=learner',
	learner_preview_url = 'tool/lachat11/learning/learning.do?mode=author',
	learner_progress_url = 'tool/lachat11/learning/learning.do?mode=teacher',
	monitor_url = 'tool/lachat11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lachat11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lachat11';

UPDATE lams_tool SET tool_version='20180801' WHERE tool_signature='lachat11';



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lachat11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;