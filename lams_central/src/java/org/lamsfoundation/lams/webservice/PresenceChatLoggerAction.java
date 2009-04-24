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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.presence.service.IPresenceChatLoggerService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thoughtworks.xstream.XStream;
 
/** 
* @author Paul Georges

* ----------------XDoclet Tags--------------------
* 
* @struts:action path="/PresenceChatLogger" 
*                parameter="method" 
*                validate="false"
* 
* ----------------XDoclet Tags--------------------
*/
public class PresenceChatLoggerAction extends LamsDispatchAction {
	
	private static Logger logger = Logger.getLogger(PresenceChatLoggerAction.class);
	
	public ActionForward getConversationHistory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			String roomName = WebUtil.readStrParam(request, "roomName");
			String from = WebUtil.readStrParam(request, "from");
			String to = WebUtil.readStrParam(request, "to");
			
		    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager
				    .getInstance().getServletContext());
		    IPresenceChatLoggerService presenceChatLoggerService = (IPresenceChatLoggerService) ctx.getBean("presenceChatLoggerService");
			    
		    List<PresenceChatMessage> messages = presenceChatLoggerService.getMessagesByConversation(from, to, roomName);
			
			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");
		    writeAJAXResponse(response, buildConversationXML(to, messages));
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
		
		return null;
	}

	public ActionForward getGroupHistory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		try {
			String roomName = WebUtil.readStrParam(request, "roomName");
			
		    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager
				    .getInstance().getServletContext());
		    IPresenceChatLoggerService presenceChatLoggerService = (IPresenceChatLoggerService) ctx.getBean("presenceChatLoggerService");
			    
		    List<PresenceChatMessage> messages = presenceChatLoggerService.getMessagesByRoomName(roomName);
			
			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");
		    writeAJAXResponse(response, buildGroupHistoryXML(messages));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
		
		return null;
	}
	
	public ActionForward saveMessage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			String roomName = WebUtil.readStrParam(request, "roomName");
			String from = WebUtil.readStrParam(request, "from");
			String to = WebUtil.readStrParam(request, "to");
			String message = WebUtil.readStrParam(request, "message");
			
			if(to.compareTo("null") == 0){
				to = null;
			}
			
		    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager
				    .getInstance().getServletContext());
		    IPresenceChatLoggerService presenceChatLoggerService = (IPresenceChatLoggerService) ctx.getBean("presenceChatLoggerService");
			    
			presenceChatLoggerService.createPresenceChatMessage(roomName, from, to, new Date(), message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	private String buildConversationXML(String nick, List<PresenceChatMessage> messages){
		String xmlOutput = "<conversation><nick>" + nick + "</nick>";
		
		XStream xstream = new XStream();
		xstream.alias("clause", PresenceChatMessage.class);
		
		for(PresenceChatMessage message: messages){
			xmlOutput += xstream.toXML(message);
		}
		
		xmlOutput += "</conversation>";
		
		return xmlOutput;
	}
	
	private String buildGroupHistoryXML(List<PresenceChatMessage> messages){
		String xmlOutput = "<groupHistory>";
		
		XStream xstream = new XStream();
		xstream.alias("clause", PresenceChatMessage.class);
		
		for(PresenceChatMessage message: messages){
				xmlOutput += xstream.toXML(message);
		}
		
		xmlOutput += "</groupHistory>";
		
		return xmlOutput;
	}
}
