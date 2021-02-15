-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5178 Add password expiration

INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required) 
VALUES ('PasswordExpirationMonths','12', 'config.password.expiration', 'config.header.password.policy', 'LONG', 1);


ALTER TABLE lams_user ADD COLUMN password_change_date DATETIME AFTER portrait_uuid;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
