package org.lamsfoundation.lams.admin.security;

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
