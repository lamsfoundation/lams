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



package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * @author Steve.Ni
 *
 *
 *
 *
 */
public class MessageSeq {

    private Long uid;
    private Message rootMessage;
    private Message message;
    private Message threadMessage;
    private short messageLevel;

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
     *
     */
    public Message getRootMessage() {
	return rootMessage;
    }

    public void setRootMessage(Message rootTopicUid) {
	this.rootMessage = rootTopicUid;
    }

    /**
     *
     *
     */
    public Message getMessage() {
	return message;
    }

    public void setMessage(Message topicUid) {
	this.message = topicUid;
    }

    /**
     *
     *
     */
    public Message getThreadMessage() {
	return threadMessage;
    }

    public void setThreadMessage(Message threadTopMessageUid) {
	this.threadMessage = threadTopMessageUid;
    }

    /**
     *
     */
    public short getMessageLevel() {
	return messageLevel;
    }

    public void setMessageLevel(short topicLevel) {
	this.messageLevel = topicLevel;
    }
}
