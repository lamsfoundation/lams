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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This is a course-level group of learners.
 */
@Entity
@Table(name = "lams_organisation_group")
public class OrganisationGroup implements Serializable, Comparable<OrganisationGroup> {
    private static final long serialVersionUID = 397854186770141054L;

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "grouping_id")
    private Long groupingId;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "lams_user_organisation_group", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public OrganisationGroup() {
    }

    public Long getGroupId() {
	return groupId;
    }

    public void setGroupId(Long groupId) {
	this.groupId = groupId;
    }

    public Long getGroupingId() {
	return this.groupingId;
    }

    public void setGroupingId(Long organisationId) {
	this.groupingId = organisationId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Set<User> getUsers() {
	return users;
    }

    public void setUsers(Set<User> users) {
	this.users = users;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(groupId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if ((obj == null) || !(obj instanceof OrganisationGroup)) {
	    return false;
	}
	OrganisationGroup other = (OrganisationGroup) obj;
	if (groupId == null) {
	    if (other.groupId != null) {
		return false;
	    }
	} else if (!groupId.equals(other.groupId)) {
	    return false;
	}
	return true;
    }

    @Override
    public int compareTo(OrganisationGroup group) {
	return new CompareToBuilder()
		.append(StringUtils.lowerCase(this.getName()), StringUtils.lowerCase(group.getName()))
		.append(this.getGroupId(), group.getGroupId()).toComparison();
    }
}