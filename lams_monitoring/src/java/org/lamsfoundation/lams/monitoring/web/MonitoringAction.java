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
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenBranchingActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LearnerProgressComparator;
import org.lamsfoundation.lams.lesson.util.LearnerProgressNameComparator;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>
 * The action servlet that provide all the monitoring functionalities. It interact with the teacher via flash and JSP
 * monitoring interface.
 * </p>
 * 
 * @author Jacky Fang
 * @since 2005-4-15
 * @version 1.1
 * 
 *          ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/monitoring" parameter="method" validate="false"
 * @struts.action-forward name = "previewdeleted" path = "/previewdeleted.jsp"
 * @struts.action-forward name = "notsupported" path = ".notsupported"
 * @struts.action-forward name = "timeChart" path = "/timeChart.jsp"
 * @struts.action-forward name = "monitorLesson" path = "/monitor.jsp"
 * 
 *                        ----------------XDoclet Tags--------------------
 */
public class MonitoringAction extends LamsDispatchAction {

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------
    private static final String PREVIEW_DELETED_REPORT_SCREEN = "previewdeleted";
    private static final String NOT_SUPPORTED_SCREEN = "notsupported";
    private static final String TIME_CHART_SCREEN = "timeChart";
    private static final String ERROR = "error";
    private static final DateFormat LESSON_SCHEDULING_DATETIME_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm");

    private static IAuditService auditService;

    private static ITimezoneService timezoneService;

    private static ILessonService lessonService;
    
    private static ISecurityService securityService;

    
    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private FlashMessage handleException(Exception e, String methodKey, IMonitoringService monitoringService) {
	LamsDispatchAction.log.error("Exception thrown " + methodKey, e);
	MonitoringAction.auditService = getAuditService();
	MonitoringAction.auditService.log(MonitoringAction.class.getName() + ":" + methodKey, e.toString());

	if (e instanceof UserAccessDeniedException) {
	    return new FlashMessage(methodKey, monitoringService.getMessageService().getMessage(
		    "error.user.noprivilege"), FlashMessage.ERROR);
	} else {
	    String[] msg = new String[1];
	    msg[0] = e.getMessage();
	    return new FlashMessage(methodKey, monitoringService.getMessageService().getMessage("error.system.error",
		    msg), FlashMessage.CRITICAL_ERROR);
	}
    }

    private FlashMessage handleCriticalError(String methodKey, String messageKey, IMonitoringService monitoringService) {
	String message = monitoringService.getMessageService().getMessage(messageKey);
	LamsDispatchAction.log.error("Error occured " + methodKey + " error ");
	MonitoringAction.auditService = getAuditService();
	MonitoringAction.auditService.log(MonitoringAction.class.getName() + ":" + methodKey, message);

	return new FlashMessage(methodKey, message, FlashMessage.CRITICAL_ERROR);
    }

    private ActionForward redirectToURL(ActionMapping mapping, HttpServletResponse response, String url)
	    throws IOException {
	if (url != null) {
	    String fullURL = WebUtil.convertToFullURL(url);
	    response.sendRedirect(response.encodeRedirectURL(fullURL));
	    return null;
	} else {
	    return mapping.findForward(MonitoringAction.NOT_SUPPORTED_SCREEN);
	}
    }

    /**
     * Initializes a lesson for specific learning design with the given lesson title and lesson description. If
     * initialization is successful, this method will the ID of new lesson.
     * 
     * Currently used only in TestHarness and Flashless Authoring Preview.
     */
    public ActionForward initializeLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

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
	Boolean learnerExportAvailable = WebUtil.readBooleanParam(request, "learnerExportPortfolio", false);
	Boolean learnerPresenceAvailable = WebUtil.readBooleanParam(request, "learnerPresenceAvailable", false);
	Boolean learnerImAvailable = WebUtil.readBooleanParam(request, "learnerImAvailable", false);
	Boolean liveEditEnabled = WebUtil.readBooleanParam(request, "liveEditEnabled", false);
	Boolean learnerRestart = WebUtil.readBooleanParam(request, "learnerRestart", false);

	Lesson newLesson = null;
	if ((copyType != null) && copyType.equals(LearningDesign.COPY_TYPE_PREVIEW)) {
	    newLesson = monitoringService.initializeLessonForPreview(title, desc, ldId, getUserId(), customCSV,
		    learnerPresenceAvailable, learnerImAvailable, liveEditEnabled);
	} else {
	    try {
		newLesson = monitoringService.initializeLesson(title, desc, ldId, organisationId, getUserId(),
			customCSV, false, false, learnerExportAvailable, learnerPresenceAvailable, learnerImAvailable,
			liveEditEnabled, false, learnerRestart, null, null);
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
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward startLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
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

    public ActionForward createLessonClass(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		.getServletContext());

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
	boolean portfolioEnable = WebUtil.readBooleanParam(request, "portfolioEnable", false);
	boolean presenceEnable = WebUtil.readBooleanParam(request, "presenceEnable", false);
	boolean imEnable = WebUtil.readBooleanParam(request, "imEnable", false);
	Integer splitNumberLessons = WebUtil.readIntParam(request, "splitNumberLessons", true);
	boolean schedulingEnable = WebUtil.readBooleanParam(request, "schedulingEnable", false);
	Date schedulingDatetime = schedulingEnable ? MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(request
		.getParameter("schedulingDatetime")) : null;
	boolean learnerRestart = WebUtil.readBooleanParam(request, "learnerRestart", false);

	boolean precedingLessonEnable = WebUtil.readBooleanParam(request, "precedingLessonEnable", false);
	Long precedingLessonId = precedingLessonEnable ? WebUtil.readLongParam(request, "precedingLessonId", true)
		: null;
	boolean timeLimitEnable = WebUtil.readBooleanParam(request, "timeLimitEnable", false);
	Integer timeLimitDays = WebUtil.readIntParam(request, "timeLimitDays", true);
	boolean timeLimitIndividualField = WebUtil.readBooleanParam(request, "timeLimitIndividual", false);
	Integer timeLimitIndividual = timeLimitEnable && timeLimitIndividualField ? timeLimitDays : null;
	Integer timeLimitLesson = timeLimitEnable && !timeLimitIndividualField ? timeLimitDays : null;

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		.getServletContext());

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
	List<User> lessonInstanceLearners = splitNumberLessons == null ? learners : new ArrayList<User>(
		(learners.size() / splitNumberLessons) + 1);
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
		for (int learnerIndex = lessonIndex - 1; learnerIndex < learners.size(); learnerIndex += splitNumberLessons) {
		    lessonInstanceLearners.add(learners.get(learnerIndex));
		}
	    }

	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Creating lesson "
			+ (splitNumberLessons == null ? "" : "(" + lessonIndex + "/" + splitNumberLessons + ") ")
			+ "\"" + lessonInstanceName + "\"");
	    }

	    Lesson lesson = null;
	    try {
		lesson = monitoringService.initializeLesson(lessonInstanceName, introDescription, ldId, organisationId,
			userId, null, introEnable, introImage, portfolioEnable, presenceEnable, imEnable,
			enableLiveEdit, notificationsEnable, learnerRestart, timeLimitIndividual, precedingLessonId);

		monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
			learnerGroupInstanceName, lessonInstanceLearners, staffGroupInstanceName, staff, userId);
	    } catch (SecurityException e) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation or lesson");
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

		    // if lesson should finish in few days, set it here
		    if (timeLimitLesson != null) {
			monitoringService.finishLessonOnSchedule(lesson.getLessonId(), timeLimitLesson, userId);
		    }
		} catch (SecurityException e) {
		    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		    return null;
		}
	    }
	}

	return null;
    }

    public ActionForward startOnScheduleLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ParseException, IOException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_START_DATE);
	Date startDate = MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateStr);
	try {
	    monitoringService.startLessonOnSchedule(lessonId, startDate, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	return null;
    }

    /**
     * The Struts dispatch method to archive a lesson.
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
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward archiveLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.unsuspendLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    monitoringService.archiveLesson(lessonId, getUserId());
	}
	return null;
    }

    /**
     * The Struts dispatch method to "unarchive" a lesson. Returns it back to its previous state. A wddx acknowledgement
     * message will be serialized and sent back to the flash component.
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
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward unarchiveLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.unarchiveLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * The purpose of suspending is to hide the lesson from learners temporarily. It doesn't make any sense to suspend a
     * created or a not started (ie scheduled) lesson as they will not be shown on the learner interface anyway! If the
     * teacher tries to suspend a lesson that is not in the STARTED_STATE, then an error should be returned to Flash.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward suspendLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.suspendLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * Unsuspend a lesson which state must be Lesson.SUPSENDED_STATE. Otherwise a error message will return to flash
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
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    monitoringService.unsuspendLesson(lessonId, getUserId());
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
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	// if this method throws an Exception, there will be no removeLesson=true in the JSON reply
	monitoringService.removeLesson(lessonId, getUserId());

	JSONObject jsonObject = new JSONObject();
	jsonObject.put("removeLesson", true);
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
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward forceComplete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	getAuditService();
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

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
	Integer learnerId = new Integer(WebUtil.readIntParam(request, MonitoringConstants.PARAM_LEARNER_ID));
	Integer requesterId = getUserId();
	boolean removeLearnerContent = WebUtil.readBooleanParam(request,
		MonitoringConstants.PARAM_REMOVE_LEARNER_CONTENT, false);

	String message = null;
	try {
	    message = monitoringService.forceCompleteActivitiesByUser(learnerId, requesterId, lessonId, activityId,
		    removeLearnerContent);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	if (LamsDispatchAction.log.isDebugEnabled()) {
	    LamsDispatchAction.log.debug("Force complete for learner " + learnerId + " lesson " + lessonId + ". "
		    + message);
	}

	// audit log force completion attempt
	String messageKey = (activityId == null) ? "audit.force.complete.end.lesson" : "audit.force.complete";
	Object[] args = new Object[] { learnerId, activityId, lessonId };
	String auditMessage = monitoringService.getMessageService().getMessage(messageKey, args);
	MonitoringAction.auditService.log(MonitoringConstants.MONITORING_MODULE_NAME, auditMessage + " " + message);

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    public ActionForward getLessonLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = monitoringService.getLessonLearners(lessonID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLessonLearners", monitoringService).serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    /**
     * Gets learners or monitors of either the lesson only or the lesson and organisation containing it.
     */
    @SuppressWarnings("unchecked")
    public ActionForward getClassMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE);
	boolean getMonitors = Role.MONITOR.equalsIgnoreCase(role);
	boolean classOnly = WebUtil.readBooleanParam(request, "classOnly", true);
	Lesson lesson = getLessonService().getLesson(lessonId);
	Set<User> classUsers = getMonitors ? lesson.getLessonClass().getStaffGroup().getUsers() : lesson
		.getLessonClass().getLearners();
	JSONArray responseJSON = new JSONArray();

	// get class members
	for (User user : classUsers) {
	    JSONObject userJSON = WebUtil.userToJSON(user);
	    if (!classOnly) {
		// mark that this user is a class member
		userJSON.put("classMember", true);
		if (lesson.getUser().equals(user)) {
		    // mark this user is lesson author
		    userJSON.put("lessonCreator", true);
		}
	    }
	    responseJSON.put(userJSON);
	}

	// add non-class, organisation members, if requested
	if (!classOnly) {
	    IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		    .getServletContext());
	    List<User> orgUsers = userManagementService.getUsersFromOrganisationByRole(lesson.getOrganisation()
		    .getOrganisationId(), getMonitors ? Role.MONITOR : Role.LEARNER, false, true);
	    for (User user : orgUsers) {
		if (!classUsers.contains(user)) {
		    JSONObject userJSON = WebUtil.userToJSON(user);
		    userJSON.put("classMember", false);
		    responseJSON.put(userJSON);
		}
	    }
	}

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
	Lesson lesson = getLessonService().getLesson(lessonId);

	// monitor user opted for removing lesson progress for following users
	IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		.getServletContext());
	List<User> allUsers = userManagementService.getUsersFromOrganisation(lesson.getOrganisation()
		.getOrganisationId());
	List<User> removedLearners = parseUserList(request, "removedLearners", allUsers);
	for (User removedLearner : removedLearners) {
	    getLessonService().removeLearnerProgress(lessonId, removedLearner.getUserId());
	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Removed progress for user ID: " + removedLearner.getUserId()
			+ " in lesson ID: " + lessonId);
	    }
	}

	List<User> learners = parseUserList(request, "learners", allUsers);
	getLessonService().setLearners(lesson, learners);

	List<User> staff = parseUserList(request, "monitors", allUsers);
	getLessonService().setStaffMembers(lesson, staff);

	return null;
    }

    public ActionForward getLessonStaff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = monitoringService.getLessonStaff(lessonID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLessonStaff", monitoringService).serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward getLearningDesignDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    getSecurityService().isLessonMonitor(lessonID, getUserId(), "get learning design details", true);
	    wddxPacket = monitoringService.getLearningDesignDetails(lessonID);
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLearningDesignDetails", monitoringService).serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward getDictionaryXML(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	MessageService messageService = monitoringService.getMessageService();

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
     * comes from Flash is the user id of the learner for which we are calculating the url. This is different to all the
     * other calls.
     */
    public ActionForward getLearnerActivityURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, LamsToolServiceException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	Integer learnerUserID = new Integer(WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID));
	Long activityID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	try {
	    String url = monitoringService.getLearnerActivityURL(lessonID, activityID, learnerUserID, getUserId());
	    return redirectToURL(mapping, response, url);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
    }

    /** Calls the server to bring up the activity's monitoring page. Assumes destination is a new window */
    public ActionForward getActivityMonitorURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, LamsToolServiceException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	String contentFolderID = WebUtil.readStrParam(request, "contentFolderID");
	try {
	    String url = monitoringService.getActivityMonitorURL(lessonID, activityID, contentFolderID, getUserId());
	    return redirectToURL(mapping, response, url);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}
    }

    public ActionForward moveLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	String wddxPacket = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    Integer targetWorkspaceFolderID = new Integer(WebUtil.readIntParam(request, "folderID"));
	    wddxPacket = monitoringService.moveLesson(lessonID, targetWorkspaceFolderID, userID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "moveLesson", monitoringService);
	    wddxPacket = flashMessage.serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
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
	if ((lessonDTO.getCreateDateTime() != null) && (lessonDTO.getCreateDateTime() != WDDXTAGS.DATE_NULL_VALUE)) {
	    DateFormat sfm = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    lessonDTO.setCreateDateTimeStr(sfm.format(lessonDTO.getCreateDateTime()));
	}
	//prepare encoded lessonId for shortened learner URL
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

	IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		.getServletContext());
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		lessonDTO.getOrganisationID());
	request.setAttribute("notificationsAvailable", organisation.getEnableCourseNotifications());
	request.setAttribute("lesson", lessonDTO);

	return mapping.findForward("monitorLesson");
    }

    /**
     * Gets users whose progress bars will be displayed in Learner tab in Monitor.
     */
    @SuppressWarnings("unchecked")
    public ActionForward getLearnerProgressPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String searchPhrase = request.getParameter("searchPhrase");
	Integer pageNumber = WebUtil.readIntParam(request, "pageNumber", true);
	if (pageNumber == null) {
	    pageNumber = 1;
	}
	boolean isProgressSorted = WebUtil.readBooleanParam(request, "isProgressSorted", false);

	JSONObject responseJSON = new JSONObject();
	Lesson lesson = getLessonService().getLesson(lessonId);
	List<LearnerProgress> learnerProgresses = new ArrayList<LearnerProgress>(lesson.getLearnerProgresses());
	// sort either by user's name or his progress
	Collections.sort(learnerProgresses, isProgressSorted ? new LearnerProgressComparator()
		: new LearnerProgressNameComparator());

	if (!StringUtils.isBlank(searchPhrase)) {
	    // get only users whose names match the given phrase
	    Set<LearnerProgress> searchResult = new LinkedHashSet<LearnerProgress>();

	    // check if there are several search phrases in the query
	    String[] searchPhrases = searchPhrase.split(";");
	    for (int searchPhraseIndex = 0; searchPhraseIndex < searchPhrases.length; searchPhraseIndex++) {
		searchPhrases[searchPhraseIndex] = searchPhrases[searchPhraseIndex].trim().toLowerCase();
	    }

	    for (LearnerProgress learnerProgress : learnerProgresses) {
		User learner = learnerProgress.getUser();
		StringBuilder learnerDisplayName = new StringBuilder(learner.getFirstName().toLowerCase()).append(" ")
			.append(learner.getLastName().toLowerCase()).append(" ")
			.append(learner.getLogin().toLowerCase());
		for (String searchPhrasePiece : searchPhrases) {
		    if (!StringUtils.isBlank(searchPhrasePiece)
			    && (learnerDisplayName.indexOf(searchPhrasePiece) != -1)) {
			searchResult.add(learnerProgress);
		    }
		}
	    }

	    learnerProgresses.clear();
	    learnerProgresses.addAll(searchResult);
	}

	// batch size is 10
	int toIndex = Math.min(pageNumber * 10, learnerProgresses.size());
	int fromIndex = Math.min((pageNumber - 1) * 10, Math.max(toIndex - 10, 0));
	// get just the requested chunk
	for (LearnerProgress learnerProgress : learnerProgresses.subList(fromIndex, toIndex)) {
	    responseJSON.append("learners", WebUtil.userToJSON(learnerProgress.getUser()));
	}

	responseJSON.put("numberActiveLearners", learnerProgresses.size());
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
	LessonDetailsDTO lessonDetails = lesson.getLessonDetails();
	String contentFolderId = lessonDetails.getContentFolderID();


	Locale userLocale = new Locale(user.getLocaleLanguage(), user.getLocaleCountry());

	responseJSON.put(AttributeNames.PARAM_LEARNINGDESIGN_ID, lessonDetails.getLearningDesignID());
	responseJSON.put("numberPossibleLearners", lessonDetails.getNumberPossibleLearners());
	responseJSON.put("lessonStateID", lessonDetails.getLessonStateID());

	Date startOrScheduleDate = lesson.getStartDateTime() == null ? lesson.getScheduleStartDate() : lesson
		.getStartDateTime();
	if (startOrScheduleDate != null) {
	    DateFormat indfm = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", userLocale);
	    Date tzStartDate = DateUtil.convertToTimeZoneFromDefault(user.getTimeZone(), startOrScheduleDate);
	    responseJSON.put("startDate",
		    indfm.format(tzStartDate) + " " + user.getTimeZone().getDisplayName(userLocale));
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
	Long branchingActivityId = WebUtil.readLongParam(request, "branchingActivityID", true);

	Lesson lesson = getLessonService().getLesson(lessonId);
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	LessonDetailsDTO lessonDetails = lesson.getLessonDetails();
	String contentFolderId = lessonDetails.getContentFolderID();

	// few details for each activity
	Map<Long, JSONObject> activitiesMap = new TreeMap<Long, JSONObject>();
	for (Activity activity : (Set<Activity>) lesson.getLearningDesign().getActivities()) {
	    if ((branchingActivityId == null) || MonitoringAction.isBranchingChild(branchingActivityId, activity)) {
		Long activityId = activity.getActivityId();
		JSONObject activityJSON = new JSONObject();
		activityJSON.put("id", activityId);
		activityJSON.put("title", activity.getTitle());

		int activityType = activity.getActivityTypeId();
		activityJSON.put("type", activityType);
		Activity parentActivity = activity.getParentActivity();
		if (activity.isBranchingActivity() && (((BranchingActivity) activity).getXcoord() == null)) {
		    // old branching is just a rectangle like Tool
		    // new branching has start and finish points, it's exploded
		    activityJSON.put("flaFormat", true);
		    activityJSON.put("x", ((BranchingActivity) activity).getStartXcoord());
		    activityJSON.put("y", ((BranchingActivity) activity).getStartYcoord());
		} else if (activity.isOptionsWithSequencesActivity()
			&& (((OptionsWithSequencesActivity) activity).getXcoord() == null)) {
		    // old optional sequences is just a long rectangle
		    // new optional sequences has start and finish points, it's exploded
		    activityJSON.put("flaFormat", true);
		    activityJSON.put("x", ((OptionsWithSequencesActivity) activity).getStartXcoord());
		    activityJSON.put("y", ((OptionsWithSequencesActivity) activity).getStartYcoord());
		} else if ((parentActivity != null)
			&& ((Activity.OPTIONS_ACTIVITY_TYPE == parentActivity.getActivityTypeId()) || (Activity.PARALLEL_ACTIVITY_TYPE == parentActivity
				.getActivityTypeId()))) {
		    // Optional Activity children had coordinates relative to parent
		    activityJSON.put("x", parentActivity.getXcoord() + activity.getXcoord());
		    activityJSON.put("y", parentActivity.getYcoord() + activity.getYcoord());
		} else {
		    activityJSON.put("x", activity.getXcoord());
		    activityJSON.put("y", activity.getYcoord());
		}

		String monitorUrl = monitoringService.getActivityMonitorURL(lessonId, activityId, contentFolderId,
			monitorUserId);
		if (monitorUrl != null) {
		    // whole activity monitor URL
		    activityJSON.put("url", monitorUrl);
		}

		activitiesMap.put(activityId, activityJSON);
	    }
	}

	JSONObject responseJSON = new JSONObject();
	for (LearnerProgress learnerProgress : (Set<LearnerProgress>) lesson.getLearnerProgresses()) {
	    User learner = learnerProgress.getUser();
	    if (learnerProgress.isComplete()) {
		JSONObject learnerJSON = WebUtil.userToJSON(learner);
		// no more details are needed for learners who completed the lesson
		responseJSON.append("completedLearners", learnerJSON);
	    } else {
		Activity currentActivity = learnerProgress.getCurrentActivity();
		if ((currentActivity != null)
			&& ((branchingActivityId == null) || MonitoringAction.isBranchingChild(branchingActivityId,
				currentActivity))) {
		    JSONObject learnerJSON = WebUtil.userToJSON(learner);

		    // assign learners to child activity or parent branching/options with sequences?
		    Activity parentActivity = currentActivity.getParentActivity();
		    Long targetActivityId = (branchingActivityId != null)
			    || (parentActivity == null)
			    || (parentActivity.getParentActivity() == null)
			    || !(parentActivity.getParentActivity().isBranchingActivity() || parentActivity
				    .getParentActivity().isOptionsWithSequencesActivity()) ? currentActivity
			    .getActivityId() : parentActivity.getParentActivity().getActivityId();

		    JSONObject targetActivityJSON = activitiesMap.get(targetActivityId);
		    if (Boolean.TRUE.equals(JsonUtil.opt(targetActivityJSON, "flaFormat"))) {
			// for new format, we always set learners to child activity, not parent
			targetActivityJSON = activitiesMap.get(currentActivity.getActivityId());
		    }
		    targetActivityJSON.append("learners", learnerJSON);
		}
	    }
	}

	responseJSON.put("activities", new JSONArray(activitiesMap.values()));
	responseJSON.put("numberPossibleLearners", lessonDetails.getNumberPossibleLearners());
	List<ContributeActivityDTO> contributeActivities = getContributeActivities(lessonId, true);
	// remove "attention required" marker for hidden activities in Flash Authoring
	if (contributeActivities != null) {
	    Iterator<ContributeActivityDTO> activityIterator = contributeActivities.iterator();
	    while (activityIterator.hasNext()) {
		ContributeActivityDTO contributeActivityDTO = activityIterator.next();
		Activity contributeActivity = monitoringService.getActivityById(contributeActivityDTO.getActivityID());
		Activity topParentActivity = contributeActivity.getParentActivity() == null ? null : contributeActivity
			.getParentActivity().getParentActivity();

		if ((branchingActivityId == null)
			&& (topParentActivity != null)
			&& (topParentActivity.isBranchingActivity() || topParentActivity
				.isOptionsWithSequencesActivity())) {
		    JSONObject topContributeActivityJSON = activitiesMap.get(topParentActivity.getActivityId());
		    if (!Boolean.TRUE.equals(JsonUtil.opt(topContributeActivityJSON, "flaFormat"))) {
			activityIterator.remove();
		    }
		}
	    }
	    Gson gson = new GsonBuilder().create();
	    responseJSON.put("contributeActivities", new JSONArray(gson.toJson(contributeActivities)));
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());

	return null;
    }

    public ActionForward releaseGate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	String wddxPacket = null;
	try {
	    Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	    wddxPacket = monitoringService.releaseGate(activityID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "releaseGate", monitoringService);
	    wddxPacket = flashMessage.serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward startPreviewLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	FlashMessage flashMessage = null;

	try {

	    Integer userID = getUserId();
	    long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    /**
	     * InitializeLessonServlet handles the Lesson initialisation process. Create Lesson Class and start Lesson
	     * Preview.
	     */

	    try {
		monitoringService.createPreviewClassForLesson(userID, lessonID);
		monitoringService.startLesson(lessonID, getUserId());
	    } catch (SecurityException e) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return null;
	    }
	    
	    flashMessage = new FlashMessage("startPreviewSession", new Long(lessonID));
	} catch (Exception e) {
	    flashMessage = handleException(e, "startPreviewSession", monitoringService);
	}

	PrintWriter writer = response.getWriter();
	writer.println(flashMessage.serializeMessage());
	return null;
    }

    public ActionForward startLiveEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws LearningDesignException, UserException, IOException {
	long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	Integer userID = getUserId();

	IAuthoringService authoringService = MonitoringServiceProxy.getAuthoringService(getServlet()
		.getServletContext());

	if (authoringService.setupEditOnFlyLock(learningDesignId, userID)) {
	    authoringService.setupEditOnFlyGate(learningDesignId, userID);
	} else {
	    response.getWriter().write("Someone else is editing the design at the moment.");
	}

	return null;
    }

    /**
     * Get AuditService bean.
     * 
     * @return
     */
    private IAuditService getAuditService() {
	if (MonitoringAction.auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    MonitoringAction.auditService = (IAuditService) ctx.getBean("auditService");
	}
	return MonitoringAction.auditService;
    }

    /**
     * Get TimezoneService bean.
     * 
     * @return
     */
    private ITimezoneService getTimezoneService() {
	if (MonitoringAction.timezoneService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    MonitoringAction.timezoneService = (ITimezoneService) ctx.getBean("timezoneService");
	}
	return MonitoringAction.timezoneService;
    }

    private ILessonService getLessonService() {
	if (MonitoringAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    MonitoringAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return MonitoringAction.lessonService;
    }
    
    private ISecurityService getSecurityService() {
	if (MonitoringAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    MonitoringAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return MonitoringAction.securityService;
    }

    /**
     * Set whether or not the export portfolio button is available in learner. Expects parameters lessonID and
     * learnerExportPortfolio.
     */
    public ActionForward learnerExportPortfolioAvailable(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean learnerExportPortfolioAvailable = WebUtil.readBooleanParam(request, "learnerExportPortfolio", false);
	try {
	    monitoringService.setLearnerPortfolioAvailable(lessonID, userID, learnerExportPortfolioAvailable);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /**
     * Set whether or not the presence available button is available in learner. Expects parameters lessonID and
     * presenceAvailable.
     */
    public ActionForward presenceAvailable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceAvailable = WebUtil.readBooleanParam(request, "presenceAvailable", false);

	try {
	    monitoringService.setPresenceAvailable(lessonID, userID, presenceAvailable);

	    if (!presenceAvailable) {
		monitoringService.setPresenceImAvailable(lessonID, userID, false);
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
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceImAvailable = WebUtil.readBooleanParam(request, "presenceImAvailable", false);

	try {
	    monitoringService.setPresenceImAvailable(lessonID, userID, presenceImAvailable);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}
	return null;
    }

    /** Open Time Chart display */
    public ActionForward viewTimeChart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

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
	IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		.getServletContext());
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

    private static boolean isBranchingChild(Long branchingActivityId, Activity activity) {
	if ((branchingActivityId == null) || (activity == null)) {
	    return false;
	}
	Activity parentActivity = activity.getParentActivity();
	return (parentActivity != null) && (parentActivity.getParentActivity() != null)
		&& parentActivity.getParentActivity().getActivityId().equals(branchingActivityId);
    }

    @SuppressWarnings("unchecked")
    private List<ContributeActivityDTO> getContributeActivities(Long lessonId, boolean skipCompletedBranching) {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	List<ContributeActivityDTO> contributeActivities = monitoringService.getAllContributeActivityDTO(lessonId);
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
			    ChosenBranchingActivity branching = (ChosenBranchingActivity) monitoringService
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
}