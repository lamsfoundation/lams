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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.notebook.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.notebook.service.NotebookService;

/**
 * @hibernate.class table="tl_lantbk11_notebook"
 */

public class Notebook implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 579733009969321015L;

	static Logger log = Logger.getLogger(NotebookService.class.getName());

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

	private boolean runOffline;

	private boolean lockOnFinished;
	
	private boolean allowRichEditor;

	private String onlineInstructions;

	private String offlineInstructions;

	private boolean contentInUse;

	private boolean defineLater;

	private Long toolContentId;

	private Set notebookAttachments;

	private Set notebookSessions;

	//*********** NON Persisit fields
	private IToolContentHandler toolContentHandler;

	// Constructors

	/** default constructor */
	public Notebook() {
	}

	/** full constructor */
	public Notebook(Date createDate, Date updateDate, Long createBy, String title,
			String instructions, boolean runOffline, boolean lockOnFinished,
			boolean filteringEnabled, String filterKeywords,
			String onlineInstructions, String offlineInstructions,
			boolean contentInUse, boolean defineLater, Long toolContentId,
			Set notebookAttachments, Set notebookSessions) {
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
		this.notebookAttachments = notebookAttachments;
		this.notebookSessions = notebookSessions;
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
	 * @hibernate.set lazy="false" inverse="false" cascade="all-delete-orphan"
	 * @hibernate.collection-key column="notebook_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.notebook.model.NotebookAttachment"
	 * 
	 */

	public Set getNotebookAttachments() {
		return this.notebookAttachments;
	}

	public void setNotebookAttachments(Set notebookAttachments) {
		this.notebookAttachments = notebookAttachments;
	}

	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="notebook_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.notebook.model.NotebookSession"
	 * 
	 */

	public Set getNotebookSessions() {
		return this.notebookSessions;
	}

	public void setNotebookSessions(Set notebookSessions) {
		this.notebookSessions = notebookSessions;
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
		if (!(other instanceof Notebook))
			return false;
		Notebook castOther = (Notebook) other;

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

	public static Notebook newInstance(Notebook fromContent, Long toContentId,
			IToolContentHandler notebookToolContentHandler) {
		Notebook toContent = new Notebook();
		fromContent.toolContentHandler = notebookToolContentHandler;
		toContent = (Notebook) fromContent.clone();
		toContent.setToolContentId(toContentId);
		toContent.setCreateDate(new Date());
		return toContent;
	}

	protected Object clone() {

		Notebook notebook = null;
		try {
			notebook = (Notebook) super.clone();
			notebook.setUid(null);

			if (notebookAttachments != null) {
				// create a copy of the attachments
				Iterator iter = notebookAttachments.iterator();
				Set<NotebookAttachment> set = new HashSet<NotebookAttachment>();
				while (iter.hasNext()) {
					NotebookAttachment originalFile = (NotebookAttachment) iter.next();
					NotebookAttachment newFile = (NotebookAttachment) originalFile
							.clone();
					set.add(newFile);
				}
				notebook.notebookAttachments = set;
			}
			// create an empty set for the notebookSession
			notebook.notebookSessions = new HashSet();

		} catch (CloneNotSupportedException cnse) {
			log.error("Error cloning " + Notebook.class);
		}
		return notebook;
	}

	public IToolContentHandler getToolContentHandler() {
		return toolContentHandler;
	}

	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}
}
