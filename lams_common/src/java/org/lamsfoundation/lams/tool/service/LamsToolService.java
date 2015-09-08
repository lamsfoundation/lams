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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;

/**
 * 
 * @author Jacky Fang
 * @since 2005-3-17
 * 
 * @author Ozgur Demirtas 24/06/2005
 * 
 */
public class LamsToolService implements ILamsToolService {
    private static Logger log = Logger.getLogger(LamsToolService.class);

    // Leader selection tool Constants
    private static final String LEADER_SELECTION_TOOL_SIGNATURE = "lalead11";
    private static final String LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID = "leader.user.id";

    public IToolDAO toolDAO;
    public IToolSessionDAO toolSessionDAO;
    public IToolContentDAO toolContentDAO;
    private ILamsCoreToolService lamsCoreToolService;
    private ILessonService lessonService;

    @Override
    public Set<User> getAllPotentialLearners(long toolSessionId) throws LamsToolServiceException {

	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	if (session != null) {
	    return session.getLearners();
	} else {
	    log.error("No tool session found for " + toolSessionId + ". No potential learners being returned.");
	    return new HashSet<User>();
	}
    }

    @Override
    public IToolVO getToolByID(Long toolId) {
	Tool tool = toolDAO.getToolByID(toolId);
	return tool.createBasicToolVO();
    }

    @Override
    public IToolVO getToolBySignature(final String toolSignature) {
	Tool tool = toolDAO.getToolBySignature(toolSignature);
	return tool.createBasicToolVO();
    }

    @Override
    public Tool getPersistToolBySignature(final String toolSignature) {
	return toolDAO.getToolBySignature(toolSignature);
    }

    @Override
    public long getToolDefaultContentIdBySignature(final String toolSignature) {
	return toolDAO.getToolDefaultContentIdBySignature(toolSignature);
    }

    @Override
    public String generateUniqueContentFolder() throws FileUtilException, IOException {

	return FileUtil.generateUniqueContentFolderID();
    }

    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {

	ToolSession toolSession = this.getToolSession(toolSessionId);
	Long lessonId = toolSession.getLesson().getLessonId();
	String learnerContentFolder = FileUtil.getLearnerContentFolder(lessonId, userId);

	return learnerContentFolder;
    }

    @Override
    public void saveOrUpdateTool(Tool tool) {
	toolDAO.saveOrUpdateTool(tool);
    }

    /**
     * Get the tool session object using the toolSessionId
     * 
     * @param toolSessionId
     * @return
     */
    @Override
    public ToolSession getToolSession(Long toolSessionId) {
	return toolSessionDAO.getToolSession(toolSessionId);
    }

    @Override
    public Boolean isGroupedActivity(long toolContentID) {
	List<Activity> activities = toolContentDAO.findByProperty(Activity.class, "toolContentId", toolContentID);
	if (activities.size() == 1) {
	    Activity activity = activities.get(0);
	    return activity.getApplyGrouping();
	} else {
	    log.debug("ToolContent contains multiple activities, can't test whether grouping applies.");
	    return null;
	}
    }

    @Override
    public Long getLeaderUserId(Long toolSessionId, Integer learnerId) {
	Long leaderUserId = null;

	ToolSession toolSession = this.getToolSession(toolSessionId);
	ToolActivity specifiedActivity = toolSession.getToolActivity();
	Activity leaderSelectionActivity = getNearestLeaderSelectionActivity(specifiedActivity, learnerId,
		toolSession.getLesson().getLessonId());

	// check if there is leaderSelectionTool available
	if (leaderSelectionActivity != null) {
	    User learner = (User) toolContentDAO.find(User.class, learnerId);
	    String outputName = LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID;
	    ToolSession leaderSelectionSession = toolSessionDAO.getToolSessionByLearner(learner,
		    leaderSelectionActivity);
	    if (leaderSelectionSession != null) {
		ToolOutput output = lamsCoreToolService.getOutputFromTool(outputName, leaderSelectionSession, null);

		// check if tool produced output
		if (output != null && output.getValue() != null) {
		    leaderUserId = output.getValue().getLong();
		}
	    }
	}

	return leaderUserId;
    }

    /**
     * Finds the nearest Leader Select activity. Works recursively. Tries to find Leader Select activity in the previous
     * activities set first, and then inside the parent set.
     */
    private Activity getNearestLeaderSelectionActivity(Activity activity, Integer userId, Long lessonId) {

	// check if current activity is Leader Select one. if so - stop searching and return it.
	Class activityClass = Hibernate.getClass(activity);
	if (activityClass.equals(ToolActivity.class)) {
	    ToolActivity toolActivity;

	    // activity is loaded as proxy due to lazy loading and in order to prevent quering DB we just re-initialize
	    // it here again
	    Hibernate.initialize(activity);
	    if (activity instanceof HibernateProxy) {
		toolActivity = (ToolActivity) ((HibernateProxy) activity).getHibernateLazyInitializer()
			.getImplementation();
	    } else {
		toolActivity = (ToolActivity) activity;
	    }

	    if (LEADER_SELECTION_TOOL_SIGNATURE.equals(toolActivity.getTool().getToolSignature())) {
		return activity;
	    }

	    // in case of floating activity
	} else if (activityClass.equals(FloatingActivity.class)) {
	    LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(userId, lessonId);
	    Map<Activity, CompletedActivityProgress> completedActivities = learnerProgress.getCompletedActivities();

	    // find the earliest finished Leader Select Activity
	    Date leaderSelectActivityFinishDate = null;
	    Activity leaderSelectionActivity = null;
	    for (Activity completedActivity : completedActivities.keySet()) {

		if (completedActivity instanceof ToolActivity) {
		    ToolActivity completedToolActivity = (ToolActivity) completedActivity;
		    if (LEADER_SELECTION_TOOL_SIGNATURE.equals(completedToolActivity.getTool().getToolSignature())) {
			Date finishDate = completedActivities.get(completedActivity).getFinishDate();

			if ((leaderSelectActivityFinishDate == null)
				|| (finishDate.compareTo(leaderSelectActivityFinishDate) < 0)) {
			    leaderSelectionActivity = completedToolActivity;
			    leaderSelectActivityFinishDate = completedActivities.get(completedActivity).getFinishDate();
			}

		    }
		}

	    }

	    return leaderSelectionActivity;
	}

	// check previous activity
	Transition transitionTo = activity.getTransitionTo();
	if (transitionTo != null) {
	    Activity fromActivity = transitionTo.getFromActivity();
	    return getNearestLeaderSelectionActivity(fromActivity, userId, lessonId);
	}

	// check parent activity
	Activity parent = activity.getParentActivity();
	if (parent != null) {
	    return getNearestLeaderSelectionActivity(parent, userId, lessonId);
	}

	return null;
    }

    @Override
    public Set<User> getUsersForActivity(Long toolSessionId) {
	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	return session != null ? session.getLearners() : new HashSet<User>();
    }

    @Override
    public Integer getCountUsersForActivity(Long toolSessionId) {

	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	if ( session.getToolSessionTypeId() == ToolSession.GROUPED_TYPE ) {
	    return toolSessionDAO.getCountUsersGrouped(toolSessionId);
	} else {
	    // expect it to be 0 or 1 
	    return session.getLearners().size();
	}
    }

    /**
     * @return Returns the toolDAO.
     */
    public IToolDAO getToolDAO() {
	return toolDAO;
    }

    /**
     * @param toolDAO
     *            The toolDAO to set.
     */
    public void setToolDAO(IToolDAO toolDAO) {
	this.toolDAO = toolDAO;
    }

    public IToolSessionDAO getToolSessionDAO() {
	return toolSessionDAO;
    }

    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO) {
	this.toolSessionDAO = toolSessionDAO;
    }

    /**
     * 
     * @return
     */
    public IToolContentDAO getToolContentDAO() {
	return toolContentDAO;
    }

    /**
     * 
     * @param toolContentDAO
     */
    public void setToolContentDAO(IToolContentDAO toolContentDAO) {
	this.toolContentDAO = toolContentDAO;
    }

    /**
     * 
     * @param toolContentDAO
     */
    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }
}
