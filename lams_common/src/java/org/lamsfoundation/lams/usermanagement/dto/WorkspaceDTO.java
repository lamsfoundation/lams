/*
 * Created on Mar 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dto;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceDTO {
	
	private Integer workspaceID;
	private Integer rootFolderID;
	
	
	public WorkspaceDTO(){
		
	}

	public WorkspaceDTO(Integer workspaceID, Integer rootFolderID) {
		super();
		this.workspaceID = workspaceID;
		this.rootFolderID = rootFolderID;
	}
	/**
	 * @return Returns the rootFolderID.
	 */
	public Integer getRootFolderID() {
		return rootFolderID;
	}
	/**
	 * @return Returns the workspaceID.
	 */
	public Integer getWorkspaceID() {
		return workspaceID;
	}
}
