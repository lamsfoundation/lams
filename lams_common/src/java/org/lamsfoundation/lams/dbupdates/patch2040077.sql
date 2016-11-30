-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-4030 Disable login for a few minutes after X number of attempts
ALTER TABLE lams_user 
ADD COLUMN failed_attempts TINYINT  DEFAULT NULL,
ADD COLUMN lock_out_time DATETIME DEFAULT NULL;

COMMIT;
SET AUTOCOMMIT = 1;


