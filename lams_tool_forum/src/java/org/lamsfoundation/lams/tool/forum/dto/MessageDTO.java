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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.forum.persistence.Message;


public class MessageDTO {

	private Message message;
	private String author;
	private boolean hasAttachment;
	private short level;
	private String bodyByHtml;
	private int threadNum;
	

	public static MessageDTO getMessageDTO(Message msg, String authorName){
		if(msg == null)
			return null;
		
		MessageDTO dto = new MessageDTO();
		dto.setMessage(msg);
		if(!StringUtils.isEmpty(msg.getBody()))
			dto.setBodyByHtml(msg.getBody().replaceAll("\n","<BR>"));
		else
			dto.setBodyByHtml("");		
		dto.setAuthor(authorName);
		if(msg.getAttachments() == null || msg.getAttachments().isEmpty())
			dto.setHasAttachment(false);
		else
			dto.setHasAttachment(true);
		return dto;
	}
	
	public static MessageDTO getMessageDTO(Message msg){
		return getMessageDTO(msg,msg.getCreatedBy().getFirstName()+" "+msg.getCreatedBy().getLastName());
	}
	
	public static List getMessageDTO(List msgSet,String authorName){
		List retSet = new ArrayList();
		if(msgSet == null || msgSet.isEmpty())
			return retSet;
		
		Iterator iter = msgSet.iterator();
		while(iter.hasNext()){
			Message msg = (Message) iter.next();
			MessageDTO msgDto = new MessageDTO();
			if(!StringUtils.isEmpty(msg.getBody()))
				msgDto.setBodyByHtml(msg.getBody().replaceAll("\n","<BR>"));
			else
				msgDto.setBodyByHtml("");
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
	public static List getMessageDTO(List msgList){
		List retSet = new ArrayList();
		if(msgList == null || msgList.isEmpty())
			return retSet;
		
		Iterator iter = msgList.iterator();
		while(iter.hasNext()){
			Message msg = (Message) iter.next();
			MessageDTO msgDto = new MessageDTO();
			if(!StringUtils.isEmpty(msg.getBody()))
				msgDto.setBodyByHtml(msg.getBody().replaceAll("\n","<BR>"));
			else
				msgDto.setBodyByHtml("");
			if(msg.getAttachments() == null || msg.getAttachments().isEmpty())
				msgDto.setHasAttachment(false);
			else
				msgDto.setHasAttachment(true);
			msgDto.setMessage(msg);
			msgDto.setAuthor(msg.getCreatedBy().getFirstName()+" "+msg.getCreatedBy().getLastName());
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
	public short getLevel() {
		return level;
	}
	public void setLevel(short level) {
		this.level = level;
	}
	public int getThreadNum() {
		return threadNum;
	}
	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public String getBodyByHtml() {
		return bodyByHtml;
	}

	public void setBodyByHtml(String bodyByHtml) {
		this.bodyByHtml = bodyByHtml;
	}	
}
