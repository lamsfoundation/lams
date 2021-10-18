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

package org.lamsfoundation.lams.web;

import java.security.Principal;
import java.util.Locale;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.log4j.Logger;
import org.jboss.security.CacheableManager;
import org.lamsfoundation.lams.security.SimplePrincipal;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.web.filter.LocaleFilter;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Listens for creation of HTTP sessions. Sets inactive timeout and default locale.
 */

public class SessionListener implements HttpSessionListener {
    private static CacheableManager<?, Principal> authenticationManager;

    private static Logger log = Logger.getLogger(SessionListener.class);

    /** HttpSessionListener interface */
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
	if (sessionEvent == null) {
	    return;
	}
	HttpSession session = sessionEvent.getSession();
	session.setMaxInactiveInterval(Configuration.getAsInt(ConfigurationKeys.INACTIVE_TIME));

	//set server default locale for STURTS and JSTL. This value should be overwrite
	//LocaleFilter class. But this part code can cope with login.jsp Locale.
	if (session != null) {
	    String defaults[] = LanguageUtil.getDefaultLangCountry();
	    Locale preferredLocale = new Locale(defaults[0] == null ? "" : defaults[0],
		    defaults[1] == null ? "" : defaults[1]);
	    session.setAttribute(LocaleFilter.PREFERRED_LOCALE_KEY, preferredLocale);
	    Config.set(session, Config.FMT_LOCALE, preferredLocale);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
	if (SessionListener.authenticationManager == null) {
	    try {
		InitialContext initialContext = new InitialContext();
		SessionListener.authenticationManager = (CacheableManager<?, Principal>) initialContext
			.lookup("java:jboss/jaas/lams/authenticationMgr");
	    } catch (NamingException e) {
		SessionListener.log.error("Error while getting authentication manager.", e);
	    }
	}

	// clear the authentication cache when the session is invalidated
	HttpSession session = sessionEvent.getSession();
	if (session != null) {
	    SessionManager.removeSessionByID(session.getId(), false, true);

	    UserDTO userDTO = (UserDTO) session.getAttribute(AttributeNames.USER);
	    if (userDTO != null) {
		String login = userDTO.getLogin();
		Principal principal = new SimplePrincipal(login);
		SessionListener.authenticationManager.flushCache(principal);
	    }
	}
    }
}