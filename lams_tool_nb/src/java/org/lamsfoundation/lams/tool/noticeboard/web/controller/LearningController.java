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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
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
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    public String unspecified(@ModelAttribute NbLearnerForm NbLearnerForm, HttpServletRequest request,
	    HttpServletResponse response, Model model) {

	return learner5(request, model);
    }

    @RequestMapping("/learner")
    public String learner5(HttpServletRequest request, Model model) {
	String url = request.getRequestURL().toString().replace("learner.do", "learnerInner.do") + "?"
		+ request.getQueryString();
	model.addAttribute("contentUrl", url);
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	model.addAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	return "learner5";
    }

    @RequestMapping("/learnerInner")
    public String learner(@ModelAttribute NbLearnerForm NbLearnerForm, HttpServletRequest request,
	    HttpServletResponse response) {

	NoticeboardContent nbContent = null;
	NoticeboardUser nbUser = null;

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	Long toolSessionID = Long.valueOf(NbLearnerForm.getToolSessionID());

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
	    return "defineLater";
	}

	boolean readOnly = false;
	if (NbLearnerForm.getMode() == null) {
	    NbLearnerForm.setMode(ToolAccessMode.LEARNER.toString());
	}
	ToolAccessMode mode = WebUtil.getToolAccessMode(NbLearnerForm.getMode());
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

	NbLearnerForm.copyValuesIntoForm(nbContent, readOnly, mode.toString());

	NotebookEntry notebookEntry = nbService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		NoticeboardConstants.TOOL_SIGNATURE, userID.intValue());
	if (notebookEntry != null) {
	    request.setAttribute("reflectEntry", notebookEntry.getEntry());
	}
	request.setAttribute("reflectInstructions", nbContent.getReflectInstructions());
	request.setAttribute("reflectOnActivity", nbContent.getReflectOnActivity());
	request.setAttribute("allowComments", nbContent.isAllowComments());
	request.setAttribute("likeAndDislike", nbContent.isCommentsLikeAndDislike());
	request.setAttribute("anonymous", nbContent.isAllowAnonymous());

	Boolean userFinished = (nbUser != null && NoticeboardUser.COMPLETED.equals(nbUser.getUserStatus()));
	request.setAttribute("userFinished", userFinished);

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, nbService.isLastActivity(toolSessionID));

	/*
	 * Checks to see if the runOffline flag is set.
	 * If the particular flag is set, control is forwarded to jsp page
	 * displaying to the user the message according to what flag is set.
	 */
	if (displayMessageToUser(nbContent, errorMap)) {
	    request.setAttribute("errorMap", errorMap);
	    return "message";
	}

	return "learnerContent5";

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
     * <p>
     * This methods checks the defineLater and runOffline flag. If defineLater flag is set, then a message is added to
     * an ActionMessages object saying that the contents have not been defined yet. If the runOffline flag is set, a
     * message is added to ActionMessages saying that the contents are not being run online. This method will return
     * true if any one of the defineLater or runOffline flag is set. Otherwise false will be returned.
     * </p>
     */
    private boolean displayMessageToUser(NoticeboardContent content, MultiValueMap<String, String> errorMap) {
	boolean isDefineLaterSet = isFlagSet(content, NoticeboardConstants.FLAG_DEFINE_LATER);
	errorMap = new LinkedMultiValueMap<>();
	if (isDefineLaterSet) {
	    if (isDefineLaterSet) {
		errorMap.add("GLOBAL", messageService.getMessage("message.defineLaterSet"));
	    }
	    return true;
	} else {
	    return false;
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

	    // Create the notebook entry if reflection is set.
	    NoticeboardContent nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionID);
	    if (nbContent.getReflectOnActivity()) {
		// check for existing notebook entry
		NotebookEntry entry = nbService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			NoticeboardConstants.TOOL_SIGNATURE, userID.intValue());

		if (entry == null) {
		    // create new entry
		    nbService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			    NoticeboardConstants.TOOL_SIGNATURE, userID.intValue(), nbLearnerForm.getReflectionText());
		} else {
		    // update existing entry
		    entry.setEntry(nbLearnerForm.getReflectionText());
		    entry.setLastModified(new Date());
		    nbService.updateEntry(entry);
		}
	    }

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

    /**
     * Indicates that the user has finished viewing the noticeboard, and will be passed onto the Notebook reflection
     * screen.
     */
    @RequestMapping(path = "/reflect", method = RequestMethod.POST)
    public String reflect(@ModelAttribute NbLearnerForm nbLearnerForm, HttpServletRequest request) {

	Long toolSessionID = Long.valueOf(nbLearnerForm.getToolSessionID());
	NoticeboardContent nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionID);
	request.setAttribute("reflectInstructions", nbContent.getReflectInstructions());
	request.setAttribute("title", nbContent.getTitle());
	request.setAttribute("allowComments", nbContent.isAllowComments());
	request.setAttribute("likeAndDislike", nbContent.isCommentsLikeAndDislike());
	request.setAttribute("anonymous", nbContent.isAllowAnonymous());
	request.setAttribute("toolSessionID", toolSessionID);
	// get the existing reflection entry
	NotebookEntry entry = nbService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		NoticeboardConstants.TOOL_SIGNATURE, getUserID(request).intValue());
	if (entry != null) {
	    request.setAttribute("reflectEntry", entry.getEntry());
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, nbService.isLastActivity(toolSessionID));
	return "reflect";
    }

}