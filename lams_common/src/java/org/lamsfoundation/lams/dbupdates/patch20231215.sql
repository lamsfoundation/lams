-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5426 Use Java timezones instead of built-in ones

INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required)
VALUES ('ServerTimezone', (SELECT timezone_id FROM lams_timezone WHERE server_timezone = 1),
        'admin.servertimezone.select.server.timezone', 'config.header.features', 'STRING', 0);
DROP TABLE lams_timezone;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;