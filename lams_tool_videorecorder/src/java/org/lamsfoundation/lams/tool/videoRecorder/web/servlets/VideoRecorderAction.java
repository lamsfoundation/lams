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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderRecordingComparator;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderCommentDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRatingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
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
	
	private static Logger logger = Logger.getLogger(VideoRecorderAction.class);

    //---------------------------------------------------------------------
	private static final String TOOL_SESSION_ID = "toolSessionId";
	private static final String USER_ID = "userId";
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String FILENAME = "filename";
	private static final String RECORDING_ID = "recordingId";
	private static final String COMMENT_ID = "commentId";
	private static final String RATING = "rating";
	private static final String RATING_ID = "ratingId";
	private static final String COMMENT = "comment";
	private static final String IS_JUST_SOUND = "isJustSound";
	private static final String RAW_IMAGE = "rawImage";
	private static final String SORT_BY = "sortBy";
	private static final String SORT_DIR = "sortDirection";
	private static final String GET_ALL = "getAll";
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
    	String sortBy = WebUtil.readStrParam(request, SORT_BY);
    	String sortDirection = WebUtil.readStrParam(request, SORT_DIR);
    	boolean getAll = WebUtil.readBooleanParam(request, GET_ALL);
    	
    	// get service
		IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServlet().getServletContext());
		
		// create list of recording DTOs
		List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs;
		
		// if no user is specified
		if(getAll){
			// get all recordings
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionId);
		// otherwise
		} else{
			// get all recordings from user specified
			videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionIdAndUserId(toolSessionId, userId);
		}
		
		// sort the list of recording DTOs in order to create the xml correctly
		Comparator<VideoRecorderRecordingDTO> comp = new VideoRecorderRecordingComparator(sortBy, sortDirection);
	    Collections.sort(videoRecorderRecordingDTOs, comp);

		String xmlOutput = buildVideoRecordingsXML(videoRecorderRecordingDTOs, userId);
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
	    	Boolean isJustSound = WebUtil.readBooleanParam(request, IS_JUST_SOUND);
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
				videoRecording.setIsJustSound(isJustSound);
				videoRecording.setRatings(null);
				videoRecording.setComments(null);
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

	public ActionForward deleteRecording(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try{
			// get paramaters from POST
	    	Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);
	    	
	    	// get service
			IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServlet().getServletContext());
			
			// get videoRecording
			VideoRecorderRecording videoRecording = videoRecorderService.getRecordingById(recordingId);
			
			// delete the videoRecording
			videoRecorderService.deleteVideoRecorderRecording(videoRecording);
			
			writeAJAXResponse(response, OK_MSG);
			
		}catch(Exception e){
			writeAJAXResponse(response, ERROR_MSG + e.getMessage());
		}
		
		return null;
	}
	
	public ActionForward saveComment(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		try{
			Long commentId = WebUtil.readLongParam(request, COMMENT_ID);
	    	Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);
	    	Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
	    	Long userId = WebUtil.readLongParam(request, USER_ID);
	    	String commentText = WebUtil.readStrParam(request, COMMENT);
	    	
	    	// get service
			IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServlet().getServletContext());
			
			// create new comment
	    	VideoRecorderComment comment;
	    	
			// get user
			VideoRecorderUser user = videoRecorderService.getUserByUID(userId);
			
			// get recording
			VideoRecorderRecording recording = videoRecorderService.getRecordingById(recordingId);
			
			// get session
			VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);
			
			// create a new comment
			if(commentId == -1){
				comment = new VideoRecorderComment();
				comment.setCreateBy(user);
				comment.setRecording(recording);
				comment.setCreateDate(new Date());
				comment.setText(commentText);
				comment.setVideoRecorderSession(session);
			}
			// otherwise get the comment from the DAO
			else{
				comment = videoRecorderService.getCommentById(commentId);
				comment.setText(commentText);
			}
			
			// get comments from recording
			Set<VideoRecorderComment> comments = recording.getComments();
			
			// add new comment to recording's comments
			comments.add(comment);
			
			// save the recording
			videoRecorderService.saveOrUpdateVideoRecorderRecording(recording);
			
			// refresh the recording object in order to get that last comment
			recording = videoRecorderService.getRecordingById(recordingId);
			
			// get the comments from the recording and transform the set into a set of DTOs
			Set<VideoRecorderCommentDTO> commentDTOs = VideoRecorderCommentDTO.getVideoRecorderCommentDTOs(recording.getComments());
			
			// return a response with all comments for current recording
			writeAJAXResponse(response, buildVideoCommentsXML(commentDTOs));
		}catch(Exception e){
			writeAJAXResponse(response, ERROR_MSG + e.getMessage());
		}
		
		return null;
	}

	public ActionForward saveRating(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		try{
			Long ratingId = WebUtil.readLongParam(request, RATING_ID);
	    	Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);
	    	Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
	    	Long userId = WebUtil.readLongParam(request, USER_ID);
	    	float ratingFloat = Float.valueOf(WebUtil.readStrParam(request, RATING));
	    	
	    	// get service
			IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(getServlet().getServletContext());
			
			// create new rating
	    	VideoRecorderRating rating;
	    	
			// get user
			VideoRecorderUser user = videoRecorderService.getUserByUID(userId);
			
			// get recording
			VideoRecorderRecording recording = videoRecorderService.getRecordingById(recordingId);
			
			// get session
			VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);
			
			// create new rating
			if(ratingId == -1){
				rating = new VideoRecorderRating();
				rating.setCreateBy(user);
				rating.setRecording(recording);
				rating.setCreateDate(new Date());
				rating.setRating(ratingFloat);
				rating.setVideoRecorderSession(session);
			}
			// otherwise get the rating from the DAO
			else{
				rating = videoRecorderService.getRatingById(ratingId);
				rating.setRating(ratingFloat);
			}
			
			// get ratings
			Set<VideoRecorderRating> ratings = recording.getRatings();
			
			// add new rating
			ratings.add(rating);
			
			// set the average rating through calculation
			recording.setRating(calculateRating(ratings));
			
			// save recording with new rating
			videoRecorderService.saveOrUpdateVideoRecorderRecording(recording);
			
			// refresh recording
			recording = videoRecorderService.getRecordingById(recordingId);
			
			writeAJAXResponse(response, buildRatingResultXML(recording.getRating(), ratingFloat));
		}catch(Exception e){
			writeAJAXResponse(response, ERROR_MSG + e.getMessage());
		}
		
		return null;
	}
	
	/*
	public ActionForward savePreviewImage(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
    	// Use functionality in org.red5.io.amf3.ByteArray to get parameters of the ByteArray
		String rawImageString = request.getParameter(RAW_IMAGE);
    	ByteArray rawImage;
    	int BCurrentlyAvailable = rawImage.bytesAvailable();
    	int BWholeSize = rawImage.length(); // Put the Red5 ByteArray into a standard Java array of bytes
    	byte c[] = new byte[BWholeSize];
    	rawImage.readBytes(c);

    	// Transform the byte array into a java buffered image
    	ByteArrayInputStream db = new ByteArrayInputStream(c);

    	if(BCurrentlyAvailable > 0) {
    		System.out.println("The byte Array currently has " + BCurrentlyAvailable + " bytes. The Buffer has " + db.available());
    		try{
    			BufferedImage JavaImage = ImageIO.read(db);
    			// Now lets try and write the buffered image out to a file
    			if(JavaImage != null) { // If you sent a jpeg to the server, just change PNG to JPEG and Red5ScreenShot.png to .jpeg
    				ImageIO.write(JavaImage, "PNG", new File("Red5ScreenShot.png"));
    			}
    		}
    		catch(IOException e){
    			System.out.println("Save_ScreenShot: Writing of screenshot failed " + e); System.out.println("IO Error " + e);
    		}
    	}
    	
    	return null;
    }
    */
	
	/* for hierarchal data
	private String buildVideoRecordingsXML(List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs, Long userId){
		//start the output
		String xmlOutput = "<xml><recordings>";
		Long lastUserId = Long.valueOf("-1");
		Long currentUserId = Long.valueOf("-1");
		
		// for every user, print out its recordings
		for(VideoRecorderRecordingDTO videoRecorderRecordingDTO: videoRecorderRecordingDTOs){
			VideoRecorderUser user = videoRecorderRecordingDTO.getCreateBy();
			Set<VideoRecorderCommentDTO> comments = videoRecorderRecordingDTO.getComments();
			Set<VideoRecorderRatingDTO> ratings = videoRecorderRecordingDTO.getRatings();
			
			currentUserId = user.getUserId();
			if(currentUserId != lastUserId){
				if(lastUserId != -1){
					xmlOutput += "</userFolder>";
				}
				xmlOutput += "<userFolder>";
				xmlOutput += "<title>" + user.getFirstName() + " " + user.getLastName() + "</title>";
			}
			
			xmlOutput += "<child><recording>";
			xmlOutput += "<recordingId>" + videoRecorderRecordingDTO.getUid() + "</recordingId>";
			xmlOutput += "<title>" + videoRecorderRecordingDTO.getTitle() + "</title>";
			xmlOutput += "<userUid>" + user.getUid() + "</userUid>";
			xmlOutput += "<userId>" + user.getUserId() + "</userId>";
			xmlOutput += "<author>" + user.getFirstName() + " " + user.getLoginName() + "</author>";
			xmlOutput += "<createDate>" + videoRecorderRecordingDTO.getCreateDate().toLocaleString() + "</createDate>";
			xmlOutput += "<updateDate>" + videoRecorderRecordingDTO.getUpdateDate().toLocaleString() + "</updateDate>";
			xmlOutput += "<description>" + videoRecorderRecordingDTO.getDescription() + "</description>";
			xmlOutput += "<filename>" + videoRecorderRecordingDTO.getFilename() + "</filename>";
			xmlOutput += "<rating>" + videoRecorderRecordingDTO.getRating() + "</rating>";
			xmlOutput += "<userRating>" + buildUserRating(ratings, userId) + "</userRating>";
			
			// for every recording, print out its comments
			xmlOutput += buildVideoCommentsXML(comments);
			xmlOutput += "</recording></child>";
			
			lastUserId = currentUserId;
		}
		
		if(lastUserId == currentUserId && (lastUserId != -1 || currentUserId != -1)){
			xmlOutput += "</userFolder>";
		}
		
		xmlOutput += "</recordings></xml>";
		return xmlOutput;
	}
	*/
	
	private String buildVideoRecordingsXML(List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs, Long userId){
		//start the output
		String xmlOutput = "<recordings>";
		
		// for all recordings, print out their information
		for(VideoRecorderRecordingDTO videoRecorderRecordingDTO: videoRecorderRecordingDTOs){
			VideoRecorderUser user = videoRecorderRecordingDTO.getCreateBy();
			Set<VideoRecorderCommentDTO> comments = videoRecorderRecordingDTO.getComments();
			Set<VideoRecorderRatingDTO> ratings = videoRecorderRecordingDTO.getRatings();
			
			xmlOutput += "<recording>";
			xmlOutput += "<recordingId>" + videoRecorderRecordingDTO.getUid() + "</recordingId>";
			xmlOutput += "<title>" + videoRecorderRecordingDTO.getTitle() + "</title>";
			xmlOutput += "<userUid>" + user.getUid() + "</userUid>";
			xmlOutput += "<userId>" + user.getUserId() + "</userId>";
			xmlOutput += "<author>" + user.getFirstName() + " " + user.getLoginName() + "</author>";
			xmlOutput += "<createDate>" + videoRecorderRecordingDTO.getCreateDate().toLocaleString() + "</createDate>";
			xmlOutput += "<updateDate>" + videoRecorderRecordingDTO.getUpdateDate().toLocaleString() + "</updateDate>";
			xmlOutput += "<description>" + videoRecorderRecordingDTO.getDescription() + "</description>";
			xmlOutput += "<filename>" + videoRecorderRecordingDTO.getFilename() + "</filename>";
			xmlOutput += "<previewImage>" + "..assets/images/24-heart-gold.png" + "</previewImage>";
			xmlOutput += "<rating>" + videoRecorderRecordingDTO.getRating() + "</rating>";
			xmlOutput += "<userRating>" + buildUserRating(ratings, userId) + "</userRating>";
			xmlOutput += "<isJustSound>" + videoRecorderRecordingDTO.getIsJustSound() + "</isJustSound>";
			
			// for every recording, print out its comments
			xmlOutput += buildVideoCommentsXML(comments);
			xmlOutput += "</recording>";
		}
		
		xmlOutput += "</recordings>";
		return xmlOutput;
	}
	
	private String buildVideoCommentsXML(Set<VideoRecorderCommentDTO> comments){
		String xmlOutput = "<comments>";
		
		for(VideoRecorderCommentDTO comment: comments){
			xmlOutput += "<comment>";
			xmlOutput += "<author>" + comment.getCreateBy().getFirstName() + " " + comment.getCreateBy().getLastName() + "</author>";
			xmlOutput += "<createDate>" + comment.getCreateDate().toLocaleString() + "</createDate>";
			xmlOutput += "<text>" + comment.getText() + "</text>";
			xmlOutput += "</comment>";
		}
		
		xmlOutput += "</comments>";
		
		return xmlOutput;
	}
	
	private String buildRatingResultXML(Float rating, Float userRating){
		String xmlOutput = "<result>";
		xmlOutput += "<rating>" + rating.toString() + "</rating>";
		xmlOutput += "<userRating>" + userRating.toString() + "</userRating>";
		xmlOutput += "</result>";
		return xmlOutput;
	}
	
	private String buildUserRating(Set<VideoRecorderRatingDTO> ratings, Long userId){
		for(VideoRecorderRatingDTO rating: ratings){
			if(rating.createBy.getUid().longValue() == userId.longValue()){
				return "" + rating.rating;
			}
		}
		return "-1";
	}
	
	private float calculateRating(Set<VideoRecorderRating> ratings){
		if(ratings.isEmpty() || ratings == null){
			return 0;
		}
		else{
			float totalRating = 0;
			for(VideoRecorderRating rating: ratings){
				totalRating += rating.getRating();
			}
			totalRating = totalRating / ratings.size();
			
			return totalRating;
		}
	}
}