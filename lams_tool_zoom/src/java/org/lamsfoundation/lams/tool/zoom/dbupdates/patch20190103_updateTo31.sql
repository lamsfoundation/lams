SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20180917.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lazoom10/authoring/start.do',
	learner_url = 'tool/lazoom10/learning/start.do?mode=learner',
	learner_preview_url = 'tool/lazoom10/learning/start.do?mode=author',
	learner_progress_url = 'tool/lazoom10/learning/start.do?mode=teacher',
	monitor_url = 'tool/lazoom10/monitoring/start.do',
	admin_url = 'tool/lazoom10/admin/start.do'
WHERE tool_signature = 'lazoom10';

UPDATE lams_tool SET tool_version='20180917' WHERE tool_signature='lazoom10';



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lazoom10';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;