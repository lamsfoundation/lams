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

package org.lamsfoundation.lams.tool.sbmt.dto;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.sbmt.model.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitUser;
import org.lamsfoundation.lams.util.NumberUtil;

/**
 * @author Manpreet Minhas
 */
public class FileDetailsDTO implements Serializable {

    private Logger log = Logger.getLogger(FileDetailsDTO.class);
    private static final long serialVersionUID = 2964711101016972263L;

    private Long submissionID;

    //basic info
    private String filePath;
    private String fileDescription;

    //owner info
    private SubmitUserDTO owner;

    //report (mark) info
    private Long reportID;
    private String comments;
    private String marks;
    private Date dateOfSubmission;
    private String markFileName;
    private Long markFileUUID;
    private Long markFileVersionID;
    private String markFileDisplayUuid;

    //file repository info
    private Long uuID;
    private Long versionID;
    private String displayUuid;

    private boolean finished;
    //if this file uploaded by current learner
    private boolean currentLearner;

    private boolean removed;

    /**
     * @param details
     *            SubmissionDetails to be converted to the FileDetailsDTO
     * @param numberFormat
     *            NumberFormat based on the current locale, with which the marks will be converted from double to
     *            string. If null then a generic NumberFormat instance will be used.
     */
    public FileDetailsDTO(SubmissionDetails details, NumberFormat numberFormat) {
	if (details == null) {
	    log.warn("SubmissionDetails is null, failed to initial FileDetailDTO");
	    return;
	}

	SubmitUser learner = details.getLearner();
	if (learner != null) {
	    this.owner = new SubmitUserDTO(learner);
	}

	this.submissionID = details.getSubmissionID();
	this.filePath = details.getFilePath();
	this.fileDescription = details.getFileDescription();
	this.dateOfSubmission = details.getDateOfSubmission();
	this.uuID = details.getUuid();
	this.versionID = details.getVersionID();
	this.removed = details.isRemoved();
	SubmitFilesReport report = details.getReport();
	if (report != null) {
	    this.reportID = report.getReportID();
	    this.comments = report.getComments();
	    this.marks = report.getMarks() != null
		    ? NumberUtil.formatLocalisedNumber(report.getMarks(), numberFormat, 2)
		    : "";
	    this.markFileName = report.getMarkFileName();
	    this.markFileUUID = report.getMarkFileUUID();
	    this.markFileVersionID = report.getMarkFileVersionID();
	}
    }

    /**
     * @return Returns the reportID.
     */
    public Long getReportID() {
	return reportID;
    }

    /**
     * @param reportID
     *            The reportID to set.
     */
    public void setReportID(Long reportID) {
	this.reportID = reportID;
    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
	return comments;
    }

    /**
     * @param comments
     *            The comments to set.
     */
    public void setComments(String comments) {
	this.comments = comments;
    }

    /**
     * @return Returns the dateOfSubmission.
     */
    public Date getDateOfSubmission() {
	return dateOfSubmission;
    }

    /**
     * @param dateOfSubmission
     *            The dateOfSubmission to set.
     */
    public void setDateOfSubmission(Date dateOfSubmission) {
	this.dateOfSubmission = dateOfSubmission;
    }

    /**
     * @return Returns the fileDescription.
     */
    public String getFileDescription() {
	return fileDescription;
    }

    /**
     * @param fileDescription
     *            The fileDescription to set.
     */
    public void setFileDescription(String fileDescription) {
	this.fileDescription = fileDescription;
    }

    /**
     * @return Returns the filePath.
     */
    public String getFilePath() {
	return filePath;
    }

    /**
     * @param filePath
     *            The filePath to set.
     */
    public void setFilePath(String filePath) {
	this.filePath = filePath;
    }

    /**
     * @return Returns the marks.
     */
    public String getMarks() {
	return marks;
    }

    /**
     * @param marks
     *            The marks to set.
     */
    public void setMarks(String marks) {
	this.marks = marks;
    }

    /**
     * @return Returns the uuID.
     */
    public Long getUuID() {
	return uuID;
    }

    /**
     * @param uuID
     *            The uuID to set.
     */
    public void setUuID(Long uuID) {
	this.uuID = uuID;
    }

    /**
     * @return Returns the versionID.
     */
    public Long getVersionID() {
	return versionID;
    }

    /**
     * @param versionID
     *            The versionID to set.
     */
    public void setVersionID(Long versionID) {
	this.versionID = versionID;
    }

    public String getDisplayUuid() {
	return displayUuid;
    }

    public void setDisplayUuid(String displayUuid) {
	this.displayUuid = displayUuid;
    }

    /**
     * @return Returns the submissionID.
     */
    public Long getSubmissionID() {
	return submissionID;
    }

    /**
     * @param submissionID
     *            The submissionID to set.
     */
    public void setSubmissionID(Long submissionID) {
	this.submissionID = submissionID;
    }

    /**
     * @return Returns the finished.
     */
    public boolean isFinished() {
	return finished;
    }

    /**
     * @param finished
     *            The finished to set.
     */
    public void setFinished(boolean finished) {
	this.finished = finished;
    }

    public boolean isCurrentLearner() {
	return currentLearner;
    }

    public void setCurrentLearner(boolean currentLearner) {
	this.currentLearner = currentLearner;
    }

    public SubmitUserDTO getOwner() {
	return owner;
    }

    public void setOwner(SubmitUserDTO owner) {
	this.owner = owner;
    }

    public String getMarkFileName() {
	return markFileName;
    }

    public void setMarkFileName(String markFileName) {
	this.markFileName = markFileName;
    }

    public Long getMarkFileUUID() {
	return markFileUUID;
    }

    public void setMarkFileUUID(Long markFileUUID) {
	this.markFileUUID = markFileUUID;
    }

    public Long getMarkFileVersionID() {
	return markFileVersionID;
    }

    public void setMarkFileVersionID(Long markFileVersionID) {
	this.markFileVersionID = markFileVersionID;
    }

    public String getMarkFileDisplayUuid() {
	return markFileDisplayUuid;
    }

    public void setMarkFileDisplayUuid(String markFileDisplayUuid) {
	this.markFileDisplayUuid = markFileDisplayUuid;
    }

    public boolean isRemoved() {
	return removed;
    }

    public void setRemoved(boolean removed) {
	this.removed = removed;
    }
}
