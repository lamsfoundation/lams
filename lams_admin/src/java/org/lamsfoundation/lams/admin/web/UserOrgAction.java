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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Jun-Dir Liew
 *
 */

/**
 * struts doclets
 * 
 * @struts:action path="/userorg"
 *                name="UserOrgForm"
 *                scope="request"
 *                validate="false"
 *
 * @struts:action-forward name="userorg"
 *                        path=".userorg"
 */
public class UserOrgAction extends Action {
	
	private static final Logger log = Logger.getLogger(UserOrgAction.class);
	private static IUserManagementService service;
	private static MessageService messageService;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		ActionMessages errors = new ActionMessages();
		Integer orgId = WebUtil.readIntParam(request,"orgId",true);
		log.debug("orgId: "+orgId);
        // get org name
		Organisation organisation = (Organisation)getService().findById(Organisation.class,orgId);

		if((orgId==null)||(orgId<=0)||organisation==null){
			request.setAttribute("errorName","UserOrgAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.org.invalid"));
			return mapping.findForward("error");
		}
		
		String orgName = organisation.getName();
		log.debug("orgName: "+orgName);
		Organisation parentOrg = organisation.getParentOrganisation();
		if(parentOrg!=null){
			request.setAttribute("pOrgId",parentOrg.getOrganisationId());
			request.setAttribute("pOrgName",parentOrg.getName());
		}
		Integer orgType = organisation.getOrganisationType().getOrganisationTypeId();
		request.setAttribute("orgType",orgType);
				
		// get list of users in org
		Integer userId = ((UserDTO)SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
		List users = new ArrayList<User>();
		Organisation orgOfCourseAdmin = (orgType.equals(OrganisationType.CLASS_TYPE)) ? parentOrg : organisation;
		if(request.isUserInRole(Role.SYSADMIN)){
			users = getService().findAll(User.class);
		}else if(getService().isUserInRole(userId,orgOfCourseAdmin.getOrganisationId(),Role.COURSE_ADMIN)
				|| getService().isUserInRole(userId,orgOfCourseAdmin.getOrganisationId(),Role.COURSE_MANAGER)){
			if(orgOfCourseAdmin.getCourseAdminCanBrowseAllUsers()){
				users = getService().findAll(User.class);
			}else if(orgType.equals(OrganisationType.CLASS_TYPE)){
				users = getService().getUsersFromOrganisation(parentOrg.getOrganisationId());
			}else if(orgType.equals(OrganisationType.COURSE_TYPE)){
				users = getService().getUsersFromOrganisation(orgId);
			}
		}else{
			request.setAttribute("errorName","UserOrgAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.authorisation"));
			return mapping.findForward("error");
		}
		users = removeDisabledUsers(users);
		Collections.sort(users);
		request.setAttribute("userlist",users);
		
		// create form object
		DynaActionForm userOrgForm = (DynaActionForm)form;
		userOrgForm.set("orgId",orgId);
		userOrgForm.set("orgName",orgName);
				
		// create list of userids, members of this org
		List<User> memberUsers = getService().getUsersFromOrganisation(orgId);
		String[] userIds = new String[memberUsers.size()];
		for(int i=0; i<userIds.length; i++){
			userIds[i] = memberUsers.get(i).getUserId().toString();
		}
		userOrgForm.set("userIds",userIds);

		return mapping.findForward("userorg");
	}
	
	private List removeDisabledUsers(List userList) {
		List filteredList = new ArrayList();
		for(int i=0; i<userList.size(); i++) {
			User u = (User)userList.get(i);
			if(!u.getDisabledFlag()) {
				filteredList.add(u);
			}
		}
		return filteredList;
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
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
