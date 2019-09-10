/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.rest;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringFullService;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Base class for LAMS REST servlets.
 *
 * @author Marcin Cieslak
 */
public abstract class RestServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(RestServlet.class);

    @Autowired
    protected IToolDAO toolDAO;
    @Autowired
    protected ILamsCoreToolService lamsCoreToolService;
    @Autowired
    protected IIntegrationService integrationService;
    @Autowired
    protected IAuthoringFullService authoringService;
    @Autowired
    protected IUserManagementService userManagementService;
    
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
     * Checks if the provided auth JSON is valid.
     */
    private UserDTO authenticate(JsonNode authenticationJSON) {
	User user = null;
	try {
	    String serverName = authenticationJSON.get(IntegrationConstants.PARAM_SERVER_ID).asText();
	    ExtServer extServer = integrationService.getExtServer(serverName);
	    String userName = authenticationJSON.get(IntegrationConstants.PARAM_USER_ID).asText();
	    String method = authenticationJSON.get(IntegrationConstants.PARAM_METHOD).asText().toLowerCase();
	    String timestamp = authenticationJSON.get(IntegrationConstants.PARAM_TIMESTAMP).asText();
	    String hash = authenticationJSON.get(IntegrationConstants.PARAM_HASH).asText();

	    // Throws AuthenticationException if it fails
	    Authenticator.authenticateLoginRequest(extServer, timestamp, userName, method, null, hash);

	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, userName);
	    user = userMap.getUser();
	    // get concrete user
	    user = (User) userManagementService.findById(User.class, user.getUserId());
	    return user.getUserDTO();
	} catch (AuthenticationException e) {
	    log.error("The user was not authenticated", e);
	} catch (UserInfoFetchException e) {
	    log.error("Could not fetch new user information from integration server", e);
	} catch (UserInfoValidationException e) {
	    log.error("User data is not valid", e);
	}

	return null;
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String requestBody = IOUtils.toString(request.getInputStream(), "UTF-8");
	ObjectNode requestJSON = new ObjectMapper().readValue(requestBody, ObjectNode.class);
	JsonNode authenticationJSON = requestJSON.get("auth");

	UserDTO userDTO = authenticate(authenticationJSON);
	if (userDTO == null) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not authenticated");
	    return;
	}
	// do not pass authentication info to concrete REST servlets
	requestJSON.remove("auth");

	// Some LAMS code, like creating a Learning Design object, requires UserDTO in the shared session.
	// User may be not authenticated at all, so we need to create a new session.
	// Or the authentication JSON specified a different user than the one that is currently authenticated in LAMS,
	// so we need to swap it in the session.
	UserDTO existingUserDTO = null;
	HttpSession session = SessionManager.getSession();
	boolean createNewSession = session == null;
	if (createNewSession) {
	    // if there is no session, start it manually
	    SessionManager.startSession(request);
	} else {
	    existingUserDTO = (UserDTO) session.getAttribute(AttributeNames.USER);
	}
	session = SessionManager.getSession();
	session.setAttribute(AttributeNames.USER, userDTO);

	try {
	    doPostInternal(requestJSON, userDTO, response);
	} catch (Exception e) {
	    throw new ServletException("Error while processing REST request", e);
	} finally {
	    if (createNewSession) {
		SessionManager.endSession();
	    } else {
		session.setAttribute(AttributeNames.USER, existingUserDTO);
	    }
	}
    }

    protected abstract void doPostInternal(ObjectNode requestJSON, UserDTO userDTO, HttpServletResponse response)
	    throws Exception;
}