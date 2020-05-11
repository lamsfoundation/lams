-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20140102.sql
-- LDEV-3123 Make all DB tables names to be lowercase
SELECT DATABASE() INTO @db_name FROM DUAL; 

SELECT Count(*)
INTO @exists
FROM information_schema.tables
WHERE table_schema = @db_name
    AND table_type = 'BASE TABLE'
    AND BINARY table_name = 'tl_laimsc11_commonCartridge';

SET @query = If(@exists>0,
    'RENAME TABLE tl_laimsc11_commonCartridge TO tl_laimsc11_commoncartridge',
    'SELECT \'nothing to rename\' status');

PREPARE stmt FROM @query;

EXECUTE stmt;


SELECT Count(*)
INTO @exists
FROM information_schema.tables
WHERE table_schema = @db_name
    AND table_type = 'BASE TABLE'
    AND BINARY table_name = 'tl_laimsc11_commonCartridge_item';

SET @query = If(@exists>0,
    'RENAME TABLE tl_laimsc11_commonCartridge_item TO tl_laimsc11_commoncartridge_item',
    'SELECT \'nothing to rename\' status');

PREPARE stmt FROM @query;

EXECUTE stmt; 

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_laimsc11_commoncartridge DROP COLUMN online_instructions;
ALTER TABLE tl_laimsc11_commoncartridge DROP COLUMN offline_instructions;
ALTER TABLE tl_laimsc11_commoncartridge DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_laimsc11_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='laimsc11';

-- patch20150217.sql
-- LDEV-3432 Do not limit reflect instructions length
ALTER TABLE tl_laimsc11_commoncartridge MODIFY COLUMN reflect_instructions text;

-- patch20150930.sql
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_laimsc11_session ADD UNIQUE (session_id);

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades
 
ALTER TABLE tl_laimsc11_commoncartridge DROP FOREIGN KEY FK_NEW_1279208528_89093BF758092FB;
ALTER TABLE tl_laimsc11_commoncartridge ADD CONSTRAINT FK_NEW_1279208528_89093BF758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laimsc11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_laimsc11_commoncartridge_item DROP FOREIGN KEY FK_NEW_1279208528_F52D1F9330E79035;
ALTER TABLE tl_laimsc11_commoncartridge_item ADD CONSTRAINT FK_NEW_1279208528_F52D1F9330E79035 FOREIGN KEY (`commonCartridge_uid`)
REFERENCES `tl_laimsc11_commoncartridge` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laimsc11_commoncartridge_item DROP FOREIGN KEY FK_NEW_1279208528_F52D1F93758092FB;
ALTER TABLE tl_laimsc11_commoncartridge_item ADD CONSTRAINT FK_NEW_1279208528_F52D1F93758092FB FOREIGN KEY (`create_by`)
REFERENCES `tl_laimsc11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_laimsc11_commoncartridge_item DROP FOREIGN KEY FK_NEW_1279208528_F52D1F93EC0D3147;
ALTER TABLE tl_laimsc11_commoncartridge_item ADD CONSTRAINT FK_NEW_1279208528_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laimsc11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

 
ALTER TABLE tl_laimsc11_item_instruction DROP FOREIGN KEY FK_NEW_1279208528_A5665013980570ED;
ALTER TABLE tl_laimsc11_item_instruction ADD CONSTRAINT FK_NEW_1279208528_A5665013980570ED FOREIGN KEY (`item_uid`)
REFERENCES `tl_laimsc11_commoncartridge_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laimsc11_item_log DROP FOREIGN KEY FK_NEW_1279208528_693580A438BF8DFE;
ALTER TABLE tl_laimsc11_item_log ADD CONSTRAINT FK_NEW_1279208528_693580A438BF8DFE FOREIGN KEY (`commonCartridge_item_uid`)
REFERENCES `tl_laimsc11_commoncartridge_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laimsc11_item_log DROP FOREIGN KEY FK_NEW_1279208528_693580A441F9365D;
ALTER TABLE tl_laimsc11_item_log ADD CONSTRAINT FK_NEW_1279208528_693580A441F9365D FOREIGN KEY (`user_uid`)
REFERENCES `tl_laimsc11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laimsc11_session DROP FOREIGN KEY FK_NEW_1279208528_24AA78C530E79035;
ALTER TABLE tl_laimsc11_session ADD CONSTRAINT FK_NEW_1279208528_24AA78C530E79035 FOREIGN KEY (`commonCartridge_uid`)
REFERENCES `tl_laimsc11_commoncartridge` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_laimsc11_user DROP FOREIGN KEY FK_NEW_1279208528_30113BFCEC0D3147;
ALTER TABLE tl_laimsc11_user ADD CONSTRAINT FK_NEW_1279208528_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_laimsc11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laimsc11_user DROP FOREIGN KEY FK_NEW_1279208528_30113BFC309ED320;
ALTER TABLE tl_laimsc11_user ADD CONSTRAINT FK_NEW_1279208528_30113BFC309ED320 FOREIGN KEY (`commonCartridge_uid`)
REFERENCES `tl_laimsc11_commoncartridge` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20160518.sql
--  LDEV-3802 Update IMS CC Author and Monitor to Bootstrap
ALTER TABLE tl_laimsc11_commoncartridge_item DROP FOREIGN KEY FK_NEW_1279208528_F52D1F93EC0D3147;
ALTER TABLE tl_laimsc11_commoncartridge_item DROP COLUMN session_uid;

-- patch20170101.sql
-- LDEV-4180

ALTER TABLE tl_laimsc11_commoncartridge MODIFY COLUMN lock_on_finished TINYINT(1),
									 	MODIFY COLUMN content_in_use TINYINT(1),
									 	MODIFY COLUMN define_later TINYINT(1),
									 	MODIFY COLUMN allow_auto_run TINYINT(1),
									 	MODIFY COLUMN reflect_on_activity TINYINT(1);
									 	
ALTER TABLE tl_laimsc11_commoncartridge_item MODIFY COLUMN create_by_author TINYINT(1),
									 		 MODIFY COLUMN is_hide TINYINT(1),
									 		 MODIFY COLUMN open_url_new_window TINYINT(1);

ALTER TABLE tl_laimsc11_item_log MODIFY COLUMN complete TINYINT(1);									 		 
	
ALTER TABLE tl_laimsc11_user MODIFY COLUMN session_finished TINYINT(1);		

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laimsc11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;