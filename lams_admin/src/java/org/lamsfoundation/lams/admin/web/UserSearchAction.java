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
import org.lamsfoundation.lams.util.MessageService;

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
	private static MessageService messageService;

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (service == null) {
			service = AdminServiceProxy.getService(getServlet().getServletContext());
		}
		if (messageService == null) {
			messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		}
		DynaActionForm userSearchForm = (DynaActionForm)form;
		
		if(!(request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin())){
			log.debug("user not sysadmin or global group admin");
			ActionMessages errors = new ActionMessages();
			errors.add("authorisation",new ActionMessage("error.authorisation"));
			saveErrors(request,errors);
			request.setAttribute("isSysadmin",false);
			return mapping.findForward("usersearchlist");
		}
		
		Boolean showAll = (Boolean)userSearchForm.get("showAll");
		Boolean searched = (Boolean)userSearchForm.get("searched");
		String term = ((String)userSearchForm.getString("term")).trim();
		
		if (searched) {
			List userList = new ArrayList();
			if (showAll) {
				Map<String, Object> objectProperties = new HashMap<String,Object>();
				objectProperties.put("disabledFlag",false);
				userList = service.findByProperties(User.class,objectProperties);
			} else {
				userList = service.searchUserSingleTerm(term);
			}
			if (userList.isEmpty()) {
				ActionMessages messages = new ActionMessages();
				messages.add("results",new ActionMessage("msg.results.none"));
				saveMessages(request,messages);
			}
			
			request.setAttribute("userList", userList);
			String[] args = { new Integer(userList.size()).toString() };
			request.setAttribute("numUsers", messageService.getMessage("label.number.of.users", args));
		}
		
		userSearchForm.set("showAll", false);
		userSearchForm.set("searched", true);
		
		return mapping.findForward("usersearchlist");
	}
	
}
