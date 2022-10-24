/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.admin.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Marcin Cieslak
 */
@Controller
@RequestMapping("/sessionmaintain")
public class SessionMaintainController {
    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping(path = "/list")
    public String list(HttpServletRequest request) {
	request.setAttribute("sessions", SessionManager.getLoginToSessionIDMappings());
	return "sessionmaintain";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) {
	String login = request.getParameter("login");
	if (StringUtils.isNotBlank(login)) {
	    User user = userManagementService.getUserByLogin(login);

	    SessionManager.removeSessionByLogin(login, true);

	    String message = new StringBuilder("User ").append(login).append(" (").append(user.getUserId())
		    .append(") got logged out by appadmin").toString();
	    logEventService.logEvent(LogEvent.TYPE_LOGOUT, user.getUserId(), user.getUserId(), null, null, message);

	    AuditLogFilter.log(AuditLogFilter.SESSION_DELETE_ACTION, message);
	}
	return list(request);
    }
}
