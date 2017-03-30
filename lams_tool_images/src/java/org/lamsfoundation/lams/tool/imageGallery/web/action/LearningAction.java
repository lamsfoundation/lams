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


package org.lamsfoundation.lams.tool.imageGallery.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
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
import org.lamsfoundation.lams.tool.imageGallery.service.UploadImageGalleryFileException;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryUtils;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageRatingForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.MultipleImagesForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	String param = mapping.getParameter();
	// -----------------------ImageGallery Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("saveNewImage")) {
	    return saveNewImage(mapping, form, request, response);
	}
	if (param.equals("saveMultipleImages")) {
	    return saveMultipleImages(mapping, form, request, response);
	}
	if (param.equals("deleteImage")) {
	    return deleteImage(mapping, form, request, response);
	}

	// ================ Comments =======================
	if (param.equals("loadImageData")) {
	    return loadImageData(mapping, form, request, response);
	}
	if (param.equals("vote")) {
	    return vote(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(ImageGalleryConstants.ERROR);
    }

    /**
     * Read imageGallery data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully. This method will avoid read database again and lost un-saved resouce item lost when
     * user "refresh page",
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	Long sessionId = new Long(request.getParameter(ImageGalleryConstants.PARAM_TOOL_SESSION_ID));
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	IImageGalleryService service = getImageGalleryService();
	ImageGallery imageGallery = service.getImageGalleryBySessionId(sessionId);

	// save toolContentID into HTTPSession
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(ImageGalleryConstants.ATTR_TOOL_SESSION_ID, sessionId);

	// get back the imageGallery and item list and display them on page
	ImageGalleryUser imageGalleryUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // imageGalleryUser may be null if the user was force completed.
	    imageGalleryUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    imageGalleryUser = getCurrentUser(service, sessionId);
	}
	Integer userId = imageGalleryUser.getUserId().intValue();

	// Get contentFolderID and save to form.
	// String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	// sessionMap.put(ImageGalleryConstants.ATTR_CONTENT_FOLDER_ID, contentFolderID);

	// check whehter finish lock is on/off
	boolean lock = imageGallery.getLockWhenFinished() && imageGalleryUser.isSessionFinished();

	// get notebook entry
	String entryText = new String();
	NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
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

	ImageGalleryConfigItem mediumImageDimensionsKey = service
		.getConfigItem(ImageGalleryConfigItem.KEY_MEDIUM_IMAGE_DIMENSIONS);
	ImageGalleryConfigItem thumbnailImageDimensionsKey = service
		.getConfigItem(ImageGalleryConfigItem.KEY_THUMBNAIL_IMAGE_DIMENSIONS);
	sessionMap.put(ImageGalleryConstants.ATTR_MEDIUM_IMAGE_DIMENSIONS,
		Integer.parseInt(mediumImageDimensionsKey.getConfigValue()));
	sessionMap.put(ImageGalleryConstants.ATTR_THUMBNAIL_IMAGE_DIMENSIONS,
		Integer.parseInt(thumbnailImageDimensionsKey.getConfigValue()));

	// add define later support
	if (imageGallery.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	imageGallery.setContentInUse(true);
	imageGallery.setDefineLater(false);
	service.saveOrUpdateImageGallery(imageGallery);

	// store how many items are rated
	if (imageGallery.isAllowRank()) {
	    int countRatedImages = service.getCountItemsRatedByUser(imageGallery.getContentId(), userId.intValue());
	    sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedImages);
	}

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// Create set of images, along with this filtering out items added by users from other groups
	TreeSet<ImageGalleryItem> images = new TreeSet<ImageGalleryItem>(new ImageGalleryItemComparator());
	if (mode.isLearner()) {
	    Set<ImageGalleryItem> groupImages = service.getImagesForGroup(imageGallery, sessionId);
	    for (ImageGalleryItem image : groupImages) {

		// initialize login name to avoid session close error in proxy object
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
	    String titleEscaped = StringEscapeUtils.escapeJavaScript(image.getTitle());
	    image.setTitleEscaped(titleEscaped);
	    String descriptionEscaped = StringEscapeUtils.escapeJavaScript(image.getDescription());
	    image.setDescriptionEscaped(descriptionEscaped);
	}

	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_LIST, images);
	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY, imageGallery);

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	// get toolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IImageGalleryService service = getImageGalleryService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(userDTO.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(ImageGalleryConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ImageGalleryException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Save file or url imageGallery item into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    private ActionForward saveNewImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	ImageGalleryItemForm itemForm = (ImageGalleryItemForm) form;
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(itemForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	
	//validate form
	boolean isLargeFilesAllowed = mode.isTeacher();
	ActionErrors errors = ImageGalleryUtils.validateImageGalleryItem(itemForm, isLargeFilesAllowed);

	try {
	    if (errors.isEmpty()) {
		extractFormToImageGalleryItem(request, itemForm);
	    }
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(ImageGalleryConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	}

	if (!errors.isEmpty()) {
	    ServletOutputStream outputStream = response.getOutputStream();
	    outputStream.print(errors.get().next().toString());
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	return null;
    }

    /**
     * Save file or url imageGallery item into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    private ActionForward saveMultipleImages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	MultipleImagesForm multipleForm = (MultipleImagesForm) form;
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(multipleForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	
	//validate form
	boolean isLargeFilesAllowed = mode.isTeacher();
	ActionErrors errors = ImageGalleryUtils.validateMultipleImages(multipleForm, isLargeFilesAllowed);

	try {
	    if (errors.isEmpty()) {
		extractMultipleFormToImageGalleryItems(request, multipleForm);
	    }
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(ImageGalleryConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	}

	if (!errors.isEmpty()) {
	    ServletOutputStream outputStream = response.getOutputStream();
	    outputStream.print(errors.get().next().toString());
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	return null;
    }

    /**
     * Save file or url imageGallery item into database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward deleteImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IImageGalleryService service = getImageGalleryService();

	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);

	service.deleteImage(sessionId, imageUid);

	// redirect
	ForwardConfig redirectConfig = mapping.findForwardConfig(ImageGalleryConstants.SUCCESS);
	ActionRedirect redirect = new ActionRedirect(redirectConfig);
	redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return redirect;
    }

    /**
     * Sets Image data to session variable, to be shown on main learning page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward loadImageData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	IImageGalleryService service = getImageGalleryService();
	ImageGallery imageGallery = (ImageGallery) sessionMap.get(ImageGalleryConstants.ATTR_IMAGE_GALLERY);
	Long userId = ((Integer) sessionMap.get(AttributeNames.PARAM_USER_ID)).longValue();

	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	ImageGalleryItem image = service.getImageGalleryItemByUid(imageUid);
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
	if (imageGallery.isAllowRank()) {

	    ItemRatingDTO itemRatingDto = service.getRatingCriteriaDtos(imageGallery.getContentId(), imageUid, userId);
	    sessionMap.put(AttributeNames.ATTR_ITEM_RATING_DTO, itemRatingDto);

	    if (itemRatingDto != null) {
		commentsMinWordsLimit = itemRatingDto.getCommentsMinWordsLimit();
		isCommentsEnabled = itemRatingDto.isCommentsEnabled();
	    }

	    // store how many items are rated
	    countRatedImages = service.getCountItemsRatedByUser(imageGallery.getContentId(), userId.intValue());
	}
	sessionMap.put("commentsMinWordsLimit", commentsMinWordsLimit);
	sessionMap.put("isCommentsEnabled", isCommentsEnabled);
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedImages);

	if (imageGallery.isAllowVote()) {
	    boolean isVotedForThisImage = false;
	    ImageVote imageVote = service.getImageVoteByImageAndUser(imageUid, userId);
	    if (imageVote != null && imageVote.isVoted()) {
		isVotedForThisImage = true;
	    }
	    sessionMap.put(ImageGalleryConstants.PARAM_IS_VOTED, isVotedForThisImage);
	}

	// set visibility of "Delete image" button
	boolean isAuthor = !image.isCreateByAuthor() && (createdBy != null) && (createdBy.getUserId().equals(userId));
	sessionMap.put(ImageGalleryConstants.PARAM_IS_AUTHOR, isAuthor);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Move down current item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward vote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);
	IImageGalleryService service = getImageGalleryService();
	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),
		sessionId);

	// persist ImageGalleryItem changes in DB
	boolean formVote = ((ImageRatingForm) form).getVote();
	ImageVote imageVote = service.getImageVoteByImageAndUser(imageUid, imageGalleryUser.getUserId());
	if (imageVote == null) {
	    imageVote = new ImageVote();
	    imageVote.setCreateBy(imageGalleryUser);
	    ImageGalleryItem image = service.getImageGalleryItemByUid(imageUid);
	    imageVote.setImageGalleryItem(image);
	}
	imageVote.setVoted(formVote);
	service.saveOrUpdateImageVote(imageVote);

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Display empty reflection form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IImageGalleryService submitFilesService = getImageGalleryService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ImageGalleryConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IImageGalleryService service = getImageGalleryService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ImageGalleryConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ImageGalleryConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************
    private IImageGalleryService getImageGalleryService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.IMAGE_GALLERY_SERVICE);
    }

    private ImageGalleryUser getCurrentUser(IImageGalleryService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),
		sessionId);

	if (imageGalleryUser == null) {
	    ImageGallerySession session = service.getImageGallerySessionBySessionId(sessionId);
	    imageGalleryUser = new ImageGalleryUser(user, session);
	    service.saveUser(imageGalleryUser);
	}
	return imageGalleryUser;
    }

    private ImageGalleryUser getSpecifiedUser(IImageGalleryService service, Long sessionId, Integer userId) {
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (imageGalleryUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for imageGallery activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return imageGalleryUser;
    }

    /**
     * Extract web form content to imageGallery item.
     *
     * @param request
     * @param imageForm
     * @throws ImageGalleryException
     */
    private void extractFormToImageGalleryItem(HttpServletRequest request, ImageGalleryItemForm imageForm)
	    throws Exception {
	IImageGalleryService service = getImageGalleryService();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(imageForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	
	ImageGallery imageGallery;
	Long toolSessionId = null;
	ImageGalleryUser user = null;
	if (mode.isLearner() || mode.isAuthor()) {
	    toolSessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);
	    imageGallery = service.getImageGalleryBySessionId(toolSessionId);
	    user = getCurrentUser(service, toolSessionId);

	// monitor
	} else {
	    Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	    imageGallery = service.getImageGalleryByContentId(contentId);
	}

	ImageGalleryItem image = new ImageGalleryItem();
	image.setCreateDate(new Timestamp(new Date().getTime()));

	// upload ImageGalleryItem file
	// and setting file properties' fields: item.setFileUuid(); item.setFileVersionId(); item.setFileType();
	// item.setFileName();
	if (imageForm.getFile() != null) {
	    try {
		service.uploadImageGalleryItemFile(image, imageForm.getFile());
	    } catch (UploadImageGalleryFileException e) {
		// remove new image!
		throw e;
	    }
	}

	String title = imageForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextImageTitleNumber = imageGallery.getNextImageTitle();
	    imageGallery.setNextImageTitle(nextImageTitleNumber + 1);

	    title = service.generateNextImageTitle(nextImageTitleNumber);
	}
	image.setTitle(title);
	
	image.setCreateBy(user);
	image.setDescription(imageForm.getDescription());
	image.setCreateByAuthor(false);
	image.setHide(false);
	if (mode.isTeacher()) {
	    image.setCreateByAuthor(true);
	}

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
	service.saveOrUpdateImageGallery(imageGallery);

	service.saveOrUpdateImageGalleryItem(image);

	// notify teachers
	if (mode.isLearner() && imageGallery.isNotifyTeachersOnImageSumbit()) {
	    service.notifyTeachersOnImageSumbit(toolSessionId, user);
	}
    }

    /**
     * Extract web form content to imageGallery items.
     *
     * @param request
     * @param multipleForm
     * @throws ImageGalleryException
     */
    private void extractMultipleFormToImageGalleryItems(HttpServletRequest request, MultipleImagesForm multipleForm)
	    throws Exception {

	List<FormFile> fileList = ImageGalleryUtils.createFileListFromMultipleForm(multipleForm);
	for (FormFile file : fileList) {
	    ImageGalleryItemForm imageForm = new ImageGalleryItemForm();
	    imageForm.setSessionMapID(multipleForm.getSessionMapID());
	    imageForm.setTitle("");
	    imageForm.setDescription("");
	    imageForm.setFile(file);
	    extractFormToImageGalleryItem(request, imageForm);
	}
    }

}
