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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * 
 * Saves roles for users that were just added.
 * Uses session scope because using request scope doesn't copy the form data
 * into UserOrgRoleForm's userBeans ArrayList (the list becomes empty).
 *
 */

/**
 * struts doclets
 * 
 * @struts:action path="/userorgrolesave"
 *                name="UserOrgRoleForm"
 *                input=".userorgrole"
 *                scope="session"
 *                validate="false"
 *
 * @struts:action-forward name="userlist"
 *                        path="/usermanage.do"
 */
public class UserOrgRoleSaveAction extends Action {
	
	private static Logger log = Logger.getLogger(UserOrgRoleSaveAction.class);
	private static IUserManagementService service;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		UserOrgRoleForm userOrgRoleForm = (UserOrgRoleForm)form;
		ArrayList userBeans = userOrgRoleForm.getUserBeans();
		log.debug("userBeans is null?"+userBeans==null);
		Integer orgId = (Integer)userOrgRoleForm.getOrgId();
		request.setAttribute("org",orgId);
		request.getSession().removeAttribute("UserOrgRoleForm");		
		if(isCancelled(request)){
			return mapping.findForward("userlist");
		}
		
		log.debug("orgId: "+orgId);
		
		for(int i=0; i<userBeans.size(); i++){
			UserBean bean = (UserBean)userBeans.get(i);
			log.debug("user: "+bean.getUserId());
			String[] roleIds = bean.getRoleIds();
			UserOrganisation uo = getService().getUserOrganisation(bean.getUserId(),orgId);
			Set uors = uo.getUserOrganisationRoles();
			for(int j=0; j<roleIds.length; j++) {
				Role currentRole = (Role)getService().findById(Role.class,new Integer(roleIds[j]));
				log.debug("setting role: "+currentRole.getRoleId());
				UserOrganisationRole newUor = new UserOrganisationRole(uo,currentRole);
				uors.add(newUor);
			}
			uo.setUserOrganisationRoles(uors);
			getService().save(uo);
		}
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
