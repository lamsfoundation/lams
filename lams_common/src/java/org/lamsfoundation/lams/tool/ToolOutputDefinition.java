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
package org.lamsfoundation.lams.tool;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Each tool that has outputs will define a set of output definitions. Some definitions will be 
 * "predefined" for a tool, e.g. "Learner's Mark". Others may be created for a specific tool activity, 
 * via a Conditions/Output tab in monitoring, e.g. Second answer contains the word "Mercury".
 * <p>
 * If the tool contains generated definitions, then they must be copied when the tool content is copied, 
 * as the conditions may be modified via Live Edit. This must not modify the original design.
 * <p>
 * For 2.1, we will not deal with complex outputs, so for now we will not define how a complex definition 
 * is defined. The field is placed in the object so that we have the place for it when we do design the 
 * complex output definitions.
 * <p>
 * Sample ToolOutputDefinition:
 * ToolOutputDefinition {
 *   name = "LEARNERS_MARK",
 *   description = "Mark for an individual learner";
 *   type = "NUMERIC";
 *   startValue = "0.0";
 *   endValue = "10.0";
 *   complexDefinition = null;
 *   }
 */
public class ToolOutputDefinition {
	
    private String name;
    private String description;
    private OutputType type;
    private Object startValue;
    private Object endValue;
    private Object complexDefinition;
    
    /** Name must be unique within the current tool content. This will be used to identify the output. 
     * If the definition is a predefined definition then the name will always be the same (e.g. LEARNER_MARK) but 
     * if it is defined in authoring then it will need to made unique for this tool content (e.g. ANSWER_2_CONTAINS_1). 
     * At lesson time, the tool will be given back the name and will need to be able to uniquely identify the required 
     * output based on name, the tool session id and possibly the learner's user id.
     */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/** Description: Description is an internationalised text string which is displayed to the 
	 * user as the output "name". It is the responsibility of the tool to internationalise the 
	 * string. We suggest that the key for each predefined definition follow the convention 
	 * OUTPUT_DESC_<output name> 
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/** 
	 * The type of the output value.  
	 */
	public OutputType getType() {
		return type;
	}
	public void setType(OutputType type) {
		this.type = type;
	}
	/** If the output value may be compared to a range, then startValue and endValue are the inclusive start values and end values 
	 * for the range. This may be used to customise fixed definitions to ranges appropriate for the current data.
	 */
	public Object getStartValue() {
		return startValue;
	}
	public void setStartValue(Object startValue) {
		this.startValue = startValue;
	}
	/** See getStartValue() */
	public Object getEndValue() {
		return endValue;
	}
	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}
	public Object getComplexDefinition() {
		return complexDefinition;
	}
	public void setComplexDefinition(Object complexDefinition) {
		this.complexDefinition = complexDefinition;
	}
    
	public String toString() {
    	return new ToStringBuilder(this)
            .append("name", name)
            .append("description", description)
            .append("type", type)
            .append("startValue", startValue)
            .append("endValue", endValue)
        .toString();
	}

	public boolean equals(Object other) {
	    if ( (this == other ) ) return true;
	    if ( !(other instanceof ToolOutputDefinition) ) return false;
	    ToolOutputDefinition castOther = (ToolOutputDefinition) other;
	    return new EqualsBuilder()
	        .append(this.name, castOther.name)
	        .append(this.description, castOther.description)
	        .append(this.type, castOther.type)
	        .append(this.startValue, castOther.startValue)
	        .append(this.endValue, castOther.endValue)
	        .isEquals();
	}

	public int hashCode() {
	    return new HashCodeBuilder()
	        .append(name)
	        .append(description)
	        .append(type)
	        .append(startValue)
	        .append(endValue)
	        .toHashCode();
	}


}
