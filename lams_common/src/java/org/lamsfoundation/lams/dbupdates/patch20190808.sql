-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4844 Add a Question Bank event for merging questions 
INSERT INTO lams_log_event_type VALUES (27, 'TYPE_QUESTIONS_MERGED', 'QUESTION_BANK');
						 	
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;