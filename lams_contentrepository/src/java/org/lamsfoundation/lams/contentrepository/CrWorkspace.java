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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_cr_workspace")
public class CrWorkspace implements IWorkspace, Serializable {

    @Id
    @Column(name = "workspace_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workspaceId;

    @Column
    private String name;

    @OneToMany(mappedBy = "crWorkspace",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true)
    private Set<CrWorkspaceCredential> crWorkspaceCredentials;

    @OneToMany(mappedBy = "crWorkspace",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true)
    private Set<CrNode> crNodes;

    /** full constructor */
    public CrWorkspace(String name, Set<CrWorkspaceCredential> crWorkspaceCredentials, Set<CrNode> crNodes) {
	this.name = name;
	this.crWorkspaceCredentials = crWorkspaceCredentials;
	this.crNodes = crNodes;
    }

    public CrWorkspace() {
    }

    @Override
    public Long getWorkspaceId() {
	return this.workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
	this.workspaceId = workspaceId;
    }

    @Override
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<CrWorkspaceCredential> getCrWorkspaceCredentials() {
	return this.crWorkspaceCredentials;
    }

    public void setCrWorkspaceCredentials(Set<CrWorkspaceCredential> crWorkspaceCredentials) {
	this.crWorkspaceCredentials = crWorkspaceCredentials;
    }

    public Set<CrNode> getCrNodes() {
	return this.crNodes;
    }

    public void setCrNodes(Set<CrNode> crNodes) {
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
