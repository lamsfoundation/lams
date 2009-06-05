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

package org.lamsfoundation.lams.tool.mdglos.web.actions;

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
import org.lamsfoundation.lams.tool.mdglos.dto.MdlGlossaryDTO;
import org.lamsfoundation.lams.tool.mdglos.model.MdlGlossary;
import org.lamsfoundation.lams.tool.mdglos.model.MdlGlossarySession;
import org.lamsfoundation.lams.tool.mdglos.model.MdlGlossaryUser;
import org.lamsfoundation.lams.tool.mdglos.service.IMdlGlossaryService;
import org.lamsfoundation.lams.tool.mdglos.service.MdlGlossaryServiceProxy;
import org.lamsfoundation.lams.tool.mdglos.util.MdlGlossaryConstants;
import org.lamsfoundation.lams.tool.mdglos.util.MdlGlossaryException;
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
 * @struts.action-forward name="mdlGlossary" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlGlossaryConstants.TOOL_SIGNATURE + "/";

    public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
    public static final String MOODLE_VIEW_URL = "mod/glossary/view.php";
    public static final String RELATIVE_TEACHER_URL = "mod/glossary/view.php?mode=author&";

    private IMdlGlossaryService mdlGlossaryService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up mdlGlossaryService
	if (mdlGlossaryService == null) {
	    mdlGlossaryService = MdlGlossaryServiceProxy.getMdlGlossaryService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	MdlGlossarySession mdlGlossarySession = mdlGlossaryService.getSessionBySessionId(toolSessionID);
	if (mdlGlossarySession == null) {
	    throw new MdlGlossaryException("Cannot retreive session with toolSessionID: " + toolSessionID);
	}

	MdlGlossary mdlGlossary = mdlGlossarySession.getMdlGlossary();
	MdlGlossaryUser mdlGlossaryUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (mdlGlossary.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	MdlGlossaryDTO mdlGlossaryDTO = new MdlGlossaryDTO();
	request.setAttribute("mdlGlossaryDTO", mdlGlossaryDTO);

	// Set the content in use flag.
	if (!mdlGlossary.isContentInUse()) {
	    mdlGlossary.setContentInUse(new Boolean(true));
	    mdlGlossaryService.saveOrUpdateMdlGlossary(mdlGlossary);
	}

	// check runOffline
	if (mdlGlossary.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	if (mdlGlossary.getExtToolContentId() != null) {
	    try {
		String responseUrl = mdlGlossaryService.getExtServerUrl(mdlGlossary.getExtLmsId());

		if (mode.equals(ToolAccessMode.TEACHER)) {
		    responseUrl += RELATIVE_TEACHER_URL;
		} else if (mode.equals(ToolAccessMode.LEARNER) || mode.equals(ToolAccessMode.AUTHOR)) {
		    responseUrl += RELATIVE_LEARNER_URL;
		}

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ toolSessionID.toString() + "&dispatch=finishActivity";

		String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_VIEW_URL, "UTF8");

		returnUrl = URLEncoder.encode(returnUrl, "UTF8");

		responseUrl += "&id=" + mdlGlossarySession.getExtSessionId() + "&returnUrl=" + returnUrl + "&dest="
			+ encodedMoodleRelativePath + "&is_learner=1" + "&isFinished="
			+ mdlGlossaryUser.isFinishedActivity();

		log.debug("Redirecting for mdl glossary learner: " + responseUrl);
		response.sendRedirect(responseUrl);
	    } catch (Exception e) {
		log.error("Could not redirect to mdl glossary authoring", e);
	    }
	} else {
	    throw new MdlGlossaryException("External content id null for learner");
	}
	return null;
    }

    private MdlGlossaryUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MdlGlossaryUser mdlGlossaryUser = mdlGlossaryService.getUserByUserIdAndSessionId(new Long(user.getUserID()
		.intValue()), toolSessionId);

	if (mdlGlossaryUser == null) {
	    MdlGlossarySession mdlGlossarySession = mdlGlossaryService.getSessionBySessionId(toolSessionId);
	    mdlGlossaryUser = mdlGlossaryService.createMdlGlossaryUser(user, mdlGlossarySession);
	}

	return mdlGlossaryUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	MdlGlossaryUser mdlGlossaryUser = getCurrentUser(toolSessionID);

	if (mdlGlossaryUser != null) {
	    mdlGlossaryUser.setFinishedActivity(true);
	    mdlGlossaryService.saveOrUpdateMdlGlossaryUser(mdlGlossaryUser);
	} else {
	    log.error("finishActivity(): couldn't find MdlGlossaryUser with id: " + mdlGlossaryUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = MdlGlossaryServiceProxy.getMdlGlossarySessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, mdlGlossaryUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new MdlGlossaryException(e);
	} catch (ToolException e) {
	    throw new MdlGlossaryException(e);
	} catch (IOException e) {
	    throw new MdlGlossaryException(e);
	}

	return null; // TODO need to return proper page.
    }
}
