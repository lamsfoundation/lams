/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.tool.sbmt.dto;

import java.io.Serializable;
import java.util.Date;

import org.lamsfoundation.lams.tool.sbmt.Learner;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * @author Manpreet Minhas
 */
public class FileDetailsDTO implements Serializable{
	
	/**
	 * For Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long userID;
	private Long submissionID;
	private Long reportID;
	private String filePath;
	private String fileDescription;
	private Date dateOfSubmission;
	private Date dateMarksReleased;
	private boolean finished;
	private String comments;	
	private Long marks;
	private Long uuID;
	private Long versionID;
	
	private UserDTO userDTO;
	

	public FileDetailsDTO(SubmissionDetails details, SubmitFilesReport report){

		Learner learner = details.getLearner();
		if(learner != null){
			this.userID = learner.getUserID();
			this.finished = learner.isFinished();
		}
		
		this.submissionID = details.getSubmissionID();
		this.filePath = details.getFilePath();
		this.fileDescription = details.getFileDescription();
		this.dateOfSubmission = details.getDateOfSubmission();
		this.uuID = details.getUuid();
		this.versionID = details.getVersionID();
		if(report != null){
			this.reportID = report.getReportID();
			this.dateMarksReleased = report.getDateMarksReleased();		
			this.comments = report.getComments();
			this.marks = report.getMarks();
		}
	}
	public FileDetailsDTO(SubmissionDetails details, SubmitFilesReport report,UserDTO userDTO){
		this(details,report);
		this.userDTO = userDTO;
	}
	public FileDetailsDTO(SubmissionDetails details){
		
		this.submissionID = details.getSubmissionID();
		this.filePath = details.getFilePath();
		this.fileDescription = details.getFileDescription();
		this.dateOfSubmission = details.getDateOfSubmission();
		this.uuID = details.getUuid();
		this.versionID = details.getVersionID();
		
		SubmitFilesReport report = details.getReport();
		if(report != null){
			this.reportID = report.getReportID();
			this.dateMarksReleased = report.getDateMarksReleased();		
			this.comments = report.getComments();
			this.marks = report.getMarks();
		}
	}
	/**
	 * @return Returns the reportID.
	 */
	public Long getReportID() {
		return reportID;
	}
	/**
	 * @param reportID The reportID to set.
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
	 * @param comments The comments to set.
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
	 * @param dateOfSubmission The dateOfSubmission to set.
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
	 * @param fileDescription The fileDescription to set.
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
	 * @param filePath The filePath to set.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return Returns the marks.
	 */
	public Long getMarks() {
		return marks;
	}
	/**
	 * @param marks The marks to set.
	 */
	public void setMarks(Long marks) {
		this.marks = marks;
	}	
	
	/**
	 * @return Returns the dateMarksReleased.
	 */
	public Date getDateMarksReleased() {
		return dateMarksReleased;
	}
	/**
	 * @param dateMarksReleased The dateMarksReleased to set.
	 */
	public void setDateMarksReleased(Date dateMarksReleased) {
		this.dateMarksReleased = dateMarksReleased;
	}
	/**
	 * @return Returns the uuID.
	 */
	public Long getUuID() {
		return uuID;
	}
	/**
	 * @param uuID The uuID to set.
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
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(Long versionID) {
		this.versionID = versionID;
	}
	/**
	 * @return Returns the submissionID.
	 */
	public Long getSubmissionID() {
		return submissionID;
	}
	/**
	 * @param submissionID The submissionID to set.
	 */
	public void setSubmissionID(Long submissionID) {
		this.submissionID = submissionID;
	}

	/**
	 * @return Returns the userID.
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * @param userID The userID to set.
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}

	/**
	 * @return Returns the userDTO.
	 */
	public UserDTO getUserDTO() {
		return userDTO;
	}

	/**
	 * @param userDTO The userDTO to set.
	 */
	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	/**
	 * @return Returns the finished.
	 */
	public boolean isFinished() {
		return finished;
	}
	/**
	 * @param finished The finished to set.
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
