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

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.action.ActivityAction;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Jacky Fang
 * @since 2005-3-10
 * @version
 *
 */
public class LearningWebUtil {

    private static Logger log = Logger.getLogger(LearningWebUtil.class);
    // ---------------------------------------------------------------------
    // Class level constants - session attributes
    // ---------------------------------------------------------------------
    public static final String PARAM_PROGRESS_ID = "progressID";

    // public static final String POPUP_WINDOW_NAME = "LearnerActivity";
    // public static final String LEARNER_WINDOW_NAME = "lWindow";

    /**
     * Helper method to retrieve the user data. Gets the id from the user details in the shared session
     *
     * @return the user id
     */
    public static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null ? learner.getUserID() : null;
    }

    /**
     * Helper method to retrieve the user data. Gets the id from the user details in the shared session then retrieves
     * the real user object.
     */
    public static User getUser(ICoreLearnerService learnerService) {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null
		? (User) learnerService.getUserManagementService().findById(User.class, learner.getUserID()) : null;
    }

    /**
     * Put the learner progress in the request. This allows some optimisation between the code that updates the progress
     * and the next action which will access the progress.
     */
    public static void putLearnerProgressInRequest(HttpServletRequest request, LearnerProgress progress) {
	if (progress != null) {
	    request.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE, progress);
	} else {
	    request.removeAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
	}
    }

    /**
     * Get the current learner progress. Check the request - in some cases it may be there.
     *
     * If not, the learner progress id might be in the request (if we've just come from complete activity). If so, get
     * it from the db using the learner progress.
     *
     * If the learner progress id isn't available, then we have to look it up using activity based on the activity /
     * activity id in the request.
     */
    public static LearnerProgress getLearnerProgress(HttpServletRequest request, ICoreLearnerService learnerService) {
	LearnerProgress learnerProgress = (LearnerProgress) request
		.getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
	if (learnerProgress != null) {
	    if (LearningWebUtil.log.isDebugEnabled()) {
		LearningWebUtil.log.debug("getLearnerProgress: found progress in the request");
	    }
	    return learnerProgress;
	}

	if (learnerProgress == null) {
	    Long learnerProgressId = WebUtil.readLongParam(request, LearningWebUtil.PARAM_PROGRESS_ID, true);
	    // temp hack until UI side updates it call.
	    if (learnerProgressId == null) {
		learnerProgressId = WebUtil.readLongParam(request, "progressId", true);
	    }

	    if (learnerProgressId != null) {
		learnerProgress = learnerService.getProgressById(new Long(learnerProgressId));
		if ((learnerProgress != null) && LearningWebUtil.log.isDebugEnabled()) {
		    LearningWebUtil.log.debug("getLearnerProgress: found progress via progress id");
		}
	    }

	}

	if (learnerProgress == null) {
	    Integer learnerId = LearningWebUtil.getUserId();
	    Activity act = LearningWebUtil.getActivityFromRequest(request, learnerService);
	    Lesson lesson = learnerService.getLessonByActivity(act);
	    learnerProgress = learnerService.getProgress(learnerId, lesson.getLessonId());
	    if ((learnerProgress != null) && LearningWebUtil.log.isDebugEnabled()) {
		LearningWebUtil.log.debug("getLearnerProgress: found progress via learner id and activity");
	    }
	}

	LearningWebUtil.putLearnerProgressInRequest(request, learnerProgress);
	return learnerProgress;
    }

    /**
     * Get the activity from request. We assume there is a parameter coming in if there is no activity can be found in
     * the http request. Then the activity id parameter is used to retrieve from database.
     *
     * @param request
     * @return
     */
    public static Activity getActivityFromRequest(HttpServletRequest request, ICoreLearnerService learnerService) {
	Activity activity = (Activity) request.getAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE);

	if (activity == null) {
	    long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	    activity = learnerService.getActivity(new Long(activityId));

	    if (activity != null) {
		// getActivityFromRequest() may be called multiple times, so make it quicker next time
		request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, activity);
	    }
	}
	return activity;
    }

    /**
     * Put an activity into the request. Calls LearnerService to get the activity, to ensure that it is a "real"
     * activity, not one of the cglib proxies. activity.
     *
     * @param request
     * @param activity
     */
    public static void putActivityInRequest(HttpServletRequest request, Activity activity,
	    ICoreLearnerService learnerService) {
	if (activity != null) {
	    Activity realActivity = learnerService.getActivity(activity.getActivityId());
	    request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, realActivity);
	} else {
	    request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, null);
	}
    }

    /**
     * "Complete" an activity from the web layer's perspective. Used for CompleteActivityAction and the Gate and
     * Grouping actions. Calls the learningService to actually complete the activity and progress.
     *
     * @param redirect
     *            Should this call redirect to the next screen (true) or use a forward (false)
     * @param windowName
     *            Name of the window that triggered this code. Normally LearnerActivity (the popup window) or lWindow
     *            (normal learner window)
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     *
     */
    public static ActionForward completeActivity(HttpServletRequest request, HttpServletResponse response,
	    ActivityMapping actionMappings, LearnerProgress progress, Activity currentActivity, Integer learnerId,
	    ICoreLearnerService learnerService, boolean redirect)
	    throws LearnerServiceException, UnsupportedEncodingException {

	Lesson lesson = progress.getLesson();

	if (currentActivity == null) {
	    progress = learnerService.joinLesson(learnerId, lesson.getLessonId());
	} else if (progress.getCompletedActivities().containsKey(currentActivity)) {

	    // recalculate activity mark and pass it to gradebook
	    learnerService.updateGradebookMark(currentActivity, progress);

	    return actionMappings.getCloseForward(currentActivity, lesson.getLessonId());
	} else {
	    learnerService.completeActivity(learnerId, currentActivity, progress);
	}

	if (currentActivity != null && (currentActivity.isFloating() || (currentActivity.getParentActivity() != null
		&& progress.getCompletedActivities().containsKey(currentActivity.getParentActivity())))) {
	    return actionMappings.getCloseForward(currentActivity, lesson.getLessonId());
	}

	LearningWebUtil.putActivityInRequest(request, progress.getNextActivity(), learnerService);
	LearningWebUtil.putLearnerProgressInRequest(request, progress);
	return actionMappings.getProgressForward(progress, redirect, false, request, learnerService);
    }

    /**
     * Get the ActionMappings.
     */
    public static ActivityMapping getActivityMapping(ServletContext context) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
	return (ActivityMapping) wac.getBean("activityMapping");
    }

    public static ActivityURL getActivityURL(ActivityMapping activityMapping, LearnerProgress learnerProgress,
	    Activity activity, boolean defaultURL, boolean isFloating) {
	ActivityURL activityURL = new ActivityURL();
	activityURL.setType(activity.getClass().getSimpleName());

	if ( isFloating && activity.isFloatingActivity() ) {
	    // special case - progress engine. Do not want the unknown activity warning
	    activityURL.setUrl(null);
	} else {
	    activityURL.setUrl(activityMapping.getActivityURL(activity));
	}

	activityURL.setActivityId(activity.getActivityId());
	activityURL.setTitle(activity.getTitle());
	activityURL.setDescription(activity.getDescription());

	byte status = learnerProgress.getProgressState(activity);
	activityURL.setStatus(status);
	if (status == LearnerProgress.ACTIVITY_COMPLETED) {
	    activityURL.setComplete(true);
	}
	activityURL.setFloating(isFloating);
	activityURL.setDefaultURL(defaultURL);
	return activityURL;
    }

    /**
     * Finds activity position within Learning Design and stores it as request attribute.
     */
    public static ActivityPositionDTO putActivityPositionInRequest(Long activityId, HttpServletRequest request,
	    ServletContext context) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
	ILearnerService learnerService = (ILearnerService) wac.getBean("learnerService");
	if (learnerService == null) {
	    LearningWebUtil.log.warn("Can not set activity position, no Learner service in servlet context.");
	    return null;
	}
	ActivityPositionDTO positionDTO = learnerService.getActivityPosition(activityId);
	if (positionDTO != null) {
	    request.setAttribute(AttributeNames.ATTR_ACTIVITY_POSITION, positionDTO);
	}
	return positionDTO;
    }

    /**
     * Finds activity position within Learning Design and stores it as request attribute.
     */
    public static ActivityPositionDTO putActivityPositionInRequestByToolSessionId(Long toolSessionId,
	    HttpServletRequest request, ServletContext context) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
	ILearnerService learnerService = (ILearnerService) wac.getBean("learnerService");
	if (learnerService == null) {
	    LearningWebUtil.log.warn("Can not set activity position, no Learner service in servlet context.");
	    return null;
	}
	ActivityPositionDTO positionDTO = learnerService.getActivityPositionByToolSessionId(toolSessionId);
	if (positionDTO != null) {
	    request.setAttribute(AttributeNames.ATTR_ACTIVITY_POSITION, positionDTO);
	}
	return positionDTO;
    }
}