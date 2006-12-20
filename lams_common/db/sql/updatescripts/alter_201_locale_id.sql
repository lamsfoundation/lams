-- Script to be run for LAMS 2.0.1 release, on LAMS 2.0 tables.
-- LAMS 2.0 could have at most 127 locales due to the locale_id column
-- type being set to TINYINT. This is changed to SMALLINT - to support
-- not only more locales but also to make it a standard datatype

ALTER TABLE lams_organisation
CHANGE COLUMN locale_id locale_id SMALLINT;

ALTER TABLE lams_user	
DROP FOREIGN KEY FK_lams_user_6;

ALTER TABLE lams_user	
CHANGE COLUMN locale_id locale_id SMALLINT;

ALTER TABLE lams_supported_locale
CHANGE COLUMN locale_id locale_id SMALLINT NOT NULL  AUTO_INCREMENT;

ALTER TABLE lams_user 
ADD CONSTRAINT FK_lams_user_6 FOREIGN KEY (locale_id)
REFERENCES lams_supported_locale (locale_id) ON DELETE NO ACTION ON UPDATE NO ACTION;

COMMIT;
