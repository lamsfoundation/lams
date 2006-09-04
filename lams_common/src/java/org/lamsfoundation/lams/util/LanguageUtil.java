/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.util.TimeZone;


/**
 * Various internationalisation (internationalization) utilities.
 *  
 * @author Fiona Malikoff
 *
 */
public class LanguageUtil {

	public static final String DEFAULT_LANGUAGE = "en";
	public static final String DEFAULT_COUNTRY = "AU";
	public static final String DEFAULT_DIRECTION = "LTR";

	/** 
	 * Get the default language, country, based on entries in the 
	 * server configuration file. 
	 * 
	 * @return String[language, country]
	 */
	public static String[] getDefaultLangCountry() {

    	String languageIsoCode = null;
    	String countryIsoCode = null;

		String serverLang = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE); 
		if ( serverLang != null) {
			// assume either "en" or "en_AU" formats
			if ( serverLang.length() >= 2)
				languageIsoCode = serverLang.substring(0,2);
			if ( serverLang.length() >= 5)
				countryIsoCode = serverLang.substring(3,5);
		} 

		// fallback to en_AU
		if ( languageIsoCode == null )
			languageIsoCode = DEFAULT_LANGUAGE;
		if ( countryIsoCode == null )
			languageIsoCode = DEFAULT_COUNTRY;

		return new String[]{languageIsoCode, countryIsoCode};

	}
	
	/** 
	 * Get the default direction, based on the values in the server configuration file. 
	 * @return direction
	 */
	public static String getDefaultDirection() {
		String direction = Configuration.get(ConfigurationKeys.SERVER_PAGE_DIRECTION);
		if ( direction == null )
			direction = DEFAULT_DIRECTION;
		return direction;
	}

	/** 
	 * Get the default timezone 
	 * @return timezone
	 */
	public static TimeZone getDefaultTimeZone() {
		return TimeZone.getDefault();
	}

}
