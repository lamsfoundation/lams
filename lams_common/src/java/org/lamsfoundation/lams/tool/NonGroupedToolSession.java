/*
 * Created on 1/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
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
                                 User user)
    {
        super(null,toolActivity,createDateTime,toolSessionStateId);
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
