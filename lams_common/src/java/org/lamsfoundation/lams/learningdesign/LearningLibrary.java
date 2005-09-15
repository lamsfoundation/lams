/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;



/** 
 * @author Manpreet Minhas
 *        @hibernate.class
 *         table="lams_learning_library"
 *     
*/
public class LearningLibrary implements Serializable {
	
	/** Represents a single LearningLibrary object in the WDDXPacket */
	public static final String LIBRARY_OBJECT ="LearningLibrary";
	
	/** Represents a list of LearningLibrary objects in the WDDXPacket */
	public static final String LIBRARY_LIST_OBJECT ="LearningLibraryList";

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

    /** full constructor */
    public LearningLibrary(Long learningLibraryId, String description, String title, Date createDateTime, Set activities) {
        this.learningLibraryId = learningLibraryId;
        this.description = description;
        this.title = title;
        this.createDateTime = createDateTime;
        this.activities = activities;
    }

    /** default constructor */
    public LearningLibrary() {
    }

    /** minimal constructor */
    public LearningLibrary(Long learningLibraryId, Date createDateTime, Set activities) {
        this.learningLibraryId = learningLibraryId;
        this.createDateTime = createDateTime;
        this.activities = activities;
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="learning_library_id"
     *         
     */
    public Long getLearningLibraryId() {
        return this.learningLibraryId;
    }

    public void setLearningLibraryId(Long learningLibraryId) {
        this.learningLibraryId = learningLibraryId;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="65535"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.property
     *             column="title"
     *             length="255"
     *         
     */
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /** 
     *            @hibernate.property
     *             column="create_date_time"
     *             length="19"
     *             not-null="true"
     *         
     */
    public Date getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    /** 
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="learning_library_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.learningdesign.Activity"
     *         
     */
    public Set getActivities() {
        return this.activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learningLibraryId", getLearningLibraryId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof LearningLibrary) ) return false;
        LearningLibrary castOther = (LearningLibrary) other;
        return new EqualsBuilder()
            .append(this.getLearningLibraryId(), castOther.getLearningLibraryId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLearningLibraryId())
            .toHashCode();
    }
	public Boolean getValidLibrary() {
		return validLibrary;
	}
	public void setValidLibrary(Boolean validLibrary) {
		this.validLibrary = validLibrary;
	}
	public LearningLibraryDTO getLearningLibraryDTO(){
		return new LearningLibraryDTO(this);
	}
	
	public LearningLibraryDTO getLearningLibraryDTO(List templateActivities) {
		return new LearningLibraryDTO(this, templateActivities);
	}
	
	
}
