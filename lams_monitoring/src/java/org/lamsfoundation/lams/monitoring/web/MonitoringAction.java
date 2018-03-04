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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.ObjectExtractor;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenBranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The action servlet that provide all the monitoring functionalities. It interact with the teacher via JSP monitoring
 * interface.
 *
 * @author Jacky Fang
 */
public class MonitoringAction extends LamsDispatchAction {

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------
    private static final String NOT_SUPPORTED_SCREEN = "notsupported";
    private static final String TIME_CHART_SCREEN = "timeChart";
    private static final String ERROR = "error";
    private static final DateFormat LESSON_SCHEDULING_DATETIME_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm");

    private static final int LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT = 53;
    private static final int LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT = 7;
    private static final int USER_PAGE_SIZE = 10;

    private static ILogEventService logEventService;

    private static ILessonService lessonService;

    private static ISecurityService securityService;

    private static IMonitoringService monitoringService;

    private static IUserManagementService userManagementService;

    private static ILearnerService learnerService;

    private static ILamsToolService toolService;

    private static MessageService messageService;

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private ActionForward redirectToURL(ActionMapping mapping, HttpServletResponse response, String url)
	    throws IOException {
	if (url == null) {
	    return mapping.findForward(MonitoringAction.NOT_SUPPORTED_SCREEN);
	}

	String fullURL = WebUtil.convertToFullURL(url);
	response.sendRedirect(response.encodeRedirectURL(fullURL));
	return null;
    }

    /**
     * Initializes a lesson for specific learning design with the given lesson title and lesson description. If
     * initialization is successful, this method will the ID of new lesson.
     *
     * Currently used only in TestHarness and Authoring Preview.
     */
    public ActionForward initializeLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String title = WebUtil.readStrParam(request, "lessonName");
	if (title == null) {
	    title = "lesson";
	}
	String desc = WebUtil.readStrParam(request, "lessonDescription", true);
	if (desc == null) {
	    desc = "description";
	}
	Integer organisationId = WebUtil.readIntParam(request, "organisationID", true);
	long ldId = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);
	Integer copyType = WebUtil.readIntParam(request, "copyType", true);
	String customCSV = request.getParameter("customCSV");
	Boolean learnerPresenceAvailable = WebUtil.readBooleanParam(request, "learnerPresenceAvailable", false);
	Boolean learnerImAvailable = WebUtil.readBooleanParam(request, "learnerImAvailable", false);
	Boolean liveEditEnabled = WebUtil.readBooleanParam(request, "liveEditEnabled", false);
	Boolean forceRestart = WebUtil.readBooleanParam(request, "forceRestart", false);
	Boolean allowRestart = WebUtil.readBooleanParam(request, "allowRestart", false);
	boolean gradebookOnComplete = WebUtil.readBooleanParam(request, "gradebookOnComplete", false);

	Lesson newLesson = null;
	if ((copyType != null) && copyType.equals(LearningDesign.COPY_TYPE_PREVIEW)) {
	    newLesson = getMonitoringService().initializeLessonForPreview(title, desc, ldId, getUserId(), customCSV,
		    learnerPresenceAvailable, learnerImAvailable, liveEditEnabled);
	} else {
	    try {
		newLesson = getMonitoringService().initializeLesson(title, desc, ldId, organisationId, getUserId(),
			customCSV, false, false, learnerPresenceAvailable, learnerImAvailable, liveEditEnabled, false,
			forceRestart, allowRestart, gradebookOnComplete, null, null);
	    } catch (SecurityException e) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
		return null;
	    }
	}

	PrintWriter writer = response.getWriter();
	writer.println(newLesson.getLessonId());
	return null;
    }

    /**
     * The Struts dispatch method that starts a lesson that has been created beforehand. Most likely, the request to
     * start lesson should be triggered by the Monitoring This method will delegate to the Spring service bean to
     * complete all the steps for starting a lesson.
     *
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward startLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    getMonitoringService().startLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write("true");
	return null;
    }

    public ActionForward createLessonClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	IUserManagementService userManagementService = MonitoringServiceProxy
		.getUserManagementService(getServlet().getServletContext());

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	List<User> allUsers = userManagementService.getUsersFromOrganisation(organisationId);
	String learnerGroupName = organisation.getName() + " learners";
	String staffGroupName = organisation.getName() + " staff";
	List<User> learners = parseUserList(request, "learners", allUsers);
	List<User> staff = parseUserList(request, "monitors", allUsers);

	try {
	    getMonitoringService().createLessonClassForLesson(lessonId, organisation, learnerGroupName, learners,
		    staffGroupName, staff, userID);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
	    return null;
	}

	return null;
    }

    public ActionForward addLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ParseException {
	String lessonName = request.getParameter("lessonName");
	if (!ValidationUtil.isOrgNameValid(lessonName)) {
	    throw new IOException("Lesson name contains invalid characters");
	}

	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	long ldId = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);

	boolean introEnable = WebUtil.readBooleanParam(request, "introEnable", false);
	String introDescription = introEnable ? request.getParameter("introDescription") : null;
	boolean introImage = introEnable && WebUtil.readBooleanParam(request, "introImage", false);
	boolean startMonitor = WebUtil.readBooleanParam(request, "startMonitor", false);
	boolean enableLiveEdit = WebUtil.readBooleanParam(request, "liveEditEnable", false);
	boolean notificationsEnable = WebUtil.readBooleanParam(request, "notificationsEnable", false);
	boolean presenceEnable = WebUtil.readBooleanParam(request, "presenceEnable", false);
	boolean imEnable = WebUtil.readBooleanParam(request, "imEnable", false);
	Integer splitNumberLessons = WebUtil.readIntParam(request, "splitNumberLessons", true);
	boolean schedulingEnable = WebUtil.readBooleanParam(request, "schedulingEnable", false);
	Date schedulingDatetime = schedulingEnable
		? MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(request.getParameter("schedulingDatetime"))
		: null;
	boolean forceRestart = WebUtil.readBooleanParam(request, "forceRestart", false);
	boolean allowRestart = WebUtil.readBooleanParam(request, "allowRestart", false);

	boolean precedingLessonEnable = WebUtil.readBooleanParam(request, "precedingLessonEnable", false);
	Long precedingLessonId = precedingLessonEnable ? WebUtil.readLongParam(request, "precedingLessonId", true)
		: null;
	boolean timeLimitEnable = WebUtil.readBooleanParam(request, "timeLimitEnable", false);
	Integer timeLimitDays = WebUtil.readIntParam(request, "timeLimitDays", true);
	boolean timeLimitIndividualField = WebUtil.readBooleanParam(request, "timeLimitIndividual", false);
	Integer timeLimitIndividual = timeLimitEnable && timeLimitIndividualField ? timeLimitDays : null;
	Integer timeLimitLesson = timeLimitEnable && !timeLimitIndividualField ? timeLimitDays : null;
	boolean gradebookOnComplete = WebUtil.readBooleanParam(request, "gradebookOnComplete", false);

	IUserManagementService userManagementService = MonitoringServiceProxy
		.getUserManagementService(getServlet().getServletContext());

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	Integer userId = getUserId();
	User creator = (User) userManagementService.findById(User.class, userId);

	List<User> allUsers = userManagementService.getUsersFromOrganisation(organisationId);
	List<User> learners = parseUserList(request, "learners", allUsers);
	String learnerGroupName = organisation.getName() + " learners";

	List<User> staff = parseUserList(request, "monitors", allUsers);
	// add the creator as staff, if not already done
	if (!staff.contains(creator)) {
	    staff.add(creator);
	}
	String staffGroupName = organisation.getName() + " staff";

	// either all users participate in a lesson, or we split them among instances
	List<User> lessonInstanceLearners = splitNumberLessons == null ? learners
		: new ArrayList<User>((learners.size() / splitNumberLessons) + 1);
	for (int lessonIndex = 1; lessonIndex <= (splitNumberLessons == null ? 1 : splitNumberLessons); lessonIndex++) {
	    String lessonInstanceName = lessonName;
	    String learnerGroupInstanceName = learnerGroupName;
	    String staffGroupInstanceName = staffGroupName;

	    if (splitNumberLessons != null) {
		// prepare data for lesson split
		lessonInstanceName += " " + lessonIndex;
		learnerGroupInstanceName += " " + lessonIndex;
		staffGroupInstanceName += " " + lessonIndex;
		lessonInstanceLearners.clear();
		for (int learnerIndex = lessonIndex - 1; learnerIndex < learners
			.size(); learnerIndex += splitNumberLessons) {
		    lessonInstanceLearners.add(learners.get(learnerIndex));
		}
	    }

	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Creating lesson "
			+ (splitNumberLessons == null ? "" : "(" + lessonIndex + "/" + splitNumberLessons + ") ") + "\""
			+ lessonInstanceName + "\"");
	    }

	    Lesson lesson = null;
	    try {
		lesson = getMonitoringService().initializeLesson(lessonInstanceName, introDescription, ldId,
			organisationId, userId, null, introEnable, introImage, presenceEnable, imEnable, enableLiveEdit,
			notificationsEnable, forceRestart, allowRestart, gradebookOnComplete, timeLimitIndividual,
			precedingLessonId);

		getMonitoringService().createLessonClassForLesson(lesson.getLessonId(), organisation,
			learnerGroupInstanceName, lessonInstanceLearners, staffGroupInstanceName, staff, userId);
	    } catch (SecurityException e) {
		try {
		    response.sendError(HttpServletResponse.SC_FORBIDDEN,
			    "User is not a monitor in the organisation or lesson");
		} catch (IllegalStateException e1) {
		    LamsDispatchAction.log
			    .warn("Tried to tell user that \"User is not a monitor in the organisation or lesson\","
				    + "but the HTTP response was already written, probably by some other error");
		}
		return null;
	    }

	    if (!startMonitor) {
		try {
		    if (schedulingDatetime == null) {
			getMonitoringService().startLesson(lesson.getLessonId(), userId);
		    } else {
			// if lesson should start in few days, set it here
			getMonitoringService().startLessonOnSchedule(lesson.getLessonId(), schedulingDatetime, userId);
		    }

		    // if lesson should finish in few days, set it here
		    if (timeLimitLesson != null) {
			getMonitoringService().finishLessonOnSchedule(lesson.getLessonId(), timeLimitLesson, userId);
		    }
		} catch (SecurityException e) {
		    try {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		    } catch (IllegalStateException e1) {
			LamsDispatchAction.log.warn("Tried to tell user that \"User is not a monitor in the lesson\","
				+ "but the HTTP response was already written, probably by some other error");
		    }
		    return null;
		}
	    }
	}

	return null;
    }

    /**
     * Adds all course learners to the given lesson.
     */
    @SuppressWarnings("unchecked")
    public ActionForward addAllOrganisationLearnersToLesson(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "add all lesson learners to lesson", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
	Lesson lesson = getLessonService().getLesson(lessonId);
	Vector<User> learners = getUserManagementService()
		.getUsersFromOrganisationByRole(lesson.getOrganisation().getOrganisationId(), Role.LEARNER, true);
	getLessonService().addLearners(lesson, learners);
	return null;
    }

    public ActionForward startOnScheduleLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ParseException, IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_START_DATE);
	Date startDate = MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateStr);
	try {
	    getMonitoringService().startLessonOnSchedule(lessonId, startDate, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	return null;
    }

    /**
     * The Struts dispatch method to archive a lesson.
     *
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward archiveLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    getMonitoringService().archiveLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	return null;
    }

    /**
     * The Struts dispatch method to "unarchive" a lesson. Returns it back to its previous state.
     *
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward unarchiveLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    getMonitoringService().unarchiveLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * The purpose of suspending is to hide the lesson from learners temporarily. It doesn't make any sense to suspend a
     * created or a not started (ie scheduled) lesson as they will not be shown on the learner interface anyway! If the
     * teacher tries to suspend a lesson that is not in the STARTED_STATE, then an error should be returned to UI.
     */
    public ActionForward suspendLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ParseException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_END_DATE, true);
	try {
	    if (dateStr == null || dateStr.length() == 0)
		getMonitoringService().suspendLesson(lessonId, getUserId(), true);
	    else
		getMonitoringService().finishLessonOnSchedule(lessonId,
			MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateStr), getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * Unsuspend a lesson which state must be Lesson.SUPSENDED_STATE. Otherwise a error message will return to UI
     * client.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward unsuspendLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    getMonitoringService().unsuspendLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * <P>
     * The STRUTS action will send back a JSON message after marking the lesson by the given lesson ID as
     * <code>Lesson.REMOVED_STATE</code> status.
     * </P>
     * <P>
     * This action need a lession ID as input.
     * </P>
     *
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer userId = getUserId();
	boolean permanently = WebUtil.readBooleanParam(request, "permanently", false);

	if (permanently) {
	    getMonitoringService().removeLessonPermanently(lessonId, userId);
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().print("OK");
	    return null;
	}

	JSONObject jsonObject = new JSONObject();

	try {
	    // if this method throws an Exception, there will be no removeLesson=true in the JSON reply
	    getMonitoringService().removeLesson(lessonId, userId);
	    jsonObject.put("removeLesson", true);

	} catch (Exception e) {
	    String[] msg = new String[1];
	    msg[0] = e.getMessage();
	    jsonObject.put("removeLesson",
		    getMonitoringService().getMessageService().getMessage("error.system.error", msg));
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(jsonObject);
	return null;
    }

    /**
     * <P>
     * This action need a lession ID, Learner ID and Activity ID as input. Activity ID is optional, if it is null, all
     * activities for this learner will complete to as end as possible.
     * </P>
     *
     * @param form
     * @param request
     * @param response
     * @return An ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws JSONException
     */
    public ActionForward forceComplete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {

	// get parameters
	Long activityId = null;
	String actId = request.getParameter(AttributeNames.PARAM_ACTIVITY_ID);
	if (actId != null) {
	    try {
		activityId = new Long(Long.parseLong(actId));
	    } catch (Exception e) {
		activityId = null;
	    }
	}

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String learnerIDs = request.getParameter(MonitoringConstants.PARAM_LEARNER_ID);
	Integer requesterId = getUserId();
	boolean removeLearnerContent = WebUtil.readBooleanParam(request,
		MonitoringConstants.PARAM_REMOVE_LEARNER_CONTENT, false);

	JSONObject jsonCommand = new JSONObject();
	jsonCommand.put("message", getMessageService().getMessage("force.complete.learner.command.message"));
	jsonCommand.put("redirectURL", "/lams/learning/learner.do?method=joinLesson&lessonID=" + lessonId);
	String command = jsonCommand.toString();

	String message = null;
	User learner = null;
	try {
	    for (String learnerIDString : learnerIDs.split(",")) {
		Integer learnerID = Integer.valueOf(learnerIDString);
		message = getMonitoringService().forceCompleteActivitiesByUser(learnerID, requesterId, lessonId,
			activityId, removeLearnerContent);

		learner = (User) getUserManagementService().findById(User.class, learnerID);
		getLearnerService().createCommandForLearner(lessonId, learner.getLogin(), command);
	    }
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	if (LamsDispatchAction.log.isDebugEnabled()) {
	    LamsDispatchAction.log
		    .debug("Force complete for learners " + learnerIDs + " lesson " + lessonId + ". " + message);
	}

	// audit log force completion attempt
	String messageKey = (activityId == null) ? "audit.force.complete.end.lesson" : "audit.force.complete";
	Object[] args = new Object[] { learnerIDs, activityId, lessonId };
	String auditMessage = getMonitoringService().getMessageService().getMessage(messageKey, args);
	getLogEventService().logEvent(LogEvent.TYPE_FORCE_COMPLETE, requesterId, null, lessonId, null,
		auditMessage + " " + message);

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    /**
     * Get learners who are part of the lesson class.
     */
    public ActionForward getLessonLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "get lesson learners", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String searchPhrase = request.getParameter("searchPhrase");
	Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	if (pageNumber == null) {
	    pageNumber = 1;
	}
	boolean orderAscending = WebUtil.readBooleanParam(request, "orderAscending", true);

	List<User> learners = getLessonService().getLessonLearners(lessonId, searchPhrase,
		MonitoringAction.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringAction.USER_PAGE_SIZE, orderAscending);
	JSONArray learnersJSON = new JSONArray();
	for (User learner : learners) {
	    learnersJSON.put(WebUtil.userToJSON(learner));
	}

	Integer learnerCount = getLessonService().getCountLessonLearners(lessonId, searchPhrase);

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("learners", learnersJSON);
	responseJSON.put("learnerCount", learnerCount);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Gets learners or monitors of the lesson and organisation containing it.
     */
    public ActionForward getClassMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "get class members", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Lesson lesson = getLessonService().getLesson(lessonId);
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE);
	boolean isMonitor = role.equals(Role.MONITOR);
	User creator = isMonitor ? lesson.getUser() : null;
	Integer currentUserId = isMonitor ? getUserId() : null;
	String searchPhrase = request.getParameter("searchPhrase");
	Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	if (pageNumber == null) {
	    pageNumber = 1;
	}
	boolean orderAscending = WebUtil.readBooleanParam(request, "orderAscending", true);
	JSONObject responseJSON = new JSONObject();

	// find organisation users and whether they participate in the current lesson
	Map<User, Boolean> users = getLessonService().getUsersWithLessonParticipation(lessonId, role, searchPhrase,
		MonitoringAction.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringAction.USER_PAGE_SIZE, orderAscending);

	// if the result is less then page size, then no need for full check of user count
	Integer userCount = users.size() < MonitoringAction.USER_PAGE_SIZE ? users.size()
		: getUserManagementService().getCountRoleForOrg(lesson.getOrganisation().getOrganisationId(),
			isMonitor ? Role.ROLE_MONITOR : Role.ROLE_LEARNER, searchPhrase);

	responseJSON.put("userCount", userCount);

	JSONArray usersJSON = new JSONArray();
	for (Entry<User, Boolean> userEntry : users.entrySet()) {
	    User user = userEntry.getKey();
	    JSONObject userJSON = WebUtil.userToJSON(user);
	    userJSON.put("classMember", userEntry.getValue());
	    // teacher can't remove lesson creator and himself from the lesson staff
	    if (isMonitor && (creator.getUserId().equals(user.getUserId()) || currentUserId.equals(user.getUserId()))) {
		userJSON.put("readonly", true);
	    }
	    usersJSON.put(userJSON);
	}
	responseJSON.put("users", usersJSON);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Gets users in JSON format who are at the given activity at the moment or finished the given lesson.
     */
    public ActionForward getCurrentLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	JSONArray learnersJSON = new JSONArray();
	Integer learnerCount = null;

	Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	if (pageNumber == null) {
	    pageNumber = 1;
	}
	boolean orderAscending = WebUtil.readBooleanParam(request, "orderAscending", true);
	// if activity ID is provided, lesson ID is ignored
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	if (activityId == null) {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "get lesson completed learners", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    List<User> learners = getMonitoringService().getUsersCompletedLesson(lessonId,
		    MonitoringAction.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringAction.USER_PAGE_SIZE,
		    orderAscending);
	    for (User learner : learners) {
		learnersJSON.put(WebUtil.userToJSON(learner));
	    }

	    learnerCount = getMonitoringService().getCountLearnersCompletedLesson(lessonId);
	} else {
	    Activity activity = getMonitoringService().getActivityById(activityId);
	    Lesson lesson = (Lesson) activity.getLearningDesign().getLessons().iterator().next();
	    if (!getSecurityService().isLessonMonitor(lesson.getLessonId(), getUserId(), "get activity learners",
		    false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    Set<Long> activities = new TreeSet<Long>();
	    activities.add(activityId);

	    List<User> learners = getMonitoringService().getLearnersByActivities(activities.toArray(new Long[] {}),
		    MonitoringAction.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringAction.USER_PAGE_SIZE,
		    orderAscending);
	    for (User learner : learners) {
		learnersJSON.put(WebUtil.userToJSON(learner));
	    }
	    learnerCount = getMonitoringService().getCountLearnersCurrentActivities(new Long[] { activityId })
		    .get(activityId);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("learners", learnersJSON);
	responseJSON.put("learnerCount", learnerCount);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Adds/removes learners and monitors to/from lesson class.
     */
    public ActionForward updateLessonClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "update lesson class", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	int userId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE);
	boolean add = WebUtil.readBooleanParam(request, "add");
	boolean result = false;

	if (role.equals(Role.MONITOR)) {
	    if (add) {
		result = getLessonService().addStaffMember(lessonId, userId);
	    } else {
		result = getLessonService().removeStaffMember(lessonId, userId);
	    }
	} else if (role.equals(Role.LEARNER)) {
	    if (add) {
		result = getLessonService().addLearner(lessonId, userId);
	    } else {
		getLessonService().removeLearnerProgress(lessonId, userId);
		result = getLessonService().removeLearner(lessonId, userId);
	    }
	}

	if (result) {
	    LamsDispatchAction.log.info((add ? "Added a " : "Removed a ") + role + " with ID " + userId
		    + (add ? " to" : " from") + " lesson " + lessonId);
	} else {
	    LamsDispatchAction.log.warn("Failed when trying to " + (add ? "add a " : "remove a ") + role + " with ID "
		    + userId + (add ? " to" : " from") + " lesson " + lessonId);
	}

	return null;
    }

    public ActionForward getDictionaryXML(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	MessageService messageService = getMonitoringService().getMessageService();

	String module = WebUtil.readStrParam(request, "module", false);

	ArrayList<String> languageCollection = new ArrayList<String>();
	if (module.equals("timechart")) {

	    languageCollection.add(new String("sys.error"));
	    languageCollection.add(new String("chart.btn.activity.split"));
	    languageCollection.add(new String("chart.btn_completion.rate"));
	    languageCollection.add(new String("chart.series.completed.time"));
	    languageCollection.add(new String("chart.series.average.time"));
	    languageCollection.add(new String("chart.series.duration"));
	    languageCollection.add(new String("chart.legend.average"));
	    languageCollection.add(new String("show.average.checkbox"));
	    languageCollection.add(new String("search.learner.textbox"));
	    languageCollection.add(new String("chart.learner.linear.axis.title"));
	    languageCollection.add(new String("chart.learner.category.axis.title"));
	    languageCollection.add(new String("chart.learner.datatip.average"));

	    languageCollection.add(new String("label.learner"));
	    languageCollection.add(new String("time.chart.panel.title"));
	    languageCollection.add(new String("chart.time.format.hours"));
	    languageCollection.add(new String("chart.time.format.minutes"));
	    languageCollection.add(new String("chart.time.format.seconds"));
	    languageCollection.add(new String("label.completed"));
	    languageCollection.add(new String("advanced.tab.form.validation.schedule.date.error"));
	    languageCollection.add(new String("alert.no.learner.data"));
	}

	String languageOutput = "<xml><language>";

	for (int i = 0; i < languageCollection.size(); i++) {
	    languageOutput += "<entry key='" + languageCollection.get(i) + "'><name>"
		    + messageService.getMessage(languageCollection.get(i)) + "</name></entry>";
	}

	languageOutput += "</language></xml>";

	response.setContentType("text/xml");
	response.setCharacterEncoding("UTF-8");
	response.getWriter().print(languageOutput);

	return null;
    }

    /**
     * Calls the server to bring up the learner progress page. Assumes destination is a new window. The userid that
     * comes from UI is the user id of the learner for which we are calculating the url. This is different to all the
     * other calls.
     */
    public ActionForward getLearnerActivityURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, LamsToolServiceException {

	Integer learnerUserID = new Integer(WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID));
	Long activityID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	try {
	    String url = getMonitoringService().getLearnerActivityURL(lessonID, activityID, learnerUserID, getUserId());
	    return redirectToURL(mapping, response, url);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
    }

    /** Calls the server to bring up the activity's monitoring page. Assumes destination is a new window */
    public ActionForward getActivityMonitorURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, LamsToolServiceException {
	Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	String contentFolderID = WebUtil.readStrParam(request, "contentFolderID");
	try {
	    String url = getMonitoringService().getActivityMonitorURL(lessonID, activityID, contentFolderID,
		    getUserId());
	    return redirectToURL(mapping, response, url);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
    }

    /**
     * Displays Monitor Lesson page.
     */
    public ActionForward monitorLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	LessonDetailsDTO lessonDTO = getLessonService().getLessonDetails(lessonId);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (lessonDTO.getCreateDateTime() != null) {
	    DateFormat sfm = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    lessonDTO.setCreateDateTimeStr(sfm.format(lessonDTO.getCreateDateTime()));
	}
	// prepare encoded lessonId for shortened learner URL
	lessonDTO.setEncodedLessonID(WebUtil.encodeLessonId(lessonId));

	if (!getSecurityService().isLessonMonitor(lessonId, user.getUserID(), "monitor lesson", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	// should info box on Sequence tab be displayed?
	Short sequenceTabInfoShowCount = (Short) ss.getAttribute("sequenceTabInfoShowCount");
	if (sequenceTabInfoShowCount == null) {
	    sequenceTabInfoShowCount = 0;
	}
	// only few times per session
	if (sequenceTabInfoShowCount < MonitoringConstants.SEQUENCE_TAB_SHOW_INFO_MAX_COUNT) {
	    sequenceTabInfoShowCount++;
	    ss.setAttribute("sequenceTabInfoShowCount", sequenceTabInfoShowCount);
	    request.setAttribute("sequenceTabShowInfo", true);
	}

	IUserManagementService userManagementService = MonitoringServiceProxy
		.getUserManagementService(getServlet().getServletContext());
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		lessonDTO.getOrganisationID());
	request.setAttribute("notificationsAvailable", organisation.getEnableCourseNotifications());
	boolean enableLiveEdit = organisation.getEnableLiveEdit() && getUserManagementService()
		.isUserInRole(user.getUserID(), organisation.getOrganisationId(), Role.AUTHOR);
	request.setAttribute("enableLiveEdit", enableLiveEdit);
	request.setAttribute("lesson", lessonDTO);
	request.setAttribute("isTBLSequence", isTBLSequence(lessonId));

	return mapping.findForward("monitorLesson");
    }
    
    /**
     * If learning design contains the following activities Grouping->(MCQ or Assessment)->Leader Selection->Scratchie
     * (potentially with some other gates or activities in the middle), there is a good chance this is a TBL sequence
     * and all activities must be grouped.
     */
    private boolean isTBLSequence(Long lessonId) {
	
	Lesson lesson = getLessonService().getLesson(lessonId);
	Long firstActivityId = lesson.getLearningDesign().getFirstActivity().getActivityId();
	//Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity
	Activity firstActivity = getMonitoringService().getActivityById(firstActivityId);
	
	return verifyNextActivityFitsTbl(firstActivity, "Grouping");
    }
    
    /**
     * Traverses the learning design verifying it follows typical TBL structure
     * 
     * @param activity
     * @param anticipatedActivity could be either "Grouping", "MCQ or Assessment", "Leaderselection" or "Scratchie"
     */
    private boolean verifyNextActivityFitsTbl(Activity activity, String anticipatedActivity) {
	    
	Transition transitionFromActivity = activity.getTransitionFrom();
	//TBL can finish with the Scratchie
	if (transitionFromActivity == null && !"Scratchie".equals(anticipatedActivity)) {
	    return false;
	}
	// query activity from DB as transition holds only proxied activity object
	Long nextActivityId = transitionFromActivity == null ? null : transitionFromActivity.getToActivity().getActivityId();
	Activity nextActivity = nextActivityId == null ? null : monitoringService.getActivityById(nextActivityId);
	
	switch (anticipatedActivity) {
	    case "Grouping":
		//the first activity should be a grouping
		if (activity instanceof GroupingActivity) {
		    return verifyNextActivityFitsTbl(nextActivity, "MCQ or Assessment");
		    
		} else {
		    return verifyNextActivityFitsTbl(nextActivity, "Grouping");
		}

	    case "MCQ or Assessment":
		//the second activity shall be a MCQ or Assessment
		if (activity.isToolActivity() && (CentralConstants.TOOL_SIGNATURE_ASSESSMENT
			.equals(((ToolActivity) activity).getTool().getToolSignature())
			|| CentralConstants.TOOL_SIGNATURE_MCQ
				.equals(((ToolActivity) activity).getTool().getToolSignature()))) {
		    return verifyNextActivityFitsTbl(nextActivity, "Leaderselection");
		    
		} else {
		    return verifyNextActivityFitsTbl(nextActivity, "MCQ or Assessment");
		}

	    case "Leaderselection":
		//the third activity shall be a Leader Selection
		if (activity.isToolActivity() && CentralConstants.TOOL_SIGNATURE_LEADERSELECTION
			.equals(((ToolActivity) activity).getTool().getToolSignature())) {
		    return verifyNextActivityFitsTbl(nextActivity, "Scratchie");
		    
		} else {
		    return verifyNextActivityFitsTbl(nextActivity, "Leaderselection");
		}
		
	    case "Scratchie":
		//the fourth activity shall be Scratchie
		if (activity.isToolActivity() && CentralConstants.TOOL_SIGNATURE_SCRATCHIE
			.equals(((ToolActivity) activity).getTool().getToolSignature())) {
		    return true;
		    
		} else if (nextActivity == null) {
		    return false;
		    
		} else {
		    return verifyNextActivityFitsTbl(nextActivity, "Scratchie");
		}

	    default:
		return false;
	}
    }

    /**
     * Gets users whose progress bars will be displayed in Learner tab in Monitor.
     */
    public ActionForward getLearnerProgressPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "get learner progress page", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String searchPhrase = request.getParameter("searchPhrase");
	Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	if (pageNumber == null) {
	    pageNumber = 1;
	}
	// are the learners sorted by the most completed first?
	boolean isProgressSorted = WebUtil.readBooleanParam(request, "isProgressSorted", false);

	// either sort by name or how much a learner progressed into the lesson
	List<User> learners = isProgressSorted
		? getMonitoringService().getLearnersByMostProgress(lessonId, searchPhrase, 10, (pageNumber - 1) * 10)
		: getLessonService().getLessonLearners(lessonId, searchPhrase, 10, (pageNumber - 1) * 10, true);
	JSONObject responseJSON = new JSONObject();
	for (User learner : learners) {
	    responseJSON.append("learners", WebUtil.userToJSON(learner));
	}

	// get all possible learners matching the given phrase, if any; used for max page number
	responseJSON.put("learnerPossibleNumber", getLessonService().getCountLessonLearners(lessonId, searchPhrase));
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON.toString());
	return null;
    }

    /**
     * Produces data to update Lesson tab in Monitor.
     */
    public ActionForward getLessonDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	if (!getSecurityService().isLessonMonitor(lessonId, user.getUserID(), "get lesson details", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	JSONObject responseJSON = new JSONObject();
	Lesson lesson = getLessonService().getLesson(lessonId);
	LearningDesign learningDesign = lesson.getLearningDesign();

	Locale userLocale = new Locale(user.getLocaleLanguage(), user.getLocaleCountry());

	responseJSON.put(AttributeNames.PARAM_LEARNINGDESIGN_ID, learningDesign.getLearningDesignId());
	responseJSON.put("numberPossibleLearners", getLessonService().getCountLessonLearners(lessonId, null));
	responseJSON.put("lessonStateID", lesson.getLessonStateId());

	responseJSON.put("lessonName", StringEscapeUtils.escapeHtml(lesson.getLessonName()));
	responseJSON.put("lessonDescription", lesson.getLessonDescription());

	Date startOrScheduleDate = lesson.getStartDateTime() == null ? lesson.getScheduleStartDate()
		: lesson.getStartDateTime();
	Date finishDate = lesson.getScheduleEndDate();
	DateFormat indfm = null;

	if ( startOrScheduleDate != null || finishDate != null )
	    indfm = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", userLocale);
	
	if (startOrScheduleDate != null) {
	    Date tzStartDate = DateUtil.convertToTimeZoneFromDefault(user.getTimeZone(), startOrScheduleDate);
	    responseJSON.put("startDate",
		    indfm.format(tzStartDate) + " " + user.getTimeZone().getDisplayName(userLocale));
	}

	if ( finishDate != null ) {
	    Date tzFinishDate = DateUtil.convertToTimeZoneFromDefault(user.getTimeZone(), finishDate);
	    responseJSON.put("finishDate",
		    indfm.format(tzFinishDate) + " " + user.getTimeZone().getDisplayName(userLocale));	    
	}
	
	List<ContributeActivityDTO> contributeActivities = getContributeActivities(lessonId, false);
	if (contributeActivities != null) {
	    Gson gson = new GsonBuilder().create();
	    responseJSON.put("contributeActivities", new JSONArray(gson.toJson(contributeActivities)));
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    public ActionForward getLessonChartData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	Integer possibleLearnersCount = getLessonService().getCountLessonLearners(lessonId, null);
	Integer completedLearnersCount = getMonitoringService().getCountLearnersCompletedLesson(lessonId);
	Integer startedLearnersCount = getLessonService().getCountActiveLessonLearners(lessonId)
		- completedLearnersCount;
	Integer notCompletedLearnersCount = possibleLearnersCount - completedLearnersCount - startedLearnersCount;

	JSONObject responseJSON = new JSONObject();
	JSONObject notStartedJSON = new JSONObject();
	notStartedJSON.put("name", getMessageService().getMessage("lesson.chart.not.completed"));
	notStartedJSON.put("value", Math.round(notCompletedLearnersCount.doubleValue() / possibleLearnersCount * 100));
	responseJSON.append("data", notStartedJSON);

	JSONObject startedJSON = new JSONObject();
	startedJSON.put("name", getMessageService().getMessage("lesson.chart.started"));
	startedJSON.put("value", Math.round((startedLearnersCount.doubleValue()) / possibleLearnersCount * 100));
	responseJSON.append("data", startedJSON);

	JSONObject completedJSON = new JSONObject();
	completedJSON.put("name", getMessageService().getMessage("lesson.chart.completed"));
	completedJSON.put("value", Math.round(completedLearnersCount.doubleValue() / possibleLearnersCount * 100));
	responseJSON.append("data", completedJSON);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Produces data for Sequence tab in Monitor.
     */
    @SuppressWarnings("unchecked")
    public ActionForward getLessonProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer monitorUserId = getUserId();
	if (!getSecurityService().isLessonMonitor(lessonId, monitorUserId, "get lesson progress", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
	Integer searchedLearnerId = WebUtil.readIntParam(request, "searchedLearnerId", true);

	Lesson lesson = getLessonService().getLesson(lessonId);
	LearningDesign learningDesign = lesson.getLearningDesign();
	String contentFolderId = learningDesign.getContentFolderID();

	Set<Activity> activities = new HashSet<Activity>();
	// filter activities that are interesting for further processing
	for (Activity activity : (Set<Activity>) learningDesign.getActivities()) {
	    if (activity.isSequenceActivity()) {
		// skip sequence activities as they are just for grouping
		continue;
	    }
	    // get real activity object, not proxy
	    activities.add(getMonitoringService().getActivityById(activity.getActivityId()));
	}

	JSONObject responseJSON = new JSONObject();
	List<ContributeActivityDTO> contributeActivities = getContributeActivities(lessonId, true);
	if (contributeActivities != null) {
	    Gson gson = new GsonBuilder().create();
	    responseJSON.put("contributeActivities", new JSONArray(gson.toJson(contributeActivities)));
	}

	// check if the searched learner has started the lesson
	LearnerProgress searchedLearnerProgress = null;
	if (searchedLearnerId != null) {
	    searchedLearnerProgress = getLessonService().getUserProgressForLesson(searchedLearnerId, lessonId);
	    responseJSON.put("searchedLearnerFound", searchedLearnerProgress != null);
	}

	// Fetch number of learners at each activity
	ArrayList<Long> activityIds = new ArrayList<Long>();
	Set<Long> leaders = new TreeSet<Long>();
	for (Activity activity : activities) {
	    activityIds.add(activity.getActivityId());
	    // find leaders from Leader Selection Tool
	    if (activity.isToolActivity()) {
		ToolActivity toolActivity = (ToolActivity) activity;
		if (ILamsToolService.LEADER_SELECTION_TOOL_SIGNATURE
			.equals(toolActivity.getTool().getToolSignature())) {
		    leaders.addAll(getToolService().getLeaderUserId(activity.getActivityId()));
		}
	    }
	}

	Map<Long, Integer> learnerCounts = getMonitoringService()
		.getCountLearnersCurrentActivities(activityIds.toArray(new Long[activityIds.size()]));

	JSONArray activitiesJSON = new JSONArray();
	for (Activity activity : activities) {
	    Long activityId = activity.getActivityId();
	    JSONObject activityJSON = new JSONObject();
	    activityJSON.put("id", activityId);
	    activityJSON.put("uiid", activity.getActivityUIID());
	    activityJSON.put("title", activity.getTitle());
	    activityJSON.put("type", activity.getActivityTypeId());

	    Activity parentActivity = activity.getParentActivity();
	    if (activity.isBranchingActivity()) {
		BranchingActivity ba = (BranchingActivity) monitoringService.getActivityById(activity.getActivityId());
		activityJSON.put("x", MonitoringAction.getActivityCoordinate(ba.getStartXcoord()));
		activityJSON.put("y", MonitoringAction.getActivityCoordinate(ba.getStartYcoord()));
	    } else if (activity.isOptionsWithSequencesActivity()) {
		activityJSON.put("x", MonitoringAction
			.getActivityCoordinate(((OptionsWithSequencesActivity) activity).getStartXcoord()));
		activityJSON.put("y", MonitoringAction
			.getActivityCoordinate(((OptionsWithSequencesActivity) activity).getStartYcoord()));
	    } else if ((parentActivity != null) && (parentActivity.isOptionsActivity()
		    || parentActivity.isParallelActivity() || parentActivity.isFloatingActivity())) {
		// Optional Activity children had coordinates relative to parent
		activityJSON.put("x", MonitoringAction.getActivityCoordinate(parentActivity.getXcoord())
			+ MonitoringAction.getActivityCoordinate(activity.getXcoord()));
		activityJSON.put("y", MonitoringAction.getActivityCoordinate(parentActivity.getYcoord())
			+ MonitoringAction.getActivityCoordinate(activity.getYcoord()));
	    } else {
		activityJSON.put("x", MonitoringAction.getActivityCoordinate(activity.getXcoord()));
		activityJSON.put("y", MonitoringAction.getActivityCoordinate(activity.getYcoord()));
	    }

	    String monitorUrl = getMonitoringService().getActivityMonitorURL(lessonId, activityId, contentFolderId,
		    monitorUserId);
	    if (monitorUrl != null && !activity.isBranchingActivity()) {
		// whole activity monitor URL
		activityJSON.put("url", monitorUrl);
	    }

	    // find few latest users and count of all users for each activity
	    int learnerCount = learnerCounts.get(activityId);
	    if (!activity.isBranchingActivity() && !activity.isOptionsWithSequencesActivity()) {
		List<User> latestLearners = getMonitoringService().getLearnersLatestByActivity(activity.getActivityId(),
			MonitoringAction.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT, null);

		// insert leaders as first of learners
		for (Long leaderId : leaders) {
		    for (User learner : latestLearners) {
			if (learner.getUserId().equals(leaderId.intValue())) {
			    latestLearners = MonitoringAction.insertHighlightedLearner(learner, latestLearners,
				    MonitoringAction.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT);
			    break;
			}
		    }
		}

		// insert the searched learner as the first one
		if ((searchedLearnerProgress != null) && (searchedLearnerProgress.getCurrentActivity() != null)
			&& activity.getActivityId()
				.equals(searchedLearnerProgress.getCurrentActivity().getActivityId())) {
		    // put the searched learner in front
		    latestLearners = MonitoringAction.insertHighlightedLearner(searchedLearnerProgress.getUser(),
			    latestLearners, MonitoringAction.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT);
		}

		// parse learners into JSON format
		if (!latestLearners.isEmpty()) {
		    JSONArray learnersJSON = new JSONArray();
		    for (User learner : latestLearners) {
			JSONObject userJSON = WebUtil.userToJSON(learner);
			if (leaders.contains(learner.getUserId().longValue())) {
			    userJSON.put("leader", true);
			}
			learnersJSON.put(userJSON);
		    }

		    activityJSON.put("learners", learnersJSON);
		}
	    }
	    activityJSON.put("learnerCount", learnerCount);

	    activitiesJSON.put(activityJSON);
	}
	responseJSON.put("activities", activitiesJSON);

	// find learners who completed the lesson
	List<User> completedLearners = getMonitoringService().getLearnersLatestCompleted(lessonId,
		MonitoringAction.LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT, null);
	Integer completedLearnerCount = null;
	if (completedLearners.size() < MonitoringAction.LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT) {
	    completedLearnerCount = completedLearners.size();
	} else {
	    completedLearnerCount = getMonitoringService().getCountLearnersCompletedLesson(lessonId);
	}
	responseJSON.put("completedLearnerCount", completedLearnerCount);

	if ((searchedLearnerProgress != null) && searchedLearnerProgress.isComplete()) {
	    // put the searched learner in front
	    completedLearners = MonitoringAction.insertHighlightedLearner(searchedLearnerProgress.getUser(),
		    completedLearners, MonitoringAction.LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT);
	}
	for (User learner : completedLearners) {
	    JSONObject learnerJSON = WebUtil.userToJSON(learner);
	    // no more details are needed for learners who completed the lesson
	    responseJSON.append("completedLearners", learnerJSON);
	}

	responseJSON.put("numberPossibleLearners", getLessonService().getCountLessonLearners(lessonId, null));

	// on first fetch get transitions metadata so Monitoring can set their SVG elems IDs
	if (WebUtil.readBooleanParam(request, "getTransitions", false)) {
	    JSONArray transitions = new JSONArray();
	    for (Transition transition : (Set<Transition>) learningDesign.getTransitions()) {
		JSONObject transitionJSON = new JSONObject();
		transitionJSON.put("uiid", transition.getTransitionUIID());
		transitionJSON.put("fromID", transition.getFromActivity().getActivityId());
		transitionJSON.put("toID", transition.getToActivity().getActivityId());

		transitions.put(transitionJSON);
	    }
	    responseJSON.put("transitions", transitions);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());

	return null;
    }

    /**
     * Gives suggestions when a Monitor searches for Learners and staff members.
     */
    public ActionForward autocomplete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getUserId(), "autocomplete in monitoring", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String searchPhrase = request.getParameter("term");
	boolean isOrganisationSearch = WebUtil.readStrParam(request, "scope").equalsIgnoreCase("organisation");

	Collection<User> users = null;
	if (isOrganisationSearch) {
	    // search for Learners in the organisation
	    Map<User, Boolean> result = getLessonService().getUsersWithLessonParticipation(lessonId, Role.LEARNER,
		    searchPhrase, MonitoringAction.USER_PAGE_SIZE, null, true);
	    users = result.keySet();
	} else {
	    // search for Learners in the lesson
	    users = getLessonService().getLessonLearners(lessonId, searchPhrase, MonitoringAction.USER_PAGE_SIZE, null,
		    true);
	}

	JSONArray responseJSON = new JSONArray();
	for (User user : users) {
	    JSONObject userJSON = new JSONObject();
	    userJSON.put("label", user.getFirstName() + " " + user.getLastName() + " " + user.getLogin());
	    userJSON.put("value", user.getUserId());

	    responseJSON.put(userJSON);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON);
	return null;
    }

    /**
     * Checks if activity A is before activity B in a sequence.
     */
    public ActionForward isActivityPreceding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	long activityAid = WebUtil.readLongParam(request, "activityA");
	long activityBid = WebUtil.readLongParam(request, "activityB");
	boolean result = false;

	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	Activity precedingActivity = monitoringService.getActivityById(activityBid);

	// move back an look for activity A
	while (!result && (precedingActivity != null)) {
	    if (precedingActivity.getTransitionTo() != null) {
		precedingActivity = precedingActivity.getTransitionTo().getFromActivity();
	    } else if (precedingActivity.getParentActivity() != null) {
		// this is a nested activity; move up
		precedingActivity = precedingActivity.getParentActivity();
		continue;
	    } else {
		precedingActivity = null;
	    }

	    if ((precedingActivity != null) && !precedingActivity.isSequenceActivity()) {
		if (precedingActivity.getActivityId().equals(activityAid)) {
		    // found it
		    result = true;
		} else if (precedingActivity.isComplexActivity()) {
		    // check descendants of a complex activity
		    ComplexActivity complexActivity = (ComplexActivity) monitoringService
			    .getActivityById(precedingActivity.getActivityId());
		    if (containsActivity(complexActivity, activityAid, monitoringService)) {
			result = true;
		    }
		}
	    }
	}

	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write(Boolean.toString(result));
	return null;
    }

    /**
     * Checks if a complex activity or its descendats contain an activity with the given ID.
     */
    @SuppressWarnings("unchecked")
    private boolean containsActivity(ComplexActivity complexActivity, long targetActivityId,
	    IMonitoringService monitoringService) {
	for (Activity childActivity : (Set<Activity>) complexActivity.getActivities()) {
	    if (childActivity.getActivityId().equals(targetActivityId)) {
		return true;
	    }
	    if (childActivity.isComplexActivity()) {
		ComplexActivity childComplexActivity = (ComplexActivity) monitoringService
			.getActivityById(childActivity.getActivityId());
		if (containsActivity(childComplexActivity, targetActivityId, monitoringService)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public ActionForward startPreviewLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	Integer userID = getUserId();
	long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	try {
	    getMonitoringService().createPreviewClassForLesson(userID, lessonID);
	    getMonitoringService().startLesson(lessonID, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
	}
	return null;
    }

    public ActionForward startLiveEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws LearningDesignException, UserException, IOException {
	long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	LearningDesign learningDesign = (LearningDesign) getUserManagementService().findById(LearningDesign.class,
		learningDesignId);
	if (learningDesign.getLessons().isEmpty()) {
	    throw new InvalidParameterException(
		    "There are no lessons associated with learning design: " + learningDesignId);
	}
	Integer organisationID = ((Lesson) learningDesign.getLessons().iterator().next()).getOrganisation()
		.getOrganisationId();
	Integer userID = getUserId();
	if (!getSecurityService().hasOrgRole(organisationID, userID, new String[] { Role.AUTHOR }, "start live edit",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an author in the organisation");
	    return null;
	}

	IAuthoringService authoringService = MonitoringServiceProxy
		.getAuthoringService(getServlet().getServletContext());

	if (authoringService.setupEditOnFlyLock(learningDesignId, userID)) {
	    authoringService.setupEditOnFlyGate(learningDesignId, userID);
	} else {
	    response.getWriter().write("Someone else is editing the design at the moment.");
	}

	return null;
    }

    private ILogEventService getLogEventService() {
	if (MonitoringAction.logEventService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.logEventService = (ILogEventService) ctx.getBean("logEventService");
	}
	return MonitoringAction.logEventService;
    }

    private ILessonService getLessonService() {
	if (MonitoringAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return MonitoringAction.lessonService;
    }

    private IMonitoringService getMonitoringService() {
	if (MonitoringAction.monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.monitoringService = (IMonitoringService) ctx.getBean("monitoringService");
	}
	return MonitoringAction.monitoringService;
    }

    private ISecurityService getSecurityService() {
	if (MonitoringAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return MonitoringAction.securityService;
    }

    private IUserManagementService getUserManagementService() {
	if (MonitoringAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return MonitoringAction.userManagementService;
    }

    private ILearnerService getLearnerService() {
	if (MonitoringAction.learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.learnerService = (ILearnerService) ctx.getBean("learnerService");
	}
	return MonitoringAction.learnerService;
    }

    private MessageService getMessageService() {
	if (MonitoringAction.messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.messageService = (MessageService) ctx.getBean("monitoringMessageService");
	}
	return MonitoringAction.messageService;
    }

    private ILamsToolService getToolService() {
	if (MonitoringAction.toolService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.toolService = (ILamsToolService) ctx.getBean("lamsToolService");
	}
	return MonitoringAction.toolService;
    }

    /**
     * Set whether or not the presence available button is available in learner. Expects parameters lessonID and
     * presenceAvailable.
     */
    public ActionForward presenceAvailable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceAvailable = WebUtil.readBooleanParam(request, "presenceAvailable", false);

	try {
	    getMonitoringService().togglePresenceAvailable(lessonID, userID, presenceAvailable);

	    if (!presenceAvailable) {
		getMonitoringService().togglePresenceImAvailable(lessonID, userID, false);
	    }
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * Set whether or not the presence available button is available in learner. Expects parameters lessonID and
     * presenceImAvailable.
     */
    public ActionForward presenceImAvailable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceImAvailable = WebUtil.readBooleanParam(request, "presenceImAvailable", false);

	try {
	    getMonitoringService().togglePresenceImAvailable(lessonID, userID, presenceImAvailable);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }
    
    /**
     * Set whether or not the activity scores / gradebook values are shown to the learner at the end of the lesson. 
     * Expects parameters lessonID and presenceAvailable.
     */
    public ActionForward gradebookOnComplete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean gradebookOnComplete = WebUtil.readBooleanParam(request, "gradebookOnComplete", false);

	try {
	    getMonitoringService().toggleGradebookOnComplete(lessonID, userID, gradebookOnComplete);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /** Open Time Chart display */
    public ActionForward viewTimeChart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	try {

	    long lessonID = WebUtil.readLongParam(request, "lessonID");

	    // check monitor privledges
	    if (!getSecurityService().isLessonMonitor(lessonID, getUserId(), "open time chart", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    request.setAttribute("lessonID", lessonID);
	    request.setAttribute("learnerID", WebUtil.readLongParam(request, "learnerID", true));

	} catch (Exception e) {
	    request.setAttribute("errorName", "MonitoringAction");
	    request.setAttribute("errorMessage", e.getMessage());

	    return mapping.findForward(MonitoringAction.ERROR);
	}

	return mapping.findForward(MonitoringAction.TIME_CHART_SCREEN);
    }

    /**
     * Creates a list of users out of string with comma-delimited user IDs.
     */
    private List<User> parseUserList(HttpServletRequest request, String paramName, Collection<User> users) {
	String userIdList = request.getParameter(paramName);
	String[] userIdArray = userIdList.split(",");
	List<User> result = new ArrayList<User>(userIdArray.length);

	for (User user : users) {
	    Integer userId = user.getUserId();
	    for (String includeId : userIdArray) {
		if (userId.toString().equals(includeId)) {
		    result.add(user);
		    break;
		}
	    }
	}

	return result;
    }

    @SuppressWarnings("unchecked")
    private List<ContributeActivityDTO> getContributeActivities(Long lessonId, boolean skipCompletedBranching) {
	List<ContributeActivityDTO> contributeActivities = getMonitoringService().getAllContributeActivityDTO(lessonId);
	Lesson lesson = getLessonService().getLesson(lessonId);

	if (contributeActivities != null) {
	    List<ContributeActivityDTO> resultContributeActivities = new ArrayList<ContributeActivityDTO>();
	    for (ContributeActivityDTO contributeActivity : contributeActivities) {
		if (contributeActivity.getContributeEntries() != null) {
		    Iterator<ContributeActivityDTO.ContributeEntry> entryIterator = contributeActivity
			    .getContributeEntries().iterator();
		    while (entryIterator.hasNext()) {
			ContributeActivityDTO.ContributeEntry contributeEntry = entryIterator.next();

			// extra filtering for chosen branching: do not show in Sequence tab if all users were assigned
			if (skipCompletedBranching
				&& ContributionTypes.CHOSEN_BRANCHING.equals(contributeEntry.getContributionType())) {
			    Set<User> learners = new HashSet<User>(lesson.getLessonClass().getLearners());
			    ChosenBranchingActivity branching = (ChosenBranchingActivity) getMonitoringService()
				    .getActivityById(contributeActivity.getActivityID());
			    for (SequenceActivity branch : (Set<SequenceActivity>) branching.getActivities()) {
				Group group = branch.getSoleGroupForBranch();
				if (group != null) {
				    learners.removeAll(group.getUsers());
				}
			    }
			    contributeEntry.setIsComplete(learners.isEmpty());
			}

			if (!contributeEntry.getIsRequired() || contributeEntry.getIsComplete()) {
			    entryIterator.remove();
			}
		    }

		    if (!contributeActivity.getContributeEntries().isEmpty()) {
			resultContributeActivities.add(contributeActivity);
		    }
		}
	    }
	    return resultContributeActivities;
	}
	return null;
    }

    private static int getActivityCoordinate(Integer coord) {
	return (coord == null) || (coord < 0) ? ObjectExtractor.DEFAULT_COORD : coord;
    }

    /**
     * Puts the searched learner in front of other learners in the list.
     */
    private static List<User> insertHighlightedLearner(User searchedLearner, List<User> latestLearners, int limit) {
	latestLearners.remove(searchedLearner);
	LinkedList<User> updatedLatestLearners = new LinkedList<User>(latestLearners);
	updatedLatestLearners.addFirst(searchedLearner);
	if (updatedLatestLearners.size() > limit) {
	    updatedLatestLearners.removeLast();
	}
	return updatedLatestLearners;
    }
}