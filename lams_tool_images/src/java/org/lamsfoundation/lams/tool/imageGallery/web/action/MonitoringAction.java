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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.dto.UserImageContributionDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.util.WebUtil;
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
	if (param.equals("toggleImageVisibility")) {
	    return toggleImageVisibility(mapping, form, request, response);
	}
	if (param.equals("viewReflection")) {
	    return viewReflection(mapping, form, request, response);
	}

	return mapping.findForward(ImageGalleryConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IImageGalleryService service = getImageGalleryService();
	List<List<Summary>> groupList = service.getSummary(contentId);

	ImageGallery imageGallery = service.getImageGalleryByContentId(contentId);

	Map<Long, Set<ReflectDTO>> reflectList = service.getReflectList(contentId, false);
	boolean isGroupedActivity = service.isGroupedActivity(contentId);

	// cache into sessionMap
	sessionMap.put(ImageGalleryConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put(ImageGalleryConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(ImageGalleryConstants.PAGE_EDITABLE, imageGallery.isContentInUse());
	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY, imageGallery);
	sessionMap.put(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(ImageGalleryConstants.ATTR_REFLECT_LIST, reflectList);
	sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
	//rating stuff
	boolean isCommentsEnabled = service.isCommentsEnabled(contentId);
	sessionMap.put(ImageGalleryConstants.ATTR_IS_COMMENTS_ENABLED, isCommentsEnabled);

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
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	ImageGallery imageGallery = (ImageGallery) sessionMap.get(ImageGalleryConstants.ATTR_IMAGE_GALLERY);
	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	ImageGalleryItem image = getImageGalleryService().getImageGalleryItemByUid(imageUid);
	Long toolSessionId = WebUtil.readLongParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	
	if (imageGallery.isAllowVote()) {
	    List<List<UserImageContributionDTO>> imageSummary = getImageGalleryService().getImageSummary(contentId,
		    imageUid);
	    request.setAttribute(ImageGalleryConstants.ATTR_IMAGE_SUMMARY, imageSummary);

	} else if (imageGallery.isAllowRank()) {
	    ItemRatingDTO itemRatingDto = getImageGalleryService().getRatingCriteriaDtos(contentId, toolSessionId, imageUid, -1L);
	    request.setAttribute("itemRatingDto", itemRatingDto);
	}

	request.setAttribute(ImageGalleryConstants.ATTR_IMAGE, image);

	ImageGalleryItemForm imageForm = (ImageGalleryItemForm) form;
	imageForm.setImageUid(image.getUid().toString());
	imageForm.setTitle(image.getTitle());
	imageForm.setDescription(image.getDescription());

	return mapping.findForward("success");
    }

    /**
     * Update image's title and description set by monitor
     */
    private ActionForward updateImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IImageGalleryService service = getImageGalleryService();
	ImageGalleryItemForm imageForm = (ImageGalleryItemForm) form;
	
	// get back sessionMAP
	String sessionMapID = imageForm.getSessionMapID();
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(imageForm.getSessionMapID());
	Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	ImageGallery imageGallery = service.getImageGalleryByContentId(contentId);

	int imageUid = NumberUtils.stringToInt(imageForm.getImageUid(), -1);
	ImageGalleryItem image = service.getImageGalleryItemByUid(new Long(imageUid));

	String title = imageForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextImageTitleNumber = imageGallery.getNextImageTitle();
	    imageGallery.setNextImageTitle(nextImageTitleNumber + 1);
	    service.saveOrUpdateImageGallery(imageGallery);
	    
	    title = getImageGalleryService().generateNextImageTitle(nextImageTitleNumber);
	}
	image.setTitle(title);

	image.setDescription(imageForm.getDescription());
	image.setHide(false);
	service.saveOrUpdateImageGalleryItem(image);

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(ImageGalleryConstants.SUCCESS));
    	redirect.addParameter(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID, contentId);
    	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
    	redirect.addParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
    	return redirect;
    }

    /**
     * Toggle image visibility, i.e. set its hide field to the opposite of the current value
     */
    private ActionForward toggleImageVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long itemUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	IImageGalleryService service = getImageGalleryService();
	service.toggleImageVisibility(itemUid);

	return null;
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
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.IMAGE_GALLERY_SERVICE);
    }

}
