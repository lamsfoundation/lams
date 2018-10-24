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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.authoring.template;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import org.apache.log4j.Logger;

/**
 * I18N and other text utils used by the Template project.
 *
 */
public class TextUtil {
    private static Logger log = Logger.getLogger(TextUtil.class);

    /* ************************************** I18N related methods ************************************************* */
    /**
     * Find the message applying to key in msgBundle, and format using the given formatter (already configured for the
     * correct locale).
     *
     * @param msgBundle
     *            : Mandatory.
     * @param formatter
     *            : Must not be null if parameters is not null.
     * @param key
     *            : Mandatory.
     * @param parameters
     *            : Optional
     * @return
     */
    public static String getText(ResourceBundle msgBundle, MessageFormat formatter, String key, Object[] parameters) {
	String txt = null;
	try {
	    txt = msgBundle.getString(key);
	} catch (java.util.MissingResourceException e) {
	}

	if ((txt == null) || txt.isEmpty()) {
	    log.error("No boilerplate text found for key " + key);
	    return "";
	} else if ((parameters != null) && (formatter != null)) {
	    formatter.applyPattern(txt);
	    return formatter.format(parameters);
	} else {
	    return txt;
	}
    }

    /**
     * @param msgBundle
     *            : Mandatory.
     * @param formatter
     *            : Must not be null if parameters is not null.
     * @param key
     *            : Mandatory.
     * @param parameters
     *            : Optional
     * @return
     */
    public static String getText(ResourceBundle msgBundle, String key) {
	return TextUtil.getText(msgBundle, null, key, null);
    }

    /** Get the boilerplate text resource but do not cache! Then they can be changed on the fly. */
    public static ResourceBundle getBoilerplateBundle(Locale locale, String templateName) {
	String name = templateName != null ? "org.lamsfoundation.lams.central." + templateName + "Resources"
		: "org.lamsfoundation.lams.central.ApplicationResources";
	return ResourceBundle.getBundle(name, locale, new ResourceBundle.Control() {
	    @Override
	    public long getTimeToLive(String arg0, Locale arg1) {
		return Control.TTL_DONT_CACHE;
	    }
	});
    }

    public static MessageFormat getFormatter(Locale locale) {
	MessageFormat formatter = new MessageFormat("");
	formatter.setLocale(locale);
	return formatter;
    }

}
