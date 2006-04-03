/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class GroupingDTO extends BaseDTO{
	
	private Long groupingID;
	private Integer groupingUIID;
	private Integer groupingTypeID;
	private Integer numberOfGroups;
	private Integer learnersPerGroup;
	private Long staffGroupID;
	private Integer maxNumberOfGroups;
	//list of GroupDTO
	private List groups;
	

	public GroupingDTO(Long groupingID, Integer groupingUIID,
			Integer groupingType, Integer numberOfGroups,
			Integer learnersPerGroup, Long staffGroupID,
			Integer maxNumberOfGroups,List groupDTOs) {		
		this.groupingID = groupingID;
		this.groupingUIID = groupingUIID;
		this.groupingTypeID = groupingType;
		this.numberOfGroups = numberOfGroups;
		this.learnersPerGroup = learnersPerGroup;
		this.staffGroupID = staffGroupID;
		this.maxNumberOfGroups = maxNumberOfGroups;
		this.groups = groupDTOs;
	}
	public GroupingDTO(Grouping grouping){
		this.groupingID = grouping.getGroupingId();
		this.groupingUIID = grouping.getGroupingUIID();
		this.maxNumberOfGroups = grouping.getMaxNumberOfGroups();		
		this.groupingTypeID = grouping.getGroupingTypeId();
		Set groupSet = grouping.getGroups();
		groups = new ArrayList();
		if(groupSet != null){
			Iterator iter = groupSet.iterator();
			while(iter.hasNext()){
				groups.add(((Group)iter.next()).getGroupDTO());
			}
		}
		/*The two lines of code below are commented out, because it creates a new grouping instance and then tries to 
		 get the attributes for it , in which the values are null. So the grouping object is passed straight into
		 the processGroupingActivity() function. */
		//Object object = Grouping.getGroupingInstance(groupingTypeID);
		//processGroupingActivity(object);
		processGroupingActivity(grouping);
	}
	public void processGroupingActivity(Object object){
		if(object instanceof RandomGrouping)
			addRandomGroupingAttributes((RandomGrouping)object);
		else if (object instanceof ChosenGrouping)
			addChosenGroupingAttributes((ChosenGrouping)object);
		else
			addLessonClassAttributes((LessonClass)object);
	}
	private void addRandomGroupingAttributes(RandomGrouping grouping){
		this.learnersPerGroup = grouping.getLearnersPerGroup();
		this.numberOfGroups = grouping.getNumberOfGroups();		
	}
	private void addChosenGroupingAttributes(ChosenGrouping grouping){
		
	}
	private void addLessonClassAttributes(LessonClass grouping){
		this.staffGroupID = grouping.getStaffGroup().getGroupId();
	}
	/**
	 * @return Returns the groupingID.
	 */
	public Long getGroupingID() {
		return groupingID;
	}
	/**
	 * @param groupingID The groupingID to set.
	 */
	public void setGroupingID(Long groupingID) {
		if(!groupingID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.groupingID = groupingID;
	}
	/**
	 * @return Returns the groupingType.
	 */
	public Integer getGroupingTypeID() {
		return groupingTypeID;
	}
	/**
	 * @param groupingType The groupingType to set.
	 */
	public void setGroupingTypeID(Integer groupingType) {
		if(!groupingType.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingTypeID = groupingType;
	}
	/**
	 * @return Returns the groupingUIID.
	 */
	public Integer getGroupingUIID() {
		return groupingUIID;
	}
	/**
	 * @param groupingUIID The groupingUIID to set.
	 */
	public void setGroupingUIID(Integer groupingUIID) {
		if(!groupingUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingUIID = groupingUIID;
	}
	/**
	 * @return Returns the learnersPerGroup.
	 */
	public Integer getLearnersPerGroup() {
		return learnersPerGroup;
	}
	/**
	 * @param learnersPerGroup The learnersPerGroup to set.
	 */
	public void setLearnersPerGroup(Integer learnersPerGroup) {
		if(!learnersPerGroup.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.learnersPerGroup = learnersPerGroup;
	}
	/**
	 * @return Returns the maxNumberOfGroups.
	 */
	public Integer getMaxNumberOfGroups() {
		return maxNumberOfGroups;
	}
	/**
	 * @param maxNumberOfGroups The maxNumberOfGroups to set.
	 */
	public void setMaxNumberOfGroups(Integer maxNumberOfGroups) {
		if(!maxNumberOfGroups.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.maxNumberOfGroups = maxNumberOfGroups;
	}
	/**
	 * @return Returns the numberOfGroups.
	 */
	public Integer getNumberOfGroups() {
		return numberOfGroups;
	}
	/**
	 * @param numberOfGroups The numberOfGroups to set.
	 */
	public void setNumberOfGroups(Integer numberOfGroups) {
		if(!numberOfGroups.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.numberOfGroups = numberOfGroups;
	}
	/**
	 * @return Returns the staffGroupID.
	 */
	public Long getStaffGroupID() {
		return staffGroupID;
	}
	/**
	 * @param staffGroupID The staffGroupID to set.
	 */
	public void setStaffGroupID(Long staffGroupID) {
		if(!staffGroupID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
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
}
