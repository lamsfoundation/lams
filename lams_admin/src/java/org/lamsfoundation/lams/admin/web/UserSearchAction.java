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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 */

/**
 * struts doclets
 * 
 * @struts:action path="/usersearch"
 *                name="UserSearchForm"
 *                input=".usersearchlist"
 *                scope="request"
 *                validate="false"
 *
 * @struts:action-forward name="usersearchlist"
 *                        path=".usersearchlist"
 */
public class UserSearchAction extends Action {
	
	private static Logger log = Logger.getLogger(UserSearchAction.class);
	private static IUserManagementService service;

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if(!request.isUserInRole(Role.SYSADMIN)){
			log.debug("user not in role sysadmin");
			ActionMessages errors = new ActionMessages();
			errors.add("authorisation",new ActionMessage("error.authorisation"));
			saveErrors(request,errors);
			request.setAttribute("isSysadmin",false);
			return mapping.findForward("usersearchlist");
		}

		service = AdminServiceProxy.getService(getServlet().getServletContext());
		DynaActionForm userSearchForm = (DynaActionForm)form;
		
		String userId = ((String)userSearchForm.get("sUserId")).trim();
		String login = ((String)userSearchForm.get("sLogin")).trim();
		String firstName = ((String)userSearchForm.get("sFirstName")).trim();
		String lastName = ((String)userSearchForm.get("sLastName")).trim();
		Boolean showAll = (Boolean)userSearchForm.get("showAll");
		
		log.debug("got userId: '"+userId+"'");
		log.debug("got login: '"+login+"'");
		log.debug("got firstName: '"+firstName+"'");
		log.debug("got lastName: '"+lastName+"'");
		
		List userList = new ArrayList();
		if (showAll) {
			userList = service.findAll(User.class);
		} else {
			if(userId.length()==0) {
				Map<String, String> stringProperties = new HashMap<String,String>();
				if(login.length()>0) stringProperties.put("login","%"+login+"%");
				if(firstName.length()>0) stringProperties.put("firstName","%"+firstName+"%");
				if(lastName.length()>0) stringProperties.put("lastName","%"+lastName+"%");
				if(!stringProperties.isEmpty()) userList = service.searchByStringProperties(User.class,stringProperties);
			}else{
				Map<String, Object> objectProperties = new HashMap<String,Object>();
				objectProperties.put("userId",userId);
				if(login.length()>0) objectProperties.put("login",login);
				if(firstName.length()>0) objectProperties.put("firstName",firstName);
				if(lastName.length()>0) objectProperties.put("lastName",lastName);
				if(!objectProperties.isEmpty()) userList = service.findByProperties(User.class,objectProperties);
			}
		}
		
		if(userList.isEmpty() && (Boolean)userSearchForm.get("searched")){
			ActionMessages messages = new ActionMessages();
			messages.add("results",new ActionMessage("msg.results.none"));
			saveMessages(request,messages);
		}
		
		userList = removeDisabledUsers(userList);
		
		userSearchForm.set("showAll", false);
		userSearchForm.set("searched", true);
		request.setAttribute("userList",userList);
		return mapping.findForward("usersearchlist");
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
	
}
