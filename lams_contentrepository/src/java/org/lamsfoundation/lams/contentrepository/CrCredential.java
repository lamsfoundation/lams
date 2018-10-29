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
@Table(name = "lams_cr_credential")
public class CrCredential implements Serializable {

    @Id
    @Column(name = "credential_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialId;

    @Column
    private String name;

    @Column
    private String password;

    @OneToMany(mappedBy = "crCredential",
	    cascade = CascadeType.ALL,
	    orphanRemoval = true)
    private Set<CrWorkspaceCredential> crWorkspaceCredentials;

    /** full constructor */
    public CrCredential(String name, String password, Set<CrWorkspaceCredential> crWorkspaceCredentials) {
	this.name = name;
	this.password = password;
	this.crWorkspaceCredentials = crWorkspaceCredentials;
    }

    /** default constructor */
    public CrCredential() {
    }

    public Long getCredentialId() {
	return this.credentialId;
    }

    public void setCredentialId(Long credentialId) {
	this.credentialId = credentialId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getPassword() {
	return this.password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public Set<CrWorkspaceCredential> getCrWorkspaceCredentials() {
	return this.crWorkspaceCredentials;
    }

    public void setCrWorkspaceCredentials(Set<CrWorkspaceCredential> crWorkspaceCredentials) {
	this.crWorkspaceCredentials = crWorkspaceCredentials;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("credentialId", getCredentialId()).append("name", getName()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof CrCredential)) {
	    return false;
	}
	CrCredential castOther = (CrCredential) other;
	return new EqualsBuilder().append(this.getCredentialId(), castOther.getCredentialId())
		.append(this.getName(), castOther.getName()).append(this.getPassword(), castOther.getPassword())
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getCredentialId()).append(getName()).append(getPassword()).toHashCode();
    }

}
