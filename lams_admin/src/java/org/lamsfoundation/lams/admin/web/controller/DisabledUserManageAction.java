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


package org.lamsfoundation.lams.admin.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author jliew
 * 
 *
 *
 *
 */
public class DisabledUserManageAction extends Action {

    private static final Logger log = Logger.getLogger(DisabledUserManageAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserManagementService service = AdminServiceProxy.getService(getServlet().getServletContext());

	if (!(request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin())) {
	    request.setAttribute("errorName", "DisabledUserManageAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.need.sysadmin"));
	    return mapping.findForward("error");
	}

	List users = service.findByProperty(User.class, "disabledFlag", true);
	log.debug("got " + users.size() + " disabled users");
	request.setAttribute("users", users);

	return mapping.findForward("disabledlist");
    }

}
