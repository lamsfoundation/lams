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

/**
 * Utility methods for handling the Help URLS.
 */
public class HelpUtil {

    private static final String LAMSFOUNDATION_TEXT = "lamsfoundation";
    private static final String LAMSDOCS = "lamsdocs";
    private static final String ENGLISH_LANGUAGE = "en";

    /**
     * Build an I18N'd tool url, based on the tool url in the database. If this is for a particular module, such as
     * "authoring", then the
     * module must be included and it will be converted to the wiki anchor format.
     *
     * @param baseToolURL
     *            Mandatory
     * @param toolSignature
     *            Mandatory
     * @param module
     *            Optional
     * @param languageCode
     *            Optional
     * @return
     */
    public static String constructToolURL(String baseToolURL, String toolSignature, String module,
	    String languageCode) {

	if (baseToolURL == null) {
	    return null;
	}

	String helpURL = HelpUtil.addLanguageToURL(baseToolURL, languageCode);

	if (module != null && module.length() > 0) {
	    helpURL = helpURL + module + "#" + toolSignature + module;
	}

	return helpURL;
    }

    /**
     * Build an I18N'd url, based on the url in the configuration table. This is normally used for admin module urls.
     *
     * @param page
     *            Mandatory
     * @param languageCode
     *            Optional
     * @return
     */
    public static String constructPageURL(String page, String languageCode) {
	return HelpUtil.addLanguageToURL(Configuration.get(ConfigurationKeys.HELP_URL), languageCode) + page;
    }

    private static String addLanguageToURL(String helpURL, String languageCode) {

	if (languageCode != null && !languageCode.equals(ENGLISH_LANGUAGE) && helpURL != null
		&& helpURL.indexOf(LAMSFOUNDATION_TEXT) != -1) {
	    // points to the LAMS Foundation site, so add the language to the path.
	    return helpURL.replace(LAMSDOCS, LAMSDOCS + languageCode);
	}

	return helpURL;
    }

}