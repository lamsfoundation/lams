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
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Manpreet Minhas
 */
public class LearningLibrary implements Serializable {

    /** identifier field */
    private Long learningLibraryId;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String title;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private Set activities;

    /** persistent field */
    private Boolean validLibrary;

    /* If the values for createDateTime is null, it will default to the current datetime */
    /** full constructor */
    public LearningLibrary(Long learningLibraryId, String description, String title, Date createDateTime,
	    Set activities) {
	this.learningLibraryId = learningLibraryId;
	this.description = description;
	this.title = title;
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
	this.activities = activities;
    }

    /** default constructor */
    public LearningLibrary() {
	this.createDateTime = new Date();
    }

    /** minimal constructor */
    public LearningLibrary(Long learningLibraryId, Date createDateTime, Set activities) {
	this.learningLibraryId = learningLibraryId;
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
	this.activities = activities;
    }

    public Long getLearningLibraryId() {
	return this.learningLibraryId;
    }

    public void setLearningLibraryId(Long learningLibraryId) {
	this.learningLibraryId = learningLibraryId;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Date getCreateDateTime() {
	return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime != null ? createDateTime : new Date();
    }

    public Set getActivities() {
	return this.activities;
    }

    public void setActivities(Set activities) {
	this.activities = activities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("learningLibraryId", getLearningLibraryId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof LearningLibrary)) {
	    return false;
	}
	LearningLibrary castOther = (LearningLibrary) other;
	return new EqualsBuilder().append(this.getLearningLibraryId(), castOther.getLearningLibraryId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getLearningLibraryId()).toHashCode();
    }

    public Boolean getValidLibrary() {
	return validLibrary;
    }

    public void setValidLibrary(Boolean validLibrary) {
	this.validLibrary = validLibrary;
    }
}