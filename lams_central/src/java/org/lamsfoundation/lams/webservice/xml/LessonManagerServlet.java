package org.lamsfoundation.lams.webservice.xml;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
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
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LessonManagerServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(LessonManagerServlet.class);

    private static IntegrationService integrationService = null;

    private static IWorkspaceManagementService service = null;

    private static IMonitoringService monitoringService = null;

    private static ILessonService lessonService = null;

    private static IExportToolContentService exportService = null;

    private static ILamsCoreToolService toolService = null;
    
    private static IUserManagementService userManagementService = null;

    /**
     * Constructor of the object.
     */
    public LessonManagerServlet() {
	super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    @Override
    public void destroy() {
	super.destroy(); // Just puts "destroy" string in log
	// Put your code here
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

	PrintWriter out = response.getWriter();
	response.setContentType("text/xml");
	response.setCharacterEncoding("UTF8");

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
	String progressUser = request.getParameter(CentralConstants.PARAM_PROGRESS_USER);
	String outputsUser = request.getParameter("outputsUser");
	String learnerIds = request.getParameter(CentralConstants.PARAM_LEARNER_IDS);
	String monitorIds = request.getParameter(CentralConstants.PARAM_MONITOR_IDS);

	// Custom CSV string to be used for tool adapters
	String customCSV = request.getParameter(CentralConstants.PARAM_CUSTOM_CSV);

	Long ldId = null;
	Long lsId = null;
	try {
	    // TODO check input parameters are valid.

	    // Create xml document
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.newDocument();

	    Element element = null;

	    if (hashValue == null || hashValue.equals("")) {
		throw new NullPointerException("Hash value missing in parameters");
	    }

	    if (method.equals(CentralConstants.METHOD_START)) {
		ldId = new Long(ldIdStr);
		Long lessonId = startLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			country, lang, customCSV);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_PREVIEW)) {
		ldId = new Long(ldIdStr);
		Long lessonId = startPreview(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			country, lang, customCSV);

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lessonId.toString());

	    } else if (method.equals(CentralConstants.METHOD_SCHEDULE)) {
		ldId = new Long(ldIdStr);
		Long lessonId = scheduleLesson(serverId, datetime, hashValue, username, ldId, courseId, title, desc,
			startDate, country, lang, customCSV);

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
		lsId = new Long(lsIdStr);
		element = getSingleStudentProgress(document, serverId, datetime, hashValue, username, lsId, courseId,
			progressUser);

	    } else if (method.equals(CentralConstants.METHOD_IMPORT)) {

		// ldId = new Long(ldIdStr);
		Long ldID = importLearningDesign(request, response, filePath, username, serverId, customCSV);

		element = document.createElement(CentralConstants.ELEM_LEARNINGDESIGN);
		element.setAttribute(CentralConstants.PARAM_LEARNING_DESIGN_ID, ldID.toString());

	    } else if (method.equals(CentralConstants.METHOD_JOIN_LESSON)) {
		Thread t = new Thread(new AddUsersToLessonThread(serverId, datetime, username, hashValue, lsIdStr,
			courseId, country, lang, learnerIds, monitorIds, request));
		t.start();

		element = document.createElement(CentralConstants.ELEM_LESSON);
		element.setAttribute(CentralConstants.ATTR_LESSON_ID, lsIdStr);

	    } else if (method.equals("toolOutputsAllUsers")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, false);
	    } else if (method.equals("authoredToolOutputsAllUsers")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputs(document, serverId, datetime, hashValue, username, lsId, courseId, true);
	    } else if (method.equals("toolOutputsUser")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputsForUser(document, serverId, datetime, hashValue, username, lsId, courseId,
			false, outputsUser);
	    } else if (method.equals("authoredToolOutputsUser")) {
		lsId = new Long(lsIdStr);
		element = getToolOutputsForUser(document, serverId, datetime, hashValue, username, lsId, courseId,
			true, outputsUser);
	    } else {
		String msg = "Method :" + method + " is not recognised";
		LessonManagerServlet.log.error(msg);
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
	    }

	    document.appendChild(element);

	    // write out the xml document.
	    DOMSource domSource = new DOMSource(document);
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.transform(domSource, result);

	    out.write(writer.toString());

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
	}

	out.flush();
	out.close();
    }

    /**
     * The doPost method of the servlet. <br>
     * 
     * This method is called when a form has its tag value method equals to
     * post.
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
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV)
	    throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = LessonManagerServlet.monitoringService.initializeLesson(title, desc, Boolean.TRUE, ldId,
		    orgMap.getOrganisation().getOrganisationId(), userMap.getUser().getUserId(), customCSV,
		    Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, orgMap.getOrganisation(), userMap.getUser());
	    // 3. start lesson
	    LessonManagerServlet.monitoringService.startLesson(lesson.getLessonId(), userMap.getUser().getUserId());
	    
	    //store information which extServer has started the lesson
	    integrationService.createExtServerLessonMap(lesson.getLessonId(), serverMap);

	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    public Long scheduleLesson(String serverId, String datetime, String hashValue, String username, long ldId,
	    String courseId, String title, String desc, String startDate, String countryIsoCode, String langIsoCode,
	    String customCSV) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = LessonManagerServlet.monitoringService.initializeLesson(title, desc, Boolean.TRUE, ldId,
		    orgMap.getOrganisation().getOrganisationId(), userMap.getUser().getUserId(), customCSV, false,
		    false, false);
	    // 2. create lessonClass for lesson
	    createLessonClass(lesson, orgMap.getOrganisation(), userMap.getUser());
	    // 3. schedule lesson
	    Date date = DateUtil.convertFromLAMSFlashFormat(startDate);
	    LessonManagerServlet.monitoringService.startLessonOnSchedule(lesson.getLessonId(), date, userMap.getUser()
		    .getUserId(), null);
	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}
    }

    public Element getAllStudentProgress(Document document, String serverId, String datetime, String hashValue,
	    String username, long lsId, String courseID) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    Lesson lesson = LessonManagerServlet.lessonService.getLesson(lsId);

	    Element element = document.createElement(CentralConstants.ELEM_LESSON_PROGRESS);
	    element.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);

	    String prefix = serverMap.getPrefix();
	    if (lesson != null) {

		int activitiesTotal = lesson.getLearningDesign().getActivities().size();
		Iterator iterator = lesson.getLearnerProgresses().iterator();
		while (iterator.hasNext()) {
		    LearnerProgress learnProg = (LearnerProgress) iterator.next();
		    LearnerProgressDTO learnerProgress = learnProg.getLearnerProgressData();

		    // get the username with the integration prefix removed
		    String userNoPrefixName = learnerProgress.getUserName().substring(prefix.length() + 1);
		    ExtUserUseridMap learnerMap = LessonManagerServlet.integrationService.getExtUserUseridMap(
			    serverMap, userNoPrefixName);

		    Element learnerProgElem = document.createElement(CentralConstants.ELEM_LEARNER_PROGRESS);

		    int completedActivities = learnerProgress.getCompletedActivities().length;
		    int attemptedActivities = learnerProgress.getAttemptedActivities().length;

		    if (learnerProgElem.getNodeType() == Node.ELEMENT_NODE) {
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_COMPLETE, ""
				+ learnerProgress.getLessonComplete());
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITY_COUNT, "" + activitiesTotal);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_COMPLETED, ""
				+ completedActivities);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_ATTEMPTED, ""
				+ attemptedActivities);
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
	    String username, long lsId, String courseID, String progressUser) throws RemoteException {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    Lesson lesson = LessonManagerServlet.lessonService.getLesson(lsId);

	    Element element = document.createElement(CentralConstants.ELEM_LESSON_PROGRESS);
	    element.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);

	    String prefix = serverMap.getPrefix();

	    if (lesson != null) {
		int activitiesTotal = lesson.getLearningDesign().getActivities().size();

		ExtUserUseridMap progressUserMap = LessonManagerServlet.integrationService.getExistingExtUserUseridMap(
			serverMap, progressUser);

		LearnerProgress learnProg = LessonManagerServlet.lessonService.getUserProgressForLesson(userMap
			.getUser().getUserId(), lsId);

		Element learnerProgElem = document.createElement(CentralConstants.ELEM_LEARNER_PROGRESS);

		// if learner progress exists, make a response, otherwise, return an empty learner progress element
		if (learnProg != null) {
		    LearnerProgressDTO learnerProgress = learnProg.getLearnerProgressData();

		    int completedActivities = learnerProgress.getCompletedActivities().length;
		    int attemptedActivities = learnerProgress.getAttemptedActivities().length;

		    if (learnerProgElem.getNodeType() == Node.ELEMENT_NODE) {
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_COMPLETE, ""
				+ learnerProgress.getLessonComplete());
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITY_COUNT, "" + activitiesTotal);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_COMPLETED, ""
				+ completedActivities);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_ATTEMPTED, ""
				+ attemptedActivities);
			// learnerProgElem.setAttribute(CentralConstants.ATTR_CURRENT_ACTIVITY , currActivity);
			learnerProgElem.setAttribute(CentralConstants.ATTR_STUDENT_ID, "" + progressUserMap.getSid());
			learnerProgElem.setAttribute(CentralConstants.ATTR_COURSE_ID, courseID);
			learnerProgElem.setAttribute(CentralConstants.ATTR_USERNAME, progressUser);
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);
		    }
		} else {
		    if (learnerProgElem.getNodeType() == Node.ELEMENT_NODE) {
			learnerProgElem.setAttribute(CentralConstants.ATTR_LESSON_COMPLETE, "false");
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITY_COUNT, "" + activitiesTotal);
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_COMPLETED, "0");
			learnerProgElem.setAttribute(CentralConstants.ATTR_ACTIVITIES_ATTEMPTED, "0");
			// learnerProgElem.setAttribute(CentralConstants.ATTR_CURRENT_ACTIVITY , currActivity);
			learnerProgElem.setAttribute(CentralConstants.ATTR_STUDENT_ID, "" + progressUserMap.getSid());
			learnerProgElem.setAttribute(CentralConstants.ATTR_COURSE_ID, courseID);
			learnerProgElem.setAttribute(CentralConstants.ATTR_USERNAME, progressUser);
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
	    String courseId, String title, String desc, String countryIsoCode, String langIsoCode, String customCSV)
	    throws RemoteException {

	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, username, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, LoginRequestDispatcher.METHOD_MONITOR);
	    // 1. init lesson
	    Lesson lesson = LessonManagerServlet.monitoringService.initializeLessonForPreview(title, desc, ldId,
		    userMap.getUser().getUserId(), customCSV, false, false, false);
	    // 2. create lessonClass for lesson
	    LessonManagerServlet.monitoringService.createPreviewClassForLesson(userMap.getUser().getUserId(), lesson
		    .getLessonId());

	    // 3. start lesson
	    LessonManagerServlet.monitoringService.startLesson(lesson.getLessonId(), userMap.getUser().getUserId());

	    return lesson.getLessonId();
	} catch (Exception e) {
	    throw new RemoteException(e.getMessage(), e);
	}

    }

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
		SessionManager.startSession(request, response);
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
	Vector<User> learnerVector = userManagementService
			.getUsersFromOrganisationByRole(organisation
					.getOrganisationId(), Role.LEARNER, false, true);
	learnerList.addAll(learnerVector);
	LessonManagerServlet.monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
		organisation.getName() + "Learners", learnerList, organisation.getName() + "Staff", staffList, creator
			.getUserId());

    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *             if an error occured
     */
    @Override
    public void init() throws ServletException {
	LessonManagerServlet.service = (IWorkspaceManagementService) WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext()).getBean("workspaceManagementService");

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

	LessonManagerServlet.userManagementService = (IUserManagementService) WebApplicationContextUtils.getRequiredWebApplicationContext(
			getServletContext()).getBean("userManagementService");
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
	private HttpServletRequest request;

	public AddUsersToLessonThread(String serverId, String datetime, String username, String hashValue,
		String lsIdStr, String courseId, String country, String lang, String learnerIds, String monitorIds,
		HttpServletRequest request) {
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
	    this.request = request;
	}

	public void run() {
	    addUsersToLesson(serverId, datetime, username, hashValue, lsIdStr, courseId, country, lang, learnerIds,
		    monitorIds, request);
	}

	/**
	 * Adds each user in learnerIds and monitorIds as learner and staff to
	 * the given lesson id; authenticates using the 3rd party server
	 * requestor's username.
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
		String monitorIds, HttpServletRequest request) {
	    try {
		if (learnerIds != null) {
		    String[] learnerIdArray = learnerIds.split(",");
		    for (String learnerId : learnerIdArray) {
			if (StringUtils.isNotBlank(learnerId)) {
			    addUserToLesson(request, serverId, datetime, requestorUsername, hashValue,
				    LoginRequestDispatcher.METHOD_LEARNER, lsIdStr, learnerId, courseId,
				    countryIsoCode, langIsoCode);
			}
		    }
		}
		if (monitorIds != null) {
		    String[] monitorIdArray = monitorIds.split(",");
		    for (String monitorId : monitorIdArray) {
			if (StringUtils.isNotBlank(monitorId)) {
			    addUserToLesson(request, serverId, datetime, requestorUsername, hashValue,
				    LoginRequestDispatcher.METHOD_MONITOR, lsIdStr, monitorId, courseId,
				    countryIsoCode, langIsoCode);
			}
		    }
		}
		return true;
	    } catch (UserInfoFetchException e) {
		LessonManagerServlet.log.error(e, e);
		return false;
	    } catch (AuthenticationException e) {
		LessonManagerServlet.log.error(e, e);
		return false;
	    }
	}

	private void addUserToLesson(HttpServletRequest request, String serverId, String datetime,
		String requestorUsername, String hashValue, String method, String lsIdStr, String username,
		String courseId, String countryIsoCode, String langIsoCode) throws AuthenticationException,
		UserInfoFetchException {

	    if (LessonManagerServlet.log.isDebugEnabled()) {
		LessonManagerServlet.log.debug("Adding user '" + username + "' as " + method + " to lesson with id '"
			+ lsIdStr + "'.");
	    }

	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);
	    Authenticator.authenticate(serverMap, datetime, requestorUsername, hashValue);
	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExtUserUseridMap(serverMap, username);
	    // adds user to group
	    ExtCourseClassMap orgMap = LessonManagerServlet.integrationService.getExtCourseClassMap(serverMap, userMap,
		    courseId, countryIsoCode, langIsoCode, null, method);

	    if (LessonManagerServlet.lessonService == null) {
		LessonManagerServlet.lessonService = (ILessonService) WebApplicationContextUtils
			.getRequiredWebApplicationContext(request.getSession().getServletContext()).getBean(
				"lessonService");
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
     * 
     * This method gets the tool outputs for an entire lesson and returns them
     * in an XML format.
     * 
     * @param document
     * @param serverId
     * @param hashValue
     * @param username
     * @param lsId
     * @param courseID
     * @param isAuthoredToolOutputs
     * @throws Exception
     * @return
     */
    @SuppressWarnings("unchecked")
    public Element getToolOutputs(Document document, String serverId, String datetime, String hashValue,
	    String username, Long lsId, String courseID, boolean isAuthoredToolOutputs) throws Exception {
	try {
	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);

	    Authenticator.authenticate(serverMap, datetime, username, hashValue);

	    // Get the lesson and activitied given an lsId
	    Lesson lesson = LessonManagerServlet.lessonService.getLesson(lsId);
	    Set<Activity> activities = lesson.getLearningDesign().getActivities();

	    // Create the root node of the xml document
	    Element toolOutputsElement = document.createElement("ToolOutputs");

	    if (lesson != null) {
		LessonManagerServlet.log.debug("Getting tool ouputs report for: " + lsId + ". With learning design: "
			+ lesson.getLearningDesign().getLearningDesignId());

		toolOutputsElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);
		toolOutputsElement.setAttribute("name", lesson.getLessonName());

		Iterator learnerIterator = lesson.getAllLearners().iterator();
		while (learnerIterator.hasNext()) {
		    User learner = (User) learnerIterator.next();

		    String userNoPrefixName = learner.getLogin().substring(serverMap.getPrefix().length() + 1);
		    toolOutputsElement.appendChild(getLearnerOutputsElement(document, learner, lesson, activities,
			    isAuthoredToolOutputs, userNoPrefixName));
		}
	    } else {
		// TODO: handle this error instead of throwing an exception
		LessonManagerServlet.log.debug("No lesson exists for: " + lsId + ". Cannot get tool outputs report.");
		throw new Exception("Lesson with lessonID: " + lsId + " could not be found for learner progresses");
	    }

	    return toolOutputsElement;

	} catch (Exception e) {
	    LessonManagerServlet.log.error("Problem creating tool output report for lesson: " + lsId.toString(), e);
	    throw new Exception(e);
	}

    }

    /**
     * 
     * This method gets the tool outputs for a specific user in a lesson and
     * returns them in XML format.
     * 
     * @param document
     * @param serverId
     * @param hashValue
     * @param username
     * @param lsId
     * @param courseID
     * @param isAuthoredToolOutputs
     * @param userId
     * @throws Exception
     * @return
     */
    @SuppressWarnings("unchecked")
    public Element getToolOutputsForUser(Document document, String serverId, String datetime, String hashValue,
	    String username, Long lsId, String courseID, boolean isAuthoredToolOutputs, String userStr)
	    throws Exception {
	try {
	    // Create the root node of the xml document
	    Element toolOutputsElement = document.createElement("ToolOutputs");

	    ExtServerOrgMap serverMap = LessonManagerServlet.integrationService.getExtServerOrgMap(serverId);

	    Authenticator.authenticate(serverMap, datetime, username, hashValue);

	    ExtUserUseridMap userMap = LessonManagerServlet.integrationService.getExistingExtUserUseridMap(serverMap,
		    userStr);
	    if (userMap != null) {
		User learner = userMap.getUser();

		// Get the lesson and activitied given an lsId
		Lesson lesson = LessonManagerServlet.lessonService.getLesson(lsId);
		Set<Activity> activities = lesson.getLearningDesign().getActivities();

		if (lesson != null) {
		    LessonManagerServlet.log.debug("Getting tool ouputs report for: " + lsId
			    + ". With learning design: " + lesson.getLearningDesign().getLearningDesignId());

		    toolOutputsElement.setAttribute(CentralConstants.ATTR_LESSON_ID, "" + lsId);
		    toolOutputsElement.setAttribute("name", lesson.getLessonName());

		    String userNoPrefixName = learner.getLogin().substring(serverMap.getPrefix().length() + 1);
		    toolOutputsElement.appendChild(getLearnerOutputsElement(document, learner, lesson, activities,
			    isAuthoredToolOutputs, userNoPrefixName));
		}
	    } else {
		// TODO: handle this error instead of throwing an exception
		LessonManagerServlet.log.debug("No user exists for: " + userStr + ". Cannot get tool outputs report.");
		throw new Exception("No user exists for: " + userStr + ". Cannot get tool outputs report.");
	    }

	    return toolOutputsElement;

	} catch (Exception e) {
	    LessonManagerServlet.log.error("Problem creating tool output report for lesson: " + lsId.toString(), e);
	    throw new Exception(e);
	}

    }

    /**
     * Gets the outputs for an activity for a specific learner and returns them
     * in XML format
     * 
     * @param document
     * @param learner
     * @param lesson
     * @param activities
     * @param isAuthoredToolOutputs
     * @return
     */
    private Element getLearnerOutputsElement(Document document, User learner, Lesson lesson, Set<Activity> activities,
	    boolean isAuthoredToolOutputs, String userNoPrefixName) {
	Element learnerElement = document.createElement("LearnerOutput");

	learnerElement.setAttribute("userName", userNoPrefixName);
	learnerElement.setAttribute("lamsUserName", learner.getLogin());
	learnerElement.setAttribute("lamsUserId", learner.getUserId().toString());
	learnerElement.setAttribute("firstName", learner.getFirstName());
	learnerElement.setAttribute("lastName", learner.getLastName());

	LearnerProgress learnerProgress = LessonManagerServlet.monitoringService.getLearnerProgress(
		learner.getUserId(), lesson.getLessonId());

	if (learnerProgress != null) {
	    learnerElement.setAttribute("completedLesson", "" + learnerProgress.isComplete());
	}

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity
	 */
	Activity firstActivity = LessonManagerServlet.monitoringService.getActivityById(lesson.getLearningDesign()
		.getFirstActivity().getActivityId());
	LessonManagerServlet.log.debug("Getting tool ouputs for first activity: " + firstActivity.getActivityId()
		+ ". For user: " + learner.getUserId());
	if (firstActivity.isToolActivity() && firstActivity instanceof ToolActivity) {
	    learnerElement.appendChild(getActivityOutputsElement(document, (ToolActivity) firstActivity, learner,
		    learnerProgress, isAuthoredToolOutputs));
	}

	for (Activity activity : activities) {

	    if (activity.getActivityId().longValue() != firstActivity.getActivityId().longValue()) {

		LessonManagerServlet.log.debug("Getting tool ouputs for activity: " + activity.getActivityId()
			+ ". For user: " + learner.getUserId());

		if (activity.isToolActivity() && activity instanceof ToolActivity) {

		    learnerElement.appendChild(getActivityOutputsElement(document, (ToolActivity) activity, learner,
			    learnerProgress, isAuthoredToolOutputs));
		}
	    }
	}
	return learnerElement;
    }

    /**
     * Gets the tool output for a specified activity and learner and returns an
     * XML representation of the data
     * 
     * @param document
     * @param toolAct
     * @param learner
     * @param progress
     * @param isAuthoredToolOutputs
     * @return
     */
    private Element getActivityOutputsElement(Document document, ToolActivity toolAct, User learner,
	    LearnerProgress progress, boolean isAuthoredToolOutputs) {
	Element activityElement = document.createElement("Activity");
	activityElement.setAttribute("title", toolAct.getTitle());
	activityElement.setAttribute("activityId", toolAct.getActivityId().toString());
	if (toolAct.getOrderId() != null) {
	    activityElement.setAttribute("orderId", toolAct.getOrderId().toString());
	}

	boolean activityAttempted = false;
	if (progress != null) {
	    boolean completed = progress.getProgressState(toolAct) == LearnerProgress.ACTIVITY_COMPLETED;
	    activityElement.setAttribute("completed", "" + completed);

	    activityAttempted = completed || progress.getProgressState(toolAct) == LearnerProgress.ACTIVITY_ATTEMPTED;
	    activityElement.setAttribute("attempted", "" + activityAttempted);

	} else {
	    activityElement.setAttribute("attempted", "" + false);
	    activityElement.setAttribute("completed", "" + false);
	}

	if (activityAttempted) {
	    ToolSession toolSession = LessonManagerServlet.toolService.getToolSessionByLearner(learner, toolAct);
	    SortedMap<String, ToolOutputDefinition> map = LessonManagerServlet.toolService
		    .getOutputDefinitionsFromTool(toolAct.getToolContentId(),
			    ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION);

	    if (toolSession != null) {

		for (String outputName : map.keySet()) {

		    try {
			ToolOutputDefinition definition = map.get(outputName);

			if (isAuthoredToolOutputs) {
			    Set<ActivityEvaluation> activityEvaluations = toolAct.getActivityEvaluations();
			    if (activityEvaluations != null) {
				for (ActivityEvaluation evaluation : activityEvaluations) {
				    if (outputName.equals(evaluation.getToolOutputDefinition())) {
					ToolOutput toolOutput = LessonManagerServlet.toolService.getOutputFromTool(
						outputName, toolSession, learner.getUserId());
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
			LessonManagerServlet.log.debug("Runtime exception when attempted to get outputs for activity: "
				+ toolAct.getActivityId() + ", continuing for other activities", e);
		    }
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
	if (upperLimit != null && upperLimit instanceof Long) {
	    return (Long) upperLimit;
	}
	return null;
    }
}
