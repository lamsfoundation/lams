SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20171031.sql to patch20190103.sql
-- It should upgrade this tool to version 3.1


-- LDEV-4451 add 'Enable Confidence levels' advanced setting
ALTER TABLE tl_lamc11_content ADD COLUMN enable_confidence_levels TINYINT(1) NOT NULL DEFAULT 0;

--add questions' hashes in order to be able to search for the similar questions with the same title and question
ALTER TABLE tl_lamc11_que_content ADD COLUMN question_hash CHAR(40);
UPDATE tl_lamc11_que_content SET question_hash = SHA1(question);

--add confidence level property to tl_laasse10_question_result
ALTER TABLE tl_lamc11_usr_attempt ADD COLUMN confidence_level int;

UPDATE lams_tool SET tool_version='20171031' WHERE tool_signature='lamc11';



-- LDEV-4490 Fix NULL ConfidenceLevel columns creating exceptions
UPDATE tl_lamc11_usr_attempt SET confidence_level = 0 WHERE confidence_level IS NULL;

ALTER TABLE tl_lamc11_usr_attempt
CHANGE COLUMN confidence_level confidence_level INT(11) NOT NULL DEFAULT 0;


-- LDEV-4558 Increase db text size for all user inputs
ALTER TABLE tl_lamc11_content MODIFY instructions MEDIUMTEXT;
ALTER TABLE tl_lamc11_content MODIFY reflectionSubject MEDIUMTEXT;
ALTER TABLE tl_lamc11_options_content MODIFY mc_que_option_text MEDIUMTEXT;

ALTER TABLE tl_lamc11_que_content MODIFY question MEDIUMTEXT;
ALTER TABLE tl_lamc11_que_content MODIFY feedback MEDIUMTEXT;


-- LDEV-4577 Split feedback and marks in MCQ
ALTER TABLE tl_lamc11_content ADD COLUMN display_feedback_only TINYINT(1) NOT NULL DEFAULT 0;



-- LDEV-4440 Change tool access URLs after migration to Spring MVC
UPDATE lams_tool SET 
	author_url = 'tool/lamc11/authoring/authoring.do',
	learner_url = 'tool/lamc11/learning/learning.do?mode=learner',
	learner_preview_url = 'tool/lamc11/learning/learning.do?mode=author',
	learner_progress_url = 'tool/lamc11/learning/learning.do?mode=teacher',
	monitor_url = 'tool/lamc11/monitoring/monitoring.do',
	pedagogical_planner_url = 'tool/lamc11/pedagogicalPlanner/initPedagogicalPlannerForm.do'
WHERE tool_signature = 'lamc11';

UPDATE lams_tool SET tool_version='20180724' WHERE tool_signature='lamc11';



-- Trigger class name rename when importing an old LD
UPDATE lams_tool SET tool_version='20181202' WHERE tool_signature='lamc11';



-- LDEV-4743 Update tools version for LAMS 3.1 release
UPDATE lams_tool SET tool_version='20190103' WHERE tool_signature='lamc11';


-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;