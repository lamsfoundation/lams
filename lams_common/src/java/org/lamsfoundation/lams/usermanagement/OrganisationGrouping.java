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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This is a course-level group of groups of learners, i.e. a grouping.
 */
@Entity
@Table(name = "lams_organisation_grouping")
public class OrganisationGrouping implements Serializable {
    private static final long serialVersionUID = -7641875911260975245L;

    @Id
    @Column(name = "grouping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupingId;

    @Column(name = "organisation_id")
    private Integer organisationId;

    @Column
    private String name;

    @OneToMany(mappedBy = "groupingId", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrganisationGroup> groups = new HashSet<OrganisationGroup>();

    public OrganisationGrouping() {
    }

    public OrganisationGrouping(Integer organisationId, String name) {
	this.organisationId = organisationId;
	this.name = name;
    }

    public Long getGroupingId() {
	return groupingId;
    }

    public void setGroupingId(Long groupId) {
	this.groupingId = groupId;
    }

    public Integer getOrganisationId() {
	return this.organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
	this.organisationId = organisationId;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

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