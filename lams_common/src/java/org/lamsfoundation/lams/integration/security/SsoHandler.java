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

import io.undertow.Handlers;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.Session;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpSessionImpl;

import java.security.AccessController;

import javax.servlet.ServletContext;
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

/**
 * Allows access to LAMS WARs if an user logged in. It puts user Account into shared session so SsoConsumer in other
 * WARs can use it.
 * 
 * @author Marcin Cieslak
 *
 */
public class SsoHandler implements ServletExtension {
    private static IUserManagementService userManagementService = null;

    protected static final String SESSION_KEY = "io.undertow.servlet.form.auth.redirect.location";

    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
	// expose servlet context so other classes can use it
	SessionManager.setServletContext(servletContext);

	// run when request and response were already parsed, but before security handlers
	deploymentInfo.addOuterHandlerChainWrapper(new HandlerWrapper() {
	    @Override
	    public HttpHandler wrap(final HttpHandler handler) {
		// just forward all requests except one for logging in
		return Handlers.path().addPrefixPath("/", handler).addExactPath("/j_security_check", new HttpHandler() {
		    @Override
		    public void handleRequest(HttpServerExchange exchange) throws Exception {
			ServletRequestContext context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
			HttpServletRequest request = (HttpServletRequest) context.getServletRequest();

			// recreate session here in case it was invalidated in login.jsp by sysadmin's LoginAs 
			HttpSession session = request.getSession();

			// LoginRequestServlet (integrations) and LoginAsAction (sysadmin) set this parameter
			String redirectURL = request.getParameter("redirectURL");
			if (!StringUtils.isBlank(redirectURL)) {
			    SsoHandler.handleRedirectBack(context, redirectURL);
			}
			
			/* Fetch UserDTO before completing request so putting it later in session is done ASAP
			 * Response is sent in another thread and if UserDTO is not present in session when browser completes redirect,
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

			if (login.equals(request.getRemoteUser())) {
			    session.setAttribute(AttributeNames.USER, userDTO);
			}

			SessionManager.endSession();
		    }
		});
	    }
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

    protected IUserManagementService getUserManagementService(ServletContext context) {
	if (SsoHandler.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    SsoHandler.userManagementService = (UserManagementService) ctx.getBean("userManagementService");
	}
	return SsoHandler.userManagementService;
    }
}