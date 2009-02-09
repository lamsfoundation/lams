/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.openid.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.openid.service.ILamsOpenIdService;
import org.lamsfoundation.lams.openid.service.LamsOpenIdServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.verisign.joid.consumer.OpenIdFilter;

/**
 * A special servlet for the openid management. This servlet manages requests
 * from openID servlets to login. If the user exists in lams, then it will log
 * them in, otherwise they will be sent to the register page with any user
 * information released by open id populating the page.
 * 
 * @author lfoxton
 * 
 */
public class LamsOpenIdServlet extends HttpServlet {

    private static final long serialVersionUID = -3815302208768159008L;

    private static Logger log = Logger.getLogger(LamsOpenIdServlet.class);

    private ILamsOpenIdService openIdService = null;

    private static final String LOGIN_REQUEST_SERVLET_URL = Configuration.get(ConfigurationKeys.SERVER_URL)
	    + "/LoginRequest?";
    private static final String REGISTER_URL = Configuration.get(ConfigurationKeys.SERVER_URL)
	    + "/openid/register_openid.jsp?";

    /**
     * The doGet method of the servlet. <br>
     * 
     * @param request
     *                the request send by the client to the server
     * @param response
     *                the response send by the server to the client
     * @throws ServletException
     *                 if an error occurred
     * @throws IOException
     *                 if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	openIdService = LamsOpenIdServiceProxy.getLamsOpenIdService(getServletContext());

	HttpSession session = request.getSession(true);

	String loggedInAs = OpenIdFilter.getCurrentUser(session);

	// First check that openid login was successful
	if (loggedInAs == null) {
	    log.error("Did not get a user from open id");
	    throw new ServletException("Did not get a user from open id");
	}

	//String extUsername = request.getParameter(LoginRequestDispatcher.PARAM_USER_ID);
	String serverId = request.getParameter(LoginRequestDispatcher.PARAM_SERVER_ID);
	//String extCourseId = request.getParameter(LoginRequestDispatcher.PARAM_COURSE_ID);
	String timestamp = request.getParameter(LoginRequestDispatcher.PARAM_TIMESTAMP);
	String hash = request.getParameter(LoginRequestDispatcher.PARAM_HASH);
	String method = request.getParameter(LoginRequestDispatcher.PARAM_METHOD);
	//String countryIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_COUNTRY);
	//String langIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_LANGUAGE);
	//String courseName = request.getParameter(CentralConstants.PARAM_COURSE_NAME);

	try {
	    ExtServerOrgMap serverMap = openIdService.getExtServerOrgMap(serverId);

	    ExtUserUseridMap userMap = openIdService.getExistingExtUserUseridMap(serverMap, loggedInAs);

	    Authenticator.authenticate(serverMap, timestamp, loggedInAs, method, hash);

	    if (userMap == null) {
		String registerURL = REGISTER_URL;
		registerURL += "&openid_url=" + loggedInAs;
		response.sendRedirect(registerURL);
	    } else {
		// User exists, do login
		User user = openIdService.getUserByLogin(serverMap.getPrefix() + "_" + loggedInAs);

		// Invalidate the session if there is one already existing
		String loginRequestUsername = (String) session.getAttribute("extUser");
		if (loginRequestUsername != null && loginRequestUsername.equals(loggedInAs)) {
		    session.invalidate();
		    session = request.getSession(true);
		} else if (loginRequestUsername != null && !loginRequestUsername.equals(loggedInAs)) {
		    session.invalidate();
		    session = request.getSession(true);
		} else if (request.getRemoteUser() != null && loginRequestUsername == null) {
		    session.invalidate();
		    session = request.getSession(true);
		}
			
		session.setAttribute("extUser", loggedInAs);
		session.setAttribute(AttributeNames.USER, user.getUserDTO());
		
		// Get the user password from the database
		String pass = openIdService.getUserPassword(user.getLogin());
		response.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL) + "j_security_check?j_username="
			+ URLEncoder.encode(user.getLogin(), "UTF8") + "&j_password=" + pass);
	    }

	} catch (Exception e) {
	    log.error("Open ID login error: ", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}
    }
}
