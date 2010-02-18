package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.openid.OpenIDConfig;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CSVUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.verisign.joid.OpenIdException;
import org.verisign.joid.consumer.OpenIdFilter;
import org.verisign.joid.util.UrlUtils;

public class SIFOpenIDServlet extends HttpServlet {

	private static final long serialVersionUID = -381530224124159008L;

	private static Logger log = Logger.getLogger(SIFOpenIDServlet.class);

	public static String PARAM_OPENID_URL = "openid_url";

	public static String ERROR_NOT_ENABLED = "OpenID is not enabled for LAMS.";
	public static String ERROR_BLACKLISTED = "Your provider is not among the trusted providers, please use the portal for logging in.";
	public static String ERROR_NO_ID_PASSED = "Authentication failed, no user id was passed.";
	public static String ERROR_AUTH = "Authentication failed, there was an error during authentication, please contact the system administrator.";
	public static String ERROR_AUTH_LAMS = "Authentication failed, A user in LAMS did not exist for openid URL: ";

	private IUserManagementService userService = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		setService();
		
		// Get the user's openid url from the request
		String userOpenIDURL = WebUtil.readStrParam(request, PARAM_OPENID_URL, true);

		// Check openid enabled
		OpenIDConfig openIDEnabled = (OpenIDConfig) userService.findById(OpenIDConfig.class, OpenIDConfig.KEY_ENABLED);

		// Check if the user is not already logged in
		HttpSession session = request.getSession(true);
		String loggedInAs = OpenIdFilter.getCurrentUser(session);

		if (openIDEnabled != null && Boolean.parseBoolean(openIDEnabled.getConfigValue())) {
			if (loggedInAs == null) {
				if (userOpenIDURL == null || userOpenIDURL.equals("")) {

					// No user openid url passed and no session, return to portal
					log.error("OpenID authentication failed, no value passed for the openid url");
					redirectToPortal(response, ERROR_NO_ID_PASSED);

				} else {
					// Attempt to use openid authentication if it is a trusted
					// identity provider
					if (isTrustedIdentityProvider(userOpenIDURL)) {
						log.info("No session found for user with url: " + userOpenIDURL
								+ ". Sending authentication request to identity provider.");

						String returnURL = UrlUtils.getBaseUrl(request) + "/OpenIDServlet";
						sendAuthenticationRequest(response, userOpenIDURL, returnURL, returnURL);
					} else {
						log.error("Identity provider not permitted: " + userOpenIDURL);
						redirectToPortal(response, ERROR_BLACKLISTED);
					}
				}
			} else {
				// Login to LAMS
				log.info("Logging user into LAMS using openid token: " + loggedInAs);
				loginUser(loggedInAs, request, response);
			}
		} else {
			redirectToPortal(response, ERROR_NOT_ENABLED);
		}

	}

	private void sendAuthenticationRequest(HttpServletResponse response, String userOpenIDURL, String returnTo,
			String trustRoot) throws IOException {
		try {
			String openidRedirectURL = OpenIdFilter.joid().getAuthUrl(userOpenIDURL, returnTo, trustRoot);
			response.sendRedirect(openidRedirectURL);
		} catch (OpenIdException e) {
			log.error("Problem getting openid url.", e);
			redirectToPortal(response, ERROR_AUTH);
		} catch (IOException e) {
			log.error("Error sending redirect request.", e);
			redirectToPortal(response, ERROR_AUTH);
		}
	}

	private boolean isTrustedIdentityProvider(String userOpenIDURL) {

		try {
			userOpenIDURL = removeHTTPFromString(userOpenIDURL);
			userOpenIDURL = removeTrailingSlashFromString(userOpenIDURL);
			userOpenIDURL = removeUserNameFromOpenIDURL(userOpenIDURL);

			OpenIDConfig trustedIDPConfig = (OpenIDConfig) userService.findById(OpenIDConfig.class,
					OpenIDConfig.KEY_TRUSTED_IDPS);
			if (trustedIDPConfig != null) {
				String[] trustedIDPs = CSVUtil.parse(trustedIDPConfig.getConfigValue());

				for (int i = 0; i < trustedIDPs.length; i++) {
					String trustedIDP = trustedIDPs[i];
					trustedIDP = removeHTTPFromString(trustedIDP);
					trustedIDP = removeTrailingSlashFromString(trustedIDP);
					
					if (userOpenIDURL.equals(trustedIDP)) {
						return true;
					}
				}

			}
		} catch (ParseException e) {
			log.error("Error parsing trusted idp csv");
		}

		return false;
	}

	private String removeHTTPFromString(String string) {
		if (string.startsWith("http://")) {
			return string.substring(7);
		}
		return string;
	}

	private String removeTrailingSlashFromString(String string) {
		if (string.endsWith("/")) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}
	
	private String removeUserNameFromOpenIDURL(String string) {
		return string.substring(string.indexOf(".") + 1);
	}

	private void loginUser(String userOpenIDURL, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (userService == null) {
			userService = getService();
		}
		User user = userService.getUserDTOByOpenidURL(userOpenIDURL);
		if (user != null) {
			HttpSession hses = request.getSession(true);

			// If there is an authenticated user, log them out
			String authenticatedUser = (String) hses.getAttribute("openidUser");
			if (authenticatedUser != null) {
				hses.invalidate();
				hses = request.getSession(true);
			}

			// set the user's session
			hses.setAttribute(AttributeNames.USER, user.getUserDTO());
			hses.setAttribute("openidUser", user.getLogin());

			String lamsURL = "j_security_check?j_username=" + user.getLogin() + "&j_password=" + user.getPassword();
			response.sendRedirect(lamsURL);
		} else {
			redirectToPortal(response, ERROR_AUTH_LAMS + userOpenIDURL);
		}
	}

	private void redirectToPortal(HttpServletResponse response, String errorString) throws IOException {
		// Get the portal url
		OpenIDConfig portalURLObject = (OpenIDConfig) userService.findById(OpenIDConfig.class,
				OpenIDConfig.KEY_PORTAL_URL);
		String portalURL = (portalURLObject != null) ? portalURLObject.getConfigValue() : Configuration
				.get(ConfigurationKeys.SERVER_URL);
		response.sendRedirect(portalURL);
	}

	private IUserManagementService getService() {
		if (userService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext());
			userService = (IUserManagementService) ctx.getBean("userManagementService");
		}
		return userService;
	}
	
	private void setService() {
		if (userService == null) {
			userService = (IUserManagementService) WebApplicationContextUtils.getRequiredWebApplicationContext(
					getServletContext()).getBean("userManagementService");
		}
	}
}
