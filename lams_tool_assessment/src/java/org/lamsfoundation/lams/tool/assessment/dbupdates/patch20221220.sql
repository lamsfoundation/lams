-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5351 Tell whether and which teacher marked the question manually
ALTER TABLE tl_laasse10_question_result
	ADD COLUMN marked_by BIGINT,
	ADD CONSTRAINT FK_tl_laasse10_question_result_3 FOREIGN KEY (marked_by) REFERENCES tl_laasse10_user(uid)
		ON UPDATE CASCADE ON DELETE SET NULL;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
