-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5178 Add password expiration

INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required) 
VALUES ('PasswordExpirationMonths', '12', 'config.password.expiration', 'config.header.password.policy', 'LONG', 1),
	   ('PasswordHistoryLimit',     '3',  'config.password.history',    'config.header.password.policy', 'LONG', 1);


ALTER TABLE lams_user ADD COLUMN password_change_date DATETIME AFTER portrait_uuid;

CREATE TABLE lams_user_password_history (
	uid INT UNSIGNED AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	change_date DATETIME NOT NULL,
	password CHAR(129) NOT NULL,
	PRIMARY KEY (uid),
	CONSTRAINT FK_lams_user_password_history_1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);
			

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
