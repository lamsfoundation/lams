-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-4767 Add new event types for log in and log out

INSERT INTO lams_log_event_type VALUES (24, 'TYPE_LOGIN',  'SECURITY'), 
									   (25, 'TYPE_LOGOUT', 'SECURITY');


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;