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

package org.lamsfoundation.lams.learning.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.command.CommandWebsocketServer;
import org.lamsfoundation.lams.learning.command.dao.ICommandDAO;
import org.lamsfoundation.lams.learning.command.model.Command;
import org.lamsfoundation.lams.learning.kumalive.model.Kumalive;
import org.lamsfoundation.lams.learning.kumalive.service.IKumaliveService;
import org.lamsfoundation.lams.learning.progress.ProgressBuilder;
import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ConditionGateActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouper;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.ToolBranchingActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.learningdesign.dto.GateActivityDTO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.exception.RequiredGroupMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * This class is a facade over the Learning middle tier.
 *
 * @author chris, Jacky Fang
 */
public class LearnerService implements ILearnerFullService {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(LearnerService.class);

    private ILearnerProgressDAO learnerProgressDAO;
    private ILessonDAO lessonDAO;
    private IActivityDAO activityDAO;
    private IGroupingDAO groupingDAO;
    private IGroupUserDAO groupUserDAO;
    private ProgressEngine progressEngine;
    private ICommandDAO commandDAO;
    private ILamsCoreToolService lamsCoreToolService;
    private ActivityMapping activityMapping;
    private IUserManagementService userManagementService;
    private ILessonService lessonService;
    private static HashMap<Integer, Long> syncMap = new HashMap<>();
    private IGradebookService gradebookService;
    private ILogEventService logEventService;
    private IKumaliveService kumaliveService;
    private MessageService messageService;

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Constructor injection
    // ---------------------------------------------------------------------

    /** Creates a new instance of LearnerService */
    public LearnerService(ProgressEngine progressEngine) {
	this.progressEngine = progressEngine;
    }

    /**
     * Creates a new instance of LearnerService. To be used by Spring, assuming the Spring will set up the progress
     * engine via method injection. If you are creating the bean manually then use the other constructor.
     */
    public LearnerService() {
    }

    /**
     * @param lessonDAO
     *            The lessonDAO to set.
     */
    public void setLessonDAO(ILessonDAO lessonDAO) {
	this.lessonDAO = lessonDAO;
    }

    /**
     * @param learnerProgressDAO
     *            The learnerProgressDAO to set.
     */
    public void setLearnerProgressDAO(ILearnerProgressDAO learnerProgressDAO) {
	this.learnerProgressDAO = learnerProgressDAO;
    }

    /**
     * @param lamsToolService
     *            The lamsToolService to set.
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsToolService) {
	lamsCoreToolService = lamsToolService;
    }

    public void setActivityMapping(ActivityMapping activityMapping) {
	this.activityMapping = activityMapping;
    }

    /**
     * @param activityDAO
     *            The activityDAO to set.
     */
    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    /**
     * @param groupingDAO
     *            The groupingDAO to set.
     */
    public void setGroupingDAO(IGroupingDAO groupingDAO) {
	this.groupingDAO = groupingDAO;
    }

    /**
     * @return the groupUserDAO
     */
    public IGroupUserDAO getGroupUserDAO() {
	return groupUserDAO;
    }

    /**
     * @param groupUserDAO
     *            groupUserDAO
     */
    public void setGroupUserDAO(IGroupUserDAO groupUserDAO) {
	this.groupUserDAO = groupUserDAO;
    }

    /**
     * @return the User Management Service
     */
    @Override
    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    /**
     * @param userService
     *            User Management Service
     */
    public void setUserManagementService(IUserManagementService userService) {
	userManagementService = userService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setKumaliveService(IKumaliveService kumaliveService) {
	this.kumaliveService = kumaliveService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    // ---------------------------------------------------------------------
    // Service Methods
    // ---------------------------------------------------------------------
    /**
     * Delegate to lesson dao to load up the lessons.
     *
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#getActiveLessonsFor(org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public LessonDTO[] getActiveLessonsFor(Integer learnerId) {
	User learner = (User) userManagementService.findById(User.class, learnerId);
	List<Lesson> activeLessons = lessonDAO.getActiveLessonsForLearner(learner);
	// remove lessons which do not have preceding lessons finished
	Iterator<Lesson> lessonIter = activeLessons.iterator();
	while (lessonIter.hasNext()) {
	    if (!lessonService.checkLessonReleaseConditions(lessonIter.next().getLessonId(), learnerId)) {
		lessonIter.remove();
	    }
	}
	return getLessonDataFor(activeLessons);
    }

    @Override
    public Lesson getLesson(Long lessonId) {
	return lessonDAO.getLesson(lessonId);
    }

    /**
     * <p>
     * Joins a User to a lesson as a learner. It could either be a new lesson or a lesson that has been started.
     * </p>
     *
     * <p>
     * In terms of new lesson, a new learner progress would be initialized. Tool session for the next activity will be
     * initialized if necessary.
     * </p>
     *
     * <p>
     * In terms of an started lesson, the learner progress will be returned without calculation. Tool session will be
     * initialized if necessary. Note that we won't initialize tool session for current activity because we assume tool
     * session will always initialize before it becomes a current activity. </p
     *
     *
     * @param learnerId
     *            the Learner's userID
     * @param lessionID
     *            identifies the Lesson to start
     * @throws LamsToolServiceException
     * @throws LearnerServiceException
     *             in case of problems.
     */
    @Override
    public LearnerProgress joinLesson(Integer learnerId, Long lessonID) {
	User learner = (User) userManagementService.findById(User.class, learnerId);

	Lesson lesson = getLesson(lessonID);

	if ((lesson == null) || !lesson.isLessonStarted()) {
	    log.error("joinLesson: Learner " + learner.getLogin() + " joining lesson " + lesson
		    + " but lesson has not started");
	    throw new LearnerServiceException("Cannot join lesson as lesson has not started");
	}
	if (!lessonService.checkLessonReleaseConditions(lessonID, learnerId)) {
	    throw new LearnerServiceException("Cannot join lesson as preceding lessons have not been finished");
	}

	LearnerProgress learnerProgress = learnerProgressDAO.getLearnerProgressByLearner(learner.getUserId(), lessonID);

	if (learnerProgress == null) {
	    // create a new learner progress for new learner
	    learnerProgress = new LearnerProgress(learner, lesson);

	    try {
		progressEngine.setUpStartPoint(learnerProgress);
	    } catch (ProgressException e) {
		log.error("error occurred in 'setUpStartPoint':" + e.getMessage());
		throw new LearnerServiceException(e.getMessage());
	    }
	    // Use TimeStamp rather than Date directly to keep consistent with Hibnerate persiste object.
	    learnerProgress.setStartDate(new Timestamp(new Date().getTime()));
	    learnerProgressDAO.saveLearnerProgress(learnerProgress);

	    // check if lesson is set to be finished for individual users then store finish date
	    if (lesson.isScheduledToCloseForIndividuals()) {
		GroupUser groupUser = groupUserDAO.getGroupUser(lesson, learnerId);
		if (groupUser != null) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(learnerProgress.getStartDate());
		    calendar.add(Calendar.DATE, lesson.getScheduledNumberDaysToLessonFinish());
		    Date endDate = calendar.getTime();
		    groupUser.setScheduledLessonEndDate(endDate);
		}
	    }

	} else {

	    Activity currentActivity = learnerProgress.getCurrentActivity();
	    if (currentActivity == null) {
		// something may have gone wrong and we need to recalculate the current activity
		try {
		    progressEngine.setUpStartPoint(learnerProgress);
		} catch (ProgressException e) {
		    log.error("error occurred in 'setUpStartPoint':" + e.getMessage());
		    throw new LearnerServiceException(e.getMessage());
		}
	    }

	    // The restarting flag should be setup when the learner hit the exit
	    // button. But it is possible that user exit by closing the browser,
	    // In this case, we set the restarting flag again.
	    if (!learnerProgress.isRestarting()) {
		learnerProgress.setRestarting(true);
		learnerProgressDAO.updateLearnerProgress(learnerProgress);
	    }
	}

	return learnerProgress;
    }

    /**
     * This method creates the tool session (if needed) for a tool activity. It won't try it for the child activities,
     * as any Sequence activities inside this activity may have a grouping WITHIN the sequence, and then the grouped
     * activities get the won't be grouped properly (See LDEV-1774). We could get the child tool activities for a
     * parallel activity but that could create a bug in the future - if we ever put sequences inside parallel activities
     * then we are stuck again!
     *
     * We look up the database to check up the existence of correspondent tool session. If the tool session doesn't
     * exist, we create a new tool session instance.
     *
     * @param learnerProgress
     *            the learner progress we are processing.
     * @throws LamsToolServiceException
     */
    @Override
    public void createToolSessionsIfNecessary(Activity activity, LearnerProgress learnerProgress) {
	try {
	    if ((activity != null) && activity.isToolActivity()) {
		// make sure that the lesson corresponds to the activity
		LearningDesign learningDesign = activity.getLearningDesign();
		Lesson lesson = learningDesign.getLessons().iterator().next();
		lamsCoreToolService.createToolSession(learnerProgress.getUser(), (ToolActivity) activity, lesson);
	    }
	} catch (RequiredGroupMissingException e) {
	    log.warn("error occurred in 'createToolSessionFor':" + e.getMessage());
	    throw e;
	} catch (ToolException e) {
	    log.error("error occurred in 'createToolSessionFor':" + e.getMessage());
	    throw new LearnerServiceException(e.getMessage());
	}
    }

    /**
     * Returns the current progress data of the User.
     *
     * @param learnerId
     *            the Learner's userID
     * @param lessonId
     *            the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException
     *             in case of problems.
     */
    @Override
    public LearnerProgress getProgress(Integer learnerId, Long lessonId) {
	return learnerProgressDAO.getLearnerProgressByLearner(learnerId, lessonId);
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#getProgressById(java.lang.Long)
     */
    @Override
    public LearnerProgress getProgressById(Long progressId) {
	return learnerProgressDAO.getLearnerProgress(progressId);
    }

    @Override
    public Integer getProgressArchiveMaxAttemptID(Integer userId, Long lessonId) {
	return learnerProgressDAO.getLearnerProgressArchiveMaxAttemptID(userId, lessonId);
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#getStructuredProgressDTOs(java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public Object[] getStructuredActivityURLs(Integer learnerId, Long lessonId) {

	LearnerProgress progress = learnerProgressDAO.getLearnerProgressByLearner(learnerId, lessonId);
	if (progress == null) {
	    return null;
	}
	Lesson lesson = progress.getLesson();

	ProgressBuilder builder = new ProgressBuilder(progress, activityDAO, activityMapping);
	builder.parseLearningDesign();

	Object[] retValue = new Object[3];
	retValue[0] = builder.getActivityList();

	retValue[1] = progress.getCurrentActivity() != null ? progress.getCurrentActivity().getActivityId() : null;
	retValue[2] = lesson.isPreviewLesson();

	return retValue;
    }

    @Override
    public List<ActivityURL> getStructuredActivityURLs(Long lessonId) {
	Lesson lesson = getLesson(lessonId);
	User learner = lesson.getAllLearners().iterator().next();
	LearnerProgress learnerProgress = new LearnerProgress(learner, lesson);

	ProgressBuilder builder = new ProgressBuilder(learnerProgress, activityDAO, activityMapping);
	builder.parseLearningDesign();
	return builder.getActivityList();
    }

    @Override
    public LearnerProgress chooseActivity(Integer learnerId, Long lessonId, Activity activity,
	    Boolean clearCompletedFlag) {
	LearnerProgress progress = learnerProgressDAO.getLearnerProgressByLearner(learnerId, lessonId);

	if (!progress.getCompletedActivities().containsKey(activity)) {
	    // if we skip a sequence in an optional sequence, or have been force completed for branching / optional
	    // sequence
	    // and we go back to the sequence later, then the LessonComplete flag must be reset so that it will step
	    // through
	    // all the activities in the sequence - otherwise it will go to the "Completed" screen after the first
	    // activity in the sequence
	    if (clearCompletedFlag && (activity.getParentActivity() != null)
		    && activity.getParentActivity().isSequenceActivity() && progress.isComplete()) {
		progress.setLessonComplete(LearnerProgress.LESSON_NOT_COMPLETE);
	    }

	    progressEngine.setActivityAttempted(progress, activity);

	    progress.setCurrentActivity(activity);
	    progress.setNextActivity(activity);

	    learnerProgressDAO.saveLearnerProgress(progress);
	}

	return progress;
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#moveToActivity(java.lang.Integer,
     *      java.lang.Long, org.lamsfoundation.lams.learningdesign.Activity,
     *      org.lamsfoundation.lams.learningdesign.Activity)
     */
    @Override
    public LearnerProgress moveToActivity(Integer learnerId, Long lessonId, Activity fromActivity, Activity toActivity)
	    throws LearnerServiceException {
	int count = 0;
	LearnerProgress progress = null;

	// wait till lock is released
	while (LearnerService.syncMap.containsKey(learnerId)) {
	    count++;
	    try {
		Thread.sleep(1000);

		if (count > 100) {
		    throw new LearnerServiceException("Thread wait count exceeded limit.");
		}

	    } catch (InterruptedException e1) {
		throw new LearnerServiceException("While retrying to move activity, thread was interrupted.", e1);
	    }
	}

	// lock
	try {

	    LearnerService.syncMap.put(learnerId, lessonId);

	    progress = learnerProgressDAO.getLearnerProgressByLearner(learnerId, lessonId);

	    if ((fromActivity != null)
		    && (fromActivity.getActivityId() != progress.getCurrentActivity().getActivityId())) {
		progress.setProgressState(fromActivity, LearnerProgress.ACTIVITY_ATTEMPTED, activityDAO);
	    }

	    if (toActivity != null) {
		progress.setProgressState(toActivity, LearnerProgress.ACTIVITY_ATTEMPTED, activityDAO);

		if (!toActivity.getReadOnly()) {
		    toActivity.setReadOnly(true);
		    activityDAO.update(toActivity);
		}

		if (!toActivity.isFloating()) {
		    progress.setCurrentActivity(toActivity);
		    progress.setNextActivity(toActivity);
		}
	    }

	    learnerProgressDAO.updateLearnerProgress(progress);
	} catch (Exception e) {
	    throw new LearnerServiceException(e.getMessage());
	} finally {
	    // remove lock
	    if (LearnerService.syncMap.containsKey(learnerId)) {
		LearnerService.syncMap.remove(learnerId);
	    }
	}

	return progress;

    }

    /**
     * Calculates learner progress and returns the data required to be displayed to the learner.
     *
     * @param completedActivity
     *            the activity just completed
     * @param learner
     *            the Learner
     * @param learnerProgress
     *            the current progress
     * @return the bean containing the display data for the Learner
     * @throws LamsToolServiceException
     * @throws LearnerServiceException
     *             in case of problems.
     */
    private void calculateProgress(Activity completedActivity, Integer learnerId, LearnerProgress learnerProgress) {
	try {
	    progressEngine.calculateProgress(learnerProgress.getUser(), completedActivity, learnerProgress);
	    learnerProgressDAO.updateLearnerProgress(learnerProgress);
	} catch (ProgressException e) {
	    throw new LearnerServiceException(e.getMessage());
	}

    }

    @Override
    public String completeToolSession(Long toolSessionId, Long learnerId) {
	// this method is called by tools, so it mustn't do anything that relies on all the tools' Spring beans
	// being available in the context. Hence it is defined in the ILearnerService interface, not the
	// IFullLearnerService
	// interface. If it calls any other methods then it mustn't use anything on the ICoreLearnerService interface.

	String returnURL = null;

	ToolSession toolSession = lamsCoreToolService.getToolSessionById(toolSessionId);
	if (toolSession == null) {
	    // something has gone wrong - maybe due to Live Edit. The tool session supplied by the tool doesn't exist.
	    // have to go to a "can't do anything" screen and the user will have to hit resume.
	    returnURL = activityMapping.getProgressBrokenURL();

	} else {
	    Long lessonId = toolSession.getLesson().getLessonId();
	    LearnerProgress currentProgress = getProgress(new Integer(learnerId.intValue()), lessonId);
	    // TODO Cache the learner progress in the session, but mark it with the progress id. Then get the progress
	    // out of the session
	    // for ActivityAction.java.completeActivity(). Update LearningWebUtil to look under the progress id, so we
	    // don't get
	    // a conflict in Preview & Learner.
	    returnURL = activityMapping.getCompleteActivityURL(toolSession.getToolActivity().getActivityId(),
		    currentProgress.getLearnerProgressId());

	}

	if (log.isDebugEnabled()) {
	    log.debug("CompleteToolSession() for tool session id " + toolSessionId + " learnerId "
		    + learnerId + " url is " + returnURL);
	}

	return returnURL;
    }

    @Override
    public String completeActivity(ActivityMapping actionMappings, LearnerProgress progress, Activity currentActivity,
	    Integer learnerId, boolean redirect) throws UnsupportedEncodingException {
	Lesson lesson = progress.getLesson();

	if (currentActivity == null) {
	    progress = joinLesson(learnerId, lesson.getLessonId());
	    
	} else if (progress.getCompletedActivities().containsKey(currentActivity)) {
	    // recalculate activity mark and pass it to gradebook
	    updateGradebookMark(currentActivity, progress);
	    return actionMappings.getCloseForward(currentActivity, lesson.getLessonId());
	    
	} else {
	    completeActivity(learnerId, currentActivity, progress.getLearnerProgressId());
	}

	if (currentActivity != null && (currentActivity.isFloating() || (currentActivity.getParentActivity() != null
		&& progress.getCompletedActivities().containsKey(currentActivity.getParentActivity())))) {
	    return actionMappings.getCloseForward(currentActivity, lesson.getLessonId());
	}

	boolean displayParallelFrames = false;
	return actionMappings.getProgressForward(progress, redirect, displayParallelFrames);
    }

    @Override
    public void completeActivity(Integer learnerId, Activity activity, Long progressID) {
	if (log.isDebugEnabled()) {
	    log.debug("Completing activity ID " + activity.getActivityId() + " for learner " + learnerId);
	}
	LearnerProgress progress = learnerProgressDAO.getLearnerProgress(progressID);
	if (progress.getCompletedActivities().keySet().contains(activity)) {
	    // progress was already updated by another thread, so prevent double processing
	    return;
	}

	// Need to synchronise the next bit of code so that if the tool calls
	// this twice in quick succession, with the same parameters, it won't update
	// the database twice! This may happen if a tool has a double submission problem.
	// I don't want to synchronise on (this), as this could cause too much of a bottleneck,
	// but if its not synchronised, we get db errors if the same tool session is completed twice
	// (invalid index). I can'tfind another object on which to synchronise - Hibernate does not give me the
	// same object for tool session or current progress and user is cached via login, not userid.

	// bottleneck synchronized (this) {
	if (activity == null) {
	    try {
		progressEngine.setUpStartPoint(progress);
	    } catch (ProgressException e) {
		log.error("error occurred in 'setUpStartPoint':" + e.getMessage(), e);
		throw new LearnerServiceException(e);
	    }

	} else {
	    // load the activity again so it is attached to the current Hibernate session
	    activity = getActivity(activity.getActivityId());
	    calculateProgress(activity, learnerId, progress);

	    updateGradebookMark(activity, progress);
	}
	// }
	logEventService.logEvent(LogEvent.TYPE_LEARNER_ACTIVITY_FINISH, learnerId, learnerId,
		progress.getLesson().getLessonId(), activity.getActivityId(),
		messageService.getMessage(ProgressEngine.AUDIT_ACTIVITY_STOP_KEY,
			new Object[] { progress.getUser().getLogin(), progress.getUser().getUserId(),
				activity.getTitle(), activity.getActivityId() }));
    }

    /**
     * If specified activity is set to produce ToolOutput, calculates and stores mark to gradebook.
     *
     * @param toolActivity
     * @param progress
     */
    private void updateGradebookMark(Activity activity, LearnerProgress progress) {
	User learner = progress.getUser();
	Lesson lesson = progress.getLesson();
	
	if ((learner == null) || (lesson == null) || (activity == null) || !(activity instanceof ToolActivity)
		|| (((ToolActivity) activity).getEvaluation() == null)) {
	    return;
	}
	ToolSession toolSession = lamsCoreToolService.getToolSessionByLearner(learner, activity);
	if (toolSession == null) {
	    return;
	}
	
	//in case this is a leader - update marks for all users in the group, otherwise update marks only for the specified user
	List<User> learnersRequiringMarkUpdate = new ArrayList<>();
	if (lamsCoreToolService.isUserLeaderInActivity(toolSession, learner)) {
	    learnersRequiringMarkUpdate.addAll(toolSession.getLearners());
	    
	} else {
	    learnersRequiringMarkUpdate.add(learner);
	}
	
	for (User learnerRequiringMarksUpdate : learnersRequiringMarkUpdate) {
	    gradebookService.updateGradebookUserActivityMark(lesson, activity, learnerRequiringMarksUpdate);
	}
    }

    @Override
    public Activity getActivity(Long activityId) {
	return activityDAO.getActivityByActivityId(activityId);
    }

    /**
     * @throws LearnerServiceException
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#performGrouping(java.lang.Long, java.lang.Long,
     *      java.lang.Integer)
     */
    @Override
    public boolean performGrouping(Long lessonId, Long groupingActivityId, Integer learnerId, boolean forceGrouping)
	    throws LearnerServiceException {
	GroupingActivity groupingActivity = (GroupingActivity) activityDAO.getActivityByActivityId(groupingActivityId,
		GroupingActivity.class);
	User learner = (User) userManagementService.findById(User.class, learnerId);

	boolean groupingDone = false;
	try {
	    if ((groupingActivity != null) && (groupingActivity.getCreateGrouping() != null) && (learner != null)) {
		Grouping grouping = groupingActivity.getCreateGrouping();

		// first check if the grouping already done for the user. If done, then skip the processing.
		groupingDone = grouping.doesLearnerExist(learner);

		if (!groupingDone) {
		    if (grouping.isRandomGrouping()) {
			// normal and preview cases for random grouping
			lessonService.performGrouping(lessonId, groupingActivity, learner);
			groupingDone = true;

		    } else if (forceGrouping) {
			// preview case for chosen grouping
			Lesson lesson = getLesson(lessonId);
			groupingDone = forceGrouping(lesson, grouping, null, learner);
		    }
		}

	    } else {
		String error = "Grouping activity " + groupingActivity + " learner " + learnerId
			+ " does not exist. Cannot perform grouping.";
		log.error(error);
		throw new LearnerServiceException(error);
	    }
	} catch (LessonServiceException e) {
	    throw new LearnerServiceException("performGrouping failed due to " + e.getMessage(), e);
	}
	return groupingDone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void learnerChooseGroup(Long lessonId, Long groupingActivityId, Long groupId, Integer learnerId)
	    throws LearnerServiceException {
	GroupingActivity groupingActivity = (GroupingActivity) activityDAO.getActivityByActivityId(groupingActivityId,
		GroupingActivity.class);

	if ((groupingActivity != null) && (groupId != null) && (learnerId != null)) {
	    Grouping grouping = groupingDAO.getGroupingById(groupingActivity.getCreateGrouping().getGroupingId());
	    if ((grouping != null) && grouping.isLearnerChoiceGrouping()) {

		User learner = (User) userManagementService.findById(User.class, learnerId);
		if (grouping.doesLearnerExist(learner)) {
		    return;
		}
		if (learner != null) {
		    Integer maxNumberOfLearnersPerGroup = null;
		    Set<Group> groups = grouping.getGroups();
		    if (((LearnerChoiceGrouping) grouping).getLearnersPerGroup() == null) {
			if (((LearnerChoiceGrouping) grouping).getEqualNumberOfLearnersPerGroup()) {
			    if (((LearnerChoiceGrouping) grouping).getLearnersPerGroup() == null) {
				Lesson lesson = getLesson(lessonId);
				int learnerCount = lesson.getAllLearners().size();
				int groupCount = grouping.getGroups().size();
				maxNumberOfLearnersPerGroup = (learnerCount / groupCount)
					+ ((learnerCount % groupCount) == 0 ? 0 : 1);
			    }
			}
		    } else {
			maxNumberOfLearnersPerGroup = ((LearnerChoiceGrouping) grouping).getLearnersPerGroup();
		    }
		    if (maxNumberOfLearnersPerGroup != null) {
			for (Group group : groups) {
			    if (group.getGroupId().equals(groupId)) {
				if (group.getUsers().size() >= maxNumberOfLearnersPerGroup) {
				    return;
				}
			    }
			}
		    }

		    lessonService.performGrouping(grouping, groupId, learner);
		}
	    }
	}
    }

    private boolean forceGrouping(Lesson lesson, Grouping grouping, Group group, User learner) {
	boolean groupingDone = false;
	if (lesson.isPreviewLesson()) {
	    ArrayList<User> learnerList = new ArrayList<>();
	    learnerList.add(learner);
	    if (group != null) {
		if (group.getGroupId() != null) {
		    lessonService.performGrouping(grouping, group.getGroupId(), learnerList);
		} else {
		    lessonService.performGrouping(grouping, group.getGroupName(), learnerList);
		}
	    } else {
		if (grouping.getGroups().size() > 0) {
		    // if any group exists, put them in there.
		    Group aGroup = grouping.getGroups().iterator().next();
		    if (aGroup.getGroupId() != null) {
			lessonService.performGrouping(grouping, aGroup.getGroupId(), learnerList);
		    } else {
			lessonService.performGrouping(grouping, aGroup.getGroupName(), learnerList);
		    }
		} else {
		    // just create a group and stick the user in there!
		    lessonService.performGrouping(grouping, (String) null, learnerList);
		}
	    }
	    groupingDone = true;
	}
	return groupingDone;
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#knockGate(java.lang.Long,
     *      org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public GateActivityDTO knockGate(Long gateActivityId, User knocker, boolean forceGate) {
	GateActivity gate = (GateActivity) activityDAO.getActivityByActivityId(gateActivityId, GateActivity.class);
	if (gate != null) {
	    return knockGate(gate, knocker, forceGate);
	}

	String error = "Gate activity " + gateActivityId + " does not exist. Cannot knock on gate.";
	log.error(error);
	throw new LearnerServiceException(error);
    }

    /**
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#knockGate(org.lamsfoundation.lams.learningdesign.GateActivity,
     *      org.lamsfoundation.lams.usermanagement.User)
     */
    @Override
    public GateActivityDTO knockGate(GateActivity gate, User knocker, boolean forceGate) {
	boolean gateOpen = false;

	if (forceGate) {
	    Lesson lesson = getLessonByActivity(gate);
	    if (lesson.isPreviewLesson()) {
		// special case for preview - if forceGate is true then brute force open the gate
		gate.setGateOpen(true);
		gate.setGateOpenUser(knocker);
		gate.setGateOpenTime(new Date());
		gateOpen = true;
	    }
	}

	Integer expectedLearnerCount = null;
	Integer waitingLearnerCount = null;

	if (!gateOpen) {
	    waitingLearnerCount = learnerProgressDAO.getNumUsersAttemptedActivity(gate);

	    expectedLearnerCount = 0;
	    Set<Group> learnerGroups = getGroupsForGate(gate);
	    for (Group learnerGroup : learnerGroups) {
		expectedLearnerCount += learnerGroup.getUsers().size();
	    }

	    // normal case - knock the gate.
	    gateOpen = gate.shouldOpenGateFor(knocker, expectedLearnerCount, waitingLearnerCount);
	    if (!gateOpen) {
		// only for a condition gate
		gateOpen = determineConditionGateStatus(gate, knocker);
	    }
	}

	// update gate including updating the waiting list and gate status in
	// the database.
	activityDAO.update(gate);
	return new GateActivityDTO(gate, expectedLearnerCount, waitingLearnerCount, gateOpen);
    }

    /**
     * Get all the groups of learners who may come through this gate. For a Group Based branch and the Teacher Grouped
     * branch, it is the group of users in the Branch's group. Otherwise we just get all learners in the lesson.
     */
    @Override
    public Set<Group> getGroupsForGate(GateActivity gate) {
	Lesson lesson = getLessonByActivity(gate);
	Set<Group> result = new HashSet<>();

	Activity branchActivity = gate.getParentBranch();
	while ((branchActivity != null) && !(branchActivity.getParentActivity().isChosenBranchingActivity()
		|| branchActivity.getParentActivity().isGroupBranchingActivity())) {
	    branchActivity = branchActivity.getParentBranch();
	}

	if (branchActivity != null) {
	    // set up list based on branch - all members of a group attached to the branch are destined for the gate
	    SequenceActivity branchSequence = (SequenceActivity) activityDAO
		    .getActivityByActivityId(branchActivity.getActivityId(), SequenceActivity.class);
	    for (BranchActivityEntry branchActivityEntry : branchSequence.getBranchEntries()) {
		Group group = branchActivityEntry.getGroup();
		if (group != null) {
		    result.add(group);
		}
	    }
	} else {
	    result.add(lesson.getLessonClass().getLearnersGroup());
	}

	return result;
    }

    /**
     * Get the lesson for this activity. If the activity is not part of a lesson (ie is from an authoring design then it
     * will return null.
     */
    @Override
    public Lesson getLessonByActivity(Activity activity) {
	Lesson lesson = lessonDAO.getLessonForActivity(activity.getActivityId());
	if (lesson == null) {
	    log.warn(
		    "Tried to get lesson id for a non-lesson based activity. An error is likely to be thrown soon. Activity was "
			    + activity);
	}
	return lesson;
    }

    // ---------------------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------------------

    /**
     * Create an array of lesson dto based a list of lessons.
     *
     * @param lessons
     *            the list of lessons.
     * @return the lesson dto array.
     */
    private LessonDTO[] getLessonDataFor(List lessons) {
	List<LessonDTO> lessonDTOList = new ArrayList<>();
	for (Iterator i = lessons.iterator(); i.hasNext();) {
	    Lesson currentLesson = (Lesson) i.next();
	    lessonDTOList.add(new LessonDTO(currentLesson));
	}
	return lessonDTOList.toArray(new LessonDTO[lessonDTOList.size()]);
    }

    /**
     * @throws LearnerServiceException
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#determineBranch(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.learningdesign.BranchingActivity, java.lang.Integer)
     */
    @Override
    public SequenceActivity determineBranch(Lesson lesson, BranchingActivity branchingActivity, Integer learnerId)
	    throws LearnerServiceException {
	User learner = (User) userManagementService.findById(User.class, learnerId);
	if (learner == null) {
	    String error = "determineBranch: learner " + learnerId + " does not exist. Cannot determine branch.";
	    log.error(error);
	    throw new LearnerServiceException(error);
	}

	try {
	    if (branchingActivity.isToolBranchingActivity()) {
		return determineToolBasedBranch(lesson, (ToolBranchingActivity) branchingActivity, learner);

	    } else {
		// assume either isGroupBranchingActivity() || isChosenBranchingActivity() )
		// in both cases, the branch is based on the group the learner is in.
		return determineGroupBasedBranch(lesson, branchingActivity, learner);

	    }
	} catch (LessonServiceException e) {
	    String message = "determineBranch failed due to " + e.getMessage();
	    log.error(message, e);
	    throw new LearnerServiceException("determineBranch failed due to " + e.getMessage(), e);
	}
    }

    /**
     * Get all the conditions for this branching activity, ordered by order id. Go through each condition until we find
     * one that passes and that is the required branch. If no conditions match, use the branch that is the "default"
     * branch for this branching activity.
     */
    private SequenceActivity determineToolBasedBranch(Lesson lesson, ToolBranchingActivity branchingActivity,
	    User learner) {
	Activity defaultBranch = branchingActivity.getDefaultActivity();
	SequenceActivity matchedBranch = null;

	// Work out the tool session appropriate for this user and branching activity. We expect there to be only one at
	// this point.
	ToolSession toolSession = null;
	for (Activity inputActivity : branchingActivity.getInputActivities()) {
	    toolSession = lamsCoreToolService.getToolSessionByLearner(learner, inputActivity);
	}

	if (toolSession != null) {

	    // Get all the conditions for this branching activity, ordered by order id.
	    Map<BranchCondition, SequenceActivity> conditionsMap = new TreeMap<>();
	    Iterator branchIterator = branchingActivity.getActivities().iterator();
	    while (branchIterator.hasNext()) {
		Activity branchActivity = (Activity) branchIterator.next();
		SequenceActivity branchSequence = (SequenceActivity) activityDAO
			.getActivityByActivityId(branchActivity.getActivityId(), SequenceActivity.class);
		Iterator<BranchActivityEntry> entryIterator = branchSequence.getBranchEntries().iterator();
		while (entryIterator.hasNext()) {
		    BranchActivityEntry entry = entryIterator.next();
		    if (entry.getCondition() != null) {
			conditionsMap.put(entry.getCondition(), branchSequence);
		    }
		}
	    }

	    // Go through each condition until we find one that passes and that is the required branch.
	    // Cache the tool output so that we aren't calling it over an over again.
	    Map<String, ToolOutput> toolOutputMap = new HashMap<>();
	    Iterator<BranchCondition> conditionIterator = conditionsMap.keySet().iterator();

	    while ((matchedBranch == null) && conditionIterator.hasNext()) {
		BranchCondition condition = conditionIterator.next();
		String conditionName = condition.getName();
		ToolOutput toolOutput = toolOutputMap.get(conditionName);
		if (toolOutput == null) {
		    toolOutput = lamsCoreToolService.getOutputFromTool(conditionName, toolSession, learner.getUserId());
		    if (toolOutput == null) {
			log.warn("Condition " + condition + " refers to a tool output " + conditionName
				+ " but tool doesn't return any tool output for that name. Skipping this condition.");
		    } else {
			toolOutputMap.put(conditionName, toolOutput);
		    }
		}

		if ((toolOutput != null) && condition.isMet(toolOutput)) {
		    matchedBranch = conditionsMap.get(condition);
		}
	    }
	}

	// If no conditions match, use the branch that is the "default" branch for this branching activity.
	if (matchedBranch != null) {
	    if (log.isDebugEnabled()) {
		log.debug("Found branch " + matchedBranch.getActivityId() + ":"
			+ matchedBranch.getTitle() + " for branching activity " + branchingActivity.getActivityId()
			+ ":" + branchingActivity.getTitle() + " for learner " + learner.getUserId() + ":"
			+ learner.getLogin());
	    }
	    return matchedBranch;

	} else if (defaultBranch != null) {
	    if (log.isDebugEnabled()) {
		log.debug("Using default branch " + defaultBranch.getActivityId() + ":"
			+ defaultBranch.getTitle() + " for branching activity " + branchingActivity.getActivityId()
			+ ":" + branchingActivity.getTitle() + " for learner " + learner.getUserId() + ":"
			+ learner.getLogin());
	    }
	    // have to convert it to a real activity of the correct type, as it could be a cglib value
	    return (SequenceActivity) activityDAO.getActivityByActivityId(defaultBranch.getActivityId(),
		    SequenceActivity.class);
	} else {
	    if (log.isDebugEnabled()) {
		log.debug(
			"No branches match and no default branch exists. Uable to allocate learner to a branch for the branching activity"
				+ branchingActivity.getActivityId() + ":" + branchingActivity.getTitle()
				+ " for learner " + learner.getUserId() + ":" + learner.getLogin());
	    }
	    return null;
	}
    }

    private SequenceActivity determineGroupBasedBranch(Lesson lesson, BranchingActivity branchingActivity,
	    User learner) {
	SequenceActivity sequenceActivity = null;

	if (branchingActivity.getGrouping() != null) {
	    Grouping grouping = branchingActivity.getGrouping();

	    // If the user is in a group, then check if the group is assigned to a sequence activity. If it
	    // is then we are done and we return the sequence
	    Group group = grouping.getGroupBy(learner);
	    if (group != null) {
		if (group.getBranchActivities() != null) {
		    Iterator branchesIterator = group.getBranchActivities().iterator();
		    while ((sequenceActivity == null) && branchesIterator.hasNext()) {
			BranchActivityEntry branchActivityEntry = (BranchActivityEntry) branchesIterator.next();
			if (branchActivityEntry.getBranchingActivity().equals(branchingActivity)) {
			    sequenceActivity = branchActivityEntry.getBranchSequenceActivity();
			}
		    }
		}
	    }

	    if (sequenceActivity != null) {
		if (log.isDebugEnabled()) {
		    log.debug("Found branch " + sequenceActivity.getActivityId() + ":"
			    + sequenceActivity.getTitle() + " for branching activity "
			    + branchingActivity.getActivityId() + ":" + branchingActivity.getTitle() + " for learner "
			    + learner.getUserId() + ":" + learner.getLogin());
		}
	    }

	}

	return sequenceActivity;
    }

    /**
     * Checks if any of the conditions that open the gate is met.
     *
     * @param gate
     *            gate to check
     * @param learner
     *            learner who is knocking to the gate
     * @return <code>true</code> if learner satisfied any of the conditions and is allowed to pass
     */
    private boolean determineConditionGateStatus(GateActivity gate, User learner) {
	boolean shouldOpenGate = false;
	if (gate instanceof ConditionGateActivity) {
	    ConditionGateActivity conditionGate = (ConditionGateActivity) gate;

	    // Work out the tool session appropriate for this user and gate activity. We expect there to be only one at
	    // this point.
	    ToolSession toolSession = null;
	    for (Activity inputActivity : conditionGate.getInputActivities()) {
		toolSession = lamsCoreToolService.getToolSessionByLearner(learner, inputActivity);
	    }

	    if (toolSession != null) {

		// Go through each condition until we find one that passes and that opens the gate.
		// Cache the tool output so that we aren't calling it over an over again.
		Map<String, ToolOutput> toolOutputMap = new HashMap<>();
		for (BranchActivityEntry entry : conditionGate.getBranchActivityEntries()) {
		    BranchCondition condition = entry.getCondition();
		    String conditionName = condition.getName();
		    ToolOutput toolOutput = toolOutputMap.get(conditionName);
		    if (toolOutput == null) {
			toolOutput = lamsCoreToolService.getOutputFromTool(conditionName, toolSession,
				learner.getUserId());
			if (toolOutput == null) {
			    log.warn("Condition " + condition + " refers to a tool output "
				    + conditionName
				    + " but tool doesn't return any tool output for that name. Skipping this condition.");
			} else {
			    toolOutputMap.put(conditionName, toolOutput);
			}
		    }

		    if ((toolOutput != null) && condition.isMet(toolOutput)) {
			shouldOpenGate = entry.getGateOpenWhenConditionMet();
			if (shouldOpenGate) {
			    // save the learner to the "allowed to pass" list so we don't check the conditions over and
			    // over
			    // again (maybe we should??)
			    conditionGate.getAllowedToPassLearners().add(learner);
			}
			break;
		    }
		}
	    }
	}
	return shouldOpenGate;
    }

    /**
     * Select a particular branch - we are in preview mode and the author has selected a particular activity.
     *
     * @throws LearnerServiceException
     * @see org.lamsfoundation.lams.learning.service.ICoreLearnerService#determineBranch(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.learningdesign.BranchingActivity, java.lang.Integer)
     */
    @Override
    public SequenceActivity selectBranch(Lesson lesson, BranchingActivity branchingActivity, Integer learnerId,
	    Long branchId) throws LearnerServiceException {

	User learner = (User) userManagementService.findById(User.class, learnerId);
	if (learner == null) {
	    String error = "selectBranch: learner " + learnerId + " does not exist. Cannot determine branch.";
	    log.error(error);
	    throw new LearnerServiceException(error);
	}

	SequenceActivity selectedBranch = (SequenceActivity) activityDAO.getActivityByActivityId(branchId,
		SequenceActivity.class);
	if (selectedBranch != null) {

	    if ((selectedBranch.getParentActivity() == null)
		    || !selectedBranch.getParentActivity().equals(branchingActivity)) {
		String error = "selectBranch: activity " + selectedBranch
			+ " is not a branch within the branching activity " + branchingActivity + ". Unable to branch.";
		log.error(error);
		throw new LearnerServiceException(error);
	    }

	    Set<Group> groups = selectedBranch.getGroupsForBranch();
	    Grouping grouping = branchingActivity.getGrouping();

	    // Does this matching branch have any groups? If so, see if the learner is in
	    // the appropriate group and add them if necessary.
	    if ((groups != null) && (groups.size() > 0)) {
		boolean isInGroup = false;
		Group aGroup = null;
		Iterator<Group> groupIter = groups.iterator();
		while (!isInGroup && groupIter.hasNext()) {
		    aGroup = groupIter.next();
		    isInGroup = aGroup.hasLearner(learner);
		}

		// If the learner is not in the appropriate group, then force the learner in the
		// last group we checked. this will only work if the user is in preview.
		if (!isInGroup) {
		    if (!forceGrouping(lesson, grouping, aGroup, learner)) {
			String error = "selectBranch: learner " + learnerId + " cannot be added to the group " + aGroup
				+ " for the branch " + selectedBranch + " for the lesson " + lesson.getLessonName()
				+ " preview is " + lesson.isPreviewLesson()
				+ ". This will only work if preview is true.";
			log.error(error);
			throw new LearnerServiceException(error);
		    }
		}

		// if no matching groups exist (just to Define in Monitor), then create one and assign it to the branch.
		// if it is a chosen grouping, make sure we allow it to go over the normal number of groups (the real
		// groups will exist
		// but it too hard to reuse them.)
	    } else {
		if (grouping.isChosenGrouping() && (grouping.getMaxNumberOfGroups() != null)) {
		    grouping.setMaxNumberOfGroups(null);
		}

		Group group = lessonService.createGroup(grouping, selectedBranch.getTitle());
		group.allocateBranchToGroup(null, selectedBranch, branchingActivity);
		if (!forceGrouping(lesson, grouping, group, learner)) {
		    String error = "selectBranch: learner " + learnerId + " cannot be added to the group " + group
			    + " for the branch " + selectedBranch + " for the lesson " + lesson.getLessonName()
			    + " preview is " + lesson.isPreviewLesson() + ". This will only work if preview is true.";
		    log.error(error);
		    throw new LearnerServiceException(error);
		}
	    }
	    groupingDAO.update(grouping);

	    if (log.isDebugEnabled()) {
		log.debug("Found branch " + selectedBranch.getActivityId() + ":"
			+ selectedBranch.getTitle() + " for branching activity " + branchingActivity.getActivityId()
			+ ":" + branchingActivity.getTitle() + " for learner " + learner.getUserId() + ":"
			+ learner.getLogin());
	    }

	    return selectedBranch;

	} else {
	    String error = "selectBranch: Unable to find branch for branch id " + branchId;
	    log.error(error);
	    throw new LearnerServiceException(error);
	}

    }

    public ProgressEngine getProgressEngine() {
	return progressEngine;
    }

    public void setProgressEngine(ProgressEngine progressEngine) {
	this.progressEngine = progressEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer calculateMaxNumberOfLearnersPerGroup(Long lessonId, Long groupingId) {
	LearnerChoiceGrouping grouping = (LearnerChoiceGrouping) getGrouping(groupingId);
	LearnerChoiceGrouping learnerChoiceGrouping = grouping;
	Integer maxNumberOfLearnersPerGroup = null;
	int learnerCount = lessonService.getCountLessonLearners(lessonId, null);
	int groupCount = grouping.getGroups().size();
	if (learnerChoiceGrouping.getLearnersPerGroup() == null) {
	    if (groupCount == 0) {
		((LearnerChoiceGrouper) grouping.getGrouper()).createGroups(learnerChoiceGrouping, 2);
		groupCount = grouping.getGroups().size();
		groupingDAO.update(grouping);
	    }
	    if (learnerChoiceGrouping.getEqualNumberOfLearnersPerGroup()) {
		maxNumberOfLearnersPerGroup = (learnerCount / groupCount) + ((learnerCount % groupCount) == 0 ? 0 : 1);
	    }
	} else {
	    maxNumberOfLearnersPerGroup = learnerChoiceGrouping.getLearnersPerGroup();
	    int desiredGroupCount = (learnerCount / maxNumberOfLearnersPerGroup)
		    + ((learnerCount % maxNumberOfLearnersPerGroup) == 0 ? 0 : 1);
	    if (desiredGroupCount > groupCount) {
		((LearnerChoiceGrouper) grouping.getGrouper()).createGroups(learnerChoiceGrouping,
			desiredGroupCount - groupCount);
		groupingDAO.update(grouping);
	    }
	}
	return maxNumberOfLearnersPerGroup;
    }

    @Override
    public Grouping getGrouping(Long groupingId) {
	return groupingDAO.getGroupingById(groupingId);
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public ICommandDAO getCommandDAO() {
	return commandDAO;
    }

    public void setCommandDAO(ICommandDAO commandDAO) {
	this.commandDAO = commandDAO;
    }

    /**
     * Finds activity position within Learning Design.
     */
    @Override
    public ActivityPositionDTO getActivityPosition(Long activityId) {
	if (activityId == null) {
	    return null;
	}
	Activity activity = getActivity(activityId);
	if (activity == null) {
	    return null;
	}

	ActivityPositionDTO result = new ActivityPositionDTO();
	// this is not really used at the moment, but can be useful in the future
	result.setActivityCount(activity.getLearningDesign().getActivities().size());
	Activity parentActivity = activity.getParentActivity();
	boolean isFirst = false;
	boolean isLast = false;

	if (parentActivity == null) {
	    // it's an activity in the main sequence
	    isFirst = activity.getTransitionTo() == null;
	    isLast = isActivityLast(activity);
	} else {
	    if (parentActivity.isSequenceActivity()) {
		// only parent's parent is the one in main sequence
		parentActivity = parentActivity.getParentActivity();
	    }

	    ActivityPositionDTO parentPosition = getActivityPosition(parentActivity.getActivityId());
	    if (parentPosition != null) {
		// looking for first-ness is easy
		isFirst = parentPosition.getFirst() && (activity.getTransitionTo() == null);

		// looking for last-ness
		if (parentActivity.isOptionsActivity()) {
		    if (parentPosition.getLast()) {
			// this is tricky: the activity is the last one only if parent is and after completing it,
			// there are no more optional activities to do
			// (for example, it's 4th out of 5 optional activities)
			OptionsActivity parentOptionsActivity = (OptionsActivity) getActivity(
				parentActivity.getActivityId());
			Integer learnerId = LearningWebUtil.getUserId();
			Lesson lesson = getLessonByActivity(activity);
			LearnerProgress learnerProgress = getProgress(learnerId, lesson.getLessonId());

			if (learnerProgress != null) {
			    int completedSubactivities = 0;
			    for (Activity subactivity : parentOptionsActivity.getActivities()) {
				if (LearnerProgress.ACTIVITY_COMPLETED == learnerProgress
					.getProgressState(subactivity)) {
				    completedSubactivities++;
				}
			    }

			    isLast = completedSubactivities == (parentOptionsActivity.getMaxNumberOfOptionsNotNull()
				    - 1);
			}
		    }
		} else if (parentActivity.isBranchingActivity() || parentActivity.isParallelActivity()) {
		    isLast = parentPosition.getLast() && isActivityLast(activity);
		}
	    }
	}

	result.setFirst(isFirst);
	result.setLast(isLast);
	return result;
    }

    @Override
    public void createCommandForLearner(Long lessonId, String userName, String jsonCommand) {
	Command command = new Command(lessonId, userName, jsonCommand);
	commandDAO.insert(command);
    }

    /**
     * Used by <code>CommandWebsocketServer</code>.
     */
    @Override
    public void createCommandForLearners(Long toolContentId, Collection<Integer> userIds, String jsonCommand) {
	// find lesson for given tool content ID
	ToolActivity activity = activityDAO.getToolActivityByToolContentId(toolContentId);
	LearningDesign learningDesign = activity.getLearningDesign();
	Lesson lesson = learningDesign.getLessons().iterator().next();
	Long lessonId = lesson.getLessonId();

	// go through each user, find his user name and add a command for him
	for (Integer userId : userIds) {
	    User user = (User) activityDAO.find(User.class, userId);
	    Command command = new Command(lessonId, user.getLogin(), jsonCommand);
	    commandDAO.insert(command);
	}
    }

    @Override
    public List<Command> getCommandsForLesson(Long lessonId, Date laterThan) {
	return commandDAO.getNewCommands(lessonId, laterThan);
    }

    @Override
    public ActivityPositionDTO getActivityPositionByToolSessionId(Long toolSessionId) {
	ToolSession toolSession = lamsCoreToolService.getToolSessionById(toolSessionId);
	return toolSession == null ? null : getActivityPosition(toolSession.getToolActivity().getActivityId());
    }

    private boolean isActivityLast(Activity activity) {
	Transition transition = activity.getTransitionFrom();
	while (transition != null) {
	    Activity nextActivity = transition.getToActivity();
	    if (!nextActivity.isGateActivity()) {
		return false;
	    }
	    transition = nextActivity.getTransitionFrom();
	}
	return true;
    }

    /* Added for RepopulateProgressMarksServlet - can be removed later */
    private static final String TOOL_SIGNATURE_ASSESSMENT = "laasse10";
    private static final String TOOL_SIGNATURE_SCRATCHIE = "lascrt11";
    private static final String TOOL_SIGNATURE_MCQ = "lamc11";

    @Override
    public String[] recalcProgressForLearner(Lesson lesson, ArrayList<Activity> activityList,
	    LearnerProgress learnerProgress, boolean updateGradebookForAll) {

	StringBuilder auditLogBuilder = new StringBuilder("");
	StringBuilder errorBuilder = new StringBuilder("");

	User learner = learnerProgress.getUser();
	Date lessonStartDate = learnerProgress.getStartDate();
	auditLogBuilder.append("\n\nUpdating ").append(learner.getLogin()).append(" ").append(learner.getFullName())
		.append("\n");

	boolean updated = false;
	for (Activity activity : activityList) {

	    CompletedActivityProgress completedProgress = learnerProgress.getCompletedActivities().get(activity);
	    if (completedProgress == null || completedProgress.getStartDate() == null
		    || completedProgress.getFinishDate() == null) {
		updated = updateProgress(lesson.getLessonId(), auditLogBuilder, errorBuilder, learnerProgress, learner,
			lessonStartDate, activity) || updated;
	    }

	    // is completed (previously or just now?), in which case update gradebook.
	    if (activity.isToolActivity()) {
		CompletedActivityProgress updatedCompletedProgress = learnerProgress.getCompletedActivities()
			.get(activity);
		if (updatedCompletedProgress != null) {
		    ToolActivity toolActivity = (ToolActivity) activity;
		    Tool tool = toolActivity.getTool();
		    if (updateGradebookForAll || TOOL_SIGNATURE_ASSESSMENT.equals(tool.getToolSignature())
			    || TOOL_SIGNATURE_SCRATCHIE.equals(tool.getToolSignature())
			    || TOOL_SIGNATURE_MCQ.equals(tool.getToolSignature())) {
			auditLogBuilder.append("Pushing mark to Gradebook for activity ")
				.append(activity.getActivityId()).append(" ").append(activity.getTitle()).append("\n");
			gradebookService.updateGradebookUserActivityMark(lesson, activity, learner);
		    }
		}
	    }

	}

	if (updated) {
	    learnerProgressDAO.updateLearnerProgress(learnerProgress);
	}

	return new String[] { auditLogBuilder.toString(), errorBuilder.toString() };
    }

    private boolean updateProgress(Long lessonId, StringBuilder auditLogBuilder, StringBuilder errorBuilder,
	    LearnerProgress learnerProgress, User learner, Date lessonStartDate, Activity activity) {

	boolean updated = false;
	ToolCompletionStatus results = recalcActivityProgress(activity, learner, lessonId, learnerProgress,
		auditLogBuilder, errorBuilder);

	// results ==  null ignore - won't harm anything if it is in attempted and nothing in the tool and
	// do not remove from completed in case it was force completed, or the tool doesn't support this

	if (results != null) {

	    if (results.getStatus() == ToolCompletionStatus.ACTIVITY_COMPLETED) {
		// completed
		Date startedDateFromAttempted = learnerProgress.getAttemptedActivities().get(activity);
		CompletedActivityProgress cap = learnerProgress.getCompletedActivities().get(activity);
		if (cap != null) {
		    if (cap.getStartDate() == null) {
			if (startedDateFromAttempted != null) {
			    cap.setStartDate(startedDateFromAttempted);
			} else if (results.getStartDate() != null) {
			    cap.setStartDate(results.getStartDate());
			}
		    }
		    if (cap.getFinishDate() == null && results.getFinishDate() != null) {
			cap.setFinishDate(results.getFinishDate());
		    }
		} else {
		    cap = new CompletedActivityProgress(
			    startedDateFromAttempted != null ? startedDateFromAttempted : results.getStartDate(),
			    results.getFinishDate());
		}
		if (cap.getStartDate() == null) {
		    // must have something or it is not seen as completed
		    cap.setStartDate(lessonStartDate);
		}
		learnerProgress.getCompletedActivities().put(activity, cap);
		learnerProgress.getAttemptedActivities().remove(activity);

		auditLogBuilder.append("Progress updated for completed activity ").append(activity.getActivityId())
			.append(" ").append(activity.getTitle()).append("\n");
		updated = true;

	    } else if (results.getStatus() == ToolCompletionStatus.ACTIVITY_ATTEMPTED) {
		// Attempted - if not already there add with tool's start date, or failing that the tool's value for
		// session start date, or the core's value for session start date, or the lesson start date.
		// Must have a date or it can't be saved.
		if (results.getStartDate() != null) {
		    learnerProgress.getAttemptedActivities().putIfAbsent(activity, results.getStartDate());
		} else {
		    learnerProgress.getAttemptedActivities().putIfAbsent(activity, lessonStartDate);
		}
		auditLogBuilder.append("Progress updated for attempted activity ").append(activity.getActivityId())
			.append(" ").append(activity.getTitle()).append("\n");
		updated = true;
	    }

	}

	return updated;
    }

    private ToolCompletionStatus recalcActivityProgress(Activity activity, User learner, Long lessonId,
	    LearnerProgress learnerProgress, StringBuilder auditLogEntry, StringBuilder errorBuilder) {

	ToolCompletionStatus status = null;

	if (activity.isToolActivity()) {
	    ToolSession toolSession = lamsCoreToolService.getToolSessionByLearner(learner, activity);
	    if (toolSession != null) {
		status = lamsCoreToolService.getCompletionStatusFromTool(learner, activity);
		if (status.getStartDate() == null) {
		    status.setStartDate(toolSession.getCreateDateTime());
		}
	    }

	} else if (activity.isComplexActivity()) {
	    ComplexActivity complexActivity = (ComplexActivity) activity;
	    boolean attempted = false;
	    boolean allComplete = true;
	    Date caStartDate = null;
	    Date caEndDate = null;
	    for (Iterator i = complexActivity.getActivities().iterator(); i.hasNext();) {
		Activity childActivity = (Activity) i.next();
		Date childStartDate = learnerProgress.getAttemptedActivities().get(childActivity);
		Date childEndDate = null;
		if (childStartDate != null) {
		    attempted = true;
		}
		CompletedActivityProgress childCap = learnerProgress.getCompletedActivities().get(childActivity);
		if (childCap == null) {
		    allComplete = false;
		} else {
		    attempted = true;
		    if (childStartDate == null) {
			childStartDate = childCap.getStartDate();
		    }
		    childEndDate = childCap.getFinishDate();
		}
		if (caStartDate == null || (childStartDate != null && childStartDate.before(caStartDate))) {
		    caStartDate = childStartDate;
		}
		if (caEndDate == null || (childEndDate != null && childEndDate.after(caStartDate))) {
		    caEndDate = childEndDate;
		}
	    }

	    if (attempted) {
		if (allComplete) {
		    status = new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, caStartDate, caEndDate);
		} else {
		    status = new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, caStartDate, null);
		}
	    }

	} else if (activity.isGateActivity()) {
	    // do nothing
	    ;
	} else if (activity.isGroupingActivity()) {
	    GroupingActivity groupingActivity = (GroupingActivity) activity;
	    Grouping grouping = groupingActivity.getCreateGrouping();
	    if (grouping.doesLearnerExist(learner)) {
		status = new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, null, null);
	    }
	} else {
	    errorBuilder.append("Unable to update status for unexpected activity ").append(activity.getActivityId())
		    .append(" ").append(activity.getTitle());
	}
	return status;

    }

    @Override
    public boolean isKumaliveDisabledForOrganisation(Integer organisationId) {
	Kumalive kumalive = kumaliveService.getKumaliveByOrganisation(organisationId);
	return kumalive == null || kumalive.getFinished();
    }

    @Override
    public boolean triggerCommandCheckAndSend() {
	return CommandWebsocketServer.triggerCheckAndSend();
    }

    @Override
    public IActivityDAO getActivityDAO() {
	return activityDAO;
    }

}