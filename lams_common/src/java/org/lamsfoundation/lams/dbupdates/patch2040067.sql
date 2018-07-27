-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3965 Ability to enable two-factor authentication for selected users
ALTER TABLE lams_user ADD COLUMN two_factor_auth_enabled TINYINT(1) DEFAULT 0 AFTER salt;
ALTER TABLE lams_user ADD COLUMN two_factor_auth_secret CHAR(64) AFTER two_factor_auth_enabled;

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
