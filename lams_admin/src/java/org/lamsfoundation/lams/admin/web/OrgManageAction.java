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
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.OrgManageForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @version
 *
 * <p>
 * <a href="OrgManageAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 20:29:13 on 2006-6-5
 */

/**
 * struts doclets
 * 
 * @struts:action path="/orgmanage"
 * 				  name="OrgManageForm"
 * 				  input=".orglist"
 * 				  scope="request"
 *                validate="false"
 *
 * @struts:action-forward name="orglist" path=".orglist"
 */
public class OrgManageAction extends Action {
	
	private static IUserManagementService service;
	private static MessageService messageService;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		service = AdminServiceProxy.getService(getServlet().getServletContext());
		messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		
		// Get organisation whose child organisations we will populate the OrgManageForm with
		Integer orgId = WebUtil.readIntParam(request, "org", true);
		Organisation org = null;
		if (orgId == null){
			orgId = (Integer)request.getAttribute("org");
		}
		if ((orgId != null) && (orgId > 0)) {
			org = (Organisation)service.findById(Organisation.class, orgId);
			if (org == null){
				request.setAttribute("errorName", "OrgManageAction");
				request.setAttribute("errorMessage", messageService.getMessage("error.org.invalid"));
				return mapping.findForward("error");
			}
		}
		
		// get number of users figure
		// TODO use hql that does a count instead of getting whole objects
		Integer numUsers = (service.getRootOrganisation().equals(org)
				? service.getCountUsers()
				: Integer.valueOf(service.getUsersFromOrganisation(orgId).size())
			);
		String key = (service.getRootOrganisation().getOrganisationId().equals(orgId)
				? "label.users.in.system"
				: "label.users.in.group"
			);
		String[] args = { numUsers.toString() };
		request.setAttribute("numUsers", messageService.getMessage(key, args));
		
		// get logged in user's id
		Integer userId = ((UserDTO)SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
		
		// Set OrgManageForm
		OrgManageForm orgManageForm = (OrgManageForm)form;
		if (orgManageForm == null) {
			orgManageForm = new OrgManageForm();
			orgManageForm.setStateId(OrganisationState.ACTIVE);
		} else if (orgManageForm.getStateId() == null) {
			orgManageForm.setStateId(OrganisationState.ACTIVE);
		}
		orgManageForm.setParentId(orgId);
		orgManageForm.setParentName(org.getName());
		orgManageForm.setType(org.getOrganisationType().getOrganisationTypeId());
		
		// Get list of child organisations depending on requestor's role and the organisation's type
		if (!orgManageForm.getType().equals(OrganisationType.CLASS_TYPE)) {
			List<OrgManageBean> orgManageBeans = new ArrayList<OrgManageBean>();
			if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN) || request.isUserInRole(Role.GROUP_MANAGER)){
				// the organisation type of the children
				Integer typeId = (orgManageForm.getType().equals(OrganisationType.ROOT_TYPE) 
						? OrganisationType.COURSE_TYPE 
						: OrganisationType.CLASS_TYPE
					);

				HashMap<String, Object> properties = new HashMap<String, Object>();
				properties.put("organisationType.organisationTypeId", typeId);
				properties.put("organisationState.organisationStateId", orgManageForm.getStateId());
				List organisations = service.findByProperties(Organisation.class, properties);
				
				for (int i=0; i<organisations.size(); i++){
					Organisation organisation = (Organisation)organisations.get(i);
					Organisation parentOrg = (typeId.equals(OrganisationType.CLASS_TYPE)) ? organisation.getParentOrganisation() : organisation;
					// do not list this org as a child if requestor is not an admin or manager in the parent, or global admin
					if (!request.isUserInRole(Role.SYSADMIN)) {
						if (!(service.isUserInRole(userId, parentOrg.getOrganisationId(), Role.GROUP_ADMIN)
								|| service.isUserInRole(userId, parentOrg.getOrganisationId(), Role.GROUP_MANAGER)
								|| service.isUserGlobalGroupAdmin()))
							continue;
					}
					// do not list this org if it is not a child of the requested parent
					if (typeId.equals(OrganisationType.CLASS_TYPE)){
						if (!parentOrg.getOrganisationId().equals(orgId))
							continue;
					}
					OrgManageBean orgManageBean = new OrgManageBean();
					BeanUtils.copyProperties(orgManageBean,organisation);
					orgManageBean.setStatus(organisation.getOrganisationState().getDescription());
					orgManageBean.setEditable(true);
					orgManageBeans.add(orgManageBean);
				}
			}
			Collections.sort(orgManageBeans);
			orgManageForm.setOrgManageBeans(orgManageBeans);
			request.setAttribute("OrgManageForm", orgManageForm);
			
			// display org info
			request.setAttribute("org", org);
		} else {
			// display class info, with parent group's 'courseAdminCan...' permissions.
			// note the org is not saved, properties set only for passing to view component.
			Organisation pOrg = org.getParentOrganisation();
			org.setCourseAdminCanAddNewUsers(pOrg.getCourseAdminCanAddNewUsers());
			org.setCourseAdminCanBrowseAllUsers(pOrg.getCourseAdminCanBrowseAllUsers());
			org.setCourseAdminCanChangeStatusOfCourse(pOrg.getCourseAdminCanChangeStatusOfCourse());
			request.setAttribute("org", org);
			
			// display parent org breadcrumb link
			request.setAttribute("parentGroupName", pOrg.getName());
			request.setAttribute("parentGroupId", pOrg.getOrganisationId());
		}
		
		
		
		// let the jsp know whether to display links
		request.setAttribute("createGroup", request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin());
		request.setAttribute("editGroup", service.canEditGroup(userId, orgId));
		request.setAttribute("manageGlobalRoles", request.isUserInRole(Role.SYSADMIN));
		return mapping.findForward("orglist");
	}

}
