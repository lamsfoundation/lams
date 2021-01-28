-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4813 Add a missing foreign key and index

ALTER TABLE tl_laasse10_assessment_result ADD CONSTRAINT FK_tl_laasse10_assessment_result_1 FOREIGN KEY (session_id)
	REFERENCES tl_laasse10_session (session_id) ON DELETE CASCADE ON UPDATE CASCADE;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
