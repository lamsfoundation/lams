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

package org.lamsfoundation.lams.learning.web.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.flux.FluxMap;
import org.lamsfoundation.lams.flux.FluxRegistry;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.presence.PresenceWebsocketServer;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.learningdesign.dto.GateActivityDTO;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.CompletedActivityProgressArchive;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LearnerProgressArchive;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LearnerActivityCompleteFluxItem;
import org.lamsfoundation.lams.lesson.util.LearnerLessonJoinFluxItem;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;

/**
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
 */
@Controller
@RequestMapping("/learner")
public class LearnerController {
    private static Logger log = Logger.getLogger(LearnerController.class);

    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private IGradebookService gradebookService;
    @Autowired
    private IMonitoringService monitoringService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ILamsToolService lamsToolService;
    @Autowired
    private ILamsCoreToolService lamsCoreToolService;
    @Autowired
    @Qualifier("learningMessageService")
    private MessageService messageService;
    @Autowired
    private ActivityMapping activityMapping;

    private static final String[] MONITOR_MESSAGE_KEYS = new String[] {
	    "label.learner.progress.activity.current.tooltip", "label.learner.progress.activity.completed.tooltip",
	    "label.learner.progress.activity.attempted.tooltip", "label.learner.progress.activity.tostart.tooltip",
	    "label.learner.progress.activity.support.tooltip" };

    private static final String[] LEARNER_MESSAGE_KEYS = new String[] { "message.learner.progress.restart.confirm",
	    "message.lesson.restart.button", "label.learner.progress.notebook", "button.exit",
	    "label.learner.progress.support", "label.my.progress" };

    // flux management
    public static final String LEARNER_TIMELINE_FLUX_NAME = "learner timeline updated";

    public LearnerController() {
	FluxRegistry.initFluxMap(LEARNER_TIMELINE_FLUX_NAME, CommonConstants.ACTIVITY_ENTERED_SINK_NAME,
		(LearnerActivityCompleteFluxItem item, LearnerActivityCompleteFluxItem key) ->
			item.getLessonId() == key.getLessonId() && item.getUserId() == key.getUserId(),
		(LearnerActivityCompleteFluxItem item) -> {
		    ObjectNode responseJSON = null;
		    try {
			responseJSON = getLearnerProgress(item.getLessonId(), item.getUserId(), true);
			if (responseJSON != null) {
			    return UriUtils.encode(responseJSON.toString(), StandardCharsets.UTF_8.toString());
			}
		    } catch (Exception e) {
			log.error("Error while getting learner timeline flux", e);
		    }
		    return "";

		}, FluxMap.STANDARD_THROTTLE, FluxMap.STANDARD_TIMEOUT);
    }

    @RequestMapping("/redirectToURL")
    @ResponseBody
    private String redirectToURL(HttpServletResponse response, String url) throws IOException, ServletException {
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
     * 	An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send the
     * 	end-user.
     * @param form
     * 	The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     * 	A standard Servlet HttpServletRequest class.
     * @param response
     * 	A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     * 	next.
     * @throws IOException
     * @throws ServletException
     */

    @RequestMapping("/joinLesson")
    public String joinLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	// get user and lesson based on request
	Integer userId = LearningWebUtil.getUserId();
	long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	try {
	    // security check
	    Lesson lesson = learnerService.getLesson(lessonID);
	    User user = (User) userManagementService.findById(User.class, userId);
	    if ((lesson.getLessonClass() == null) || !lesson.getLessonClass().getLearners().contains(user)) {
		request.setAttribute("messageKey",
			"User " + user.getLogin() + " is not a learner in the requested lesson.");
		return "msgContent";
	    }
	    // check lesson's state if its suitable for learner's access
	    if (!lesson.isLessonAccessibleForLearner()) {
		request.setAttribute("messageKey", "Lesson is inaccessible");
		return "msgContent";
	    }

	    if (log.isDebugEnabled()) {
		log.debug("The learner [" + userId + "] is joining the lesson [" + lessonID + "]");
	    }

	    // join user to the lesson on the server
	    LearnerProgress learnerProgress = learnerService.joinLesson(userId, lessonID);

	    if (log.isDebugEnabled()) {
		log.debug("The learner [" + userId + "] joined lesson. The" + "progress data is:"
			+ learnerProgress.toString());
	    }

	    String url = "learning/" + activityMapping.getDisplayActivityAction(lessonID);
	    redirectToURL(response, url);

	    FluxRegistry.emit(CommonConstants.LESSON_JOINED_SINK_NAME, new LearnerLessonJoinFluxItem(lessonID, userId));

	} catch (Exception e) {
	    log.error("An error occurred while learner " + userId + " attempting to join the lesson.", e);
	    return "error";
	}

	return null;
    }

    /**
     * Archives current learner progress and moves a learner back to the start of lesson.
     */
    @RequestMapping("/restartLesson")
    public String restartLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	// fetch necessary parameters
	long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonID);
	if (!lesson.getAllowLearnerRestart()) {
	    throw new ServletException("Lesson with ID " + lessonID + " does not allow learners to restart it.");
	}
	Integer userID = LearningWebUtil.getUserId();
	User user = userManagementService.getUserById(userID);

	// find number of previous attempts
	Integer attemptID = learnerService.getProgressArchiveMaxAttemptID(userID, lessonID);
	if (attemptID == null) {
	    attemptID = 0;
	}
	attemptID++;

	// make a copy of attempted and completed activities
	Date archiveDate = new Date();
	LearnerProgress learnerProgress = learnerService.getProgress(userID, lessonID);
	Map<Activity, Date> attemptedActivities = new HashMap<>(learnerProgress.getAttemptedActivities());
	Map<Activity, CompletedActivityProgressArchive> completedActivities = new HashMap<>();
	for (Entry<Activity, CompletedActivityProgress> entry : learnerProgress.getCompletedActivities().entrySet()) {
	    CompletedActivityProgressArchive activityArchive = new CompletedActivityProgressArchive(
		    entry.getValue().getStartDate(), entry.getValue().getFinishDate());
	    completedActivities.put(entry.getKey(), activityArchive);
	}

	// save the historic attempt
	LearnerProgressArchive learnerProgressArchive = new LearnerProgressArchive(user, learnerProgress.getLesson(),
		attemptID, attemptedActivities, completedActivities, learnerProgress.getCurrentActivity(),
		learnerProgress.getLessonComplete(), learnerProgress.getStartDate(), learnerProgress.getFinishDate(),
		archiveDate);
	userManagementService.save(learnerProgressArchive);
	gradebookService.archiveLearnerMarks(lessonID, userID, archiveDate);
	gradebookService.removeLearnerFromLesson(lessonID, userID);
	monitoringService.removeLearnerContent(lessonID, userID);
	// remove learner progress
	lessonService.removeLearnerProgress(lessonID, userID);

	// display Learner interface with updated data
	return joinLesson(request, response);
    }

    /**
     * Produces necessary data for learner progress bar.
     */
    @RequestMapping("/getLearnerProgress")
    @ResponseBody
    public String getLearnerProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	if (lessonId == null) {
	    // depending on when this is called, there may only be a toolSessionId known, not the lessonId.
	    Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	    ToolSession toolSession = lamsToolService.getToolSession(toolSessionId);
	    lessonId = toolSession.getLesson().getLessonId();
	}

	ObjectNode responseJSON = getLearnerProgress(lessonId, learnerId, monitorId != null);
	if (responseJSON == null) {
	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	    return null;
	}
	Lesson lesson = lessonService.getLesson(lessonId);
	responseJSON.put("lessonName", lesson.getLessonName());
	responseJSON.set("messages", getProgressBarMessages());

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Produces necessary data for learner progress bar.
     */

    @RequestMapping(path = "/getLearnerProgressUpdateFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getLearnerProgressUpdateFlux(@RequestParam long lessonId, @RequestParam int userId)
	    throws JsonProcessingException, IOException {
	return FluxRegistry.get(LEARNER_TIMELINE_FLUX_NAME,
		new LearnerActivityCompleteFluxItem(lessonId, userId, null));
    }

    @SuppressWarnings("unchecked")
    private ObjectNode getLearnerProgress(long lessonId, int learnerId, boolean monitorMode) throws IOException {
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();

	Object[] ret = learnerService.getStructuredActivityURLs(learnerId, lessonId);
	if (ret == null) {
	    return null;
	}

	responseJSON.put("currentActivityId", (Long) ret[1]);
	responseJSON.put("isPreview", (boolean) ret[2]);
	for (ActivityURL activity : (List<ActivityURL>) ret[0]) {
	    if (activity.getFloating()) {
		// these are support activities
		for (ActivityURL childActivity : activity.getChildActivities()) {
		    responseJSON.withArray("support")
			    .add(activityProgressToJSON(childActivity, null, lessonId, learnerId, monitorMode));
		}
	    } else {
		responseJSON.withArray("activities")
			.add(activityProgressToJSON(activity, (Long) ret[1], lessonId, learnerId, monitorMode));
	    }
	}
	return responseJSON;
    }

    @RequestMapping("/getPresenceChatActiveUserCount")
    @ResponseBody
    public String getPresenceChatActiveUserCount(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	int count = PresenceWebsocketServer.getActiveUserCount(lessonId);

	return String.valueOf(count);
    }

    /**
     * Forces a move to a destination Activity in the learning sequence, redirecting to the new page rather.
     */

    @RequestMapping("/forceMoveRedirect")
    public String forceMoveRedirect(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

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

	if (log.isDebugEnabled()) {
	    log.debug("Force move for learner " + learnerId + " lesson " + lessonId + ". ");
	}

	String url = null;
	if (!toActivity.isFloating()) {
	    url = "/learning" + activityMapping.getDisplayActivityAction(lessonId);
	} else {
	    url = activityMapping.getActivityURL(toActivity);
	}

	return redirectToURL(response, url);
    }

    /**
     * Converts an activity in learner progress to a JSON object.
     */
    private ObjectNode activityProgressToJSON(ActivityURL activity, Long currentActivityId, Long lessonId,
	    Integer learnerId, boolean monitorMode) throws IOException {
	ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
	activityJSON.put("id", activity.getActivityId());
	activityJSON.put("name", activity.getTitle());
	int status = activity.getActivityId().equals(currentActivityId) ? 0 : activity.getStatus();
	activityJSON.put("status", status);

	// URL in learner mode
	String url = activity.getUrl();
	if ((url != null) && monitorMode) {
	    // URL in monitor mode
	    url = Configuration.get(ConfigurationKeys.SERVER_URL)
		    + "monitoring/monitoring/getLearnerActivityURL.do?lessonID=" + lessonId + "&activityID="
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
	} else if (actType.contains("options") || actType.contains("ordered")) {
	    type = "o";
	} else if (actType.contains("branching")) {
	    type = "b";
	} else {
	    if (activity.getIconURL() != null) {
		activityJSON.put("iconURL", activity.getIconURL());
	    }

	    if (status == 1) {
		GradebookUserActivity activityMark = gradebookService.getGradebookUserActivity(activity.getActivityId(),
			learnerId);
		if (activityMark != null && activityMark.getMark() != null) {
		    activityJSON.put("mark", activityMark.getMark());
		}
	    }
	}

	Long activityMaxMark = lamsCoreToolService.getActivityMaxPossibleMark(activity.getActivityId());
	if (activityMaxMark != null) {
	    activityJSON.put("maxMark", activityMaxMark);
	}
	if (activity.getDuration() != null) {
	    activityJSON.put("duration", DateUtil.convertTimeToString(activity.getDuration()));
	}

	activityJSON.put("type", type);
	activityJSON.put("isGrouping", actType.contains("grouping"));

	if (activity.getChildActivities() != null) {
	    for (ActivityURL childActivity : activity.getChildActivities()) {
		activityJSON.withArray("childActivities")
			.add(activityProgressToJSON(childActivity, currentActivityId, lessonId, learnerId,
				monitorMode));
	    }
	}

	return activityJSON;
    }

    private ObjectNode getProgressBarMessages() {
	ObjectNode progressBarMessages = JsonNodeFactory.instance.objectNode();
	for (String key : MONITOR_MESSAGE_KEYS) {
	    String value = messageService.getMessage(key);
	    progressBarMessages.put(key, value);
	}
	for (String key : LEARNER_MESSAGE_KEYS) {
	    String value = messageService.getMessage(key);
	    progressBarMessages.put(key, value);
	}
	return progressBarMessages;
    }

    /**
     * Gets the lesson details based on lesson id or the current tool session
     */
    @RequestMapping("/getLessonDetails")
    @ResponseBody
    public String getLessonDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	Lesson lesson = null;

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	if (lessonID != null) {
	    lesson = lessonService.getLesson(lessonID);

	} else {
	    Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	    ToolSession toolSession = lamsToolService.getToolSession(toolSessionId);
	    lesson = toolSession.getLesson();
	}

	responseJSON.put(AttributeNames.PARAM_LESSON_ID, lesson.getLessonId());
	responseJSON.put(AttributeNames.PARAM_TITLE, lesson.getLessonName());
	responseJSON.put("allowRestart", lesson.getAllowLearnerRestart());
	responseJSON.put(AttributeNames.PARAM_PRESENCE_ENABLED, lesson.getLearnerPresenceAvailable());
	responseJSON.put(AttributeNames.PARAM_PRESENCE_IM_ENABLED, lesson.getLearnerImAvailable());

	response.setContentType("application/json;charset=utf-8");

	return responseJSON.toString();
    }

    @RequestMapping("/isNextGateActivityOpen")
    @ResponseBody
    public String isNextGateActivityOpen(@RequestParam(required = false) Long toolSessionId,
	    @RequestParam(required = false) Long activityId, HttpSession session, Locale locale) {

	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	if (userDto == null) {
	    throw new IllegalArgumentException("No user is logged in");
	}

	Integer userId = userDto.getUserID();
	GateActivityDTO gateDto = null;
	if (toolSessionId != null) {
	    gateDto = learnerService.isNextGateActivityOpenByToolSessionId(userId, toolSessionId);
	} else if (activityId != null) {
	    gateDto = learnerService.isNextGateActivityOpenByActivityId(userId, activityId);
	} else {
	    throw new IllegalArgumentException("Either tool session ID or activity ID has to be provided");
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (gateDto == null) {
	    responseJSON.put("status", "open");
	} else {
	    responseJSON.put("status", "closed");

	    String message = null;
	    GateActivity gate = gateDto.getGate();
	    if (gate.isScheduleGate()) {
		ScheduleGateActivity scheduleGate = (ScheduleGateActivity) gate;
		if (!Boolean.TRUE.equals(scheduleGate.getGateActivityCompletionBased())) {
		    Lesson lesson = gate.getLearningDesign().getLessons().iterator().next();
		    User user = userManagementService.getUserById(userId);
		    TimeZone userTimeZone = TimeZone.getTimeZone(user.getTimeZone());

		    Calendar openTime = new GregorianCalendar(userTimeZone);
		    Date lessonStartTime = DateUtil.convertToTimeZoneFromDefault(userTimeZone,
			    lesson.getStartDateTime());
		    openTime.setTime(lessonStartTime);
		    openTime.add(Calendar.MINUTE, scheduleGate.getGateStartTimeOffset().intValue());
		    String openDateString = DateUtil.convertToStringForJSON(openTime.getTime(), locale);
		    message = messageService.getMessage("label.gate.closed.preceding.activity.schedule",
			    new Object[] { openDateString });
		}
	    } else if (gate.isConditionGate()) {
		message = messageService.getMessage("label.gate.closed.preceding.activity.condition");
	    }

	    if (message == null) {
		message = messageService.getMessage("label.gate.closed.preceding.activity");
	    }

	    responseJSON.put("message", message);
	}

	return responseJSON.toString();
    }
}