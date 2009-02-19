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

import java.sql.Array;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderSessionDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderUserDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.web.forms.AuthoringForm;
import org.lamsfoundation.lams.tool.videoRecorder.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="videoRecorder_display"
 *                        path="tiles:/monitoring/videoRecorder_display"
 * 
 */
public class MonitoringAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(MonitoringAction.class);
	
	private static final boolean MODE_OPTIONAL = false;
	
	public IVideoRecorderService videoRecorderService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		setupService();
		
		// get httpsession
		HttpSession ss = SessionManager.getSession();
		
		// get LAMS user
		UserDTO user = (UserDTO)ss.getAttribute(AttributeNames.USER);
		
		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		String contentFolderID = WebUtil.readStrParam(request,
				AttributeNames.PARAM_CONTENT_FOLDER_ID);
		
		VideoRecorder videoRecorder = videoRecorderService
				.getVideoRecorderByContentId(toolContentID);

		if (videoRecorder == null) {
			// TODO error page.
		}

		VideoRecorderDTO videoRecorderDT0 = new VideoRecorderDTO(videoRecorder);
				
		// get first session
		Set<VideoRecorderSessionDTO> sessions = videoRecorderDT0.getSessionDTOs();
		Object[] sessionsArray = sessions.toArray();
		VideoRecorderSessionDTO firstSession = (VideoRecorderSessionDTO)sessionsArray[0];

		// get toolSessionId
		Long toolSessionID = firstSession.getSessionID();
		
		// stupid shit to convert to Long
		long primLongUserId = user.getUserID();
		Long longUserId = primLongUserId;
		
		// get user
		VideoRecorderUser videoRecorderUser = videoRecorderService.getUserByUserIdAndSessionId(longUserId, toolSessionID);
		
		Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB,true);
		videoRecorderDT0.setCurrentTab(currentTab);
		
		request.setAttribute("contentEditable", true);
		request.setAttribute("mode", "author");
		request.setAttribute("userId", videoRecorderUser.getUid());
		request.setAttribute("toolSessionId", toolSessionID);
		request.setAttribute("toolContentId", toolContentID);
		request.setAttribute("videoRecorderDTO", videoRecorderDT0);
		request.setAttribute("contentFolderID", contentFolderID);
		
		// set language xml
		request.setAttribute("languageXML", videoRecorderService.getLanguageXML());
		
		// set red5 server url
		String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
		request.setAttribute("red5ServerUrl", red5ServerUrl);
		
		// set LAMS server url
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		request.setAttribute("serverUrl", serverUrl);
		
		return mapping.findForward("success");
	}

	public ActionForward showVideoRecorder(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		setupService();
		
		Long uid = new Long(WebUtil.readLongParam(request, "userUID"));

		VideoRecorderUser user = videoRecorderService.getUserByUID(uid);
		NotebookEntry entry = videoRecorderService.getEntry(user.getEntryUID());

		VideoRecorderUserDTO userDTO = new VideoRecorderUserDTO(user, entry);

		request.setAttribute("userDTO", userDTO);

		return mapping.findForward("videoRecorder_display");
	}
	
	/**
	 * set up videoRecorderService
	 */
	private void setupService() {
		if (videoRecorderService == null) {
			videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(this
					.getServlet().getServletContext());
		}
	}
}
