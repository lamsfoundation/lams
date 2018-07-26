-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/laqa11/authoring/authoring.do',
	learner_url = 'tool/laqa11/learning/learning.do',
	monitor_url = 'tool/laqa11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/laqa11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'laqa11';