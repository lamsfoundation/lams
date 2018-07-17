-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4617 Add column for email verification
ALTER TABLE lams_signup_organisation ADD COLUMN email_verify TINYINT(1) DEFAULT 0 AFTER add_with_monitor;
ALTER TABLE lams_user ADD COLUMN email_verified TINYINT(1) DEFAULT 1 AFTER email;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;