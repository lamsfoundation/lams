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
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.BatchSize;
import org.lamsfoundation.lams.learningdesign.dto.GroupingDTO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Jacky Fang
 */
@Entity
@Table(name = "lams_grouping")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "grouping_type_id", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Grouping implements Serializable {

    private static final long serialVersionUID = -8644392914291370672L;

    /** Grouping type id of random grouping */
    public static final Integer RANDOM_GROUPING_TYPE = 1;

    /** Grouping type id of chosen grouping */
    public static final Integer CHOSEN_GROUPING_TYPE = 2;

    /** Grouping type id for lesson class grouping */
    public static final Integer CLASS_GROUPING_TYPE = 3;

    /** Grouping type id for learner's choice grouping */
    public static final Integer LEARNER_CHOICE_GROUPING_TYPE = 4;

    @Id
    @Column(name = "grouping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupingId;

    /**
     * TODO It make sense only if we want to setup some limits for the number
     * of groups the author can setup in the authoring GUI. It might need
     * to be deleted if the end user doesn't like this limits.
     */
    @Column(name = "max_number_of_groups")
    private Integer maxNumberOfGroups;

    @Column(name = "grouping_ui_id")
    private Integer groupingUIID;

    @OneToMany(mappedBy = "grouping", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Group> groups = new HashSet<Group>();

    @OneToMany(mappedBy = "grouping")
    private Set<Activity> activities = new HashSet<Activity>();

    @Transient
    protected Set<User> learners = new HashSet<User>();

    @Transient
    protected Grouper grouper;
    /**
     * static final variables indicating the grouping_support of activities
     *******************************************************************/
    public static final int GROUPING_SUPPORT_NONE = 1;

    public static final int GROUPING_SUPPORT_OPTIONAL = 2;

    public static final int GROUPING_SUPPORT_REQUIRED = 3;

    /******************************************************************/

    /** full constructor */
    public Grouping(Long groupingId, Set<Group> groups, Set<Activity> activities, Grouper grouper) {
	this.groupingId = groupingId;
	this.groups = groups;
	this.activities = activities;
	this.grouper = grouper;
    }

    /** default constructor */
    public Grouping() {
    }

    /**
     * Create a deep copy of the this grouping. It should return the same
     * subclass as the grouping being copied. Does not copy the tool sessions.
     * Copies the groups but not users in the groups. Copies any group to
     * branch mappings, updating the group but not the activity.
     *
     * Any implementation of this method can call copyGroupingFields(Grouping newGrouping)
     * to set max number of groups, grouping UIID, copy the groups
     * and the group to branch mappings.
     *
     * @return deep copy of this object
     */
    public abstract Grouping createCopy(int uiidOffset);

    /**
     * Copy all the groups within this grouping to the newGrouping object.
     * Copies the groups but not users in the groups. Copies any group to branch
     * mappings, updating the group but not the activity. Used by createCopy()
     * implementations.
     */
    protected void copyGroupingFields(Grouping newGrouping, int uiidOffset) {
	newGrouping.setMaxNumberOfGroups(this.getMaxNumberOfGroups());
	newGrouping.setGroupingUIID(LearningDesign.addOffset(this.getGroupingUIID(), uiidOffset));

	if (this.getGroups() != null && this.getGroups().size() > 0) {
	    Iterator<Group> iter = this.getGroups().iterator();
	    while (iter.hasNext()) {
		Group oldGroup = iter.next();
		Group newGroup = oldGroup.createCopy(newGrouping);
		newGroup.setGroupUIID(LearningDesign.addOffset(newGroup.getGroupUIID(), uiidOffset));
		newGrouping.getGroups().add(newGroup);
	    }
	}
    }

    public Long getGroupingId() {
	return groupingId;
    }

    public void setGroupingId(Long groupingId) {
	this.groupingId = groupingId;
    }

    public Integer getGroupingTypeId() {
	if (this instanceof LessonClass) {
	    return Grouping.CLASS_GROUPING_TYPE;
	} else if (this instanceof ChosenGrouping) {
	    return Grouping.CHOSEN_GROUPING_TYPE;
	} else if (this instanceof LearnerChoiceGrouping) {
	    return Grouping.LEARNER_CHOICE_GROUPING_TYPE;
	} else {
	    return Grouping.RANDOM_GROUPING_TYPE;
	}

    }

    public Set<Group> getGroups() {
	if (groups == null) {
	    setGroups(new TreeSet<Group>());
	}
	return groups;
    }

    public void setGroups(Set<Group> groups) {
	this.groups = groups;
    }

    public Set<Activity> getActivities() {
	if (activities == null) {
	    setActivities(new TreeSet<Activity>(new ActivityOrderComparator()));
	}
	return activities;
    }

    public void setActivities(Set<Activity> activities) {
	this.activities = activities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("groupingId", getGroupingId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof Grouping)) {
	    return false;
	}
	Grouping castOther = (Grouping) other;
	return new EqualsBuilder().append(this.getGroupingId(), castOther.getGroupingId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getGroupingId()).toHashCode();
    }

    /**
     *
     */
    public Integer getMaxNumberOfGroups() {
	return maxNumberOfGroups;
    }

    /**
     * @param maxNumberOfGroups
     *            The maxNumberOfGroups to set.
     */
    public void setMaxNumberOfGroups(Integer maxNumberOfGroups) {
	this.maxNumberOfGroups = maxNumberOfGroups;
    }

    public Integer getGroupingUIID() {
	return groupingUIID;
    }

    public void setGroupingUIID(Integer groupingUIID) {
	this.groupingUIID = groupingUIID;
    }

    //---------------------------------------------------------------------
    // Service methods
    //---------------------------------------------------------------------
    /**
     * Return the next group order id. Can't do it on size as groups may have been deleted.
     * Returns -1 if the proposed name is the same as existing name
     * Synchronisation is not perfect here as the same order ID can be picked up before the new Group gets created
     * and added to groups collection.
     *
     * @return the next order id.
     */
    synchronized int getNextGroupOrderIdCheckName(String proposedName) {
	int maxOrderId = 0;

	for (Group group : getGroups()) {
	    if (proposedName.equals(group.getGroupName())) {
		return -1;
	    }
	    if (group.getOrderId() > maxOrderId) {
		maxOrderId = group.getOrderId();
	    }
	}
	return ++maxOrderId;
    }

    /**
     * Return all the learners who participate this grouping.
     *
     * @return the learners set.
     */
    public Set<User> getLearners() {

	learners = new HashSet<User>();
	for (Group group : groups) {
	    if (isLearnerGroup(group)) {
		learners.addAll(group.getUsers());
	    }
	}
	return learners;
    }

    /**
     * Returns the group that current learner is in.
     *
     * @param learner
     *            the user in the group
     * @return the group that has the learner
     */
    public Group getGroupBy(User learner) {
	for (Iterator<Group> i = getGroups().iterator(); i.hasNext();) {
	    Group group = i.next();
	    if (isLearnerGroup(group) && group.hasLearner(learner)) {
		return group;
	    }
	}
	return new NullGroup();
    }

    /**
     * Iterate through all the groups in this grouping and figure out the group
     * with the least members in it.
     *
     * @return the group with the least member.
     */
    public Group getGroupWithLeastMember() {
	List<Group> groups = new ArrayList<Group>(this.getGroups());

	Group minGroup = groups.get(0);

	for (int i = 1; i < groups.size(); i++) {
	    Group tempGroup = groups.get(i);
	    if (tempGroup.getUsers().size() < minGroup.getUsers().size()) {
		minGroup = tempGroup;
	    }
	}
	return minGroup;
    }

    /**
     * Is this group a learner group. It is also possible that the group is a
     * staff group.
     *
     * @return whether the group is learner group or not.
     */
    public abstract boolean isLearnerGroup(Group group);

    /**
     * Return whether a learner is a existing learner for this grouping or not.
     *
     * @param learner
     *            the current leaner
     * @return the boolean result
     */
    public boolean doesLearnerExist(User learner) {
	return !getGroupBy(learner).isNull();
    }

    public GroupingDTO getGroupingDTO(boolean setupUserList) {
	return new GroupingDTO(this, setupUserList);
    }

    public static Object getGroupingInstance(Integer groupingType) {
	if (groupingType.equals(Grouping.RANDOM_GROUPING_TYPE)) {
	    return new RandomGrouping();
	} else if (groupingType.equals(Grouping.CHOSEN_GROUPING_TYPE)) {
	    return new ChosenGrouping();
	} else if (groupingType.equals(Grouping.LEARNER_CHOICE_GROUPING_TYPE)) {
	    return new LearnerChoiceGrouping();
	} else {
	    return new LessonClass();
	}
    }

    public boolean isRandomGrouping() {
	return getGroupingTypeId() == Grouping.RANDOM_GROUPING_TYPE;
    }

    public boolean isChosenGrouping() {
	return getGroupingTypeId() == Grouping.CHOSEN_GROUPING_TYPE;
    }

    public boolean isClassGrouping() {
	return getGroupingTypeId() == Grouping.CLASS_GROUPING_TYPE;
    }

    public boolean isLearnerChoiceGrouping() {
	return getGroupingTypeId() == Grouping.LEARNER_CHOICE_GROUPING_TYPE;
    }

    public Grouper getGrouper() {
	return grouper;
    }

    public void setGrouper(Grouper grouper) {
	this.grouper = grouper;
    }

    public Group getGroup(Integer groupUIID) {
	if (this.getGroups() != null) {
	    Iterator<Group> iter = this.getGroups().iterator();
	    while (iter.hasNext()) {
		Group elem = iter.next();
		if (elem.getGroupUIID().equals(groupUIID)) {
		    return elem;
		}
	    }
	}
	return null;
    }

    /**
     * Is this grouping used for branching? That is, is there a grouped branching activity that uses this grouping?
     * If so, that has implications for the changes allowed at runtime.
     */
    public boolean isUsedForBranching() {
	Iterator<Activity> actIter = getActivities().iterator();
	while (actIter.hasNext()) {
	    Activity act = actIter.next();
	    if (act.isBranchingActivity()) {
		return true;
	    }
	}
	return false;
    }
}
