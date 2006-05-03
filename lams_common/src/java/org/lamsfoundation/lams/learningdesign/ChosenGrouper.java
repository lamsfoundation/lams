/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
/* $Id$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * The chosen grouper algorithm implementation. It creates a new group for
 * the learners that the teacher requested.
 * 
 * @author Jacky Fang
 * @since  2005-3-24
 * @version 1.1
 * 
 */
public class ChosenGrouper implements Grouper,Serializable
{

	private static Logger log = Logger.getLogger(ChosenGrouper.class);

	/**
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.String, org.lamsfoundation.lams.usermanagement.User)
     */
    public void doGrouping(Grouping chosenGrouping, String groupName, User learner)
    {
        //convert the single user into a list.
        List learners = new ArrayList();
        learners.add(learner);
        //delegate to do grouping for a list of learners.
        doGrouping(chosenGrouping,groupName, learners);
    }
    
    /**
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.String, java.util.List)
     */
    public void doGrouping(Grouping chosenGrouping,String groupName, List learners)
    {
    	String newGroupName = groupName;
    	if ( newGroupName == null ) {
    		newGroupName = "Group"+System.currentTimeMillis();
    		log.warn("Chosen grouper for grouping "+chosenGrouping.toString()+" didn't get a group name. Selecting default name of "+newGroupName);
    	}
        chosenGrouping.getGroups().add(Group.createLearnerGroup(chosenGrouping,newGroupName,
                                                                new HashSet(learners)));
    }

}
