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

package org.lamsfoundation.lams.tool;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.BranchCondition;

import java.util.List;

/**
 * Each tool that has outputs will define a set of output definitions. Some definitions will be "predefined" for a tool,
 * e.g. "Learner's Mark". Others may be created for a specific tool activity, via a Conditions/Output tab in monitoring,
 * e.g. Second answer contains the word "Mercury".
 * <p>
 * If the tool contains generated definitions, then they must be copied when the tool content is copied, as the
 * conditions may be modified via Live Edit. This must not modify the original design.
 * <p>
 * For 2.1, we will not deal with complex outputs, so for now we will not define how a complex definition is defined.
 * The field is placed in the object so that we have the place for it when we do design the complex output definitions.
 * <p>
 * Sample ToolOutputDefinition: ToolOutputDefinition { name = "LEARNERS_MARK", description = "Mark for an individual
 * learner"; type = "NUMERIC"; startValue = "0.0"; endValue = "10.0"; complexDefinition = null; }
 */
public class ToolOutputDefinition implements Comparable<ToolOutputDefinition> {
    /*
     * Definition Type indicates what kind of definitions should be provided. Some outputs are not valid for conditions,
     * some are not valid for data flow between tools.
     */
    public static final int DATA_OUTPUT_DEFINITION_TYPE_CONDITION = 1;

    public static final int DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW = 2;

    private String name;
    private String description;
    private OutputType type;
    private Boolean weightable = false;
    private Object startValue;
    private Object endValue;
    private Object complexDefinition;
    private Boolean showConditionNameOnly;
    private Boolean isDefaultGradebookMark;
    // whether if changing this output will impact other learners' outputs, for example in Peer Review
    private Boolean impactsOtherLearners = false;
    private List<BranchCondition> conditions;
    // we need it to filter definitions which other tools can not process; it must be set in the definition, otherwise
    // unsupported values can be passed to the receiving end of the data flow; most methods for creating Output
    // Definitions set the class already, but it must be set manually for complex definitions
    private Class valueClass;

    /**
     * Name must be unique within the current tool content. This will be used to identify the output. If the definition
     * is a predefined definition then the name will always be the same (e.g. LEARNER_MARK) but if it is defined in
     * authoring then it will need to made unique for this tool content (e.g. ANSWER_2_CONTAINS_1). At lesson time, the
     * tool will be given back the name and will need to be able to uniquely identify the required output based on name,
     * the tool session id and possibly the learner's user id.
     */
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * Description: Description is an internationalised text string which is displayed to the user as the output "name".
     * It is the responsibility of the tool to internationalise the string. We suggest that the key for each predefined
     * definition follow the convention OUTPUT_DESC_<output name>
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

    /**
     * Can this output be used in weighting, i.e. does it have "max score" and % of this score can be calculated
     */
    public Boolean getWeightable() {
	return weightable;
    }

    public void setWeightable(Boolean weightable) {
	this.weightable = weightable;
    }

    /**
     * If the output value may be compared to a range, then startValue and endValue are the inclusive start values and
     * end values for the range. This may be used to customise fixed definitions to ranges appropriate for the current
     * data.
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

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("name", name).append("description", description).append("type", type)
		.append("startValue", startValue).append("endValue", endValue).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof ToolOutputDefinition)) {
	    return false;
	}
	ToolOutputDefinition castOther = (ToolOutputDefinition) other;
	return new EqualsBuilder().append(name, castOther.name).append(type, castOther.type).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(name).append(type).toHashCode();
    }

    @Override
    public int compareTo(ToolOutputDefinition o) {
	return new CompareToBuilder().append(name, o.name).append(type, o.type).toComparison();
    }

    public List<BranchCondition> getConditions() {
	return conditions;
    }

    public void setConditions(List<BranchCondition> conditions) {
	this.conditions = conditions;
    }

    /**
     * Should UI show the definition of the branch conditions (e.g. Range from blah to blah) or just the name of the
     * condition. Set to true if the definition relates to an internal parameter and will mean nothing to the user
     */
    public Boolean isShowConditionNameOnly() {
	return showConditionNameOnly;
    }

    public void setShowConditionNameOnly(Boolean showConditionNameOnly) {
	this.showConditionNameOnly = showConditionNameOnly;
    }

    /**
     * If set, this flag makes the current tool output definition the default output to go straight to gradebook marks
     * when the user completes an activity. There should only be one of these per tool.
     *
     * @return
     */
    public Boolean isDefaultGradebookMark() {
	return isDefaultGradebookMark;
    }

    public void setIsDefaultGradebookMark(Boolean isDefaultGradebookMark) {
	this.isDefaultGradebookMark = isDefaultGradebookMark;
    }

    public Boolean getImpactsOtherLearners() {
	return impactsOtherLearners;
    }

    public void setImpactsOtherLearners(Boolean impactsOtherLearners) {
	this.impactsOtherLearners = impactsOtherLearners;
    }

    public Class getValueClass() {
	return valueClass;
    }

    public void setValueClass(Class valueClass) {
	this.valueClass = valueClass;
    }

}