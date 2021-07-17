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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ConditionGateActivity;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PasswordGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.ToolBranchingActivity;
import org.lamsfoundation.lams.util.HelpUtil;

/**
 * @author Manpreet Minhas
 */
public class AuthoringActivityDTO extends BaseDTO {

    /*******************************************************************************************************************
     * Attributes
     ******************************************************************************************************************/

    /** identifier field */
    private Long activityID;

    /**
     * Authoring generated value. Unique per LearningDesign. Required by Authorig only.
     */
    private Integer activityUIID;

    /** Description of the activity */
    private String description;

    /** Title of the activity */
    private String activityTitle;

    /** Help URL for the activity */
    private String helpURL;

    /**
     * UI specific attribute indicating the position of the activity
     */
    private Integer xCoord;

    /**
     * UI specific attribute indicating the position of the activity
     */
    private Integer yCoord;

    /**
     * The activity that acts as a container/parent for this activity. Normally would be one of the complex activities
     * which have child activities defined inside them.
     */
    private Long parentActivityID;

    /** the activity_ui_id of the parent activity */
    private Integer parentUIID;

    /** The type of activity */
    private Integer activityTypeID;

    /** The Grouping that applies to this activity */
    private Long groupingID;

    /**
     * The grouping_ui_id of the Grouping that applies that to this activity
     */
    private Integer groupingUIID;

    /**
     * Indicates the order in which the activities appear inside complex activities. Starts from 0, 1 and so on.
     */
    private Integer orderID;

    /** The LearningDesign to which this activity belongs */
    private Long learningDesignID;

    /** The LearningLibrary of which this activity is a part */
    private Long learningLibraryID;

    /** Date this activity was created */
    private Date createDateTime;

    /** Maximum number of activities to be attempted */
    private Integer maxOptions;

    /** Maximum number of floating activities allow */
    private Integer maxActivities;

    /** Minimum number of activities to be attempted */
    private Integer minOptions;

    /** Instructions for OptionsActivity */
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

    private Integer gateActivityLevelID;

    private Boolean gateOpen;

    private Long gateStartTimeOffset;

    private Long gateEndTimeOffset;

    private Date gateStartDateTime;

    private Date gateEndDateTime;

    private Boolean gateActivityCompletionBased;

    private boolean gateStopAtPrecedingActivity;

    private String gatePassword;

    private Boolean applyGrouping;

    private Integer groupingSupportType;

    private Integer groupingType;

    /**
     * The image that represents the icon of this activity in the UI
     */
    private String libraryActivityUIImage;

    private Long createGroupingID;

    private Integer createGroupingUIID;

    private Boolean readOnly;

    /**
     * An activity is initialised if it is ready to be used in lesson ie the tool content is set up, schedule gates are
     * scheduled, etc. Used to detect which activities need to be initialised for live edit.
     */
    private Boolean initialised;

    /**
     * If stopAfterActivity is true, then the progress engine should "end" the lesson at this point. Used to arbitrarily
     * stop somewhere in a design, such as at the end of the branch. The normal final activity of a design does not
     * necessarily have this set - the progress engine will just stop when it runs out of transitions to follow.
     */
    private Boolean stopAfterActivity;

    /*
     * Server will send Grouping objects as an array (in the Groupings array) rather than being part of the
     * GroupingActivity. For the groupings array see LearningDesignDTO.
     */
    // private GroupingDTO groupingDTO;
    /**
     * Single Library can have one or more activities defined inside it. This field indicates which activity is this.
     */
    private Long libraryActivityID;

    /**
     * Name of the file (including the package) that contains the text strings for this activity. e.g.
     * org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
     */
    private String languageFile;

    /**
     * List of the UIIDs of the activities that are input activities for this activity. This array will only contain
     * integers.
     */
    private ArrayList<Integer> inputActivities;

    /**
     * List of all the competence mappings for this activity, only applies to tool activities
     */
    private ArrayList<String> competenceMappingTitles;

    private PlannerActivityMetadataDTO plannerMetadataDTO;

    /** List of the UIIDs of the activities that are input activities for this activity */
    private Integer toolActivityUIID;

    private List<String> evaluation;

    /**
     * Used by a sequence activity to determine the start of the transition based sequence and used by tool based
     * branching to determine the default branch.
     */
    private Integer defaultActivityUIID;

    private Integer startXCoord;
    private Integer startYCoord;
    private Integer endXCoord;
    private Integer endYCoord;

    private Boolean branchingOrderedAsc;

    /** Used for I18N the URLS. Does not need to be sent to clients, so no getter exists. */
    private String languageCode;

    private Boolean supportsOutputs;

    /**
     * This string identifies the tool as a tool adapter tool, null otherwise
     */
    private String extLmsId;

    /*******************************************************************************************************************
     * Constructors
     ******************************************************************************************************************/
    public AuthoringActivityDTO() {
    }

    public AuthoringActivityDTO(Activity activity, ArrayList<BranchActivityEntryDTO> branchMappings,
	    String languageCode) {
	activityID = activity.getActivityId();
	activityUIID = activity.getActivityUIID();
	description = activity.getDescription();
	activityTitle = activity.getTitle();
	xCoord = activity.getXcoord();
	yCoord = activity.getYcoord();
	parentActivityID = activity.getParentActivity() != null ? activity.getParentActivity().getActivityId() : null;
	parentUIID = activity.getParentUIID();
	activityTypeID = activity.getActivityTypeId();

	groupingID = activity.getGrouping() != null ? activity.getGrouping().getGroupingId() : null;

	groupingUIID = activity.getGroupingUIID();
	orderID = activity.getOrderId();
	learningDesignID = activity.getLearningDesign() != null ? activity.getLearningDesign().getLearningDesignId()
		: null;
	learningLibraryID = activity.getLearningLibrary() != null ? activity.getLearningLibrary().getLearningLibraryId()
		: null;
	createDateTime = activity.getCreateDateTime();
	languageFile = activity.getLanguageFile();
	libraryActivityUIImage = activity.getLibraryActivityUiImage();
	libraryActivityID = activity.getLibraryActivity() != null ? activity.getLibraryActivity().getActivityId()
		: null;
	applyGrouping = activity.getApplyGrouping();
	groupingSupportType = activity.getGroupingSupportType();
	readOnly = activity.getReadOnly();
	initialised = activity.isInitialised();
	stopAfterActivity = activity.isStopAfterActivity();

	inputActivities = activity.getInputActivityUIIDs();
	toolActivityUIID = activity.getToolInputActivityUIID();

	this.languageCode = languageCode;

	processActivityType(activity, branchMappings);
    }

    private void processActivityType(Activity activity, ArrayList<BranchActivityEntryDTO> branchMappings) {
	if (activity.isGroupingActivity()) {
	    addGroupingActivityAttributes((GroupingActivity) activity);
	} else if (activity.isToolActivity()) {
	    addToolActivityAttributes((ToolActivity) activity);
	} else if (activity.isGateActivity()) {
	    addGateActivityAttributes(activity, branchMappings);
	} else {
	    addComplexActivityAttributes(activity, branchMappings);
	}
    }

    private void addComplexActivityAttributes(Activity activity, ArrayList<BranchActivityEntryDTO> branchMappings) {
	ComplexActivity complex = (ComplexActivity) activity;
	if (complex.getDefaultActivity() != null) {
	    defaultActivityUIID = complex.getDefaultActivity().getActivityUIID();
	}

	if (activity.isOptionsWithSequencesActivity()) {
	    addOptionsWithSequencesActivityAttributes((OptionsWithSequencesActivity) activity);
	} else if (activity.isOptionsActivity()) {
	    addOptionsActivityAttributes((OptionsActivity) activity);
	} else if (activity.isParallelActivity()) {
	    addParallelActivityAttributes((ParallelActivity) activity);
	} else if (activity.isBranchingActivity()) {
	    addBranchingActivityAttributes((BranchingActivity) activity);
	} else if (activity.isFloatingActivity()) {
	    addFloatingActivityAttributes((FloatingActivity) activity);
	} else {
	    addSequenceActivityAttributes((SequenceActivity) activity, branchMappings);
	}

    }

    private void addGroupingActivityAttributes(GroupingActivity groupingActivity) {
	Grouping grouping = groupingActivity.getCreateGrouping();

	// this.groupingDTO = grouping.getGroupingDTO();
	createGroupingID = grouping.getGroupingId();
	createGroupingUIID = grouping.getGroupingUIID();
	// this.groupingType = grouping.getGroupingTypeId();
	adminURL = groupingActivity.getSystemTool().getAdminUrl();
    }

    private void addOptionsActivityAttributes(OptionsActivity optionsActivity) {
	maxOptions = optionsActivity.getMaxNumberOfOptions();
	minOptions = optionsActivity.getMinNumberOfOptions();
	optionsInstructions = optionsActivity.getOptionsInstructions();
    }

    private void addOptionsWithSequencesActivityAttributes(OptionsWithSequencesActivity optionsActivity) {
	addOptionsActivityAttributes(optionsActivity);

	startXCoord = optionsActivity.getStartXcoord();
	startYCoord = optionsActivity.getStartYcoord();
	endXCoord = optionsActivity.getEndXcoord();
	endYCoord = optionsActivity.getEndYcoord();
    }

    private void addParallelActivityAttributes(ParallelActivity activity) {
    }

    private void addBranchingActivityAttributes(BranchingActivity activity) {
	startXCoord = activity.getStartXcoord();
	startYCoord = activity.getStartYcoord();
	endXCoord = activity.getEndXcoord();
	endYCoord = activity.getEndYcoord();
	if (activity.isToolBranchingActivity()) {
	    branchingOrderedAsc = ((ToolBranchingActivity) activity).getBranchingOrderedAsc();
	}
    }

    private void addFloatingActivityAttributes(FloatingActivity floatingActivity) {
	maxActivities = floatingActivity.getMaxNumberOfActivities();
    }

    private void addSequenceActivityAttributes(SequenceActivity activity,
	    ArrayList<BranchActivityEntryDTO> branchMappings) {

	Activity parentActivity = activity.getParentActivity();
	Integer toolActivityUIID = parentActivity == null ? null : parentActivity.getToolInputActivityUIID();

	if (activity.getBranchEntries() != null) {
	    Iterator<BranchActivityEntry> iter = activity.getBranchEntries().iterator();
	    while (iter.hasNext()) {
		BranchActivityEntry ba = iter.next();
		branchMappings.add(ba.getBranchActivityEntryDTO(toolActivityUIID));
	    }
	}
    }

    private void addToolActivityAttributes(ToolActivity toolActivity) {
	toolContentID = toolActivity.getToolContentId();
	toolID = toolActivity.getTool().getToolId();
	toolSignature = toolActivity.getTool().getToolSignature();
	authoringURL = toolActivity.getTool().getAuthorUrl();
	monitoringURL = toolActivity.getTool().getMonitorUrl();
	adminURL = toolActivity.getTool().getAdminUrl();
	toolDisplayName = toolActivity.getTool().getToolDisplayName();
	toolVersion = toolActivity.getTool().getToolVersion();
	supportsOutputs = toolActivity.getTool().getSupportsOutputs();
	extLmsId = toolActivity.getTool().getExtLmsId();
	helpURL = HelpUtil.constructToolURL(toolActivity.getTool().getHelpUrl(), toolSignature, "", languageCode);

	competenceMappingTitles = new ArrayList<>();
	if (toolActivity.getCompetenceMappings() != null) {

	    Set<CompetenceMapping> competenceMappings = toolActivity.getCompetenceMappings();
	    for (CompetenceMapping competenceMapping : competenceMappings) {
		// CompetenceMappingDTO competenceMappingDTO = new CompetenceMappingDTO(competenceMapping);
		String competenceMappingTitle = competenceMapping.getCompetence().getTitle();

		competenceMappingTitles.add(competenceMappingTitle);
	    }
	}

	if (toolActivity.getEvaluation() != null) {
	    evaluation = new ArrayList<>();
	    ActivityEvaluation eval = toolActivity.getEvaluation();
	    evaluation.add(eval.getToolOutputDefinition());
	    if (eval.getWeight() != null) {
		evaluation.add(String.valueOf(eval.getWeight()));
	    }
	}

	plannerMetadataDTO = toolActivity.getPlannerMetadata() == null ? null
		: new PlannerActivityMetadataDTO(toolActivity.getPlannerMetadata());

    }

    private void addGateActivityAttributes(Object activity, ArrayList<BranchActivityEntryDTO> branchMappings) {
	if (activity instanceof ConditionGateActivity) {
	    addConditionGateActivityAttributes((ConditionGateActivity) activity, branchMappings);
	} else if (activity instanceof PasswordGateActivity) {
	    addPasswordGateActivityAttributes((PasswordGateActivity) activity);
	} else if (activity instanceof ScheduleGateActivity) {
	    addScheduleGateActivityAttributes((ScheduleGateActivity) activity);
	}
	GateActivity gateActivity = (GateActivity) activity;
	gateActivityLevelID = gateActivity.getGateActivityLevelId();
	gateOpen = gateActivity.getGateOpen();
	gateStopAtPrecedingActivity = gateActivity.getGateStopAtPrecedingActivity();
	adminURL = gateActivity.getSystemTool().getAdminUrl();
    }

    private void addConditionGateActivityAttributes(ConditionGateActivity activity,
	    ArrayList<BranchActivityEntryDTO> branchMappings) {
	if (activity.getBranchActivityEntries() != null) {
	    Iterator<BranchActivityEntry> iter = activity.getBranchActivityEntries().iterator();
	    while (iter.hasNext()) {
		BranchActivityEntry ba = iter.next();
		branchMappings.add(ba.getBranchActivityEntryDTO(toolActivityUIID));
	    }
	}
    }

    private void addPasswordGateActivityAttributes(PasswordGateActivity activity) {
	gatePassword = activity.getGatePassword();
    }

    private void addScheduleGateActivityAttributes(ScheduleGateActivity activity) {
	gateStartTimeOffset = activity.getGateStartTimeOffset();
	gateEndTimeOffset = activity.getGateEndTimeOffset();
	gateActivityCompletionBased = activity.getGateActivityCompletionBased();
    }

    /*******************************************************************************************************************
     * Getters
     ******************************************************************************************************************/

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

    public Boolean getGateActivityCompletionBased() {
	return gateActivityCompletionBased;
    }

    public void setGateActivityCompletionBased(Boolean gateActivityCompletionBased) {
	this.gateActivityCompletionBased = gateActivityCompletionBased;
    }

    public boolean isGateStopAtPrecedingActivity() {
	return gateStopAtPrecedingActivity;
    }

    public void setGateStopAtPrecedingActivity(boolean gateStopAtPrecedingActivity) {
	this.gateStopAtPrecedingActivity = gateStopAtPrecedingActivity;
    }

    public String getGatePassword() {
	return gatePassword;
    }

    public void setGatePassword(String gatePassword) {
	this.gatePassword = gatePassword;
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
     * @return Returns the maxActivities.
     */
    public Integer getMaxActivities() {
	return maxActivities;
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
     * Name of the file (including the package) that contains the text strings for this activity. e.g.
     * org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
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

    public String getHelpURL() {
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

    /*******************************************************************************************************************
     * Setters
     ******************************************************************************************************************/

    /**
     * @param activityID
     *            The activityID to set.
     */
    public void setActivityID(Long activityId) {
	activityID = activityId;
    }

    /**
     * @param activityTypeID
     *            The activityTypeID to set.
     */
    public void setActivityTypeID(Integer activityTypeId) {
	activityTypeID = activityTypeId;
    }

    /**
     * @param activityUIID
     *            The activityUIID to set.
     */
    public void setActivityUIID(Integer activityUIID) {
	this.activityUIID = activityUIID;
    }

    /**
     * @param createDateTime
     *            The createDateTime to set.
     */
    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    /**
     * @param createGroupingID
     *            The createGroupingID to set.
     */
    public void setCreateGroupingID(Long createGroupingID) {
	this.createGroupingID = createGroupingID;
    }

    /**
     * @param createGroupingUIID
     *            The createGroupingUIID to set.
     */
    public void setCreateGroupingUIID(Integer createGroupingUIID) {
	this.createGroupingUIID = createGroupingUIID;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @param gateActivityLevelID
     *            The gateActivityLevelID to set.
     */
    public void setGateActivityLevelID(Integer gateActivityLevelID) {
	this.gateActivityLevelID = gateActivityLevelID;
    }

    /**
     * @param gateEndDateTime
     *            The gateEndDateTime to set.
     */
    public void setGateEndDateTime(Date gateEndDateTime) {
	this.gateEndDateTime = gateEndDateTime;
    }

    /**
     * @param gateEndTimeOffset
     *            The gateEndTimeOffset to set.
     */
    public void setGateEndTimeOffset(Long gateEndTimeOffset) {
	this.gateEndTimeOffset = gateEndTimeOffset;
    }

    /**
     * @param gateOpen
     *            The gateOpen to set.
     */
    public void setGateOpen(Boolean gateOpen) {
	this.gateOpen = gateOpen;
    }

    /**
     * @param gateStartDateTime
     *            The gateStartDateTime to set.
     */
    public void setGateStartDateTime(Date gateStartDateTime) {
	this.gateStartDateTime = gateStartDateTime;
    }

    /**
     * @param gateStartTimeOffset
     *            The gateStartTimeOffset to set.
     */
    public void setGateStartTimeOffset(Long gateStartTimeOffset) {
	this.gateStartTimeOffset = gateStartTimeOffset;
    }

    /**
     * @param groupingID
     *            The groupingID to set.
     */
    public void setGroupingID(Long groupingID) {
	this.groupingID = groupingID;
    }

    /**
     * @param groupingUIID
     *            The groupingUIID to set.
     */
    public void setGroupingUIID(Integer groupingUIID) {
	this.groupingUIID = groupingUIID;
    }

    /**
     * @param learningDesignID
     *            The learningDesignID to set.
     */
    public void setLearningDesignID(Long learningDesignID) {
	this.learningDesignID = learningDesignID;
    }

    /**
     * @param learningLibraryID
     *            The learningLibraryID to set.
     */
    public void setLearningLibraryID(Long learningLibraryID) {
	this.learningLibraryID = learningLibraryID;
    }

    /**
     * @param libraryActivityID
     *            The libraryActivityID to set.
     */
    public void setLibraryActivityID(Long libraryActivityID) {
	this.libraryActivityID = libraryActivityID;
    }

    /**
     * @param libraryActivityUiImage
     *            The libraryActivityUiImage to set.
     */
    public void setLibraryActivityUIImage(String libraryActivityUiImage) {
	libraryActivityUIImage = libraryActivityUiImage;
    }

    /**
     * @param maxActivities
     *            The maxActivities to set.
     */
    public void setMaxActivities(Integer maxActivities) {
	this.maxActivities = maxActivities;
    }

    /**
     * @param maxOptions
     *            The maxOptions to set.
     */
    public void setMaxOptions(Integer maxOptions) {
	this.maxOptions = maxOptions;
    }

    /**
     * @param minOptions
     *            The minOptions to set.
     */
    public void setMinOptions(Integer minOptions) {
	this.minOptions = minOptions;
    }

    /**
     * @param optionsInstructions
     *            The optionsInstructions to set.
     */
    public void setOptionsInstructions(String optionsInstructions) {
	this.optionsInstructions = optionsInstructions;
    }

    /**
     * @param orderID
     *            The orderID to set.
     */
    public void setOrderID(Integer orderID) {
	this.orderID = orderID;
    }

    /**
     * @param parentActivityID
     *            The parentActivityID to set.
     */
    public void setParentActivityID(Long parentActivityID) {
	this.parentActivityID = parentActivityID;
    }

    /**
     * @param parentUIID
     *            The parentUIID to set.
     */
    public void setParentUIID(Integer parentUIID) {
	this.parentUIID = parentUIID;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setActivityTitle(String title) {
	activityTitle = title;
    }

    /**
     * @param toolContentID
     *            The toolContentID to set.
     */
    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
    }

    /**
     * @param toolID
     *            The toolID to set.
     */
    public void setToolID(Long toolID) {
	this.toolID = toolID;
    }

    /**
     * @param xcoord
     *            The xcoord to set.
     */
    public void setxCoord(Integer xcoord) {
	xCoord = xcoord;
    }

    /**
     * @param ycoord
     *            The ycoord to set.
     */
    public void setyCoord(Integer ycoord) {
	yCoord = ycoord;
    }

    /**
     * @param applyGrouping
     *            The applyGrouping to set.
     */
    public void setApplyGrouping(Boolean applyGrouping) {
	this.applyGrouping = applyGrouping;
    }

    /**
     * @param groupingSupportType
     *            The groupingSupportType to set.
     */
    public void setGroupingSupportType(Integer groupingSupportType) {
	this.groupingSupportType = groupingSupportType;
    }

    /**
     * @param groupingType
     *            The groupingType to set.
     */
    public void setGroupingType(Integer groupingType) {
	this.groupingType = groupingType;
    }

    /**
     *
     * @param readOnly
     *            The readOnly to set.
     */
    public void setReadOnly(Boolean readOnly) {
	this.readOnly = readOnly;
    }

    public void setAuthoringURL(String toolAuthoringURL) {
	authoringURL = toolAuthoringURL;
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
	this.defaultActivityUIID = defaultActivityUIID;
    }

    public void setEndXCoord(Integer endXCoord) {
	this.endXCoord = endXCoord;
    }

    public void setEndYCoord(Integer endYCoord) {
	this.endYCoord = endYCoord;
    }

    public void setStartXCoord(Integer startXCoord) {
	this.startXCoord = startXCoord;
    }

    public void setStartYCoord(Integer startYCoord) {
	this.startYCoord = startYCoord;
    }

    public Boolean getBranchingOrderedAsc() {
	return branchingOrderedAsc;
    }

    public void setBranchingOrderedAsc(Boolean branchingOrderedAsc) {
	this.branchingOrderedAsc = branchingOrderedAsc;
    }

    public String getAdminURL() {
	return adminURL;
    }

    public void setAdminURL(String adminURL) {
	this.adminURL = adminURL;
    }

    public ArrayList<Integer> getInputActivities() {
	return inputActivities;
    }

    public void setInputActivities(ArrayList<Integer> inputActivities) {
	this.inputActivities = inputActivities;
    }

    public Integer getToolActivityUIID() {
	return toolActivityUIID;
    }

    public void setToolActivityUIID(Integer toolActivityUIID) {
	this.toolActivityUIID = toolActivityUIID;
    }

    public Boolean getSupportsOutputs() {
	return supportsOutputs;
    }

    public void setSupportsOutputs(Boolean supportsOutputs) {
	this.supportsOutputs = supportsOutputs;
    }

    public String getExtLmsId() {
	return extLmsId;
    }

    public void setExtLmsId(String extLmsId) {
	this.extLmsId = extLmsId;
    }

    public ArrayList<String> getCompetenceMappingTitles() {
	return competenceMappingTitles;
    }

    public void setCompetenceMappingTitles(ArrayList<String> competenceMappingTitles) {
	this.competenceMappingTitles = competenceMappingTitles;
    }

    public List<String> getEvaluation() {
	return evaluation;
    }

    public void setEvaluation(List<String> evaluation) {
	this.evaluation = evaluation;
    }

    public PlannerActivityMetadataDTO getPlannerMetadataDTO() {
	return plannerMetadataDTO;
    }

    public void setPlannerMetadataDTO(PlannerActivityMetadataDTO plannerActivityMetadata) {
	this.plannerMetadataDTO = plannerActivityMetadata;
    }
}