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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 * 
 * 
 * @hibernate.class table="tl_laddim10_dimdim"
 */

public class Dimdim implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 579733009969321015L;

	private static final Logger logger = Logger.getLogger(Dimdim.class);

	private IToolContentHandler toolContentHandler;

	// Persistent Fields

	private Long uid;

	private Date createDate;

	private Date updateDate;

	private Long createBy;

	private String title;

	private String instructions;

	private boolean runOffline;

	private boolean lockOnFinished;

	private boolean allowRichEditor;

	private String onlineInstructions;

	private String offlineInstructions;

	private boolean contentInUse;

	private boolean defineLater;

	private Long toolContentId;

	private Set<DimdimAttachment> dimdimAttachments;

	private Set<DimdimSession> dimdimSessions;

	private String topic;

	private Integer maxAttendeeMikes;

	// Constructors

	/** default constructor */
	public Dimdim() {
	}

	/** full constructor */
	// TODO check full constructor to see whether we need add new fields
	public Dimdim(Date createDate, Date updateDate, Long createBy,
			String title, String instructions, boolean runOffline,
			boolean lockOnFinished, boolean filteringEnabled,
			String filterKeywords, String onlineInstructions,
			String offlineInstructions, boolean contentInUse,
			boolean defineLater, Long toolContentId,
			Set<DimdimAttachment> dimdimAttachments,
			Set<DimdimSession> dimdimSessions) {
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.createBy = createBy;
		this.title = title;
		this.instructions = instructions;
		this.runOffline = runOffline;
		this.lockOnFinished = lockOnFinished;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.contentInUse = contentInUse;
		this.defineLater = defineLater;
		this.toolContentId = toolContentId;
		this.dimdimAttachments = dimdimAttachments;
		this.dimdimSessions = dimdimSessions;
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

	public boolean isRunOffline() {
		return this.runOffline;
	}

	public void setRunOffline(boolean runOffline) {
		this.runOffline = runOffline;
	}

	/**
	 * @hibernate.property column="lock_on_finished" length="1"
	 * 
	 */

	public boolean isLockOnFinished() {
		return this.lockOnFinished;
	}

	public void setLockOnFinished(boolean lockOnFinished) {
		this.lockOnFinished = lockOnFinished;
	}

	/**
	 * @hibernate.property column="allow_rich_editor" length="1"
	 * @return
	 */
	public boolean isAllowRichEditor() {
		return allowRichEditor;
	}

	public void setAllowRichEditor(boolean allowRichEditor) {
		this.allowRichEditor = allowRichEditor;
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

	public boolean isContentInUse() {
		return this.contentInUse;
	}

	public void setContentInUse(boolean contentInUse) {
		this.contentInUse = contentInUse;
	}

	/**
	 * @hibernate.property column="define_later" length="1"
	 * 
	 */

	public boolean isDefineLater() {
		return this.defineLater;
	}

	public void setDefineLater(boolean defineLater) {
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
	 * @hibernate.set lazy="true" inverse="false" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="dimdim_uid"
	 * @hibernate.collection-one-to-many 
	 *                                   class="org.lamsfoundation.lams.tool.dimdim.model.DimdimAttachment"
	 * 
	 */

	public Set<DimdimAttachment> getDimdimAttachments() {
		return this.dimdimAttachments;
	}

	public void setDimdimAttachments(Set<DimdimAttachment> dimdimAttachments) {
		this.dimdimAttachments = dimdimAttachments;
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="dimdim_uid"
	 * @hibernate.collection-one-to-many 
	 *                                   class="org.lamsfoundation.lams.tool.dimdim.model.DimdimSession"
	 * 
	 */
	public Set<DimdimSession> getDimdimSessions() {
		return this.dimdimSessions;
	}

	public void setDimdimSessions(Set<DimdimSession> dimdimSessions) {
		this.dimdimSessions = dimdimSessions;
	}

	/**
	 * @hibernate.property column="topic"
	 * @return
	 */
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @hibernate.property column="max_attendee_mikes"
	 * @return
	 */
	public Integer getMaxAttendeeMikes() {
		return maxAttendeeMikes;
	}

	public void setMaxAttendeeMikes(Integer maxAttendeeMikes) {
		this.maxAttendeeMikes = maxAttendeeMikes;
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
		if (!(other instanceof Dimdim))
			return false;
		Dimdim castOther = (Dimdim) other;

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

	public static Dimdim newInstance(Dimdim fromContent, Long toContentId,
			IToolContentHandler dimdimToolContentHandler) {
		Dimdim toContent = new Dimdim();
		fromContent.toolContentHandler = dimdimToolContentHandler;
		toContent = (Dimdim) fromContent.clone();
		toContent.setToolContentId(toContentId);
		toContent.setCreateDate(new Date());
		return toContent;
	}

	protected Object clone() {

		Dimdim dimdim = null;
		try {
			dimdim = (Dimdim) super.clone();
			dimdim.setUid(null);

			if (dimdimAttachments != null) {
				// create a copy of the attachments
				Set<DimdimAttachment> set = new HashSet<DimdimAttachment>();
				for (DimdimAttachment att : dimdimAttachments) {
					set.add((DimdimAttachment) att.clone());
				}
				dimdim.dimdimAttachments = set;
			}
			// create an empty set for the dimdimSession
			dimdim.dimdimSessions = new HashSet<DimdimSession>();

		} catch (CloneNotSupportedException cnse) {
			logger.error("Error cloning " + Dimdim.class);
		}
		return dimdim;
	}

	public IToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}
}
