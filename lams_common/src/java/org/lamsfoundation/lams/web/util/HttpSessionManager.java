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
package org.lamsfoundation.lams.web.util;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.jsp.jstl.core.Config;

import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.filter.LocaleFilter;

/**
 * Sets inactive timeout and locale in shared session. Also serves as a container for servlet context.
 */
public class HttpSessionManager {
    private static int timeout; //in seconds

    static {
	HttpSessionManager.timeout = Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME);
    }

    /**
     * Matches sessionCreated in SessionListener, which implements the HttpSessionListener interface. SessionListener
     * passes the session event to here. Can't be accessed directly (ie defined in web.xml) in this class as this class
     * is a singleton and the creation method is private.
     */
    public static void sessionCreated(HttpSessionEvent sessionEvent) {
	if (sessionEvent == null) {
	    return;
	}
	HttpSession session = sessionEvent.getSession();
	session.setMaxInactiveInterval(HttpSessionManager.timeout);

	//set server default locale for STURTS and JSTL. This value should be overwrite 
	//LocaleFilter class. But this part code can cope with login.jsp Locale.
	if (session != null) {
	    Locale preferredLocale = new Locale(Configuration.get(ConfigurationKeys.SERVER_LANGUAGE));
	    if (preferredLocale != null) {
		session.setAttribute(LocaleFilter.PREFERRED_LOCALE_KEY, preferredLocale);
		Config.set(session, Config.FMT_LOCALE, preferredLocale);
	    }
	}
    }
}