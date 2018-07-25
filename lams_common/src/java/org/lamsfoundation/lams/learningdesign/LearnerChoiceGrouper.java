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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

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
 * The learner choice grouper algorithm implementation. It creates a new group for
 * the learners that the learner requested.
 *
 * @author Jacky Fang
 * @since 2005-3-24
 * @version 1.1
 *
 */
public class LearnerChoiceGrouper extends Grouper implements Serializable {

    private static final long serialVersionUID = -8498560084860150033L;
    private static Logger log = Logger.getLogger(LearnerChoiceGrouper.class);

    /**
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.String,
     *      org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public void doGrouping(Grouping learnerChoiceGrouping, String groupName, User learner) {
	//convert the single user into a list.
	List learners = new ArrayList();
	learners.add(learner);
	//delegate to do grouping for a list of learners.
	doGrouping(learnerChoiceGrouping, groupName, learners);
    }

    /**
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.String,
     *      java.util.List)
     */
    @Override
    public void doGrouping(Grouping learnerChoiceGrouping, String groupName, List learners) {
	Group selectedGroup = null;

	String trimmedName = groupName != null ? groupName.trim() : null;
	if (trimmedName == null || trimmedName.length() == 0) {
	    trimmedName = generateGroupName(learnerChoiceGrouping);
	} else {
	    Iterator iter = learnerChoiceGrouping.getGroups().iterator();
	    while (iter.hasNext() && selectedGroup == null) {
		Group group = (Group) iter.next();
		if (trimmedName.equals(group.getGroupName())) {
		    selectedGroup = group;
		}
	    }
	}
	doGrouping(learnerChoiceGrouping, selectedGroup, trimmedName, learners);
    }

    /**
     * @param learnerChoiceGrouping
     * @return
     */
    private String generateGroupName(Grouping learnerChoiceGrouping) {
	String trimmedName;
	String prefix = getPrefix();
	trimmedName = prefix + " " + System.currentTimeMillis();
	LearnerChoiceGrouper.log.info("Chosen grouper for grouping " + learnerChoiceGrouping.toString()
		+ " did not get a group name. Selecting default name of " + trimmedName);
	return trimmedName;
    }

    /**
     * @throws GroupingException
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.Long,
     *      java.util.List)
     */
    @Override
    public void doGrouping(Grouping learnerChoiceGrouping, Long groupId, List learners) throws GroupingException {
	if (groupId != null) {
	    Iterator iter = learnerChoiceGrouping.getGroups().iterator();
	    Group selectedGroup = null;
	    while (iter.hasNext() && selectedGroup == null) {
		Group group = (Group) iter.next();
		if (group.getGroupId().equals(groupId)) {
		    selectedGroup = group;
		}
	    }
	    if (selectedGroup == null) {
		String error = "Tried to add users to group " + groupId + " but group cannot be found.";
		LearnerChoiceGrouper.log.error(error);
		throw new GroupingException(error);
	    }
	    doGrouping(learnerChoiceGrouping, selectedGroup, null, learners);
	} else {
	    String groupName = generateGroupName(learnerChoiceGrouping);
	    doGrouping(learnerChoiceGrouping, null, groupName, learners);
	}
    }

    /**
     * If the group exists add them to the group, otherwise creates a new group. If group is not supplied, then
     * groupName must
     * be supplied.
     * 
     * @param learnerChoiceGrouping
     *            (Mandatory)
     * @param group
     *            (Optional)
     * @param groupName
     *            (Optional)
     * @param learners
     *            (Mandatory)
     */
    private void doGrouping(Grouping learnerChoiceGrouping, Group group, String groupName, List learners) {
	if (group != null) {
	    group.getUsers().addAll(learners);
	} else {
	    learnerChoiceGrouping.getGroups()
		    .add(Group.createLearnerGroup(learnerChoiceGrouping, groupName, new HashSet(learners)));
	}
    }

    /**
     * Create an empty group for the given grouping. If the current number of groups = max number of
     * groups then a grouping exception is thrown.
     * 
     * @param grouping
     *            (mandatory)
     * @param groupName
     *            (mandatory)
     */
    @Override
    public Group createGroup(Grouping grouping, String name) throws GroupingException {
	int currentSize = grouping.getGroups().size();
	if (grouping.getMaxNumberOfGroups() != null && currentSize == grouping.getMaxNumberOfGroups()) {
	    String error = "Tried to add group " + name + " to grouping " + grouping
		    + ". Exceeded max number of groups - current size is " + currentSize;
	    LearnerChoiceGrouper.log.error(error);
	    throw new GroupingException(error);
	}
	return super.createGroup(grouping, name);

    }

    /**
     * Create new groups and insert them into the grouping. Group names
     * are Group 1, Group 2, etc.
     * 
     * @param learnerChoiceGrouping
     *            the requested grouping.
     * @param numOfGroupsTobeCreated
     *            the number new groups need to be created.
     */
    public void createGroups(LearnerChoiceGrouping learnerChoiceGrouping, int numOfGroupsTobeCreated) {
	String prefix = getPrefix();
	int size = learnerChoiceGrouping.getGroups().size();
	for (int numCreated = 0, groupIndex = size + 1; numCreated < numOfGroupsTobeCreated; groupIndex++) {
	    String groupName = prefix + " " + new Integer(groupIndex).toString();
	    // if the name of the group already exists, then createLearnerGroup will return null
	    // the name may exist if people have been creating and then removing groups in authoring.
	    Group newGroup = Group.createLearnerGroup(learnerChoiceGrouping, groupName, new HashSet());
	    if (newGroup != null) {
		learnerChoiceGrouping.getGroups().add(newGroup);
		numCreated++;
	    }
	}
    }
}