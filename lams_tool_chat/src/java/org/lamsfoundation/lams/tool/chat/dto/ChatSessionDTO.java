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

/* $Id$ */

package org.lamsfoundation.lams.tool.chat.dto;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;

public class ChatSessionDTO implements Comparable {
	
	public ChatSessionDTO(ChatSession session) {
		this.sessionID = session.getSessionId();
		this.sessionName = session.getSessionName();
		
		chatMessages = new TreeSet<ChatMessageDTO>();
		for (Iterator i = session.getChatMessages().iterator(); i.hasNext();) {
			chatMessages.add(new ChatMessageDTO((ChatMessage) i.next()));
		}
	}
	
	public ChatSessionDTO (ChatSession session, List messages) {
		this.sessionID = session.getSessionId();
		this.sessionName = session.getSessionName();
		
		chatMessages = new TreeSet<ChatMessageDTO>();
		for (Iterator i = messages.iterator(); i.hasNext();) {
			chatMessages.add(new ChatMessageDTO((ChatMessage) i.next()));
		}		
	}
	
	public ChatSessionDTO() {}
	
	Long sessionID;

	String sessionName;
	
	Set<ChatMessageDTO> chatMessages;
	
	public Long getSessionID() {
		return sessionID;
	}

	public void setSessionID(Long sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Set<ChatMessageDTO> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(Set<ChatMessageDTO> chatMessages) {
		this.chatMessages = chatMessages;
	}

	public int compareTo(Object o) {
		int returnValue;
		ChatSessionDTO toSession = (ChatSessionDTO)o;
		returnValue = this.sessionName.compareTo(toSession.sessionName);
		if (returnValue == 0) {
			returnValue = this.sessionID.compareTo(toSession.sessionID);			
		}
		return returnValue;		
	}
}
