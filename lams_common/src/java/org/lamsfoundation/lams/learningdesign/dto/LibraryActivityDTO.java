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

import java.util.Date;
import java.util.Set;

import org.lamsfoundation.lams.integration.ExtServerToolAdapterMap;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ConditionGateActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.util.HelpUtil;

public class LibraryActivityDTO extends BaseDTO {
    private Integer activityTypeID;
    private Long activityID;
    private Integer activityUIID;
    private Long learningLibraryID; //not sure if this is needed
    private Long learningDesignID;
    private Long libraryActivityID;
    private Long parentActivityID;
    private Integer parentUIID;
    private Integer orderID;
    private Long groupingID;
    private Integer groupingUIID;
    private String description;
    private String activityTitle;
    private String helpURL;
    private Integer xCoord;
    private Integer yCoord;
    private String libraryActivityUIImage;
    private Boolean applyGrouping;
    private Date createDateTime;
    private Integer groupingSupportType;
    /**
     * Name of the file (including the package) that contains the text strings for
     * this activity. e.g. org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties.
     */
    private String languageFile;

    /* Properties Specific to ToolActivity */
    private Long toolID;
    private Long toolContentID;
    private String toolDisplayName;
    private String toolLanguageFile;
    private String toolSignature;
    private Boolean supportsModeration;
    private Boolean supportsContribute;
    private String authoringURL;
    private String adminURL;
    private Boolean supportsOutputs;
    private String extLmsId;

    /* Grouping Activities */

    /* Optional Activities */
    private Integer maxOptions;
    private Integer minOptions;
    private String optionsInstructions;

    /* Sequence Activities */

    /* Gate Activities */
    private Integer gateActivityLevelID;
    private Boolean gateOpen;
    private Long gateStartTimeOffset;
    private Long gateEndTimeOffset;
    private Date gateStartDateTime;
    private Date gateEndDateTime;
    private Boolean gateActivityCompletionBased;

    /** Used for I18N the URLS. Does not need to be sent to clients, so no getter exists. */
    private String languageCode;

    private String[] mappedServers;

    public LibraryActivityDTO(Activity activity, String languageCode) {
	activityTypeID = activity.getActivityTypeId();
	activityID = activity.getActivityId();
	activityUIID = activity.getActivityUIID();
	learningLibraryID = activity.getLearningLibrary() != null ? activity.getLearningLibrary().getLearningLibraryId()
		: null;
	learningDesignID = activity.getLearningDesign() != null ? activity.getLearningDesign().getLearningDesignId()
		: null;
	libraryActivityID = activity.getLibraryActivity() != null ? activity.getLibraryActivity().getActivityId()
		: null;
	parentActivityID = activity.getParentActivity() != null ? activity.getParentActivity().getActivityId() : null;
	parentUIID = activity.getParentUIID();
	orderID = activity.getOrderId();
	groupingID = activity.getGrouping() != null ? activity.getGrouping().getGroupingId() : null;
	groupingUIID = activity.getGroupingUIID();
	description = activity.getDescription();
	activityTitle = activity.getTitle();
	xCoord = activity.getXcoord();
	yCoord = activity.getYcoord();
	libraryActivityUIImage = activity.getLibraryActivityUiImage();
	applyGrouping = activity.getApplyGrouping();
	createDateTime = activity.getCreateDateTime();
	groupingSupportType = activity.getGroupingSupportType();
	languageFile = activity.getLanguageFile();

	this.languageCode = languageCode;
	processActivityType(activity);

    }

    private void processActivityType(Activity activity) {
	if (activity.isGroupingActivity()) {
	    addGroupingActivityAttributes((GroupingActivity) activity);
	} else if (activity.isToolActivity()) {
	    addToolActivityAttributes((ToolActivity) activity);
	} else if (activity.isGateActivity()) {
	    addGateActivityAttributes(activity);
	} else {
	    addComplexActivityAttributes(activity);
	}
    }

    private void addComplexActivityAttributes(Activity activity) {
	if (activity.isOptionsActivity()) {
	    addOptionsActivityAttributes((OptionsActivity) activity);
	} else if (activity.isParallelActivity()) {
	    addParallelActivityAttributes((ParallelActivity) activity);
	} else if (activity.isBranchingActivity()) {
	    addBranchingActivityAttributes((BranchingActivity) activity);
	} else {
	    addSequenceActivityAttributes((SequenceActivity) activity);
	}

    }

    private void addGroupingActivityAttributes(GroupingActivity groupingActivity) {
	/*
	 * Grouping grouping = groupingActivity.getCreateGrouping();
	 * this.groupingDTO = grouping.getGroupingDTO();
	 * this.createGroupingID = grouping.getGroupingId();
	 * this.createGroupingUIID = grouping.getGroupingUIID();
	 * this.groupingType = grouping.getGroupingTypeId();
	 */
    }

    private void addOptionsActivityAttributes(OptionsActivity optionsActivity) {
	maxOptions = optionsActivity.getMaxNumberOfOptions();
	minOptions = optionsActivity.getMinNumberOfOptions();
	optionsInstructions = optionsActivity.getOptionsInstructions();
    }

    private void addParallelActivityAttributes(ParallelActivity activity) {

    }

    private void addBranchingActivityAttributes(BranchingActivity activity) {

    }

    private void addSequenceActivityAttributes(SequenceActivity activity) {
    }

    private void addToolActivityAttributes(ToolActivity toolActivity) {
	Tool tool = toolActivity.getTool();
	if (tool != null) {
	    toolID = tool.getToolId();
	    toolContentID = tool.getDefaultToolContentId();
	    toolDisplayName = tool.getToolDisplayName();
	    toolLanguageFile = tool.getLanguageFile();
	    toolSignature = tool.getToolSignature();
	    authoringURL = tool.getAuthorUrl();
	    adminURL = tool.getAdminUrl();
	    supportsOutputs = tool.getSupportsOutputs();
	    extLmsId = tool.getExtLmsId();

	    Set<ExtServerToolAdapterMap> mappedServersArray = tool.getMappedServers();
	    if (mappedServersArray != null) {
		this.mappedServers = new String[mappedServersArray.size()];
		int i = 0;
		for (ExtServerToolAdapterMap map : mappedServersArray) {
		    mappedServers[i] = map.getExtServer().getServerid();
		    i++;
		}
	    }

	    helpURL = HelpUtil.constructToolURL(tool.getHelpUrl(), toolSignature, "", languageCode);
	}

    }

    private void addGateActivityAttributes(Object activity) {
	if (activity instanceof SynchGateActivity) {
	    addSynchGateActivityAttributes((SynchGateActivity) activity);
	} else if (activity instanceof PermissionGateActivity) {
	    addPermissionGateActivityAttributes((PermissionGateActivity) activity);
	} else if (activity instanceof ConditionGateActivity) {
	    addConditionGateActivityAttributes((ConditionGateActivity) activity);
	} else {
	    addScheduleGateActivityAttributes((ScheduleGateActivity) activity);
	}
	GateActivity gateActivity = (GateActivity) activity;
	gateActivityLevelID = gateActivity.getGateActivityLevelId();
	gateOpen = gateActivity.getGateOpen();

    }

    private void addSynchGateActivityAttributes(SynchGateActivity activity) {
    }

    private void addPermissionGateActivityAttributes(PermissionGateActivity activity) {
    }

    private void addScheduleGateActivityAttributes(ScheduleGateActivity activity) {
	gateStartTimeOffset = activity.getGateStartTimeOffset();
	gateEndTimeOffset = activity.getGateEndTimeOffset();
	gateActivityCompletionBased = activity.getGateActivityCompletionBased();
    }

    private void addConditionGateActivityAttributes(ConditionGateActivity activity) {
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
     * @return Returns the applyGrouping.
     */
    public Boolean getApplyGrouping() {
	return applyGrouping;
    }

    /**
     * @return Returns the createDateTime.
     */
    public Date getCreateDateTime() {
	return createDateTime;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the groupingID.
     */
    public Long getGroupingID() {
	return groupingID;
    }

    /**
     * @return Returns the groupingSupportType.
     */
    public Integer getGroupingSupportType() {
	return groupingSupportType;
    }

    /**
     * @return Returns the groupingUIID.
     */
    public Integer getGroupingUIID() {
	return groupingUIID;
    }

    /**
     *
     * @return Return the helpURL
     */
    public String getHelpURL() {
	return helpURL;
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
     * @return Returns the libraryActivityUIImage.
     */
    public String getLibraryActivityUIImage() {
	return libraryActivityUIImage;
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
     * @return Returns the xCoord.
     */
    public Integer getxCoord() {
	return xCoord;
    }

    /**
     * @return Returns the yCoord.
     */
    public Integer getyCoord() {
	return yCoord;
    }

    /**
     * @return Returns the authoringURL.
     */
    public String getAuthoringURL() {
	return authoringURL;
    }

    /**
     * @return Returns the adminURL.
     */
    public String getAdminURL() {
	return adminURL;
    }

    /**
     * @return Returns the supportsContribute.
     */
    public Boolean getSupportsContribute() {
	return supportsContribute;
    }

    /**
     * @return Returns the supportsModeration.
     */
    public Boolean getSupportsModeration() {
	return supportsModeration;
    }

    /**
     * @return Returns the toolContentID.
     */
    public Long getToolContentID() {
	return toolContentID;
    }

    /**
     * @return Returns the toolDisplayName.
     */
    public String getToolDisplayName() {
	return toolDisplayName;
    }

    /**
     * @return Returns the toolID.
     */
    public Long getToolID() {
	return toolID;
    }

    /**
     *
     * @return Returns the toolSignature
     */
    public String getToolSignature() {
	return toolSignature;
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
     * @return Returns the languge file for the activity (for I8N).
     */
    public String getLanguageFile() {
	return languageFile;
    }

    /**
     * @return Returns the languge file for the tool (for I8N).
     */
    public String getToolLanguageFile() {
	return toolLanguageFile;
    }

    /** Set the activity's description */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Set the activity's help url
     *
     * @param helpURL
     */
    public void setHelpURL(String helpURL) {
	this.helpURL = helpURL;
    }

    /** Set the activity's title */
    public void setActivityTitle(String title) {
	activityTitle = title;
    }

    /** Set the tool's display name (similar to title) */
    public void setToolDisplayName(String toolDisplayName) {
	this.toolDisplayName = toolDisplayName;
    }

    /**
     * Set the tool's signature
     *
     * @param toolSignature
     */

    public void setToolSignature(String toolSignature) {
	this.toolSignature = toolSignature;
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

    public String[] getMappedServers() {
	return mappedServers;
    }

    public void setMappedServers(String[] mappedServers) {
	this.mappedServers = mappedServers;
    }

}