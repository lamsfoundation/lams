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

package org.lamsfoundation.lams.lesson;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;

/**
 * A type of Grouping that represents all the Learners in a Lesson. The
 * LessonClass is used as the default Grouping.
 *
 * @author chris
 */
@Entity
@DiscriminatorValue("3")
public class LessonClass extends Grouping {

    private static final long serialVersionUID = 5363725488837028794L;

    private static Logger log = Logger.getLogger(LessonClass.class);

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_group_id")
    private Group staffGroup;

    /** Creates a new instance of LessonClass */
    public LessonClass() {
    }

    /** full constructor */
    public LessonClass(Long groupingId, Set<Group> groups, Set<Activity> activities, Group staffGroup, Lesson lesson) {
	//don't think lesson class need perform doGrouping. set grouper to null.
	super(groupingId, groups, activities, null);
	this.staffGroup = staffGroup;
    }

    public Group getStaffGroup() {
	return this.staffGroup;
    }

    public void setStaffGroup(Group staffGroup) {
	this.staffGroup = staffGroup;
    }

    /**
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     *      Returns false if group is null
     */
    @Override
    public boolean isLearnerGroup(Group group) {
	if (group == null || group.getGroupId() == null || staffGroup.getGroupId() == null) {
	    return false;
	}

	return staffGroup.getGroupId() != group.getGroupId();
    }

    /**
     * This method creates a deep copy of the LessonClass
     *
     * @return LessonClass The deep copied LessonClass object
     */
    @Override
    public Grouping createCopy(int uiidOffset) {
	LessonClass lessonClass = new LessonClass();
	lessonClass.staffGroup = this.staffGroup;
	return lessonClass;
    }

    /**
     * Is this user a staff member for this lesson class? Returns false if the userID is null.
     */
    public boolean isStaffMember(User user) {
	if (user == null) {
	    return false;
	}

	Group staff = getStaffGroup();
	return staff != null && staff.hasLearner(user);
    }

    /**
     * Add a learner to the lesson class. Checks for duplicates.
     *
     * @return true if added user, returns false if the user already a learner and hence not added.
     */
    public boolean addLearner(User user) {
	if (user == null) {
	    return false;
	}

	Group learnersGroup = createLearnerGroupIfMissing();

	if (!learnersGroup.hasLearner(user)) {
	    learnersGroup.getUsers().add(user);
	    return true;
	}
	return false;
    }

    /**
     * When the users's are added from an external LMS e.g. Moodle, the Learner Group may not exist.
     * If that happens, then this code will ensure that there is a learners group, etc and so adding
     * a user won't throw an exception. This is just fallback code!!!!
     *
     * @return the learner group
     */
    private Group createLearnerGroupIfMissing() {
	Group learnersGroup = getLearnersGroup();
	if (learnersGroup == null) {
	    // should be one ordinary group for lesson class, and this is all the learners in the lesson class
	    Organisation lessonOrganisation = getLesson() != null ? getLesson().getOrganisation() : null;
	    if (lessonOrganisation == null) {
		log.warn(
			"Adding a learner to a lesson class with no related organisation. Learner group name will be \'learners'.");
	    }
	    String learnerGroupName = lessonOrganisation != null ? lessonOrganisation.getName() : "";
	    learnerGroupName = learnerGroupName + "learners";
	    getGroups().add(Group.createLearnerGroup(this, learnerGroupName, new HashSet<User>()));
	    learnersGroup = getLearnersGroup();
	}
	return learnersGroup;
    }

    /**
     * Add one or more learners to the lesson class. Doesn't bother checking for duplicates as it goes into a set,
     * and User does a check on userID field for equals anyway.
     *
     * @return number of learners added
     */
    public int addLearners(Collection<User> newLearners) {
	if (newLearners == null) {
	    return 0;
	}

	Group learnersGroup = createLearnerGroupIfMissing();
	int originalNumber = learnersGroup.getUsers().size();
	learnersGroup.getUsers().addAll(newLearners);
	int newNumber = learnersGroup.getUsers().size();
	return newNumber - originalNumber;
    }

    /**
     * Sets the staff to the value given by the input collection.
     *
     * @return number of staff set
     */
    public int setLearners(Collection<User> newLearners) {
	if (newLearners == null) {
	    return 0;
	}

	Group learnersGroup = createLearnerGroupIfMissing();
	learnersGroup.getUsers().clear();
	learnersGroup.getUsers().addAll(newLearners);
	int newNumber = learnersGroup.getUsers().size();
	return newNumber;
    }

    public Group getLearnersGroup() {
	Group learnersGroup = null;
	Iterator<Group> iter = getGroups().iterator();
	while (learnersGroup == null && iter.hasNext()) {
	    learnersGroup = iter.next();
	    if (!isLearnerGroup(learnersGroup)) {
		learnersGroup = null;
	    }
	}
	return learnersGroup;
    }

    /**
     * Add a staff member to the lesson class. Checks for duplicates.
     *
     * @return true if added user, returns false if the user already a staff member and hence not added.
     */
    public boolean addStaffMember(User user) {

	if (user == null) {
	    return false;
	}

	// should be one ordinary group for lesson class, and this is all the learners in the lesson class
	Group staffGroup = getStaffGroup();
	if (staffGroup == null) {
	    staffGroup = createStaffGroupIfMissing();
	}

	if (!staffGroup.hasLearner(user)) {
	    staffGroup.getUsers().add(user);
	    return true;
	}
	return false;

    }

    /**
     * Add one or more staff members to the lesson class. Doesn't bother checking for duplicates it goes
     * into a set, and User does a check on userID field for equals anyway.
     *
     * @return number of learners added
     */
    public int addStaffMembers(Collection<User> newStaff) {
	if (newStaff == null) {
	    return 0;
	}

	Group staffGroup = createStaffGroupIfMissing();
	int originalNumber = staffGroup.getUsers().size();
	staffGroup.getUsers().addAll(newStaff);
	int newNumber = staffGroup.getUsers().size();
	return newNumber - originalNumber;
    }

    /**
     * Sets the staff to the value given by the input collection.
     *
     * @return number of staff set
     */
    public int setStaffMembers(Collection<User> newStaff) {
	if (newStaff == null) {
	    return 0;
	}

	Group staffGroup = createStaffGroupIfMissing();
	staffGroup.getUsers().clear();
	staffGroup.getUsers().addAll(newStaff);
	int newNumber = staffGroup.getUsers().size();
	return newNumber;
    }

    /**
     * When the users's are added from an external LMS e.g. Moodle, the Staff Group may not exist.
     * If that happens, then this code will ensure that there is a learners group, etc and so adding
     * a user won't throw an exception. This is just fallback code!!!!
     *
     * @return the staff group
     */
    private Group createStaffGroupIfMissing() {
	Group staffGroup = getStaffGroup();
	if (staffGroup == null) {
	    Organisation lessonOrganisation = getLesson() != null ? getLesson().getOrganisation() : null;
	    if (lessonOrganisation == null) {
		log.warn(
			"Adding a staff member to a lesson class with no related organisation. Staff group name will be \'staff\'.");
	    }
	    String staffGroupName = lessonOrganisation != null ? lessonOrganisation.getName() : "";
	    staffGroupName = staffGroupName + "staff";
	    setStaffGroup(Group.createStaffGroup(this, staffGroupName, new HashSet<User>()));
	    staffGroup = getStaffGroup();
	}
	return staffGroup;
    }

    private Lesson getLesson() {
	return (Lesson) UserManagementService.getInstance()
		.findByProperty(Lesson.class, "lessonClass.groupingId", getGroupingId()).get(0);
    }

}