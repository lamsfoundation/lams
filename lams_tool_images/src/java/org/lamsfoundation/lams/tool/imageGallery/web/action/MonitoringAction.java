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
import java.util.List;
import java.util.Map;
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
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.dto.UserImageContributionDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageComment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.service.ImageGalleryApplicationException;
import org.lamsfoundation.lams.tool.imageGallery.service.UploadImageGalleryFileException;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageCommentComparator;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageCommentForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String param = mapping.getParameter();

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("imageSummary")) {
	    return imageSummary(mapping, form, request, response);
	}
	if (param.equals("updateImage")) {
	    return updateImage(mapping, form, request, response);
	}		
	if (param.equals("showitem")) {
	    return showitem(mapping, form, request, response);
	}
	if (param.equals("hideitem")) {
	    return hideitem(mapping, form, request, response);
	}
	if (param.equals("editComment")) {
	    return editComment(mapping, form, request, response);
	}
	if (param.equals("saveComment")) {
	    return saveComment(mapping, form, request, response);
	}
	if (param.equals("removeComment")) {
	    return removeComment(mapping, form, request, response);
	}	
	if (param.equals("viewReflection")) {
	    return viewReflection(mapping, form, request, response);
	}

	return mapping.findForward(ImageGalleryConstants.ERROR);
    }
    
    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
		AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IImageGalleryService service = getImageGalleryService();
	List<List<Summary>> groupList = service.getSummary(contentId);

	ImageGallery imageGallery = service.getImageGalleryByContentId(contentId);
	imageGallery.toDTO();

	Map<Long, Set<ReflectDTO>> reflectList = service.getReflectList(contentId, false);

	// cache into sessionMap
	sessionMap.put(ImageGalleryConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(ImageGalleryConstants.PAGE_EDITABLE, imageGallery.isContentInUse());
	sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE, imageGallery);
	sessionMap.put(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECT_LIST, reflectList);

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
    private ActionForward imageSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	ImageGallery imageGallery = (ImageGallery) sessionMap.get(ImageGalleryConstants.ATTR_RESOURCE);
	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	ImageGalleryItem image =getImageGalleryService().getImageGalleryItemByUid(imageUid);
	
	if (imageGallery.isAllowCommentImages() || imageGallery.isAllowRank() || imageGallery.isAllowVote()) {
	    List<List<UserImageContributionDTO>> imageSummary = getImageGalleryService().getImageSummary(contentId, imageUid);
	    request.setAttribute(ImageGalleryConstants.ATTR_IMAGE_SUMMARY, imageSummary);
	}
	request.setAttribute(ImageGalleryConstants.ATTR_IMAGE, image);
	sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE_ITEM_UID, imageUid);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());	
	
	ImageGalleryItemForm imageForm = (ImageGalleryItemForm) form;
	imageForm.setImageUid(image.getUid().toString());
	imageForm.setTitle(image.getTitle());
	imageForm.setDescription(image.getDescription());
	    
	return mapping.findForward("success");
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
    private ActionForward updateImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	// get back sessionMAP
	ImageGalleryItemForm imageForm = (ImageGalleryItemForm) form;
	String sessionMapID = imageForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap
		.get(AttributeNames.PARAM_CONTENT_FOLDER_ID));

	extractFormToImageGalleryItem(request, imageForm);

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    private ActionForward showitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long itemUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	IImageGalleryService service = getImageGalleryService();
	service.setItemVisible(itemUid, true);

	// get back SessionMap
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// update session value
	List<List> groupList = (List<List>) sessionMap.get(ImageGalleryConstants.ATTR_SUMMARY_LIST);
	if (groupList != null) {
	    for (List<Summary> group : groupList) {
		for (Summary sum : group) {
		    if (itemUid.equals(sum.getItemUid())) {
			sum.setItemHide(false);
			break;
		    }
		}
	    }
	}
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }
    
    private ActionForward hideitem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long itemUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	IImageGalleryService service = getImageGalleryService();
	service.setItemVisible(itemUid, false);

	// get back SessionMap
	String sessionMapID = request.getParameter(ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// update session value
	List<List> groupList = (List<List>) sessionMap.get(ImageGalleryConstants.ATTR_SUMMARY_LIST);
	if (groupList != null) {
	    for (List<Summary> group : groupList) {
		for (Summary sum : group) {
		    if (itemUid.equals(sum.getItemUid())) {
			sum.setItemHide(true);
			break;
		    }
		}
	    }
	}

	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }
    
    /**
     * Edit existing comment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	IImageGalleryService service = getImageGalleryService();
	
	Long commentUid = new Long(request.getParameter(ImageGalleryConstants.ATTR_COMMENT_UID));
	ImageComment comment = service.getImageCommentByUid(commentUid);
	ImageCommentForm commentForm = (ImageCommentForm) form;
	commentForm.setSessionMapID(sessionMapID);
	commentForm.setCommentUid(commentUid.toString());
	commentForm.setComment(comment.getComment());
	commentForm.setCreateBy(comment.getCreateBy().getLoginName());
	commentForm.setCreateDate(comment.getCreateDate().toString());
	
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward("success");
    }
    
    /**
     * Save edited comment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward saveComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IImageGalleryService service = getImageGalleryService();
	ImageCommentForm commentForm = (ImageCommentForm) form;
	String sessionMapID = commentForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	
	String commentMessage = commentForm.getComment();
	if (StringUtils.isBlank(commentMessage)) {
	    ActionErrors errors = new ActionErrors();
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_COMMENT_BLANK));
	    this.addErrors(request, errors);
	    return mapping.findForward("comment");
	}

	Long commentUid = NumberUtils.createLong(commentForm.getCommentUid());
	ImageComment comment = service.getImageCommentByUid(commentUid);
	comment.setComment(commentMessage);
	service.saveImageComment(comment);
			
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }
    
    /**
     * Delete user comment.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	IImageGalleryService service = getImageGalleryService();
	
	Long commentUid = new Long(request.getParameter(ImageGalleryConstants.ATTR_COMMENT_UID));
	ImageComment comment = service.getImageCommentByUid(commentUid);
	
	Long imageUid = (Long) sessionMap.get(ImageGalleryConstants.ATTR_RESOURCE_ITEM_UID);
	ImageGalleryItem image = service.getImageGalleryItemByUid(imageUid);
	Set<ImageComment> dbComments = image.getComments();
	dbComments.remove(comment);
	service.saveOrUpdateImageGalleryItem(image);
	service.deleteImageComment(commentUid);
	
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long uid = WebUtil.readLongParam(request, ImageGalleryConstants.ATTR_USER_UID);
	Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	IImageGalleryService service = getImageGalleryService();
	ImageGalleryUser user = service.getUser(uid);
	NotebookEntry notebookEntry = service.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ImageGalleryConstants.TOOL_SIGNATURE, user.getUserId().intValue());

	ImageGallerySession session = service.getImageGallerySessionBySessionId(sessionID);

	ReflectDTO refDTO = new ReflectDTO(user);
	if (notebookEntry == null) {
	    refDTO.setFinishReflection(false);
	    refDTO.setReflect(null);
	} else {
	    refDTO.setFinishReflection(true);
	    refDTO.setReflect(notebookEntry.getEntry());
	}
	refDTO.setReflectInstrctions(session.getImageGallery().getReflectInstructions());

	request.setAttribute("userDTO", refDTO);
	return mapping.findForward("success");
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IImageGalleryService getImageGalleryService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.RESOURCE_SERVICE);
    }
    
    /**
     * Extract web form content to imageGallery item.
     * 
     * @param request
     * @param imageForm
     * @throws ImageGalleryApplicationException
     */
    private void extractFormToImageGalleryItem(HttpServletRequest request, ImageGalleryItemForm imageForm){

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(imageForm.getSessionMapID());
	IImageGalleryService service = getImageGalleryService();

	Long imageUid = new Long(imageForm.getImageUid());
	ImageGalleryItem image = service.getImageGalleryItemByUid(imageUid);

	String title = imageForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	    ImageGallery imageGallery = service.getImageGalleryByContentId(contentId);
	    Long nextConsecutiveImageTitle = imageGallery.getNextImageTitle();
	    imageGallery.setNextImageTitle(nextConsecutiveImageTitle + 1);
	    service.saveOrUpdateImageGallery(imageGallery);
	    String imageLocalized = getImageGalleryService().getLocalisedMessage("label.authoring.image", null);
	    title = imageLocalized + " " + nextConsecutiveImageTitle;
	}
	image.setTitle(title);

	image.setDescription(imageForm.getDescription());
	service.saveOrUpdateImageGalleryItem(image);
    }

}
