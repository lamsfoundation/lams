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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.usermanagement.Country;
import org.lamsfoundation.lams.usermanagement.Language;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Jun-Dir Liew
 *
 * Created at 17:00:18 on 13/06/2006
 */

/**
 * @struts:action path="/user"
 *              name="UserForm"
 *              scope="request"
 *              parameter="method"
 * 				validate="false"
 * 
 * @struts:action-forward name="user" path=".user"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 */
public class UserAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(UserAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils
			.getWebApplicationContext(HttpSessionManager.getInstance()
					.getServletContext());
	private static IUserManagementService service = (IUserManagementService) ctx
			.getBean("userManagementServiceTarget");
    private static List countries = service.findAll(Country.class);
	private static List languages = service.findAll(Language.class);
	
	public ActionForward edit(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // retain orgId to return to userlist
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		if(orgId != null) {
		    request.setAttribute("org",orgId);
		}
		
		// editing a user
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		if(userId != null) {
			log.debug("got userid to edit: "+userId);
			User user = (User)service.findById(User.class,userId);
			DynaActionForm userForm = (DynaActionForm)form;
			BeanUtils.copyProperties(userForm, user);
			BeanUtils.setProperty(userForm,"password2",user.getPassword());
			
			// get system's roles
			List allRoles = service.findAll(Role.class);
			/*ArrayList<RoleDTO> rolelist = new ArrayList<RoleDTO>();
			for(int i=0; i<allRoles.size(); i++){
				Role r = (Role)allRoles.get(i);
			    rolelist.add(new RoleDTO(r));
			}
			Collections.sort(rolelist);
			request.setAttribute("rolelist",rolelist);*/
			Collections.sort(allRoles);
			request.setAttribute("rolelist",allRoles);
			
			// get user's roles
			Iterator iter = user.getUserOrganisations().iterator();
			while(iter.hasNext()){
			    UserOrganisation uo = (UserOrganisation)iter.next();
			    if(uo.getOrganisation().getOrganisationId().equals(orgId)){
			        Iterator iter2 = uo.getUserOrganisationRoles().iterator();
			        String[] roles = new String[uo.getUserOrganisationRoles().size()];
			        int i=0;
			        while(iter2.hasNext()){
			            UserOrganisationRole uor = (UserOrganisationRole)iter2.next();
			            roles[i]=uor.getRole().getRoleId().toString();
			            log.debug("got roleid: "+roles[i]);
			            i++;
			        }
			        userForm.set("roles",roles);
			        break;
			    }
			}
			
		}
		
		Organisation org = (Organisation)service.findById(Organisation.class,orgId);
		Organisation pOrg = org.getParentOrganisation();
		if(pOrg!=null){
			request.setAttribute("pOrgId",pOrg.getOrganisationId());
			request.setAttribute("pOrgName",pOrg.getName());
		}
		request.setAttribute("orgId",orgId);
		request.setAttribute("orgName",org.getName());
		request.setAttribute("orgType",org.getOrganisationType().getOrganisationTypeId());
		request.setAttribute("countries",countries);
		request.setAttribute("languages",languages);
		return mapping.findForward("user");
	}
	
	public ActionForward remove(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		log.debug("removing userid: "+userId);
		service.deleteById(User.class,userId);
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		request.setAttribute("org",orgId);
		return mapping.findForward("userlist");
	}
}
