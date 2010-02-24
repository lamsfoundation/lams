package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.net.URL;
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

/**
 * @author lfoxton
 * 
 * Servlet to log user into LAMS using OpenID
 * 
 * Accepts the openid_url param and used joid libraries to authenticate the user.
 * 
 * If the identity provider server authenticates the user, log them in through SSO
 *
 */
public class SIFOpenIDServlet extends HttpServlet {

	private static final long serialVersionUID = -381530224124159008L;

	private static final Logger log = Logger.getLogger(SIFOpenIDServlet.class);

	private static final String PARAM_OPENID_URL = "openid_url";
	private static final String PARAM_ERROR_MSG = "errorMsg";

	private static final String ERROR_NOT_ENABLED = "OpenID is not enabled for LAMS.";
	private static final String ERROR_BLACKLISTED = "Your provider is not among the trusted providers, please use the portal for logging in.";
	private static final String ERROR_NO_ID_PASSED = "Authentication failed, no user id was passed.";
	private static final String ERROR_AUTH = "Authentication failed, there was an error during authentication, please contact the system administrator.";
	private static final String ERROR_AUTH_LAMS = "Authentication failed, A user in LAMS did not exist for openid URL: ";

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
					String returnURL = UrlUtils.getBaseUrl(request) + "/OpenIDServlet";
					sendAuthenticationRequest(response, userOpenIDURL, returnURL, UrlUtils.getBaseUrl(request));
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

	/**
	 * Attempt to send authentication request, if it is a trusted openid provider
	 * 
	 * @param response
	 * @param userOpenIDURL
	 * @param returnTo
	 * @param trustRoot
	 * @throws IOException
	 */
	private void sendAuthenticationRequest(HttpServletResponse response, String userOpenIDURL, String returnTo,
			String trustRoot) throws IOException {
		try {
			String openidRedirectURL = OpenIdFilter.joid().getAuthUrl(userOpenIDURL, returnTo, trustRoot);
			
			// See if it is a trusted server, then redirect
			if (isTrustedIdentityProvider(openidRedirectURL)) {
				log.info("No session found for user with url: " + userOpenIDURL
						+ ". Sending authentication request to identity provider.");
				response.sendRedirect(openidRedirectURL);
			} else {
				log.error("Identity provider not permitted: " + userOpenIDURL);
				redirectToPortal(response, ERROR_BLACKLISTED);
			}
		} catch (OpenIdException e) {
			log.error("Problem getting openid url.", e);
			redirectToPortal(response, ERROR_AUTH);
		} catch (Exception e) {
			log.error("Error sending redirect request.", e);
			redirectToPortal(response, ERROR_AUTH);
		}
	}

	/**
	 * Check the identity provider url against the list of supported servers
	 * 
	 * @param idpURLString
	 * @return
	 */
	private boolean isTrustedIdentityProvider(String idpURLString) {

		try {
			URL idpURL = new URL(idpURLString);

			// Get the list of trusted servers
			OpenIDConfig trustedIDPConfig = (OpenIDConfig) userService.findById(OpenIDConfig.class,
					OpenIDConfig.KEY_TRUSTED_IDPS);
			if (trustedIDPConfig != null) {
				String[] trustedIDPs = CSVUtil.parse(trustedIDPConfig.getConfigValue());

				// Test each against the trusted idp
				for (int i = 0; i < trustedIDPs.length; i++) {
					String trustedIDPStr = trustedIDPs[i];
					URL trustedIDPURL = new URL(trustedIDPStr);

					if (trustedIDPURL.getHost().equals(idpURL.getHost())) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			log.error("Error parsing trusted idps");
		}
		return false;
	}

	/**
	 * Fetch the user via their openidurl in lams. Then log them in
	 * 
	 * @param userOpenIDURL
	 * @param request
	 * @param response
	 * @throws IOException
	 */
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

	/**
	 * Redirect back to the portal attaching the error string as a parameters
	 * 
	 * @param response
	 * @param errorString
	 * @throws IOException
	 */
	private void redirectToPortal(HttpServletResponse response, String errorString) throws IOException {
		// Get the portal url
		OpenIDConfig portalURLObject = (OpenIDConfig) userService.findById(OpenIDConfig.class,
				OpenIDConfig.KEY_PORTAL_URL);
		String portalURL = (portalURLObject != null) ? portalURLObject.getConfigValue() : Configuration
				.get(ConfigurationKeys.SERVER_URL);

		if (errorString != null && errorString.length() > 0) {
			portalURL += "?" + PARAM_ERROR_MSG + "=" + errorString;
		}
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
