/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.web.lamscommunity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Action to login to lamscommunity when the user reaches the main page
 *
 * Steps:
 *
 * 1) If the user has previously logged in and has a lamscommunity login token
 * saved, then it will immediately log them in through the lamscommunity sso
 * servlet, otherwise it will take them to a authentication page where the user
 * will enter their lamscommunity username and password
 *
 * 2) Once the username and password has been entered, a request will be sent to
 * lams community with the username/password which lams community will
 * authenticate and return a valid user token for this user, which can be used
 * to login as in step one for thence forward.
 *
 *
 * @author lfoxton
 *
 *
 *
 *
 *
 *
 *
 */
public class LamsCommunityLoginAction extends LamsDispatchAction {

    private IUserManagementService service;

    private static final String ATTR_ERROR_MESSAGE = "errorMessage";

    private static final String LC_METHOD_IMPORT = "import";
    private static final String LC_METHOD_NEWS = "news";
    private static final String LC_METHOD_SAVE = "save";

    /**
     * Default action response
     * Checks whether the user has a lamscommunity token, if so logs them in
     * immediately, otherwise forward to lamsCommunityLogin.jsp that
     * prompts for login and password
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	User user = getUser(request);

	// Getting the destination of the lamscommunity call
	String dest = WebUtil.readStrParam(request, LamsCommunityUtil.PARAM_DEST, true);
	if (dest == null) {
	    dest = LC_METHOD_NEWS;
	}

	// Checking the server is registered
	Registration reg = Configuration.getRegistration();
	if (reg == null || reg.isEnableLamsCommunityIntegration() == false) {
	    request.setAttribute("registered", Boolean.FALSE);
	} else {
	    request.setAttribute("registered", Boolean.TRUE);
	}

	if (user.getLamsCommunityToken() == null || user.getLamsCommunityToken().equals("")) {
	    // Authenticate the user manually
	    return mapping.findForward("lamsCommunityLogin");
	} else {
	    // log the user into lamscommunity directly using the user token
	    loginToLamsCommunity(mapping, form, request, response);
	}

	return null;
    }

    public ActionForward getLCUserLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return mapping.findForward("lamsCommunityLogin");
    }

    /**
     * Authenticates the user with lamscommunity
     * If the username/password is successfull a user token from the lams
     * community is saved for the user which will be used henceforth to do the
     * login request.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward authenticate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LamsCommunityLoginForm loginForm = (LamsCommunityLoginForm) form;

	//configurationService = getConfiguration();
	Registration reg = Configuration.getRegistration();

	String responseString = null;
	if (reg != null && reg.getServerID() != null && reg.getServerKey() != null) {
	    responseString = lamsCommunityAuth(loginForm.getLcUserName(), loginForm.getLcPassword(), reg.getServerID(),
		    reg.getServerKey());
	} else {
	    throw new Exception("Attempt to authenticate in lams community without registration");
	}

	if (responseString != null) {
	    String[] result = responseString.split(",");
	    if (result[0].equals("success")) {
		// Save the lams community user token to the user table
		User user = getUser(request);
		user.setLamsCommunityUsername(loginForm.getLcUserName());
		user.setLamsCommunityToken(result[1]);
		getService().saveUser(user);

		// then login
		return loginToLamsCommunity(mapping, form, request, response);
	    } else {
		if (result.length == 2) {
		    // login failed known reason
		    request.setAttribute(ATTR_ERROR_MESSAGE, result[1]);
		} else {
		    // login failed unknown reason
		    request.setAttribute(ATTR_ERROR_MESSAGE, "lamscommunity.login.failed");
		}
		return mapping.findForward("lamsCommunityLogin");
	    }
	} else {
	    // login failed, did not recieve response from lamscommunity
	    request.setAttribute(ATTR_ERROR_MESSAGE, "lamscommunity.no.result");
	    return mapping.findForward("lamsCommunityLogin");
	}
    }

    /**
     * Handles the call to lamscommunity to authenticate the user
     * An encrypted message is sent to the lamscommunity with the user's
     * credetials and lamscommunity returns a token if the authentication was
     * successful
     *
     * @param userName
     * @param lcPassword
     * @param serverID
     * @param serverKey
     * @return
     * @throws Exception
     */
    public String lamsCommunityAuth(String userName, String lcPassword, String serverID, String serverKey)
	    throws Exception {

	String timestamp = "" + new Date().getTime();

	// encrypt the user data
	String encryption = LamsCommunityUtil.encryptAuthenticationInfo(timestamp, userName, lcPassword, serverID,
		serverKey);

	// Create the requst parameters
	HashMap<String, String> params = new HashMap<String, String>();
	params.put(LamsCommunityUtil.PARAM_HASH, URLEncoder.encode(encryption, "UTF8"));
	params.put(LamsCommunityUtil.PARAM_SERVER_ID, URLEncoder.encode(serverID, "UTF8"));

	// send the request to lamscommunity
	InputStream is = WebUtil.getResponseInputStreamFromExternalServer(LamsCommunityUtil.LAMS_COMMUNITY_AUTH_URL,
		params);
	BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	String str = isReader.readLine();
	return str;
    }

    /**
     * Logs the user into lams community directly using the user token which
     * was saved in the authentication action
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward loginToLamsCommunity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	User user = getUser(request);

	// The destination
	String lamscommunityDest = WebUtil.readStrParam(request, LamsCommunityUtil.PARAM_DEST, true);

	// check that the server is registered with lamscommunity
	Registration reg = Configuration.getRegistration();
	String serverID;
	String serverKey;
	if (reg != null && reg.getServerID() != null && reg.getServerKey() != null) {
	    serverID = reg.getServerID();
	    serverKey = reg.getServerKey();
	} else {
	    throw new Exception("Attempt to authenticate in lams community without registration");
	}

	// Update the user session, set the logged into lamscommunity flag
	UserDTO userDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	userDTO.setLoggedIntoLamsCommunity(true);

	String timestamp = "" + new Date().getTime();
	String hash = LamsCommunityUtil.createAuthenticationHash(timestamp, user.getLamsCommunityUsername(),
		user.getLamsCommunityToken(), serverID, serverKey);

	// Redirect the user to the lamscommunity sso url

	String url = LamsCommunityUtil.LAMS_COMMUNITY_SSO_URL + "?";
	url = LamsCommunityUtil.appendAuthInfoToURL(url, user);

	if (lamscommunityDest != null) {
	    String customCSV = WebUtil.readStrParam(request, AttributeNames.PARAM_CUSTOM_CSV, true);
	    url = appendReturnUrl(url, lamscommunityDest, customCSV);
	}

	log.debug("Redirecting to lamscommunity url: " + url);

	response.sendRedirect(url);
	return null;
    }

    private String appendReturnUrl(String url, String dest, String customCSV) throws Exception {
	url += "&" + LamsCommunityUtil.PARAM_DEST + "=" + URLEncoder.encode(dest, "UTF8");
	if (dest != null && dest.equals(LC_METHOD_IMPORT)) {
	    String returnURL = Configuration.get(ConfigurationKeys.SERVER_URL)
		    + "/authoring/importToolContent.do?method=importLCFinish";

	    url += "&" + LamsCommunityUtil.PARAM_RETURN_URL + "=" + URLEncoder.encode(returnURL, "UTF8");
	} else if (dest != null && dest.equals(LC_METHOD_SAVE)) {
	    // handle save
	}

	// append the customCSV if it is there so we get it on the return
	if (customCSV != null && !(customCSV.length() == 0)) {
	    url += "&" + AttributeNames.PARAM_CUSTOM_CSV + "=" + customCSV;
	}
	return url;
    }

    private User getUser(HttpServletRequest request) {
	return getService().getUserByLogin(request.getRemoteUser());
    }

    private IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }
}
