/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.dto;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lamsfoundation.lams.tool.forum.persistence.Message;

public class MessageDTO {

	private Message message;
	private String author;
	private boolean hasAttachment;
	public static MessageDTO getMessageDTO(Message msg, String authorName){
		if(msg == null)
			return null;
		
		MessageDTO dto = new MessageDTO();
		dto.setMessage(msg);
		dto.setAuthor(authorName);
		if(msg.getAttachments() == null || msg.getAttachments().isEmpty())
			dto.setHasAttachment(false);
		else
			dto.setHasAttachment(true);
		return dto;
	}
	public static Set getMessageDTO(Set msgSet,String authorName){
		Set retSet = new HashSet();
		if(msgSet == null || msgSet.isEmpty())
			return retSet;
		
		Iterator iter = msgSet.iterator();
		while(iter.hasNext()){
			Message msg = (Message) iter.next();
			MessageDTO msgDto = new MessageDTO();
			if(msg.getAttachments() == null || msg.getAttachments().isEmpty())
				msgDto.setHasAttachment(false);
			else
				msgDto.setHasAttachment(true);
			msgDto.setMessage(msg);
			msgDto.setAuthor(authorName);
			retSet.add(msgDto);
		}
		return retSet;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public boolean getHasAttachment() {
		return hasAttachment;
	}
	public void setHasAttachment(boolean isAttachment) {
		this.hasAttachment = isAttachment;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
}
