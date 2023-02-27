-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5360 Use core Users and not AssessmentUsers for marker column
UPDATE tl_laasse10_question_result AS r JOIN tl_laasse10_user AS u ON r.marked_by = u.uid
	SET r.marked_by = u.user_id;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;