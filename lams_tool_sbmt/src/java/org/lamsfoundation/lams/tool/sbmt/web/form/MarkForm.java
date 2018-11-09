/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.sbmt.web.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lfoxton
 */
public class MarkForm {

    private Long toolSessionID;
    private Integer userID;
    private Long detailID;
    private Long reportID;
    private String marks;
    private MultipartFile markFile;
    private String comments;
    private String updateMode;
    private Long markFileUUID;
    private Long markFileVersionID;

    public MarkForm() {
    }

    public Long getToolSessionID() {
	return toolSessionID;
    }

    public void setToolSessionID(Long toolSessionID) {
	this.toolSessionID = toolSessionID;
    }

    public Integer getUserID() {
	return userID;
    }

    public void setUserID(Integer userID) {
	this.userID = userID;
    }

    public Long getDetailID() {
	return detailID;
    }

    public void setDetailID(Long detailID) {
	this.detailID = detailID;
    }

    public Long getReportID() {
	return reportID;
    }

    public void setReportID(Long reportID) {
	this.reportID = reportID;
    }

    public String getMarks() {
	return marks;
    }

    public void setMarks(String marks) {
	this.marks = marks;
    }

    public MultipartFile getMarkFile() {
	return markFile;
    }

    public void setMarkFile(MultipartFile markFile) {
	this.markFile = markFile;
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public String getUpdateMode() {
	return updateMode;
    }

    public void setUpdateMode(String updateMode) {
	this.updateMode = updateMode;
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
}
