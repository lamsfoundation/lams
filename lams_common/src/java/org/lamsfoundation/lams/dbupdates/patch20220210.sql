-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5286 
								   
ALTER TABLE lams_ext_server_org_map ADD COLUMN add_staff_to_all_lessons TINYINT NOT NULL DEFAULT 0 AFTER time_to_live_login_request_enabled;					

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
