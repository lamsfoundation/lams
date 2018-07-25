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

    public UserAccessFoldersDTO(Integer workspaceFolderID, String name, Date createDateTime, Integer parentFolderID) {
	super();
	this.workspaceFolderID = workspaceFolderID;
	this.name = name;
	this.createDateTime = createDateTime;
	this.parentFolderID = parentFolderID;
    }

    public UserAccessFoldersDTO(WorkspaceFolder folder) {
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
     * @param createDateTime
     *            The createDateTime to set.
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
     * @param name
     *            The name to set.
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
     * @param parentFolderID
     *            The parentFolderID to set.
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
     * @param workspaceFolderID
     *            The workspaceFolderID to set.
     */
    public void setWorkspaceFolderID(Integer workspaceFolderID) {
	this.workspaceFolderID = workspaceFolderID;
    }
}
