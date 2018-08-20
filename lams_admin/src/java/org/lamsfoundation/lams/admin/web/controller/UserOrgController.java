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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.UserOrgForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Jun-Dir Liew
 *
 */

/**
 *
 *
 *
 *
 *
 *
 *
 *
 */
@Controller
public class UserOrgController {

    private static final Logger log = Logger.getLogger(UserOrgController.class);
    private static IUserManagementService service;
    private static MessageService messageService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/userorg", method = RequestMethod.POST)
    public String execute(@ModelAttribute UserOrgForm userOrgForm, HttpServletRequest request) throws Exception {

	service = AdminServiceProxy.getService(applicationContext.getServletContext());
	messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());

	//ActionMessages errors = new ActionMessages();
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	log.debug("orgId: " + orgId);
	// get org name
	Organisation organisation = (Organisation) service.findById(Organisation.class, orgId);

	if ((orgId == null) || (orgId <= 0) || organisation == null) {
	    request.setAttribute("errorName", "UserOrgAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.org.invalid"));
	    return "error";
	}

	String orgName = organisation.getName();
	log.debug("orgName: " + orgName);
	Organisation parentOrg = organisation.getParentOrganisation();
	if (parentOrg != null && !parentOrg.equals(service.getRootOrganisation())) {
	    request.setAttribute("pOrgId", parentOrg.getOrganisationId());
	    request.setAttribute("pOrgName", parentOrg.getName());
	}
	Integer orgType = organisation.getOrganisationType().getOrganisationTypeId();
	request.setAttribute("orgType", orgType);

	// create form object
	userOrgForm.setOrgId(orgId);
	userOrgForm.setOrgName(orgName);

	String[] args = { "0" };
	request.setAttribute("numExistUsers", messageService.getMessage("label.number.of.users", args));
	request.setAttribute("numPotentialUsers", messageService.getMessage("label.number.of.potential.users", args));

	return "userorg";
    }
}
