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

package org.lamsfoundation.lams.tool.dlfrum.web.actions;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dlfrum.dto.DotLRNForumDTO;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForum;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumSession;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumUser;
import org.lamsfoundation.lams.tool.dlfrum.service.DotLRNForumServiceProxy;
import org.lamsfoundation.lams.tool.dlfrum.service.IDotLRNForumService;
import org.lamsfoundation.lams.tool.dlfrum.util.DotLRNForumConstants;
import org.lamsfoundation.lams.tool.dlfrum.util.DotLRNForumException;
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
 * @struts.action path="/learning" parameter="dispatch" scope="request" name="learningForm"
 * @struts.action-forward name="dotLRNForum" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;
	
	private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/" + DotLRNForumConstants.TOOL_SIGNATURE + "/";

	private IDotLRNForumService dotLRNForumService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//LearningForm learningForm = (LearningForm) form;

		// 'toolSessionID' and 'mode' paramters are expected to be present.
		// TODO need to catch exceptions and handle errors.
		//ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, MODE_OPTIONAL);

		Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up dotLRNForumService
		if (dotLRNForumService == null) {
			dotLRNForumService = DotLRNForumServiceProxy.getDotLRNForumService(this.getServlet().getServletContext());
		}

		// Retrieve the session and content.
		DotLRNForumSession dotLRNForumSession = dotLRNForumService.getSessionBySessionId(toolSessionID);
		if (dotLRNForumSession == null) {
			throw new DotLRNForumException("Cannot retreive session with toolSessionID: "+ toolSessionID);
		}

		DotLRNForum dotLRNForum = dotLRNForumSession.getDotLRNForum();

		// check defineLater
		if (dotLRNForum.isDefineLater()) {
			return mapping.findForward("defineLater");
		}

		// set mode, toolSessionID and DotLRNForumDTO
		//request.setAttribute("mode", mode.toString());
		//learningForm.setToolSessionID(toolSessionID);

		DotLRNForumDTO dotLRNForumDTO = new DotLRNForumDTO();

		
		request.setAttribute("dotLRNForumDTO", dotLRNForumDTO);

		// Set the content in use flag.
		if (!dotLRNForum.isContentInUse()) {
			dotLRNForum.setContentInUse(new Boolean(true));
			dotLRNForumService.saveOrUpdateDotLRNForum(dotLRNForum);
		}

		// check runOffline
		if (dotLRNForum.isRunOffline()) {
			return mapping.findForward("runOffline");
		}

		if (dotLRNForum.getExtToolContentId()!=null)
		{
			try{
				String returnUrl = TOOL_APP_URL + "learning.do?"
					+ AttributeNames.PARAM_TOOL_SESSION_ID + "=" + toolSessionID.toString()
					+ "&dispatch=finishActivity";
				returnUrl = URLEncoder.encode(returnUrl, "UTF8");
				
				String responseUrl = dotLRNForum.getExtCourseUrl() + "/forums/forum-view?"
					+ "forum_id=" + dotLRNForumSession.getExtSessionId()
					+ "&" + AttributeNames.PARAM_TOOL_SESSION_ID + "=" + toolSessionID.toString()
					+ "&returnUrl=" + returnUrl;
				
				/*
				String responseUrl = dotLRNForum.getExtCourseUrl() + "/forums/forum-view?"
					+ "forum_id=" + dotLRNForum.getExtToolContentId()
					+ "&" + AttributeNames.PARAM_TOOL_SESSION_ID + "=" + toolSessionID.toString()
					+ "&returnUrl=" + returnUrl;
				*/
				
				log.debug("Redirecting for dotLRN forum learner: " + responseUrl);
				response.sendRedirect(responseUrl);
			}
			catch (Exception e)
			{
				log.error("Could not redirect to dotLRN forum authoring", e);	
			}
		}
		else
		{
			throw new DotLRNForumException ("External content id null for learner");
		}	
		return null;
	}

	private DotLRNForumUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		DotLRNForumUser dotLRNForumUser = dotLRNForumService
				.getUserByUserIdAndSessionId(new Long(user.getUserID()
						.intValue()), toolSessionId);

		if (dotLRNForumUser == null) {
			DotLRNForumSession dotLRNForumSession = dotLRNForumService
					.getSessionBySessionId(toolSessionId);
			dotLRNForumUser = dotLRNForumService.createDotLRNForumUser(user,
					dotLRNForumSession);
		}

		return dotLRNForumUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

		DotLRNForumUser dotLRNForumUser = getCurrentUser(toolSessionID);

		if (dotLRNForumUser != null) {
			dotLRNForumUser.setFinishedActivity(true);
			dotLRNForumService.saveOrUpdateDotLRNForumUser(dotLRNForumUser);
		} else {
			log.error("finishActivity(): couldn't find DotLRNForumUser with id: "
					+ dotLRNForumUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		ToolSessionManager sessionMgrService = DotLRNForumServiceProxy
			.getDotLRNForumSessionManager(getServlet().getServletContext());

		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,
					dotLRNForumUser.getUserId());
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new DotLRNForumException(e);
		} catch (ToolException e) {
			throw new DotLRNForumException(e);
		} catch (IOException e) {
			throw new DotLRNForumException(e);
		}

		return null; // TODO need to return proper page.
	}
}
