-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;