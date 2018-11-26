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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_cr_workspace_credential")
public class CrWorkspaceCredential implements Serializable {

    @Id
    @Column(name = "wc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wcId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id")
    private org.lamsfoundation.lams.contentrepository.CrCredential crCredential;

    /** full constructor */
    public CrWorkspaceCredential(org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace,
	    org.lamsfoundation.lams.contentrepository.CrCredential crCredential) {
	this.crWorkspace = crWorkspace;
	this.crCredential = crCredential;
    }

    /** default constructor */
    public CrWorkspaceCredential() {
    }

    public Long getWcId() {
	return this.wcId;
    }

    public void setWcId(Long wcId) {
	this.wcId = wcId;
    }

    public org.lamsfoundation.lams.contentrepository.CrWorkspace getCrWorkspace() {
	return this.crWorkspace;
    }

    public void setCrWorkspace(org.lamsfoundation.lams.contentrepository.CrWorkspace crWorkspace) {
	this.crWorkspace = crWorkspace;
    }

    public org.lamsfoundation.lams.contentrepository.CrCredential getCrCredential() {
	return this.crCredential;
    }

    public void setCrCredential(org.lamsfoundation.lams.contentrepository.CrCredential crCredential) {
	this.crCredential = crCredential;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("wcId", getWcId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof CrWorkspaceCredential)) {
	    return false;
	}
	CrWorkspaceCredential castOther = (CrWorkspaceCredential) other;
	return new EqualsBuilder().append(this.getWcId(), castOther.getWcId())
		.append(this.getCrWorkspace(), castOther.getCrWorkspace())
		.append(this.getCrCredential(), castOther.getCrCredential()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getWcId()).append(getCrWorkspace()).append(getCrCredential()).toHashCode();
    }

}
