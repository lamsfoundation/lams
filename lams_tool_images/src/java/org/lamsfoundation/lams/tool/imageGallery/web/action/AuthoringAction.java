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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.imageGallery.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryAttachment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryApplicationException;
import org.lamsfoundation.lams.tool.imageGallery.service.UploadImageGalleryFileException;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Andrey Balan
 * @version $Revision$
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
	if (param.equals("uploadOnlineFile")) {
	    return uploadOnline(mapping, form, request, response);
	}
	if (param.equals("uploadOfflineFile")) {
	    return uploadOffline(mapping, form, request, response);
	}
	if (param.equals("deleteOnlineFile")) {
	    return deleteOnlineFile(mapping, form, request, response);
	}
	if (param.equals("deleteOfflineFile")) {
	    return deleteOfflineFile(mapping, form, request, response);
	}
	// ----------------------- Add imageGallery item function ---------------------------
	if (param.equals("newItemInit")) {
	    return newItemlInit(mapping, form, request, response);
	}
	if (param.equals("editItemInit")) {
	    return editItemInit(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateItem")) {
	    return saveOrUpdateItem(mapping, form, request, response);
	}
	if (param.equals("removeItem")) {
	    return removeItem(mapping, form, request, response);
	}
	if (param.equals("upItem")) {
	    return upImage(mapping, form, request, response);
	}
	if (param.equals("downItem")) {
	    return downImage(mapping, form, request, response);
	}
	if (param.equals("removeItemAttachment")) {
	    return removeItemAttachment(mapping, form, request, response);
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
	SessionMap sessionMap = new SessionMap();
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

	    // initialize instruction attachment list
	    List attachmentList = getAttachmentList(sessionMap);
	    attachmentList.clear();
	    attachmentList.addAll(imageGallery.getAttachments());
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
	SortedSet<ImageGalleryItem> imageGalleryItemList = getImageGalleryItemList(sessionMap);
	imageGalleryItemList.clear();
	imageGalleryItemList.addAll(items);

	sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE_FORM, imageGalleryForm);
	sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, imageGallery.getNextImageTitle());
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	ImageGalleryForm existForm = (ImageGalleryForm) sessionMap.get(ImageGalleryConstants.ATTR_RESOURCE_FORM);

	ImageGalleryForm imageGalleryForm = (ImageGalleryForm) form;
	try {
	    PropertyUtils.copyProperties(imageGalleryForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(ImageGalleryConstants.SUCCESS);
	} else {
	    return mapping.findForward(ImageGalleryConstants.DEFINE_LATER);
	}
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(imageGalleryForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	ActionMessages errors = validateImageGallery(imageGalleryForm, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    if (mode.isAuthor()) {
		return mapping.findForward("author");
	    } else {
		return mapping.findForward("monitor");
	    }
	}

	ImageGallery imageGallery = imageGalleryForm.getImageGallery();
	IImageGalleryService service = getImageGalleryService();

	// **********************************Get ImageGallery PO*********************
	ImageGallery imageGalleryPO = service.getImageGalleryByContentId(imageGalleryForm.getImageGallery()
		.getContentId());
	if (imageGalleryPO == null) {
	    // new ImageGallery, create it.
	    imageGalleryPO = imageGallery;
	    imageGalleryPO.setCreated(new Timestamp(new Date().getTime()));
	    imageGalleryPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    if (mode.isAuthor()) {
		Long uid = imageGalleryPO.getUid();
		PropertyUtils.copyProperties(imageGalleryPO, imageGallery);
		// get back UID
		imageGalleryPO.setUid(uid);
	    } else { // if it is Teacher, then just update basic tab content (definelater)
		imageGalleryPO.setInstructions(imageGallery.getInstructions());
		imageGalleryPO.setTitle(imageGallery.getTitle());
		// change define later status
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
		imageGalleryForm.getImageGallery().getContentId());
	if (imageGalleryUser == null) {
	    imageGalleryUser = new ImageGalleryUser(user, imageGalleryPO);
	}

	imageGalleryPO.setCreatedBy(imageGalleryUser);

	// **********************************Handle Authoring Instruction Attachement *********************
	// merge attachment info
	// so far, attPOSet will be empty if content is existed. because PropertyUtils.copyProperties() is executed
	Set attPOSet = imageGalleryPO.getAttachments();
	if (attPOSet == null) {
	    attPOSet = new HashSet();
	}
	List attachmentList = getAttachmentList(sessionMap);
	List deleteAttachmentList = getDeletedAttachmentList(sessionMap);

	// current attachemnt in authoring instruction tab.
	Iterator iter = attachmentList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryAttachment newAtt = (ImageGalleryAttachment) iter.next();
	    attPOSet.add(newAtt);
	}
	attachmentList.clear();

	// deleted attachment. 2 possible types: one is persist another is non-persist before.
	iter = deleteAttachmentList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryAttachment delAtt = (ImageGalleryAttachment) iter.next();
	    iter.remove();
	    // it is an existed att, then delete it from current attachmentPO
	    if (delAtt.getUid() != null) {
		Iterator attIter = attPOSet.iterator();
		while (attIter.hasNext()) {
		    ImageGalleryAttachment att = (ImageGalleryAttachment) attIter.next();
		    if (delAtt.getUid().equals(att.getUid())) {
			attIter.remove();
			break;
		    }
		}
		service.deleteImageGalleryAttachment(delAtt.getUid());
	    }// end remove from persist value
	}

	// copy back
	imageGalleryPO.setAttachments(attPOSet);
	// ************************* Handle imageGallery items *******************
	// Handle imageGallery items
	Set itemList = new LinkedHashSet();
	SortedSet topics = getImageGalleryItemList(sessionMap);
	iter = topics.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem item = (ImageGalleryItem) iter.next();
	    if (item != null) {
		// This flushs user UID info to message if this user is a new user.
		item.setCreateBy(imageGalleryUser);
		itemList.add(item);
	    }
	}
	imageGalleryPO.setImageGalleryItems(itemList);
	// delete instructino file from database.
	List delImageGalleryItemList = getDeletedImageGalleryItemList(sessionMap);
	iter = delImageGalleryItemList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem item = (ImageGalleryItem) iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		service.deleteImageGalleryItem(item.getUid());
	    }
	}
	// handle imageGallery item attachment file:
	List delItemAttList = getDeletedItemAttachmentList(sessionMap);
	iter = delItemAttList.iterator();
	while (iter.hasNext()) {
	    ImageGalleryItem delAtt = (ImageGalleryItem) iter.next();
	    iter.remove();
	}

	// if miniview number is bigger than available items, then set it topics size
	if (imageGalleryPO.getNumberColumns() > topics.size()) {
	    imageGalleryPO.setNumberColumns((topics.size()));
	}
	// **********************************************
	// finally persist imageGalleryPO again
	service.saveOrUpdateImageGallery(imageGalleryPO);

	// initialize attachmentList again
	attachmentList = getAttachmentList(sessionMap);
	attachmentList.addAll(imageGallery.getAttachments());
	imageGalleryForm.setImageGallery(imageGalleryPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	} else {
	    return mapping.findForward("monitor");
	}
    }

    /**
     * Handle upload online instruction files request.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws UploadImageGalleryFileException
     */
    public ActionForward uploadOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UploadImageGalleryFileException {
	return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE, request);
    }

    /**
     * Handle upload offline instruction files request.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws UploadImageGalleryFileException
     */
    public ActionForward uploadOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UploadImageGalleryFileException {
	return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    /**
     * Common method to upload online or offline instruction files request.
     * 
     * @param mapping
     * @param form
     * @param type
     * @param request
     * @return
     * @throws UploadImageGalleryFileException
     */
    private ActionForward uploadFile(ActionMapping mapping, ActionForm form, String type, HttpServletRequest request)
	    throws UploadImageGalleryFileException {

	ImageGalleryForm imageGalleryForm = (ImageGalleryForm) form;
	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(imageGalleryForm.getSessionMapID());

	FormFile file;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    file = imageGalleryForm.getOfflineFile();
	} else {
	    file = imageGalleryForm.getOnlineFile();
	}

	if ((file == null) || StringUtils.isBlank(file.getFileName())) {
	    return mapping.findForward(ImageGalleryConstants.SUCCESS);
	}

	// validate file size
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(file, true, errors);
	if (!errors.isEmpty()) {
	    this.saveErrors(request, errors);
	    return mapping.findForward(ImageGalleryConstants.SUCCESS);
	}

	IImageGalleryService service = getImageGalleryService();
	// upload to repository
	ImageGalleryAttachment att = service.uploadInstructionFile(file, type);
	// handle session value
	List attachmentList = getAttachmentList(sessionMap);
	List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
	// first check exist attachment and delete old one (if exist) to deletedAttachmentList
	Iterator iter = attachmentList.iterator();
	ImageGalleryAttachment existAtt;
	while (iter.hasNext()) {
	    existAtt = (ImageGalleryAttachment) iter.next();
	    if (StringUtils.equals(existAtt.getFileName(), att.getFileName())
		    && StringUtils.equals(existAtt.getFileType(), att.getFileType())) {
		// if there is same name attachment, delete old one
		deleteAttachmentList.add(existAtt);
		iter.remove();
		break;
	    }
	}
	// add to attachmentList
	attachmentList.add(att);

	return mapping.findForward(ImageGalleryConstants.SUCCESS);

    }

    /**
     * Delete offline instruction file from current ImageGallery authoring page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, request, response, form, IToolContentHandler.TYPE_OFFLINE);
    }

    /**
     * Delete online instruction file from current ImageGallery authoring page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, request, response, form, IToolContentHandler.TYPE_ONLINE);
    }

    /**
     * General method to delete file (online or offline)
     * 
     * @param mapping
     * @param request
     * @param response
     * @param form
     * @param type
     * @return
     */
    private ActionForward deleteFile(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	    ActionForm form, String type) {
	Long versionID = new Long(WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_FILE_VERSION_ID));
	Long uuID = new Long(WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_FILE_UUID));

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	// handle session value
	List attachmentList = getAttachmentList(sessionMap);
	List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
	// first check exist attachment and delete old one (if exist) to deletedAttachmentList
	Iterator iter = attachmentList.iterator();
	ImageGalleryAttachment existAtt;
	while (iter.hasNext()) {
	    existAtt = (ImageGalleryAttachment) iter.next();
	    if (existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)) {
		// if there is same name attachment, delete old one
		deleteAttachmentList.add(existAtt);
		iter.remove();
	    }
	}

	request.setAttribute(ImageGalleryConstants.ATTR_FILE_TYPE_FLAG, type);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);

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
    private ActionForward removeItemAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("itemAttachment", null);
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
    private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<ImageGalleryItem> imageGalleryList = getImageGalleryItemList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(imageGalleryList);
	    ImageGalleryItem item = rList.remove(itemIdx);
	    imageGalleryList.clear();
	    imageGalleryList.addAll(rList);
	    // add to delList
	    List delList = getDeletedImageGalleryItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int imageIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	if (imageIdx != -1) {
	    SortedSet<ImageGalleryItem> taskListList = getImageGalleryItemList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(taskListList);
	    // get current and the target item, and switch their sequnece
	    ImageGalleryItem image = rList.get(imageIdx);
	    ImageGalleryItem repImage;
	    if (up)
		repImage = rList.get(--imageIdx);
	    else
		repImage = rList.get(++imageIdx);
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
     * Display edit page for existed imageGallery item.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), -1);
	ImageGalleryItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ImageGalleryItem> imageGalleryList = getImageGalleryItemList(sessionMap);
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(imageGalleryList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, (ImageGalleryItemForm) form, request);
	    }
	}
	return (item == null) ? null : mapping.findForward("file");
    }

    /**
     * Display empty page for new imageGallery item.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	((ImageGalleryItemForm) form).setSessionMapID(sessionMapID);

	return mapping.findForward("file");
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
    private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ImageGalleryItemForm itemForm = (ImageGalleryItemForm) form;
	ActionErrors errors = validateImageGalleryItem(itemForm);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return mapping.findForward("file");
	}

	try {
	    extractFormToImageGalleryItem(request, itemForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	    if (!errors.isEmpty()) {
		this.addErrors(request, errors);
		return mapping.findForward("file");
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }



    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return ImageGalleryService bean.
     */
    private IImageGalleryService getImageGalleryService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.RESOURCE_SERVICE);
    }

    /**
     * @param request
     * @return
     */
    private List getAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATT_ATTACHMENT_LIST);
    }

    /**
     * @param request
     * @return
     */
    private List getDeletedAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_ATTACHMENT_LIST);
    }

    /**
     * List save current imageGallery items.
     * 
     * @param request
     * @return
     */
    private SortedSet<ImageGalleryItem> getImageGalleryItemList(SessionMap sessionMap) {
	SortedSet<ImageGalleryItem> list = (SortedSet<ImageGalleryItem>) sessionMap
		.get(ImageGalleryConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<ImageGalleryItem>(new ImageGalleryItemComparator());
	    sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted imageGallery items, which could be persisted or non-persisted items.
     * 
     * @param request
     * @return
     */
    private List getDeletedImageGalleryItemList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_RESOURCE_ITEM_LIST);
    }

    /**
     * If a imageGallery item has attahment file, and the user edit this item and change the attachment to new file,
     * then the old file need be deleted when submitting the whole authoring page. Save the file uuid and version id
     * into ImageGalleryItem object for temporarily use.
     * 
     * @param request
     * @return
     */
    private List getDeletedItemAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ImageGalleryConstants.ATTR_DELETED_RESOURCE_ITEM_ATTACHMENT_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     * 
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
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
	form.setUrl(item.getUrl());
	form.setOpenUrlNewWindow(item.isOpenUrlNewWindow());
	if (itemIdx >= 0) {
	    form.setImageIndex(new Integer(itemIdx).toString());
	}

	if (item.getFileUuid() != null) {
	    form.setFileUuid(item.getFileUuid());
	    form.setFileVersionId(item.getFileVersionId());
	    form.setFileName(item.getFileName());
	    form.setHasFile(true);
	} else {
	    form.setHasFile(false);
	}
    }

    /**
     * Extract web from content to imageGallery item.
     * 
     * @param request
     * @param itemForm
     * @throws ImageGalleryApplicationException
     */
    private void extractFormToImageGalleryItem(HttpServletRequest request,
	    ImageGalleryItemForm itemForm) throws Exception {
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to a old or new ImageGalleryItem instance.
	 * It gets all info EXCEPT ImageGalleryItem.createDate and ImageGalleryItem.createBy, which need be set when
	 * persisting this imageGallery item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ImageGalleryItem> imageGalleryList = getImageGalleryItemList(sessionMap);
	int imageIdx = NumberUtils.stringToInt(itemForm.getImageIndex(), -1);
	ImageGalleryItem item = null;

	if (imageIdx == -1) { // add
	    item = new ImageGalleryItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (imageGalleryList != null && imageGalleryList.size() > 0) {
		ImageGalleryItem last = imageGalleryList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    item.setSequenceId(maxSeq);
	    imageGalleryList.add(item);
	} else { // edit
	    List<ImageGalleryItem> rList = new ArrayList<ImageGalleryItem>(imageGalleryList);
	    item = rList.get(imageIdx);
	}

	// uploadImageGalleryItemFile 
	// and setting file properties' fields: item.setFileUuid(); item.setFileVersionId(); item.setFileType(); item.setFileName();
	if (itemForm.getFile() != null) {
	    // if it has old file, and upload a new, then save old to deleteList
	    ImageGalleryItem delAttItem = new ImageGalleryItem();
	    boolean hasOld = false;
	    if (item.getFileUuid() != null) {
		hasOld = true;
		// be careful, This new ImageGalleryItem object never be save into database
		// just temporarily use for saving fileUuid and versionID use:
		delAttItem.setFileUuid(item.getFileUuid());
		delAttItem.setFileVersionId(item.getFileVersionId());
	    }
	    IImageGalleryService service = getImageGalleryService();
	    try {
		service.uploadImageGalleryItemFile(item, itemForm.getFile());
	    } catch (UploadImageGalleryFileException e) {
		// if it is new add , then remove it!
		if (imageIdx == -1) {
		    imageGalleryList.remove(item);
		}
		throw e;
	    }
	    // put it after "upload" to ensure deleted file added into list only no exception happens during upload
	    if (hasOld) {
		List delAtt = getDeletedItemAttachmentList(sessionMap);
		delAtt.add(delAttItem);
	    }
	}
	
	String title = itemForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextConsecutiveImageTitle = (Long) sessionMap.get(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE);
	    sessionMap.put(ImageGalleryConstants.ATTR_NEXT_IMAGE_TITLE, nextConsecutiveImageTitle+1);
	    String imageLocalized = getImageGalleryService().getLocalisedMessage("label.authoring.image", null);
	    title = imageLocalized + " " + nextConsecutiveImageTitle;
	}
	item.setTitle(title);
	
	item.setDescription(itemForm.getDescription());	
	item.setCreateByAuthor(true);
	item.setHide(false);
    }

    /**
     * Validate imageGallery item.
     * 
     * @param itemForm
     * @return
     */
    private ActionErrors validateImageGalleryItem(ImageGalleryItemForm itemForm) {
	ActionErrors errors = new ActionErrors();
	
	// validate file size
	FileValidatorUtil.validateFileSize(itemForm.getFile(), true, errors);
	// for edit validate: file already exist
	if (!itemForm.isHasFile()
		&& ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_FILE_BLANK));
	}
	
	//check for allowed format : gif, png, jpg
	String contentType = itemForm.getFile().getContentType();
	if (StringUtils.isEmpty(contentType) || !(contentType.equals("image/gif") || contentType.equals("image/png") || contentType.equals("image/jpg"))) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_NOT_ALLOWED_FORMAT));
	}	

	return errors;
    }

    /**
     * Validate imageGallery.
     * 
     * @param imageGalleryForm
     * @return
     */
    private ActionMessages validateImageGallery(ImageGalleryForm imageGalleryForm, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	// if (StringUtils.isBlank(imageGalleryForm.getImageGallery().getTitle())) {
	// ActionMessage error = new ActionMessage("error.resource.item.title.blank");
	// errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	// }

	// define it later mode(TEACHER) skip below validation.
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errors;
	}

	// Some other validation outside basic Tab.

	return errors;
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
