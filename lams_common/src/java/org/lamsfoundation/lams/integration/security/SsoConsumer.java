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

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.impl.SecurityContextImpl;
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
 * Allows access to LAMS WARs if an user logged in. It pulls out user Account from shared session and notifies security
 * context that the user is already logged in.
 * 
 * TODO: Always allow access for static content like images or JS files. It can be done by putting an URI check here or
 * by adding an another auth mechanism in front of it.
 * 
 * @author Marcin Cieslak
 *
 */
public class SsoConsumer implements ServletExtension {
    private final static String MECHANISM_NAME = "LAMS SSO";
    private final static String SSO_ATTRIBUTE_NAME = "ssoAccount";

    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {

	// SSO goes first
	deploymentInfo.addFirstAuthenticationMechanism(SsoConsumer.MECHANISM_NAME, new AuthenticationMechanism() {

	    @Override
	    public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange, SecurityContext sc) {
		ServletRequestContext requestContext = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
		// pass authentication further if it is a non-processable request or the user is loggin in just now
		if (requestContext == null || exchange.getRequestURI().endsWith("j_security_check")) {
		    return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
		}

		// get the account information
		HttpServletRequest request = (HttpServletRequest) requestContext.getServletRequest();
		ServletResponse response = requestContext.getServletResponse();
		SessionManager.startSession(request, response);
		HttpSession session = SessionManager.getSession();
		Account ssoAccount = session == null ? null : (Account) session
			.getAttribute(SsoConsumer.SSO_ATTRIBUTE_NAME);
		SessionManager.endSession();

		if (ssoAccount != null) {
		    SecurityContextImpl securityContext = (SecurityContextImpl) sc;
		    Account account = securityContext.getAuthenticatedAccount();
		    if (account == null) {
			// Notify security context that the user is logged in.
			// Do not cache it as the user would still be logged in the given WAR,
			// even if he was logged out Central.
			securityContext.authenticationComplete(ssoAccount, SsoConsumer.MECHANISM_NAME, false);
		    }
		    return AuthenticationMechanismOutcome.AUTHENTICATED;
		}

		// Let other auth mechanisms do their checking
		return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
	    }

	    @Override
	    public ChallengeResult sendChallenge(HttpServerExchange exchange, SecurityContext securityContext) {
		return new ChallengeResult(false);
	    }
	});
    }
}