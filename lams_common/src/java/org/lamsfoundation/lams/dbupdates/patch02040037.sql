SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- LDEV-3340: Remove cache management from sysadmin
DELETE FROM lams_configuration WHERE config_key = 'UseCacheDebugListener';

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;