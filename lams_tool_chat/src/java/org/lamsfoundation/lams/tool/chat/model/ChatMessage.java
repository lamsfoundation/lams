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
 * along with this program; if not, write toUser the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */



package org.lamsfoundation.lams.tool.chat.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents a message sent by a user toUser a groupchat session
 *
 * @author Anthony Sukkar
 *
 *
 */
public class ChatMessage implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3976906267301586708L;

    public static final String MESSAGE_TYPE_PUBLIC = "groupchat";

    public static final String MESSAGE_TYPE_PRIVATE = "chat";

    // Fields
    private Long uid;

    private ChatSession chatSession;

    private ChatUser fromUser;

    private ChatUser toUser;

    private String type;

    private String body;

    private Date sendDate;

    private boolean hidden;

    /** default constructor */
    public ChatMessage() {
    }

    // Property accessors

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     */
    public ChatSession getChatSession() {
	return chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
	this.chatSession = chatSession;
    }

    /**
     *
     *
     * 
     */
    public ChatUser getFromUser() {
	return fromUser;
    }

    public void setFromUser(ChatUser from) {
	this.fromUser = from;
    }

    /**
     *
     *
     * 
     * The toUser field is null when the type is "groupchat", and non null when
     * the type is "chat"
     */
    public ChatUser getToUser() {
	return toUser;
    }

    public void setToUser(ChatUser to) {
	this.toUser = to;
    }

    /**
     *
     */
    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    /**
     *
     */
    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    /**
     *
     */
    public Date getSendDate() {
	return sendDate;
    }

    public void setSendDate(Date sendDate) {
	this.sendDate = sendDate;
    }

    /**
     * 
     *
     */
    public boolean isHidden() {
	return hidden;
    }

    public void setHidden(boolean hidden) {
	this.hidden = hidden;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", uid).append("body", body).toString();
    }
}
