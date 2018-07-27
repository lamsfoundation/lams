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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UserOrganisationRole implements Serializable {

    /** identifier field */
    private Integer userOrganisationRoleId;

    /** persistent field */
    private UserOrganisation userOrganisation;

    /** persistent field */
    private Role role;

    /** full constructor */
    public UserOrganisationRole(UserOrganisation userOrganisation, Role role) {
	this.userOrganisation = userOrganisation;
	this.role = role;
    }

    /** default constructor */
    public UserOrganisationRole() {
    }

    public Integer getUserOrganisationRoleId() {
	return this.userOrganisationRoleId;
    }

    public void setUserOrganisationRoleId(Integer userOrganisationRoleId) {
	this.userOrganisationRoleId = userOrganisationRoleId;
    }

    public UserOrganisation getUserOrganisation() {
	return this.userOrganisation;
    }

    public void setUserOrganisation(UserOrganisation userOrganisation) {
	this.userOrganisation = userOrganisation;
    }

    public Role getRole() {
	return this.role;
    }

    public void setRole(Role role) {
	this.role = role;
    }

    public boolean hasRole(String[] roles) {
	for (int i = 0; i <= roles.length; i++) {
	    if (this.role.getName().equals(roles[i])) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("userOrganisationRoleId", getUserOrganisationRoleId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof UserOrganisationRole)) {
	    return false;
	}
	UserOrganisationRole castOther = (UserOrganisationRole) other;
	return new EqualsBuilder().append(this.getUserOrganisationRoleId(), castOther.getUserOrganisationRoleId())
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUserOrganisationRoleId()).toHashCode();
    }

}
