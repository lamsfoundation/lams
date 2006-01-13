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
package org.lamsfoundation.lams.tool;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Not used at present - creates a separate ToolSession for each learner.
 * When we have a user interface that allows the author to select the whole of the class
 * vs an individual learner for the tool session, then it will be used.
 * 
 * @author daveg
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
        				   +"_"
                           +toolActivity.getActivityId().toString()
        				   +"_"
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
