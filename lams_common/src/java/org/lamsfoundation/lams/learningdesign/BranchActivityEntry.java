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


package org.lamsfoundation.lams.learningdesign;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.BranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ToolOutputBranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ToolOutputGateActivityEntryDTO;

/**
 * Models the relationship between a group and a branch for Chosen or Group based branching. Links the group to the
 * sequence activity that makes up a branch within a branching activity.
 *
 * Models the relationship between a group and a branch for Tool Output Based branching. Links the condition that
 * controls the set of users that access the sequence activity that makes up a branch within a branching activity. From
 * the condition, a group can be created and populated.
 *
 * @author Fiona Malikoff
 * @version 2.1
 */
public class BranchActivityEntry {

    /** identifier field */
    protected Long entryId;
    /** persistent field */
    protected Integer entryUIID;
    /** persistent field */
    protected SequenceActivity branchSequenceActivity;
    /** persistent field */
    protected Activity branchingActivity;
    /** persistent field */
    private Group group;
    /** persistent field */
    private BranchCondition condition;
    /** persistent field */
    private Boolean gateOpenWhenConditionMet;

    /**
     * Default Constructor (for hibernate)
     */
    public BranchActivityEntry() {
    }

    /**
     * Constructor for group based linking
     */
    public BranchActivityEntry(Long entryId, Integer entryUIID, SequenceActivity branchSequenceActivity,
	    Activity branchingActivity, Group group) {

	this.entryId = entryId;
	this.entryUIID = entryUIID;
	this.branchSequenceActivity = branchSequenceActivity;
	this.branchingActivity = branchingActivity;
	this.group = group;

	condition = null;
    }

    /**
     * Constructor for tool output based linking
     */
    public BranchActivityEntry(Long entryId, Integer entryUIID, SequenceActivity branchSequenceActivity,
	    Activity branchingActivity, BranchCondition condition, Boolean gateOpenWhenConditionMet) {

	this.entryId = entryId;
	this.entryUIID = entryUIID;
	this.branchSequenceActivity = branchSequenceActivity;
	this.branchingActivity = branchingActivity;
	group = null;
	this.gateOpenWhenConditionMet = gateOpenWhenConditionMet;
	this.condition = condition;
    }

    public Long getEntryId() {
	return entryId;
    }

    public void setEntryId(Long id) {
	entryId = id;
    }

    public Integer getEntryUIID() {
	return entryUIID;
    }

    public void setEntryUIID(Integer entryUIID) {
	this.entryUIID = entryUIID;
    }

    public org.lamsfoundation.lams.learningdesign.SequenceActivity getBranchSequenceActivity() {
	return branchSequenceActivity;
    }

    public void setBranchSequenceActivity(
	    org.lamsfoundation.lams.learningdesign.SequenceActivity branchSequenceActivity) {
	this.branchSequenceActivity = branchSequenceActivity;
    }

    public org.lamsfoundation.lams.learningdesign.Activity getBranchingActivity() {
	return branchingActivity;
    }

    public void setBranchingActivity(Activity branchingActivity) {
	this.branchingActivity = branchingActivity;
    }

    public org.lamsfoundation.lams.learningdesign.Group getGroup() {
	return group;
    }

    public void setGroup(org.lamsfoundation.lams.learningdesign.Group group) {
	this.group = group;
    }

    public BranchCondition getCondition() {
	return condition;
    }

    public void setCondition(BranchCondition condition) {
	this.condition = condition;
    }

    public BranchActivityEntryDTO getBranchActivityEntryDTO(Integer toolActivityUIID) {

	BranchActivityEntryDTO entryDTO = null;
	if (getCondition() != null) {
	    ToolOutputBranchActivityEntryDTO toolEntryDTO = null;
	    if (getGateOpenWhenConditionMet() != null) {
		ToolOutputGateActivityEntryDTO gateEntryDTO = new ToolOutputGateActivityEntryDTO();
		gateEntryDTO.setGateOpenWhenConditionMet(getGateOpenWhenConditionMet());
		toolEntryDTO = gateEntryDTO;
	    } else {
		toolEntryDTO = new ToolOutputBranchActivityEntryDTO();
	    }
	    toolEntryDTO.setCondition(getCondition().getBranchConditionDTO(toolActivityUIID));
	    entryDTO = toolEntryDTO;

	} else {
	    entryDTO = new BranchActivityEntryDTO();
	}

	entryDTO.setEntryID(getEntryId());
	entryDTO.setEntryUIID(getEntryUIID());
	if (getGroup() != null) {
	    entryDTO.setGroupUIID(getGroup().getGroupUIID());
	}
	if (getBranchSequenceActivity() != null) {
	    entryDTO.setSequenceActivityUIID(getBranchSequenceActivity().getActivityUIID());
	}
	entryDTO.setBranchingActivityUIID(getBranchingActivity().getActivityUIID());
	return entryDTO;
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof BranchActivityEntry)) {
	    return false;
	}
	BranchActivityEntry castOther = (BranchActivityEntry) other;
	return new EqualsBuilder().append(this.getEntryId(), castOther.getEntryId())
		.append(this.getEntryUIID(), castOther.getEntryUIID()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getEntryId()).append(getEntryUIID()).toHashCode();
    }

    public int compareTo(Object other) {
	BranchActivityEntry castOther = (BranchActivityEntry) other;
	return new CompareToBuilder().append(this.getEntryId(), castOther.getEntryId())
		.append(this.getEntryUIID(), castOther.getEntryUIID()).toComparison();
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("entryId", getEntryId()).append("entryUIID", getEntryUIID())
		.append("group", getGroup() != null ? getGroup().getGroupId().toString() : "")
		.append("sequence activity", getBranchSequenceActivity() != null
			? getBranchSequenceActivity().getActivityId().toString() : "")
		.toString();
    }

    public Boolean getGateOpenWhenConditionMet() {
	return gateOpenWhenConditionMet;
    }

    public void setGateOpenWhenConditionMet(Boolean gateOpenWhenConditionMet) {
	this.gateOpenWhenConditionMet = gateOpenWhenConditionMet;
    }

}