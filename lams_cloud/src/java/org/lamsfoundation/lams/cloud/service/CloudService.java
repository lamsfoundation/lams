/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */ 
package org.lamsfoundation.lams.cloud.service;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dao.IGradebookDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;

/**
 * This service handles all gradebook-related service calls
 *
 * @author Andrey Balan
 */
public class CloudService implements ICloudService {

    private static Logger logger = Logger.getLogger(CloudService.class);

    // Services
    private ILamsCoreToolService toolService;
    private IGradebookDAO gradebookDAO;
    private ILessonService lessonService;
    private IUserManagementService userService;
    private IBaseDAO baseDAO;
    private IActivityDAO activityDAO;
    private MessageService messageService;



    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#getGradebookUserLesson(java.lang.Long,
     *      java.lang.Integer)
     */
    public GradebookUserLesson getGradebookUserLesson(Long lessonID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForLesson(lessonID, userID);
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#getGradebookUserActivity(java.lang.Long,
     *      java.lang.Integer)
     */
    public GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID) {
	return gradebookDAO.getGradebookUserDataForActivity(activityID, userID);
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#getAverageMarkForActivity(java.lang.Long)
     */
    public Double getAverageMarkForActivity(Long activityID) {
	return gradebookDAO.getAverageMarkForActivity(activityID);
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#getAverageMarkForLesson(java.lang.Long)
     */
    public Double getAverageMarkForLesson(Long lessonID) {
	return gradebookDAO.getAverageMarkForLesson(lessonID);
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#updateUserLessonGradebookMark(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User, java.lang.Double)
     */
    public void updateUserLessonGradebookMark(Lesson lesson, User learner, Double mark) {
	if (lesson != null && learner != null) {
	    GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradebookUserLesson == null) {
		gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	    }
	    gradebookUserLesson.setMark(mark);
	    gradebookDAO.insertOrUpdate(gradebookUserLesson);
	}
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#updateUserActivityGradebookMark(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User,
     *      org.lamsfoundation.lams.learningdesign.Activity, java.lang.Double)
     */
    public void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook) {
	if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {

	    // First, update the mark for the activity
	    GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(activity
		    .getActivityId(), learner.getUserId());

	    if (gradebookUserActivity == null) {
		gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	    }

	    gradebookUserActivity.setMark(mark);
	    gradebookUserActivity.setMarkedInGradebook(markedInGradebook);
	    gradebookDAO.insertOrUpdate(gradebookUserActivity);

	    // Now update the lesson mark
	    GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		    learner.getUserId());

	    if (gradebookUserLesson == null) {
		gradebookUserLesson = new GradebookUserLesson();
		gradebookUserLesson.setLearner(learner);
		gradebookUserLesson.setLesson(lesson);
	    }

	    aggregateTotalMarkForLesson(gradebookUserLesson);
	}
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#updateUserLessonGradebookFeedback(org.lamsfoundation.lams.lesson.Lesson,
     *      org.lamsfoundation.lams.usermanagement.User, java.lang.String)
     */
    public void updateUserLessonGradebookFeedback(Lesson lesson, User learner, String feedback) {

	GradebookUserLesson gradebookUserLesson = gradebookDAO.getGradebookUserDataForLesson(lesson.getLessonId(),
		learner.getUserId());

	if (gradebookUserLesson == null) {
	    gradebookUserLesson = new GradebookUserLesson(lesson, learner);
	}

	gradebookUserLesson.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#updateUserActivityGradebookFeedback(org.lamsfoundation.lams.learningdesign.Activity,
     *      org.lamsfoundation.lams.usermanagement.User, java.lang.String)
     */
    public void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback) {

	GradebookUserActivity gradebookUserActivity = gradebookDAO.getGradebookUserDataForActivity(activity
		.getActivityId(), learner.getUserId());

	if (gradebookUserActivity == null) {
	    gradebookUserActivity = new GradebookUserActivity((ToolActivity) activity, learner);
	}

	gradebookUserActivity.setFeedback(feedback);
	gradebookDAO.insertOrUpdate(gradebookUserActivity);
    }


    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#updateActivityMark(java.lang.Double,
     *      java.lang.String, java.lang.Integer, java.lang.Long)
     */
    public void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionID);
	User learner = (User) userService.findById(User.class, userID);
	if (learner != null && toolSession != null) {
	    ToolActivity activity = toolSession.getToolActivity();
	    GradebookUserActivity gradebookUserActivity = getGradebookUserActivity(activity.getActivityId(), userID);

	    // If gradebook user activity is null, save the mark and feedback
	    if (gradebookUserActivity == null || !gradebookUserActivity.getMarkedInGradebook()) {
		updateUserActivityGradebookMark(toolSession.getLesson(), learner, activity, mark, markedInGradebook);
	    }

	}
    }

    /**
     * @see org.lamsfoundation.lams.cloud.service.ICloudService#getActivityById(java.lang.Long)
     */
    public Activity getActivityById(Long activityID) {
	return activityDAO.getActivityByActivityId(activityID);
    }

    /**
     * Gets the internationalised date
     * 
     * @param user
     * @param date
     * @return
     */
    private String getLocaleDateString(User user, Date date) {
	if (user == null || date == null) {
	    return null;
	}

	Locale locale = new Locale(user.getLocale().getLanguageIsoCode(), user.getLocale().getCountryIsoCode());
	String dateStr = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale).format(date);
	return dateStr;
    }

    /**
     * Adds a mark to the aggregated total and saves it
     * 
     * @param gradebookUserLesson
     */
    private void aggregateTotalMarkForLesson(GradebookUserLesson gradebookUserLesson) {
	Double totalMark = gradebookDAO.getGradebookUserActivityMarkSum(gradebookUserLesson.getLesson().getLessonId(),
		gradebookUserLesson.getLearner().getUserId());
	gradebookUserLesson.setMark(totalMark);
	gradebookDAO.insertOrUpdate(gradebookUserLesson);
    }


    private Long getActivityDuration(LearnerProgress learnerProgress, Activity activity) {
	if (learnerProgress != null) {
	    if (learnerProgress.getCompletedActivities().get(activity) != null) {
		CompletedActivityProgress compProg = learnerProgress.getCompletedActivities().get(activity);
		if (compProg != null) {
		    Date startTime = compProg.getStartDate();
		    Date endTime = compProg.getFinishDate();
		    if (startTime != null && endTime != null) {
			return endTime.getTime() - startTime.getTime();
		    }
		}
	    }
	}
	return null;
    }

    /**
     * Returns the lesson status string which is a reference to an image
     * 
     * @param learnerProgress
     * @return
     */
    private String getLessonStatusStr(LearnerProgress learnerProgress) {
	String status = "-";

	final String IMAGES_DIR = Configuration.get(ConfigurationKeys.SERVER_URL) + "images";
	if (learnerProgress != null) {
	    if (learnerProgress.isComplete()) {
		status = "<img src='" + IMAGES_DIR + "/tick.png' />";
	    } else if (learnerProgress.getAttemptedActivities() != null
		    && learnerProgress.getAttemptedActivities().size() > 0) {
		status = "<img src='" + IMAGES_DIR + "/cog.png' />";
	    }
	}
	return status;
    }

    /**
     * Returns the activity status string which is a reference to an image
     * 
     * @param learnerProgress
     * @param activity
     * @return
     */
    private String getActivityStatusStr(LearnerProgress learnerProgress, Activity activity) {

	final String IMAGES_DIR = Configuration.get(ConfigurationKeys.SERVER_URL) + "images";
	if (learnerProgress != null) {
	    byte statusByte = learnerProgress.getProgressState(activity);
	    if (statusByte == LearnerProgress.ACTIVITY_ATTEMPTED) {
		return "<img src='" + IMAGES_DIR + "/cog.png' />";
	    } else if (statusByte == LearnerProgress.ACTIVITY_COMPLETED) {
		return "<img src='" + IMAGES_DIR + "/tick.png' />";
	    }
	}
	return "-";
    }

    /**
     * Gets the outputs for a tool activity and returns the html for the ouputs
     * cell in the grid
     * 
     * @param toolAct
     * @param toolSession
     * @param learner
     * @return
     */
    private String getToolOutputsStr(ToolActivity toolAct, ToolSession toolSession, User learner) {
	String toolOutputsStr = "";
	boolean noOutputs = true;

	if (toolAct != null && toolSession != null && learner != null) {

	    SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(toolAct
		    .getToolContentId(), ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	    Set<ToolOutput> toolOutputs = new HashSet<ToolOutput>();

	    if (map.keySet().size() > 0) {

		for (String outputName : map.keySet()) {

		    try {
			ToolOutput toolOutput = toolService.getOutputFromTool(outputName, toolSession, learner
				.getUserId());

			if (toolOutput != null && toolOutput.getValue().getType() != OutputType.OUTPUT_COMPLEX) {
			    toolOutputs.add(toolOutput);

			    toolOutputsStr += toolOutput.getDescription() + ": " + toolOutput.getValue().getString();
			    toolOutputsStr += "<br />";

			    noOutputs = false;
			}

		    } catch (RuntimeException e) {
			CloudService.logger.debug("Runtime exception when attempted to get outputs for activity: "
				+ toolAct.getActivityId() + ", continuing for other activities", e);
		    }
		}
		// toolOutputsStr += "</ul>";
	    }
	}

	// Fix up outputs html if there are not outputs available
	if (noOutputs) {
	    toolOutputsStr = "-";
	}

	return toolOutputsStr;
    }

    /**
     * Gets the
     * 
     * @param activity
     * @return
     */
    private Long getTotalMarksAvailable(ToolActivity activity) {
	SortedMap<String, ToolOutputDefinition> map = toolService.getOutputDefinitionsFromTool(activity
		.getToolContentId(), ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	Set<ActivityEvaluation> actEvals = activity.getActivityEvaluations();

	if (map != null) {
	    for (String key : map.keySet()) {
		ToolOutputDefinition definition = map.get(key);
		if (actEvals != null && actEvals.size() > 0) {

		    // get first evaluation
		    ActivityEvaluation actEval = actEvals.iterator().next();

		    if (actEval.getToolOutputDefinition().equals(key)) {

			Object upperLimit = definition.getEndValue();
			if (upperLimit != null && upperLimit instanceof Long) {
			    return (Long) upperLimit;
			}
			break;
		    }
		} else {
		    if (definition.isDefaultGradebookMark() != null && definition.isDefaultGradebookMark()) {
			Object upperLimit = definition.getEndValue();
			if (upperLimit != null && upperLimit instanceof Long) {
			    return (Long) upperLimit;
			}
			break;
		    }
		}
	    }
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    private LearnerProgress getLearnerProgress(Lesson lesson, User user) {
	if (lesson != null && user != null) {
	    Map<String, Object> properties = new HashMap<String, Object>();
	    properties.put("lesson", lesson);
	    properties.put("user", user);
	    List learnerProgressList = baseDAO.findByProperties(LearnerProgress.class, properties);

	    if (learnerProgressList != null && learnerProgressList.size() > 0) {
		return (LearnerProgress) learnerProgressList.get(0);
	    } else {
		return null;
	    }
	} else {
	    return null;
	}
    }

    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    // Getter and setter methods -----------------------------------------------

    public ILamsCoreToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsCoreToolService toolService) {
	this.toolService = toolService;
    }

    public IGradebookDAO getGradebookDAO() {
	return gradebookDAO;
    }

    public void setGradebookDAO(IGradebookDAO gradebookDAO) {
	this.gradebookDAO = gradebookDAO;
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public IUserManagementService getUserService() {
	return userService;
    }

    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
    }

    public IBaseDAO getBaseDAO() {
	return baseDAO;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public IActivityDAO getActivityDAO() {
	return activityDAO;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    // -------------------------------------------------------------------------
}
