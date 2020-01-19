-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4813 Add a missing foreign key and index to speed up queries
ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_2 FOREIGN KEY (question_option_uid)
	REFERENCES tl_laasse10_question_option (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;