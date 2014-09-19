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
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.handlers.ServletRequestContext;

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
    private final static String SSO_ATTRIBUTE_NAME = "ssoAccount";

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

			// create session so UniversalLoginModule can access it
			SessionManager.startSession(request, response);
			// do the logging in UniversalLoginModule
			handler.handleRequest(exchange);

			HttpSession session = SessionManager.getSession();
			// get the just-logged-in user account and put it in the shared session
			Account account = exchange.getSecurityContext().getAuthenticatedAccount();
			session.setAttribute(SsoProducer.SSO_ATTRIBUTE_NAME, account);

			SessionManager.endSession();
		    }
		});
	    }
	});
    }
}