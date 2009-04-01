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
 
package org.lamsfoundation.lams.webservice; 

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.presence.service.IPresenceChatLoggerService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
 
/**
 * @author pgeorges
 *
 * @web:servlet name="PresenceChatLoggerServlet"
 * @web:servlet-mapping url-pattern="/PresenceChatLogger"
 */
public class PresenceChatLoggerServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(PresenceChatLoggerServlet.class);
	
	/*
	private static MessageContext context = MessageContext.getCurrentContext();
	
	private static IPresenceChatLoggerService presenceChatLoggerService =
		(IPresenceChatLoggerService) WebApplicationContextUtils.getRequiredWebApplicationContext(
			((HttpServlet) context.getProperty(HTTPConstants.MC_HTTP_SERVLET))
					.getServletContext()).getBean("presenceChatLoggerService");
	*/
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException {

		try {
			String roomName = WebUtil.readStrParam(request, "roomName");
			String from = WebUtil.readStrParam(request, "from");
			String to = WebUtil.readStrParam(request, "to");
			String dateSentString = WebUtil.readStrParam(request, "dateSent");
			String message = WebUtil.readStrParam(request, "message");
			
			Date dateSent = new Date(dateSentString);

			/*
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
			IPresenceChatLoggerService presenceChatLoggerService =  (IPresenceChatLoggerService)wac.getBean("presenceChatLoggerService");
			*/
			
		    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager
				    .getInstance().getServletContext());
		    IPresenceChatLoggerService presenceChatLoggerService = (IPresenceChatLoggerService) ctx.getBean("presenceChatLoggerService");
			    
			presenceChatLoggerService.createPresenceChatMessage(roomName, from, to, dateSent, message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException {
		doPost(request, response);
	}

}
 