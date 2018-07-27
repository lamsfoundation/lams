-- Turn off autocommit, so nothing is committed if there is an error

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- LDEV-3597 Remove SSL configuration as it is done differently in WildFly
DELETE FROM lams_configuration WHERE config_key = 'TruststorePassword'
                                  OR config_key = 'TruststorePath';

-- If there were no errors, commit and restore autocommit to on
SET FOREIGN_KEY_CHECKS=0;
COMMIT;
SET AUTOCOMMIT = 1;