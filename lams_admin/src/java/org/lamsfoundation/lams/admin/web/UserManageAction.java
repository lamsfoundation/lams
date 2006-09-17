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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserManageBean;
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
 * Created at 13:51:51 on 9/06/2006
 */

/**
 * struts doclets
 * 
 * @struts:action path="/usermanage"
 *                validate="false"
 *
 * @struts:action-forward name="userlist"
 *                        path=".userlist"
 */
public class UserManageAction extends Action {
	
	private static final Logger log = Logger.getLogger(UserManageAction.class);
	private static IUserManagementService service;
	private static MessageService messageService;
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		// get id of org to list users for
		Integer orgId = WebUtil.readIntParam(request,"org",true);
		if(orgId==null){
			orgId = (Integer)request.getAttribute("org");
		}
		if((orgId==null)||(orgId<=0)){
			request.setAttribute("errorName","UserManageAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.org.invalid"));
			return mapping.findForward("error");
		}
		log.debug("orgId: "+orgId);
		
		// get org name
		Organisation organisation = (Organisation)getService().findById(Organisation.class,orgId);
		if(organisation==null) {
			request.setAttribute("errorName","UserManageAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.org.invalid"));
			return mapping.findForward("error");
		}
		String orgName = organisation.getName();
		log.debug("orgName: "+orgName);
		
		Organisation pOrg = organisation.getParentOrganisation();
		if(pOrg!=null){
			request.setAttribute("pOrgId",pOrg.getOrganisationId());
			request.setAttribute("pOrgName",pOrg.getName());
		}
		OrganisationType orgType = organisation.getOrganisationType();
		request.setAttribute("orgType",orgType.getOrganisationTypeId());
		
		// create form object
		UserListDTO userManageForm = new UserListDTO();
		
		Integer userId = ((UserDTO)SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
		Organisation orgOfCourseAdmin = (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) ? pOrg : organisation;
        // check permission
		if(request.isUserInRole(Role.SYSADMIN)){
			userManageForm.setCourseAdminCanAddNewUsers(true);
			userManageForm.setCourseAdminCanBrowseAllUsers(true);
		}else if(getService().isUserInRole(userId,orgOfCourseAdmin.getOrganisationId(),Role.COURSE_ADMIN) 
				|| getService().isUserInRole(userId,orgOfCourseAdmin.getOrganisationId(),Role.COURSE_MANAGER)){
			userManageForm.setCourseAdminCanAddNewUsers(orgOfCourseAdmin.getCourseAdminCanAddNewUsers());
			userManageForm.setCourseAdminCanBrowseAllUsers(orgOfCourseAdmin.getCourseAdminCanBrowseAllUsers());
		}else{
			request.setAttribute("errorName","UserManageAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.authorisation"));
			return mapping.findForward("error");
		}
		
		userManageForm.setOrgId(orgId);
		userManageForm.setOrgName(orgName);
		List<UserManageBean> userManageBeans = getService().getUserManageBeans(orgId);
		Collections.sort(userManageBeans);
		userManageForm.setUserManageBeans(userManageBeans);
		request.setAttribute("UserManageForm", userManageForm);
		
		return mapping.findForward("userlist");
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
