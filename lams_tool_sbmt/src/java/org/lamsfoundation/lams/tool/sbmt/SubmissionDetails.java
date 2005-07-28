package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/**
 * @hibernate.class table="tl_lasbmt11_submission_details"
 * @serial 5093528405144051727L
 */
public class SubmissionDetails implements Serializable,Cloneable{

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
	private Long userID;

	/** persistent field */
	private Long uuid;

	/** persistent field */
	private Long versionID;
	
	/** persistent field */
	private SubmitFilesReport report;

	/** persistent field, but not cloned to avoid to clone block*/
	private SubmitFilesSession submitFileSession;
	
	/** default constructor */
	public SubmissionDetails() {
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
		return this.versionID;
	}

	public void setVersionID(Long versionID) {
		this.versionID = versionID;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("submissionID",getSubmissionID()).append("filePath", getFilePath())
				.append("fileDescription", getFileDescription())
				.append("dateOfSubmission", getDateOfSubmission())
				.append("uuid",getUuid())
				.append("versionID", getVersionID())
				.append("userID",getUserID())
				.append("report",getReport())
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
				.append(this.getReport(),castOther.getReport())
				.append(this.getUserID(),castOther.getUserID())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getSubmissionID()).append(
				getFilePath()).append(getFileDescription()).append(
				getDateOfSubmission()).append(getUuid()).append(getVersionID())
				.append(getReport()).append(getUserID()).toHashCode();
	}

	/**
	 * @hibernate.one-to-one  cascade="all" not-null="false"
	 * foreign-key="report_id"
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone()  {
		Object obj = null;
		try {
			obj = super.clone();
			if(this.report != null)
				((SubmissionDetails)obj).report = (SubmitFilesReport) this.report.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmissionDetails.class + " failed");
		}
		return obj;
	}

	/**
	 * @hibernate.many-to-one column="session_id" cascade="save-update" 
	 * @return Returns the submitFileSession.
	 */
	public SubmitFilesSession getSubmitFileSession() {
		return submitFileSession;
	}
	/**
	 * @param submitFileSession The submitFileSession to set.
	 */
	public void setSubmitFileSession(SubmitFilesSession submitFileSession) {
		this.submitFileSession = submitFileSession;
	}
}
