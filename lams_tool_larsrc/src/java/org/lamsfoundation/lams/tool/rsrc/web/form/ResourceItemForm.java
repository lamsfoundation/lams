/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.rsrc.web.form;

/**
 * Resource Item Form.
 *
 * @author Steve.Ni
 */
public class ResourceItemForm {
    private String itemIndex;
    private String sessionMapID;

    //tool access mode;
    private String mode;

    private String title;
    private short itemType;
    private String instructions;
    private String url;
    //flag of this item has attachment or not
    private boolean hasFile;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileName;
    private String tmpFileUploadId;
    private boolean allowRating;
    private boolean allowComments;

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String description) {
	this.instructions = description;
    }

    public String getTmpFileUploadId() {
	return tmpFileUploadId;
    }

    public void setTmpFileUploadId(String tmpFileUploadId) {
	this.tmpFileUploadId = tmpFileUploadId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getItemIndex() {
	return itemIndex;
    }

    public void setItemIndex(String itemIndex) {
	this.itemIndex = itemIndex;
    }

    public short getItemType() {
	return itemType;
    }

    public void setItemType(short type) {
	this.itemType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long fileUuid) {
	this.fileUuid = fileUuid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long fileVersionId) {
	this.fileVersionId = fileVersionId;
    }

    public boolean isHasFile() {
	return hasFile;
    }

    public void setHasFile(boolean hasFile) {
	this.hasFile = hasFile;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
    }

    public boolean isAllowRating() {
	return allowRating;
    }

    public void setAllowRating(boolean allowRating) {
	this.allowRating = allowRating;
    }

    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }
}
