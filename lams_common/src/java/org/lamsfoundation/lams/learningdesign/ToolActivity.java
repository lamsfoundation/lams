package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.NonGroupedToolSession;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;

/**
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
    public ToolActivity(){
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
    
    /**
     * Makes a copy of the ToolActivity for authoring, preview and monitoring enviornment 
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return ToolActivity Returns a deep-copy of the originalActivity
     */
    public static ToolActivity createCopy(ToolActivity originalActivity){    	
    	ToolActivity newToolActivity = new ToolActivity();
    	 
		newToolActivity.setTool(originalActivity.getTool());
		
		/* TODO Generate a new toolContentID for this new ToolActivity
		*  For now setting it to the contentID of the old activity
		*/
		newToolActivity.setToolContentId(originalActivity.getToolContentId());
    	
		newToolActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newToolActivity.setDescription(originalActivity.getDescription());
    	newToolActivity.setTitle(originalActivity.getTitle());
    	newToolActivity.setHelpText(originalActivity.getHelpText());
    	newToolActivity.setXcoord(originalActivity.getXcoord());
    	newToolActivity.setYcoord(originalActivity.getYcoord());
    	newToolActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newToolActivity.setOrderId(originalActivity.getOrderId());
    	newToolActivity.setDefineLater(originalActivity.getDefineLater());
    	newToolActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newToolActivity.setCreateDateTime(new Date());
    	newToolActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newToolActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());
    	newToolActivity.setLibraryActivity(originalActivity.getLibraryActivity());
    	newToolActivity.setToolSessions(new HashSet());    	    	
    	return newToolActivity;
    }    
    
    /**
     * Factory method to create a new tool session for the user when he is
     * running current activity.
     * @param learner the user who should be using this tool session.
     * @return the new tool session.
     */
    public ToolSession createToolSessionForActivity(User learner)
    {
        if(this.getTool().getSupportsGrouping())
            return new GroupedToolSession(this,
                                          new Date(System.currentTimeMillis()),
                                          ToolSession.STARTED_STATE,
                                          this.getGroupFor(learner));
        else
            return new NonGroupedToolSession(this,
                                             new Date(System.currentTimeMillis()),
                                             ToolSession.STARTED_STATE,
                                             learner);
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
}
