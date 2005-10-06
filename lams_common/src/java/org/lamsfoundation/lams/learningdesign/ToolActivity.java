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
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.ToolActivityStrategy;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Manpreet Minhas
 * @hibernate.class
 */
public class ToolActivity extends SimpleActivity implements Serializable
{
    
    /** Holds value of property toolContentId. */
    private Long toolContentId;
    
    /** Holds value of property tool.*/
    private Tool tool;
    
    /** List of sessions associated with this ToolActivity */
    private Set toolSessions;
    
   
       
    /** full constructor */
    public ToolActivity(Long activityId,
            Integer id,
            String description,
            String title,
            Integer xcoord,
            Integer ycoord,
            Integer orderId,
            Boolean defineLater,
            java.util.Date createDateTime,
            LearningLibrary learningLibrary,
            Activity parentActivity,
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign,
            Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Tool tool,
            Long toolContentId)
    {
        super(activityId,
                id,
                description,
                title,
                xcoord,
                ycoord,
                orderId,
                defineLater,
                createDateTime,
                learningLibrary,
                parentActivity,
				libraryActivity,
				parentUIID,
                learningDesign,
                grouping,
                activityTypeId,
                transitionTo,
				transitionFrom);
        this.tool = tool;
        this.toolContentId = toolContentId;
        super.simpleActivityStrategy = new ToolActivityStrategy(this);
    }
    
    /** default constructor */
    public ToolActivity(){
    	super.simpleActivityStrategy = new ToolActivityStrategy(this);
    }
    
    /** minimal constructor */
    public ToolActivity(Long activityId,
            Boolean defineLater,
            java.util.Date createDateTime,
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
            org.lamsfoundation.lams.learningdesign.Activity parentActivity,
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
            org.lamsfoundation.lams.learningdesign.Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Tool tool,
            Long toolContentId)
    {
        super(activityId,
                defineLater,
                createDateTime,
                learningLibrary,
                parentActivity,
                learningDesign,
                grouping,
                activityTypeId,
                transitionTo,
				transitionFrom);
        this.tool = tool;
        this.toolContentId = toolContentId;
        super.simpleActivityStrategy = new ToolActivityStrategy(this);
    }
    
    /**
     * Makes a copy of the ToolActivity for authoring, preview and monitoring enviornment 
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return ToolActivity Returns a deep-copy of the originalActivity
     */
    public static ToolActivity createCopy(ToolActivity originalActivity){    	
    	ToolActivity newToolActivity = new ToolActivity();
    	 
		newToolActivity.setTool(originalActivity.getTool());
		newToolActivity.setToolContentId(originalActivity.getToolContentId());    	
		newToolActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newToolActivity.setDescription(originalActivity.getDescription());
    	newToolActivity.setTitle(originalActivity.getTitle());
    	newToolActivity.setHelpText(originalActivity.getHelpText());
    	newToolActivity.setXcoord(originalActivity.getXcoord());
    	newToolActivity.setYcoord(originalActivity.getYcoord());
    	newToolActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	
    	newToolActivity.setGroupingSupportType(originalActivity.getGroupingSupportType());
    	newToolActivity.setApplyGrouping(originalActivity.getApplyGrouping());
    	newToolActivity.setGrouping(originalActivity.getGrouping());
    	
    	newToolActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	newToolActivity.setOrderId(originalActivity.getOrderId());
    	newToolActivity.setDefineLater(originalActivity.getDefineLater());
    	newToolActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newToolActivity.setCreateDateTime(new Date());
    	newToolActivity.setRunOffline(originalActivity.getRunOffline());
    	newToolActivity.setActivityCategoryID(originalActivity.getActivityCategoryID());
    	newToolActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());
    	newToolActivity.setLibraryActivity(originalActivity.getLibraryActivity());
    	return newToolActivity;
    }    
    
    /**
     * Factory method to create a new tool session for the user when he is
     * running current activity.
     * @param learner the user who should be using this tool session.
     * @return the new tool session.
     */
    public ToolSession createToolSessionForActivity(User learner,Lesson lesson)
    {
        if(this.getApplyGrouping().booleanValue())
            return new GroupedToolSession(this,
                                          new Date(System.currentTimeMillis()),
                                          ToolSession.STARTED_STATE,
                                          this.getGroupFor(learner),
                                          lesson);
        else
            return new NonGroupedToolSession(this,
                                             new Date(System.currentTimeMillis()),
                                             ToolSession.STARTED_STATE,
                                             learner,
                                             lesson);
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }
    
    /**
     * Getter for property toolContentId.
     * @return Value of property toolContentId.
     */
    public Long getToolContentId()
    {
        
        return this.toolContentId;
    }
    
    /**
     * Setter for property toolContentId.
     * @param toolContentId New value of property toolContentId.
     */
    public void setToolContentId(Long toolContentId)
    {
        
        this.toolContentId = toolContentId;
    }
    
    /**
     * Getter for property tool.
     * @return Value of property tool.
     */
    public Tool getTool()
    {
        
        return this.tool;
    }
    
    /**
     * Setter for property tool.
     * @param tool New value of property tool.
     */
    public void setTool(Tool tool)
    {
        
        this.tool = tool;
    }
    
	/**
	 * @return Returns the toolSessions.
	 */
	public Set getToolSessions() {
		return toolSessions;
	}
	/**
	 * @param toolSessions The toolSessions to set.
	 */
	public void setToolSessions(Set toolSessions) {
		this.toolSessions = toolSessions;
	}

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull()
    {
        return false;
    }    
    

	/** 
	 * Get all the tool activities in this activity. Called by Activity.getAllToolActivities()
	 * As we are a tool activity, just add ourself.
	 */
	protected void getToolActivitiesInActivity(SortedSet toolActivities) {
        toolActivities.add(this);
	}
}
