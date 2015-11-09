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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.ObjectExtractor;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenBranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
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

    private static final Integer LATEST_LEARNER_PROGRESS_LESSON_DISPLAY_LIMIT = 53;
    private static final Integer LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT = 7;

    private static IAuditService auditService;

    private static ITimezoneService timezoneService;

    private static ILessonService lessonService;

    private static ISecurityService securityService;

    private static IMonitoringService monitoringService;

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private FlashMessage handleException(Exception e, String methodKey) {
	LamsDispatchAction.log.error("Exception thrown " + methodKey, e);
	MonitoringAction.auditService = getAuditService();
	MonitoringAction.auditService.log(MonitoringAction.class.getName() + ":" + methodKey, e.toString());

	if (e instanceof UserAccessDeniedException) {
	    return new FlashMessage(methodKey,
		    getMonitoringService().getMessageService().getMessage("error.user.noprivilege"),
		    FlashMessage.ERROR);
	} else {
	    String[] msg = new String[1];
	    msg[0] = e.getMessage();
	    return new FlashMessage(methodKey,
		    getMonitoringService().getMessageService().getMessage("error.system.error", msg),
		    FlashMessage.CRITICAL_ERROR);
	}
    }

    private FlashMessage handleCriticalError(String methodKey, String messageKey) {
	String message = getMonitoringService().getMessageService().getMessage(messageKey);
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
	    newLesson = getMonitoringService().initializeLessonForPreview(title, desc, ldId, getUserId(), customCSV,
		    learnerPresenceAvailable, learnerImAvailable, liveEditEnabled);
	} else {
	    try {
		newLesson = getMonitoringService().initializeLesson(title, desc, ldId, organisationId, getUserId(),
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
	boolean portfolioEnable = WebUtil.readBooleanParam(request, "portfolioEnable", false);
	boolean presenceEnable = WebUtil.readBooleanParam(request, "presenceEnable", false);
	boolean imEnable = WebUtil.readBooleanParam(request, "imEnable", false);
	Integer splitNumberLessons = WebUtil.readIntParam(request, "splitNumberLessons", true);
	boolean schedulingEnable = WebUtil.readBooleanParam(request, "schedulingEnable", false);
	Date schedulingDatetime = schedulingEnable
		? MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(request.getParameter("schedulingDatetime"))
		: null;
	boolean learnerRestart = WebUtil.readBooleanParam(request, "learnerRestart", false);

	boolean precedingLessonEnable = WebUtil.readBooleanParam(request, "precedingLessonEnable", false);
	Long precedingLessonId = precedingLessonEnable ? WebUtil.readLongParam(request, "precedingLessonId", true)
		: null;
	boolean timeLimitEnable = WebUtil.readBooleanParam(request, "timeLimitEnable", false);
	Integer timeLimitDays = WebUtil.readIntParam(request, "timeLimitDays", true);
	boolean timeLimitIndividualField = WebUtil.readBooleanParam(request, "timeLimitIndividual", false);
	Integer timeLimitIndividual = timeLimitEnable && timeLimitIndividualField ? timeLimitDays : null;
	Integer timeLimitLesson = timeLimitEnable && !timeLimitIndividualField ? timeLimitDays : null;

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
		LamsDispatchAction.log
			.debug("Creating lesson "
				+ (splitNumberLessons == null ? ""
					: "(" + lessonIndex + "/" + splitNumberLessons + ") ")
				+ "\"" + lessonInstanceName + "\"");
	    }

	    Lesson lesson = null;
	    try {
		lesson = getMonitoringService().initializeLesson(lessonInstanceName, introDescription, ldId,
			organisationId, userId, null, introEnable, introImage, portfolioEnable, presenceEnable,
			imEnable, enableLiveEdit, notificationsEnable, learnerRestart, timeLimitIndividual,
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

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    getMonitoringService().unsuspendLesson(lessonId, getUserId());
	} catch (SecurityException e) {
	    getMonitoringService().archiveLesson(lessonId, getUserId());
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
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	try {
	    getMonitoringService().suspendLesson(lessonId, getUserId());
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

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	JSONObject jsonObject = new JSONObject();

	try {
	    // if this method throws an Exception, there will be no removeLesson=true in the JSON reply
	    getMonitoringService().removeLesson(lessonId, getUserId());
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
	    message = getMonitoringService().forceCompleteActivitiesByUser(learnerId, requesterId, lessonId, activityId,
		    removeLearnerContent);
	} catch (SecurityException e) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	if (LamsDispatchAction.log.isDebugEnabled()) {
	    LamsDispatchAction.log
		    .debug("Force complete for learner " + learnerId + " lesson " + lessonId + ". " + message);
	}

	// audit log force completion attempt
	String messageKey = (activityId == null) ? "audit.force.complete.end.lesson" : "audit.force.complete";
	Object[] args = new Object[] { learnerId, activityId, lessonId };
	String auditMessage = getMonitoringService().getMessageService().getMessage(messageKey, args);
	MonitoringAction.auditService.log(MonitoringConstants.MONITORING_MODULE_NAME, auditMessage + " " + message);

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    public ActionForward getLessonLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = getMonitoringService().getLessonLearners(lessonID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLessonLearners").serializeMessage();
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
	Set<User> classUsers = getMonitors ? lesson.getLessonClass().getStaffGroup().getUsers()
		: lesson.getLessonClass().getLearners();
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
	    IUserManagementService userManagementService = MonitoringServiceProxy
		    .getUserManagementService(getServlet().getServletContext());
	    List<User> orgUsers = userManagementService.getUsersFromOrganisationByRole(
		    lesson.getOrganisation().getOrganisationId(), getMonitors ? Role.MONITOR : Role.LEARNER, false,
		    true);
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
     * Gets users in JSON format who are at the given activity at the moment or finished the given lesson.
     */
    public ActionForward getCurrentLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	JSONArray responseJSON = new JSONArray();
	// if activity ID is provided, lesson ID is ignored
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	if (activityId == null) {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    List<User> learners = getMonitoringService().getUsersCompletedLesson(lessonId);
	    for (User learner : learners) {
		responseJSON.put(WebUtil.userToJSON(learner));
	    }
	} else {
	    boolean flaFormat = WebUtil.readBooleanParam(request, "flaFormat", true);
	    Activity activity = getMonitoringService().getActivityById(activityId);
	    Set<Long> activities = new TreeSet<Long>();
	    activities.add(activityId);

	    // for the Flash format of LD SVGs, children activities are hidden
	    // and the parent activity shows all learners
	    if (!flaFormat && (activity.isBranchingActivity() || activity.isOptionsWithSequencesActivity())) {
		Set<Activity> descendants = getDescendants((ComplexActivity) activity);
		for (Activity descendat : descendants) {
		    activities.add(descendat.getActivityId());
		}
	    }

	    List<User> learners = getMonitoringService().getLearnersByActivities(activities.toArray(new Long[] {}),
		    null, null);
	    for (User learner : learners) {
		responseJSON.put(WebUtil.userToJSON(learner));
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
	IUserManagementService userManagementService = MonitoringServiceProxy
		.getUserManagementService(getServlet().getServletContext());
	List<User> allUsers = userManagementService
		.getUsersFromOrganisation(lesson.getOrganisation().getOrganisationId());
	List<User> removedLearners = parseUserList(request, "removedLearners", allUsers);
	for (User removedLearner : removedLearners) {
	    getLessonService().removeLearnerProgress(lessonId, removedLearner.getUserId());
	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug(
			"Removed progress for user ID: " + removedLearner.getUserId() + " in lesson ID: " + lessonId);
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
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = getMonitoringService().getLessonStaff(lessonID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLessonStaff").serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward getLearningDesignDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    getSecurityService().isLessonMonitor(lessonID, getUserId(), "get learning design details", true);
	    wddxPacket = getMonitoringService().getLearningDesignDetails(lessonID);
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLearningDesignDetails").serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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
     * comes from Flash is the user id of the learner for which we are calculating the url. This is different to all the
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

    public ActionForward moveLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    Integer targetWorkspaceFolderID = new Integer(WebUtil.readIntParam(request, "folderID"));
	    wddxPacket = getMonitoringService().moveLesson(lessonID, targetWorkspaceFolderID, userID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "moveLesson");
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
	request.setAttribute("enableLiveEdit", organisation.getEnableLiveEdit());
	request.setAttribute("enableExportPortfolio", organisation.getEnableExportPortfolio());
	request.setAttribute("lesson", lessonDTO);

	return mapping.findForward("monitorLesson");
    }

    /**
     * Gets users whose progress bars will be displayed in Learner tab in Monitor.
     */
    public ActionForward getLearnerProgressPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
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
		: getLessonService().getLessonLearners(lessonId, searchPhrase, 10, (pageNumber - 1) * 10);
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
	String contentFolderId = learningDesign.getContentFolderID();

	Locale userLocale = new Locale(user.getLocaleLanguage(), user.getLocaleCountry());

	responseJSON.put(AttributeNames.PARAM_LEARNINGDESIGN_ID, learningDesign.getLearningDesignId());
	responseJSON.put("numberPossibleLearners", getLessonService().getCountLessonLearners(lessonId, null));
	responseJSON.put("lessonStateID", lesson.getLessonStateId());

	Date startOrScheduleDate = lesson.getStartDateTime() == null ? lesson.getScheduleStartDate()
		: lesson.getStartDateTime();
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
	Integer searchedLearnerId = WebUtil.readIntParam(request, "searchedLearnerId", true);

	Lesson lesson = getLessonService().getLesson(lessonId);
	LearningDesign learningDesign = lesson.getLearningDesign();
	String contentFolderId = learningDesign.getContentFolderID();

	// find out if the LD SVG is in new Flashless (exploded) format
	JSONObject responseJSON = new JSONObject();
	boolean flaFormat = false;
	for (Activity activity : (Set<Activity>) learningDesign.getActivities()) {
	    if ((activity.isBranchingActivity() || activity.isOptionsWithSequencesActivity())
		    && (((ComplexActivity) activity).getXcoord() == null)) {
		// if a single activity is in FLA format, all of them are
		flaFormat = true;
		break;
	    }
	}
	responseJSON.put("flaFormat", flaFormat);

	Set<Activity> activities = new HashSet<Activity>();
	List<ContributeActivityDTO> contributeActivities = getContributeActivities(lessonId, true);
	Map<Long, Set<Activity>> parentToChildren = new TreeMap<Long, Set<Activity>>();
	// filter activities that are interesting for further processing
	for (Activity activity : (Set<Activity>) learningDesign.getActivities()) {
	    if (activity.isSequenceActivity()) {
		// skip sequence activities as they are just for grouping
		continue;
	    }

	    if (flaFormat) {
		// in FLA format everything is exploded so there are no hidden child activities
		activities.add(activity);
		continue;
	    }

	    Activity parentActivity = activity.getParentActivity();
	    Activity parentParentActivity = parentActivity == null ? null : parentActivity.getParentActivity();
	    if (parentParentActivity == null) {
		activities.add(activity);
		continue;
	    }

	    if (!parentParentActivity.isOptionsWithSequencesActivity() && (!parentParentActivity.isBranchingActivity()
		    || parentParentActivity.getActivityId().equals(branchingActivityId))) {
		activities.add(activity);
	    } else {
		// branching and options with sequences in Flash format have hidden activities
		// map the children to their parent for further processing
		Set<Activity> children = parentToChildren.get(parentParentActivity.getActivityId());
		if (children == null) {
		    children = new HashSet<Activity>();
		    parentToChildren.put(parentParentActivity.getActivityId(), children);
		}
		children.add(activity);

		// skip hidden contribute activities
		if (contributeActivities != null) {
		    Iterator<ContributeActivityDTO> contributeActivityIterator = contributeActivities.iterator();
		    while (contributeActivityIterator.hasNext()) {
			if (activity.getActivityId().equals(contributeActivityIterator.next().getActivityID())) {
			    contributeActivityIterator.remove();
			}
		    }
		}
	    }
	}

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

	JSONArray activitiesJSON = new JSONArray();
	for (Activity activity : activities) {
	    Long activityId = activity.getActivityId();
	    JSONObject activityJSON = new JSONObject();
	    activityJSON.put("id", activityId);
	    activityJSON.put("uiid", activity.getActivityUIID());
	    activityJSON.put("title", activity.getTitle());
	    activityJSON.put("type", activity.getActivityTypeId());

	    Activity parentActivity = activity.getParentActivity();
	    if (activity.isBranchingActivity() && (((BranchingActivity) activity).getXcoord() == null)) {
		// old branching is just a rectangle like Tool
		// new branching has start and finish points, it's exploded
		activityJSON.put("x",
			MonitoringAction.getActivityCoordinate(((BranchingActivity) activity).getStartXcoord()));
		activityJSON.put("y",
			MonitoringAction.getActivityCoordinate(((BranchingActivity) activity).getStartYcoord()));
	    } else if (activity.isOptionsWithSequencesActivity()
		    && (((OptionsWithSequencesActivity) activity).getXcoord() == null)) {
		// old optional sequences is just a long rectangle
		// new optional sequences has start and finish points, it's exploded
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
	    if (monitorUrl != null) {
		// whole activity monitor URL
		activityJSON.put("url", monitorUrl);
	    }

	    // find few latest users and count of all users for each activity
	    int learnerCount = 0;
	    if (activity.isBranchingActivity() || activity.isOptionsWithSequencesActivity()) {
		// go through hidden children of complex activities and take them into account
		learnerCount += getMonitoringService().getCountLearnersCurrentActivity(activity);
		Set<Activity> children = parentToChildren.get(activityId);
		if (children != null) {
		    for (Activity child : children) {
			learnerCount += getMonitoringService().getCountLearnersCurrentActivity(child);
		    }
		}
	    } else {
		List<User> latestLearners = getMonitoringService().getLearnersLatestByActivity(activity.getActivityId(),
			MonitoringAction.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT, null);
		if (latestLearners.size() < MonitoringAction.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT) {
		    // if there are less learners than the limit, we already know the size
		    learnerCount = latestLearners.size();
		} else {
		    learnerCount = getMonitoringService().getCountLearnersCurrentActivity(activity);
		}

		if ((searchedLearnerProgress != null) && (searchedLearnerProgress.getCurrentActivity() != null)
			&& activity.getActivityId()
				.equals(searchedLearnerProgress.getCurrentActivity().getActivityId())) {
		    // put the searched learner in front
		    latestLearners = MonitoringAction.insertSearchedLearner(searchedLearnerProgress.getUser(),
			    latestLearners, MonitoringAction.LATEST_LEARNER_PROGRESS_ACTIVITY_DISPLAY_LIMIT);
		}

		// parse learners into JSON format
		if (!latestLearners.isEmpty()) {
		    JSONArray learnersJSON = new JSONArray();
		    for (User learner : latestLearners) {
			learnersJSON.put(WebUtil.userToJSON(learner));
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
	    completedLearners = MonitoringAction.insertSearchedLearner(searchedLearnerProgress.getUser(),
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
     * Gives suggestions when a Monitor searches for a Learner in Sequence and Learners tabs.
     */
    public ActionForward autocompleteMonitoringLearners(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String searchPhrase = request.getParameter("term");

	List<User> learners = getLessonService().getLessonLearners(lessonId, searchPhrase, 10, null);
	JSONArray responseJSON = new JSONArray();
	for (User learner : learners) {
	    JSONObject learnerJSON = new JSONObject();
	    learnerJSON.put("label", learner.getFirstName() + " " + learner.getLastName() + " " + learner.getLogin());
	    learnerJSON.put("value", learner.getUserId());

	    responseJSON.put(learnerJSON);
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

    public ActionForward releaseGate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket = null;
	try {
	    Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	    wddxPacket = getMonitoringService().releaseGate(activityID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "releaseGate");
	    wddxPacket = flashMessage.serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward startPreviewLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	FlashMessage flashMessage = null;

	try {

	    Integer userID = getUserId();
	    long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    /**
	     * InitializeLessonServlet handles the Lesson initialisation process. Create Lesson Class and start Lesson
	     * Preview.
	     */

	    try {
		getMonitoringService().createPreviewClassForLesson(userID, lessonID);
		getMonitoringService().startLesson(lessonID, getUserId());
	    } catch (SecurityException e) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return null;
	    }

	    flashMessage = new FlashMessage("startPreviewSession", new Long(lessonID));
	} catch (Exception e) {
	    flashMessage = handleException(e, "startPreviewSession");
	}

	PrintWriter writer = response.getWriter();
	writer.println(flashMessage.serializeMessage());
	return null;
    }

    public ActionForward startLiveEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws LearningDesignException, UserException, IOException {
	long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	Integer userID = getUserId();

	IAuthoringService authoringService = MonitoringServiceProxy
		.getAuthoringService(getServlet().getServletContext());

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
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
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
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    MonitoringAction.timezoneService = (ITimezoneService) ctx.getBean("timezoneService");
	}
	return MonitoringAction.timezoneService;
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

    /**
     * Set whether or not the export portfolio button is available in learner. Expects parameters lessonID and
     * learnerExportPortfolio.
     */
    public ActionForward learnerExportPortfolioAvailable(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean learnerExportPortfolioAvailable = WebUtil.readBooleanParam(request, "learnerExportPortfolio", false);
	try {
	    getMonitoringService().setLearnerPortfolioAvailable(lessonID, userID, learnerExportPortfolioAvailable);
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

	Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	Integer userID = getUserId();
	Boolean presenceAvailable = WebUtil.readBooleanParam(request, "presenceAvailable", false);

	try {
	    getMonitoringService().setPresenceAvailable(lessonID, userID, presenceAvailable);

	    if (!presenceAvailable) {
		getMonitoringService().setPresenceImAvailable(lessonID, userID, false);
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
	    getMonitoringService().setPresenceImAvailable(lessonID, userID, presenceImAvailable);
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
	IUserManagementService userManagementService = MonitoringServiceProxy
		.getUserManagementService(getServlet().getServletContext());
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
     * Gets all children and their childre etc. of the given complex activity.
     */
    @SuppressWarnings("unchecked")
    private Set<Activity> getDescendants(ComplexActivity complexActivity) {
	Set<Activity> result = new HashSet<Activity>();
	for (Activity child : (Set<Activity>) complexActivity.getActivities()) {
	    child = getMonitoringService().getActivityById(child.getActivityId());
	    if (child.isComplexActivity()) {
		result.addAll(getDescendants((ComplexActivity) child));
	    } else {
		result.add(child);
	    }
	}
	return result;
    }

    
    /**
     * Puts the searched learner in front of other learners in the list. 
     */
    private static List<User> insertSearchedLearner(User searchedLearner, List<User> latestLearners, int limit) {
	latestLearners.remove(searchedLearner);
	LinkedList<User> updatedLatestLearners = new LinkedList<User>(latestLearners);
	updatedLatestLearners.addFirst(searchedLearner);
	if (updatedLatestLearners.size() > limit) {
	    updatedLatestLearners.removeLast();
	}
	return updatedLatestLearners;
    }
}