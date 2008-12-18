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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
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

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="videoRecorder" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
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
					"Cannot retreive session with toolSessionID"
							+ toolSessionID);
		}

		VideoRecorder videoRecorder = videoRecorderSession.getVideoRecorder();

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
		
		// set mode, toolSessionID and VideoRecorderDTO
		request.setAttribute("mode", mode.toString());
		request.setAttribute("userId", videoRecorderUser.getUid());
		learningForm.setToolSessionID(toolSessionID);
		request.setAttribute("toolSessionId", toolSessionID);

		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO(videoRecorder);
		request.setAttribute("videoRecorderDTO", videoRecorderDTO);
		
		/*
		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs;
		
		if(videoRecorderDTO.allowLearnerVideoVisibility){
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionID);
		} else{
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionIdAndUserId(toolSessionID, videoRecorderUser.getUserId());
		}
		
		request.setAttribute("videoRecorderRecordingDTOs", videoRecorderRecordingDTOs);
		*/
		
		// Set the content in use flag.
		if (!videoRecorder.isContentInUse()) {
			videoRecorder.setContentInUse(new Boolean(true));
			videoRecorderService.saveOrUpdateVideoRecorder(videoRecorder);
		}

		// check runOffline
		if (videoRecorder.isRunOffline()) {
			return mapping.findForward("runOffline");
		}
		
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
