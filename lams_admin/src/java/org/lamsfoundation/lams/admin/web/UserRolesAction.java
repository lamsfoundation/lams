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

/* $Id$ */
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 * @struts:action path="/userroles"
 *              name="UserRolesForm"
 *              scope="request"
 *              parameter="method"
 * 				validate="false"
 * 
 * @struts:action-forward name="userrole" path=".userrole"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 */
public class UserRolesAction extends Action {
	
	private static Logger log = Logger.getLogger(UserRolesAction.class);
	private static IUserManagementService service;
	private static MessageService messageService;
	private static List<Role> rolelist;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		ActionMessages errors = new ActionMessages();
		DynaActionForm userRolesForm = (DynaActionForm)form;
		Integer orgId = WebUtil.readIntParam(request,"orgId",true);
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		
		// user and org ids passed as attributes by UserSaveAction
		if (orgId==null) orgId = (Integer)request.getAttribute("orgId");
		if (orgId==null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.org.invalid"));
			saveErrors(request,errors);
			return mapping.findForward("userrole");
		}
		if (userId==null) userId = (Integer)request.getAttribute("userId");
		if (userId==null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.userid.invalid"));
			saveErrors(request,errors);
			return mapping.findForward("userrole");
		}
		log.debug("editing roles for userId: "+userId+" and orgId: "+orgId);
		
		// test requestor's permission
		Organisation org = (Organisation)getService().findById(Organisation.class,orgId);
		User user = (User)getService().findById(User.class, userId);
		OrganisationType orgType = org.getOrganisationType();
		Integer orgIdOfCourse = (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) 
			? org.getParentOrganisation().getOrganisationId() : orgId;
		Boolean isSysadmin = request.isUserInRole(Role.SYSADMIN);
		User requestor = (User)getService().getUserByLogin(request.getRemoteUser());
		Boolean requestorHasRole = getService().isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.COURSE_ADMIN)
			|| getService().isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.COURSE_MANAGER);
		
		if (!(requestorHasRole || isSysadmin)) {
			request.setAttribute("errorName","UserRolesAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.authorisation"));
			return mapping.findForward("error");
		}
		
		userRolesForm.set("userId",userId);
		userRolesForm.set("orgId", org.getOrganisationId());
		request.setAttribute("rolelist",filterRoles(rolelist,isSysadmin,orgType));
		request.setAttribute("login", user.getLogin());
		request.setAttribute("fullName", user.getFullName());
		
		String[] roles = null;
		UserOrganisation uo = getService().getUserOrganisation(userId, orgId);
		if (uo != null) {
		   	Iterator iter2 = uo.getUserOrganisationRoles().iterator();
		   	roles = new String[uo.getUserOrganisationRoles().size()];
		   	int i=0;
		   	while(iter2.hasNext()){
		   		UserOrganisationRole uor = (UserOrganisationRole)iter2.next();
		   		roles[i]=uor.getRole().getRoleId().toString();
		   		log.debug("got roleid: "+roles[i]);
		   		i++;
		   	}
		} else {
			ActionMessages messages = new ActionMessages();
			messages.add("roles", new ActionMessage("msg.add.to.org", org.getName()));
		   	saveMessages(request,messages);
		}
	    userRolesForm.set("roles",roles);
		
		return mapping.findForward("userrole");
	}
	
	private List<Role> filterRoles(List<Role> rolelist, Boolean isSysadmin, OrganisationType orgType){
		List<Role> allRoles = new ArrayList<Role>();
		allRoles.addAll(rolelist);
		Role role = new Role();
		if(!isSysadmin) {
			role.setRoleId(Role.ROLE_SYSADMIN);
			allRoles.remove(role);
		}
		if(orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
			role.setRoleId(Role.ROLE_COURSE_ADMIN);
			allRoles.remove(role);
			role.setRoleId(Role.ROLE_COURSE_MANAGER);
			allRoles.remove(role);
		}
		return allRoles;
	}
	
	@SuppressWarnings("unchecked")
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
			rolelist = service.findAll(Role.class);
			Collections.sort(rolelist);
		}
		return service;
	}

	private MessageService getMessageService(){
		if(messageService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			messageService = (MessageService)ctx.getBean("adminMessageService");
		}
		return messageService;
	}
}
