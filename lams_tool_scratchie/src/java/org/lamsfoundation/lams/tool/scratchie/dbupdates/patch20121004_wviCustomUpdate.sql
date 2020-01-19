-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV-2657 Date and time restriction 
ALTER TABLE tl_lascrt11_user ADD COLUMN leader smallint DEFAULT 0;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;