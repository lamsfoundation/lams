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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The LoginRequestServlet handles login request by an integrated external
 * system. This servlet checks for the UserId, Timestamp, Hash and ServerId if
 * it's valid it fetch the password from database and pass it to
 * j_security_check for authentication
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// create http session required by the valve
		// since valve can't create session
		/*HttpSession hses = request.getSession();
		if(hses!=null) 	hses.invalidate();
		HttpSession sharedsession = SessionManager.getSession();
		if(sharedsession!=null)	sharedsession.invalidate();*/
		HttpSession hses = request.getSession(true);

		String extUsername = request.getParameter(LoginRequestDispatcher.PARAM_USER_ID);
		String serverId = request.getParameter(LoginRequestDispatcher.PARAM_SERVER_ID);
		String extCourseId = request.getParameter(LoginRequestDispatcher.PARAM_COURSE_ID);
		String timestamp = request.getParameter(LoginRequestDispatcher.PARAM_TIMESTAMP);
		String hash = request.getParameter(LoginRequestDispatcher.PARAM_HASH);
		String method = request.getParameter(LoginRequestDispatcher.PARAM_METHOD);
		String countryIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_COUNTRY);
		String langIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_LANGUAGE);

		if (extUsername == null || method == null || serverId == null || timestamp == null
				|| hash == null || extCourseId == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Login Failed - login parameters missing");
			return;
		}
		ExtServerOrgMap serverMap = getService().getExtServerOrgMap(serverId);

		try {
			ExtUserUseridMap userMap = getService().getExtUserUseridMap(serverMap, extUsername);
			Authenticator.authenticate(serverMap, timestamp, extUsername, method, hash);
			ExtCourseClassMap orgMap = getService().getExtCourseClassMap(serverMap, userMap, extCourseId, countryIsoCode, langIsoCode);
			User user = userMap.getUser();
			String login = user.getLogin();
			//was using hses.inNew() API to check if the external user has logged in yet,
			// but it seems not working. The "extUser" attribute works as a flag to indicate
			// if the user has logged in
			String loginRequestUsername = (String)hses.getAttribute("extUser");
			if(loginRequestUsername != null && loginRequestUsername.equals(login)){
				String url = LoginRequestDispatcher.getRequestURL(request);
				log.debug("redirect url - "+url);
				response.sendRedirect(response.encodeRedirectURL(url));
				return;
			} else if(loginRequestUsername != null && !loginRequestUsername.equals(login)){
	    	    hses.invalidate();
	    	    hses = request.getSession(true);
			} else if(request.getRemoteUser() != null && loginRequestUsername == null){
	    	    hses.invalidate();
	    	    hses = request.getSession(true);
			}
			Organisation org = orgMap.getOrganisation();
			IUserManagementService userManagementService = integrationService.getService();
			UserOrganisation uo = userManagementService.getUserOrganisation(user.getUserId(), org.getOrganisationId());
			//COURSE_MANAGER role is to enable user to see the course's workspace folder
			Integer[] roleIds = new Integer[]{Role.ROLE_AUTHOR,Role.ROLE_COURSE_MANAGER,Role.ROLE_LEARNER};
			//we have to assign all the roles to the external user here, because once the user logged in, the roles
			//are cached in JBoss, all the calls of request.isUserInRole() will be based on the cached roles
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("userOrganisation.userOrganisationId", uo.getUserOrganisationId());
			for(Integer roleId : roleIds){
				properties.put("role.roleId", roleId);
				List list = userManagementService.findByProperties(UserOrganisationRole.class, properties);
				if(list==null || list.size()==0){
					UserOrganisationRole uor = new UserOrganisationRole(uo, (Role)userManagementService.findById(Role.class, roleId));
					userManagementService.save(uor);
				}		
			}
			log.debug("Session Id - "+hses.getId());
			// connect to DB and get password here
			String pass = getUserPassword(userMap.getUser().getLogin());
			// should post the parameters back so it's little more secure,
			// but forward doesn't work, use this until a better method is found
			hses.setAttribute("extUser", login);
			hses.setAttribute(AttributeNames.USER, user.getUserDTO());
			response.sendRedirect("j_security_check?j_username=" + login + "&j_password=" + pass);
		} catch (AuthenticationException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Login Failed - authentication error");
		} catch (UserInfoFetchException e) {
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, 
					"Login Failed - failed to fetch user info from the third party server");
		} catch (FailedLoginException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Login Failed - user was not found");
		} catch (NamingException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	// using JDBC connection to prevent the caching of passwords by hibernate
	private String getUserPassword(String username) throws FailedLoginException, NamingException,
			SQLException {
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
			if (rs.next() == false)
				throw new FailedLoginException("invalid username");

			password = rs.getString(1);
			rs.close();
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return password;
	}

	private IntegrationService getService() {
		if (integrationService == null) {
			integrationService = (IntegrationService) WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext()).getBean(
							"integrationService");
		}
		return integrationService;
	}
}
