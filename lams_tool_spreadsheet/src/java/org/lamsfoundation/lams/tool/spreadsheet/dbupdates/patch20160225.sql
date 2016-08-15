-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3687 Unable to add marks due to foreign key on spreadsheet_mark table

ALTER TABLE `tl_lasprd10_spreadsheet_mark` DROP FOREIGN KEY `tl_lasprd10_spreadsheet_mark_ibfk_1`;

ALTER TABLE tl_lasprd10_user_modified_spreadsheet DROP FOREIGN KEY FK_tl_lasprd10_user_modified_spreadsheet_1;
ALTER TABLE tl_lasprd10_user_modified_spreadsheet ADD CONSTRAINT FK_tl_lasprd10_user_modified_spreadsheet_1 FOREIGN KEY (`mark_id`)
REFERENCES `tl_lasprd10_spreadsheet_mark` (`uid`) ON DELETE SET NULL ON UPDATE NO ACTION;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;