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



package org.lamsfoundation.lams.tool.forum.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.forum.model.ForumReport;
import org.lamsfoundation.lams.tool.forum.model.Message;

public class MessageDTO {

    private Message message;
    private String author;
    private Long authorUserId;
    private boolean hasAttachment;
    private short level;
    private int threadNum;
    private boolean isAuthor;
    private Float mark;
    private String comment;
    private boolean released;

    //rating field used by lams:Rating tag
    private ItemRatingDTO itemRatingDto;

    // number of posts the learner has made in this topic.
    // used when this message is a root topic.
    private int numOfPosts;

    private int newPostingsNum;

    /**
     * Get a <code>MessageDTO</code> instance from a given <code>Message</code>.
     *
     * @param msg
     * @return
     */
    public static MessageDTO getMessageDTO(Message msg) {
	if (msg == null) {
	    return null;
	}

	MessageDTO dto = new MessageDTO();
	dto.setMessage(msg);
	if (msg.getCreatedBy() != null) {
	    dto.setAuthor(msg.getCreatedBy().getFirstName() + " " + msg.getCreatedBy().getLastName());
	    dto.setAuthorUserId(msg.getCreatedBy().getUserId());
	}
	if ((msg.getAttachments() == null) || msg.getAttachments().isEmpty()) {
	    dto.setHasAttachment(false);
	} else {
	    dto.setHasAttachment(true);
	}

	ForumReport report = msg.getReport();
	if ((report != null) && (report.getMark() != null)) {
	    dto.mark = report.getMark();
	    dto.comment = report.getComment();
	    dto.released = msg.getToolSession().isMarkReleased();
	}

	return dto;
    }

    /**
     * Get a list of <code>MessageDTO</code> according to given list of <code>Message</code>.
     *
     * @param msgList
     * @return
     */
    public static List<MessageDTO> getMessageDTO(List<Message> msgList) {
	List<MessageDTO> retSet = new ArrayList<MessageDTO>();
	if ((msgList == null) || msgList.isEmpty()) {
	    return retSet;
	}

	Iterator<Message> iter = msgList.iterator();
	while (iter.hasNext()) {
	    Message msg = iter.next();
	    MessageDTO msgDto = new MessageDTO();
	    msgDto.setMessage(msg);
	    if (msg.getCreatedBy() != null) {
		msgDto.setAuthor(msg.getCreatedBy().getFirstName() + " " + msg.getCreatedBy().getLastName());
		msgDto.setAuthorUserId(msg.getCreatedBy().getUserId());
	    }

	    if ((msg.getAttachments() == null) || msg.getAttachments().isEmpty()) {
		msgDto.setHasAttachment(false);
	    } else {
		msgDto.setHasAttachment(true);
	    }
	    ForumReport report = msg.getReport();
	    if ((report != null) && (report.getMark() != null)) {
		msgDto.mark = report.getMark();
		msgDto.comment = report.getComment();
		msgDto.released = msg.getToolSession().isMarkReleased();
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

    public int getNumOfPosts() {
	return numOfPosts;
    }

    public void setNumOfPosts(int numOfPosts) {
	this.numOfPosts = numOfPosts;
    }

    public void setNewPostingsNum(int newPostingsNum) {
	this.newPostingsNum = newPostingsNum;
    }

    public int getNewPostingsNum() {
	return newPostingsNum;
    }

    public Long getAuthorUserId() {
	return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
	this.authorUserId = authorUserId;
    }

    public ItemRatingDTO getItemRatingDto() {
	return itemRatingDto;
    }

    public void setItemRatingDto(ItemRatingDTO itemRatingDto) {
	this.itemRatingDto = itemRatingDto;
    }
}
