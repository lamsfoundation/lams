/*
 * Created on Apr 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Hashtable;

import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupingDTO extends BaseDTO{
	
	private Long groupingID;
	private Integer groupingUIID;
	private Integer groupingType;
	private Integer numberOfGroups;
	private Integer learnersPerGroup;
	private Long staffGroupID;
	private Integer maxNumberOfGroups;
	
	

	public GroupingDTO(Long groupingID, Integer groupingUIID,
			Integer groupingType, Integer numberOfGroups,
			Integer learnersPerGroup, Long staffGroupID,
			Integer maxNumberOfGroups) {		
		this.groupingID = groupingID;
		this.groupingUIID = groupingUIID;
		this.groupingType = groupingType;
		this.numberOfGroups = numberOfGroups;
		this.learnersPerGroup = learnersPerGroup;
		this.staffGroupID = staffGroupID;
		this.maxNumberOfGroups = maxNumberOfGroups;
	}
	public GroupingDTO(Grouping grouping){
		this.groupingID = grouping.getGroupingId();
		this.groupingUIID = grouping.getGroupingUIID();
		this.maxNumberOfGroups = grouping.getMaxNumberOfGroups();		
		this.groupingType = grouping.getGroupingTypeId();
		Object object = Grouping.getGroupingInstance(groupingType);
		processGroupingActivity(object);
	}
	public GroupingDTO(Hashtable groupingDetails){
		if(groupingDetails.containsKey("groupingID"))
			this.groupingID =convertToLong(groupingDetails.get("groupingID"));
		if(groupingDetails.containsKey("groupingUIID"))
			this.groupingUIID =convertToInteger(groupingDetails.get("groupingUIID"));
		if(groupingDetails.containsKey("maxNumberOfGroups"))
			this.maxNumberOfGroups =convertToInteger(groupingDetails.get("maxNumberOfGroups"));
		if(groupingDetails.containsKey("groupingType"))
			this.groupingType =convertToInteger(groupingDetails.get("groupingType"));		
		if(groupingDetails.containsKey("staffGroupID"))
				this.staffGroupID =convertToLong(groupingDetails.get("staffGroupID"));
		if(groupingDetails.containsKey("numberOfGroups"))
				this.numberOfGroups =convertToInteger(groupingDetails.get("numberOfGroups"));
		if(groupingDetails.containsKey("learnersPerGroup"))
				this.learnersPerGroup =convertToInteger(groupingDetails.get("learnersPerGroup"));
		
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
		return groupingID!=null?groupingID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
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
	public Integer getGroupingType() {
		return groupingType!=null?groupingType:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @param groupingType The groupingType to set.
	 */
	public void setGroupingType(Integer groupingType) {
		if(!groupingType.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingType = groupingType;
	}
	/**
	 * @return Returns the groupingUIID.
	 */
	public Integer getGroupingUIID() {
		return groupingUIID!=null?groupingUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
		return learnersPerGroup!=null?learnersPerGroup:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
		return maxNumberOfGroups!=null?maxNumberOfGroups:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
		return numberOfGroups!=null?numberOfGroups:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
		return staffGroupID!=null?staffGroupID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @param staffGroupID The staffGroupID to set.
	 */
	public void setStaffGroupID(Long staffGroupID) {
		if(!staffGroupID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.staffGroupID = staffGroupID;
	}
}
