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
package org.lamsfoundation.lams.web;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.jsp.jstl.core.Config;

import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.filter.LocaleFilter;

/**
 * Listens for creation of HTTP sessions. Sets inactive timeout and default locale.
 */

public class SessionListener implements HttpSessionListener {
    private static int timeout; //in seconds

    static {
	SessionListener.timeout = Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME);
    }

    /** HttpSessionListener interface */
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
	if (sessionEvent == null) {
	    return;
	}
	HttpSession session = sessionEvent.getSession();
	session.setMaxInactiveInterval(SessionListener.timeout);

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

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
	//nothing to do
    }
}