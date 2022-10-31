package org.lamsfoundation.lams.web.controller;

import static org.imsglobal.lti.BasicLTIConstants.LTI_MESSAGE_TYPE;
import static org.imsglobal.lti.BasicLTIConstants.LTI_VERSION;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.imsglobal.lti.BasicLTIConstants;
import org.imsglobal.lti.BasicLTIUtil;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.ExtServerLessonMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.integration.util.LtiUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.oauth.OAuth;

/**
 * Shows either addLesson.jsp or learnerMonitor.jsp pages.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/lti")
public class LtiController {

    private static Logger log = Logger.getLogger(LtiController.class);

    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private IMonitoringService monitoringService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private IWorkspaceManagementService workspaceManagementService;
    @Autowired
    private ISecurityService securityService;

    /**
     * Single entry point to LAMS LTI processing mechanism. It determines here whether to show author or learnerMonitor
     * pages
     */
    @RequestMapping("")
    public String unspecified(HttpServletRequest request, HttpServletResponse respnse) throws IOException,
	    UserAccessDeniedException, RepositoryCheckedException, UserInfoFetchException, UserInfoValidationException {
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String tcGradebookId = request.getParameter(BasicLTIConstants.LIS_RESULT_SOURCEDID);
	String extUserId = request.getParameter(BasicLTIConstants.USER_ID);
	String customContextMembershipUrl = request.getParameter(LtiUtils.CUSTOM_CONTEXT_MEMBERSHIPS_URL);
	Long customLessonId = WebUtil.readLongParam(request, "custom_lessonid", true);
	boolean isContentItemSelection = WebUtil.readBooleanParam(request, "custom_iscontentitemselection", false);
	//parameter containing original resource_link_id, available after course copy (BB only)
	String resourceLinkIdHistory = request.getParameter("resource_link_id_history");

	ExtServerLessonMap extLessonMap = integrationService.getLtiConsumerLesson(consumerKey, resourceLinkId);
	ExtServer extServer = integrationService.getExtServer(consumerKey);

	//it's the case of ContentItemSelection request or course-copy. (Currently we support course-copy only for links created using ContentItemSelection or from Blackboard server)
	if (extLessonMap == null && (isContentItemSelection || StringUtils.isNotBlank(resourceLinkIdHistory))) {

	    //figure out original lesson
	    Lesson lesson;
	    if (isContentItemSelection) {
		lesson = lessonService.getLesson(customLessonId);

	    } else {
		ExtServerLessonMap oldExtLessonMap = integrationService.getLtiConsumerLesson(consumerKey,
			resourceLinkIdHistory);
		lesson = lessonService.getLesson(oldExtLessonMap.getLessonId());
	    }

	    if (lesson == null) {
		//abort as this is not normal case
		request.setAttribute("messageKey", "lessonsearch.noresults");
		return "msgContent";
	    }

	    String extCourseId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
	    ExtCourseClassMap currentOrgMap = integrationService.getExtCourseClassMap(extServer.getSid(), extCourseId);
	    Integer currentOrganisationId = currentOrgMap.getOrganisation().getOrganisationId();

	    //check if the new lesson should be created after course copy, that potentially has happened on LMS side;
	    //(we can detect it by comparing orgId of the custom_lessonid's organisation and CONTEXT_ID's one).
	    boolean isLessonCopyRequired = lesson.getOrganisation() != null
		    && !lesson.getOrganisation().getOrganisationId().equals(currentOrganisationId);
	    if (isLessonCopyRequired) {

		//add users to the course
		integrationService.addUsersUsingMembershipService(extServer, null, extCourseId, resourceLinkId,
			customContextMembershipUrl);

		// clone lesson
		Integer creatorId = lesson.getUser().getUserId();
		Long newLessonId = monitoringService.cloneLesson(lesson.getLessonId(), creatorId, true, true, null,
			null, currentOrgMap.getOrganisation());
		// store information which extServer has started the lesson
		extLessonMap = integrationService.createExtServerLessonMap(newLessonId, extServer);

		extLessonMap.setResourceLinkId(resourceLinkId);
		userManagementService.save(extLessonMap);
	    }
	}

	//update lessonFinishCallbackUrl. We store it one time during the very first call to LAMS and it stays the same all the time afterwards
	String lessonFinishCallbackUrl = request.getParameter(BasicLTIConstants.LIS_OUTCOME_SERVICE_URL);
	if (StringUtils.isNotBlank(lessonFinishCallbackUrl) && StringUtils.isBlank(extServer.getLessonFinishUrl())) {
	    extServer.setLessonFinishUrl(lessonFinishCallbackUrl);
	    userManagementService.save(extServer);
	}

	//update MembershipUrl. We store it one time during the very first call to LAMS and it stays the same all the time afterwards
	String membershipUrl = request.getParameter("custom_context_memberships_url");
	if (StringUtils.isNotBlank(membershipUrl) && StringUtils.isBlank(extServer.getMembershipUrl())) {
	    extServer.setMembershipUrl(membershipUrl);
	    userManagementService.save(extServer);
	}

	//check if learner tries to access the link that hasn't been authored by teacher yet
	String method = request.getParameter("_" + IntegrationConstants.PARAM_METHOD);
	if (IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method) && extLessonMap == null) {
	    String roles = request.getParameter(BasicLTIConstants.ROLES);
	    //try to detect monitor with custom roles not supported by LTI specification
	    String messageKey = roles.contains("Instructor") || roles.contains("Admin")
		    ? "message.teacher.role.not.recognized"
		    : "message.lesson.not.started.cannot.participate";

	    request.setAttribute("messageKey", messageKey);
	    return "msgContent";
	}

	//determine whether to show author or learnerMonitor pages
	if (extLessonMap == null) {
	    return addLesson(request, respnse);

	} else {

	    //as per LTI spec we need to update tool consumer's gradebook id on every LTI call
	    ExtUserUseridMap extUserMap = integrationService.getExistingExtUserUseridMap(extServer, extUserId);
	    extUserMap.setTcGradebookId(tcGradebookId);
	    userManagementService.save(extUserMap);

	    return learnerMonitor(request, respnse);
	}
    }

    /**
     * When teacher accesses link for the very first time, we show him addLesson page where he can choose learning
     * design and start a lesson.
     */
    @RequestMapping("/addLesson")
    private String addLesson(HttpServletRequest request, HttpServletResponse response) throws IOException,
	    UserAccessDeniedException, RepositoryCheckedException, UserInfoFetchException, UserInfoValidationException {
	Integer userId = getUser().getUserID();
	String contextId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
	String customContextMembershipUrl = request.getParameter(LtiUtils.CUSTOM_CONTEXT_MEMBERSHIPS_URL);
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String resourceLinkTitle = request.getParameter(BasicLTIConstants.RESOURCE_LINK_TITLE);
	String resourceLinkDescription = request.getParameter(BasicLTIConstants.RESOURCE_LINK_DESCRIPTION);

	ExtServer extServer = integrationService.getExtServer(consumerKey);
	ExtCourseClassMap extCourse = integrationService.getExtCourseClassMap(extServer.getSid(), contextId);
	Integer organisationId = extCourse.getOrganisation().getOrganisationId();
	//only monitors are allowed to create lesson
	if (!securityService.isGroupMonitor(organisationId, userId, "add lesson")) {
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
	request.setAttribute(LtiUtils.CUSTOM_CONTEXT_MEMBERSHIPS_URL, customContextMembershipUrl);
	request.setAttribute(CentralConstants.PARAM_TITLE, resourceLinkTitle);
	request.setAttribute(CentralConstants.PARAM_DESC, resourceLinkDescription);
	request.setAttribute(BasicLTIConstants.TOOL_CONSUMER_INFO_PRODUCT_FAMILY_CODE,
		request.getParameter(BasicLTIConstants.TOOL_CONSUMER_INFO_PRODUCT_FAMILY_CODE));

	//support for ContentItemSelectionRequest
	String ltiMessageType = request.getParameter(BasicLTIConstants.LTI_MESSAGE_TYPE);
	if (LtiUtils.LTI_MESSAGE_TYPE_CONTENTITEMSELECTIONREQUEST.equals(ltiMessageType)) {
	    String contentItemReturnUrl = request.getParameter(LtiUtils.CONTENT_ITEM_RETURN_URL);
	    if (StringUtils.isEmpty(contentItemReturnUrl)) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST,
			"ContentItemSelectionRequest is missing content_item_return_url parameter");
		return null;
	    }

	    request.setAttribute(BasicLTIConstants.LTI_MESSAGE_TYPE, ltiMessageType);
	    request.setAttribute(LtiUtils.CONTENT_ITEM_RETURN_URL, contentItemReturnUrl);
	    request.setAttribute("title", request.getParameter("title"));
	    //text parameter can be null in case of BB server
	    String description = request.getParameter("text") == null ? ""
		    : request.getParameter("text").replaceAll("\\<[^>]*>", "");
	    request.setAttribute("desc", description);
	    request.setAttribute("data", request.getParameter("data"));
	}

	return "lti/addLesson";
    }

    /**
     * Starts a lesson. Then prompts to learnerMonitor page or autosubmitForm (in case of ContentItemSelectionRequest).
     */
    @RequestMapping("/startLesson")
    public String startLesson(HttpServletRequest request, HttpServletResponse response) throws IOException,
	    UserAccessDeniedException, RepositoryCheckedException, UserInfoValidationException, UserInfoFetchException {
	Integer userId = getUser().getUserID();
	User user = getRealUser(getUser());

	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);
	String title = request.getParameter(CentralConstants.PARAM_TITLE);
	String desc = request.getParameter(CentralConstants.PARAM_DESC);
	String ldIdStr = request.getParameter(CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String extCourseId = request.getParameter(BasicLTIConstants.CONTEXT_ID);
	String customContextMembershipUrl = request.getParameter(LtiUtils.CUSTOM_CONTEXT_MEMBERSHIPS_URL);
	String toolConsumerFamily = request.getParameter(BasicLTIConstants.TOOL_CONSUMER_INFO_PRODUCT_FAMILY_CODE);
	Integer organisationId = WebUtil.readIntParam(request, CentralConstants.ATTR_COURSE_ID);
	boolean enableLessonIntro = WebUtil.readBooleanParam(request, "enableLessonIntro", false);

	//only monitors are allowed to create lesson
	if (!securityService.isGroupMonitor(organisationId, userId, "add lesson")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	ExtServer extServer = integrationService.getExtServer(consumerKey);
	Organisation organisation = monitoringService.getOrganisation(organisationId);

	// 1. init lesson
	Boolean liveEditEnabled = extServer.getLiveEditEnabled() == null ? false : extServer.getLiveEditEnabled();
	Lesson lesson = monitoringService.initializeLesson(title, desc, new Long(ldIdStr),
		organisation.getOrganisationId(), user.getUserId(), null, false, enableLessonIntro,
		extServer.getLearnerPresenceAvailable(), extServer.getLearnerImAvailable(), liveEditEnabled,
		extServer.getEnableLessonNotifications(), extServer.getForceLearnerRestart(),
		extServer.getAllowLearnerRestart(), extServer.getGradebookOnComplete(), null, null);
	Long lessonId = lesson.getLessonId();
	// 2. create lessonClass for lesson
	List<User> staffList = new LinkedList<>();
	staffList.add(user);
	List<User> learnerList = new LinkedList<>();
	Vector<User> learnerVector = userManagementService
		.getUsersFromOrganisationByRole(organisation.getOrganisationId(), Role.LEARNER, true);
	learnerList.addAll(learnerVector);
	monitoringService.createLessonClassForLesson(lessonId, organisation, organisation.getName() + "Learners",
		learnerList, organisation.getName() + "Staff", staffList, user.getUserId());
	// 3. start lesson
	monitoringService.startLesson(lessonId, user.getUserId());
	// store information which extServer has started the lesson

	/*
	 * Blackboard sends a resource link ID which corresponds to a content space and not the LAMS lesson which is
	 * being created.
	 * If we map the received ID to a newly created lesson right now, then we can not create another lesson for this
	 * content space. It is because we receive the content space ID which is already mapped to a lesson and we
	 * display the lesson instead of authoring interface.
	 * The real ID is only received when an user enters the lesson and only then we can create the mapping.
	 *
	 * BUT if there is another integration client which does not use deep linking and sends proper resource link ID
	 * right away, then we use this link. Currently we are aware of desire2learn that uses this approach.
	 */
	boolean useResourceLinkId = toolConsumerFamily == null
		|| !toolConsumerFamily.toLowerCase().contains("blackboard");
	integrationService.createExtServerLessonMap(lessonId, useResourceLinkId ? resourceLinkId : null, extServer);

	integrationService.addUsersUsingMembershipService(extServer, lessonId, extCourseId, resourceLinkId,
		customContextMembershipUrl);

	//support for ContentItemSelectionRequest
	String ltiMessageType = request.getParameter(BasicLTIConstants.LTI_MESSAGE_TYPE);
	String contentItemReturnUrl = request.getParameter(LtiUtils.CONTENT_ITEM_RETURN_URL);
	if (LtiUtils.LTI_MESSAGE_TYPE_CONTENTITEMSELECTIONREQUEST.equals(ltiMessageType)) {
	    String opaqueTCData = request.getParameter("data");

	    // Get the post data for the placement
	    String returnValues = postLaunchHTML(extServer, lesson, contentItemReturnUrl, opaqueTCData);

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("utf-8");
	    response.addDateHeader("Expires", System.currentTimeMillis() - (1000L * 60L * 60L * 24L * 365L));
	    response.addDateHeader("Last-Modified", System.currentTimeMillis());
	    response.addHeader("Cache-Control",
		    "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
	    response.addHeader("Pragma", "no-cache");
	    ServletOutputStream out = response.getOutputStream();

	    out.println("<!DOCTYPE html>");
	    out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
	    out.println("<html>\n<head>");
	    out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
	    out.println("</head>\n<body>");
	    out.println(returnValues);
	    out.println("</body>\n</html>");

	    return null;

	    //regular BasicLTI request
	} else {
	    //set roles to contain monitor so that the user can see monitor link
	    String redirectURL = "redirect:/lti/learnerMonitor.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, LtiUtils.OAUTH_CONSUMER_KEY, consumerKey);
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, BasicLTIConstants.RESOURCE_LINK_ID, resourceLinkId);
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, BasicLTIConstants.CONTEXT_ID, extCourseId);
	    return redirectURL;
	}
    }

    /**
     * Return HTML form to be posted to TC
     */
    private String postLaunchHTML(ExtServer extServer, Lesson lesson, String contentItemReturnUrl, String opaqueTCData)
	    throws UnsupportedEncodingException {
	String key = extServer.getServerid();
	String secret = extServer.getServerkey();

	//required parameters
//	  <input type="hidden" name="lti_message_type" value="ContentItemSelection" />
//	  <input type="hidden" name="lti_version" value="LTI-1p0" />
//	  <input type="hidden" name="content_items" value="{ &quot;@context&quot;: &quot;http://purl.imsglobal.org/ctx/lti/v1/ContentItem&quot;, &quot;@graph&quot;: [ { &quot;@type&quot;: &quot;FileItem&quot;, &quot;url&quot;: &quot;https://www.imsglobal.org/sites/default/files/IMSconformancelogosm.png&quot;, &quot;mediaType&quot;: &quot;image/png&quot;, &quot;text&quot;: &quot;IMS logo for certified products&quot;, &quot;title&quot;: &quot;The logo used to identify IMS certified products&quot;, &quot;placementAdvice&quot;: { &quot;displayWidth&quot;: 147, &quot;displayHeight&quot;: 184, &quot;presentationDocumentTarget&quot;: &quot;embed&quot; } } ] }" />
//	  <input type="hidden" name="data" value="Some opaque TC data" />
//	  <input type="hidden" name="oauth_version" value="1.0" />
//	  <input type="hidden" name="oauth_nonce" value="..." />
//	  <input type="hidden" name="oauth_timestamp" value="..." />
//	  <input type="hidden" name="oauth_consumer_key" value="${oauth_consumer_key}" />
//	  <input type="hidden" name="oauth_callback" value="about:blank" />
//	  <input type="hidden" name="oauth_signature_method" value="HMAC-SHA1" />
//	  <input type="hidden" name="oauth_signature" value="..." />

	Properties properties = new Properties();
	properties.put(LTI_MESSAGE_TYPE, "ContentItemSelection");
	properties.put(LTI_VERSION, "LTI-1p0");
	if (StringUtils.isNotBlank(opaqueTCData)) {
	    properties.put("data", opaqueTCData);
	}

	properties.put("lti_msg", "Information about LAMS lesson has been imported successfully.");
	properties.put(OAuth.OAUTH_VERSION, OAuth.VERSION_1_0);
	properties.put("oauth_callback", "about:blank");
	properties.put(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);

	//contentItem Json
//	{
//	    "@context" : "http://purl.imsglobal.org/ctx/lti/v1/ContentItem",
//	    "@graph" : [
//	{
//	    "@type" : "LtiLinkItem",
//	    "mediaType" : "application/vnd.ims.lti.v1.ltilink",
//	    "icon" : {
//	      "@id" : "https://www.server.com/path/animage.png",
//	      "width" : 50,
//	      "height" : 50
//	    },
//	    "title" : "Week 1 reading",
//	    "text" : "Read this section prior to your tutorial.",
//	    "custom" : {
//	      "chapter" : "12",
//	      "section" : "3"
//	    },
//	    "lineItem":{
//	      "reportingMethod":"http://purl.imsglobal.org/ctx/lis/v2p1/Result#totalScore",
//	      "@type":"LineItem",
//	      "assignedActivity":{
//	        "activityId":"a-9334df-33"
//	      },
//	      "scoreConstraints":{
//	        "@type":"NumericLimits",
//	        "normalMaximum":45,
//	        "extraCreditMaximum":5,
//	        "totalMaximum":50
//	      }
//	    },
//	  }
//	]
//    }
	ObjectNode contentItemJSON = JsonNodeFactory.instance.objectNode();
	contentItemJSON.put("@type", "LtiLinkItem");
	contentItemJSON.put("mediaType", "application/vnd.ims.lti.v1.ltilink");
	contentItemJSON.put("title", lesson.getLessonName());
	contentItemJSON.put("text", lesson.getLessonDescription());
	ObjectNode customJSON = JsonNodeFactory.instance.objectNode();
	customJSON.put("lessonid", lesson.getLessonId().toString());
	customJSON.put("iscontentitemselection", "true");
	contentItemJSON.set("custom", customJSON);

	ObjectNode lineItem = JsonNodeFactory.instance.objectNode();
	lineItem.put("reportingMethod", "http://purl.imsglobal.org/ctx/lis/v2p1/Result#totalScore");
	lineItem.put("@type", "LineItem");
	lineItem.put("label", lesson.getLessonName());
	ObjectNode assignedActivity = JsonNodeFactory.instance.objectNode();
	assignedActivity.put("activityId", key + "_" + lesson.getLessonId().toString());
	lineItem.set("assignedActivity", assignedActivity);
	ObjectNode scoreConstraints = JsonNodeFactory.instance.objectNode();
	scoreConstraints.put("@type", "NumericLimits");
	scoreConstraints.put("normalMaximum", 10);
	scoreConstraints.put("extraCreditMaximum", 1);
	scoreConstraints.put("totalMaximum", 100);
	lineItem.set("scoreConstraints", scoreConstraints);
	contentItemJSON.set("lineItem", lineItem);

	ArrayNode graph = JsonNodeFactory.instance.arrayNode();
	graph.add(contentItemJSON);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.set("@graph", graph);
	responseJSON.put("@context", "http://purl.imsglobal.org/ctx/lti/v1/ContentItem");

	String content_items = responseJSON.toString();//URLEncoder.encode(responseJSON.toString(), "UTF-8");
//	content_items = content_items.replace("\"", "%22");
//	content_items = content_items.replace("'", "%27");
	properties.put("content_items", content_items);

	properties = BasicLTIUtil.signProperties(properties, contentItemReturnUrl, "POST", key, secret, null, null,
		null);
	boolean dodebug = false;
	String postData = BasicLTIUtil.postLaunchHTML(properties, contentItemReturnUrl, dodebug);
	return postData;
    }

    /**
     * Once lesson was created, start showing learnerMonitor page to everybody regardless of his role.
     */
    @RequestMapping("/learnerMonitor")
    public String learnerMonitor(HttpServletRequest request, HttpServletResponse response) throws IOException,
	    UserAccessDeniedException, RepositoryCheckedException, UserInfoValidationException, UserInfoFetchException {
	Integer userId = getUser().getUserID();
	String consumerKey = request.getParameter(LtiUtils.OAUTH_CONSUMER_KEY);
	String resourceLinkId = request.getParameter(BasicLTIConstants.RESOURCE_LINK_ID);

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
	// only teachers can see "Open monitor" link
	boolean isMonitor = securityService.isLessonMonitor(lessonId, userId, null, true);
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

	return "lti/learnerMonitor";
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return userManagementService.getUserByLogin(dto.getLogin());
    }

}
