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

package org.lamsfoundation.lams.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Various internationalisation (internationalization) utilities.
 *
 * @author Fiona Malikoff
 */
public class LanguageUtil {

    public static final String DEFAULT_LANGUAGE = "en";

    public static final String DEFAULT_COUNTRY = "AU";

    public static final String DEFAULT_DIRECTION = "LTR";

    private static IUserManagementService service;
    private static MessageService messageService;

    /**
     * Get the default language, country, based on entries in the server configuration file.
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
	    languageIsoCode = LanguageUtil.DEFAULT_LANGUAGE;
	}
	if (countryIsoCode == null) {
	    languageIsoCode = LanguageUtil.DEFAULT_COUNTRY;
	}

	return new String[] { languageIsoCode, countryIsoCode };

    }

    /**
     * Get the default direction, based on the values in the server configuration file.
     *
     * @return direction
     */
    public static String getDefaultDirection() {
	String direction = Configuration.get(ConfigurationKeys.SERVER_PAGE_DIRECTION);
	if (direction == null) {
	    direction = LanguageUtil.DEFAULT_DIRECTION;
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

    public static String getDefaultCountry() {
	String serverCountry = Configuration.get(ConfigurationKeys.SERVER_COUNTRY);
	return StringUtils.isBlank(serverCountry) ? LanguageUtil.DEFAULT_COUNTRY : serverCountry;
    }

    /**
     * Checks whether specified country belongs to the list of allowed country codes. If positive return it, and if not
     * - default country.
     *
     * @param input
     * @return
     */
    public static String getSupportedCountry(String input) {
	String country = input == null ? null : input.toUpperCase();
	if (StringUtils.isBlank(country) || !Arrays.asList(CommonConstants.COUNTRY_CODES).contains(country)) {
	    country = LanguageUtil.getDefaultCountry();
	}
	return country;
    }

    /**
     * Returns server default locale; if invalid, uses en_AU.
     */
    public static SupportedLocale getDefaultLocale() {
	String localeName = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
	String langIsoCode = LanguageUtil.DEFAULT_LANGUAGE;
	// try to use the server's country first
	String countryIsoCode = LanguageUtil.DEFAULT_COUNTRY;
	if (StringUtils.isNotBlank(localeName) && (localeName.length() > 2)) {
	    langIsoCode = localeName.substring(0, 2);
	    countryIsoCode = localeName.substring(3);
	}

	SupportedLocale locale = null;
	locale = LanguageUtil.getSupportedLocaleOrNull(langIsoCode, countryIsoCode);
	if (locale == null) {
	    // if default language and server do not yield result, default to en_AU
	    locale = LanguageUtil.getSupportedLocaleOrNull(LanguageUtil.DEFAULT_LANGUAGE, LanguageUtil.DEFAULT_COUNTRY);
	}

	return locale;
    }

    /**
     * Searches for a locale based on language, then country, matching the single input string. Otherwise returns server
     * default locale.
     */
    public static SupportedLocale getSupportedLocale(String input) {
	List list = LanguageUtil.getService().findByProperty(SupportedLocale.class, "languageIsoCode", input, true);
	if ((list != null) && (list.size() > 0)) {
	    return (SupportedLocale) list.get(0);
	} else {
	    list = LanguageUtil.getService().findByProperty(SupportedLocale.class, "countryIsoCode", input, true);
	    if ((list != null) && (list.size() > 0)) {
		return (SupportedLocale) list.get(0);
	    }
	}
	return LanguageUtil.getDefaultLocale();
    }

    /**
     * Wrapper method for getSupportedLocaleByNameOrLanguageCode(String input).
     */
    public static Locale getSupportedLocaleByNameOrLanguageCode(Locale locale) {
	String localeId = MessageFormat.format("{0}_{1}", locale.getLanguage(), locale.getCountry());
	SupportedLocale supportedLocale = LanguageUtil.getSupportedLocaleByNameOrLanguageCode(localeId);
	return new Locale(supportedLocale.getLanguageIsoCode(), supportedLocale.getCountryIsoCode());
    }

    /**
     * Searches for a supported locale based on the provided input, first assuming it has "xx_XX" format, then that the
     * first two letters is a language ISO code. Otherwise returns server default locale.
     */
    public static SupportedLocale getSupportedLocaleByNameOrLanguageCode(String input) {
	SupportedLocale locale = null;
	if (StringUtils.isNotBlank(input)) {

	    //check if input follows xx_XX format
	    if (input.length() == 5 && input.indexOf("_") == 2) {
		String localeLanguage = input.substring(0, 2).toLowerCase();
		String localeCountry = input.substring(3).toUpperCase();
		locale = LanguageUtil.getSupportedLocaleOrNull(localeLanguage, localeCountry);
	    }

	    //if exactly the same locale is not supported or input has another format - try to get the first available locale with requested language
	    if (locale == null && input.length() >= 2) {
		String localeLanguage = input.substring(0, 2).toLowerCase();

		List<SupportedLocale> list = LanguageUtil.getService().findByProperty(SupportedLocale.class,
			"languageIsoCode", localeLanguage, true);
		if ((list != null) && (list.size() > 0)) {
		    locale = list.get(0);
		}
	    }

	}

	if (locale == null) {
	    locale = LanguageUtil.getDefaultLocale();
	}

	return locale;
    }

    /**
     * Finds a locale based on language and/or country, use server locale if invalid.
     */
    public static SupportedLocale getSupportedLocale(String langIsoCode, String countryIsoCode) {
	SupportedLocale locale = LanguageUtil.getSupportedLocaleOrNull(langIsoCode, countryIsoCode);
	if (locale == null) {
	    locale = LanguageUtil.getDefaultLocale();
	}

	return locale;
    }

    // Given langIsoCode and countryIsoCode, returns SupportedLocale (null if
    // doesn't exist)
    private static SupportedLocale getSupportedLocaleOrNull(String langIsoCode, String countryIsoCode) {
	SupportedLocale locale = null;

	Map<String, Object> properties = new HashMap<>();

	if (StringUtils.isNotBlank(countryIsoCode)) {
	    properties.put("countryIsoCode", countryIsoCode.trim());
	}
	if (StringUtils.isNotBlank(langIsoCode)) {
	    properties.put("languageIsoCode", langIsoCode.trim());
	}

	if (properties.isEmpty()) {
	    return null;
	}

	List list = LanguageUtil.getService().findByProperties(SupportedLocale.class, properties, true);
	if ((list != null) && (list.size() > 0)) {
	    Collections.sort(list);
	    locale = (SupportedLocale) list.get(0);
	} else {
	    locale = null;
	}
	return locale;
    }

    /**
     * Get list of all available country names sorted alphabetically.
     *
     * @parama enforceUsingDefaultLocale in some rare cases (like with SignupAction) it's useful to enforce using LAMS
     *         server's default locale, instead of the system default one
     */
    public static Map<String, String> getCountryCodes(boolean enforceUsingDefaultLocale) {
	LanguageUtil.getMessageService();
	SupportedLocale lamsDefaultLocale = enforceUsingDefaultLocale ? LanguageUtil.getDefaultLocale() : null;

	Map<String, String> countryCodesMap = new HashMap<>();
	for (String countryCode : CommonConstants.COUNTRY_CODES) {
	    String countryName = enforceUsingDefaultLocale
		    ? messageService.getMessage("country." + countryCode, lamsDefaultLocale)
		    : messageService.getMessage("country." + countryCode);
	    countryCodesMap.put(countryCode, countryName);
	}

	//sort alphabetically
	return countryCodesMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
			LinkedHashMap::new));
    }

    private static IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }

    private static MessageService getMessageService() {
	if (messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    messageService = (MessageService) ctx.getBean("commonMessageService");
	}
	return messageService;
    }

}
