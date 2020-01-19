-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lasprd10_spreadsheet DROP COLUMN online_instructions;
ALTER TABLE tl_lasprd10_spreadsheet DROP COLUMN offline_instructions;
ALTER TABLE tl_lasprd10_spreadsheet DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lasprd10_attachment;

-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_lasprd10_spreadsheet MODIFY COLUMN reflect_instructions text;

-- LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lasprd10_session ADD UNIQUE (session_id);

-- LDEV-3612 Marks should accept decimal 
ALTER TABLE tl_lasprd10_spreadsheet_mark CHANGE COLUMN marks marks FLOAT NULL DEFAULT NULL;

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

-- LDEV-3687 Unable to add marks due to foreign key on spreadsheet_mark table
ALTER TABLE `tl_lasprd10_spreadsheet_mark` DROP FOREIGN KEY `tl_lasprd10_spreadsheet_mark_ibfk_1`;

ALTER TABLE tl_lasprd10_user_modified_spreadsheet DROP FOREIGN KEY FK_tl_lasprd10_user_modified_spreadsheet_1;
ALTER TABLE tl_lasprd10_user_modified_spreadsheet ADD CONSTRAINT FK_tl_lasprd10_user_modified_spreadsheet_1 FOREIGN KEY (`mark_id`)
REFERENCES `tl_lasprd10_spreadsheet_mark` (`uid`) ON DELETE SET NULL ON UPDATE NO ACTION;

-- LDEV-4180
ALTER TABLE tl_lasprd10_spreadsheet MODIFY COLUMN lock_on_finished TINYINT(1),
									 	MODIFY COLUMN content_in_use TINYINT(1),
									 	MODIFY COLUMN define_later TINYINT(1),
									 	MODIFY COLUMN is_learner_allowed_to_save TINYINT(1),
									 	MODIFY COLUMN reflect_on_activity TINYINT(1),
									 	MODIFY COLUMN is_marking_enabled TINYINT(1);
									 	
ALTER TABLE tl_lasprd10_session MODIFY COLUMN status TINYINT(1);									 		 
	
ALTER TABLE tl_lasprd10_user MODIFY COLUMN session_finished TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lasprd10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;