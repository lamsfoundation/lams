-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5277 Set grading information for LTI Advantage

ALTER TABLE lams_ext_server_lesson_map ADD COLUMN grading_type VARCHAR(50),
									   ADD COLUMN grading_url  VARCHAR(255);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
