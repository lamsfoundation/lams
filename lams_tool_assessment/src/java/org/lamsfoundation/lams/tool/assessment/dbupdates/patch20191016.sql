-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4813 Add missing indexes to speed up queries
 ALTER TABLE tl_laasse10_assessment_result ADD INDEX (latest);
 
 ALTER TABLE tl_laasse10_option_answer ADD INDEX (answer_boolean);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;