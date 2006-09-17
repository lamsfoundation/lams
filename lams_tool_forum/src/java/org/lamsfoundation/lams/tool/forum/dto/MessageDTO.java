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

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.forum.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.Message;


public class MessageDTO {

	private Message message;
	private String author;
	private boolean hasAttachment;
	private short level;
	private int threadNum;
	private boolean isAuthor;
	private Float mark;
	private String comment;
	private boolean released;
	
	//2 fields use for export portfolio function
	private String attachmentName;
	private String attachmentLocalUrl;

	/**
	 * Get a <code>MessageDTO</code> instance from a given <code>Message</code>.
	 * 
	 * @param msg
	 * @return
	 */
	public static MessageDTO getMessageDTO(Message msg){
		if(msg == null)
			return null;
		
		MessageDTO dto = new MessageDTO();
		dto.setMessage(msg);
		dto.setAuthor(msg.getCreatedBy().getFirstName()+" "+msg.getCreatedBy().getLastName());
		if(msg.getAttachments() == null || msg.getAttachments().isEmpty())
			dto.setHasAttachment(false);
		else
			dto.setHasAttachment(true);
		
		ForumReport report = msg.getReport();
		if(report != null && report.getMark() != null){
			dto.mark = report.getMark();
			dto.comment = report.getComment();
			dto.released = report.getDateMarksReleased() == null?false:true;
		}
		
		return dto;
	}

	/**
	 * Get a list of <code>MessageDTO</code> according to given list of <code>Message</code>.
	 * @param msgList
	 * @return
	 */
	public static List<MessageDTO> getMessageDTO(List msgList){
		List<MessageDTO> retSet = new ArrayList<MessageDTO>();
		if(msgList == null || msgList.isEmpty())
			return retSet;
		
		Iterator iter = msgList.iterator();
		while(iter.hasNext()){
			Message msg = (Message) iter.next();
			MessageDTO msgDto = new MessageDTO();
			msgDto.setMessage(msg);
			msgDto.setAuthor(msg.getCreatedBy().getFirstName()+" "+msg.getCreatedBy().getLastName());
			
			if(msg.getAttachments() == null || msg.getAttachments().isEmpty())
				msgDto.setHasAttachment(false);
			else
				msgDto.setHasAttachment(true);
			ForumReport report = msg.getReport();
			if(report != null && report.getMark() != null){
				msgDto.mark = report.getMark();
				msgDto.comment = report.getComment();
				msgDto.released = report.getDateMarksReleased() == null?false:true;
			}

			retSet.add(msgDto);
		}
		return retSet;
	}
	//-------------------------------DTO get/set method------------------------------
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

	public boolean getIsAuthor() {
		return isAuthor;
	}

	public void setAuthor(boolean isAuthor) {
		this.isAuthor = isAuthor;
	}
	
	public Float getMark() {
		return mark;
	}

	public void setMark(Float mark) {
		this.mark = mark;
	}
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isReleased() {
		return released;
	}

	public void setReleased(boolean isReleased) {
		this.released = isReleased;
	}

	public String getAttachmentLocalUrl() {
		return attachmentLocalUrl;
	}

	public void setAttachmentLocalUrl(String attachmentLocalUrl) {
		this.attachmentLocalUrl = attachmentLocalUrl;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	
}
