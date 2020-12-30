SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20171114.sql to patch20190128.sql
-- It should upgrade this tool to version 3.1

-- LDEV-4482


ALTER TABLE tl_lasbmt11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE tl_lasbmt11_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_lasbmt11_session ADD INDEX FK_lasbmt11_session_1 (group_leader_uid), ADD CONSTRAINT tl_lasbmt11_session FOREIGN KEY (group_leader_uid) REFERENCES tl_lasbmt11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the assessment user table so no same user_id and session_id can be repetead 
-- (Please, note there are some duplicate users exist (their session_uid is null), they got formed when techers created questions in authoring. It's required to keep track of questions' authors.) 
ALTER TABLE tl_lasbmt11_user ADD UNIQUE INDEX(user_id, session_id);
 



-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lasbmt11_content MODIFY instruction MEDIUMTEXT;
ALTER TABLE tl_lasbmt11_content MODIFY reflect_instructions MEDIUMTEXT;
ALTER TABLE tl_lasbmt11_report MODIFY comments MEDIUMTEXT;
ALTER TABLE tl_lasbmt11_submission_details MODIFY fileDescription MEDIUMTEXT;



-- LDEV-4611 Release marks is now a state
ALTER TABLE tl_lasbmt11_session ADD COLUMN marks_released TINYINT(1) DEFAULT 0;

UPDATE tl_lasbmt11_session as sessions, tl_lasbmt11_report as report, tl_lasbmt11_submission_details as details
		SET sessions.marks_released = 1 
		WHERE report.report_id = details.submission_id AND details.session_id=sessions.session_id AND report.date_marks_released IS NOT NULL;

ALTER TABLE tl_lasbmt11_report DROP COLUMN date_marks_released;




-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lasbmt11/authoring/authoring.do',
	learner_url = 'tool/lasbmt11/learning/learner.do',
	learner_preview_url = 'tool/lasbmt11/learning/author.do',
	learner_progress_url = 'tool/lasbmt11/learning/teacher.do',
	monitor_url = 'tool/lasbmt11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lasbmt11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lasbmt11';

UPDATE lams_tool SET tool_version='20180720' WHERE tool_signature='lasbmt11';



						    
-- Trigger class name rename when importing an old LD
UPDATE lams_tool SET tool_version='20181202' WHERE tool_signature='lasbmt11';




-- LDEV-4913 Include a minimum number of files to submit
ALTER TABLE tl_lasbmt11_content ADD COLUMN min_limit_upload_number bigint;



-- LDEV-4743 Update tool version to mark LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lasbmt11';

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;