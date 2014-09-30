SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

--  LDEV-3315 Remove obsolete AUTHOR ADMIN role

DELETE FROM lams_user_organisation_role WHERE role_id = (SELECT role_id FROM lams_role WHERE name = 'AUTHOR ADMIN');
DELETE FROM lams_role_privilege WHERE role_id = (SELECT role_id FROM lams_role WHERE name = 'AUTHOR ADMIN');
DELETE FROM lams_role WHERE name = 'AUTHOR ADMIN';

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;