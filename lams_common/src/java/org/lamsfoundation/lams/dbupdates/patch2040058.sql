-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3749 Remove OpenId DB stuff
DROP TABLE lams_openid_config;

COMMIT;
SET AUTOCOMMIT = 1;