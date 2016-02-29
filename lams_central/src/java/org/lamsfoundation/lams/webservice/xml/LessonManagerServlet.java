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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.security.AuthenticationException;
import org.lamsfoundation.lams.integration.security.Authenticator;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
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
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class LessonManagerServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(LessonManagerServlet.class);

    private static IntegrationService integrationService = null;

    private static IMonitoringService monitoringService = null;

    private static ILessonService lessonService = null;

    private static IExportToolContentService exportService = null;

    private static ILamsCoreToolService toolService = null;

    private static IGradebookService gradebookService = null;

    private static IUserManagementService userManagementService = null;

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
	String lang = request.getParameter(CentralConstants.PARAM_LANG);
	String method = request.getParameter(CentralConstants.PARAM_METHOD);
	String filePath = request.getParameter(CentralConstants.PARAM_FILEPATH);
	String outputsUser = request.getParameter("outputsUser");
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	String monitorIds = request.getParameter(CentralConstants.PARAM_MONITOR_IDS);

	// Custom CSV string to be used for tool adapters
	String customCSV = request.getParameter(CentralConstants.PARAM_CUSTOM_CSV);

	// parameters used for creating user at joinLesson method
	String firstNames = request.getParameter("firstNames");
	String lastNames = request.getParameter("lastNames");
	String emails = request.getParameter("emails");

	boolean presenceEnable = WebUtil.readBooleanParam(request, CentralConstants.PARAM_LEARNER_PRESENCE_ENABLE,
		false);
	boolean imEnable = WebUtil.readBooleanParam(request, CentralConstants.PARAM_LEARNER_IM_ENABLE, false);

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
		ldId = new Long(ldIdStr);
		Long lessonId = startLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			country, lang, customCSV, presenceEnable, imEnable);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_PREVIEW)) {
		ldId = new Long(ldIdStr);
		Long lessonId = startPreview(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			country, lang, customCSV, presenceEnable, imEnable);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_SCHEDULE)) {
		ldId = new Long(ldIdStr);
		Long lessonId = scheduleLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			startDate, country, lang, customCSV, presenceEnable, imEnable);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_DELETE)) {
		lsId = new Long(lsIdStr);
		Boolean deleted = deleteLesson(serverId, datetime, hashValue, username, lsId);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lsId.toString());
		element.setAttribute(CentralConstants.ATTR_DELETED, deleted.toString());

	    } else if (method.equals(CentralConstants.METHOD_STUDENT_PROGRESS)) {
		lsId = new Long(lsIdStr);
		element = getAllStudentProgress(document, serverId, datetime, hashValue, username, lsId, courseId);

	    } else if (method.equals(CentralConstants.METHOD_SINGLE_STUDENT_PROGRESS)) {
		String firstName = request.getParameter(LoginRequestDispatcher.PARAM_FIRST_NAME);
		String lastName = request.getParameter(LoginRequestDispatcher.PARAM_LAST_NAME);
		String email = request.getParameter(LoginRequestDispatcher.PARAM_EMAIL);

		lsId = new Long(lsIdStr);
		element = getSingleStudentProgress(document, serverId, datetime, hashValue, username, firstName,
			lastName, lang, country, email, lsId, courseId);

	    } else if (method.equals(CentralConstants.METHOD_IMPORT)) {

		// ldId = new Long(ldIdStr);
		Long ldID = importLearningDesign(request, response, filePath, username, serverId, customCSV);

		element = document.createElement(CentralConstants.ELEM_LEARNINGDESIGN);
		element.setAttribute(CentralConstants.PARAM_LEARNING_DESIGN_ID, ldID.toString());

	    } else if (method.equals(CentralConstants.METHOD_JOIN_LESSON)) {
		Thread t = new Thread(new AddUsersToLessonThread(serverId, datetime, username, hashValue, lsIdStr,
			courseId, country, lang, learnerIds, monitorIds, firstNames, lastNames, emails, request));
		t.start();

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lsIdStr);

	    } else if (method.equals("gradebookMarksUser")) {
		lsId = new Long(lsIdStr);
		element = getGradebookMarks(document, serverId, datetime, hashValue, username, lsId, null, outputsUser);

	    } else if (method.equals("gradebookMarksLesson")) {
		lsId = new Long(lsIdStr);
		element = getGradebookMarks(document, serverId, datetime, hashValue, username, lsId, null, null);

	    } else if (method.equals("gradebookMarksCourse")) {
		element = getGradebookMarks(document, serverId, datetime, hashValue, username, null, courseId, null);

	    } else if (method.equals("toolOutputsAllUsers")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, false,
			null);

	    } else if (method.equals("authoredToolOutputsAllUsers")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, true, null);

	    } else if (method.equals("toolOutputsUser")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, false,
			outputsUser);

	    } else if (method.equals("authoredToolOutputsUser")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, true,
			outputsUser);

	    } else if (method.equals(CentralConstants.METHOD_VERIFY_EXT_SERVER)) {
		verify(serverId, datetime, hashValue);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("1");
		out.flush();
		out.close();
		return;

	    } else {
		String msg = "Method :" + method + " is not recognised";
		LessonManagerServlet.log.error(msg);
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
	    LessonManagerServlet.log.error("lsId or ldId is not an integer" + lsIdStr + ldIdStr, nfe);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "lsId or ldId is not an integer");
	} catch (TransformerConfigurationException e) {
	    LessonManagerServlet.log.error("Can not convert XML document to string", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (TransformerException e) {
	    LessonManagerServlet.log.error("Can not convert XML document to string", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (ParserConfigurationException e) {
	    LessonManagerServlet.log.error("Can not build XML document", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (NullPointerException e) {
	    LessonManagerServlet.log.error("Missing parameters", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	} catch (Exception e) {
	    LessonManagerServlet.log.error("Problem loading learning manager servlet request", e);
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

    public Long startLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV,
	    boolean presenceEnable, boolean imEnable) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    User user = userMap.getUser();
	    Organisation organisation = orgMap.getOrganisation();

	    // 1. init lesson
	    Lesson lesson = LessonManagerServlet.monitoringService.initializeLesson(title, desc, ldId,
		    organisation.getOrganisationId(), user.getUserId(), customCSV, false, false, presenceEnable,
		    imEnable, true, false, false, null, null);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, organisation, user);
	    // 3. start lesson
	    LessonManagerServlet.monitoringService.startLesson(lesson.getLessonId(), user.getUserId());
	    // store information which extServer has started the lesson
	    LessonManagerServlet.integrationService.createExtServerLessonMap(lesson.getLessonId(), serverMap);

	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    public Long scheduleLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String startDate, String countryIsoCode, String langIsoCode,
	    String customCSV, boolean presenceEnable, boolean imEnable) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = LessonManagerServlet.monitoringService.initializeLesson(title, desc, ldId,
		    orgMap.getOrganisation().getOrganisationId(), userMap.getUser().getUserId(), customCSV, false,
		    false, presenceEnable, imEnable, true, false, false, null, null);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, orgMap.getOrganisation(), userMap.getUser());
	    // 3. schedule lesson
	    Date date = DateUtil.convertFromLAMSFlashFormat(startDate);
	    LessonManagerServlet.monitoringService.startLessonOnSchedule(lesson.getLessonId(), date,
		    userMap.getUser().getUserId());
	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    @SuppressWarnings("unchecked")
    public Element getAllStudentProgress(Document document, String serverId, String datetime, String hashValue,
	    String username, long lsId, String courseID) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    Lesson lesson = LessonManagerServlet.lessonService.getLesson(lsId);

	    Element element = document.createElement(CentralConstants.ELEM_LESSON_PROGRESS);
	    element.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);

	    String prefix = serverMap.getPrefix();
	    if (lesson != null) {

		int activitiesTotal = lesson.getLearningDesign().getActivities().size();
		Iterator<LearnerProgress> iterator = lesson.getLearnerProgresses().iterator();
		while (iterator.hasNext()) {
		    LearnerProgress learnProg = iterator.next();
		    LearnerProgressDTO learnerProgress = learnProg.getLearnerProgressData();

		    // get the username with the integration prefix removed
		    String userNoPrefixName = learnerProgress.getUserName().substring(prefix.length() + 1);
		    ExtUserUseridMap learnerMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap,
			    userNoPrefixName);

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

    public Element getSingleStudentProgress(Document document, String serverId, String datetime, String hashValue,
	    String username, String firstName, String lastName, String language, String country, String email,
	    long lsId, String courseID) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    Lesson lesson = LessonManagerServlet.lessonService.getLesson(lsId);

	    Element element = document.createElement(CentralConstants.ELEM_LESSON_PROGRESS);
	    element.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);

	    if (lesson != null) {
		int activitiesTotal = lesson.getLearningDesign().getActivities().size();

		// create new user if required
		final boolean usePrefix = true;
		final boolean isUpdateUserDetails = false;
		ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getImplicitExtUserUseridMap(
			serverMap, username, firstName, lastName, language, country, email, usePrefix,
			isUpdateUserDetails);

		LearnerProgress learnProg = LessonManagerServlet.lessonService
			.getUserProgressForLesson(userMap.getUser().getUserId(), lsId);

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

    public boolean deleteLesson(String serverId, String datetime, String hashValue, String username, long lsId)
	    throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    LessonManagerServlet.monitoringService.removeLesson(lsId, userMap.getUser().getUserId());
	    return true;
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    public Long startPreview(String serverId, String datetime, String hashValue, String username, Long ldId,
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV,
	    boolean presenceEnable, boolean imEnable) throws RemoteException {

	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    Integer userId = userMap.getUser().getUserId();

	    // 1. init lesson
	    Lesson lesson = LessonManagerServlet.monitoringService.initializeLessonForPreview(title, desc, ldId, userId,
		    customCSV, presenceEnable, imEnable, false);
	    // 2. create lessonClass for lesson
	    LessonManagerServlet.monitoringService.createPreviewClassForLesson(userId, lesson.getLessonId());

	    // 3. start lesson
	    LessonManagerServlet.monitoringService.startLesson(lesson.getLessonId(), userId);

	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}

    }

    @SuppressWarnings("unchecked")
    public Long importLearningDesign(HttpServletRequest request, HttpServletResponse response, String filePath,
	    String username, String serverId, String customCSV) throws RemoteException {

	List<String> ldErrorMsgs = new ArrayList<String>();
	List<String> toolsErrorMsgs = new ArrayList<String>();
	Long ldId = null;
	Integer workspaceFolderUid = null;
	ExtUserUseridMap userMap;
	User user = null;
	ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);

	try {

	    userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);

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
	    Object[] ldResults = LessonManagerServlet.exportService.importLearningDesign(designFile, user,
		    workspaceFolderUid, toolsErrorMsgs, customCSV);
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
	List<User> staffList = new LinkedList<User>();
	staffList.add(creator);
	List<User> learnerList = new LinkedList<User>();
	Vector<User> learnerVector = LessonManagerServlet.userManagementService
		.getUsersFromOrganisationByRole(organisation.getOrganisationId(), Role.LEARNER, false, true);
	learnerList.addAll(learnerVector);
	LessonManagerServlet.monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
		organisation.getName() + " learners", learnerList, organisation.getName() + " staff", staffList,
		creator.getUserId());

    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *             if an error occured
     */
    @Override
    public void init() throws ServletException {

	LessonManagerServlet.integrationService = (IntegrationService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("integrationService");

	LessonManagerServlet.monitoringService = (IMonitoringService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("monitoringService");

	LessonManagerServlet.lessonService = (ILessonService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("lessonService");

	LessonManagerServlet.exportService = (IExportToolContentService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("exportToolContentService");

	LessonManagerServlet.toolService = (ILamsCoreToolService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("lamsCoreToolService");

	LessonManagerServlet.gradebookService = (IGradebookService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("gradebookService");

	LessonManagerServlet.userManagementService = (IUserManagementService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("userManagementService");
    }

    private class AddUsersToLessonThread implements Runnable {

	private String serverId;
	private String datetime;
	private String username;
	private String hashValue;
	private String lsIdStr;
	private String courseId;
	private String country;
	private String lang;
	private String learnerIds;
	private String monitorIds;
	private String firstNames;
	private String lastNames;
	private String emails;
	private HttpServletRequest request;

	public AddUsersToLessonThread(String serverId, String datetime, String username, String hashValue,
		String lsIdStr, String courseId, String country, String lang, String learnerIds, String monitorIds,
		String firstNames, String lastNames, String emails, HttpServletRequest request) {
	    this.serverId = serverId;
	    this.datetime = datetime;
	    this.username = username;
	    this.hashValue = hashValue;
	    this.lsIdStr = lsIdStr;
	    this.courseId = courseId;
	    this.country = country;
	    this.lang = lang;
	    this.learnerIds = learnerIds;
	    this.monitorIds = monitorIds;
	    this.firstNames = firstNames;
	    this.lastNames = lastNames;
	    this.emails = emails;
	    this.request = request;
	}

	@Override
	public void run() {
	    addUsersToLesson(serverId, datetime, username, hashValue, lsIdStr, courseId, country, lang, learnerIds,
		    monitorIds, firstNames, lastNames, emails, request);
	}

	/**
	 * Adds each user in learnerIds and monitorIds as learner and staff to the given lesson id; authenticates using
	 * the 3rd party server requestor's username.
	 * 
	 * @param serverId
	 * @param datetime
	 * @param hashValue
	 * @param lsIdStr
	 * @param learnerIds
	 * @param monitorIds
	 * @param request
	 * @return
	 */
	public Boolean addUsersToLesson(String serverId, String datetime, String requestorUsername, String hashValue,
		String lsIdStr, String courseId, String countryIsoCode, String langIsoCode, String learnerIds,
		String monitorIds, String firstNames, String lastNames, String emails, HttpServletRequest request) {
	    try {

		// get Server map
		ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
		// authenticate
		Authenticator.authenticate(serverMap, datetime, requestorUsername, hashValue);

		String[] learnerIdArray = (learnerIds != null) ? learnerIds.split(",") : new String[0];
		String[] monitorIdArray = (monitorIds != null) ? monitorIds.split(",") : new String[0];
		String[] firstNameArray = (firstNames != null) ? firstNames.split(",") : new String[0];
		String[] lastNameArray = (lastNames != null) ? lastNames.split(",") : new String[0];
		String[] emailArray = (emails != null) ? emails.split(",") : new String[0];

		// in case there is firstNames available - check all arrays have the same length, as otherwise it's
		// prone to ArrayOutOfBounds exceptions
		if ((firstNames != null) && ((firstNameArray.length != lastNameArray.length)
			|| (firstNameArray.length != emailArray.length)
			|| (firstNameArray.length != (learnerIdArray.length + monitorIdArray.length)))) {
		    LessonManagerServlet.log.error("Invalid parameters sent: wrong array length.");
		    return false;
		}

		int i = 0;
		for (String userName : learnerIdArray) {
		    String firstName = null;
		    String lastName = null;
		    String email = null;
		    if (firstNames != null) {
			firstName = firstNameArray[i];
			lastName = lastNameArray[i];
			email = emailArray[i];
		    }

		    if (StringUtils.isNotBlank(userName)) {
			addUserToLesson(request, serverMap, LoginRequestDispatcher.METHOD_LEARNER, lsIdStr, userName,
				firstName, lastName, email, courseId, countryIsoCode, langIsoCode);
		    }
		    i++;
		}

		for (String userName : monitorIdArray) {
		    String firstName = null;
		    String lastName = null;
		    String email = null;
		    if (firstNames != null) {
			firstName = firstNameArray[i];
			lastName = lastNameArray[i];
			email = emailArray[i];
		    }

		    if (StringUtils.isNotBlank(userName)) {
			addUserToLesson(request, serverMap, LoginRequestDispatcher.METHOD_MONITOR, lsIdStr, userName,
				firstName, lastName, email, courseId, countryIsoCode, langIsoCode);
		    }
		    i++;
		}

		return true;
	    } catch (UserInfoFetchException e) {
		LessonManagerServlet.log.error(e, e);
		return false;
	    } catch (UserInfoValidationException e) {
		LessonManagerServlet.log.error(e, e);
		return false;
	    } catch (AuthenticationException e) {
		LessonManagerServlet.log.error(e, e);
		return false;
	    }
	}

	private void addUserToLesson(HttpServletRequest request, ExtServerOrgMap serverMap, String method,
		String lsIdStr, String username, String firstName, String lastName, String email, String courseId,
		String countryIsoCode, String langIsoCode) throws UserInfoFetchException, UserInfoValidationException {

	    if (LessonManagerServlet.log.isDebugEnabled()) {
		LessonManagerServlet.log
			.debug("Adding user '" + username + "' as " + method + " to lesson with id '" + lsIdStr + "'.");
	    }

	    ExtUserUseridMap userMap = null;
	    if ((firstName == null) && (lastName == null)) {
		userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    } else {
		final boolean usePrefix = true;
		final boolean isUpdateUserDetails = false;
		userMap = LessonManagerServlet.integrationService.getImplicitExtUserUseridMap(serverMap, username,
			firstName, lastName, langIsoCode, countryIsoCode, email, usePrefix, isUpdateUserDetails);
	    }

	    // ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap,
	    // username);
	    // adds user to group
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, method);

	    if (LessonManagerServlet.lessonService == null) {
		LessonManagerServlet.lessonService = (ILessonService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(request.getSession().getServletContext())
			.getBean("lessonService");
	    }

	    User user = userMap.getUser();
	    if (user == null) {
		String error = "Unable to add user to lesson class as user is missing from the user map";
		LessonManagerServlet.log.error(error);
		throw new UserInfoFetchException(error);
	    }

	    if (LoginRequestDispatcher.METHOD_LEARNER.equals(method)) {
		LessonManagerServlet.lessonService.addLearner(Long.parseLong(lsIdStr), user.getUserId());
	    } else if (LoginRequestDispatcher.METHOD_MONITOR.equals(method)) {
		LessonManagerServlet.lessonService.addStaffMember(Long.parseLong(lsIdStr), user.getUserId());
	    }

	}
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
     * @param outputsUser
     *            if outputsUser is null return results for the whole lesson, otherwise - for the specified learner
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Element getGradebookMarks(Document document, String serverId, String datetime, String hashValue,
	    String username, Long lessonIdParam, String courseId, String outputsUser) throws Exception {

	ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	Authenticator.authenticate(serverMap, datetime, username, hashValue);

	List<Lesson> lessons = new LinkedList<Lesson>();
	if (courseId != null) {

	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap.getSid(),
		    courseId);
	    if (orgMap == null) {
		LessonManagerServlet.log
			.debug("No course exists for: " + courseId + ". Cannot get tool outputs report.");
		throw new Exception("Course with courseId: " + courseId + " could not be found");
	    }
	    Integer organisationId = orgMap.getOrganisation().getOrganisationId();

	    lessons.addAll(LessonManagerServlet.lessonService.getLessonsByGroup(organisationId));

	} else {
	    Lesson lesson = LessonManagerServlet.lessonService.getLesson(lessonIdParam);
	    if (lesson == null) {
		LessonManagerServlet.log
			.debug("No lesson exists for: " + lessonIdParam + ". Cannot get tool outputs report.");
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

	    // calculate lesson's MaxPossibleMark
	    Set<ToolActivity> activities = getLessonActivities(lesson);
	    Long lessonMaxPossibleMark = 0L;
	    for (ToolActivity activity : activities) {
		Long activityMaxPossibleMark = LessonManagerServlet.toolService.getActivityMaxPossibleMark(activity);
		if (activityMaxPossibleMark != null) {
		    lessonMaxPossibleMark += activityMaxPossibleMark;
		}
	    }
	    lessonElement.setAttribute("lessonMaxPossibleMark", lessonMaxPossibleMark.toString());

	    //get gradebook marks from DB
	    List<GradebookUserLesson> gradebookUserLessons = new LinkedList<GradebookUserLesson>();
	    // if outputsUser is null we build results for the whole lesson, otherwise - for the specified learner
	    if (outputsUser != null) {

		ExtUserUseridMap userMap = LessonManagerServlet.integrationService
			.getExistingExtUserUseridMap(serverMap, outputsUser);
		if (userMap == null) {
		    throw new Exception("No user exists for: " + outputsUser + ". Cannot get tool outputs report.");
		}
		User user = userMap.getUser();
		Integer userId = user.getUserId();

		GradebookUserLesson gradebookUserLesson = LessonManagerServlet.gradebookService
			.getGradebookUserLesson(lessonId, userId);
		if (gradebookUserLesson == null) {
		    gradebookUserLesson = new GradebookUserLesson(lesson, user);
		}
		gradebookUserLessons.add(gradebookUserLesson);

	    } else {
		gradebookUserLessons.addAll(LessonManagerServlet.gradebookService.getGradebookUserLesson(lessonId));
		LessonManagerServlet.log.debug("Getting tool ouputs report for: " + lessonId
			+ ". With learning design: " + lesson.getLearningDesign().getLearningDesignId());
	    }

	    List<ExtUserUseridMap> allUsers = LessonManagerServlet.integrationService
		    .getExtUserUseridMapByServerMap(serverMap);

	    for (GradebookUserLesson gradebookUserLesson : gradebookUserLessons) {
		Integer userId = gradebookUserLesson.getLearner().getUserId();

		//find user
		ExtUserUseridMap extUser = null;
		for (ExtUserUseridMap extUserIter : allUsers) {
		    if (extUserIter.getUser().getUserId().equals(userId)) {
			extUser = extUserIter;
			break;
		    }
		}

		if (extUser == null) {
		    throw new Exception(
			    "User with userId: " + userId + " doesn't belong to extServer: " + serverMap.getSid());
		}

		Element learnerElement = document.createElement("Learner");
		learnerElement.setAttribute("extUsername", extUser.getExtUsername());
		String userTotalMark = gradebookUserLesson.getMark() == null ? ""
			: gradebookUserLesson.getMark().toString();
		learnerElement.setAttribute("userTotalMark", userTotalMark);

		lessonElement.appendChild(learnerElement);
	    }

	    gradebookMarksElement.appendChild(lessonElement);
	}

	return gradebookMarksElement;
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
    public Element getToolOutputs(Document document, String serverId, String datetime, String hashValue,
	    String username, Long lessonId, String courseID, boolean isAuthoredToolOutputs, String outputsUser)
		    throws Exception {

	ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	Authenticator.authenticate(serverMap, datetime, username, hashValue);

	Lesson lesson = LessonManagerServlet.lessonService.getLesson(lessonId);
	if (lesson == null) {
	    // TODO: handle this error instead of throwing an exception
	    LessonManagerServlet.log.debug("No lesson exists for: " + lessonId + ". Cannot get tool outputs report.");
	    throw new Exception("Lesson with lessonID: " + lessonId + " could not be found for learner progresses");
	}

	Set<ToolActivity> activities = getLessonActivities(lesson);

	Set<User> learners = new TreeSet<User>();
	// if outputsUser is null we build results for the whole lesson, otherwise - for the specified learner
	if (outputsUser != null) {

	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExistingExtUserUseridMap(serverMap,
		    outputsUser);
	    if (userMap == null) {
		// TODO: handle this error instead of throwing an exception
		LessonManagerServlet.log
			.debug("No user exists for: " + outputsUser + ". Cannot get tool outputs report.");
		throw new Exception("No user exists for: " + outputsUser + ". Cannot get tool outputs report.");
	    }
	    learners.add(userMap.getUser());

	} else {
	    learners.addAll(lesson.getAllLearners());
	    LessonManagerServlet.log.debug("Getting tool ouputs report for: " + lessonId + ". With learning design: "
		    + lesson.getLearningDesign().getLearningDesignId());
	}

	// Create the root node of the xml document
	Element toolOutputsElement = document.createElement("ToolOutputs");

	toolOutputsElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lessonId);
	toolOutputsElement.setAttribute("name", lesson.getLessonName());

	List<LearnerProgress> learnerProgresses = LessonManagerServlet.lessonService
		.getUserProgressForLesson(lesson.getLessonId());
	List<ToolSession> toolSessions = LessonManagerServlet.toolService.getToolSessionsByLesson(lesson);

	// map contains pairs toolContentId -> toolOutputDefinitions
	Map<Long, Map<String, ToolOutputDefinition>> toolOutputDefinitionsMap = new TreeMap<Long, Map<String, ToolOutputDefinition>>();
	for (ToolActivity activity : activities) {
	    Long toolContentId = activity.getToolContentId();
	    if (toolOutputDefinitionsMap.get(toolContentId) == null) {
		SortedMap<String, ToolOutputDefinition> toolOutputDefinitions = LessonManagerServlet.toolService
			.getOutputDefinitionsFromTool(toolContentId,
				ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);
		toolOutputDefinitionsMap.put(toolContentId, toolOutputDefinitions);
	    }
	}

	for (User learner : learners) {

	    Element learnerElement = document.createElement("LearnerOutput");

	    String userNoPrefixName = learner.getLogin().substring(serverMap.getPrefix().length() + 1);
	    learnerElement.setAttribute("userName", userNoPrefixName);
	    learnerElement.setAttribute("lamsUserName", learner.getLogin());
	    learnerElement.setAttribute("lamsUserId", learner.getUserId().toString());
	    learnerElement.setAttribute("firstName", learner.getFirstName());
	    learnerElement.setAttribute("lastName", learner.getLastName());

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
     * Returns lesson tool activities. It works almost the same as lesson.getLearningDesign().getActivities() except it
     * solves problem with first activity unable to cast to ToolActivity.
     */
    @SuppressWarnings("unchecked")
    private Set<ToolActivity> getLessonActivities(Lesson lesson) {
	Set<Activity> activities = new TreeSet<Activity>();
	Set<ToolActivity> toolActivities = new TreeSet<ToolActivity>();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = LessonManagerServlet.monitoringService
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
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, hash);
	    return true;
	} catch (Exception e) {
	    LessonManagerServlet.log.error("Problem verifying external server: " + serverId, e);
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
			Set<ActivityEvaluation> activityEvaluations = activity.getActivityEvaluations();
			if (activityEvaluations != null) {
			    for (ActivityEvaluation evaluation : activityEvaluations) {
				if (outputName.equals(evaluation.getToolOutputDefinition())) {
				    ToolOutput toolOutput = LessonManagerServlet.toolService
					    .getOutputFromTool(outputName, toolSession, learner.getUserId());
				    activityElement.appendChild(getOutputElement(document, toolOutput, definition));
				}
			    }
			}
		    } else {
			ToolOutput toolOutput = LessonManagerServlet.toolService.getOutputFromTool(outputName,
				toolSession, learner.getUserId());

			if (toolOutput != null) {
			    activityElement.appendChild(getOutputElement(document, toolOutput, definition));

			}

		    }

		} catch (RuntimeException e) {
		    LessonManagerServlet.log.error("Runtime exception when attempted to get outputs for activity: "
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
}
