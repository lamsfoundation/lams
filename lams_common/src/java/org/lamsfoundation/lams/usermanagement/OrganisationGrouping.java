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
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This is a course-level group of groups of learners, i.e. a grouping.
 *
 * @hibernate.class table="lams_organisation_grouping"
 */
public class OrganisationGrouping implements Serializable {

    /** identifier field */
    private Long groupingId;

    /** persistent field */
    private Integer organisationId;

    /** nullable persistent field */
    private String name;

    private Set<OrganisationGroup> groups;

    public OrganisationGrouping() {
    }

    public OrganisationGrouping(Integer organisationId, String name) {
	this.organisationId = organisationId;
	this.name = name;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="grouping_id"
     */
    public Long getGroupingId() {
	return groupingId;
    }

    public void setGroupingId(Long groupId) {
	this.groupingId = groupId;
    }

    /**
     * @hibernate.property column="organisation_id"
     */
    public Integer getOrganisationId() {
	return this.organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
	this.organisationId = organisationId;
    }

    /**
     * @hibernate.property column="name"
     */
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * @hibernate.set cascade="all-delete-orphan" inverse="true"
     * @hibernate.collection-key column="grouping_id"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.usermanagement.OrganisationGroup"
     */
    public Set<OrganisationGroup> getGroups() {
	return groups;
    }

    public void setGroups(Set<OrganisationGroup> groups) {
	this.groups = groups;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(groupingId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof OrganisationGroup)) {
	    return false;
	}
	OrganisationGrouping other = (OrganisationGrouping) obj;
	if (groupingId == null) {
	    if (other.groupingId != null) {
		return false;
	    }
	} else if (!groupingId.equals(other.groupingId)) {
	    return false;
	}
	return true;
    }
}