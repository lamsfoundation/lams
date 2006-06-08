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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.chat.service.ChatService;

/**
 * @hibernate.class table="tl_lachat11_chat"
 */

public class Chat implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 579733009969321015L;

	static Logger log = Logger.getLogger(ChatService.class.getName());

	// Fields
	/**
	 * 
	 */
	private Long uid;

	private Date createDate;

	private Date updateDate;

	private Long createBy;

	private String title;

	private String instructions;

	private Boolean runOffline;

	private Boolean lockOnFinished;

	private Boolean filteringEnabled;

	private String filterKeywords;

	private String onlineInstructions;

	private String offlineInstructions;

	private Boolean contentInUse;

	private Boolean defineLater;

	private Long toolContentId;

	private Set chatAttachments;

	private Set chatSessions;

	//*********** NON Persisit fields
	private IToolContentHandler toolContentHandler;

	// Constructors

	/** default constructor */
	public Chat() {
	}

	/** full constructor */
	public Chat(Date createDate, Date updateDate, Long createBy, String title,
			String instructions, Boolean runOffline, Boolean lockOnFinished,
			Boolean filteringEnabled, String filterKeywords,
			String onlineInstructions, String offlineInstructions,
			Boolean contentInUse, Boolean defineLater, Long toolContentId,
			Set chatAttachments, Set chatSessions) {
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.createBy = createBy;
		this.title = title;
		this.instructions = instructions;
		this.runOffline = runOffline;
		this.lockOnFinished = lockOnFinished;
		this.filteringEnabled = filteringEnabled;
		this.filterKeywords = filterKeywords;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.contentInUse = contentInUse;
		this.defineLater = defineLater;
		this.toolContentId = toolContentId;
		this.chatAttachments = chatAttachments;
		this.chatSessions = chatSessions;
	}

	// Property accessors
	/**
	 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
	 * 
	 */

	public Long getUid() {
		return this.uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @hibernate.property column="create_date"
	 * 
	 */

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @hibernate.property column="update_date"
	 * 
	 */

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @hibernate.property column="create_by" length="20"
	 * 
	 */

	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * @hibernate.property column="title" length="255"
	 * 
	 */

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property column="instructions" length="65535"
	 * 
	 */

	public String getInstructions() {
		return this.instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @hibernate.property column="run_offline" length="1"
	 * 
	 */

	public Boolean getRunOffline() {
		return this.runOffline;
	}

	public void setRunOffline(Boolean runOffline) {
		this.runOffline = runOffline;
	}

	/**
	 * @hibernate.property column="lock_on_finished" length="1"
	 * 
	 */

	public Boolean getLockOnFinished() {
		return this.lockOnFinished;
	}

	public void setLockOnFinished(Boolean lockOnFinished) {
		this.lockOnFinished = lockOnFinished;
	}

	/**
	 * @hibernate.property column="online_instructions" length="65535"
	 * 
	 */

	public String getOnlineInstructions() {
		return this.onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	/**
	 * @hibernate.property column="offline_instructions" length="65535"
	 * 
	 */

	public String getOfflineInstructions() {
		return this.offlineInstructions;
	}

	public void setOfflineInstructions(String offlineInstructions) {
		this.offlineInstructions = offlineInstructions;
	}

	/**
	 * @hibernate.property column="content_in_use" length="1"
	 * 
	 */

	public Boolean getContentInUse() {
		return this.contentInUse;
	}

	public void setContentInUse(Boolean contentInUse) {
		this.contentInUse = contentInUse;
	}

	/**
	 * @hibernate.property column="define_later" length="1"
	 * 
	 */

	public Boolean getDefineLater() {
		return this.defineLater;
	}

	public void setDefineLater(Boolean defineLater) {
		this.defineLater = defineLater;
	}

	/**
	 * @hibernate.property column="tool_content_id" length="20"
	 * 
	 */

	public Long getToolContentId() {
		return this.toolContentId;
	}

	public void setToolContentId(Long toolContentId) {
		this.toolContentId = toolContentId;
	}

	/**
	 * @hibernate.set lazy="false" inverse="false" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="chat_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.chat.model.ChatAttachment"
	 * 
	 */

	public Set getChatAttachments() {
		return this.chatAttachments;
	}

	public void setChatAttachments(Set chatAttachments) {
		this.chatAttachments = chatAttachments;
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="chat_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.chat.model.ChatSession"
	 * 
	 */

	public Set getChatSessions() {
		return this.chatSessions;
	}

	public void setChatSessions(Set chatSessions) {
		this.chatSessions = chatSessions;
	}

	/**
	 * @hibernate.property column="filtering_enabled" length="1"
	 */
	public Boolean getFilteringEnabled() {
		return filteringEnabled;
	}

	public void setFilteringEnabled(Boolean filteringEnabled) {
		this.filteringEnabled = filteringEnabled;
	}

	/**
	 * @hibernate.property column="filter_keywords" length="65535"
	 */
	public String getFilterKeywords() {
		return filterKeywords;
	}

	public void setFilterKeywords(String filterKeywords) {
		this.filterKeywords = filterKeywords;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@").append(
				Integer.toHexString(hashCode())).append(" [");
		buffer.append("title").append("='").append(getTitle()).append("' ");
		buffer.append("instructions").append("='").append(getInstructions())
				.append("' ");
		buffer.append("toolContentId").append("='").append(getToolContentId())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Chat))
			return false;
		Chat castOther = (Chat) other;

		return ((this.getUid() == castOther.getUid()) || (this.getUid() != null
				&& castOther.getUid() != null && this.getUid().equals(
				castOther.getUid())));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result
				+ (getUid() == null ? 0 : this.getUid().hashCode());
		return result;
	}

	public static Chat newInstance(Chat fromContent, Long toContentId,
			IToolContentHandler chatToolContentHandler) {
		Chat toContent = new Chat();
		fromContent.toolContentHandler = chatToolContentHandler;
		toContent = (Chat) fromContent.clone();
		toContent.setToolContentId(toContentId);
		toContent.setCreateDate(new Date());
		return toContent;
	}

	protected Object clone() {

		Chat chat = null;
		try {
			chat = (Chat) super.clone();
			chat.setUid(null);

			if (chatAttachments != null) {
				// create a copy of the attachments
				Iterator iter = chatAttachments.iterator();
				Set set = new HashSet();
				while (iter.hasNext()) {
					ChatAttachment originalFile = (ChatAttachment) iter.next();
					ChatAttachment newFile = (ChatAttachment) originalFile
							.clone();
					if (toolContentHandler != null) {
						// duplicate file node in repository
						NodeKey keys = toolContentHandler.copyFile(originalFile
								.getFileUuid());
						newFile.setFileUuid(keys.getUuid());
						newFile.setFileVersionId(keys.getVersion());
					}
					set.add(newFile);
				}
				chat.chatAttachments = set;
			}
			// create an empty set for the chatSession
			chat.chatSessions = new HashSet();

		} catch (CloneNotSupportedException cnse) {
			log.error("Error cloning " + Chat.class);
		} catch (ItemNotFoundException infe) {
			log.error("Item Not found " + Chat.class);
		} catch (RepositoryCheckedException rce) {
			log.error("Repository checked exception " + Chat.class);
		}
		return chat;
	}

	public IToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}
}
