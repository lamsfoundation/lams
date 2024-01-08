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
import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.tool.sbmt.model.SubmitUser;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

/**
 * @author Manpreet Minhas
 * @serial 5900249986365640342L
 */
public class SubmitUserDTO implements Serializable, IUserDetails {

    private static final long serialVersionUID = 5900249986365640342L;
    private Long toolSessionID;

    //user personal info dto
    private Long userUid;
    private Integer userID;
    private String firstName;
    private String lastName;
    private String login;

    //learner uploaded file info
    private String fileName;
    private String fileDescription;
    private String comments;
    private Float marks;
    private Date dateOfSubmission;
    private boolean anyFilesMarked;

    //submit file list
    private List filesUploaded;

    public SubmitUserDTO(SubmitUser user) {
	this.userUid = user.getUid();
	this.userID = user.getUserID();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.login = user.getLogin();

    }

    /**
     * @return Returns the comments.
     */
    public String getComments() {
	return comments;
    }

    /**
     * @param comments
     * 	The comments to set.
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
     * 	The dateOfSubmission to set.
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
     * 	The fileDescription to set.
     */
    public void setFileDescription(String fileDescription) {
	this.fileDescription = fileDescription;
    }

    /**
     * @return Returns the name.
     */
    public String getFileName() {
	return fileName;
    }

    /**
     * @param name
     * 	The name to set.
     */
    public void setFileName(String name) {
	this.fileName = name;
    }

    /**
     * @return Returns the marks.
     */
    public Float getMarks() {
	return marks;
    }

    /**
     * @param marks
     * 	The marks to set.
     */
    public void setMarks(Float marks) {
	this.marks = marks;
    }

    /**
     * @return Returns the toolSessionID.
     */
    public Long getToolSessionID() {
	return toolSessionID;
    }

    /**
     * @param toolSessionID
     * 	The toolSessionID to set.
     */
    public void setToolSessionID(Long toolSessionID) {
	this.toolSessionID = toolSessionID;
    }

    /**
     * @return Returns the filesUploaded.
     */
    public List getFilesUploaded() {
	return filesUploaded;
    }

    /**
     * @param filesUploaded
     * 	The filesUploaded to set.
     */
    public void setFilesUploaded(List filesUploaded) {
	this.filesUploaded = filesUploaded;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String loginName) {
	this.login = loginName;
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }

    public Integer getUserID() {
	return userID;
    }

    public void setUserID(Integer userID) {
	this.userID = userID;
    }

    public boolean isAnyFilesMarked() {
	return anyFilesMarked;
    }

    public void setAnyFilesMarked(boolean anyFilesMarked) {
	this.anyFilesMarked = anyFilesMarked;
    }

    public String getFullName() {
	return new StringBuilder(getLastName()).append(" ").append(getFirstName()).toString();
    }
}