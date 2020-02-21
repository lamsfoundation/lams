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
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.imsglobal.lti.BasicLTIConstants;
import org.imsglobal.lti.launch.LtiLaunch;
import org.imsglobal.lti.launch.LtiOauthVerifier;
import org.imsglobal.lti.launch.LtiVerificationException;
import org.imsglobal.lti.launch.LtiVerificationResult;
import org.imsglobal.lti.launch.LtiVerifier;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtServerLessonMap;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.integration.util.LtiUtils;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * The LoginRequestLtiServlet handles login request by LTI tool consumers. This servlet checks for the correctly signed
 * by OAuth parameters,
 * and if it's valid it redirects it to LoginRequestServlet for actual authentication.
 *
 * @author Andrey Balan
 */
@SuppressWarnings("serial")
public class LoginRequestLtiServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(LoginRequestLtiServlet.class);

    @Autowired
    private IntegrationService integrationService = null;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private IUserManagementService userManagementService;

    private final String DEFAULT_FIRST_NAME = "John";
    private final String DEFAULT_LAST_NAME = "Doe";

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	ExtServer extServer = integrationService.getExtServer(consumerKey);
	//get user id as "user_id" parameter, or as lis_person_sourcedid (if according option is ON for this LTI server)
	String lisPersonSourcedid = request.getParameter(BasicLTIConstants.LIS_PERSON_SOURCEDID);
	String extUsername = extServer.getUseAlternativeUseridParameterName()
		&& StringUtils.isNotBlank(lisPersonSourcedid) ? lisPersonSourcedid
			: request.getParameter(BasicLTIConstants.USER_ID);
	String roles = request.getParameter(BasicLTIConstants.ROLES);
	
	// implicit login params
	String firstName = request.getParameter(BasicLTIConstants.LIS_PERSON_NAME_GIVEN);
	String lastName = request.getParameter(BasicLTIConstants.LIS_PERSON_NAME_FAMILY);
	String email = request.getParameter(BasicLTIConstants.LIS_PERSON_CONTACT_EMAIL_PRIMARY);

	String locale = request.getParameter(BasicLTIConstants.LAUNCH_PRESENTATION_LOCALE);
	String countryIsoCode = LoginRequestLtiServlet.getCountry(locale);
	String langIsoCode = LoginRequestLtiServlet.getLanguage(locale);
	
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String contextId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
	String contextLabel = request.getParameter(BasicLTIConstants.CONTEXT_LABEL);
	
	//log all incoming request parameters, so we can use them later to debug future issues
	String logMessage = "LoginRequestLtiServlet is requested with the following parameters: ";
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            logMessage += paramName + "=";
 
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                if (i>0) {
                    logMessage += "|";
                }
                logMessage += paramValue;
            }
            logMessage += ", ";
        }
        log.debug(logMessage);

	if ((extUsername == null) || (consumerKey == null)) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Login Failed - login parameters missing");
	    return;
	}

	//verify whether request was correctly signed by OAuth
	String secret = extServer.getServerkey();// retrieve corresponding secret for key from db
	LtiVerificationResult ltiResult = null;
	try {
	    LtiVerifier ltiVerifier = new LtiOauthVerifier();
	    ltiResult = ltiVerifier.verify(request, secret);
	} catch (LtiVerificationException e) {
	    log.error("Authentication error: ", e);
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		    "Login Failed - authentication error. " + e.getMessage());
	    return;
	}
	LtiLaunch ltiLaunch = ltiResult.getLtiLaunchResult();
	if (!ltiResult.getSuccess()) {
	    log.warn("Authentication error: " + ltiResult.getMessage());
	    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
		    "Login Failed - authentication error. " + ltiResult.getMessage());
	    return;
	}

	//in case both first and last names are missing, try to get them using other ways
	if (StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
	    
	    //check LIS_PERSON_NAME_FULL parameter
	    String fullName = request.getParameter(BasicLTIConstants.LIS_PERSON_NAME_FULL);
	    if (StringUtils.isNotBlank(fullName)) {
		firstName = fullName;
		lastName = " ";
		
	    } else {
		//provide default values for user names, as we can't fetch them from LTI Tool consumer
		firstName = DEFAULT_FIRST_NAME;
		lastName = DEFAULT_LAST_NAME;
	    }
	
	//only firstName is missing
	} else if (StringUtils.isBlank(firstName)) {
	    firstName = " ";
	    
	//only lastName is missing
	} else if (StringUtils.isBlank(lastName)) {
	    lastName = " ";
	}

	//Determine method based on the "role" parameter. Author roles can be either LTI standard ones or tool consumer's custom ones set
	//In case of ContentItemSelectionRequest user must still be a stuff member in order to create a lesson.
	boolean isCustomMonitorRole = LtiUtils.isToolConsumerCustomRole(roles,
		extServer.getLtiToolConsumerMonitorRoles());
	String method = LtiUtils.isStaff(roles, extServer) || LtiUtils.isAdmin(roles) || isCustomMonitorRole
		? IntegrationConstants.METHOD_AUTHOR
		: IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION;

	ExtServerLessonMap extLessonMap = integrationService.getLtiConsumerLesson(consumerKey, resourceLinkId);
	if (extLessonMap == null) {
	    //if deep linking was used to create the lesson and it's accessed for the first time, try to get lessonId as "custom_lessonid" parameter
	    Long customLessonId = WebUtil.readLongParam(request, "custom_lessonid", true);
	    boolean isContentItemSelection = WebUtil.readBooleanParam(request, "custom_iscontentitemselection", false);
	    if (isContentItemSelection && customLessonId != null) {
		//update its ExtServerLesson's resourceLinkId for the first time
		extLessonMap = integrationService.getExtServerLessonMap(customLessonId);
		extLessonMap.setResourceLinkId(resourceLinkId);
		userManagementService.save(extLessonMap);
	    }
	}
	//lessonId would be empty in case learner accesses LTI link before teacher authored it
	String lessonId = extLessonMap == null ? "" : extLessonMap.getLessonId().toString();

	String timestamp = String.valueOf(System.currentTimeMillis());

	// in case of learnerStrictAuth we should also include lsid value when creating hash: [ts + uid + method + lsid + serverID + serverKey]
	// regular case: [ts + uid + method + serverID + serverKey]
	String plaintext = timestamp.toLowerCase().trim() + extUsername.toLowerCase().trim()
		+ method.toLowerCase().trim()
		+ (IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method) ? lessonId : "")
		+ consumerKey.toLowerCase().trim() + secret.toLowerCase().trim();
	String hash = HashUtil.sha1(plaintext);

	// constructing redirectUrl by getting request.getQueryString() for POST requests
	String redirectUrl = "lti.do";
	redirectUrl = WebUtil.appendParameterToURL(redirectUrl, "_" + IntegrationConstants.PARAM_METHOD, method);
	for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
	    String paramName = e.nextElement();

	    //skip parameters starting with "oath_"
	    if (LtiUtils.OAUTH_CONSUMER_KEY.equals(paramName)
		    || !paramName.startsWith(BasicLTIConstants.OAUTH_PREFIX)) {
		//set "user_id" parameter taking into account extServer.getUseAlternativeUseridParameterName() setting
		String paramValue = BasicLTIConstants.USER_ID.equals(paramName) ? extUsername
			: request.getParameter(paramName);
		redirectUrl = WebUtil.appendParameterToURL(redirectUrl, paramName,
			URLEncoder.encode(paramValue, "UTF-8"));
	    }
	}

	String url = "LoginRequest";
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_USER_ID,
		URLEncoder.encode(extUsername, "UTF8"));
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_METHOD, method);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_TIMESTAMP, timestamp);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_SERVER_ID, consumerKey);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_HASH, hash);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_COURSE_ID, contextId);
	url = WebUtil.appendParameterToURL(url, CentralConstants.PARAM_COURSE_NAME, contextLabel);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_COUNTRY, countryIsoCode);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_LANGUAGE, langIsoCode);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_FIRST_NAME,
		URLEncoder.encode(firstName, "UTF-8"));
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_LAST_NAME,
		URLEncoder.encode(lastName, "UTF-8"));
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_LESSON_ID, lessonId);
	url = WebUtil.appendParameterToURL(url, IntegrationConstants.PARAM_EMAIL, email);
	url = WebUtil.appendParameterToURL(url, "redirectURL", URLEncoder.encode(redirectUrl, "UTF-8"));
	response.sendRedirect(response.encodeRedirectURL(url));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    /**
     *
     * @param localeStr
     *            the full balckboard locale string
     * @return the language
     */
    private static String getLanguage(String localeStr) {
	if (localeStr == null) {
	    return "xx";
	}
	String[] split = localeStr.split("_");
	return split[0];
    }

    /**
     *
     * @param localeStr
     *            the full balckboard locale string
     * @return the country
     */
    private static String getCountry(String localeStr) {
	if (localeStr == null) {
	    return "XX";
	}
	String[] split = localeStr.split("_");

	//default country set to AU
	String country = split.length > 1 ? split[1] : "AU";
	return country;
    }
}
