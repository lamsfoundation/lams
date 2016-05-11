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


package org.lamsfoundation.lams.learningdesign.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.BranchCondition;

/**
 * The BranchConditionDTO records the details of one particular condition, used for tool output based branching. On one
 * branching activity there will be one or more conditions, some with the same name. If the condition is a range
 * condition then startValue and endValue will be populated. If it is an exactMatchCondition (such as for a boolean
 * output definition) then the exactMatchValue will be true/false. Order id is used to ensure that the conditions are
 * always listed in the same order on the screen.
 */
public class BranchConditionDTO {

    private Long conditionId;
    private Integer conditionUIID;
    private Integer orderID;
    private String name;
    private String displayName;
    private String type;
    private String startValue;
    private String endValue;
    private String exactMatchValue;
    private Integer toolActivityUIID;

    public BranchConditionDTO(BranchCondition condition, Integer toolActivityUIID) {
	setConditionId(condition.getConditionId());
	setConditionUIID(condition.getConditionUIID());
	setOrderID(condition.getOrderId());
	setName(condition.getName());
	setDisplayName(condition.getDisplayName());
	setType(condition.getType());
	setStartValue(condition.getStartValue());
	setEndValue(condition.getEndValue());
	setExactMatchValue(condition.getExactMatchValue());
	setToolActivityUIID(toolActivityUIID);
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

    public Integer getOrderID() {
	return orderID;
    }

    public void setOrderID(Integer orderId) {
	orderID = orderId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("conditionId", conditionId).append("conditionUIID", conditionUIID)
		.append("orderId", orderID).append("name", name).append("type", type).append("startValue", startValue)
		.append("endValue", endValue).append("exactMatchValue", exactMatchValue).toString();
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    /**
     * The UIID of the tool activity that will be queried for the tool output. This should match the output field in the
     * appropriate branching activity. It is here to make life easier on the Authoring side - so that it has the UIID
     * somewhere if the user changes from a tool based branch -> group/teacher based and back to tool based without
     * saving.
     *
     * @return toolActivityUIID
     */
    public Integer getToolActivityUIID() {
	return toolActivityUIID;
    }

    public void setToolActivityUIID(Integer toolActivityUIID) {
	this.toolActivityUIID = toolActivityUIID;
    }

    public BranchCondition getCondition() {
	return new BranchCondition(this);
    }
}