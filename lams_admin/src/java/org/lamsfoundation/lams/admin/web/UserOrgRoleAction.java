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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * 
 * Called when a user has added users to an organisation.
 *
 */

/**
 * struts doclets
 * 
 * @struts:action path="/userorgrole"
 *                name="UserOrgRoleForm"
 *                scope="session"
 *                validate="false"
 *
 * @struts:action-forward name="userorgrole"
 *                        path=".userorgrole"
 */
public class UserOrgRoleAction extends Action {
	
	private static Logger log = Logger.getLogger(UserOrgRoleAction.class);
	private static IUserManagementService service;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		UserOrgRoleForm userOrgRoleForm = (UserOrgRoleForm)form;
		
		// set list of roles appropriate for the organisation type
		List roles = (List)request.getAttribute("roles");
		request.setAttribute("numroles", roles.size());
		Collections.sort(roles);
		request.setAttribute("roles",roles);
		
		Organisation organisation = (Organisation)getService().findById(Organisation.class, (Integer)request.getAttribute("orgId"));
		userOrgRoleForm.setOrgId(organisation.getOrganisationId());
		
		// populate form with users
		ArrayList userOrgs = (ArrayList)request.getAttribute("newUserOrganisations");
		for(int i=0; i<userOrgs.size(); i++){
			UserBean userBean = new UserBean();
			User user = ((UserOrganisation)userOrgs.get(i)).getUser();
			BeanUtils.copyProperties(userBean,user);
			// flag users that will be added to parent group if necessary
			userBean.setMemberOfParent(true);
			if (organisation.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
				if (getService().getUserOrganisation(user.getUserId(), organisation.getParentOrganisation().getOrganisationId())==null)
					userBean.setMemberOfParent(false);
			}
			userOrgRoleForm.addUserBean(userBean);
			log.debug("ready to assign role for user="+userBean.getUserId());
		}
		log.debug("ready to assign roles for "+userOrgRoleForm.getUserBeans().size()+" new users in organisation "+organisation.getName());

		return mapping.findForward("userorgrole");		
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
	}

}