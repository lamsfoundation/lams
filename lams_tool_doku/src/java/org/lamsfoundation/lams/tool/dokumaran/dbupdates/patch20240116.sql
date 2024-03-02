-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LAI-9 Add columns to store AI reviews and learning outcomes
ALTER TABLE tl_ladoku11_session ADD COLUMN ai_review TEXT;
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN ai_learning_outcomes TEXT;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;