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
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.SystemGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
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
	private String activityTitle;
	
	/** Help text for the activity*/
	private String helpText;

	/** Help URL for the activity */
	private String helpURL;
	
	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer xCoord;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer yCoord;
	
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
	 * would be defined later in the monitoring environment or not.*/
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
	
	/** The tool_signature of the activity */
	private String toolSignature;
	
	/** The tool_id of the activity */
	private Long toolID;
	
	/** The tool_content_id of the tool */
	private Long toolContentID;
	
	/** The tool's display name */
	private String toolDisplayName;
	
	/** The tool's version */
	private String toolVersion;

	/** The url of the tool's authoring screen. */
	private String authoringURL;
	
	/** The url of the tool's monitoring screen. */
	private String monitoringURL;

	/** The url of the tool's contribute screen. */
	private String contributeURL;

	/** The url of the tool's moderation screen. */
	private String moderationURL;

	/** The url of the tool's admin screen. */
	private String adminURL;

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
	private String libraryActivityUIImage;
	
	private Long createGroupingID;
	
	private Integer createGroupingUIID;
	
	private Boolean readOnly;

	/** An activity is initialised if it is ready to be used in lesson ie the tool content
	 * is set up, schedule gates are scheduled, etc. Used to detect which activities
	 * need to be initialised for live edit. */
	private Boolean initialised;

	/** If stopAfterActivity is true, then the progress engine should "end" the lesson at this point.
	 * Used to arbitrarily stop somewhere in a design, such as at the end of the branch. The normal
	 * final activity of a design does not necessarily have this set - the progress engine will just
	 * stop when it runs out of transitions to follow. */
	private Boolean stopAfterActivity;
 
	/* Server will send Grouping objects as an array (in the Groupings array)
	 * rather than being part of the GroupingActivity. For the groupings array
	 * see LearningDesignDTO.
	 */
	//private GroupingDTO groupingDTO;
	
	
	/** Single Library can have one or more activities
	 * defined inside it. This field indicates which
	 * activity is this.*/
	private Long libraryActivityID;
	
	/** Name of the file (including the package) that contains the text strings for
	 * this activity. e.g. org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties. */
	private String languageFile;
	
	/** List of the UIIDs of the activities that are input activities for this activity */
	private ArrayList inputActivities;

	/** List of the UIIDs of the activities that are input activities for this activity */
	private Integer toolActivityUIID;

	/** Used by a sequence activity to determine the start of the transition based sequence and used by tool
	 * based branching to determine the default branch. */
	private Integer defaultActivityUIID;
	
	private Integer startXCoord;
	private Integer startYCoord;
	private Integer endXCoord;
	private Integer endYCoord;
	

	/*****************************************************************************
	 * Constructors
	 *****************************************************************************/
	public AuthoringActivityDTO(Long activityID, Integer activityUIID,
			String description, String title, String helpText, Integer xcoord,
			Integer ycoord, Long parentActivityID, Integer parentUIID,
			Integer activityTypeId, Long groupingID, Integer groupingUIID,
			Integer orderID, Boolean defineLater, Long learningDesignID,
			Long learningLibraryID, Date createDateTime, Boolean runOffline,
			String languageFile,
			Integer maxOptions, Integer minOptions,
			String optionsInstructions, String toolSignature, Long toolID, Long toolContentID,
			Integer activityCategoryID, Integer gateActivityLevelID,
			Boolean gateOpen, Long gateStartTimeOffset, Long gateEndTimeOffset,
			Date gateStartDateTime, Date gateEndDateTime,
			String libraryActivityUiImage, Long createGroupingID,
			Integer createGroupingUIID, Long libraryActivityID,
			Boolean applyGrouping,Integer groupingSupportType,
			Integer groupingType,GroupingDTO groupingDTO, 
			Boolean readOnly, Boolean initialised, Boolean stopAfterActivity,
			ArrayList<Integer> inputActivities, Integer toolActivityUIID, Integer defaultActivityUIID,
			Integer startXCoord, Integer startYCoord, Integer endXCoord, Integer endYCoord) {
		super();
		this.activityID = activityID;
		this.activityUIID = activityUIID;
		this.description = description;
		this.activityTitle = title;
		this.helpText = helpText;
		this.xCoord = xcoord;
		this.yCoord = ycoord;
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
		this.languageFile = languageFile;
		this.maxOptions = maxOptions;
		this.minOptions = minOptions;
		this.optionsInstructions = optionsInstructions;
		this.toolSignature = toolSignature;
		this.toolID = toolID;
		this.toolContentID = toolContentID;
		this.activityCategoryID = activityCategoryID;
		this.gateActivityLevelID = gateActivityLevelID;
		this.gateOpen = gateOpen;
		this.gateStartTimeOffset = gateStartTimeOffset;
		this.gateEndTimeOffset = gateEndTimeOffset;
		this.gateStartDateTime = gateStartDateTime;
		this.gateEndDateTime = gateEndDateTime;
		this.libraryActivityUIImage = libraryActivityUiImage;
		this.createGroupingID = createGroupingID;
		this.createGroupingUIID = createGroupingUIID;
		this.libraryActivityID = libraryActivityID;
		this.applyGrouping = applyGrouping;
		this.groupingSupportType = groupingSupportType;
		this.groupingType = groupingType;
		//this.groupingDTO = groupingDTO;
		this.readOnly = readOnly;
		this.initialised = initialised;
		this.stopAfterActivity=stopAfterActivity;
		this.toolActivityUIID=toolActivityUIID;
		this.inputActivities=inputActivities;
		// Complex Activity field
		this.defaultActivityUIID = defaultActivityUIID;
		// Branching Activity fields
		this.startXCoord = startXCoord;
		this.startYCoord = startYCoord;
		this.endXCoord = endXCoord;
		this.endYCoord = endYCoord;

	}
	public AuthoringActivityDTO(Activity activity, ArrayList<BranchActivityEntryDTO> branchMappings){
		processActivityType(activity, branchMappings);
		this.activityID = activity.getActivityId();
		this.activityUIID = activity.getActivityUIID();
		this.description = activity.getDescription();
		this.activityTitle = activity.getTitle();
		this.helpText = activity.getHelpText();
		this.xCoord = activity.getXcoord();
		this.yCoord = activity.getYcoord();
		this.parentActivityID = activity.getParentActivity()!=null?
								activity.getParentActivity().getActivityId():
								null;
		this.parentUIID = activity.getParentUIID();
		this.activityTypeID = activity.getActivityTypeId();
		
		this.groupingID = activity.getGrouping()!=null?
						  activity.getGrouping().getGroupingId():
						  null;
		
		this.groupingUIID = activity.getGroupingUIID();
		this.orderID = activity.getOrderId();
		this.defineLater = activity.getDefineLater();
		this.learningDesignID = activity.getLearningDesign()!=null?
								activity.getLearningDesign().getLearningDesignId():
								null;
		this.learningLibraryID = activity.getLearningLibrary()!=null?
								 activity.getLearningLibrary().getLearningLibraryId():
								 null;
		this.createDateTime = activity.getCreateDateTime();
		this.runOffline = activity.getRunOffline();
		this.languageFile = activity.getLanguageFile();
		this.activityCategoryID = activity.getActivityCategoryID();		
		this.libraryActivityUIImage = activity.getLibraryActivityUiImage();		
		this.libraryActivityID = activity.getLibraryActivity()!=null?
								 activity.getLibraryActivity().getActivityId():
								 null;	
		this.applyGrouping = activity.getApplyGrouping();
		this.groupingSupportType = activity.getGroupingSupportType();
		this.readOnly = activity.getReadOnly();
		this.initialised = activity.isInitialised();
		this.stopAfterActivity = activity.isStopAfterActivity();
		
		if ( activity.getInputActivities() != null && activity.getInputActivities().size() > 0 ) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			Iterator iter = activity.getInputActivities().iterator();
			while ( iter.hasNext() ) {
				Activity inputAct = (Activity) iter.next();
				list.add(inputAct.getActivityUIID());
				this.toolActivityUIID=inputAct.getActivityUIID();
			}
			this.inputActivities = list;
		}
	}
	
	

	private  void processActivityType(Activity activity, ArrayList<BranchActivityEntryDTO> branchMappings){
		if(activity.isGroupingActivity())
			 addGroupingActivityAttributes((GroupingActivity)activity);
		else if(activity.isToolActivity())
			 addToolActivityAttributes((ToolActivity)activity);
		else if(activity.isGateActivity())
			 addGateActivityAttributes(activity);
		else 			
			 addComplexActivityAttributes(activity, branchMappings);		
	}
	private void addComplexActivityAttributes(Activity activity, ArrayList<BranchActivityEntryDTO> branchMappings){		
		if(activity.isOptionsActivity())
			addOptionsActivityAttributes((OptionsActivity)activity);
		else if (activity.isParallelActivity())
			addParallelActivityAttributes((ParallelActivity)activity);
		else if(activity.isBranchingActivity())
			addBranchingActivityAttributes((BranchingActivity)activity);
		else
			addSequenceActivityAttributes((SequenceActivity)activity, branchMappings);
		
	}
	private void addGroupingActivityAttributes(GroupingActivity groupingActivity){
		Grouping grouping = groupingActivity.getCreateGrouping();
		
		//this.groupingDTO = grouping.getGroupingDTO();
		this.createGroupingID = grouping.getGroupingId();
		this.createGroupingUIID = grouping.getGroupingUIID();
		//this.groupingType = grouping.getGroupingTypeId();
		this.adminURL = groupingActivity.getSystemTool().getAdminUrl();
	}
	private void addOptionsActivityAttributes(OptionsActivity optionsActivity){
		this.maxOptions = optionsActivity.getMaxNumberOfOptions();
		this.minOptions = optionsActivity.getMinNumberOfOptions();
		this.optionsInstructions = optionsActivity.getOptionsInstructions();		
	}
	private void addParallelActivityAttributes(ParallelActivity activity){		
	}
	private void addBranchingActivityAttributes(BranchingActivity activity){		
		this.startXCoord = activity.getStartXcoord();
		this.startYCoord = activity.getStartYcoord();
		this.endXCoord = activity.getEndXcoord();
		this.endYCoord = activity.getEndYcoord();
	}
	private void addSequenceActivityAttributes(SequenceActivity activity, ArrayList<BranchActivityEntryDTO> branchMappings){
		Iterator iter = activity.getBranchEntries().iterator();
		while ( iter.hasNext() ) {
			BranchActivityEntry ba = (BranchActivityEntry) iter.next();
			branchMappings.add(ba.getBranchActivityEntryDTO());
		}
	}
	private void addToolActivityAttributes(ToolActivity toolActivity){
		this.toolContentID = toolActivity.getToolContentId();
		this.toolID = toolActivity.getTool().getToolId();
		this.toolSignature = toolActivity.getTool().getToolSignature();
		this.authoringURL = toolActivity.getTool().getAuthorUrl();
		this.monitoringURL = toolActivity.getTool().getMonitorUrl();
		this.contributeURL = toolActivity.getTool().getContributeUrl();
		this.moderationURL = toolActivity.getTool().getModerationUrl();
		this.helpURL = toolActivity.getTool().getHelpUrl();
		this.adminURL = toolActivity.getTool().getAdminUrl();
		this.toolDisplayName = toolActivity.getTool().getToolDisplayName();
		this.toolVersion = toolActivity.getTool().getToolVersion();
	}
	private void addGateActivityAttributes(Object activity){
		if(activity instanceof SynchGateActivity)
			addSynchGateActivityAttributes((SynchGateActivity)activity);
		else if (activity instanceof PermissionGateActivity)
			addPermissionGateActivityAttributes((PermissionGateActivity)activity);
		else if(activity instanceof SystemGateActivity)
			addSystemGateActivityAttributes((SystemGateActivity)activity);
		else
			addScheduleGateActivityAttributes((ScheduleGateActivity)activity);
		GateActivity gateActivity = (GateActivity)activity ;
		this.gateActivityLevelID = gateActivity.getGateActivityLevelId();
		this.gateOpen = gateActivity.getGateOpen();
		this.adminURL = gateActivity.getSystemTool().getAdminUrl();
				
	}
	private void addSynchGateActivityAttributes(SynchGateActivity activity){	
	}
	private void addPermissionGateActivityAttributes(PermissionGateActivity activity){		
	}
	private void addSystemGateActivityAttributes(SystemGateActivity activity){	
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
		return activityCategoryID;
	}
	/**
	 * @return Returns the activityID.
	 */
	public Long getActivityID() {
		return activityID;
	}
	/**
	 * @return Returns the activityTypeID.
	 */
	public Integer getActivityTypeID() {
		return activityTypeID;
	}
	/**
	 * @return Returns the activityUIID.
	 */
	public Integer getActivityUIID() {
		return activityUIID;
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime;
	}
	/**
	 * @return Returns the createGroupingID.
	 */
	public Long getCreateGroupingID() {
		return createGroupingID;
	}
	/**
	 * @return Returns the createGroupingUIID.
	 */
	public Integer getCreateGroupingUIID() {
		return createGroupingUIID;
	}
	/**
	 * @return Returns the defineLater.
	 */
	public Boolean getDefineLater() {
		return defineLater;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return Returns the gateActivityLevelID.
	 */
	public Integer getGateActivityLevelID() {
		return gateActivityLevelID;
	}
	/**
	 * @return Returns the gateEndDateTime.
	 */
	public Date getGateEndDateTime() {
		return gateEndDateTime;
	}
	/**
	 * @return Returns the gateEndTimeOffset.
	 */
	public Long getGateEndTimeOffset() {
		return gateEndTimeOffset;
	}
	/**
	 * @return Returns the gateOpen.
	 */
	public Boolean getGateOpen() {
		return gateOpen;
	}
	/**
	 * @return Returns the gateStartDateTime.
	 */
	public Date getGateStartDateTime() {
		return gateStartDateTime;
	}
	/**
	 * @return Returns the gateStartTimeOffset.
	 */
	public Long getGateStartTimeOffset() {
		return gateStartTimeOffset;
	}
	/**
	 * @return Returns the groupingID.
	 */
	public Long getGroupingID() {
		return groupingID;
	}
	/**
	 * @return Returns the groupingUIID.
	 */
	public Integer getGroupingUIID() {
		return groupingUIID;
	}
	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText() {
		return helpText;
	}
	/**
	 * @return Returns the learningDesignID.
	 */
	public Long getLearningDesignID() {
		return learningDesignID;
	}
	/**
	 * @return Returns the learningLibraryID.
	 */
	public Long getLearningLibraryID() {
		return learningLibraryID;
	}
	/**
	 * @return Returns the libraryActivityID.
	 */
	public Long getLibraryActivityID() {
		return libraryActivityID;
	}
	/**
	 * @return Returns the libraryActivityUiImage.
	 */
	public String getLibraryActivityUIImage() {
		return libraryActivityUIImage;
	}
	/**
	 * @return Returns the maxOptions.
	 */
	public Integer getMaxOptions() {
		return maxOptions;
	}
	/**
	 * @return Returns the minOptions.
	 */
	public Integer getMinOptions() {
		return minOptions;
	}
	/**
	 * @return Returns the optionsInstructions.
	 */
	public String getOptionsInstructions() {
		return optionsInstructions;
	}
	/**
	 * @return Returns the orderID.
	 */
	public Integer getOrderID() {
		return orderID;
	}
	/**
	 * @return Returns the parentActivityID.
	 */
	public Long getParentActivityID() {
		return parentActivityID;
	}
	/**
	 * @return Returns the parentUIID.
	 */
	public Integer getParentUIID() {
		return parentUIID;
	}
	/**
	 * @return Returns the runOffline.
	 */
	public Boolean getRunOffline() {
		return runOffline;
	}
	/**
	 * @return Returns the title.
	 */
	public String getActivityTitle() {
		return activityTitle;
	}
	/**
	 * @return Returns the toolContentID.
	 */
	public Long getToolContentID() {
		return toolContentID;
	}
	/**
	 * @return Returns the toolID.
	 */
	public Long getToolID() {
		return toolID;
	}
	/**
	 * @return Returns the xcoord.
	 */
	public Integer getxCoord() {
		return xCoord;
	}
	/**
	 * @return Returns the ycoord.
	 */
	public Integer getyCoord() {
		return yCoord;
	}	
	/** Get the authoring url related to this tool */
	public String getAuthoringURL() {
		return authoringURL;
	}
	/**
	 * @return Returns the readOnly.
	 */
	public Boolean getReadOnly() {
		return readOnly;
	}
	/** 
	 * Name of the file (including the package) that contains the text strings for
	 * this activity. e.g. org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
	 */
	public String getLanguageFile() {
		return languageFile;
	}
	/** Get the tool's display name */
	public String getToolDisplayName() {
		return toolDisplayName;
	}
	/** Get the contribution url related to this tool */
	public String getContributeURL() {
		return contributeURL;
	}
	/** Get the monitoring url related to this tool */
	public String getMonitoringURL() {
		return monitoringURL;
	}
	public String getModerationURL() {
		return moderationURL;
	}
	public String getHelpURL(){
		return helpURL;
	}
	public String getToolSignature() {
		return toolSignature;
	}
	public String getToolVersion() {
		return toolVersion;
	}
	public Boolean getInitialised() {
		return initialised;
	}
	public Boolean getStopAfterActivity() {
		return stopAfterActivity;
	}
	/**
	 * @return Returns the applyGrouping.
	 */
	public Boolean getApplyGrouping() {
		return applyGrouping;
	}
	/**
	 * @return Returns the groupingSupportType.
	 */
	public Integer getGroupingSupportType() {
		return groupingSupportType;
	}
	/**
	 * @return Returns the groupingType.
	 */
	public Integer getGroupingType() {
		return groupingType;
	}
	/** Get the UI ID of the first activity within a sequence activity or default branch in tool based branching */
	public Integer getDefaultActivityUIID() {
		return defaultActivityUIID;
	}
	/**
	 * @return Returns the xcoord of the end hub for a branching activity
	 */
	public Integer getEndXCoord() {
		return endXCoord;
	}
	/**
	 * @return Returns the tcoord of the end hub for a branching activity
	 */
	public Integer getEndYCoord() {
		return endYCoord;
	}
	/**
	 * @return Returns the xcoord of the start hub for a branching activity
	 */
	public Integer getStartXCoord() {
		return startXCoord;
	}
	/**
	 * @return Returns the ycoord of the start hub for a branching activity
	 */
	public Integer getStartYCoord() {
		return startYCoord;
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
	public void setLibraryActivityUIImage(String libraryActivityUiImage) {
		if(!libraryActivityUiImage.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.libraryActivityUIImage = libraryActivityUiImage;
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
	public void setActivityTitle(String title) {
		if(!title.equals(WDDXTAGS.STRING_NULL_VALUE))
			this.activityTitle = title;
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
	public void setxCoord(Integer xcoord) {
		if(!xcoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.xCoord = xcoord;
	}
	/**
	 * @param ycoord The ycoord to set.
	 */
	public void setyCoord(Integer ycoord) {
		if(!xCoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.yCoord = ycoord;
	}	
	/**
	 * @param applyGrouping The applyGrouping to set.
	 */
	public void setApplyGrouping(Boolean applyGrouping) {
		this.applyGrouping = applyGrouping;
	}
	/**
	 * @param groupingSupportType The groupingSupportType to set.
	 */
	public void setGroupingSupportType(Integer groupingSupportType) {
		if(!groupingSupportType.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingSupportType = groupingSupportType;
	}
	/**
	 * @param groupingType The groupingType to set.
	 */
	public void setGroupingType(Integer groupingType) {
		if(!groupingType.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			this.groupingType = groupingType;
	}
	/**
	 * 
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(Boolean readOnly) {
		if(!readOnly.equals(WDDXTAGS.BOOLEAN_NULL_VALUE))
			this.readOnly = readOnly;
	}
	
	public void setAuthoringURL(String toolAuthoringURL) {
		this.authoringURL = toolAuthoringURL;
	}
	public void setToolDisplayName(String toolDisplayName) {
		this.toolDisplayName = toolDisplayName;
	}
	public void setLanguageFile(String languageFile) {
		this.languageFile = languageFile;
	}
	public void setContributeURL(String contributeURL) {
		this.contributeURL = contributeURL;
	}
	public void setMonitoringURL(String monitoringURL) {
		this.monitoringURL = monitoringURL;
	}
	public void setModerationURL(String moderationURL) {
		this.moderationURL = moderationURL;
	}
	public void setHelpURL(String helpURL) {
		this.helpURL = helpURL;
	}
	public void setToolSignature(String toolSignature) {
		this.toolSignature = toolSignature;
	}
	public void setToolVersion(String toolVersion) {
		this.toolVersion = toolVersion;
	}
	public void setInitialised(Boolean initialised) {
		this.initialised = initialised;
	}
	public void setStopAfterActivity(Boolean stopAfterActivity) {
		this.stopAfterActivity = stopAfterActivity;
	}
	public void setDefaultActivityUIID(Integer defaultActivityUIID) {
		if(!defaultActivityUIID.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.defaultActivityUIID = defaultActivityUIID;
	}
	public void setEndXCoord(Integer endXCoord) {
		if(!endXCoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.endXCoord = endXCoord;
	}
	public void setEndYCoord(Integer endYCoord) {
		if(!endYCoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.endYCoord = endYCoord;
	}
	public void setStartXCoord(Integer startXCoord) {
		if(!startXCoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.startXCoord = startXCoord;
	}
	public void setStartYCoord(Integer startYCoord) {
		if(!startYCoord.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			this.startYCoord = startYCoord;
	}
	public String getAdminURL() {
		return adminURL;
	}
	public void setAdminURL(String adminURL) {
		this.adminURL = adminURL;
	}
	public ArrayList getInputActivities() {
		return inputActivities;
	}
	public void setInputActivities(ArrayList inputActivities) {
		this.inputActivities = inputActivities;
	}
	public Integer getToolActivityUIID() {
		return toolActivityUIID;
	}
	public void setToolActivityUIID(Integer toolActivityUIID) {
		this.toolActivityUIID = toolActivityUIID;
	}
}
