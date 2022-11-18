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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class DisabledUserManageController {
    private static final Logger log = Logger.getLogger(DisabledUserManageController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private MessageService adminMessageService;

    @RequestMapping("/disabledmanage")
    public String execute(HttpServletRequest request) throws Exception {
	if (!(request.isUserInRole(Role.APPADMIN) || request.isUserInRole(Role.SYSADMIN)
		|| userManagementService.isUserGlobalGroupManager())) {
	    request.setAttribute("errorName", "DisabledUserManageAction");
	    request.setAttribute("errorMessage", adminMessageService.getMessage("error.need.appadmin"));
	    return "error";
	}

	List<User> users = userManagementService.findByProperty(User.class, "disabledFlag", true);
	log.debug("got " + users.size() + " disabled users");
	request.setAttribute("users", users);

	return "disabledusers";
    }

}
