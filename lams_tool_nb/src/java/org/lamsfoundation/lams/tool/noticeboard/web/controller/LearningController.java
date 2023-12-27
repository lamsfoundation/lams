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

package org.lamsfoundation.lams.tool.noticeboard.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.web.form.NbLearnerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Creation Date: 27-06-05
 *
 * The learner url can be of three modes learner, teacher or author. Depending on what mode was set, it will trigger the
 * corresponding action. If the mode parameter is missing or a key is not found in the keymap, it will call the
 * unspecified method which defaults to the learner action.
 *
 * <p>
 * The difference between author mode (which is basically the preview) is that if the defineLater flag is set, it will
 * not be able to see the noticeboard screen
 * </p>
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    static Logger logger = Logger.getLogger(LearningController.class.getName());

    @Autowired
    private INoticeboardService nbService;

    @Autowired
    @Qualifier("nbMessageService")
    private MessageService messageService;

    private UserDTO getUserDTO(HttpServletRequest request) {
	// set up the user details
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (user == null) {
	    String error = messageService.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "User");
	    logger.error(error);
	    throw new NbApplicationException(error);
	}
	return user;
    }

    /** Get the user id from the shared session */
    public Long getUserID(HttpServletRequest request) {
	UserDTO user = getUserDTO(request);
	return new Long(user.getUserID().longValue());
    }

    /** Get the user id from the url - needed for the monitoring mode */
    public Long getUserIDFromURLCall(HttpServletRequest request) {
	return WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
    }

    @RequestMapping("/learner")
    public String learner(@ModelAttribute NbLearnerForm nbLearnerForm, HttpServletRequest request,
	    HttpServletResponse response) {

	NoticeboardContent nbContent = null;
	NoticeboardUser nbUser = null;

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	Long toolSessionID = Long.valueOf(nbLearnerForm.getToolSessionID());

	if (toolSessionID == null) {
	    String error = "Unable to continue. The parameters tool session id is missing";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionID);
	// nbSession = nbService.retrieveNoticeboardSession(toolSessionId);

	if (nbContent == null) {
	    String error = "An Internal error has occurred. Please exit and retry this sequence";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	if (isFlagSet(nbContent, NoticeboardConstants.FLAG_DEFINE_LATER)) {
	    request.setAttribute(NoticeboardConstants.TOOL_SESSION_ID, toolSessionID);
	    return "defineLater";
	}

	boolean readOnly = false;
	if (nbLearnerForm.getMode() == null) {
	    nbLearnerForm.setMode(ToolAccessMode.LEARNER.toString());
	}
	ToolAccessMode mode = WebUtil.getToolAccessMode(nbLearnerForm.getMode());
	Long userID = null;
	if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR) {
	    userID = getUserID(request);
	    nbUser = nbService.retrieveNbUserBySession(userID, toolSessionID);

	    if (!nbContent.isContentInUse()) {
		/* Set the ContentInUse flag to true, and defineLater flag to false */
		nbContent.setContentInUse(true);
		nbService.saveNoticeboard(nbContent);
	    }

	    if (nbUser == null) {
		//create a new user with this session id
		nbUser = new NoticeboardUser(userID);
		UserDTO user = getUserDTO(request);
		nbUser.setUsername(user.getLogin());
		nbUser.setFullname(user.getFirstName() + " " + user.getLastName());
		nbService.addUser(toolSessionID, nbUser);
	    }
	} else {
	    // user will not exist if force completed.
	    userID = getUserIDFromURLCall(request);
	    nbUser = nbService.retrieveNbUserBySession(userID, toolSessionID);
	    readOnly = true;
	}

	nbLearnerForm.copyValuesIntoForm(nbContent, readOnly, mode.toString());

	request.setAttribute("allowComments", nbContent.isAllowComments());
	request.setAttribute("likeAndDislike", nbContent.isCommentsLikeAndDislike());
	request.setAttribute("anonymous", nbContent.isAllowAnonymous());
	Boolean userFinished = (nbUser != null && NoticeboardUser.COMPLETED.equals(nbUser.getUserStatus()));
	request.setAttribute("userFinished", userFinished);
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, nbService.isLastActivity(toolSessionID));

	return "learnerContent";
    }

    @RequestMapping("/teacher")
    public String teacher(@ModelAttribute NbLearnerForm NbLearnerForm, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	NbLearnerForm.setMode("teacher");
	return learner(NbLearnerForm, request, response);
    }

    @RequestMapping("/author")
    public String author(@ModelAttribute NbLearnerForm NbLearnerForm, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {
	NbLearnerForm.setMode("author");
	return learner(NbLearnerForm, request, response);
    }

    /**
     * <p>
     * Performs a check on the flag indicated by <code>flag</code> In this noticeboard tool, there are 3 possible
     * flags:
     * <li>defineLater</li>
     * <li>contentInUse</li>
     * <br>
     * Returns true if the flag is set, false otherwise
     */
    private boolean isFlagSet(NoticeboardContent content, int flag) throws NbApplicationException {
	switch (flag) {
	    case NoticeboardConstants.FLAG_DEFINE_LATER:
		return (content.isDefineLater());
	    //  break;
	    case NoticeboardConstants.FLAG_CONTENT_IN_USE:
		return (content.isContentInUse());
	    //	break;
	    default:
		throw new NbApplicationException("Invalid flag");
	}
    }

    /**
     * Indicates that the user has finished viewing the noticeboard. The session is set to complete and leaveToolSession
     * is called.
     */
    @RequestMapping("/finish")
    public String finish(@ModelAttribute NbLearnerForm nbLearnerForm, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	Long userID = getUserID(request);

	Long toolSessionID = Long.valueOf(nbLearnerForm.getToolSessionID());
	if (toolSessionID == null) {
	    String error = "Unable to continue. The parameters tool session id is missing";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	ToolSessionManager sessionMgrService = (ToolSessionManager) nbService;

	ToolAccessMode mode = WebUtil.getToolAccessMode(nbLearnerForm.getMode());
	if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR) {
	    NoticeboardSession nbSession = nbService.retrieveNoticeboardSession(toolSessionID);
	    NoticeboardUser nbUser = nbService.retrieveNbUserBySession(userID, toolSessionID);

	    nbUser.setUserStatus(NoticeboardUser.COMPLETED);
	    nbService.updateNoticeboardSession(nbSession);
	    nbService.updateNoticeboardUser(nbUser);

	    String nextActivityUrl;
	    try {
		nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, getUserID(request));
	    } catch (DataMissingException e) {
		logger.error(e);
		throw new ServletException(e);
	    } catch (ToolException e) {
		logger.error(e);
		throw new ServletException(e);
	    }

	    response.sendRedirect(nextActivityUrl);
	    return null;
	}
	request.setAttribute(NoticeboardConstants.READ_ONLY_MODE, "true");

	return "learnerContent";
    }

}