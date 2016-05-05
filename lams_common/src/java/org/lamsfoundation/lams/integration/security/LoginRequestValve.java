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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.apache.catalina.authenticator.Constants;
import org.apache.catalina.authenticator.SavedRequest;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;

/**
 * When j_security_check authentication is successful the user is redirected to the original requested URL. The
 * LoginRequestValve is responsible for setting the original request URL to trick j_security_check
 *
 * @author Anthony Xiao, Fei Yang
 */
public class LoginRequestValve extends ValveBase {

    private static final Logger log = Logger.getLogger(LoginRequestValve.class);

    // Declare the constants
    private static final String PARAM_USERID = "uid";

    private static final String LOGIN_REQUEST = "LoginRequest";

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
	// Skip logging for non-HTTP requests and responses
	if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
	    return;
	}

	// get HttpServletRequest
	HttpServletRequest hreq = request.getRequest();

	// invoke next valve,
	// so we can get internal session and manager
	getNext().invoke(request, response);

	// when coming back from LoginRequest save the redirect to catalina
	// internal session
	if (hreq.getRequestURI().endsWith(LoginRequestValve.LOGIN_REQUEST)) {
	    // Looking at response header to determine redirect location
	    boolean isLoginSuccessful = false;
	    String[] headerNames = response.getHeaderNames();
	    LoginRequestValve.log.info("There are " + headerNames.length + " headers in the response");
	    for (String name : headerNames) {
		String[] values = response.getHeaderValues(name);
		if (values.length > 0) {
		    LoginRequestValve.log.info(name + " = " + values[0]);
		    if (name.toLowerCase().equals("location")
			    && values[0].matches(".*" + Constants.FORM_ACTION + ".*")) {
			isLoginSuccessful = true;
		    }
		} else {
		    LoginRequestValve.log.info("empty header-" + name);
		}
	    }

	    // if login request is successful then it will redirected the page
	    // to j_security_check otherwise it's unsuccessful.
	    if (!isLoginSuccessful) {
		LoginRequestValve.log.info("LOGIN REQUEST DETECTED - BUT NO LOGIN IS CARRIED OUT");
	    } else {

		HttpSession hses = hreq.getSession(false);
		LoginRequestValve.log.debug("Session Id - " + hses.getId());
		String userid = hreq.getParameter(LoginRequestValve.PARAM_USERID);

		// get the location from either redirectURL or from the method setting
		String redirect = LoginRequestDispatcher.getRequestURL(hreq);

		// check required parameters
		if ((userid != null) && (redirect != null) && (hses != null)) {
		    LoginRequestValve.log.info("LOGIN REQUEST DETECTED - LOGIN SUCCESSFUL");
		    LoginRequestValve.log.info("character encoding of the request - " + request.getCharacterEncoding());
		    // redirect = URLDecoder.decode(redirect, "US-ASCII");
		    LoginRequestValve.log.info("Redirect URL - " + redirect);
		    // create catalina internal session
		    Session session = request.getContext().getManager().findSession(hses.getId());
		    // Create and populate a SavedRequest object for this
		    // request
		    SavedRequest saved = new SavedRequest();

		    // saved.setMethod("POST");
		    // saved.setQueryString("");
		    saved.setRequestURI(redirect);

		    // Tomcat's FormAuthenticator looks at
		    // Constants.FORM_REQUEST_NOTE
		    // for the redirect object
		    session.setNote(Constants.FORM_REQUEST_NOTE, saved);
		} else {
		    LoginRequestValve.log.info("LOGIN REQUEST DETECTED - BUT MISSING REQUIRED PARAM");
		}
	    }
	}
    }
}