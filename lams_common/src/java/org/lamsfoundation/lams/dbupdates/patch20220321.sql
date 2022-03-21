-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5302 Rename SYSADMIN to APPADMIN
								   
UPDATE lams_role SET name = 'APPADMIN', description = 'LAMS Application Adminstrator' WHERE name = 'SYSADMIN';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
