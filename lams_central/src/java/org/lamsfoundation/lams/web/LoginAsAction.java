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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * 
 * @struts.action path="/loginas" validate="false"
 * @struts.action-forward name="usersearch" path="/admin/usersearch.do"
 */
public class LoginAsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	IUserManagementService service = (IUserManagementService) ctx.getBean("userManagementService");
	MessageService messageService = (MessageService) ctx.getBean("centralMessageService");
	String login = WebUtil.readStrParam(request, "login", false);

	if (service.isUserSysAdmin()) {
	    if (login != null && login.trim().length() > 0) {
		if (service.getUserByLogin(login) != null) {
		    
		    // audit log when loginas
		    UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		    IAuditService auditService = (IAuditService) ctx.getBean("auditService");
		    String[] args = new String[]{sysadmin.getLogin() + "(" + sysadmin.getUserID() + ")", login};
		    String message = messageService.getMessage("audit.admin.loginas", args);
		    auditService.log(CentralConstants.MODULE_NAME, message);
		    
		    // logout, but not the LAMS shared session; needed by UniversalLoginModule
		    // to check for sysadmin role
		    request.getSession().invalidate();

		    // send to index page; the following attribute will be cleared there
		    request.getSession().setAttribute("login", login);
		    return (new ActionForward("/index.jsp"));
		}
	    }
	} else {
	    request.setAttribute("errorName", "LoginAsAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	return mapping.findForward("usersearch");
    }

}
