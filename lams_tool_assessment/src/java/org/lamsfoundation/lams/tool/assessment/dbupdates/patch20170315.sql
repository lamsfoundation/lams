-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4271 Remove unnecessary fields
ALTER TABLE `tl_laasse10_assessment_question` DROP FOREIGN KEY `FK_NEW_1720029621_F52D1F93758092FB`;
ALTER TABLE `tl_laasse10_assessment_question` DROP COLUMN `create_by`, DROP INDEX `FK_NEW_1720029621_F52D1F93758092FB`;

ALTER TABLE `tl_laasse10_assessment_question` DROP COLUMN `create_date`;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;