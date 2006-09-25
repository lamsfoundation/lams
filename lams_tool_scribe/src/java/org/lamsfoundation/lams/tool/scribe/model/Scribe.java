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
/* $$Idx$ */

package org.lamsfoundation.lams.tool.scribe.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.scribe.service.ScribeService;

/**
 * @hibernate.class table="tl_lascrb11_scribe"
 */

public class Scribe implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 579733009969321015L;

	static Logger log = Logger.getLogger(ScribeService.class.getName());

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

	private Boolean reflectOnActivity;
	
	private String reflectInstructions;

	private String onlineInstructions;

	private String offlineInstructions;

	private Boolean contentInUse;

	private Boolean defineLater;
	
	private Boolean autoSelectScribe;

	private Long toolContentId;

	private Set scribeAttachments;

	private Set scribeSessions;
	
	private Set scribeHeadings;
	
	
	//*********** NON Persisit fields
	private IToolContentHandler toolContentHandler;

	// Constructors

	/** default constructor */
	public Scribe() {
	}

	/** full constructor */
	public Scribe(Date createDate, Date updateDate, Long createBy, String title,
			String instructions, Boolean runOffline,
			String onlineInstructions, String offlineInstructions,
			Boolean contentInUse, Boolean defineLater, Long toolContentId,
			Set scribeAttachments, Set scribeSessions) {
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.createBy = createBy;
		this.title = title;
		this.instructions = instructions;
		this.runOffline = runOffline;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
		this.contentInUse = contentInUse;
		this.defineLater = defineLater;
		this.toolContentId = toolContentId;
		this.scribeAttachments = scribeAttachments;
		this.scribeSessions = scribeSessions;
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
	 * @hibernate.property column="auto_select_scribe" length=1
	 * 
	 */
	public Boolean getAutoSelectScribe() {
		return autoSelectScribe;
	}

	public void setAutoSelectScribe(Boolean autoSelectScribe) {
		this.autoSelectScribe = autoSelectScribe;
	}
	
	/**
	 * @hibernate.property column="reflect_on_activity" length="1"
	 */
	public Boolean getReflectOnActivity() {
		return reflectOnActivity;
	}

	public void setReflectOnActivity(Boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}
	
	/**
	 * @hibernate.property column="reflect_instructions" length="65535"
	 */
	public String getReflectInstructions() {
		return reflectInstructions;
	}

	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
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
	 * @hibernate.collection-key column="scribe_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.scribe.model.ScribeAttachment"
	 * 
	 */

	public Set getScribeAttachments() {
		return this.scribeAttachments;
	}

	public void setScribeAttachments(Set scribeAttachments) {
		this.scribeAttachments = scribeAttachments;
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="scribe_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.scribe.model.ScribeSession"
	 * 
	 */

	public Set getScribeSessions() {
		return this.scribeSessions;
	}

	public void setScribeSessions(Set scribeSessions) {
		this.scribeSessions = scribeSessions;
	}
	
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="scribe_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.scribe.model.ScribeHeading" 
	 */
	public Set getScribeHeadings() {
		return scribeHeadings;
	}

	public void setScribeHeadings(Set scribeHeadings) {
		this.scribeHeadings = scribeHeadings;
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
		if (!(other instanceof Scribe))
			return false;
		Scribe castOther = (Scribe) other;

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

	public static Scribe newInstance(Scribe fromContent, Long toContentId,
			IToolContentHandler scribeToolContentHandler) {
		Scribe toContent = new Scribe();
		fromContent.toolContentHandler = scribeToolContentHandler;
		toContent = (Scribe) fromContent.clone();
		toContent.setToolContentId(toContentId);
		toContent.setCreateDate(new Date());
		return toContent;
	}

	protected Object clone() {

		Scribe scribe = null;
		try {
			scribe = (Scribe) super.clone();
			scribe.setUid(null);

			if (scribeAttachments != null) {
				// create a copy of the attachments
				Iterator iter = scribeAttachments.iterator();
				Set<ScribeAttachment> set = new HashSet<ScribeAttachment>();
				while (iter.hasNext()) {
					ScribeAttachment originalFile = (ScribeAttachment) iter.next();
					ScribeAttachment newFile = (ScribeAttachment) originalFile
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
				scribe.scribeAttachments = set;
			}
			
			if (scribeHeadings != null) {
				// create copy of headings
				Iterator iter = scribeHeadings.iterator();
				Set<ScribeHeading> set = new HashSet<ScribeHeading>();
				while (iter.hasNext()) {
					ScribeHeading originalHeading = (ScribeHeading) iter.next();
					ScribeHeading newHeading = (ScribeHeading)originalHeading.clone();
					set.add(newHeading);
				}
				scribe.scribeHeadings = set;
				
			}
			
			
			// create an empty set for the scribeSession
			scribe.scribeSessions = new HashSet();
			


		} catch (CloneNotSupportedException cnse) {
			log.error("Error cloning " + Scribe.class);
		} catch (ItemNotFoundException infe) {
			log.error("Item Not found " + Scribe.class);
		} catch (RepositoryCheckedException rce) {
			log.error("Repository checked exception " + Scribe.class);
		}
		return scribe;
	}

	public IToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}
}