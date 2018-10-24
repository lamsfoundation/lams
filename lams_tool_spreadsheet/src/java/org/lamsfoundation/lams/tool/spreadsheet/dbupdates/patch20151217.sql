-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lasprd10_session DROP FOREIGN KEY FK_NEW_2065267438_24AA78C530E79035;
ALTER TABLE tl_lasprd10_session ADD CONSTRAINT FK_NEW_2065267438_24AA78C530E79035 FOREIGN KEY (`spreadsheet_uid`)
REFERENCES `tl_lasprd10_spreadsheet` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lasprd10_spreadsheet DROP FOREIGN KEY FK_NEW_2065267438_89093BF758092FB;
ALTER TABLE tl_lasprd10_spreadsheet ADD CONSTRAINT FK_NEW_2065267438_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_lasprd10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

 
ALTER TABLE tl_lasprd10_user DROP FOREIGN KEY FK_NEW_2065267438_30113BFC309ED320;
ALTER TABLE tl_lasprd10_user ADD CONSTRAINT FK_NEW_2065267438_30113BFC309ED320 FOREIGN KEY (`spreadsheet_uid`)
REFERENCES `tl_lasprd10_spreadsheet` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasprd10_user DROP FOREIGN KEY FK_NEW_2065267438_30113BFCEC0D3147;
ALTER TABLE tl_lasprd10_user ADD CONSTRAINT FK_NEW_2065267438_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_lasprd10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lasprd10_user DROP FOREIGN KEY FK_NEW_2065267438_693580A441F9365D;
ALTER TABLE tl_lasprd10_user ADD CONSTRAINT FK_NEW_2065267438_693580A441F9365D FOREIGN KEY (`user_modified_spreadsheet_uid`)
REFERENCES `tl_lasprd10_user_modified_spreadsheet` (`uid`) ON DELETE SET NULL ON UPDATE NO ACTION;


ALTER TABLE tl_lasprd10_user_modified_spreadsheet DROP FOREIGN KEY FK_tl_lasprd10_user_modified_spreadsheet_1;
ALTER TABLE tl_lasprd10_user_modified_spreadsheet ADD CONSTRAINT FK_tl_lasprd10_user_modified_spreadsheet_1 FOREIGN KEY (`mark_id`)
REFERENCES `tl_lasprd10_spreadsheet_mark` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lasprd10_spreadsheet_mark ADD FOREIGN KEY (`uid`)
REFERENCES `tl_lasprd10_user_modified_spreadsheet` (`mark_id`) ON DELETE CASCADE ON UPDATE CASCADE;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;