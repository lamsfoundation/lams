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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Manpreet Minhas
 */
@Entity
@Table(name = "lams_learning_library")
public class LearningLibrary implements Serializable {
    private static final long serialVersionUID = -6055089410863321036L;

    @Id
    @Column(name = "learning_library_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningLibraryId;

    @Column
    private String description;

    @Column
    private String title;

    @Column(name = "create_date_time")
    private Date createDateTime;

    @OneToMany(mappedBy = "learningLibrary")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<Activity> activities = new HashSet<Activity>();

    @Column(name = "valid_flag")
    private Boolean validLibrary;

    /** default constructor */
    public LearningLibrary() {
	this.createDateTime = new Date();
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

    public Set<Activity> getActivities() {
	return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
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