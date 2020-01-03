-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4876 Ability for LTI servers to get userId from another request parameter name
ALTER TABLE lams_ext_server_org_map ADD COLUMN `use_alternative_user_id_parameter_name` tinyint(1) DEFAULT '0';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;