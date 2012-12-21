-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- 	LDEV-2986 Allow choosing which tab is active on a signup page
ALTER TABLE lams_signup_organisation ADD COLUMN login_tab_active TINYINT(1) DEFAULT 0; 

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;
