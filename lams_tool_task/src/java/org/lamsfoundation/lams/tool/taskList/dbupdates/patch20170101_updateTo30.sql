-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-3123 Make all DB tables names to be lowercase
SELECT DATABASE() INTO @db_name FROM DUAL; 

SELECT Count(*)
INTO @exists
FROM information_schema.tables
WHERE table_schema = @db_name
    AND table_type = 'BASE TABLE'
    AND BINARY table_name = 'tl_latask10_taskList';

SET @query = If(@exists>0,
    'RENAME TABLE tl_latask10_taskList TO tl_latask10_tasklist',
    'SELECT \'nothing to rename\' status');

PREPARE stmt FROM @query;

EXECUTE stmt;


SELECT Count(*)
INTO @exists
FROM information_schema.tables
WHERE table_schema = @db_name
    AND table_type = 'BASE TABLE'
    AND BINARY table_name = 'tl_latask10_taskList_item';

SET @query = If(@exists>0,
    'RENAME TABLE tl_latask10_taskList_item TO tl_latask10_tasklist_item',
    'SELECT \'nothing to rename\' status');

PREPARE stmt FROM @query;

EXECUTE stmt; 


-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_latask10_tasklist DROP COLUMN online_instructions;
ALTER TABLE tl_latask10_tasklist DROP COLUMN offline_instructions;
ALTER TABLE tl_latask10_tasklist DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_latask10_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='latask10';

-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_latask10_tasklist MODIFY COLUMN reflect_instructions text;

--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_latask10_session ADD UNIQUE (session_id);

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_latask10_condition DROP FOREIGN KEY FK_tl_latask10_condition_1;
ALTER TABLE tl_latask10_condition ADD CONSTRAINT FK_tl_latask10_condition_1 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_condition_tl_item DROP FOREIGN KEY FK_tl_latask10_tasklist_item_condition_1;
ALTER TABLE tl_latask10_condition_tl_item ADD CONSTRAINT FK_tl_latask10_tasklist_item_condition_1 FOREIGN KEY (`condition_uid`)
REFERENCES `tl_latask10_condition` (`condition_uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_condition_tl_item DROP FOREIGN KEY FK_tl_latask10_tasklist_item_condition_2;
ALTER TABLE tl_latask10_condition_tl_item ADD CONSTRAINT FK_tl_latask10_tasklist_item_condition_2 FOREIGN KEY (`uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_item_attachment DROP FOREIGN KEY FK_tl_latask10_item_attachment_1;
ALTER TABLE tl_latask10_item_attachment ADD CONSTRAINT FK_tl_latask10_item_attachment_1 FOREIGN KEY (`taskList_item_uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_item_attachment DROP FOREIGN KEY FK_tl_latask10_item_attachment_2;
ALTER TABLE tl_latask10_item_attachment ADD CONSTRAINT FK_tl_latask10_item_attachment_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_latask10_item_comment DROP FOREIGN KEY FK_tl_latask10_item_comment_2;
ALTER TABLE tl_latask10_item_comment ADD CONSTRAINT FK_tl_latask10_item_comment_2 FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_item_comment DROP FOREIGN KEY FK_tl_latask10_item_comment_3;
ALTER TABLE tl_latask10_item_comment ADD CONSTRAINT FK_tl_latask10_item_comment_3 FOREIGN KEY (`taskList_item_uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

 
ALTER TABLE tl_latask10_session DROP FOREIGN KEY FK_NEW_174079138_24AA78C530E79035;
ALTER TABLE tl_latask10_session ADD CONSTRAINT FK_NEW_174079138_24AA78C530E79035 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_tasklist DROP FOREIGN KEY FK_NEW_174079138_89093BF758092FB;
ALTER TABLE tl_latask10_tasklist ADD CONSTRAINT FK_NEW_174079138_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_latask10_tasklist_item DROP FOREIGN KEY FK_NEW_174079138_F52D1F9330E79035;
ALTER TABLE tl_latask10_tasklist_item ADD CONSTRAINT FK_NEW_174079138_F52D1F9330E79035 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_tasklist_item DROP FOREIGN KEY FK_NEW_174079138_F52D1F93758092FB;
ALTER TABLE tl_latask10_tasklist_item ADD CONSTRAINT FK_NEW_174079138_F52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_latask10_tasklist_item DROP FOREIGN KEY FK_NEW_174079138_F52D1F93EC0D3147;
ALTER TABLE tl_latask10_tasklist_item ADD CONSTRAINT FK_NEW_174079138_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_latask10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_latask10_user DROP FOREIGN KEY FK_NEW_174079138_30113BFCEC0D3147;
ALTER TABLE tl_latask10_user ADD CONSTRAINT FK_NEW_174079138_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_latask10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_latask10_user DROP FOREIGN KEY FK_NEW_174079138_30113BFC309ED320;
ALTER TABLE tl_latask10_user ADD CONSTRAINT FK_NEW_174079138_30113BFC309ED320 FOREIGN KEY (`taskList_uid`)
REFERENCES `tl_latask10_tasklist` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
		
			 
ALTER TABLE tl_latask10_item_log DROP FOREIGN KEY FK_NEW_174079138_693580A441F9365D;
ALTER TABLE tl_latask10_item_log ADD CONSTRAINT FK_NEW_174079138_693580A441F9365D FOREIGN KEY (`user_uid`)
REFERENCES `tl_latask10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;		

ALTER TABLE tl_latask10_item_log DROP FOREIGN KEY FK_NEW_174079138_693580A438BF8DFE;
ALTER TABLE tl_latask10_item_log ADD CONSTRAINT FK_NEW_174079138_693580A438BF8DFE FOREIGN KEY (`taskList_item_uid`)
REFERENCES `tl_latask10_tasklist_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-4180
ALTER TABLE tl_latask10_tasklist MODIFY COLUMN lock_when_finished TINYINT(1),
							   	 MODIFY COLUMN content_in_use TINYINT(1),
							   	 MODIFY COLUMN define_later TINYINT(1),
							   	 MODIFY COLUMN is_sequential_order TINYINT(1),
							   	 MODIFY COLUMN reflect_on_activity TINYINT(1),
							   	 MODIFY COLUMN allow_contribute_tasks TINYINT(1),
							   	 MODIFY COLUMN is_monitor_verification_required TINYINT(1);
									 	
ALTER TABLE tl_latask10_tasklist_item MODIFY COLUMN create_by_author TINYINT(1),
								 	  MODIFY COLUMN is_required TINYINT(1),
								 	  MODIFY COLUMN is_comments_allowed TINYINT(1),
								 	  MODIFY COLUMN is_comments_required TINYINT(1),
								 	  MODIFY COLUMN is_files_allowed TINYINT(1),
								 	  MODIFY COLUMN is_files_required TINYINT(1),
								 	  MODIFY COLUMN is_comments_files_allowed TINYINT(1),
								 	  MODIFY COLUMN show_comments_to_all TINYINT(1),
								 	  MODIFY COLUMN is_child_task TINYINT(1);

ALTER TABLE tl_latask10_item_log MODIFY COLUMN complete TINYINT(1);	

ALTER TABLE tl_latask10_session MODIFY COLUMN status TINYINT(1);	

ALTER TABLE tl_latask10_user MODIFY COLUMN session_finished TINYINT(1),
							 MODIFY COLUMN is_verified_by_monitor TINYINT(1);

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='latask10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;