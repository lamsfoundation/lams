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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.tool.chat.model;

/**
 * Represents a jabber message sent by a user toUser a groupchat session
 * 
 * @author Anthony Sukkar
 * 
 * @hibernate.class table="tl_lachat11_message"
 */
public class ChatMessage implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3976906267301586708L;

	// Fields
	private Long uid;

	private ChatUser fromUser;

	private ChatUser toUser;

	private String type;

	private String body;

	/** default constructor */
	public ChatMessage() {
	}

	/** full constructor */
	public ChatMessage(Long messageId, ChatUser from, ChatUser to, String type,
			String body) {
		this.fromUser = from;
		this.toUser = to;
		this.type = type;
		this.body = body;
	}

	// Property accessors

	/**
	 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
	 */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="from_user_id"
	 * 
	 */
	public ChatUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(ChatUser from) {
		this.fromUser = from;
	}

	/**
	 * @hibernate.many-to-one not-null="false"
	 * @hibernate.column name="to_user_id"
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
	 * @hibernate.property column="type" length="255"
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @hibernate.property column="body" length="255"
	 */
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
