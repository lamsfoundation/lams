/*
 * Created on Apr 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.dto;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FolderContentDTO {
	
	public static final String LESSON ="lesson";
	public static final String DESIGN ="learningDesign";
	public static final String FOLDER ="folder";
	
	private String name;
	private String description;
	private Date creationDateTime;
	private Date lastModifiedDateTime;
	private String resourceType;
	private Long resourceID;
	private Integer permissionCode;
	
	public FolderContentDTO(){
		
	}

	public FolderContentDTO(String name, String description,
			Date creationDateTime, Date lastModifiedDateTime,
			String resourceType, Long resourceID, Integer permissionCode) {
		super();
		this.name = name;
		this.description = description;
		this.creationDateTime = creationDateTime;
		this.lastModifiedDateTime = lastModifiedDateTime;
		this.resourceType = resourceType;
		this.resourceID = resourceID;
		this.permissionCode = permissionCode;
	}
	public FolderContentDTO(LearningDesign design, Integer permissionCode){
		this.name = design.getTitle();
		this.description = design.getDescription();
		this.creationDateTime = design.getCreateDateTime();
		this.lastModifiedDateTime = design.getLastModifiedDateTime();
		this.resourceType = DESIGN;
		this.resourceID = design.getLearningDesignId();
		this.permissionCode = permissionCode;
	}
	public FolderContentDTO(WorkspaceFolder workspaceFolder, Integer permissionCode){
		this.name = workspaceFolder.getName();
		this.description = "Folder";
		this.creationDateTime = workspaceFolder.getCreationDate();
		this.lastModifiedDateTime = workspaceFolder.getLastModifiedDate();
		this.resourceType = FOLDER;
		this.resourceID = new Long(workspaceFolder.getWorkspaceFolderId().intValue());
		this.permissionCode = permissionCode;
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
}
