/*
 * Created on Dec 5, 2004
 *
 * Last modified on Dec 5, 2004
 */
package org.lamsfoundation.lams.admin.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * This utility class provides redirecting strategy support for form-based
 * security mechanism currently implemented in LAMS.
 * 
 * <p>
 * <a href="JspRedirectStrategy.java.html"> <i>View Source </i> </a>
 * </p>
 * 
 * @author Kevin Han
 *         Fei Yang
 */
public class JspRedirectStrategy {

	public static final String AUTH_OBJECT_NAME = "authorizationObjectName";

	public static final String SECURITY_CHECK_NOT_PASSED = "SecurityCheckNotPassed";
	
	public static final String SECURITY_CHECK_PASSED = "securityCheckPassed";

	public static final String WELCOME_PAGE = "index.jsp ";

	private static Logger log = Logger.getLogger(JspRedirectStrategy.class);

	/**
	 * login page (defined in web.xml) calls this method to have its response
	 * redirected whenever necessary, and have its authorization object updated
	 * as well.
	 * 
	 * @param request
	 * @param response
	 * @return true if response has been redirected; false if login page should
	 *         continue.
	 *  
	 */
	public static boolean loginPageRedirected(HttpServletRequest request,HttpServletResponse response) throws java.io.IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			log.debug("===>LOGIN PAGE: session not found, simply stop here.");
			return true;
		}
		String checkStatus = (String) session.getAttribute(AUTH_OBJECT_NAME);
		String id = session.getId();

		if (checkStatus == null) {
			log.debug("===>LOGIN PAGE: there is no auth obj in session, auth obj created. session id: " + id);
			session.setAttribute(AUTH_OBJECT_NAME, SECURITY_CHECK_NOT_PASSED);
			response.sendRedirect(WELCOME_PAGE);
			return true;
		} else if (checkStatus.equals(SECURITY_CHECK_NOT_PASSED)) {
			log.debug("===>LOGIN PAGE: accessing login page before login succeed, display login page. session id: "+ id);
			return false;
		} else if (checkStatus.equals(SECURITY_CHECK_PASSED)) {
			log.debug("===>LOGIN PAGE: accessing login after login succeed. Invalidate the session: " + id + " and redirect to "+ WELCOME_PAGE);
			session.invalidate();
			response.sendRedirect(WELCOME_PAGE);
			return true;
		} else {
			log.debug("===>LOGIN PAGE: logically impossible to be here, no valid status found : "+ id);
			session.invalidate();
			response.sendRedirect(WELCOME_PAGE);
			return true;
		}

	}

	public static void welcomePageStatusUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			log.debug("===>INDEX PAGE: session not found, simply stop here.");
			return;
		}
		if (!session.getAttribute(AUTH_OBJECT_NAME).equals(SECURITY_CHECK_PASSED))
			session.setAttribute(AUTH_OBJECT_NAME, new String(SECURITY_CHECK_PASSED));

	}

}