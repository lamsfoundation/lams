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

package org.lamsfoundation.lams.tool.mdchat.web.actions;

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
import org.lamsfoundation.lams.tool.mdchat.dto.MdlChatDTO;
import org.lamsfoundation.lams.tool.mdchat.model.MdlChat;
import org.lamsfoundation.lams.tool.mdchat.model.MdlChatConfigItem;
import org.lamsfoundation.lams.tool.mdchat.model.MdlChatSession;
import org.lamsfoundation.lams.tool.mdchat.model.MdlChatUser;
import org.lamsfoundation.lams.tool.mdchat.service.MdlChatServiceProxy;
import org.lamsfoundation.lams.tool.mdchat.service.IMdlChatService;
import org.lamsfoundation.lams.tool.mdchat.util.MdlChatConstants;
import org.lamsfoundation.lams.tool.mdchat.util.MdlChatException;
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
 * @struts.action-forward name="mdlChat" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private static final String TOOL_APP_URL = Configuration
			.get(ConfigurationKeys.SERVER_URL)
			+ "/tool/" + MdlChatConstants.TOOL_SIGNATURE + "/";

	// public static final String RELATIVE_LEARNER_URL = "mod/Chat/view.php?";

	public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
	public static final String MOODLE_VIEW_URL = "mod/chat/view.php";
	public static final String RELATIVE_TEACHER_URL = "mod/chat/view.php?";

	private IMdlChatService mdlChatService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up mdlChatService
		if (mdlChatService == null) {
			mdlChatService = MdlChatServiceProxy.getMdlChatService(this
					.getServlet().getServletContext());
		}

		// Retrieve the session and content.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,
				AttributeNames.PARAM_MODE, false);
		MdlChatSession mdlChatSession = mdlChatService
				.getSessionBySessionId(toolSessionID);
		if (mdlChatSession == null) {
			throw new MdlChatException(
					"Cannot retreive session with toolSessionID: "
							+ toolSessionID);
		}

		MdlChat mdlChat = mdlChatSession.getMdlChat();
		MdlChatUser mdlChatUser = getCurrentUser(toolSessionID);
		
		// check defineLater
		if (mdlChat.isDefineLater()) {
			return mapping.findForward("defineLater");
		}

		MdlChatDTO mdlChatDTO = new MdlChatDTO();
		request.setAttribute("mdlChatDTO", mdlChatDTO);

		// Set the content in use flag.
		if (!mdlChat.isContentInUse()) {
			mdlChat.setContentInUse(new Boolean(true));
			mdlChatService.saveOrUpdateMdlChat(mdlChat);
		}

		// check runOffline
		if (mdlChat.isRunOffline()) {
			return mapping.findForward("runOffline");
		}

		if (mdlChat.getExtToolContentId() != null) {
			try {
				String responseUrl = mdlChatService.getConfigItem(
						MdlChatConfigItem.KEY_EXTERNAL_SERVER_URL)
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
				
				responseUrl += "&id=" + mdlChatSession.getExtSessionId()
						+ "&returnUrl=" + returnUrl + "&dest="
						+ encodedMoodleRelativePath + "&is_learner=1"
						+ "&isFinished=" + mdlChatUser.isFinishedActivity();

				log.debug("Redirecting for mdl Chat learner: " + responseUrl);
				response.sendRedirect(responseUrl);
			} catch (Exception e) {
				log.error("Could not redirect to mdl Chat authoring", e);
			}
		} else {
			throw new MdlChatException("External content id null for learner");
		}
		return null;
	}

	private MdlChatUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		MdlChatUser mdlChatUser = mdlChatService
				.getUserByUserIdAndSessionId(new Long(user.getUserID()
						.intValue()), toolSessionId);

		if (mdlChatUser == null) {
			MdlChatSession mdlChatSession = mdlChatService
					.getSessionBySessionId(toolSessionId);
			mdlChatUser = mdlChatService.createMdlChatUser(user,
					mdlChatSession);
		}

		return mdlChatUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

		MdlChatUser mdlChatUser = getCurrentUser(toolSessionID);

		if (mdlChatUser != null) {
			mdlChatUser.setFinishedActivity(true);
			mdlChatService.saveOrUpdateMdlChatUser(mdlChatUser);
		} else {
			log.error("finishActivity(): couldn't find MdlChatUser with id: "
					+ mdlChatUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		ToolSessionManager sessionMgrService = MdlChatServiceProxy
				.getMdlChatSessionManager(getServlet().getServletContext());

		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,
					mdlChatUser.getUserId());
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new MdlChatException(e);
		} catch (ToolException e) {
			throw new MdlChatException(e);
		} catch (IOException e) {
			throw new MdlChatException(e);
		}

		return null; // TODO need to return proper page.
	}
}
