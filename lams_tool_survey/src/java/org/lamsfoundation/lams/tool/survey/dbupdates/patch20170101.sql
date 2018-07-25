-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_lasurv11_survey MODIFY COLUMN lock_on_finished TINYINT(1),
							   MODIFY COLUMN content_in_use TINYINT(1),
							   MODIFY COLUMN define_later TINYINT(1),
							   MODIFY COLUMN show_questions_on_one_page TINYINT(1),
							   MODIFY COLUMN reflect_on_activity TINYINT(1),
							   MODIFY COLUMN answer_submit_notify TINYINT(1);
									 	
ALTER TABLE tl_lasurv11_question MODIFY COLUMN append_text TINYINT(1),
								 MODIFY COLUMN optional TINYINT(1),
								 MODIFY COLUMN allow_multiple_answer TINYINT(1);

ALTER TABLE tl_lasurv11_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lasurv11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;