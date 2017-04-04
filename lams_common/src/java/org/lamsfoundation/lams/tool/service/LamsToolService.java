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

package org.lamsfoundation.lams.tool.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.FloatingActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.service.ILessonService;
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
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * @author Jacky Fang
 * @since 2005-3-17
 *
 * @author Ozgur Demirtas 24/06/2005
 */
public class LamsToolService implements ILamsToolService {
    private static Logger log = Logger.getLogger(LamsToolService.class);

    // Leader selection tool Constants
    private static final String LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID = "leader.user.id";

    private IActivityDAO activityDAO;
    private IAuditService auditService;
    private IToolDAO toolDAO;
    private IToolSessionDAO toolSessionDAO;
    private IToolContentDAO toolContentDAO;
    private IGradebookService gradebookService;
    private ILamsCoreToolService lamsCoreToolService;
    private ILessonService lessonService;

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

    @SuppressWarnings("unchecked")
    @Override
    public Boolean isGroupedActivity(long toolContentID) {
	ToolActivity toolActivity = activityDAO.getToolActivityByToolContentId(toolContentID);
	boolean isGroupedActivity = toolActivity == null ? null : toolActivity.getApplyGrouping();
	
	return isGroupedActivity;
    }
    
    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	auditService.logStartEditingActivityInMonitor(toolContentID);
    }    

    @Override
    public String getActivityEvaluation(Long toolContentId) {

	ToolActivity toolActivity = activityDAO.getToolActivityByToolContentId(toolContentId);
	Set<ActivityEvaluation> activityEvaluations = toolActivity.getActivityEvaluations();
	if (activityEvaluations.isEmpty()) {
	    return null;
	} else {
	    ActivityEvaluation activityEvaluation = activityEvaluations.iterator().next();
	    return activityEvaluation.getToolOutputDefinition();
	}
    }

    @Override
    public void setActivityEvaluation(Long toolContentId, String toolOutputDefinition) {

	if (StringUtils.isEmpty(toolOutputDefinition)) {
	    return;
	}

	ToolActivity toolActivity = activityDAO.getToolActivityByToolContentId(toolContentId);
	Set<ActivityEvaluation> activityEvaluations = toolActivity.getActivityEvaluations();

	// Get the first (only) ActivityEvaluation if it exists
	ActivityEvaluation activityEvaluation;
	boolean isToolOutputDefinitionChanged = true;
	if (activityEvaluations.isEmpty()) {
	    activityEvaluation = new ActivityEvaluation();
	    activityEvaluation.setActivity(toolActivity);

	    activityEvaluations = new HashSet<ActivityEvaluation>();
	    activityEvaluations.add(activityEvaluation);
	    toolActivity.setActivityEvaluations(activityEvaluations);

	} else {
	    activityEvaluation = activityEvaluations.iterator().next();
	    isToolOutputDefinitionChanged = !toolOutputDefinition.equals(activityEvaluation.getToolOutputDefinition());
	}

	activityEvaluation.setToolOutputDefinition(toolOutputDefinition);
	activityDAO.insertOrUpdate(activityEvaluation);

	// update the parent toolActivity
	toolActivity.setActivityEvaluations(activityEvaluations);
	activityDAO.insertOrUpdate(toolActivity);

	//update gradebook marks if required
	if (isToolOutputDefinitionChanged) {
	    gradebookService.recalculateGradebookMarksForActivity(toolActivity);
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
	    String outputName = LamsToolService.LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID;
	    ToolSession leaderSelectionSession = toolSessionDAO.getToolSessionByLearner(learner,
		    leaderSelectionActivity);
	    if (leaderSelectionSession != null) {
		ToolOutput output = lamsCoreToolService.getOutputFromTool(outputName, leaderSelectionSession, null);

		// check if tool produced output
		if ((output != null) && (output.getValue() != null)) {
		    leaderUserId = output.getValue().getLong();
		}
	    }
	}

	return leaderUserId;
    }
    
    @Override
    public Set<Long> getAllLeaderUserIds(Long toolSessionId, Integer learnerId) {
	Set<Long> leaderUserIds = null;

	ToolSession toolSession = this.getToolSession(toolSessionId);
	ToolActivity specifiedActivity = toolSession.getToolActivity();
	Activity leaderSelectionActivity = getNearestLeaderSelectionActivity(specifiedActivity, learnerId,
		toolSession.getLesson().getLessonId());
	
	// check if there is leaderSelectionTool available
	if (leaderSelectionActivity != null) {
	    leaderUserIds = getLeaderUserId(leaderSelectionActivity.getActivityId());
	}
	
	return leaderUserIds;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Long> getLeaderUserId(Long leaderSelectionActivityId) {
	Activity activity = activityDAO.getActivityByActivityId(leaderSelectionActivityId);
	List<ToolSession> toolSessions = toolSessionDAO.getToolSessionByActivity(activity);
	Set<Long> result = new TreeSet<Long>();
	for (ToolSession toolSession : toolSessions) {
	    ToolOutput output = lamsCoreToolService.getOutputFromTool(LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID,
		    toolSession, null);

	    // check if tool produced output
	    if ((output != null) && (output.getValue() != null)) {
		result.add(output.getValue().getLong());
	    }
	}

	return result;
    }

    /**
     * Finds the nearest Leader Select activity. Works recursively. Tries to find Leader Select activity in the previous
     * activities set first, and then inside the parent set.
     */
    @SuppressWarnings("rawtypes")
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

	    if (LamsToolService.LEADER_SELECTION_TOOL_SIGNATURE.equals(toolActivity.getTool().getToolSignature())) {
		return activity;
	    }

	    //in case of floating activity
	} else if (activityClass.equals(FloatingActivity.class)) {
	    LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(userId, lessonId);
	    Map<Activity, CompletedActivityProgress> completedActivities = learnerProgress.getCompletedActivities();

	    //find the earliest finished Leader Select Activity
	    Date leaderSelectActivityFinishDate = null;
	    Activity leaderSelectionActivity = null;
	    for (Activity completedActivity : completedActivities.keySet()) {

		if (completedActivity instanceof ToolActivity) {
		    ToolActivity completedToolActivity = (ToolActivity) completedActivity;
		    if (LamsToolService.LEADER_SELECTION_TOOL_SIGNATURE
			    .equals(completedToolActivity.getTool().getToolSignature())) {
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
    public Integer getCountUsersForActivity(Long toolSessionId) {
	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
	return session.getLearners().size();
    }

    /**
     * @param toolDAO
     *            The toolDAO to set.
     */
    public void setToolDAO(IToolDAO toolDAO) {
	this.toolDAO = toolDAO;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }
    
    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    public void setToolSessionDAO(IToolSessionDAO toolSessionDAO) {
	this.toolSessionDAO = toolSessionDAO;
    }

    /**
     *
     * @param toolContentDAO
     */
    public void setToolContentDAO(IToolContentDAO toolContentDAO) {
	this.toolContentDAO = toolContentDAO;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
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