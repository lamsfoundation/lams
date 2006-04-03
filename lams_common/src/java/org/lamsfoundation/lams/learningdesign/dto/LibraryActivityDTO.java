/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
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

import java.util.Date;

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
import org.lamsfoundation.lams.tool.Tool;

public class LibraryActivityDTO extends BaseDTO
{
    private Integer activityTypeID;
    private Long activityID;
    private Integer activityCategoryID;
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
	private String title;
	private String helpText;
	private Integer xCoord;
	private Integer yCoord;
	private String libraryActivityUIImage;
	private Boolean applyGrouping;
	private Boolean runOffline;
	private Boolean defineLater;
	private Date createDateTime;
	private Integer groupingSupportType;
	/** Name of the file (including the package) that contains the text strings for
	 * this activity. e.g. org.lamsfoundation.lams.tool.sbmt.SbmtResources.properties. */
	private String languageFile;
	
	/* Properties Specific to ToolActivity */
	private Long toolID;
	private Long toolContentID;
	private String toolDisplayName;
	private String toolLanguageFile;
	private Boolean supportsDefineLater;
	private Boolean supportsRunOffline;
	private Boolean supportsModeration;
	private Boolean supportsContribute;	
	private String authoringURL;
	
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
		
	public LibraryActivityDTO (Activity activity)
	{
	    this.activityTypeID = activity.getActivityTypeId();
	    this.activityID = activity.getActivityId();
	    this.activityCategoryID = activity.getActivityCategoryID();
	    this.activityUIID = activity.getActivityUIID();
	    this.learningLibraryID = activity.getLearningLibrary() != null ? activity.getLearningLibrary().getLearningLibraryId() : null;
	    this.learningDesignID = activity.getLearningDesign() != null ? activity.getLearningDesign().getLearningDesignId() : null;
	    this.libraryActivityID = activity.getLibraryActivity() != null ? activity.getLibraryActivity().getActivityId() : null;
	    this.parentActivityID = activity.getParentActivity() != null ? activity.getParentActivity().getActivityId() : null;
	    this.parentUIID = activity.getParentUIID();
	    this.orderID = activity.getOrderId();
	    this.groupingID = activity.getGrouping()!= null? activity.getGrouping().getGroupingId(): null;
	    this.groupingUIID = activity.getGroupingUIID();
	    this.description = activity.getDescription();
	    this.title = activity.getTitle();
	    this.helpText = activity.getHelpText();
	    this.xCoord = activity.getXcoord();
	    this.yCoord = activity.getYcoord();
	    this.libraryActivityUIImage = activity.getLibraryActivityUiImage();
	    this.applyGrouping = activity.getApplyGrouping();
	    this.runOffline = activity.getRunOffline();
	    this.defineLater = activity.getDefineLater();
	    this.createDateTime = activity.getCreateDateTime();
	    this.groupingSupportType = activity.getGroupingSupportType();
	    this.languageFile = activity.getLanguageFile();
	    processActivityType(activity);
	    
	}
	
	private  void processActivityType(Activity activity){
		if(activity.isGroupingActivity())
			 addGroupingActivityAttributes((GroupingActivity)activity);
		else if(activity.isToolActivity())
			 addToolActivityAttributes((ToolActivity)activity);
		else if(activity.isGateActivity())
			 addGateActivityAttributes(activity);
		else 			
			 addComplexActivityAttributes(activity);		
	}
	
	private void addComplexActivityAttributes(Activity activity){		
		if(activity.isOptionsActivity())
			addOptionsActivityAttributes((OptionsActivity)activity);
		else if (activity.isParallelActivity())
			addParallelActivityAttributes((ParallelActivity)activity);
		else
			addSequenceActivityAttributes((SequenceActivity)activity);
		
	}
	private void addGroupingActivityAttributes(GroupingActivity groupingActivity){
		/*	Grouping grouping = groupingActivity.getCreateGrouping();		
		this.groupingDTO = grouping.getGroupingDTO();
		this.createGroupingID = grouping.getGroupingId();
		this.createGroupingUIID = grouping.getGroupingUIID();
		this.groupingType = grouping.getGroupingTypeId(); */
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
	    Tool tool = toolActivity.getTool();
	    if (tool != null)
	    {
	        this.toolID = tool.getToolId();
			this.toolContentID = new Long(tool.getDefaultToolContentId());
			this.toolDisplayName = tool.getToolDisplayName();
			this.toolLanguageFile = tool.getLanguageFile();
			this.supportsDefineLater = new Boolean(tool.getSupportsDefineLater());
			this.supportsRunOffline = new Boolean(tool.getSupportsRunOffline());
			this.supportsModeration = new Boolean(tool.getSupportsModeration());
			this.supportsContribute = new Boolean(tool.getSupportsContribute());
			this.authoringURL = tool.getAuthorUrl();
	    }
	     
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
     * @return Returns the runOffline.
     */
    public Boolean getRunOffline() {
        return runOffline;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
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
     * @return Returns the supportsContribute.
     */
    public Boolean getSupportsContribute() {
        return supportsContribute;
    }
    /**
     * @return Returns the supportsDefineLater.
     */
    public Boolean getSupportsDefineLater() {
        return supportsDefineLater;
    }
    /**
     * @return Returns the supportsModeration.
     */
    public Boolean getSupportsModeration() {
        return supportsModeration;
    }
    /**
     * @return Returns the supportsRunOffline.
     */
    public Boolean getSupportsRunOffline() {
        return supportsRunOffline;
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

	/** Set the activity's help text */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	/** Set the activity's title */
	public void setTitle(String title) {
		this.title = title;
	}

	/** Set the tool's display name (similar to title) */
	public void setToolDisplayName(String toolDisplayName) {
		this.toolDisplayName = toolDisplayName;
	}


}