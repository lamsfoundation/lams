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

/* $Id$ */

package org.lamsfoundation.lams.presence.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.presence.dao.IPresenceChatMessageDAO;
import org.lamsfoundation.lams.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.util.MessageService;

public class PresenceChatLoggerService implements IPresenceChatLoggerService {

	private static Logger log = Logger.getLogger(PresenceChatLoggerService.class);

	private IPresenceChatMessageDAO presenceChatMessageDAO;
	
	protected MessageService messageService;
	
	public PresenceChatMessage createPresenceChatMessage(String roomName, String from, String to,
			Date dateSent, String message) {
		PresenceChatMessage presenceChatMessage = new PresenceChatMessage(null, roomName, from, to,
				dateSent, message);
		
		saveOrUpdatePresenceChatMessage(presenceChatMessage);
		return presenceChatMessage;
	}

	public PresenceChatMessage getMessageById(Long id){
		return presenceChatMessageDAO.getMessageById(id);
	}
	
	public List<PresenceChatMessage> getMessagesByRoomName(String roomName){
		return presenceChatMessageDAO.getMessagesByRoomName(roomName);
	}
	
	public List<PresenceChatMessage> getMessagesByConversation(String from, String to, String roomName){
		return presenceChatMessageDAO.getMessagesByConversation(from, to, roomName);
	}
			
	public void saveOrUpdatePresenceChatMessage(PresenceChatMessage presenceChatMessage){
		presenceChatMessageDAO.saveOrUpdate(presenceChatMessage);
	}

	public IPresenceChatMessageDAO getPresenceChatMessageDAO(){
		return this.presenceChatMessageDAO;
	}
	
	public void setPresenceChatMessageDAO(IPresenceChatMessageDAO presenceChatMessageDAO){
		this.presenceChatMessageDAO = presenceChatMessageDAO;
	}
	
	public MessageService getMessageService(){
		return this.messageService;
	}
	
	public void setMessageService(MessageService messageService){
		this.messageService = messageService;
	}
}
