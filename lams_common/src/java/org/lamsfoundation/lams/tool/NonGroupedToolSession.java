/*
 * Created on 1/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author daveg
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NonGroupedToolSession extends ToolSession {

    /** persistent field */
    private User user;

    public NonGroupedToolSession(ToolActivity toolActivity,
                                 Date createDateTime,
                                 int toolSessionStateId,
                                 User user,
                                 Lesson lesson)
    {
        super(null,toolActivity,createDateTime,toolSessionStateId,lesson);
        super.setUniqueKey(UNIQUE_KEY_PREFIX
                           +toolActivity.getActivityId().toString()
                           +user.getUserId().toString());
        this.user=user;
    }
    /**default constructor*/
    public NonGroupedToolSession(){}
    
	public User getUser() 
	{
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
