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
/* $$Id$$ */
package org.lamsfoundation.lams.security;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.usermanagement.*;

/**
 * @author kevin
 *
 */
public class WebAuthServlet extends HttpServlet {

	private static final String WEBAUTH_TOKEN = "WEBAUTH_USER";
		
	public void init() throws ServletException 
	{
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException 
	{
		HttpSession oldSession = request.getSession(false);
		if ( oldSession != null )
		{
			oldSession.invalidate();
		}
			
		String webAuthUserID = (String)request.getAttribute(WEBAUTH_TOKEN);
		HttpSession session = request.getSession(true);
		if ( webAuthUserID == null )
		//there is no valid WebAuth authenticated user
		{
			session.removeAttribute(WEBAUTH_TOKEN);		
		}
		else
		{
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
			UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
			User webAuthUser = service.getUserByLogin(webAuthUserID);
				
			if (webAuthUser != null) 
			//valid webauth user also is registered in Lams as well		
			{
				session.setAttribute(WEBAUTH_TOKEN, webAuthUserID);		
			}
			else
			//though webauth authenticated, but not registered.
			{
				session.removeAttribute(WEBAUTH_TOKEN);					
			}
		}
		
		//In anycase, goto Lams welcome page	 
		response.sendRedirect("index.jsp");
		
	}

}
