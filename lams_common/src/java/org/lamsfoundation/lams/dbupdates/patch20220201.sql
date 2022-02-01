-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5277 Add default country, locale and timezone to LTI consumer
								   
ALTER TABLE lams_ext_server_org_map    ADD COLUMN default_country  	CHAR(2) 		 AFTER user_id_parameter_name,
									   ADD COLUMN default_locale_id TINYINT UNSIGNED AFTER default_country,
									   ADD COLUMN default_timezone 	VARCHAR(255) 	 AFTER default_locale_id;						

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
