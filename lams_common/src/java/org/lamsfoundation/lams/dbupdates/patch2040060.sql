-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3661 Remove obsolete columns
ALTER TABLE lams_notification_subscription DROP COLUMN periodicity,
										   DROP COLUMN last_operation_time;

COMMIT;
SET AUTOCOMMIT = 1;