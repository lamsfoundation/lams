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
package org.lamsfoundation.lams.tool.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;

/**
 * @author Mitchell Seaton
 * 
 * This class acts as a data transfer object for
 * transferring information between FLASH and the core module. 
 * 
 * This class is required in the authoring environment to pass
 * information about the ToolOutput for a ToolActivity 
 */
public class ToolOutputDefinitionDTO {
	
	private String name;
	private String description;
	private String type;
	private String startValue;
	private String endValue;
	private String complexDefinition;
	
	public ToolOutputDefinitionDTO(String name, String description, String type, String startValue, String endValue, String complexDefinition) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;

		this.startValue = startValue;
		this.endValue = endValue;
		if ( startValue != null && endValue == null ) {
			endValue = BranchConditionDTO.MAX_FOR_FLASH;
		}

		this.complexDefinition = complexDefinition;
	}
	
	public ToolOutputDefinitionDTO(ToolOutputDefinition definition) {
		super();
		this.name = definition.getName();
		this.description = definition.getDescription();
		this.type = (definition.getType() != null)? definition.getType().toString() : null;

		this.startValue = (definition.getStartValue() != null) ? definition.getStartValue().toString() : null;
		this.endValue = (definition.getEndValue() != null) ? definition.getEndValue().toString() : null;
		if ( startValue != null && endValue == null ) {
			endValue = BranchConditionDTO.MAX_FOR_FLASH;
		}

		this.complexDefinition = (definition.getComplexDefinition() != null) ? definition.getComplexDefinition().toString() : null;
	}
	
	/**
	 * Returns the name.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the description.
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the output type.
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns the start value.
	 * @return
	 */
	public String getStartValue() {
		return startValue;
	}
	
	/**
	 * Returns the end value.
	 * @return
	 */
	public String getEndValue() {
		return endValue;
	}
	
	/**
	 * Returns the complex definition.
	 * @return
	 */
	public String getComplexDefinition() {
		return complexDefinition;
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
}
