-- SQL statements to update from LAMS 2.3.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV-2421 Adding sorting for resource items
ALTER TABLE tl_larsrc11_resource_item ADD COLUMN order_id integer DEFAULT NULL;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;