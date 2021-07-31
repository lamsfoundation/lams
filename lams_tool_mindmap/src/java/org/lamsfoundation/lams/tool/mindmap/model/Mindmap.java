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

package org.lamsfoundation.lams.tool.mindmap.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mindmap.service.MindmapService;
//import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;

/**
 *
 */
@Entity
@Table(name = "tl_lamind10_mindmap")
public class Mindmap implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 579733009969321015L;
    static Logger log = Logger.getLogger(MindmapService.class.getName());

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @Column(name = "create_by")
    private Long createBy;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    @Column(name = "multiuser_mode")
    private boolean multiUserMode;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @Column(name = "export_content")
    private String mindmapExportContent;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "gallery_walk_enabled")
    private boolean galleryWalkEnabled;

    @Column(name = "gallery_walk_read_only")
    private boolean galleryWalkReadOnly;

    @Column(name = "gallery_walk_started")
    private boolean galleryWalkStarted;

    @Column(name = "gallery_walk_finished")
    private boolean galleryWalkFinished;

    @Column(name = "gallery_walk_edit_enabled")
    private boolean galleryWalkEditEnabled;

    @Column(name = "gallery_walk_instructions")
    private String galleryWalkInstructions;

    @OneToMany(mappedBy = "mindmap")
    private Set<MindmapSession> mindmapSessions;

    // Constructors

    /** default constructor */
    public Mindmap() {
    }

    /** full constructor */
    public Mindmap(Date createDate, Date updateDate, Long createBy, String title, String instructions,
	    boolean lockOnFinished, boolean filteringEnabled, String filterKeywords, boolean contentInUse,
	    boolean defineLater, Long toolContentId, Set<MindmapSession> mindmapSessions) {
	this.createDate = createDate;
	this.updateDate = updateDate;
	this.createBy = createBy;
	this.title = title;
	this.instructions = instructions;
	this.lockOnFinished = lockOnFinished;
	this.contentInUse = contentInUse;
	this.defineLater = defineLater;
	this.toolContentId = toolContentId;
	this.mindmapSessions = mindmapSessions;
    }

    // Property accessors
    /**
     *
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
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     *
     */
    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * Returns deadline for learner's submission
     *
     * @return submissionDeadline
     *
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     * Sets deadline for learner's submission
     *
     * @param submissionDeadline
     */
    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     *
     *
     */
    public Long getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     *
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     *
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     *
     */
    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     *
     * @return
     */
    public boolean isMultiUserMode() {
	return multiUserMode;
    }

    public void setMultiUserMode(boolean multiUserMode) {
	this.multiUserMode = multiUserMode;
    }

    /**
     *
     *
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     *
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     */
    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     */
    public String getMindmapExportContent() {
	return mindmapExportContent;
    }

    public void setMindmapExportContent(String mindmapExportContent) {
	this.mindmapExportContent = mindmapExportContent;
    }

    /**
     *
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isGalleryWalkEnabled() {
	return galleryWalkEnabled;
    }

    public void setGalleryWalkEnabled(boolean galleryWalkEnabled) {
	this.galleryWalkEnabled = galleryWalkEnabled;
    }

    public boolean isGalleryWalkReadOnly() {
	return galleryWalkReadOnly;
    }

    public void setGalleryWalkReadOnly(boolean galleryWalkReadOnly) {
	this.galleryWalkReadOnly = galleryWalkReadOnly;
    }

    public boolean isGalleryWalkStarted() {
	return galleryWalkStarted;
    }

    public void setGalleryWalkStarted(boolean galleryWalkStarted) {
	this.galleryWalkStarted = galleryWalkStarted;
    }

    public boolean isGalleryWalkFinished() {
	return galleryWalkFinished;
    }

    public void setGalleryWalkFinished(boolean galleryWalkFinished) {
	this.galleryWalkFinished = galleryWalkFinished;
    }

    public boolean isGalleryWalkEditEnabled() {
	return galleryWalkEditEnabled;
    }

    public void setGalleryWalkEditEnabled(boolean galleryWalkEditEnabled) {
	this.galleryWalkEditEnabled = galleryWalkEditEnabled;
    }

    public String getGalleryWalkInstructions() {
	return galleryWalkInstructions;
    }

    public void setGalleryWalkInstructions(String galleryWalkInstructions) {
	this.galleryWalkInstructions = galleryWalkInstructions;
    }

    public Set<MindmapSession> getMindmapSessions() {
	return mindmapSessions;
    }

    public void setMindmapSessions(Set<MindmapSession> mindmapSessions) {
	this.mindmapSessions = mindmapSessions;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null) {
	    return false;
	}
	if (!(other instanceof Mindmap)) {
	    return false;
	}
	Mindmap castOther = (Mindmap) other;

	return this.getUid() == castOther.getUid()
		|| this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Mindmap newInstance(Mindmap fromContent, Long toContentId) {
	Mindmap toContent = new Mindmap();
	toContent = (Mindmap) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Mindmap mindmap = null;
	try {
	    mindmap = (Mindmap) super.clone();
	    mindmap.setUid(null);

	    // create an empty set for the mindmapSession
	    mindmap.mindmapSessions = new HashSet<>();

	} catch (CloneNotSupportedException cnse) {
	    Mindmap.log.error("Error cloning " + Mindmap.class);
	}
	return mindmap;
    }
}
