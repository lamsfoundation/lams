-- SQL statements to update from LAMS 2.3

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

--  LDEV-2125 ------------- 
ALTER TABLE lams_user ADD COLUMN timezone TINYINT;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;