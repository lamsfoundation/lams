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
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.strategy.ToolActivityStrategy;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
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
    
	private static Logger log = Logger.getLogger(ToolActivity.class);

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
     * @return ToolActivity Returns a deep-copy of the originalActivity
     */
    public Activity createCopy(){    	
    	ToolActivity newToolActivity = new ToolActivity();
    	 
		newToolActivity.setTool(this.getTool());
		newToolActivity.setToolContentId(this.getToolContentId());    	
		newToolActivity.setActivityUIID(this.getActivityUIID());
    	newToolActivity.setDescription(this.getDescription());
    	newToolActivity.setTitle(this.getTitle());
    	newToolActivity.setHelpText(this.getHelpText());
    	newToolActivity.setXcoord(this.getXcoord());
    	newToolActivity.setYcoord(this.getYcoord());
    	newToolActivity.setActivityTypeId(this.getActivityTypeId());
    	
    	newToolActivity.setGroupingSupportType(this.getGroupingSupportType());
    	newToolActivity.setApplyGrouping(this.getApplyGrouping());
    	newToolActivity.setGrouping(this.getGrouping());
    	
    	newToolActivity.setGroupingUIID(this.getGroupingUIID());
    	newToolActivity.setOrderId(this.getOrderId());
    	newToolActivity.setDefineLater(this.getDefineLater());
    	newToolActivity.setLearningLibrary(this.getLearningLibrary());
    	newToolActivity.setCreateDateTime(new Date());
    	newToolActivity.setRunOffline(this.getRunOffline());
    	newToolActivity.setActivityCategoryID(this.getActivityCategoryID());
    	newToolActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());
    	newToolActivity.setLibraryActivity(this.getLibraryActivity());
    	return newToolActivity;
    }    
    
    /**
     * Factory method to create a new tool session for a single user when he is
     * running current activity. Does not check to see if a tool session already exists.
     * <p>
     * If the activity has groupingSupportType = GROUPING_SUPPORT_NONE then
     * a new tool session is created for each learner.
     * <p>
     * If the activity has groupingSupportType = GROUPING_SUPPORT_REQUIRED 
     * then a new tool session is created for each group of learners. It will fall back
     * to a class group if no grouping is found - the user interface should not
     * have allowed this!
     * <p>
     * If the activity has groupingSupportType = GROUPING_SUPPORT_OPTIONAL 
     * then a new tool session is created for each group of learners. If no grouping
     * is available then a whole of class group is created.
     * <p>
     * If groupingSupportType is not set then defaults to GROUPING_SUPPORT_NONE.
     * If for some reason a grouped session if also does the equivalent of GROUPING_SUPPORT_NONE.
     * This way the system will still function, if not as expected!
     * <p>
     * @param learner the user who should be using this tool session.
     * @return the new tool session.
     */
    public ToolSession createToolSessionForActivity(User learner,Lesson lesson)
    {
    	Date now = new Date(System.currentTimeMillis());
    	Integer supportType = getGroupingSupportType();
    	ToolSession session = null;

    	if ( supportType != null && 
    			( supportType.intValue() == GROUPING_SUPPORT_REQUIRED || supportType.intValue() == GROUPING_SUPPORT_OPTIONAL ) ) {

	    	// Both cases create a small group if a grouping exists, otherwise creates a class group.
	    	Group learners = null;
	        if( getApplyGrouping().booleanValue()) {
	        	learners = this.getGroupFor(learner);
			} 
	        
	    	if ( supportType.intValue() == GROUPING_SUPPORT_REQUIRED && learners == null ) {
	    		log.error("Activity "+getActivityId()
	    				+" requires grouping (groupingSupportType=GROUPING_SUPPORT_REQUIRED) but no grouping was available. "
	    				+" applyGrouping = "+getApplyGrouping()
	    				+" grouping = "+getGrouping()
	    				+". Falling back to a class grouping.");
	    	}
	
	    	if ( learners == null ) {
				LessonClass lessonClass = lesson.getLessonClass();
				learners = this.getGroupFor(learner, lessonClass);
			}
	    	
	        if ( learners != null ) {
	        	session = new GroupedToolSession(this,
	                    now,
	                    ToolSession.STARTED_STATE,
	                    learners,
	                    lesson);
	        } else {
	        	log.error("Unable to get the group for a new tool session for Activity "+getActivityId()
	        			+" Falling back to one learner per session."
	        			+" Learner "+learner
	        			+", lesson is "+lesson);
	        }
    	}
    	
    	if ( session == null ) {
	    	// Either GROUPING_SUPPORT_NONE was selected, supportType == null or the grouped tool sessions could not be created.
	    	// So create one session per user.
            session = new NonGroupedToolSession(this,
                    now,
                    ToolSession.STARTED_STATE,
                    learner,
                    lesson);
    	}

    	return session;
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
