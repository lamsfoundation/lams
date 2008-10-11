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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dto.InstructionNavDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.service.IImageGalleryService;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryItemComparator;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryWebUtils;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ViewItemAction extends Action {

    private static final Logger log = Logger.getLogger(ViewItemAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	// -----------------------Display Learning Object function ---------------------------
	if (param.equals("reviewItem")) {
	    return reviewItem(mapping, form, request, response);
	}
	// for preview top frame html page use:
	if (param.equals("nextInstruction")) {
	    return nextInstruction(mapping, form, request, response);
	}
	// for preview top frame html page use:
	if (param.equals("openUrlPopup")) {
	    return openUrlPopup(mapping, form, request, response);
	}

	return mapping.findForward(ImageGalleryConstants.ERROR);
    }

    /**
     * Open url in popup window page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward openUrlPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String url = request.getParameter(ImageGalleryConstants.PARAM_OPEN_URL_POPUP);
	String title = request.getParameter(ImageGalleryConstants.PARAM_TITLE);
	request.setAttribute(ImageGalleryConstants.PARAM_OPEN_URL_POPUP, url);
	request.setAttribute(ImageGalleryConstants.PARAM_TITLE, title);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Return next instrucion to page. It need four input parameters, mode, imageIndex or itemUid, and insIdx.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward nextInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sesionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	ImageGalleryItem item = getImageGalleryItem(request, sesionMap, mode);
	if (item == null) {
	    return mapping.findForward(ImageGalleryConstants.ERROR);
	}

	int currIns = NumberUtils.stringToInt(request
		.getParameter(ImageGalleryConstants.PARAM_CURRENT_INSTRUCTION_INDEX), 0);

//	Set instructions = item.getItemInstructions();
//	InstructionNavDTO navDto = new InstructionNavDTO();
//	// For Learner upload item, its instruction will display description/comment fields in ReosourceItem.
//	if (!item.isCreateByAuthor()) {
//	    List<ImageGalleryItemInstruction> navItems = new ArrayList<ImageGalleryItemInstruction>(1);
//	    // create a new instruction and put ImageGalleryItem description into it: just for display use.
//	    ImageGalleryItemInstruction ins = new ImageGalleryItemInstruction();
//	    ins.setSequenceId(1);
//	    ins.setDescription(item.getDescription());
//	    navItems.add(ins);
//	    navDto.setAllInstructions(navItems);
//	    instructions.add(ins);
//	} else {
//	    navDto.setAllInstructions(new ArrayList(instructions));
//	}
//	navDto.setTitle(item.getTitle());
//	navDto.setTotal(instructions.size());
//	if (instructions.size() > 0) {
//	    navDto.setInstruction((ImageGalleryItemInstruction) new ArrayList(instructions).get(currIns));
//	    navDto.setCurrent(currIns + 1);
//	} else {
//	    navDto.setCurrent(0);
//	    navDto.setInstruction(null);
//	}

	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);
//	request.setAttribute(ImageGalleryConstants.ATTR_RESOURCE_INSTRUCTION, navDto);
	return mapping.findForward(ImageGalleryConstants.SUCCESS);
    }

    /**
     * Display main frame to display instrcution and item content.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);

	String sessionMapID = WebUtil.readStrParam(request, ImageGalleryConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	ImageGalleryItem item = getImageGalleryItem(request, sessionMap, mode);

	String idStr = request.getParameter(ImageGalleryConstants.ATTR_TOOL_SESSION_ID);
	Long sessionId = NumberUtils.createLong(idStr);
	// mark this item access flag if it is learner
	if (ToolAccessMode.LEARNER.toString().equals(mode)) {
	    IImageGalleryService service = getImageGalleryService();
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    service.setItemAccess(item.getUid(), new Long(user.getUserID().intValue()), sessionId);
	}

	if (item == null) {
	    return mapping.findForward(ImageGalleryConstants.ERROR);
	}
	// set url to content frame
	request.setAttribute(ImageGalleryConstants.ATTR_RESOURCE_REVIEW_URL, getReviewUrl(item, sessionMapID));

	// these attribute will be use to instruction navigator page
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX));
	request.setAttribute(ImageGalleryConstants.PARAM_IMAGE_INDEX, itemIdx);
	Long itemUid = NumberUtils.createLong(request.getParameter(ImageGalleryConstants.PARAM_RESOURCE_ITEM_UID));
	request.setAttribute(ImageGalleryConstants.PARAM_RESOURCE_ITEM_UID, itemUid);
	request.setAttribute(ImageGalleryConstants.ATTR_TOOL_SESSION_ID, sessionId);
	request.setAttribute(ImageGalleryConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return mapping.findForward(ImageGalleryConstants.SUCCESS);

    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return resoruce item according to ToolAccessMode.
     * 
     * @param request
     * @param sessionMap
     * @param mode
     * @return
     */
    private ImageGalleryItem getImageGalleryItem(HttpServletRequest request, SessionMap sessionMap, String mode) {
	ImageGalleryItem item = null;
	if (ImageGalleryConstants.MODE_AUTHOR_SESSION.equals(mode)) {
	    int itemIdx = NumberUtils.stringToInt(request.getParameter(ImageGalleryConstants.PARAM_IMAGE_INDEX), 0);
	    // authoring: does not save item yet, so only has ItemList from session and identity by Index
	    List<ImageGalleryItem> imageGalleryList = new ArrayList<ImageGalleryItem>(
		    getImageGalleryItemList(sessionMap));
	    item = imageGalleryList.get(itemIdx);
	} else {
	    Long itemUid = NumberUtils.createLong(request.getParameter(ImageGalleryConstants.PARAM_RESOURCE_ITEM_UID));
	    // get back the imageGallery and item list and display them on page
	    IImageGalleryService service = getImageGalleryService();
	    item = service.getImageGalleryItemByUid(itemUid);
	}
	return item;
    }

    private IImageGalleryService getImageGalleryService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IImageGalleryService) wac.getBean(ImageGalleryConstants.RESOURCE_SERVICE);
    }

    private static Pattern wikipediaPattern = Pattern.compile("wikipedia", Pattern.CASE_INSENSITIVE
	    | Pattern.UNICODE_CASE);

    private Object getReviewUrl(ImageGalleryItem item, String sessionMapID) {
//	short type = item.getType();
	String url = null;
//	switch (type) {
//	case ImageGalleryConstants.RESOURCE_TYPE_URL:
//	    // See LDEV-1736 regarding wikipedia regex
//	    Matcher matcher = ViewItemAction.wikipediaPattern.matcher(item.getUrl());
//	    boolean wikipediaInURL = matcher.find();
//
//	    if (item.isOpenUrlNewWindow() || wikipediaInURL) {
//		try {
//		    url = "/openUrlPopup.do?popupUrl="
//			    + URLEncoder.encode(ImageGalleryWebUtils.protocol(item.getUrl()), "UTF8") + "&title="
//			    + URLEncoder.encode(item.getTitle(), "UTF8");
//		} catch (UnsupportedEncodingException e) {
//		    ViewItemAction.log.error(e);
//		}
//	    } else {
//		url = ImageGalleryWebUtils.protocol(item.getUrl());
//	    }
//	    break;
//	case ImageGalleryConstants.RESOURCE_TYPE_FILE:
//	    url = "/download/?uuid=" + item.getFileUuid() + "&preferDownload=false";
//	    break;
//	case ImageGalleryConstants.RESOURCE_TYPE_WEBSITE:
//	    url = "/download/?uuid=" + item.getFileUuid() + "&preferDownload=false";
//	    break;
//	case ImageGalleryConstants.RESOURCE_TYPE_LEARNING_OBJECT:
//	    url = "/pages/learningobj/mainframe.jsp?sessionMapID=" + sessionMapID;
//	    break;
//	}
	return url;
    }

    /**
     * List save current imageGallery items.
     * 
     * @param request
     * @return
     */
    private SortedSet<ImageGalleryItem> getImageGalleryItemList(SessionMap sessionMap) {
	SortedSet<ImageGalleryItem> list = (SortedSet) sessionMap.get(ImageGalleryConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<ImageGalleryItem>(new ImageGalleryItemComparator());
	    sessionMap.put(ImageGalleryConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }

}
