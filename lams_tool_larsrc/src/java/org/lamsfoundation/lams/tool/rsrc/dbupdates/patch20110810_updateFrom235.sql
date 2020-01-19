-- SQL statements to update from LAMS 2.3.5

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV-2705 Improve reporting for Share Resources
ALTER TABLE tl_larsrc11_item_log ADD COLUMN complete_date datetime DEFAULT NULL;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;