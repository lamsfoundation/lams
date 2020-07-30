-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5058 Move activity categories to only place where they are needed: authoring front end

ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_12;
ALTER TABLE lams_learning_activity DROP COLUMN activity_category_id;
DROP TABLE lams_activity_category;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
