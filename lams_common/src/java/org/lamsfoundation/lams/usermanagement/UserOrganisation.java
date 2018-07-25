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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UserOrganisation implements Serializable {

    /** identifier field */
    private Integer userOrganisationId;

    /** persistent field */
    private User user;

    /** persistent field */
    private Organisation organisation;

    /** persistent field */
    private Set<UserOrganisationRole> userOrganisationRoles;

    /** full constructor */
    public UserOrganisation(User user, Organisation organisation, Set<UserOrganisationRole> userOrganisationRoles) {
	this.user = user;
	this.organisation = organisation;
	this.userOrganisationRoles = userOrganisationRoles;
    }

    /** minimal constructor */
    public UserOrganisation(User user, Organisation organisation) {
	this.user = user;
	this.organisation = organisation;
    }

    /** default constructor */
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
