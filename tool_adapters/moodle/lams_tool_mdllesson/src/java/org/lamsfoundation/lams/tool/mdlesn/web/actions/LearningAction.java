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

package org.lamsfoundation.lams.tool.mdlesn.web.actions;

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
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonUser;
import org.lamsfoundation.lams.tool.mdlesn.dto.MdlLessonDTO;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLesson;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonConfigItem;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonSession;
import org.lamsfoundation.lams.tool.mdlesn.service.IMdlLessonService;
import org.lamsfoundation.lams.tool.mdlesn.service.MdlLessonServiceProxy;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonConstants;
import org.lamsfoundation.lams.tool.mdlesn.util.MdlLessonException;
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
 * @struts.action-forward name="mdlLesson" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlLessonConstants.TOOL_SIGNATURE + "/";

    public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
    public static final String MOODLE_VIEW_URL = "mod/lesson/view.php";
    public static final String RELATIVE_TEACHER_URL = "mod/lesson/report.php?"; 

    private IMdlLessonService mdlLessonService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up mdlLessonService
	if (mdlLessonService == null) {
	    mdlLessonService = MdlLessonServiceProxy.getMdlLessonService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, false);
	MdlLessonSession mdlLessonSession = mdlLessonService.getSessionBySessionId(toolSessionID);
	if (mdlLessonSession == null) {
	    throw new MdlLessonException("Cannot retreive session with toolSessionID: " + toolSessionID);
	}

	MdlLesson mdlLesson = mdlLessonSession.getMdlLesson();
	MdlLessonUser mdlLessonUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (mdlLesson.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	MdlLessonDTO mdlLessonDTO = new MdlLessonDTO();
	request.setAttribute("mdlLessonDTO", mdlLessonDTO);

	// Set the content in use flag.
	if (!mdlLesson.isContentInUse()) {
	    mdlLesson.setContentInUse(new Boolean(true));
	    mdlLessonService.saveOrUpdateMdlLesson(mdlLesson);
	}

	// check runOffline
	if (mdlLesson.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	if (mdlLesson.getExtToolContentId() != null) {
		 try {
				String responseUrl = mdlLessonService.getConfigItem(MdlLessonConfigItem.KEY_EXTERNAL_SERVER_URL)
					.getConfigValue();
				
				if(mode.equals(ToolAccessMode.TEACHER))
				{
					responseUrl += RELATIVE_TEACHER_URL;
				}
				else if (mode.equals(ToolAccessMode.LEARNER) || mode.equals(ToolAccessMode.AUTHOR))
				{
					responseUrl += RELATIVE_LEARNER_URL;
				}

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ toolSessionID.toString() + "&dispatch=finishActivity";
		
		String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_VIEW_URL, "UTF8");

		returnUrl = URLEncoder.encode(returnUrl, "UTF8");
		

		responseUrl += "&id=" + mdlLessonSession.getExtSessionId() + "&returnUrl=" + returnUrl
			+ "&dest=" + encodedMoodleRelativePath + "&is_learner=1" + "&isFinished=" + mdlLessonUser.isFinishedActivity();
		
		
		log.debug("Redirecting for mdl lesson learner: " + responseUrl);
		response.sendRedirect(responseUrl);
	    } catch (Exception e) {
		log.error("Could not redirect to mdl lesson authoring", e);
	    }
	} else {
	    throw new MdlLessonException("External content id null for learner");
	}
	return null;
    }

    private MdlLessonUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MdlLessonUser mdlLessonUser = mdlLessonService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (mdlLessonUser == null) {
	    MdlLessonSession mdlLessonSession = mdlLessonService.getSessionBySessionId(toolSessionId);
	    mdlLessonUser = mdlLessonService.createMdlLessonUser(user, mdlLessonSession);
	}

	return mdlLessonUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	MdlLessonUser mdlLessonUser = getCurrentUser(toolSessionID);

	if (mdlLessonUser != null) {
	    mdlLessonUser.setFinishedActivity(true);
	    mdlLessonService.saveOrUpdateMdlLessonUser(mdlLessonUser);
	} else {
	    log.error("finishActivity(): couldn't find MdlLessonUser with id: " + mdlLessonUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = MdlLessonServiceProxy.getMdlLessonSessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, mdlLessonUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new MdlLessonException(e);
	} catch (ToolException e) {
	    throw new MdlLessonException(e);
	} catch (IOException e) {
	    throw new MdlLessonException(e);
	}

	return null; // TODO need to return proper page.
    }
}
