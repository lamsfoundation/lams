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
package org.lamsfoundation.lams.integration.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Fei Yang, Anthony Xiao
 */
public class LoginRequestDispatcher {

	private static Logger log = Logger.getLogger(LoginRequestDispatcher.class);

	public static final String PARAM_USER_ID = "uid";

	public static final String PARAM_SERVER_ID = "sid";

	public static final String PARAM_TIMESTAMP = "ts";

	public static final String PARAM_HASH = "hash";

	public static final String PARAM_METHOD = "method";

	public static final String PARAM_COURSE_ID = "courseid";

	public static final String PARAM_COUNTRY = "country";

	public static final String PARAM_LANGUAGE = "lang";

	public static final String METHOD_AUTHOR = "author";

	public static final String METHOD_MONITOR = "monitor";

	public static final String METHOD_LEARNER = "learner";

	private static final String PARAM_LESSON_ID = "lsid";

	private static final String URL_DEFAULT = "/index.jsp";

	private static final String URL_AUTHOR = "/home.do?method=author";

	private static final String URL_LEARNER = "/home.do?method=learner&lessonID=";

	private static final String URL_MONITOR = "/home.do?method=monitorLesson&lessonID=";

	private static IIntegrationService integrationService = null;

	private static ILessonService lessonService = null;

	/**
	 * This method is called within LoginRequestValve and LoginRequestServlet.
	 * It simply fetch the method parameter from HttpServletRequest and build
	 * the url to redirect user to.
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestURL(HttpServletRequest request) throws ServletException {

		String method = request.getParameter(PARAM_METHOD);
		String lessonId = request.getParameter(PARAM_LESSON_ID);

		try {
			addUserToLessonClass(request, lessonId, method);
		} catch (UserInfoFetchException e) {
			throw new ServletException(e);
		}

		/** AUTHOR * */
		if (METHOD_AUTHOR.equals(method)) {
			return request.getContextPath() + URL_AUTHOR;
		}
		/** MONITOR * */
		else if (METHOD_MONITOR.equals(method) && lessonId != null) {
			return request.getContextPath() + URL_MONITOR + lessonId;
		}
		/** LEARNER * */
		else if (METHOD_LEARNER.equals(method) && lessonId != null) {
			return request.getContextPath() + URL_LEARNER + lessonId;
		} else {
			return request.getContextPath() + URL_DEFAULT;
		}
	}

	private static void addUserToLessonClass(HttpServletRequest request, String lessonId,
			String method) throws UserInfoFetchException {
		if (integrationService == null) {
			integrationService = (IntegrationService) WebApplicationContextUtils
					.getRequiredWebApplicationContext(request.getSession().getServletContext())
					.getBean("integrationService");
		}
		if (lessonService == null) {
			lessonService = (ILessonService) WebApplicationContextUtils
					.getRequiredWebApplicationContext(request.getSession().getServletContext())
					.getBean("lessonService");
		}
		String serverId = request.getParameter(PARAM_SERVER_ID);
		String extUsername = request.getParameter(PARAM_USER_ID);
		ExtServerOrgMap serverMap = integrationService.getExtServerOrgMap(serverId);
		ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(serverMap, extUsername);
		User user = userMap.getUser();
		if ( user == null ) {
			String error = "Unable to add user to lesson class as user is missing from the user map";
			log.error(error);
			throw new UserInfoFetchException(error);
		}
		
		if(METHOD_LEARNER.equals(method))
			lessonService.addLearner(Long.parseLong(lessonId), user.getUserId());
		else if(METHOD_MONITOR.equals(method))
			lessonService.addStaffMember(Long.parseLong(lessonId), user.getUserId());
	}
}
