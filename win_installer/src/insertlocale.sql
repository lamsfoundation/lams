DROP PROCEDURE IF EXISTS insertLocale
create procedure insertLocale 
	(IN _language VARCHAR(2), 
	 IN _locale VARCHAR(2), 
	 IN _description VARCHAR(??), 
	 IN _rtl  VARCHAR(3))
begin
	select count(*) into _numRows from lams_supported_locale 
		where language_iso_code = '_language' and country_iso_code = '_locale';

	if numRows = 0 then
		insert lams_supported_locale 
			(language_iso_code, country_iso_code, description, direction)
			values (_language,'_locale','_description','_rtl');
	end if;
end


