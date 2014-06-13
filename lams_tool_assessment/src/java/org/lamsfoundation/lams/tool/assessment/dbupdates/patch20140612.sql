-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3249 Removing old redundant question answers that had left from the times when autopatch created new question answers on each save 
DELETE tl_laasse10_option_answer.* FROM tl_laasse10_question_result, tl_laasse10_option_answer
                      WHERE tl_laasse10_question_result.uid = tl_laasse10_option_answer.question_result_uid 
                      AND tl_laasse10_question_result.result_uid IS NULL;
DELETE FROM tl_laasse10_question_result where result_uid IS NULL;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;