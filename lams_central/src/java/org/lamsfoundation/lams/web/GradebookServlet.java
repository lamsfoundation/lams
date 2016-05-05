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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Marcin Cieslak
 */
@SuppressWarnings("serial")
public class GradebookServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(GradebookServlet.class);

    private static IntegrationService integrationService = null;
    private static IUserManagementService userManagementService;

    private static final String GRADEBOOK_MONITOR_LESSON_URL = "gradebook/gradebookMonitoring.do?lessonID=";
    private static final String GRADEBOOK_MONITOR_ORGANISATION_URL = "gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=";
    private static final String GRADEBOOK_LEARNER_ORGANISATION_URL = "gradebook/gradebookLearning.do?dispatch=courseLearner&organisationID=";

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

	String username = request.getParameter(LoginRequestDispatcher.PARAM_USER_ID);
	String serverId = request.getParameter(LoginRequestDispatcher.PARAM_SERVER_ID);
	String timestamp = request.getParameter(LoginRequestDispatcher.PARAM_TIMESTAMP);
	String hash = request.getParameter(LoginRequestDispatcher.PARAM_HASH);
	String countryIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_COUNTRY);
	String langIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_LANGUAGE);
	String extCourseId = request.getParameter(LoginRequestDispatcher.PARAM_COURSE_ID);
	String lessonId = request.getParameter(LoginRequestDispatcher.PARAM_LESSON_ID);
	String courseName = request.getParameter(CentralConstants.PARAM_COURSE_NAME);
	String method = request.getParameter(LoginRequestDispatcher.PARAM_METHOD);

	// either lesson ID or course ID is required; if both provided, only lesson ID is used
	if ((username == null) || (serverId == null) || (timestamp == null) || (hash == null)
		|| ((lessonId == null) && (extCourseId == null))) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Gradebook request failed - invalid parameters");
	} else {
	    ExtServerOrgMap serverMap = getIntegrationService().getExtServerOrgMap(serverId);
	    try {
		// if request comes from LoginRequest, method parameter was meaningful there
		// if it's a direct call, it can be anything
		Authenticator.authenticate(serverMap, timestamp, username, method, hash);
		boolean isTeacher = StringUtils.equals(method, LoginRequestDispatcher.METHOD_AUTHOR)
			|| StringUtils.equals(method, LoginRequestDispatcher.METHOD_MONITOR);

		if (lessonId == null) {
		    String gradebookServletURL = isTeacher ? GradebookServlet.GRADEBOOK_MONITOR_ORGANISATION_URL
			    : GradebookServlet.GRADEBOOK_LEARNER_ORGANISATION_URL;

		    // translate external course ID to internal organisation ID and then get the gradebook for it
		    ExtUserUseridMap userMap = getIntegrationService().getExtUserUseridMap(serverMap, username);
		    ExtCourseClassMap orgMap = getIntegrationService().getExtCourseClassMap(serverMap, userMap,
			    extCourseId, courseName, countryIsoCode, langIsoCode,
			    getUserManagementService().getRootOrganisation().getOrganisationId().toString(), isTeacher,
			    false);
		    Organisation org = orgMap.getOrganisation();

		    response.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL) + gradebookServletURL
			    + org.getOrganisationId());
		} else {
		    if (isTeacher) {
			// get gradebook for particular lesson
			response.sendRedirect(Configuration.get(ConfigurationKeys.SERVER_URL)
				+ GradebookServlet.GRADEBOOK_MONITOR_LESSON_URL + lessonId);
		    } else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
				"There is no lesson level gradebook for learner");
		    }
		}
	    } catch (AuthenticationException e) {
		GradebookServlet.log.error(e);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login failed - authentication error");
	    } catch (UserInfoFetchException e) {
		GradebookServlet.log.error(e);
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"Gradebook request failed - user info fetch exception");
	    } catch (UserInfoValidationException e) {
		GradebookServlet.log.error(e);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Gradebook request failed. " + e.getMessage());
	    }
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

    private IntegrationService getIntegrationService() {
	if (GradebookServlet.integrationService == null) {
	    GradebookServlet.integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");
	}
	return GradebookServlet.integrationService;
    }

    protected IUserManagementService getUserManagementService() {
	if (GradebookServlet.userManagementService == null) {
	    GradebookServlet.userManagementService = (IUserManagementService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext())
		    .getBean(CentralConstants.USER_MANAGEMENT_SERVICE_BEAN_NAME);

	}
	return GradebookServlet.userManagementService;
    }
}