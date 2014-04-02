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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;