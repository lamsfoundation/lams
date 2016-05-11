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

package org.lamsfoundation.lams.learningdesign;

import java.util.Set;

/**
 * Grouping formed by learner's choice
 *
 * @author Marcin Cieslak
 */
public class LearnerChoiceGrouping extends Grouping {
    /** nullable persistent field */
    private Boolean equalNumberOfLearnersPerGroup;

    /** nullable persistent field */
    private Integer numberOfGroups;

    /** nullable persistent field */
    private Integer learnersPerGroup;

    /** nullable persistent field */
    private Boolean viewStudentsBeforeSelection;

    /** Creates a new instance of ChosenGrouping */
    public LearnerChoiceGrouping() {
	super.grouper = new LearnerChoiceGrouper();
    }

    /** full constructor */
    public LearnerChoiceGrouping(Long groupingId, Set groups, Set activities) {
	super(groupingId, groups, activities, new LearnerChoiceGrouper());
    }

    /**
     * This method creates a deep copy of the Grouping
     *
     * @return LearnerChoiceGrouping The deep copied Grouping object
     */
    @Override
    public Grouping createCopy(int uiidOffset) {
	LearnerChoiceGrouping learnerChoiceGrouping = new LearnerChoiceGrouping();
	copyGroupingFields(learnerChoiceGrouping, uiidOffset);
	learnerChoiceGrouping.setEqualNumberOfLearnersPerGroup(getEqualNumberOfLearnersPerGroup());
	learnerChoiceGrouping.setLearnersPerGroup(getLearnersPerGroup());
	learnerChoiceGrouping.setNumberOfGroups(getNumberOfGroups());
	learnerChoiceGrouping.setViewStudentsBeforeSelection(getViewStudentsBeforeSelection());
	return learnerChoiceGrouping;
    }

    /**
     * This type of grouping doesn't have groups other than learner groups. So it always return <code>true</code>.
     *
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     */
    @Override
    public boolean isLearnerGroup(Group group) {
	return true;
    }

    public Boolean getEqualNumberOfLearnersPerGroup() {
	return equalNumberOfLearnersPerGroup;
    }

    public void setEqualNumberOfLearnersPerGroup(Boolean equalNumberOfLearnersPerGroup) {
	this.equalNumberOfLearnersPerGroup = equalNumberOfLearnersPerGroup;
    }

    public Integer getLearnersPerGroup() {
	return learnersPerGroup;
    }

    public void setLearnersPerGroup(Integer learnersPerGroup) {
	this.learnersPerGroup = learnersPerGroup;
    }

    public Integer getNumberOfGroups() {
	return numberOfGroups;
    }

    public void setNumberOfGroups(Integer numberOfGroups) {
	this.numberOfGroups = numberOfGroups;
    }

    public Boolean getViewStudentsBeforeSelection() {
	return viewStudentsBeforeSelection;
    }

    public void setViewStudentsBeforeSelection(Boolean viewStudentsBeforeSelection) {
	this.viewStudentsBeforeSelection = viewStudentsBeforeSelection;
    }
}