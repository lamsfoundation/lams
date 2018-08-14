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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author Jun-Dir Liew
 *
 * Created at 17:22:21 on 20/06/2006
 */

/**
 * struts doclets
 *
 *
 *
 *
 *
 */
public class UserOrgSaveAction extends Action {

    private static Logger log = Logger.getLogger(UserOrgSaveAction.class);
    private static IUserManagementService service;
    private List<Role> rolelist;

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm userOrgForm = (DynaActionForm) form;

	Integer orgId = (Integer) userOrgForm.get("orgId");
	request.setAttribute("org", orgId);

	if (isCancelled(request)) {
	    return mapping.findForward("userlist");
	}

	service = AdminServiceProxy.getService(getServlet().getServletContext());
	if (rolelist == null) {
	    rolelist = service.findAll(Role.class);
	}

	Organisation organisation = (Organisation) service.findById(Organisation.class, orgId);
	Set uos = organisation.getUserOrganisations();

	String[] userIds = (String[]) userOrgForm.get("userIds");
	List<String> userIdList = Arrays.asList(userIds);
	log.debug("new user membership of orgId=" + orgId + " will be: " + userIdList);

	// remove UserOrganisations that aren't in form data
	Iterator iter = uos.iterator();
	while (iter.hasNext()) {
	    UserOrganisation uo = (UserOrganisation) iter.next();
	    Integer userId = uo.getUser().getUserId();
	    if (userIdList.indexOf(userId.toString()) < 0) {
		User user = (User) service.findById(User.class, userId);
		Set userUos = user.getUserOrganisations();
		userUos.remove(uo);
		user.setUserOrganisations(userUos);
		iter.remove();
		log.debug("removed userId=" + userId + " from orgId=" + orgId);
		// remove from subgroups
		service.deleteChildUserOrganisations(uo.getUser(), uo.getOrganisation());
	    }
	}
	// add UserOrganisations that are in form data
	List newUserOrganisations = new ArrayList();
	for (int i = 0; i < userIdList.size(); i++) {
	    Integer userId = new Integer(userIdList.get(i));
	    Iterator iter2 = uos.iterator();
	    Boolean alreadyInOrg = false;
	    while (iter2.hasNext()) {
		UserOrganisation uo = (UserOrganisation) iter2.next();
		if (uo.getUser().getUserId().equals(userId)) {
		    alreadyInOrg = true;
		    break;
		}
	    }
	    if (!alreadyInOrg) {
		User user = (User) service.findById(User.class, userId);
		UserOrganisation uo = new UserOrganisation(user, organisation);
		newUserOrganisations.add(uo);
	    }
	}

	organisation.setUserOrganisations(uos);
	service.save(organisation);

	// if no new users, then finish; otherwise forward to where roles can be assigned for new users.
	if (newUserOrganisations.isEmpty()) {
	    log.debug("no new users to add to orgId=" + orgId);
	    return mapping.findForward("userlist");
	} else {
	    request.setAttribute("roles", service.filterRoles(rolelist, request.isUserInRole(Role.SYSADMIN),
		    organisation.getOrganisationType()));
	    request.setAttribute("newUserOrganisations", newUserOrganisations);
	    request.setAttribute("orgId", orgId);
	    return mapping.findForward("userorgrole");
	}
    }

}
