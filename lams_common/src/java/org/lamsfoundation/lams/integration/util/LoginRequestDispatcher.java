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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.web.util.AttributeNames;
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

    public static final String PARAM_IS_UPDATE_USER_DETAILS = "isUpdateUserDetails";

    public static final String PARAM_REQUEST_SRC = "requestSrc";

    public static final String PARAM_IS_POST_MESSAGE_TO_PARENT = "isPostMessageToParent";

    public static final String PARAM_FIRST_NAME = "firstName";

    public static final String PARAM_LAST_NAME = "lastName";

    public static final String PARAM_EMAIL = "email";

    public static final String PARAM_CUSTOM_CSV = "customCSV";

    public static final String PARAM_EXT_LMS_ID = "extlmsid";

    public static final String PARAM_MODE = "mode";

    public static final String MODE_GRADEBOOK = "gradebook";

    public static final String METHOD_AUTHOR = "author";

    public static final String METHOD_MONITOR = "monitor";

    public static final String METHOD_LEARNER = "learner";

    // the same as METHOD_LEARNER but additionally requires hash to contain lsId in order to prevent users tampering
    // with lesson id parameter
    public static final String METHOD_LEARNER_STRICT_AUTHENTICATION = "learnerStrictAuth";

    public static final String PARAM_LEARNING_DESIGN_ID = "ldId";

    public static final String PARAM_LESSON_ID = "lsid";

    private static final String URL_DEFAULT = "/index.jsp";

    private static final String URL_AUTHOR = "/home.do?method=author";

    private static final String URL_LEARNER = "/home.do?method=learner&lessonID=";

    private static final String URL_MONITOR = "/home.do?method=monitorLesson&lessonID=";

    private static final String URL_GRADEBOOK = "/services/Gradebook?";

    private static IIntegrationService integrationService = null;

    private static ILessonService lessonService = null;

    /**
     * This method is called within LoginRequestValve and LoginRequestServlet.
     * If there is a redirectURL parameter then this becomes the redirect, otherwise it
     * fetches the method parameter from HttpServletRequest and builds the redirect url.
     * If the method parameter is used and a lessonId is supplied, then the user is added
     * to the LessonClass.
     *
     * @param request
     * @return
     */

    public static String getRequestURL(HttpServletRequest request) throws ServletException {

	// get the location from an explicit parameter if it exists
	String redirect = request.getParameter("redirectURL");
	if (redirect != null) {
	    return request.getContextPath() + "/" + redirect;
	}

	String method = request.getParameter(PARAM_METHOD);
	String lessonId = request.getParameter(PARAM_LESSON_ID);
	String mode = request.getParameter(PARAM_MODE);

	if (lessonId != null) {
	    try {
		LoginRequestDispatcher.addUserToLessonClass(request, lessonId, method);
	    } catch (UserInfoFetchException e) {
		throw new ServletException(e);
	    } catch (UserInfoValidationException e) {
		throw new ServletException(e);
	    }
	}

	if (MODE_GRADEBOOK.equals(mode)) {
	    return request.getContextPath() + URL_GRADEBOOK + request.getQueryString();
	}
	/** AUTHOR * */
	else if (METHOD_AUTHOR.equals(method)) {
	    String requestSrc = request.getParameter(PARAM_REQUEST_SRC);
	    String notifyCloseURL = request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL);
	    String isPostMessageToParent = request.getParameter(PARAM_IS_POST_MESSAGE_TO_PARENT);
	    String ldID = request.getParameter(PARAM_LEARNING_DESIGN_ID);

	    // Custom CSV string to be used for tool adapters
	    String customCSV = request.getParameter(PARAM_CUSTOM_CSV);
	    String extLmsId = request.getParameter(PARAM_SERVER_ID);

	    String parameters = "";

	    // append the extra parameters if they are present in the request
	    try {
		parameters = ldID != null ? parameters + "&learningDesignID" + "=" + ldID : parameters;
		parameters = customCSV != null ? parameters + "&" + PARAM_CUSTOM_CSV + "=" + customCSV : parameters;
		parameters = extLmsId != null ? parameters + "&" + PARAM_EXT_LMS_ID + "=" + extLmsId : parameters;
		parameters = requestSrc != null
			? parameters + "&" + PARAM_REQUEST_SRC + "=" + URLEncoder.encode(requestSrc, "UTF8")
			: parameters;
		parameters = notifyCloseURL != null ? parameters + "&" + AttributeNames.PARAM_NOTIFY_CLOSE_URL + "="
			+ URLEncoder.encode(notifyCloseURL, "UTF8") : parameters;
		parameters = isPostMessageToParent != null
			? parameters + "&" + PARAM_IS_POST_MESSAGE_TO_PARENT + "=" + isPostMessageToParent : parameters;
	    } catch (UnsupportedEncodingException e) {
		log.error(e);
	    }

	    return request.getContextPath() + URL_AUTHOR + parameters;
	}
	/** MONITOR * */
	else if (METHOD_MONITOR.equals(method) && lessonId != null) {
	    return request.getContextPath() + URL_MONITOR + lessonId;
	}
	/** LEARNER * */
	else if ((METHOD_LEARNER.equals(method) || METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method))
		&& lessonId != null) {
	    String url = request.getContextPath() + URL_LEARNER + lessonId;
	    if (mode != null) {
		url += "&" + PARAM_MODE + "=" + mode;
	    }
	    return url;
	} else {
	    return request.getContextPath() + URL_DEFAULT;
	}
    }

    private static void addUserToLessonClass(HttpServletRequest request, String lessonId, String method)
	    throws UserInfoFetchException, UserInfoValidationException {
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
	ExtServer extServer = integrationService.getExtServer(serverId);
	ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, extUsername);
	User user = userMap.getUser();
	if (user == null) {
	    String error = "Unable to add user to lesson class as user is missing from the user map";
	    log.error(error);
	    throw new UserInfoFetchException(error);
	}

	if (METHOD_LEARNER.equals(method) || METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method)) {
	    lessonService.addLearner(Long.parseLong(lessonId), user.getUserId());
	} else if (METHOD_MONITOR.equals(method)) {
	    lessonService.addStaffMember(Long.parseLong(lessonId), user.getUserId());
	}
    }
}
