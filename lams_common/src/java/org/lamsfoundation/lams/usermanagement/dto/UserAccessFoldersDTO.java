/*
 * Created on Mar 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dto;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

/**
 * @author Manpreet Minhas
 */
public class UserAccessFoldersDTO {
	
	private Integer workspaceFolderID;	
	private String name;
	private Date createDateTime;	
	private Integer parentFolderID;	
	
	
	public UserAccessFoldersDTO(Integer workspaceFolderID, String name,
			Date createDateTime, Integer parentFolderID) {
		super();
		this.workspaceFolderID = workspaceFolderID;
		this.name = name;
		this.createDateTime = createDateTime;
		this.parentFolderID = parentFolderID;	
	}
	public UserAccessFoldersDTO(WorkspaceFolder folder){
		this.workspaceFolderID = folder.getWorkspaceFolderId();
		this.name = folder.getName();
		this.createDateTime = folder.getCreationDate();
		this.parentFolderID = folder.getParentWorkspaceFolder().getWorkspaceFolderId();		
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime;
	}
	/**
	 * @param createDateTime The createDateTime to set.
	 */
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
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
	 * @return Returns the parentFolderID.
	 */
	public Integer getParentFolderID() {
		return parentFolderID;
	}
	/**
	 * @param parentFolderID The parentFolderID to set.
	 */
	public void setParentFolderID(Integer parentFolderID) {
		this.parentFolderID = parentFolderID;
	}
	/**
	 * @return Returns the workspaceFolderID.
	 */
	public Integer getWorkspaceFolderID() {
		return workspaceFolderID;
	}
	/**
	 * @param workspaceFolderID The workspaceFolderID to set.
	 */
	public void setWorkspaceFolderID(Integer workspaceFolderID) {
		this.workspaceFolderID = workspaceFolderID;
	}
}
