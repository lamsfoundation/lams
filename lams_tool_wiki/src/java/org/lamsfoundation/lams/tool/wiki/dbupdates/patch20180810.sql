-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;