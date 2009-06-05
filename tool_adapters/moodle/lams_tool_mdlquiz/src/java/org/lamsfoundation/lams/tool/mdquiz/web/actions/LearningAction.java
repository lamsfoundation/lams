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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.mdquiz.web.actions;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdquiz.dto.MdlQuizDTO;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuiz;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizSession;
import org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizUser;
import org.lamsfoundation.lams.tool.mdquiz.service.IMdlQuizService;
import org.lamsfoundation.lams.tool.mdquiz.service.MdlQuizServiceProxy;
import org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizConstants;
import org.lamsfoundation.lams.tool.mdquiz.util.MdlQuizException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="mdlQuiz" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlQuizConstants.TOOL_SIGNATURE + "/";

    public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
    public static final String MOODLE_VIEW_URL = "mod/quiz/view.php";
    public static final String RELATIVE_TEACHER_URL = "mod/quiz/report.php?";

    private IMdlQuizService mdlQuizService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	//LearningForm learningForm = (LearningForm) form;

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up mdlQuizService
	if (mdlQuizService == null) {
	    mdlQuizService = MdlQuizServiceProxy.getMdlQuizService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	MdlQuizSession mdlQuizSession = mdlQuizService.getSessionBySessionId(toolSessionID);
	if (mdlQuizSession == null) {
	    throw new MdlQuizException("Cannot retreive session with toolSessionID: " + toolSessionID);
	}

	MdlQuiz mdlQuiz = mdlQuizSession.getMdlQuiz();
	MdlQuizUser mdlQuizUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (mdlQuiz.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	MdlQuizDTO mdlQuizDTO = new MdlQuizDTO();
	request.setAttribute("mdlQuizDTO", mdlQuizDTO);

	// Set the content in use flag.
	if (!mdlQuiz.isContentInUse()) {
	    mdlQuiz.setContentInUse(new Boolean(true));
	    mdlQuizService.saveOrUpdateMdlQuiz(mdlQuiz);
	}

	// check runOffline
	if (mdlQuiz.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	if (mdlQuiz.getExtToolContentId() != null) {
	    try {
		String responseUrl = mdlQuizService.getExtServerUrl(mdlQuiz.getExtLmsId());

		if (mode.equals(ToolAccessMode.TEACHER)) {
		    responseUrl += RELATIVE_TEACHER_URL;
		} else if (mode.equals(ToolAccessMode.LEARNER) || mode.equals(ToolAccessMode.AUTHOR)) {
		    responseUrl += RELATIVE_LEARNER_URL;
		}

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ toolSessionID.toString() + "&dispatch=finishActivity";

		String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_VIEW_URL, "UTF8");

		returnUrl = URLEncoder.encode(returnUrl, "UTF8");

		responseUrl += "&id=" + mdlQuizSession.getExtSessionId() + "&returnUrl=" + returnUrl + "&dest="
			+ encodedMoodleRelativePath + "&is_learner=1" + "&isFinished="
			+ mdlQuizUser.isFinishedActivity();

		log.debug("Redirecting for mdl quiz learner: " + responseUrl);
		response.sendRedirect(responseUrl);
	    } catch (Exception e) {
		log.error("Could not redirect to mdl quiz authoring", e);
	    }
	} else {
	    throw new MdlQuizException("External content id null for learner");
	}
	return null;
    }

    private MdlQuizUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MdlQuizUser mdlQuizUser = mdlQuizService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (mdlQuizUser == null) {
	    MdlQuizSession mdlQuizSession = mdlQuizService.getSessionBySessionId(toolSessionId);
	    mdlQuizUser = mdlQuizService.createMdlQuizUser(user, mdlQuizSession);
	}

	return mdlQuizUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	MdlQuizUser mdlQuizUser = getCurrentUser(toolSessionID);

	if (mdlQuizUser != null) {
	    mdlQuizUser.setFinishedActivity(true);
	    mdlQuizService.saveOrUpdateMdlQuizUser(mdlQuizUser);
	} else {
	    log.error("finishActivity(): couldn't find MdlQuizUser with id: " + mdlQuizUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = MdlQuizServiceProxy.getMdlQuizSessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, mdlQuizUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new MdlQuizException(e);
	} catch (ToolException e) {
	    throw new MdlQuizException(e);
	} catch (IOException e) {
	    throw new MdlQuizException(e);
	}

	return null; // TODO need to return proper page.
    }
}
