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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderSessionDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderUserDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

	private static final long serialVersionUID = -2829707715037631881L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "videoRecorder_main.html";
	public static final String VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "tool/lavidr10/includes/flash/";
	public static final String VIDEORECORDER_RECORDINGS_HTTP_FOLDER_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "tool/lavidr10/recordings/";
	public static final String VIDEORECORDER_RECORDINGS_FOLDER_URL = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + "/" + "lams-tool-lavidr10.war" + "/" + "recordings" + "/";

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
		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO(videoRecorder);

		UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession()
				.getAttribute(AttributeNames.USER);

		VideoRecorderUser videoRecorderUser = videoRecorderService
				.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()),
						toolSessionID);
		
		VideoRecorderUserDTO videoRecorderUserDTO = new VideoRecorderUserDTO(videoRecorderUser);

		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionIdAndUserId(toolSessionID, videoRecorderUser.getUid());
		
		int nbRecordings = videoRecorderRecordingDTOs.size();
		
		request.getSession().setAttribute("videoRecorderDTO", videoRecorderDTO);
		request.getSession().setAttribute("videoRecorderUserDTO", videoRecorderUserDTO);
		request.getSession().setAttribute("videoRecorderRecordingDTOs", videoRecorderRecordingDTOs);
		
		// set language xml
		request.getSession().setAttribute("languageXML", videoRecorderService.getLanguageXMLForFCK());
		
		// set red5 server url
		String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
		request.getSession().setAttribute("red5ServerUrl", red5ServerUrl);
		
		// set LAMS server url
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		request.getSession().setAttribute("serverUrl", serverUrl);
		
		ArrayList<String>[] fileArray = new ArrayList[2];
		fileArray[0] = new ArrayList<String>();
		fileArray[1] = new ArrayList<String>();

		fileArray[0].add("VideoRecorderFCKEditor.swf");
		fileArray[0].add("playerProductInstall.swf");
		fileArray[0].add("AC_OETags.js");
		
		try {
			Iterator<VideoRecorderRecordingDTO> iter = videoRecorderRecordingDTOs.iterator();
			while(iter.hasNext()){
				VideoRecorderRecordingDTO vr = (VideoRecorderRecordingDTO) iter.next();
			    InputStream is = getResponseInputStreamFromExternalServer("http://172.20.100.22:8080/streams/" + vr.getFilename(), new HashMap<String, String>());
	
			    File imageFile = new File(VIDEORECORDER_RECORDINGS_FOLDER_URL + vr.getFilename());
			    
			    FileOutputStream out = new FileOutputStream(imageFile);

			    byte[] buf = new byte[10 * 1024];
			    int len;
			    while ((len = is.read(buf)) > 0) {
			    	out.write(buf, 0, len);
			    }
			    
			    out.close();
			    
			    fileArray[1].add(vr.getFilename());
			}
		} catch (Exception e) {
			logger.error("Could not find files on Red5 server", e);
		}

		// bundling files
		try {
			String[] urls = new String[2];
			urls[0] = VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL;
			urls[1] = VIDEORECORDER_RECORDINGS_HTTP_FOLDER_URL;
			
			MultipleDirFileBundler fileBundler = new MultipleDirFileBundler();
		    fileBundler.bundle(request, cookies, directoryName, urls, fileArray);
		} catch (Exception e) {
			logger.error("Could not bundle files", e);
		}
	}

	private void doTeacherExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws VideoRecorderException {

		logger.debug("doExportTeacher: toolContentID:" + toolContentID);

		// check if toolContentID available
		if (toolSessionID == null) {
			String error = "Tool Session ID is missing. Unable to continue";
			logger.error(error);
			throw new VideoRecorderException(error);
		}

		VideoRecorderSession videoRecorderSession = videoRecorderService
				.getSessionBySessionId(toolSessionID);

		VideoRecorder videoRecorder = videoRecorderSession.getVideoRecorder();
		VideoRecorderDTO videoRecorderDTO = new VideoRecorderDTO(videoRecorder);
		
		UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession()
				.getAttribute(AttributeNames.USER);

		VideoRecorderUser videoRecorderUser = videoRecorderService
				.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()),
						toolSessionID);
		
		VideoRecorderUserDTO videoRecorderUserDTO = new VideoRecorderUserDTO(videoRecorderUser);

		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionID);
		
		int nbRecordings = videoRecorderRecordingDTOs.size();
		
		request.getSession().setAttribute("videoRecorderDTO", videoRecorderDTO);
		request.getSession().setAttribute("videoRecorderUserDTO", videoRecorderUserDTO);
		request.getSession().setAttribute("videoRecorderRecordingDTOs", videoRecorderRecordingDTOs);
		
		// set language xml
		request.getSession().setAttribute("languageXML", videoRecorderService.getLanguageXMLForFCK());
		
		// set red5 server url
		String red5ServerUrl = Configuration.get(ConfigurationKeys.RED5_SERVER_URL);
		request.getSession().setAttribute("red5ServerUrl", red5ServerUrl);
		
		// set LAMS server url
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		request.getSession().setAttribute("serverUrl", serverUrl);
		
		ArrayList<String>[] fileArray = new ArrayList[2];

		fileArray[0].add("VideoRecorderFCKEditor.swf");
		fileArray[0].add("playerProductInstall.swf");
		fileArray[0].add("AC_OETags.js");
		
		try {
			int counter = 3;
			int listSize = videoRecorderRecordingDTOs.size();
			Iterator<VideoRecorderRecordingDTO> iter = videoRecorderRecordingDTOs.iterator();
			while(iter.hasNext()){
				VideoRecorderRecordingDTO vr = (VideoRecorderRecordingDTO) iter.next();
			    InputStream is = getResponseInputStreamFromExternalServer("http://172.20.100.22:8080/streams/" + vr.getFilename(), new HashMap<String, String>());
	
			    File imageFile = new File(VIDEORECORDER_RECORDINGS_FOLDER_URL + vr.getFilename());
			    
			    FileOutputStream out = new FileOutputStream(imageFile);

			    byte[] buf = new byte[10 * 1024];
			    int len;
			    while ((len = is.read(buf)) > 0) {
			    	out.write(buf, 0, len);
			    }
			    
			    out.close();
			    
			    fileArray[1].add(vr.getFilename());
			    
			    counter++;
			}
		} catch (Exception e) {
			logger.error("Could not find files on Red5 server", e);
		}

		// bundling files
		try {
			String[] urls = new String[2];
			urls[0] = VIDEORECORDER_INCLUDES_HTTP_FOLDER_URL;
			urls[1] = VIDEORECORDER_RECORDINGS_FOLDER_URL;
			
			MultipleDirFileBundler fileBundler = new MultipleDirFileBundler();
		    fileBundler.bundle(request, cookies, directoryName, urls, fileArray);
		} catch (Exception e) {
			logger.error("Could not bundle files", e);
		}
	}

    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
    throws ToolException, IOException {
		if (!urlStr.endsWith("?"))
		    urlStr += "?";
		
		for (Entry<String, String> entry : params.entrySet()) {
		    urlStr += "&" + entry.getKey() + "=" + entry.getValue();
		}
		
		logger.debug("Making request to external servlet: " + urlStr);
		
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		if (!(conn instanceof HttpURLConnection)) {
		    logger.error("Fail to connect to external server though url:  " + urlStr);
		    throw new ToolException("Fail to connect to external server though url:  " + urlStr);
		}
		
		HttpURLConnection httpConn = (HttpURLConnection) conn;
		if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		    logger.error("Fail to fetch data from external server, response code:  " + httpConn.getResponseCode()
			    + " Url: " + urlStr);
		    throw new ToolException("Fail to fetch data from external server, response code:  "
			    + httpConn.getResponseCode() + " Url: " + urlStr);
		}
		
		InputStream is = url.openConnection().getInputStream();
		if (is == null) {
		    logger.error("Fail to fetch data from external server, return InputStream null:  " + urlStr);
		    throw new ToolException("Fail to fetch data from external server, return inputStream null:  " + urlStr);
		}
		
		return is;
	}
}
