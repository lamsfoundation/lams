/***************************************************************************
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
 * ***********************************************************************/

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
 * <p>
 * The random grouping algorithm implementation. It allocates user into the groups according to the attributes accepted
 * from user interface: <code>numberOfGroups</code> and <code>learnersPerGroup</code>. If any of these two attribute has
 * be set, the random grouping will be done correspondently.
 * </p>
 *
 * <p>
 * In the case of random grouping by number of groups, the number of groups will be fixed, and learners are evenly
 * allocated into the groups. For example, there are 10 users and <code>numberOfGroups=3</code>, the number of learners
 * in three groups will 4,3,3. And if there are 1 user and <code>numberOfGroups=3</code>, all three groups still will be
 * created with learner number 1,0,0.
 * </p>
 *
 * <p>
 * In the case of random grouping by learners per group, groups are only created if the total learner number exceeds the
 * existing group capacity. Learners are also evenly allocated into the groups.For example, if there is 5 users and
 * <code>learnerPerGroup=3</code>, only 2 groups will be created.
 * </p>
 *
 * @author Jacky Fang
 * @since 2005-3-24
 * @version 1.1
 *
 */
public class RandomGrouper extends Grouper implements Serializable {
    private static final long serialVersionUID = -3696368461795411181L;

    private static Logger log = Logger.getLogger(RandomGrouper.class);

    // ---------------------------------------------------------------------
    // Grouping algorithm Implementation Method
    // ---------------------------------------------------------------------
    /**
     * Do the grouping for single new learner.
     *
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,
     *      java.lang.String,org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public void doGrouping(Grouping randomGrouping, String groupName, User learner) {
	// convert the single user into a list.
	List learners = new ArrayList();
	learners.add(learner);
	// delegate to do grouping for a list of learners.
	doGrouping(randomGrouping, groupName, learners);
    }

    /**
     * Do the grouping for a list of new learners.
     *
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.String,
     *      java.util.List)
     */
    @Override
    public void doGrouping(Grouping randomGrouping, String groupName, List learners) {
	// calculate how many new groups needs to be created.
	int numOfGroupsTobeCreated = 0;
	if (randomGrouping.getGroups().size() == 0) {
	    numOfGroupsTobeCreated = calculateNumOfNewGroups((RandomGrouping) randomGrouping, learners, true);
	} else {
	    numOfGroupsTobeCreated = calculateNumOfNewGroups((RandomGrouping) randomGrouping, learners, false);
	}
	// create new groups
	createGroups((RandomGrouping) randomGrouping, numOfGroupsTobeCreated);
	// join the new learners into these groups.
	joinGroups(randomGrouping, learners);
    }

    /**
     * Need to be able to force a user into a particular group for group based branching in preview. So this is a
     * non-random random. Bleah!
     *
     * @throws GroupingException
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping,java.lang.Long,
     *      java.util.List)
     */
    @Override
    public void doGrouping(Grouping randomGrouping, Long groupId, List learners) throws GroupingException {
	if (groupId != null) {
	    Iterator iter = randomGrouping.getGroups().iterator();
	    Group selectedGroup = null;
	    while (iter.hasNext() && (selectedGroup == null)) {
		Group group = (Group) iter.next();
		if (group.getGroupId().equals(groupId)) {
		    selectedGroup = group;
		}
	    }
	    if (selectedGroup == null) {
		String error = "Tried to add users to group " + groupId + " but group cannot be found.";
		RandomGrouper.log.error(error);
		throw new GroupingException(error);
	    }
	    selectedGroup.getUsers().addAll(learners);
	} else {
	    // normal random !!!
	    doGrouping(randomGrouping, (String) null, learners);
	}

    }

    // ---------------------------------------------------------------------
    // Helper Methods - doGrouping
    // ---------------------------------------------------------------------
    /**
     * Compute the number of new groups needs to be created based on passed in grouping. It figures out group by number
     * of groups or group by learner per group automatically.
     *
     * @param randomGrouping
     *            the grouping we used to group learner
     * @param learners
     *            the list of learners need to be grouped
     * @return the number required new group.
     */
    private int calculateNumOfNewGroups(RandomGrouping randomGrouping, List learners, boolean isInitialization) {
	if ((randomGrouping.getNumberOfGroups() != null) && (randomGrouping.getNumberOfGroups().intValue() > 0)) {
	    return getNewGroupsByNumberOfGroups(randomGrouping, randomGrouping.getNumberOfGroups(), isInitialization);
	} else if ((randomGrouping.getLearnersPerGroup() != null)
		&& (randomGrouping.getLearnersPerGroup().intValue() > 0)) {
	    return getNewGroupsByLearnerPerGroup(randomGrouping, learners);
	} else {
	    RandomGrouper.log.warn("Random Grouping id=" + randomGrouping.getGroupingId()
		    + " is missing both the number of groups and learners per group. Defaulting to two groups.");
	    return getNewGroupsByNumberOfGroups(randomGrouping, 2, isInitialization);
	}
    }

    /**
     * Create new groups and insert them into the grouping. Group names are Group 1, Group 2, etc.
     *
     * @param randomGrouping
     *            the requested grouping.
     * @param numOfGroupsTobeCreated
     *            the number new groups need to be created.
     */
    private void createGroups(RandomGrouping randomGrouping, int numOfGroupsTobeCreated) {
	String prefix = getPrefix();
	int size = randomGrouping.getGroups().size();
	for (int numCreated = 0, groupIndex = size + 1; numCreated < numOfGroupsTobeCreated; groupIndex++) {
	    String groupName = prefix + " " + new Integer(groupIndex).toString();
	    // if the name of the group already exists, then createLearnerGroup will return null
	    // the name may exist if people have been creating and then removing groups in authoring.
	    Group newGroup = Group.createLearnerGroup(randomGrouping, groupName, new HashSet());
	    if (newGroup != null) {
		randomGrouping.getGroups().add(newGroup);
		numCreated++;
	    }
	}
    }

    /**
     * Allocate the learner into groups within the specified grouping.
     *
     * @param randomGrouping
     *            the specified grouping.
     * @param learners
     *            the list of learners need to be grouped
     */
    private void joinGroups(Grouping randomGrouping, List learners) {
	// go through all learners select a group for them to join.
	for (Iterator i = learners.iterator(); i.hasNext();) {
	    User learner = (User) i.next();
	    if (!randomGrouping.doesLearnerExist(learner)) {
		Group selectedGroup = selectGroupToJoin((RandomGrouping) randomGrouping);
		selectedGroup.getUsers().add(learner);
	    }
	}
    }

    // ---------------------------------------------------------------------
    // Helper Methods - joinGroups
    // ---------------------------------------------------------------------
    /**
     * Always choose the group with the least member to join. Therefore, we ensure the members are evenly allocated into
     * groups.
     *
     * @param randomGrouping
     *            the specified grouping.
     * @return the desirable group
     */
    private Group selectGroupToJoin(RandomGrouping randomGrouping) {
	return randomGrouping.getGroupWithLeastMember();
    }

    // ---------------------------------------------------------------------
    // Helper Methods - calculateNumOfNewGroups
    // ---------------------------------------------------------------------
    /**
     * Calculate number of new groups according to doGrouping by learner per group.
     *
     * @param randomGrouping
     *            the requested grouping.
     * @param learners
     *            the learners need to be grouped.
     * @return the number of new groups need to be created.
     */
    private int getNewGroupsByLearnerPerGroup(RandomGrouping randomGrouping, List learners) {
	int newLearners = 0;
	for (Iterator i = learners.iterator(); i.hasNext();) {
	    User learner = (User) i.next();
	    if (!randomGrouping.doesLearnerExist(learner)) {
		newLearners++;
	    }
	}

	double totalRequiredGroups = Math.ceil((randomGrouping.getLearners().size() + newLearners)
		/ randomGrouping.getLearnersPerGroup().doubleValue());
	int existingGroups = randomGrouping.getGroups().size();

	return (int) totalRequiredGroups - existingGroups;
    }

    /**
     * Calculate number of new groups according to doGrouping by number of groups.
     *
     * @param randomGrouping
     *            the requested grouping.
     * @param isInitialization
     *            is this the first we do grouping.
     * @return the number of new groups need to be created.
     */
    private int getNewGroupsByNumberOfGroups(RandomGrouping randomGrouping, int numberOfGroups,
	    boolean isInitialization) {
	if (isInitialization) {
	    return numberOfGroups;
	} else {
	    int numberOfNewGroups = numberOfGroups - randomGrouping.getGroups().size();

	    return numberOfNewGroups > 0 ? numberOfNewGroups : 0;
	}
    }

}
