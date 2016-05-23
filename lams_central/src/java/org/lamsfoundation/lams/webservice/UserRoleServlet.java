package org.lamsfoundation.lams.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Allows user role granting for integrated environments.
 *
 * @author Marcin Cieslak
 *
 */
public class UserRoleServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(UserRoleServlet.class);

    private static IntegrationService integrationService = null;
    private static IUserManagementService userManagementService = null;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	String method = request.getParameter(CentralConstants.PARAM_METHOD);
	String targetUsername = request.getParameter("targetUsername");
	String role = request.getParameter(AttributeNames.PARAM_ROLE);

	try {

	    ExtServerOrgMap serverMap = UserRoleServlet.integrationService.getExtServerOrgMap(serverId);
	    String plaintext = datetime.toLowerCase().trim() + username.toLowerCase().trim()
		    + targetUsername.toLowerCase().trim() + method.toLowerCase().trim() + role.toLowerCase().trim()
		    + serverMap.getServerid().toLowerCase().trim() + serverMap.getServerkey().toLowerCase().trim();
	    if (!hashValue.equals(HashUtil.sha1(plaintext))) {
		log.error("Authentication failed while trying to set role for user: " + targetUsername);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed, invalid hash");
	    }
	    ExtUserUseridMap userMap = UserRoleServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    if ("grant".equalsIgnoreCase(method)) {
		grant(userMap.getUser(), role);
	    } else if ("revoke".equalsIgnoreCase(method)) {
		revoke(userMap.getUser(), role);
	    } else {
		log.error("Unknown method: " + method);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown method: " + method);
	    }
	} catch (Exception e) {
	    UserRoleServlet.log.error("Error while setting user roles", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while setting user roles");
	}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    /**
     * Initialization of the servlet.
     */
    @Override
    public void init() throws ServletException {
	UserRoleServlet.integrationService = (IntegrationService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");
	UserRoleServlet.userManagementService = (IUserManagementService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("userManagementService");
    }

    /**
     * It only supports SYSADMIN role now, but can be extended in the future.
     */
    private void grant(User user, String role) throws IOException {
	switch (role) {
	    case Role.SYSADMIN:
		Organisation rootOrganisation = userManagementService.getRootOrganisation();
		List<String> roles = new ArrayList<String>(Arrays.asList(Role.ROLE_SYSADMIN.toString()));
		userManagementService.setRolesForUserOrganisation(user, rootOrganisation.getOrganisationId(), roles);
		break;
	    default:
		throw new IOException("Unknown role: " + role);
	}
    }

    /**
     * It only supports SYSADMIN role now, but can be extended in the future.
     */
    private void revoke(User user, String role) throws IOException {
	switch (role) {
	    case Role.SYSADMIN:
		Organisation rootOrganisation = userManagementService.getRootOrganisation();
		List<String> roles = new ArrayList<String>();
		userManagementService.setRolesForUserOrganisation(user, rootOrganisation.getOrganisationId(), roles);
		break;
	    default:
		throw new IOException("Unknown role: " + role);
	}
    }
}