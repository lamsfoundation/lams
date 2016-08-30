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
 * @serial 5093528405144051727L
 */
public class SubmissionDetails implements Serializable, Cloneable {

    private static final long serialVersionUID = 5093528405144051727L;

    private static Logger log = Logger.getLogger(SubmissionDetails.class);

    /** identifier field */
    private Long submissionID;

    /** persistent field */
    private String filePath;

    /** persistent field */
    private String fileDescription;

    /** persistent field */
    private Date dateOfSubmission;

    /** persistent field */
    private Long uuid;

    /** persistent field */
    private Long versionID;

    /** persistent field */
    private SubmitFilesReport report;

    private SubmitUser learner;

    private Boolean removed;
    
    /** persistent field, but not cloned to avoid to clone block */
    private SubmitFilesSession submitFileSession;

    /** default constructor */
    public SubmissionDetails() {
	this.removed = Boolean.FALSE;
    }

    /**
     *
     *
     */
    public Long getSubmissionID() {
	return this.submissionID;
    }

    public void setSubmissionID(Long submissionID) {
	this.submissionID = submissionID;
    }

    /**
     *
     */
    public String getFilePath() {
	return this.filePath;
    }

    public void setFilePath(String filePath) {
	this.filePath = filePath;
    }

    /**
     *
     */
    public String getFileDescription() {
	return this.fileDescription;
    }

    public void setFileDescription(String fileDescription) {
	this.fileDescription = fileDescription;
    }

    /**
     *
     */
    public Date getDateOfSubmission() {
	return this.dateOfSubmission;
    }

    public void setDateOfSubmission(Date dateOfSubmission) {
	this.dateOfSubmission = dateOfSubmission;
    }

    /**
     *
     */
    public Long getUuid() {
	return this.uuid;
    }

    public void setUuid(Long uuid) {
	this.uuid = uuid;
    }

    /**
     *
     */
    public Long getVersionID() {
	return this.versionID;
    }

    public void setVersionID(Long versionID) {
	this.versionID = versionID;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("submissionID", getSubmissionID()).append("filePath", getFilePath())
		.append("fileDescription", getFileDescription()).append("dateOfSubmission", getDateOfSubmission())
		.append("uuid", getUuid()).append("versionID", getVersionID()).append("report", getReport()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof SubmissionDetails)) {
	    return false;
	}
	SubmissionDetails castOther = (SubmissionDetails) other;
	return new EqualsBuilder().append(this.getSubmissionID(), castOther.getSubmissionID())
		.append(this.getFilePath(), castOther.getFilePath())
		.append(this.getFileDescription(), castOther.getFileDescription())
		.append(this.getDateOfSubmission(), castOther.getDateOfSubmission())
		.append(this.getUuid(), castOther.getUuid()).append(this.getVersionID(), castOther.getVersionID())
		.append(this.getReport(), castOther.getReport()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getSubmissionID()).append(getFilePath()).append(getFileDescription())
		.append(getDateOfSubmission()).append(getUuid()).append(getVersionID()).append(getReport())
		.toHashCode();
    }

    /**
     *
     * foreign-key="report_id"
     * 
     * @return Returns the report.
     */
    public SubmitFilesReport getReport() {
	return report;
    }

    /**
     * @param report
     *            The report to set.
     */
    public void setReport(SubmitFilesReport report) {
	this.report = report;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    //never clone key!
	    ((SubmissionDetails) obj).setSubmissionID(null);
	    if (this.report != null) {
		((SubmissionDetails) obj).report = (SubmitFilesReport) this.report.clone();
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SubmissionDetails.class + " failed");
	}
	return obj;
    }

    /**
     *
     * @return Returns the submitFileSession.
     */
    public SubmitFilesSession getSubmitFileSession() {
	return submitFileSession;
    }

    /**
     * @param submitFileSession
     *            The submitFileSession to set.
     */
    public void setSubmitFileSession(SubmitFilesSession submitFileSession) {
	this.submitFileSession = submitFileSession;
    }

    /**
     * /**
     *
     * @return Returns the learner.
     */
    public SubmitUser getLearner() {
	return learner;
    }

    /**
     * @param learner
     *            The learner to set.
     */
    public void setLearner(SubmitUser learner) {
	this.learner = learner;
    }

    public Boolean isRemoved() {
	return removed;
    }

    public void setRemoved(Boolean removed) {
	this.removed = removed;
    }


}
