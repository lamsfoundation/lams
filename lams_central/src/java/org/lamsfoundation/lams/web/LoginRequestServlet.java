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
package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.catalina.authenticator.Constants;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The LoginRequestServlet handles login request by an integrated external system. This servlet checks for the UserId,
 * Timestamp, Hash and ServerId if it's valid it fetch the password from database and pass it to j_security_check for
 * authentication
 *
 * @author Fei Yang, Anthony Xiao
 */
@SuppressWarnings("serial")
public class LoginRequestServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(LoginRequestServlet.class);

    private static IntegrationService integrationService = null;

    private static final String JNDI_DATASOURCE = "java:/jdbc/lams-ds";

    private static final String PASSWORD_QUERY = "select password from lams_user where login=?";

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	String usePrefix = request.getParameter(CentralConstants.PARAM_USE_PREFIX);
	boolean isUpdateUserDetails = WebUtil.readBooleanParam(request,
		LoginRequestDispatcher.PARAM_IS_UPDATE_USER_DETAILS, false);

	// implicit login params
	String firstName = request.getParameter(LoginRequestDispatcher.PARAM_FIRST_NAME);
	String lastName = request.getParameter(LoginRequestDispatcher.PARAM_LAST_NAME);
	String email = request.getParameter(LoginRequestDispatcher.PARAM_EMAIL);

	if ((extUsername == null) || (method == null) || (serverId == null) || (timestamp == null) || (hash == null)) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login Failed - login parameters missing");
	    return;
	}

	// LDEV-2196 preserve character encoding if necessary
	if (request.getCharacterEncoding() == null) {
	    log.debug(
		    "request.getCharacterEncoding is empty, parsing username and courseName as 8859_1 to UTF-8...");
	    extUsername = new String(extUsername.getBytes("8859_1"), "UTF-8");
	    if (courseName != null) {
		courseName = new String(courseName.getBytes("8859_1"), "UTF-8");
	    }
	}

	ExtServer extServer = getIntegrationService().getExtServer(serverId);
	boolean prefix = (usePrefix == null) ? true : Boolean.parseBoolean(usePrefix);
	try {
	    ExtUserUseridMap userMap = null;
	    if ((firstName == null) && (lastName == null)) {
		userMap = getIntegrationService().getExtUserUseridMap(extServer, extUsername, prefix);
	    } else {
		userMap = getIntegrationService().getImplicitExtUserUseridMap(extServer, extUsername, firstName,
			lastName, langIsoCode, countryIsoCode, email, prefix, isUpdateUserDetails);
	    }

	    // in case of request for learner with strict authentication check cache should also contain lsid
	    String lsId = request.getParameter(LoginRequestDispatcher.PARAM_LESSON_ID);
	    if ((LoginRequestDispatcher.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method)
		    || LoginRequestDispatcher.METHOD_MONITOR.equals(method)) && lsId == null) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login Failed - lsId parameter missing");
		return;
	    }
	    Authenticator.authenticateLoginRequest(extServer, timestamp, extUsername, method, lsId, hash);

	    if (extCourseId == null && lsId != null) {
		// derive course ID from lesson ID
		ExtCourseClassMap classMap = integrationService.getExtCourseClassMap(extServer.getSid(),
			Long.parseLong(lsId));
		if (classMap == null) {
		    log.warn("Lesson " + lsId + " is not mapped to any course for server " + extServer.getServername());
		} else {
		    extCourseId = classMap.getCourseid();
		}
	    }

	    if (extCourseId != null) {
		// check if organisation, ExtCourseClassMap and user roles exist and up-to-date, and if not update them
		getIntegrationService().getExtCourseClassMap(extServer, userMap, extCourseId, countryIsoCode,
			langIsoCode, courseName, method, prefix);
	    }
	    
	    //in case of method=monitor is requested, check whether the user is lesson's monitor. And if not - add him
	    if (LoginRequestDispatcher.METHOD_MONITOR.equals(method)) {
		getIntegrationService().getLessonService().addStaffMember(Long.parseLong(lsId),
			userMap.getUser().getUserId());
	    }

	    User user = userMap.getUser();
	    String login = user.getLogin();
	    // The "extUser" attribute works as a flag to indicate if the user has logged in
	    String loginRequestUsername = (String) hses.getAttribute("extUser");
	    // for checking if requested role is the same as already assigned
	    String role = method.equals(LoginRequestDispatcher.METHOD_LEARNER_STRICT_AUTHENTICATION)
		    ? LoginRequestDispatcher.METHOD_LEARNER : method;
	    role = role.toUpperCase();
	    if ((loginRequestUsername != null) && loginRequestUsername.equals(login) && request.isUserInRole(role)) {
		String url = LoginRequestDispatcher.getRequestURL(request);
		response.sendRedirect(response.encodeRedirectURL(url));
		return;
	    } else if (loginRequestUsername == null ? request.getRemoteUser() != null
		    : (!loginRequestUsername.equals(login) || !request.isUserInRole(role))) {
		hses = recreateSession(request, response);
	    }

	    log.debug("Session Id - " + hses.getId());
	    // connect to DB and get password here
	    String pass = getUserPassword(userMap.getUser().getLogin());
	    // should post the parameters back so it's little more secure,
	    // but forward doesn't work, use this until a better method is found
	    hses.setAttribute("extUser", login);
	    hses.setAttribute(AttributeNames.USER, user.getUserDTO());

	    // for NTU Blackboard's based templates, force to https to co-exist with Blackboard
	    String redirect = request.getParameter("redirectURL");
	    if (redirect != null && redirect.indexOf("ldtemplate") >= 0) {
		response.sendRedirect("https://" + request.getServerName() + request.getContextPath()
			+ "/j_security_check?j_username=" + login + "&j_password=" + pass);
	    } else {
		response.sendRedirect("j_security_check?j_username=" + login + "&j_password=" + pass);
	    }
	} catch (AuthenticationException e) {
	    log.error("Authentication error: ", e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		    "Login Failed - authentication error. " + e.getMessage());
	} catch (UserInfoFetchException e) {
	    log.error("User fetch info error: ", e);
	    response.sendError(HttpServletResponse.SC_BAD_GATEWAY,
		    "Login Failed - failed to fetch user info from the third party server");
	} catch (UserInfoValidationException e) {
	    log.error("User validation error: ", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
	} catch (FailedLoginException e) {
	    log.error("Login error: ", e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login Failed - user was not found");
	} catch (NamingException e) {
	    log.error("Naming error: ", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	} catch (SQLException e) {
	    log.error("Database error: ", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    // using JDBC connection to prevent the caching of passwords by hibernate
    private String getUserPassword(String username) throws FailedLoginException, NamingException, SQLException {
	InitialContext ctx = new InitialContext();

	DataSource ds = (DataSource) ctx.lookup(JNDI_DATASOURCE);
	Connection conn = null;
	String password = null;
	try {
	    conn = ds.getConnection();
	    PreparedStatement ps = conn.prepareStatement(PASSWORD_QUERY);
	    ps.setString(1, username);
	    ResultSet rs = ps.executeQuery();

	    // check if there is any result
	    if (rs.next() == false) {
		throw new FailedLoginException("invalid username");
	    }

	    password = rs.getString(1);
	    rs.close();
	} finally {
	    if ((conn != null) && !conn.isClosed()) {
		conn.close();
	    }
	}
	return password;
    }

    private HttpSession recreateSession(HttpServletRequest request, HttpServletResponse response) {
	HttpSession hses = request.getSession(false);
	hses.invalidate();
	hses = request.getSession(true);

	Cookie cookies[] = request.getCookies();
	if (cookies != null) {
	    for (int i = 0; i < cookies.length; i++) {
		if (Constants.SINGLE_SIGN_ON_COOKIE.equals(cookies[i].getName())) {
		    Cookie cookie = new Cookie(cookies[i].getName(), "");
		    cookie.setPath("/");
		    cookie.setMaxAge(0);
		    response.addCookie(cookie);
		    break;
		}
	    }
	}
	return hses;
    }

    private IntegrationService getIntegrationService() {
	if (integrationService == null) {
	    integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");
	}
	return integrationService;
    }
}