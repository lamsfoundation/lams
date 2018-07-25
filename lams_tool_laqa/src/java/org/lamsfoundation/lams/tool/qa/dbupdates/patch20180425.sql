-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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
	learner_url = 'tool/laqa11/learning/learning.do',
	monitor_url = 'tool/laqa11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/laqa11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'laqa11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;