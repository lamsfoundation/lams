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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * @author Steve.Ni
 *
 * @hibernate.class table="tl_lafrum11_message_seq"
 *
 */
public class MessageSeq {

	private Long uid;
	private Message rootMessage;
	private Message message;
	private short messageLevel;
	
    /**
     * @hibernate.id column="uid" generator-class="native"
     */
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * @hibernate.many-to-one column="root_message_uid"
	 * 	cascade="none"
	 */
	public Message getRootMessage() {
		return rootMessage;
	}
	public void setRootMessage(Message rootTopicUid) {
		this.rootMessage = rootTopicUid;
	}
	/**
	 * @hibernate.many-to-one column="message_uid"
	 * 	cascade="none"
	 */
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message topicUid) {
		this.message = topicUid;
	}
	/**
	 * @hibernate.property column="message_level"
	 */
	public short getMessageLevel() {
		return messageLevel;
	}
	public void setMessageLevel(short topicLevel) {
		this.messageLevel = topicLevel;
	}
}
