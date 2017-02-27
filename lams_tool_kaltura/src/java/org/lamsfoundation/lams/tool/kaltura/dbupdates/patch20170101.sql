-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180

ALTER TABLE tl_lakalt11_kaltura_item MODIFY COLUMN create_by_author TINYINT(1),
									 MODIFY COLUMN is_hidden TINYINT(1);

ALTER TABLE tl_lakalt11_item_log MODIFY COLUMN complete TINYINT(1);
	
ALTER TABLE tl_lakalt11_comment MODIFY COLUMN is_hidden TINYINT(1);		

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lakalt11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;