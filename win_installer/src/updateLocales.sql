-- SQL script for language-pack.sql
-- checks if the languages in the language pack exist
-- inserts rows into lams_supported_locale iff the languages dont exist

drop table if exists locale_temp;

create table locale_temp  (
     language_iso_code VARCHAR(2) NOT NULL
     , country_iso_code VARCHAR(2)
     , description VARCHAR(255) NOT NULL
     , direction VARCHAR(3) NOT NULL
     , combined VARCHAR(5) NOT NULL
)TYPE=InnoDB;

-- the combined column must be 'language_iso_code,country_iso_code' or 'language_iso_code'. It must match the value
-- given by SELECT CONCAT_WS(',',l.language_iso_code,l.country_iso_code) FROM lams_supported_locale l
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('en','AU','English (Australia)','LTR','en,AU');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('es','ES','Español','LTR','es,ES');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('mi','NZ','Māori','LTR','mi,NZ');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('de','DE','Deutsch','LTR','de,DE');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('zh','CN','简体中文','LTR','zh,CN');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('fr','FR','Français','LTR','fr,FR');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('it','IT','Italiano','LTR','it,IT');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('no','NO','Norsk','LTR','no,NO');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('sv','SE','Svenska','LTR','sv,SE');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('ko','KR','한국어','LTR','ko,KR');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('pl','PL','Polski','LTR','pl,PL');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('pt','BR','Português (Brasil)','LTR','pt,BR');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('hu','HU','Magyar','LTR','hu,HU');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('bg','BG','Български','LTR','bg,BG');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('cy','GB','Cymraeg (Cymru)','LTR','cy,GB');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('th','TH','Thai','LTR','th,TH');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('el','GR','Ελληνικά','LTR','el,GR');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('nl','BE','Nederlands (België)','LTR','nl,BE');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('ar','JO','عربي','RTL','ar,JO');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('da','DK','Dansk','LTR','da,DK');
--INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('ru','RU','Русский','LTR','ru,RU');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('vi','VN','Tiếng Việt','LTR','vi,VN');
INSERT INTO locale_temp (language_iso_code, country_iso_code, description, direction, combined) VALUES ('zh','TW','Chinese (Taiwan)','LTR','zh,TW');


INSERT INTO lams_supported_locale (language_iso_code, country_iso_code, description, direction) 
(SELECT t.language_iso_code, t.country_iso_code, t.description, t.direction
FROM locale_temp t WHERE t.combined NOT IN
(SELECT CONCAT_WS(',',l.language_iso_code,l.country_iso_code) FROM lams_supported_locale l));

UPDATE lams_configuration set config_value='2006-12-22' WHERE config_key='DictionaryDateCreated';

drop table if exists locale_temp;
