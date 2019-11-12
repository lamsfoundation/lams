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

package org.lamsfoundation.lams.learning.web.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.controller.DisplayActivityController;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * This class contains the standard struts action mappings for errors as well as methods that get required Action/URL to
 * display an Activity or LearnerProgress.
 *
 * In order to return a URL this class needs to know the baseURL. This can be set using in the application context.
 *
 * @author daveg
 */
public class ActivityMapping implements Serializable {
    private static final long serialVersionUID = 5887602834473598770L;

    /* These are global struts forwards. */
    public static final String ERROR = "error";
    public static final String NO_SESSION_ERROR = "noSessionError";
    public static final String NO_ACCESS_ERROR = "noAccessError";
    public static final String DOUBLE_SUBMIT_ERROR = "doubleSubmitError";
    public static final String LEARNING = "learning";

    private ActivityMappingStrategy activityMappingStrategy = new ActivityMappingStrategy();

    private ILamsCoreToolService toolService;

    /**
     * Creates a Struts ActionForward to display an activity.
     *
     * @param activity,
     *            the Activity to be displayed
     * @param progress,
     *            the LearnerProgress associated with the Activity and learner
     */
    public String getActivityForward(Activity activity, LearnerProgress progress, boolean redirect) {
	String action = this.activityMappingStrategy.getActivityAction(activity);
	action = WebUtil.appendParameterToURL(action, AttributeNames.PARAM_LEARNER_PROGRESS_ID,
		progress.getLearnerProgressId().toString());
	if (activity != null) {
	    action = WebUtil.appendParameterToURL(action, AttributeNames.PARAM_ACTIVITY_ID,
		    activity.getActivityId().toString());
	}

	return ActivityMapping.actionToForward(action, activity, redirect);
    }

    /**
     * Creates a Struts ActionForward to display a next activity. If the previous activity was a ParallelActivity then
     * the frames will be cleared.
     *
     * @param progress,
     *            the LearnerProgress associated with the Activity and learner
     * @param redirect,
     *            If true a RedirectActionForward is used
     * @param displayParallelFrames,
     *            if true then try to display the parallel activity frames rather than the internal tool screen. This is
     *            only set to true for DisplayActivityAction, which is called when the user joins/resumes the lesson.
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getProgressForward(LearnerProgress progress, boolean redirect, boolean displayParallelFrames)
	    throws UnsupportedEncodingException {
	String forward = null;

	if (progress.isComplete()) {
	    // If lesson complete forward to lesson complete action. This action will
	    // cause a client request to clear ALL frames. Need to append the progress
	    // id as getting to the end from an activity can't have the progress in the request
	    // and there isn't an activity from which we can determine the lesson and hence
	    // the progress.
	    String action = this.getActivityMappingStrategy().getLessonCompleteAction();
	    action = WebUtil.appendParameterToURL(action, AttributeNames.PARAM_LEARNER_PROGRESS_ID,
		    progress.getLearnerProgressId().toString());
	    action = ActivityMapping.actionToURL(action, null, true);
	    forward = ActivityMapping.getClearFramesForward(action, progress.getLearnerProgressId().toString());
	} else {
	    if (!displayParallelFrames && (progress.getParallelWaiting() == LearnerProgress.PARALLEL_WAITING)) {
		// processing the screen WITHIN parallel activity frames.
		// progress is waiting, goto waiting page
		String action = this.getActivityMappingStrategy().getWaitingAction();
		forward = ActivityMapping.actionToForward(action, null, redirect);
	    } else {
		// display next activity
		if (progress.getParallelWaiting() == LearnerProgress.PARALLEL_WAITING_COMPLETE) {
		    // if previous activity was a parallel activity then we need to
		    // clear frames.
		    String activityURL = this.getActivityURL(progress.getNextActivity());
		    forward = ActivityMapping.getClearFramesForward(activityURL,
			    progress.getLearnerProgressId().toString());
		} else {
		    forward = getActivityForward(progress.getNextActivity(), progress, redirect);
		}
	    }
	}
	return forward;
    }

    /**
     * Call the requestDisplay.do action to break out of any frames. Doesn't need to redirect this forward, as the
     * requestDisplay.do does a redirect
     *
     * @param activityURL
     *            URL to which we will be redirected. Does not need to be encoded as this method will encode it.
     * @param progressId
     * @return actionForward to which to forward
     * @throws UnsupportedEncodingException
     */
    private static String getClearFramesForward(String activityURL, String progressId)
	    throws UnsupportedEncodingException {
	String encodedURL = URLEncoder.encode(activityURL, "UTF-8");
	String action = "/requestURL.jsp?url=" + encodedURL;
	action = WebUtil.appendParameterToURL(action, AttributeNames.PARAM_LEARNER_PROGRESS_ID, progressId);

	String forward = ActivityMapping.actionToForward(action, null, false);
	return forward;
    }

    /**
     * Generates an ActivityURL for an Activity using it's progress. The URL is for the client and so includes hostname
     * etc. Note that the URL could also be a wait message or a jsp to clear the frames.
     *
     * @param activity,
     *            the Activity to be displayed
     * @param progress,
     *            the LearnerProgress associated with the Activity and learner
     */
    public String getActivityURL(Activity activity) {
	String action = this.activityMappingStrategy.getActivityAction(activity);
	return ActivityMapping.actionToURL(action, activity, true);
    }

    /**
     * Generates an ActivityURL for a Tool Activity or SystemToolActivity. The URL is for the tool and not for the tool
     * loading page. The URL also includes toolSessionId or toolContentId and all other required data.
     *
     * @param activity,
     *            the Activity to be displayed
     * @param progress,
     *            the current LearnerProgress, used to get activity status
     */
    public String getLearnerToolURL(Lesson lesson, Activity activity, User learner) {
	try {
	    if (lesson.isPreviewLesson()) {
		return toolService.getToolLearnerPreviewURL(lesson.getLessonId(), activity, learner);
	    } else {
		return toolService.getToolLearnerURL(lesson.getLessonId(), activity, learner);
	    }
	} catch (LamsToolServiceException e) {
	    throw new LearnerServiceException(e.getMessage());
	}
    }

    /**
     * Creates a URL for a struts action and an activity.
     *
     * @param strutsAction,
     *            the struts action path.
     * @param activity,
     *            the activity the action is for.
     * @param useContext,
     *            if true prepends the server and context to the URL.
     */
    public static String actionToURL(String action, Activity activity, boolean useContext) {
	String url = action;

	if (activity != null && !url.contains(AttributeNames.PARAM_ACTIVITY_ID)) {
	    url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_ACTIVITY_ID,
		    activity.getActivityId().toString());
	}
	if (useContext) {
	    String lamsUrl = Configuration.get(ConfigurationKeys.SERVER_URL) + ActivityMapping.LEARNING;
	    url = lamsUrl + url;
	}

	return url;
    }

    /**
     * Creates a Struts ActionForward for an action and activity.
     *
     * @param strutsAction,
     *            the struts action
     * @param activity,
     *            activity that is being displayed
     * @param redirect,
     *            should the action be a client redirect
     * @return
     */
//    protected String actionToForward(String action, Activity activity, boolean redirect) {
//	return redirect ? "redirect:" + ActivityMapping.actionToURL(action, activity, false) : "forward:" + action;
//    }

    protected static String actionToForward(String action, Activity activity, boolean addParams) {
	return "redirect:" + (addParams ? ActivityMapping.actionToURL(action, activity, false) : action);
    }

    /**
     * Takes a Struts forward containing the path such as /DisplayRequest.do and turns it into a full URL including
     * servername
     */

    /**
     * Calculate the activity url for progress view at learner side.
     *
     * @param learner
     *            the learner who owns the progress data
     * @param activity
     *            the activity the learner want to view
     * @return the url for that tool.
     */
    public String calculateActivityURLForProgressView(Lesson lesson, User learner, Activity activity) {

	if (((activity != null) && activity.isToolActivity()) || activity.isSystemToolActivity()) {
	    return WebUtil.convertToFullURL(getLearnerToolURL(lesson, (activity), learner));
	} else {
	    // fall back to the strategy for complex activities
	    return getActivityURL(activity);
	}
    }

    public void setToolService(ILamsCoreToolService toolService) {
	this.toolService = toolService;
    }

    public ActivityMappingStrategy getActivityMappingStrategy() {
	return activityMappingStrategy;
    }

    public void setActivityMappingStrategy(ActivityMappingStrategy activityMappingStrategy) {
	this.activityMappingStrategy = activityMappingStrategy;
    }

    /**
     * If the activity is already completed it could be one of five cases: (1) the user has opened just the one activity
     * in in a popup window from the progress bar, in which case we want to close the window (2) the user has opened up
     * a parallel activity in a popup window from the progress bar, and the completed activity is one of the contained
     * activities. In this case we want to display the "wait" message or close depending on the activity in the other
     * frame. (3) the activity was force completed while the user was doing the activity. This case includes
     * "standalone" activities + activities in a parallel frameset (4) the activity is part of a parallel activity and
     * the other activity isn't completed. In cases (3) and (4) we want to do whatever we would normally do, apart from
     * completing the activity, but we know whether it is (1),(2) or (3),(4) until we get back to a jsp and can check
     * the window name. so prepare the urls for (2), (3) and (4) then call the close window screen and it will sort it
     * out.
     */
    public String getCloseForward(Activity justCompletedActivity, Long lessonId) throws UnsupportedEncodingException {

	String closeWindowURLAction = activityMappingStrategy.getCloseWindowAction();

	// Always calculate the url for the "normal" next case as we won't know till we reach the close window if we need it.

	String action = getDisplayActivityAction(lessonId);
	action = ActivityMapping.actionToURL(action, null, true);
	action = WebUtil.appendParameterToURL(action, DisplayActivityController.PARAM_INITIAL_DISPLAY, "false");
	action = URLEncoder.encode(action, "UTF-8");

	if (!justCompletedActivity.isFloating()) {
	    closeWindowURLAction = WebUtil.appendParameterToURL(closeWindowURLAction, "nextURL", action);
	}

	// If we are in the parallel frameset then we might need the nextURL, or we might need the "waiting" url.
	if ((justCompletedActivity.getParentActivity() != null)
		&& justCompletedActivity.getParentActivity().isParallelActivity()) {
	    action = getActivityMappingStrategy().getWaitingAction();
	    action = ActivityMapping.actionToURL(action, null, true);
	    action = URLEncoder.encode(action, "UTF-8");
	    closeWindowURLAction = WebUtil.appendParameterToURL(closeWindowURLAction, "waitURL", action);
	}

	return ActivityMapping.actionToForward(closeWindowURLAction, null, false);
    }

    public String getProgressBrokenURL() {
	return ActivityMapping.actionToURL(activityMappingStrategy.getProgressBrokenAction(), null, true);
    }

    public String getCompleteActivityURL(Long activityId, Long progressId) {
	String url = ActivityMapping.actionToURL(activityMappingStrategy.getCompleteActivityAction(), null, true);
	if (activityId != null) {
	    url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_ACTIVITY_ID, activityId.toString());
	}
	if (progressId != null) {
	    url = WebUtil.appendParameterToURL(url, AttributeNames.PARAM_LEARNER_PROGRESS_ID, progressId.toString());
	}
	return url;
    }

    /**
     * Get the "bootstrap" activity action. This is the action called when the user first joins a lesson, and sets up
     * the necessary request details based on the user's progress details. If lessonID is not null then it is appended
     * onto the string. If this is for a JSP call then the lessonID is needed.
     */
    public String getDisplayActivityAction(Long lessonID) {
	if (lessonID != null) {
	    return WebUtil.appendParameterToURL("/DisplayActivity.do", AttributeNames.PARAM_LESSON_ID,
		    lessonID.toString());
	} else {
	    return "/DisplayActivity.do";
	}
    }

}