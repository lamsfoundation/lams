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
package org.lamsfoundation.lams.admin.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.LdapService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author jliew
 *
 * @struts:action path="/ldap"
 *              scope="request"
 * 				validate="false"
 * 
 * @struts:action-forward name="ldap" path=".ldap"
 * @struts:action-forward name="sysadmin" path="/sysadminstart.do"
 */
public class LdapConfigAction extends Action {

	private static Logger log = Logger.getLogger(LdapConfigAction.class);
	private static IUserManagementService service;
	private static LdapService ldapService;
	private static MessageService messageService;
	
	private IUserManagementService getService() {
		if (service == null) {
			service = AdminServiceProxy.getService(getServlet().getServletContext());
		}
		return service;
	}
	
	private LdapService getLdapService() {
		if (ldapService == null) {
			ldapService = AdminServiceProxy.getLdapService(getServlet().getServletContext());
		}
		return ldapService;
	}
	
	private MessageService getMessageService() {
		if (messageService == null) {
			messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
		}
		return messageService;
	}
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		// check if url contains request for refresh folder sizes only
		String action = WebUtil.readStrParam(request, "action", true);
		if (action != null && StringUtils.equals(action, "sync")) {
			return sync(mapping, form, request, response);
		}
		
		// get number of ldap users
		List ldapUsers = getService().findByProperty(
				User.class, 
				"authenticationMethod.authenticationMethodId", 
				AuthenticationMethod.LDAP
			);
		if (ldapUsers != null) {
			int numLdapUsers = ldapUsers.size();
			request.setAttribute(
					"numLdapUsersMsg", 
					getMessageService().getMessage(
							"msg.num.ldap.users",
							getNumLdapUsersMessage(numLdapUsers)
					)
			);
		}
		
		return mapping.findForward("ldap");
	}
	
	public ActionForward sync(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		log.info("=== Beginning LDAP user sync ===");
		int numLdapUsers = getLdapService().updateLAMSFromLdap();
		log.info("=== Finished LDAP user sync ===");
		request.setAttribute(
				"numLdapUsersMsg", 
				getMessageService().getMessage(
						"msg.num.ldap.users",
						getNumLdapUsersMessage(numLdapUsers)
				)
		);
		request.setAttribute("done", getMessageService().getMessage("msg.done"));
		
		return mapping.findForward("ldap");
	}
	
	private String[] getNumLdapUsersMessage(int numLdapUsers) {
		String[] args = new String[1];
		args[0] = String.valueOf(numLdapUsers);
		return args;
	}
}
