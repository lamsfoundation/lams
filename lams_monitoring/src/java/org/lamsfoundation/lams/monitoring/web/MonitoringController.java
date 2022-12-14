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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.IAuthoringService;
import org.lamsfoundation.lams.flux.FluxMap;
import org.lamsfoundation.lams.flux.FluxRegistry;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenBranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.ActivityTimeLimitDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LearnerActivityCompleteFluxItem;
import org.lamsfoundation.lams.lesson.util.LearnerLessonJoinFluxItem;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.service.ICommonScratchieService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;

/**
 * The action servlet that provide all the monitoring functionalities. It interact with the teacher via JSP monitoring
 * interface.
 *
 * @author Jacky Fang
 */
@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    private static Logger log = Logger.getLogger(MonitoringController.class);

    private static final DateFormat LESSON_SCHEDULING_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final int LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT = 53;
    private static final int LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT = 7;
    private static final int USER_PAGE_SIZE = 10;

    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IActivityDAO activityDAO;
    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILearnerService learnerService;
    @Autowired
    private ILamsToolService lamsToolService;
    @Autowired
    @Qualifier("monitoringMessageService")
    private MessageService messageService;
    @Autowired
    private IAuthoringService authoringService;
    @Autowired
    @Qualifier("scratchieService")
    private ICommonScratchieService commonScratchieService;

    public MonitoringController() {
	// bind sinks so a learner finishing an activity also triggers an update in lesson progress
	FluxRegistry.bindSink(CommonConstants.ACTIVITY_ENTERED_SINK_NAME, CommonConstants.LESSON_PROGRESSED_SINK_NAME,
		learnerProgressFluxItem -> ((LearnerActivityCompleteFluxItem) learnerProgressFluxItem).getLessonId());
	// bind sinks so a learner entering a lesson also triggers an update in lesson progress
	FluxRegistry.bindSink(CommonConstants.LESSON_JOINED_SINK_NAME, CommonConstants.LESSON_PROGRESSED_SINK_NAME,
		lessonJoinedFluxItem -> ((LearnerLessonJoinFluxItem) lessonJoinedFluxItem).getLessonId());

	FluxRegistry.initFluxMap(MonitoringConstants.CANVAS_REFRESH_FLUX_NAME,
		CommonConstants.LESSON_PROGRESSED_SINK_NAME, null, lessonId -> "doRefresh", FluxMap.STANDARD_THROTTLE,
		FluxMap.STANDARD_TIMEOUT);
	FluxRegistry.initFluxMap(MonitoringConstants.GRADEBOOK_REFRESH_FLUX_NAME,
		CommonConstants.LESSON_PROGRESSED_SINK_NAME, null, lessonId -> "doRefresh", FluxMap.STANDARD_THROTTLE,
		FluxMap.STANDARD_TIMEOUT);
	FluxRegistry.initFluxMap(MonitoringConstants.TIME_LIMIT_REFRESH_FLUX_NAME,
		CommonConstants.ACTIVITY_TIME_LIMIT_CHANGED_SINK_NAME,
		(Collection<Long> key, Collection<Long> item) -> key.containsAll(item), toolContentIds -> "doRefresh",
		FluxMap.SHORT_THROTTLE, FluxMap.STANDARD_TIMEOUT);
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private String redirectToURL(HttpServletResponse response, String url) throws IOException {
	if (url == null) {
	    return "notsupported";
	}

	String fullURL = WebUtil.convertToFullURL(url);
	response.sendRedirect(response.encodeRedirectURL(fullURL));
	return null;
    }

    @RequestMapping(path = "/getLearnerProgressUpdateFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getLearnerProgressUpdateFlux(@RequestParam long lessonId)
	    throws JsonProcessingException, IOException {
	return FluxRegistry.get(MonitoringConstants.CANVAS_REFRESH_FLUX_NAME, lessonId);
    }

    @RequestMapping(path = "/getGradebookUpdateFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getGradebookUpdateFlux(@RequestParam long lessonId)
	    throws JsonProcessingException, IOException {
	return FluxRegistry.get(MonitoringConstants.GRADEBOOK_REFRESH_FLUX_NAME, lessonId);
    }

    @RequestMapping(path = "/getTimeLimitUpdateFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getTimeLimitUpdateFlux(@RequestParam Set<Long> toolContentIds)
	    throws JsonProcessingException, IOException {
	return FluxRegistry.get(MonitoringConstants.TIME_LIMIT_REFRESH_FLUX_NAME, toolContentIds);
    }

    /**
     * Initializes a lesson for specific learning design with the given lesson title and lesson description. If
     * initialization is successful, this method will the ID of new lesson.
     *
     * Currently used only in TestHarness and Authoring Preview.
     */
    @RequestMapping("/initializeLesson")
    @ResponseBody
    public String initializeLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

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
	    newLesson = monitoringService.initializeLessonForPreview(title, desc, ldId, getUserId(), customCSV,
		    learnerPresenceAvailable, learnerImAvailable, liveEditEnabled);
	} else {
	    try {
		newLesson = monitoringService.initializeLesson(title, desc, ldId, organisationId, getUserId(),
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
     */
    @RequestMapping(path = "/startLesson", method = RequestMethod.POST)
    @ResponseBody
    public String startLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.startLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write("true");
	return null;
    }

    /**
     * Renames lesson. Invoked by Ajax call from general LAMS monitoring.
     */
    @RequestMapping(path = "/renameLesson", method = RequestMethod.POST)
    @ResponseBody
    public String renameLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, "pk");

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (!securityService.isLessonMonitor(lessonId, user.getUserID(), "rename lesson")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String newLessonName = request.getParameter("value");
	if (StringUtils.isBlank(newLessonName)) {
	    return null;
	}

	Lesson lesson = lessonService.getLesson(lessonId);
	lesson.setLessonName(newLessonName);
	userManagementService.save(lesson);

	ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
	jsonObject.put("successful", true);
	response.setContentType("application/json;charset=utf-8");
	return jsonObject.toString();
    }

    @RequestMapping("/createLessonClass")
    public String createLessonClass(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	List<User> allUsers = userManagementService.getUsersFromOrganisation(organisationId);
	String learnerGroupName = organisation.getName() + " learners";
	String staffGroupName = organisation.getName() + " staff";
	List<User> learners = parseUserList(request, "learners", allUsers);
	List<User> staff = parseUserList(request, "monitors", allUsers);

	try {
	    monitoringService.createLessonClassForLesson(lessonId, organisation, learnerGroupName, learners,
		    staffGroupName, staff, userID);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
	    return null;
	}

	return null;
    }

    @RequestMapping(path = "/addLesson", method = RequestMethod.POST)
    public String addLesson(HttpServletRequest request, HttpServletResponse response, @RequestParam String lessonName,
	    @RequestParam long learningDesignID) throws IOException, ServletException, ParseException {
	if (!ValidationUtil.isOrgNameValid(lessonName)) {
	    throw new IOException("Lesson name contains invalid characters");
	}

	String[] organisationIdsStr = request.getParameterValues(AttributeNames.PARAM_ORGANISATION_ID);
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
	Date schedulingDatetime = null;
	Date schedulingEndDatetime = null;
	if (schedulingEnable) {
	    String dateString = request.getParameter("schedulingDatetime");
	    if (dateString != null && dateString.length() > 0) {
		schedulingDatetime = MonitoringController.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateString);
	    }
	    dateString = request.getParameter("schedulingEndDatetime");
	    if (dateString != null && dateString.length() > 0) {
		schedulingEndDatetime = MonitoringController.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateString);
	    }
	}
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

	Integer userId = getUserId();
	User creator = (User) userManagementService.findById(User.class, userId);

	for (String organisationIdStr : organisationIdsStr) {
	    Integer organisationId = Integer.parseInt(organisationIdStr);
	    Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		    organisationId);
	    List<User> organisationUsers = userManagementService.getUsersFromOrganisation(organisationId);
	    List<User> learners = parseUserList(request, "learners", organisationUsers);
	    String learnerGroupName = organisation.getName() + " learners";

	    List<User> staff = parseUserList(request, "monitors", organisationUsers);
	    // add the creator as staff, if not already done
	    if (!staff.contains(creator)) {
		staff.add(creator);
	    }
	    String staffGroupName = organisation.getName() + " staff";

	    // either all users participate in a lesson, or we split them among instances
	    List<User> lessonInstanceLearners = splitNumberLessons == null ? learners
		    : new ArrayList<>((learners.size() / splitNumberLessons) + 1);
	    for (int lessonIndex = 1; lessonIndex <= (splitNumberLessons == null ? 1
		    : splitNumberLessons); lessonIndex++) {
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

		if (log.isDebugEnabled()) {
		    log.debug("Creating lesson "
			    + (splitNumberLessons == null ? "" : "(" + lessonIndex + "/" + splitNumberLessons + ") ")
			    + "\"" + lessonInstanceName + "\"");
		}

		Lesson lesson = null;
		try {
		    lesson = monitoringService.initializeLesson(lessonInstanceName, introDescription, learningDesignID,
			    organisationId, userId, null, introEnable, introImage, presenceEnable, imEnable,
			    enableLiveEdit, notificationsEnable, forceRestart, allowRestart, gradebookOnComplete,
			    timeLimitIndividual, precedingLessonId);

		    monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
			    learnerGroupInstanceName, lessonInstanceLearners, staffGroupInstanceName, staff, userId);
		} catch (SecurityException e) {
		    try {
			response.sendError(HttpServletResponse.SC_FORBIDDEN,
				"User is not a monitor in the organisation or lesson");
		    } catch (IllegalStateException e1) {
			log.warn("Tried to tell user that \"User is not a monitor in the organisation or lesson\","
				+ "but the HTTP response was already written, probably by some other error");
		    }
		    return null;
		}

		if (!startMonitor) {
		    try {
			if (schedulingDatetime == null) {
			    monitoringService.startLesson(lesson.getLessonId(), userId);
			} else {
			    // if lesson should start in few days, set it here
			    monitoringService.startLessonOnSchedule(lesson.getLessonId(), schedulingDatetime, userId);
			}

			// monitor has given an end date/time for the lesson
			if (schedulingEndDatetime != null) {
			    monitoringService.finishLessonOnSchedule(lesson.getLessonId(), schedulingEndDatetime,
				    userId);
			    // if lesson should finish in few days, set it here
			} else if (timeLimitLesson != null) {
			    monitoringService.finishLessonOnSchedule(lesson.getLessonId(), timeLimitLesson, userId);
			}

		    } catch (SecurityException e) {
			try {
			    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
			} catch (IllegalStateException e1) {
			    log.warn("Tried to tell user that \"User is not a monitor in the lesson\","
				    + "but the HTTP response was already written, probably by some other error");
			}
			return null;
		    }
		}
	    }
	}

	return null;
    }

    /**
     * Adds all course learners to the given lesson.
     */
    @RequestMapping("/addAllOrganisationLearnersToLesson")
    public void addAllOrganisationLearnersToLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getUserId(), "add all lesson learners to lesson")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return;
	}
	Lesson lesson = lessonService.getLesson(lessonId);
	Vector<User> learners = userManagementService
		.getUsersFromOrganisationByRole(lesson.getOrganisation().getOrganisationId(), Role.LEARNER, true);
	lessonService.addLearners(lesson, learners);
    }

    @RequestMapping("/startOnScheduleLesson")
    public void startOnScheduleLesson(HttpServletRequest request, HttpServletResponse response)
	    throws ParseException, IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_START_DATE);
	Date startDate = MonitoringController.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateStr);
	try {
	    monitoringService.startLessonOnSchedule(lessonId, startDate, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
    }

    /**
     * The Struts dispatch method to archive a lesson.
     */
    @RequestMapping(path = "/archiveLesson", method = RequestMethod.POST)
    public void archiveLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.archiveLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
    }

    /**
     * The Struts dispatch method to "unarchive" a lesson. Returns it back to its previous state.
     */
    @RequestMapping(path = "/unarchiveLesson", method = RequestMethod.POST)
    public void unarchiveLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.unarchiveLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
    }

    /**
     * The purpose of suspending is to hide the lesson from learners temporarily. It doesn't make any sense to suspend a
     * created or a not started (ie scheduled) lesson as they will not be shown on the learner interface anyway! If the
     * teacher tries to suspend a lesson that is not in the STARTED_STATE, then an error should be returned to UI.
     */
    @RequestMapping(path = "/suspendLesson", method = RequestMethod.POST)
    public void suspendLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, ParseException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_END_DATE, true);
	try {
	    if (dateStr == null || dateStr.length() == 0) {
		monitoringService.suspendLesson(lessonId, getUserId(), true);
	    } else {
		monitoringService.finishLessonOnSchedule(lessonId,
			MonitoringController.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateStr), getUserId());
	    }
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
    }

    /**
     * Unsuspend a lesson which state must be Lesson.SUPSENDED_STATE. Otherwise a error message will return to UI
     * client.
     */
    @RequestMapping(path = "/unsuspendLesson", method = RequestMethod.POST)
    public void unsuspendLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.unsuspendLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
    }

    /**
     * <P>
     * The STRUTS action will send back a JSON message after marking the lesson by the given lesson ID as
     * <code>Lesson.REMOVED_STATE</code> status.
     * </P>
     * <P>
     * This action need a lession ID as input.
     * </P>
     */
    @RequestMapping(path = "/removeLesson", method = RequestMethod.POST)
    @ResponseBody
    public String removeLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer userId = getUserId();
	boolean permanently = WebUtil.readBooleanParam(request, "permanently", false);

	if (permanently) {
	    monitoringService.removeLessonPermanently(lessonId, userId);
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().print("OK");
	    return null;
	}

	ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();

	try {
	    // if this method throws an Exception, there will be no removeLesson=true in the JSON reply
	    monitoringService.removeLesson(lessonId, userId);
	    jsonObject.put("removeLesson", true);

	} catch (Exception e) {
	    String[] msg = new String[1];
	    msg[0] = e.getMessage();
	    jsonObject.put("removeLesson", messageService.getMessage("error.system.error", msg));
	}

	response.setContentType("application/json;charset=utf-8");
	return jsonObject.toString();
    }

    /**
     * <P>
     * This action need a lession ID, Learner ID and Activity ID as input. Activity ID is optional, if it is null, all
     * activities for this learner will complete to as end as possible.
     * </P>
     */
    @RequestMapping(path = "/forceComplete", method = RequestMethod.POST)
    @ResponseBody
    public void forceComplete(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

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
	String learnerIDsParam = request.getParameter(MonitoringConstants.PARAM_LEARNER_ID);
	// are we moving selected learners or all of learners who are currently in the activity
	Long moveAllFromActivityId = WebUtil.readLongParam(request, "moveAllFromActivityID", true);
	Integer requesterId = getUserId();
	boolean removeLearnerContent = WebUtil.readBooleanParam(request,
		MonitoringConstants.PARAM_REMOVE_LEARNER_CONTENT, false);

	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("message", messageService.getMessage("force.complete.learner.command.message"));
	jsonCommand.put("redirectURL", "/lams/learning/learner/joinLesson.do?lessonID=" + lessonId);
	String command = jsonCommand.toString();

	String activityDescription = null;
	if (activityId != null) {
	    Activity activity = monitoringService.getActivityById(activityId);
	    activityDescription = new StringBuffer(activity.getTitle() == null ? "" : activity.getTitle()).append(" (")
		    .append(activityId).append(")").toString();
	}

	List<User> learners = null;
	if (moveAllFromActivityId == null) {
	    learners = new LinkedList<>();
	    for (String learnerIDString : learnerIDsParam.split(",")) {
		Integer learnerID = Integer.valueOf(learnerIDString);
		User learner = (User) userManagementService.findById(User.class, learnerID);
		learners.add(learner);
	    }
	} else {
	    learners = monitoringService.getLearnersByActivities(new Long[] { moveAllFromActivityId }, null, null,
		    true);
	}

	String message = null;
	StringBuilder learnerIdNameBuilder = new StringBuilder();

	try {
	    for (User learner : learners) {
		message = monitoringService.forceCompleteActivitiesByUser(learner.getUserId(), requesterId, lessonId,
			activityId, removeLearnerContent);
		learnerService.createCommandForLearner(lessonId, learner.getLogin(), command);
		learnerIdNameBuilder.append(learner.getLogin()).append(" (").append(learner.getUserId()).append(")")
			.append(", ");
	    }
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return;
	}

	String learnerIdNameString = learnerIdNameBuilder.substring(0, learnerIdNameBuilder.length() - 2);
	if (log.isDebugEnabled()) {
	    log.debug("Force complete for learners " + learnerIdNameString.toString() + " lesson " + lessonId + ". "
		    + message);
	}

	// audit log force completion attempt
	String messageKey = (activityId == null) ? "audit.force.complete.end.lesson" : "audit.force.complete";

	Object[] args = new Object[] { learnerIdNameString, activityDescription, lessonId };
	String auditMessage = messageService.getMessage(messageKey, args);
	logEventService.logEvent(LogEvent.TYPE_FORCE_COMPLETE, requesterId, null, lessonId, activityId,
		auditMessage + " " + message);

	PrintWriter writer = response.getWriter();
	writer.println(message);
    }

    /**
     * Get learners who are part of the lesson class.
     */
    @RequestMapping("/getLessonLearners")
    @ResponseBody
    public String getLessonLearners(HttpServletRequest request, HttpServletResponse response) throws IOException {

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getUserId(), "get lesson learners")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String searchPhrase = request.getParameter("searchPhrase");
	Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	if (pageNumber == null) {
	    pageNumber = 1;
	}
	boolean orderAscending = WebUtil.readBooleanParam(request, "orderAscending", true);

	List<User> learners = lessonService.getLessonLearners(lessonId, searchPhrase,
		MonitoringController.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringController.USER_PAGE_SIZE,
		orderAscending);
	ArrayNode learnersJSON = JsonNodeFactory.instance.arrayNode();
	for (User learner : learners) {
	    learnersJSON.add(WebUtil.userToJSON(learner));
	}

	Integer learnerCount = lessonService.getCountLessonLearners(lessonId, searchPhrase);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.set("learners", learnersJSON);
	responseJSON.put("learnerCount", learnerCount);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Gets learners or monitors of the lesson and organisation containing it.
     */
    @RequestMapping("/getClassMembers")
    @ResponseBody
    public String getClassMembers(HttpServletRequest request, HttpServletResponse response) throws IOException {

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getUserId(), "get class members")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Lesson lesson = lessonService.getLesson(lessonId);
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
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();

	// find organisation users and whether they participate in the current lesson
	Map<User, Boolean> users = lessonService.getUsersWithLessonParticipation(lessonId, role, searchPhrase,
		MonitoringController.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringController.USER_PAGE_SIZE, true,
		orderAscending);

	// if the result is less then page size, then no need for full check of user count
	Integer userCount = users.size() < MonitoringController.USER_PAGE_SIZE ? users.size()
		: userManagementService.getCountRoleForOrg(lesson.getOrganisation().getOrganisationId(),
			isMonitor ? Role.ROLE_MONITOR : Role.ROLE_LEARNER, searchPhrase);

	responseJSON.put("userCount", userCount);

	ArrayNode usersJSON = JsonNodeFactory.instance.arrayNode();
	for (Entry<User, Boolean> userEntry : users.entrySet()) {
	    User user = userEntry.getKey();
	    ObjectNode userJSON = WebUtil.userToJSON(user);
	    userJSON.put("classMember", userEntry.getValue());
	    // teacher can't remove lesson creator and himself from the lesson staff
	    if (isMonitor && (creator.getUserId().equals(user.getUserId()) || currentUserId.equals(user.getUserId()))) {
		userJSON.put("readonly", true);
	    }
	    usersJSON.add(userJSON);
	}
	responseJSON.set("users", usersJSON);

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Gets users in JSON format who are at the given activity at the moment or finished the given lesson.
     */
    @RequestMapping("/getCurrentLearners")
    @ResponseBody
    public String getCurrentLearners(HttpServletRequest request, HttpServletResponse response) throws IOException {
	ArrayNode learnersJSON = JsonNodeFactory.instance.arrayNode();
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
	    if (!securityService.isLessonMonitor(lessonId, getUserId(), "get lesson completed learners")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    List<User> learners = monitoringService.getUsersCompletedLesson(lessonId,
		    MonitoringController.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringController.USER_PAGE_SIZE,
		    orderAscending);
	    for (User learner : learners) {
		learnersJSON.add(WebUtil.userToJSON(learner));
	    }

	    learnerCount = monitoringService.getCountLearnersCompletedLesson(lessonId);
	} else {
	    Activity activity = monitoringService.getActivityById(activityId);
	    Lesson lesson = activity.getLearningDesign().getLessons().iterator().next();
	    if (!securityService.isLessonMonitor(lesson.getLessonId(), getUserId(), "get activity learners")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    Set<Long> activities = new TreeSet<>();
	    activities.add(activityId);

	    List<User> learners = monitoringService.getLearnersByActivities(activities.toArray(new Long[] {}),
		    MonitoringController.USER_PAGE_SIZE, (pageNumber - 1) * MonitoringController.USER_PAGE_SIZE,
		    orderAscending);
	    for (User learner : learners) {
		learnersJSON.add(WebUtil.userToJSON(learner, activity));
	    }
	    learnerCount = monitoringService.getCountLearnersCurrentActivities(new Long[] { activityId })
		    .get(activityId);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.set("learners", learnersJSON);
	responseJSON.put("learnerCount", learnerCount);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Adds/removes learners and monitors to/from lesson class.
     */
    @RequestMapping(path = "/updateLessonClass", method = RequestMethod.POST)
    public void updateLessonClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getUserId(), "update lesson class")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return;
	}

	int userId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE);
	boolean add = WebUtil.readBooleanParam(request, "add");
	boolean result = false;

	if (role.equals(Role.MONITOR)) {
	    if (add) {
		result = lessonService.addStaffMember(lessonId, userId);
	    } else {
		result = lessonService.removeStaffMember(lessonId, userId);
	    }
	} else if (role.equals(Role.LEARNER)) {
	    if (add) {
		result = lessonService.addLearner(lessonId, userId);
	    } else {
		lessonService.removeLearnerProgress(lessonId, userId);
		result = lessonService.removeLearner(lessonId, userId);
	    }
	}

	if (result) {
	    log.info((add ? "Added a " : "Removed a ") + role + " with ID " + userId + (add ? " to" : " from")
		    + " lesson " + lessonId);
	} else {
	    log.warn("Failed when trying to " + (add ? "add a " : "remove a ") + role + " with ID " + userId
		    + (add ? " to" : " from") + " lesson " + lessonId);
	}
    }

    @RequestMapping("/getDictionaryXML")
    public String getDictionaryXML(HttpServletRequest request, HttpServletResponse response) throws IOException {

	String module = WebUtil.readStrParam(request, "module", false);

	ArrayList<String> languageCollection = new ArrayList<>();
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
    @RequestMapping("/getLearnerActivityURL")
    public String getLearnerActivityURL(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, LamsToolServiceException {

	Integer learnerUserID = new Integer(WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID));
	Long activityID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	try {
	    String url = monitoringService.getLearnerActivityURL(lessonID, activityID, learnerUserID, getUserId());
	    return redirectToURL(response, url);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
    }

    /** Calls the server to bring up the activity's monitoring page. Assumes destination is a new window */
    @RequestMapping("/getActivityMonitorURL")
    public String getActivityMonitorURL(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, LamsToolServiceException {
	Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	String contentFolderID = WebUtil.readStrParam(request, "contentFolderID");
	try {
	    String url = monitoringService.getActivityMonitorURL(lessonID, activityID, contentFolderID, getUserId());
	    return redirectToURL(response, url);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
    }

    /**
     * Displays Monitor Lesson page.
     */
    @RequestMapping("/monitorLesson")
    public String monitorLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	LessonDetailsDTO lessonDTO = lessonService.getLessonDetails(lessonId);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (lessonDTO.getCreateDateTime() != null) {
	    DateFormat sfm = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    lessonDTO.setCreateDateTimeStr(sfm.format(lessonDTO.getCreateDateTime()));
	}
	// prepare encoded lessonId for shortened learner URL
	lessonDTO.setEncodedLessonID(WebUtil.encodeLessonId(lessonId));

	if (!securityService.isLessonMonitor(lessonId, user.getUserID(), "monitor lesson")) {
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

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		lessonDTO.getOrganisationID());
	request.setAttribute("notificationsAvailable", organisation.getEnableCourseNotifications());
	boolean enableLiveEdit = organisation.getEnableLiveEdit()
		&& userManagementService.isUserInRole(user.getUserID(), organisation.getOrganisationId(), Role.AUTHOR);
	request.setAttribute("enableLiveEdit", enableLiveEdit);
	request.setAttribute("lesson", lessonDTO);
	boolean isTBLSequence = learningDesignService.isTBLSequence(lessonDTO.getLearningDesignID());
	request.setAttribute("isTBLSequence", isTBLSequence);
	if (isTBLSequence) {
	    List<Activity> lessonActivities = getLessonActivities(lessonService.getLesson(lessonId));
	    TblMonitoringController.setupAvailableActivityTypes(request, lessonActivities);

	    boolean burningQuestionsEnabled = false;
	    Long traToolActivityId = (Long) request.getAttribute("traToolActivityId");
	    if (traToolActivityId != null) {
		long traToolContentId = activityDAO.find(ToolActivity.class, traToolActivityId).getToolContentId();
		burningQuestionsEnabled = commonScratchieService.isBurningQuestionsEnabled(traToolContentId);
	    }
	    request.setAttribute("burningQuestionsEnabled", burningQuestionsEnabled);
	}

	return "monitor";
    }

    @RequestMapping("/displaySequenceTab")
    public String displaySequenceTab() {
	return "monitor-sequence-tab";
    }

    @RequestMapping("/displayLearnersTab")
    public String displayLearnersTab() {
	return "monitor-learners-tab";
    }

    @RequestMapping("/displayGradebookTab")
    public String displayGradebookTab() {
	return "monitor-gradebook-tab";
    }

    /**
     * Gets users whose progress bars will be displayed in Learner tab in Monitor.
     */
    @RequestMapping("/getLearnerProgressPage")
    @ResponseBody
    public String getLearnerProgressPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getUserId(), "get learner progress page")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	Integer searchedLearnerId = WebUtil.readIntParam(request, "searchedLearnerID", true);
	if (searchedLearnerId == null) {
	    Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	    if (pageNumber == null || pageNumber < 1) {
		pageNumber = 1;
	    }
	    // are the learners sorted by the most completed first?
	    boolean isProgressSorted = WebUtil.readBooleanParam(request, "isProgressSorted", false);

	    // either sort by name or how much a learner progressed into the lesson
	    List<User> learners = isProgressSorted
		    ? monitoringService.getLearnersByMostProgress(lessonId, null, 10, (pageNumber - 1) * 10)
		    : lessonService.getLessonLearners(lessonId, null, 10, (pageNumber - 1) * 10, true);

	    for (int i = 0; i < 5; i++) {
		for (User learner : learners) {
		    responseJSON.withArray("learners").add(WebUtil.userToJSON(learner));
		}
	    }

	    // get all possible learners matching the given phrase, if any; used for max page number
	    responseJSON.put("learnerPossibleNumber", lessonService.getCountLessonLearners(lessonId, null) * 5);
	} else {
	    // only one learner is searched
	    User learner = userManagementService.getUserById(searchedLearnerId);
	    responseJSON.withArray("learners").add(WebUtil.userToJSON(learner));
	    responseJSON.put("learnerPossibleNumber", 1);
	}
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Produces data to update Lesson tab in Monitor.
     */
    @RequestMapping("/getLessonDetails")
    @ResponseBody
    public String getLessonDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	if (!securityService.isLessonMonitor(lessonId, user.getUserID(), "get lesson details")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	Lesson lesson = lessonService.getLesson(lessonId);
	LearningDesign learningDesign = lesson.getLearningDesign();

	Locale userLocale = new Locale(user.getLocaleLanguage(), user.getLocaleCountry());

	responseJSON.put(AttributeNames.PARAM_LEARNINGDESIGN_ID, learningDesign.getLearningDesignId());
	responseJSON.put("numberPossibleLearners", lessonService.getCountLessonLearners(lessonId, null));
	responseJSON.put("lessonStateID", lesson.getLessonStateId());

	responseJSON.put("lessonName", HtmlUtils.htmlEscape(lesson.getLessonName()));
	responseJSON.put("lessonInstructions", learningDesign.getDescription());

	Date startOrScheduleDate = lesson.getStartDateTime() == null ? lesson.getScheduleStartDate()
		: lesson.getStartDateTime();
	Date finishDate = lesson.getScheduleEndDate();
	DateFormat indfm = null;

	if (startOrScheduleDate != null || finishDate != null) {
	    indfm = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", userLocale);
	}

	if (startOrScheduleDate != null) {
	    Date tzStartDate = DateUtil.convertToTimeZoneFromDefault(user.getTimeZone(), startOrScheduleDate);
	    responseJSON.put("startDate",
		    indfm.format(tzStartDate) + " " + user.getTimeZone().getDisplayName(userLocale));
	}

	if (finishDate != null) {
	    Date tzFinishDate = DateUtil.convertToTimeZoneFromDefault(user.getTimeZone(), finishDate);
	    responseJSON.put("finishDate",
		    indfm.format(tzFinishDate) + " " + user.getTimeZone().getDisplayName(userLocale));
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping("/getLessonChartData")
    @ResponseBody
    public String getLessonChartData(HttpServletRequest request, HttpServletResponse response) throws IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	Integer possibleLearnersCount = lessonService.getCountLessonLearners(lessonId, null);
	Integer completedLearnersCount = monitoringService.getCountLearnersCompletedLesson(lessonId);
	Integer startedLearnersCount = lessonService.getCountActiveLessonLearners(lessonId) - completedLearnersCount;
	Integer notCompletedLearnersCount = possibleLearnersCount - completedLearnersCount - startedLearnersCount;

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	ObjectNode startedJSON = JsonNodeFactory.instance.objectNode();
	startedJSON.put("name", messageService.getMessage("lesson.chart.started"));
	startedJSON.put("value", Math.round((startedLearnersCount.doubleValue()) / possibleLearnersCount * 100));
	startedJSON.put("raw", startedLearnersCount);
	responseJSON.withArray("data").add(startedJSON);

	ObjectNode completedJSON = JsonNodeFactory.instance.objectNode();
	completedJSON.put("name", messageService.getMessage("lesson.chart.completed"));
	completedJSON.put("value", Math.round(completedLearnersCount.doubleValue() / possibleLearnersCount * 100));
	completedJSON.put("raw", completedLearnersCount);
	responseJSON.withArray("data").add(completedJSON);

	ObjectNode notStartedJSON = JsonNodeFactory.instance.objectNode();
	notStartedJSON.put("name", messageService.getMessage("lesson.chart.not.completed"));
	notStartedJSON.put("value", Math.round(notCompletedLearnersCount.doubleValue() / possibleLearnersCount * 100));
	notStartedJSON.put("raw", notCompletedLearnersCount);
	responseJSON.withArray("data").add(notStartedJSON);

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Produces data for Sequence tab in Monitor.
     */
    @RequestMapping("/getLessonProgress")
    @ResponseBody
    public String getLessonProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer monitorUserId = getUserId();
	if (!securityService.isLessonMonitor(lessonId, monitorUserId, "get lesson progress")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
	Integer searchedLearnerId = WebUtil.readIntParam(request, "searchedLearnerId", true);

	Lesson lesson = lessonService.getLesson(lessonId);
	LearningDesign learningDesign = lesson.getLearningDesign();
	String contentFolderId = learningDesign.getContentFolderID();

	Set<Activity> activities = new HashSet<>();
	// filter activities that are interesting for further processing
	for (Activity activity : learningDesign.getActivities()) {
	    if (activity.isSequenceActivity()) {
		// skip sequence activities as they are just for grouping
		continue;
	    }
	    // get real activity object, not proxy
	    activities.add(monitoringService.getActivityById(activity.getActivityId()));
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	List<ContributeActivityDTO> contributeActivities = getContributeActivities(lessonId, true, false);
	if (contributeActivities != null) {
	    responseJSON.set("contributeActivities", JsonUtil.readArray(contributeActivities));
	}

	// check if the searched learner has started the lesson
	LearnerProgress searchedLearnerProgress = null;
	if (searchedLearnerId != null) {
	    searchedLearnerProgress = lessonService.getUserProgressForLesson(searchedLearnerId, lessonId);
	    responseJSON.put("searchedLearnerFound", searchedLearnerProgress != null);
	}

	// Fetch number of learners at each activity
	ArrayList<Long> activityIds = new ArrayList<>();
	Set<Long> leaders = new TreeSet<>();
	for (Activity activity : activities) {
	    activityIds.add(activity.getActivityId());
	    // find leaders from Leader Selection Tool
	    if (activity.isToolActivity()) {
		ToolActivity toolActivity = (ToolActivity) activity;
		if (ILamsToolService.LEADER_SELECTION_TOOL_SIGNATURE
			.equals(toolActivity.getTool().getToolSignature())) {
		    leaders.addAll(lamsToolService.getLeaderUserId(activity.getActivityId()));
		}
	    }
	}

	Map<Long, Integer> learnerCounts = monitoringService
		.getCountLearnersCurrentActivities(activityIds.toArray(new Long[activityIds.size()]));

	ArrayNode activitiesJSON = JsonNodeFactory.instance.arrayNode();
	for (Activity activity : activities) {
	    Long activityId = activity.getActivityId();
	    ObjectNode activityJSON = JsonNodeFactory.instance.objectNode();
	    activityJSON.put("id", activityId);
	    activityJSON.put("uiid", activity.getActivityUIID());
	    activityJSON.put("title", activity.getTitle());
	    activityJSON.put("type", activity.getActivityTypeId());

	    if (activity.isGateActivity()) {
		activityJSON.put("gateOpen", ((GateActivity) activity).getGateOpen());
	    }

	    Activity parentActivity = activity.getParentActivity();
	    if (activity.isBranchingActivity()) {
		BranchingActivity ba = (BranchingActivity) monitoringService.getActivityById(activity.getActivityId());
		activityJSON.put("x", MonitoringController.getActivityCoordinate(ba.getStartXcoord()));
		activityJSON.put("y", MonitoringController.getActivityCoordinate(ba.getStartYcoord()));
	    } else if (activity.isOptionsWithSequencesActivity()) {
		activityJSON.put("x", MonitoringController
			.getActivityCoordinate(((OptionsWithSequencesActivity) activity).getStartXcoord()));
		activityJSON.put("y", MonitoringController
			.getActivityCoordinate(((OptionsWithSequencesActivity) activity).getStartYcoord()));
	    } else if ((parentActivity != null) && (parentActivity.isOptionsActivity()
		    || parentActivity.isParallelActivity() || parentActivity.isFloatingActivity())) {
		// Optional Activity children had coordinates relative to parent
		activityJSON.put("x", MonitoringController.getActivityCoordinate(parentActivity.getXcoord())
			+ MonitoringController.getActivityCoordinate(activity.getXcoord()));
		activityJSON.put("y", MonitoringController.getActivityCoordinate(parentActivity.getYcoord())
			+ MonitoringController.getActivityCoordinate(activity.getYcoord()));
	    } else {
		activityJSON.put("x", MonitoringController.getActivityCoordinate(activity.getXcoord()));
		activityJSON.put("y", MonitoringController.getActivityCoordinate(activity.getYcoord()));
	    }

	    String monitorUrl = monitoringService.getActivityMonitorURL(lessonId, activityId, contentFolderId,
		    monitorUserId);
	    if (monitorUrl != null && !activity.isBranchingActivity()) {
		// whole activity monitor URL
		activityJSON.put("url", monitorUrl);
	    }

	    // find few latest users and count of all users for each activity
	    int learnerCount = learnerCounts.get(activityId);
	    if (!activity.isBranchingActivity() && !activity.isOptionsWithSequencesActivity()) {
		List<User> latestLearners = monitoringService.getLearnersLatestByActivity(activity.getActivityId(),
			MonitoringController.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT, null);

		// insert leaders as first of learners
		for (Long leaderId : leaders) {
		    for (User learner : latestLearners) {
			if (learner.getUserId().equals(leaderId.intValue())) {
			    latestLearners = MonitoringController.insertHighlightedLearner(learner, latestLearners,
				    MonitoringController.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT);
			    break;
			}
		    }
		}

		// insert the searched learner as the first one
		if ((searchedLearnerProgress != null) && (searchedLearnerProgress.getCurrentActivity() != null)
			&& activity.getActivityId()
				.equals(searchedLearnerProgress.getCurrentActivity().getActivityId())) {
		    // put the searched learner in front
		    latestLearners = MonitoringController.insertHighlightedLearner(searchedLearnerProgress.getUser(),
			    latestLearners, MonitoringController.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT);
		}

		// parse learners into JSON format
		if (!latestLearners.isEmpty()) {
		    ArrayNode learnersJSON = JsonNodeFactory.instance.arrayNode();
		    for (User learner : latestLearners) {
			ObjectNode userJSON = WebUtil.userToJSON(learner, activity);
			if (leaders.contains(learner.getUserId().longValue())) {
			    userJSON.put("leader", true);
			}
			learnersJSON.add(userJSON);
		    }

		    activityJSON.set("learners", learnersJSON);
		}
	    }
	    activityJSON.put("learnerCount", learnerCount);

	    activitiesJSON.add(activityJSON);
	}
	responseJSON.set("activities", activitiesJSON);

	// find learners who completed the lesson
	List<User> completedLearners = monitoringService.getLearnersLatestCompleted(lessonId,
		MonitoringController.LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT, null);
	Integer completedLearnerCount = null;
	if (completedLearners.size() < MonitoringController.LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT) {
	    completedLearnerCount = completedLearners.size();
	} else {
	    completedLearnerCount = monitoringService.getCountLearnersCompletedLesson(lessonId);
	}
	responseJSON.put("completedLearnerCount", completedLearnerCount);

	if ((searchedLearnerProgress != null) && searchedLearnerProgress.isComplete()) {
	    // put the searched learner in front
	    completedLearners = MonitoringController.insertHighlightedLearner(searchedLearnerProgress.getUser(),
		    completedLearners, MonitoringController.LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT);
	}
	for (User learner : completedLearners) {
	    ObjectNode learnerJSON = WebUtil.userToJSON(learner);
	    // no more details are needed for learners who completed the lesson
	    responseJSON.withArray("completedLearners").add(learnerJSON);
	}

	responseJSON.put("numberPossibleLearners", lessonService.getCountLessonLearners(lessonId, null));

	// on first fetch get transitions metadata so Monitoring can set their SVG elems IDs
	if (WebUtil.readBooleanParam(request, "getTransitions", false)) {
	    ArrayNode transitions = JsonNodeFactory.instance.arrayNode();
	    for (Transition transition : learningDesign.getTransitions()) {
		ObjectNode transitionJSON = JsonNodeFactory.instance.objectNode();
		transitionJSON.put("uiid", transition.getTransitionUIID());
		transitionJSON.put("fromID", transition.getFromActivity().getActivityId());
		transitionJSON.put("toID", transition.getToActivity().getActivityId());

		transitions.add(transitionJSON);
	    }
	    responseJSON.set("transitions", transitions);
	}

	// check for live edit status
	if (lesson.getLiveEditEnabled()) {
	    if (lesson.getLockedForEdit()) {
		responseJSON.put("lockedForEdit", true);
		User currentEditor = lesson.getLearningDesign().getEditOverrideUser();
		if (currentEditor != null) {
		    responseJSON.put("lockedForEditUserId", currentEditor.getUserId());
		    responseJSON.put("lockedForEditUsername", currentEditor.getFullName());
		}
	    } else {
		responseJSON.put("lockedForEdit", false);
	    }
	}

	List<ActivityTimeLimitDTO> absoluteTimeLimits = lessonService.getRunningAbsoluteTimeLimits(lessonId);
	if (!absoluteTimeLimits.isEmpty()) {
	    responseJSON.set("timeLimits", JsonUtil.readArray(absoluteTimeLimits));
	}

	response.setContentType("application/json;charset=utf-8");

	return responseJSON.toString();
    }

    /**
     * Gives suggestions when a Monitor searches for Learners and staff members.
     */
    @RequestMapping("/autocomplete")
    @ResponseBody
    public String autocomplete(HttpServletRequest request, HttpServletResponse response) throws Exception {

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getUserId(), "autocomplete in monitoring")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String searchPhrase = request.getParameter("term");
	boolean isOrganisationSearch = WebUtil.readStrParam(request, "scope").equalsIgnoreCase("organisation");

	Collection<User> users = null;
	if (isOrganisationSearch) {
	    // search for Learners in the organisation
	    Map<User, Boolean> result = lessonService.getUsersWithLessonParticipation(lessonId, Role.LEARNER,
		    searchPhrase, MonitoringController.USER_PAGE_SIZE, null, false, true);
	    users = result.keySet();
	} else {
	    // search for Learners in the lesson
	    users = lessonService.getLessonLearners(lessonId, searchPhrase, MonitoringController.USER_PAGE_SIZE, null,
		    true);
	}

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (User user : users) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("label", user.getFirstName() + " " + user.getLastName() + " " + user.getLogin());
	    userJSON.put("value",
		    user.getUserId() + (user.getPortraitUuid() == null ? "" : "_" + user.getPortraitUuid().toString()));

	    responseJSON.add(userJSON);
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Checks if activity A is before activity B in a sequence.
     */
    @RequestMapping("/isActivityPreceding")
    @ResponseBody
    public String isActivityPreceding(HttpServletRequest request, HttpServletResponse response) throws Exception {
	long activityAid = WebUtil.readLongParam(request, "activityA");
	long activityBid = WebUtil.readLongParam(request, "activityB");
	boolean result = false;

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
    private boolean containsActivity(ComplexActivity complexActivity, long targetActivityId,
	    IMonitoringService monitoringService) {
	for (Activity childActivity : complexActivity.getActivities()) {
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

    @RequestMapping("/startPreviewLesson")
    public String startPreviewLesson(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer userID = getUserId();
	long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	try {
	    monitoringService.createPreviewClassForLesson(userID, lessonID);
	    monitoringService.startLesson(lessonID, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
	}
	return null;
    }

    @RequestMapping("/startLiveEdit")
    @ResponseBody
    public String startLiveEdit(HttpServletRequest request, HttpServletResponse response)
	    throws LearningDesignException, UserException, IOException {

	long learningDesignId = WebUtil.readLongParam(request, CommonConstants.PARAM_LEARNING_DESIGN_ID);
	LearningDesign learningDesign = (LearningDesign) userManagementService.findById(LearningDesign.class,
		learningDesignId);
	if (learningDesign.getLessons().isEmpty()) {
	    throw new InvalidParameterException(
		    "There are no lessons associated with learning design: " + learningDesignId);
	}
	Integer organisationID = learningDesign.getLessons().iterator().next().getOrganisation().getOrganisationId();
	Integer userID = getUserId();
	if (!securityService.hasOrgRole(organisationID, userID, new String[] { Role.AUTHOR }, "start live edit")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an author in the organisation");
	    return null;
	}

	if (authoringService.setupEditOnFlyLock(learningDesignId, userID)) {
	    authoringService.setupEditOnFlyGate(learningDesignId, userID);
	} else {
	    response.getWriter().write("Someone else is editing the design at the moment.");
	}

	return null;
    }

    /**
     * Set whether or not the presence available button is available in learner. Expects parameters lessonID and
     * presenceAvailable.
     */
    @RequestMapping(path = "/presenceAvailable", method = RequestMethod.POST)
    public String presenceAvailable(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceAvailable = WebUtil.readBooleanParam(request, "presenceAvailable", false);

	try {
	    monitoringService.togglePresenceAvailable(lessonID, userID, presenceAvailable);

	    if (!presenceAvailable) {
		monitoringService.togglePresenceImAvailable(lessonID, userID, false);
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
    @RequestMapping(path = "/presenceImAvailable", method = RequestMethod.POST)
    public String presenceImAvailable(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceImAvailable = WebUtil.readBooleanParam(request, "presenceImAvailable", false);

	try {
	    monitoringService.togglePresenceImAvailable(lessonID, userID, presenceImAvailable);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * Set whether or not the activity scores / gradebook values are shown to the learner at the end of the lesson.
     * Expects parameters lessonID and presenceAvailable.
     */
    @RequestMapping(path = "/gradebookOnComplete", method = RequestMethod.POST)
    public String gradebookOnComplete(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean gradebookOnComplete = WebUtil.readBooleanParam(request, "gradebookOnComplete", false);

	try {
	    monitoringService.toggleGradebookOnComplete(lessonID, userID, gradebookOnComplete);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /** Open Time Chart display */
    @RequestMapping("/viewTimeChart")
    public String viewTimeChart(HttpServletRequest request, HttpServletResponse response) {
	try {

	    long lessonID = WebUtil.readLongParam(request, "lessonID");

	    // check monitor privledges
	    if (!securityService.isLessonMonitor(lessonID, getUserId(), "open time chart")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    request.setAttribute("lessonID", lessonID);
	    request.setAttribute("learnerID", WebUtil.readLongParam(request, "learnerID", true));

	} catch (Exception e) {
	    request.setAttribute("errorName", "MonitoringAction");
	    request.setAttribute("errorMessage", e.getMessage());

	    return "systemErrorContent";
	}

	return "timer";
    }

    @GetMapping("/getTimeLimits")
    @ResponseBody
    public String getTimeLimits(@RequestParam long lessonID, HttpServletResponse response) throws IOException {
	ArrayNode responseJSON = null;

	List<ActivityTimeLimitDTO> absoluteTimeLimits = lessonService.getRunningAbsoluteTimeLimits(lessonID);
	if (absoluteTimeLimits.isEmpty()) {
	    responseJSON = JsonNodeFactory.instance.arrayNode();
	} else {
	    responseJSON = JsonUtil.readArray(absoluteTimeLimits);
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Creates a list of users out of string with comma-delimited user IDs.
     */
    private List<User> parseUserList(HttpServletRequest request, String paramName, Collection<User> users) {
	String userIdList = request.getParameter(paramName);
	String[] userIdArray = userIdList.split(",");
	List<User> result = new ArrayList<>(userIdArray.length);

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
    private List<ContributeActivityDTO> getContributeActivities(Long lessonId, boolean skipCompletedBranching,
	    boolean skipOpenedGates) {
	List<ContributeActivityDTO> contributeActivities = monitoringService.getAllContributeActivityDTO(lessonId);
	Lesson lesson = lessonService.getLesson(lessonId);

	if (contributeActivities != null) {
	    List<ContributeActivityDTO> resultContributeActivities = new ArrayList<>();
	    for (ContributeActivityDTO contributeActivity : contributeActivities) {
		if (contributeActivity.getContributeEntries() != null) {
		    Iterator<ContributeActivityDTO.ContributeEntry> entryIterator = contributeActivity
			    .getContributeEntries().iterator();
		    while (entryIterator.hasNext()) {
			ContributeActivityDTO.ContributeEntry contributeEntry = entryIterator.next();

			// extra filtering for chosen branching: do not show in Sequence tab if all users were assigned
			if (skipCompletedBranching
				&& ContributionTypes.CHOSEN_BRANCHING.equals(contributeEntry.getContributionType())) {
			    Set<User> learners = new HashSet<>(lesson.getLessonClass().getLearners());
			    ChosenBranchingActivity branching = (ChosenBranchingActivity) monitoringService
				    .getActivityById(contributeActivity.getActivityID());
			    for (SequenceActivity branch : (Set<SequenceActivity>) (Set<?>) branching.getActivities()) {
				Group group = branch.getSoleGroupForBranch();
				if (group != null) {
				    learners.removeAll(group.getUsers());
				}
			    }
			    contributeEntry.setIsComplete(learners.isEmpty());
			}

			if (!contributeEntry.getIsRequired() || (skipOpenedGates && contributeEntry.getIsComplete())) {
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
	return (coord == null) || (coord < 0) ? CommonConstants.DEFAULT_COORD : coord;
    }

    /**
     * Puts the searched learner in front of other learners in the list.
     */
    private static List<User> insertHighlightedLearner(User searchedLearner, List<User> latestLearners, int limit) {
	latestLearners.remove(searchedLearner);
	LinkedList<User> updatedLatestLearners = new LinkedList<>(latestLearners);
	updatedLatestLearners.addFirst(searchedLearner);
	if (updatedLatestLearners.size() > limit) {
	    updatedLatestLearners.removeLast();
	}
	return updatedLatestLearners;
    }

    private List<Activity> getLessonActivities(Lesson lesson) {
	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 *
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = activityDAO
		.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity().getActivityId());
	List<Activity> activities = new ArrayList<>();
	sortActivitiesByLearningDesignOrder(firstActivity, activities);

	return activities;
    }

    @SuppressWarnings("unchecked")
    private void sortActivitiesByLearningDesignOrder(Activity activity, List<Activity> sortedActivities) {
	sortedActivities.add(activity);

	//in case of branching activity - add all activities based on their orderId
	if (activity.isBranchingActivity()) {
	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    Set<SequenceActivity> sequenceActivities = new TreeSet<>(new ActivityOrderComparator());
	    sequenceActivities.addAll((Set<SequenceActivity>) (Set<?>) branchingActivity.getActivities());
	    for (Activity sequenceActivityNotInitialized : sequenceActivities) {
		SequenceActivity sequenceActivity = (SequenceActivity) monitoringService
			.getActivityById(sequenceActivityNotInitialized.getActivityId());
		Set<Activity> childActivities = new TreeSet<>(new ActivityOrderComparator());
		childActivities.addAll(sequenceActivity.getActivities());

		//add one by one in order to initialize all activities
		for (Activity childActivity : childActivities) {
		    Activity activityInit = monitoringService.getActivityById(childActivity.getActivityId());
		    sortedActivities.add(activityInit);
		}
	    }

	    // In case of complex activity (parallel, help or optional activity) add all its children activities.
	    // They will be sorted by orderId
	} else if (activity.isComplexActivity()) {
	    ComplexActivity complexActivity = (ComplexActivity) activity;
	    Set<Activity> childActivities = new TreeSet<>(new ActivityOrderComparator());
	    childActivities.addAll(complexActivity.getActivities());

	    // add one by one in order to initialize all activities
	    for (Activity childActivity : childActivities) {
		Activity activityInit = monitoringService.getActivityById(childActivity.getActivityId());
		sortedActivities.add(activityInit);
	    }
	}

	Transition transitionFrom = activity.getTransitionFrom();
	if (transitionFrom != null) {
	    // query activity from DB as transition holds only proxied activity object
	    Long nextActivityId = transitionFrom.getToActivity().getActivityId();
	    Activity nextActivity = monitoringService.getActivityById(nextActivityId);

	    sortActivitiesByLearningDesignOrder(nextActivity, sortedActivities);
	}
    }
}