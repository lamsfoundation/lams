-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4705 Remove course admin role
SET @admin_roles = (SELECT config_value FROM lams_configuration WHERE config_key = 'LDAPGroupAdminMap');
UPDATE lams_configuration SET config_value = CONCAT(config_value, ';', @admin_roles)
WHERE config_key = 'LDAPGroupManagerMap';

DELETE FROM lams_configuration WHERE config_key = 'LDAPGroupAdminMap';
----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;