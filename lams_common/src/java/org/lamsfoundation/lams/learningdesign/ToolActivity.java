package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.lamsfoundation.lams.tool.Tool;

/**
 * @hibernate.class
 */
public class ToolActivity extends SimpleActivity implements Serializable
{
    
    /**
     * Holds value of property toolContentId.
     */
    private Long toolContentId;
    
    /**
     * Holds value of property tool.
     */
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
            String offlineInstructions,
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
                offlineInstructions,
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
    }
    
    /** default constructor */
    public ToolActivity()
    {
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
    }
    public static ToolActivity createCopy(ToolActivity originalActivity){
    	
    	ToolActivity newToolActivity = new ToolActivity();
    	newToolActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newToolActivity.setDescription(originalActivity.getDescription());
    	newToolActivity.setTitle(originalActivity.getTitle());
    	newToolActivity.setXcoord(originalActivity.getXcoord());
    	newToolActivity.setYcoord(originalActivity.getYcoord());
    	newToolActivity.setDefineLater(originalActivity.getDefineLater());
    	newToolActivity.setCreateDateTime(new Date());
    	newToolActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newToolActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newToolActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	/**
    	 * TODO calculate how these two values would be set for COPY 
    	 * */
    	//newToolActivity.setTransitionTo();
    	//newToolActivity.setTransitionFrom();
    	newToolActivity.setTool(originalActivity.getTool());    	
    	return newToolActivity;
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
}
