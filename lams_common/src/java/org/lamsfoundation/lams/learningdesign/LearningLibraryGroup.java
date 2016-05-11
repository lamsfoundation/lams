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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents a tool (learning library) group for use in Authoring.
 *
 *
 */
public class LearningLibraryGroup implements Serializable {
    private Long groupId;

    private String name;

    private Set<LearningLibrary> learningLibraries;

    /**
     *
     */
    public Long getGroupId() {
	return groupId;
    }

    public void setGroupId(Long groupId) {
	this.groupId = groupId;
    }

    /**
     *
     */
    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     *
     *
     *
     *
     */
    public Set<LearningLibrary> getLearningLibraries() {
	return learningLibraries;
    }

    public void setLearningLibraries(Set<LearningLibrary> learningLibraries) {
	this.learningLibraries = learningLibraries;
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
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	LearningLibraryGroup other = (LearningLibraryGroup) obj;
	if (groupId == null) {
	    if (other.groupId != null) {
		return false;
	    }
	} else if (!groupId.equals(other.groupId)) {
	    return false;
	}
	return true;
    }
}