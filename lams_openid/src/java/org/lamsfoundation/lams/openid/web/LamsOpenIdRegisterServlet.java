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
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
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
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A special servlet for registering open id users
 * User will be registered, put into an organisation and its lessons, then 
 * logged in
 * 
 * @author lfoxton
 * 
 */
public class LamsOpenIdRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LamsOpenIdRegisterServlet.class);

    private ILamsOpenIdService openIdService = null;

    /**
     * The doGet method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to get.
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

	try {
	    openIdService = LamsOpenIdServiceProxy.getLamsOpenIdService(getServletContext());
	    
	    HttpSession hses = request.getSession(true);

	    String extUsername = request.getParameter(LoginRequestDispatcher.PARAM_USER_ID);
	    String serverId = request.getParameter(LoginRequestDispatcher.PARAM_SERVER_ID);
	    String extCourseId = request.getParameter(LoginRequestDispatcher.PARAM_COURSE_ID);
	    String timestamp = request.getParameter(LoginRequestDispatcher.PARAM_TIMESTAMP);
	    String hash = request.getParameter(LoginRequestDispatcher.PARAM_HASH);
	    String method = request.getParameter(LoginRequestDispatcher.PARAM_METHOD);
	    String countryIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_COUNTRY);
	    String langIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_LANGUAGE);
	    String courseName = request.getParameter(CentralConstants.PARAM_COURSE_NAME);

	    // implicit login params
	    String firstName = request.getParameter(LoginRequestDispatcher.PARAM_FIRST_NAME);
	    String lastName = request.getParameter(LoginRequestDispatcher.PARAM_LAST_NAME);
	    String email = request.getParameter(LoginRequestDispatcher.PARAM_EMAIL);

	    if (extUsername == null || method == null || serverId == null || timestamp == null || hash == null
		    || extCourseId == null) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login Failed - login parameters missing");
		return;
	    }

	    // Get the server map using the server id
	    ExtServerOrgMap serverMap = openIdService.getExtServerOrgMap(serverId);
	    ExtUserUseridMap userMap = null;

	    // Authenticate the call using the hash
	    Authenticator.authenticate(serverMap, timestamp, extUsername, method, hash);
	    
	    // get the user if it exists, otherwise greate it
	    if (firstName == null && lastName == null) {
		userMap = openIdService.getExtUserUseridMap(serverMap, extUsername);
	    } else {
		userMap = openIdService.getImplicitExtUserUseridMap(serverMap, extUsername, firstName, lastName,
			langIsoCode, countryIsoCode, email);
	    }

	    // Get a handle on the user, userMap.getUser() was throwing hibernate errors
	    User user = openIdService.getUserByLogin(serverMap.getPrefix() + "_" + userMap.getExtUsername());
	    //User user = userMap.getUser();
	    
	    // Invalidate the session if there is an existing session
	    String login = user.getLogin();
	    String loginRequestUsername = (String) hses.getAttribute("extUser");
	    if (loginRequestUsername != null && loginRequestUsername.equals(login)) {
		hses.invalidate();
		hses = request.getSession(true);
	    } else if (loginRequestUsername != null && !loginRequestUsername.equals(login)) {
		hses.invalidate();
		hses = request.getSession(true);
	    } else if (request.getRemoteUser() != null && loginRequestUsername == null) {
		hses.invalidate();
		hses = request.getSession(true);
	    }

	    // adding the user to the specified group
	    Boolean addToGroup = WebUtil.readBooleanParam(request, "addToGroup");
	    if (addToGroup != null && addToGroup == Boolean.TRUE) {
		boolean addToGroupAsTeacher = WebUtil.readBooleanParam(request, "addToGroupAsTeacher", true);

		ExtCourseClassMap orgMap = openIdService.addUserToGroup(extUsername, serverId, timestamp, hash, extCourseId, courseName,
			countryIsoCode, langIsoCode, addToGroupAsTeacher);

		if (orgMap != null) {
		    
		    // Add the user to the specified group lessons
		    boolean addToGroupLessons = WebUtil.readBooleanParam(request, "addToGroupLessons", true);
		    if (addToGroupLessons) {
			boolean addToGroupLessonsAsStaff = WebUtil.readBooleanParam(request,
				"addToGroupLessonsAsStaff", true);
			openIdService.addUserToGroupLessons(extUsername, serverId, timestamp, hash, extCourseId, courseName,
				countryIsoCode, langIsoCode, addToGroupLessonsAsStaff, orgMap);
		    }
		}
	    }

	    log.debug("Session Id - " + hses.getId());
	    
	    // connect to DB and get password here
	    String pass = openIdService.getUserPassword(userMap.getUser().getLogin());

	    // Put the user info in the session 
	    hses.setAttribute("extUser", login);
	    hses.setAttribute(AttributeNames.USER, user.getUserDTO());
	    
	    // Send redirect to the login action
	    response.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL) + "j_security_check?j_username=" + URLEncoder.encode(login, "UTF8") + "&j_password=" + pass);
	} catch (Exception e) {
	    log.error("Error registering user in OpenID", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}
    }
}
