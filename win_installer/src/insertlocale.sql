DELIMITER "//"

DROP PROCEDURE IF EXISTS insertlocale //
CREATE PROCEDURE insertlocale (IN _language VARCHAR(2), IN _locale VARCHAR(2), IN _description VARCHAR(255), IN _rtl  VARCHAR(3))
BEGIN

	if (select count(*) from lams_supported_locale where language_iso_code = _language and country_iso_code = _locale) = 0 then
		insert into lams_supported_locale
		(language_iso_code, country_iso_code, description, direction)
		values (_language,_locale,_description,_rtl);
	end if;
END //

DELIMITER //

DROP PROCEDURE IF EXISTS updateLocales //
CREATE PROCEDURE updateLocales ()
BEGIN
  	/*Currently supported languages
	 * To enter a new one, simply enter:
	 * insertLocale(language_iso_code, country_iso_code, description, direction)
	 * at the bottom of the code
	 */
	call insertLocale('en','AU','English (Australia)','LTR');
	call insertLocale('es','ES','Español','LTR');
	call insertLocale('mi', 'NZ', 'Māori', 'LTR');
	call insertLocale('de','DE','Deutsch','LTR');
	call insertLocale('zh', 'CN', '简体中文', 'LTR');
	call insertLocale('fr', 'FR', 'Français', 'LTR');
	call insertLocale('it', 'IT', 'Italiano', 'LTR');
	call insertLocale('no', 'NO', 'Norsk', 'LTR');
	call insertLocale('sv', 'SE', 'Svenska', 'LTR');
	call insertLocale('ko', 'KR', '한국어', 'LTR');
	call insertLocale('pl', 'PL', 'Polski', 'LTR');
	call insertLocale('pt', 'BR', 'Português (Brasil)', 'LTR');
	call insertLocale('hu', 'HU', 'Magyar', 'LTR');
	call insertLocale('bg', 'BG', 'Български', 'LTR');
	call insertLocale('cy', 'GB', 'Cymraeg (Cymru)', 'LTR');
	call insertLocale('th', 'TH', 'Thai', 'LTR');
	call insertLocale('el', 'GR', 'Ελληνικά', 'LTR');
	call insertLocale('nl', 'BE', 'Nederlands (BelgiÃƒÂ«)', 'LTR');
	call insertLocale('ar', 'JO', 'عربي', 'RTL');
	call insertLocale('da', 'DK', 'Dansk', 'LTR');
	call insertLocale('ru', 'RU', 'Русский', 'LTR');
	call insertLocale('vi', 'VN', 'Tiếng Việt', 'LTR');
	call insertLocale('zh', 'TW', 'Chinese (Taiwan)', 'LTR');
	commit;
END //

DELIMITER ;