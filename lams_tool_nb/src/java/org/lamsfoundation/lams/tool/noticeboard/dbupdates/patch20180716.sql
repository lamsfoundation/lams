-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;