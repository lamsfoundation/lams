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

package org.lamsfoundation.lams.tool.mdfrum.web.actions;

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
import org.lamsfoundation.lams.tool.mdfrum.dto.MdlForumDTO;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForum;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumConfigItem;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumSession;
import org.lamsfoundation.lams.tool.mdfrum.model.MdlForumUser;
import org.lamsfoundation.lams.tool.mdfrum.service.MdlForumServiceProxy;
import org.lamsfoundation.lams.tool.mdfrum.service.IMdlForumService;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumConstants;
import org.lamsfoundation.lams.tool.mdfrum.util.MdlForumException;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
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
 * @struts.action-forward name="mdlForum" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private static final String TOOL_APP_URL = Configuration
			.get(ConfigurationKeys.SERVER_URL)
			+ "/tool/" + MdlForumConstants.TOOL_SIGNATURE + "/";

	// public static final String RELATIVE_LEARNER_URL = "mod/Forum/view.php?";

	public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
	public static final String MOODLE_VIEW_URL = "mod/forum/view.php";
	public static final String RELATIVE_TEACHER_URL = "mod/forum/view.php?";

	private IMdlForumService mdlForumService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up mdlForumService
		if (mdlForumService == null) {
			mdlForumService = MdlForumServiceProxy.getMdlForumService(this
					.getServlet().getServletContext());
		}

		// Retrieve the session and content.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,
				AttributeNames.PARAM_MODE, false);
		MdlForumSession mdlForumSession = mdlForumService
				.getSessionBySessionId(toolSessionID);
		if (mdlForumSession == null) {
			throw new MdlForumException(
					"Cannot retreive session with toolSessionID: "
							+ toolSessionID);
		}

		MdlForum mdlForum = mdlForumSession.getMdlForum();
		MdlForumUser mdlForumUser = getCurrentUser(toolSessionID);
		
		// check defineLater
		if (mdlForum.isDefineLater()) {
			return mapping.findForward("defineLater");
		}

		MdlForumDTO mdlForumDTO = new MdlForumDTO();
		request.setAttribute("mdlForumDTO", mdlForumDTO);

		// Set the content in use flag.
		if (!mdlForum.isContentInUse()) {
			mdlForum.setContentInUse(new Boolean(true));
			mdlForumService.saveOrUpdateMdlForum(mdlForum);
		}

		// check runOffline
		if (mdlForum.isRunOffline()) {
			return mapping.findForward("runOffline");
		}

		if (mdlForum.getExtToolContentId() != null) {
			try {
				String responseUrl = mdlForumService.getConfigItem(
						MdlForumConfigItem.KEY_EXTERNAL_SERVER_URL)
						.getConfigValue();

				if (mode.equals(ToolAccessMode.TEACHER)) {
					responseUrl += RELATIVE_TEACHER_URL;
				
				} else if (mode.equals(ToolAccessMode.LEARNER)|| mode.equals(ToolAccessMode.AUTHOR))
				{
					responseUrl += RELATIVE_LEARNER_URL;
				}

				String returnUrl = TOOL_APP_URL + "learning.do?"
						+ AttributeNames.PARAM_TOOL_SESSION_ID + "="
						+ toolSessionID.toString() + "&dispatch=finishActivity";

				String encodedMoodleRelativePath = URLEncoder.encode(
						MOODLE_VIEW_URL, "UTF8");

				returnUrl = URLEncoder.encode(returnUrl, "UTF8");
				
				responseUrl += "&id=" + mdlForumSession.getExtSessionId()
						+ "&returnUrl=" + returnUrl + "&dest="
						+ encodedMoodleRelativePath + "&is_learner=1"
						+ "&isFinished=" + mdlForumUser.isFinishedActivity();

				log.debug("Redirecting for mdl Forum learner: " + responseUrl);
				response.sendRedirect(responseUrl);
			} catch (Exception e) {
				log.error("Could not redirect to mdl Forum authoring", e);
			}
		} else {
			throw new MdlForumException("External content id null for learner");
		}
		return null;
	}

	private MdlForumUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		MdlForumUser mdlForumUser = mdlForumService
				.getUserByUserIdAndSessionId(new Long(user.getUserID()
						.intValue()), toolSessionId);

		if (mdlForumUser == null) {
			MdlForumSession mdlForumSession = mdlForumService
					.getSessionBySessionId(toolSessionId);
			mdlForumUser = mdlForumService.createMdlForumUser(user,
					mdlForumSession);
		}

		return mdlForumUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

		MdlForumUser mdlForumUser = getCurrentUser(toolSessionID);

		if (mdlForumUser != null) {
			mdlForumUser.setFinishedActivity(true);
			mdlForumService.saveOrUpdateMdlForumUser(mdlForumUser);
		} else {
			log.error("finishActivity(): couldn't find MdlForumUser with id: "
					+ mdlForumUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		ToolSessionManager sessionMgrService = MdlForumServiceProxy
				.getMdlForumSessionManager(getServlet().getServletContext());

		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,
					mdlForumUser.getUserId());
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new MdlForumException(e);
		} catch (ToolException e) {
			throw new MdlForumException(e);
		} catch (IOException e) {
			throw new MdlForumException(e);
		}

		return null; // TODO need to return proper page.
	}
}
