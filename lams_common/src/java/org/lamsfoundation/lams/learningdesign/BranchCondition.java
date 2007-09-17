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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputValue;


/**
 * The ToolOutputBranchActivityEntries record the branch selection details for tool output based branching. During 
 * authoring, the tool will have supplied a ToolOutputDefinition object and this will have been used to configure 
 * which tool outputs match to a particular branch. This mapping is as a ToolOutputBranchActivityEntry.
 * 
 * There should be one branch condition for each ToolOutputBranchActivityEntry.
 */
public class BranchCondition implements Comparable {

	private Long conditionId;
	private Integer conditionUIID;
    private Integer orderId; 
    private String name; 
    private String displayName;
    private String type; 
    private String startValue; 
    private String endValue; 
    private String exactMatchValue;
    
    /** default constructor */
    public BranchCondition() {
    }

    /** full constructor */
    public BranchCondition(Long conditionId, Integer conditionUIID, Integer orderId, 
    			String name, String displayName, String type, String startValue, String endValue, String exactMatchValue) {
    	this.conditionId = conditionId;
    	this.conditionUIID = conditionUIID;
    	this.orderId = orderId;
    	this.name = name;
    	this.displayName = displayName;
    	this.type = type;
    	this.startValue = startValue;
    	this.endValue = endValue;
    	this.exactMatchValue = exactMatchValue;
    }

    /** Create a condition object based on an existing DTO object. Copies all fields including the id field */
    public BranchCondition(BranchConditionDTO conditionDTO) {
    	this(conditionDTO.getConditionId(), 
			conditionDTO.getConditionUIID(), 
			conditionDTO.getOrderID(),
			conditionDTO.getName(),
			conditionDTO.getDisplayName(),
			conditionDTO.getType(),
			conditionDTO.getStartValue(),
			conditionDTO.getEndValue(),
			conditionDTO.getExactMatchValue());
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
	 * The display name is a name shown to the user so they can link a condition to a branch. 
	 * @hibernate.property column="display_name" length="255"
	 */
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
            .append("displayName", displayName)
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
    	return new BranchCondition(null, conditionUIID, orderId, name, displayName, type, startValue, endValue, exactMatchValue);
    }

	public int compareTo(Object arg0) {
		BranchCondition other = (BranchCondition) arg0;
		return new CompareToBuilder()
			.append(orderId, other.getOrderId())
			.append(conditionId, other.getConditionId())
			.append(name, other.getName())
			.append(conditionUIID, other.getConditionUIID())
			.toComparison();
	}

	public int hashCode() {
		return new HashCodeBuilder()
			.append(orderId)
			.append(conditionId)
			.append(name)
			.append(conditionUIID)
			.toHashCode();
	}

	public boolean equals(Object arg0) {
		BranchCondition other = (BranchCondition) arg0;
		return new EqualsBuilder()
			.append(orderId, other.getOrderId())
			.append(conditionId, other.getConditionId())
			.append(name, other.getName())
			.append(conditionUIID, other.getConditionUIID())
			.isEquals();
	}
	
	/** Is this condition met? */
	public boolean isMet(ToolOutput output) {
		if ( output != null ) {
			if ( exactMatchValue != null ) {
				return exactMatchMet(output.getValue());
			} else if ( startValue != null ) {
				return inRange(output.getValue());
			}
		}
		return false;
	}
	
	public boolean exactMatchMet(ToolOutputValue outputValue) {
		if ( "OUTPUT_LONG".equals(type) ) {
			Long exactMatchObj = exactMatchValue != null ? convertToLong(exactMatchValue) : null;
			Long actualValue = outputValue.getLong();
			return ( actualValue != null && actualValue.equals(exactMatchObj)); 
		} else if ( "OUTPUT_DOUBLE".equals(type) ) {
			Double exactMatchObj = exactMatchValue != null ? Double.parseDouble(exactMatchValue) : null;
			Double actualValue = outputValue.getDouble();
			return ( actualValue != null && actualValue.equals(exactMatchObj)); 
		} else if ( "OUTPUT_BOOLEAN".equals(type) ) {
			Boolean exactMatchObj = exactMatchValue != null ? Boolean.parseBoolean(exactMatchValue) : null;
			Boolean actualValue = outputValue.getBoolean();
			return ( actualValue != null && actualValue.equals(exactMatchObj)); 
		} else if ( "OUTPUT_STRING".equals(type) ) {
			Double actualValue = outputValue.getDouble();
			return ( actualValue != null && actualValue.equals(exactMatchValue)); 
		} 
		return false;	
	}
	
	public boolean inRange(ToolOutputValue outputValue) {
		if ( "OUTPUT_LONG".equals(type) ) {
			Long startValueLong = startValue != null ? convertToLong(startValue) : null;
			Long endValueLong = endValue != null ? convertToLong(endValue) : null;
			Long actualValue = outputValue.getLong();
			return ( actualValue != null &&
				( startValueLong==null || actualValue.compareTo(startValueLong) >= 0 ) &&
				( endValueLong==null || actualValue.compareTo(endValueLong) <= 0 )) ;
		} else if ( "OUTPUT_DOUBLE".equals(type) ) {
			Double startValueDouble = startValue != null ? Double.parseDouble(startValue) : null;
			Double endValueDouble = endValue != null ? Double.parseDouble(endValue) : null;
			Double actualValue = outputValue.getDouble();
			return ( actualValue != null &&
				( startValueDouble==null || actualValue.compareTo(startValueDouble) >= 0 ) &&
				( endValueDouble==null || actualValue.compareTo(endValueDouble) <= 0 ));
		} else if ( "OUTPUT_BOOLEAN".equals(type) ) {
			// this is a nonsense, but we'll code it just in case. What order is a boolean? True greater than false?
			Boolean startValueBoolean = startValue != null ? Boolean.parseBoolean(startValue) : null;
			Boolean endValueBoolean = endValue != null ? Boolean.parseBoolean(endValue) : null;
			Boolean actualValue = outputValue.getBoolean();
			return ( actualValue != null &&
				( startValueBoolean==null || actualValue.compareTo(startValueBoolean) >= 0 ) &&
				( endValueBoolean==null || actualValue.compareTo(endValueBoolean) <= 0 ));
			
		} else if ( "OUTPUT_STRING".equals(type) ) {
			String actualValue = outputValue.getString();
			return ( actualValue != null &&
				( startValue==null || actualValue.compareTo(startValue) >= 0 ) &&
				( endValue==null || actualValue.compareTo(endValue) <= 0 ));
		} 
		return false;
	}

	/** The data may have come in from WDDX and have .0 on the end of Longs, so eliminate that */
	private Long convertToLong( String textValue ) {
		if ( textValue.length() == 0 )
			return null;

		int posPeriod = textValue.indexOf('.');
		if ( posPeriod > 0 ) {
			textValue = textValue.substring(0,posPeriod);
		}
		return new Long (textValue);
	}
}
