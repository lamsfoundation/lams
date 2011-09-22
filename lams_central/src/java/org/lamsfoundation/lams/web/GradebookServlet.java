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

    private static final String GRADEBOOK_LESSON_URL = "gradebook/gradebookMonitoring.do?lessonID=";
    private static final String GRADEBOOK_ORGANISATION_URL = "gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID=";

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

	String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	String serverId = request.getParameter(LoginRequestDispatcher.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hash = request.getParameter(LoginRequestDispatcher.PARAM_HASH);
	String countryIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_COUNTRY);
	String langIsoCode = request.getParameter(LoginRequestDispatcher.PARAM_LANGUAGE);
	String extCourseId = request.getParameter(LoginRequestDispatcher.PARAM_COURSE_ID);
	String lessonId = request.getParameter(CentralConstants.PARAM_LESSON_ID);

	// either lesson ID or course ID is required; if both provided, only lesson ID is used
	if ((username == null) || (serverId == null) || (datetime == null) || (hash == null)
		|| ((lessonId == null) && (extCourseId == null))) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Gradebook request failed - invalid parameters");
	} else {
	    ExtServerOrgMap serverMap = getService().getExtServerOrgMap(serverId);
	    try {
		Authenticator.authenticate(serverMap, datetime, username, hash);
		String redirect = null;

		if (lessonId == null) {
		    // translate external course ID to internal organisation ID and then get the gradebook for it
		    ExtUserUseridMap userMap = GradebookServlet.integrationService.getExtUserUseridMap(serverMap,
			    username);
		    ExtCourseClassMap orgMap = GradebookServlet.integrationService.getExtCourseClassMap(serverMap,
			    userMap, extCourseId, countryIsoCode, langIsoCode, null, null);
		    Organisation org = orgMap.getOrganisation();
		    redirect = Configuration.get(ConfigurationKeys.SERVER_URL)
			    + GradebookServlet.GRADEBOOK_ORGANISATION_URL + org.getOrganisationId();
		} else {
		    // get gradebook for particular lesson
		    redirect = Configuration.get(ConfigurationKeys.SERVER_URL) + GradebookServlet.GRADEBOOK_LESSON_URL
			    + lessonId;
		}

		response.sendRedirect(redirect);
	    } catch (AuthenticationException e) {
		GradebookServlet.log.error(e);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login failed - authentication error");
	    } catch (UserInfoFetchException e) {
		GradebookServlet.log.error(e);
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"Gradebook request failed - user info fetch exception");
	    }
	}
    }

    private IntegrationService getService() {
	if (GradebookServlet.integrationService == null) {
	    GradebookServlet.integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");
	}
	return GradebookServlet.integrationService;
    }
}