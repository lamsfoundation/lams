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

package org.lamsfoundation.lams.workspace.dto;

import java.util.Date;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Manpreet Minhas
 */
public class FolderContentDTO implements Comparable<FolderContentDTO> {

    public static final String LESSON = "Lesson";
    public static final String DESIGN = "LearningDesign";
    public static final String FOLDER = "Folder";
    public static final String FILE = "File";

    public String name;
    public String description;
    public Date creationDateTime;
    public Date lastModifiedDateTime;
    public String formattedLastModifiedDateTime;
    public String resourceType;
    public Long resourceTypeID;
    public Long resourceID;
    public Integer permissionCode;
    public Vector versionDetails;
    public Long licenseID; // applicable for designs only
    public String licenseText; // applicable for designs only
    public Boolean readOnly; // applicable for designs only
    public String designType; // applicable for designs only
    public String author;
    public String originalAuthor;

    public FolderContentDTO() {

    }

    public FolderContentDTO(String name, String description, Date creationDateTime, Date lastModifiedDateTime,
	    String resourceType, Long resourceID, Integer permissionCode, Long licenseID) {
	super();
	this.name = name;
	this.description = description;
	this.creationDateTime = creationDateTime;
	this.lastModifiedDateTime = lastModifiedDateTime;
	this.resourceType = resourceType;
	this.resourceID = resourceID;
	this.permissionCode = permissionCode;
	this.licenseID = licenseID;
	this.versionDetails = null;
	this.designType = null;
	this.readOnly = false;
    }

    public FolderContentDTO(LearningDesign design, Integer permissionCode, User user) {
	this.name = design.getTitle();
	this.description = design.getDescription();
	this.creationDateTime = design.getCreateDateTime();
	this.lastModifiedDateTime = design.getLastModifiedDateTime();
	this.formattedLastModifiedDateTime = formatLastModifiedDateTime(TimeZone.getTimeZone(user.getTimeZone()));
	this.resourceType = FolderContentDTO.DESIGN;
	this.resourceID = design.getLearningDesignId();
	this.permissionCode = permissionCode;
	this.licenseID = (design.getLicense() != null ? design.getLicense().getLicenseID() : null);
	this.licenseText = design.getLicenseText();
	this.designType = design.getDesignType();
	this.versionDetails = null;
	this.readOnly = design.getReadOnly();
	this.author = design.getUser().getFullName();
	this.originalAuthor = design.getOriginalUser() == null ? null : design.getOriginalUser().getFullName();
    }

    public FolderContentDTO(WorkspaceFolder workspaceFolder, Integer permissionCode, User user) {
	this.name = workspaceFolder.getName();
	this.description = "Folder";
	this.creationDateTime = workspaceFolder.getCreationDate();
	this.lastModifiedDateTime = workspaceFolder.getLastModifiedDate();
	this.formattedLastModifiedDateTime = formatLastModifiedDateTime(TimeZone.getTimeZone(user.getTimeZone()));
	this.resourceType = FolderContentDTO.FOLDER;
	this.resourceTypeID = new Long(workspaceFolder.getWorkspaceFolderType().intValue());
	this.resourceID = new Long(workspaceFolder.getWorkspaceFolderId().intValue());
	this.permissionCode = permissionCode;
	this.licenseID = null;
	this.designType = null;
	this.versionDetails = null;
	this.readOnly = Boolean.FALSE;
    }

    public FolderContentDTO(Integer permissionCode, WorkspaceFolderContent workspaceFolderContent, SortedSet details,
	    User user) {
	this.name = workspaceFolderContent.getName();
	this.description = workspaceFolderContent.getDescription();
	this.creationDateTime = workspaceFolderContent.getCreateDate();
	this.lastModifiedDateTime = workspaceFolderContent.getLastModified();
	this.formattedLastModifiedDateTime = formatLastModifiedDateTime(TimeZone.getTimeZone(user.getTimeZone()));
	this.resourceID = workspaceFolderContent.getFolderContentID();
	this.permissionCode = permissionCode;
	if (workspaceFolderContent.getContentTypeID().equals(WorkspaceFolderContent.CONTENT_TYPE_FILE)) {
	    this.resourceType = FolderContentDTO.FILE;
	} else {
	    this.resourceType = FolderContentDTO.FOLDER;
	}
	this.licenseID = null;
	this.versionDetails = new Vector();
	versionDetails.addAll(details);
	this.readOnly = Boolean.FALSE;
	this.designType = null;
    }

    @Override
    public int compareTo(FolderContentDTO anotherContent) {
	if (anotherContent == null) {
	    return 1;
	}
	// folders go first, then sort by name
	return resourceType.equals(anotherContent.getResourceType())
		? (name == null ? (anotherContent.getName() == null ? 0 : -1)
			: (anotherContent.getName() == null ? 1 : name.compareToIgnoreCase(anotherContent.getName())))
		: (FolderContentDTO.FOLDER.equals(anotherContent.getResourceType()) ? -1 : 1);
    }

    /**
     * @return Returns the creationDateTime.
     */
    public Date getCreationDateTime() {
	return creationDateTime;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the lastModifiedDateTime.
     */
    public Date getLastModifiedDateTime() {
	return lastModifiedDateTime;
    }

    /**
     * @return Returns the lastModifiedDateTime.
     */
    public String getFormattedLastModifiedDateTime() {
	return formattedLastModifiedDateTime;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @return Returns the permissionCode.
     */
    public Integer getPermissionCode() {
	return permissionCode;
    }

    /**
     * @return Returns the resourceID.
     */
    public Long getResourceID() {
	return resourceID;
    }

    /**
     * @return Returns the resourceType.
     */
    public String getResourceType() {
	return resourceType;
    }

    /**
     * @return Returns the designType.
     */
    public String getDesignType() {
	return designType;
    }

    /**
     * @return Returns the resourceTypeID.
     */
    public Long getResourceTypeID() {
	return resourceTypeID;
    }

    /**
     * @return Returns the versionDetails.
     */
    public Vector getVersionDetails() {
	return versionDetails;
    }

    /** Get the ID of the related license. Only applicable for learning designs */
    public Long getLicenseID() {
	return licenseID;
    }

    /** Set the ID of the related license. Only applicable for learning designs */
    public void setLicenseID(Long licenseID) {
	this.licenseID = licenseID;
    }

    public String getLicenseText() {
	return licenseText;
    }

    public void setLicenseText(String licenseText) {
	this.licenseText = licenseText;
    }

    public Boolean getReadOnly() {
	return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
	this.readOnly = readOnly;
    }

    public String getAuthor() {
	return this.author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getOriginalAuthor() {
	return originalAuthor;
    }

    public void setOriginalAuthor(String originalAuthor) {
	this.originalAuthor = originalAuthor;
    }

    public void setDesignType(String designType) {
	this.designType = designType;
    }

    private String formatLastModifiedDateTime(TimeZone tz) {
	if (this.lastModifiedDateTime != null) {
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	    return sdf.format(DateUtil.convertToTimeZoneFromDefault(tz, this.lastModifiedDateTime));
	} else {
	    return null;
	}
    }
}