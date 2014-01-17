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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.web.action.CustomToolImageBundler;
import org.lamsfoundation.lams.learning.export.web.action.MultipleDirFileBundler;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderUserDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderRecordingComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

	private static final long serialVersionUID = -2829707715037631881L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "videoRecorder_main.html";
	public static final String VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "tool/lavidr10/includes/flash/";
	public static final String VIDEORECORDER_RECORDINGS_HTTP_FOLDER_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "tool/lavidr10/recordings/";
	public static final String VIDEORECORDER_RECORDINGS_FOLDER_DEST = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + "/" + "lams-tool-lavidr10.war" + "/" + "recordings" + "/";
	public static final String VIDEORECORDER_RECORDINGS_FOLDER_SRC = Configuration.get(ConfigurationKeys.RED5_RECORDINGS_URL);
	public static final String FLV_EXTENSION = ".flv";

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

		String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
		writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp",
				directoryName, FILENAME, cookies);

		return FILENAME;
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
		
		// get the session from the toolSessionId
		VideoRecorderSession videoRecorderSession = videoRecorderService
				.getSessionBySessionId(toolSessionID);

		// get the video recorder from the session		
		VideoRecorder videoRecorder = videoRecorderSession.getVideoRecorder();
		
		// get toolContentId from video recorder
		toolContentID = videoRecorder.getToolContentId();
		
		/*
		// get videorecorder from toolContentId
		VideoRecorder videoRecorder = videoRecorderService.getVideoRecorderByContentId(toolContentID);
		*/
		
		// get export options
		boolean exportAll = videoRecorder.isExportAll();
		boolean exportOffline = videoRecorder.isExportOffline();
				
		// transform to dto
		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO(videoRecorder);

		// get lams user from session
		UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession()
				.getAttribute(AttributeNames.USER);

		// get video recorder user from userId and sessionId
		VideoRecorderUser videoRecorderUser = videoRecorderService
				.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()),
						toolSessionID);
		
		// transform to dto
		VideoRecorderUserDTO videoRecorderUserDTO = new VideoRecorderUserDTO(videoRecorderUser);
		
		// get the video recording dtos
		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs;
		
		// get video recording dtos
		if(exportAll)
		{
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionID, toolContentID);
		}else{
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionIdAndUserId(toolSessionID, videoRecorderUser.getUid(), toolContentID);
		}
		
		// sort the list of recording dtos in order to create the xml correctly
		Comparator<VideoRecorderRecordingDTO> comp = new VideoRecorderRecordingComparator("author", "ascending");
	    Collections.sort(videoRecorderRecordingDTOs, comp);
	    
	    // get the nb of recordings
		int nbRecordings = videoRecorderRecordingDTOs.size();
		
		// set dtos to request (used in export jsp)
		request.getSession().setAttribute("videoRecorderDTO", videoRecorderDTO);
		request.getSession().setAttribute("videoRecorderUserDTO", videoRecorderUserDTO);
		request.getSession().setAttribute("videoRecorderRecordingDTOs", videoRecorderRecordingDTOs);
		
		// set language xml to request
		request.getSession().setAttribute("languageXML", videoRecorderService.getLanguageXMLForFCK());
		
		// set red5 server url to request
		String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
		request.getSession().setAttribute("red5ServerUrl", red5ServerUrl);
		
		// set LAMS server url to request
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		request.getSession().setAttribute("serverUrl", serverUrl);
		
		// if doing export offline, copy recordings files
		if(exportOffline){
			bundleFilesForOffline(request, response, directoryName, cookies, videoRecorderRecordingDTOs);
		}
		// otherwise, just copy files (like js)
		else{
			bundleFilesForOnline(request, response, directoryName, cookies, videoRecorderRecordingDTOs);
		}
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
		
		// get video recorder from tool content id
		VideoRecorder videoRecorder = videoRecorderService.getVideoRecorderByContentId(toolContentID);
		
		// make into dto
		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO(videoRecorder);
		
		// get export options
		boolean exportAll = videoRecorder.isExportAll();
		boolean exportOffline = videoRecorder.isExportOffline();
				
		// get lams user from session
		UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession()
				.getAttribute(AttributeNames.USER);
		
		// get video recording dtos from just tool content id
		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionID, toolContentID);
		
		// get nb of recordings
		int nbRecordings = videoRecorderRecordingDTOs.size();
		
		// add dtos to request
		request.getSession().setAttribute("videoRecorderDTO", videoRecorderDTO);
		request.getSession().setAttribute("videoRecorderRecordingDTOs", videoRecorderRecordingDTOs);
		
		// set language xml to request
		request.getSession().setAttribute("languageXML", videoRecorderService.getLanguageXMLForFCK());
		
		// set red5 server url to request
		String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
		request.getSession().setAttribute("red5ServerUrl", red5ServerUrl);
		
		// set LAMS server url to request
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		request.getSession().setAttribute("serverUrl", serverUrl);
		
		// if doing export offline, copy recordings files
		if(exportOffline){
			bundleFilesForOffline(request, response, directoryName, cookies, videoRecorderRecordingDTOs);
		}
		// otherwise, just copy files (like js)
		else{
			bundleFilesForOnline(request, response, directoryName, cookies, videoRecorderRecordingDTOs);
		}
	}
	
	private void bundleFilesForOnline(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies,
			List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs){
		// prepare the file array
		// index 0 is for the files from VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL
		ArrayList<String>[] fileArray = new ArrayList[1];
		fileArray[0] = new ArrayList<String>();

		// add the files need from VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL
		fileArray[0].add("VideoRecorderFCKEditor.swf");
		fileArray[0].add("playerProductInstall.swf");
		fileArray[0].add("AC_OETags.js");
		
		// bundling files
		try {
			String[] srcDirs = new String[2];
			srcDirs[0] = VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL;

			String[] targetDirs = new String[2];
			targetDirs[0] = directoryName + File.separator + "files";
			
			MultipleDirFileBundler fileBundler = new MultipleDirFileBundler();
			fileBundler.bundle(request, cookies, srcDirs, targetDirs, fileArray);
		} catch (Exception e) {
			logger.error("Could not bundle files", e);
		}
	}
	
	private void bundleFilesForOffline(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies,
			List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs){
		// prepare 
		ArrayList<String>[] fileArray = new ArrayList[2];
		fileArray[0] = new ArrayList<String>();
		fileArray[1] = new ArrayList<String>();

		// add the files need from VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL
		fileArray[0].add("VideoRecorderFCKEditor.swf");
		fileArray[0].add("playerProductInstall.swf");
		fileArray[0].add("AC_OETags.js");
		
		// add the files from VIDEORECORDER_RECORDINGS_HTTP_FOLDER_URL
		try {
			// for each video recording
			Iterator<VideoRecorderRecordingDTO> iter = videoRecorderRecordingDTOs.iterator();
			while(iter.hasNext()){
				// get the video recording
				VideoRecorderRecordingDTO vr = (VideoRecorderRecordingDTO) iter.next();
				
				fileArray[1].add(vr.getFilename() + FLV_EXTENSION);
		    }
		} catch (Exception e) {
			logger.error("Could not find files on Red5 server", e);
		}

		// bundling files
		try {
			String[] srcDirs = new String[2];
			srcDirs[0] = VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL;
			srcDirs[1] = VIDEORECORDER_RECORDINGS_FOLDER_SRC;

			String[] targetDirs = new String[2];
			targetDirs[0] = directoryName + File.separator + "files";
			targetDirs[1] = directoryName + File.separator + "files";
			
			MultipleDirFileBundler fileBundler = new MultipleDirFileBundler();
		    fileBundler.bundle(request, cookies, srcDirs, targetDirs, fileArray);
		} catch (Exception e) {
			logger.error("Could not bundle files", e);
		}
	}
}
