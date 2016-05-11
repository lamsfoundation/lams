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


package org.lamsfoundation.lams.tool.videoRecorder.web.actions;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderCommentDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRatingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderRecordingComparator;
import org.lamsfoundation.lams.util.Base64StringToImageUtil;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Paul Georges
 * 
 *         ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/videoRecorderActions"
 *                parameter="method"
 *                validate="false"
 * 
 *                ----------------XDoclet Tags--------------------
 */
public class VideoRecorderAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(VideoRecorderAction.class);

    //---------------------------------------------------------------------
    private static final String TOOL_SESSION_ID = "toolSessionId";
    private static final String TOOL_CONTENT_ID = "toolContentId";
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
    private static final String IS_LOCAL = "isLocal";
    private static final String SORT_BY = "sortBy";
    private static final String SORT_DIR = "sortDirection";
    private static final String GET_ALL = "getAll";
    private static final String OK_MSG = "ok";
    private static final String ERROR_MSG = "error";
    private static final String EXTENSTION = "ext";
    private static final String IMAGEDATA = "data";
    private static final String DIR = "dir";

    private static final String FLV_EXTENSION = ".flv";
    private static final String JPG_EXTENSION = ".jpg";

    /**
     * Desctipion
     *
     * Input parameters: toolSessionID, userID (possibly null)
     * 
     * Output format: xml
     */
    public ActionForward getRecordingsByToolSessionIdAndUserId(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	// get paramaters from POST
	Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
	Long toolContentId = WebUtil.readLongParam(request, TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, USER_ID);
	String sortBy = WebUtil.readStrParam(request, SORT_BY);
	String sortDirection = WebUtil.readStrParam(request, SORT_DIR);
	boolean getAll = WebUtil.readBooleanParam(request, GET_ALL);

	// get service
	IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		.getVideoRecorderService(getServlet().getServletContext());

	// get session
	VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);

	// create list of recording DTOs
	List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs;

	// if no user is specified
	if (getAll) {
	    // get all recordings
	    videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionId(toolSessionId,
		    toolContentId);
	    // otherwise
	} else {
	    // get all recordings from user specified
	    videoRecorderRecordingDTOs = videoRecorderService.getRecordingsByToolSessionIdAndUserUid(toolSessionId,
		    userId, toolContentId);
	}

	// sort the list of recording DTOs in order to create the xml correctly
	Comparator<VideoRecorderRecordingDTO> comp = new VideoRecorderRecordingComparator(sortBy, sortDirection);
	Collections.sort(videoRecorderRecordingDTOs, comp);

	String contentFolderIdLearner = session.getContentFolderId();
	String previewImageLearnerPath = Configuration.get(ConfigurationKeys.SERVER_URL) + File.separator + "www"
		+ File.separator + "secure" + File.separator + contentFolderIdLearner + File.separator + "previewImages"
		+ File.separator;

	WebApplicationContext ctx = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	IBaseDAO baseDAO = (IBaseDAO) ctx.getBean("baseDAO");
	ToolSession toolSession = (ToolSession) baseDAO.find(ToolSession.class, toolSessionId);
	String contentFolderIdAuthor = toolSession.getLesson().getLearningDesign().getContentFolderID();
	String previewImageAuthorPath = Configuration.get(ConfigurationKeys.SERVER_URL) + File.separator + "www"
		+ File.separator + "secure" + File.separator + contentFolderIdAuthor + File.separator + "Recordings"
		+ File.separator;

	String soundOnlyImage = Configuration.get(ConfigurationKeys.SERVER_URL) + File.separator + "tool"
		+ File.separator + "lavidr10" + File.separator + "images" + File.separator + "soundOnly.png";

	String xmlOutput = buildVideoRecordingsXML(videoRecorderRecordingDTOs, userId, previewImageLearnerPath,
		previewImageAuthorPath, soundOnlyImage);

	response.setContentType("text/xml");
	response.setCharacterEncoding("UTF-8");
	writeAJAXResponse(response, xmlOutput);
	return null;
    }

    public ActionForward saveRecording(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	try {
	    // get paramaters from POST
	    Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID, true);
	    Long toolContentId = WebUtil.readLongParam(request, TOOL_CONTENT_ID, true);
	    Long recordingId = WebUtil.readLongParam(request, RECORDING_ID, true);
	    Long userId = WebUtil.readLongParam(request, USER_ID, true);
	    String title = WebUtil.readStrParam(request, TITLE, true);
	    String description = WebUtil.readStrParam(request, DESCRIPTION, true);
	    String filename = WebUtil.readStrParam(request, FILENAME, true);
	    Boolean isJustSound = WebUtil.readBooleanParam(request, IS_JUST_SOUND, false);
	    Boolean isLocal = WebUtil.readBooleanParam(request, IS_LOCAL, false);
	    int rating = WebUtil.readIntParam(request, RATING, false);

	    title = title.replaceAll("[<>\"]", "");
	    description = description.replaceAll("[<>\"]", "");

	    // get service
	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());

	    // initialize session, user and recording
	    VideoRecorderSession session = null;
	    VideoRecorderUser user = null;
	    VideoRecorderRecording videoRecording = null;
	    VideoRecorder videoRecorder = null;

	    // if saving from author
	    if (toolContentId != -1) {
		// get video recorder
		videoRecorder = videoRecorderService.getVideoRecorderByContentId(toolContentId);

		if (videoRecorder != null) {
		    // get recording
		    videoRecording = videoRecorderService.getFirstRecordingByToolContentId(toolContentId);

		    // if no recording exists create a new one
		    if (videoRecording == null) {
			videoRecording = new VideoRecorderRecording();

			videoRecording.setFilename(filename);
			videoRecording.setRating((float) rating);
			videoRecording.setCreateDate(new Date());
			videoRecording.setIsJustSound(isJustSound);
			videoRecording.setIsLocal(isLocal);
			videoRecording.setRatings(null);
			videoRecording.setComments(null);
			videoRecording.setCreateBy(null);
			videoRecording.setToolContentId(toolContentId);
		    }
		}

	    }
	    // if saving from learner
	    else if (toolSessionId != -1) {
		// get session
		session = videoRecorderService.getSessionBySessionId(toolSessionId);

		// get user
		user = videoRecorderService.getUserByUID(userId);

		// if no recording id is specified
		if (recordingId == -1) {
		    // create a new recording
		    videoRecording = new VideoRecorderRecording();
		    videoRecording.setFilename(filename);
		    videoRecording.setRating((float) rating);
		    videoRecording.setCreateDate(new Date());
		    videoRecording.setIsJustSound(isJustSound);
		    videoRecording.setIsLocal(isLocal);
		    videoRecording.setRatings(null);
		    videoRecording.setComments(null);
		    videoRecording.setCreateBy(user);
		    videoRecording.setVideoRecorderSession(session);
		}
		// otherwise get the recording from the DAO
		else {
		    videoRecording = videoRecorderService.getRecordingById(recordingId);
		}
	    }

	    // add the last common information
	    videoRecording.setUpdateDate(new Date());
	    videoRecording.setTitle(title);
	    videoRecording.setDescription(description);
	    videoRecording.setFilename(filename);

	    // save
	    videoRecorderService.saveOrUpdateVideoRecorderRecording(videoRecording);

	    writeAJAXResponse(response, OK_MSG);

	} catch (Exception e) {
	    writeAJAXResponse(response, ERROR_MSG + e.getMessage());
	}

	return null;
    }

    public ActionForward deleteRecording(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	try {
	    // get paramaters from POST
	    Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);

	    // get service
	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());

	    // get videoRecording
	    VideoRecorderRecording videoRecording = videoRecorderService.getRecordingById(recordingId);

	    // delete the videoRecording
	    videoRecorderService.deleteVideoRecorderRecording(videoRecording);

	    writeAJAXResponse(response, OK_MSG);

	} catch (Exception e) {
	    writeAJAXResponse(response, ERROR_MSG + e.getMessage());
	}

	return null;
    }

    public ActionForward saveComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	try {
	    Long commentId = WebUtil.readLongParam(request, COMMENT_ID);
	    Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);
	    Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
	    Long userId = WebUtil.readLongParam(request, USER_ID);
	    String commentText = WebUtil.readStrParam(request, COMMENT, true);

	    commentText = commentText.replaceAll("[<>\"]", "");

	    // get service
	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());

	    // create new comment
	    VideoRecorderComment comment;

	    // get user
	    VideoRecorderUser user = videoRecorderService.getUserByUID(userId);

	    // get recording
	    VideoRecorderRecording recording = videoRecorderService.getRecordingById(recordingId);

	    // get session
	    VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);

	    // create a new comment
	    if (commentId == -1) {
		comment = new VideoRecorderComment();
		comment.setCreateBy(user);
		comment.setRecording(recording);
		comment.setCreateDate(new Date());
		comment.setText(commentText);
		comment.setVideoRecorderSession(session);
	    }
	    // otherwise get the comment from the DAO
	    else {
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
	    Set<VideoRecorderCommentDTO> commentDTOs = VideoRecorderCommentDTO
		    .getVideoRecorderCommentDTOs(recording.getComments());

	    // return a response with all comments for current recording
	    response.setContentType("text/xml");
	    response.setCharacterEncoding("UTF-8");
	    writeAJAXResponse(response, buildVideoCommentsXML(commentDTOs));
	} catch (Exception e) {
	    writeAJAXResponse(response, ERROR_MSG + e.getMessage());
	}

	return null;
    }

    public ActionForward saveRating(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	try {
	    Long ratingId = WebUtil.readLongParam(request, RATING_ID);
	    Long recordingId = WebUtil.readLongParam(request, RECORDING_ID);
	    Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID);
	    Long userId = WebUtil.readLongParam(request, USER_ID);
	    float ratingFloat = Float.valueOf(WebUtil.readStrParam(request, RATING));

	    // get service
	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());

	    // create new rating
	    VideoRecorderRating rating;

	    // get user
	    VideoRecorderUser user = videoRecorderService.getUserByUID(userId);

	    // get recording
	    VideoRecorderRecording recording = videoRecorderService.getRecordingById(recordingId);

	    // get session
	    VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);

	    // create new rating
	    if (ratingId == -1) {
		rating = new VideoRecorderRating();
		rating.setCreateBy(user);
		rating.setRecording(recording);
		rating.setCreateDate(new Date());
		rating.setRating(ratingFloat);
		rating.setVideoRecorderSession(session);
	    }
	    // otherwise get the rating from the DAO
	    else {
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

	    response.setContentType("text/xml");
	    response.setCharacterEncoding("UTF-8");
	    writeAJAXResponse(response, buildRatingResultXML(recording.getRating(), ratingFloat));
	} catch (Exception e) {
	    writeAJAXResponse(response, ERROR_MSG + e.getMessage());
	}

	return null;
    }

    public ActionForward getLanguageXML(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// get service
	try {
	    response.setContentType("text/xml");
	    response.setCharacterEncoding("UTF-8");
	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());
	    writeAJAXResponse(response, videoRecorderService.getLanguageXML());
	} catch (Exception e) {
	    writeAJAXResponse(response, ERROR_MSG + e.getMessage());
	}

	return null;

    }

    public ActionForward saveImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	try {
	    String dir = WebUtil.readStrParam(request, DIR, true);
	    String filename = WebUtil.readStrParam(request, FILENAME);
	    String ext = WebUtil.readStrParam(request, EXTENSTION);
	    String data = WebUtil.readStrParam(request, IMAGEDATA);
	    Long toolSessionId = WebUtil.readLongParam(request, TOOL_SESSION_ID, true);

	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());

	    // if this is being added by a learner
	    if (toolSessionId != null) {
		VideoRecorderSession session = videoRecorderService.getSessionBySessionId(toolSessionId);
		String folder = session.getContentFolderId();
		dir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + "lams-www.war"
			+ File.separator + "secure" + File.separator + folder + File.separator + "previewImages"
			+ File.separator;
	    }

	    // at this point, the dir variable should contain a path to the learning design content folder (author)
	    // or a path to the session content folder (learner)
	    if (dir != null) {
		boolean success = Base64StringToImageUtil.create(dir, filename, ext, data);
		writeAJAXResponse(response, String.valueOf(success));
	    } else {
		writeAJAXResponse(response, ERROR_MSG);
	    }
	} catch (Exception e) {
	    writeAJAXResponse(response, ERROR_MSG + e.getMessage());
	}

	return null;

    }

    public ActionForward getLanguageXMLForFCK(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// get service
	try {
	    IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		    .getVideoRecorderService(getServlet().getServletContext());
	    response.setContentType("text/xml");
	    response.setCharacterEncoding("UTF-8");
	    writeAJAXResponse(response, videoRecorderService.getLanguageXMLForFCK());
	} catch (Exception e) {
	    writeAJAXResponse(response, "");
	}

	return null;

    }

    private String buildVideoRecordingsXML(List<VideoRecorderRecordingDTO> videoRecorderRecordingDTOs, Long userId,
	    String previewImageLearnerPath, String previewImageAuthorPath, String soundOnlyImage) {
	//start the output
	String xmlOutput = "<recordings>";

	// get service
	IVideoRecorderService videoRecorderService = VideoRecorderServiceProxy
		.getVideoRecorderService(getServlet().getServletContext());

	// for all recordings, print out their information
	for (VideoRecorderRecordingDTO videoRecorderRecordingDTO : videoRecorderRecordingDTOs) {
	    VideoRecorderUser user = videoRecorderRecordingDTO.getCreateBy();
	    Set<VideoRecorderCommentDTO> comments = videoRecorderRecordingDTO.getComments();

	    Set<VideoRecorderRatingDTO> ratings = videoRecorderRecordingDTO.getRatings();

	    xmlOutput += "<recording>";
	    xmlOutput += "<recordingId>" + videoRecorderRecordingDTO.getUid() + "</recordingId>";
	    xmlOutput += "<title>" + videoRecorderRecordingDTO.getTitle() + "</title>";

	    // user stuff
	    if (user != null) {
		xmlOutput += "<isToolContent>false</isToolContent>";
		xmlOutput += "<userUid>" + user.getUid() + "</userUid>";
		xmlOutput += "<userId>" + user.getUserId() + "</userId>";
		xmlOutput += "<author>" + user.getFirstName() + " " + user.getLoginName() + "</author>";
	    } else if (user == null && videoRecorderRecordingDTO.getToolContentId() != -1) {
		xmlOutput += "<isToolContent>true</isToolContent>";
		xmlOutput += "<author>" + videoRecorderService.getMessage("videorecorder.instructor") + "</author>";
	    }

	    xmlOutput += "<createDate>" + videoRecorderRecordingDTO.getCreateDate().toLocaleString() + "</createDate>";
	    xmlOutput += "<updateDate>" + videoRecorderRecordingDTO.getUpdateDate().toLocaleString() + "</updateDate>";
	    xmlOutput += "<description>" + videoRecorderRecordingDTO.getDescription() + "</description>";
	    xmlOutput += "<filename>" + videoRecorderRecordingDTO.getFilename() + FLV_EXTENSION + "</filename>";

	    // preview image stuff
	    if (videoRecorderRecordingDTO.getIsJustSound()) {
		xmlOutput += "<previewImage>" + soundOnlyImage + "</previewImage>";
	    } else {
		if (user != null) {
		    xmlOutput += "<previewImage>" + previewImageLearnerPath + videoRecorderRecordingDTO.getFilename()
			    + JPG_EXTENSION + "</previewImage>";
		} else if (user == null && videoRecorderRecordingDTO.getToolContentId() != -1) {
		    xmlOutput += "<previewImage>" + previewImageAuthorPath + videoRecorderRecordingDTO.getFilename()
			    + JPG_EXTENSION + "</previewImage>";
		}
	    }
	    xmlOutput += "<rating>" + videoRecorderRecordingDTO.getRating() + "</rating>";
	    xmlOutput += "<userRating>" + buildUserRating(ratings, userId) + "</userRating>";
	    xmlOutput += "<isJustSound>" + videoRecorderRecordingDTO.getIsJustSound() + "</isJustSound>";
	    xmlOutput += "<isLocal>" + videoRecorderRecordingDTO.getIsLocal() + "</isLocal>";

	    // for every recording, print out its comments
	    xmlOutput += buildVideoCommentsXML(comments);
	    xmlOutput += "</recording>";
	}

	xmlOutput += "</recordings>";
	return xmlOutput;
    }

    private String buildVideoCommentsXML(Set<VideoRecorderCommentDTO> comments) {
	String xmlOutput = "<comments>";

	VideoRecorderCommentDTO[] commentArray = comments.toArray(new VideoRecorderCommentDTO[comments.size()]);

	//for(VideoRecorderCommentDTO comment: comments){
	for (int i = commentArray.length - 1; i >= 0; i--) {
	    VideoRecorderCommentDTO comment = commentArray[i];
	    xmlOutput += "<comment>";
	    xmlOutput += "<author>" + comment.getCreateBy().getFirstName() + " " + comment.getCreateBy().getLastName()
		    + "</author>";
	    xmlOutput += "<createDate>" + comment.getCreateDate().toLocaleString() + "</createDate>";
	    xmlOutput += "<text>" + comment.getText() + "</text>";
	    xmlOutput += "</comment>";
	}

	xmlOutput += "</comments>";

	return xmlOutput;
    }

    private String buildRatingResultXML(Float rating, Float userRating) {
	String xmlOutput = "<result>";
	xmlOutput += "<rating>" + rating.toString() + "</rating>";
	xmlOutput += "<userRating>" + userRating.toString() + "</userRating>";
	xmlOutput += "</result>";
	return xmlOutput;
    }

    private String buildUserRating(Set<VideoRecorderRatingDTO> ratings, Long userId) {
	for (VideoRecorderRatingDTO rating : ratings) {
	    if (rating.createBy.getUid().longValue() == userId.longValue()) {
		return "" + rating.rating;
	    }
	}
	return "-1";
    }

    private float calculateRating(Set<VideoRecorderRating> ratings) {
	if (ratings.isEmpty() || ratings == null) {
	    return 0;
	} else {
	    float totalRating = 0;
	    for (VideoRecorderRating rating : ratings) {
		totalRating += rating.getRating();
	    }
	    totalRating = totalRating / ratings.size();

	    return totalRating;
	}
    }
}