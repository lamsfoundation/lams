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

package org.lamsfoundation.lams.tool.imageGallery.web.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.MultipleImagesForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
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

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    @Qualifier("laimagImageGalleryService")
    private IImageGalleryService imageGalleryService;

    @Autowired
    @Qualifier("laimagMessageService")
    private MessageService messageService;

    /**
     * Read imageGallery data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     */
    @RequestMapping("/start")
    public String start(@ModelAttribute ImageGalleryForm imageGalleryForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return readDatabaseData(imageGalleryForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String defineLater(@ModelAttribute ImageGalleryForm imageGalleryForm, HttpServletRequest request)
	    throws ServletException {

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ImageGallery imageGallery = imageGalleryService.getImageGalleryByContentId(contentId);

	imageGallery.setDefineLater(true);
	imageGalleryService.saveOrUpdateImageGallery(imageGallery);

	//audit log the teacher has started editing activity in monitor
	imageGalleryService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return readDatabaseData(imageGalleryForm, request);
    }

    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(ImageGalleryForm imageGalleryForm, HttpServletRequest request)
	    throws ServletException {
	// save toolContentID into HTTPSession
	Long contentId = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_TOOL_CONTENT_ID);

	List<ImageGalleryItem> items = null;
	ImageGallery imageGallery = null;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	imageGalleryForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	imageGalleryForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    imageGallery = imageGalleryService.getImageGalleryByContentId(contentId);
	    // if imageGallery does not exist, try to use default content instead.
	    if (imageGallery == null) {
		imageGallery = imageGalleryService.getDefaultContent(contentId);
		if (imageGallery.getImageGalleryItems() != null) {
		    items = new ArrayList<>(imageGallery.getImageGalleryItems());
		    imageGalleryService.fillImageDisplayUuid(items);
		} else {
		    items = null;
		}
	    } else {
		items = imageGalleryService.getAuthoredItems(imageGallery.getUid());
	    }

	    imageGalleryForm.setImageGallery(imageGallery);
	    imageGalleryForm.setAllowRatingsOrVote(imageGallery.isAllowVote() || imageGallery.isAllowRank());
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<>();
	} else {
	    ImageGalleryUser imageGalleryUser = null;
	    // handle system default question: createBy is null, now set it to current user
	    for (ImageGalleryItem item : items) {
		if (item.getCreateBy() == null) {
		    if (imageGalleryUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			imageGalleryUser = new ImageGalleryUser(user, imageGallery);
		    }
		    item.setCreateBy(imageGalleryUser);
		}
	    }
	}
	// init imageGallery item list
	SortedSet<ImageGalleryItem> imageGalleryItemList = getImageList(sessionMap);
	imageGalleryItemList.clear();
	imageGalleryItemList.addAll(items);

	// get rating criterias from DB
	List<RatingCriteria> ratingCriterias = imageGalleryService.getRatingCriterias(contentId);
	sessionMap.put(AttributeNames.ATTR_RATING_CRITERIAS, ratingCriterias);

	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY_FORM, imageGalleryForm);
	sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, imageGallery.getNextImageTitle());
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	request.setAttribute("startForm", imageGalleryForm);
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping(path = "/init", method = RequestMethod.POST)
    public String initPage(@ModelAttribute ImageGalleryForm startForm, HttpServletRequest request)
	    throws ServletException {

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	ImageGalleryForm existForm = (ImageGalleryForm) sessionMap.get(ImageGalleryConstants.ATTR_IMAGE_GALLERY_FORM);

	try {
	    PropertyUtils.copyProperties(startForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	request.setAttribute("imageGalleryForm", startForm);
	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all imageGallery item, information etc.
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute ImageGalleryForm imageGalleryForm, HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException, Exception {

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(imageGalleryForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	ImageGallery imageGallery = imageGalleryForm.getImageGallery();
	Long contentId = imageGalleryForm.getImageGallery().getContentId();

	// **********************************Get ImageGallery PO*********************
	ImageGallery imageGalleryPO = imageGalleryService.getImageGalleryByContentId(contentId);
	if (imageGalleryPO == null) {
	    // new ImageGallery, create it.
	    imageGalleryPO = imageGallery;
	    imageGalleryPO.setCreated(new Timestamp(new Date().getTime()));
	    imageGalleryPO.setUpdated(new Timestamp(new Date().getTime()));

	} else {

	    Long uid = imageGalleryPO.getUid();
	    PropertyUtils.copyProperties(imageGalleryPO, imageGallery);
	    // get back UID
	    imageGalleryPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		imageGalleryPO.setDefineLater(false);
	    }
	    imageGalleryPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle nextImageTitle*******************
	Long nextConsecutiveImageTitle = (Long) sessionMap.get(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE);
	imageGalleryPO.setNextImageTitle(nextConsecutiveImageTitle);

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ImageGalleryUser imageGalleryUser = imageGalleryService.getUserByIDAndContent(user.getUserID().longValue(),
		contentId);
	if (imageGalleryUser == null) {
	    imageGalleryUser = new ImageGalleryUser(user, imageGalleryPO);
	}

	imageGalleryPO.setCreatedBy(imageGalleryUser);

	// ************************* Handle imageGallery allowRank item *******************
	imageGalleryPO.setAllowRank(imageGalleryForm.isAllowRatingsOrVote() && !imageGalleryPO.isAllowVote());
	if (!imageGalleryPO.isAllowRank()) {
	    imageGalleryPO.setMaximumRates(0);
	    imageGalleryPO.setMinimumRates(0);
	}

	// ************************* Handle imageGallery items *******************
	// Handle imageGallery items
	Set<ImageGalleryItem> itemList = new LinkedHashSet<>();
	SortedSet<ImageGalleryItem> imageList = getImageList(sessionMap);
	Iterator<ImageGalleryItem> iter = imageList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem item = iter.next();
	    if (item != null) {
		// This flushs user UID info to message if this user is a new user.
		item.setCreateBy(imageGalleryUser);
		itemList.add(item);
	    }
	}
	imageGalleryPO.setImageGalleryItems(itemList);
	// delete instructions file from database.
	List<ImageGalleryItem> delImageGalleryItemList = getDeletedImageGalleryItemList(sessionMap);
	iter = delImageGalleryItemList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem item = iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		imageGalleryService.deleteImageGalleryItem(item.getUid());
	    }
	}
	// handle imageGallery item attachment file:
	List<ImageGalleryItem> delItemAttList = getDeletedItemAttachmentList(sessionMap);
	iter = delItemAttList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem delAtt = iter.next();
	    iter.remove();
	}
	// **********************************************
	// finally persist imageGalleryPO again
	imageGalleryService.saveOrUpdateImageGallery(imageGalleryPO);

	// ************************* Handle rating criterias *******************
	if (mode.isAuthor()) {
	    List<RatingCriteria> oldCriterias = (List<RatingCriteria>) sessionMap
		    .get(AttributeNames.ATTR_RATING_CRITERIAS);

	    imageGalleryService.saveRatingCriterias(request, oldCriterias, contentId);
	}
	imageGalleryForm.setImageGallery(imageGalleryPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	return "pages/authoring/authoring";
    }

    // **********************************************************
    // Add Image methods
    // **********************************************************

    /**
     * Display empty page for new imageGallery item.
     */
    @RequestMapping("/newImageInit")
    public String newImageInit(@ModelAttribute ImageGalleryItemForm imageGalleryItemForm, HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	imageGalleryItemForm.setSessionMapID(sessionMapID);

	// saveUsingLearningAction param is true in case request comes from learning or monitor and the we should use
	// LearningAction's method to save uploaded image
	boolean saveUsingLearningAction = WebUtil.readBooleanParam(request, "saveUsingLearningAction", false);
	request.setAttribute("saveUsingLearningAction", saveUsingLearningAction);

	imageGalleryItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	return "pages/authoring/parts/addimage";
    }

    /**
     * Display edit page for existed imageGallery item.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/editImage")
    public String editImage(@ModelAttribute ImageGalleryItemForm imageGalleryItemForm, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.toInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	ImageGalleryItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ImageGalleryItem> imageGalleryList = getImageList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<>(imageGalleryList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, imageGalleryItemForm, request);
	    }
	}

	imageGalleryItemForm.setTmpFileUploadId(FileUtil.generateTmpFileUploadId());

	return (item == null) ? null : "pages/authoring/parts/addimage";
    }

    /**
     * Remove imageGallery item attachment, such as single file, learning object ect. It is a ajax call and just
     * temporarily remove from page, all permenant change will happen only when user sumbit this imageGallery item
     * again.
     */
    @RequestMapping("/removeImageFile")
    public String removeImageFile(HttpServletRequest request) {

	request.setAttribute("itemAttachment", null);
	return "pages/authoring/parts/imagefile";
    }

    /**
     * This method will get necessary information from imageGallery item form and save or update into
     * <code>HttpSession</code> ImageGalleryItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @RequestMapping("/saveOrUpdateImage")
    public String saveOrUpdateImage(@ModelAttribute ImageGalleryItemForm imageGalleryItemForm,
	    HttpServletRequest request, HttpServletResponse response) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	try {
	    extractFormToImageGalleryItems(request, imageGalleryItemForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage(ImageGalleryConstants.ERROR_MSG_UPLOAD_FAILED,
		    new Object[] { e.getMessage() }));
	}

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/authoring/parts/addimage";
	}

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, imageGalleryItemForm.getSessionMapID());
	// return null to close this window
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Move up current item.
     */
    @RequestMapping("/upImage")
    public String upImage(HttpServletRequest request) {
	return switchItem(request, true);
    }

    /**
     */
    @RequestMapping("/downImage")
    private String downImage(HttpServletRequest request) {
	return switchItem(request, false);
    }

    private String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int imageIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	if (imageIdx != -1) {
	    SortedSet<ImageGalleryItem> taskListList = getImageList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<>(taskListList);
	    // get current and the target item, and switch their sequnece
	    ImageGalleryItem image = rList.get(imageIdx);
	    ImageGalleryItem repImage;
	    if (up) {
		repImage = rList.get(--imageIdx);
	    } else {
		repImage = rList.get(++imageIdx);
	    }
	    int upSeqId = repImage.getSequenceId();
	    repImage.setSequenceId(image.getSequenceId());
	    image.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    taskListList.clear();
	    taskListList.addAll(rList);
	}

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Display empty page for muiltiple image upload.
     */
    @RequestMapping("/initMultipleImages")
    public String initMultipleImages(@ModelAttribute MultipleImagesForm multipleImagesForm,
	    HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	multipleImagesForm.setSessionMapID(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return "pages/authoring/parts/addmultipleimages";
    }

    /**
     * Remove imageGallery item from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/removeImage")
    public String removeImage(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<ImageGalleryItem> imageGalleryList = getImageList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<>(imageGalleryList);
	    ImageGalleryItem item = rList.remove(itemIdx);
	    imageGalleryList.clear();
	    imageGalleryList.addAll(rList);
	    // add to delList
	    List<ImageGalleryItem> delList = getDeletedImageGalleryItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    /**
     * List save current imageGallery items.
     */
    private SortedSet<ImageGalleryItem> getImageList(SessionMap<String, Object> sessionMap) {
	SortedSet<ImageGalleryItem> list = (SortedSet<ImageGalleryItem>) sessionMap
		.get(ImageGalleryConstants.ATTR_IMAGE_LIST);
	if (list == null) {
	    list = new TreeSet<>(new ImageGalleryItemComparator());
	    sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted imageGallery items, which could be persisted or non-persisted items.
     */
    private List<ImageGalleryItem> getDeletedImageGalleryItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_IMAGE_LIST);
    }

    /**
     * If a imageGallery item has attahment file, and the user edit this item and change the attachment to new file,
     * then the old file need be deleted when submitting the whole authoring page. Save the file uuid and version id
     * into ImageGalleryItem object for temporarily use.
     */
    private List<ImageGalleryItem> getDeletedItemAttachmentList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_IMAGE_ATTACHMENT_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     */
    private List<ImageGalleryItem> getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List<ImageGalleryItem> list = (List<ImageGalleryItem>) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList<>();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * This method will populate imageGallery item information to its form for edit use.
     */
    private void populateItemToForm(int itemIdx, ImageGalleryItem item, ImageGalleryItemForm form,
	    HttpServletRequest request) {
	form.setDescription(item.getDescription());
	form.setTitle(item.getTitle());
	if (itemIdx >= 0) {
	    form.setImageIndex(String.valueOf(itemIdx));
	}

	if (item.getOriginalFileUuid() != null) {
	    form.setFileUuid(item.getOriginalFileUuid());
	    form.setFileName(item.getFileName());
	    form.setFileDisplayUuid(item.getOriginalFileDisplayUuid());
	    form.setHasFile(true);
	} else {
	    form.setHasFile(false);
	}
    }

    /**
     * Extract web form content to imageGallery item.
     */
    @SuppressWarnings("unchecked")
    private void extractFormToImageGalleryItems(HttpServletRequest request, ImageGalleryItemForm imageForm)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to an old or new ImageGalleryItem
	 * instance.
	 * It gets all info EXCEPT ImageGalleryItem.createDate and ImageGalleryItem.createBy, which need be set when
	 * persisting this imageGallery item.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(imageForm.getSessionMapID());

	SortedSet<ImageGalleryItem> imageList = getImageList(sessionMap);
	int imageIdx = NumberUtils.toInt(imageForm.getImageIndex(), -1);
	ImageGalleryItem item = null;

	// check whether it is "edit(old item)" or "add(new item)"
	boolean hasOld = imageIdx >= 0;
	if (hasOld) {
	    List<ImageGalleryItem> rList = new ArrayList<>(imageList);
	    item = rList.get(imageIdx);
	} else {
	    item = new ImageGalleryItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (imageList != null && imageList.size() > 0) {
		ImageGalleryItem last = imageList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    item.setSequenceId(maxSeq);
	}

	String title = imageForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextImageTitleNumber = (Long) sessionMap.get(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE);
	    sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, nextImageTitleNumber + 1);
	    title = imageGalleryService.generateNextImageTitle(nextImageTitleNumber);
	}
	item.setTitle(title);

	item.setDescription(imageForm.getDescription());
	item.setCreateByAuthor(true);
	item.setHide(false);

	File uploadDir = FileUtil.getTmpFileUploadDir(imageForm.getTmpFileUploadId());
	if (uploadDir.canRead()) {
	    File[] files = uploadDir.listFiles();
	    if (files.length == 0) {
		return;
	    }

	    if (hasOld) {
		// if we are replacing an image, there can be only 1 upload
		if (files.length > 1) {
		    throw new ServletException("Uploaded more than 1 image while editing an Image Gallery Item");
		}

		// if it has old file, and upload a new, then save old to deleteList
		ImageGalleryItem delImage = new ImageGalleryItem();
		// be careful, This new ImageGalleryItem object never be save into database
		// just temporarily use for saving fileUuid
		delImage.setOriginalFileUuid(item.getOriginalFileUuid());
		List<ImageGalleryItem> delAtt = getDeletedItemAttachmentList(sessionMap);
		delAtt.add(delImage);
	    }

	    int sequenceId = item.getSequenceId();
	    for (File file : files) {
		// if user uploaded multiple new images, all of them get the same title and description
		if (sequenceId > item.getSequenceId()) {
		    ImageGalleryItem nextItem = new ImageGalleryItem();
		    nextItem.setCreateDate(item.getCreateDate());
		    nextItem.setSequenceId(sequenceId);

		    title = imageForm.getTitle();
		    if (StringUtils.isBlank(title)) {
			Long nextImageTitleNumber = (Long) sessionMap.get(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE);
			sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, nextImageTitleNumber + 1);
			title = imageGalleryService.generateNextImageTitle(nextImageTitleNumber);
		    }
		    nextItem.setTitle(title);
		    nextItem.setDescription(item.getDescription());
		    nextItem.setCreateByAuthor(true);
		    nextItem.setHide(false);
		    item = nextItem;
		}

		imageGalleryService.uploadImageGalleryItemFile(item, file);
		if (!hasOld) {
		    imageList.add(item);
		}
		sequenceId++;
	    }

	    FileUtil.deleteTmpFileUploadDir(imageForm.getTmpFileUploadId());
	}
    }
}
