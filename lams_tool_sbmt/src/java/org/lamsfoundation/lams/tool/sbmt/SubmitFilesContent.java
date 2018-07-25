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


package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @serial 9072799761861936838L
 */
public class SubmitFilesContent implements Serializable, Cloneable {

    private static final long serialVersionUID = 9072799761861936838L;

    private static Logger log = Logger.getLogger(SubmitFilesContent.class);

    /** identifier field */
    private Long contentID;

    /** persistent field */
    // basic tab fields
    private String title;

    private String instruction;

    // advance tab fields
    private boolean lockOnFinished;

    private boolean notifyLearnersOnMarkRelease;

    private boolean notifyTeachersOnFileSubmit;

    // system level fields
    private boolean defineLater;

    private boolean contentInUse;
    
    private boolean useSelectLeaderToolOuput;

    private boolean limitUpload;

    private int limitUploadNumber;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private Date created;

    private Date updated;

    private Date submissionDeadline;

    private SubmitUser createdBy;

    /** default constructor */
    public SubmitFilesContent() {
    }

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

    /**
     *
     */
    public Long getContentID() {
	return contentID;
    }

    public void setContentID(Long contentID) {
	this.contentID = contentID;
    }

    /**
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
     */
    public String getInstruction() {
	return instruction;
    }

    public void setInstruction(String instructions) {
	instruction = instructions;
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

    /**
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
     *
     *
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

    /**
     *
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     * @return
     */
    public boolean isLimitUpload() {
	return limitUpload;
    }

    public void setLimitUpload(boolean limitUpload) {
	this.limitUpload = limitUpload;
    }

    /**
     *
     * @return
     */
    public int getLimitUploadNumber() {
	return limitUploadNumber;
    }

    public void setLimitUploadNumber(int limitUploadNumber) {
	this.limitUploadNumber = limitUploadNumber;
    }

    /**
     *
     * @return
     */
    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     *
     *
     * @return
     */
    public SubmitUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(SubmitUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     *
     * @return
     */
    public Date getUpdated() {
	return updated;
    }

    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     *
     * @return
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     *
     * @return
     */
    public boolean isNotifyLearnersOnMarkRelease() {
	return notifyLearnersOnMarkRelease;
    }

    public void setNotifyLearnersOnMarkRelease(boolean notifyLearnersOnMarkRelease) {
	this.notifyLearnersOnMarkRelease = notifyLearnersOnMarkRelease;
    }

    /**
     *
     * @return
     */
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
