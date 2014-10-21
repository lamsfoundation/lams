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
import io.undertow.security.idm.Account;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.Session;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpSessionImpl;
import io.undertow.util.Methods;

import java.security.AccessController;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.web.session.SessionManager;

/**
 * Allows access to LAMS WARs if an user logged in. It puts user Account into shared session so SsoConsumer in other
 * WARs can use it.
 * 
 * @author Marcin Cieslak
 *
 */
public class SsoProducer implements ServletExtension {
    protected static final String SSO_ATTRIBUTE_NAME = "ssoAccount";

    protected static final String SESSION_KEY = "io.undertow.servlet.form.auth.redirect.location";

    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
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
			ServletResponse response = context.getServletResponse();

			// LoginRequestServlet (integrations) sets this parameter
			String redirectURL = request.getParameter("redirectURL");
			if (redirectURL != null) {
			    SsoProducer.handleRedirectBack(context, redirectURL);
			}

			// create session so UniversalLoginModule can access it
			SessionManager.startSession(request, response);
			// do the logging in UniversalLoginModule
			exchange.setRequestMethod(Methods.POST);
			handler.handleRequest(exchange);

			HttpSession session = SessionManager.getSession();
			// get the just-logged-in user account and put it in the shared session
			Account account = exchange.getSecurityContext().getAuthenticatedAccount();
			if (account == null) {
			    session.removeAttribute(SsoProducer.SSO_ATTRIBUTE_NAME);
			} else {
			    session.setAttribute(SsoProducer.SSO_ATTRIBUTE_NAME, account);
			    if (redirectURL != null) {
				// there is a good chance that the redirectURL parameter came from integrations
				// if so, and the log in failed, remove the parameter so it can be attempted again
				HttpSession hses = request.getSession(false);
				if (hses != null) {
				    hses.removeAttribute("extUser");
				}
			    }
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
	HttpSessionImpl httpSession = context.getCurrentServletContext().getSession(context.getExchange(), false);
	if (httpSession != null) {
	    Session session;
	    if (System.getSecurityManager() == null) {
		session = httpSession.getSession();
	    } else {
		session = AccessController.doPrivileged(new HttpSessionImpl.UnwrapSessionAction(httpSession));
	    }

	    session.setAttribute(SsoProducer.SESSION_KEY, redirectURL);
	}
    }
}