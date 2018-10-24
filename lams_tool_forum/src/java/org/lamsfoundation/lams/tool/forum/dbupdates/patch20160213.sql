-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

--     LDEV-4218  Display messages as created in monitoring using a colour
ALTER TABLE tl_lafrum11_message  ADD COLUMN is_monitor SMALLINT(6) DEFAULT 0; 

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;