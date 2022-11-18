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

package org.lamsfoundation.lams.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jliew
 */
@Controller
public class UserBasicListController {

    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping(path = "/user/basiclist", method = RequestMethod.POST)
    public String execute(HttpServletRequest request) throws Exception {

	HttpSession session = SessionManager.getSession();
	if (session != null) {
	    UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	    if (userDto != null) {
		// get inputs
		Integer userId = userDto.getUserID();
		Integer orgId = WebUtil.readIntParam(request, "orgId", true);
		String potential = WebUtil.readStrParam(request, "potential", true);
		if (orgId != null) {
		    if (!StringUtils.equals(potential, "1")) {
			// list users in org
			List users = userManagementService.getUsersFromOrganisation(orgId);
			request.setAttribute("users", users);
		    } else {
			// get all potential users of this org instead... filters results according to user's roles
			// get group
			Organisation org = (Organisation) userManagementService.findById(Organisation.class, orgId);
			Organisation group;
			if (org != null) {
			    if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
				group = org.getParentOrganisation();
			    } else {
				group = org;
			    }
			    // get users
			    List users = new ArrayList();
			    if (request.isUserInRole(Role.APPADMIN) || request.isUserInRole(Role.SYSADMIN)
				    || userManagementService.isUserGlobalGroupManager()) {
				users = userManagementService.getAllUsers(org.getOrganisationId());
			    } else if (userManagementService.isUserInRole(userId, group.getOrganisationId(),
				    Role.GROUP_MANAGER)) {
				if (group.getCourseAdminCanBrowseAllUsers()) {
				    users = userManagementService.getAllUsers(org.getOrganisationId());
				} else if (org.getOrganisationType().getOrganisationTypeId()
					.equals(OrganisationType.CLASS_TYPE)) {
				    users = userManagementService.findUsers(null, group.getOrganisationId(), orgId);
				}
			    }
			    request.setAttribute("users", users);
			}
		    }
		}
	    }
	}
	return "user/basiclist";
    }
}