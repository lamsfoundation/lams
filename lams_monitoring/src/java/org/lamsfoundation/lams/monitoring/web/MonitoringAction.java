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
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.util.LessonComparator;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;

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

    /** See deleteOldPreviewLessons */
    public static final String NUM_DELETED = "numDeleted";

    private static IAuditService auditService;

    private static ITimezoneService timezoneService;

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

    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------
    /**
     * This STRUTS action method will initialize a lesson for specific learning design with the given lesson title and
     * lesson description.
     * <p>
     * If initialization is successed, this method will return a WDDX message which includes the ID of new lesson.
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
     * @deprecated
     */
    public ActionForward initializeLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	FlashMessage flashMessage = null;

	try {
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
	    Boolean learnerExportAvailable = WebUtil.readBooleanParam(request, "learnerExportPortfolio", false);
	    Boolean learnerPresenceAvailable = WebUtil.readBooleanParam(request, "learnerPresenceAvailable", false);
	    Boolean learnerImAvailable = WebUtil.readBooleanParam(request, "learnerImAvailable", false);
	    Boolean liveEditEnabled = WebUtil.readBooleanParam(request, "liveEditEnabled", false);
	    Lesson newLesson = monitoringService.initializeLesson(title, desc, ldId, organisationId, getUserId(), null,
		    Boolean.FALSE, Boolean.FALSE, learnerExportAvailable, learnerPresenceAvailable, learnerImAvailable,
		    liveEditEnabled, Boolean.FALSE, null, null);

	    flashMessage = new FlashMessage("initializeLesson", newLesson.getLessonId());
	} catch (Exception e) {
	    flashMessage = handleException(e, "initializeLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    /**
     * The Struts dispatch method that starts a lesson that has been created beforehand. Most likely, the request to
     * start lesson should be triggered by the flash component. This method will delegate to the Spring service bean to
     * complete all the steps for starting a lesson. Finally, a wddx acknowledgement message will be serialized and sent
     * back to the flash component.
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
	FlashMessage flashMessage = null;

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.startLesson(lessonId, getUserId());
	    flashMessage = new FlashMessage("startLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "startLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    public ActionForward newLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ParseException {
	String lessonName = request.getParameter("lessonName");
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	long ldId = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);

	boolean introEnable = WebUtil.readBooleanParam(request, "introEnable", false);
	String introDescription = introEnable ? request.getParameter("introDescription") : null;
	boolean introImage = introEnable && WebUtil.readBooleanParam(request, "introImage", false);
	boolean startMonitor = WebUtil.readBooleanParam(request, "startMonitor", false);
	boolean enableLiveEdit = WebUtil.readBooleanParam(request, "enableLiveEdit", false);
	boolean notificationsEnable = WebUtil.readBooleanParam(request, "notificationsEnable", false);
	boolean portfolioEnable = WebUtil.readBooleanParam(request, "portfolioEnable", false);
	boolean presenceEnable = WebUtil.readBooleanParam(request, "presenceEnable", false);
	boolean imEnable = WebUtil.readBooleanParam(request, "imEnable", false);
	Integer splitNumberLessons = WebUtil.readIntParam(request, "splitNumberLessons", true);
	boolean schedulingEnable = WebUtil.readBooleanParam(request, "schedulingEnable", false);
	Date schedulingDatetime = schedulingEnable ? MonitoringAction.LESSON_SCHEDULING_DATETIME_FORMAT.parse(request
		.getParameter("schedulingDatetime")) : null;

	boolean precedingLessonEnable = WebUtil.readBooleanParam(request, "precedingLessonEnable", false);
	Long precedingLessonId = WebUtil.readLongParam(request, "precedingLessonId", true);
	boolean timeLimitEnableField = WebUtil.readBooleanParam(request, "timeLimitEnableField", false);
	Integer timeLimitDaysField = WebUtil.readIntParam(request, "timeLimitDaysField", true);
	boolean timeLimitIndividualField = WebUtil.readBooleanParam(request, "timeLimitIndividualField", false);
	Integer timeLimitIndividual = timeLimitEnableField && timeLimitIndividualField ? timeLimitDaysField : null;
	Integer timeLimitLesson = timeLimitEnableField && !timeLimitIndividualField ? timeLimitDaysField : null;

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	IUserManagementService userManagementService = MonitoringServiceProxy.getUserManagementService(getServlet()
		.getServletContext());

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	Integer userId = getUserId();
	User user = (User) userManagementService.findById(User.class, userId);

	// parse comma delimited learner list and get the real users objects
	String learnersParam = request.getParameter("learners");
	List<User> learners = new ArrayList<User>();
	for (String learnerID : learnersParam.split(",")) {
	    // last one will be blank
	    if (!StringUtils.isBlank(learnerID)) {
		User learner = (User) userManagementService.findById(User.class, Integer.valueOf(learnerID));
		learners.add(learner);
	    }
	}
	String learnerGroupName = organisation.getName() + " learners";

	String staffParam = request.getParameter("monitors");
	List<User> staff = new ArrayList<User>();
	for (String staffMemberID : staffParam.split(",")) {
	    if (!StringUtils.isBlank(staffMemberID)) {
		User staffMemeber = (User) userManagementService.findById(User.class, Integer.valueOf(staffMemberID));
		staff.add(staffMemeber);
	    }
	}
	// add the creator as staff, if not already done
	if (!staff.contains(user)) {
	    staff.add(user);
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
	    Lesson lesson = monitoringService.initializeLesson(lessonInstanceName, introDescription, ldId,
		    organisationId, userId, null, introEnable, introImage, portfolioEnable, presenceEnable, imEnable,
		    enableLiveEdit, notificationsEnable, timeLimitIndividual, precedingLessonId);

	    monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation, learnerGroupInstanceName,
		    lessonInstanceLearners, staffGroupInstanceName, staff, userId);

	    if (!startMonitor) {
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
	    }
	}

	return null;
    }

    /**
     * The Struts dispatch method that starts a lesson on schedule that has been created beforehand.
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
     * @throws
     */
    public ActionForward startOnScheduleLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	FlashMessage flashMessage = null;

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	    String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_START_DATE);
	    Date startDate = DateUtil.convertFromLAMSFlashFormat(dateStr);

	    monitoringService.startLessonOnSchedule(lessonId, startDate, getUserId());
	    flashMessage = new FlashMessage("startOnScheduleLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "startOnScheduleLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    /**
     * The Struts dispatch method that finsh a lesson on schedule that has been started beforehand.
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
     * @throws
     */
    public ActionForward finishOnScheduleLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	FlashMessage flashMessage = null;

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    int scheduledNumberDaysToLessonFinish = WebUtil.readIntParam(request,
		    MonitoringConstants.PARAM_SCHEDULED_NUMBER_DAYS_TO_LESSON_FINISH);
	    monitoringService.finishLessonOnSchedule(lessonId, scheduledNumberDaysToLessonFinish, getUserId());
	    flashMessage = new FlashMessage("finishOnScheduleLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "finishOnScheduleLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    /**
     * The Struts dispatch method to archive a lesson. A wddx acknowledgement message will be serialized and sent back
     * to the flash component.
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
	FlashMessage flashMessage = null;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.archiveLesson(lessonId, getUserId());
	    flashMessage = new FlashMessage("archiveLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "archiveLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
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
	FlashMessage flashMessage = null;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.unarchiveLesson(lessonId, getUserId());
	    flashMessage = new FlashMessage("unarchiveLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "unarchiveLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
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
	FlashMessage flashMessage = null;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.suspendLesson(lessonId, getUserId());
	    flashMessage = new FlashMessage("suspendLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "suspendLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
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
	FlashMessage flashMessage = null;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.unsuspendLesson(lessonId, getUserId());
	    flashMessage = new FlashMessage("unsuspendLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "unsuspendLesson", monitoringService);
	}

	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;
    }

    /**
     * <P>
     * The STRUTS action will send back a WDDX message after marking the lesson by the given lesson ID as
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
	    HttpServletResponse response) throws IOException, ServletException {
	FlashMessage flashMessage = null;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.removeLesson(lessonId, getUserId());
	    flashMessage = new FlashMessage("removeLesson", Boolean.TRUE);
	} catch (Exception e) {
	    flashMessage = handleException(e, "removeLesson", monitoringService);
	}
	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
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
    public ActionForward removeLessonJson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException, ServletException {
	JSONObject jsonObject = new JSONObject();
	Object removeLessonResult = Boolean.TRUE.toString();
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());

	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    monitoringService.removeLesson(lessonId, getUserId());

	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "removeLesson", monitoringService);
	    removeLessonResult = flashMessage.getMessageValue();
	}

	jsonObject.put("removeLesson", removeLessonResult);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(jsonObject);
	return null;
    }

    /**
     * <P>
     * </P>
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
	FlashMessage flashMessage = null;
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

	// force complete
	try {
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    Integer learnerId = new Integer(WebUtil.readIntParam(request, MonitoringConstants.PARAM_LEARNER_ID));
	    Integer requesterId = getUserId();
	    String message = monitoringService.forceCompleteLessonByUser(learnerId, requesterId, lessonId, activityId);
	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Force complete for learner " + learnerId + " lesson " + lessonId + ". "
			+ message);
	    }
	    flashMessage = new FlashMessage("forceComplete", message);
	} catch (Exception e) {
	    flashMessage = handleException(e, "forceComplete", monitoringService);
	}
	String message = flashMessage.serializeMessage();

	PrintWriter writer = response.getWriter();
	writer.println(message);
	return null;

    }

    public ActionForward getLessonDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = monitoringService.getLessonDetails(lessonID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLessonDetails", monitoringService).serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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
	    wddxPacket = monitoringService.getLearningDesignDetails(lessonID);
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLearningDesignDetails", monitoringService).serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    /** Get the learner progress data for all learners in a lesson. This is called by the sequence tab in monitoring */
    public ActionForward getAllLearnersProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = WebUtil.readLongParam(request, "lessonID", false);
	    wddxPacket = monitoringService.getAllLearnersProgress(lessonID, getUserId(), false);
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getAllLearnersProgress", monitoringService).serializeMessage();
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
	if (module.equals("wizard")) {
	    // pre-existing flash app (AS 2.0) labels
	    languageCollection.add(new String("sys.error.msg.start"));
	    languageCollection.add(new String("sys.error"));
	    languageCollection.add(new String("wizardTitle.x.lbl"));
	    languageCollection.add(new String("finish.btn"));
	    languageCollection.add(new String("title.lbl"));
	    languageCollection.add(new String("desc.lbl"));
	    languageCollection.add(new String("learner.lbl"));
	    languageCollection.add(new String("staff.lbl"));
	    languageCollection.add(new String("al.alert"));
	    languageCollection.add(new String("al.ok"));
	    languageCollection.add(new String("al.validation.msg1"));
	    languageCollection.add(new String("al.validation.msg2"));
	    languageCollection.add(new String("al.validation.msg3.2"));
	    languageCollection.add(new String("al.validation.schtime"));
	    languageCollection.add(new String("learners.group.name"));
	    languageCollection.add(new String("staff.group.name"));
	    languageCollection.add(new String("wizard.splitLearners.splitSum"));
	    languageCollection.add(new String("wizard.splitLearners.LearnersPerLesson.lbl"));
	    languageCollection.add(new String("wizard.splitLearners.cb.lbl"));
	    languageCollection.add(new String("wizard.learner.expp.cb.lbl"));
	    languageCollection.add(new String("wizard.learner.enLiveEdit.cb.lbl"));
	    languageCollection.add(new String("wizard.learner.enpres.cb.lbl"));
	    languageCollection.add(new String("wizard.wkspc.date.modified.lbl"));
	    languageCollection.add(new String("summery.desc.lbl"));

	    // monitoring webapp existing labels
	    languageCollection.add(new String("error.system.error"));
	    languageCollection.add(new String("button.ok"));

	    // new flex (CloudWizard) labels
	    languageCollection.add(new String("add.lesson.panel.title"));
	    languageCollection.add(new String("lesson.tab.label"));
	    languageCollection.add(new String("class.tab.label"));
	    languageCollection.add(new String("advanced.tab.label"));
	    languageCollection.add(new String("lesson.tab.heading.label"));
	    languageCollection.add(new String("add.now.button.label"));
	    languageCollection.add(new String("advanced.tab.form.advanced.options.label"));
	    languageCollection.add(new String("advanced.tab.enable.lesson.notifications"));
	    languageCollection.add(new String("advanced.tab.form.enable.im.label"));
	    languageCollection.add(new String("advanced.tab.form.time.limits.label"));
	    languageCollection.add(new String("advanced.tab.form.enter.number.days.label"));
	    languageCollection.add(new String("advanced.tab.form.individual.not.entire.group.label"));
	    languageCollection.add(new String("advanced.tab.form.scheduling.label"));
	    languageCollection.add(new String("advanced.tab.form.enable.label"));
	    languageCollection.add(new String("advanced.tab.form.validation.no.learners.error"));
	    languageCollection.add(new String("advanced.tab.form.item.split.lessons.label"));
	    languageCollection.add(new String("advanced.tab.form.details.label"));
	    languageCollection.add(new String("class.tab.available.label"));
	    languageCollection.add(new String("class.tab.selected.label"));
	    languageCollection.add(new String("class.tab.print.name.label"));
	    languageCollection.add(new String("class.tab.heading.label"));
	    languageCollection.add(new String("advanced.tab.form.validation.schedule.date.error"));
	    languageCollection.add(new String("conditions.tab.label"));
	    languageCollection.add(new String("conditions.tab.form.preceding.label"));
	    languageCollection.add(new String("conditions.tab.form.preceding.info.label"));
	    languageCollection.add(new String("advanced.tab.form.time.limits.info.label"));
	    languageCollection.add(new String("advanced.tab.enable.lesson.intro"));
	    languageCollection.add(new String("advanced.tab.display.design.image"));

	} else if (module.equals("timechart")) {

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

	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	if (module.equals("wizard") && (orgId != null)) {

	    Organisation organisation = monitoringService.getOrganisation(orgId);

	    int count = 0;
	    Set<Lesson> organisationLessons = new TreeSet<Lesson>(new LessonComparator());
	    organisationLessons.addAll(organisation.getLessons());
	    for (Lesson lesson : organisationLessons) {
		if (!Lesson.REMOVED_STATE.equals(lesson.getLessonStateId())
			&& !Lesson.FINISHED_STATE.equals(lesson.getLessonStateId())) {
		    languageOutput += "<entry key='lessonID" + count++ + "'>" + "<name>" + lesson.getLessonName()
			    + "</name>" + "<data>" + lesson.getLessonId() + "</data>" + "</entry>";
		}
	    }

	    // // sort lessons
	    // TreeSet<Timezone> lessons = new TreeSet<Timezone>(new TimezoneComparator());
	    // lessons.addAll(getTimezoneService().getDefaultTimezones());

	    // let Flex know the number of lessons
	    languageOutput += "<entry key='lessonsNumber'><data>" + count + "</data></entry>";

	    languageOutput += "<entry key='orgName'><name>" + organisation.getName() + "</name></entry>";
	}

	languageOutput += "</language></xml>";

	response.setContentType("text/xml");
	response.setCharacterEncoding("UTF-8");
	response.getWriter().print(languageOutput);

	return null;
    }

    public ActionForward getAllCompletedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = WebUtil.readLongParam(request, "lessonID", false);
	    Long learnerID = WebUtil.readLongParam(request, "learnerID", true);
	    wddxPacket = monitoringService.getAllCompletedActivities(lessonID, learnerID, getUserId());

	} catch (Exception e) {
	    wddxPacket = handleException(e, "getAllLearnersProgress", monitoringService).serializeMessage();
	}

	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;

    }

    /**
     * Get the first batch of learner progress data learners in a lesson. This is called by the learner progress tab in
     * monitoring
     */
    public ActionForward getInitialLearnersProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = WebUtil.readLongParam(request, "lessonID", false);
	    wddxPacket = monitoringService.getInitialLearnersProgress(lessonID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getInitialLearnersProgress", monitoringService).serializeMessage();
	}

	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    /**
     * Get a followup batch of learner progress data learners in a lesson. This is called by the learner progress tab in
     * monitoring
     */
    public ActionForward getAdditionalLearnersProgress(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	String wddxPacket;
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	try {
	    Long lessonID = WebUtil.readLongParam(request, "lessonID", false);
	    Integer lastUserID = WebUtil.readIntParam(request, "lastUserID", false);
	    wddxPacket = monitoringService.getAdditionalLearnersProgress(lessonID, lastUserID, getUserId());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getAdditionalLearnersProgress", monitoringService).serializeMessage();
	}

	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward getAllContributeActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	String wddxPacket = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = monitoringService.getAllContributeActivities(lessonID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "getAllContributeActivities", monitoringService);
	    wddxPacket = flashMessage.serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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

	String url = monitoringService.getLearnerActivityURL(lessonID, activityID, learnerUserID, getUserId());
	return redirectToURL(mapping, response, url);
    }

    /** Calls the server to bring up the activity's monitoring page. Assumes destination is a new window */
    public ActionForward getActivityMonitorURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, LamsToolServiceException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));
	String contentFolderID = WebUtil.readStrParam(request, "contentFolderID");
	String url = monitoringService.getActivityMonitorURL(lessonID, activityID, contentFolderID, getUserId());

	return redirectToURL(mapping, response, url);
    }

    /** Calls the server to bring up the activity's define later page. Assumes destination is a new window */
    public ActionForward getActivityDefineLaterURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, LamsToolServiceException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	Long lessonID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID));

	String url = monitoringService.getActivityDefineLaterURL(lessonID, activityID, getUserId());
	return redirectToURL(mapping, response, url);
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

    public ActionForward renameLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	String wddxPacket = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    String name = WebUtil.readStrParam(request, "name");
	    wddxPacket = monitoringService.renameLesson(lessonID, name, userID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "renameLesson", monitoringService);
	    wddxPacket = flashMessage.serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward checkGateStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	String wddxPacket = null;
	try {
	    Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    wddxPacket = monitoringService.checkGateStatus(activityID, lessonID);
	} catch (Exception e) {
	    FlashMessage flashMessage = handleException(e, "checkGateStatus", monitoringService);
	    wddxPacket = flashMessage.serializeMessage();
	}
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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

	    if (new Long(lessonID) != null) {

		monitoringService.createPreviewClassForLesson(userID, lessonID);
		monitoringService.startLesson(lessonID, getUserId());

		flashMessage = new FlashMessage("startPreviewSession", new Long(lessonID));

	    } else {
		flashMessage = handleCriticalError("startPreviewSession", "error.system.error", monitoringService);
	    }

	} catch (Exception e) {
	    flashMessage = handleException(e, "startPreviewSession", monitoringService);
	}

	PrintWriter writer = response.getWriter();
	writer.println(flashMessage.serializeMessage());
	return null;
    }

    /**
     * Delete all old preview lessons and their related data, across all organisations. Should go to a monitoring
     * webservice maybe ?
     */
    public ActionForward deleteOldPreviewLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	int numDeleted = monitoringService.deleteAllOldPreviewLessons();
	request.setAttribute(MonitoringAction.NUM_DELETED, Integer.toString(numDeleted));
	return mapping.findForward(MonitoringAction.PREVIEW_DELETED_REPORT_SCREEN);
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

    /**
     * Set whether or not the export portfolio button is available in learner. Expects parameters lessonID and
     * learnerExportPortfolio.
     */
    public ActionForward learnerExportPortfolioAvailable(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	FlashMessage flashMessage = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    Boolean learnerExportPortfolioAvailable = WebUtil
		    .readBooleanParam(request, "learnerExportPortfolio", false);
	    monitoringService.setLearnerPortfolioAvailable(lessonID, userID, learnerExportPortfolioAvailable);

	    flashMessage = new FlashMessage("learnerExportPortfolioAvailable", "learnerExportPortfolioAvailable");
	} catch (Exception e) {
	    flashMessage = handleException(e, "renameLesson", monitoringService);
	}
	String wddxPacket = flashMessage.serializeMessage();
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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
	FlashMessage flashMessage = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    Boolean presenceAvailable = WebUtil.readBooleanParam(request, "presenceAvailable", false);

	    monitoringService.setPresenceAvailable(lessonID, userID, presenceAvailable);

	    if (!presenceAvailable) {
		monitoringService.setPresenceImAvailable(lessonID, userID, false);
	    }

	    flashMessage = new FlashMessage("presenceAvailable", "presenceAvailable");
	} catch (Exception e) {
	    flashMessage = handleException(e, "presenceAvailable", monitoringService);
	}
	String wddxPacket = flashMessage.serializeMessage();
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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
	FlashMessage flashMessage = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    Boolean presenceImAvailable = WebUtil.readBooleanParam(request, "presenceImAvailable", false);
	    monitoringService.setPresenceImAvailable(lessonID, userID, presenceImAvailable);

	    flashMessage = new FlashMessage("presenceImAvailable", "presenceImAvailable");
	} catch (Exception e) {
	    flashMessage = handleException(e, "presenceImAvailable", monitoringService);
	}
	String wddxPacket = flashMessage.serializeMessage();
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    /**
     * Set whether or not the presence available button is available in learner. Expects parameters lessonID and
     * presenceAvailable.
     */
    public ActionForward liveEditAvailable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	FlashMessage flashMessage = null;
	try {
	    Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
	    Integer userID = getUserId();
	    Boolean liveEditAvailable = WebUtil.readBooleanParam(request, "liveEditAvailable", false);
	    monitoringService.setLiveEditEnabled(lessonID, userID, liveEditAvailable);

	    flashMessage = new FlashMessage("liveEditAvailable", "liveEditAvailable");
	} catch (Exception e) {
	    flashMessage = handleException(e, "liveEditAvailable", monitoringService);
	}
	String wddxPacket = flashMessage.serializeMessage();
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
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
	    monitoringService.openTimeChart(lessonID, getUserId());

	    request.setAttribute("lessonID", lessonID);
	    request.setAttribute("learnerID", WebUtil.readLongParam(request, "learnerID", true));

	} catch (Exception e) {
	    request.setAttribute("errorName", "MonitoringAction");
	    request.setAttribute("errorMessage", e.getMessage());

	    return mapping.findForward(MonitoringAction.ERROR);
	}

	return mapping.findForward(MonitoringAction.TIME_CHART_SCREEN);
    }
}
