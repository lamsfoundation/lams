-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- LDEV-3661 Remove internal SMTP server
DELETE FROM lams_configuration WHERE config_key = 'InternalSMTPServer';

-- clean up Event Notifications tables
DELETE FROM lams_events WHERE fail_time IS NOT NULL;

ALTER TABLE lams_event_subscriptions RENAME lams_notification_subscription;
ALTER TABLE lams_events RENAME lams_notification_event,
						DROP COLUMN triggered,
						DROP COLUMN default_subject,
						DROP COLUMN default_message;

COMMIT;
SET AUTOCOMMIT = 1;