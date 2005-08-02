/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Manpreet Minhas
 * @serial 5900249986365640342L
 */
public class LearnerDetailsDTO implements Serializable{
	
	private static final long serialVersionUID = 5900249986365640342L;
	private Long userID;
	private Long toolSessionID;
	private String name;
	private String fileDescription;
	private Date dateOfSubmission;
	private String comments;	
	private Long marks;	
	private Date dateMarksReleased;
	
	private String contentTitle;
	private String contentInstruction;
	private boolean contentLockOnFinished;
	
	private List filesUploaded;

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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return Returns the contentInstruction.
	 */
	public String getContentInstruction() {
		return contentInstruction;
	}
	/**
	 * @param contentInstruction The contentInstruction to set.
	 */
	public void setContentInstruction(String contentInstruction) {
		this.contentInstruction = contentInstruction;
	}
	/**
	 * @return Returns the contentLockOnFinished.
	 */
	public boolean isContentLockOnFinished() {
		return contentLockOnFinished;
	}
	/**
	 * @param contentLockOnFinished The contentLockOnFinished to set.
	 */
	public void setContentLockOnFinished(boolean contentLockOnFinished) {
		this.contentLockOnFinished = contentLockOnFinished;
	}
	/**
	 * @return Returns the contentTitle.
	 */
	public String getContentTitle() {
		return contentTitle;
	}
	/**
	 * @param contentTitle The contentTitle to set.
	 */
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	/**
	 * @return Returns the toolSessionID.
	 */
	public Long getToolSessionID() {
		return toolSessionID;
	}
	/**
	 * @param toolSessionID The toolSessionID to set.
	 */
	public void setToolSessionID(Long toolSessionID) {
		this.toolSessionID = toolSessionID;
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
	 * @return Returns the filesUploaded.
	 */
	public List getFilesUploaded() {
		return filesUploaded;
	}
	/**
	 * @param filesUploaded The filesUploaded to set.
	 */
	public void setFilesUploaded(List filesUploaded) {
		this.filesUploaded = filesUploaded;
	}
}
