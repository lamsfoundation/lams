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

package org.lamsfoundation.lams.tool.mdchce.web.actions;

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
import org.lamsfoundation.lams.tool.mdchce.dto.MdlChoiceDTO;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoice;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceSession;
import org.lamsfoundation.lams.tool.mdchce.model.MdlChoiceUser;
import org.lamsfoundation.lams.tool.mdchce.service.IMdlChoiceService;
import org.lamsfoundation.lams.tool.mdchce.service.MdlChoiceServiceProxy;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceConstants;
import org.lamsfoundation.lams.tool.mdchce.util.MdlChoiceException;
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
 * @struts.action-forward name="mdlChoice" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlChoiceConstants.TOOL_SIGNATURE + "/";

    public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
    public static final String MOODLE_VIEW_URL = "mod/choice/view.php";
    public static final String RELATIVE_TEACHER_URL = "mod/choice/report.php?";

    private IMdlChoiceService mdlChoiceService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up mdlChoiceService
	if (mdlChoiceService == null) {
	    mdlChoiceService = MdlChoiceServiceProxy.getMdlChoiceService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	MdlChoiceSession mdlChoiceSession = mdlChoiceService.getSessionBySessionId(toolSessionID);
	if (mdlChoiceSession == null) {
	    throw new MdlChoiceException("Cannot retreive session with toolSessionID: " + toolSessionID);
	}

	MdlChoice mdlChoice = mdlChoiceSession.getMdlChoice();
	MdlChoiceUser mdlChoiceUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (mdlChoice.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	MdlChoiceDTO mdlChoiceDTO = new MdlChoiceDTO();
	request.setAttribute("mdlChoiceDTO", mdlChoiceDTO);

	// Set the content in use flag.
	if (!mdlChoice.isContentInUse()) {
	    mdlChoice.setContentInUse(new Boolean(true));
	    mdlChoiceService.saveOrUpdateMdlChoice(mdlChoice);
	}

	// check runOffline
	if (mdlChoice.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	if (mdlChoice.getExtToolContentId() != null) {
	    try {
		String responseUrl = mdlChoiceService.getExtServerUrl(mdlChoice.getExtLmsId());

		if (mode.equals(ToolAccessMode.TEACHER)) {
		    responseUrl += RELATIVE_TEACHER_URL;
		} else if (mode.equals(ToolAccessMode.LEARNER) || mode.equals(ToolAccessMode.AUTHOR)) {
		    responseUrl += RELATIVE_LEARNER_URL;
		}

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ toolSessionID.toString() + "&dispatch=finishActivity";

		String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_VIEW_URL, "UTF8");

		returnUrl = URLEncoder.encode(returnUrl, "UTF8");

		responseUrl += "&id=" + mdlChoiceSession.getExtSessionId() + "&returnUrl=" + returnUrl + "&dest="
			+ encodedMoodleRelativePath + "&is_learner=1" + "&isFinished="
			+ mdlChoiceUser.isFinishedActivity();

		log.debug("Redirecting for mdl choice learner: " + responseUrl);
		response.sendRedirect(responseUrl);
	    } catch (Exception e) {
		log.error("Could not redirect to mdl choice authoring", e);
	    }
	} else {
	    throw new MdlChoiceException("External content id null for learner");
	}
	return null;
    }

    private MdlChoiceUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MdlChoiceUser mdlChoiceUser = mdlChoiceService.getUserByUserIdAndSessionId(
		new Long(user.getUserID().intValue()), toolSessionId);

	if (mdlChoiceUser == null) {
	    MdlChoiceSession mdlChoiceSession = mdlChoiceService.getSessionBySessionId(toolSessionId);
	    mdlChoiceUser = mdlChoiceService.createMdlChoiceUser(user, mdlChoiceSession);
	}

	return mdlChoiceUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	MdlChoiceUser mdlChoiceUser = getCurrentUser(toolSessionID);

	if (mdlChoiceUser != null) {
	    mdlChoiceUser.setFinishedActivity(true);
	    mdlChoiceService.saveOrUpdateMdlChoiceUser(mdlChoiceUser);
	} else {
	    log.error("finishActivity(): couldn't find MdlChoiceUser with id: " + mdlChoiceUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = MdlChoiceServiceProxy.getMdlChoiceSessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, mdlChoiceUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new MdlChoiceException(e);
	} catch (ToolException e) {
	    throw new MdlChoiceException(e);
	} catch (IOException e) {
	    throw new MdlChoiceException(e);
	}

	return null; // TODO need to return proper page.
    }
}
