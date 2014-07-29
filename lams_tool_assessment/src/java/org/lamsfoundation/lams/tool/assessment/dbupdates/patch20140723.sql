-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN max_words_limit integer DEFAULT 0;
ALTER TABLE tl_laasse10_assessment_question ADD COLUMN min_words_limit integer DEFAULT 0;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;