-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5302 Rename SYSADMIN to APPADMIN, add new SYSADMIN role
								   
UPDATE lams_role SET name = 'APPADMIN', description = 'LAMS Application Adminstrator' WHERE name = 'SYSADMIN';

INSERT INTO lams_role(role_id, name, description, create_date) VALUES (7, 'SYSADMIN', 'LAMS System Adminstrator', NOW());
INSERT INTO lams_user_organisation_role SELECT NULL, user_organisation_id, 7 FROM lams_user_organisation_role WHERE role_id = 1;


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
