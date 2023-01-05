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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.exception.GroupingException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;

/**
 * This is interface that defines the contract for performing grouping algorithm.
 *
 * It would be nicer to get the message service directly from within the classes, rather than have a setter. But can't
 * think of a way to directly access it when the grouper object doesn't have any link to the Spring context. (Fiona
 * Malikoff)
 *
 * @author Jacky Fang
 * @since 2005-3-24
 * @version 1.1
 *
 */
public abstract class Grouper {

    private static Logger log = Logger.getLogger(Grouper.class);

    String DEFAULT_GROUP_NAME_PREFIX_I18N = "group.name.prefix";
    private MessageService commonMessageService;

    /**
     * Set the message service, needed for the I18N of the default group name prefix. Should call this method before
     * calling doGrouping() or getPrefix()
     */
    public void setCommonMessageService(MessageService commonMessageService) {
	this.commonMessageService = commonMessageService;
    }

    protected MessageService getCommonMessageService() {
	return commonMessageService;
    }

    /**
     * Do the grouping for a list of learners that the teacher requested. If you don't supply a name, you may get a
     * system generated name.
     *
     * @param grouping
     *            the grouping that is used to perform groups creation.
     * @param name
     *            for this group (optional)
     * @param learners
     *            the list of learners that the teacher requested.
     */
    public abstract void doGrouping(Grouping grouping, String groupName, List<User> learners) throws GroupingException;

    /**
     * Do the grouping for a single learner. Should call setCommonMessageService() before calling this method. If you
     * don't supply a name, you may get a system generated name.
     *
     * @param grouping
     *            the grouping that is used to perform groups creation.
     * @param name
     *            for this group (optional)
     * @param learner
     *            the learner teacher want to add.
     */
    public abstract void doGrouping(Grouping grouping, String groupName, User learner) throws GroupingException;

    /**
     * Do the grouping for a list of learners that the teacher requested. If you don't supply an id, you may get a group
     * with a system generated name.
     *
     * @param grouping
     *            the grouping that is used to perform groups creation.
     * @param id
     *            of the group (optional)
     * @param learner
     *            the learner teacher want to add.
     * @throws GroupingException
     *             if the group does not exist.
     */
    public abstract void doGrouping(Grouping chosenGrouping, Long groupId, List<User> learners)
	    throws GroupingException;

    /**
     * Get the default group name prefix
     *
     * @return default group name prefix
     */
    public String getPrefix() {
	String prefix = commonMessageService != null ? commonMessageService.getMessage(DEFAULT_GROUP_NAME_PREFIX_I18N)
		: "";
	prefix = prefix.trim();
	return prefix.length() > 0 ? prefix : "Group";
    }

    /**
     * Remove the give learners from the given group. Cannot remove learners if the group is already in use (i.e. a tool
     * session exists)
     *
     * Trims the name of the group before checking if it is null or before matching to a group.
     *
     * @param grouping
     *            (mandatory)
     * @param groupID
     *            if not null only remove user from this group, if null remove learner from any group.
     * @param learner
     *            the learner to be removed (mandatory)
     */
    public void removeLearnersFromGroup(Grouping grouping, Long groupID, List<User> learners) throws GroupingException {
	Set groups = grouping.getGroups();
	Iterator iter = groups.iterator();
	boolean groupFound = false;

	while (iter.hasNext() && !groupFound) {
	    Group group = (Group) iter.next();

	    if (groupID == null || groupID.equals(group.getGroupId())) {

		groupFound = groupID != null;

		if (group.mayBeDeleted()) {

		    boolean removed = group.getUsers().removeAll(learners);
		    if (removed) {
			if (log.isDebugEnabled()) {
			    log.debug("Removed " + learners.size() + " users from group " + group.getGroupName());
			}
		    }

		} else {
		    String error = "Tried to remove a group which cannot be removed (tool sessions probably exist). Group "
			    + group + " grouping " + grouping + ". Not removing the group.";
		    log.error(error);
		    throw new GroupingException(error);
		}

	    }
	}
    }

    /**
     * Remove all the learners from the given grouping. Cannot remove learners if any of the groups are already in use
     * (i.e. a tool
     * session exists)
     *
     * @param grouping
     *            (mandatory)
     * @param groupID
     *            (mandatory)
     */
    public void removeAllLearnersFromGrouping(Grouping grouping) throws GroupingException {

	for (Group group : grouping.getGroups()) {
	    if (group == null) {
		continue;
	    }
	    if (!group.mayBeDeleted()) {
		String error = "Tried to clear a group which cannot be removed (tool sessions probably exist). Grouping "
			+ grouping + ". Not removing the groupings.";
		log.error(error);
		throw new GroupingException(error);
	    }
	}

	for (Group group : grouping.getGroups()) {
	    if (group == null || group.getUsers() == null) {
		continue;
	    }
	    if (log.isDebugEnabled()) {
		log.debug("Cleared all users from group " + group.getGroupName());
	    }
	    group.getUsers().clear();
	}
    }

    /**
     * Create an empty group for the given grouping. Trims the name of the group before creating the group. If the group
     * name group name already exists then it appends a datetime string to make the name unique. Gives it 5 attempts to
     * make it unique then gives up.
     *
     * Throws a GroupingException if name is null or blank.
     *
     * @param grouping
     *            (mandatory)
     * @param name
     *            (mandatory)
     * @param forceName
     *            (mandatory)
     */
    public Group createGroup(Grouping grouping, String name) throws GroupingException {
	String trimmedName = name.trim();
	if (trimmedName == null || trimmedName.length() == 0) {
	    log.warn("Tried to add a group with no name to grouping " + grouping + ". Not creating group.");
	    return null;
	}
	Set emptySet = new HashSet();
	Group newGroup = Group.createLearnerGroup(grouping, trimmedName, emptySet);

	if (newGroup == null) {
	    trimmedName = trimmedName + " " + new Long(System.currentTimeMillis()).toString();
	    newGroup = Group.createLearnerGroup(grouping, trimmedName, emptySet);
	}

	if (newGroup == null) {
	    // what, still not unique? Okay try sticking a number on the end. Try 5 times then give up
	    log.warn("Having trouble creating a unique name for a group. Have tried " + trimmedName);
	    int attempt = 1;
	    while (newGroup == null && attempt < 5) {
		newGroup = Group.createLearnerGroup(grouping, trimmedName + " " + new Integer(attempt).toString(),
			emptySet);
	    }
	    if (newGroup == null) {
		String error = "Unable to create a unique name for a group. Tried 5 variations on " + trimmedName
			+ " now giving up.";
		log.error(error);
		throw new GroupingException(error);

	    }
	}

	if (newGroup != null) {
	    grouping.getGroups().add(newGroup);
	}

	return newGroup;

    }

    /**
     * Remove a group for the given grouping. If the group is already used (e.g. a tool session exists) then it throws a
     * GroupingException. If the group does not exist, nothing happens.
     *
     * Trims the name of the group before selecting the group.
     *
     * When a group is removed all the users in the group become ungrouped.
     *
     * Also throws a GroupingException if groupName is null or blank.
     *
     * @param grouping
     *            (mandatory)
     * @param groupID
     *            (mandatory)
     */
    public void removeGroup(Grouping grouping, Long groupID) throws GroupingException {

	Iterator iter = grouping.getGroups().iterator();
	boolean groupDeleted = false;

	while (!groupDeleted && iter.hasNext()) {
	    Group group = (Group) iter.next();
	    if (group.getGroupId().equals(groupID)) {

		if (group.mayBeDeleted()) {

		    // all okay so we can delete
		    if (log.isDebugEnabled()) {
			log.warn("Deleting group " + group.getGroupName() + " for grouping " + grouping);
		    }
		    iter.remove();
		    groupDeleted = true;

		} else {
		    String error = "Tried to remove a group which cannot be removed (tool sessions probably exist). Group "
			    + group + " grouping " + grouping + ". Not removing the group.";
		    log.error(error);
		    throw new GroupingException(error);
		}
	    }
	}

	if (!groupDeleted) {
	    log.warn("Tried to remove a group " + groupID + " but the group does not exist for grouping " + grouping
		    + ".");
	}
    }

}