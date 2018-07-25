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


package org.lamsfoundation.lams.admin.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author Jun-Dir Liew
 *
 */

/**
 * struts doclets
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class UserOrgAction extends Action {

    private static final Logger log = Logger.getLogger(UserOrgAction.class);
    private static IUserManagementService service;
    private static MessageService messageService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(getServlet().getServletContext());
	messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());

	//ActionMessages errors = new ActionMessages();
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	log.debug("orgId: " + orgId);
	// get org name
	Organisation organisation = (Organisation) service.findById(Organisation.class, orgId);

	if ((orgId == null) || (orgId <= 0) || organisation == null) {
	    request.setAttribute("errorName", "UserOrgAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.org.invalid"));
	    return mapping.findForward("error");
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
	DynaActionForm userOrgForm = (DynaActionForm) form;
	userOrgForm.set("orgId", orgId);
	userOrgForm.set("orgName", orgName);

	String[] args = { "0" };
	request.setAttribute("numExistUsers", messageService.getMessage("label.number.of.users", args));
	request.setAttribute("numPotentialUsers", messageService.getMessage("label.number.of.potential.users", args));

	return mapping.findForward("userorg");
    }
}
