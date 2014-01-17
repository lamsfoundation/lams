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

package org.lamsfoundation.lams.tool.videoRecorder.web.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderUserDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderConstants;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.tool.videoRecorder.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="videoRecorder" path="tiles:/learning/main"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private IVideoRecorderService videoRecorderService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		LearningForm learningForm = (LearningForm) form;

		// 'toolSessionID' and 'mode' paramters are expected to be present.
		// TODO need to catch exceptions and handle errors.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,
				AttributeNames.PARAM_MODE, MODE_OPTIONAL);

		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up videoRecorderService
		if (videoRecorderService == null) {
			videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(this
					.getServlet().getServletContext());
		}

		// Retrieve the session and content.
		VideoRecorderSession videoRecorderSession = videoRecorderService
				.getSessionBySessionId(toolSessionID);
		if (videoRecorderSession == null) {
			throw new VideoRecorderException(
					"Cannot retrieve session with toolSessionID"
							+ toolSessionID);
		}

		VideoRecorder videoRecorder = videoRecorderSession.getVideoRecorder();

		Long toolContentID = videoRecorder.getToolContentId();

		// check defineLater
		if (videoRecorder.isDefineLater()) {
			return mapping.findForward("defineLater");
		}

		VideoRecorderUser videoRecorderUser;
		if (mode.equals(ToolAccessMode.TEACHER)) {
			Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
			videoRecorderUser = videoRecorderService.getUserByUserIdAndSessionId(userID, toolSessionID);
		} else {
			videoRecorderUser = getCurrentUser(toolSessionID);
		}
		
		VideoRecorderDTO videoRecorderDT0 = new VideoRecorderDTO(videoRecorder);
		
		VideoRecorderUserDTO videoRecorderUserDTO = new VideoRecorderUserDTO(videoRecorderUser);
		
		// set mode, toolSessionID and userId
		request.setAttribute("mode", mode.toString());
		request.setAttribute("videoRecorderUserDTO", videoRecorderUserDTO);
		request.setAttribute("userId", videoRecorderUser.getUid());
		request.setAttribute("videoRecorderDTO", videoRecorderDT0);
		learningForm.setToolSessionID(toolSessionID);
		request.setAttribute("toolSessionId", toolSessionID);
		request.setAttribute("toolContentId", toolContentID);
		
		// getting the contentfolderid using the session and lesson
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		IBaseDAO baseDAO =(IBaseDAO) ctx.getBean("baseDAO");
		ToolSession toolSession = (ToolSession)baseDAO.find(ToolSession.class, toolSessionID);
		String contentFolderId = toolSession.getLesson().getLearningDesign().getContentFolderID();
		
		request.setAttribute("contentFolderId", contentFolderId);
		
		// set language xml
		request.setAttribute("languageXML", videoRecorderService.getLanguageXML());
		
		// set red5 server url
		String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
		request.setAttribute("red5ServerUrl", red5ServerUrl);
		
		// set LAMS server url
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		request.setAttribute("serverUrl", serverUrl);
		
		// Set the content in use flag.
		if (!videoRecorder.isContentInUse()) {
			videoRecorder.setContentInUse(new Boolean(true));
			videoRecorderService.saveOrUpdateVideoRecorder(videoRecorder);
		}
		
        	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request, getServlet()
        		.getServletContext());
		
		// get any existing videoRecorder entry
		
		// set readOnly flag.
		if (mode.equals(ToolAccessMode.TEACHER) || (videoRecorder.isLockOnFinished() && videoRecorderUser.isFinishedActivity())) {
			request.setAttribute("contentEditable", false);
		} else {
			request.setAttribute("contentEditable", true);
		}
		request.setAttribute("finishedActivity", videoRecorderUser.isFinishedActivity());

		return mapping.findForward("videoRecorder");
	}

	private VideoRecorderUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		VideoRecorderUser videoRecorderUser = videoRecorderService
				.getUserByUserIdAndSessionId(new Long(user.getUserID()
						.intValue()), toolSessionId);

		if (videoRecorderUser == null) {
			VideoRecorderSession videoRecorderSession = videoRecorderService
					.getSessionBySessionId(toolSessionId);
			videoRecorderUser = videoRecorderService.createVideoRecorderUser(user,
					videoRecorderSession);
		}

		return videoRecorderUser;
	}

	public ActionForward finishActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

		VideoRecorderUser videoRecorderUser = getCurrentUser(toolSessionID);

		if (videoRecorderUser != null) {

			LearningForm learningForm = (LearningForm) form;

			// TODO fix idType to use real value not 999

			if (videoRecorderUser.getEntryUID() == null) {
				videoRecorderUser.setEntryUID(videoRecorderService.createNotebookEntry(
						toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL, VideoRecorderConstants.TOOL_SIGNATURE,
						videoRecorderUser.getUserId().intValue(), learningForm
								.getEntryText()));
			} else {
				// update existing entry.
				videoRecorderService.updateEntry(videoRecorderUser.getEntryUID(),
						learningForm.getEntryText());
			}

			videoRecorderUser.setFinishedActivity(true);
			videoRecorderService.saveOrUpdateVideoRecorderUser(videoRecorderUser);
		} else {
			log.error("finishActivity(): couldn't find VideoRecorderUser with id: "
					+ videoRecorderUser.getUserId() + "and toolSessionID: "
					+ toolSessionID);
		}

		ToolSessionManager sessionMgrService = VideoRecorderServiceProxy
				.getVideoRecorderSessionManager(getServlet().getServletContext());

		String nextActivityUrl;
		try {
			nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID,
					videoRecorderUser.getUserId());
			response.sendRedirect(nextActivityUrl);
		} catch (DataMissingException e) {
			throw new VideoRecorderException(e);
		} catch (ToolException e) {
			throw new VideoRecorderException(e);
		} catch (IOException e) {
			throw new VideoRecorderException(e);
		}

		return null; // TODO need to return proper page.
	}
}
