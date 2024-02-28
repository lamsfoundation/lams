-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5449 Make answer rating optional
ALTER TABLE tl_laasse10_assessment ADD COLUMN allow_answer_rating TINYINT DEFAULT 1 AFTER allow_disclose_answers;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;