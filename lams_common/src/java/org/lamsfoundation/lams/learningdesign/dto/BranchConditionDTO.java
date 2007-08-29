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
package org.lamsfoundation.lams.learningdesign.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.BranchCondition;


/**
 * The BranchConditionDTO records the details of one particular condition, used for tool output based branching.
 * On one branching activity there will be one or more conditions, some with the same name. If the condition is a 
 * range condition then startValue and endValue will be populated. If it is an exactMatchCondition (such as for a 
 * boolean output definition) then the exactMatchValue will be true/false. Order id is used to ensure that the 
 * conditions are always listed in the same order on the screen.
 */
public class BranchConditionDTO {

	private Long conditionId;
	private Integer conditionUIID;
    private Integer orderId; 
    private String name; 
    private String type; 
    private String startValue; 
    private String endValue; 
    private String exactMatchValue;
    
    public BranchConditionDTO( BranchCondition condition ) {
    	this.conditionId = condition.getConditionId();
    	this.conditionUIID = condition.getConditionUIID();
    	this.orderId = condition.getOrderId();
    	this.name = condition.getName();
    	this.type = condition.getType();
    	this.startValue = condition.getStartValue();
    	this.endValue = condition.getEndValue();
    	this.exactMatchValue = condition.getExactMatchValue();
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
 
}
