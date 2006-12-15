DELIMITER $$

DROP PROCEDURE IF EXISTS insertlocale $$
CREATE PROCEDURE insertlocale (IN _language VARCHAR(2), IN _locale VARCHAR(2), IN _description VARCHAR(255), IN _rtl  VARCHAR(3))
BEGIN

	if (select count(*) from lams_supported_locale where language_iso_code = '_language' and country_iso_code = '_locale') = 0 then
		insert lams_supported_locale 
		(language_iso_code, country_iso_code, description, direction)
		values (_language,'_locale','_description','_rtl');
	end if;
END $$

DELIMITER ;


