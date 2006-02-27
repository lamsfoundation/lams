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
package org.lamsfoundation.lams.workspace.dto;
import java.util.Date;
import java.util.SortedSet;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Manpreet Minhas
 */
public class FolderContentDTO {
	
	public static final String LESSON ="Lesson";
	public static final String DESIGN ="LearningDesign";
	public static final String FOLDER ="Folder";
	public static final String FILE ="File";
	
	public String name;
	public String description;
	public Date creationDateTime;
	public Date lastModifiedDateTime;
	public String resourceType;
	public Long resourceID;
	public Integer permissionCode;
	public Vector versionDetails;
	public Long licenseID; // applicable for designs only
	public String licenseText; // applicable for designs only
	
	public FolderContentDTO(){
		
	}

	public FolderContentDTO(String name, String description,
			Date creationDateTime, Date lastModifiedDateTime,
			String resourceType, Long resourceID, Integer permissionCode,
			Long licenseID) {
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
	}
	public FolderContentDTO(LearningDesign design, Integer permissionCode){
		this.name = design.getTitle();
		this.description = design.getDescription();
		this.creationDateTime = design.getCreateDateTime();
		this.lastModifiedDateTime = design.getLastModifiedDateTime();
		this.resourceType = DESIGN;
		this.resourceID = design.getLearningDesignId();
		this.permissionCode = permissionCode;
		this.licenseID = ( design.getLicense() != null ? design.getLicense().getLicenseID() : null);
		this.licenseText = design.getLicenseText();
		this.versionDetails = null;
	}
	public FolderContentDTO(WorkspaceFolder workspaceFolder, Integer permissionCode){
		this.name = workspaceFolder.getName();
		this.description = "Folder";
		this.creationDateTime = workspaceFolder.getCreationDate();
		this.lastModifiedDateTime = workspaceFolder.getLastModifiedDate();
		this.resourceType = FOLDER;
		this.resourceID = new Long(workspaceFolder.getWorkspaceFolderId().intValue());
		this.permissionCode = permissionCode;
		this.licenseID = null;
		this.versionDetails = null;
	}	
	public FolderContentDTO(Integer permissionCode, WorkspaceFolderContent workspaceFolderContent,SortedSet details){
		this.name =workspaceFolderContent.getName();
		this.description = workspaceFolderContent.getDescription();
		this.creationDateTime = workspaceFolderContent.getCreateDate();
		this.lastModifiedDateTime = workspaceFolderContent.getLastModified();
		this.resourceID = workspaceFolderContent.getFolderContentID();
		this.permissionCode = permissionCode;		
		if(workspaceFolderContent.getContentTypeID().equals(WorkspaceFolderContent.CONTENT_TYPE_FILE))
			this.resourceType = FILE;
		else
			this.resourceType = FOLDER;			
		this.licenseID = null;
		this.versionDetails = new Vector();
		versionDetails.addAll(details);
	}	

	/**
	 * @return Returns the creationDateTime.
	 */
	public Date getCreationDateTime() {
		return creationDateTime!=null?creationDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the lastModifiedDateTime.
	 */
	public Date getLastModifiedDateTime() {
		return lastModifiedDateTime!=null?lastModifiedDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name!=null?name:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the permissionCode.
	 */
	public Integer getPermissionCode() {
		return permissionCode!=null?permissionCode:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the resourceID.
	 */
	public Long getResourceID() {
		return resourceID!=null?resourceID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the resourceType.
	 */
	public String getResourceType() {
		return resourceType!=null?resourceType:WDDXTAGS.STRING_NULL_VALUE;
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
}
