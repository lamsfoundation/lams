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
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserFlashDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.svg.SVGGenerator;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.lamsfoundation.lams.workspace.service.WorkspaceManagementService;
import org.lamsfoundation.lams.workspace.web.WorkspaceAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * this is an action where all lams client environments launch. initial configuration of the individual environment
 * setting is done here.
 * 
 * @struts:action path="/home" validate="false" parameter="method"
 * @struts:action-forward name="sysadmin" path="/sysadmin.jsp"
 * @struts:action-forward name="learner" path="/learner.jsp"
 * @struts:action-forward name="lessonIntro" path="/lessonIntro.jsp"
 * @struts:action-forward name="author" path="/author.jsp"
 * @struts:action-forward name="monitorLesson" path="/monitorLesson.jsp"
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
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	try {
	    HomeAction.log.debug("request learner");

	    Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	    String mode = WebUtil.readStrParam(req, AttributeNames.PARAM_MODE, true);

	    UserDTO user = getUser();
	    if (user == null) {
		HomeAction.log.error("learner: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Lesson lesson = lessonId != null ? getLessonService().getLesson(lessonId) : null;
		if ((lesson == null) || !lesson.isLessonStarted()) {
		    return displayMessage(mapping, req, "message.lesson.not.started.cannot.participate");
		}
		if (!getLessonService().checkLessonReleaseConditions(lessonId, user.getUserID())) {
		    return displayMessage(mapping, req, "message.preceding.lessons.not.finished.cannot.participate");
		}

		if ((lesson.getLessonClass() == null)
			|| !lesson.getLessonClass().getLearners().contains(getRealUser(user))) {
		    HomeAction.log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested lesson. Cannot access the lesson.");
		    return displayMessage(mapping, req, "error.authorisation");
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

		if (mode != null) {
		    req.setAttribute(AttributeNames.PARAM_MODE, mode);
		}

		req.setAttribute(AttributeNames.PARAM_EXPORT_PORTFOLIO_ENABLED,
			lesson.getLearnerExportAvailable() != null ? lesson.getLearnerExportAvailable() : Boolean.TRUE);
		req.setAttribute(AttributeNames.PARAM_PRESENCE_ENABLED, lesson.getLearnerPresenceAvailable());
		req.setAttribute(AttributeNames.PARAM_PRESENCE_IM_ENABLED, lesson.getLearnerImAvailable());
		req.setAttribute(AttributeNames.PARAM_TITLE, lesson.getLessonName());

		/* Date Format for Chat room append */
		DateFormat sfm = new SimpleDateFormat("yyyyMMdd_HHmmss");
		req.setAttribute(AttributeNames.PARAM_CREATE_DATE_TIME, sfm.format(lesson.getCreateDateTime()));

		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		req.setAttribute("serverUrl", serverUrl);
		req.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);

		// show lesson intro page if required
		if (lesson.isEnableLessonIntro()) {
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
		} else {
		    return mapping.findForward("learner");
		}

	    }

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
	    HomeAction.log.debug("request author");
	    UserDTO user = getUser();
	    if (user == null) {
		HomeAction.log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Long learningDesignID = null;
		String layout = null;
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		req.setAttribute("serverUrl", serverUrl);

		String requestSrc = req.getParameter("requestSrc");
		String notifyCloseURL = req.getParameter("notifyCloseURL");
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
		req.setAttribute("notifyCloseURL", notifyCloseURL);
		req.setAttribute("isPostMessageToParent", isPostMessageToParent);
		req.setAttribute(AttributeNames.PARAM_CUSTOM_CSV, customCSV);
		req.setAttribute(AttributeNames.PARAM_EXT_LMS_ID, extLmsId);

		return mapping.findForward("author");
	    }

	} catch (Exception e) {
	    HomeAction.log.error("Failed to load author", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for monitor environment
     */
    public ActionForward monitorLesson(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, ServletException {

	try {
	    HomeAction.log.debug("request monitorLesson");
	    Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	    UserDTO user = getUser();
	    if (user == null) {
		HomeAction.log.error("admin: User missing from session. ");
		return mapping.findForward("error");
	    } else {
		Lesson lesson = lessonId != null ? getLessonService().getLesson(lessonId) : null;
		if (lesson == null) {
		    HomeAction.log.error("monitorLesson: Lesson " + lessonId
			    + " does not exist. Unable to monitor lesson");
		    return mapping.findForward("error");
		}

		if ((lesson.getLessonClass() == null)
			|| (!lesson.getLessonClass().isStaffMember(getRealUser(user)) && !req
				.isUserInRole(Role.GROUP_MANAGER))) {
		    HomeAction.log.error("learner: User " + user.getLogin()
			    + " is not a learner in the requested lesson. Cannot access the lesson.");
		    return displayMessage(mapping, req, "error.authorisation");
		}

		HomeAction.log.debug("user is staff");
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		req.setAttribute("serverUrl", serverUrl);
		req.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
		return mapping.findForward("monitorLesson");
	    }
	} catch (Exception e) {
	    HomeAction.log.error("Failed to load monitor lesson", e);
	    return mapping.findForward("error");
	}
    }

    @SuppressWarnings("unchecked")
    public ActionForward addLesson(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException, UserAccessDeniedException, JSONException,
	    RepositoryCheckedException {
	UserDTO userDTO = getUser();

	// get all user accessible folders and LD descriptions as JSON
	JSONObject learningDesigns = getDeepFolderContents(userDTO.getUserID(), null);
	req.setAttribute("folderContents", learningDesigns.toString());

	Integer organisationID = new Integer(WebUtil.readIntParam(req, "organisationID"));
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

    public ActionForward createLearningDesignThumbnail(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws JDOMException, IOException, TranscoderException {
	Long learningDesignId = WebUtil.readLongParam(req, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String imagePath = getLearningDesignService().createLearningDesignSVG(learningDesignId,
		SVGGenerator.OUTPUT_FORMAT_PNG);

	res.setContentType("image/png");
	OutputStream output = res.getOutputStream();
	FileInputStream input = new FileInputStream(imagePath);
	IOUtils.copy(input, output);
	IOUtils.closeQuietly(input);
	IOUtils.closeQuietly(output);

	return null;
    }

    @SuppressWarnings("unchecked")
    private JSONObject getDeepFolderContents(Integer userID, Integer folderID) throws JSONException, IOException,
	    UserAccessDeniedException, RepositoryCheckedException {
	JSONObject result = new JSONObject();
	Vector<FolderContentDTO> folderContents = null;

	// get use accessible folders in the start
	if (folderID == null) {
	    folderContents = new Vector<FolderContentDTO>(3);
	    MessageService msgService = getWorkspaceManagementService().getMessageService();

	    FolderContentDTO userFolder = getWorkspaceManagementService().getUserWorkspaceFolder(userID);
	    if (userFolder != null) {
		folderContents.add(userFolder);
	    }

	    FolderContentDTO myGroupsFolder = new FolderContentDTO(msgService.getMessage("organisations"),
		    msgService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
		    WorkspaceAction.ORG_FOLDER_ID.longValue(), WorkspaceFolder.READ_ACCESS, null);

	    folderContents.add(myGroupsFolder);

	    FolderContentDTO publicFolder = getWorkspaceManagementService().getPublicWorkspaceFolder(userID);
	    if (publicFolder != null) {
		folderContents.add(publicFolder);
	    }
	    // special behaviour for organisation folders
	} else if (folderID.equals(WorkspaceAction.ORG_FOLDER_ID)) {
	    folderContents = getWorkspaceManagementService().getAccessibleOrganisationWorkspaceFolders(userID);

	    if (folderContents.size() == 1) {
		FolderContentDTO folder = folderContents.firstElement();
		if (folder.getResourceID().equals(WorkspaceAction.ROOT_ORG_FOLDER_ID)) {
		    return getDeepFolderContents(userID, WorkspaceAction.ROOT_ORG_FOLDER_ID);
		}
	    }
	} else {
	    WorkspaceFolder folder = getWorkspaceManagementService().getWorkspaceFolder(folderID);
	    folderContents = getWorkspaceManagementService().getFolderContents(userID, folder,
		    WorkspaceManagementService.AUTHORING);
	}

	// recursively check folders, building a tree
	for (FolderContentDTO folderContent : folderContents) {
	    String contentType = folderContent.getResourceType();
	    if (FolderContentDTO.FOLDER.equals(contentType)) {
		JSONObject subfolder = getDeepFolderContents(userID, folderContent.getResourceID().intValue());
		subfolder.put("name", folderContent.getName());
		result.append("folders", subfolder);
	    } else if (FolderContentDTO.DESIGN.equals(contentType)) {
		JSONObject learningDesign = new JSONObject();
		learningDesign.put("learningDesignId", folderContent.getResourceID());
		learningDesign.put("name", folderContent.getName());
		result.append("learningDesigns", learningDesign);
	    } else {
		if (HomeAction.log.isDebugEnabled()) {
		    HomeAction.log.debug("Unsupported folder content found, named \"" + folderContent.getName() + "\"");
		}
	    }
	}

	return result;
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

	} else {
	    req.getSession().invalidate();

	    // clear system shared session.
	    SessionManager.getSession().invalidate();

	    return mapping.findForward("index");
	}
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private IUserManagementService getService() {
	if (HomeAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    HomeAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return HomeAction.service;
    }

    private ILessonService getLessonService() {
	if (HomeAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    HomeAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return HomeAction.lessonService;
    }

    private ILearningDesignService getLearningDesignService() {
	if (HomeAction.learningDesignService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    HomeAction.learningDesignService = (ILearningDesignService) ctx.getBean("learningDesignService");
	}
	return HomeAction.learningDesignService;
    }

    private IGroupUserDAO getGroupUserDAO() {
	if (HomeAction.groupUserDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    HomeAction.groupUserDAO = (IGroupUserDAO) ctx.getBean("groupUserDAO");
	}
	return HomeAction.groupUserDAO;
    }

    private IWorkspaceManagementService getWorkspaceManagementService() {
	if (HomeAction.workspaceManagementService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    HomeAction.workspaceManagementService = (IWorkspaceManagementService) webContext
		    .getBean("workspaceManagementService");
	}

	return HomeAction.workspaceManagementService;
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return getService().getUserByLogin(dto.getLogin());
    }
}