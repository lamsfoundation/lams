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
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.dto.WorkspaceDTO;

public class Workspace implements Serializable {

    /** identifier field */
    private Integer workspaceId;

    /** persistent field */
    private Set workspaceWorkspaceFolders;

    /** persistent field */
    private WorkspaceFolder defaultFolder;

    /** persistent field */
    private WorkspaceFolder defaultRunSequencesFolder;

    /** persistent field */
    private Set users;

    /** persistent field */
    private Set organisations;

    /**
     * nullable persistent field representing the name of the workspace,
     * defaults to the name of the Organisation
     */
    private String name;

    public Workspace(String name) {
	super();
	this.name = name;
    }

    /** full constructor */
    public Workspace(WorkspaceFolder workspaceFolder, Set users, Set organisations) {
	this.users = users;
	this.organisations = organisations;
    }

    /** default constructor */
    public Workspace() {
    }

    public Integer getWorkspaceId() {
	return this.workspaceId;
    }

    public void setWorkspaceId(Integer workspaceId) {
	this.workspaceId = workspaceId;
    }

    public WorkspaceFolder getDefaultFolder() {
	return this.defaultFolder;
    }

    public void setDefaultFolder(WorkspaceFolder defaultFolder) {
	this.defaultFolder = defaultFolder;
    }

    /**
     * WorkspaceWorkspaceFolder is a join object that links a workspace folder to its workspaces.
     */
    public Set getWorkspaceWorkspaceFolders() {
	return workspaceWorkspaceFolders;
    }

    public void setWorkspaceWorkspaceFolders(Set workspaceWorkspaceFolders) {
	this.workspaceWorkspaceFolders = workspaceWorkspaceFolders;
    }

    /**
     * Get all the folders for this workspace, based on the lams_workspace_workspace join table. This set is not a
     * persistent
     * set so to add a folder use "addFolder(folder)" rather than getFolders().put(folder).
     */
    public Set<WorkspaceFolder> getFolders() {
	HashSet<WorkspaceFolder> set = new HashSet<WorkspaceFolder>();
	if (getWorkspaceWorkspaceFolders() != null) {
	    Iterator iter = getWorkspaceWorkspaceFolders().iterator();
	    while (iter.hasNext()) {
		WorkspaceWorkspaceFolder wwf = (WorkspaceWorkspaceFolder) iter.next();
		set.add(wwf.getWorkspaceFolder());
	    }
	}
	return set;
    }

    /** Add a folder to workspace. */
    public void addFolder(WorkspaceFolder folder) {
	// check that the folder doesn't already exist
	if (getWorkspaceWorkspaceFolders() != null) {
	    Iterator iter = getWorkspaceWorkspaceFolders().iterator();
	    while (iter.hasNext()) {
		WorkspaceWorkspaceFolder wwf = (WorkspaceWorkspaceFolder) iter.next();
		WorkspaceFolder wf = wwf.getWorkspaceFolder();
		if (wf.equals(folder)) {
		    return;
		}
	    }
	}

	// not found so add it to the set
	WorkspaceWorkspaceFolder wwf = new WorkspaceWorkspaceFolder(null, this, folder);
	if (getWorkspaceWorkspaceFolders() == null) {
	    setWorkspaceWorkspaceFolders(new HashSet());
	}
	getWorkspaceWorkspaceFolders().add(wwf);
    }

    public WorkspaceFolder getDefaultRunSequencesFolder() {
	return defaultRunSequencesFolder;
    }

    public void setDefaultRunSequencesFolder(WorkspaceFolder defaultRunSequencesFolder) {
	this.defaultRunSequencesFolder = defaultRunSequencesFolder;
    }

    public Set getUsers() {
	return this.users;
    }

    public void setUsers(Set users) {
	this.users = users;
    }

    public Set getOrganisations() {
	return this.organisations;
    }

    public void setOrganisations(Set organisations) {
	this.organisations = organisations;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("workspaceId", getWorkspaceId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof Workspace)) {
	    return false;
	}
	Workspace castOther = (Workspace) other;
	return new EqualsBuilder().append(this.getWorkspaceId(), castOther.getWorkspaceId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getWorkspaceId()).toHashCode();
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

    public WorkspaceDTO getWorkspaceDTO() {
	return new WorkspaceDTO(workspaceId, defaultFolder.getWorkspaceFolderId());
    }
}
