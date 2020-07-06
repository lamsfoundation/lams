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
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.UniversalLoginModule;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * The LoginRequestServlet handles login request by an integrated external system. This servlet checks for the UserId,
 * Timestamp, Hash and ServerId if it's valid it passes it to login.jsp
 *
 * @author Fei Yang, Anthony Xiao
 */
@SuppressWarnings("serial")
public class LoginRequestServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(LoginRequestServlet.class);

    private static final String URL_DEFAULT = "/index.jsp";

    private static final String URL_AUTHOR = "/home/author.do";

    private static final String URL_LEARNER = "/home/learner.do?lessonID=";

    private static final String URL_MONITOR = "/home/monitorLesson.do?lessonID=";

    private static final String URL_GRADEBOOK = "/services/Gradebook?";

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private MessageService centralMessageService;

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	HttpSession hses = request.getSession(true);

	String extUsername = request.getParameter(IntegrationConstants.PARAM_USER_ID);
	String serverId = request.getParameter(IntegrationConstants.PARAM_SERVER_ID);
	String extCourseId = request.getParameter(IntegrationConstants.PARAM_COURSE_ID);
	String timestamp = request.getParameter(IntegrationConstants.PARAM_TIMESTAMP);
	String hash = request.getParameter(IntegrationConstants.PARAM_HASH);
	String method = request.getParameter(IntegrationConstants.PARAM_METHOD);
	String country = request.getParameter(IntegrationConstants.PARAM_COUNTRY);
	String locale = request.getParameter(IntegrationConstants.PARAM_LANGUAGE);
	String courseName = request.getParameter(CentralConstants.PARAM_COURSE_NAME);
	String usePrefix = request.getParameter(CentralConstants.PARAM_USE_PREFIX);
	boolean isUpdateUserDetails = WebUtil.readBooleanParam(request,
		IntegrationConstants.PARAM_IS_UPDATE_USER_DETAILS, false);
	String lessonId = request.getParameter(IntegrationConstants.PARAM_LESSON_ID);

	// implicit login params
	String firstName = request.getParameter(IntegrationConstants.PARAM_FIRST_NAME);
	String lastName = request.getParameter(IntegrationConstants.PARAM_LAST_NAME);
	String email = request.getParameter(IntegrationConstants.PARAM_EMAIL);

	if ((extUsername == null) || (method == null) || (serverId == null) || (timestamp == null) || (hash == null)) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login Failed - login parameters missing");
	    return;
	}

	// LDEV-2196 preserve character encoding if necessary
	if (request.getCharacterEncoding() == null) {
	    log.debug("request.getCharacterEncoding is empty, parsing username and courseName as 8859_1 to UTF-8...");
	    extUsername = new String(extUsername.getBytes("8859_1"), "UTF-8");
	    if (courseName != null) {
		courseName = new String(courseName.getBytes("8859_1"), "UTF-8");
	    }
	}

	ExtServer extServer = integrationService.getExtServer(serverId);
	boolean prefix = (usePrefix == null) ? true : Boolean.parseBoolean(usePrefix);
	try {
	    // in case of request for learner with strict authentication check cache should also contain lessonId
	    if ((IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method)
		    || IntegrationConstants.METHOD_MONITOR.equals(method)) && StringUtils.isBlank(lessonId)) {
		//show different messages for LTI learners (as this is a typical expected scenario) and all others
		String errorMessage = IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method)
			&& extServer.isLtiConsumer()
				? centralMessageService.getMessage("message.lesson.not.started.cannot.participate")
				: "Login Failed - lsid parameter missing";
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
		return;
	    }

	    Authenticator.authenticateLoginRequest(extServer, timestamp, extUsername, method, lessonId, hash);

	    // LKC workflow automation
	    boolean isWorkflowAutomation = false;
	    if (method.equals(IntegrationConstants.WORKFLOW_AUTOMATION_METHOD_MONITOR)) {
		method = IntegrationConstants.METHOD_MONITOR;
		isWorkflowAutomation = true;
	    } else if (method.equals(IntegrationConstants.WORKFLOW_AUTOMATION_METHOD_LEARNER)) {
		method = IntegrationConstants.METHOD_LEARNER;
		isWorkflowAutomation = true;
	    }

	    String waEventId = null;
	    String waLearningActivityId = null;
	    // if we are processing workflow automation call, check for required parameters
	    if (isWorkflowAutomation) {
		waEventId = request.getParameter(IntegrationConstants.WORKFLOW_AUTOMATION_PARAM_EVENT_ID);
		waLearningActivityId = request
			.getParameter(IntegrationConstants.WORKFLOW_AUTOMATION_PARAM_LEARNING_ACTIVITY_ID);
		if (StringUtils.isBlank(waEventId) || StringUtils.isBlank(waLearningActivityId)) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
			    "Login Failed - Workflow Automation parameter eventId or learningActivityId is missing");
		    return;
		}
	    }

	    ExtUserUseridMap userMap = null;
	    // do not create user if we are processing Workflow Automation call
	    if (isWorkflowAutomation || (firstName == null && lastName == null)) {
		userMap = integrationService.getExtUserUseridMap(extServer, extUsername, prefix);
	    } else {
		userMap = integrationService.getImplicitExtUserUseridMap(extServer, extUsername, firstName, lastName,
			locale, country, email, prefix, isUpdateUserDetails);
	    }

	    if (extCourseId == null && StringUtils.isNotBlank(lessonId)) {
		// derive course ID from lesson ID
		ExtCourseClassMap classMap = integrationService.getExtCourseClassMap(extServer.getSid(),
			Long.parseLong(lessonId));
		if (classMap == null) {
		    log.warn("Lesson " + lessonId + " is not mapped to any course for server "
			    + extServer.getServername());
		} else {
		    extCourseId = classMap.getCourseid();
		}
	    }

	    if (extCourseId != null) {
		// check if organisation, ExtCourseClassMap and user roles exist and up-to-date, and if not update them
		integrationService.getExtCourseClassMap(extServer, userMap, extCourseId, courseName, method, prefix);
	    }

	    User user = userMap.getUser();
	    if (user == null) {
		String error = "Unable to add user to lesson class as user is missing from the user map";
		log.error(error);
		throw new UserInfoFetchException(error);
	    }

	    //adds users to the lesson with respective roles
	    if (StringUtils.isNotBlank(lessonId)) {
		if (IntegrationConstants.METHOD_LEARNER.equals(method)
			|| IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method)) {
		    lessonService.addLearner(Long.parseLong(lessonId), user.getUserId());

		} else if (IntegrationConstants.METHOD_MONITOR.equals(method)
			|| IntegrationConstants.METHOD_AUTHOR.equals(method)) {
		    lessonService.addStaffMember(Long.parseLong(lessonId), user.getUserId());
		}
	    }

	    String login = user.getLogin();
	    UserDTO loggedInUserDTO = (UserDTO) hses.getAttribute(AttributeNames.USER);
	    String loggedInLogin = loggedInUserDTO == null ? null : loggedInUserDTO.getLogin();
	    // for checking if requested role is the same as already assigned
	    String role = method.equals(IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION)
		    ? IntegrationConstants.METHOD_LEARNER
		    : method;
	    // check if there is a redirect URL parameter already
	    String requestUrl = null;

	    if (isWorkflowAutomation) {
		// build redirect URL which hashes event ID, learning activity ID, method and user ID
		String waHash = new StringBuilder(waEventId).append(waLearningActivityId).append(method)
			.append(user.getUserId()).append(extServer.getServerkey()).toString();
		waHash = HashUtil.sha1(waHash);
		StringBuilder redirectURLBuilder = new StringBuilder("/lams/wa/home.do?")
			.append(IntegrationConstants.WORKFLOW_AUTOMATION_PARAM_EVENT_ID).append("=").append(waEventId)
			.append("&").append(IntegrationConstants.WORKFLOW_AUTOMATION_PARAM_LEARNING_ACTIVITY_ID)
			.append("=").append(waLearningActivityId).append("&").append(IntegrationConstants.PARAM_METHOD)
			.append("=").append(method).append("&").append(IntegrationConstants.PARAM_HASH).append("=")
			.append(waHash);
		requestUrl = redirectURLBuilder.toString();
	    }

	    if (requestUrl == null) {
		requestUrl = LoginRequestServlet.getRequestURL(request);
	    }
	    if ((loggedInLogin != null) && loggedInLogin.equals(login) && request.isUserInRole(role)) {
		response.sendRedirect(response.encodeRedirectURL(requestUrl));
		return;
	    }

	    // login.jsp knows what to do with these
	    hses.setAttribute("login", login);
	    String token = "#LAMS" + RandomPasswordGenerator.nextPassword(10);
	    hses.setAttribute("password", token);
	    // if there is logout URL at the integrated server, put it into session right now
	    // if authentication fails, it will be cleared
	    hses.setAttribute("integratedLogoutURL", extServer.getLogoutUrl());
	    // notify the login module that the user has been authenticated correctly
	    UniversalLoginModule.setAuthenticationToken(token);

	    String redirectURL = WebUtil.getBaseServerURL() + requestUrl;
	    redirectURL = URLEncoder.encode(redirectURL, "UTF-8");
	    response.sendRedirect("login.jsp?redirectURL=" + redirectURL);
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

    /**
     * If there is a redirectURL parameter then this becomes the redirect, otherwise it
     * fetches the method parameter from HttpServletRequest and builds the redirect url.
     */
    private static String getRequestURL(HttpServletRequest request) {
	String method = request.getParameter(IntegrationConstants.PARAM_METHOD);
	String lessonId = request.getParameter(IntegrationConstants.PARAM_LESSON_ID);
	String mode = request.getParameter(IntegrationConstants.PARAM_MODE);

	// get the location from an explicit parameter if it exists
	String redirect = request.getParameter("redirectURL");
	if (redirect != null) {
	    return request.getContextPath() + "/" + redirect;
	}

	if (IntegrationConstants.MODE_GRADEBOOK.equals(mode)) {
	    return request.getContextPath() + URL_GRADEBOOK + request.getQueryString();
	}
	/** AUTHOR * */
	else if (IntegrationConstants.METHOD_AUTHOR.equals(method)) {
	    String authorUrl = request.getContextPath() + URL_AUTHOR;

	    // append the extra parameters if they are present in the request
	    String ldID = request.getParameter(IntegrationConstants.PARAM_LEARNING_DESIGN_ID);
	    if (ldID != null) {
		authorUrl = WebUtil.appendParameterToURL(authorUrl, "learningDesignID", ldID);
	    }

	    // Custom CSV string to be used for tool adapters
	    String customCSV = request.getParameter(IntegrationConstants.PARAM_CUSTOM_CSV);
	    if (customCSV != null) {
		authorUrl = WebUtil.appendParameterToURL(authorUrl, IntegrationConstants.PARAM_CUSTOM_CSV, customCSV);
	    }

	    String extLmsId = request.getParameter(IntegrationConstants.PARAM_SERVER_ID);
	    if (extLmsId != null) {
		authorUrl = WebUtil.appendParameterToURL(authorUrl, IntegrationConstants.PARAM_EXT_LMS_ID, extLmsId);
	    }

	    return authorUrl;
	}
	/** MONITOR * */
	else if (IntegrationConstants.METHOD_MONITOR.equals(method) && lessonId != null) {
	    return request.getContextPath() + URL_MONITOR + lessonId;
	}
	/** LEARNER * */
	else if ((IntegrationConstants.METHOD_LEARNER.equals(method)
		|| IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method)) && lessonId != null) {
	    String url = request.getContextPath() + URL_LEARNER + lessonId;
	    if (mode != null) {
		url += "&" + IntegrationConstants.PARAM_MODE + "=" + mode;
	    }
	    return url;
	} else {
	    return request.getContextPath() + URL_DEFAULT;
	}
    }
}
