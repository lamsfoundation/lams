
package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ToolActivity;


/**
 * This is the tool session shared within a learner group. It is meant to be
 * unique against learner group and a tool activity instance.
 * 
 * @author daveg, Jacky Fang
 *
 */
public class GroupedToolSession extends ToolSession {

    /** persistent field */
    private Group group;

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
                              Group group)
    {
        super(null,toolActivity,createDateTime,toolSessionStateId);
        super.setUniqueKey(UNIQUE_KEY_PREFIX
                           +toolActivity.getActivityId().toString()
                           +group.getGroupId().toString());
        this.group=group;
    }
    
	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
}
