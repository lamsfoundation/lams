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

import java.util.HashSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;


/**
 * The ToolOutputBranchActivityEntries record the branch selection details for tool output based branching. During 
 * authoring, the tool will have supplied a ToolOutputDefinition object and this will have been used to configure 
 * which tool outputs match to a particular branch. This mapping is as a ToolOutputBranchActivityEntry.
 * 
 * There should be one branch condition for each ToolOutputBranchActivityEntry.
 */
public class BranchCondition {

	private Long conditionId;
	private Integer conditionUIID;
    private Integer orderId; 
    private String name; 
    private String type; 
    private String startValue; 
    private String endValue; 
    private String exactMatchValue;
    
    /** default constructor */
    public BranchCondition() {
    }

    /** full constructor */
    public BranchCondition(Long conditionId, Integer conditionUIID, Integer orderId, 
    			String name, String type, String startValue, String endValue, String exactMatchValue) {
    	this.conditionId = conditionId;
    	this.conditionUIID = conditionUIID;
    	this.orderId = orderId;
    	this.name = name;
    	this.type = type;
    	this.startValue = startValue;
    	this.endValue = endValue;
    	this.exactMatchValue = exactMatchValue;
    }

    
 	/** 
	 *            @hibernate.id
	 *             generator-class="native"
	 *             type="java.lang.Long"
	 *             column="condition_id"
	 *         
	 */
	public Long getConditionId() {
		return conditionId;
	}
	public void setConditionId(Long conditionId) {
		this.conditionId = conditionId;
	}
	
	/** 
	 *            @hibernate.property column="condition_ui_id" length="11"
	 *         
	 */
	public Integer getConditionUIID() {
		return conditionUIID;
	}
	public void setConditionUIID(Integer conditionUIID) {
		this.conditionUIID = conditionUIID;
	}
	
	/**
	 * @hibernate.property column="order_id" length="11"
	 */
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	/**
	 * @hibernate.property column="name" length="255"
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @hibernate.property column="type" length="255"
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @hibernate.property column="start_value" length="255"
	 */
	public String getStartValue() {
		return startValue;
	}
	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}
	/**
	 * @hibernate.property column="end_value" length="255"
	 */
	public String getEndValue() {
		return endValue;
	}
	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}
	/**
	 * @hibernate.property column="exact_match_value" length="255"
	 */
	public String getExactMatchValue() {
		return exactMatchValue;
	}
	public void setExactMatchValue(String exactMatchValue) {
		this.exactMatchValue = exactMatchValue;
	} 

	public BranchConditionDTO getBranchConditionDTO() {
		return new BranchConditionDTO(this);
	}

    public String toString() {
        return new ToStringBuilder(this)
        	.append("conditionId", conditionId)
            .append("conditionUIID", conditionUIID)
            .append("orderId", orderId)
            .append("name", name)
            .append("type", type)
            .append("startValue", startValue)
            .append("endValue", endValue)
            .append("exactMatchValue", exactMatchValue)
            .toString();
    }
 
    /** Allocate this condition to the given branch, in a branching activity. This creates the BranchActivityEntry record and adds it 
     * to the branchActivities set. EntryUIID will only be populated if this is called from authoring
     */
    public BranchActivityEntry allocateBranchToCondition(Integer entryUIID, SequenceActivity branch, BranchingActivity branchingActivity) {
    	BranchActivityEntry entry = new BranchActivityEntry(null, entryUIID, branch, (BranchingActivity) branchingActivity, this);
		return entry;
    }
    
    /** Create a new BranchCondition based on itself, leaving conditionId as null */
    public BranchCondition clone() {
    	return new BranchCondition(null, conditionUIID, orderId, name, type, startValue, endValue, exactMatchValue);
    }

}
