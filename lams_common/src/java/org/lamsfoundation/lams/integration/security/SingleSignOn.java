/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.integration.security;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.catalina.Session;
import org.apache.catalina.authenticator.Constants;
import org.apache.catalina.authenticator.SingleSignOnEntry;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;

/**
 * <p>
 * <a href="SingleSignOn.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class SingleSignOn extends org.apache.catalina.authenticator.SingleSignOn {
    private static Logger log = Logger.getLogger(SingleSignOn.class);

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

	request.removeNote(Constants.REQ_SSOID_NOTE);

	// Has a valid user already been authenticated?
//		if (log.isDebugEnabled())
//			log.debug("Process request for '" + request.getRequestURI() + "'");
	Principal p = request.getUserPrincipal();
	String username = null;
	String password = null;
	if (request.getUserPrincipal() != null) {
//			if (log.isDebugEnabled())
//				log.debug(" Principal '" + request.getUserPrincipal().getName()
//						+ "' has already been authenticated");
	    getNext().invoke(request, response);
	    return;
	} else {
	    Session session = request.getSessionInternal(false);
	    if (session != null) {
		p = (Principal) session.getNote(Constants.FORM_PRINCIPAL_NOTE);
		username = (String) session.getNote(Constants.SESS_USERNAME_NOTE);
		username = (String) session.getNote(Constants.SESS_PASSWORD_NOTE);
	    }
	}

	// Check for the single sign on cookie
//		if (log.isDebugEnabled())
//			log.debug(" Checking for SSO cookie");
	Cookie cookie = null;
	Cookie cookies[] = request.getCookies();
	if (cookies == null) {
	    cookies = new Cookie[0];
	}
	for (int i = 0; i < cookies.length; i++) {
	    if (Constants.SINGLE_SIGN_ON_COOKIE.equals(cookies[i].getName())) {
		cookie = cookies[i];
		break;
	    }
	}

	if (cookie == null) {
//			if (log.isDebugEnabled())
//				log.debug(" SSO cookie is not present");
	    getNext().invoke(request, response);
	    return;
	}

	// Look up the cached Principal associated with this cookie value
//		if (log.isDebugEnabled())
//			log.debug(" Checking for cached principal for " + cookie.getValue());
	// register principal from internal session. This principal is set
	// in internal session by catalina FormAuthenticator
	if (p != null) {
//			log.debug("principal - " + p.getName());
	    register(cookie.getValue(), p, Constants.FORM_METHOD, username, password);
	}
	SingleSignOnEntry entry = lookup(cookie.getValue());
	if (entry != null) {
	    // if (log.isDebugEnabled())
	    // log.debug(" Found cached principal '" + entry.getPrincipal().getName()
	    // + "' with auth type '" + entry.getAuthType() + "'");
	    request.setNote(Constants.REQ_SSOID_NOTE, cookie.getValue());
	    // Only set security elements if reauthentication is not required
	    if (!getRequireReauthentication()) {
		request.setAuthType(entry.getAuthType());
		request.setUserPrincipal(entry.getPrincipal());
	    }
	}

	// Invoke the next Valve in our pipeline
	getNext().invoke(request, response);

    }
}
