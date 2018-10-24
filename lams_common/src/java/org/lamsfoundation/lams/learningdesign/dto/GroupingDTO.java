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

package org.lamsfoundation.lams.learningdesign.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.lesson.LessonClass;

/**
 * @author Manpreet Minhas
 */
public class GroupingDTO extends BaseDTO {

    private Long groupingID;
    private Integer groupingUIID;
    private Integer groupingTypeID;
    private Integer numberOfGroups;
    private Integer learnersPerGroup;
    private Long staffGroupID;
    private Integer maxNumberOfGroups;
    private Boolean equalNumberOfLearnersPerGroup;
    private Boolean viewStudentsBeforeSelection;
    // list of GroupDTO
    private List groups;

    public GroupingDTO() {

    }

    public GroupingDTO(Long groupingID, Integer groupingUIID, Integer groupingType, Integer numberOfGroups,
	    Integer learnersPerGroup, Long staffGroupID, Integer maxNumberOfGroups,
	    Boolean equalNumberOfLearnersPerGroup, Boolean viewStudentsBeforeSelection, List groupDTOs) {
	this.groupingID = groupingID;
	this.groupingUIID = groupingUIID;
	groupingTypeID = groupingType;
	this.numberOfGroups = numberOfGroups;
	this.learnersPerGroup = learnersPerGroup;
	this.staffGroupID = staffGroupID;
	this.maxNumberOfGroups = maxNumberOfGroups;
	this.equalNumberOfLearnersPerGroup = equalNumberOfLearnersPerGroup;
	this.viewStudentsBeforeSelection = viewStudentsBeforeSelection;
	groups = groupDTOs;
    }

    public GroupingDTO(Grouping grouping, boolean setupUserList) {
	groupingID = grouping.getGroupingId();
	groupingUIID = grouping.getGroupingUIID();
	maxNumberOfGroups = grouping.getMaxNumberOfGroups();
	groupingTypeID = grouping.getGroupingTypeId();
	Set groupSet = grouping.getGroups();
	groups = new ArrayList();
	if (groupSet != null) {
	    Iterator iter = groupSet.iterator();
	    while (iter.hasNext()) {
		groups.add(((Group) iter.next()).getGroupDTO(setupUserList));
	    }
	}
	/*
	 * The two lines of code below are commented out, because it creates a new grouping instance and then tries to
	 * get the attributes for it , in which the values are null. So the grouping object is passed straight into the
	 * processGroupingActivity() function.
	 */
	// Object object = Grouping.getGroupingInstance(groupingTypeID);
	// processGroupingActivity(object);
	processGroupingActivity(grouping);
    }

    public void processGroupingActivity(Object object) {
	if (object instanceof RandomGrouping) {
	    addRandomGroupingAttributes((RandomGrouping) object);
	} else if (object instanceof ChosenGrouping) {
	    addChosenGroupingAttributes((ChosenGrouping) object);
	} else if (object instanceof LearnerChoiceGrouping) {
	    addLearnerChoiceGroupingAttributes((LearnerChoiceGrouping) object);
	} else {
	    addLessonClassAttributes((LessonClass) object);
	}
    }

    private void addRandomGroupingAttributes(RandomGrouping grouping) {
	learnersPerGroup = grouping.getLearnersPerGroup();
	numberOfGroups = grouping.getNumberOfGroups();
    }

    private void addChosenGroupingAttributes(ChosenGrouping grouping) {

    }

    private void addLearnerChoiceGroupingAttributes(LearnerChoiceGrouping grouping) {
	learnersPerGroup = grouping.getLearnersPerGroup();
	numberOfGroups = grouping.getNumberOfGroups();
	equalNumberOfLearnersPerGroup = grouping.getEqualNumberOfLearnersPerGroup();
	viewStudentsBeforeSelection = grouping.getViewStudentsBeforeSelection();
    }

    private void addLessonClassAttributes(LessonClass grouping) {
	staffGroupID = grouping.getStaffGroup().getGroupId();
    }

    /**
     * @return Returns the groupingID.
     */
    public Long getGroupingID() {
	return groupingID;
    }

    /**
     * @param groupingID
     *            The groupingID to set.
     */
    public void setGroupingID(Long groupingID) {
	this.groupingID = groupingID;
    }

    /**
     * @return Returns the groupingType.
     */
    public Integer getGroupingTypeID() {
	return groupingTypeID;
    }

    /**
     * @param groupingType
     *            The groupingType to set.
     */
    public void setGroupingTypeID(Integer groupingType) {
	groupingTypeID = groupingType;
    }

    /**
     * @return Returns the groupingUIID.
     */
    public Integer getGroupingUIID() {
	return groupingUIID;
    }

    /**
     * @param groupingUIID
     *            The groupingUIID to set.
     */
    public void setGroupingUIID(Integer groupingUIID) {
	this.groupingUIID = groupingUIID;
    }

    /**
     * @return Returns the learnersPerGroup.
     */
    public Integer getLearnersPerGroup() {
	return learnersPerGroup;
    }

    /**
     * @param learnersPerGroup
     *            The learnersPerGroup to set.
     */
    public void setLearnersPerGroup(Integer learnersPerGroup) {
	this.learnersPerGroup = learnersPerGroup;
    }

    /**
     * @return Returns the maxNumberOfGroups.
     */
    public Integer getMaxNumberOfGroups() {
	return maxNumberOfGroups;
    }

    /**
     * @param maxNumberOfGroups
     *            The maxNumberOfGroups to set.
     */
    public void setMaxNumberOfGroups(Integer maxNumberOfGroups) {
	this.maxNumberOfGroups = maxNumberOfGroups;
    }

    /**
     * @return Returns the numberOfGroups.
     */
    public Integer getNumberOfGroups() {
	return numberOfGroups;
    }

    /**
     * @param numberOfGroups
     *            The numberOfGroups to set.
     */
    public void setNumberOfGroups(Integer numberOfGroups) {
	this.numberOfGroups = numberOfGroups;
    }

    /**
     * @return Returns the staffGroupID.
     */
    public Long getStaffGroupID() {
	return staffGroupID;
    }

    /**
     * @param staffGroupID
     *            The staffGroupID to set.
     */
    public void setStaffGroupID(Long staffGroupID) {
	this.staffGroupID = staffGroupID;
    }

    /**
     *
     * @return group list belongs to this grouping.
     */
    public List getGroups() {
	return groups;
    }

    public void setGroups(List groups) {
	this.groups = groups;
    }

    public Boolean getEqualNumberOfLearnersPerGroup() {
	return equalNumberOfLearnersPerGroup;
    }

    public void setEqualNumberOfLearnersPerGroup(Boolean equalNumberOfLearnersPerGroup) {
	this.equalNumberOfLearnersPerGroup = equalNumberOfLearnersPerGroup;
    }

    public Boolean getViewStudentsBeforeSelection() {
	return viewStudentsBeforeSelection;
    }

    public void setViewStudentsBeforeSelection(Boolean viewStudentsBeforeSelection) {
	this.viewStudentsBeforeSelection = viewStudentsBeforeSelection;
    }
}
