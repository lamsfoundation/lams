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
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Jun-Dir Liew
 *
 * Created at 17:22:21 on 20/06/2006
 */

/**
 * struts doclets
 * 
 * @struts:action path="/userorgsave"
 *                name="UserOrgForm"
 *                input=".userorg"
 *                scope="request"
 *                validate="false"
 *
 * @struts:action-forward name="userlist"
 *                        path="/usermanage.do"
 * @struts:action-forward name="userorgrole"
 *                        path="/userorgrole.do"
 */
public class UserOrgSaveAction extends Action{

	private static Logger log = Logger.getLogger(UserOrgSaveAction.class);
	private static IUserManagementService service;
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		DynaActionForm userOrgForm = (DynaActionForm)form;
		log.debug("orgId: "+userOrgForm.get("orgId"));
				
		Integer orgId = (Integer)userOrgForm.get("orgId");
		request.setAttribute("org",orgId);
		
		if(isCancelled(request)){
			return mapping.findForward("userlist");
		}
		
		Organisation organisation = (Organisation)getService().findById(Organisation.class, orgId);
		Set uos = organisation.getUserOrganisations();
		
		String[] userIds = (String[])userOrgForm.get("userIds");
		List<String> userIdList = Arrays.asList(userIds);
		log.debug("userIdList: "+userIdList);
		List newUserOrganisations = new ArrayList();
		
		// remove UserOrganisations that aren't in form data
		Iterator iter = uos.iterator();
		while(iter.hasNext()){
			UserOrganisation uo = (UserOrganisation)iter.next();
			Integer userId = uo.getUser().getUserId();
			if(userIdList.indexOf(userId.toString())<0){
				iter.remove();
				User user = (User)getService().findById(User.class, userId);
				Set userUos = user.getUserOrganisations();
				userUos.remove(uo);
				user.setUserOrganisations(userUos);
				log.debug("removed: "+userId);
			}
		}
		// add UserOrganisations that are in form data
		for(int i=0; i<userIdList.size(); i++){
			Integer userId = new Integer((String)userIdList.get(i));
			Iterator iter2 = uos.iterator();
			Boolean alreadyInOrg = false;
			while(iter2.hasNext()){
				UserOrganisation uo = (UserOrganisation)iter2.next();
				if(uo.getUser().getUserId().equals(userId)){
					alreadyInOrg = true;
					break;
				}
			}
			if(!alreadyInOrg){
				User user = (User)getService().findById(User.class,userId);
				UserOrganisation uo = new UserOrganisation(user,organisation);
				newUserOrganisations.add(uo);
				/*Role role = (Role)getService().findByProperty(Role.class,"name",Role.LEARNER).get(0);
		        UserOrganisationRole uor = new UserOrganisationRole(uo,role);
		        getService().save(uor);*/
			}
		}
		
		organisation.setUserOrganisations(uos);
		getService().save(organisation);
		
		if(newUserOrganisations.isEmpty()){
			return mapping.findForward("userlist");
		}else{  // send user to screen where they can assign roles for the newly added users
			List roles = getService().getRolesForOrgType(organisation.getOrganisationType(),false);
			request.setAttribute("roles",roles);
			request.setAttribute("newUserOrganisations",newUserOrganisations);
			request.setAttribute("orgId",orgId);
			return mapping.findForward("userorgrole");
		}
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
	}
}
