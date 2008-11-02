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
package org.lamsfoundation.lams.tool.imageGallery.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageComment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.UploadImageGalleryFileException;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageCommentComparator;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageCommentForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
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
	    HttpServletResponse response) {

	String param = mapping.getParameter();
	// -----------------------ImageGallery Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("complete")) {
	    return complete(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("addimage")) {
	    return addImage(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateImage")) {
	    return saveOrUpdateImage(mapping, form, request, response);
	}
	
	// ================ Comments =======================	
	if (param.equals("showComments")) {
	    return showComments(mapping, form, request, response);
	}	
	if (param.equals("addNewComment")) {
	    return addNewComment(mapping, form, request, response);
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
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(ImageGalleryConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the imageGallery and item list and display them on page
	IImageGalleryService service = getImageGalleryService();
	ImageGalleryUser imageGalleryUser = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // imageGalleryUser may be null if the user was force completed.
	    imageGalleryUser = getSpecifiedUser(service, sessionId, WebUtil.readIntParam(request,
		    AttributeNames.PARAM_USER_ID, false));
	} else {
	    imageGalleryUser = getCurrentUser(service, sessionId);
	}

	List<ImageGalleryItem> items = null;
	ImageGallery imageGallery;
	items = service.getImageGalleryItemsBySessionId(sessionId);
	imageGallery = service.getImageGalleryBySessionId(sessionId);

	// check whehter finish lock is on/off
	boolean lock = imageGallery.getLockWhenFinished() && (imageGalleryUser != null)
		&& imageGalleryUser.isSessionFinished();

	// get notebook entry
	String entryText = new String();
	if (imageGalleryUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ImageGalleryConstants.TOOL_SIGNATURE, imageGalleryUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// basic information
	sessionMap.put(ImageGalleryConstants.ATTR_TITLE, imageGallery.getTitle());
	sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE_INSTRUCTION, imageGallery.getInstructions());
	sessionMap.put(ImageGalleryConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(ImageGalleryConstants.ATTR_LOCK_ON_FINISH, imageGallery.getLockWhenFinished());
	sessionMap.put(ImageGalleryConstants.ATTR_USER_FINISHED, (imageGalleryUser != null)
		&& imageGalleryUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_ON, imageGallery.isReflectOnActivity());
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_INSTRUCTION, imageGallery.getReflectInstructions());
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (imageGallery.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	imageGallery.setContentInUse(true);
	imageGallery.setDefineLater(false);
	service.saveOrUpdateImageGallery(imageGallery);

	// add run offline support
	if (imageGallery.getRunOffline()) {
	    sessionMap.put(ImageGalleryConstants.PARAM_RUN_OFFLINE, true);
	    return mapping.findForward("runOffline");
	} else {
	    sessionMap.put(ImageGalleryConstants.PARAM_RUN_OFFLINE, false);
	}

	// init imageGallery item list
	SortedSet<ImageGalleryItem> imageGalleryItemList = getImageList(sessionMap);
	imageGalleryItemList.clear();
	if (items != null) {
	    // remove hidden items.
	    for (ImageGalleryItem item : items) {
		// becuase in webpage will use this login name. Here is just
		// initial it to avoid session close error in proxy object.
		if (item.getCreateBy() != null) {
		    item.getCreateBy().getLoginName();
		}
		if (!item.isHide()) {
		    imageGalleryItemList.add(item);
		}
	    }
	}

	// set complete flag for display purpose
	if (imageGalleryUser != null) {
	    service.retrieveComplete(imageGalleryItemList, imageGalleryUser);
	}
	sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE, imageGallery);

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Mark imageGallery item as complete status.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);

	doComplete(request);

	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	//	// auto run mode, when use finish the only one imageGallery item, mark it as complete then finish this activity
	//	// as well.
	//	String imageGalleryItemUid = request.getParameter(ImageGalleryConstants.PARAM_RESOURCE_ITEM_UID);
	//	if (imageGalleryItemUid != null) {
	//	    doComplete(request);
	//	    // NOTE:So far this flag is useless(31/08/2006).
	//	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	//	    request.setAttribute(ImageGalleryConstants.ATTR_IS_ALLOW_VOTE, true);
	//	} else {
	//	    request.setAttribute(ImageGalleryConstants.ATTR_IS_ALLOW_VOTE, false);
	//	}

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Initial page for add imageGallery item (single file or URL).
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ImageGalleryItemForm itemForm = (ImageGalleryItemForm) form;
	itemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
	itemForm.setSessionMapID(WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID));
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
     */
    private ActionForward saveOrUpdateImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back SessionMap
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);

	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	ImageGalleryItemForm itemForm = (ImageGalleryItemForm) form;
	ActionErrors errors = validateImageGalleryItem(itemForm);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return mapping.findForward("file");
	}

	// create a new ImageGalleryItem
	ImageGalleryItem item = new ImageGalleryItem();
	IImageGalleryService service = getImageGalleryService();
	ImageGalleryUser imageGalleryUser = getCurrentUser(service, sessionId);
	item.setTitle(itemForm.getTitle());
	item.setDescription(itemForm.getDescription());
	item.setCreateDate(new Timestamp(new Date().getTime()));
	item.setCreateByAuthor(false);
	item.setCreateBy(imageGalleryUser);

	// special attribute for URL or FILE
	try {
	    service.uploadImageGalleryItemFile(item, itemForm.getFile());
	} catch (UploadImageGalleryFileException e) {
	    LearningAction.log.error("Failed upload ImageGallery File " + e.toString());
	    return mapping.findForward(ImageGalleryConstants.ERROR);
	}
	// save and update session

	ImageGallerySession resSession = service.getImageGallerySessionBySessionId(sessionId);
	if (resSession == null) {
	    LearningAction.log.error("Failed update ImageGallerySession by ID[" + sessionId + "]");
	    return mapping.findForward(ImageGalleryConstants.ERROR);
	}
	Set<ImageGalleryItem> items = resSession.getImageGalleryItems();
	if (items == null) {
	    items = new HashSet<ImageGalleryItem>();
	    resSession.setImageGalleryItems(items);
	}
	items.add(item);
	service.saveOrUpdateImageGallerySession(resSession);

	// update session value
	SortedSet<ImageGalleryItem> imageGalleryItemList = getImageList(sessionMap);
	imageGalleryItemList.add(item);

	// URL or file upload
	request.setAttribute(AttributeNames.ATTR_MODE, mode);

	//	ImageGallery imageGallery = resSession.getImageGallery();
	//	if (imageGallery.isAllowRank()
	//		&& service.getEventNotificationService().eventExists(ImageGalleryConstants.TOOL_SIGNATURE,
	//			ImageGalleryConstants.EVENT_NAME_NOTIFY_TEACHERS_ON_ASSIGMENT_SUBMIT,
	//			imageGallery.getContentId())) {
	//	    String fullName = imageGalleryUser.getLastName() + " " + imageGalleryUser.getFirstName();
	//	    service.getEventNotificationService().trigger(ImageGalleryConstants.TOOL_SIGNATURE,
	//		    ImageGalleryConstants.EVENT_NAME_NOTIFY_TEACHERS_ON_ASSIGMENT_SUBMIT, imageGallery.getContentId(),
	//		    new Object[] { fullName });
	//	}
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
    private ActionForward showComments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	ImageGalleryItem image = getImageGalleryService().getImageGalleryItemByUid(imageUid);
	TreeSet<ImageComment> comments = new TreeSet<ImageComment>(new ImageCommentComparator());
	comments.addAll(image.getComments());
	sessionMap.put(ImageGalleryConstants.PARAM_COMMENTS, comments);
	sessionMap.put(ImageGalleryConstants.PARAM_CURRENT_IMAGE, image);
	
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
    private ActionForward addNewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	

	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);

	String commentMessage = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_COMMENT);
	if(commentMessage == null || StringUtils.isBlank(commentMessage))
		return mapping.findForward(ImageGalleryConstants.SUCCESS);
	
	//TODO fix error system
//	ActionErrors errors = validateImageGalleryItem(itemForm);
//	if (!errors.isEmpty()) {
//	    ActionErrors errors = new ActionErrors();
//	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_FILE_BLANK));
//	    
//	    this.addErrors(request, errors);
//	    return mapping.findForward("file");
//	}
	
	
	ImageComment comment = new ImageComment();
	comment.setComment(commentMessage);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	IImageGalleryService service = getImageGalleryService();
	ImageGalleryUser imageGalleryUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),sessionId);
	comment.setCreateBy(imageGalleryUser);
	comment.setCreateDate(new Timestamp(new Date().getTime()));
	
	//persist ImageGallery changes in DB 
	Long currentImageUid =  new Long(request.getParameter(ImageGalleryConstants.ATTR_CURRENT_IMAGE_UID));
	ImageGalleryItem dbItem = service.getImageGalleryItemByUid(currentImageUid);
	Set<ImageComment> dbComments = dbItem.getComments();
	dbComments.add(comment);
	service.saveOrUpdateImageGalleryItem(dbItem);
			
//	//to make available new changes be visible in jsp page
//	sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM, dbItem);
	
	TreeSet<ImageComment> comments = new TreeSet<ImageComment>(new ImageCommentComparator());
	comments.addAll(dbItem.getComments());
	sessionMap.put(ImageGalleryConstants.PARAM_COMMENTS, comments);
	
//	form.reset(mapping, request);
	
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

	SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
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
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.RESOURCE_SERVICE);
    }

    /**
     * List save current imageGallery items.
     * 
     * @param request
     * @return
     */
    private SortedSet<ImageGalleryItem> getImageList(SessionMap sessionMap) {
	SortedSet<ImageGalleryItem> list = (SortedSet<ImageGalleryItem>) sessionMap
		.get(ImageGalleryConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<ImageGalleryItem>(new ImageGalleryItemComparator());
	    sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
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
	    service.createUser(imageGalleryUser);
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
     * @param itemForm
     * @return
     */
    private ActionErrors validateImageGalleryItem(ImageGalleryItemForm itemForm) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(itemForm.getTitle())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_TITLE_BLANK));
	}

	if ((itemForm.getFile() != null) && FileUtil.isExecutableFile(itemForm.getFile().getFileName())) {
	    ActionMessage msg = new ActionMessage("error.attachment.executable");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
	}

	// validate item size
	FileValidatorUtil.validateFileSize(itemForm.getFile(), false, errors);

	// for edit validate: file already exist
	if (!itemForm.isHasFile()
		&& ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_FILE_BLANK));
	}
	return errors;
    }

    /**
     * Set complete flag for given imageGallery item.
     * 
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request) {
	// get back sessionMap
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long imageGalleryItemUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	IImageGalleryService service = getImageGalleryService();
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);
	service.setItemComplete(imageGalleryItemUid, new Long(user.getUserID().intValue()), sessionId);

	// set imageGallery item complete tag
	SortedSet<ImageGalleryItem> imageGalleryItemList = getImageList(sessionMap);
	for (ImageGalleryItem item : imageGalleryItemList) {
	    if (item.getUid().equals(imageGalleryItemUid)) {
		item.setComplete(true);
		break;
	    }
	}
    }

}
