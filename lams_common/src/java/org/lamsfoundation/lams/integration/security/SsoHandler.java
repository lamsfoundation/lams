/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.integration.security;

import java.security.AccessController;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.undertow.Handlers;
import io.undertow.server.session.Session;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpSessionImpl;

/**
 * Allows access to LAMS WARs when an user logs in.
 *
 * @author Marcin Cieslak
 *
 */
public class SsoHandler implements ServletExtension {
    private static IUserManagementService userManagementService = null;

    protected static final String SESSION_KEY = "io.undertow.servlet.form.auth.redirect.location";

    // if this attribute is set in session, credential cache will not be cleared on session destro in SessionListener
    public static final String NO_FLUSH_FLAG = "noFlush";

    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
	// expose servlet context so other classes can use it
	SessionManager.setServletContext(servletContext);

	// run when request and response were already parsed, but before security handlers
	deploymentInfo.addOuterHandlerChainWrapper(handler -> {
	    // just forward all requests except one for logging in
	    return Handlers.path().addPrefixPath("/", handler).addExactPath("/j_security_check", exchange -> {
		ServletRequestContext context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
		HttpServletRequest request = (HttpServletRequest) context.getServletRequest();

		// initialise jvmRoute for runtime statistics servlet
		if (SessionManager.getJvmRoute() == null) {
		    SsoHandler.setJvmRoute(request);
		}

		// recreate session here in case it was invalidated in login.jsp by sysadmin's LoginAs
		HttpSession session = request.getSession();

		// LoginRequestServlet (integrations) and LoginAsAction (sysadmin) set this parameter
		String redirectURL = request.getParameter("redirectURL");
		if (!StringUtils.isBlank(redirectURL)) {
		    SsoHandler.handleRedirectBack(context, redirectURL);
		}

		/*
		 * Fetch UserDTO before completing request so putting it later in session is done ASAP
		 * Response is sent in another thread and if UserDTO is not present in session when browser completes
		 * redirect,
		 * it results in error. Winning this race is the easiest option.
		 */
		UserDTO userDTO = null;
		String login = request.getParameter("j_username");
		if (!StringUtils.isBlank(login)) {
		    User user = getUserManagementService(session.getServletContext()).getUserByLogin(login);
		    if (user != null) {
			userDTO = user.getUserDTO();
		    }
		}

		// store session so UniversalLoginModule can access it
		SessionManager.startSession(request);

		// do the logging in UniversalLoginModule or cache
		handler.handleRequest(exchange);

		if (!StringUtils.isBlank(login) && login.equals(request.getRemoteUser())) {
		    session.setAttribute(AttributeNames.USER, userDTO);

		    HttpSession existingSession = SessionManager.getSessionForLogin(login);
		    if (existingSession != null) {
			// tell SessionListener not to flush credential cache on session destroy,
			// otherwise this authentication processs fails
			existingSession.setAttribute(NO_FLUSH_FLAG, true);
			// remove an existing session for the given user
			SessionManager.removeSession(login, true);
		    }
		    // register current session as the only one for the given user
		    SessionManager.addSession(login, session);
		}

		SessionManager.endSession();
	    });
	});
    }

    /**
     * Notifies authentication mechanism where it should redirect after log in. Based on
     * ServletFormAuthenticationMechanism method.
     */
    protected static void handleRedirectBack(ServletRequestContext context, String redirectURL) {
	HttpSessionImpl httpSession = context.getCurrentServletContext().getSession(context.getExchange(), true);
	if (httpSession != null) {
	    Session session;
	    if (System.getSecurityManager() == null) {
		session = httpSession.getSession();
	    } else {
		session = AccessController.doPrivileged(new HttpSessionImpl.UnwrapSessionAction(httpSession));
	    }

	    session.setAttribute(SsoHandler.SESSION_KEY, redirectURL);
	}
    }

    /**
     * Memorises jvmRoute if it is already set.
     */
    protected static void setJvmRoute(HttpServletRequest request) {
	// if previous requests has not created a session, cookies will be null
	Cookie[] cookies = request.getCookies();
	if (cookies == null) {
	    return;
	}

	for (Cookie cookie : cookies) {
	    if (cookie.getName().equals("JSESSIONID")) {
		// look for jvmRoute
		int index = cookie.getValue().indexOf('.');
		if (index > 0) {
		    SessionManager.setJvmRoute(cookie.getValue().substring(index + 1));
		    return;
		}
	    }
	}
    }

    protected IUserManagementService getUserManagementService(ServletContext context) {
	if (SsoHandler.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    SsoHandler.userManagementService = (UserManagementService) ctx.getBean("userManagementService");
	}
	return SsoHandler.userManagementService;
    }
}