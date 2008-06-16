-- Fixing Japanese language string. See LDEV-1702
-- checks if the languages in the language pack exist
-- inserts rows into lams_supported_locale iff the languages dont exist
drop table if exists locale_temp;

create table locale_temp  (
     language_iso_code VARCHAR(2) NOT NULL
     , country_iso_code VARCHAR(2)
     , description VARCHAR(255) NOT NULL
     , direction VARCHAR(3) NOT NULL
     , combined VARCHAR(5) NOT NULL
     , fckeditor_code VARCHAR(10) 
)TYPE=InnoDB;

-- the combined column must be 'language_iso_code,country_iso_code' or 'language_iso_code'. It must match the value
-- given by SELECT CONCAT_WS(',',l.language_iso_code,l.country_iso_code) FROM lams_supported_locale l
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined, fckeditor_code) VALUES ('ja','JP','日本語','LTR','ja,JP', 'ja');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined, fckeditor_code) VALUES ('ms', 'MY','Malay (Malaysia)','LTR','ms,MY', 'ms');


INSERT INTO lams_supported_locale (language_iso_code, country_iso_code, description, direction, fckeditor_code) 
(SELECT t.language_iso_code, t.country_iso_code, t.description, t.direction, t.fckeditor_code
FROM locale_temp t WHERE t.combined NOT IN
(SELECT CONCAT_WS(',',l.language_iso_code,l.country_iso_code) FROM lams_supported_locale l));
drop table if exists locale_temp;

-- Fixing Japanese language string. See LDEV-1702
-- description description may be set to "Japanese (Japan)"
UPDATE lams_supported_locale SET description='日本語' WHERE language_iso_code='ja' AND country_iso_code='JP';