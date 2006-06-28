CREATE TABLE lams_supported_locale (
       locale_id TINYINT(4) NOT NULL AUTO_INCREMENT
     , language_iso_code VARCHAR(2) NOT NULL
     , country_iso_code VARCHAR(2)
     , description VARCHAR(255) NOT NULL
     , PRIMARY KEY (locale_id)
);
ALTER TABLE lams_supported_locale COMMENT='Describes the valid language/country combinations. Can''t make language_iso_code and country_iso_code a unique key as MySQL considers that "NULL=NULL" is not true.';
ALTER TABLE lams_supported_locale MODIFY COLUMN language_iso_code VARCHAR(2) NOT NULL
      COMMENT 'ISO 639-1 Language Code (2 letter version) Java only supports 2 letter properly, not the 3 letter codes.';
ALTER TABLE lams_supported_locale MODIFY COLUMN country_iso_code VARCHAR(2)
      COMMENT 'ISO 3166 Country Code';

