/***************************************************************************
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
 * ************************************************************************
 */

package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Fei Yang,Manpreet Minhas
 *
 */
public class WorkspaceFolder implements Serializable {

    /** static final variables indicating the type of workspaceFolder */
    /******************************************************************/
    public static final Integer NORMAL = new Integer(1);
    public static final Integer RUN_SEQUENCES = new Integer(2);
    public static final Integer PUBLIC_SEQUENCES = new Integer(3);
    /******************************************************************/

    /** static final variables indicating the permissions on the workspaceFolder */
    /******************************************************************/
    public static final Integer READ_ACCESS = new Integer(1);
    public static final Integer MEMBERSHIP_ACCESS = new Integer(2);
    public static final Integer OWNER_ACCESS = new Integer(3);
    public static final Integer NO_ACCESS = new Integer(4);
    /******************************************************************/

    /** identifier field */
    private Integer workspaceFolderId;

    /** persistent field */
    private String name;

    /** persistent field */
    private WorkspaceFolder parentWorkspaceFolder;

    /** persistent field */
    private Set childWorkspaceFolders;

    private Set learningDesigns;

    /**
     * non-nullable persistent field indicating the
     * user who created/owns the workspace folder
     */
    private Integer userID;

    private Integer organisationID;

    /** non-nullable persistent field */
    private Date creationDate;

    /** non-nullable persistent field */
    private Date lastModifiedDate;

    /**
     * non-nullable persistent field indicating
     * the type of workspace folder. Can be either
     * NORMAL OR RUN_SEQUENCES
     */
    private Integer workspaceFolderType;

    /**
     * A Collection of <code>WorkspaceFolderContent</code>
     * objects representing the content of this
     * folder. As of now it represents only Files.
     */
    private Set folderContent;

    public WorkspaceFolder(String name, Integer userID, Date creationDate, Date lastModifiedDate,
	    Integer workspaceFolderType) {
	this.name = name;
	this.userID = userID;
	this.creationDate = creationDate;
	this.lastModifiedDate = lastModifiedDate;
	this.workspaceFolderType = workspaceFolderType;
    }

    public WorkspaceFolder(String name, WorkspaceFolder parentWorkspaceFolder, Integer userID, Date creationDate,
	    Date lastModifiedDate, Integer workspaceFolderType) {
	super();
	this.name = name;
	this.parentWorkspaceFolder = parentWorkspaceFolder;
	this.userID = userID;
	this.creationDate = creationDate;
	this.lastModifiedDate = lastModifiedDate;
	this.workspaceFolderType = workspaceFolderType;
    }

    /**
     * @return Returns the learningDesigns.
     */
    public Set getLearningDesigns() {
	return learningDesigns;
    }

    /**
     * @param learningDesigns
     *            The learningDesigns to set.
     */
    public void setLearningDesigns(Set learningDesigns) {
	this.learningDesigns = learningDesigns;
    }

    /** full constructor */
    public WorkspaceFolder(String name, WorkspaceFolder parentWorkspaceFolder, Set childWorkspaceFolders) {
	this.name = name;
	this.parentWorkspaceFolder = parentWorkspaceFolder;
	this.childWorkspaceFolders = childWorkspaceFolders;
    }

    /** default constructor */
    public WorkspaceFolder() {
    }

    public Integer getWorkspaceFolderId() {
	return this.workspaceFolderId;
    }

    public void setWorkspaceFolderId(Integer workspaceFolderId) {
	this.workspaceFolderId = workspaceFolderId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public WorkspaceFolder getParentWorkspaceFolder() {
	return this.parentWorkspaceFolder;
    }

    public void setParentWorkspaceFolder(WorkspaceFolder parentWorkspaceFolder) {
	this.parentWorkspaceFolder = parentWorkspaceFolder;
    }

    public Set getChildWorkspaceFolders() {
	return this.childWorkspaceFolders;
    }

    public void setChildWorkspaceFolders(Set childWorkspaceFolders) {
	this.childWorkspaceFolders = childWorkspaceFolders;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("workspaceFolderId", getWorkspaceFolderId()).append("name", getName())
		.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof WorkspaceFolder)) {
	    return false;
	}
	WorkspaceFolder castOther = (WorkspaceFolder) other;
	return new EqualsBuilder().append(this.getWorkspaceFolderId(), castOther.getWorkspaceFolderId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getWorkspaceFolderId()).toHashCode();
    }

    /**
     * @return Returns the userID.
     */
    public Integer getUserID() {
	return userID;
    }

    /**
     * @param userID
     *            The userID to set.
     */
    public void setUserID(Integer userID) {
	this.userID = userID;
    }

    public Integer getOrganisationID() {
	return organisationID;
    }

    public void setOrganisationID(Integer organisationID) {
	this.organisationID = organisationID;
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    /**
     * @return Returns the lastModifiedDate.
     */
    public Date getLastModifiedDate() {
	return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate
     *            The lastModifiedDate to set.
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
	this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return Returns the workspaceFolderType.
     */
    public Integer getWorkspaceFolderType() {
	return workspaceFolderType;
    }

    /**
     * @param workspaceFolderType
     *            The workspaceFolderType to set.
     */
    public void setWorkspaceFolderType(Integer workspaceFolderType) {
	this.workspaceFolderType = workspaceFolderType;
    }

    /**
     * This is a utility function which checks if the given
     * workspaceFolder has subFolders defined inside it.
     *
     * @return boolean A boolean value indicating whether the
     *         current workspaces contains subFolders
     */
    public boolean hasSubFolders() {
	if (this.childWorkspaceFolders != null && childWorkspaceFolders.size() != 0) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * This is a utility function which checks whether the given
     * workspace Folder is empty or not.
     *
     * @return boolean A boolean value indicating whether this
     *         folder is empty or it contains Learning Designs
     */
    public boolean isEmpty() {
	if (!hasSubFolders() && !hasFiles()) {
	    if (this.learningDesigns == null || this.learningDesigns.size() == 0) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    /**
     * This is a utility function which checks if the given
     * workspaceFolder has files inside it.
     *
     * @return boolean A boolean value indicating whether this
     *         folder is empty or it contains Files
     */
    public boolean hasFiles() {
	if (this.folderContent != null && this.folderContent.size() != 0) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * @return Returns the folderContents.
     */
    public Set getFolderContent() {
	return folderContent;
    }

    /**
     * @param folderContents
     *            The folderContents to set.
     */
    public void setFolderContent(Set folderContent) {
	this.folderContent = folderContent;
    }

    /**
     * @param workspaceFolderContent
     *            The content to be added
     */
    public void addFolderContent(WorkspaceFolderContent workspaceFolderContent) {
	if (this.folderContent == null) {
	    this.folderContent = new HashSet();
	}
	this.folderContent.add(workspaceFolderContent);
	workspaceFolderContent.setWorkspaceFolder(this);
    }

    public void addChild(WorkspaceFolder workspaceFolder) {
	if (childWorkspaceFolders == null) {
	    childWorkspaceFolders = new HashSet();
	}
	childWorkspaceFolders.add(workspaceFolder);
    }
}
