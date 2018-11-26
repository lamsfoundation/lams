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

package org.lamsfoundation.lams.tool.forum.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Steve.Ni
 *
 */
@Entity
@Table(name = "tl_lafrum11_message_seq")
public class MessageSeq {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_message_uid")
    private Message rootMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_uid")
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_message_uid")
    private Message threadMessage;

    @Column(name = "message_level")
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
