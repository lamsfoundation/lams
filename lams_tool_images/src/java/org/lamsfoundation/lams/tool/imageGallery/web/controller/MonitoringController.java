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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.dto.UserImageContributionDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    public static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    @Qualifier("laimagImageGalleryService")
    private IImageGalleryService igService;

    @Autowired
    @Qualifier("laimagMessageService")
    private MessageService messageService;

    @RequestMapping("/summary")
    public String summary(HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<List<Summary>> groupList = igService.getSummary(contentId);

	ImageGallery imageGallery = igService.getImageGalleryByContentId(contentId);
	boolean isGroupedActivity = igService.isGroupedActivity(contentId);

	// cache into sessionMap
	sessionMap.put(ImageGalleryConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put(ImageGalleryConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(ImageGalleryConstants.PAGE_EDITABLE, imageGallery.isContentInUse());
	sessionMap.put(ImageGalleryConstants.ATTR_IMAGE_GALLERY, imageGallery);
	sessionMap.put(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
	//rating stuff
	boolean isCommentsEnabled = igService.isCommentsEnabled(contentId);
	sessionMap.put(ImageGalleryConstants.ATTR_IS_COMMENTS_ENABLED, isCommentsEnabled);

	return "pages/monitoring/monitoring";
    }

    /**
     * Display edit page for existed imageGallery item.
     */
    @RequestMapping("/imageSummary")
    public String imageSummary(@ModelAttribute ImageGalleryItemForm imageGalleryItemForm, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	ImageGallery imageGallery = (ImageGallery) sessionMap.get(ImageGalleryConstants.ATTR_IMAGE_GALLERY);
	Long imageUid = new Long(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_UID));
	ImageGalleryItem image = igService.getImageGalleryItemByUid(imageUid);
	Long toolSessionId = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_TOOL_SESSION_ID);

	if (imageGallery.isAllowVote()) {
	    List<List<UserImageContributionDTO>> imageSummary = igService.getImageSummary(contentId, imageUid);
	    request.setAttribute(ImageGalleryConstants.ATTR_IMAGE_SUMMARY, imageSummary);

	} else if (imageGallery.isAllowRank()) {
	    ItemRatingDTO itemRatingDto = igService.getRatingCriteriaDtos(contentId, toolSessionId, imageUid, -1L);
	    request.setAttribute("itemRatingDto", itemRatingDto);
	}

	igService.fillImageDisplayUuid(image);
	request.setAttribute(ImageGalleryConstants.ATTR_IMAGE, image);

	imageGalleryItemForm.setImageUid(image.getUid().toString());
	imageGalleryItemForm.setTitle(image.getTitle());
	imageGalleryItemForm.setDescription(image.getDescription());

	return "pages/monitoring/imagesummary";
    }

    /**
     * Update image's title and description set by monitor
     */
    @RequestMapping(path = "/updateImage", method = RequestMethod.POST)
    public String updateImage(@ModelAttribute ImageGalleryItemForm imageGalleryItemForm, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = imageGalleryItemForm.getSessionMapID();
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(imageGalleryItemForm.getSessionMapID());
	Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	ImageGallery imageGallery = igService.getImageGalleryByContentId(contentId);

	int imageUid = NumberUtils.stringToInt(imageGalleryItemForm.getImageUid(), -1);
	ImageGalleryItem image = igService.getImageGalleryItemByUid(new Long(imageUid));

	String title = imageGalleryItemForm.getTitle();
	if (StringUtils.isBlank(title)) {
	    Long nextImageTitleNumber = imageGallery.getNextImageTitle();
	    imageGallery.setNextImageTitle(nextImageTitleNumber + 1);
	    igService.saveOrUpdateImageGallery(imageGallery);

	    title = igService.generateNextImageTitle(nextImageTitleNumber);
	}
	image.setTitle(title);

	image.setDescription(imageGalleryItemForm.getDescription());
	image.setHide(false);
	igService.saveOrUpdateImageGalleryItem(image);

	return null;
    }

    /**
     * Toggle image visibility, i.e. set its hide field to the opposite of the current value
     */
    @RequestMapping(path = "/toggleImageVisibility", method = RequestMethod.POST)
    public String toggleImageVisibility(HttpServletRequest request) {

	Long itemUid = WebUtil.readLongParam(request, ImageGalleryConstants.PARAM_IMAGE_UID);
	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long contentId = (Long) sessionMap.get(ImageGalleryConstants.ATTR_TOOL_CONTENT_ID);
	igService.toggleImageVisibility(itemUid, contentId);

	return null;
    }

}
