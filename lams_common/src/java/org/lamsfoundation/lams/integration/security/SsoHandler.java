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

    protected static final String SESSION_KEY = "io.undertow.servlet.form.auth.redirect.location";

    // if this attribute is set in session, credential cache will not be cleared on session destro in SessionListener
    public static final String NO_FLUSH_FLAG = "noFlush";

    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
	// expose servlet context so other classes can use it
	SessionManager.setServletContext(servletContext);

	// run when request and response were already parsed, but before security handlers
	deploymentInfo.addOuterHandlerChainWrapper(handler -> {
	    // just forward all requests except one for logging in
	    return Handlers.path().addPrefixPath("/", handler).addExactPath("/j_security_check", exchange -> {
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
		 * Fetch UserDTO before completing request so putting it later in session is done ASAP
		 * Response is sent in another thread and if UserDTO is not present in session when browser completes
		 * redirect,
		 * it results in error. Winning this race is the easiest option.
		 */

		String login = request.getParameter("j_username");
		User user = null;
		if (StringUtils.isBlank(login)) {
		    serveLoginPage(exchange, "/login.jsp?failed=true");
		    return;
		}
		user = getUserManagementService(session.getServletContext()).getUserByLogin(login);
		if (user == null) {
		    serveLoginPage(exchange, "/login.jsp?failed=true");
		    return;
		}
		UserDTO userDTO = user.getUserDTO();
		String password = request.getParameter("j_password");
		if (user.getLockOutTime() != null && user.getLockOutTime().getTime() > System.currentTimeMillis()
			&& password != null && !password.startsWith("#LAMS")) {
		    serveLoginPage(exchange, "/login.jsp?lockedOut=true");
		    return;
		}

		// LoginRequestServlet (integrations) and LoginAsAction (sysadmin) set this parameter
		String redirectURL = request.getParameter("redirectURL");
		if (!StringUtils.isBlank(redirectURL)) {
		    SsoHandler.handleRedirectBack(context, redirectURL);
		}

		// if user is not yet authorized and has 2FA shared secret set up - redirect him to
		// loginTwoFactorAuth.jsp to prompt user to enter his verification code (Time-based One-time Password)
		if (request.getRemoteUser() == null && user.isTwoFactorAuthenticationEnabled()
			&& user.getTwoFactorAuthenticationSecret() != null) {
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
			String redirectUrl = "/lams/loginTwoFactorAuth.jsp"
				+ ((verificationCodeStr == null) ? "" : "?failed=true");
			response.sendRedirect(redirectUrl);
			return;
		    }

		}

		// prevent session fixation attack
		// This will become obsolete on Undertow upgrade to version 1.1.10+
		SessionManager.removeSessionByID(session.getId(), false, false);
		request.changeSessionId();
		session = request.getSession();

		// store session so UniversalLoginModule can access it
		SessionManager.startSession(request);

		// do the logging in UniversalLoginModule or cache
		handler.handleRequest(exchange);

		if (login.equals(request.getRemoteUser())) {
		    session.setAttribute(AttributeNames.USER, userDTO);

		    HttpSession existingSession = SessionManager.getSessionForLogin(login);
		    if (existingSession != null) {
			try {
			    // tell SessionListener not to flush credential cache on session destroy,
			    // otherwise this authentication processs fails
			    existingSession.setAttribute(NO_FLUSH_FLAG, true);
			} catch (IllegalStateException e) {
			    // if it was already invalidated, do nothing
			}
			// remove an existing session for the given user
			SessionManager.removeSessionByLogin(login, true);
		    }
		    // register current session as the only one for the given user
		    SessionManager.addSession(login, session);
		    Integer failedAttempts = user.getFailedAttempts();
		    if (failedAttempts != null && failedAttempts > 0 && password != null
			    && !password.startsWith("#LAMS")) {
			user.setFailedAttempts(null);
			user.setLockOutTime(null);
			getUserManagementService(session.getServletContext()).save(user);
		    }

		} else {
		    Integer failedAttempts = user.getFailedAttempts();
		    if (failedAttempts == null) {
			failedAttempts = 1;
		    } else {
			failedAttempts++;
		    }
		    user.setFailedAttempts(failedAttempts);
		    Integer failedAttemptsConfig = Configuration.getAsInt(ConfigurationKeys.FAILED_ATTEMPTS);

		    if (failedAttempts >= failedAttemptsConfig) {
			Integer lockOutTimeConfig = Configuration.getAsInt(ConfigurationKeys.LOCK_OUT_TIME);
			Long lockOutTimeMillis = lockOutTimeConfig * 60L * 1000;
			Long currentTimeMillis = System.currentTimeMillis();
			Date date = new Date(currentTimeMillis + lockOutTimeMillis);
			user.setLockOutTime(date);
			String message = new StringBuilder("User ").append(user.getLogin()).append("(")
				.append(user.getUserId()).append(") is locked out for ")
				.append(Configuration.getAsInt(ConfigurationKeys.LOCK_OUT_TIME)).append(" mins after ")
				.append(failedAttempts).append(" failed attempts.").toString();
			getLogEventService(session.getServletContext()).logEvent(LogEvent.TYPE_ACCOUNT_LOCKED,
				user.getUserId(), user.getUserId(), null, null, message);
		    }
		    getUserManagementService(session.getServletContext()).save(user);
		}

		SessionManager.endSession();
	    });
	});
    }

    /**
     * Forward to the login page with a specific error message. Avoids a redirect. Based on the
     * ServletFormAuthenticationMechanism method. The location should be relative to the current
     * context and start with "/" e.g. /login.jsp
     *
     * @throws IOException
     * @throws ServletException
     */
    protected Integer serveLoginPage(final HttpServerExchange exchange, final String location)
	    throws ServletException, IOException {

	ServletRequestContext context = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
	HttpServletRequest request = (HttpServletRequest) context.getServletRequest();
	HttpServletResponse response = (HttpServletResponse) context.getServletResponse();

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
    protected static void handleRedirectBack(ServletRequestContext context, String redirectURL) {
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

	    session.setAttribute(SsoHandler.SESSION_KEY, redirectURL);
	}
    }

    /**
     * Memorises jvmRoute if it is already set.
     */
    protected static void setJvmRoute(HttpServletRequest request) {
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

    protected IUserManagementService getUserManagementService(ServletContext context) {
	if (SsoHandler.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    SsoHandler.userManagementService = (UserManagementService) ctx.getBean("userManagementService");
	}
	return SsoHandler.userManagementService;
    }

    protected ILogEventService getLogEventService(ServletContext context) {
	if (SsoHandler.logEventService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	    SsoHandler.logEventService = (ILogEventService) ctx.getBean("logEventService");
	}
	return SsoHandler.logEventService;
    }
}