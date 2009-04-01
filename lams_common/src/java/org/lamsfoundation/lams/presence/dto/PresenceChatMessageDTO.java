/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.presence.dto;

import java.util.Date;

import org.lamsfoundation.lams.presence.model.PresenceChatMessage;

public class PresenceChatMessageDTO
{
	public Long uid;

	private String roomName;
	
	private String from;
	
	private String to;
	
	private Date dateSent;
	
	private String message;

	public PresenceChatMessageDTO(Long uid, String roomName, String from,
			String to, Date dateSent, String message) {
		super();
		this.uid = uid;
		this.roomName = roomName;
		this.from = from;
		this.to = to;
		this.dateSent = dateSent;
		this.message = message;
	}

	public PresenceChatMessageDTO(PresenceChatMessage presenceChatMessage){
		this.uid = presenceChatMessage.getUid();
		this.roomName = presenceChatMessage.getRoomName();
		this.from = presenceChatMessage.getFrom();
		this.to = presenceChatMessage.getTo();
		this.dateSent = presenceChatMessage.getDateSent();
		this.message = presenceChatMessage.getMessage();
	}
	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
