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


package org.lamsfoundation.lams.tool.videoRecorder.web.actions;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderSessionDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderUserDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
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
 * @struts.action path="/monitoring" parameter="method" scope="request"
 *                name="monitoringForm" validate="false"
 *
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="videoRecorder_display"
 *                        path="tiles:/monitoring/videoRecorder_display"
 * @struts.action-forward name="videoRecorder_openInstance"
 *                        path="/pages/monitoring/videoRecorderOpenInstance.jsp"
 *
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    private static final boolean MODE_OPTIONAL = false;

    public IVideoRecorderService videoRecorderService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	// get httpsession
	HttpSession ss = SessionManager.getSession();

	// get LAMS user
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	VideoRecorder videoRecorder = videoRecorderService.getVideoRecorderByContentId(toolContentID);

	if (videoRecorder == null) {
	    // TODO error page.
	}

	VideoRecorderDTO videoRecorderDT0 = new VideoRecorderDTO(videoRecorder);

	// get sessions in order to get first session
	Set<VideoRecorderSessionDTO> sessions = videoRecorder.getVideoRecorderSessions();
	//videoRecorderDT0.getSessionDTOs();

	for (Iterator sessIter = sessions.iterator(); sessIter.hasNext();) {
	    VideoRecorderSession session = (VideoRecorderSession) sessIter.next();

	    VideoRecorderSessionDTO sessionDTO = new VideoRecorderSessionDTO(session);
	    videoRecorderDT0.getSessionDTOs().add(sessionDTO);

	}

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	videoRecorderDT0.setCurrentTab(currentTab);

	request.setAttribute("videoRecorderDTO", videoRecorderDT0);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	boolean isGroupedActivity = videoRecorderService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	return mapping.findForward("success");
    }

    public ActionForward showVideoRecorder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long uid = new Long(WebUtil.readLongParam(request, "userUID"));

	VideoRecorderUser user = videoRecorderService.getUserByUID(uid);
	NotebookEntry entry = videoRecorderService.getEntry(user.getEntryUID());

	VideoRecorderUserDTO userDTO = new VideoRecorderUserDTO(user, entry);

	request.setAttribute("userDTO", userDTO);

	return mapping.findForward("videoRecorder_display");
    }

    public ActionForward openVideoRecorderInstance(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	// get httpsession
	HttpSession ss = SessionManager.getSession();

	// get LAMS user
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Long sessionId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));

	VideoRecorder videoRecorder = videoRecorderService.getVideoRecorderByContentId(toolContentID);

	if (videoRecorder == null) {
	    // TODO error page.
	}

	VideoRecorderDTO videoRecorderDT0 = new VideoRecorderDTO(videoRecorder);
	VideoRecorderSession session = videoRecorderService.getSessionBySessionId(sessionId);

	// check Monitor user is part of the session
	VideoRecorderUser videoRecorderUser = videoRecorderService
		.getUserByUserIdAndSessionId(new Long(user.getUserID()), sessionId);

	if (videoRecorderUser == null) {
	    // create new Monitoring user for Session
	    videoRecorderUser = videoRecorderService.createVideoRecorderUser(user, session);
	}

	request.setAttribute("contentEditable", true);
	request.setAttribute("mode", "author");

	request.setAttribute("videoRecorderDTO", videoRecorderDT0);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	request.setAttribute("monitoringUid", videoRecorderUser.getUid());
	request.setAttribute("contentFolderID", contentFolderID);

	// set language xml
	request.setAttribute("languageXML", videoRecorderService.getLanguageXML());

	// set red5 server url
	String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
	request.setAttribute("red5ServerUrl", red5ServerUrl);

	// set LAMS server url
	String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	request.setAttribute("serverUrl", serverUrl);

	return mapping.findForward("videoRecorder_openInstance");
    }

    /**
     * set up videoRecorderService
     */
    private void setupService() {
	if (videoRecorderService == null) {
	    videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(this.getServlet().getServletContext());
	}
    }
}
