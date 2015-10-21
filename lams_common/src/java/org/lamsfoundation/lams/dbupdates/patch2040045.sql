-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3578 Add salt to password. Remove browser encryption.
ALTER TABLE lams_user MODIFY COLUMN password CHAR(64),
					  ADD COLUMN salt CHAR(64) AFTER password;
					  
DELETE FROM lams_configuration WHERE config_key = 'LDAPEncryptPasswordFromBrowser';

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
