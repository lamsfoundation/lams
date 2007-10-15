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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learningdesign;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.BranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ToolOutputBranchActivityEntryDTO;

/** 
 * Models the relationship between a group and a branch for Chosen or Group based branching. 
 * Links the group to the sequence activity that makes up a branch within a branching activity.
 *
 * Models the relationship between a group and a branch for Tool Output Based branching. 
 * Links the condition that controls the set of users that access the sequence activity that makes up 
 * a branch within a branching activity. From the condition, a group can be created and populated.
 *
 * @author Fiona Malikoff
 * @version 2.1
 * 
 * @hibernate.class table="lams_branch_activity_entry"
*/
public class BranchActivityEntry {

	/** identifier field */
	protected Long entryId;
	/** persistent field */
	protected Integer entryUIID;
	/** persistent field */
	protected SequenceActivity branchSequenceActivity;
	/** persistent field */
	protected BranchingActivity branchingActivity;
    /** persistent field */
    private Group group;
    /** persistent field */
	private BranchCondition condition;

	/**
	 * Default Constructor (for hibernate)
	 */
	public BranchActivityEntry() {
	}

	/**
	 * Constructor for group based linking
	 */
	public BranchActivityEntry(Long entryId, Integer entryUIID, SequenceActivity branchSequenceActivity, 
			BranchingActivity branchingActivity, Group group) {
		
		this.entryId = entryId;
		this.entryUIID = entryUIID;
		this.branchSequenceActivity = branchSequenceActivity;
		this.branchingActivity = branchingActivity;
		this.group = group;
		this.condition = null;
	}
	
	/**
	 * Constructor for tool output based linking
	 */
	public BranchActivityEntry(Long entryId, Integer entryUIID, SequenceActivity branchSequenceActivity, 
			BranchingActivity branchingActivity, BranchCondition condition) {
		
		this.entryId = entryId;
		this.entryUIID = entryUIID;
		this.branchSequenceActivity = branchSequenceActivity;
		this.branchingActivity = branchingActivity;
		this.group = null;
		this.condition = condition;
	}
	

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
	 * Used for tool output based branching only
	 * 
	 *            @hibernate.many-to-one
	 *             not-null="true" cascade="all-delete-orphan" 
	 *            @hibernate.column name="condition_id"         
	 *         
	 */
	public BranchCondition getCondition() {
		return condition;
	}

	public void setCondition(BranchCondition condition) {
		this.condition = condition;
	}


	public BranchActivityEntryDTO getBranchActivityEntryDTO(Integer toolActivityUIID) {

		BranchActivityEntryDTO entryDTO = null;
		if ( getCondition() != null ) {
			ToolOutputBranchActivityEntryDTO toolEntryDTO = new ToolOutputBranchActivityEntryDTO();
			toolEntryDTO.setCondition(getCondition().getBranchConditionDTO(toolActivityUIID));
			entryDTO = toolEntryDTO;
		} else {
			entryDTO = new BranchActivityEntryDTO();
		}

		entryDTO.setEntryID(getEntryId());
		entryDTO.setEntryUIID(getEntryUIID());
		if ( getGroup() != null ) {
			entryDTO.setGroupUIID(getGroup().getGroupUIID());
		}
		entryDTO.setSequenceActivityUIID(getBranchSequenceActivity().getActivityUIID());
		entryDTO.setBranchingActivityUIID(getBranchingActivity().getActivityUIID());
		return entryDTO;
	}

	public boolean equals(Object other) {
	    if ( (this == other ) ) return true;
	    if ( !(other instanceof BranchActivityEntry) ) return false;
	    BranchActivityEntry castOther = (BranchActivityEntry) other;
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

	public int compareTo(Object other) {
		BranchActivityEntry castOther = (BranchActivityEntry) other;
		return new CompareToBuilder()
			.append(this.getEntryId(), castOther.getEntryId())
			.append(this.getEntryUIID(), castOther.getEntryUIID())
			.toComparison();
	}
	
	   public String toString() {
	        return new ToStringBuilder(this)
	            .append("entryId", getEntryId())
	            .append("entryUIID", getEntryUIID())
	            .append("group", getGroup() != null ? getGroup().getGroupId().toString() : "")
	            .append("sequence activity", getBranchSequenceActivity() != null ? getBranchSequenceActivity().getActivityId().toString() : "")
	            .toString();
	    }


}