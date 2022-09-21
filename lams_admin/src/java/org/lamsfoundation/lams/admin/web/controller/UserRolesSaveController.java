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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.admin.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.UserRolesForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jliew
 */
@Controller
public class UserRolesSaveController {
    private static Logger log = Logger.getLogger(UserRolesSaveController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    private static List<Role> rolelist;

    @RequestMapping(path = "/userrolessave", method = RequestMethod.POST)
    public String execute(@ModelAttribute UserRolesForm userRolesForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer orgId = userRolesForm.getOrgId();
	Integer rootOrgId = userManagementService.getRootOrganisation().getOrganisationId();
	boolean isGlobalRolesSet = orgId.equals(rootOrgId);
	if (isGlobalRolesSet) {
	    securityService.isSysadmin(getUserId(), "save global roles", true);
	}

	if (rolelist == null) {
	    rolelist = userManagementService.findAll(Role.class);
	    Collections.sort(rolelist);
	}

	Integer userId = userRolesForm.getUserId();
	String[] roles = userRolesForm.getRoles();

	request.setAttribute("org", orgId);

	if (log.isDebugEnabled()) {
	    String numRoles = roles != null ? Integer.toString(roles.length) : "0";
	    log.debug(new StringBuilder("userId: ").append(userId).append(", orgId: ").append(orgId)
		    .append(" will have ").append(numRoles).append(" roles").toString());
	}
	Organisation org = (Organisation) userManagementService.findById(Organisation.class, orgId);
	User user = (User) userManagementService.findById(User.class, userId);

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	// user must have at least 1 role
	if (!isGlobalRolesSet && (roles == null || roles.length < 1)) {
	    errorMap.add("roles", messageService.getMessage("error.roles.empty"));
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute("rolelist", userManagementService.filterRoles(rolelist,
		    request.isUserInRole(Role.APPADMIN), org.getOrganisationType()));
	    request.setAttribute("login", user.getLogin());
	    request.setAttribute("fullName", user.getFullName());
	    return "forward:/userroles.do";
	}
	List<String> userRolesList = roles == null || roles.length < 1 ? List.of() : Arrays.asList(roles);
	if (userRolesList.contains(Role.ROLE_SYSADMIN.toString())
		&& !userRolesList.contains(Role.ROLE_APPADMIN.toString())) {
	    //all sysadmins are also appadmins
	    userRolesList = new ArrayList<>(userRolesList);
	    userRolesList.add(Role.ROLE_APPADMIN.toString());
	}
	userManagementService.setRolesForUserOrganisation(user, orgId, userRolesList);

	if (userRolesList.contains(Role.ROLE_APPADMIN.toString())
		&& !userRolesList.contains(Role.ROLE_SYSADMIN.toString())) {
	    // appadmin need to have 2FA on, unless sysadmin says otherwise in user edit panels
	    user.setTwoFactorAuthenticationEnabled(true);
	    userManagementService.save(user);
	}

	auditLog(orgId, userId, roles);

	return "redirect:/usermanage.do?org=" + orgId;
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private void auditLog(Integer organisationId, Integer userId, String[] roleIds) {
	List<String> roles = Stream.of(roleIds).collect(Collectors
		.mapping(roleId -> Role.ROLE_MAP.get(Integer.valueOf(roleId)), Collectors.toUnmodifiableList()));
	StringBuilder auditLogMessage = new StringBuilder("assigned to user ").append(userId).append(" roles ")
		.append(roles).append(" in organisation ").append(organisationId);
	AuditLogFilter.log(auditLogMessage);
    }
}
