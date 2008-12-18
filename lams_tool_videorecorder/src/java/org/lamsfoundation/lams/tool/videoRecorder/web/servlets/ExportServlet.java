/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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

/* $Id$ */

package org.lamsfoundation.lams.tool.videoRecorder.web.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderSessionDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderUserDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

	private static final long serialVersionUID = -2829707715037631881L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "videoRecorder_main.html";

	private IVideoRecorderService videoRecorderService;

	protected String doExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies) {

		if (videoRecorderService == null) {
			videoRecorderService = VideoRecorderServiceProxy
					.getVideoRecorderService(getServletContext());
		}

		try {
			if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
				request.getSession().setAttribute(AttributeNames.ATTR_MODE,
						ToolAccessMode.LEARNER);
				doLearnerExport(request, response, directoryName, cookies);
			} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER
					.toString())) {
				request.getSession().setAttribute(AttributeNames.ATTR_MODE,
						ToolAccessMode.TEACHER);
				doTeacherExport(request, response, directoryName, cookies);
			}
		} catch (VideoRecorderException e) {
			logger.error("Cannot perform export for videoRecorder tool.");
		}

		String basePath = request.getScheme() + "://" + request.getServerName()
				+ ":" + request.getServerPort() + request.getContextPath();
		writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp",
				directoryName, FILENAME, cookies);

		return FILENAME;
	}

	protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {
        if (toolContentID == null && toolSessionID == null) {
            logger.error("Tool content Id or and session Id are null. Unable to activity title");
        } else {
    		if (videoRecorderService == null) {
    			videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServletContext());
    		}

        	VideoRecorder content = null;
            if ( toolContentID != null ) {
            	content = videoRecorderService.getVideoRecorderByContentId(toolContentID);
            } else {
            	VideoRecorderSession session=videoRecorderService.getSessionBySessionId(toolSessionID);
            	if ( session != null )
            		content = session.getVideoRecorder();
            }
            if ( content != null ) {
            	activityTitle = content.getTitle();
            }
        }
        return super.doOfflineExport(request, response, directoryName, cookies);
	}
	
	private void doLearnerExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws VideoRecorderException {

		logger.debug("doExportLearner: toolContentID:" + toolSessionID);

		// check if toolContentID available
		if (toolSessionID == null) {
			String error = "Tool Session ID is missing. Unable to continue";
			logger.error(error);
			throw new VideoRecorderException(error);
		}

		VideoRecorderSession videoRecorderSession = videoRecorderService
				.getSessionBySessionId(toolSessionID);

		VideoRecorder videoRecorder = videoRecorderSession.getVideoRecorder();

		UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession()
				.getAttribute(AttributeNames.USER);

		VideoRecorderUser videoRecorderUser = videoRecorderService
				.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()),
						toolSessionID);

		NotebookEntry videoRecorderEntry = videoRecorderService.getEntry(videoRecorderUser
				.getEntryUID());

		// construct dto's
		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO();
		videoRecorderDTO.setTitle(videoRecorder.getTitle());
		videoRecorderDTO.setInstructions(videoRecorder.getInstructions());

		VideoRecorderSessionDTO sessionDTO = new VideoRecorderSessionDTO();
		sessionDTO.setSessionName(videoRecorderSession.getSessionName());
		sessionDTO.setSessionID(videoRecorderSession.getSessionId());

		// If the user hasn't put in their entry yet, videoRecorderEntry will be null;
		VideoRecorderUserDTO userDTO = videoRecorderEntry != null 
			? new VideoRecorderUserDTO(videoRecorderUser,videoRecorderEntry) 
			: new VideoRecorderUserDTO(videoRecorderUser);

		sessionDTO.getUserDTOs().add(userDTO);
		videoRecorderDTO.getSessionDTOs().add(sessionDTO);

		request.getSession().setAttribute("videoRecorderDTO", videoRecorderDTO);
	}

	private void doTeacherExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws VideoRecorderException {

		logger.debug("doExportTeacher: toolContentID:" + toolContentID);

		// check if toolContentID available
		if (toolContentID == null) {
			String error = "Tool Content ID is missing. Unable to continue";
			logger.error(error);
			throw new VideoRecorderException(error);
		}

		VideoRecorder videoRecorder = videoRecorderService
				.getVideoRecorderByContentId(toolContentID);

		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO(videoRecorder);
		
		request.getSession().setAttribute("videoRecorderDTO", videoRecorderDTO);
	}

}
