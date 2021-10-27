package org.lamsfoundation.lams.webservice.xml;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.OutputType;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

@SuppressWarnings("serial")
public class LessonManagerServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(LessonManagerServlet.class);

    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IMonitoringService monitoringService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private IExportToolContentService exportToolContentService;
    @Autowired
    private ILamsCoreToolService lamsCoreToolService;
    @Autowired
    private IGradebookService gradebookService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ISecurityService securityService;

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	response.setContentType("text/xml");
	response.setCharacterEncoding("UTF-8");

	String serverId = request.getParameter(CentralConstants.PARAM_SERVER_ID);
	String datetime = request.getParameter(CentralConstants.PARAM_DATE_TIME);
	String hashValue = request.getParameter(CentralConstants.PARAM_HASH_VALUE);
	String username = request.getParameter(CentralConstants.PARAM_USERNAME);
	String courseId = request.getParameter(CentralConstants.PARAM_COURSE_ID);
	String ldIdStr = request.getParameter(CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String lsIdStr = request.getParameter(CentralConstants.PARAM_LESSON_ID);
	String country = request.getParameter(CentralConstants.PARAM_COUNTRY);
	String title = request.getParameter(CentralConstants.PARAM_TITLE);
	String desc = request.getParameter(CentralConstants.PARAM_DESC);
	String startDate = request.getParameter(CentralConstants.PARAM_STARTDATE);
	String locale = request.getParameter(CentralConstants.PARAM_LANG);
	String method = request.getParameter(CentralConstants.PARAM_METHOD);
	String filePath = request.getParameter(CentralConstants.PARAM_FILEPATH);
	String outputsUser = request.getParameter("outputsUser");
	String userIds = request.getParameter(CentralConstants.PARAM_USER_IDS);
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	String monitorIds = request.getParameter(CentralConstants.PARAM_MONITOR_IDS);

	// Custom CSV string to be used for tool adapters
	String customCSV = request.getParameter(CentralConstants.PARAM_CUSTOM_CSV);

	// parameters used for creating user at joinLesson method
	String firstNames = request.getParameter("firstNames");
	String lastNames = request.getParameter("lastNames");
	String emails = request.getParameter("emails");

	String presenceEnableString = WebUtil.readStrParam(request, CentralConstants.PARAM_LEARNER_PRESENCE_ENABLE,
		true);
	Boolean presenceEnable = presenceEnableString == null ? null : Boolean.valueOf(presenceEnableString);

	String imEnableString = WebUtil.readStrParam(request, CentralConstants.PARAM_LEARNER_IM_ENABLE, true);
	Boolean imEnable = imEnableString == null ? null : Boolean.valueOf(imEnableString);

	String enableNotificationString = WebUtil.readStrParam(request, CentralConstants.PARAM_ENABLE_NOTIFICATIONS,
		true);
	Boolean enableNotifications = enableNotificationString == null ? null
		: Boolean.valueOf(enableNotificationString);

	String allowLearnerRestartString = WebUtil.readStrParam(request, CentralConstants.PARAM_ALLOW_LEARNER_RESTART,
		true);
	// whether this lesson was created with this option ON
	boolean enforceAllowLearnerRestart = StringUtils.isNotBlank(allowLearnerRestartString)
		&& Boolean.valueOf(allowLearnerRestartString);

	Long ldId = null;
	Long lsId = null;
	ServletOutputStream outputStream = null;
	try {
	    // TODO check input parameters are valid.

	    // Create xml document
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.newDocument();

	    Element element = null;
	    if ((hashValue == null) || hashValue.equals("")) {
		throw new NullPointerException("Hash value missing in parameters");
	    }

	    if (method.equals(CentralConstants.METHOD_START)) {
		ldId = Long.parseLong(ldIdStr);
		Long lessonId = startLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			enforceAllowLearnerRestart, country, locale, customCSV, presenceEnable, imEnable,
			enableNotifications);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_PREVIEW)) {
		ldId = Long.parseLong(ldIdStr);
		Long lessonId = startPreview(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			country, locale, customCSV, presenceEnable, imEnable);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_SCHEDULE)) {
		ldId = Long.parseLong(ldIdStr);
		Long lessonId = scheduleLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			enforceAllowLearnerRestart, startDate, country, locale, customCSV, presenceEnable, imEnable,
			enableNotifications);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_CLONE)) {

		lsId = Long.parseLong(lsIdStr);
		Long lessonId = cloneLesson(serverId, datetime, hashValue, username, lsId, courseId);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_DELETE)
		    || method.equals(CentralConstants.METHOD_REMOVE_LESSON)) {
		verifyPostRequestMethod(request);

		lsId = Long.parseLong(lsIdStr);
		Boolean deleted = removeLesson(serverId, datetime, hashValue, username, lsId);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lsId.toString());
		element.setAttribute(CentralConstants.ATTR_DELETED, deleted.toString());

	    } else if (method.equals(CentralConstants.METHOD_REMOVE_ALL_LESSONS)) {
		verifyPostRequestMethod(request);

		element = removeAllLessons(document, serverId, datetime, hashValue, username, courseId);

	    } else if (method.equals(CentralConstants.METHOD_REMOVE_USER)) {
		verifyPostRequestMethod(request);

		lsId = Long.parseLong(lsIdStr);
		element = removeUser(document, serverId, datetime, hashValue, username, lsId, userIds);

	    } else if (method.equals(CentralConstants.METHOD_REMOVE_ALL_USERS)) {
		verifyPostRequestMethod(request);

		lsId = Long.parseLong(lsIdStr);
		element = removeAllUsers(document, serverId, datetime, hashValue, username, lsId);

	    } else if (method.equals(CentralConstants.METHOD_STUDENT_PROGRESS)) {
		lsId = Long.parseLong(lsIdStr);
		element = getAllStudentProgress(document, serverId, datetime, hashValue, username, lsId, courseId);

	    } else if (method.equals(CentralConstants.METHOD_SINGLE_STUDENT_PROGRESS)) {
		String firstName = request.getParameter(IntegrationConstants.PARAM_FIRST_NAME);
		String lastName = request.getParameter(IntegrationConstants.PARAM_LAST_NAME);
		String email = request.getParameter(IntegrationConstants.PARAM_EMAIL);

		lsId = Long.parseLong(lsIdStr);
		element = getSingleStudentProgress(document, serverId, datetime, hashValue, username, firstName,
			lastName, locale, country, email, lsId, courseId);

	    } else if (method.equals(CentralConstants.METHOD_IMPORT)) {
		Long ldID = importLearningDesign(request, response, filePath, username, serverId, customCSV);

		element = document.createElement(CentralConstants.ELEM_LEARNINGDESIGN);
		element.setAttribute(CentralConstants.PARAM_LEARNING_DESIGN_ID, ldID.toString());

	    } else if (method.equals(CentralConstants.METHOD_JOIN_LESSON)) {
		lsId = Long.parseLong(lsIdStr);
		Thread t = new Thread(new AddUsersToLessonThread(serverId, datetime, username, hashValue, lsId,
			courseId, locale, country, learnerIds, monitorIds, firstNames, lastNames, emails, request));
		t.start();

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lsIdStr);

	    } else if (method.equals("gradebookMarksUser")) {
		lsId = Long.parseLong(lsIdStr);
		element = getGradebookMarks(document, serverId, datetime, hashValue, username, lsId, null, outputsUser);

	    } else if (method.equals("gradebookMarksLesson")) {
		lsId = Long.parseLong(lsIdStr);
		element = getGradebookMarks(document, serverId, datetime, hashValue, username, lsId, null, null);

	    } else if (method.equals("gradebookMarksCourse")) {
		element = getGradebookMarks(document, serverId, datetime, hashValue, username, null, courseId, null);

	    } else if (method.equals("toolOutputsAllUsers")) {
		lsId = Long.parseLong(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, false,
			null);

	    } else if (method.equals("authoredToolOutputsAllUsers")) {
		lsId = Long.parseLong(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, true, null);

	    } else if (method.equals("toolOutputsUser")) {
		lsId = Long.parseLong(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, false,
			outputsUser);

	    } else if (method.equals("authoredToolOutputsUser")) {
		lsId = Long.parseLong(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, true,
			outputsUser);

	    } else if (method.equals(CentralConstants.METHOD_CHECK_LESSON_FOR_NUMERIC_TOOL_OUTPUTS)) {
		lsId = Long.parseLong(lsIdStr);
		element = checkLessonForNumericToolOutputs(document, serverId, datetime, hashValue, username, lsId);

	    } else if (method.equals(CentralConstants.METHOD_VERIFY_EXT_SERVER)) {
		verify(serverId, datetime, hashValue);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("1");
		out.flush();
		out.close();
		return;

	    } else if (method.equals(CentralConstants.METHOD_LIST_MONITOR)) {
		element = getLessonMonitorList(document, serverId, datetime, hashValue, username, courseId, country,
			locale);
	    } else {
		String msg = "Method :" + method + " is not recognised";
		log.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
	    }

	    document.appendChild(element);

	    // write out the xml document.
	    outputStream = response.getOutputStream();
	    DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
	    LSSerializer lsSerializer = domImplementation.createLSSerializer();
	    LSOutput lsOutput = domImplementation.createLSOutput();
	    lsOutput.setEncoding("UTF-8");
	    lsOutput.setByteStream(outputStream);
	    lsSerializer.write(document, lsOutput);

	} catch (NumberFormatException nfe) {
	    log.error("lsId or ldId is not an integer" + lsIdStr + ldIdStr, nfe);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "lsId or ldId is not an integer");
	} catch (TransformerConfigurationException e) {
	    log.error("Can not convert XML document to string", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (TransformerException e) {
	    log.error("Can not convert XML document to string", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (ParserConfigurationException e) {
	    log.error("Can not build XML document", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (NullPointerException e) {
	    log.error("Missing parameters", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (Exception e) {
	    log.error("Problem loading learning manager servlet request", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

	} finally {
	    if (outputStream != null) {
		outputStream.flush();
		outputStream.close();

	    }
	}

    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request
     *            the request send by the client to the server
     * @param response
     *            the response send by the server to the client
     * @throws ServletException
     *             if an error occurred
     * @throws IOException
     *             if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    private Long startLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, boolean enforceAllowLearnerRestart, String countryIsoCode,
	    String langIsoCode, String customCSV, Boolean presenceEnable, Boolean imEnable, Boolean enableNotifications)
	    throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, null,
		    IntegrationConstants.METHOD_MONITOR);
	    User user = userMap.getUser();
	    Organisation organisation = orgMap.getOrganisation();

	    // 1. init lesson
	    Lesson lesson = monitoringService.initializeLesson(title, desc, ldId, organisation.getOrganisationId(),
		    user.getUserId(), customCSV, false, false,
		    presenceEnable == null ? extServer.getLearnerPresenceAvailable() : presenceEnable,
		    imEnable == null ? extServer.getLearnerImAvailable() : imEnable, extServer.getLiveEditEnabled(),
		    enableNotifications == null ? extServer.getEnableLessonNotifications() : enableNotifications,
		    extServer.getForceLearnerRestart(),
		    enforceAllowLearnerRestart ? true : extServer.getAllowLearnerRestart(),
		    extServer.getGradebookOnComplete(), null, null);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, organisation, user);
	    // 3. start lesson
	    monitoringService.startLesson(lesson.getLessonId(), user.getUserId());
	    // store information which extServer has started the lesson
	    integrationService.createExtServerLessonMap(lesson.getLessonId(), extServer);

	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    private Long scheduleLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, boolean enforceAllowLearnerRestart, String startDate,
	    String countryIsoCode, String langIsoCode, String customCSV, Boolean presenceEnable, Boolean imEnable,
	    Boolean enableNotifications) throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, null,
		    IntegrationConstants.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = monitoringService.initializeLesson(title, desc, ldId,
		    orgMap.getOrganisation().getOrganisationId(), userMap.getUser().getUserId(), customCSV, false,
		    false, presenceEnable == null ? extServer.getLearnerPresenceAvailable() : presenceEnable,
		    imEnable == null ? extServer.getLearnerImAvailable() : imEnable, extServer.getLiveEditEnabled(),
		    enableNotifications == null ? extServer.getEnableLessonNotifications() : enableNotifications,
		    extServer.getForceLearnerRestart(),
		    enforceAllowLearnerRestart ? true : extServer.getAllowLearnerRestart(),
		    extServer.getGradebookOnComplete(), null, null);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, orgMap.getOrganisation(), userMap.getUser());
	    // 3. schedule lesson
	    Date date = DateUtil.convertFromString(startDate, DateUtil.SCHEDULE_LESSON_FORMAT);
	    monitoringService.startLessonOnSchedule(lesson.getLessonId(), date, userMap.getUser().getUserId());
	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    private Long cloneLesson(String serverId, String datetime, String hashValue, String username, long lsId,
	    String courseId) throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);

	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    Integer creatorId = userMap.getUser().getUserId();

	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, null,
		    IntegrationConstants.METHOD_MONITOR);
	    if (orgMap == null) {
		log.debug("No course exists for: " + courseId + ". Can't delete any lessons.");
		throw new Exception("Course with courseId: " + courseId + " could not be found");
	    }
	    Organisation organisation = orgMap.getOrganisation();

	    // clone lesson
	    Long newLessonId = monitoringService.cloneLesson(lsId, creatorId, true, true, null, null, organisation);
	    // store information which extServer has started the lesson
	    integrationService.createExtServerLessonMap(newLessonId, extServer);

	    return newLessonId;
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    @SuppressWarnings("unchecked")
    private Element getAllStudentProgress(Document document, String serverId, String datetime, String hashValue,
	    String username, long lsId, String courseID) throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    Lesson lesson = lessonService.getLesson(lsId);

	    Element element = document.createElement(CentralConstants.ELEM_LESSON_PROGRESS);
	    element.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);

	    String prefix = extServer.getPrefix();
	    if (lesson != null) {

		int activitiesTotal = lesson.getLearningDesign().getActivities().size();
		Iterator<LearnerProgress> iterator = lesson.getLearnerProgresses().iterator();
		while (iterator.hasNext()) {
		    LearnerProgress learnProg = iterator.next();
		    LearnerProgressDTO learnerProgress = learnProg.getLearnerProgressData();

		    // get the username with the integration prefix removed
		    String userNoPrefixName = learnerProgress.getUserName().substring(prefix.length() + 1);
		    ExtUserUseridMap learnerMap = integrationService.getExtUserUseridMap(extServer, userNoPrefixName);

		    Element learnerProgElem = document.createElement(CentralConstants.ELEM_LEARNER_PROGRESS);

		    int completedActivities = learnerProgress.getCompletedActivities().length;
		    int attemptedActivities = learnerProgress.getAttemptedActivities().length;

		    if (learnerProgElem.getNodeType() == Node.ELEMENT_NODE) {
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_COMPLETE,
				"" + learnerProgress.getLessonComplete());
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITY_COUNT, "" + activitiesTotal);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_COMPLETED,
				"" + completedActivities);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_ATTEMPTED,
				"" + attemptedActivities);
			// learnerProgElem.setAttribute(CentralConstants.ATTR_CURRENT_ACTIVITY , currActivity);
			learnerProgElem.setAttribute(CentralConstants.ATTR_STUDENT_ID, "" + learnerMap.getSid());
			learnerProgElem.setAttribute(CentralConstants.ATTR_COURSE_ID, courseID);
			learnerProgElem.setAttribute(CentralConstants.ATTR_USERNAME, userNoPrefixName);
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);
		    }

		    element.appendChild(learnerProgElem);
		}
	    } else {
		throw new Exception("Lesson with lessonID: " + lsId + " could not be found for learner progresses");
	    }

	    return element;

	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}

    }

    private Element getSingleStudentProgress(Document document, String serverId, String datetime, String hashValue,
	    String username, String firstName, String lastName, String locale, String country, String email, long lsId,
	    String courseID) throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    Lesson lesson = lessonService.getLesson(lsId);

	    Element element = document.createElement(CentralConstants.ELEM_LESSON_PROGRESS);
	    element.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);

	    if (lesson != null) {
		int activitiesTotal = lesson.getLearningDesign().getActivities().size();

		// create new user if required
		final boolean usePrefix = true;
		final boolean isUpdateUserDetails = false;
		ExtUserUseridMap userMap = integrationService.getImplicitExtUserUseridMap(extServer, username,
			firstName, lastName, locale, country, email, usePrefix, isUpdateUserDetails);

		LearnerProgress learnProg = lessonService.getUserProgressForLesson(userMap.getUser().getUserId(), lsId);

		Element learnerProgElem = document.createElement(CentralConstants.ELEM_LEARNER_PROGRESS);

		// if learner progress exists, make a response, otherwise, return an empty learner progress element
		if (learnProg != null) {
		    LearnerProgressDTO learnerProgress = learnProg.getLearnerProgressData();

		    int completedActivities = learnerProgress.getCompletedActivities().length;
		    int attemptedActivities = learnerProgress.getAttemptedActivities().length;

		    if (learnerProgElem.getNodeType() == Node.ELEMENT_NODE) {
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_COMPLETE,
				"" + learnerProgress.getLessonComplete());
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITY_COUNT, "" + activitiesTotal);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_COMPLETED,
				"" + completedActivities);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_ATTEMPTED,
				"" + attemptedActivities);
			// learnerProgElem.setAttribute(CentralConstants.ATTR_CURRENT_ACTIVITY , currActivity);
			learnerProgElem.setAttribute(CentralConstants.ATTR_STUDENT_ID, "" + userMap.getSid());
			learnerProgElem.setAttribute(CentralConstants.ATTR_COURSE_ID, courseID);
			learnerProgElem.setAttribute(CentralConstants.ATTR_USERNAME, username);
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);
		    }
		} else {
		    if (learnerProgElem.getNodeType() == Node.ELEMENT_NODE) {
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_COMPLETE, "false");
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITY_COUNT, "" + activitiesTotal);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_COMPLETED, "0");
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_ATTEMPTED, "0");
			// learnerProgElem.setAttribute(CentralConstants.ATTR_CURRENT_ACTIVITY , currActivity);
			learnerProgElem.setAttribute(CentralConstants.ATTR_STUDENT_ID, "" + userMap.getSid());
			learnerProgElem.setAttribute(CentralConstants.ATTR_COURSE_ID, courseID);
			learnerProgElem.setAttribute(CentralConstants.ATTR_USERNAME, username);
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);
		    }
		}

		element.appendChild(learnerProgElem);
	    } else {
		throw new Exception("Lesson with lessonID: " + lsId + " could not be found for learner progresses");
	    }

	    return element;

	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}

    }

    private boolean removeLesson(String serverId, String datetime, String hashValue, String username, long lsId)
	    throws RemoteException {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    monitoringService.removeLesson(lsId, userMap.getUser().getUserId());
	    return true;
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    /**
     * Deletes all users from the specified course.
     */
    private Element removeAllLessons(Document document, String serverId, String datetime, String hashValue,
	    String username, String courseId) throws Exception {
	// Create the root node of the xml document
	Element lessonsElement = document.createElement("Lessons");

	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);

	// find all lesons in organisation
	ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer.getSid(), courseId);
	if (orgMap == null) {
	    log.debug("No course exists for: " + courseId + ". Can't delete any lessons.");
	    throw new Exception("Course with courseId: " + courseId + " could not be found");
	}
	Integer organisationId = orgMap.getOrganisation().getOrganisationId();
	List<Lesson> lessons = lessonService.getLessonsByGroup(organisationId);

	if (lessons != null) {
	    for (Lesson lesson : lessons) {

		Long lessonId = lesson.getLessonId();

		// remove lesson
		monitoringService.removeLesson(lessonId, userMap.getUser().getUserId());

		// add lessonId to output xml document
		Element lessonElement = document.createElement(CentralConstants.ELEM_LESSON);
		lessonElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lessonId);
		lessonElement.setAttribute(CentralConstants.ATTR_DELETED, "true");
		lessonsElement.appendChild(lessonElement);
	    }
	}

	return lessonsElement;
    }

    /**
     * Deletes all lessons from the specified course.
     */
    private Element removeUser(Document document, String serverId, String datetime, String hashValue, String username,
	    long lsId, String userIds) throws Exception {
	// Create the root node of the xml document
	Element usersElement = document.createElement("Users");

	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	// check is user monitor
	ExtUserUseridMap monitorMap = integrationService.getExtUserUseridMap(extServer, username);
	securityService.isLessonMonitor(lsId, monitorMap.getUser().getUserId(), "remove user", true);

	// remove requested user
	String[] extUsernames = (userIds != null) ? userIds.split(",") : new String[0];
	for (String extUsername : extUsernames) {

	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, extUsername);
	    Integer userId = userMap.getUser().getUserId();
	    // remove user
	    lessonService.removeLearner(lsId, userId);

	    // add userId to output xml document
	    Element lessonElement = document.createElement("User");
	    lessonElement.setAttribute(CentralConstants.PARAM_USER_ID, "" + userId);
	    lessonElement.setAttribute(CentralConstants.ATTR_DELETED, "true");
	    usersElement.appendChild(lessonElement);
	}

	return usersElement;
    }

    /**
     * Deletes all lessons from the specified course.
     */
    private Element removeAllUsers(Document document, String serverId, String datetime, String hashValue,
	    String username, long lsId) throws Exception {
	// Create the root node of the xml document
	Element usersElement = document.createElement("Users");

	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	// check is user monitor
	ExtUserUseridMap monitorMap = integrationService.getExtUserUseridMap(extServer, username);
	securityService.isLessonMonitor(lsId, monitorMap.getUser().getUserId(), "remove all users", true);

	// remove all users from the specified lesson
	Lesson lesson = lessonService.getLesson(lsId);
	Set<User> users = lesson.getAllLearners();
	if (users != null) {
	    for (User user : users) {

		Integer userId = user.getUserId();
		// remove user
		lessonService.removeLearner(lsId, userId);

		// add userId to output xml document
		Element lessonElement = document.createElement("User");
		lessonElement.setAttribute(CentralConstants.PARAM_USER_ID, "" + userId);
		lessonElement.setAttribute(CentralConstants.ATTR_DELETED, "true");
		usersElement.appendChild(lessonElement);
	    }
	}

	return usersElement;
    }

    private Long startPreview(String serverId, String datetime, String hashValue, String username, Long ldId,
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV,
	    Boolean presenceEnable, Boolean imEnable) throws RemoteException {

	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, username, hashValue);
	    ExtUserUseridMap userMap = integrationService.getExtUserUseridMap(extServer, username);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseId, null,
		    IntegrationConstants.METHOD_MONITOR);
	    Integer userId = userMap.getUser().getUserId();

	    // 1. init lesson
	    Lesson lesson = monitoringService.initializeLessonForPreview(title, desc, ldId, userId, customCSV,
		    presenceEnable == null ? extServer.getLearnerPresenceAvailable() : presenceEnable,
		    imEnable == null ? extServer.getLearnerImAvailable() : imEnable, extServer.getLiveEditEnabled());
	    // 2. create lessonClass for lesson
	    monitoringService.createPreviewClassForLesson(userId, lesson.getLessonId());

	    // 3. start lesson
	    monitoringService.startLesson(lesson.getLessonId(), userId);

	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}

    }

    @SuppressWarnings("unchecked")
    private Long importLearningDesign(HttpServletRequest request, HttpServletResponse response, String filePath,
	    String username, String serverId, String customCSV) throws RemoteException {

	List<String> ldErrorMsgs = new ArrayList<>();
	List<String> toolsErrorMsgs = new ArrayList<>();
	Long ldId = null;
	Integer workspaceFolderUid = null;
	ExtUserUseridMap userMap;
	User user = null;
	ExtServer extServer = integrationService.getExtServer(serverId);

	try {

	    userMap = integrationService.getExtUserUseridMap(extServer, username);

	    user = userMap.getUser();

	    HttpSession ss = SessionManager.getSession();
	    boolean createdTemporarySession = false;
	    if (ss == null) {
		// import requires a session containing the user details, so dummy it up here.
		SessionManager.startSession(request);
		ss = SessionManager.getSession();
		ss.setAttribute(AttributeNames.USER, user.getUserDTO());
		createdTemporarySession = true;
	    }

	    File designFile = new File(filePath);
	    Object[] ldResults = exportToolContentService.importLearningDesign(designFile, user, workspaceFolderUid,
		    toolsErrorMsgs, customCSV);
	    ldId = (Long) ldResults[0];
	    ldErrorMsgs = (List<String>) ldResults[1];
	    toolsErrorMsgs = (List<String>) ldResults[2];

	    if (createdTemporarySession) {
		SessionManager.endSession();
	    }

	    return ldId;
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}

    }

    @SuppressWarnings("unchecked")
    private void createLessonClass(Lesson lesson, Organisation organisation, User creator) {
	List<User> staffList = new LinkedList<>();
	staffList.add(creator);
	List<User> learnerList = new LinkedList<>();
	Vector<User> learnerVector = userManagementService
		.getUsersFromOrganisationByRole(organisation.getOrganisationId(), Role.LEARNER, true);
	learnerList.addAll(learnerVector);
	monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
		organisation.getName() + " learners", learnerList, organisation.getName() + " staff", staffList,
		creator.getUserId());
    }

    private class AddUsersToLessonThread implements Runnable {

	private String serverId;
	private String datetime;
	private String username;
	private String hashValue;
	private Long lessonId;
	private String courseId;
	private String locale;
	private String country;
	private String learnerIds;
	private String monitorIds;
	private String firstNames;
	private String lastNames;
	private String emails;
	private HttpServletRequest request;

	public AddUsersToLessonThread(String serverId, String datetime, String username, String hashValue,
		Long lessonId, String courseId, String locale, String country, String learnerIds, String monitorIds,
		String firstNames, String lastNames, String emails, HttpServletRequest request) {
	    this.serverId = serverId;
	    this.datetime = datetime;
	    this.username = username;
	    this.hashValue = hashValue;
	    this.lessonId = lessonId;
	    this.courseId = courseId;
	    this.locale = locale;
	    this.country = country;
	    this.learnerIds = learnerIds;
	    this.monitorIds = monitorIds;
	    this.firstNames = firstNames;
	    this.lastNames = lastNames;
	    this.emails = emails;
	    this.request = request;
	}

	@Override
	public void run() {
	    addUsersToLesson(serverId, datetime, username, hashValue, lessonId, courseId, locale, country, learnerIds,
		    monitorIds, firstNames, lastNames, emails, request);
	}

	/**
	 * Adds each user in learnerIds and monitorIds as learner and staff to the given lesson id; authenticates using
	 * the 3rd party server requestor's username.
	 *
	 * @param serverId
	 * @param datetime
	 * @param hashValue
	 * @param lessonId
	 * @param learnerIds
	 * @param monitorIds
	 * @param request
	 * @return
	 */
	public Boolean addUsersToLesson(String serverId, String datetime, String requestorUsername, String hashValue,
		Long lessonId, String courseId, String locale, String country, String learnerIds, String monitorIds,
		String firstNames, String lastNames, String emails, HttpServletRequest request) {
	    try {

		// get Server map
		ExtServer extServer = integrationService.getExtServer(serverId);
		// authenticate
		Authenticator.authenticate(extServer, datetime, requestorUsername, hashValue);

		String[] learnerIdArray = (learnerIds != null) ? learnerIds.split(",") : new String[0];
		String[] monitorIdArray = (monitorIds != null) ? monitorIds.split(",") : new String[0];
		String[] firstNameArray = (firstNames != null)
			? firstNames.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
			: new String[0];
		String[] lastNameArray = (lastNames != null) ? lastNames.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
			: new String[0];
		String[] emailArray = (emails != null) ? emails.split(",") : new String[0];

		// in case there is firstNames available - check all arrays have the same length, as otherwise it's
		// prone to ArrayOutOfBounds exceptions
		if ((firstNames != null) && ((firstNameArray.length != lastNameArray.length)
			|| (firstNameArray.length != emailArray.length)
			|| (firstNameArray.length != (learnerIdArray.length + monitorIdArray.length)))) {
		    log.error("Invalid parameters sent: wrong array length. " + "learnerIds=" + learnerIds
			    + " &monitorIds=" + monitorIds + " &firstNames=" + firstNames + " &lastNames=" + lastNames
			    + " &emails=" + emails + " &array lengths=" + learnerIdArray.length + "!"
			    + monitorIdArray.length + "!" + firstNameArray.length + "!" + lastNameArray.length + "!"
			    + emailArray.length);
		    return false;
		}

		int i = 0;
		for (String userName : learnerIdArray) {
		    String firstName = null;
		    String lastName = null;
		    String email = null;
		    if (firstNames != null) {
			// unescape values passed from the external server. Works OK even if the values were not escaped
			firstName = StringEscapeUtils.unescapeCsv(firstNameArray[i]);
			lastName = StringEscapeUtils.unescapeCsv(lastNameArray[i]);
			email = StringEscapeUtils.unescapeCsv(emailArray[i]);
		    }

		    if (StringUtils.isNotBlank(userName)) {
//			integrationService.addExtUserToLesson(extServer, IntegrationConstants.METHOD_LEARNER, lessonId,
//				userName, firstName, lastName, email, courseId, countryIsoCode, langIsoCode);
			integrationService.addExtUserToCourseAndLesson(extServer, IntegrationConstants.METHOD_LEARNER,
				lessonId, userName, firstName, lastName, email, courseId, country, locale);
		    }
		    i++;
		}

		for (String userName : monitorIdArray) {
		    String firstName = null;
		    String lastName = null;
		    String email = null;
		    if (firstNames != null) {
			// unescape values passed from the external server. Works OK even if the values were not escaped
			firstName = StringEscapeUtils.unescapeCsv(firstNameArray[i]);
			lastName = StringEscapeUtils.unescapeCsv(lastNameArray[i]);
			email = StringEscapeUtils.unescapeCsv(emailArray[i]);
		    }

		    if (StringUtils.isNotBlank(userName)) {
			integrationService.addExtUserToCourseAndLesson(extServer, IntegrationConstants.METHOD_MONITOR,
				lessonId, userName, firstName, lastName, email, courseId, country, locale);
		    }
		    i++;
		}

		return true;
	    } catch (UserInfoFetchException e) {
		log.error(e, e);
		return false;
	    } catch (UserInfoValidationException e) {
		log.error(e, e);
		return false;
	    } catch (AuthenticationException e) {
		log.error(e, e);
		return false;
	    }
	}
    }

    /**
     * This method gets the tool outputs for a lesson or a specific user and returns them in XML format. In case of
     * requesting results for a lesson/course it returns them only for users that had finished the lesson.
     *
     * @param document
     * @param serverId
     * @param datetime
     * @param hashValue
     * @param username
     * @param lessonId
     * @param courseID
     * @param outputsUser
     *            if outputsUser is null return results for the whole lesson, otherwise - for the specified learner
     * @return
     * @throws Exception
     */
    private Element getGradebookMarks(Document document, String serverId, String datetime, String hashValue,
	    String username, Long lessonIdParam, String courseId, String outputsUser) throws Exception {

	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	List<Lesson> lessons = new LinkedList<>();
	if (courseId != null) {

	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer.getSid(), courseId);
	    if (orgMap == null) {
		log.debug("No course exists for: " + courseId + ". Cannot get tool outputs report.");
		throw new Exception("Course with courseId: " + courseId + " could not be found");
	    }
	    Integer organisationId = orgMap.getOrganisation().getOrganisationId();

	    lessons.addAll(lessonService.getLessonsByGroup(organisationId));

	} else {
	    Lesson lesson = lessonService.getLesson(lessonIdParam);
	    if (lesson == null) {
		log.debug("No lesson exists for: " + lessonIdParam + ". Cannot get tool outputs report.");
		throw new Exception(
			"Lesson with lessonID: " + lessonIdParam + " could not be found for learner progresses");
	    }
	    lessons.add(lesson);
	}

	// Create the root node of the xml document
	Element gradebookMarksElement = document.createElement("GradebookMarks");

	for (Lesson lesson : lessons) {
	    Long lessonId = lesson.getLessonId();
	    Element lessonElement = document.createElement(CentralConstants.ELEM_LESSON);
	    lessonElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lessonId);
	    lessonElement.setAttribute("lessonName", lesson.getLessonName());
	    String createDateTime = lesson.getCreateDateTime().toString();
	    lessonElement.setAttribute("createDateTime", StringUtils.isBlank(createDateTime) ? "" : createDateTime);

	    // calculate lesson's MaxPossibleMark
	    Long lessonMaxPossibleMark = lamsCoreToolService.getLessonMaxPossibleMark(lesson);
	    lessonElement.setAttribute("lessonMaxPossibleMark", lessonMaxPossibleMark.toString());

	    List<ExtUserUseridMap> allUsers = integrationService.getExtUserUseridMapByExtServer(extServer);

	    // if outputsUser is null we build results for the whole lesson, otherwise - for the specified learner
	    if (outputsUser != null) {

		ExtUserUseridMap userMap = integrationService.getExistingExtUserUseridMap(extServer, outputsUser);
		if (userMap == null) {
		    throw new Exception("No user exists for: " + outputsUser + ". Cannot get tool outputs report.");
		}
		User user = userMap.getUser();
		Integer userId = user.getUserId();

		GradebookUserLesson gradebookUserLesson = gradebookService.getGradebookUserLesson(lessonId, userId);
		if (gradebookUserLesson == null) {
		    gradebookUserLesson = new GradebookUserLesson(lesson, user);
		}
		Double gradebookUserLessonMark = gradebookUserLesson.getMark();

		// Creates learner element and appends it to the specified lessonElement
		createLearnerElement(extServer, lessonElement, allUsers, userId, gradebookUserLessonMark);

	    } else {
		log.debug("Getting tool ouputs report for: " + lessonId + ". With learning design: "
			+ lesson.getLearningDesign().getLearningDesignId());

		List<User> usersCompletedLesson = monitoringService.getUsersCompletedLesson(lessonId, null, null, true);
		List<GradebookUserLesson> gradebookUserLessons = gradebookService.getGradebookUserLesson(lessonId);

		// add all users who had finished the lesson
		for (User user : usersCompletedLesson) {
		    Integer userId = user.getUserId();

		    // find user's gradebookLessonMark (it will be null in cases when lesson contains zero activities that
		    // set to produce toolOutputs)
		    Double gradebookUserLessonMark = null;
		    for (GradebookUserLesson gradebookUserLesson : gradebookUserLessons) {
			if (userId.equals(gradebookUserLesson.getLearner().getUserId())) {
			    gradebookUserLessonMark = gradebookUserLesson.getMark();
			    break;
			}
		    }

		    // Creates learner element and appends it to the specified lessonElement
		    createLearnerElement(extServer, lessonElement, allUsers, userId, gradebookUserLessonMark);
		}

	    }

	    gradebookMarksElement.appendChild(lessonElement);
	}

	return gradebookMarksElement;
    }

    /*
     * Creates learner element and appends it to the specified lessonElement.
     *
     * @param extServer
     *
     * @param lessonElement
     *
     * @param allUsers all available users associated with extServer
     *
     * @param userId userId of the learner to whom this learnerElement corresponds
     *
     * @param gradebookUserLessonMark mark of the gradebookUserLesson
     *
     * @return
     *
     * @throws Exception
     */
    private void createLearnerElement(ExtServer extServer, Element lessonElement, List<ExtUserUseridMap> allUsers,
	    Integer userId, Double gradebookUserLessonMark) throws Exception {

	// find user
	ExtUserUseridMap extUser = null;
	for (ExtUserUseridMap extUserIter : allUsers) {
	    if (extUserIter.getUser().getUserId().equals(userId)) {
		extUser = extUserIter;
		break;
	    }
	}

	if (extUser == null) {
	    throw new Exception("User with userId: " + userId + " doesn't belong to extServer: " + extServer.getSid());
	}

	Document document = lessonElement.getOwnerDocument();
	Element learnerElement = document.createElement("Learner");
	learnerElement.setAttribute("extUsername", extUser.getExtUsername());
	String email = extUser.getUser().getEmail();
	learnerElement.setAttribute("email", StringUtils.isBlank(email) ? "" : email);
	String userTotalMark = gradebookUserLessonMark == null ? "" : gradebookUserLessonMark.toString();
	learnerElement.setAttribute("userTotalMark", userTotalMark);

	lessonElement.appendChild(learnerElement);
    }

    /**
     * This method gets the tool outputs for a lesson or a specific user and returns them in XML format.
     *
     * @param document
     * @param serverId
     * @param datetime
     * @param hashValue
     * @param username
     * @param lessonId
     * @param courseID
     * @param isAuthoredToolOutputs
     * @param outputsUser
     *            if outputsUser is null return results for the whole lesson, otherwise - for the specified learner
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Element getToolOutputs(Document document, String serverId, String datetime, String hashValue,
	    String username, Long lessonId, String courseID, boolean isAuthoredToolOutputs, String outputsUser)
	    throws Exception {

	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	Lesson lesson = lessonService.getLesson(lessonId);
	if (lesson == null) {
	    // TODO: handle this error instead of throwing an exception
	    log.debug("No lesson exists for: " + lessonId + ". Cannot get tool outputs report.");
	    throw new Exception("Lesson with lessonID: " + lessonId + " could not be found for learner progresses");
	}

	Set<ToolActivity> activities = getLessonActivities(lesson);

	Set<User> learners = new TreeSet<>();
	// if outputsUser is null we build results for the whole lesson, otherwise - for the specified learner
	if (outputsUser != null) {

	    ExtUserUseridMap userMap = integrationService.getExistingExtUserUseridMap(extServer, outputsUser);
	    if (userMap == null) {
		// TODO: handle this error instead of throwing an exception
		log.debug("No user exists for: " + outputsUser + ". Cannot get tool outputs report.");
		throw new Exception("No user exists for: " + outputsUser + ". Cannot get tool outputs report.");
	    }
	    learners.add(userMap.getUser());

	} else {
	    learners.addAll(lesson.getAllLearners());
	    log.debug("Getting tool ouputs report for: " + lessonId + ". With learning design: "
		    + lesson.getLearningDesign().getLearningDesignId());
	}

	// Create the root node of the xml document
	Element toolOutputsElement = document.createElement("ToolOutputs");

	toolOutputsElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lessonId);
	toolOutputsElement.setAttribute("name", lesson.getLessonName());
	String createDateTime = lesson.getCreateDateTime().toString();
	toolOutputsElement.setAttribute("createDateTime", StringUtils.isBlank(createDateTime) ? "" : createDateTime);

	List<LearnerProgress> learnerProgresses = lessonService.getUserProgressForLesson(lesson.getLessonId());
	List<ToolSession> toolSessions = lamsCoreToolService.getToolSessionsByLesson(lesson);

	// map contains pairs toolContentId -> toolOutputDefinitions
	Map<Long, Map<String, ToolOutputDefinition>> toolOutputDefinitionsMap = new TreeMap<>();
	for (ToolActivity activity : activities) {
	    Long toolContentId = activity.getToolContentId();
	    if (toolOutputDefinitionsMap.get(toolContentId) == null) {
		SortedMap<String, ToolOutputDefinition> toolOutputDefinitions = lamsCoreToolService
			.getOutputDefinitionsFromTool(toolContentId,
				ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);
		toolOutputDefinitionsMap.put(toolContentId, toolOutputDefinitions);
	    }
	}

	for (User learner : learners) {

	    Element learnerElement = document.createElement("LearnerOutput");

	    String userNoPrefixName = learner.getLogin().substring(extServer.getPrefix().length() + 1);
	    learnerElement.setAttribute("userName", userNoPrefixName);
	    learnerElement.setAttribute("lamsUserName", learner.getLogin());
	    learnerElement.setAttribute("lamsUserId", learner.getUserId().toString());
	    learnerElement.setAttribute("firstName", learner.getFirstName());
	    learnerElement.setAttribute("lastName", learner.getLastName());
	    String email = learner.getEmail();
	    learnerElement.setAttribute("email", StringUtils.isBlank(email) ? "" : email);

	    // find required learnerProgress from learnerProgresses (this way we don't querying DB).
	    LearnerProgress learnerProgress = null;
	    for (LearnerProgress dbLearnerProgress : learnerProgresses) {
		if (dbLearnerProgress.getUser().getUserId().equals(learner.getUserId())) {
		    learnerProgress = dbLearnerProgress;
		}
	    }
	    if (learnerProgress != null) {
		learnerElement.setAttribute("completedLesson", "" + learnerProgress.isComplete());
	    }

	    for (ToolActivity activity : activities) {
		// find required toolSession from toolSessions (this way we don't querying DB).
		ToolSession toolSession = null;
		for (ToolSession dbToolSession : toolSessions) {
		    if (dbToolSession.getToolActivity().getActivityId().equals(activity.getActivityId())
			    && dbToolSession.getLearners().contains(learner)) {
			toolSession = dbToolSession;
		    }
		}
		Map<String, ToolOutputDefinition> toolOutputDefinitions = toolOutputDefinitionsMap
			.get(activity.getToolContentId());

		learnerElement.appendChild(getActivityOutputsElement(document, activity, learner, learnerProgress,
			toolSession, toolOutputDefinitions, isAuthoredToolOutputs));
	    }

	    toolOutputsElement.appendChild(learnerElement);
	}

	return toolOutputsElement;

    }

    /**
     * Returns whether specified lesson has numeric tool outputs (i.e. contains Scratchie, Assessment or MCQ).
     */
    @SuppressWarnings("unchecked")
    private Element checkLessonForNumericToolOutputs(Document document, String serverId, String datetime,
	    String hashValue, String username, Long lessonId) throws Exception {

	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	Lesson lesson = lessonService.getLesson(lessonId);
	if (lesson == null) {
	    throw new Exception("No lesson exists for: " + lessonId);
	}

	Set<ToolActivity> activities = getLessonActivities(lesson);

	// Scratchie, Assessment and MCQ are the tools that produce numeric tool outputs
	boolean hasNumericToolOutput = false;
	for (ToolActivity activity : activities) {
	    String toolSignature = activity.getTool().getToolSignature();
	    hasNumericToolOutput |= CommonConstants.TOOL_SIGNATURE_ASSESSMENT.equals(toolSignature)
		    || CommonConstants.TOOL_SIGNATURE_MCQ.equals(toolSignature)
		    || CommonConstants.TOOL_SIGNATURE_SCRATCHIE.equals(toolSignature);
	}

	// Create the root node of the xml document
	Element lessonElement = document.createElement("Lesson");
	lessonElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lessonId);
	lessonElement.setAttribute("hasNumericToolOutput", Boolean.toString(hasNumericToolOutput));
	return lessonElement;
    }

    /**
     * Returns lesson tool activities. It works almost the same as lesson.getLearningDesign().getActivities() except it
     * solves problem with first activity unable to cast to ToolActivity.
     */
    @SuppressWarnings("unchecked")
    private Set<ToolActivity> getLessonActivities(Lesson lesson) {
	Set<Activity> activities = new TreeSet<>();
	Set<ToolActivity> toolActivities = new TreeSet<>();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 *
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = monitoringService
		.getActivityById(lesson.getLearningDesign().getFirstActivity().getActivityId());
	activities.add(firstActivity);
	activities.addAll(lesson.getLearningDesign().getActivities());

	for (Activity activity : activities) {
	    if (activity instanceof ToolActivity) {
		ToolActivity toolActivity = (ToolActivity) activity;
		toolActivities.add(toolActivity);
	    }
	}

	return toolActivities;
    }

    /**
     * Verifies external server.
     *
     * @param serverId
     * @param datetime
     * @param hash
     * @return
     * @throws Exception
     */
    private boolean verify(String serverId, String datetime, String hash) throws Exception {
	try {
	    ExtServer extServer = integrationService.getExtServer(serverId);
	    Authenticator.authenticate(extServer, datetime, hash);
	    return true;
	} catch (Exception e) {
	    log.error("Problem verifying external server: " + serverId, e);
	    throw new Exception(e);
	}
    }

    /**
     * Gets the tool output for a specified activity and learner and returns an XML representation of the data
     *
     * @param document
     * @param activity
     * @param learner
     * @param progress
     * @param isAuthoredToolOutputs
     * @return
     */
    private Element getActivityOutputsElement(Document document, ToolActivity activity, User learner,
	    LearnerProgress progress, ToolSession toolSession, Map<String, ToolOutputDefinition> toolOutputDefinitions,
	    boolean isAuthoredToolOutputs) {
	Element activityElement = document.createElement("Activity");
	activityElement.setAttribute("title", activity.getTitle());
	activityElement.setAttribute("activityId", activity.getActivityId().toString());
	if (activity.getOrderId() != null) {
	    activityElement.setAttribute("orderId", activity.getOrderId().toString());
	}

	boolean activityAttempted = false;
	if (progress != null) {
	    boolean completed = progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED;
	    activityElement.setAttribute("completed", "" + completed);

	    activityAttempted = completed
		    || (progress.getProgressState(activity) == LearnerProgress.ACTIVITY_ATTEMPTED);
	    activityElement.setAttribute("attempted", "" + activityAttempted);

	} else {
	    activityElement.setAttribute("attempted", "" + false);
	    activityElement.setAttribute("completed", "" + false);
	}

	if (activityAttempted && (toolSession != null)) {

	    for (String outputName : toolOutputDefinitions.keySet()) {

		try {
		    ToolOutputDefinition definition = toolOutputDefinitions.get(outputName);

		    if (isAuthoredToolOutputs) {
			ActivityEvaluation evaluation = (ActivityEvaluation) userManagementService
				.findById(ActivityEvaluation.class, activity.getActivityId());
			if (evaluation != null) {
			    if (outputName.equals(evaluation.getToolOutputDefinition())) {
				ToolOutput toolOutput = lamsCoreToolService.getOutputFromTool(outputName, toolSession,
					learner.getUserId());
				activityElement.appendChild(getOutputElement(document, toolOutput, definition));
			    }
			}
		    } else {
			ToolOutput toolOutput = lamsCoreToolService.getOutputFromTool(outputName, toolSession,
				learner.getUserId());

			if (toolOutput != null) {
			    activityElement.appendChild(getOutputElement(document, toolOutput, definition));

			}

		    }

		} catch (RuntimeException e) {
		    log.error("Runtime exception when attempted to get outputs for activity: "
			    + activity.getActivityId() + ", continuing for other activities", e);
		}
	    }

	}
	return activityElement;
    }

    /**
     * Returns an XML element containing the tool output data for one instance
     *
     * @param document
     * @param toolOutput
     * @return
     */
    private Element getOutputElement(Document document, ToolOutput toolOutput, ToolOutputDefinition definition) {
	Element toolOutputElement = document.createElement("ToolOutput");
	toolOutputElement.setAttribute("name", toolOutput.getName());
	toolOutputElement.setAttribute("description", toolOutput.getDescription());
	toolOutputElement.setAttribute("output", toolOutput.getValue().getString());

	Long marksPossible = getTotalMarksAvailable(definition);

	toolOutputElement.setAttribute("marksPossible", marksPossible != null ? marksPossible.toString() : "");

	String type;
	OutputType outputType = toolOutput.getValue().getType();

	if (outputType == OutputType.OUTPUT_BOOLEAN) {
	    type = "boolean";
	} else if (outputType == OutputType.OUTPUT_COMPLEX) {
	    type = "complex";
	} else if (outputType == OutputType.OUTPUT_DOUBLE) {
	    type = "double";
	} else if (outputType == OutputType.OUTPUT_LONG) {
	    type = "long";
	} else if (outputType == OutputType.OUTPUT_SET_BOOLEAN) {
	    type = "set_boolean";
	} else {
	    type = "string";
	}

	toolOutputElement.setAttribute("type", type);
	return toolOutputElement;
    }

    /**
     * Gets the total marks available for a tool output definition
     *
     * @param activity
     * @return
     */
    private Long getTotalMarksAvailable(ToolOutputDefinition definition) {

	Object upperLimit = definition.getEndValue();
	if ((upperLimit != null) && (upperLimit instanceof Long)) {
	    return (Long) upperLimit;
	}
	return null;
    }

    /**
     * Checks whether request method is POST, throws SecurityException if not.
     *
     * @param request
     * @return
     * @throws SecurityException
     */
    private boolean verifyPostRequestMethod(HttpServletRequest request) throws SecurityException {
	boolean isPost = "POST".equals(request.getMethod());

	if (!isPost) {
	    throw new SecurityException("This API call allows only POST request method.");
	}

	return isPost;
    }

    /**
     * Lists lessons for which the given user is a Monitor in the given organisation.
     */
    private Element getLessonMonitorList(Document document, String serverId, String datetime, String hashValue,
	    String username, String courseID, String countryIsoCode, String langIsoCode) throws Exception {
	ExtServer extServer = integrationService.getExtServer(serverId);
	Authenticator.authenticate(extServer, datetime, username, hashValue);

	ExtUserUseridMap userMap = integrationService.getExistingExtUserUseridMap(extServer, username);
	ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer, userMap, courseID, null,
		IntegrationConstants.METHOD_MONITOR);
	Organisation organisation = orgMap.getOrganisation();

	Map<Long, IndexLessonBean> map = lessonService.getLessonsByOrgAndUserWithCompletedFlag(
		userMap.getUser().getUserId(), organisation.getOrganisationId(), Role.ROLE_MONITOR);

	Element lessonsElem = document.createElement("Lessons");
	for (IndexLessonBean bean : map.values()) {
	    Element lessonElem = document.createElement(CentralConstants.ELEM_LESSON);
	    lessonElem.setAttribute(CentralConstants.ATTR_LESSON_ID, bean.getId().toString());
	    lessonElem.setAttribute(CentralConstants.ATTR_NAME, bean.getName());
	    lessonElem.setAttribute("description", bean.getDescription());
	    lessonElem.setAttribute("state", bean.getState().toString());

	    lessonsElem.appendChild(lessonElem);
	}

	return lessonsElem;
    }
}
