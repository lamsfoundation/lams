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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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

    private static IUserManagementService service;

    private static IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }

    public static void setService(IUserManagementService service) {
	LanguageUtil.service = service;
    }

    /**
     * Get the default language, country, based on entries in the server
     * configuration file.
     *
     * @return String[language, country]
     */
    public static String[] getDefaultLangCountry() {

	String languageIsoCode = null;
	String countryIsoCode = null;

	String serverLang = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
	if (serverLang != null) {
	    // assume either "en" or "en_AU" formats
	    if (serverLang.length() >= 2) {
		languageIsoCode = serverLang.substring(0, 2);
	    }
	    if (serverLang.length() >= 5) {
		countryIsoCode = serverLang.substring(3, 5);
	    }
	}

	// fallback to en_AU
	if (languageIsoCode == null) {
	    languageIsoCode = DEFAULT_LANGUAGE;
	}
	if (countryIsoCode == null) {
	    languageIsoCode = DEFAULT_COUNTRY;
	}

	return new String[] { languageIsoCode, countryIsoCode };

    }

    /**
     * Get the default direction, based on the values in the server
     * configuration file.
     *
     * @return direction
     */
    public static String getDefaultDirection() {
	String direction = Configuration.get(ConfigurationKeys.SERVER_PAGE_DIRECTION);
	if (direction == null) {
	    direction = DEFAULT_DIRECTION;
	}
	return direction;
    }

    /**
     * Get the default timezone
     *
     * @return timezone
     */
    public static TimeZone getDefaultTimeZone() {
	return TimeZone.getDefault();
    }

    /**
     * Returns server default locale; if invalid, uses en_AU.
     */
    public static SupportedLocale getDefaultLocale() {
	String localeName = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
	String langIsoCode = DEFAULT_LANGUAGE;
	String countryIsoCode = DEFAULT_COUNTRY;
	if (StringUtils.isNotBlank(localeName) && localeName.length() > 2) {
	    langIsoCode = localeName.substring(0, 2);
	    countryIsoCode = localeName.substring(3);
	}

	SupportedLocale locale = null;
	locale = LanguageUtil.getSupportedLocaleOrNull(langIsoCode, countryIsoCode);
	if (locale == null) {
	    locale = LanguageUtil.getSupportedLocaleOrNull(DEFAULT_LANGUAGE, DEFAULT_COUNTRY);
	}

	return locale;
    }

    /**
     * Searches for a locale based on language, then country, matching the
     * single input string. Otherwise returns server default locale.
     */
    public static SupportedLocale getSupportedLocale(String input) {
	List list = LanguageUtil.getService().findByProperty(SupportedLocale.class, "languageIsoCode", input);
	if (list != null && list.size() > 0) {
	    return (SupportedLocale) list.get(0);
	} else {
	    list = LanguageUtil.getService().findByProperty(SupportedLocale.class, "countryIsoCode", input);
	    if (list != null && list.size() > 0) {
		return (SupportedLocale) list.get(0);
	    }
	}
	return LanguageUtil.getDefaultLocale();
    }

    /**
     * Finds a locale based on language and/or country, use server locale if
     * invalid.
     */
    public static SupportedLocale getSupportedLocale(String langIsoCode, String countryIsoCode) {
	SupportedLocale locale = null;

	locale = LanguageUtil.getSupportedLocaleOrNull(langIsoCode, countryIsoCode);
	if (locale == null) {
	    locale = LanguageUtil.getDefaultLocale();
	}

	return locale;
    }

    // Given langIsoCode and countryIsoCode, returns SupportedLocale (null if
    // doesn't exist)
    private static SupportedLocale getSupportedLocaleOrNull(String langIsoCode, String countryIsoCode) {
	SupportedLocale locale = null;

	Map<String, Object> properties = new HashMap<String, Object>();

	if (StringUtils.isNotBlank(countryIsoCode)) {
	    properties.put("countryIsoCode", countryIsoCode.trim());
	}
	if (StringUtils.isNotBlank(langIsoCode)) {
	    properties.put("languageIsoCode", langIsoCode.trim());
	}

	if (properties.isEmpty()) {
	    return null;
	}

	List list = LanguageUtil.getService().findByProperties(SupportedLocale.class, properties);
	if (list != null && list.size() > 0) {
	    Collections.sort(list);
	    locale = (SupportedLocale) list.get(0);
	} else {
	    locale = null;
	}
	return locale;
    }

}
