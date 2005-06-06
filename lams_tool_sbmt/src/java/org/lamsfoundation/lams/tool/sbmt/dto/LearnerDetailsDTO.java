/*
 * Created on May 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Manpreet Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LearnerDetailsDTO implements Serializable{
	
	private String name;
	private String fileDescription;
	private Date dateOfSubmission;
	private String comments;	
	private Long marks;	
	private Date dateMarksReleased;
	
	
	

	public LearnerDetailsDTO(String name,String fileDescription, 
							Date dateOfSubmission, String comments,
							Long marks, Date dateMarksReleased) {
		super();
		this.name = name;
		this.fileDescription = fileDescription;
		this.dateOfSubmission = dateOfSubmission;
		this.comments = comments;
		this.marks = marks;
		this.dateMarksReleased = dateMarksReleased;
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
}
