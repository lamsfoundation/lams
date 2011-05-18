-- SQL statements to update to LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

-- LDEV-2657 Date and time restriction 
ALTER TABLE tl_laasse10_assessment ADD COLUMN submission_deadline DATETIME DEFAULT null;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;