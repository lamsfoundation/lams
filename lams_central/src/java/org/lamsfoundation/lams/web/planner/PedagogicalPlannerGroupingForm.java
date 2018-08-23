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

package org.lamsfoundation.lams.web.planner;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Form for grouping activities in Pedagogical Planner.
 *
 * @author Marcin Cieslak
 *
 */
public class PedagogicalPlannerGroupingForm extends PedagogicalPlannerActivitySpringForm {
    private Integer groupingTypeId;
    private String numberOfGroups;
    private String learnersPerGroup;
    private Boolean equalGroupSizes;
    private Boolean viewStudentsBeforeSelection;

    @Autowired
    MessageService messageService;

    public Integer getGroupingTypeId() {
	return groupingTypeId;
    }

    public void setGroupingTypeId(Integer groupingTypeId) {
	this.groupingTypeId = groupingTypeId;
    }

    public String getNumberOfGroups() {
	return numberOfGroups;
    }

    public void setNumberOfGroups(String numberOfGroups) {
	this.numberOfGroups = numberOfGroups;
    }

    /**
     * Fills the form properties with existing grouping object.
     *
     * @param grouping
     *            persistent object to use
     */
    public void fillForm(Grouping grouping) {
	if (grouping != null) {
	    setGroupingTypeId(grouping.getGroupingTypeId());
	    if (grouping.isRandomGrouping()) {
		RandomGrouping randomGrouping = (RandomGrouping) grouping;
		String number = randomGrouping.getNumberOfGroups() == null ? null
			: String.valueOf(randomGrouping.getNumberOfGroups());
		setNumberOfGroups(number);

		number = randomGrouping.getLearnersPerGroup() == null ? null
			: String.valueOf(randomGrouping.getLearnersPerGroup());
		setLearnersPerGroup(number);
	    } else if (grouping.isLearnerChoiceGrouping()) {
		LearnerChoiceGrouping learnerChoiceGrouping = (LearnerChoiceGrouping) grouping;
		String number = learnerChoiceGrouping.getNumberOfGroups() == null ? null
			: String.valueOf(learnerChoiceGrouping.getNumberOfGroups());
		setNumberOfGroups(number);

		number = learnerChoiceGrouping.getLearnersPerGroup() == null ? null
			: String.valueOf(learnerChoiceGrouping.getLearnersPerGroup());
		setLearnersPerGroup(number);

		setEqualGroupSizes(learnerChoiceGrouping.getEqualNumberOfLearnersPerGroup());
		setViewStudentsBeforeSelection(learnerChoiceGrouping.getViewStudentsBeforeSelection());
	    } else {
		String numberOfGroups = grouping.getMaxNumberOfGroups() == null ? null
			: String.valueOf(grouping.getMaxNumberOfGroups());
		setNumberOfGroups(numberOfGroups);
	    }
	}
    }

    /**
     * Checks if the provided group/learner number is a nonnegative integer.
     */

    public MultiValueMap<String, String> validate() {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	boolean valid = true;
	boolean numberValid = true;

	String numberToParse = StringUtils.isEmpty(getNumberOfGroups()) ? getLearnersPerGroup() : getNumberOfGroups();
	try {
	    int number = Integer.parseInt(numberToParse);
	    if (number < 0) {
		numberValid = false;
	    }
	} catch (Exception e) {
	    numberValid = false;
	}
	if (!numberValid) {
	    errorMap.add("Global", messageService.getMessage("error.planner.grouping.number.integer"));
	    valid = false;
	}
	setValid(valid);
	return errorMap;
    }

    public String getLearnersPerGroup() {
	return learnersPerGroup;
    }

    public void setLearnersPerGroup(String numberOfLearners) {
	learnersPerGroup = numberOfLearners;
    }

    public Boolean getEqualGroupSizes() {
	return equalGroupSizes;
    }

    public void setEqualGroupSizes(Boolean equalGroupsSizes) {
	equalGroupSizes = equalGroupsSizes;
    }

    public Boolean getViewStudentsBeforeSelection() {
	return viewStudentsBeforeSelection;
    }

    public void setViewStudentsBeforeSelection(Boolean viewStudentsBeforeSelection) {
	this.viewStudentsBeforeSelection = viewStudentsBeforeSelection;
    }
}