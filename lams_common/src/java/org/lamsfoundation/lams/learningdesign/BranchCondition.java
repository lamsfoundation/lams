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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputValue;

/**
 * The ToolOutputBranchActivityEntries record the branch selection details for tool output based branching. During
 * authoring, the tool will have supplied a ToolOutputDefinition object and this will have been used to configure which
 * tool outputs match to a particular branch. This mapping is as a ToolOutputBranchActivityEntry.
 *
 * There should be one branch condition for each ToolOutputBranchActivityEntry.
 */
@Entity
@Table(name = "lams_branch_condition")
@Inheritance(strategy = InheritanceType.JOINED)
public class BranchCondition implements Comparable<BranchCondition> {

    public static final String OUTPUT_TYPE_STRING = "OUTPUT_STRING";

    public static final String OUTPUT_TYPE_BOOLEAN = "OUTPUT_BOOLEAN";

    public static final String OUTPUT_TYPE_DOUBLE = "OUTPUT_DOUBLE";

    public static final String OUTPUT_TYPE_LONG = "OUTPUT_LONG";

    public static final String OUTPUT_TYPE_COMPLEX = "OUTPUT_COMPLEX";

    private static Logger log = Logger.getLogger(BranchCondition.class);

    @Id
    @Column(name = "condition_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long conditionId;

    @Column(name = "condition_ui_id")
    protected Integer conditionUIID;

    @Column(name = "order_id")
    protected Integer orderId;

    @Column
    protected String name;

    @Column(name = "display_name")
    protected String displayName;

    @Column
    protected String type;

    @Column(name = "start_value")
    protected String startValue;

    @Column(name = "end_value")
    protected String endValue;

    @Column(name = "exact_match_value")
    protected String exactMatchValue;

    /** default constructor */
    public BranchCondition() {
    }

    /** full constructor */
    public BranchCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String type, String startValue, String endValue, String exactMatchValue) {
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
	this(conditionDTO.getConditionId(), conditionDTO.getConditionUIID(), conditionDTO.getOrderID(),
		conditionDTO.getName(), conditionDTO.getDisplayName(), conditionDTO.getType(),
		conditionDTO.getStartValue(), conditionDTO.getEndValue(), conditionDTO.getExactMatchValue());
    }

    public Long getConditionId() {
	return conditionId;
    }

    public void setConditionId(Long conditionId) {
	this.conditionId = conditionId;
    }

    public Integer getConditionUIID() {
	return conditionUIID;
    }

    public void setConditionUIID(Integer conditionUIID) {
	this.conditionUIID = conditionUIID;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getStartValue() {
	return startValue;
    }

    public void setStartValue(String startValue) {
	this.startValue = startValue;
    }

    public String getEndValue() {
	return endValue;
    }

    public void setEndValue(String endValue) {
	this.endValue = endValue;
    }

    public String getExactMatchValue() {
	return exactMatchValue;
    }

    public void setExactMatchValue(String exactMatchValue) {
	this.exactMatchValue = exactMatchValue;
    }

    public BranchConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new BranchConditionDTO(this, toolActivityUIID);
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("conditionId", conditionId).append("conditionUIID", conditionUIID)
		.append("orderId", orderId).append("name", name).append("displayName", displayName).append("type", type)
		.append("startValue", startValue).append("endValue", endValue)
		.append("exactMatchValue", exactMatchValue).toString();
    }

    /**
     * Allocate this condition to the given branch, in a branching activity or as a gate opening condition in condition
     * gate. This creates the BranchActivityEntry record and adds it to the branchActivities set. EntryUIID will only be
     * populated if this is called from authoring
     */
    public BranchActivityEntry allocateBranchToCondition(Integer entryUIID, SequenceActivity branch,
	    Activity outputBasedActivity, Boolean gateOpenWhenConditionMet) {
	BranchActivityEntry entry = new BranchActivityEntry(null, entryUIID, branch, outputBasedActivity, this,
		gateOpenWhenConditionMet);
	return entry;
    }

    /** Create a new BranchCondition based on itself, leaving conditionId as null */
    public BranchCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	return new BranchCondition(null, newConditionUIID, orderId, name, displayName, type, startValue, endValue,
		exactMatchValue);
    }

    @Override
    public Object clone() {
	return new BranchCondition(null, null, orderId, name, displayName, type, startValue, endValue, exactMatchValue);
    }

    @Override
    public int compareTo(BranchCondition other) {
	return new CompareToBuilder().append(orderId, other.getOrderId()).append(conditionId, other.getConditionId())
		.append(name, other.getName()).append(conditionUIID, other.getConditionUIID()).toComparison();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(orderId).append(conditionId).append(name).append(conditionUIID)
		.toHashCode();
    }

    @Override
    public boolean equals(Object arg0) {
	BranchCondition other = (BranchCondition) arg0;
	return new EqualsBuilder().append(orderId, other.getOrderId()).append(conditionId, other.getConditionId())
		.append(name, other.getName()).append(conditionUIID, other.getConditionUIID()).isEquals();
    }

    /** Is this condition met? */
    public boolean isMet(ToolOutput output) {
	ToolOutputValue value = output == null ? null : output.getValue();
	if (value != null && OutputType.OUTPUT_COMPLEX != value.getType()
		&& OutputType.OUTPUT_SET_BOOLEAN != value.getType()) {
	    if (exactMatchValue != null) {
		return exactMatchMet(value);
	    } else if (startValue != null || endValue != null) {
		return inRange(value);
	    }
	}
	return false;
    }

    public boolean exactMatchMet(ToolOutputValue outputValue) {
	if (BranchCondition.OUTPUT_TYPE_LONG.equals(type)) {
	    Long exactMatchObj = exactMatchValue != null ? convertToLong(exactMatchValue) : null;
	    Long actualValue = outputValue.getLong();
	    return actualValue != null && actualValue.equals(exactMatchObj);
	} else if (BranchCondition.OUTPUT_TYPE_DOUBLE.equals(type)) {
	    Double exactMatchObj = exactMatchValue != null ? Double.parseDouble(exactMatchValue) : null;
	    Double actualValue = outputValue.getDouble();
	    return actualValue != null && actualValue.equals(exactMatchObj);
	} else if (BranchCondition.OUTPUT_TYPE_BOOLEAN.equals(type)) {
	    Boolean exactMatchObj = exactMatchValue != null ? Boolean.parseBoolean(exactMatchValue) : null;
	    Boolean actualValue = outputValue.getBoolean();
	    return actualValue != null && actualValue.equals(exactMatchObj);
	} else if (BranchCondition.OUTPUT_TYPE_STRING.equals(type)) {
	    Double actualValue = outputValue.getDouble();
	    return actualValue != null && actualValue.equals(exactMatchValue);
	}
	return false;
    }

    public boolean inRange(ToolOutputValue outputValue) {
	if (BranchCondition.OUTPUT_TYPE_LONG.equals(type)) {
	    Long startValueLong = startValue != null ? convertToLong(startValue) : null;
	    Long endValueLong = endValue != null ? convertToLong(endValue) : null;
	    Long actualValue = outputValue.getLong();
	    return actualValue != null && (startValueLong == null || actualValue.compareTo(startValueLong) >= 0)
		    && (endValueLong == null || actualValue.compareTo(endValueLong) <= 0);
	} else if (BranchCondition.OUTPUT_TYPE_DOUBLE.equals(type)) {
	    Double startValueDouble = startValue != null ? Double.parseDouble(startValue) : null;
	    Double endValueDouble = endValue != null ? Double.parseDouble(endValue) : null;
	    Double actualValue = outputValue.getDouble();
	    return actualValue != null && (startValueDouble == null || actualValue.compareTo(startValueDouble) >= 0)
		    && (endValueDouble == null || actualValue.compareTo(endValueDouble) <= 0);
	} else if (BranchCondition.OUTPUT_TYPE_BOOLEAN.equals(type)) {
	    // this is a nonsense, but we'll code it just in case. What order is a boolean? True greater than false?
	    Boolean startValueBoolean = startValue != null ? Boolean.parseBoolean(startValue) : null;
	    Boolean endValueBoolean = endValue != null ? Boolean.parseBoolean(endValue) : null;
	    Boolean actualValue = outputValue.getBoolean();
	    return actualValue != null && (startValueBoolean == null || actualValue.compareTo(startValueBoolean) >= 0)
		    && (endValueBoolean == null || actualValue.compareTo(endValueBoolean) <= 0);

	} else if (BranchCondition.OUTPUT_TYPE_STRING.equals(type)) {
	    String actualValue = outputValue.getString();
	    return actualValue != null && (startValue == null || actualValue.compareTo(startValue) >= 0)
		    && (endValue == null || actualValue.compareTo(endValue) <= 0);
	}
	return false;
    }

    /** The data may have come in from WDDX and have .0 on the end of Longs, so eliminate that */
    private Long convertToLong(String textValue) {
	if (textValue.length() == 0) {
	    return null;
	}

	int posPeriod = textValue.indexOf('.');
	if (posPeriod > 0) {
	    textValue = textValue.substring(0, posPeriod);
	}
	return Long.valueOf(textValue);
    }

    /**
     * All conditions must have either (a) an exact match value or (b) a start value and no end value or (c) start value
     * and an end value and the end value must be >= start value.
     */
    protected boolean isValid() {
	if (exactMatchValue != null) {
	    try {
		if (getTypedValue(exactMatchValue) != null) {
		    return true;
		}
	    } catch (Exception e) {
	    }
	    BranchCondition.log.error("Condition contains an unconvertible value for exactMatchValue. Type is " + type
		    + " value " + exactMatchValue);
	    return false;
	} else {
	    Comparable typedStartValue = null;
	    Comparable typedEndValue = null;

	    try {
		if (startValue != null) {
		    typedStartValue = getTypedValue(startValue);
		}
	    } catch (Exception e) {
		BranchCondition.log.error("Condition contains an unconvertible value for startValue. Type is " + type
			+ " value " + startValue);
		return false;
	    }

	    try {
		if (endValue != null) {
		    typedEndValue = getTypedValue(endValue);
		}
	    } catch (Exception e) {
		BranchCondition.log.error("Condition contains an unconvertible value for endValue. Type is " + type
			+ " value " + endValue);
		return false;
	    }

	    if (typedStartValue == null && typedEndValue != null) {
		return true;
	    } else if (typedEndValue == null || typedEndValue.compareTo(typedStartValue) >= 0) {
		return true;
	    }
	}
	return false;
    }

    private Comparable<?> getTypedValue(String untypedValue) {
	if (BranchCondition.OUTPUT_TYPE_LONG.equals(type)) {
	    return convertToLong(untypedValue);
	} else if (BranchCondition.OUTPUT_TYPE_DOUBLE.equals(type)) {
	    return Double.parseDouble(untypedValue);
	} else if (BranchCondition.OUTPUT_TYPE_BOOLEAN.equals(type)) {
	    return Boolean.parseBoolean(untypedValue);
	} else if (BranchCondition.OUTPUT_TYPE_STRING.equals(type)) {
	    return untypedValue;
	} else {
	    return null;
	}
    }
}