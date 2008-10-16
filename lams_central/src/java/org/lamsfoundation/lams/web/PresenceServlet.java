/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserFlashDTO;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.lamsfoundation.lams.workspace.web.WorkspaceAction;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.XMPPUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;

/**
 * @author pgeorges
 *
 * @web:servlet name="XMPP"
 * @web:servlet-mapping url-pattern="/XMPP"
 */

public class PresenceServlet extends LamsDispatchAction{
	
	protected Logger log = Logger.getLogger(WorkspaceAction.class.getName());
	
	/** Send the flash message back to Flash */
	private ActionForward returnWDDXPacket(FlashMessage flashMessage, HttpServletResponse response) throws IOException {
	        PrintWriter writer = response.getWriter();
	        writer.println(flashMessage.serializeMessage());
	        return null;
	}

	/** Send the flash message back to Flash */
	private ActionForward returnWDDXPacket(String serializedFlashMessage, HttpServletResponse response) throws IOException {
	        PrintWriter writer = response.getWriter();
	        writer.println(serializedFlashMessage);
	        return null;
	}
	
	public ActionForward createXmppId(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)throws Exception{
		
		
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		String xmppIdCreated = XMPPUtil.createId(user);
		
		FlashMessage flashMessage = null; 
		try {
			flashMessage = new FlashMessage("createXmppId", xmppIdCreated);
		} catch (Exception e) {
		}
		
		String wddxPacket = flashMessage.serializeMessage();
		return returnWDDXPacket(wddxPacket, response);
	}
	
	public ActionForward createXmppRoom(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)throws Exception{
		
		String xmppRoomName = (String)WebUtil.readStrParam(request,"xmppRoomName");
		Boolean xmppRoomCreated = XMPPUtil.createMultiUserChat(xmppRoomName);
		
		FlashMessage flashMessage = null; 
		try {
			flashMessage = new FlashMessage("createXmppRoom", xmppRoomCreated);
		} catch (Exception e) {
		}
		
		String wddxPacket = flashMessage.serializeMessage();
		return returnWDDXPacket(wddxPacket, response);
	}
}
