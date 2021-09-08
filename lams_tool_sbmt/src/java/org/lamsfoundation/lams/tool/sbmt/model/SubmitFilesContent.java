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

package org.lamsfoundation.lams.tool.sbmt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "tl_lasbmt11_content")
public class SubmitFilesContent implements Serializable, Cloneable {
    private static final long serialVersionUID = 9072799761861936838L;
    private static Logger log = Logger.getLogger(SubmitFilesContent.class);

    @Id
    @Column(name = "content_id")
    private Long contentID;

    // basic tab fields

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String instruction;

    // advance tab fields

    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    @Column(name = "file_submit_notify")
    private boolean notifyTeachersOnFileSubmit;

    // system level fields

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "use_select_leader_tool_ouput")
    private boolean useSelectLeaderToolOuput;

    @Column(name = "limit_upload")
    private boolean limitUpload;

    //max limitUploadNumber
    @Column(name = "limit_upload_number")
    private int limitUploadNumber;

    @Column(name = "min_limit_upload_number")
    private Integer minLimitUploadNumber;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column
    private Date created;

    @Column
    private Date updated;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "created_by")
    private SubmitUser createdBy;

    /**
     * Copy constructor to create a new SubmitFiles tool's content.
     *
     * @param content
     *            The original tool content
     * @param newContentID
     *            The new <code>SubmitFiles</code> contentID
     * @param toolContentHandler
     * @return SubmitFilesContent The new SubmitFilesContent object
     */
    public static SubmitFilesContent newInstance(SubmitFilesContent content, Long newContentID) {
	SubmitFilesContent newContent = (SubmitFilesContent) content.clone();
	newContent.setContentID(newContentID);
	return newContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("contentID", getContentID()).append("title", getTitle())
		.append("instructions", getInstruction()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof SubmitFilesContent)) {
	    return false;
	}
	SubmitFilesContent castOther = (SubmitFilesContent) other;
	return new EqualsBuilder().append(this.getContentID(), castOther.getContentID())
		.append(this.getTitle(), castOther.getTitle()).append(this.getInstruction(), castOther.getInstruction())
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getContentID()).append(getTitle()).append(getInstruction()).toHashCode();
    }

    @Override
    public Object clone() {
	SubmitFilesContent content = null;
	try {
	    content = (SubmitFilesContent) super.clone();
	    // never clone key!
	    content.setContentID(null);

	    // clone SubmitUser as well
	    if (createdBy != null) {
		content.setCreatedBy((SubmitUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    SubmitFilesContent.log.error("When clone " + SubmitFilesContent.class + " failed");
	}

	return content;
    }

    // ***********************************************************
    // Get / Set methods
    // ***********************************************************

    public Long getContentID() {
	return contentID;
    }

    public void setContentID(Long contentID) {
	this.contentID = contentID;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstruction() {
	return instruction;
    }

    public void setInstruction(String instructions) {
	instruction = instructions;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @return Returns the contentInUse.
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    /**
     * @param contentInUse
     *            The contentInUse to set.
     */
    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @return Returns the lockOnFinished.
     */
    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    /**
     * @param lockOnFinished
     *            The lockOnFinished to set.
     */
    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public boolean isLimitUpload() {
	return limitUpload;
    }

    public void setLimitUpload(boolean limitUpload) {
	this.limitUpload = limitUpload;
    }

    public int getLimitUploadNumber() {
	return limitUploadNumber;
    }

    public void setLimitUploadNumber(int limitUploadNumber) {
	this.limitUploadNumber = limitUploadNumber;
    }

    public Integer getMinLimitUploadNumber() {
	return minLimitUploadNumber;
    }

    public void setMinLimitUploadNumber(Integer minLimitUploadNumber) {
	this.minLimitUploadNumber = minLimitUploadNumber;
    }

    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public SubmitUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(SubmitUser createdBy) {
	this.createdBy = createdBy;
    }

    public Date getUpdated() {
	return updated;
    }

    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    public boolean isNotifyTeachersOnFileSubmit() {
	return notifyTeachersOnFileSubmit;
    }

    public void setNotifyTeachersOnFileSubmit(boolean notifyTeachersOnFileSubmit) {
	this.notifyTeachersOnFileSubmit = notifyTeachersOnFileSubmit;
    }

    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }
}
