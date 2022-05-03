-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5277 Add ID for communication with LTI Advantage platform, which is not necessarily the same as external user ID
								   
ALTER TABLE lams_ext_user_userid_map ADD COLUMN lti_adv_username VARCHAR(250) AFTER external_username;					

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
