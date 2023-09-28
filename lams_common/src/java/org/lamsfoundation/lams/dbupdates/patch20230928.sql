-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5416 Add unique keys on roles tables to avoid duplicate entries

ALTER TABLE lams_user_organisation ADD UNIQUE KEY UQ_lams_user_organisation_1 (organisation_id, user_id);
ALTER TABLE lams_user_organisation_role ADD UNIQUE KEY UQ_lams_user_organisation_role_1 (user_organisation_id, role_id);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;