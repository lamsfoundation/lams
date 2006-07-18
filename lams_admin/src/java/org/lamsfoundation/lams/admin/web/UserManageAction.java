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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
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
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		// get id of org to list users for
		ActionMessages errors = new ActionMessages();
		Integer orgId = WebUtil.readIntParam(request,"org",true);
		if(orgId==null){
			orgId = (Integer)request.getAttribute("org");
		}
		if((orgId==null)||(orgId<=0)){
			errors.add("org",new ActionMessage("error.org.invalid"));
			saveErrors(request,errors);
			return mapping.findForward("error");
		}
		log.debug("orgId: "+orgId);
		
		// get org name
		Organisation organisation = (Organisation)getService().findById(Organisation.class,orgId);
		if(organisation==null) {
			errors.add("org",new ActionMessage("error.org.invalid"));
			saveErrors(request,errors);
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
		
		
		Integer userId = getService().getUserByLogin(request.getRemoteUser()).getUserId();
		Integer orgIdOfCourseAdmin = (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) ? pOrg.getOrganisationId() : orgId;
        // check permission
		if(request.isUserInRole(Role.SYSADMIN)){
			request.setAttribute("canAdd",true);
		}else if(!getService().isUserInRole(userId,orgIdOfCourseAdmin,Role.COURSE_ADMIN)){
			errors.add("authorisation",new ActionMessage("error.authorisation"));
			saveErrors(request,errors);
			return mapping.findForward("error");
		}else{
			request.setAttribute("canAdd",organisation.getCourseAdminCanAddNewUsers());
		}
		
		// get list of users in org
		List<User> users = getService().getUsersFromOrganisation(orgId);
		if(users==null){
			errors.add("org",new ActionMessage("error.org.invalid"));
			saveErrors(request,errors);
			return mapping.findForward("error");
		}
		
		// create form object
		UserListDTO userManageForm = new UserListDTO();
		userManageForm.setOrgId(orgId);
		userManageForm.setOrgName(orgName);
		
		// populate form object
		List<UserManageBean> userManageBeans = new ArrayList<UserManageBean>();
		for(int i=0; i<users.size(); i++) {
			User user = (User)users.get(i);
			UserManageBean userManageBean = new UserManageBean();
			BeanUtils.copyProperties(userManageBean, user);
			List roles;
			try{
				roles = getService().getRolesForUserByOrganisation(user, orgId);
				Collections.sort(roles);
			} catch(NullPointerException e){
				roles = new ArrayList();
				log.debug("no roles found for user: "+user);
			}
			userManageBean.setRoles(roles);
			userManageBeans.add(userManageBean);
		}
		
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
}
