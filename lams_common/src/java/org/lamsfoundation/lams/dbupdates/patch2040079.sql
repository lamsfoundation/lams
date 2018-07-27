SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4123 Theme management: remove old highcontrast
UPDATE lams_user SET theme_id = (SELECT theme_id FROM lams_theme WHERE name = 'defaultHTML')
WHERE theme_id = (SELECT theme_id FROM lams_theme WHERE name = 'highContrast');

DELETE FROM lams_theme WHERE name = 'highContrast';

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
