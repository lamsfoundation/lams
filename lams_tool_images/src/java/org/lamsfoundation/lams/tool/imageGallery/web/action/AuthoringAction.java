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

package org.lamsfoundation.lams.tool.imageGallery.web.action;

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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryException;
import org.lamsfoundation.lams.tool.imageGallery.service.UploadImageGalleryFileException;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryUtils;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.MultipleImagesForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Andrey Balan
 */
public class AuthoringAction extends Action {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------ImageGallery Author function ---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = getAccessMode(request);
	    // teacher mode "check for new" button enter.
	    if (mode != null) {
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    } else {
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR.toString());
	    }
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IImageGalleryService service = getImageGalleryService();
	    ImageGallery imageGallery = service.getImageGalleryByContentId(contentId);

	    imageGallery.setDefineLater(true);
	    service.saveOrUpdateImageGallery(imageGallery);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// ----------------------- Add imageGallery item function ---------------------------
	if (param.equals("newImageInit")) {
	    return newImageInit(mapping, form, request, response);
	}
	if (param.equals("editImage")) {
	    return editImage(mapping, form, request, response);
	}
	if (param.equals("removeImageFile")) {
	    return removeImageFile(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateImage")) {
	    return saveOrUpdateImage(mapping, form, request, response);
	}
	if (param.equals("upImage")) {
	    return upImage(mapping, form, request, response);
	}
	if (param.equals("downImage")) {
	    return downImage(mapping, form, request, response);
	}
	if (param.equals("removeImage")) {
	    return removeImage(mapping, form, request, response);
	}
	if (param.equals("initMultipleImages")) {
	    return initMultipleImages(mapping, form, request, response);
	}
	if (param.equals("saveMultipleImages")) {
	    return saveMultipleImages(mapping, form, request, response);
	}

	return mapping.findForward(ImageGalleryConstants.ERROR);
    }

    /**
     * Read imageGallery data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_TOOL_CONTENT_ID));

	// get back the imageGallery and item list and display them on page
	IImageGalleryService service = getImageGalleryService();

	List<ImageGalleryItem> items = null;
	ImageGallery imageGallery = null;
	ImageGalleryForm imageGalleryForm = (ImageGalleryForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	imageGalleryForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	imageGalleryForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    imageGallery = service.getImageGalleryByContentId(contentId);
	    // if imageGallery does not exist, try to use default content instead.
	    if (imageGallery == null) {
		imageGallery = service.getDefaultContent(contentId);
		if (imageGallery.getImageGalleryItems() != null) {
		    items = new ArrayList<ImageGalleryItem>(imageGallery.getImageGalleryItems());
		} else {
		    items = null;
		}
	    } else {
		items = service.getAuthoredItems(imageGallery.getUid());
	    }

	    imageGalleryForm.setImageGallery(imageGallery);
	    imageGalleryForm.setAllowRatingsOrVote(imageGallery.isAllowVote() || imageGallery.isAllowRank());
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<ImageGalleryItem>();
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
	List<RatingCriteria> ratingCriterias = service.getRatingCriterias(contentId);
	sessionMap.put(AttributeNames.ATTR_RATING_CRITERIAS, ratingCriterias);

	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY_FORM, imageGalleryForm);
	sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, imageGallery.getNextImageTitle());
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	ImageGalleryForm existForm = (ImageGalleryForm) sessionMap.get(ImageGalleryConstants.ATTR_IMAGE_GALLERY_FORM);

	ImageGalleryForm imageGalleryForm = (ImageGalleryForm) form;
	try {
	    PropertyUtils.copyProperties(imageGalleryForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all imageGallery item, information etc.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ImageGalleryForm imageGalleryForm = (ImageGalleryForm) (form);

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(imageGalleryForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	ImageGallery imageGallery = imageGalleryForm.getImageGallery();
	IImageGalleryService service = getImageGalleryService();
	Long contentId = imageGalleryForm.getImageGallery().getContentId();

	// **********************************Get ImageGallery PO*********************
	ImageGallery imageGalleryPO = service.getImageGalleryByContentId(contentId);
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
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
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
	Set<ImageGalleryItem> itemList = new LinkedHashSet<ImageGalleryItem>();
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
	// delete instructino file from database.
	List<ImageGalleryItem> delImageGalleryItemList = getDeletedImageGalleryItemList(sessionMap);
	iter = delImageGalleryItemList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem item = iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		service.deleteImageGalleryItem(item.getUid());
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
	service.saveOrUpdateImageGallery(imageGalleryPO);

	// ************************* Handle rating criterias *******************
	if (mode.isAuthor()) {
	    List<RatingCriteria> oldCriterias = (List<RatingCriteria>) sessionMap
		    .get(AttributeNames.ATTR_RATING_CRITERIAS);

	    service.saveRatingCriterias(request, oldCriterias, contentId);
	}
	imageGalleryForm.setImageGallery(imageGalleryPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    // **********************************************************
    // Add Image methods
    // **********************************************************

    /**
     * Display empty page for new imageGallery item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newImageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	((ImageGalleryItemForm) form).setSessionMapID(sessionMapID);

	return mapping.findForward("image");
    }

    /**
     * Display edit page for existed imageGallery item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	ImageGalleryItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ImageGalleryItem> imageGalleryList = getImageList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(imageGalleryList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, (ImageGalleryItemForm) form, request);
	    }
	}
	return (item == null) ? null : mapping.findForward("image");
    }

    /**
     * Remove imageGallery item attachment, such as single file, learning object ect. It is a ajax call and just
     * temporarily remove from page, all permenant change will happen only when user sumbit this imageGallery item
     * again.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeImageFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("itemAttachment", null);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * This method will get necessary information from imageGallery item form and save or update into
     * <code>HttpSession</code> ImageGalleryItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ImageGalleryItemForm itemForm = (ImageGalleryItemForm) form;
	ActionErrors errors = ImageGalleryUtils.validateImageGalleryItem(itemForm, true);

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
	    this.addErrors(request, errors);
	    return mapping.findForward("image");
	}
	
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Move up current item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, true);
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
    private ActionForward downImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, false);
    }

    private ActionForward switchItem(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	int imageIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	if (imageIdx != -1) {
	    SortedSet<ImageGalleryItem> taskListList = getImageList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(taskListList);
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
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Display empty page for muiltiple image upload.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward initMultipleImages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	((MultipleImagesForm) form).setSessionMapID(sessionMapID);

	return mapping.findForward("images");
    }

    /**
     * This method will get necessary information from imageGallery item form and save or update into
     * <code>HttpSession</code> ImageGalleryItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveMultipleImages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	MultipleImagesForm multipleForm = (MultipleImagesForm) form;
	ActionErrors errors = ImageGalleryUtils.validateMultipleImages(multipleForm, true);

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
	    this.addErrors(request, errors);
	    return mapping.findForward("images");
	}
	
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, multipleForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Remove imageGallery item from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<ImageGalleryItem> imageGalleryList = getImageList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(imageGalleryList);
	    ImageGalleryItem item = rList.remove(itemIdx);
	    imageGalleryList.clear();
	    imageGalleryList.addAll(rList);
	    // add to delList
	    List<ImageGalleryItem> delList = getDeletedImageGalleryItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************
    /**
     * Return ImageGalleryService bean.
     */
    private IImageGalleryService getImageGalleryService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.IMAGE_GALLERY_SERVICE);
    }

    /**
     * List save current imageGallery items.
     *
     * @param request
     * @return
     */
    private SortedSet<ImageGalleryItem> getImageList(SessionMap<String, Object> sessionMap) {
	SortedSet<ImageGalleryItem> list = (SortedSet<ImageGalleryItem>) sessionMap
		.get(ImageGalleryConstants.ATTR_IMAGE_LIST);
	if (list == null) {
	    list = new TreeSet<ImageGalleryItem>(new ImageGalleryItemComparator());
	    sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted imageGallery items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List<ImageGalleryItem> getDeletedImageGalleryItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_IMAGE_LIST);
    }

    /**
     * If a imageGallery item has attahment file, and the user edit this item and change the attachment to new file,
     * then the old file need be deleted when submitting the whole authoring page. Save the file uuid and version id
     * into ImageGalleryItem object for temporarily use.
     *
     * @param request
     * @return
     */
    private List<ImageGalleryItem> getDeletedItemAttachmentList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_IMAGE_ATTACHMENT_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List<ImageGalleryItem> getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List<ImageGalleryItem> list = (List<ImageGalleryItem>) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList<ImageGalleryItem>();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * This method will populate imageGallery item information to its form for edit use.
     *
     * @param itemIdx
     * @param item
     * @param form
     * @param request
     */
    private void populateItemToForm(int itemIdx, ImageGalleryItem item, ImageGalleryItemForm form,
	    HttpServletRequest request) {
	form.setDescription(item.getDescription());
	form.setTitle(item.getTitle());
	if (itemIdx >= 0) {
	    form.setImageIndex(new Integer(itemIdx).toString());
	}

	if (item.getOriginalFileUuid() != null) {
	    form.setFileUuid(item.getOriginalFileUuid());
	    form.setFileVersionId(item.getFileVersionId());
	    form.setFileName(item.getFileName());
	    form.setHasFile(true);
	} else {
	    form.setHasFile(false);
	}
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
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to a old or new ImageGalleryItem instance.
	 * It gets all info EXCEPT ImageGalleryItem.createDate and ImageGalleryItem.createBy, which need be set when
	 * persisting this imageGallery item.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(imageForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ImageGalleryItem> imageList = getImageList(sessionMap);
	int imageIdx = NumberUtils.stringToInt(imageForm.getImageIndex(), -1);
	ImageGalleryItem image = null;

	if (imageIdx == -1) { // add
	    image = new ImageGalleryItem();
	    image.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (imageList != null && imageList.size() > 0) {
		ImageGalleryItem last = imageList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    image.setSequenceId(maxSeq);
	    imageList.add(image);
	} else { // edit
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(imageList);
	    image = rList.get(imageIdx);
	}

	// uploadImageGalleryItemFile
	// and setting file properties' fields: item.setFileUuid(); item.setFileVersionId(); item.setFileType();
	// item.setFileName();
	if (imageForm.getFile() != null) {
	    // if it has old file, and upload a new, then save old to deleteList
	    ImageGalleryItem delImage = new ImageGalleryItem();
	    boolean hasOld = false;
	    if (image.getOriginalFileUuid() != null) {
		hasOld = true;
		// be careful, This new ImageGalleryItem object never be save into database
		// just temporarily use for saving fileUuid and versionID use:
		delImage.setOriginalFileUuid(image.getOriginalFileUuid());
		delImage.setFileVersionId(image.getFileVersionId());
	    }
	    IImageGalleryService service = getImageGalleryService();
	    try {
		service.uploadImageGalleryItemFile(image, imageForm.getFile());
	    } catch (UploadImageGalleryFileException e) {
		// if it is new add , then remove it!
		if (imageIdx == -1) {
		    imageList.remove(image);
		}
		throw e;
	    }
	    // put it after "upload" to ensure deleted file added into list only no exception happens during upload
	    if (hasOld) {
		List<ImageGalleryItem> delAtt = getDeletedItemAttachmentList(sessionMap);
		delAtt.add(delImage);
	    }
	}

	String title = imageForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextConsecutiveImageTitle = (Long) sessionMap.get(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE);
	    sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, nextConsecutiveImageTitle + 1);
	    String imageLocalized = getImageGalleryService().getLocalisedMessage("label.authoring.image", null);
	    title = imageLocalized + " " + nextConsecutiveImageTitle;
	}
	image.setTitle(title);

	image.setDescription(imageForm.getDescription());
	image.setCreateByAuthor(true);
	image.setHide(false);
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

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(multipleForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ImageGalleryItem> imageList = getImageList(sessionMap);

	List<FormFile> fileList = ImageGalleryUtils.createFileListFromMultipleForm(multipleForm);
	for (FormFile file : fileList) {
	    ImageGalleryItem image = new ImageGalleryItem();
	    image.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (imageList != null && imageList.size() > 0) {
		ImageGalleryItem last = imageList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    image.setSequenceId(maxSeq);
	    imageList.add(image);

	    // uploadImageGalleryItemFile
	    // and setting file properties' fields: item.setFileUuid(); item.setFileVersionId(); item.setFileType();
	    // item.setFileName();
	    IImageGalleryService service = getImageGalleryService();
	    try {
		service.uploadImageGalleryItemFile(image, file);
	    } catch (UploadImageGalleryFileException e) {
		imageList.remove(image);
		throw e;
	    }

	    Long nextConsecutiveImageTitle = (Long) sessionMap.get(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE);
	    sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, nextConsecutiveImageTitle + 1);
	    String imageLocalized = getImageGalleryService().getLocalisedMessage("label.authoring.image", null);
	    String title = imageLocalized + " " + nextConsecutiveImageTitle;
	    image.setTitle(title);

	    image.setDescription("");
	    image.setCreateByAuthor(true);
	    image.setHide(false);
	}
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

}
