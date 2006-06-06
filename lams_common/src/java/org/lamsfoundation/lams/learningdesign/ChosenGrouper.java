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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.exception.GroupingException;
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
public class ChosenGrouper extends Grouper implements Serializable
{

	private static final long serialVersionUID = -8498560084860150033L;
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
    	String trimmedName = groupName.trim();
    	if ( trimmedName == null ) {
    		String prefix = getPrefix();
    		trimmedName = prefix+" "+System.currentTimeMillis();
    		log.warn("Chosen grouper for grouping "+chosenGrouping.toString()+" did not get a group name. Selecting default name of "+trimmedName);
    	}
    	Iterator iter = chosenGrouping.getGroups().iterator();
    	Group selectedGroup = null;
    	while (iter.hasNext() && selectedGroup==null) {
			Group group = (Group) iter.next();
			if ( trimmedName.equals(selectedGroup.getGroupName()) ) {
				selectedGroup = group;
			}
		}
    	doGrouping(chosenGrouping, selectedGroup, trimmedName, learners);
    }

    /**
     * @throws GroupingException 
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.Long, java.util.List)
     */
    public void doGrouping(Grouping chosenGrouping,Long groupId, List learners) throws GroupingException
    {
    	Iterator iter = chosenGrouping.getGroups().iterator();
    	Group selectedGroup = null;
    	while (iter.hasNext() && selectedGroup==null) {
			Group group = (Group) iter.next();
			if ( group.getGroupId().equals(groupId) ) {
				selectedGroup = group;
			}
		}
    	if ( selectedGroup == null ) {
    		String error = "Tried to add users to group "+groupId+" but group cannot be found.";
    		log.error(error); 
    		throw new GroupingException(error);
    	}
    	doGrouping(chosenGrouping, selectedGroup, null, learners);
    }

    /** If the group exists add them to the group, otherwise creates a new group. Group will always
     * exist if called from doGrouping(Grouping chosenGrouping,Long groupId, List learners), but it may or 
     * may not exist if called from doGrouping(Grouping chosenGrouping,String groupName, List learners).
     * @param chosenGrouping
     * @param group
     * @param groupName
     * @param learners
     */
    public void doGrouping(Grouping chosenGrouping, Group group, String groupName, List learners)
    {
    	if ( group != null ) {
    		group.getUsers().addAll(learners);
    	} else {
    		chosenGrouping.getGroups().add(Group.createLearnerGroup(chosenGrouping,groupName,
                                                                new HashSet(learners)));
    	}
    }
    
    /** Create an empty group for the given grouping. If the current number of groups = max number of 
     * groups then a grouping exception is thrown.
     * 
     * @param grouping (mandatory)
     * @param groupName (mandatory)
     */
    public Group createGroup(Grouping grouping, String name) throws GroupingException
    {
    	int currentSize = grouping.getGroups().size(); 
    	if ( currentSize == grouping.getMaxNumberOfGroups() ) {
    		String error = "Tried to add group "+name+" to grouping "+grouping+". Exceeded max number of groups - current size is "+currentSize;
    		log.error(error); 
    		throw new GroupingException(error);
    	}
    	return super.createGroup(grouping, name);

	}

}
