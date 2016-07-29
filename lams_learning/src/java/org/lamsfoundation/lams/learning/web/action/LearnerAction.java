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

package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.presence.PresenceWebsocketServer;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.CompletedActivityProgressArchive;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LearnerProgressArchive;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 *
 * <p>
 * The action servlet that interacts with learner to start a lams learner module, join a user to the lesson and allows a
 * user to exit a lesson.
 * </p>
 *
 * <p>
 * It is also responsible for the interaction between lams server and UI. UI will call method implemented in this class
 * to get progress data or trigger a lams server calculation here
 * </p>
 *
 * <b>Note:</b>It needs to extend the <code>LamsDispatchAction</code> which has been customized to accomodate struts
 * features to solve duplicate submission problem.
 *
 * @author Jacky Fang
 * @since 3/03/2005
 * @version 1.1
 */
public class LearnerAction extends LamsDispatchAction {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(LearnerAction.class);

    private static JSONObject progressBarMessages = null;

    private static final String[] MONITOR_MESSAGE_KEYS = new String[] {
	    "label.learner.progress.activity.current.tooltip", "label.learner.progress.activity.completed.tooltip",
	    "label.learner.progress.activity.attempted.tooltip", "label.learner.progress.activity.tostart.tooltip",
	    "label.learner.progress.activity.support.tooltip"};

    private static final String[] LEARNER_MESSAGE_KEYS = new String[] {
	    "message.learner.progress.restart.confirm", "message.lesson.restart.button",
	    "label.learner.progress.notebook", "button.exit",
	    "label.learner.progress.support", "label.my.progress"};

    
    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------

    private ActionForward redirectToURL(ActionMapping mapping, HttpServletResponse response, String url)
	    throws IOException, ServletException {
	if (url != null) {
	    String fullURL = WebUtil.convertToFullURL(url);
	    response.sendRedirect(response.encodeRedirectURL(fullURL));
	} else {
	    throw new ServletException("Tried to redirect to url but url is null");
	}
	return null;
    }

    /**
     * <p>
     * The structs dispatch action that joins a learner into a lesson. This call is used for a user to start a lesson.
     * </p>
     *
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     *
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     *
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward joinLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// initialize service object
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	Integer learner = null;
	try {

	    // get user and lesson based on request.
	    learner = LearningWebUtil.getUserId();
	    long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    // security check
	    Lesson lesson = learnerService.getLesson(lessonID);
	    User user = (User) LearnerServiceProxy.getUserManagementService(getServlet().getServletContext())
		    .findById(User.class, learner);
	    if ((lesson.getLessonClass() == null) || !lesson.getLessonClass().getLearners().contains(user)) {
		request.setAttribute("messageKey",
			"User " + user.getLogin() + " is not a learner in the requested lesson.");
		return mapping.findForward("message");
	    }
	    // check lesson's state if its suitable for learner's access
	    if (!lesson.isLessonAccessibleForLearner()) {
		request.setAttribute("messageKey", "Lesson is inaccessible");
		return mapping.findForward("message");
	    }

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("The learner [" + learner + "] is joining the lesson [" + lessonID + "]");
	    }

	    // join user to the lesson on the server
	    LearnerProgress learnerProgress = learnerService.joinLesson(learner, lessonID);

	    if (LearnerAction.log.isDebugEnabled()) {
		LearnerAction.log.debug("The learner [" + learner + "] joined lesson. The" + "progress data is:"
			+ learnerProgress.toString());
	    }

	    LearningWebUtil.putLearnerProgressInRequest(request, learnerProgress);

	    ActivityMapping activityMapping = LearnerServiceProxy
		    .getActivityMapping(this.getServlet().getServletContext());
	    String url = "learning/" + activityMapping.getDisplayActivityAction(lessonID);

	    redirectToURL(mapping, response, url);

	} catch (Exception e) {
	    LearnerAction.log.error("An error occurred while learner " + learner + " attempting to join the lesson.",
		    e);
	    return mapping.findForward(ActivityMapping.ERROR);
	}

	return null;
    }

    /**
     * Archives current learner progress and moves a learner back to the start of lesson.
     */
    public ActionForward restartLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// fetch necessary parameters
	long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	User user = LearningWebUtil.getUser(learnerService);
	Integer userID = user.getUserId();

	// find number of previous attempts
	Integer attemptID = learnerService.getProgressArchiveMaxAttemptID(userID, lessonID);
	if (attemptID == null) {
	    attemptID = 0;
	}
	attemptID++;

	// make a copy of attempted and completed activities
	LearnerProgress learnerProgress = learnerService.getProgress(userID, lessonID);
	Map<Activity, Date> attemptedActivities = new HashMap<Activity, Date>(learnerProgress.getAttemptedActivities());
	Map<Activity, CompletedActivityProgressArchive> completedActivities = new HashMap<Activity, CompletedActivityProgressArchive>();
	for (Entry<Activity, CompletedActivityProgress> entry : learnerProgress.getCompletedActivities().entrySet()) {
	    CompletedActivityProgressArchive activityArchive = new CompletedActivityProgressArchive(learnerProgress,
		    entry.getKey(), entry.getValue().getStartDate(), entry.getValue().getFinishDate());
	    completedActivities.put(entry.getKey(), activityArchive);
	}

	// save the historic attempt
	LearnerProgressArchive learnerProgressArchive = new LearnerProgressArchive(user, learnerProgress.getLesson(),
		attemptID, attemptedActivities, completedActivities, learnerProgress.getCurrentActivity(),
		learnerProgress.getLessonComplete(), learnerProgress.getStartDate(), learnerProgress.getFinishDate());

	// remove learner progress
	ILessonService lessonService = LearnerServiceProxy.getLessonService(getServlet().getServletContext());
	lessonService.removeLearnerProgress(lessonID, userID);

	IUserManagementService userManagementService = LearnerServiceProxy
		.getUserManagementService(getServlet().getServletContext());
	userManagementService.save(learnerProgressArchive);

	// display Learner interface with updated data
	return joinLesson(mapping, form, request, response);
    }

    /**
     * Produces necessary data for learner progress bar.
     */
    @SuppressWarnings("unchecked")
    public ActionForward getLearnerProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	Integer learnerId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, true);
	Integer monitorId = null;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userId = user != null ? user.getUserID() : null;
	if (learnerId == null) {
	    // get progress for current user
	    learnerId = userId;
	} else {
	    // monitor mode; get progress for user given in the parameter
	    monitorId = userId;
	}

	JSONObject responseJSON = new JSONObject();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	if (lessonId == null) {
	    // depending on when this is called, there may only be a toolSessionId known, not the lessonId.
	    Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	    ToolSession toolSession = LearnerServiceProxy.getLamsToolService(getServlet().getServletContext())
		    .getToolSession(toolSessionId);
	    lessonId = toolSession.getLesson().getLessonId();
	}

	responseJSON.put("messages", getProgressBarMessages());
	
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	Object[] ret = learnerService.getStructuredActivityURLs(learnerId, lessonId);
	if (ret == null) {
	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	    return null;
	}

	responseJSON.put("currentActivityId", ret[1]);
	responseJSON.put("isPreview", ret[2]);
	for (ActivityURL activity : (List<ActivityURL>) ret[0]) {
	    if (activity.getFloating()) {
		// these are support activities
		for (ActivityURL childActivity : activity.getChildActivities()) {
		    responseJSON.append("support",
			    activityProgressToJSON(childActivity, null, lessonId, learnerId, monitorId));
		}
	    } else {
		responseJSON.append("activities",
			activityProgressToJSON(activity, (Long) ret[1], lessonId, learnerId, monitorId));
	    }
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON.toString());

	return null;
    }

    public ActionForward getPresenceChatActiveUserCount(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	int count = PresenceWebsocketServer.getActiveUserCount(lessonId);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(count);
	return null;
    }

    /**
     * Forces a move to a destination Activity in the learning sequence, redirecting to the new page rather.
     */
    public ActionForward forceMoveRedirect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	ICoreLearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	Long fromActivityId = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_ACTIVITY_ID, true);
	Long toActivityId = WebUtil.readLongParam(request, AttributeNames.PARAM_DEST_ACTIVITY_ID, true);

	Activity fromActivity = null;
	Activity toActivity = null;

	if (fromActivityId != null) {
	    fromActivity = learnerService.getActivity(fromActivityId);
	}

	if (toActivityId != null) {
	    toActivity = learnerService.getActivity(toActivityId);
	}

	Integer learnerId = LearningWebUtil.getUserId();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	learnerService.moveToActivity(learnerId, lessonId, fromActivity, toActivity);

	if (LearnerAction.log.isDebugEnabled()) {
	    LearnerAction.log.debug("Force move for learner " + learnerId + " lesson " + lessonId + ". ");
	}

	String url = null;
	ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
	if (!toActivity.isFloating()) {
	    url = "/learning" + activityMapping.getDisplayActivityAction(lessonId);
	} else {
	    url = activityMapping.getActivityURL(toActivity);
	}

	return redirectToURL(mapping, response, url);
    }

    /**
     * Converts an activity in learner progress to a JSON object.
     */
    private JSONObject activityProgressToJSON(ActivityURL activity, Long currentActivityId, Long lessonId,
	    Integer learnerId, Integer monitorId) throws JSONException, IOException {
	JSONObject activityJSON = new JSONObject();
	activityJSON.put("id", activity.getActivityId());
	activityJSON.put("name", activity.getTitle());
	activityJSON.put("status", activity.getActivityId().equals(currentActivityId) ? 0 : activity.getStatus());

	// URL in learner mode
	String url = activity.getUrl();
	if ((url != null) && (monitorId != null)) {
	    // URL in monitor mode
	    url = Configuration.get(ConfigurationKeys.SERVER_URL)
		    + "monitoring/monitoring.do?method=getLearnerActivityURL&lessonID=" + lessonId + "&activityID="
		    + activity.getActivityId() + "&userID=" + learnerId;
	}

	if (url != null) {
	    if (url.startsWith("learner.do")) {
		url = "learning/" + url;
	    }
	    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	    if (!url.startsWith(serverUrl)) {
		// monitor mode URLs should be prepended with server URL
		url = serverUrl + url;
	    }
	    activityJSON.put("url", url);
	}

	String actType = activity.getType().toLowerCase();
	String type = "a";
	if (actType.contains("gate")) {
	    type = "g";
	} else if (actType.contains("options")) {
	    type = "o";
	} else if (actType.contains("branching")) {
	    type = "b";
	}
	activityJSON.put("type", type);

	if (activity.getChildActivities() != null) {
	    for (ActivityURL childActivity : activity.getChildActivities()) {
		activityJSON.append("childActivities",
			activityProgressToJSON(childActivity, currentActivityId, lessonId, learnerId, monitorId));
	    }
	}

	return activityJSON;
    }

    private JSONObject getProgressBarMessages() throws JSONException {
	if (progressBarMessages == null) {
	    progressBarMessages = new JSONObject();
	    MessageService messageService = LearnerServiceProxy.getMonitoringMessageService(getServlet().getServletContext());
	    for (String key : MONITOR_MESSAGE_KEYS) {
		String value = messageService.getMessage(key);
		progressBarMessages.put(key, value);
	    }
	    messageService = LearnerServiceProxy.getMessageService(getServlet().getServletContext());
	    for (String key : LEARNER_MESSAGE_KEYS) {
		String value = messageService.getMessage(key);
		progressBarMessages.put(key, value);
	    }
	}
	return progressBarMessages;
    }
    
    /**
     * Gets the lesson details based on lesson id or the current tool session
     */
    public ActionForward getLessonDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	JSONObject responseJSON = new JSONObject();
	Lesson lesson = null;
	
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	if ( lessonID != null ) {
	    lesson = LearnerServiceProxy.getLearnerService(getServlet().getServletContext()).getLesson(lessonID);

	} else {
	    Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	    ToolSession toolSession = LearnerServiceProxy.getLamsToolService(getServlet().getServletContext())
		    .getToolSession(toolSessionId);
	    lesson = toolSession.getLesson();
	}

	responseJSON.put(AttributeNames.PARAM_LESSON_ID, lesson.getLessonId());
	responseJSON.put(AttributeNames.PARAM_TITLE, lesson.getLessonName());
	responseJSON.put("allowRestart", lesson.getAllowLearnerRestart());
	responseJSON.put(AttributeNames.PARAM_PRESENCE_ENABLED, lesson.getLearnerPresenceAvailable());
	responseJSON.put(AttributeNames.PARAM_PRESENCE_IM_ENABLED, lesson.getLearnerImAvailable());

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON.toString());

	return null;
    }
}