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

package org.lamsfoundation.lams.learningdesign.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Models the relationship between a group and a branch for Chosen or Group based branching.
 * Links the group to the sequence activity that makes up a branch within a branching activity.
 * Used by Authoring - so we just record the related UI IDs, rather than the actual objects
 * as they will appear elsewhere in the design.
 *
 * @author Fiona Malikoff
 * @version 2.1
 * 
 */
public class BranchActivityEntryDTO implements Serializable {

    private Long entryID;
    private Integer entryUIID;
    private Integer groupUIID;
    private Integer sequenceActivityUIID;
    private Integer branchingActivityUIID;

    //---------------------------------------------------------------------
    // Object creation Methods
    //---------------------------------------------------------------------

    public Long getEntryID() {
	return this.entryID;
    }

    public void setEntryID(Long id) {
	entryID = id;
    }

    public Integer getGroupUIID() {
	return this.groupUIID;
    }

    public void setGroupUIID(Integer groupUIID) {
	this.groupUIID = groupUIID;
    }

    public Integer getSequenceActivityUIID() {
	return this.sequenceActivityUIID;
    }

    public void setSequenceActivityUIID(Integer sequenceActivityUIID) {
	this.sequenceActivityUIID = sequenceActivityUIID;
    }

    public Integer getEntryUIID() {
	return entryUIID;
    }

    public void setEntryUIID(Integer entryUIID) {
	this.entryUIID = entryUIID;
    }

    public Integer getBranchingActivityUIID() {
	return branchingActivityUIID;
    }

    public void setBranchingActivityUIID(Integer branchingActivityUIID) {
	this.branchingActivityUIID = branchingActivityUIID;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("entryID", entryID).append("entryUIID", entryUIID)
		.append("branchingActivityUIID", branchingActivityUIID)
		.append("sequenceActivityUIID", sequenceActivityUIID).append("groupUIID", groupUIID).toString();
    }
}
