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

package org.lamsfoundation.lams.tool.rsrc.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.service.IResourceService;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewItemController {
    @Autowired
    private IResourceService resourceService;

    private static final Set<String> DISPLAYABLE_IMAGE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".bmp",
	    ".webp", ".svg");
    private static final Set<String> DISPLAYABLE_MEDIA_EXTENSIONS = Set.of(".mov", ".mp4", ".webm", ".ogg", ".mp3",
	    ".wav");
    private static final Set<String> DISPLAYABLE_EMBED_EXTENSIONS = Set.of(".pdf");

    /**
     * Display main frame to display item content in monitoring
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/reviewItem")
    public String reviewItem(HttpServletRequest request) throws UnsupportedEncodingException {
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID, true);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	if (mode.equals(ToolAccessMode.TEACHER.toString())) {
	    sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
	} else {
	    sessionMap.put(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR);
	}

	ResourceItem item = getResourceItem(request, sessionMap, mode);

	if (item == null) {
	    return "error";
	}

	Long itemUid = NumberUtils.createLong(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	request.setAttribute(ResourceConstants.PARAM_RESOURCE_ITEM_UID, itemUid);
	Integer itemIdx = WebUtil.readIntParam(request, ResourceConstants.PARAM_ITEM_INDEX, true);
	request.setAttribute(ResourceConstants.PARAM_ITEM_INDEX, itemIdx);
	String idStr = request.getParameter(ResourceConstants.ATTR_TOOL_SESSION_ID);
	Long sessionId = NumberUtils.createLong(idStr);
	request.setAttribute(ResourceConstants.ATTR_TOOL_SESSION_ID, sessionId);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	request.setAttribute(ResourceConstants.ATTR_TITLE, item.getTitle());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);

	return "pages/itemreview/mainframe";
    }

    /**
     * Display single item content
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/itemReviewContent")
    public String getItemReviewContent(HttpServletRequest request) throws UnsupportedEncodingException {

	SessionMap<String, Object> sessionMap = null;
	String sessionMapID = WebUtil.readStrParam(request, ResourceConstants.ATTR_SESSION_MAP_ID, true);
	if (sessionMapID == null) {
	    sessionMap = new SessionMap<>();
	    sessionMapID = sessionMap.getSessionID();
	    request.getSession().setAttribute(sessionMapID, sessionMap);
	} else {
	    sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	}

	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.isBlank(mode)) {
	    ToolAccessMode toolAccessMode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	    mode = toolAccessMode.toString();
	}
	ResourceItem item = getResourceItem(request, sessionMap, mode);

	String sessionIdString = request.getParameter(ResourceConstants.ATTR_TOOL_SESSION_ID);
	Long sessionId = null;
	// mark this item access flag if it is learner
	if (ToolAccessMode.LEARNER.toString().equals(mode)) {
	    sessionId = NumberUtils.createLong(sessionIdString);
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    resourceService.setItemAccess(item.getUid(), user.getUserID().longValue(), sessionId);
	}

	if (item == null) {
	    return "error";
	}
	boolean isDownload = item.getType() == ResourceConstants.RESOURCE_TYPE_FILE;
	request.setAttribute(ResourceConstants.ATTR_IS_DOWNLOAD, isDownload);
	String lowercaseFileName = isDownload && StringUtils.isNotBlank(item.getFileName())
		? item.getFileName().toLowerCase()
		: null;
	boolean isDisplayableImage = lowercaseFileName != null
		&& DISPLAYABLE_IMAGE_EXTENSIONS.stream().anyMatch(ext -> lowercaseFileName.endsWith(ext));
	request.setAttribute(ResourceConstants.ATTR_IS_DISPLAYABLE_IMAGE, isDisplayableImage);

	boolean isDisplayableMedia = lowercaseFileName != null
		&& DISPLAYABLE_MEDIA_EXTENSIONS.stream().anyMatch(ext -> lowercaseFileName.endsWith(ext));
	request.setAttribute(ResourceConstants.ATTR_IS_DISPLAYABLE_MEDIA, isDisplayableMedia);

	boolean isDisplayableEmbed = lowercaseFileName != null
		&& DISPLAYABLE_EMBED_EXTENSIONS.stream().anyMatch(ext -> lowercaseFileName.endsWith(ext));
	request.setAttribute(ResourceConstants.ATTR_IS_DISPLAYABLE_EMBED, isDisplayableEmbed);

	request.setAttribute(ResourceConstants.ATTR_IS_DISPLAYABLE_PAGE,
		item.getType() == ResourceConstants.RESOURCE_TYPE_WEBSITE);

	String reviewUrl = getReviewUrl(item, sessionMapID);
	request.setAttribute(ResourceConstants.ATTR_RESOURCE_REVIEW_URL, reviewUrl);

	request.setAttribute(ResourceConstants.ATTR_RESOURCE_INSTRUCTION, item.getInstructions());
	request.setAttribute(ResourceConstants.ATTR_ALLOW_COMMENTS, item.isAllowComments());
	request.setAttribute(ResourceConstants.ATTR_ALLOW_RATING, item.isAllowRating());
	request.setAttribute("ratingDTO", item.getRatingDTO());

	// these attribute will be use to instruction navigator page
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	Long itemUid = WebUtil.readLongParam(request, ResourceConstants.PARAM_RESOURCE_ITEM_UID, true);
	if (itemUid == null) {
	    itemUid = 0L;
	}
	request.setAttribute(ResourceConstants.PARAM_RESOURCE_ITEM_UID, itemUid);
	request.setAttribute(ResourceConstants.ATTR_TOOL_SESSION_ID, sessionIdString);
	request.setAttribute(ResourceConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return "pages/itemreview/itemContent";
    }

    @RequestMapping("/completeItem")
    public void completeItem(@RequestParam String mode, @RequestParam String sessionMapID, @RequestParam Long itemUid,
	    HttpSession session) {
	SessionMap sessionMap = (SessionMap) session.getAttribute(sessionMapID);

	HttpSession ss = SessionManager.getSession();

	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(ResourceConstants.ATTR_TOOL_SESSION_ID);
	resourceService.setItemComplete(itemUid, user.getUserID().longValue(), sessionId);

	// set resource item complete tag
	Set<ResourceItem> resourceItemList = getResourceItemList(sessionMap);
	for (ResourceItem item : resourceItemList) {
	    if (item.getUid().equals(itemUid)) {
		item.setComplete(true);
		break;
	    }
	}

    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    /**
     * Return resource item according to ToolAccessMode.
     */
    private ResourceItem getResourceItem(HttpServletRequest request, SessionMap<String, Object> sessionMap,
	    String mode) {
	ResourceItem item = null;
	if (ResourceConstants.MODE_AUTHOR_SESSION.equals(mode)) {
	    Integer itemIdx = WebUtil.readIntParam(request, ResourceConstants.PARAM_ITEM_INDEX, true);
	    if (itemIdx == null) {
		itemIdx = 0;
	    }
	    // authoring: does not save item yet, so only has ItemList from session and identity by Index
	    List<ResourceItem> resourceList = new ArrayList<>(getResourceItemList(sessionMap));
	    item = resourceList.get(itemIdx);
	} else if (ToolAccessMode.TEACHER.toString().equals(mode)) {
	    Long itemUid = NumberUtils.createLong(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	    // get back the resource and item list and display them on page
	    item = resourceService.getResourceItemByUid(itemUid);
	} else {
	    Long itemUid = NumberUtils.createLong(request.getParameter(ResourceConstants.PARAM_RESOURCE_ITEM_UID));
	    Set<ResourceItem> resourceItems = getResourceItemList(sessionMap);
	    for (ResourceItem resourceItem : resourceItems) {
		if (resourceItem.getUid().equals(itemUid)) {
		    item = resourceItem;
		    break;
		}
	    }
	}
	return item;
    }

    private static Pattern protocolExists = Pattern.compile("http://|https://|ftp://|nntp://",
	    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final String DEFAULT_PROTOCOL = "http://";
    public static final String HTTPS_SCHEME = "https";

    private String getReviewUrl(ResourceItem item, String sessionMapID) {
	String url = null;
	switch (item.getType()) {
	    case ResourceConstants.RESOURCE_TYPE_URL:
		// protocol missing? Assume http. Must do before the popup checks otherwise LDEV-4503 case may be missed.
		url = item.getUrl();
		if (!protocolExists.matcher(url).find()) {
		    url = DEFAULT_PROTOCOL + url;
		}
		break;
	    case ResourceConstants.RESOURCE_TYPE_FILE:
	    case ResourceConstants.RESOURCE_TYPE_WEBSITE:
		url = "/download/?uuid=" + item.getFileDisplayUuid();
		break;
	}
	return url;
    }

    /**
     * List current resource items.
     */
    private Set<ResourceItem> getResourceItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<ResourceItem> list = (SortedSet) sessionMap.get(ResourceConstants.ATTR_RESOURCE_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new ResourceItemComparator());
	    sessionMap.put(ResourceConstants.ATTR_RESOURCE_ITEM_LIST, list);
	}
	return list;
    }
}
