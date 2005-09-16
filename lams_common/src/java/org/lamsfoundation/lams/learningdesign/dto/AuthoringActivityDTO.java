/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;
import java.util.Hashtable;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
/**
 * @author Manpreet Minhas
 */
public class AuthoringActivityDTO extends BaseDTO{
	
	/*****************************************************************************
	 * Attributes
	 *****************************************************************************/
	
	/** identifier field */
	private Long activityID;

	/** FLASH generated value. Unique per LearningDesign.
	 * Required by flash only.*/
	private Integer activityUIID;

	/** Description of the activity*/
	private String description;

	/** Title of the activity*/
	private String title;
	
	/** Help text for the activity*/
	private String helpText;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer xcoord;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer ycoord;
	
	/** The activity that acts as a container/parent for
	 * this activity. Normally would be one of the 
	 * complex activities which have child activities 
	 * defined inside them. */
	private Long parentActivityID;
	
	/** the activity_ui_id of the parent activity */
	private Integer parentUIID;
	
	/** The type of activity */
	private Integer activityTypeID;
	
	/** The Grouping that applies to this activity*/
	private Long groupingID;
	
	/** The grouping_ui_id of the Grouping that
	 * applies that to this activity
	 * */
	private Integer groupingUIID;
	
	/** Indicates the order in which the activities
	 * appear inside complex activities. Starts from 
	 * 0, 1 and so on.*/
	private Integer orderID;

	/** Indicates whether the content of this activity 
	 * would be defined later in the monitoring enviornment or not.*/
	private Boolean defineLater;
	
	/** The LearningDesign to which this activity belongs*/
	private Long learningDesignID;
	
	/** The LearningLibrary of which this activity is a part*/
	private Long learningLibraryID;
	
	/** Date this activity was created */
	private Date createDateTime;
	
	/** Indicates whether this activity is available offline*/
	private Boolean runOffline;

	/** Maximum number of activities to be attempted */
	private Integer maxOptions;
	
	/** Minimum number of activities to be attempted */
	private Integer minOptions;
	
	/** Instructions for OptionsActivity*/
	private String optionsInstructions;
	
	/** The tool_id of the activity */
	private Long toolID;
	
	/** The tool_content_id of the tool */
	private Long toolContentID;
	
	/** The category of activity */
	private Integer activityCategoryID;
	
	private Integer gateActivityLevelID;
	
	private Boolean gateOpen;
	
	private Long gateStartTimeOffset;
	
	private Long gateEndTimeOffset;
	
	private Date gateStartDateTime;
	
	private Date gateEndDateTime;
	
	private Boolean applyGrouping;
	
	private Integer groupingSupportType;
	
	private Integer groupingType;

	
	/** The image that represents the icon of this 
	 * activity in the UI*/
	private String libraryActivityUiImage;
	
	private Long createGroupingID;
	
	private Integer createGroupingUIID;
	
	private GroupingDTO groupingDTO;
	
	
	/** Single Library can have one or more activities
	 * defined inside it. This field indicates which
	 * activity is this.*/
	private Long libraryActivityID;
	
	/*****************************************************************************
	 * Constructors
	 *****************************************************************************/
	public AuthoringActivityDTO(Long activityID, Integer activityUIID,
			String description, String title, String helpText, Integer xcoord,
			Integer ycoord, Long parentActivityID, Integer parentUIID,
			Integer activityTypeId, Long groupingID, Integer groupingUIID,
			Integer orderID, Boolean defineLater, Long learningDesignID,
			Long learningLibraryID, Date createDateTime, Boolean runOffline,
			Integer maxOptions, Integer minOptions,
			String optionsInstructions, Long toolID, Long toolContentID,
			Integer activityCategoryID, Integer gateActivityLevelID,
			Boolean gateOpen, Long gateStartTimeOffset, Long gateEndTimeOffset,
			Date gateStartDateTime, Date gateEndDateTime,
			String libraryActivityUiImage, Long createGroupingID,
			Integer createGroupingUIID, Long libraryActivityID,
			Boolean applyGrouping,Integer groupingSupportType,
			Integer groupingType,GroupingDTO groupingDTO) {
		super();
		this.activityID = activityID;
		this.activityUIID = activityUIID;
		this.description = description;
		this.title = title;
		this.helpText = helpText;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.parentActivityID = parentActivityID;
		this.parentUIID = parentUIID;
		this.activityTypeID = activityTypeId;
		this.groupingID = groupingID;
		this.groupingUIID = groupingUIID;
		this.orderID = orderID;
		this.defineLater = defineLater;
		this.learningDesignID = learningDesignID;
		this.learningLibraryID = learningLibraryID;
		this.createDateTime = createDateTime;
		this.runOffline = runOffline;
		this.maxOptions = maxOptions;
		this.minOptions = minOptions;
		this.optionsInstructions = optionsInstructions;
		this.toolID = toolID;
		this.toolContentID = toolContentID;
		this.activityCategoryID = activityCategoryID;
		this.gateActivityLevelID = gateActivityLevelID;
		this.gateOpen = gateOpen;
		this.gateStartTimeOffset = gateStartTimeOffset;
		this.gateEndTimeOffset = gateEndTimeOffset;
		this.gateStartDateTime = gateStartDateTime;
		this.gateEndDateTime = gateEndDateTime;
		this.libraryActivityUiImage = libraryActivityUiImage;
		this.createGroupingID = createGroupingID;
		this.createGroupingUIID = createGroupingUIID;
		this.libraryActivityID = libraryActivityID;
		this.applyGrouping = applyGrouping;
		this.groupingSupportType = groupingSupportType;
		this.groupingType = groupingType;
		this.groupingDTO = groupingDTO;
	}
	public AuthoringActivityDTO(Object object){
		processActivityType(object);
		Activity activity = (Activity)object;
		this.activityID = activity.getActivityId();
		this.activityUIID = activity.getActivityUIID();
		this.description = activity.getDescription();
		this.title = activity.getTitle();
		this.helpText = activity.getHelpText();
		this.xcoord = activity.getXcoord();
		this.ycoord = activity.getYcoord();
		this.parentActivityID = activity.getParentActivity()!=null?
								activity.getParentActivity().getActivityId():
								WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.parentUIID = activity.getParentUIID();
		this.activityTypeID = activity.getActivityTypeId();
		
		this.groupingID = activity.getGrouping()!=null?
						  activity.getGrouping().getGroupingId():
						  WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		
		this.groupingUIID = activity.getGroupingUIID();
		this.orderID = activity.getOrderId();
		this.defineLater = activity.getDefineLater();
		this.learningDesignID = activity.getLearningDesign()!=null?
								activity.getLearningDesign().getLearningDesignId():
								WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.learningLibraryID = activity.getLearningLibrary()!=null?
								 activity.getLearningLibrary().getLearningLibraryId():
								 WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.createDateTime = activity.getCreateDateTime();
		this.runOffline = activity.getRunOffline();
		this.activityCategoryID = activity.getActivityCategoryID();		
		this.libraryActivityUiImage = activity.getLibraryActivityUiImage();		
		this.libraryActivityID = activity.getLibraryActivity()!=null?
								 activity.getLibraryActivity().getActivityId():
								 WDDXTAGS.NUMERIC_NULL_VALUE_LONG;	
		this.applyGrouping = activity.getApplyGrouping();
		this.groupingSupportType = activity.getGroupingSupportType();
	}
	
	
	public AuthoringActivityDTO(Hashtable activityDetails) throws WDDXProcessorConversionException{
		this.activityUIID = convertToInteger(activityDetails,"activityUIID");
		this.description = convertToString(activityDetails,"description");
		this.title = convertToString(activityDetails,"title");			
		this.helpText = convertToString(activityDetails,"helpText");
		this.xcoord = convertToInteger(activityDetails, "xcoord");
		this.ycoord = convertToInteger(activityDetails, "ycoord");

		this.parentActivityID = convertToLong(activityDetails, "parentActivityID");
		this.parentUIID = convertToInteger(activityDetails, "parentUIID");

		this.activityTypeID = convertToInteger(activityDetails, "activityTypeID");
		this.groupingID = convertToLong(activityDetails,"groupingID");
		this.groupingUIID = convertToInteger(activityDetails,"groupingUIID");
		this.orderID = convertToInteger(activityDetails,"orderID");
		this.defineLater = convertToBoolean(activityDetails,"defineLater");
		this.learningDesignID = convertToLong(activityDetails,"learningDesignID");
		this.learningLibraryID = convertToLong(activityDetails,"learningLibraryID");
		this.createDateTime = convertToDate(activityDetails,"createDateTime");
		this.runOffline = convertToBoolean(activityDetails,"runOffline");
		this.maxOptions = convertToInteger(activityDetails,"maxOptions");
		this.minOptions = convertToInteger(activityDetails,"minOptions");
		this.optionsInstructions = convertToString(activityDetails,"optionsInstructions");
		this.toolID = convertToLong(activityDetails,"toolID");
		this.toolContentID = convertToLong(activityDetails,"toolContentID");
		this. activityCategoryID = convertToInteger(activityDetails,"activityCategoryID");

		this.gateActivityLevelID = convertToInteger(activityDetails,"gateActivityLevelID");
		this.gateOpen = convertToBoolean(activityDetails,"gateOpen");
		this.gateEndTimeOffset = convertToLong(activityDetails,"gateEndTimeOffset");
		this.gateStartTimeOffset = convertToLong(activityDetails,"gateStartTimeOffset");
		this.gateEndDateTime = convertToDate(activityDetails,"gateEndDateTime");
		this.gateStartDateTime = convertToDate(activityDetails,"gateStartDateTime");
		
		this.libraryActivityUiImage=convertToString(activityDetails,"libraryActivityUiImage");
		this.createGroupingID= convertToLong(activityDetails,"createGroupingID");
		this.createGroupingUIID=convertToInteger(activityDetails,"createGroupingUIID");
		this.libraryActivityID=convertToLong(activityDetails,"libraryActivityID");
		this.applyGrouping=convertToBoolean(activityDetails,"applyGrouping");
		this.groupingSupportType=convertToInteger(activityDetails,"groupingSupportType");
		this.groupingType=convertToInteger(activityDetails,"groupingType");
		if(activityDetails.containsKey("groupingDTO")){
			Hashtable groupingDetails = (Hashtable) activityDetails.get("groupingDTO");			
			this.groupingDTO = new GroupingDTO(groupingDetails);
		}

	}
	private  void processActivityType(Object activity){
		if(activity instanceof GroupingActivity)
			 addGroupingActivityAttributes((GroupingActivity)activity);
		else if(activity instanceof ToolActivity)
			 addToolActivityAttributes((ToolActivity)activity);
		else if(activity instanceof GateActivity)
			 addGateActivityAttributes(activity);
		else 			
			 addComplexActivityAttributes(activity);		
	}
	private void addComplexActivityAttributes(Object activity){		
		if(activity instanceof OptionsActivity)
			addOptionsActivityAttributes((OptionsActivity)activity);
		else if (activity instanceof ParallelActivity)
			addParallelActivityAttributes((ParallelActivity)activity);
		else
			addSequenceActivityAttributes((SequenceActivity)activity);
		
	}
	private void addGroupingActivityAttributes(GroupingActivity groupingActivity){
		Grouping grouping = groupingActivity.getCreateGrouping();
		
		this.groupingDTO = grouping.getGroupingDTO();
		this.createGroupingID = grouping.getGroupingId();
		this.createGroupingUIID = grouping.getGroupingUIID();
		this.groupingType = grouping.getGroupingTypeId();
	}
	private void addOptionsActivityAttributes(OptionsActivity optionsActivity){
		this.maxOptions = optionsActivity.getMaxNumberOfOptions();
		this.minOptions = optionsActivity.getMinNumberOfOptions();
		this.optionsInstructions = optionsActivity.getOptionsInstructions();		
	}
	private void addParallelActivityAttributes(ParallelActivity activity){		
	}
	private void addSequenceActivityAttributes(SequenceActivity activity){
		
	}
	private void addToolActivityAttributes(ToolActivity toolActivity){
		this.toolContentID = toolActivity.getToolContentId();
		this.toolID = toolActivity.getTool().getToolId();													 
	}
	private void addGateActivityAttributes(Object activity){
		if(activity instanceof SynchGateActivity)
			addSynchGateActivityAttributes((SynchGateActivity)activity);
		else if (activity instanceof PermissionGateActivity)
			addPermissionGateActivityAttributes((PermissionGateActivity)activity);
		else
			addScheduleGateActivityAttributes((ScheduleGateActivity)activity);
		GateActivity gateActivity = (GateActivity)activity ;
		this.gateActivityLevelID = gateActivity.getGateActivityLevelId();
		this.gateOpen = gateActivity.getGateOpen();
				
	}
	private void addSynchGateActivityAttributes(SynchGateActivity activity){	
	}
	private void addPermissionGateActivityAttributes(PermissionGateActivity activity){		
	}
	private void addScheduleGateActivityAttributes(ScheduleGateActivity activity){
		this.gateStartDateTime = activity.getGateStartDateTime();
		this.gateStartTimeOffset = activity.getGateStartTimeOffset();
		this.gateEndDateTime = activity.getGateEndDateTime();
		this.gateEndTimeOffset = activity.getGateEndTimeOffset();		
	}
	
	/*****************************************************************************
	 * Getters
	 *****************************************************************************/
	
	/**
	 * @return Returns the activityCategoryID.
	 */
	public Integer getActivityCategoryID() {
		return activityCategoryID!=null?activityCategoryID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the activityID.
	 */
	public Long getActivityID() {
		return activityID!=null?activityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the activityTypeID.
	 */
	public Integer getActivityTypeID() {
		return activityTypeID!=null?activityTypeID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the activityUIID.
	 */
	public Integer getActivityUIID() {
		return activityUIID!=null?activityUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the createGroupingID.
	 */
	public Long getCreateGroupingID() {
		return createGroupingID!=null?createGroupingID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the createGroupingUIID.
	 */
	public Integer getCreateGroupingUIID() {
		return createGroupingUIID!=null?createGroupingUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the defineLater.
	 */
	public Boolean getDefineLater() {
		return defineLater!=null?defineLater:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the gateActivityLevelID.
	 */
	public Integer getGateActivityLevelID() {
		return gateActivityLevelID!=null?gateActivityLevelID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the gateEndDateTime.
	 */
	public Date getGateEndDateTime() {
		return gateEndDateTime!=null?gateEndDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the gateEndTimeOffset.
	 */
	public Long getGateEndTimeOffset() {
		return gateEndTimeOffset!=null?gateEndTimeOffset:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the gateOpen.
	 */
	public Boolean getGateOpen() {
		return gateOpen!=null?gateOpen:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	/**
	 * @return Returns the gateStartDateTime.
	 */
	public Date getGateStartDateTime() {
		return gateStartDateTime!=null?gateStartDateTime:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the gateStartTimeOffset.
	 */
	public Long getGateStartTimeOffset() {
		return gateStartTimeOffset!=null?gateStartTimeOffset:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the groupingID.
	 */
	public Long getGroupingID() {
		return groupingID!=null?groupingID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the groupingUIID.
	 */
	public Integer getGroupingUIID() {
		return groupingUIID!=null?groupingUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText() {
		return helpText!=null?helpText:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the learningDesignID.
	 */
	public Long getLearningDesignID() {
		return learningDesignID!=null?learningDesignID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the learningLibraryID.
	 */
	public Long getLearningLibraryID() {
		return learningLibraryID!=null?libraryActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the libraryActivityID.
	 */
	public Long getLibraryActivityID() {
		return libraryActivityID!=null?libraryActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the libraryActivityUiImage.
	 */
	public String getLibraryActivityUiImage() {
		return libraryActivityUiImage!=null?libraryActivityUiImage:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the maxOptions.
	 */
	public Integer getMaxOptions() {
		return maxOptions!=null?maxOptions:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the minOptions.
	 */
	public Integer getMinOptions() {
		return minOptions!=null?minOptions:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the optionsInstructions.
	 */
	public String getOptionsInstructions() {
		return optionsInstructions!=null?optionsInstructions:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the orderID.
	 */
	public Integer getOrderID() {
		return orderID!=null?orderID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the parentActivityID.
	 */
	public Long getParentActivityID() {
		return parentActivityID!=null?parentActivityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the parentUIID.
	 */
	public Integer getParentUIID() {
		return parentUIID!=null?parentUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the runOffline.
	 */
	public Boolean getRunOffline() {
		return runOffline!=null?runOffline:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the toolContentID.
	 */
	public Long getToolContentID() {
		return toolContentID!=null?toolContentID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the toolID.
	 */
	public Long getToolID() {
		return toolID!=null?toolID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the xcoord.
	 */
	public Integer getXcoord() {
		return xcoord!=null?xcoord:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the ycoord.
	 */
	public Integer getYcoord() {
		return ycoord!=null?ycoord:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}	
	/*************************************************
	 * Setters
	 ************************************************/
	/**
	 * @param activityCategoryID The activityCategoryID to set.
	 */
	public void setActivityCategoryID(Integer activityCategoryID) {
		if(!activityCategoryID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.activityCategoryID = activityCategoryID;
	}
	/**
	 * @param activityID The activityID to set.
	 */
	public void setActivityID(Long activityId) {
		if(!activityId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.activityID = activityId;
	}
	/**
	 * @param activityTypeID The activityTypeID to set.
	 */
	public void setActivityTypeID(Integer activityTypeId) {
		if(!activityTypeId.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.activityTypeID = activityTypeId;
	}
	/**
	 * @param activityUIID The activityUIID to set.
	 */
	public void setActivityUIID(Integer activityUIID) {
		if(!activityUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.activityUIID = activityUIID;
	}
	/**
	 * @param createDateTime The createDateTime to set.
	 */
	public void setCreateDateTime(Date createDateTime) {
		if(!createDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.createDateTime = createDateTime;
	}
	/**
	 * @param createGroupingID The createGroupingID to set.
	 */
	public void setCreateGroupingID(Long createGroupingID) {
		if(!createGroupingID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.createGroupingID = createGroupingID;
	}
	/**
	 * @param createGroupingUIID The createGroupingUIID to set.
	 */
	public void setCreateGroupingUIID(Integer createGroupingUIID) {
		if(!createGroupingUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.createGroupingUIID = createGroupingUIID;
	}
	/**
	 * @param defineLater The defineLater to set.
	 */
	public void setDefineLater(Boolean defineLater) {		
		this.defineLater = defineLater;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		if(!description.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.description = description;
	}
	/**
	 * @param gateActivityLevelID The gateActivityLevelID to set.
	 */
	public void setGateActivityLevelID(Integer gateActivityLevelID) {
		if(!gateActivityLevelID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.gateActivityLevelID = gateActivityLevelID;
	}
	/**
	 * @param gateEndDateTime The gateEndDateTime to set.
	 */
	public void setGateEndDateTime(Date gateEndDateTime) {
		if(!gateEndDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.gateEndDateTime = gateEndDateTime;
	}
	/**
	 * @param gateEndTimeOffset The gateEndTimeOffset to set.
	 */
	public void setGateEndTimeOffset(Long gateEndTimeOffset) {
		if(!gateEndTimeOffset.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.gateEndTimeOffset = gateEndTimeOffset;
	}
	/**
	 * @param gateOpen The gateOpen to set.
	 */
	public void setGateOpen(Boolean gateOpen) {
		this.gateOpen = gateOpen;
	}
	/**
	 * @param gateStartDateTime The gateStartDateTime to set.
	 */
	public void setGateStartDateTime(Date gateStartDateTime) {
		if(!gateStartDateTime.equals(WDDXTAGS.DATE_NULL_VALUE))
			this.gateStartDateTime = gateStartDateTime;
	}
	/**
	 * @param gateStartTimeOffset The gateStartTimeOffset to set.
	 */
	public void setGateStartTimeOffset(Long gateStartTimeOffset) {
		if(!gateStartTimeOffset.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.gateStartTimeOffset = gateStartTimeOffset;
	}
	/**
	 * @param groupingID The groupingID to set.
	 */
	public void setGroupingID(Long groupingID) {
		if(!groupingID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.groupingID = groupingID;
	}
	/**
	 * @param groupingUIID The groupingUIID to set.
	 */
	public void setGroupingUIID(Integer groupingUIID) {
		if(!groupingUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingUIID = groupingUIID;
	}
	/**
	 * @param helpText The helpText to set.
	 */
	public void setHelpText(String helpText) {
		if(!helpText.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.helpText = helpText;
	}
	/**
	 * @param learningDesignID The learningDesignID to set.
	 */
	public void setLearningDesignID(Long learningDesignID) {
		if(!learningDesignID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.learningDesignID = learningDesignID;
	}
	/**
	 * @param learningLibraryID The learningLibraryID to set.
	 */
	public void setLearningLibraryID(Long learningLibraryID) {
		if(!learningLibraryID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.learningLibraryID = learningLibraryID;
	}
	/**
	 * @param libraryActivityID The libraryActivityID to set.
	 */
	public void setLibraryActivityID(Long libraryActivityID) {
		if(!libraryActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.libraryActivityID = libraryActivityID;
	}
	/**
	 * @param libraryActivityUiImage The libraryActivityUiImage to set.
	 */
	public void setLibraryActivityUiImage(String libraryActivityUiImage) {
		if(!libraryActivityUiImage.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.libraryActivityUiImage = libraryActivityUiImage;
	}
	/**
	 * @param maxOptions The maxOptions to set.
	 */
	public void setMaxOptions(Integer maxOptions) {
		if(!maxOptions.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.maxOptions = maxOptions;
	}
	/**
	 * @param minOptions The minOptions to set.
	 */
	public void setMinOptions(Integer minOptions) {
		if(!minOptions.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.minOptions = minOptions;
	}
	/**
	 * @param optionsInstructions The optionsInstructions to set.
	 */
	public void setOptionsInstructions(String optionsInstructions) {
		if(!optionsInstructions.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.optionsInstructions = optionsInstructions;
	}
	/**
	 * @param orderID The orderID to set.
	 */
	public void setOrderID(Integer orderID) {
		if(!orderID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.orderID = orderID;
	}
	/**
	 * @param parentActivityID The parentActivityID to set.
	 */
	public void setParentActivityID(Long parentActivityID) {
		if(!parentActivityID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.parentActivityID = parentActivityID;
	}
	/**
	 * @param parentUIID The parentUIID to set.
	 */
	public void setParentUIID(Integer parentUIID) {
		if(!parentUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.parentUIID = parentUIID;
	}
	/**
	 * @param runOffline The runOffline to set.
	 */
	public void setRunOffline(Boolean runOffline) {
		this.runOffline = runOffline;
	}
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		if(!title.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.title = title;
	}
	/**
	 * @param toolContentID The toolContentID to set.
	 */
	public void setToolContentID(Long toolContentID) {
		if(!toolContentID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.toolContentID = toolContentID;
	}
	/**
	 * @param toolID The toolID to set.
	 */
	public void setToolID(Long toolID) {
		if(!toolID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.toolID = toolID;
	}
	/**
	 * @param xcoord The xcoord to set.
	 */
	public void setXcoord(Integer xcoord) {
		if(!xcoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.xcoord = xcoord;
	}
	/**
	 * @param ycoord The ycoord to set.
	 */
	public void setYcoord(Integer ycoord) {
		if(!xcoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.ycoord = ycoord;
	}	
	/**
	 * @return Returns the applyGrouping.
	 */
	public Boolean getApplyGrouping() {
		return applyGrouping!=null?applyGrouping:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	/**
	 * @param applyGrouping The applyGrouping to set.
	 */
	public void setApplyGrouping(Boolean applyGrouping) {
		this.applyGrouping = applyGrouping;
	}
	/**
	 * @return Returns the groupingSupportType.
	 */
	public Integer getGroupingSupportType() {
		return groupingSupportType!=null?groupingSupportType:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @param groupingSupportType The groupingSupportType to set.
	 */
	public void setGroupingSupportType(Integer groupingSupportType) {
		if(!groupingSupportType.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingSupportType = groupingSupportType;
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
	 * @return Returns the groupingDTO.
	 */
	public GroupingDTO getGroupingDTO() {
		return groupingDTO;
	}
	/**
	 * @param groupingDTO The groupingDTO to set.
	 */
	public void setGroupingDTO(GroupingDTO groupingDTO) {
		this.groupingDTO = groupingDTO;
	}	
}
