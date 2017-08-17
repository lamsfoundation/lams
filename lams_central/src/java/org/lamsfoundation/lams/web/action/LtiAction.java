package org.lamsfoundation.lams.web.action;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONException;
import org.imsglobal.lti.BasicLTIConstants;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerLessonMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.integration.util.LtiUtils;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Shows either addLesson.jsp or learnerMonitor.jsp pages.
 * 
 * @author Andrey Balan
 */
public class LtiAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LtiAction.class);
    private static IIntegrationService integrationService = null;
    private static IMonitoringService monitoringService = null;
    private static IUserManagementService userManagementService = null;
    private static ILearningDesignService learningDesignService;
    private static ILessonService lessonService = null;
    private static IWorkspaceManagementService workspaceManagementService;
    private static ISecurityService securityService;

    /**
     * Single entry point to LAMS LTI processing mechanism. It determines here whether to show author or learnerMonitor
     * pages
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, UserAccessDeniedException, JSONException,
	    RepositoryCheckedException, UserInfoFetchException, UserInfoValidationException {
	initServices();
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String tcGradebookId = request.getParameter(BasicLTIConstants.LIS_RESULT_SOURCEDID);
	String extUserId = request.getParameter(BasicLTIConstants.USER_ID);
	
	ExtServerLessonMap lesson = integrationService.getLtiConsumerLesson(consumerKey, resourceLinkId);
	
	//update lessonFinishCallbackUrl. We store it one time during the very first call to LAMS and it stays the same all the time afterwards
	String lessonFinishCallbackUrl = request.getParameter(BasicLTIConstants.LIS_OUTCOME_SERVICE_URL);
	ExtServer extServer = integrationService.getExtServer(consumerKey);
	if (StringUtils.isNotBlank(lessonFinishCallbackUrl) && StringUtils.isBlank(extServer.getLessonFinishUrl())) {
	    extServer.setLessonFinishUrl(lessonFinishCallbackUrl);
	    userManagementService.save(extServer);
	}

	//check if learner tries to access the link that hasn't been authored by teacher yet
	String method = request.getParameter("_" + LoginRequestDispatcher.PARAM_METHOD);
	if (LoginRequestDispatcher.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method) && lesson == null) {
	    String errorMsg = "Learner tries to access the link that hasn't been authored by teacher yet. resource_link_id: "
		    + tcGradebookId;
	    log.debug(errorMsg);
	    request.setAttribute("error", errorMsg);
	    request.setAttribute("javax.servlet.error.exception", new UserAccessDeniedException(errorMsg));
	    return mapping.findForward("error");
	}

	//determine whether to show author or learnerMonitor pages	
	if (lesson == null) {
	    return addLesson(mapping, form, request, response);
	    
	} else {

	    //as per LTI spec we need to update tool consumer's gradebook id on every LTI call 
	    ExtUserUseridMap extUserMap = integrationService.getExistingExtUserUseridMap(extServer, extUserId);
	    extUserMap.setTcGradebookId(tcGradebookId);
	    userManagementService.save(extUserMap);

	    return learnerMonitor(mapping, form, request, response);
	}

    }

    /**
     * When teacher accesses link for the very first time, we show him addLesson page where he can choose learning
     * design and start a lesson.
     */
    public ActionForward addLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, UserAccessDeniedException, JSONException,
	    RepositoryCheckedException, UserInfoFetchException, UserInfoValidationException {
	initServices();
	Integer userId = getUser().getUserID();
	String contextId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String resourceLinkTitle = request.getParameter(BasicLTIConstants.RESOURCE_LINK_TITLE);
	String resourceLinkDescription = request.getParameter(BasicLTIConstants.RESOURCE_LINK_DESCRIPTION);

	ExtServer extServer = integrationService.getExtServer(consumerKey);
	ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer.getSid(), contextId);
	Integer organisationId = orgMap.getOrganisation().getOrganisationId();
	//only monitors are allowed to create lesson
	if (!securityService.isGroupMonitor(organisationId, userId, "add lesson", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	// get all user accessible folders and LD descriptions as JSON
	String folderContentsJSON = workspaceManagementService.getFolderContentsJSON(null, userId, false);
	request.setAttribute("folderContents", folderContentsJSON);
	request.setAttribute(LtiUtils.OAUTH_CONSUMER_KEY, consumerKey);
	request.setAttribute(BasicLTIConstants.RESOURCE_LINK_ID, resourceLinkId);
	request.setAttribute(CentralConstants.ATTR_COURSE_ID, organisationId);
	request.setAttribute(BasicLTIConstants.CONTEXT_ID, contextId);
	request.setAttribute(CentralConstants.PARAM_TITLE, resourceLinkTitle);
	request.setAttribute(CentralConstants.PARAM_DESC, resourceLinkDescription);

	return mapping.findForward("addLesson");
    }

    /**
     * Starts a lesson. Then prompts to learnerMonitor page.
     */
    public ActionForward startLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, UserAccessDeniedException, JSONException,
	    RepositoryCheckedException, UserInfoValidationException, UserInfoFetchException {
	initServices();
	Integer userId = getUser().getUserID();
	User user = getRealUser(getUser());

	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String title = request.getParameter(CentralConstants.PARAM_TITLE);
	String desc = request.getParameter(CentralConstants.PARAM_DESC);
	String ldIdStr = request.getParameter(CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String contextId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
	Integer organisationId = new Integer(WebUtil.readIntParam(request, CentralConstants.ATTR_COURSE_ID));
	boolean enableLessonIntro = WebUtil.readBooleanParam(request, "enableLessonIntro", false);
	
	//only monitors are allowed to create lesson
	if (!securityService.isGroupMonitor(organisationId, userId, "add lesson", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	ExtServer extServer = integrationService.getExtServer(consumerKey);
	Organisation organisation = monitoringService.getOrganisation(organisationId);

	// 1. init lesson
	Lesson lesson = monitoringService.initializeLesson(title, desc, new Long(ldIdStr),
		organisation.getOrganisationId(), user.getUserId(), null, false, enableLessonIntro, false, false,
		true, true, false, false, null, null);
	// 2. create lessonClass for lesson
	List<User> staffList = new LinkedList<User>();
	staffList.add(user);
	List<User> learnerList = new LinkedList<User>();
	Vector<User> learnerVector = userManagementService
		.getUsersFromOrganisationByRole(organisation.getOrganisationId(), Role.LEARNER, true);
	learnerList.addAll(learnerVector);
	monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
		organisation.getName() + "Learners", learnerList, organisation.getName() + "Staff", staffList,
		user.getUserId());
	// 3. start lesson
	monitoringService.startLesson(lesson.getLessonId(), user.getUserId());
	// store information which extServer has started the lesson
	integrationService.createExtServerLessonMap(lesson.getLessonId(), resourceLinkId, extServer);

	//set roles to contain monitor so that the user can see monitor link
	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("learnerMonitorRedirect"));
	redirect.addParameter(LtiUtils.OAUTH_CONSUMER_KEY, consumerKey);
	redirect.addParameter(BasicLTIConstants.RESOURCE_LINK_ID, resourceLinkId);
	redirect.addParameter(BasicLTIConstants.CONTEXT_ID, contextId);
	return redirect;
    }

    /**
     * Once lesson was created, start showing learnerMonitor page to everybody regardless of his role.
     */
    public ActionForward learnerMonitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, UserAccessDeniedException, JSONException,
	    RepositoryCheckedException, UserInfoValidationException, UserInfoFetchException {
	initServices();
	Integer userId = getUser().getUserID();
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	
	//get orgId
//	String contextId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
//	ExtServer extServer = integrationService.getExtServer(consumerKey);
//	ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer.getSid(), contextId);
//	Integer organisationId = orgMap.getOrganisation().getOrganisationId();

	//get lesson
	ExtServerLessonMap extLesson = integrationService.getLtiConsumerLesson(consumerKey, resourceLinkId);
	Long lessonId = extLesson.getLessonId();
	Lesson lesson = (Lesson) userManagementService.findById(Lesson.class, lessonId);

	//set all required attributes for jsp
	request.setAttribute("lessonId", lessonId);
	request.setAttribute("ldId", lesson.getLearningDesign().getLearningDesignId());
	request.setAttribute("title", lesson.getLessonName());
	request.setAttribute("description", lesson.getLessonDescription());
	request.setAttribute("isDisplayDesignImage", lesson.isDisplayDesignImage());
	boolean isMonitor = securityService.isLessonMonitor(lessonId, userId, "learner monitor", false);
	request.setAttribute("isMonitor", isMonitor);

	//get learnerProgressDto
	LearnerProgress learnProg = lessonService.getUserProgressForLesson(userId, lessonId);
	if (learnProg != null) {
	    LearnerProgressDTO learnerProgress = learnProg.getLearnerProgressData();
	    request.setAttribute("learnerProgressDto", learnerProgress);
	}
	
	// check if we need to create svg, png files on the server
//	if (lesson.isDisplayDesignImage() && lesson.getLearnerProgresses().isEmpty()) {
//	    Long learningDesignId = lesson.getLearningDesign().getLearningDesignId();
//	    learningDesignService.createLearningDesignSVG(learningDesignId, SVGGenerator.OUTPUT_FORMAT_SVG);
//	    learningDesignService.createLearningDesignSVG(learningDesignId, SVGGenerator.OUTPUT_FORMAT_PNG);
//	}

	return mapping.findForward("learnerMonitor");
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return userManagementService.getUserByLogin(dto.getLogin());
    }

    private void initServices() {
	if (integrationService == null) {
	    integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("integrationService");
	}

	if (monitoringService == null) {
	    monitoringService = (IMonitoringService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("monitoringService");
	}

	if (userManagementService == null) {
	    userManagementService = (IUserManagementService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext())
		    .getBean("userManagementService");
	}
	
	if (learningDesignService == null) {
	    learningDesignService = (ILearningDesignService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext())
		    .getBean("learningDesignService");
	}

	if (lessonService == null) {
	    lessonService = (ILessonService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("lessonService");
	}

	if (workspaceManagementService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    workspaceManagementService = (IWorkspaceManagementService) webContext.getBean("workspaceManagementService");
	}

	if (securityService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    securityService = (ISecurityService) webContext.getBean("securityService");
	}
    }

}
