/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.chat.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = "tl_lachat11_session")
public class ChatSession implements java.io.Serializable {

    private static Logger log = Logger.getLogger(ChatSession.class);

    private static final long serialVersionUID = 4407078136514639026L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "session_end_date")
    private Date sessionEndDate;

    @Column(name = "session_start_date")
    private Date sessionStartDate;

    @Column
    private Integer status;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "session_name")
    private String sessionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_uid")
    private Chat chat;

    @OneToMany(mappedBy = "chatSession")
    private Set<ChatUser> chatUsers = new HashSet<ChatUser>();

    @OneToMany(mappedBy = "chatSession")
    private Set<ChatMessage> chatMessages = new HashSet<ChatMessage>();

    public ChatSession() {
    }

    public ChatSession(Date sessionEndDate, Date sessionStartDate, Integer status, Long sessionId, String sessionName,
	    Chat chat, Set<ChatUser> chatUsers) {
	this.sessionEndDate = sessionEndDate;
	this.sessionStartDate = sessionStartDate;
	this.status = status;
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	this.chat = chat;
	this.chatUsers = chatUsers;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getSessionEndDate() {
	return this.sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    public Date getSessionStartDate() {
	return this.sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Long getSessionId() {
	return this.sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return this.sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Chat getChat() {
	return this.chat;
    }

    public void setChat(Chat chat) {
	this.chat = chat;
    }

    public Set<ChatUser> getChatUsers() {
	return this.chatUsers;
    }

    public void setChatUsers(Set<ChatUser> chatUsers) {
	this.chatUsers = chatUsers;
    }

    public Set<ChatMessage> getChatMessages() {
	return this.chatMessages;
    }

    public void setChatMessages(Set<ChatMessage> chatMessages) {
	this.chatMessages = chatMessages;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("sessionEndDate").append("='").append(getSessionEndDate()).append("' ");
	buffer.append("sessionStartDate").append("='").append(getSessionStartDate()).append("' ");
	buffer.append("status").append("='").append(getStatus()).append("' ");
	buffer.append("sessionID").append("='").append(getSessionId()).append("' ");
	buffer.append("sessionName").append("='").append(getSessionName()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof ChatSession)) {
	    return false;
	}
	ChatSession castOther = (ChatSession) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    @Override
    public Object clone() {

	ChatSession session = null;
	try {
	    session = (ChatSession) super.clone();
	    session.chatUsers = new HashSet<ChatUser>();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ChatSession.class + " failed");
	}
	return session;
    }
}