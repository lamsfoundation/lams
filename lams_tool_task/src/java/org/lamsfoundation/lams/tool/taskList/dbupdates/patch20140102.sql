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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;