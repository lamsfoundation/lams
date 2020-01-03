SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180517.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- Convert to mindmup. Color now #xxxxxx not just xxxxxx
ALTER TABLE tl_lamind10_node MODIFY node_color VARCHAR(7);
UPDATE tl_lamind10_node SET node_color = CONCAT('#',node_color) WHERE NOT node_color LIKE ('#%');



-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lamind10/authoring/authoring.do',
	learner_url = 'tool/lamind10/learning/learning.do?mode=learner',
	learner_preview_url = 'tool/lamind10/learning/learning.do?mode=author',
	learner_progress_url = 'tool/lamind10/learning/learning.do?mode=teacher',
	monitor_url = 'tool/lamind10/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lamind10/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lamind10';

UPDATE lams_tool SET tool_version='20180731' WHERE tool_signature='lamind10';



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lamind10';



-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;