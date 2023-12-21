/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.scribe.web.controller;

import java.io.IOException;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeSessionDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeUserDTO;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.util.ScribeException;
import org.lamsfoundation.lams.tool.scribe.util.ScribeUtils;
import org.lamsfoundation.lams.tool.scribe.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    private static final boolean MODE_OPTIONAL = false;

    @Autowired
    @Qualifier("lascrbScribeService")
    private IScribeService scribeService;

    @Autowired
    @Qualifier("lascrbMessageService")
    private MessageService messageService;

    @RequestMapping("")
    public String unspecified(@ModelAttribute("learningForm") LearningForm learningform, HttpServletRequest request)
	    throws Exception {
	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningController.MODE_OPTIONAL);
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	scribeService.createReportEntry(toolSessionID);

	// Retrieve the session and content.
	ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	if (scribeSession == null) {
	    throw new ScribeException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}
	Scribe scribe = scribeSession.getScribe();
	
	learningform.setToolSessionID(scribeSession.getSessionId());
	request.setAttribute("MODE", mode.toString());

	// check defineLater
	if (scribe.isDefineLater()) {
	    return "/pages/learning/defineLater";
	}

	// Ensure that the content in use flag is set.
	if (!scribe.isContentInUse()) {
	    scribe.setContentInUse(true);
	    scribeService.saveOrUpdateScribe(scribe);
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, scribeService.isLastActivity(toolSessionID));

	// Retrieve the current user
	ScribeUser scribeUser = getCurrentUser(toolSessionID);

	// check whether scribe has been appointed
	while (scribeSession.getAppointedScribe() == null) {
	    // check autoSelectScribe
	    if (scribe.isAutoSelectScribe() == false) {
		// learner needs to wait until a scribe has been appointed by
		// teacher.
		return "pages/learning/waitForScribe";

	    } else {
		// appoint the currentUser as the scribe
		scribeSession.setAppointedScribe(scribeUser);
		scribeService.saveOrUpdateScribeSession(scribeSession);
	    }
	}

	// setup dto's forms and attributes.
	setupDTOs(request, scribeSession, scribeUser);

	// check force complete
	if (scribeSession.isForceComplete()) {
	    // go to report page
	    if (scribeSession.getScribe().isShowAggregatedReports()) {
		setupOtherGroupReportDTO(request, scribeSession, scribeUser);
	    }
	    return "pages/learning/report";
	}

	// check if user has started activity
	if (!scribeUser.isStartedActivity()) {
	    if (scribeSession.getAppointedScribe().getUid() == scribeUser.getUid()) {
		request.setAttribute("role", "scribe");
	    } else {
		request.setAttribute("role", "learner");
	    }
	    return "pages/learning/instructions";
	}

	// check if current user is the scribe.
	if (scribeSession.getAppointedScribe().getUid() == scribeUser.getUid()) {
	    return "pages/learning/scribe";
	} else {
	    return "pages/learning/learning";
	}

    }

    private ScribeUser getCurrentUser(Long toolSessionID) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionID
	ScribeUser scribeUser = scribeService.getUserByUserIdAndSessionId(user.getUserID().longValue(),
		toolSessionID);

	if (scribeUser == null) {
	    ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	    scribeUser = scribeService.createScribeUser(user, scribeSession);
	}

	return scribeUser;
    }

    @RequestMapping("/startActivity")
    public String startActivity(@ModelAttribute("learningForm") LearningForm learningform, HttpServletRequest request) {

	Long toolSessionID = learningform.getToolSessionID();

	ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	ScribeUser scribeUser = getCurrentUser(toolSessionID);

	// setup dto's, forms and attributes.
	learningform.setToolSessionID(scribeSession.getSessionId());
	request.setAttribute("MODE", learningform.getMode());
	setupDTOs(request, scribeSession, scribeUser);

	// update scribe user and go to instructions page
	scribeUser.setStartedActivity(true);
	scribeService.saveOrUpdateScribeUser(scribeUser);

	// check if current user is the scribe.
	if (scribeSession.getAppointedScribe().getUid() == scribeUser.getUid()) {
	    return "pages/learning/scribe";
	}

	return "pages/learning/learning";
    }

    @RequestMapping("/finishActivity")
    public String finishActivity(@ModelAttribute("learningForm") LearningForm learningform, HttpServletRequest request,
	    HttpServletResponse response) {

	// set the finished flag
	ScribeUser scribeUser = scribeService.getUserByUID(learningform.getScribeUserUID());
	if (scribeUser != null) {
	    scribeUser.setFinishedActivity(true);
	    scribeService.saveOrUpdateScribeUser(scribeUser);
	} else {
	    LearningController.log
		    .error("finishActivity(): couldn't find ScribeUser with uid: " + learningform.getScribeUserUID());
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = user.getUserID().longValue();
	Long toolSessionID = scribeUser.getScribeSession().getSessionId();

	String nextActivityUrl;
	try {
	    nextActivityUrl = scribeService.leaveToolSession(toolSessionID, userID);
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new ScribeException(e);
	} catch (ToolException e) {
	    throw new ScribeException(e);
	} catch (IOException e) {
	    throw new ScribeException(e);
	}

	return null; // TODO need to return proper page.
    }

    @RequestMapping("/openNotebook")
    public String openNotebook(@ModelAttribute("learningForm") LearningForm learningform, HttpServletRequest request,
	    HttpServletResponse response) {

	// set the finished flag
	ScribeUser scribeUser = scribeService.getUserByUID(learningform.getScribeUserUID());
	ScribeDTO scribeDTO = new ScribeDTO(scribeUser.getScribeSession().getScribe());
	request.setAttribute("scribeDTO", scribeDTO);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, scribeUser.getScribeSession().getSessionId());

	boolean isLastActivity = scribeService.isLastActivity(scribeUser.getScribeSession().getSessionId());
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, isLastActivity);
	return "pages/learning/notebook";
    }

    @RequestMapping(value = "/submitReflection", method = RequestMethod.POST)
    public String submitReflection(@ModelAttribute("learningForm") LearningForm learningform,
	    HttpServletRequest request, HttpServletResponse response) {

	// save the reflection entry and call the notebook.
	ScribeUser scribeUser = scribeService.getUserByUID(learningform.getScribeUserUID());

	scribeService.createNotebookEntry(scribeUser.getScribeSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId().intValue(),
		learningform.getEntryText());

	return finishActivity(learningform, request, response);
    }

    @RequestMapping("/forceCompleteActivity")
    public String forceCompleteActivity(@ModelAttribute("learningForm") LearningForm learningform,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	ScribeUser scribeUser = scribeService.getUserByUID(learningform.getScribeUserUID());
	ScribeSession session = scribeUser.getScribeSession();

	if (session.getAppointedScribe().getUid() == scribeUser.getUid()) {
	    session.setForceComplete(true);
	} else {
	    // TODO need to implement this.
	    LearningController.log
		    .error("ScribeUserUID: " + scribeUser.getUid() + " is not allowed to forceComplete this session");
	}

	request.setAttribute("MODE", learningform.getMode());
	setupDTOs(request, session, scribeUser);
	scribeService.saveOrUpdateScribeUser(scribeUser);

	if (session.getScribe().isShowAggregatedReports()) {
	    setupOtherGroupReportDTO(request, session, scribeUser);
	}

	boolean isLastActivity = scribeService.isLastActivity(session.getSessionId());
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, isLastActivity);
	return "pages/learning/report";
    }

    // Private methods.

    /**
     * Set up all the DTO relating to this session. Doesn't set up the DTO containing the reports of the other groups.
     */
    private void setupDTOs(HttpServletRequest request, ScribeSession scribeSession, ScribeUser scribeUser) {

	ScribeDTO scribeDTO = new ScribeDTO(scribeSession.getScribe());
	request.setAttribute("scribeDTO", scribeDTO);

	ScribeSessionDTO sessionDTO = new ScribeSessionDTO(scribeSession);
	request.setAttribute("scribeSessionDTO", sessionDTO);

	ScribeUserDTO scribeUserDTO = new ScribeUserDTO(scribeUser);
	if (scribeUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = scribeService.getEntry(scribeSession.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE,
		    scribeUser.getUserId().intValue());
	    if (notebookEntry != null) {
		scribeUserDTO.notebookEntry = notebookEntry.getEntry();
	    }
	}
	request.setAttribute("scribeUserDTO", scribeUserDTO);
    }

    /**
     * Create a map of the reports (in ScribeSessionDTO format) for all the other groups/sessions, where the key is the
     * group/session name. The code ensures that the session name is unique, adding the session id if necessary. It will
     * only include the finalized reports.
     */
    private void setupOtherGroupReportDTO(HttpServletRequest request, ScribeSession scribeSession,
	    ScribeUser scribeUser) {
	TreeMap<String, ScribeSessionDTO> otherScribeSessions = ScribeUtils.getReportDTOs(scribeSession);
	if (otherScribeSessions.size() > 0) {
	    request.setAttribute("otherScribeSessions", otherScribeSessions.values());
	}
    }

}
