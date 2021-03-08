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

package org.lamsfoundation.lams.lesson.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouper;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.exception.GroupingException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Access the general lesson details.
 *
 * A lesson has three different "lists" of learners.
 * <OL>
 * <LI>The learners who are in the learner group attached to the lesson. This is fixed when the lesson is started and is
 * a list of all the learners who could ever participate in to the lesson. This is available via lesson.getAllLearners()
 * <LI>The learners who have started the lesson. They may or may not be logged in currently, or if they are logged in
 * they may or may not be doing this lesson. This is available via getActiveLessonLearners().
 * </OL>
 *
 * There used to be a list of all the learners who were logged into a lesson. This has been removed as we do not need
 * the functionality at present. If this is required later it should be combined with the user's shared session logic
 * and will need to purge users who haven't done anything for a while - otherwise a user whose PC has crashed and then
 * never returns to a lesson will staying in the cache forever.
 *
 */
public class LessonService implements ILessonService {
    private static Logger log = Logger.getLogger(LessonService.class);

    private ILessonDAO lessonDAO;
    private ILessonClassDAO lessonClassDAO;
    private IGroupingDAO groupingDAO;
    private MessageService messageService;
    private IBaseDAO baseDAO;
    private ILearnerProgressDAO learnerProgressDAO;

    /* Spring injection methods */
    public void setLessonDAO(ILessonDAO lessonDAO) {
	this.lessonDAO = lessonDAO;
    }

    public void setLessonClassDAO(ILessonClassDAO lessonClassDAO) {
	this.lessonClassDAO = lessonClassDAO;
    }

    public void setGroupingDAO(IGroupingDAO groupingDAO) {
	this.groupingDAO = groupingDAO;
    }

    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO) {
	this.learnerProgressDAO = learnerProgressDAO;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    @Override
    public List getActiveLessonLearners(Long lessonId) {
	return lessonDAO.getActiveLearnerByLesson(lessonId);
    }

    @Override
    public Integer getCountActiveLessonLearners(Long lessonId) {
	return lessonDAO.getCountActiveLearnerByLesson(lessonId);
    }

    @Override
    public List<User> getLessonLearners(Long lessonId, String searchPhrase, Integer limit, Integer offset,
	    boolean orderAscending) {
	return lessonDAO.getLearnersByLesson(lessonId, searchPhrase, limit, offset, orderAscending);
    }

    @Override
    public Map<User, Boolean> getUsersWithLessonParticipation(Long lessonId, String role, String searchPhrase,
	    Integer limit, Integer offset, boolean orderByLastName, boolean orderAscending) {
	return lessonDAO.getUsersWithLessonParticipation(lessonId, role, searchPhrase, limit, offset, orderByLastName,
		orderAscending);
    }

    @Override
    public Integer getCountLessonLearners(Long lessonId, String searchPhrase) {
	return lessonDAO.getCountLearnersByLesson(lessonId, searchPhrase);
    }

    @Override
    public LessonDetailsDTO getLessonDetails(Long lessonId) {
	Lesson lesson = lessonDAO.getLesson(lessonId);
	LessonDetailsDTO dto = null;
	if (lesson != null) {
	    dto = lesson.getLessonDetails();
	    Integer active = getCountActiveLessonLearners(lessonId);
	    dto.setNumberStartedLearners(active != null ? active : new Integer(0));
	}
	return dto;
    }

    @Override
    public Lesson getLesson(Long lessonId) {
	return lessonDAO.getLesson(lessonId);
    }

    @Override
    public Lesson getLessonByToolContentId(long toolContentId) {
	ToolActivity toolActivity = baseDAO.findByProperty(ToolActivity.class, "toolContentId", toolContentId).get(0);
	return toolActivity.getLearningDesign().getLessons().iterator().next();
    }

    @Override
    public void performGrouping(Long lessonId, GroupingActivity groupingActivity, User learner)
	    throws LessonServiceException {
	Grouping grouping = groupingActivity.getCreateGrouping();
	if ((grouping != null) && grouping.isRandomGrouping()) {
	    // get the real objects, not the CGLIB version
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();

	    if (grouper != null) {
		grouper.setCommonMessageService(messageService);
		try {
		    if (grouping.getGroups().size() == 0) {
			// no grouping done yet - do everyone already in the lesson.
			List usersInLesson = getActiveLessonLearners(lessonId);
			grouper.doGrouping(grouping, (String) null, usersInLesson);
		    } else if (!grouping.doesLearnerExist(learner)) {
			// done the others, just do the one user
			grouper.doGrouping(grouping, null, learner);
		    }
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
		groupingDAO.update(grouping);
	    }

	} else {
	    String error = "The method performGrouping supports only grouping methods where the grouper decides the groups (currently only RandomGrouping). Called with a groupingActivity with the wrong grouper "
		    + groupingActivity.getActivityId();
	    LessonService.log.error(error);
	    throw new LessonServiceException(error);
	}
    }

    @Override
    public void performGrouping(GroupingActivity groupingActivity, String groupName, List learners)
	    throws LessonServiceException {

	Grouping grouping = groupingActivity.getCreateGrouping();
	performGrouping(grouping, groupName, learners);
    }

    @Override
    public void performGrouping(Grouping grouping, String groupName, List learners) throws LessonServiceException {

	if (grouping != null) {
	    // Ensure we have a real grouping object, not just a CGLIB version (LDEV-1817)
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		grouper.setCommonMessageService(messageService);
		try {
		    grouper.doGrouping(grouping, groupName, learners);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
		groupingDAO.update(grouping);
	    }
	}
    }

    @Override
    public void performGrouping(Grouping grouping, Long groupId, User learner) throws LessonServiceException {
	if (grouping != null) {
	    // Ensure we have a real grouping object, not just a CGLIB version (LDEV-1817)
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		grouper.setCommonMessageService(messageService);
		try {
		    List<User> learners = new ArrayList<>(1);
		    learners.add(learner);
		    grouper.doGrouping(grouping, groupId, learners);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
		groupingDAO.update(grouping);
	    }
	}
    }

    @Override
    public void performGrouping(Grouping grouping, Long groupId, List learners) throws LessonServiceException {
	if (grouping != null) {
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		grouper.setCommonMessageService(messageService);
		try {
		    grouper.doGrouping(grouping, groupId, learners);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
		groupingDAO.update(grouping);
	    }
	} else {
	    String error = "The method performChosenGrouping supports only grouping methods where the supplied list should be used as a single group (currently only ChosenGrouping). Called with a grouping with the wrong grouper "
		    + grouping;
	    LessonService.log.error(error);
	    throw new LessonServiceException(error);
	}
    }

    @Override
    public void removeLearnersFromGroup(Grouping grouping, Long groupID, List<User> learners)
	    throws LessonServiceException {
	if (grouping != null) {
	    // get the real objects, not the CGLIB version
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		try {
		    grouper.removeLearnersFromGroup(grouping, groupID, learners);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
	    }
	    groupingDAO.update(grouping);
	}
    }

    @Override
    public void removeAllLearnersFromGrouping(Grouping grouping) throws LessonServiceException {
	if (grouping != null) {
	    // get the real objects, not the CGLIB version
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		try {
		    grouper.removeAllLearnersFromGrouping(grouping);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
	    }
	    groupingDAO.update(grouping);
	}
    }

    @Override
    public Group createGroup(Grouping grouping, String name) throws LessonServiceException {
	Group newGroup = null;
	if (grouping != null) {
	    // get the real objects, not the CGLIB version
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		try {
		    newGroup = grouper.createGroup(grouping, name);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
	    }
	    groupingDAO.update(grouping);
	}
	return newGroup;
    }

    @Override
    public void removeGroup(Grouping grouping, Long groupID) throws LessonServiceException {
	if (grouping != null) {
	    // get the real objects, not the CGLIB version
	    grouping = groupingDAO.getGroupingById(grouping.getGroupingId());
	    Grouper grouper = grouping.getGrouper();
	    if (grouper != null) {
		try {
		    grouper.removeGroup(grouping, groupID);
		} catch (GroupingException e) {
		    throw new LessonServiceException(e);
		}
	    }
	    groupingDAO.update(grouping);
	}
    }

    @Override
    public boolean addLearner(Long lessonId, Integer userId) throws LessonServiceException {
	Lesson lesson = lessonDAO.getLesson(lessonId);
	if (lesson == null) {
	    throw new LessonServiceException(
		    "Lesson " + lessonId + " does not exist. Unable to add learner to lesson.");
	}

	LessonClass lessonClass = lesson.getLessonClass();
	if (lessonClass == null) {
	    throw new LessonServiceException(
		    "Lesson class for " + lessonId + " does not exist. Unable to add learner to lesson.");
	}

	// initialise the lesson group, or we get a lazy loading error when logging in
	// from moodle. Should only be two groups - learner and staff
	// yes this is a bit of a hack!
	Group learnersGroup = lessonClass.getLearnersGroup();
	if (learnersGroup != null) {
	    lessonDAO.initialize(learnersGroup);
	}

	User user = baseDAO.find(User.class, userId);
	boolean ret = lessonClass.addLearner(user);
	if (ret) {
	    lessonClassDAO.updateLessonClass(lessonClass);
	}
	return ret;
    }

    @Override
    public boolean removeLearner(Long lessonId, Integer userId) {
	Lesson lesson = lessonDAO.getLesson(lessonId);
	LessonClass lessonClass = lesson.getLessonClass();
	User user = baseDAO.find(User.class, userId);
	Group learnerGroup = lessonClass.getGroupBy(user);
	boolean result = learnerGroup.getUsers().remove(user);

	if (result) {
	    lessonClassDAO.updateLessonClass(lessonClass);
	}
	return result;
    }

    @Override
    public void addLearners(Long lessonId, Integer[] userIds) throws LessonServiceException {

	Lesson lesson = lessonDAO.getLesson(lessonId);
	if (lesson == null) {
	    throw new LessonServiceException(
		    "Lesson " + lessonId + " does not exist. Unable to add learner to lesson.");
	}
	LessonClass lessonClass = lesson.getLessonClass();
	if (lessonClass == null) {
	    throw new LessonServiceException(
		    "Lesson class for " + lessonId + " does not exist. Unable to add learner to lesson.");
	}

	// initialise the lesson group, or we might get a lazy loading error in the future
	// when logging in from an external system. Should only be two groups - learner and staff
	// yes this is a bit of a hack!
	Group learnersGroup = lessonClass.getLearnersGroup();
	if (learnersGroup != null) {
	    lessonDAO.initialize(learnersGroup);
	}

	Set<User> users = new HashSet<>();
	for (Integer userId : userIds) {
	    User user = baseDAO.find(User.class, userId);
	    users.add(user);
	}
	addLearners(lesson, users);
    }

    @Override
    public void addLearners(Lesson lesson, Collection<User> users) throws LessonServiceException {

	LessonClass lessonClass = lesson.getLessonClass();
	int numAdded = lessonClass.addLearners(users);
	if (numAdded > 0) {
	    lessonClassDAO.updateLessonClass(lessonClass);
	}
	if (LessonService.log.isDebugEnabled()) {
	    LessonService.log.debug("Added " + numAdded + " learners to lessonClass " + lessonClass.getGroupingId());
	}
    }

    @Override
    public void updateLearners(Lesson lesson, Collection<User> users) throws LessonServiceException {
	LessonClass lessonClass = lesson.getLessonClass();
	int numberOfLearners = lessonClass.setLearners(users);
	lessonClassDAO.updateLessonClass(lessonClass);
	if (LessonService.log.isDebugEnabled()) {
	    LessonService.log
		    .debug("Set " + numberOfLearners + " learners in lessonClass " + lessonClass.getGroupingId());
	}
    }

    @Override
    public boolean addStaffMember(Long lessonId, Integer userId) {
	Lesson lesson = lessonDAO.getLesson(lessonId);
	if (lesson == null) {
	    throw new LessonServiceException(
		    "Lesson " + lessonId + " does not exist. Unable to add staff member to lesson.");
	}

	LessonClass lessonClass = lesson.getLessonClass();

	if (lessonClass == null) {
	    throw new LessonServiceException(
		    "Lesson class for " + lessonId + " does not exist. Unable to add staff member to lesson.");
	}

	lessonDAO.initialize(lessonClass.getStaffGroup());
	User user = baseDAO.find(User.class, userId);

	boolean ret = lessonClass.addStaffMember(user);
	if (ret) {
	    lessonClassDAO.updateLessonClass(lessonClass);
	}
	return ret;
    }

    @Override
    public boolean removeStaffMember(Long lessonId, Integer userId) {
	Lesson lesson = lessonDAO.getLesson(lessonId);
	LessonClass lessonClass = lesson.getLessonClass();
	User user = baseDAO.find(User.class, userId);
	Group staffGroup = lessonClass.getStaffGroup();
	boolean result = staffGroup.getUsers().remove(user);

	if (result) {
	    lessonClassDAO.updateLessonClass(lessonClass);
	}
	return result;
    }

    @Override
    public void addStaffMembers(Long lessonId, Integer[] userIds) throws LessonServiceException {

	Lesson lesson = lessonDAO.getLesson(lessonId);
	if (lesson == null) {
	    throw new LessonServiceException(
		    "Lesson " + lessonId + " does not exist. Unable to add learner to lesson.");
	}
	LessonClass lessonClass = lesson.getLessonClass();
	if (lessonClass == null) {
	    throw new LessonServiceException(
		    "Lesson class for " + lessonId + " does not exist. Unable to add learner to lesson.");
	}

	// initialise the lesson group, or we might get a lazy loading error in the future
	// when logging in from an external system. Should only be two groups - learner and staff
	// yes this is a bit of a hack!
	lessonDAO.initialize(lessonClass.getStaffGroup());

	Set<User> users = new HashSet<>();
	for (Integer userId : userIds) {
	    User user = baseDAO.find(User.class, userId);
	    users.add(user);
	}
	addStaffMembers(lesson, users);
    }

    @Override
    public void addStaffMembers(Lesson lesson, Collection<User> users) throws LessonServiceException {

	LessonClass lessonClass = lesson.getLessonClass();
	int numAdded = lessonClass.addStaffMembers(users);
	if (numAdded > 0) {
	    lessonClassDAO.updateLessonClass(lessonClass);
	}
	if (LessonService.log.isDebugEnabled()) {
	    LessonService.log
		    .debug("Added " + numAdded + " staff members to lessonClass " + lessonClass.getGroupingId());
	}
    }

    @Override
    public void updateStaffMembers(Lesson lesson, Collection<User> users) throws LessonServiceException {
	LessonClass lessonClass = lesson.getLessonClass();
	int numberOfStaff = lessonClass.setStaffMembers(users);
	lessonClassDAO.updateLessonClass(lessonClass);
	if (LessonService.log.isDebugEnabled()) {
	    LessonService.log
		    .debug("Set " + numberOfStaff + " staff members in lessonClass " + lessonClass.getGroupingId());
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeProgressReferencesToActivity(Activity activity) {
	if (activity != null) {
	    LessonService.log.debug("Processing learner progress for activity " + activity.getActivityId());

	    List<LearnerProgress> progresses = learnerProgressDAO.getLearnerProgressReferringToActivity(activity);
	    if (progresses != null) {
		for (LearnerProgress progress : progresses) {
		    if (LessonService.log.isDebugEnabled()) {
			LessonService.log
				.debug("Processing learner progress learner " + progress.getUser().getUserId());
		    }

		    boolean recordUpdated = false;
		    boolean removed = progress.getAttemptedActivities().remove(activity) != null;
		    if (removed) {
			recordUpdated = true;
			LessonService.log.debug("Removed activity from attempted activities");
		    }

		    removed = progress.getCompletedActivities().remove(activity) != null;
		    if (removed) {
			recordUpdated = true;
			LessonService.log.debug("Removed activity from completed activities");
		    }

		    if ((progress.getCurrentActivity() != null) && progress.getCurrentActivity().equals(activity)) {
			progress.setCurrentActivity(null);
			recordUpdated = true;
			LessonService.log.debug("Removed activity as current activity");
		    }

		    if ((progress.getNextActivity() != null) && progress.getNextActivity().equals(activity)) {
			progress.setNextActivity(null);
			recordUpdated = true;
			LessonService.log.debug("Removed activity as next activity");
		    }

		    if ((progress.getPreviousActivity() != null) && progress.getPreviousActivity().equals(activity)) {
			progress.setPreviousActivity(null);
			recordUpdated = true;
			LessonService.log.debug("Removed activity as previous activity");
		    }

		    if (recordUpdated) {
			learnerProgressDAO.updateLearnerProgress(progress);
		    }
		}
	    }
	}
    }

    /**
     * Completely removes learner progress as if the user has not started the lesson yet.
     */
    @Override
    public void removeLearnerProgress(Long lessonId, Integer userId) {
	LearnerProgress learnerProgress = getUserProgressForLesson(userId, lessonId);
	if (learnerProgress != null) {
	    learnerProgressDAO.deleteLearnerProgress(learnerProgress);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void performMarkLessonUncompleted(Long lessonId, Long firstAddedActivityId) throws LessonServiceException {
	if (LessonService.log.isDebugEnabled()) {
	    LessonService.log.debug("Setting learner progress to uncompleted for lesson " + lessonId);
	}
	int count = 0;
	List<LearnerProgress> progresses = learnerProgressDAO.getCompletedLearnerProgressForLesson(lessonId, null, null,
		true);
	Activity firstAddedActivity = baseDAO.find(Activity.class, firstAddedActivityId);

	for (LearnerProgress progress : progresses) {
	    if (progress.getLessonComplete() == LearnerProgress.LESSON_END_OF_DESIGN_COMPLETE) {
		progress.setLessonComplete(LearnerProgress.LESSON_NOT_COMPLETE);
		progress.setFinishDate(null);
		// set the next activity user will take
		// previous activity should also be set, but it is not vital
		progress.setCurrentActivity(firstAddedActivity);
		progress.setNextActivity(firstAddedActivity);

		count++;
	    }
	}

	if (LessonService.log.isDebugEnabled()) {
	    LessonService.log.debug("Reset completed flag for " + count + " learners for lesson " + lessonId);
	}
    }

    @Override
    public List<User> getLearnersAttemptedOrCompletedActivity(Activity activity) throws LessonServiceException {
	return learnerProgressDAO.getLearnersAttemptedOrCompletedActivity(activity);
    }

    @Override
    public Integer getCountLearnersHaveAttemptedActivity(Activity activity) throws LessonServiceException {
	return learnerProgressDAO.getNumUsersAttemptedActivity(activity);
    }

    @Override
    public Integer getCountLearnersInCurrentActivity(Activity activity) {
	return learnerProgressDAO.getNumUsersCurrentActivity(activity);
    }

    @Override
    public Map<Long, IndexLessonBean> getLessonsByOrgAndUserWithCompletedFlag(Integer userId, Integer orgId,
	    Integer userRole) {
	TreeMap<Long, IndexLessonBean> map = new TreeMap<>();
	List list = lessonDAO.getLessonsByOrgAndUserWithCompletedFlag(userId, orgId, userRole);
	if (list != null) {
	    Iterator iterator = list.iterator();
	    while (iterator.hasNext()) {
		Object[] tuple = (Object[]) iterator.next();
		Long lessonId = (Long) tuple[0];
		String lessonName = (String) tuple[1];
		String lessonDescription = (String) tuple[2];
		Integer lessonState = (Integer) tuple[3];
		Boolean lessonCompleted = (Boolean) tuple[4];
		lessonCompleted = lessonCompleted == null ? false : lessonCompleted.booleanValue();
		Boolean enableLessonNotifications = (Boolean) tuple[5];
		enableLessonNotifications = enableLessonNotifications == null ? false
			: enableLessonNotifications.booleanValue();
		Boolean dependent = (Boolean) tuple[6];
		dependent = dependent == null ? false : dependent.booleanValue();
		Boolean scheduledFinish = (Boolean) tuple[7];
		IndexLessonBean bean = new IndexLessonBean(lessonId, lessonName, lessonDescription, lessonState,
			lessonCompleted, enableLessonNotifications, dependent, scheduledFinish);
		map.put(new Long(lessonId), bean);
	    }
	}
	return map;
    }

    @Override
    public List<Lesson> getLessonsByGroupAndUser(Integer userId, Integer organisationId) {
	List<Lesson> list = lessonDAO.getLessonsByGroupAndUser(userId, organisationId);
	return list;
    }

    @Override
    public List<Lesson> getLessonsByGroup(Integer organisationId) {
	List<Lesson> list = lessonDAO.getLessonsByGroup(organisationId);
	return list;
    }

    @Override
    public LearnerProgress getUserProgressForLesson(Integer learnerId, Long lessonId) {
	return learnerProgressDAO.getLearnerProgressByLearner(learnerId, lessonId);
    }

    @Override
    public List<LearnerProgress> getUserProgressForLesson(Long lessonId) {
	List<LearnerProgress> list = learnerProgressDAO.getLearnerProgressForLesson(lessonId);
	return list;
    }

    @Override
    public List getLessonsByOriginalLearningDesign(Long ldId, Integer orgId) {
	return lessonDAO.getLessonsByOriginalLearningDesign(ldId, orgId);
    }

    @Override
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return lessonDAO.getMonitorsByToolSessionId(sessionId);
    }

    @Override
    public long[] getPreviewLessonCount() {
	return lessonDAO.getPreviewLessonCount();
    }

    @Override
    public List<Long> getPreviewLessons(Integer limit) {
	return lessonDAO.getPreviewLessons(limit);
    }

    @Override
    public List<Long> getOrganisationLessons(Integer organisationId) {
	return lessonDAO.getOrganisationLessons(organisationId);
    }

    @Override
    public boolean checkLessonReleaseConditions(Long lessonId, Integer learnerId) {
	Lesson lesson = getLesson(lessonId);
	if (lesson != null) {
	    for (Lesson precedingLesson : lesson.getPrecedingLessons()) {
		LearnerProgress progress = getUserProgressForLesson(learnerId, precedingLesson.getLessonId());
		if ((progress == null) || !progress.isComplete()) {
		    return false;
		}
	    }
	}
	return true;
    }

    @Override
    public Set<Lesson> getReleasedSucceedingLessons(Long completedLessonId, Integer learnerId) {
	Set<Lesson> releasedSucceedingLessons = new HashSet<>();
	Lesson lesson = getLesson(completedLessonId);
	if (lesson != null) {
	    for (Lesson succeedingLesson : lesson.getSucceedingLessons()) {
		if (succeedingLesson.isLessonAccessibleForLearner()
			&& checkLessonReleaseConditions(succeedingLesson.getLessonId(), learnerId)) {
		    releasedSucceedingLessons.add(succeedingLesson);
		}
	    }
	}
	return releasedSucceedingLessons;
    }

    @Override
    public void saveLesson(Lesson lesson) {
	lessonDAO.saveLesson(lesson);
    }
}