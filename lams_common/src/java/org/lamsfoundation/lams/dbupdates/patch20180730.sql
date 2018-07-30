-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4641 Add SMTP port and authentication type
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('SMTPPort','25', 'config.smtp.port', 'config.header.email', 'LONG', 0),
	   ('SMTPAuthSecurity','none', 'config.smtp.auth.security', 'config.header.email', 'STRING', 1);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;