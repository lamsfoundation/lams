-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4705 Remove course admin role
DROP TABLE lams_role_privilege;
DROP TABLE lams_privilege;

-- add group manager role to user who has got only group admin and not group manager yet
INSERT INTO lams_user_organisation_role
	SELECT NULL, user_organisation_id, 2
	FROM lams_user_organisation_role AS a 
	WHERE role_id = 6 
	AND NOT EXISTS
		(SELECT 1 FROM lams_user_organisation_role WHERE a.user_organisation_id = user_organisation_id AND role_id = 2);

-- delete role assigments for group admin
DELETE FROM lams_user_organisation_role WHERE role_id = 6;

-- delete role itself
DELETE FROM lams_role WHERE role_id = 6;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;