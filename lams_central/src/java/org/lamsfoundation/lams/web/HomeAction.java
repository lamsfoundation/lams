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

package org.lamsfoundation.lams.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.jdom.JDOMException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LessonDTOComparator;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserFlashDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.svg.SVGGenerator;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * this is an action where all lams client environments launch. initial configuration of the individual environment
 * setting is done here.
 *
 * @struts:action path="/home" validate="false" parameter="method"
 * @struts:action-forward name="sysadmin" path="/sysadmin.jsp"
 * @struts:action-forward name="lessonIntro" path="/lessonIntro.jsp"
 * @struts:action-forward name="author" path="/author.jsp"
 * @struts:action-forward name="addLesson" path="/addLesson.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 * @struts:action-forward name="passwordChange" path=".passwordChange"
 * @struts:action-forward name="index" path="/index.jsp"
 *
 */
public class HomeAction extends DispatchAction {

    private static Logger log = Logger.getLogger(HomeAction.class);

    private static IUserManagementService service;
    private static ILessonService lessonService;
    private static ILearningDesignService learningDesignService;
    private static IGroupUserDAO groupUserDAO;
    private static IWorkspaceManagementService workspaceManagementService;
    private static ISecurityService securityService;

    /**
     * request for sysadmin environment
     */
    public ActionForward sysadmin(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {

	try {
	    HomeAction.log.debug("request sysadmin");
	    int orgId = new Integer(req.getParameter("orgId")).intValue();
	    UserDTO user = getUser();
	    if (user == null) {
		HomeAction.log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else if (getService().isUserInRole(user.getUserID(), orgId, Role.SYSADMIN)) {
		HomeAction.log.debug("user is sysadmin");
		return mapping.findForward("sysadmin");
	    } else {
		HomeAction.log.error("User " + user.getLogin()
			+ " tried to get sysadmin screen but isn't sysadmin in organisation: " + orgId);
		return displayMessage(mapping, req, "error.authorisation");
	    }

	} catch (Exception e) {
	    HomeAction.log.error("Failed to load sysadmin", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for learner environment
     */
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {
	try {
	    Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	    UserDTO user = getUser();
	    if (user == null) {
		HomeAction.log.error("learner: User missing from session. ");
		return mapping.findForward("error");
	    }

	    if (!getSecurityService().isLessonLearner(lessonId, user.getUserID(), "access lesson", false)) {
		res.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the lesson");
		return null;
	    }

	    String mode = WebUtil.readStrParam(req, AttributeNames.PARAM_MODE, true);
	    Lesson lesson = lessonId != null ? getLessonService().getLesson(lessonId) : null;
	    if (!lesson.isLessonStarted()) {
		return displayMessage(mapping, req, "message.lesson.not.started.cannot.participate");
	    }
	    if (!getLessonService().checkLessonReleaseConditions(lessonId, user.getUserID())) {
		return displayMessage(mapping, req, "message.preceding.lessons.not.finished.cannot.participate");
	    }

	    // check if the lesson is scheduled to be finished to individual users
	    if (lesson.isScheduledToCloseForIndividuals()) {
		GroupUser groupUser = getGroupUserDAO().getGroupUser(lesson, user.getUserID());
		if ((groupUser != null) && (groupUser.getScheduledLessonEndDate() != null)
			&& groupUser.getScheduledLessonEndDate().before(new Date())) {
		    HomeAction.log.error("learner: User " + user.getLogin()
			    + " cannot access the lesson due to lesson end date has passed.");
		    return displayMessage(mapping, req, "error.finish.date.passed");
		}
	    }

	    // check lesson's state if its suitable for learner's access
	    if (!lesson.isLessonAccessibleForLearner()) {
		return displayMessage(mapping, req, "error.lesson.not.accessible.for.learners");
	    }

	    // show lesson intro page if required and it's not been shown already
	    boolean isLessonIntroWatched = WebUtil.readBooleanParam(req, "isLessonIntroWatched", false);
	    if (lesson.isEnableLessonIntro() && !isLessonIntroWatched) {
		req.setAttribute("learnerURL", "learnerURL");
		req.setAttribute("lesson", lesson);
		req.setAttribute("displayDesignImage", lesson.isDisplayDesignImage());
		req.setAttribute("isMonitor", lesson.getLessonClass().isStaffMember(getRealUser(user)));

		// check if we need to create learning design SVG
		if (lesson.isDisplayDesignImage()) {
		    Long learningDesignId = lesson.getLearningDesign().getLearningDesignId();
		    req.setAttribute(AttributeNames.PARAM_LEARNINGDESIGN_ID, learningDesignId);

		    if (lesson.getLearnerProgresses().isEmpty()) {
			// create svg, png files on the server
			getLearningDesignService().createLearningDesignSVG(learningDesignId,
				SVGGenerator.OUTPUT_FORMAT_SVG);
			getLearningDesignService().createLearningDesignSVG(learningDesignId,
				SVGGenerator.OUTPUT_FORMAT_PNG);
		    }
		}
		return mapping.findForward("lessonIntro");
	    }

	    if (lesson.getForceLearnerRestart()) {
		// start the lesson from the beginning each time
		getLessonService().removeLearnerProgress(lessonId, user.getUserID());
	    }

	    if (mode != null) {
		req.setAttribute(AttributeNames.PARAM_MODE, mode);
	    }

	    Boolean isPortfolioEnabled = lesson.getLearnerExportAvailable() != null ? lesson.getLearnerExportAvailable()
		    : Boolean.TRUE;
	    Organisation organisation = lesson.getOrganisation();
	    //in case of preview lesson (organisation is null) don't check organisation's settings
	    if (organisation != null) {
		isPortfolioEnabled &= organisation.getEnableExportPortfolio();
	    }

	    req.setAttribute(AttributeNames.PARAM_LESSON_ID, String.valueOf(lessonId));
	    req.setAttribute(AttributeNames.PARAM_EXPORT_PORTFOLIO_ENABLED, String.valueOf(isPortfolioEnabled));
	    req.setAttribute("allowRestart", lesson.getAllowLearnerRestart());
	    req.setAttribute(AttributeNames.PARAM_PRESENCE_ENABLED,
		    String.valueOf(lesson.getLearnerPresenceAvailable()));
	    req.setAttribute(AttributeNames.PARAM_PRESENCE_IM_ENABLED, String.valueOf(lesson.getLearnerImAvailable()));
	    req.setAttribute(AttributeNames.PARAM_TITLE, lesson.getLessonName());

	    /* Date Format for Chat room append */
	    DateFormat sfm = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    req.setAttribute(AttributeNames.PARAM_CREATE_DATE_TIME, sfm.format(lesson.getCreateDateTime()));

	    // forward to /lams/learning/main.jsp
	    String serverURLContextPath = Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH);
	    serverURLContextPath = serverURLContextPath.startsWith("/") ? serverURLContextPath
		    : "/" + serverURLContextPath;
	    serverURLContextPath += serverURLContextPath.endsWith("/") ? "" : "/";
	    getServlet().getServletContext().getContext(serverURLContextPath + "learning")
		    .getRequestDispatcher("/main.jsp").forward(req, res);
	    return null;

	} catch (Exception e) {
	    HomeAction.log.error("Failed to load learner", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for author environment
     */
    public ActionForward author(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {
	try {
	    UserDTO user = getUser();
	    if (user == null) {
		HomeAction.log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    }

	    Long learningDesignID = null;
	    String layout = null;
	    String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	    req.setAttribute("serverUrl", serverUrl);

	    String requestSrc = req.getParameter("requestSrc");
	    String notifyCloseURL = req.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL);
	    String isPostMessageToParent = req.getParameter("isPostMessageToParent");
	    String customCSV = req.getParameter(AttributeNames.PARAM_CUSTOM_CSV);
	    String extLmsId = req.getParameter(AttributeNames.PARAM_EXT_LMS_ID);

	    if (req.getParameter("learningDesignID") != null) {
		learningDesignID = WebUtil.readLongParam(req, "learningDesignID");
	    }

	    if (req.getParameter("layout") != null) {
		layout = WebUtil.readStrParam(req, "layout");
	    }

	    if (layout != null) {
		req.setAttribute("layout", layout);
	    }

	    if (req.getParameter("learningDesignID") != null) {
		learningDesignID = WebUtil.readLongParam(req, "learningDesignID");
	    }

	    if (learningDesignID != null) {
		req.setAttribute("learningDesignID", learningDesignID);
	    }

	    req.setAttribute("requestSrc", requestSrc);
	    req.setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL, notifyCloseURL);
	    req.setAttribute("isPostMessageToParent", isPostMessageToParent);
	    req.setAttribute(AttributeNames.PARAM_CUSTOM_CSV, customCSV);
	    req.setAttribute(AttributeNames.PARAM_EXT_LMS_ID, extLmsId);

	    return mapping.findForward("author");
	} catch (Exception e) {
	    HomeAction.log.error("Failed to load author", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * Request for Monitor environment.
     */
    public ActionForward monitorLesson(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {
	Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	UserDTO user = getUser();
	if (user == null) {
	    HomeAction.log.error("User missing from session. Can not open Lesson Monitor.");
	    return mapping.findForward("error");
	}

	Lesson lesson = lessonId == null ? null : getLessonService().getLesson(lessonId);
	if (lesson == null) {
	    HomeAction.log.error("Lesson " + lessonId + " does not exist. Can not open Lesson Monitor.");
	    return mapping.findForward("error");
	}

	// security check will be done there
	String url = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "monitoring/monitoring.do?method=monitorLesson&lessonID=" + lessonId;
	res.sendRedirect(url);
	return null;
    }

    @SuppressWarnings("unchecked")
    public ActionForward addLesson(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res)
	    throws IOException, UserAccessDeniedException, JSONException, RepositoryCheckedException {
	UserDTO userDTO = getUser();
	Integer organisationID = new Integer(WebUtil.readIntParam(req, "organisationID"));

	if (!getSecurityService().isGroupMonitor(organisationID, userDTO.getUserID(), "add lesson", false)) {
	    res.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	// get all user accessible folders and LD descriptions as JSON
	String folderContentsJSON = getWorkspaceManagementService().getFolderContentsJSON(null, userDTO.getUserID(),
		false);
	req.setAttribute("folderContents", folderContentsJSON);
	JSONObject users = new JSONObject();

	// get learners available for newly created lesson
	Vector<UserFlashDTO> learners = getWorkspaceManagementService().getUsersFromOrganisationByRole(organisationID,
		"LEARNER");
	for (UserFlashDTO user : learners) {
	    JSONObject userJSON = new JSONObject();
	    userJSON.put("userID", user.getUserID());
	    userJSON.put("firstName", user.getFirstName());
	    userJSON.put("lastName", user.getLastName());
	    userJSON.put("login", user.getLogin());

	    users.append("selectedLearners", userJSON);
	}

	Vector<UserFlashDTO> monitors = getWorkspaceManagementService().getUsersFromOrganisationByRole(organisationID,
		"MONITOR");
	for (UserFlashDTO user : monitors) {
	    JSONObject userJSON = new JSONObject();
	    userJSON.put("userID", user.getUserID());
	    userJSON.put("firstName", user.getFirstName());
	    userJSON.put("lastName", user.getLastName());
	    userJSON.put("login", user.getLogin());

	    if (userDTO.getUserID().equals(user.getUserID())) {
		// creator is always selected
		users.append("selectedMonitors", userJSON);
	    } else {
		users.append("unselectedMonitors", userJSON);
	    }
	}

	req.setAttribute("users", users.toString());

	// find lessons which can be set as preceding ones for newly created lesson
	Organisation organisation = (Organisation) getService().findById(Organisation.class, organisationID);
	Set<LessonDTO> availableLessons = new TreeSet<LessonDTO>(new LessonDTOComparator());
	for (Lesson availableLesson : (Set<Lesson>) organisation.getLessons()) {
	    Integer availableLessonState = availableLesson.getLessonStateId();
	    if (!Lesson.REMOVED_STATE.equals(availableLessonState)
		    && !Lesson.FINISHED_STATE.equals(availableLessonState)) {
		availableLessons.add(new LessonDTO(availableLesson));
	    }
	}
	req.setAttribute("availablePrecedingLessons", availableLessons);

	return mapping.findForward("addLesson");
    }

    /**
     * Gets subfolder contents in Add Lesson screen.
     */
    public ActionForward getFolderContents(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res)
	    throws UserAccessDeniedException, JSONException, IOException, RepositoryCheckedException {
	Integer folderID = WebUtil.readIntParam(req, "folderID", true);
	boolean allowInvalidDesigns = WebUtil.readBooleanParam(req, "allowInvalidDesigns", false);
	String folderContentsJSON = getWorkspaceManagementService().getFolderContentsJSON(folderID,
		getUser().getUserID(), allowInvalidDesigns);

	res.setContentType("application/json;charset=UTF-8");
	res.getWriter().print(folderContentsJSON);
	return null;
    }

    public ActionForward createLearningDesignThumbnail(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws JDOMException, IOException, TranscoderException {
	Long learningDesignId = WebUtil.readLongParam(req, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	Integer format = WebUtil.readIntParam(req, CentralConstants.PARAM_SVG_FORMAT, true);
	format = format == null ? SVGGenerator.OUTPUT_FORMAT_PNG : format;
	Long branchingActivityId = WebUtil.readLongParam(req, "branchingActivityID", true);
	boolean download = WebUtil.readBooleanParam(req, "download", false);
	String imagePath = null;
	if (branchingActivityId == null) {
	    imagePath = getLearningDesignService().createLearningDesignSVG(learningDesignId, format);
	} else {
	    imagePath = getLearningDesignService().createBranchingSVG(branchingActivityId, format);
	}

	// should the image be downloaded or a part of page?
	if (download) {
	    String name = getLearningDesignService()
		    .getLearningDesignDTO(learningDesignId, getUser().getLocaleLanguage()).getTitle();
	    name += "." + (format == SVGGenerator.OUTPUT_FORMAT_PNG ? "png" : "svg");
	    name = FileUtil.encodeFilenameForDownload(req, name);
	    res.setContentType("application/x-download");
	    res.setHeader("Content-Disposition", "attachment;filename=" + name);
	} else {
	    res.setContentType(format == SVGGenerator.OUTPUT_FORMAT_PNG ? "image/png" : "image/svg+xml");
	}
	OutputStream output = res.getOutputStream();
	FileInputStream input = new FileInputStream(imagePath);
	IOUtils.copy(input, output);
	IOUtils.closeQuietly(input);
	IOUtils.closeQuietly(output);

	return null;
    }

    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	UserDTO userDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	if ((userDTO.getLoggedIntoLamsCommunity() != null) && userDTO.getLoggedIntoLamsCommunity()) {
	    HomeAction.log.debug("Need to log out user from lamscoomunity");
	    req.getSession().invalidate();

	    // clear system shared session.
	    SessionManager.getSession().invalidate();

	    // redirect to lamscommunity logout servlet to log out.
	    String url = "http://lamscommunity.org/register/logout?return_url=";
	    url += URLEncoder.encode(Configuration.get(ConfigurationKeys.SERVER_URL), "UTF8");
	    res.sendRedirect(url);
	    return null;
	}
	
	req.getSession().invalidate();

	// clear system shared session.
	SessionManager.getSession().invalidate();

	return mapping.findForward("index");
    }
    
    /**
     * Redirects to URL specified in redirectURL parameter. Used by LoginRequest.
     */
    public ActionForward redirect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String redirectUrlParam = request.getParameter("redirectURL");
	if (redirectUrlParam != null) {
	    log.info("home.do?method=redirect is requested. Redirecting to " + redirectUrlParam);
	    redirectUrlParam = response.encodeRedirectURL(redirectUrlParam);
	    response.sendRedirect(redirectUrlParam);
	    return null;
	}
	
	log.warn("home.do?method=redirect is requested but no redirectURL paramter is provided.");
	String defaultUrl = request.getContextPath() + "/index.jsp";
	response.sendRedirect(defaultUrl);
	return null;
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private IUserManagementService getService() {
	if (HomeAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return HomeAction.service;
    }

    private ILessonService getLessonService() {
	if (HomeAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return HomeAction.lessonService;
    }

    private ILearningDesignService getLearningDesignService() {
	if (HomeAction.learningDesignService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.learningDesignService = (ILearningDesignService) ctx.getBean("learningDesignService");
	}
	return HomeAction.learningDesignService;
    }

    private IGroupUserDAO getGroupUserDAO() {
	if (HomeAction.groupUserDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.groupUserDAO = (IGroupUserDAO) ctx.getBean("groupUserDAO");
	}
	return HomeAction.groupUserDAO;
    }

    private IWorkspaceManagementService getWorkspaceManagementService() {
	if (HomeAction.workspaceManagementService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.workspaceManagementService = (IWorkspaceManagementService) webContext
		    .getBean("workspaceManagementService");
	}

	return HomeAction.workspaceManagementService;
    }

    private ISecurityService getSecurityService() {
	if (HomeAction.securityService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.securityService = (ISecurityService) webContext.getBean("securityService");
	}

	return HomeAction.securityService;
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return getService().getUserByLogin(dto.getLogin());
    }
}