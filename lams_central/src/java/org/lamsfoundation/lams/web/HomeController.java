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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.learningdesign.service.LearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LessonDTOComparator;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This is an action where all lams client environments launch. initial configuration of the individual environment
 * setting is done here.
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    private static Logger log = Logger.getLogger(HomeController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private IGroupUserDAO groupUserDAO;
    @Autowired
    private IWorkspaceManagementService workspaceManagementService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ILogEventService logEventService;

    /**
     * request for sysadmin environment
     */
    @RequestMapping("/sysadmin")
    public String sysadmin(HttpServletRequest req) throws IOException, ServletException {

	try {
	    HomeController.log.debug("request sysadmin");
	    int orgId = new Integer(req.getParameter("orgId")).intValue();
	    UserDTO user = getUser();
	    if (user == null) {
		HomeController.log.error("admin: User missing from session. ");
		return "errorContent";
	    } else if (userManagementService.isUserInRole(user.getUserID(), orgId, Role.SYSADMIN)) {
		HomeController.log.debug("user is sysadmin");
		return "sysadmin";
	    } else {
		HomeController.log.error("User " + user.getLogin()
			+ " tried to get sysadmin screen but isn't sysadmin in organisation: " + orgId);
		return displayMessage(req, "error.authorisation");
	    }

	} catch (Exception e) {
	    HomeController.log.error("Failed to load sysadmin", e);
	    return "errorContent";
	}
    }

    /**
     * request for learner environment
     */
    @RequestMapping("/learner")
    public String learner(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
	try {
	    Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	    UserDTO user = getUser();
	    if (user == null) {
		HomeController.log.error("learner: User missing from session. ");
		return "errorContent";
	    }

	    if (!securityService.isLessonLearner(lessonId, user.getUserID(), "access lesson", false)) {
		res.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the lesson");
		return null;
	    }

	    String mode = WebUtil.readStrParam(req, AttributeNames.PARAM_MODE, true);
	    Lesson lesson = lessonId != null ? lessonService.getLesson(lessonId) : null;
	    if (!lesson.isLessonStarted()) {
		return displayMessage(req, "message.lesson.not.started.cannot.participate");
	    }
	    if (!lessonService.checkLessonReleaseConditions(lessonId, user.getUserID())) {
		return displayMessage(req, "message.preceding.lessons.not.finished.cannot.participate");
	    }

	    // check if the lesson is scheduled to be finished to individual users
	    if (lesson.isScheduledToCloseForIndividuals()) {
		GroupUser groupUser = groupUserDAO.getGroupUser(lesson, user.getUserID());
		if ((groupUser != null) && (groupUser.getScheduledLessonEndDate() != null)
			&& groupUser.getScheduledLessonEndDate().before(new Date())) {
		    HomeController.log.error("learner: User " + user.getLogin()
			    + " cannot access the lesson due to lesson end date has passed.");
		    return displayMessage(req, "error.finish.date.passed");
		}
	    }

	    // check lesson's state if its suitable for learner's access
	    if (!lesson.isLessonAccessibleForLearner()) {
		return displayMessage(req, "error.lesson.not.accessible.for.learners");
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
		return "lessonIntro";
	    }

	    if (lesson.getForceLearnerRestart()) {
		// start the lesson from the beginning each time
		lessonService.removeLearnerProgress(lessonId, user.getUserID());
	    }

	    if (mode != null) {
		req.setAttribute(AttributeNames.PARAM_MODE, mode);
	    }

	    // forward to the next (possibly first) activity.
	    String serverURLContextPath = Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH);
	    serverURLContextPath = serverURLContextPath.startsWith("/") ? serverURLContextPath
		    : "/" + serverURLContextPath;
	    serverURLContextPath += serverURLContextPath.endsWith("/") ? "" : "/";
	    res.sendRedirect(serverURLContextPath + "learning/welcome.jsp?lessonID=" + lessonId);
	    return null;

	} catch (Exception e) {
	    HomeController.log.error("Failed to load learner", e);
	    return "errorContent";
	}
    }

    /**
     * request for author environment
     *
     * @throws IOException
     */
    @RequestMapping("/author")
    public String author(HttpServletRequest req, HttpServletResponse res) throws IOException {
	String url = Configuration.get(ConfigurationKeys.SERVER_URL) + "authoring/openAuthoring.do";
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
    @RequestMapping("/monitorLesson")
    public String monitorLesson(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
	Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	UserDTO user = getUser();
	if (user == null) {
	    HomeController.log.error("User missing from session. Can not open Lesson Monitor.");
	    return "errorContent";
	}

	Lesson lesson = lessonId == null ? null : lessonService.getLesson(lessonId);
	if (lesson == null) {
	    HomeController.log.error("Lesson " + lessonId + " does not exist. Can not open Lesson Monitor.");
	    return "errorContent";
	}

	boolean forceRegularMonitor = WebUtil.readBooleanParam(req, "forceRegularMonitor", false);
	// security check will be done there
	String url = Configuration.get(ConfigurationKeys.SERVER_URL)
		+ "monitoring/monitoring/monitorLesson.do?lessonID=" + lessonId + "&forceRegularMonitor="
		+ forceRegularMonitor;
	res.sendRedirect(url);
	return null;
    }

    @RequestMapping("/addLesson")
    @SuppressWarnings("unchecked")
    public String addLesson(HttpServletRequest req, HttpServletResponse res, @RequestParam Integer organisationID)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException {
	UserDTO userDTO = getUser();
	if (!securityService.isGroupMonitor(organisationID, userDTO.getUserID(), "add lesson", false)) {
	    res.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	ObjectNode users = JsonNodeFactory.instance.objectNode();

	// get learners available for newly created lesson
	Vector<User> learners = userManagementService.getUsersFromOrganisationByRole(organisationID, "LEARNER", true);
	for (User user : learners) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("userID", user.getUserId());
	    userJSON.put("firstName", user.getFirstName());
	    userJSON.put("lastName", user.getLastName());
	    userJSON.put("login", user.getLogin());

	    users.withArray("selectedLearners").add(userJSON);
	}

	Vector<User> monitors = userManagementService.getUsersFromOrganisationByRole(organisationID, "MONITOR", true);
	for (User user : monitors) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("userID", user.getUserId());
	    userJSON.put("firstName", user.getFirstName());
	    userJSON.put("lastName", user.getLastName());
	    userJSON.put("login", user.getLogin());

	    // all monitors are added by default
	    users.withArray("selectedMonitors").add(userJSON);
	}
	req.setAttribute("users", users.toString());

	// find lessons which can be set as preceding ones for newly created lesson
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	Set<LessonDTO> availableLessons = new TreeSet<>(new LessonDTOComparator());
	for (Lesson availableLesson : organisation.getLessons()) {
	    Integer availableLessonState = availableLesson.getLessonStateId();
	    if (!Lesson.REMOVED_STATE.equals(availableLessonState)
		    && !Lesson.FINISHED_STATE.equals(availableLessonState)) {
		availableLessons.add(new LessonDTO(availableLesson));
	    }
	}
	req.setAttribute("availablePrecedingLessons", availableLessons);

	// find subgroups which can be set as multiple lessons start
	req.setAttribute("subgroups", organisation.getChildOrganisations());

	return "addLesson";
    }

    /**
     * Gets subfolder contents in Add Lesson screen.
     */
    @ResponseBody
    @RequestMapping("/getFolderContents")
    public void getFolderContents(HttpServletRequest req, HttpServletResponse res)
	    throws UserAccessDeniedException, IOException, RepositoryCheckedException {
	Integer folderID = WebUtil.readIntParam(req, "folderID", true);
	boolean allowInvalidDesigns = WebUtil.readBooleanParam(req, "allowInvalidDesigns", false);
	String folderContentsJSON = workspaceManagementService.getFolderContentsJSON(folderID, getUser().getUserID(),
		allowInvalidDesigns);

	res.setContentType("application/json;charset=UTF-8");
	res.getWriter().print(folderContentsJSON);
    }

    @ResponseBody
    @RequestMapping("/getLearningDesignThumbnail")
    public void getLearningDesignThumbnail(HttpServletRequest req, HttpServletResponse res) throws IOException {
	Long learningDesignId = WebUtil.readLongParam(req, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	String imagePath = LearningDesignService.getLearningDesignSVGPath(learningDesignId);
	File imageFile = new File(imagePath);
	if (!imageFile.canRead()) {
	    res.sendError(HttpServletResponse.SC_NOT_FOUND);
	    return;
	}

	boolean download = WebUtil.readBooleanParam(req, "download", false);
	// should the image be downloaded or a part of page?
	if (download) {
	    String name = learningDesignService.getLearningDesignDTO(learningDesignId, getUser().getLocaleLanguage())
		    .getTitle();
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

    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest req) throws IOException, ServletException {
	UserDTO userDTO = getUser();
	HttpSession session = req.getSession();
	String logoutURL = (String) session.getAttribute("integratedLogoutURL");

	session.invalidate();

	if (userDTO != null) {
	    String message = new StringBuilder("User ").append(userDTO.getLogin()).append(" (")
		    .append(userDTO.getUserID()).append(") logged out manually").toString();
	    logEventService.logEvent(LogEvent.TYPE_LOGOUT, userDTO.getUserID(), userDTO.getUserID(), null, null,
		    message);
	}

	return "redirect:" + (StringUtils.isBlank(logoutURL) ? "/index.do" : logoutURL);
    }

    private String displayMessage(HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return "msgContent";
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser(UserDTO dto) {
	return userManagementService.getUserByLogin(dto.getLogin());
    }
}