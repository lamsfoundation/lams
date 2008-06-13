-- FOR LAMS 2.1 Release: See LDEV-1488
-- We found after upgrading demo to LAMS 2.1 that some of the existing designs had the group activities missing
-- their system tool id. This script will fix this. 

update lams_learning_activity set system_tool_id = 1 where learning_activity_type_id = '2' and system_tool_id is null;

-- See LDEV-1349
alter table lams_supported_locale
ADD COLUMN fckeditor_code VARCHAR(10);

UPDATE lams_supported_locale SET fckeditor_code='en-au' WHERE language_iso_code='en' AND country_iso_code='AU';
UPDATE lams_supported_locale SET fckeditor_code='es' WHERE language_iso_code='es' AND country_iso_code='ES';
UPDATE lams_supported_locale SET fckeditor_code='en-au' WHERE language_iso_code='mi' AND country_iso_code='NZ';
UPDATE lams_supported_locale SET fckeditor_code='de' WHERE language_iso_code='de' AND country_iso_code='DE';
UPDATE lams_supported_locale SET fckeditor_code='zh-cn' WHERE language_iso_code='zh' AND country_iso_code='CN';
UPDATE lams_supported_locale SET fckeditor_code='fr' WHERE language_iso_code='fr' AND country_iso_code='FR';
UPDATE lams_supported_locale SET fckeditor_code='it' WHERE language_iso_code='it' AND country_iso_code='IT';
UPDATE lams_supported_locale SET fckeditor_code='no' WHERE language_iso_code='no' AND country_iso_code='NO';
UPDATE lams_supported_locale SET fckeditor_code='sv' WHERE language_iso_code='sv' AND country_iso_code='SE';
UPDATE lams_supported_locale SET fckeditor_code='ko' WHERE language_iso_code='ko' AND country_iso_code='KR';
UPDATE lams_supported_locale SET fckeditor_code='pl' WHERE language_iso_code='pl' AND country_iso_code='PL';
UPDATE lams_supported_locale SET fckeditor_code='pt-br' WHERE language_iso_code='pt' AND country_iso_code='BR';
UPDATE lams_supported_locale SET fckeditor_code='hu' WHERE language_iso_code='hu' AND country_iso_code='HU';
UPDATE lams_supported_locale SET fckeditor_code='bg' WHERE language_iso_code='bg' AND country_iso_code='BG';
UPDATE lams_supported_locale SET fckeditor_code='en-au' WHERE language_iso_code='cy' AND country_iso_code='GB';
UPDATE lams_supported_locale SET fckeditor_code='th' WHERE language_iso_code='th' AND country_iso_code='TH';
UPDATE lams_supported_locale SET fckeditor_code='el' WHERE language_iso_code='el' AND country_iso_code='GR';
UPDATE lams_supported_locale SET fckeditor_code='nl' WHERE language_iso_code='nl' AND country_iso_code='BE';
UPDATE lams_supported_locale SET fckeditor_code='ar' WHERE language_iso_code='ar' AND country_iso_code='JO';
UPDATE lams_supported_locale SET fckeditor_code='da' WHERE language_iso_code='da' AND country_iso_code='DK';
UPDATE lams_supported_locale SET fckeditor_code='ru' WHERE language_iso_code='ru' AND country_iso_code='RU';
UPDATE lams_supported_locale SET fckeditor_code='vi' WHERE language_iso_code='vi' AND country_iso_code='VN';
UPDATE lams_supported_locale SET fckeditor_code='zh' WHERE language_iso_code='zh' AND country_iso_code='TW';
UPDATE lams_supported_locale SET fckeditor_code='ja' WHERE language_iso_code='ja' AND country_iso_code='JP';
UPDATE lams_supported_locale SET fckeditor_code='ms' WHERE language_iso_code='ms' AND country_iso_code='MY';

-- See LDEV-273
CREATE TABLE lams_password_request (
       request_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL
     , request_key VARCHAR(32) NOT NULL
     , request_date DATETIME NOT NULL
     , PRIMARY KEY (request_id)
)TYPE=InnoDB;
CREATE UNIQUE INDEX IX_lams_psswd_rqst_key ON lams_password_request (request_key ASC);

CREATE INDEX email ON lams_user (email ASC);

