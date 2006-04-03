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
