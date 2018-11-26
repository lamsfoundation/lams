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

package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_user_organisation")
@NamedNativeQuery(resultClass = UserOrganisation.class, name = "userOrganisationsNotById", query = "SELECT userOrganisation.*"
	+ " FROM lams_user_organisation AS userOrganisation"
	+ " JOIN lams_organisation AS o ON userOrganisation.user_id = :userId"
	+ " AND userOrganisation.organisation_id != :orgId AND o.parent_organisation_id != :orgId"
	+ " AND o.organisation_id = userOrganisation.organisation_id")
public class UserOrganisation implements Serializable {
    private static final long serialVersionUID = 4859079742960814389L;

    @Id
    @Column(name = "user_organisation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userOrganisationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @OneToMany(mappedBy = "userOrganisation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserOrganisationRole> userOrganisationRoles = new HashSet<UserOrganisationRole>();

    public UserOrganisation(User user, Organisation organisation) {
	this.user = user;
	this.organisation = organisation;
    }

    public UserOrganisation() {
    }

    public Integer getUserOrganisationId() {
	return this.userOrganisationId;
    }

    public void setUserOrganisationId(Integer userOrganisationId) {
	this.userOrganisationId = userOrganisationId;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Organisation getOrganisation() {
	return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public Set<UserOrganisationRole> getUserOrganisationRoles() {
	return this.userOrganisationRoles;
    }

    public void setUserOrganisationRoles(Set<UserOrganisationRole> userOrganisationRoles) {
	this.userOrganisationRoles = userOrganisationRoles;
    }

    public void addUserOrganisationRole(UserOrganisationRole userOrganisationRole) {
	if (userOrganisationRoles == null) {
	    userOrganisationRoles = new HashSet<>();
	}
	userOrganisationRoles.add(userOrganisationRole);
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("userOrganisationId", getUserOrganisationId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof UserOrganisation)) {
	    return false;
	}
	UserOrganisation castOther = (UserOrganisation) other;
	return new EqualsBuilder().append(this.getUserOrganisationId(), castOther.getUserOrganisationId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUserOrganisationId()).toHashCode();
    }
}