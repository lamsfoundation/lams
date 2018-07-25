-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_laasse10_assessment MODIFY COLUMN allow_question_feedback tinyint(1),
								   MODIFY COLUMN allow_overall_feedback tinyint(1),
								   MODIFY COLUMN allow_right_answers tinyint(1),
								   MODIFY COLUMN allow_wrong_answers tinyint(1),
								   MODIFY COLUMN allow_grades_after_attempt tinyint(1),
								   MODIFY COLUMN allow_history_responses tinyint(1),
								   MODIFY COLUMN display_summary tinyint(1),
								   MODIFY COLUMN shuffled tinyint(1),
   								   MODIFY COLUMN attempt_completion_notify tinyint(1) DEFAULT 0,
   								   MODIFY COLUMN numbered tinyint(1) DEFAULT 1;

ALTER TABLE tl_laasse10_assessment_question MODIFY COLUMN shuffle tinyint(1),
   								   			MODIFY COLUMN case_sensitive tinyint(1),
   								   			MODIFY COLUMN correct_answer tinyint(1) DEFAULT 0,
   								   			MODIFY COLUMN allow_rich_editor tinyint(1) DEFAULT 0;
   								   			
ALTER TABLE tl_laasse10_question_reference MODIFY COLUMN random_question tinyint(1) DEFAULT 0;


UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laasse10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;