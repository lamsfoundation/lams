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

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Fei Yang
 *
 *
 *
 *
 *
 *
 */
public class OrganisationAction extends LamsDispatchAction {

    private static IUserManagementService service;
    private static MessageService messageService;
    private static List<SupportedLocale> locales;
    private static List status;

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	OrganisationAction.service = AdminServiceProxy.getService(getServlet().getServletContext());
	initLocalesAndStatus();
	DynaActionForm orgForm = (DynaActionForm) form;
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);

	HttpSession session = SessionManager.getSession();
	if (session != null) {
	    UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	    if (userDto != null) {
		Integer userId = userDto.getUserID();
		// sysadmin, global group admin, group manager, group admin can edit group
		if (OrganisationAction.service.canEditGroup(userId, orgId)) {
		    // edit existing organisation
		    if (orgId != null) {
			Organisation org = (Organisation) OrganisationAction.service.findById(Organisation.class,
				orgId);
			BeanUtils.copyProperties(orgForm, org);
			orgForm.set("parentId", org.getParentOrganisation().getOrganisationId());
			orgForm.set("parentName", org.getParentOrganisation().getName());
			orgForm.set("typeId", org.getOrganisationType().getOrganisationTypeId());
			orgForm.set("stateId", org.getOrganisationState().getOrganisationStateId());
			SupportedLocale locale = org.getLocale();
			orgForm.set("localeId", locale != null ? locale.getLocaleId() : null);
		    }
		    request.getSession().setAttribute("locales", OrganisationAction.locales);
		    request.getSession().setAttribute("status", OrganisationAction.status);
		    if (OrganisationAction.service.isUserSysAdmin()
			    || OrganisationAction.service.isUserGlobalGroupAdmin()) {
			return mapping.findForward("organisation");
		    } else {
			return mapping.findForward("organisationCourseAdmin");
		    }
		}
	    }
	}

	return error(mapping, request);
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	OrganisationAction.service = AdminServiceProxy.getService(getServlet().getServletContext());
	initLocalesAndStatus();
	DynaActionForm orgForm = (DynaActionForm) form;

	if (!(request.isUserInRole(Role.SYSADMIN) || OrganisationAction.service.isUserGlobalGroupAdmin())) {
	    // only sysadmins and global group admins can create groups
	    if (((orgForm.get("typeId") != null) && orgForm.get("typeId").equals(OrganisationType.COURSE_TYPE))
		    || (orgForm.get("typeId") == null)) {
		return error(mapping, request);
	    }
	}

	// creating new organisation
	orgForm.set("orgId", null);
	Integer parentId = WebUtil.readIntParam(request, "parentId", true);
	if (parentId != null) {
	    Organisation parentOrg = (Organisation) OrganisationAction.service.findById(Organisation.class, parentId);
	    orgForm.set("parentName", parentOrg.getName());
	}
	request.getSession().setAttribute("locales", OrganisationAction.locales);
	request.getSession().setAttribute("status", OrganisationAction.status);
	return mapping.findForward("organisation");
    }

    /**
     * Looks up course ID by its name. Used mainly by TestHarness.
     */
    @SuppressWarnings("unchecked")
    public ActionForward getOrganisationIdByName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String organisationName = WebUtil.readStrParam(request, "name");
	OrganisationAction.service = AdminServiceProxy.getService(getServlet().getServletContext());
	List<Organisation> organisations = service.findByProperty(Organisation.class, "name", organisationName);
	if (!organisations.isEmpty()) {
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().print(organisations.get(0).getOrganisationId());
	}
	return null;
    }

    private ActionForward error(ActionMapping mapping, HttpServletRequest request) {
	OrganisationAction.messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	request.setAttribute("errorName", "OrganisationAction");
	request.setAttribute("errorMessage", OrganisationAction.messageService.getMessage("error.authorisation"));
	return mapping.findForward("error");
    }

    /*
     * public ActionForward remove(ActionMapping mapping, ActionForm form,HttpServletRequest request,
     * HttpServletResponse response){
     * Integer orgId = WebUtil.readIntParam(request,"orgId");
     * getService().deleteById(Organisation.class,orgId);
     * Integer parentId = WebUtil.readIntParam(request,"parentId");
     * request.setAttribute("org",parentId);
     * return mapping.findForward("orglist");
     * }
     */

    @SuppressWarnings("unchecked")
    private void initLocalesAndStatus() {
	if ((OrganisationAction.locales == null)
		|| ((OrganisationAction.status == null) && (OrganisationAction.service != null))) {
	    OrganisationAction.locales = OrganisationAction.service.findAll(SupportedLocale.class);
	    OrganisationAction.status = OrganisationAction.service.findAll(OrganisationState.class);
	    Collections.sort(OrganisationAction.locales);
	}
    }
}