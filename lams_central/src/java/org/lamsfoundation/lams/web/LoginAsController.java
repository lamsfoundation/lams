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

package org.lamsfoundation.lams.web;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.security.UniversalLoginModule;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class LoginAsController {

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("/loginas")
    public String execute(HttpServletRequest request) throws Exception {
	String login = WebUtil.readStrParam(request, "login", false);

	if (userManagementService.isUserSysAdmin() && Configuration.getAsBoolean(ConfigurationKeys.LOGIN_AS_ENABLE)) {
	    if ((login != null) && (login.trim().length() > 0)) {
		User user = userManagementService.getUserByLogin(login);
		if (user != null) {

		    // If the user is an integration learner and ALLOW_DIRECT_ACCESS_FOR_INTEGRATION_LEARNERS if off do not let syadmin log in
		    // as they will not be able to access the index page. This test should be the same test as found in IndexAction.
		    Boolean allowDirectAccessIntegrationLearner = Configuration
			    .getAsBoolean(ConfigurationKeys.ALLOW_DIRECT_ACCESS_FOR_INTEGRATION_LEARNERS);
		    if (!allowDirectAccessIntegrationLearner) {
			boolean isIntegrationUser = integrationService.isIntegrationUser(user.getUserId());
			if (isIntegrationUser && isOnlyLearner(user.getUserId())) {
			    request.setAttribute("errorName", "Login As");
			    request.setAttribute("errorMessage",
				    messageService.getMessage("error.cannot.login.as.with.not.allow.direct.access"));
			    return "errorpages/errorWithMessage";
			}
		    }

		    // audit log when loginas
		    UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		    String[] args = new String[] { sysadmin.getLogin() + " (" + sysadmin.getUserID() + ")", login };
		    String message = messageService.getMessage("audit.admin.loginas", args);
		    logEventService.logEvent(LogEvent.TYPE_LOGIN_AS, sysadmin.getUserID(), user.getUserId(), null, null,
			    message);

		    // login.jsp knows what to do with these
		    request.setAttribute("login", login);
		    String token = "#LAMS" + RandomPasswordGenerator.nextPassword(10);
		    request.setAttribute("password", token);
		    // notify the login module that the user has been authenticated correctly
		    UniversalLoginModule.setAuthenticationToken(token);
		    // redirect to login page
		    request.setAttribute("redirectURL", "/lams/index.jsp");
		    request.setAttribute("isLoginAs", true);
		    return "login";
		}
	    }
	} else {
	    request.setAttribute("errorName", "Login As");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "errorpages/errorWithMessage";
	}

	return "forward:/admin/usersearch.do";
    }

    private boolean isOnlyLearner(Integer userId) {
	Map<Integer, Set<Integer>> orgRoleSets = userManagementService.getRolesForUser(userId);
	for (Set<Integer> orgRoleSet : orgRoleSets.values()) {
	    for (Integer role : orgRoleSet) {
		if (role.equals(Role.ROLE_AUTHOR) || role.equals(Role.ROLE_MONITOR)
			|| role.equals(Role.ROLE_GROUP_MANAGER) || role.equals(Role.ROLE_SYSADMIN)) {
		    return false;
		}
	    }
	}
	return true;
    }

}
