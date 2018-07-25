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


package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 *
 *
 *
*/
public class CrWorkspace implements IWorkspace, Serializable {

    /** identifier field */
    private Long workspaceId;

    /** persistent field */
    private String name;

    /** persistent field */
    private Set crWorkspaceCredentials;

    /** persistent field */
    private Set crNodes;

    /** full constructor */
    public CrWorkspace(String name, Set crWorkspaceCredentials, Set crNodes) {
	this.name = name;
	this.crWorkspaceCredentials = crWorkspaceCredentials;
	this.crNodes = crNodes;
    }

    /** default constructor */
    public CrWorkspace() {
    }

    /**
     *
     *
     *
     *
     *
     *
     */
    @Override
    public Long getWorkspaceId() {
	return this.workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
	this.workspaceId = workspaceId;
    }

    /**
     *
     *
     *
     *
     *
     */
    @Override
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * bi-directional one-to-many association to CrWorkspaceCredential
     *
     *
     *
     *
     *
     *
     * 
     */
    public Set getCrWorkspaceCredentials() {
	return this.crWorkspaceCredentials;
    }

    public void setCrWorkspaceCredentials(Set crWorkspaceCredentials) {
	this.crWorkspaceCredentials = crWorkspaceCredentials;
    }

    /**
     * bi-directional one-to-many association to CrNode
     *
     *
     *
     *
     *
     *
     * 
     */
    public Set getCrNodes() {
	return this.crNodes;
    }

    public void setCrNodes(Set crNodes) {
	this.crNodes = crNodes;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("workspaceId", getWorkspaceId()).append("name", getName()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof CrWorkspace)) {
	    return false;
	}
	CrWorkspace castOther = (CrWorkspace) other;
	return new EqualsBuilder().append(this.getWorkspaceId(), castOther.getWorkspaceId())
		.append(this.getName(), castOther.getName()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getWorkspaceId()).append(getName()).toHashCode();
    }

}
