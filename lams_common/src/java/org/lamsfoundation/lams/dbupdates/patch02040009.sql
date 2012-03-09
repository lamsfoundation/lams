-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Slovenian and Spanish MX where introduced in LAMS 2.3.5
-- LDEV-2830 Enabling Slovenian, Spanish (Mexico), Czech and Indonesian
-- INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) VALUES (28, 'sl', 'SI', 'Slovenščina', 'LTR', 'sl');
-- INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) VALUES (29, 'es', 'MX', 'Español (México)', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code)
VALUES (30, 'cs', 'CZ', 'Čeština', 'LTR', 'cs');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code)
VALUES (31, 'id', 'ID', 'Indonesian', 'LTR', 'en-au');

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
