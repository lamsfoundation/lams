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
package org.lamsfoundation.lams.integration.security;

import java.io.IOException;
import java.security.AccessController;
import java.util.Date;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import io.undertow.Handlers;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.Session;
import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpSessionImpl;
import io.undertow.util.Headers;

/**
 * Allows access to LAMS WARs when an user logs in.
 *
 * @author Marcin Cieslak
 *
 */
public class SsoHandler implements ServletExtension {
    private static ILogEventService logEventService = null;
    private static IUserManagementService userManagementService = null;

    private static final String REDIRECT_KEY = "io.undertow.servlet.form.auth.redirect.location";
    static final String KEEP_SESSION_ID_KEY = "lams.keepSessionId";

    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
	// If WildFly was run with VM parameter -Dlams.keepSessionId=true
	// session ID will not be changed after log in
	// This is necessary for TestHarness to run as it does not process session ID change correctly
	boolean keepSessionId = Boolean.parseBoolean(System.getProperty(KEEP_SESSION_ID_KEY));
	if (keepSessionId) {
	    deploymentInfo.setChangeSessionIdOnLogin(false);
	}

	// expose servlet context so other classes can use it
	SessionManager.setServletContext(servletContext);

	// run when request and response were already parsed, but before security handlers
	deploymentInfo.addOuterHandlerChainWrapper(handler -> {
	    // just forward all requests except one for logging in
	    return Handlers.path().addPrefixPath("/", handler).addExactPath("/j_security_check", exchange -> {
		// intercept login page
		ServletRequestContext context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
		HttpServletResponse response = (HttpServletResponse) context.getServletResponse();
		HttpServletRequest request = (HttpServletRequest) context.getServletRequest();

		// initialise jvmRoute for runtime statistics servlet
		if (SessionManager.getJvmRoute() == null) {
		    SsoHandler.setJvmRoute(request);
		}

		// recreate session here in case it was invalidated in login.jsp by sysadmin's LoginAs
		HttpSession session = request.getSession();
		/*
		 * Fetch UserDTO before completing request, so putting it later in session is done ASAP
		 * Response is sent in another thread and if UserDTO is not present in session when browser completes
		 * redirect, it results in error. Winning this race is the easiest option.
		 */

		String login = request.getParameter("j_username");
		if (StringUtils.isBlank(login)) {
		    SsoHandler.serveLoginPage(exchange, request, response, "/login.jsp?failed=true");
		    return;
		}
		User user = SsoHandler.getUserManagementService(session.getServletContext()).getUserByLogin(login);
		if (user == null) {
		    SsoHandler.serveLoginPage(exchange, request, response, "/login.jsp?failed=true");
		    return;
		}
		UserDTO userDTO = user.getUserDTO();
		String password = request.getParameter("j_password");
		if (user.getLockOutTime() != null && user.getLockOutTime().getTime() > System.currentTimeMillis()
			&& password != null && !password.startsWith("#LAMS")) {
		    SsoHandler.serveLoginPage(exchange, request, response, "/login.jsp?lockedOut=true");
		    return;
		}

		// LoginRequestServlet (integrations) and LoginAsAction (sysadmin) set this parameter
		String redirectURL = request.getParameter("redirectURL");

		//bypass 2FA if using Login-as
		boolean isPasswordToken = password.startsWith("#LAMS");
		boolean isUsingLoginAsFeature = isPasswordToken && StringUtils.equals(redirectURL, "/lams/index.jsp");

		// if user is not yet authorized and has 2FA shared secret set up - redirect him to
		// loginTwoFactorAuth.jsp to prompt user to enter his verification code (Time-based One-time Password)
		if (request.getRemoteUser() == null && user.isTwoFactorAuthenticationEnabled()
			&& user.getTwoFactorAuthenticationSecret() != null && !isUsingLoginAsFeature) {
		    String verificationCodeStr = request.getParameter("verificationCode");
		    int verificationCode = NumberUtils.toInt(verificationCodeStr);
		    GoogleAuthenticator gAuth = new GoogleAuthenticator();
		    boolean isCodeValid = gAuth.authorize(user.getTwoFactorAuthenticationSecret(), verificationCode);

		    //user entered correct TOTP password
		    if (isCodeValid) {
			//do nothing and let regular login to happen

			//user hasn't yet entered TOTP password (request came from login.jsp) or entered the wrong one
		    } else {
			session.setAttribute("login", login);
			session.setAttribute("password", password);

			//verificationCodeStr equals null in case request came from login.jsp
			redirectURL = "/lams/loginTwoFactorAuth.jsp"
				+ ((verificationCodeStr == null) ? "" : "?failed=true");
			response.sendRedirect(redirectURL);
			return;
		    }
		}

		// when user clicks
		UserDTO loggedInUser = session == null ? null : (UserDTO) session.getAttribute(AttributeNames.USER);
		if (isPasswordToken && loggedInUser != null && loggedInUser.getLogin().equals(login)) {
		    response.sendRedirect(redirectURL);
		    return;
		}

		if (!StringUtils.isBlank(redirectURL)) {
		    SsoHandler.handleRedirectBack(context, redirectURL);
		}

		// store session so UniversalLoginModule can access it
		SessionManager.startSession(request);

		String oldSessionID = session.getId();

		// do the logging in UniversalLoginModule or cache
		handler.handleRequest(exchange);

		// session ID was changed after log in
		SessionManager.updateSessionID(oldSessionID);

		if (login.equals(request.getRemoteUser())) {
		    session.setAttribute(AttributeNames.USER, userDTO);

		    Integer failedAttempts = user.getFailedAttempts();
		    if (failedAttempts != null && failedAttempts > 0 && password != null
			    && !password.startsWith("#LAMS")) {
			user.setFailedAttempts(null);
			user.setLockOutTime(null);
			SsoHandler.getUserManagementService(session.getServletContext()).save(user);
		    }

		    SsoHandler.logLogin(userDTO, request);
		} else {
		    // clear after failed authentication, if it was set in LoginRequestServlet
		    session.removeAttribute("integratedLogoutURL");

		    Integer failedAttempts = user.getFailedAttempts();
		    Integer failedAttemptsConfig = Configuration.getAsInt(ConfigurationKeys.FAILED_ATTEMPTS);
		    // do not allow more failed attempts than limit in config as we may overflow failedAttempts column in DB
		    failedAttempts = failedAttempts == null ? 1 : Math.min(failedAttempts + 1, failedAttemptsConfig);
		    user.setFailedAttempts(failedAttempts);

		    if (failedAttempts >= failedAttemptsConfig) {
			Integer lockOutTimeConfig = Configuration.getAsInt(ConfigurationKeys.LOCK_OUT_TIME);
			Long lockOutTimeMillis = lockOutTimeConfig * 60L * 1000;
			Long currentTimeMillis = System.currentTimeMillis();
			Date date = new Date(currentTimeMillis + lockOutTimeMillis);
			user.setLockOutTime(date);
			String message = new StringBuilder("User ").append(user.getLogin()).append(" (")
				.append(user.getUserId()).append(") is locked out for ")
				.append(Configuration.getAsInt(ConfigurationKeys.LOCK_OUT_TIME)).append(" mins after ")
				.append(failedAttempts).append(" failed attempts.").toString();
			SsoHandler.getLogEventService(session.getServletContext()).logEvent(
				LogEvent.TYPE_ACCOUNT_LOCKED, user.getUserId(), user.getUserId(), null, null, message);
		    }
		    SsoHandler.getUserManagementService(session.getServletContext()).save(user);
		}

		SessionManager.endSession();
	    });
	});
    }

    /**
     * Forward to the login page with a specific error message. Avoids a redirect. Based on the
     * ServletFormAuthenticationMechanism method. The location should be relative to the current
     * context and start with "/" e.g. /login.jsp
     */
    private static Integer serveLoginPage(final HttpServerExchange exchange, final HttpServletRequest request,
	    final HttpServletResponse response, final String location) throws ServletException, IOException {
	exchange.getResponseHeaders().add(Headers.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
	exchange.getResponseHeaders().add(Headers.PRAGMA, "no-cache");
	exchange.getResponseHeaders().add(Headers.EXPIRES, "0");

	request.getRequestDispatcher(location).forward(request, response);
	return null;
    }

    /**
     * Notifies authentication mechanism where it should redirect after log in. Based on
     * ServletFormAuthenticationMechanism method.
     */
    private static void handleRedirectBack(ServletRequestContext context, String redirectURL) {
	/*
	 * Prevent HTTP Response Splitting attack by sanitizing redirectURL.
	 * The attack was possible by changing action of login form to, for example,
	 * "j_security_check?redirectURL=%0d%0aAppScanHeader:%20AppScanValue%2f1%2e2%2d3%0d%0aSecondAppScanHeader:%20whatever"
	 * Putting it in redirectURL form field or using another GET parameter ("something", "j_username") did not work.
	 * The result was a split HTTP response with AppScanHeader and SecondAppScanHeader set, resultint in a security
	 * threat.
	 */
	if (redirectURL.contains("\n") || redirectURL.contains("\r")) {
	    throw new SecurityException(
		    "redirectURL contains forbidden characters: \\n or \\r. Possible HTTP Response Splitting attack.");
	}

	HttpSessionImpl httpSession = context.getCurrentServletContext().getSession(context.getExchange(), true);
	if (httpSession != null) {
	    Session session;
	    if (System.getSecurityManager() == null) {
		session = httpSession.getSession();
	    } else {
		session = AccessController.doPrivileged(new HttpSessionImpl.UnwrapSessionAction(httpSession));
	    }

	    session.setAttribute(SsoHandler.REDIRECT_KEY, redirectURL);
	}
    }

    /**
     * Memorises jvmRoute if it is already set.
     */
    private static void setJvmRoute(HttpServletRequest request) {
	// if previous requests has not created a session, cookies will be null
	Cookie[] cookies = request.getCookies();
	if (cookies == null) {
	    return;
	}

	for (Cookie cookie : cookies) {
	    if (cookie.getName().equals("JSESSIONID")) {
		// look for jvmRoute
		int index = cookie.getValue().indexOf('.');
		if (index > 0) {
		    SessionManager.setJvmRoute(cookie.getValue().substring(index + 1));
		    return;
		}
	    }
	}
    }

    private static void logLogin(UserDTO user, HttpServletRequest request) {
	String clientIP = null;
	String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	if (xForwardedForHeader == null) {
	    clientIP = request.getRemoteAddr();
	} else {
	    // As of https://en.wikipedia.org/wiki/X-Forwarded-For
	    // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
	    // we only want the client
	    clientIP = new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
	}

	String message = new StringBuilder("User ").append(user.getLogin()).append(" (").append(user.getUserID())
		.append(") logged in from IP ").append(clientIP).toString();
	SsoHandler.getLogEventService(SessionManager.getServletContext()).logEvent(LogEvent.TYPE_LOGIN,
		user.getUserID(), user.getUserID(), null, null, message);
    }

    private static void logLogout(UserDTO user) {
	String message = new StringBuilder("User ").append(user.getLogin()).append(" (").append(user.getUserID())
		.append(") got logged out from another browser").toString();
	SsoHandler.getLogEventService(SessionManager.getServletContext()).logEvent(LogEvent.TYPE_LOGOUT,
		user.getUserID(), user.getUserID(), null, null, message);
    }

    private static IUserManagementService getUserManagementService(ServletContext context) {
	if (SsoHandler.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    SsoHandler.userManagementService = (UserManagementService) ctx.getBean("userManagementService");
	}
	return SsoHandler.userManagementService;
    }

    private static ILogEventService getLogEventService(ServletContext context) {
	if (SsoHandler.logEventService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    SsoHandler.logEventService = (ILogEventService) ctx.getBean("logEventService");
	}
	return SsoHandler.logEventService;
    }
}