-- SQL statements to update from LAMS 2.3

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

ALTER TABLE tl_laimag10_imagegallery_item DROP FOREIGN KEY FK_NEW_1821149711_F52D1F93EC0D3147, 
										  DROP COLUMN session_uid;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;

SET AUTOCOMMIT = 1;