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
//import java.util.Iterator;
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
//import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.Organisation;
//import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.User;
//import org.lamsfoundation.lams.usermanagement.UserOrganisation;
//import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 *
 * <p>
 * <a href="UserManageAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:jliew@melcoe.mq.edu.au">Jun-Dir Liew</a>
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
	
	private static final Logger log = Logger.getLogger(OrgManageAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static IUserManagementService service = (IUserManagementService)ctx.getBean("userManagementServiceTarget");
	
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
		log.debug("userlist orgId: "+orgId);
		
		// check user permission
		//String username = request.getRemoteUser();
		
		// get org name
		Organisation organisation = service.getOrganisationById(orgId);
		if(organisation==null) {
			errors.add("org",new ActionMessage("error.org.invalid"));
			saveErrors(request,errors);
			return mapping.findForward("error");
		}
		String orgName = organisation.getName();
		log.debug("userlist orgName: "+orgName);
		
		// get list of users in org
		List<User> users = service.getUsersFromOrganisation(orgId);
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
			userManageBeans.add(userManageBean);
		}
		
		userManageForm.setUserManageBeans(userManageBeans);
		request.setAttribute("UserManageForm", userManageForm);
		return mapping.findForward("userlist");
	}
}
