/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AuthoringActivityDTO {
	
	/*****************************************************************************
	 * Attributes
	 *****************************************************************************/
	
	/** identifier field */
	private Long activityId;

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
	private Integer activityTypeId;
	
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

	/** Offline Instruction for this activity*/
	private String offlineInstructions;
	
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
	
	private Date gateStartTimeOffset;
	
	private Date gateEndTimeOffset;
	
	private Date gateStartDateTime;
	
	private Date gateEndDateTime;
	

	/** The image that represents the icon of this 
	 * activity in the UI*/
	private String libraryActivityUiImage;
	
	private Long createGroupingID;
	
	private Integer createGroupingUIID;
	
	
	/** Single Library can have one or more activities
	 * defined inside it. This field indicates which
	 * activity is this.*/
	private Long libraryActivityID;
	
	/*****************************************************************************
	 * Constructors
	 *****************************************************************************/
	public AuthoringActivityDTO(Long activityId, Integer activityUIID,
			String description, String title, String helpText, Integer xcoord,
			Integer ycoord, Long parentActivityID, Integer parentUIID,
			Integer activityTypeId, Long groupingID, Integer groupingUIID,
			Integer orderID, Boolean defineLater, Long learningDesignID,
			Long learningLibraryID, Date createDateTime, Boolean runOffline,
			String offlineInstructions, Integer maxOptions, Integer minOptions,
			String optionsInstructions, Long toolID, Long toolContentID,
			Integer activityCategoryID, Integer gateActivityLevelID,
			Boolean gateOpen, Date gateStartTimeOffset, Date gateEndTimeOffset,
			Date gateStartDateTime, Date gateEndDateTime,
			String libraryActivityUiImage, Long createGroupingID,
			Integer createGroupingUIID, Long libraryActivityID) {
		super();
		this.activityId = activityId;
		this.activityUIID = activityUIID;
		this.description = description;
		this.title = title;
		this.helpText = helpText;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.parentActivityID = parentActivityID;
		this.parentUIID = parentUIID;
		this.activityTypeId = activityTypeId;
		this.groupingID = groupingID;
		this.groupingUIID = groupingUIID;
		this.orderID = orderID;
		this.defineLater = defineLater;
		this.learningDesignID = learningDesignID;
		this.learningLibraryID = learningLibraryID;
		this.createDateTime = createDateTime;
		this.runOffline = runOffline;
		this.offlineInstructions = offlineInstructions;
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
	}
	public AuthoringActivityDTO(Object activity){
		
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
	 * @return Returns the activityId.
	 */
	public Long getActivityId() {
		return activityId!=null?activityId:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the activityTypeId.
	 */
	public Integer getActivityTypeId() {
		return activityTypeId!=null?activityTypeId:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
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
	public Date getGateEndTimeOffset() {
		return gateEndTimeOffset!=null?gateEndTimeOffset:WDDXTAGS.DATE_NULL_VALUE;
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
		return gateStartDateTime!=null?gateStartTimeOffset:WDDXTAGS.DATE_NULL_VALUE;
	}
	/**
	 * @return Returns the gateStartTimeOffset.
	 */
	public Date getGateStartTimeOffset() {
		return gateStartTimeOffset!=null?gateStartTimeOffset:WDDXTAGS.DATE_NULL_VALUE;
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
	 * @return Returns the offlineInstructions.
	 */
	public String getOfflineInstructions() {
		return offlineInstructions!=null?offlineInstructions:WDDXTAGS.STRING_NULL_VALUE;
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
	/*****************************************************************************
	 * Utility Methods
	 **************************************************************************/
	 
	private  void processActivityType(Object activity){		
		String className = activity.getClass().getName();
		if(activity instanceof GroupingActivity)
			buildGroupingActivityObject(activity);
	/*	else if(activity instanceof ToolActivity)
			//buildToolActivity(activities,(ToolActivity)activity);
		else if(activity instanceof GateActivity)
			//buildGateActivityObject(activities,activity);
		else 			
			buildComplexActivityObject(activities,activity);*/		
	}
	private  void buildGroupingActivityObject(Object activity){
		
		GroupingActivity groupingActivity = (GroupingActivity)activity;
		this.createGroupingID = groupingActivity.getCreateGrouping()!=null?
								groupingActivity.getCreateGrouping().getGroupingId():
								WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
		this.createGroupingUIID = groupingActivity.getCreateGrouping()!=null?
								  groupingActivity.getCreateGrouping().getGroupingUIID():
								  WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
}
