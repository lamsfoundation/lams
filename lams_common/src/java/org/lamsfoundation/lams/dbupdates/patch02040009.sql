SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;

-- delete all entried from  lams_supported_locale 
DELETE FROM  lams_supported_locale;

-- Add again all supported Locales  LDEV-2830
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (1, 'en', 'AU', 'English (Australia)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (2, 'es', 'ES', 'EspaÃ±ol', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (3, 'mi', 'NZ', 'MÄori', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (4, 'de', 'DE', 'Deutsch', 'LTR', 'de');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (5, 'zh', 'CN', 'ç®€ä½“ä¸­æ–‡', 'LTR', 'zh-cn');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (6, 'fr', 'FR', 'FranÃ§ais', 'LTR', 'fr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (7, 'it', 'IT', 'Italiano', 'LTR', 'it');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (8, 'no', 'NO', 'Norsk', 'LTR', 'no');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (9, 'sv', 'SE', 'Svenska', 'LTR', 'sv');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (10, 'ko', 'KR', 'í•œêµ­ì–´', 'LTR', 'ko');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (11, 'pl', 'PL', 'Polski', 'LTR', 'pl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (12, 'pt', 'BR', 'PortuguÃªs (Brasil)', 'LTR', 'pt-br');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (13, 'hu', 'HU', 'Magyar', 'LTR', 'hu');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (14, 'bg', 'BG', 'Ð‘ÑŠÐ»Ð³Ð°Ñ€ÑÐºÐ¸', 'LTR', 'bg');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (15, 'cy', 'GB', 'Cymraeg (Cymru)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (16, 'th', 'TH', 'Thai', 'LTR', 'th');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (17, 'el', 'GR', 'Î•Î»Î»Î·Î½Î¹ÎºÎ¬', 'LTR', 'el');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (18, 'nl', 'BE', 'Nederlands (BelgiÃ«)', 'LTR', 'nl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (19, 'ar', 'JO', 'Ø¹Ø±Ø¨ÙŠ', 'RTL', 'ar');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (20, 'da', 'DK', 'Dansk', 'LTR', 'da');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (21, 'ru', 'RU', 'Ð ÑƒÑÑÐºÐ¸Ð¹', 'LTR', 'ru');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (22, 'vi', 'VN', 'Tiáº¿ng Viá»‡t', 'LTR', 'vi');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (23, 'zh', 'TW', 'Chinese (Taiwan)', 'LTR', 'zh');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (24, 'ja', 'JP', 'æ—¥æœ¬èªž', 'LTR', 'ja');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (25, 'ms', 'MY', 'Malay (Malaysia)', 'LTR', 'ms');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (26, 'tr', 'TR', 'TÃ¼rkÃ§e', 'LTR', 'tr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (27, 'ca', 'ES', 'CatalÃ ', 'LTR', 'ca');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (28, 'sl', 'SI', 'SlovenÅ¡Äina', 'LTR', 'sl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (29, 'es', 'MX', 'EspaÃ±ol (MÃ©xico)', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (30, 'cs', 'CZ', 'ÄŒeÅ¡tina', 'LTR', 'cs');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (31, 'id', 'ID', 'Indonesian', 'LTR', 'en-au');

SET FOREIGN_KEY_CHECKS=1;
COMMIT;
SET AUTOCOMMIT = 1;
