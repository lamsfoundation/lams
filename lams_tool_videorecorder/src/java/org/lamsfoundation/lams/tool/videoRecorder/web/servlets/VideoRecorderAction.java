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

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderRecordingComparator;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
* The action servlet that provides the support for the 
* <UL>
* <LI>AJAX based Chosen Grouping screen</LI>
* <LI>forwards to the learner's view grouping screen for Random Grouping.</LI>
* </UL>
* 
* @author Paul Georges

* ----------------XDoclet Tags--------------------
* 
* @struts:action path="/videoRecorderActions" 
*                parameter="method" 
*                validate="false"
* 
* ----------------XDoclet Tags--------------------
*/
public class VideoRecorderAction extends LamsDispatchAction {

    //---------------------------------------------------------------------
	private static final String TOOL_SESSION_ID = "toolSessionId";
	private static final String USER_ID = "userId";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String FILENAME = "filename";
	private static final String RECORDING_ID = "recordingId";
	private static final String RATING = "rating";
	
	private static final String OK_MSG = "ok";
	private static final String ERROR_MSG = "error";
	
	private Integer getUserId(HttpServletRequest request) {
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		return user != null ? user.getUserID() : null;
	}
	
	/** 
	 * Desctipion
     *
	 * Input parameters: toolSessionID, userID (possibly null)
	 * 
	 * Output format: xml
	 */
	public ActionForward getRecordingsByToolSessionIdAndUserId(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		// get paramaters from POST
    	Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
    	Long userId = WebUtil.readLongParam(request, USER_ID);
    	
    	// get service
		IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServlet().getServletContext());
		
		// create list of recording DTOs
		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs;
		
		/*
		// if no user is specified
		if(userId == 0){
			// get all recordings
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionId);
		// otherwise
		} else{
			// get all recordings from user specified
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionIdAndUserId(toolSessionId, userId);
		}
		*/
		
		// if no user is specified
		videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionId);
		
		// sort the list of recording DTOs in order to create the xml correctly
		Comparator<VideoRecorderRecordingDTO> comp = new VideoRecorderRecordingComparator();
	    Collections.sort(videoRecorderRecordingDTOs, comp);

		String xmlOutput = buildVideoRecordingsXML(videoRecorderRecordingDTOs);
		writeAJAXResponse(response, xmlOutput);
		return null;
	}

	public ActionForward saveRecording(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try{
			// get paramaters from POST
			Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
	    	Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);
	    	Long userId = WebUtil.readLongParam(request, USER_ID);
	    	String title = WebUtil.readStrParam(request, TITLE);
	    	String description = WebUtil.readStrParam(request, DESCRIPTION);
	    	String filename = WebUtil.readStrParam(request, FILENAME);
	    	int rating = WebUtil.readIntParam(request, RATING);
	    	
	    	// get service
			IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServlet().getServletContext());
			
			// get user
			VideoRecorderUser user = videoRecorderService.getUserByUID(userId);
			
			// get session
			VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);
			
			// create videoRecording
			VideoRecorderRecording videoRecording;
			
			// if no recordingId is sent, create a new recording
			if(recordingId == -1){
				videoRecording = new VideoRecorderRecording();
				videoRecording.setCreateBy(user);
				videoRecording.setVideoRecorderSession(session);
				videoRecording.setFilename(filename);
				videoRecording.setRating((float)rating);
				videoRecording.setCreateDate(new Date());
				videoRecording.setNotes("");
			}
			// otherwise get the recording from the DAO
			else{
				videoRecording = videoRecorderService.getRecordingById(recordingId);
			}
			
			videoRecording.setUpdateDate(new Date());
			videoRecording.setTitle(title);
			videoRecording.setDescription(description);
			
			videoRecorderService.saveOrUpdateVideoRecorderRecording(videoRecording);
			
			writeAJAXResponse(response, OK_MSG);
			
		}catch(Exception e){
			writeAJAXResponse(response, ERROR_MSG + e.getMessage());
		}
		
		return null;
	}
	
	private String buildVideoRecordingsXML(List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs){
		String xmlOutput = "<xml><recordings>";
		Long lastUserId = Long.valueOf("-1");
		Long currentUserId = Long.valueOf("-1");
		
		for(VideoRecorderRecordingDTO videoRecorderRecordingDTO: videoRecorderRecordingDTOs){
			VideoRecorderUser user = videoRecorderRecordingDTO.getCreateBy();
			currentUserId = user.getUserId();
			if(currentUserId != lastUserId){
				if(lastUserId != -1){
					xmlOutput += "</userFolder>";
				}
				xmlOutput += "<userFolder>";
				xmlOutput += "<title>" + user.getFirstName() + " " + user.getLastName() + "</title>";
			}
			
			xmlOutput += "<child><recording>";
			xmlOutput += "<title>" + videoRecorderRecordingDTO.getTitle() + "</title>";
			xmlOutput += "<uid>" + user.getUid() + "</uid>";
			xmlOutput += "<userId>" + user.getUserId() + "</userId>";
			xmlOutput += "<author>" + user.getFirstName() + " " + user.getLoginName() + "</author>";
			xmlOutput += "<createDate>" + videoRecorderRecordingDTO.getCreateDate() + "</createDate>";
			xmlOutput += "<updateDate>" + videoRecorderRecordingDTO.getUpdateDate() + "</updateDate>";
			xmlOutput += "<description>" + videoRecorderRecordingDTO.getDescription() + "</description>";
			xmlOutput += "<notes>" + videoRecorderRecordingDTO.getNotes() + "</notes>";
			xmlOutput += "<rating>" + videoRecorderRecordingDTO.getUid() + "</rating>";
			xmlOutput += "<filename>" + videoRecorderRecordingDTO.getFilename() + "</filename>";
			xmlOutput += "</recording></child>";
			
			lastUserId = currentUserId;
		}
		
		if(lastUserId == currentUserId && (lastUserId != -1 || currentUserId != -1)){
			xmlOutput += "</userFolder>";
		}
		
		xmlOutput += "</recordings></xml>";
		return xmlOutput;
	}
}