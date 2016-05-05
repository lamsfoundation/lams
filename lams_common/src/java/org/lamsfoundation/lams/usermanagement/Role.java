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
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Role implements Serializable, Comparable {

    public static final String LEARNER = "LEARNER";

    public static final String MONITOR = "MONITOR";

    public static final String AUTHOR = "AUTHOR";

    public static final String GROUP_MANAGER = "GROUP MANAGER";

    public static final String GROUP_ADMIN = "GROUP ADMIN";

    public static final String SYSADMIN = "SYSADMIN";// for future use

    /**
     * Added by Manpreet Minhas *********************** final static variables indicating the various roles available
     * for a given user ar per the database. New roles may be added/deleted in the near future
     *
     ************************************************************/
    public static final Integer ROLE_SYSADMIN = new Integer(1);
    public static final Integer ROLE_GROUP_MANAGER = new Integer(2);
    public static final Integer ROLE_AUTHOR = new Integer(3);
    public static final Integer ROLE_MONITOR = new Integer(4);
    public static final Integer ROLE_LEARNER = new Integer(5);
    public static final Integer ROLE_GROUP_ADMIN = new Integer(6);
    /***********************************************************/

    /** identifier field */
    private Integer roleId;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Set rolePrivileges;

    /** full constructor */
    public Role(String name, String description) {
	this.name = name;
	this.description = description;
    }

    /** default constructor */
    public Role() {
    }

    /** minimal constructor */
    public Role(String name, Set userOrganisationRoles) {
	this.name = name;
    }

    public Integer getRoleId() {
	return this.roleId;
    }

    public void setRoleId(Integer roleId) {
	this.roleId = roleId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Set getRolePrivileges() {
	return rolePrivileges;
    }

    public void setRolePrivileges(Set rolePrivileges) {
	this.rolePrivileges = rolePrivileges;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("roleId", getRoleId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof Role)) {
	    return false;
	}
	Role castOther = (Role) other;
	return new EqualsBuilder().append(this.getRoleId(), castOther.getRoleId()).isEquals();
    }

    @Override
    public int compareTo(Object role) {
	Role r = (Role) role;
	return name.compareTo(r.getName());
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getRoleId()).toHashCode();
    }

    public boolean isAuthor() {
	return this.roleId.equals(Role.ROLE_AUTHOR);
    }

    public boolean isGroupManager() {
	return this.roleId.equals(Role.ROLE_GROUP_MANAGER);
    }

    public boolean isGroupAdmin() {
	return this.roleId.equals(Role.ROLE_GROUP_ADMIN);
    }

    public boolean isMonitor() {
	return this.roleId.equals(Role.ROLE_MONITOR);
    }

    public boolean isSysAdmin() {
	return this.roleId.equals(Role.ROLE_SYSADMIN);
    }
}