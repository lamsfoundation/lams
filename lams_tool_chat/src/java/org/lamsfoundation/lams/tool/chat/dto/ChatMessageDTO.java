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



package org.lamsfoundation.lams.tool.chat.dto;

import java.util.Date;

import org.lamsfoundation.lams.tool.chat.model.ChatMessage;

public class ChatMessageDTO implements Comparable {

    public Long uid;

    public String from;
    public Long fromUserId;

    public String body;

    public String type;

    public Date sendDate;

    public Boolean hidden;

    public ChatMessageDTO(ChatMessage chatMessage) {
	this.from = chatMessage.getFromUser().getNickname();
	this.fromUserId = chatMessage.getFromUser().getUserId();
	this.body = chatMessage.getBody();
	this.type = chatMessage.getType();
	this.sendDate = chatMessage.getSendDate();
	this.uid = chatMessage.getUid();
	this.hidden = chatMessage.isHidden();
    }

    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public String getFrom() {
	return from;
    }

    public void setFrom(String from) {
	this.from = from;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public Date getSendDate() {
	return sendDate;
    }

    public void setSendDate(Date sendDate) {
	this.sendDate = sendDate;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    @Override
    public int compareTo(Object o) {
	ChatMessageDTO toMessage = (ChatMessageDTO) o;
	int returnValue = this.sendDate.compareTo(toMessage.sendDate);

	if (returnValue == 0) {
	    returnValue = this.uid.compareTo(toMessage.getUid());
	}
	return returnValue;
    }

    public Boolean getHidden() {
	return hidden;
    }

    public void setHidden(Boolean hidden) {
	this.hidden = hidden;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }
}
