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

package org.lamsfoundation.lams.tool.imageGallery.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryConfigItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageVote;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryException;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageRatingForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    @Qualifier("laimagImageGalleryService")
    private IImageGalleryService igService;

    @Autowired
    @Qualifier("laimagMessageService")
    private MessageService messageService;

    /**
     * Read imageGallery data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully. This method will avoid read database again and lost un-saved resouce item lost when
     * user "refresh page",
     *
     */
    @RequestMapping("/start")
    public String start(HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	Long sessionId = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_TOOL_SESSION_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	ImageGallery imageGallery = igService.getImageGalleryBySessionId(sessionId);

	// save toolContentID into HTTPSession
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(ImageGalleryConstants.ATTR_TOOL_SESSION_ID, sessionId);

	// get back the imageGallery and item list and display them on page
	ImageGalleryUser imageGalleryUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // imageGalleryUser may be null if the user was force completed.
	    imageGalleryUser = getSpecifiedUser(igService, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    imageGalleryUser = getCurrentUser(igService, sessionId);
	}
	Integer userId = imageGalleryUser.getUserId().intValue();

	// Get contentFolderID and save to form.
	// String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	// sessionMap.put(ImageGalleryConstants.ATTR_CONTENT_FOLDER_ID, contentFolderID);

	// check whehter finish lock is on/off
	boolean lock = imageGallery.getLockWhenFinished() && imageGalleryUser.isSessionFinished();

	// get notebook entry
	String entryText = new String();
	NotebookEntry notebookEntry = igService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ImageGalleryConstants.TOOL_SIGNATURE, userId);
	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}

	// basic information
	sessionMap.put(ImageGalleryConstants.ATTR_TITLE, imageGallery.getTitle());
	sessionMap.put(ImageGalleryConstants.ATTR_INSTRUCTIONS, imageGallery.getInstructions());
	sessionMap.put(ImageGalleryConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(ImageGalleryConstants.ATTR_LOCK_ON_FINISH, imageGallery.getLockWhenFinished());
	sessionMap.put(ImageGalleryConstants.ATTR_USER_FINISHED, imageGalleryUser.isSessionFinished());
	sessionMap.put(AttributeNames.PARAM_USER_ID, userId);

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_ON, imageGallery.isReflectOnActivity());
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_INSTRUCTION, imageGallery.getReflectInstructions());
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_ENTRY, entryText);

	ImageGalleryConfigItem mediumImageDimensionsKey = igService
		.getConfigItem(ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	ImageGalleryConfigItem thumbnailImageDimensionsKey = igService
		.getConfigItem(ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	sessionMap.put(ImageGalleryConstants.ATTR_MEDIUM_IMAGE_DIMENSIONS,
		Integer.parseInt(mediumImageDimensionsKey.getConfigValue()));
	sessionMap.put(ImageGalleryConstants.ATTR_THUMBNAIL_IMAGE_DIMENSIONS,
		Integer.parseInt(thumbnailImageDimensionsKey.getConfigValue()));

	// add define later support
	if (imageGallery.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	imageGallery.setContentInUse(true);
	imageGallery.setDefineLater(false);
	igService.saveOrUpdateImageGallery(imageGallery);

	// store how many items are rated
	if (imageGallery.isAllowRank()) {
	    int countRatedImages = igService.getCountItemsRatedByUser(imageGallery.getContentId(), userId.intValue());
	    sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedImages);
	}

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, igService.isLastActivity(sessionId));

	// Create set of images, along with this filtering out items added by users from other groups
	TreeSet<ImageGalleryItem> images = new TreeSet<>(new ImageGalleryItemComparator());
	if (mode.isLearner()) {
	    Set<ImageGalleryItem> groupImages = igService.getImagesForGroup(imageGallery, sessionId);
	    for (ImageGalleryItem image : groupImages) {

		// initialize login name abd userid to avoid session close error in proxy object
		if (image.getCreateBy() != null) {
		    image.getCreateBy().getLoginName();
		}

		// remove hidden items
		if (!image.isHide()) {
		    images.add(image);
		}
	    }
	} else {
	    images.addAll(imageGallery.getImageGalleryItems());
	}

	// escape characters
	for (ImageGalleryItem image : images) {
	    String titleEscaped = HtmlUtils.htmlEscape(image.getTitle());
	    image.setTitleEscaped(titleEscaped);
	    String descriptionEscaped = StringEscapeUtils.escapeJavaScript(image.getDescription());
	    image.setDescriptionEscaped(descriptionEscaped);
	}

	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_LIST, images);
	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY, imageGallery);

	return "pages/learning/learning";
    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/finish")
    public String finish(HttpServletRequest request) {

	// get back SessionMap
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get toolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = userDTO.getUserID().longValue();

	    nextActivityUrl = igService.finishToolSession(sessionId, userID);
	    request.setAttribute(ImageGalleryConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ImageGalleryException e) {
	    LearningController.log.error("Failed get next activity url:" + e.getMessage());
	}

	return "pages/learning/finish";
    }

    /**
     * Save file or url imageGallery item into database.
     */
    @RequestMapping(path = "/saveNewImage", method = RequestMethod.POST)
    public String saveNewImage(@ModelAttribute ImageGalleryItemForm imageGalleryItemForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	try {
	    extractFormToImageGalleryItem(request, imageGalleryItemForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage(ImageGalleryConstants.ERROR_MSG_UPLOAD_FAILED,
		    new Object[] { e.getMessage() }));
	}

	if (!errorMap.isEmpty()) {
	    ServletOutputStream outputStream = response.getOutputStream();
	    StringBuilder sb = new StringBuilder();
	    Iterator it = errorMap.entrySet().iterator();
	    while (it.hasNext()) {
		MultiValueMap.Entry pair = (MultiValueMap.Entry) it.next();
		sb.append(pair.getKey() + " " + pair.getValue());
	    }
	    outputStream.print(sb.toString());
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, sb.toString());
	}

	return null;
    }

    /**
     * Save file or url imageGallery item into database.
     */
    @RequestMapping("/deleteImage")
    public String deleteImage(HttpServletRequest request, HttpServletResponse response) {

	Long imageUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);

	igService.deleteImage(sessionId, imageUid);

	// redirect
	String redirect = "redirect:/learning/start.do";
	redirect = WebUtil.appendParameterToURL(redirect, AttributeNames.ATTR_MODE, mode.toString());
	redirect = WebUtil.appendParameterToURL(redirect, AttributeNames.PARAM_TOOL_SESSION_ID, sessionId.toString());
	return redirect;
    }

    /**
     * Sets Image data to session variable, to be shown on main learning page.
     */
    @RequestMapping("/loadImageData")
    public String loadImageData(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	ImageGallery imageGallery = (ImageGallery) sessionMap.get(ImageGalleryConstants.ATTR_IMAGE_GALLERY);
	Long userId = ((Integer) sessionMap.get(AttributeNames.PARAM_USER_ID)).longValue();

	Long imageUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	ImageGalleryItem image = igService.getImageGalleryItemByUid(imageUid);
	String escapedDescription = image.getDescription().replaceAll("[\"]", "&quot;");
	image.setDescription(escapedDescription);
	sessionMap.put(ImageGalleryConstants.PARAM_CURRENT_IMAGE, image);

	// becuase in webpage will use this login name. Here is just
	// initialize it to avoid session close error in a proxy object
	ImageGalleryUser createdBy = image.getCreateBy();
	if (createdBy != null) {
	    image.getCreateBy().getLoginName();
	}

	//handle rating criterias
	int commentsMinWordsLimit = 0;
	boolean isCommentsEnabled = false;
	int countRatedImages = 0;
	Long toolSessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);

	if (imageGallery.isAllowRank()) {

	    ItemRatingDTO itemRatingDto = igService.getRatingCriteriaDtos(imageGallery.getContentId(), toolSessionId,
		    imageUid, userId);
	    sessionMap.put(AttributeNames.ATTR_ITEM_RATING_DTO, itemRatingDto);

	    if (itemRatingDto != null) {
		commentsMinWordsLimit = itemRatingDto.getCommentsMinWordsLimit();
		isCommentsEnabled = itemRatingDto.isCommentsEnabled();
	    }

	    // store how many items are rated
	    countRatedImages = igService.getCountItemsRatedByUser(imageGallery.getContentId(), userId.intValue());
	}
	sessionMap.put("commentsMinWordsLimit", commentsMinWordsLimit);
	sessionMap.put("isCommentsEnabled", isCommentsEnabled);
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedImages);

	if (imageGallery.isAllowVote()) {
	    boolean isVotedForThisImage = false;
	    ImageVote imageVote = igService.getImageVoteByImageAndUser(imageUid, userId);
	    if (imageVote != null && imageVote.isVoted()) {
		isVotedForThisImage = true;
	    }
	    sessionMap.put(ImageGalleryConstants.PARAM_IS_VOTED, isVotedForThisImage);
	}

	// set visibility of "Delete image" button
	boolean isAuthor = !image.isCreateByAuthor() && (createdBy != null) && (createdBy.getUserId().equals(userId));
	sessionMap.put(ImageGalleryConstants.PARAM_IS_AUTHOR, isAuthor);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/learning/parts/commentsarea";
    }

    /**
     * Move down current item.
     */
    @RequestMapping("/vote")
    public String vote(@ModelAttribute ImageRatingForm imageRatingForm, HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);
	Long imageUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	ImageGalleryUser imageGalleryUser = igService.getUserByIDAndSession(user.getUserID().longValue(), sessionId);

	// persist ImageGalleryItem changes in DB
	boolean formVote = imageRatingForm.getVote();
	ImageVote imageVote = igService.getImageVoteByImageAndUser(imageUid, imageGalleryUser.getUserId());
	if (imageVote == null) {
	    imageVote = new ImageVote();
	    imageVote.setCreateBy(imageGalleryUser);
	    ImageGalleryItem image = igService.getImageGalleryItemByUid(imageUid);
	    imageVote.setImageGalleryItem(image);
	}
	imageVote.setVoted(formVote);
	igService.saveOrUpdateImageVote(imageVote);

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/learning/parts/commentsarea";
    }

    /**
     * Display empty reflection form.
     */
    @RequestMapping("/newReflection")
    public String newReflection(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	reflectionForm.setUserID(user.getUserID());
	reflectionForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = igService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ImageGalleryConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    reflectionForm.setEntryText(entry.getEntry());
	}

	return "pages/learning/notebook";
    }

    /**
     * Submit reflection form input database.
     */
    @RequestMapping("/submitReflection")
    public String submitReflection(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request) {

	Integer userId = reflectionForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = igService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ImageGalleryConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    igService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ImageGalleryConstants.TOOL_SIGNATURE, userId, reflectionForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(reflectionForm.getEntryText());
	    entry.setLastModified(new Date());
	    igService.updateEntry(entry);
	}

	return finish(request);
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    private ImageGalleryUser getCurrentUser(IImageGalleryService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndSession(user.getUserID().longValue(), sessionId);

	if (imageGalleryUser == null) {
	    ImageGallerySession session = service.getImageGallerySessionBySessionId(sessionId);
	    imageGalleryUser = new ImageGalleryUser(user, session);
	    service.saveUser(imageGalleryUser);
	}
	return imageGalleryUser;
    }

    private ImageGalleryUser getSpecifiedUser(IImageGalleryService service, Long sessionId, Integer userId) {
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndSession(userId.longValue(), sessionId);
	if (imageGalleryUser == null) {
	    LearningController.log.error(
		    "Unable to find specified user for imageGallery activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return imageGalleryUser;
    }

    /**
     * Extract web form content to imageGallery item.
     */
    @SuppressWarnings("unchecked")
    private void extractFormToImageGalleryItem(HttpServletRequest request, ImageGalleryItemForm imageForm)
	    throws Exception {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(imageForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);

	ImageGallery imageGallery;
	Long toolSessionId = null;
	ImageGalleryUser user = null;
	if (mode.isLearner() || mode.isAuthor()) {
	    toolSessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);
	    imageGallery = igService.getImageGalleryBySessionId(toolSessionId);
	    user = getCurrentUser(igService, toolSessionId);

	    // monitor
	} else {
	    Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	    imageGallery = igService.getImageGalleryByContentId(contentId);
	}

	ImageGalleryItem image = new ImageGalleryItem();
	image.setCreateDate(new Timestamp(new Date().getTime()));

	File uploadDir = FileUtil.getTmpFileUploadDir(imageForm.getTmpFileUploadId());
	if (uploadDir.canRead()) {
	    File[] files = uploadDir.listFiles();
	    if (files.length == 0) {
		throw new ServletException("No image uploaded");
	    }

	    if (files.length > 1) {
		throw new ServletException("Uploaded more than 1 image while editing an Image Gallery Item");
	    }

	    igService.uploadImageGalleryItemFile(image, files[0]);
	} else {
	    throw new ServletException("Can not access upload dir");
	}

	String title = imageForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextImageTitleNumber = imageGallery.getNextImageTitle();
	    imageGallery.setNextImageTitle(nextImageTitleNumber + 1);

	    title = igService.generateNextImageTitle(nextImageTitleNumber);
	}
	image.setTitle(title);

	image.setCreateBy(user);
	image.setDescription(imageForm.getDescription());
	image.setCreateByAuthor(mode.isTeacher());
	image.setHide(false);

	// setting SequenceId
	Set<ImageGalleryItem> imageList = imageGallery.getImageGalleryItems();
	int maxSeq = 0;
	for (ImageGalleryItem dbImage : imageList) {
	    if (dbImage.getSequenceId() > maxSeq) {
		maxSeq = dbImage.getSequenceId();
	    }
	}
	maxSeq++;
	image.setSequenceId(maxSeq);

	imageList.add(image);
	igService.saveOrUpdateImageGallery(imageGallery);

	igService.saveOrUpdateImageGalleryItem(image);

	// notify teachers
	if (mode.isLearner() && imageGallery.isNotifyTeachersOnImageSumbit()) {
	    igService.notifyTeachersOnImageSumbit(toolSessionId, user);
	}
    }
}
