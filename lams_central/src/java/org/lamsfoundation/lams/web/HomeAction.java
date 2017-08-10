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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.learningdesign.service.LearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LessonDTOComparator;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This is an action where all lams client environments launch. initial configuration of the individual environment
 * setting is done here.
 */
public class HomeAction extends DispatchAction {

    private static Logger log = Logger.getLogger(HomeAction.class);

    private static IUserManagementService userManagementService;
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
	    } else if (getUserManagementService().isUserInRole(user.getUserID(), orgId, Role.SYSADMIN)) {
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

		// check if we need to create learning design SVG
		if (lesson.isDisplayDesignImage()) {
		    Long learningDesignId = lesson.getLearningDesign().getLearningDesignId();
		    req.setAttribute(AttributeNames.PARAM_LEARNINGDESIGN_ID, learningDesignId);
		}
		return mapping.findForward("lessonIntro");
	    }

	    if (mode != null) {
		req.setAttribute(AttributeNames.PARAM_MODE, mode);
	    }

	    // forward to the next (possibly first) activity.
	    String serverURLContextPath = Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH);
	    serverURLContextPath = serverURLContextPath.startsWith("/") ? serverURLContextPath
		    : "/" + serverURLContextPath;
	    serverURLContextPath += serverURLContextPath.endsWith("/") ? "" : "/";
	    getServlet().getServletContext().getContext(serverURLContextPath + "learning")
		    .getRequestDispatcher("/content.do?lessonID=" + lessonId).forward(req, res);
	    return null;

	} catch (Exception e) {
	    HomeAction.log.error("Failed to load learner", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * request for author environment
     *
     * @throws IOException
     */
    public ActionForward author(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException {
	String url = Configuration.get(ConfigurationKeys.SERVER_URL) + "authoring/author.do?method=openAuthoring";
	Long learningDesignID = WebUtil.readLongParam(req, "learningDesignID", true);

	if (learningDesignID != null) {
	    url += "&learningDesignID=" + learningDesignID;
	}

	res.sendRedirect(url);
	return null;
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
	    HttpServletResponse res) throws IOException, UserAccessDeniedException, RepositoryCheckedException {
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
	ObjectNode users = JsonNodeFactory.instance.objectNode();

	// get learners available for newly created lesson
	Vector<User> learners = getUserManagementService().getUsersFromOrganisationByRole(organisationID, "LEARNER",
		true);
	for (User user : learners) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("userID", user.getUserId());
	    userJSON.put("firstName", user.getFirstName());
	    userJSON.put("lastName", user.getLastName());
	    userJSON.put("login", user.getLogin());

	    users.withArray("selectedLearners").add(userJSON);
	}

	Vector<User> monitors = getUserManagementService().getUsersFromOrganisationByRole(organisationID, "MONITOR",
		true);
	for (User user : monitors) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("userID", user.getUserId());
	    userJSON.put("firstName", user.getFirstName());
	    userJSON.put("lastName", user.getLastName());
	    userJSON.put("login", user.getLogin());

	    if (userDTO.getUserID().equals(user.getUserId())) {
		// creator is always selected
		users.withArray("selectedMonitors").add(userJSON);
	    } else {
		users.withArray("unselectedMonitors").add(userJSON);
	    }
	}

	req.setAttribute("users", users.toString());

	// find lessons which can be set as preceding ones for newly created lesson
	Organisation organisation = (Organisation) getUserManagementService().findById(Organisation.class,
		organisationID);
	Set<LessonDTO> availableLessons = new TreeSet<>(new LessonDTOComparator());
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
	    HttpServletResponse res) throws UserAccessDeniedException, IOException, RepositoryCheckedException {
	Integer folderID = WebUtil.readIntParam(req, "folderID", true);
	boolean allowInvalidDesigns = WebUtil.readBooleanParam(req, "allowInvalidDesigns", false);
	String folderContentsJSON = getWorkspaceManagementService().getFolderContentsJSON(folderID,
		getUser().getUserID(), allowInvalidDesigns);

	res.setContentType("application/json;charset=UTF-8");
	res.getWriter().print(folderContentsJSON);
	return null;
    }

    public ActionForward getLearningDesignThumbnail(ActionMapping mapping, ActionForm form, HttpServletRequest req,
	    HttpServletResponse res) throws IOException {
	Long learningDesignId = WebUtil.readLongParam(req, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String imagePath = LearningDesignService.getLearningDesignSVGPath(learningDesignId);
	File imageFile = new File(imagePath);
	if (!imageFile.canRead()) {
	    res.sendError(HttpServletResponse.SC_NOT_FOUND);
	    return null;
	}

	boolean download = WebUtil.readBooleanParam(req, "download", false);
	// should the image be downloaded or a part of page?
	if (download) {
	    String name = getLearningDesignService()
		    .getLearningDesignDTO(learningDesignId, getUser().getLocaleLanguage()).getTitle();
	    name += "." + "svg";
	    name = FileUtil.encodeFilenameForDownload(req, name);
	    res.setContentType("application/x-download");
	    res.setHeader("Content-Disposition", "attachment;filename=" + name);
	} else {
	    res.setContentType("image/svg+xml");
	}

	FileInputStream input = new FileInputStream(imagePath);
	OutputStream output = res.getOutputStream();
	IOUtils.copy(input, output);
	IOUtils.closeQuietly(input);
	IOUtils.closeQuietly(output);

	return null;
    }

    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	req.getSession().invalidate();
	return mapping.findForward("index");
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private IUserManagementService getUserManagementService() {
	if (HomeAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    HomeAction.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return HomeAction.userManagementService;
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
	return getUserManagementService().getUserByLogin(dto.getLogin());
    }
}