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
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Join table that maps workspace to workspace folders. Was having trouble getting Hibernate
 * to persist the join table.
 *
 * @author Fiona Malikoff
 */
public class WorkspaceWorkspaceFolder implements Serializable {

    private static final long serialVersionUID = -2111064926800456263L;

    /** identifier field */
    private Integer id;

    /** identifier field */
    private Workspace workspace;

    /** identifier field */
    private WorkspaceFolder workspaceFolder;

    /** default constructor */
    public WorkspaceWorkspaceFolder() {
    }

    public WorkspaceWorkspaceFolder(Integer id, Workspace workspace, WorkspaceFolder workspaceFolder) {
	this.id = id;
	this.workspace = workspace;
	this.workspaceFolder = workspaceFolder;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public WorkspaceFolder getWorkspaceFolder() {
	return this.workspaceFolder;
    }

    public void setWorkspaceFolder(WorkspaceFolder workspaceFolder) {
	this.workspaceFolder = workspaceFolder;
    }

    public Workspace getWorkspace() {
	return this.workspace;
    }

    public void setWorkspace(Workspace workspace) {
	this.workspace = workspace;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("id", getId()).append("workspace", getWorkspace())
		.append("workspaceFolder", getWorkspaceFolder()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof WorkspaceWorkspaceFolder)) {
	    return false;
	}
	WorkspaceWorkspaceFolder castOther = (WorkspaceWorkspaceFolder) other;
	return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
