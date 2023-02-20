-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5361 Add option to prevent learners from advancing if they have not found all answers
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN require_all_answers TINYINT DEFAULT '1' AFTER double_click;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
