SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- delete all entried from  lams_supported_locale 
DELETE FROM  lams_supported_locale;

-- Add again all supported Locales  LDEV-2830
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (1, 'en', 'AU', 'English (Australia)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (2, 'es', 'ES', 'Español', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (3, 'mi', 'NZ', 'Māori', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (4, 'de', 'DE', 'Deutsch', 'LTR', 'de');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (5, 'zh', 'CN', '简体中文', 'LTR', 'zh-cn');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (6, 'fr', 'FR', 'Français', 'LTR', 'fr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (7, 'it', 'IT', 'Italiano', 'LTR', 'it');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (8, 'no', 'NO', 'Norsk', 'LTR', 'no');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (9, 'sv', 'SE', 'Svenska', 'LTR', 'sv');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (10, 'ko', 'KR', '한국어', 'LTR', 'ko');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (11, 'pl', 'PL', 'Polski', 'LTR', 'pl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (12, 'pt', 'BR', 'Português (Brasil)', 'LTR', 'pt-br');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (13, 'hu', 'HU', 'Magyar', 'LTR', 'hu');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (14, 'bg', 'BG', 'Български', 'LTR', 'bg');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (15, 'cy', 'GB', 'Cymraeg (Cymru)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (16, 'th', 'TH', 'Thai', 'LTR', 'th');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (17, 'el', 'GR', 'Ελληνικά', 'LTR', 'el');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (18, 'nl', 'BE', 'Nederlands (België)', 'LTR', 'nl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (19, 'ar', 'JO', 'عربي', 'RTL', 'ar');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (20, 'da', 'DK', 'Dansk', 'LTR', 'da');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (21, 'ru', 'RU', 'Русский', 'LTR', 'ru');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (22, 'vi', 'VN', 'Tiếng Việt', 'LTR', 'vi');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (23, 'zh', 'TW', 'Chinese (Taiwan)', 'LTR', 'zh');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (24, 'ja', 'JP', '日本語', 'LTR', 'ja');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (25, 'ms', 'MY', 'Malay (Malaysia)', 'LTR', 'ms');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (26, 'tr', 'TR', 'Türkçe', 'LTR', 'tr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (27, 'ca', 'ES', 'Català', 'LTR', 'ca');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (28, 'sl', 'SI', 'Slovenščina', 'LTR', 'sl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (29, 'es', 'MX', 'Español (México)', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (30, 'cs', 'CZ', 'Čeština', 'LTR', 'cs');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (31, 'id', 'ID', 'Indonesian', 'LTR', 'en-au');


SET FOREIGN_KEY_CHECKS=1;
COMMIT;
SET AUTOCOMMIT = 1;
