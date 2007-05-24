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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 * @struts.action path="/loginas" validate="false"
 * @struts.action-forward name="usersearch" path="/admin/usersearch.do"
 */
public class LoginAsAction extends Action {
	
	private static final String JNDI_DATASOURCE = "java:/jdbc/lams-ds";
	private static final String PASSWORD_QUERY = "select password from lams_user where login=?";
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		IUserManagementService service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		MessageService messageService = (MessageService)ctx.getBean("centralMessageService");
		String login = WebUtil.readStrParam(request, "login", false);
		
		if (service.isUserSysAdmin()) {
			if (login!=null && login.trim().length()>0) {
				if (service.getUserByLogin(login)!=null) {
					// logout
					request.getSession().invalidate();
					SessionManager.getSession().invalidate();
					
					// send to index page; session attributes will be cleared there
					String pass = getUserPassword(login);
					request.getSession().setAttribute("login", login);
					request.getSession().setAttribute("pass", pass);
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

	// Copied from LoginRequestServlet.java
	// using JDBC connection to prevent the caching of passwords by hibernate
	private String getUserPassword(String username) throws FailedLoginException, NamingException,
			SQLException {
		InitialContext ctx = new InitialContext();

		DataSource ds = (DataSource) ctx.lookup(JNDI_DATASOURCE);
		Connection conn = null;
		String password = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(PASSWORD_QUERY);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			// check if there is any result
			if (rs.next() == false)
				throw new FailedLoginException("invalid username");

			password = rs.getString(1);
			rs.close();
		} finally {
			if (conn != null && !conn.isClosed())
				conn.close();
		}
		return password;
	}
}
