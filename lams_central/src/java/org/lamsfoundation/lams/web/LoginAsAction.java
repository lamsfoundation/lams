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


package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.security.UniversalLoginModule;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 *
 *
 */
public class LoginAsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	WebApplicationContext ctx = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	IUserManagementService service = (IUserManagementService) ctx.getBean("userManagementService");
	MessageService messageService = (MessageService) ctx.getBean("centralMessageService");
	String login = WebUtil.readStrParam(request, "login", false);

	if (service.isUserSysAdmin()) {
	    if ((login != null) && (login.trim().length() > 0)) {
		User user = service.getUserByLogin(login);
		if (user != null) {
		    // audit log when loginas
		    UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		    ILogEventService logEventService = (ILogEventService) ctx.getBean("logEventService");
		    String[] args = new String[] { sysadmin.getLogin() + " (" + sysadmin.getUserID() + ")", login };
		    String message = messageService.getMessage("audit.admin.loginas", args);
		    logEventService.logEvent(LogEvent.TYPE_LOGIN_AS, sysadmin.getUserID(), user.getUserId(), null, null, message);

		    // login.jsp knows what to do with these
		    request.setAttribute("login", login);
		    String token = "#LAMS" + RandomPasswordGenerator.nextPassword(10);
		    request.setAttribute("password", token);
		    // notify the login module that the user has been authenticated correctly
		    UniversalLoginModule.setAuthenticationToken(token);
		    // redirect to login page
		    return (new ActionForward("/login.jsp?redirectURL=/lams/index.jsp"));
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
