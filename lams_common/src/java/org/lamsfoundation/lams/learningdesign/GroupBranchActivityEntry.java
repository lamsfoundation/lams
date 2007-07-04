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
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.GroupBranchActivityEntryDTO;


/** 
 * Models the relationship between a group and a branch for Chosen or Group based branching. 
 * Links the group to the sequence activity that makes up a branch within a branching activity.
 * 
 * @author Fiona Malikoff
 * @version 2.1
 * 
 * @hibernate.class table="lams_group_branch_activity"
*/
public class GroupBranchActivityEntry implements Serializable, Comparable {
    
    /** identifier field */
    private Long entryId;
    
    /** persistent field */
    private Integer entryUIID;
    
    /** persistent field */
    private Group group;

    /** persistent field */
    private SequenceActivity branchSequenceActivity;

    /** persistent field */
    private BranchingActivity branchingActivity;

    //---------------------------------------------------------------------
    // Object creation Methods
    //---------------------------------------------------------------------
    
    /** full constructor */
    public GroupBranchActivityEntry(Long entryId, Integer entryUIID, Group group, SequenceActivity branchSequenceActivity, BranchingActivity branchingActivity) {
        this.entryId = entryId;
        this.entryUIID = entryUIID;
        this.group = group;
        this.branchSequenceActivity = branchSequenceActivity;
        this.branchingActivity = branchingActivity;
    }

    /** default constructor */
    public GroupBranchActivityEntry() {
    }
    //---------------------------------------------------------------------
    // Field Access Methods
    //---------------------------------------------------------------------

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="entry_id"
     *         
     */
    public Long getEntryId() {
        return this.entryId;
    }
    
    public void setEntryId(Long id) {
    	entryId = id;
    }

	/**
	 * @hibernate.property column="entry_ui_id" length="11"
	 */
	public Integer getEntryUIID() {
		return this.entryUIID;
	}

	public void setEntryUIID(Integer entryUIID) {
		this.entryUIID = entryUIID;
	}

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="group_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.Group getGroup() {
        return this.group;
    }

    public void setGroup(org.lamsfoundation.lams.learningdesign.Group group) {
        this.group = group;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="sequence_activity_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.SequenceActivity getBranchSequenceActivity() {
        return this.branchSequenceActivity;
    }

    public void setBranchSequenceActivity(org.lamsfoundation.lams.learningdesign.SequenceActivity branchSequenceActivity) {
        this.branchSequenceActivity = branchSequenceActivity;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="branch_activity_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.BranchingActivity getBranchingActivity() {
        return this.branchingActivity;
    }

    public void setBranchingActivity(org.lamsfoundation.lams.learningdesign.BranchingActivity branchingActivity) {
        this.branchingActivity = branchingActivity;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("entryId", getEntryId())
            .append("entryUIID", getEntryUIID())
            .append("group", getGroup() != null ? getGroup().getGroupId().toString() : "")
            .append("sequence activity", getBranchSequenceActivity() != null ? getBranchSequenceActivity().getActivityId().toString() : "")
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof GroupBranchActivityEntry) ) return false;
        GroupBranchActivityEntry castOther = (GroupBranchActivityEntry) other;
        return new EqualsBuilder()
        	.append(this.getEntryId(), castOther.getEntryId())
        	.append(this.getEntryUIID(), castOther.getEntryUIID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
        	.append(getEntryId())
        	.append(getEntryUIID())
            .toHashCode();
    }
    
    public GroupBranchActivityEntryDTO getGroupBranchActivityDTO() {
    	return new GroupBranchActivityEntryDTO(this);
    }

	public int compareTo(Object other) {
        GroupBranchActivityEntry castOther = (GroupBranchActivityEntry) other;
		return new CompareToBuilder()
			.append(this.getEntryId(), castOther.getEntryId())
    		.append(this.getEntryUIID(), castOther.getEntryUIID())
    		.toComparison();
	}

}
