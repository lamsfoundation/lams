
package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;


/**
 * This is the tool session shared within a learner group. It is meant to be
 * unique against learner group and a tool activity instance.
 * 
 * @author daveg, Jacky Fang
 *
 */
public class GroupedToolSession extends ToolSession {

    /** persistent field */
    private Group sessionGroup;

    /**default constructor*/
    public GroupedToolSession(){ }
    
    /**
     * Grouped tool session initialization constructor.
     * @param toolActivity the tool activity for that group
     * @param createDateTime the time this tool session is created.
     * @param toolSessionStateId the tool session status.
     * @param group the target group
     */
    public GroupedToolSession(ToolActivity toolActivity,
                              Date createDateTime,
                              int toolSessionStateId,
                              Group sessionGroup,
                              Lesson lesson)
    {
        super(null,toolActivity,createDateTime,toolSessionStateId,lesson);
        super.setUniqueKey(UNIQUE_KEY_PREFIX
        				   +"_"
                           +toolActivity.getActivityId().toString()
        				   +"_"
                           +sessionGroup.getGroupId().toString());
        this.sessionGroup=sessionGroup;
        //set toolSession name as same as name of relatived group.
        this.setToolSessionName(sessionGroup.getGroupName());
    }
    
	public Group getSessionGroup() {
		return sessionGroup;
	}
	
	public void setSessionGroup(Group sessionGroup) {
		this.sessionGroup = sessionGroup;
	}
}
