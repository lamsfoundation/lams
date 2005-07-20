package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SubmissionDetails implements Serializable{
	/** identifier field */
	private Long submissionID;

	/** persistent field */
	private String filePath;

	/** persistent field */
	private String fileDescription;

	/** persistent field */
	private Date dateOfSubmission;
	
	/** persistent field */
	private Long userID;

	/** persistent field */
	private Long uuid;

	/** persistent field */
	private Long versionId;
	
	/** default constructor */
	public SubmissionDetails() {
	}

	/** full constructor */
	public SubmissionDetails(String filePath, Long uuid, Long versionId) {
	    this.filePath = filePath;
	    this.uuid = uuid;
	    this.versionId = versionId;
	}
	
	
	/** full constructor */
	public SubmissionDetails(String filePath, String fileDescription, Date dateOfSubmission, Long userID, Long uuid, Long versionId) {
	    this.filePath = filePath;
	    this.fileDescription = fileDescription;
	    this.dateOfSubmission = dateOfSubmission;
	    this.userID = userID;
	    this.uuid = uuid;
	    this.versionId = versionId;
	}

	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long"
	 *               column="submission_id"
	 */
	public Long getSubmissionID() {
		return this.submissionID;
	}

	public void setSubmissionID(Long submissionID) {
		this.submissionID = submissionID;
	}

	/**
	 * @hibernate.property column="filePath" length="250"
	 */
	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @hibernate.property column="fileDescription" length="250"
	 */
	public String getFileDescription() {
		return this.fileDescription;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	/**
	 * @hibernate.property column="date_of_submission" length="19"
	 */
	public Date getDateOfSubmission() {
		return this.dateOfSubmission;
	}

	public void setDateOfSubmission(Date dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	/**
	 * @hibernate.property column="uuid" length="20"
	 */
	public Long getUuid() {
		return this.uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	/**
	 * @hibernate.property column="version_id" length="20"
	 */
	public Long getVersionID() {
		return this.versionId;
	}

	public void setVersionID(Long versionID) {
		this.versionId = versionID;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("submissionID",getSubmissionID()).append("filePath", getFilePath())
				.append("fileDescription", getFileDescription())
				.append("dateOfSubmission", getDateOfSubmission())
				.append("uuid",getUuid())
				.append("versionID", getVersionID())
				.append("userID",getUserID())
				.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubmissionDetails))
			return false;
		SubmissionDetails castOther = (SubmissionDetails) other;
		return new EqualsBuilder().append(this.getSubmissionID(),
				castOther.getSubmissionID()).append(this.getFilePath(),
				castOther.getFilePath()).append(this.getFileDescription(),
				castOther.getFileDescription()).append(
				this.getDateOfSubmission(), castOther.getDateOfSubmission())
				.append(this.getUuid(), castOther.getUuid())
				.append(this.getVersionID(), castOther.getVersionID())				
				.append(this.getUserID(),castOther.getUserID())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSubmissionID()).append(
				getFilePath()).append(getFileDescription()).append(
				getDateOfSubmission()).append(getUuid()).append(getVersionID())
				.toHashCode();
	}

	/**
	 * @hibernate.property column="user_id" length="20"
	 * @return Returns the userID.
	 */
	public Long getUserID() {
		return userID;
	}
	/**
	 * @param userID
	 *            The userID to set.
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	/**
	 * @return Returns the versionId.
	 */
	public Long getVersionId() {
		return versionId;
	}
	/**
	 * @param versionId The versionId to set.
	 */
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
}
